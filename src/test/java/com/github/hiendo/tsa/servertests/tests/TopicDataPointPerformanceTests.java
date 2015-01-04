
package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.fail;

/**
 *
 */
@Test
public class TopicDataPointPerformanceTests extends AbstractServerTests {
    private static int NUM_POINTS = 500;

    private String topic;
    private GraphiteImportedTestHelper graphiteImportedTestHelper;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        topic = "topic-" + UUID.randomUUID();
        graphiteImportedTestHelper = new GraphiteImportedTestHelper(topicDataPointOperations);
    }

    @Test
    public void canSendManyPointsUsingHttp() throws Exception {
        long now = System.currentTimeMillis();
        for (int i = 0; i < NUM_POINTS; i++) {
            topicDataPointOperations.addData(topic, new DataPoint(i, 4.4));
        }
        System.out.println("http: " + (System.currentTimeMillis() - now) / 1000.0);

        graphiteImportedTestHelper.verifyTopicNumberOfPoints(topic, NUM_POINTS);
    }


    @Test
    public void canSendManyPointsUsingGraphite() throws Exception {
        long now = System.currentTimeMillis();

        for (int i = 0; i < NUM_POINTS; i++) {
            graphite.send(topic, "44444", i);
        }

        System.out.println("graphite: " + (System.currentTimeMillis() - now) / 1000.0);

        graphiteImportedTestHelper.verifyTopicNumberOfPoints(topic, NUM_POINTS);
    }
}
