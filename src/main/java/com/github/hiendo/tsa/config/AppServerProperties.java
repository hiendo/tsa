package com.github.hiendo.tsa.config;

import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Server properties such as port number.
 */
@ConfigurationProperties(prefix = "app.server")
public class AppServerProperties {

    @Range(min = 0, max = 65535)
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getBaseUrl() {
        return "http://localhost:" + getPort();
    }
}