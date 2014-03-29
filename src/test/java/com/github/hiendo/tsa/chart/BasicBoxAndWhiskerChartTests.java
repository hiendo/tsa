package com.github.hiendo.tsa.chart;

import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class BasicBoxAndWhiskerChartTests {

    @Test
    public void canCreateImage() throws Exception {
        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        List<Double> values = new ArrayList<Double>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        dataset.add(values, "hellow", "world");

        values = new ArrayList<Double>();
        values.add(5.0);
        values.add(6.0);
        values.add(7.0);
        dataset.add(values, "hellowwwww", "worlddddd");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        BasicBoxAndWhiskerChart basicBoxAndWhiskerChart = new BasicBoxAndWhiskerChart(new ChartOptions());
        basicBoxAndWhiskerChart.writeChart(byteArrayOutputStream, dataset);

        byte[] image = byteArrayOutputStream.toByteArray();

        assertThat("Image size", image.length, not(0));
        ImageIO.read(new ByteArrayInputStream(image));
    }
}
