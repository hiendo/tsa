TSA
===

Time Series App


# Build
./gradlew clean build

# Cassandra must be run first.  Then run the app
java -jar build/libs/tsa-1.0.0-SNAPSHOT.jar

# Startup the server with embedded cassandra for testing
com.github.hiendo.tsa.DevAppMain