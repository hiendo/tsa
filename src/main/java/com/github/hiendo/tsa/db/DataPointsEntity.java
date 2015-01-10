package com.github.hiendo.tsa.db;

import com.github.hiendo.tsa.web.entities.DataPoints;

/**
 * Represents a data points for a specific topic.
 */
public class DataPointsEntity {
    protected String topic = "";
    protected long[] timestamps;
    protected double[] values;

    public DataPointsEntity(String topic, long[] timestamps, double[] values) {
        if (timestamps.length != values.length) {
            throw new IllegalArgumentException("Mismatch size of x and y values");
        }
        this.topic = topic;
        this.timestamps = timestamps;
        this.values = values;
    }

    public int size() {
        return timestamps.length;
    }

    public double getValue(int index) {
        return values[index];
    }

    public long getTimestamp(int index) {
        return timestamps[index];
    }

    public DataPoints toApiEntity() {
        return new DataPoints(timestamps, values);
    }

    public String getTopic() {
        return topic;
    }

    public long[] getTimestamps() {
        return timestamps;
    }

    public double[] getValues() {
        return values;
    }

    public long getFirstTimestamp() {
        return timestamps[0];
    }

    public long getLastTimestamp() {
        return timestamps[size() - 1];
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
