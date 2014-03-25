package com.github.hiendo.tsa.db;

import com.github.hiendo.tsa.web.entities.DataPoints;

/**
 *
 */
public class DataPointsEntity {
    private String topic = "";
    private double[] xValues;
    private double[] yValues;

    public DataPointsEntity(String topic, double[] xValues, double[] yValues) {
        assert xValues.length == yValues.length;
        this.topic = topic;
        this.xValues = xValues;
        this.yValues = yValues;
    }

    public int size() {
        return xValues.length;
    }

    public double getY(int index) {
        return yValues[index];
    }

    public double getX(int index) {
        return xValues[index];
    }

    public DataPoints toApiEntity() {
        return new DataPoints(xValues, yValues);
    }

    public String getTopic() {
        return topic;
    }
}
