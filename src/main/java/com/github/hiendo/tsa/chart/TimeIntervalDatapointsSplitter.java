package com.github.hiendo.tsa.chart;

import com.github.hiendo.tsa.db.DataPointsEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Splits up a DataPoints into a set of DataPoints by an incrementatl number.
 */
@Component
public class TimeIntervalDatapointsSplitter {

    /**
     * Split up DataPoints to a set of DataPoints separated by the specified interval.
     *
     * @param dataPointsEntity datapoints so split
     * @param startTime start point of the interval
     * @param interval interval between the DataPoints in the set
     * @return set of DataPoints
     */
    public DataPointsSet splitDatapoints(DataPointsEntity dataPointsEntity, Long startTime, Double interval) {

        List<DataPointsEntity> dataPointsEntities = new ArrayList<>();
        if (dataPointsEntity.isEmpty()) {
            return new DataPointsSet(dataPointsEntities);
        }

        if (startTime == null) {
            startTime = Long.MIN_VALUE;
        }

        startTime = Math.max(dataPointsEntity.getFirstTimestamp(), startTime);

        if (interval == null) {
            interval = Double.MAX_VALUE;
        }

        double nextResetCounter = startTime + interval;

        ArrayList<Long> timestamps = new ArrayList<>();
        ArrayList<Double> values = new ArrayList<>();
        for (int i = 0; i < dataPointsEntity.size(); i++) {
            long timestamp = dataPointsEntity.getTimestamp(i);
            double value = dataPointsEntity.getValue(i);
            if (timestamp < startTime) {
                continue;
            }

            if (timestamp < nextResetCounter) {
                timestamps.add(timestamp);
                values.add(value);
            } else {
                dataPointsEntities.add(new DataPointsEntity(dataPointsEntity.getTopic(), convertTimestamps(timestamps),
                        convertValues(values)));
                timestamps = new ArrayList<>();
                values = new ArrayList<>();

                timestamps.add(timestamp);
                values.add(value);
                nextResetCounter = nextResetCounter + interval;
            }
        }

        dataPointsEntities.add(new DataPointsEntity(dataPointsEntity.getTopic(), convertTimestamps(timestamps),
                convertValues(values)));

        return new DataPointsSet(dataPointsEntities);
    }

    private long[] convertTimestamps(ArrayList<Long> timesToAdd) {
        long[] values =  new long[timesToAdd.size()];
        for(int i = 0; i < timesToAdd.size(); i++) {
            values[i] = timesToAdd.get(i);
        }

        return values;
    }

    private double[] convertValues(ArrayList<Double> timesToAdd) {
        double[] values =  new double[timesToAdd.size()];
        for(int i = 0; i < timesToAdd.size(); i++) {
            values[i] = timesToAdd.get(i);
        }

        return values;
    }
}
