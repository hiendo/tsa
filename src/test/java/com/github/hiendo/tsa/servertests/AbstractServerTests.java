package com.github.hiendo.tsa.servertests;

import com.codahale.metrics.graphite.Graphite;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.github.hiendo.tsa.config.AppConfiguration;
import com.github.hiendo.tsa.config.AppServerProperties;
import com.github.hiendo.tsa.servertests.operations.*;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.bind.PropertySourcesPropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AbstractServerTests {
    private static final boolean RUN_EMBEDDED_SERVER = true;

    protected static ConfigurableApplicationContext context;

    private AppServerProperties appServerProperties;

    protected static StaticFileOperations staticFileOperations;
    protected static TopicDataPointOperations topicDataPointOperations;
    protected static TopicChartOperations topicChartOperations;
    protected static MetricsIntervalOperations metricsIntervalOperations;

    protected static Session cassandraSession;
    protected static Graphite graphite;
    private Cluster cluster;

    @BeforeSuite
    public void startupEmbeddedServer() throws Exception {
        loadTestProperties();
        WebTarget webTarget = setupClient(appServerProperties.getBaseUrl());
        setupOperationClasses(webTarget);

        if (RUN_EMBEDDED_SERVER) {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra();
            cluster = new Cluster.Builder().addContactPoints("localhost").withPort(9142).build();
            Session session = cluster.connect();
            CQLDataLoader dataLoader = new CQLDataLoader(session);
            dataLoader.load(new ClassPathCQLDataSet("schema.cql"));

            Future<ConfigurableApplicationContext> startupFuture = startupServer();
            context = startupFuture.get(30, TimeUnit.SECONDS);
            cassandraSession = context.getBean(Session.class);
        }

        graphite = new Graphite(new InetSocketAddress("localhost", 2003));
        graphite.connect();
    }

    @AfterSuite
    public void shutdownEmbeddedServer() throws Exception {
        if (context != null) {
            context.close();
        }

        if (graphite != null) {
            graphite.close();
        }

        if (cluster != null) {
            cluster.close();
        }
    }

    private void loadTestProperties() throws Exception {
        MutablePropertySources mutablePropertySources = PropertySourcesFactory.createPropertySources();

        appServerProperties = new AppServerProperties();
        RelaxedDataBinder propertiesBinder = new RelaxedDataBinder(appServerProperties, "app.server");
        propertiesBinder.bind(new PropertySourcesPropertyValues(mutablePropertySources));
    }

    private void setupOperationClasses(WebTarget webTarget) throws Exception {
        staticFileOperations = new StaticFileOperations(webTarget);
        topicDataPointOperations = new TopicDataPointOperations(webTarget);
        topicChartOperations = new TopicChartOperations(webTarget);
        metricsIntervalOperations = new MetricsIntervalOperations(webTarget);
    }

    private Future<ConfigurableApplicationContext> startupServer() throws Exception {
        Future<ConfigurableApplicationContext> future =
                Executors.newSingleThreadExecutor().submit(new Callable<ConfigurableApplicationContext>() {
                    @Override
                    public ConfigurableApplicationContext call() throws Exception {
                        return SpringApplication.run(AppConfiguration.class);
                    }
                });

        return future;
    }

    private WebTarget setupClient(String serverBaseUrl) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.property(ClientProperties.ASYNC_THREADPOOL_SIZE, "5");
        Client client = ClientBuilder.newClient(clientConfig).register(JacksonFeature.class);
        return client.target(serverBaseUrl);
    }
}
