package com.github.hiendo.tsa.db;


import com.github.hiendo.tsa.config.CassandraProperties;
import org.apache.cassandra.service.CassandraDaemon;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

public class EmbeddedCassandra {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private CassandraDaemon cassandra;
    private CassandraProperties cassandraProperties;

    public EmbeddedCassandra(CassandraProperties cassandraProperties) {
        this.cassandraProperties = cassandraProperties;
    }

    public synchronized void start() throws IOException {
        //System.setProperty("os.name", "unix"); // bug: https://issues.apache.org/jira/browse/CASSANDRA-8452
        File cassandraHome = new File(cassandraProperties.getCassandraHome());
        if (cassandraProperties.isDeleteCassandraHome() && cassandraHome.exists() && !FileUtils.deleteQuietly(cassandraHome)) {
            throw new RuntimeException("Unable to delete cassandra home " + cassandraProperties.getCassandraHome());
        }

        String cassandraConfigContent = IOUtils.toString(EmbeddedCassandra.class.getClassLoader().
                getResourceAsStream("cassandra-template.yaml"));
        cassandraConfigContent = cassandraConfigContent.replace("$CASSANDRA_HOME$", cassandraHome.getAbsolutePath());

        File cassandraConfigFile = new File(cassandraHome, "cassandra.yaml");
        FileUtils.write(cassandraConfigFile, cassandraConfigContent);

        System.setProperty("cassandra.config", "file:" + cassandraConfigFile.getPath());

        cassandra = new CassandraDaemon();
        Future<Object> future = executorService.submit(new Callable<Object>() {
                                                           @Override
                                                           public Object call() throws Exception {
                                                               try {
                                                                   cassandra.activate();
                                                               } catch (Exception e) {
                                                                   e.printStackTrace();
                                                               }
                                                               return null;
                                                           }
                                                       }
        );

        try {
            future.get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                EmbeddedCassandra.this.stop();
            }
        });
    }

    public synchronized void stop() {
        executorService.shutdownNow();
        if (cassandra != null) {
            cassandra.deactivate();
        }
    }
}
