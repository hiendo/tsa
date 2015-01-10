package com.github.hiendo.tsa.db;

import com.github.hiendo.tsa.config.CassandraProperties;
import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.hamcrest.collection.IsCollectionWithSize;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

@Test
public class DataPointRepositoryTests extends AbstractServerTests {


    private DataPointRepository dataPointRepository;
    private String topic;

    @BeforeClass
    public void beforeClass() {
        dataPointRepository = new DataPointRepository(cassandraSession, new CassandraProperties());
    }

    @BeforeMethod
    public void beforeMethod() {
        topic = "topic-" + UUID.randomUUID();
    }


    @Test
    public void canInsertDataPoint() throws Exception {
        dataPointRepository.saveDataPoint(topic, 10, 1.1);
        dataPointRepository.saveDataPoint(topic, new DataPoint(20, 2.2));
        dataPointRepository.saveDataPointAsync(topic, 30, 3.3).getUninterruptibly();

        DataPointsEntity dataPoints = dataPointRepository.getAllDataPointsForTopic(topic);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points topic", dataPoints.getTopic(), is(topic));
        assertThat("Data points size", dataPoints.size(), is(3));
        assertThat("Data points time", dataPoints.getTimestamp(0), is(10l));
        assertThat("Data points time", dataPoints.getTimestamp(1), is(20l));
        assertThat("Data points time", dataPoints.getTimestamp(2), is(30l));
        assertThat("Data points value", dataPoints.getValue(0), closeTo(1.1, .01));
        assertThat("Data points value", dataPoints.getValue(1), closeTo(2.2, .01));
        assertThat("Data points value", dataPoints.getValue(2), closeTo(3.3, .01));
    }

    @Test
    public void canGetAllPointsBetweenXValueRanges() throws Exception {
        dataPointRepository.saveDataPoint(topic, new DataPoint(8, 8.8));
        dataPointRepository.saveDataPoint(topic, new DataPoint(2, 2.2));
        dataPointRepository.saveDataPoint(topic, new DataPoint(3, 3.3));
        dataPointRepository.saveDataPoint(topic, new DataPoint(1, 1.1));
        dataPointRepository.saveDataPoint(topic, new DataPoint(5, 5.5));
        dataPointRepository.saveDataPoint(topic, new DataPoint(9, 9.9));

        DataPointsEntity dataPoints = dataPointRepository.getDataPointsForTopic(topic, 3l, 8l);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points topic", dataPoints.getTopic(), is(topic));
        assertThat("Data points size", dataPoints.size(), is(3));
        assertThat("Data points time", dataPoints.getTimestamp(0), is(3l));
        assertThat("Data points time", dataPoints.getTimestamp(1), is(5l));
        assertThat("Data points time", dataPoints.getTimestamp(2), is(8l));
        assertThat("Data points value", dataPoints.getValue(0), closeTo(3.3, .01));
        assertThat("Data points value", dataPoints.getValue(1), closeTo(5.5, .01));
        assertThat("Data points value", dataPoints.getValue(2), closeTo(8.8, .01));
    }

    @Test
    public void canGetDataPointsForTopicWithNoData() throws Exception {
        DataPointsEntity dataPoints = dataPointRepository.getAllDataPointsForTopic("non.existing.topic");

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), is(0));
    }

    @Test
    public void canInsertLotsOfDuplicatedTimestamps() throws Exception {
        int numPoints = 200;

        for (int i = 0; i < numPoints; i++) {
            dataPointRepository.saveDataPoint(topic, 10, i);
        }

        DataPointsEntity dataPoints = dataPointRepository.getAllDataPointsForTopic(topic);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points topic", dataPoints.getTopic(), is(topic));
        assertThat("Data points size", dataPoints.size(), is(numPoints));

        Set<Double> values = new HashSet<>();
        for (int i = 0; i < numPoints; i++) {
            assertThat("Data points time", dataPoints.getTimestamp(i), is(10l));
            values.add(dataPoints.getValue(i));
        }

        assertThat("Data points values", values, hasSize(numPoints));
    }
}
