package com.github.hiendo.tsa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Min;

/**
 * Cassandra Server properties such location of where to store the DB data.
 */
@ConfigurationProperties(prefix = "cassandra")
public class CassandraProperties {

    // Delete cassandra's home folder on application load
    private boolean deleteCassandraHome = false;

    // Location of the Cassandra's home to store the data
    private String cassandraHome = System.getProperty("user.home") + "/cassandra";

    // Number of days to keep data.  -1 means to never delete the data.
    @Min(-1)
    private int numberOfDaysToKeepData = 7;

    public boolean isDeleteCassandraHome() {
        return deleteCassandraHome;
    }

    public void setDeleteCassandraHome(boolean deleteCassandraHome) {
        this.deleteCassandraHome = deleteCassandraHome;
    }

    public String getCassandraHome() {
        return cassandraHome;
    }

    public void setCassandraHome(String cassandraHome) {
        this.cassandraHome = cassandraHome;
    }

    public int getNumberOfDaysToKeepData() {
        return numberOfDaysToKeepData;
    }

    public void setNumberOfDaysToKeepData(int numberOfDaysToKeepData) {
        this.numberOfDaysToKeepData = numberOfDaysToKeepData;
    }
}