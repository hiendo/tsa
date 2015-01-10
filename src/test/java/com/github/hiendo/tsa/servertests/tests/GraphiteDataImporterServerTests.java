package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoints;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;

/**
 *
 */
@Test
public class GraphiteDataImporterServerTests extends AbstractServerTests {

    @Test
    public void canSendGraphiteData() throws Exception {
        String topic = "topic-" + UUID.randomUUID();

        graphite.send(topic, "44444", 44);
        graphite.send(topic, "33333", 33);
        graphite.send(topic, "33333", 33);
        graphite.send(topic, "55555", 55);


        new GraphiteImportedTestHelper(topicDataPointOperations).verifyTopicNumberOfPoints(topic, 4);

        DataPoints dataPoints = topicDataPointOperations.getDataForTopic(topic);
        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), equalTo(4));
        assertThat("Data points time", dataPoints.getTimestamp(0), is(33l));
        assertThat("Data points time", dataPoints.getTimestamp(1), is(33l));
        assertThat("Data points time", dataPoints.getTimestamp(2), is(44l));
        assertThat("Data points time", dataPoints.getTimestamp(3), is(55l));
        assertThat("Data points value", dataPoints.getValue(0), closeTo(33333, .01));
        assertThat("Data points value", dataPoints.getValue(1), closeTo(33333, .01));
        assertThat("Data points value", dataPoints.getValue(2), closeTo(44444, .01));
        assertThat("Data points value", dataPoints.getValue(3), closeTo(55555, .01));
    }
}
