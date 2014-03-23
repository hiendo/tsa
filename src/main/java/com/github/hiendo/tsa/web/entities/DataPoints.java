package com.github.hiendo.tsa.web.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility= JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class DataPoints {
    private long[] times;
    private double[] values;

    // Json serialization
    private DataPoints(){}

    public DataPoints(long[] times, double[] values) {
        assert times.length == values.length;
        this.times = times;
        this.values = values;
    }

    public int size() {
        return times.length;
    }

    @JsonIgnore
    public double getValueAt(int index) {
        return values[index];
    }

    @JsonIgnore
    public long getTimeAt(int index) {
        return times[index];
    }

    public long[] getTimes() {
        return times;
    }

    public double[] getValues() {
        return values;
    }
}
