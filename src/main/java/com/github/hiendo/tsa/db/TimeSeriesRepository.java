package com.github.hiendo.tsa.db;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.github.hiendo.tsa.web.entities.DataPoint;
import com.google.common.collect.ObjectArrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

// @todo: look here to implement shard:
// http://planetcassandra.org/blog/post/getting-started-with-time-series-data-modeling/
@Component
public class TimeSeriesRepository {

    private Session session;

    @Autowired
    public TimeSeriesRepository(Session session) {
        this.session = session;
    }

    public void saveTime(String topic, DataPoint dataPoint) {
        String command = "INSERT INTO timeseries (topic, time, value) " +
                "VALUES ('" + topic + "', '" + dataPoint.getTime() + "', " + dataPoint.getValue() + ");";
        session.execute(command);
    }

    public DataPointsEntity getAllDataPointsForTopic(String topic) {
        String command = "SELECT * FROM timeseries " + "WHERE topic = '" + topic + "';";
        ResultSet results = session.execute(command);

        List<Row> rowList = results.all();
        long[] times = new long[rowList.size()];
        double[] values = new double[rowList.size()];

        int rowCount = 0;
        for (Row row : rowList) {
            times[rowCount] = row.getDate("time").getTime();
            values[rowCount] = row.getDouble("value");
            rowCount++;
        }

        return new DataPointsEntity(topic, times, values);
    }
}
