package com.github.hiendo.tsa.chart;

import javax.ws.rs.QueryParam;
import java.util.concurrent.TimeUnit;

/**
 * Options for a box and whisker chart with stats aggregation in time increments.
 */
public class BoxAndWhisperMetricAggregationIntervalChartOption extends ChartOptions {

    @QueryParam("interval")
    private Double interval = Double.MAX_VALUE;

    public double getInterval() {
        if (interval == null) {
            return Double.MAX_VALUE;
        }
        return interval;
    }
}
