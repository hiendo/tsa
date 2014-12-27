package com.github.hiendo.tsa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cassandra Server properties such location of where to store the DB data.
 */
@ConfigurationProperties(prefix = "cassandra")
public class CassandraProperties {

    private boolean deleteData = false;
    private String cassandraHome = System.getProperty("user.home") + "/cassandra";

    public boolean isDeleteData() {
        return deleteData;
    }

    public void setDeleteData(boolean deleteData) {
        this.deleteData = deleteData;
    }

    public String getCassandraHome() {
        return cassandraHome;
    }

    public void setCassandraHome(String cassandraHome) {
        this.cassandraHome = cassandraHome;
    }
}