package com.github.hiendo.tsa.db;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

@Test
public class TimeSeriesRepositoryTests extends AbstractDbBaseTests {

    private TimeSeriesRepository timeSeriesRepository;

    @BeforeClass
    public void beforeClass() {
        timeSeriesRepository = new TimeSeriesRepository(session);
    }

    @Test
    public void canInsertTimeSeriesData() throws Exception {
        TimeSeriesRepository timeSeriesRepository = new TimeSeriesRepository(session);
        long now = new Date().getTime();

        timeSeriesRepository.saveTime("topic", 2.54);
        timeSeriesRepository.saveTime("topic", 2.55);

        DataPoints dataPoints = timeSeriesRepository.getAllDataPointsForTopic("topic");
        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), is(2));
        assertThat("Data points time", dataPoints.getTimeAt(0), allOf(greaterThan(now - 100), lessThan(now + 100)));
        assertThat("Data points time", dataPoints.getTimeAt(1), allOf(greaterThan(now - 100), lessThan(now + 100)));
        assertThat("Data points value", dataPoints.getValueAt(0), closeTo(2.54, .01));
        assertThat("Data points value", dataPoints.getValueAt(1), closeTo(2.55, .01));
    }

    @Test
    public void canGetTimeSeriesForTopicWithNoData() throws Exception {
        DataPoints dataPoints = timeSeriesRepository.getAllDataPointsForTopic("non.existing.topic");

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), is(0));
    }
}
