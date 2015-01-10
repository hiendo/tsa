package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.AggregatedStatsSet;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;


@Test
public class MetricsIntervalTests extends AbstractServerTests {

    private String topic;

    @BeforeClass
    public void setUp() throws Exception {
        topic = "topic-" + UUID.randomUUID();

        topicDataPointOperations.addData(topic, new DataPoint(0, 0));
        topicDataPointOperations.addData(topic, new DataPoint(10, 5));

        topicDataPointOperations.addData(topic, new DataPoint(100, 10));
        topicDataPointOperations.addData(topic, new DataPoint(110, 11));
        topicDataPointOperations.addData(topic, new DataPoint(190, 19));

        topicDataPointOperations.addData(topic, new DataPoint(220, 220));
        topicDataPointOperations.addData(topic, new DataPoint(250, 250));
        topicDataPointOperations.addData(topic, new DataPoint(270, 270));
        topicDataPointOperations.addData(topic, new DataPoint(280, 280));
    }

    @Test
    public void canAggregateTopicInTimeRange() throws Exception {
        AggregatedStatsSet aggregatedStatsSet =
                metricsIntervalOperations.aggregateTopicInTimeRange(topic, 11, 219);

        assertThat("Data points", aggregatedStatsSet, notNullValue());
        assertThat("Data points set size", aggregatedStatsSet.size(), equalTo(1));
        assertThat("Data points topic", aggregatedStatsSet.getAggregatedStats(0).getTopic(), is(topic));
        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(0).getStartTime(), is(100l));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(0).getEndTime(), is(190l));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(0).getNumberOfDataPoints(), is(3));
        assertThat("Data points min", aggregatedStatsSet.getAggregatedStats(0).getMin(), closeTo(10, .01));
        assertThat("Data points max", aggregatedStatsSet.getAggregatedStats(0).getMax(), closeTo(19, .01));
    }

    @Test
    public void canAggregateStatsByTimeInterval() throws Exception {
        AggregatedStatsSet aggregatedStatsSet =
                metricsIntervalOperations.aggregateTopicByTimeInterval(topic, 0, 100);

        assertThat("Data points", aggregatedStatsSet, notNullValue());
        assertThat("Data points set size", aggregatedStatsSet.size(), equalTo(3));

        assertThat("Data points topic", aggregatedStatsSet.getAggregatedStats(0).getTopic(), is(topic));
        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(0).getStartTime(), is(0l));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(0).getEndTime(), is(10l));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(0).getNumberOfDataPoints(), is(2));
        assertThat("Data points max", aggregatedStatsSet.getAggregatedStats(0).getMax(), closeTo(5, .01));

        assertThat("Data points topic", aggregatedStatsSet.getAggregatedStats(1).getTopic(), is(topic));
        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(1).getStartTime(), is(100l));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(1).getEndTime(), is(190l));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(1).getNumberOfDataPoints(), is(3));
        assertThat("Data points max", aggregatedStatsSet.getAggregatedStats(1).getMax(), closeTo(19, .01));

        assertThat("Data points topic", aggregatedStatsSet.getAggregatedStats(2).getTopic(), is(topic));
        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(2).getStartTime(), is(220l));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(2).getEndTime(), is(280l));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(2).getNumberOfDataPoints(), is(4));
        assertThat("Data points max", aggregatedStatsSet.getAggregatedStats(2).getMax(), closeTo(280, .01));
    }

    @Test
    public void canAggregateStatsForAllTime() throws Exception {
        AggregatedStatsSet aggregatedStatsSet =
                metricsIntervalOperations.aggregateTopicForAllTime(topic);

        assertThat("Data points", aggregatedStatsSet, notNullValue());
        assertThat("Data points set size", aggregatedStatsSet.size(), equalTo(1));
        assertThat("Data points topic", aggregatedStatsSet.getAggregatedStats(0).getTopic(), is(topic));
        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(0).getStartTime(), is(0l));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(0).getEndTime(), is(280l));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(0).getNumberOfDataPoints(), is(9));
    }


    @Test
    public void canAggregateStatsWhenStartIsLowerThanFirstValueByMoreThanInterval() throws Exception {
        AggregatedStatsSet aggregatedStatsSet =
                metricsIntervalOperations.aggregateTopicByTimeInterval(topic, -1000000, 100);

        assertThat("Data points", aggregatedStatsSet, notNullValue());
        assertThat("Data points set size", aggregatedStatsSet.size(), equalTo(3));

        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(0).getNumberOfDataPoints(), is(2));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(1).getNumberOfDataPoints(), is(3));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(2).getNumberOfDataPoints(), is(4));
    }

    @Test
    public void canAggregateStatsWhenStartIsLowerThanFirstValueWithinAnInterval() throws Exception {
        AggregatedStatsSet aggregatedStatsSet =
                metricsIntervalOperations.aggregateTopicByTimeInterval(topic, -5, 100);

        assertThat("Data points", aggregatedStatsSet, notNullValue());
        assertThat("Data points set size", aggregatedStatsSet.size(), equalTo(3));

        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(0).getNumberOfDataPoints(), is(2));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(1).getNumberOfDataPoints(), is(3));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(2).getNumberOfDataPoints(), is(4));
    }

    @Test
    public void canAggregateStatsWithStartGreaterThanFirstValue() throws Exception {
        AggregatedStatsSet aggregatedStatsSet =
                metricsIntervalOperations.aggregateTopicByTimeInterval(topic, 100, 100);

        assertThat("Data points", aggregatedStatsSet, notNullValue());
        assertThat("Data points set size", aggregatedStatsSet.size(), equalTo(2));

        assertThat("Data points topic", aggregatedStatsSet.getAggregatedStats(0).getTopic(), is(topic));
        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(0).getStartTime(), is(100l));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(0).getEndTime(), is(190l));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(0).getNumberOfDataPoints(), is(3));
        assertThat("Data points max", aggregatedStatsSet.getAggregatedStats(0).getMax(), closeTo(19, .01));

        assertThat("Data points topic", aggregatedStatsSet.getAggregatedStats(1).getTopic(), is(topic));
        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(1).getStartTime(), is(220l));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(1).getEndTime(), is(280l));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(1).getNumberOfDataPoints(), is(4));
        assertThat("Data points max", aggregatedStatsSet.getAggregatedStats(1).getMax(), closeTo(280, .01));
    }
}
