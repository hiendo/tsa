package com.github.hiendo.tsa.chart;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.text.SimpleDateFormat;

/**
 * Options for a basic xy chart.  Options can be specified in a fluent-api-style.
 */
public class XyChartOptions extends ChartOptions {

    @QueryParam("connectPoints")
    @DefaultValue("true")
    private boolean connectPoints = true;

    public static XyChartOptions newOptions() {
        return new XyChartOptions();
    }

    public boolean isConnectPoints() {
        return connectPoints;
    }

}
