package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import com.github.hiendo.tsa.web.entities.DataPoints;
import org.joda.time.MutableDateTime;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.testng.Assert.fail;


@Test
public class TimeSeriesTopicTests extends AbstractServerTests {

    @Test
    public void canAddDataPoint() throws Exception {
        long now = MutableDateTime.now().toDate().getTime();
        String topic = "topic-" + UUID.randomUUID();

        timeSeriesTopicOperations.addData(topic, new DataPoint(44444, 4.4));
        timeSeriesTopicOperations.addData(topic, new DataPoint(33333, 3.3));
        timeSeriesTopicOperations.addDataOccuringNow(topic, 5.5);

        DataPoints dataPoints = timeSeriesTopicOperations.getDataForTopic(topic);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), equalTo(3));
        assertThat("Data points time", dataPoints.getTimeAt(0), equalTo(33333l));
        assertThat("Data points time", dataPoints.getTimeAt(1), equalTo(44444l));
        assertThat("Data points time", dataPoints.getTimeAt(2), allOf(greaterThan(now - 5000), lessThan(now + 5000)));
        assertThat("Data points value", dataPoints.getValueAt(0), closeTo(3.3, .01));
        assertThat("Data points value", dataPoints.getValueAt(1), closeTo(4.4, .01));
        assertThat("Data points value", dataPoints.getValueAt(2), closeTo(5.5, .01));
    }

    @Test
    public void canGetDataPointsWhenTopicHasNoDataYet() throws Exception {
        String topic = "topic-" + UUID.randomUUID();

        DataPoints dataPoints = timeSeriesTopicOperations.getDataForTopic(topic);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), equalTo(0));
    }

    @Test
    public void canDowloadGraphImage() throws Exception {
        String topic = "topic-" + UUID.randomUUID();

        timeSeriesTopicOperations.addDataOccuringNow(topic, 3.3);
        timeSeriesTopicOperations.addDataOccuringNow(topic, 4.4);

        byte[] image = timeSeriesTopicOperations.downloadGraph(topic);

        verifyImageIsValid(image);
    }

    @Test
    public void canDowloadChartWhenTopicHasNoDataYet() throws Exception {
        String topic = "topic-" + UUID.randomUUID();

        byte[] image = timeSeriesTopicOperations.downloadGraph(topic);

        verifyImageIsValid(image);
    }

    private void verifyImageIsValid(byte[] image) {
        assertThat("Image", image, notNullValue());
        assertThat("Image size", image.length, not(0));
        try {
            ImageIO.read(new ByteArrayInputStream(image));
        } catch (Exception e) {
            fail("Downloaded file is not an image.");
        }
    }
}
