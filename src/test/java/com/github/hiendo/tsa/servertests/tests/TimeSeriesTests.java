package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.db.DataPoints;
import com.github.hiendo.tsa.entities.TestEntity;
import com.github.hiendo.tsa.servertests.AbstractServerTests;
import org.joda.time.MutableDateTime;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;


@Test
public class TimeSeriesTests extends AbstractServerTests {

    @Test(enabled = false)
    public void canAddDataPoint() throws Exception {
        long now = MutableDateTime.now().toDate().getTime();
        String topic = "topic-" + UUID.randomUUID();

        timeSeriesOperations.addData(topic, 3.14);
        timeSeriesOperations.addData(topic, 3.15);
        timeSeriesOperations.addData(topic, 3.16);

        DataPoints dataPoints = timeSeriesOperations.getDataForTopic(topic);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), is(2));
        assertThat("Data points time", dataPoints.getTimeAt(0), allOf(greaterThan(now - 1000), lessThan(now + 1000)));
        assertThat("Data points time", dataPoints.getTimeAt(1), allOf(greaterThan(now - 1000), lessThan(now + 1000)));
        assertThat("Data points value", dataPoints.getValueAt(0), closeTo(2.54, .01));
        assertThat("Data points value", dataPoints.getValueAt(1), closeTo(2.55, .01));
    }
}
