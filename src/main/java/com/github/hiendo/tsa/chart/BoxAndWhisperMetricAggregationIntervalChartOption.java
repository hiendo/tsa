package com.github.hiendo.tsa.chart;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.util.concurrent.TimeUnit;

/**
 * Options for a box and whisker chart with stats aggregation in time increments.
 */
public class BoxAndWhisperMetricAggregationIntervalChartOption extends ChartOptions {

    @QueryParam("interval")
    @DefaultValue("3600000") // 1 hour
    private long interval = TimeUnit.HOURS.toMillis(1);

    public long getInterval() {
        return interval;
    }
}
