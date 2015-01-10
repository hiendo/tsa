package com.github.hiendo.tsa.web.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * List of timestamps and data value associated with it.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility= JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class DataPoints {
    private long[] timeStamps;
    private double[] values;

    // Json serialization
    private DataPoints(){}

    public DataPoints(long[] timeStamps, double[] values) {
        assert timeStamps.length == values.length;
        this.timeStamps = timeStamps;
        this.values = values;
    }

    public int size() {
        return timeStamps.length;
    }

    @JsonIgnore
    public long getTimestamp(int index) {
        return timeStamps[index];
    }

    @JsonIgnore
    public double getValue(int index) {
        return values[index];
    }

    public long[] getTimeStamps() {
        return timeStamps;
    }

    public double[] getValues() {
        return values;
    }
}
