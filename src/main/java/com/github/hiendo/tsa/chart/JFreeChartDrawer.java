package com.github.hiendo.tsa.chart;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Draws chart in windows frame.
 */
public class JFreeChartDrawer {
    final static Logger logger = LoggerFactory.getLogger(JFreeChartDrawer.class);

    public static void drawChart(JFreeChart chart) {
        ApplicationFrame applicationFrame = new ApplicationFrame("");
        ChartPanel chartPanel = new ChartPanel(chart);
        applicationFrame.setContentPane(chartPanel);
        applicationFrame.pack();
        RefineryUtilities.centerFrameOnScreen(applicationFrame);
        applicationFrame.setVisible(true);
    }
}
