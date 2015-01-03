package com.github.hiendo.tsa.db;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.github.hiendo.tsa.config.CassandraProperties;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

// @todo: look here to implement shard:
// http://planetcassandra.org/blog/post/getting-started-with-time-series-data-modeling/
@Component
public class DataPointRepository {

    private Session session;
    private CassandraProperties cassandraProperties;
    int numberOfDaysToKeepData;

    @Autowired
    public DataPointRepository(Session session, CassandraProperties cassandraProperties) {
        this.session = session;
        this.cassandraProperties = cassandraProperties;
        this.numberOfDaysToKeepData = cassandraProperties.getNumberOfDaysToKeepData();
    }

    /**
     * Save the data point for the specified topic.
     *
     * @param topic topic to save data point for
     * @param dataPoint data point to save
     */
    public void saveDataPoint(String topic, DataPoint dataPoint) {
        saveDataPoint(topic, dataPoint.getxValue(), dataPoint.getyValue());
    }

    /**
     * Save the data point for the specified topic.
     *
     * @param topic topic to save data point for
     * @param xValue x value to save
     * @param yValue y value to save
     */
    public void saveDataPoint(String topic, double xValue, double yValue) {
        Insert queryStatement = QueryBuilder.insertInto("datapoints").value("topic", topic)
                .value("xValue", xValue).value("yValue", yValue);

        if (numberOfDaysToKeepData != -1) {
            session.execute(queryStatement);
        } else {
            session.execute(queryStatement.using(ttl((int) TimeUnit.DAYS.toSeconds(
                    cassandraProperties.getNumberOfDaysToKeepData()))));
        }
    }


    /**
     * Get all data points for the range.
     *
     * @param topic topic to get points for
     * @param startX start x range; null means to get all points up to end x range
     * @param endX end x range; null means to get all points from start x range to infinity
     * @return data points within range.
     */
    public DataPointsEntity getDataPointsForTopic(String topic, Number startX, Number endX) {
        Select.Where where = QueryBuilder.select().from("datapoints").where(eq("topic", topic));

        if (startX != null) {
            where = where.and(gte("xValue", startX));
        }

        if (endX != null) {
            where = where.and(lte("xValue", endX));
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

    private DataPointsEntity convert(String topic, ResultSet results) {
        List<Row> rowList = results.all();
        double[] xValues = new double[rowList.size()];
        double[] yValues = new double[rowList.size()];

        int rowCount = 0;
        for (Row row : rowList) {
            xValues[rowCount] = row.getDouble("xValue");
            yValues[rowCount] = row.getDouble("yValue");
            rowCount++;
        }

        return new DataPointsEntity(topic, xValues, yValues);
    }
}
