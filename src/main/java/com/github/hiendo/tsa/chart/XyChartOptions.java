package com.github.hiendo.tsa.chart;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.text.SimpleDateFormat;

/**
 * Options for a basic xy chart.  Options can be specified in a fluent-api-style.
 */
public class XyChartOptions {

    @QueryParam("title")
    @DefaultValue("Default Chart Title")
    private String title = "Default Chart Title";

    @QueryParam("xAxisLabel")
    @DefaultValue("Default X-Axis Title")
    private String xAxisLabel = "Default X-Axis Label";

    @QueryParam("yAxisLabel")
    @DefaultValue("Default Chart Title")
    private String yAxisLabel = "Default Y-Axis Label";

    @QueryParam("connectPoints")
    @DefaultValue("true")
    private boolean connectPoints = true;

    @QueryParam("xAxisAsDate")
    @DefaultValue("false")
    private boolean xAxisAsDate = true;

    @QueryParam("dateFormat")
    @DefaultValue("MM/dd-HH:mm")
    private  SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd-HH:mm");

    @QueryParam("height")
    @DefaultValue("600")
    private int height = 600;

    @QueryParam("width")
    @DefaultValue("400")
    private int width = 400;

    public static XyChartOptions newOptions() {
        return new XyChartOptions();
    }

    public boolean isConnectPoints() {
        return connectPoints;
    }

    public boolean isxAxisAsDate() {
        return xAxisAsDate;
    }

    public String getTitle() {
        return title;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
