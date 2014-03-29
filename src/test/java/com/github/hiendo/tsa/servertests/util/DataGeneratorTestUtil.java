package com.github.hiendo.tsa.servertests.util;

import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Test(groups = "util")
public class DataGeneratorTestUtil extends AbstractServerTests {

    /**
     http://localhost:9999/api/charts/xyline?topic=curve10&topic=curve11&topic=curve12&topic=curve13&title=Comparing%20Different%20Topics&xAxisLabel=Some%20X%20Values&yAxisLabel=Some%20Metric&xAxisAsDate=false&connectPoints=true&startX=4&endX=10000
     */
    @Test
    public void uploadMultipleDifferentTopics() throws Exception {
        int numPoints = 10000;

        for (int curveNum = 10; curveNum <= 13; curveNum++) {
            for (int i = 0; i < numPoints; i++) {
                long xValue = i;
                double yValue = xValue * curveNum;
                graphite.send("curve" + curveNum, String.valueOf(yValue), xValue);
            }
        }
    }

    /**
     http://localhost:9999/api/charts/xyline?topic=cpu.server1&title=CPU%20for%20Server%201&xAxisLabel=Date&yAxisLabel=CPU%20Percentage%20Load&connectPoints=true
     */
    @Test
    public void uploadFakeCpuData() throws Exception {
        String topic = "cpu.server1";
        Random random = new Random();

        long now = new Date().getTime();
        long incrementCount = TimeUnit.DAYS.toMillis(1)/300;
        long incrementingTime = now;

        for ( int i = 0; i < 50; i++) {
            incrementingTime +=  incrementCount++;
            double randomValue = 20 + random.nextInt(5) + random.nextDouble();
            topicDataPointOperations.addData(topic, new DataPoint(incrementingTime, randomValue));
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
        }

        for ( int i = 0; i < 150; i++) {
            incrementingTime +=  incrementCount++;
            double randomValue = 60 + random.nextInt(10) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
        }

        for ( int i = 0; i < 100; i++) {
            incrementingTime +=  incrementCount++;
            double randomValue = 30 + random.nextInt(5) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
        }
    }

    /**
     http://localhost:9999/api/charts/xyline?topic=mem.server1&title=CPU%20for%20Server%201&xAxisLabel=Date&yAxisLabel=CPU%20Percentage%20Load&connectPoints=true
     */
    @Test
    public void uploadMemData() throws Exception {
        String topic = "mem.server1";
        Random random = new Random();

        long now = new Date().getTime();
        long incrementCount = TimeUnit.DAYS.toMillis(1)/300;
        long incrementingTime = now;

        for ( int i = 0; i < 150; i++) {
            incrementingTime +=  incrementCount++;
            double randomValue = 400 + random.nextInt(50) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
        }

        for ( int i = 0; i < 50; i++) {
            incrementingTime +=  incrementCount++;
            double randomValue = 900 + random.nextInt(100) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
        }

        for ( int i = 0; i < 100; i++) {
            incrementingTime +=  incrementCount++;
            double randomValue = 100 + random.nextInt(20) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
        }
    }


    // Aggregated CPU stats every 1 hours (default interval)
    // http://localhost:9999/api/topics/cpu.server1/aggregator/interval?start=0
}
