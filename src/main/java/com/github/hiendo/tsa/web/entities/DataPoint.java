package com.github.hiendo.tsa.web.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility= JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class DataPoint {

    private long timestamp;

    private double value;

    // Json serialization
    private DataPoint(){}

    public DataPoint(long timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }
}
