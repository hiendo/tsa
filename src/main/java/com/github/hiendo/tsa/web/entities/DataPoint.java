package com.github.hiendo.tsa.web.entities;

/**
 *
 */
public class DataPoint {

    private long time;
    private double value;

    // Json serialization
    private DataPoint(){}

    public DataPoint(long time, double value) {
        this.time = time;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public long getTime() {
        return time;
    }
}
