package com.github.hiendo.tsa.servertests;

import com.github.hiendo.tsa.config.AppConfiguration;
import com.github.hiendo.tsa.config.AppServerProperties;
import com.github.hiendo.tsa.servertests.operations.StaticFileOperations;
import com.github.hiendo.tsa.servertests.operations.TestControllerOperations;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;
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
    protected static ConfigurableApplicationContext context;

    private AppServerProperties appServerProperties;

    protected static StaticFileOperations staticFileOperations;
    protected static TestControllerOperations testControllerOperations;

    @BeforeSuite
	public void startupEmbeddedServer() throws Exception {
        loadProperties();

        String serverBaseUrl = "http://localhost:" + appServerProperties.getPort();

        Future<ConfigurableApplicationContext> startupFuture = startupServer();
        WebTarget webTarget = setupClient(serverBaseUrl);
        setupOperationClasses(webTarget);

        context = startupFuture.get(30, TimeUnit.SECONDS);
    }

    @AfterSuite
	public void shutdownEmbeddedServer() throws Exception {
        if (context != null) {
            context.close();
        }
    }

    private void loadProperties() throws Exception {
        MutablePropertySources mutablePropertySources = PropertySourcesFactory.createPropertySources();

        appServerProperties = new AppServerProperties();
        RelaxedDataBinder propertiesBinder = new RelaxedDataBinder(appServerProperties, "app.server");
        propertiesBinder.bind(new PropertySourcesPropertyValues(mutablePropertySources));
    }

    private void setupOperationClasses(WebTarget webTarget) {
        staticFileOperations = new StaticFileOperations(webTarget);
        testControllerOperations = new TestControllerOperations(webTarget);
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
        clientConfig.connectorProvider(new GrizzlyConnectorProvider());
        clientConfig.property(ClientProperties.ASYNC_THREADPOOL_SIZE, "5");
        Client client = ClientBuilder.newClient(clientConfig).register(JacksonFeature.class);
        return client.target(serverBaseUrl);
    }
}
