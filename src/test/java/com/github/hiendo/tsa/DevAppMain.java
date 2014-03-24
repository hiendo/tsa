package com.github.hiendo.tsa;

import com.github.hiendo.tsa.config.AppConfiguration;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.boot.SpringApplication;


public class DevAppMain {

    public static void main(String[] args) throws Exception {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        CQLDataLoader dataLoader = new CQLDataLoader("localhost", 9142);
        dataLoader.load(new ClassPathCQLDataSet("schema.cql"));

        SpringApplication app = new SpringApplication(AppConfiguration.class);
        app.run(new String[]{"--debug"});
    }
}
