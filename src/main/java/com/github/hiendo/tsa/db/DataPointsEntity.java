package com.github.hiendo.tsa.db;

import com.github.hiendo.tsa.web.entities.DataPoints;

/**
 *
 */
public class DataPointsEntity {
    private long[] times;
    private double[] values;

    public DataPointsEntity(long[] times, double[] values) {
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

    public DataPoints toApiEntity() {
        return new DataPoints(times, values);
    }
}
