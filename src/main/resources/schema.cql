CREATE KEYSPACE IF NOT EXISTS tsa WITH replication={'class' : 'SimpleStrategy', 'replication_factor':1};
USE tsa;

CREATE TABLE IF NOT EXISTS datapoints (
    topic text,
    xValue double,
    yValue double,
    PRIMARY KEY(topic, xValue));
