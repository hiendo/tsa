package com.github.hiendo.tsa.db;

import com.github.hiendo.tsa.web.entities.DataPoints;

/**
 * Represents a data points for a specific topic.
 */
public class DataPointsEntity {
    protected String topic = "";
    protected double[] xValues;
    protected double[] yValues;

    public DataPointsEntity(String topic, double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("Mismatch size of x and y values");
        }
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

    public double[] getXValues() {
        return xValues;
    }

    public double[] getYValues() {
        return yValues;
    }

    public double getFirstX() {
        return xValues[0];
    }

    public double getLastX() {
        return xValues[size() - 1];
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void timeShiftToFirstXValue() {
        if (isEmpty()) {
            return;
        }

        timeFirstAgainstXValue(xValues[0]);
    }

    public void timeFirstAgainstXValue(double xValue) {
        for(int i = 0; i < xValues.length; i++) {
            xValues[i] = xValues[i] - xValue;
        }
    }
}
