package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.AggregatedStatsSet;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;


@Test
public class MetricsAggregatorTests extends AbstractServerTests {

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
    public void canAggregateStats() throws Exception {


        AggregatedStatsSet aggregatedStatsSet =
                metricsAggregatorOperations.aggregateMetricByTimeInterval(topic, 0, 100);

        assertThat("Data points", aggregatedStatsSet, notNullValue());
        assertThat("Data points set size", aggregatedStatsSet.size(), equalTo(3));

        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(0).getStartX(), closeTo(0.0, .01));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(0).getEndX(), closeTo(10, .01));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(0).getNumberOfPoints(), is(2));
        assertThat("Data points max", aggregatedStatsSet.getAggregatedStats(0).getMax(), closeTo(5, .01));

        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(1).getStartX(), closeTo(100, .01));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(1).getEndX(), closeTo(190, .01));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(1).getNumberOfPoints(), is(3));
        assertThat("Data points max", aggregatedStatsSet.getAggregatedStats(1).getMax(), closeTo(19, .01));

        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(2).getStartX(), closeTo(220, .01));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(2).getEndX(), closeTo(280, .01));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(2).getNumberOfPoints(), is(4));
        assertThat("Data points max", aggregatedStatsSet.getAggregatedStats(2).getMax(), closeTo(280, .01));
    }


    @Test
    public void canAggregateStatsWithTimeRange() throws Exception {
        AggregatedStatsSet aggregatedStatsSet =
                metricsAggregatorOperations.aggregateMetricByTimeInterval(topic, 100, 100);

        assertThat("Data points", aggregatedStatsSet, notNullValue());
        assertThat("Data points set size", aggregatedStatsSet.size(), equalTo(2));

        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(0).getStartX(), closeTo(100, .01));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(0).getEndX(), closeTo(190, .01));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(0).getNumberOfPoints(), is(3));
        assertThat("Data points max", aggregatedStatsSet.getAggregatedStats(0).getMax(), closeTo(19, .01));

        assertThat("Data points start", aggregatedStatsSet.getAggregatedStats(1).getStartX(), closeTo(220, .01));
        assertThat("Data points end", aggregatedStatsSet.getAggregatedStats(1).getEndX(), closeTo(280, .01));
        assertThat("Data points num", aggregatedStatsSet.getAggregatedStats(1).getNumberOfPoints(), is(4));
        assertThat("Data points max", aggregatedStatsSet.getAggregatedStats(1).getMax(), closeTo(280, .01));
    }
}
