
package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.chart.BasicXyLineChart;
import com.github.hiendo.tsa.servertests.operations.TopicDataPointOperations;
import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import static org.testng.Assert.fail;

public class GraphiteImportedTestHelper {
    final static Logger logger = LoggerFactory.getLogger(BasicXyLineChart.class);

    private TopicDataPointOperations topicDataPointOperations;

    public GraphiteImportedTestHelper(TopicDataPointOperations topicDataPointOperations) {
        this.topicDataPointOperations = topicDataPointOperations;
    }

    public void verifyTopicNumberOfPoints(String topic, int numPoints) throws InterruptedException {
        long time = 0;
        while (true) {
            DataPoints dataPoints = topicDataPointOperations.getDataForTopic(topic);
            if (dataPoints.size() == numPoints) {
                break;
            }
            logger.debug("Waiting for point count " + numPoints + " but got " + dataPoints.size());

            Thread.sleep(200);
            time += 200;

            if (time > 5000) {
                fail("Timed out waiting for points.  Total points: " + dataPoints.size());
            }
        }
    }
}
