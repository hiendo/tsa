package com.github.hiendo.tsa.chart;

import com.github.hiendo.tsa.db.DataPointsEntity;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * Convenient utility methods for calculating stats.
 */
public class StatsCalculator {
    private StatsCalculator() {
    }

    /**
     * Get the stats for the values in the data points
     *
     * @param dataPointsEntity data points
     * @return Stats.
     */
    public static DescriptiveStatistics getDescriptiveStatistics(DataPointsEntity dataPointsEntity) {
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
        for (int dataPointIndex = 0; dataPointIndex < dataPointsEntity.size(); dataPointIndex++) {
            descriptiveStatistics.addValue(dataPointsEntity.getValueAt(dataPointIndex));
        }

        return descriptiveStatistics;
    }
}
