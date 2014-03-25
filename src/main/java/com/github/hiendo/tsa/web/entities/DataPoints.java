package com.github.hiendo.tsa.web.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility= JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class DataPoints {
    private double[] xValues;
    private double[] yValues;

    // Json serialization
    private DataPoints(){}

    public DataPoints(double[] xValues, double[] yValues) {
        assert xValues.length == yValues.length;
        this.xValues = xValues;
        this.yValues = yValues;
    }

    public int size() {
        return xValues.length;
    }

    @JsonIgnore
    public double getX(int index) {
        return yValues[index];
    }

    @JsonIgnore
    public double getY(int index) {
        return xValues[index];
    }

    public double[] getxValues() {
        return xValues;
    }

    public double[] getyValues() {
        return yValues;
    }
}
