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
     * http://localhost:9999/rest/topic/cpu.server1/chart?title=CPU%20for%20Server%201&xAxisLabel=Date&yAxisLabel=CPU%20Percentage%20Load&connectPoints=true
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
            timeSeriesTopicOperations.addData(topic, new DataPoint(incrementingTime, randomValue));
        }

        for ( int i = 0; i < 150; i++) {
            incrementingTime +=  incrementCount++;
            double randomValue = 60 + random.nextInt(10) + random.nextDouble();
            timeSeriesTopicOperations.addData(topic, new DataPoint(incrementingTime, randomValue));
        }

        for ( int i = 0; i < 100; i++) {
            incrementingTime +=  incrementCount++;
            double randomValue = 30 + random.nextInt(5) + random.nextDouble();
            timeSeriesTopicOperations.addData(topic, new DataPoint(incrementingTime, randomValue));
        }
    }

    /**
     * http://localhost:9999/rest/topic/mem.server1/chart?title=Memory%20for%20Server%201&xAxisLabel=Date&yAxisLabel=Memory%20(MB)&connectPoints=true
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
            timeSeriesTopicOperations.addData(topic, new DataPoint(incrementingTime, randomValue));
        }

        for ( int i = 0; i < 50; i++) {
            incrementingTime +=  incrementCount++;
            double randomValue = 900 + random.nextInt(100) + random.nextDouble();
            timeSeriesTopicOperations.addData(topic, new DataPoint(incrementingTime, randomValue));
        }

        for ( int i = 0; i < 100; i++) {
            incrementingTime +=  incrementCount++;
            double randomValue = 100 + random.nextInt(20) + random.nextDouble();
            timeSeriesTopicOperations.addData(topic, new DataPoint(incrementingTime, randomValue));
        }
    }

    /**
     * http://localhost:9999/rest/topic/alotOfPoints/chart?title=Alot%20Of%20Points&xAxisLabel=Date&yAxisLabel=Some%20Metric&connectPoints=false
     * @throws Exception
     */
    @Test
    public void uploadAlotOfDataPoints() throws Exception {
        int numPoints = 10000;
        String topic = "alotOfPoints";
        Random random = new Random();

        long now = new Date().getTime();
        long incrementCount = TimeUnit.DAYS.toMillis(1)/numPoints;
        long incrementingTime = now;

        for ( int i = 0; i < numPoints; i++) {
            incrementingTime +=  incrementCount++;
            double randomValue = 20 + random.nextInt(10) + random.nextDouble();
            timeSeriesTopicOperations.addData(topic, new DataPoint(incrementingTime, randomValue));
        }
    }
}
