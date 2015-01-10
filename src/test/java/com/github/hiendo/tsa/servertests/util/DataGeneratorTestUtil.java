package com.github.hiendo.tsa.servertests.util;

import com.codahale.metrics.graphite.Graphite;
import com.github.hiendo.tsa.servertests.tests.GraphiteImportedTestHelper;
import org.testng.annotations.*;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;


/**
 * Used to generate graphs for manual testing.
 */
@Test(groups = "util")
public class DataGeneratorTestUtil {

    private ExecutorService executorService = Executors.newFixedThreadPool(5);;
    private List<Graphite> graphiteClients = new ArrayList<>();
    private Graphite graphite;

    @BeforeClass(alwaysRun = true)
    public void setup() throws Exception {
        executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            Graphite graphite = new Graphite(new InetSocketAddress("localhost", 2003));
            graphiteClients.add(graphite);
            graphite.connect();
        }
        graphite = graphiteClients.get(0);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() throws Exception {
        if (executorService != null) {
            executorService.shutdown();
        }

        for (Graphite graphite : graphiteClients) {
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
     http://localhost:8888/api/charts/xyline?topic=cpu.server1&title=CPU%20for%20Server%201&xAxisLabel=Date&yAxisLabel=CPU%20Percentage%20Load&connectPoints=true&
     */
    @Test
    public void uploadFakeCpuData() throws Exception {
        String topic = "cpu.server1";
        Random random = new Random();

        long now = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime());
        long incrementCount = TimeUnit.SECONDS.toSeconds(1);
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

        long now = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime());
        long incrementCount = TimeUnit.MINUTES.toSeconds(1);
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
     http://localhost:8888/api/charts/xyline?topic=cpu.server1.large&title=CPU%20for%20Server%201&xAxisLabel=Date&yAxisLabel=CPU%20Load%20(%)&connectPoints=false
     */
    @Test
    public void uploadLargeFakeCpuData() throws Exception {
        final String topic = "cpu.server1.large";
        final Random random = new Random();

        final long now = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime());
        final long secondIncrement = 1;

        final long endDay1 = now + TimeUnit.DAYS.toSeconds(1);
        Future<Void> future1 = executorService.submit(new Callable<Void>() {
            @Override public Void call() throws Exception {

                for ( long time = now; time < endDay1; time += secondIncrement) {
                    double randomValue = 20 + random.nextInt(5) + random.nextDouble();
                    graphiteClients.get(0).send(topic, String.valueOf(randomValue), time);
                }
                return null;
            }
        });

        final long endDay2 = endDay1 + TimeUnit.DAYS.toSeconds(1);
        Future<Void> future2 = executorService.submit(new Callable<Void>() {
            @Override public Void call() throws Exception {

                for ( long time = endDay1; time < endDay2; time += secondIncrement) {
                    double randomValue = 60 + random.nextInt(10) + random.nextDouble();
                    graphiteClients.get(1).send(topic, String.valueOf(randomValue), time);
                }
                return null;
            }
        });

        final long endDay3 = endDay2 + TimeUnit.DAYS.toSeconds(1);
        Future<Void> future3 = executorService.submit(new Callable<Void>() {
            @Override public Void call() throws Exception {
                for (long time = endDay2; time < endDay3; time += secondIncrement) {
                    double randomValue = 30 + random.nextInt(5) + random.nextDouble();
                    graphiteClients.get(2).send(topic, String.valueOf(randomValue), time);
                }
                return null;
            }
        });

        future1.get();
        future2.get();
        future3.get();
    }


    // Aggregated cpu stats every hour (default interval)
    // http://localhost:8888/api/topics/cpu.server1.large/metrics/interval

    // Aggregated cpu stats every 43200 seconds (12 hours)
    // http://localhost:8888/api/topics/cpu.server1.large/metrics/interval?interval=43200

    // Get all data points for cpu stats
    // http://localhost:8888/api/topics/cpu.server1.large/datapoints

    // Box whisker box for CPU stats every 1 day
    // http://localhost:8888/api/charts/boxwhisker?topic=cpu.server1.large&title=CPU%20for%20Server%201&xAxisLabel=Date&yAxisLabel=CPU%20Percentage%20Load&connectPoints=true&interval=86400000
}
