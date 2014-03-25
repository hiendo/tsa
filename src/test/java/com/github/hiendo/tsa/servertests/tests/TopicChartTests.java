package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.fail;


@Test
public class TopicChartTests extends AbstractServerTests {

    @Test
    public void canDownloadXyLineChart() throws Exception {
        String topic = "topic-" + UUID.randomUUID();

        topicDataPointOperations.addData(topic, new DataPoint(44444, 4.4));
        topicDataPointOperations.addData(topic, new DataPoint(33333, 3.3));

        byte[] image = topicChartOperations.downloadDefaultLineChart(topic);

        verifyImageIsValid(image);
    }

    @Test
    public void canDowloadChartWhenTopicHasNoDataYet() throws Exception {
        String topic = "topic-" + UUID.randomUUID();

        byte[] image = topicChartOperations.downloadDefaultLineChart(topic);

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
