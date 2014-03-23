package com.github.hiendo.tsa.web.entities;

/**
 *
 */
public class DataPoint {

    private double value;

    // Json serialization
    private DataPoint(){}

    public DataPoint(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
