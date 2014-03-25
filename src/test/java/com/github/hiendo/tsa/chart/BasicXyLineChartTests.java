package com.github.hiendo.tsa.chart;

import com.github.hiendo.tsa.db.DataPointsEntity;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class BasicXyLineChartTests {

    @Test
    public void canCreateImage() throws IOException {
        BasicXyLineChart basicXyLineChart = new BasicXyLineChart(XyChartOptions.newOptions());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        basicXyLineChart.writeChart(byteArrayOutputStream, aDataPoints());

        byte[] image = byteArrayOutputStream.toByteArray();

        assertThat("Image is null", image, notNullValue());
        assertThat("Image size", image.length, not(0));
        ImageIO.read(new ByteArrayInputStream(image));
    }

    private DataPointsEntity aDataPoints() {
        return new DataPointsEntity("topic", new double[]{1, 2, 3}, new double[]{1.1, 2.2, 3.3});
    }
}
