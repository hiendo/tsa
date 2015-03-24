package com.github.hiendo.tsa.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Draws a box and whisker plot.
 */
public class BasicBoxAndWhiskerChart {
    final static Logger logger = LoggerFactory.getLogger(BasicXyLineChart.class);

    private ChartOptions chartOptions;

    public BasicBoxAndWhiskerChart(ChartOptions chartOptions) {
        this.chartOptions = chartOptions;
    }

    /**
     * Creates a basic box and whisker chart.
     *
     * @param dataset a dataset for the chart
     * @return Chart.
     */
    public JFreeChart createChart(BoxAndWhiskerCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(chartOptions.getTitle(), chartOptions.getxAxisLabel(),
                chartOptions.getyAxisLabel(), dataset, true);
        chart.setBackgroundPaint(Color.white);

        BoxAndWhiskerRenderer boxAndWhiskerRenderer = new BoxAndWhiskerRenderer();
        boxAndWhiskerRenderer.setMeanVisible(true);
        boxAndWhiskerRenderer.setMedianVisible(true);
        boxAndWhiskerRenderer.setMaximumBarWidth(.02);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        categoryPlot.setBackgroundPaint(Color.white);
        categoryPlot.setDomainGridlinePaint(Color.lightGray);
        categoryPlot.setRangeGridlinePaint(Color.lightGray);
        categoryPlot.setRenderer(boxAndWhiskerRenderer);
        categoryPlot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        categoryPlot.getDomainAxis().setMaximumCategoryLabelLines(2);
        return chart;
    }

    /**
     * Write the chart out to the specified output stream.
     *
     * @param outputStream output stream to write out to
     * @param dataset dataset to chart
     */
    public void writeChart(OutputStream outputStream, BoxAndWhiskerCategoryDataset dataset) {
        long startCreateChart = System.currentTimeMillis();

        JFreeChart jFreeChart = createChart(dataset);
        long writeChartStart = System.currentTimeMillis();

        logger.info("Chart creation generation took " + (writeChartStart - startCreateChart) / 1000.0);

        try {
            ChartUtilities.writeChartAsJPEG(outputStream, jFreeChart, chartOptions.getWidth(), chartOptions.getHeight());
        } catch (IOException e) {
            throw new RuntimeException("Unable to draw chart", e);
        }

        logger.info("Chart writing took " + (System.currentTimeMillis() - writeChartStart) / 1000.0);
    }
}
