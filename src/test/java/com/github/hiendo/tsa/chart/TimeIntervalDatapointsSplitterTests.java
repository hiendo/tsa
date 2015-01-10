package com.github.hiendo.tsa.chart;

import com.github.hiendo.tsa.db.DataPointsEntity;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TimeIntervalDatapointsSplitterTests {

    @Test
    public void canAggregateStatsInTime() throws IOException {
        TimeIntervalDatapointsSplitter timeIntervalDatapointsSplitter = new TimeIntervalDatapointsSplitter();

        DataPointsEntity dataPointsEntity =
                new DataPointsEntity("topic", new long[]{0, 40, 120, 140, 160, 200, 210, 220, 230},
                        new double[]{1, 41, 121, 141, 161, 201, 211, 221, 231});

        DataPointsSet timeIntervalDataPointsSplitterResult =
                timeIntervalDatapointsSplitter.splitDatapoints(dataPointsEntity, 0l, 100.0);

        assertThat("stats set", timeIntervalDataPointsSplitterResult, notNullValue());
        assertThat("stats set size", timeIntervalDataPointsSplitterResult.getSize(), is(3));
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(0), matchingFirstSet());
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(1), matchingSecondSet());
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(2), matchingThirdSet());
    }

    @Test
    public void canAggregateStatsInTimeEvenWhenStartValueIsMoreThanIntervalTimeLowerThanFirstValue() throws IOException {
        TimeIntervalDatapointsSplitter timeIntervalDatapointsSplitter = new TimeIntervalDatapointsSplitter();

        DataPointsEntity dataPointsEntity =
                new DataPointsEntity("topic", new long[]{0, 40, 120, 140, 160, 200, 210, 220, 230},
                        new double[]{1, 41, 121, 141, 161, 201, 211, 221, 231});

        DataPointsSet timeIntervalDataPointsSplitterResult =
                timeIntervalDatapointsSplitter.splitDatapoints(dataPointsEntity, -10000l, 100.0);

        assertThat("stats set", timeIntervalDataPointsSplitterResult, notNullValue());
        assertThat("stats set size", timeIntervalDataPointsSplitterResult.getSize(), is(3));
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(0), matchingFirstSet());
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(1), matchingSecondSet());
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(2), matchingThirdSet());
    }

    @Test
    public void canAggregateStatsInTimeEvenWhenStartValueIsWithinAnIntervalTimeLowerThanFirstValue() throws IOException {
        TimeIntervalDatapointsSplitter timeIntervalDatapointsSplitter = new TimeIntervalDatapointsSplitter();

        DataPointsEntity dataPointsEntity =
                new DataPointsEntity("topic", new long[]{0, 40, 120, 140, 160, 200, 210, 220, 230},
                        new double[]{1, 41, 121, 141, 161, 201, 211, 221, 231});

        DataPointsSet timeIntervalDataPointsSplitterResult =
                timeIntervalDatapointsSplitter.splitDatapoints(dataPointsEntity, -1l, 100.0);

        assertThat("stats set", timeIntervalDataPointsSplitterResult, notNullValue());
        assertThat("stats set size", timeIntervalDataPointsSplitterResult.getSize(), is(3));
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(0), matchingFirstSet());
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(1), matchingSecondSet());
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(2), matchingThirdSet());
    }

    @Test
    public void canAggregateStatsInTimeWhenStartValueIsInMiddleOfRange() throws IOException {
        double interval = 100.0;
        TimeIntervalDatapointsSplitter timeIntervalDatapointsSplitter = new TimeIntervalDatapointsSplitter();

        DataPointsEntity dataPointsEntity =
                new DataPointsEntity("topic", new long[]{0, 40, 120, 140, 160, 200, 210, 220, 230},
                        new double[]{1, 41, 121, 141, 161, 201, 211, 221, 231});

        DataPointsSet timeIntervalDataPointsSplitterResult =
                timeIntervalDatapointsSplitter.splitDatapoints(dataPointsEntity, 30l, interval);

        assertThat("stats set", timeIntervalDataPointsSplitterResult, notNullValue());
        assertThat("stats set size", timeIntervalDataPointsSplitterResult.getSize(), is(3));

        int expectedXValue = 30;
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(0),
                hasXvalueBetween(expectedXValue, expectedXValue += interval));
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(1),
                hasXvalueBetween(expectedXValue, expectedXValue += interval));
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(2),
                hasXvalueBetween(expectedXValue, expectedXValue += interval));
    }

    @Test
    public void returnEmptyAggregateStatsThereAreNoDatapoints() throws IOException {
        TimeIntervalDatapointsSplitter timeIntervalDatapointsSplitter = new TimeIntervalDatapointsSplitter();

        DataPointsEntity dataPointsEntity = new DataPointsEntity("topic", new long[]{}, new double[]{});

        DataPointsSet timeIntervalDataPointsSplitterResult =
                timeIntervalDatapointsSplitter.splitDatapoints(dataPointsEntity, 0l, 100.0);

        assertThat("stats set", timeIntervalDataPointsSplitterResult, notNullValue());
        assertThat("stats set size", timeIntervalDataPointsSplitterResult.getSize(), is(0));
    }


    private TypeSafeMatcher<DataPointsEntity> hasXvalueBetween(final int min, final int max) {
        return new TypeSafeMatcher<DataPointsEntity>() {
            @Override
            protected boolean matchesSafely(DataPointsEntity item) {
                for (double value : item.getTimestamps()) {
                    if (value < min || value > max) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    private TypeSafeMatcher<DataPointsEntity> matchingFirstSet() {
        return new TypeSafeMatcher<DataPointsEntity>() {
            @Override
            protected boolean matchesSafely(DataPointsEntity item) {
                return item.getTimestamp(0) == 0 && item.getTimestamp(1) == 40 &&
                        item.getValue(0) == 1 && item.getValue(1) == 41;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    private TypeSafeMatcher<DataPointsEntity> matchingSecondSet() {
        return new TypeSafeMatcher<DataPointsEntity>() {
            @Override
            protected boolean matchesSafely(DataPointsEntity item) {
                return item.getTimestamp(0) == 120 && item.getTimestamp(1) == 140 && item.getTimestamp(2) == 160
                        && item.getValue(0) == 121 && item.getValue(1) == 141 && item.getValue(2) == 161;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    private TypeSafeMatcher<DataPointsEntity> matchingThirdSet() {
        return new TypeSafeMatcher<DataPointsEntity>() {
            @Override
            protected boolean matchesSafely(DataPointsEntity item) {
                return item.getTimestamp(0) == 200 && item.getTimestamp(1) == 210 && item.getTimestamp(
                        2) == 220 && item.getTimestamp(3) == 230 &&
                        item.getValue(0) == 201 && item.getValue(1) == 211 && item.getValue(2) == 221 && item.getValue(
                        3) == 231;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
