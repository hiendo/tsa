CREATE KEYSPACE IF NOT EXISTS tsa WITH replication={'class' : 'SimpleStrategy', 'replication_factor':1};
USE tsa;

CREATE TABLE IF NOT EXISTS timeseries(
    topic text,
    time timestamp,
    value double,
    PRIMARY KEY(topic, time));
