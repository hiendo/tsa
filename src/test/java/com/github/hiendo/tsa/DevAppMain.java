package com.github.hiendo.tsa;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.github.hiendo.tsa.config.AppConfiguration;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.boot.SpringApplication;


public class DevAppMain {

    public static void main(String[] args) throws Exception {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        Cluster cluster = new Cluster.Builder().addContactPoints("localhost").withPort(9142).build();
        Session session = cluster.connect();
        CQLDataLoader dataLoader = new CQLDataLoader(session);
        dataLoader.load(new ClassPathCQLDataSet("schema.cql"));

        SpringApplication app = new SpringApplication(AppConfiguration.class);
        app.run(new String[]{"--debug"});
    }
}
