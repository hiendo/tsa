package com.github.hiendo.tsa.chart;

import com.github.hiendo.tsa.db.DataPointsEntity;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Draws basic XY line chart.
 */
public class BasicXyLineChart {
    final static Logger logger = LoggerFactory.getLogger(BasicXyLineChart.class);

    private XyChartOptions chartOptions;

    public BasicXyLineChart(XyChartOptions chartOptions) {
        this.chartOptions = chartOptions;
    }

    /**
     * Create a single chart with line graphs, each represent by a series of point
     *
     * @param xySeries set of data sets to graph
     * @return a chart
     */
    public JFreeChart createChart(XYSeries... xySeries) {
        return createChart(DatasetConverter.convertXySeriesToXyDataset(xySeries));
    }

    /**
     * Create a single chart with line graphs
     *
     * @param dataset data sets to graph
     * @return a chart
     */
    public JFreeChart createChart(XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart(chartOptions.getTitle(),
                chartOptions.getxAxisLabel(),
                chartOptions.getyAxisLabel(),
                dataset,
                PlotOrientation.VERTICAL,
                true,           // include legend
                true,           // tooltips
                false           // urls
        );
        chart.setBackgroundPaint(Color.white);

        // Set the chart background
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        // Mark the data points
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer.setSeriesLinesVisible(i, chartOptions.isConnectPoints());
            renderer.setSeriesShapesVisible(i, true);
            renderer.setShape(new Rectangle(1, 1));
        }
        plot.setRenderer(renderer);

        // case where time unit is sec so we need to convert min/max time axis to ms
        Long smallestX = chartOptions.getStartX() == null ? null :  Math.round(chartOptions.getStartX());
        if (smallestX != null && chartOptions.isxAxisAsDate() && chartOptions.getTimeUnit().equals("sec")) {
            smallestX = smallestX * 1000;
        }

        Long largestX = chartOptions.getEndX() == null ? null :  Math.round(chartOptions.getEndX());
        if (largestX != null && chartOptions.isxAxisAsDate() && chartOptions.getTimeUnit().equals("sec")) {
            largestX = largestX * 1000;
        }

        smallestX = smallestX == null ? getSmallestXInDataset(dataset) : smallestX;
        largestX = largestX == null ? getLargestXInDataset(dataset) : largestX;

        // case where there are zero data.
        if (smallestX == Long.MAX_VALUE) {
            smallestX = 0l;
        }
        if (largestX == Long.MIN_VALUE) {
            largestX = 0l;
        }


        if (chartOptions.isxAxisAsDate()) {
            DateAxis dateAxis = new DateAxis(chartOptions.getxAxisLabel());
            dateAxis.setDateFormatOverride(chartOptions.getDateFormat());
            dateAxis.setVerticalTickLabels(true);

            dateAxis.setMinimumDate(new Date(smallestX));
            dateAxis.setMaximumDate(new Date(largestX));
            plot.setDomainAxis(dateAxis);
        } else {
            org.jfree.chart.axis.NumberAxis numberAxis = (org.jfree.chart.axis.NumberAxis) plot.getDomainAxis();
            numberAxis.setRange(new Range(smallestX, largestX));
        }

        return chart;
    }


    private long getSmallestXInDataset(XYDataset dataset) {
        long smallestX = Long.MAX_VALUE;
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            if (dataset.getItemCount(i) > 0) {
                smallestX = Math.min(Math.round((double) dataset.getX(i, 0)), smallestX);
            }
        }

        return smallestX;
    }

    private long getLargestXInDataset(XYDataset dataset) {
        long largestX = Long.MIN_VALUE;
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            if (dataset.getItemCount(i) > 0) {
                largestX = Math.max(Math.round((double) dataset.getX(i, dataset.getItemCount(i) - 1)), largestX);
            }
        }

        return largestX;
    }

    /**
     * Create a chart and write out data to stream.
     *
     * @param outputStream       output stream to write chart to
     * @param dataPointsEntities set of DataPoints to graph
     */
    public void writeChart(OutputStream outputStream, DataPointsEntity... dataPointsEntities) {
        long startCreateChart = System.currentTimeMillis();

        List<XYSeries> xySeriesList = new ArrayList<>();
        for (DataPointsEntity dataPointsEntity : dataPointsEntities) {
            XYSeries xySeries = new XYSeries(dataPointsEntity.getTopic());
            xySeriesList.add(xySeries);

            int multiplier = 1;
            if (chartOptions.isxAxisAsDate()) {
                // Convert time from seconds to milliseconds
                if (chartOptions.getTimeUnit().equals("sec")) {
                    multiplier = 1000;
                }
            }

            for (int i = 0; i < dataPointsEntity.size(); i++) {
                xySeries.add(dataPointsEntity.getX(i) * multiplier, dataPointsEntity.getY(i));
            }
        }

        try {
            JFreeChart jFreeChart = createChart(xySeriesList.toArray(new XYSeries[0]));
            long writeChartStart = System.currentTimeMillis();

            logger.info("Chart creation generation took " + (writeChartStart - startCreateChart) / 1000.0);

            ChartUtilities
                    .writeChartAsJPEG(outputStream, jFreeChart, chartOptions.getWidth(), chartOptions.getHeight());

            logger.info("Chart writing took " + (System.currentTimeMillis() - writeChartStart) / 1000.0);
        } catch (IOException e) {
            throw new RuntimeException("Error in writing chart to output stream", e);
        }
    }
}
