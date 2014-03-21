package com.github.hvd617.tsa.db;

/**
 *
 */
public class DataPoints {
    private long[] times;
    private double[] values;

    public DataPoints(long[] times, double[] values) {
        assert times.length == values.length;
        this.times = times;
        this.values = values;
    }

    public int size() {
        return times.length;
    }

    public double getValueAt(int index) {
        return values[index];
    }

    public long getTimeAt(int index) {
        return times[index];
    }
}
