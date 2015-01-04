
package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.servertests.operations.TopicDataPointOperations;
import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import static org.testng.Assert.fail;

public class GraphiteImportedTestHelper {

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

            Thread.sleep(200);
            time += 200;
            if (time > 2000) {
                fail("Timed out waiting for points.  Total points: " + dataPoints.size());
            }
        }
    }
}
