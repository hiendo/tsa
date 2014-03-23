package com.github.hiendo.tsa.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://www.datastax.com/documentation/developer/java-driver/2.0/java-driver/quick_start/qsSimpleClientAddSession_t.html
public class AbstractDbBaseTests {
    protected String keyspace = "tsa";

    protected Session session;
    protected Cluster cluster;

    @BeforeClass
    public void setupDbBeforeClass() throws Exception {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra("embeddedDbTestCassandraConfig.yml");
        CQLDataLoader dataLoader = new CQLDataLoader("localhost", 9143);
        dataLoader.load(new ClassPathCQLDataSet("schema.cql"));

        cluster = new Cluster.Builder().addContactPoints("localhost").withPort(9143).build();
        session = cluster.connect(keyspace);
    }

    @AfterClass
    public void cleanupDbAfterClass() {
        cluster.shutdown();
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
