package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;
import org.joda.time.MutableDateTime;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;


@Test
public class BasicDataPointTests extends AbstractServerTests {

    @Test
    public void canAddDataPoint() throws Exception {
        long now = MutableDateTime.now().toDate().getTime();
        String topic = "topic-" + UUID.randomUUID();

        basicDataPointOperation.addData(topic, new DataPoint(44444, 4.4));
        basicDataPointOperation.addData(topic, new DataPoint(33333, 3.3));
        basicDataPointOperation.addData(topic, new DataPoint(now, 5.5));

        DataPoints dataPoints = basicDataPointOperation.getDataForTopic(topic);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), equalTo(3));
        assertThat("Data points time", dataPoints.getY(0), closeTo(33333, .01));
        assertThat("Data points time", dataPoints.getY(1), closeTo(44444, .01));
        assertThat("Data points time", dataPoints.getY(2), closeTo(now, .01));
        assertThat("Data points value", dataPoints.getX(0), closeTo(3.3, .01));
        assertThat("Data points value", dataPoints.getX(1), closeTo(4.4, .01));
        assertThat("Data points value", dataPoints.getX(2), closeTo(5.5, .01));
    }

    @Test
    public void canGetDataPointsWhenTopicHasNoDataYet() throws Exception {
        String topic = "topic-" + UUID.randomUUID();

        DataPoints dataPoints = basicDataPointOperation.getDataForTopic(topic);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), equalTo(0));
    }
}
