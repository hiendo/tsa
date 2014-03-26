/*
 * Project Horizon
 * (c) 2013 VMware, Inc. All rights reserved.
 * VMware Confidential.
 */

package com.github.hiendo.tsa.servertests.tests;

import com.codahale.metrics.graphite.Graphite;
import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.testng.Assert.fail;

/**
 *
 */
@Test
public class TopicDataPointPerformanceTests extends AbstractServerTests {
    private static int NUM_POINTS = 500;

    private String topic;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        topic = "topic-" + UUID.randomUUID();
    }

    @Test
    public void canSendManyPointsUsingHttp() throws Exception {
        long now = System.currentTimeMillis();
        for (int i = 0; i < NUM_POINTS; i++) {
            topicDataPointOperations.addData(topic, new DataPoint(i, 4.4));
        }
        System.out.println("http: " + (System.currentTimeMillis() - now) / 1000.0);

        verifyTopicHasCorrectPoints();

    }

    private void verifyTopicHasCorrectPoints() throws InterruptedException {
        long time = 0;
        while (true) {
            DataPoints dataPoints = topicDataPointOperations.getDataForTopic(topic);
            if (dataPoints.size() == NUM_POINTS) {
                break;
            }

            Thread.sleep(200);
            time += 200;
            if (time > 2000) {
                fail("Timed out waiting for points.  Total points: " + dataPoints.size());
            }
        }
    }

    @Test
    public void canSendManyPointsUsingGraphite() throws Exception {
        long now = System.currentTimeMillis();

        for (int i = 0; i < NUM_POINTS; i++) {
            graphite.send(topic, "44444", i);
        }

        System.out.println("graphite: " + (System.currentTimeMillis() - now) / 1000.0);

        verifyTopicHasCorrectPoints();
    }
}
