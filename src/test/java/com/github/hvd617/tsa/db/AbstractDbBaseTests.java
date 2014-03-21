package com.github.hvd617.tsa.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://www.datastax.com/documentation/developer/java-driver/2.0/java-driver/quick_start/qsSimpleClientAddSession_t.html
public class AbstractDbBaseTests {
    protected String keyspace = "tsa";

    protected Session session;
    protected Cluster cluster;

    @BeforeClass
    public void setupDbBeforeClass() throws Exception {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        cluster = new Cluster.Builder().addContactPoints("localhost").withPort(9142).build();

        CQLDataLoader dataLoader = new CQLDataLoader("localhost", 9142);
        dataLoader.load(new ClassPathCQLDataSet("schema.cql"));
        session = cluster.connect(keyspace);
    }

    @AfterClass
    public void cleanupDbAfterClass() {
        cluster.shutdown();
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
