package com.github.hiendo.tsa.chart;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Convert one dataset format to another.
 */
public class DatasetConverter {

    /**
     * Convert the specified list of series to a dataset.
     *
     * @param seriesArray array of series to convert
     * @return Dataset converted from the array of series.
     */
    public static XYDataset convertXySeriesToXyDataset(XYSeries... seriesArray) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (XYSeries series : seriesArray) {
            dataset.addSeries(series);
        }

        return dataset;
    }
}
