package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoints;
import org.joda.time.MutableDateTime;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.testng.Assert.fail;


@Test
public class TimeSeriesTopicTests extends AbstractServerTests {

    @Test
    public void canAddDataPoint() throws Exception {
        long now = MutableDateTime.now().toDate().getTime();
        String topic = "topic-" + UUID.randomUUID();

        timeSeriesTopicOperations.addData(topic, 3.14);
        timeSeriesTopicOperations.addData(topic, 3.15);

        DataPoints dataPoints = timeSeriesTopicOperations.getDataForTopic(topic);

        assertThat("Data points", dataPoints, notNullValue());
        assertThat("Data points size", dataPoints.size(), is(2));
        assertThat("Data points time", dataPoints.getTimeAt(0), allOf(greaterThan(now - 5000), lessThan(now + 5000)));
        assertThat("Data points time", dataPoints.getTimeAt(1), allOf(greaterThan(now - 5000), lessThan(now + 5000)));
        assertThat("Data points value", dataPoints.getValueAt(0), closeTo(3.14, .01));
        assertThat("Data points value", dataPoints.getValueAt(1), closeTo(3.15, .01));
    }

    @Test
    public void canDowloadGraphImage() throws Exception {
        String topic = "topic-" + UUID.randomUUID();

        timeSeriesTopicOperations.addData(topic, 3.14);
        timeSeriesTopicOperations.addData(topic, 3.15);

        byte[] image = timeSeriesTopicOperations.downloadGraph(topic);

        assertThat("Image", image, notNullValue());
        assertThat("Image size", image.length, not(0));
        try {
            ImageIO.read(new ByteArrayInputStream(image));
        } catch (Exception e) {
            fail("Downloaded file is not an image.");
        }

    }
}
