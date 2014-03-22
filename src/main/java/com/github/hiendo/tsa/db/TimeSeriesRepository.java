package com.github.hiendo.tsa.db;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.Date;

// @todo: look here to implement shard:
// http://planetcassandra.org/blog/post/getting-started-with-time-series-data-modeling/
public class TimeSeriesRepository {

    private Session session;

    public TimeSeriesRepository(Session session) {
        this.session = session;
    }

    public void saveTime(String topic, double value) {
        String command = "INSERT INTO timeseries (topic, time, value) " +
                        "VALUES ('" + topic + "', '" + new Date().getTime() + "', " + value + ");";
        session.execute(command);
    }

    public DataPoints getAllDataPointsForTopic(String topic) {
        String command = "SELECT * FROM timeseries " + "WHERE topic = '" + topic + "';";
        ResultSet results = session.execute(command);

        long[] times = new long[results.getAvailableWithoutFetching()];
        double[] values = new double[results.getAvailableWithoutFetching()];

        int rowCount = 0;
        for (Row row : results) {
            times[rowCount] = row.getDate("time").getTime();
            values[rowCount] = row.getDouble("value");
            rowCount++;
        }

        return new DataPoints(times, values);
    }
}
