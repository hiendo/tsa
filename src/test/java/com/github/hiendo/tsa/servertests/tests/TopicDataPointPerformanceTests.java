
package com.github.hiendo.tsa.servertests.tests;

import com.codahale.metrics.graphite.Graphite;
import com.github.hiendo.tsa.servertests.AbstractServerTests;
import com.github.hiendo.tsa.web.entities.DataPoint;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.testng.Assert.fail;

/**
 *
 */
@Test
public class TopicDataPointPerformanceTests extends AbstractServerTests {
    private static int NUM_POINTS = 500;
    int NUM_CLIENTS = 5;

    private String topic;
    private GraphiteImportedTestHelper graphiteImportedTestHelper;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);;
    private List<Graphite> graphiteClients = new ArrayList<>();

    @BeforeClass(alwaysRun = true)
    public void setup() throws Exception {
        executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            Graphite graphite = new Graphite(new InetSocketAddress("localhost", 2003));
            graphiteClients.add(graphite);
            graphite.connect();
        }
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

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        topic = "topic-" + UUID.randomUUID();
        graphiteImportedTestHelper = new GraphiteImportedTestHelper(topicDataPointOperations);
    }

    @Test
    public void canSendManyPointsUsingHttp() throws Exception {
        for (int i = 0; i < NUM_POINTS; i++) {
            topicDataPointOperations.addData(topic, new DataPoint(i, 4.4));
        }

        graphiteImportedTestHelper.verifyTopicNumberOfPoints(topic, NUM_POINTS);
    }


    @Test
    public void canSendManyPointsUsingGraphite() throws Exception {
        for (int clientIndex = 0; clientIndex < NUM_CLIENTS; clientIndex++) {
            final int clientIndexToUse = clientIndex;

            executorService.submit(new Callable<Void>() {
                @Override public Void call() throws Exception {
                    for (int time = NUM_POINTS * clientIndexToUse; time < NUM_POINTS * (clientIndexToUse + 1); time++) {
                        graphiteClients.get(clientIndexToUse).send(topic, "44444", time);
                    }
                    return null;
                }
            });
        }

        graphiteImportedTestHelper.verifyTopicNumberOfPoints(topic, NUM_POINTS * NUM_CLIENTS);
    }
}
