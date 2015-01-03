package com.github.hiendo.tsa.chart;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.text.SimpleDateFormat;

/**
 * General options for basic coordinate chart
 */
public class ChartOptions {
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("MM/dd-HH:mm");
    @QueryParam("title")
    @DefaultValue("Default Chart Title")
    private String title = "Default Chart Title";
    @QueryParam("xAxisLabel")
    @DefaultValue("Default X-Axis Title")
    private String xAxisLabel = "Default X-Axis Label";
    @QueryParam("yAxisLabel")
    @DefaultValue("Default Chart Title")
    private String yAxisLabel = "Default Y-Axis Label";
    @QueryParam("xAxisAsDate")
    @DefaultValue("true")
    private boolean xAxisAsDate = false;
    @QueryParam("timeUnit")
    @DefaultValue("ms")
    private String timeUnit = "ms";
    @QueryParam("dateFormat")
    @DefaultValue("MM/dd-HH:mm")
    private SimpleDateFormat dateFormat = DEFAULT_DATE_FORMAT;
    @QueryParam("height")
    @DefaultValue("600")
    private int height = 600;
    @QueryParam("width")
    @DefaultValue("1000")
    private int width = 1000;
    @QueryParam("startX")
    private Double startX = null;
    @QueryParam("endX")
    private Double endX = null;

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

    public Double getEndX() {
        return endX;
    }

    public Double getStartX() {
        return startX;
    }

    public String getTimeUnit() {
        return timeUnit;
    }
}
