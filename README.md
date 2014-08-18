TSA
===

Time Series App with subset featues similar to the graphite tool (http://graphite.wikidot.com/).  This is used to experiment with Cassandra database time series feature with the intention that it can be also used in automated fashion via REST to get simple stats and graphs for a particular performance data/topic.


# Build
./gradlew clean build

# Cassandra must be run first.  Then run the app.
java -jar build/libs/tsa-1.0.0-SNAPSHOT.jar

# Startup the server with embedded cassandra for testing
com.github.hiendo.tsa.DevAppMain