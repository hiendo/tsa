package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;
import org.joda.time.MutableDateTime;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;


@Test
public class TopicDataPointTests extends AbstractServerTests {

    @Test
    public void canAddDataPoint() throws Exception {
        long now = MutableDateTime.now().toDate().getTime();
        String topic = "topic-" + UUID.randomUUID();

        topicDataPointOperations.addData(topic, new DataPoint(44444, 4.4));
        topicDataPointOperations.addData(topic, new DataPoint(33333, 3.3));
        topicDataPointOperations.addData(topic, new DataPoint(33333, 3.7));
        topicDataPointOperations.addData(topic, new DataPoint(now, 5.5));

        DataPoints dataPoints = topicDataPointOperations.getDataForTopic(topic);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), equalTo(4));
        assertThat("Data points time", dataPoints.getTimestamp(0), is(33333l));
        assertThat("Data points time", dataPoints.getTimestamp(1), is(33333l));
        assertThat("Data points time", dataPoints.getTimestamp(2), is(44444l));
        assertThat("Data points time", dataPoints.getTimestamp(3), is(now));
        assertThat("Data points value", dataPoints.getValue(0), anyOf(closeTo(3.3, .01), closeTo(3.7, .01)));
        assertThat("Data points value", dataPoints.getValue(1), anyOf(closeTo(3.3, .01), closeTo(3.7, .01)));
        assertThat("Data points value", dataPoints.getValue(2), closeTo(4.4, .01));
        assertThat("Data points value", dataPoints.getValue(3), closeTo(5.5, .01));
    }

    @Test
    public void canGetValuesBetweenTimeRange() throws Exception {
        String topic = "topic-" + UUID.randomUUID();

        topicDataPointOperations.addData(topic, new DataPoint(8, 8.8));
        topicDataPointOperations.addData(topic, new DataPoint(5, 5.5));
        topicDataPointOperations.addData(topic, new DataPoint(4, 4.4));
        topicDataPointOperations.addData(topic, new DataPoint(6, 6.6));

        DataPoints dataPoints = topicDataPointOperations.getDataForTopicInRange(topic, 4, 6);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), equalTo(3));
        assertThat("Data points time", dataPoints.getTimestamp(0), is(4l));
        assertThat("Data points time", dataPoints.getTimestamp(1), is(5l));
        assertThat("Data points time", dataPoints.getTimestamp(2), is(6l));
        assertThat("Data points value", dataPoints.getValue(0), closeTo(4.4, .01));
        assertThat("Data points value", dataPoints.getValue(1), closeTo(5.5, .01));
        assertThat("Data points value", dataPoints.getValue(2), closeTo(6.6, .01));
    }

    @Test
    public void canGetDataPointsWhenTopicHasNoDataYet() throws Exception {
        String topic = "topic-" + UUID.randomUUID();

        DataPoints dataPoints = topicDataPointOperations.getDataForTopic(topic);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), equalTo(0));
    }
}
