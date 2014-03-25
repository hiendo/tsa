package com.github.hiendo.tsa.servertests;

import com.datastax.driver.core.Session;
import com.github.hiendo.tsa.config.AppConfiguration;
import com.github.hiendo.tsa.config.AppServerProperties;
import com.github.hiendo.tsa.servertests.operations.TopicChartOperations;
import com.github.hiendo.tsa.servertests.operations.TopicDataPointOperations;
import com.github.hiendo.tsa.servertests.operations.RestTestOperations;
import com.github.hiendo.tsa.servertests.operations.StaticFileOperations;
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
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AbstractServerTests {
    private static final boolean RUN_EMBEDDED_SERVER = false;

    protected static ConfigurableApplicationContext context;

    private AppServerProperties appServerProperties;

    protected static StaticFileOperations staticFileOperations;
    protected static RestTestOperations restTestOperations;
    protected static TopicDataPointOperations topicDataPointOperations;
    protected static TopicChartOperations topicChartOperations;

    protected static Session cassandraSession;

    @BeforeSuite
	public void startupEmbeddedServer() throws Exception {
        loadTestProperties();
        WebTarget webTarget = setupClient(appServerProperties.getBaseUrl());
        setupOperationClasses(webTarget);

        if (RUN_EMBEDDED_SERVER) {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra();
            CQLDataLoader dataLoader = new CQLDataLoader("localhost", 9142);
            dataLoader.load(new ClassPathCQLDataSet("schema.cql"));

            Future<ConfigurableApplicationContext> startupFuture = startupServer();
            context = startupFuture.get(30, TimeUnit.SECONDS);
            cassandraSession = context.getBean(Session.class);
        }
    }

    @AfterSuite
	public void shutdownEmbeddedServer() throws Exception {
        if (context != null) {
            context.close();
        }
    }

    private void loadTestProperties() throws Exception {
        MutablePropertySources mutablePropertySources = PropertySourcesFactory.createPropertySources();

        appServerProperties = new AppServerProperties();
        RelaxedDataBinder propertiesBinder = new RelaxedDataBinder(appServerProperties, "app.server");
        propertiesBinder.bind(new PropertySourcesPropertyValues(mutablePropertySources));
    }

    private void setupOperationClasses(WebTarget webTarget) {
        staticFileOperations = new StaticFileOperations(webTarget);
        restTestOperations = new RestTestOperations(webTarget);
        topicDataPointOperations = new TopicDataPointOperations(webTarget);
        topicChartOperations = new TopicChartOperations(webTarget);
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
