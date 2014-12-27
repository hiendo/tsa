TSA
===

Time Series App with subset featues similar to the graphite tool (http://graphite.wikidot.com/).  This is used to experiment with Cassandra database time series feature with the intention that it can be also used in automated fashion via REST to get simple stats and graphs for a particular performance data/topic.  Starting up the server will a app server with an embedded cassandra.


# Build
./gradlew clean build

# Run the application.  A sample client to run is located in DataGeneratorTestUtil.
java -jar build/libs/tsa-1.0.0-SNAPSHOT-exec.jar

# Startup the server within IDE for testing
com.github.hiendo.tsa.DevAppMain