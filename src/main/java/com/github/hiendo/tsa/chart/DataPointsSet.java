package com.github.hiendo.tsa.chart;

import com.github.hiendo.tsa.db.DataPointsEntity;

import java.util.List;

/**
 * List of DataPoints
 */
public class DataPointsSet {
    private List<DataPointsEntity> dataPointsEntities;

    public DataPointsSet(List<DataPointsEntity> dataPointsEntities) {
        this.dataPointsEntities = dataPointsEntities;
    }

    public int getSize() {
        return dataPointsEntities.size();
    }

    public DataPointsEntity getDataPoints(int index) {
        return dataPointsEntities.get(index);
    }
}
