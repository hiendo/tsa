package com.github.hiendo.tsa.chart;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.util.concurrent.TimeUnit;

/**
 * Options for a box and whisker chart with stats aggregation in time increments.
 */
public class BoxAndWhisperStatAggregationInTimeChartOption extends ChartOptions {

    @QueryParam("timeIncrementInSec")
    @DefaultValue("3600000") // 1 hour
    private long timeIncrementInSec = TimeUnit.HOURS.toMillis(1);

    public long getTimeIncrementInSec() {
        return timeIncrementInSec;
    }
}
