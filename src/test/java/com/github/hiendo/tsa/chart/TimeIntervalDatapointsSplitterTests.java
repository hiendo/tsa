package com.github.hiendo.tsa.chart;

import com.github.hiendo.tsa.db.DataPointsEntity;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TimeIntervalDatapointsSplitterTests {

    @Test
    public void canAggregateStatsInTime() throws IOException {
        TimeIntervalDatapointsSplitter timeIntervalDatapointsSplitter = new TimeIntervalDatapointsSplitter();

        DataPointsEntity dataPointsEntity =
                new DataPointsEntity("topic", new double[]{0, 40, 120, 140, 160, 200, 210, 220, 230},
                        new double[]{1, 41, 121, 141, 161, 201, 211, 221, 231});

        DataPointsSet timeIntervalDataPointsSplitterResult =
                timeIntervalDatapointsSplitter.splitDatapoints(dataPointsEntity, 0, 100);

        assertThat("stats set", timeIntervalDataPointsSplitterResult, notNullValue());
        assertThat("stats set size", timeIntervalDataPointsSplitterResult.getSize(), is(3));
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(0), matchingFirstSet());
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(1), matchingSecondSet());
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(2), matchingThirdSet());
    }

    @Test
    public void canAggregateStatsInTimeEvenWhenStartValueIsReallyLow() throws IOException {
        TimeIntervalDatapointsSplitter timeIntervalDatapointsSplitter = new TimeIntervalDatapointsSplitter();

        DataPointsEntity dataPointsEntity =
                new DataPointsEntity("topic", new double[]{0, 40, 120, 140, 160, 200, 210, 220, 230},
                        new double[]{1, 41, 121, 141, 161, 201, 211, 221, 231});

        DataPointsSet timeIntervalDataPointsSplitterResult =
                timeIntervalDatapointsSplitter.splitDatapoints(dataPointsEntity, -10000, 100);

        assertThat("stats set", timeIntervalDataPointsSplitterResult, notNullValue());
        assertThat("stats set size", timeIntervalDataPointsSplitterResult.getSize(), is(3));
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(0), matchingFirstSet());
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(1), matchingSecondSet());
        assertThat("stats set", timeIntervalDataPointsSplitterResult.getDataPoints(2), matchingThirdSet());
    }


    private TypeSafeMatcher<DataPointsEntity> matchingFirstSet() {
        return new TypeSafeMatcher<DataPointsEntity>() {
            @Override
            protected boolean matchesSafely(DataPointsEntity item) {
                return item.getX(0) == 0 && item.getX(1) == 40 &&
                        item.getY(0) == 1 && item.getY(1) == 41;
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
                return item.getX(0) == 120 && item.getX(1) == 140 && item.getX(2) == 160
                        && item.getY(0) == 121 && item.getY(1) == 141 && item.getY(2) == 161;
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
                return item.getX(0) == 200 && item.getX(1) == 210 && item.getX(2) == 220 && item.getX(3) == 230 &&
                       item.getY(0) == 201 && item.getY(1) == 211 && item.getY(2) == 221 && item.getY(3) == 231;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
