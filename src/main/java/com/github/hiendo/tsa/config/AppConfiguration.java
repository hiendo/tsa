package com.github.hiendo.tsa.config;

import org.apache.catalina.Context;
import org.apache.log4j.Logger;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.JarScannerCallback;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.MongoTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.DeviceResolverAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.reactor.ReactorAutoConfiguration;
import org.springframework.boot.autoconfigure.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.ServletContext;
import java.util.Set;


@Configuration
@ComponentScan(basePackages = "com.github.hiendo")
@EnableAutoConfiguration(
        exclude = {RabbitAutoConfiguration.class, AopAutoConfiguration.class, BatchAutoConfiguration.class,
                JpaRepositoriesAutoConfiguration.class, MongoAutoConfiguration.class,
                MongoTemplateAutoConfiguration.class, DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class, JmsTemplateAutoConfiguration.class,
                JmxAutoConfiguration.class, DeviceResolverAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
                ReactorAutoConfiguration.class, RedisAutoConfiguration.class, SecurityAutoConfiguration.class,
                ThymeleafAutoConfiguration.class, EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class,
                EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class, MultipartAutoConfiguration.class})
@EnableConfigurationProperties({AppServerProperties.class})
@EnableWebSocket
public class AppConfiguration implements WebSocketConfigurer {

    private static final Logger logger = Logger.getLogger(AppConfiguration.class);

    @Autowired
    private AppServerProperties appServerProperties;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(testwebsocket(), "/testwebsocket")
                .withSockJS();
    }

    @Bean
   	public WebSocketHandler testwebsocket() {
   		return new TextWebSocketHandler();
   	}

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory embeddedServletContainerFactory =
                new TomcatEmbeddedServletContainerFactory(appServerProperties.getPort());

        embeddedServletContainerFactory.addContextCustomizers(new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                context.setJarScanner(new JarScanner() {
                    @Override
                    public void scan(ServletContext context, ClassLoader classloader, JarScannerCallback callback,
                            Set<String> jarsToSkip) {
                        // Don't do any tomcat's jar scanning to make startup a little bit faster
                    }
                });
            }
        });

        return embeddedServletContainerFactory;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages("com.github.hiendo");
        resourceConfig.register(JacksonFeature.class);
        ServletContainer servletContainer = new org.glassfish.jersey.servlet.ServletContainer(resourceConfig);
        return new ServletRegistrationBean(servletContainer,"/rest/*");
    }
}
