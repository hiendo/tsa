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
        cluster = new Cluster.Builder().addContactPoints("localhost").withPort(9142).build();
        session = cluster.connect();

        CQLDataLoader dataLoader = new CQLDataLoader(session);
        dataLoader.load(new ClassPathCQLDataSet("schema.cql"));
    }

    @AfterClass
    public void cleanupDbAfterClass() {
        cluster.close();
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
