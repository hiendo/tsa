package com.github.hiendo.tsa.web.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility= JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class AggregatedStats {

    private String topic;
    long startTime;
    long endTime;
    int numberOfDataPoints;
    private double mean;
    private double median;
    private double min;
    private double max;
    private double standardDeviation;
    private double percentile10;
    private double percentile90;
    private double sum;

    // Json serialization
    private AggregatedStats(){}

    public AggregatedStats(String topic, long startTime, long endTime, int numberOfDataPoints, double sum, double mean, double median, double min,
            double max, double standardDeviation, double percentile10, double percentile90) {
        this.topic = topic;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfDataPoints = numberOfDataPoints;
        this.sum = sum;
        this.mean = mean;
        this.median = median;
        this.min = min;
        this.max = max;
        this.standardDeviation = standardDeviation;
        this.percentile10 = percentile10;
        this.percentile90 = percentile90;
    }

    public String getTopic() {
        return topic;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getNumberOfDataPoints() {
        return numberOfDataPoints;
    }

    public double getMean() {
        return mean;
    }

    public double getMedian() {
        return median;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getSum() {
        return sum;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public double getPercentile10() {
        return percentile10;
    }

    public double getPercentile90() {
        return percentile90;
    }
}
