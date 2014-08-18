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
     * @param start start point of the interval
     * @param interval interval between the DataPoints in the set
     * @return set of DataPoints
     */
    public DataPointsSet splitDatapoints(DataPointsEntity dataPointsEntity, double start, double interval) {
        List<DataPointsEntity> dataPointsEntities = new ArrayList<>();
        if (dataPointsEntity.isEmpty()) {
            return new DataPointsSet(dataPointsEntities);
        }

        start = normalizeStart(dataPointsEntity.getFirstX(), start, interval);

        double nextResetCounter = start + interval;

        ArrayList<Double> xValues = new ArrayList<>();
        ArrayList<Double> yValues = new ArrayList<>();
        for (int i = 0; i < dataPointsEntity.size(); i++) {
            double xValue = dataPointsEntity.getX(i);
            double yValue = dataPointsEntity.getY(i);
            if (xValue < nextResetCounter) {
                xValues.add(xValue);
                yValues.add(yValue);
            } else {
                dataPointsEntities.add(new DataPointsEntity(dataPointsEntity.getTopic(), convert(xValues),
                        convert(yValues)));
                xValues = new ArrayList<>();
                yValues = new ArrayList<>();

                xValues.add(xValue);
                yValues.add(yValue);
                nextResetCounter = nextResetCounter + interval;
            }
        }

        dataPointsEntities.add(new DataPointsEntity(dataPointsEntity.getTopic(), convert(xValues), convert(yValues)));

        return new DataPointsSet(dataPointsEntities);
    }

    /**
     * Normalize the start value, particularly in the case where start is smaller than first value by more than an
     * interval.
     *
     * In this case, we want to quickly move the start time so it's smaller than the first value by within an interval.
     *
     * @param firstValue first value of the data point
     * @param start data point to start from
     * @param interval interval to increment each time time data points are split.
     * @return normalized start value.
     */
    private double normalizeStart(double firstValue, double start, double interval) {
        if (start < firstValue) {
            double distance = Math.floor((firstValue - start) / interval);

            start = start + (distance * interval);
        }
        return start;
    }

    private double[] convert(ArrayList<Double> timesToAdd) {
        double[] values =  new double[timesToAdd.size()];
        for(int i = 0; i < timesToAdd.size(); i++) {
            values[i] = timesToAdd.get(i);
        }

        return values;
    }
}