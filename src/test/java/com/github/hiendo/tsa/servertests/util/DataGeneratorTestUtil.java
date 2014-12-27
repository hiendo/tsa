package com.github.hiendo.tsa.servertests.util;

import com.codahale.metrics.graphite.Graphite;
import com.datastax.driver.core.Session;
import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.annotations.*;

import javax.ws.rs.client.WebTarget;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * Used to generate graphs for manual testing.
 */
@Test(groups = "util")
public class DataGeneratorTestUtil {

    private Graphite graphite;

    @BeforeClass(alwaysRun = true)
    public void startupEmbeddedServer() throws Exception {
        graphite = new Graphite(new InetSocketAddress("localhost", 2003));
        graphite.connect();
    }

    @AfterClass(alwaysRun = true)
    public void shutdownEmbeddedServer() throws Exception {
        if (graphite != null) {
            graphite.close();
        }
    }

    /**
     http://localhost:8888/api/charts/xyline?topic=curve1&topic=curve2&topic=curve3&topic=curve4&topic=curve5&title=Comparing%20Different%20Topics&xAxisLabel=Some%20X%20Values&yAxisLabel=Some%20Metric&xAxisAsDate=false&connectPoints=true&startX=4&endX=10000
     */
    @Test
    public void uploadMultipleDifferentTopics() throws Exception {
        int numPoints = 10000;

        for (int curveNum = 1; curveNum <= 5; curveNum++) {
            for (int i = 0; i < numPoints; i++) {
                long xValue = i;
                double yValue = xValue * curveNum;
                graphite.send("curve" + curveNum, String.valueOf(yValue), xValue);
            }
        }
    }

    /**
     http://localhost:8888/api/charts/xyline?topic=cpu.server1&title=CPU%20for%20Server%201&xAxisLabel=Date&yAxisLabel=CPU%20Percentage%20Load&connectPoints=true
     */
    @Test
    public void uploadFakeCpuData() throws Exception {
        String topic = "cpu.server1";
        Random random = new Random();

        long now = new Date().getTime();
        long incrementCount = TimeUnit.HOURS.toMillis(1);
        long incrementingTime = now;

        for ( int i = 0; i < 50; i++) {
            double randomValue = 20 + random.nextInt(5) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
            incrementingTime +=  incrementCount;
        }

        for ( int i = 0; i < 150; i++) {
            double randomValue = 60 + random.nextInt(10) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
            incrementingTime +=  incrementCount;
        }

        for ( int i = 0; i < 100; i++) {
            double randomValue = 30 + random.nextInt(5) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
            incrementingTime +=  incrementCount;
        }
    }

    /**
     http://localhost:8888/api/charts/xyline?topic=mem.server1&title=Memory%20for%20Server%201&xAxisLabel=Date&yAxisLabel=Memory%20(MB)&connectPoints=true     */
    @Test
    public void uploadMemData() throws Exception {
        String topic = "mem.server1";
        Random random = new Random();

        long now = new Date().getTime();
        long incrementCount = TimeUnit.HOURS.toMillis(1);
        long incrementingTime = now;

        for ( int i = 0; i < 150; i++) {
            double randomValue = 400 + random.nextInt(50) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
            incrementingTime +=  incrementCount;
        }

        for ( int i = 0; i < 50; i++) {
            double randomValue = 900 + random.nextInt(100) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
            incrementingTime +=  incrementCount;
        }

        for ( int i = 0; i < 100; i++) {
            double randomValue = 100 + random.nextInt(20) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
            incrementingTime +=  incrementCount;
        }
    }

    /**
     http://localhost:8888/api/charts/xyline?topic=cpu.server1.large&title=CPU%20for%20Server%201&xAxisLabel=Date&yAxisLabel=CPU%20Load%20(%)&connectPoints=false%20
     */
    @Test
    public void uploadLargeFakeCpuData() throws Exception {
        String topic = "cpu.server1.large";
        Random random = new Random();

        long now = new Date().getTime();
        long incrementCount = TimeUnit.SECONDS.toMillis(1);
        long incrementingTime = now;

        for ( int i = 0; i < TimeUnit.DAYS.toSeconds(1); i++) {
            double randomValue = 20 + random.nextInt(5) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
            incrementingTime +=  incrementCount;
        }

        for ( int i = 0; i < TimeUnit.DAYS.toSeconds(1); i++) {
            double randomValue = 60 + random.nextInt(10) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
            incrementingTime +=  incrementCount;
        }

        for ( int i = 0; i < TimeUnit.DAYS.toSeconds(1); i++) {
            double randomValue = 30 + random.nextInt(5) + random.nextDouble();
            graphite.send(topic, String.valueOf(randomValue), incrementingTime);
            incrementingTime +=  incrementCount;
        }
    }


    // Aggregated cpu stats every hour (default interval)
    // http://localhost:8888/api/topics/cpu.server1.large/metrics/interval

    // Aggregated cpu stats every min
    // http://localhost:8888/api/topics/cpu.server1.large/metrics/interval?interval=60000

    // Get all data points for cpu stats
    // http://localhost:8888/api/topics/cpu.server1.large/datapoints

    // Box whisker box for CPU stats every 1 day
    // http://localhost:8888/api/charts/boxwhisker?topic=cpu.server1.large&title=CPU%20for%20Server%201&xAxisLabel=Date&yAxisLabel=CPU%20Percentage%20Load&connectPoints=true&interval=86400000
}
