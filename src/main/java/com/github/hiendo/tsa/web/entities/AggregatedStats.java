package com.github.hiendo.tsa.web.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility= JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class AggregatedStats {

    double startX;
    double endX;
    int numberOfPoints;
    private double mean;
    private double median;
    private double min;
    private double max;

    // Json serialization
    private AggregatedStats(){}

    public AggregatedStats(double startX, double endX, int numberOfPoints, double mean, double median, double min,
            double max) {
        this.startX = startX;
        this.endX = endX;
        this.numberOfPoints = numberOfPoints;
        this.mean = mean;
        this.median = median;
        this.min = min;
        this.max = max;
    }

    public double getStartX() {
        return startX;
    }

    public double getEndX() {
        return endX;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
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
}
