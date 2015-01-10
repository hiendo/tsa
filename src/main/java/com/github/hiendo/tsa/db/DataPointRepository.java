package com.github.hiendo.tsa.db;


import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.utils.UUIDs;
import com.github.hiendo.tsa.config.CassandraProperties;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

// @todo: look here to implement shard:
// http://planetcassandra.org/blog/post/getting-started-with-time-series-data-modeling/
@Component
public class DataPointRepository {
    private static final Logger logger = LoggerFactory.getLogger(DataPointRepository.class);


    private final Random random;
    private Session session;
    private CassandraProperties cassandraProperties;
    int numberOfDaysToKeepData;

    @Autowired
    public DataPointRepository(Session session, CassandraProperties cassandraProperties) {
        this.random = new Random(System.currentTimeMillis());
        this.session = session;
        this.cassandraProperties = cassandraProperties;
        this.numberOfDaysToKeepData = cassandraProperties.getNumberOfDaysToKeepData();
    }

    /**
     * Save the data point for the specified topic.
     *
     * @param topic     topic to save data point for
     * @param dataPoint data point to save
     */
    public void saveDataPoint(String topic, DataPoint dataPoint) {
        saveDataPoint(topic, dataPoint.getTimestamp(), dataPoint.getValue());
    }

    /**
     * Save the data point for the specified topic.
     *
     * @param topic     topic to save data point for
     * @param timestamp timestamp to save
     * @param value     value to save
     */
    public void saveDataPoint(String topic, long timestamp, double value) {
        session.execute(createInsertStatement(topic, timestamp, value));
    }

    /**
     * Save the data point for the specified topic.
     *
     * @param topic     topic to save data point for
     * @param timestamp timestamp to save
     * @param value     value to save
     * @return future result of the query
     */
    public ResultSetFuture saveDataPointAsync(String topic, long timestamp, double value) {
        return session.executeAsync(createInsertStatement(topic, timestamp, value));
    }

    /**
     * Get all data points for the range.
     *
     * @param topic     topic to get points for
     * @param startTime start time; null means to get all points up to end x range
     * @param endTime   end time; null means to get all points from start x range to infinity
     * @return data points within range.
     */
    public DataPointsEntity getDataPointsForTopic(String topic, Long startTime, Long endTime) {
        Select.Where where = QueryBuilder.select().from("datapoints").where(eq("topic", topic));

        if (startTime != null) {
            where = where.and(gte("timestamp", org.apache.cassandra.utils.UUIDGen.minTimeUUID(startTime)));
        }

        if (endTime != null) {
            where = where.and(lte("timestamp", org.apache.cassandra.utils.UUIDGen.maxTimeUUID(endTime)));
        }

        ResultSet results = session.execute(where);

        return convert(topic, results);
    }

    /**
     * Get all points for topic.
     *
     * @param topic topic to get points for
     * @return all points for topic.
     */
    protected DataPointsEntity getAllDataPointsForTopic(String topic) {
        return getDataPointsForTopic(topic, null, null);
    }

    private Statement createInsertStatement(String topic, long timestamp, double value) {
        // Can't sem to find good way to create timeuuid from specific timestamp.  This is good enough as
        // likelihood of collision is very low
        java.util.UUID timeuuid = org.apache.cassandra.utils.UUIDGen.getTimeUUID(timestamp, random.nextLong());

        Insert queryStatement = QueryBuilder.insertInto("datapoints").value("topic", topic)
                .value("timestamp", timeuuid).value("value", value);

        if (numberOfDaysToKeepData != -1) {
            return queryStatement;
        } else {
            return queryStatement.using(ttl((int) TimeUnit.DAYS.toSeconds(
                    cassandraProperties.getNumberOfDaysToKeepData())));
        }

    }

    private DataPointsEntity convert(String topic, ResultSet results) {
        List<Row> rowList = results.all();
        long[] timestamps = new long[rowList.size()];
        double[] values = new double[rowList.size()];

        int rowCount = 0;
        for (Row row : rowList) {
            timestamps[rowCount] = UUIDs.unixTimestamp(row.getUUID("timestamp"));
            values[rowCount] = row.getDouble("value");
            rowCount++;
        }

        return new DataPointsEntity(topic, timestamps, values);
    }
}
