package com.github.hiendo.tsa.chart;

import com.github.hiendo.tsa.db.DataPointsEntity;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        if (chartOptions.isxAxisAsDate()) {
            DateAxis dateAxis = new DateAxis(chartOptions.getxAxisLabel());
            dateAxis.setDateFormatOverride(chartOptions.getDateFormat());
            //dateAxis.setTickUnit(new DateTickUnit(DateTickUnit.SECOND, 30));
            dateAxis.setVerticalTickLabels(true);
            plot.setDomainAxis(dateAxis);
        }

        return chart;
    }

    /**
     * Create a chart and write out data to stream.
     *
     * @param outputStream output stream to write chart to
     * @param dataPointsEntities set of DataPoints to graph
     */
    public void writeChart(OutputStream outputStream, DataPointsEntity... dataPointsEntities) {
        long startCreateChart = System.currentTimeMillis();

        List<XYSeries> xySeriesList = new ArrayList<>();
        for (DataPointsEntity dataPointsEntity : dataPointsEntities) {
            XYSeries xySeries = new XYSeries(dataPointsEntity.getTopic());
            xySeriesList.add(xySeries);

            if (chartOptions.isxAxisAsDate()) {
                // Convert time from seconds to milliseconds
                for (int i = 0; i < dataPointsEntity.size(); i++) {
                    xySeries.add(dataPointsEntity.getX(i) * 1000, dataPointsEntity.getY(i));
                }
            } else {
                for (int i = 0; i < dataPointsEntity.size(); i++) {
                    xySeries.add(dataPointsEntity.getX(i), dataPointsEntity.getY(i));
                }
            }

        }

        try {
            JFreeChart jFreeChart = createChart(xySeriesList.toArray(new XYSeries[0]));
            long writeChartStart = System.currentTimeMillis();

            logger.info("Chart creation generation took " + (writeChartStart - startCreateChart) / 1000.0);

            ChartUtilities.writeChartAsJPEG(outputStream, jFreeChart, chartOptions.getWidth(), chartOptions.getHeight());

            logger.info("Chart writing took " + (System.currentTimeMillis() - writeChartStart) / 1000.0);
        } catch (IOException e) {
            throw new RuntimeException("Error in writing chart to output stream", e);
        }
    }
}
