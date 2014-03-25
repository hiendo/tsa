package com.github.hiendo.tsa.db;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

// @todo: look here to implement shard:
// http://planetcassandra.org/blog/post/getting-started-with-time-series-data-modeling/
@Component
public class DataPointRepository {

    private Session session;

    @Autowired
    public DataPointRepository(Session session) {
        this.session = session;
    }

    public void saveDataPoint(String topic, DataPoint dataPoint) {
        String command = "INSERT INTO datapoints (topic, xValue, yValue) " +
                "VALUES ('" + topic + "', " + dataPoint.getxValue() + ", " + dataPoint.getyValue() + ");";
        session.execute(command);
    }

    public DataPointsEntity getAllDataPointsForTopic(String topic) {
        String command = "SELECT * FROM datapoints " + "WHERE topic = '" + topic + "';";
        ResultSet results = session.execute(command);

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
