package com.github.hiendo.tsa.db;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

@Test
public class DataPointRepositoryTests extends AbstractServerTests {

    private DataPointRepository dataPointRepository;

    @BeforeClass
    public void beforeClass() {
        dataPointRepository = new DataPointRepository(cassandraSession);
    }

    @Test
    public void canInsertDataPoint() throws Exception {
        dataPointRepository.saveDataPoint("topic", new DataPoint(2, 2.2));
        dataPointRepository.saveDataPoint("topic", new DataPoint(3, 3.3));
        dataPointRepository.saveDataPoint("topic", new DataPoint(1, 1.1));

        DataPointsEntity dataPoints = dataPointRepository.getAllDataPointsForTopic("topic");
        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points topic", dataPoints.getTopic(), is("topic"));
        assertThat("Data points size", dataPoints.size(), is(3));
        assertThat("Data points x", dataPoints.getX(0), closeTo(1, .01));
        assertThat("Data points x", dataPoints.getX(1), closeTo(2, .01));
        assertThat("Data points x", dataPoints.getX(2), closeTo(3, .01));
        assertThat("Data points y", dataPoints.getY(0), closeTo(1.1, .01));
        assertThat("Data points y", dataPoints.getY(1), closeTo(2.2, .01));
        assertThat("Data points y", dataPoints.getY(2), closeTo(3.3, .01));
    }

    @Test
    public void canGetDataPointsForTopicWithNoData() throws Exception {
        DataPointsEntity dataPoints = dataPointRepository.getAllDataPointsForTopic("non.existing.topic");

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), is(0));
    }
}
