# Flink Score Publisher

## Overview
The Flink Score Publisher is a tool designed to publish scores from a Flink job to a specified endpoint.
In this case it is used to publish match updates to a Redis store which is then used by a prediction web-service
to display the probability of a team winning a match.
It is built using Java and utilizes the Apache Flink framework for stream processing.


## Prerequisites
- Java 11 (or higher)
- Kafka cluster 
- Redis cluster
- Apache Flink 1.19.2 (Standalone for local testing)
- Maven 3.8.1 (or higher)
- IDE of your choice 

## Setup

Download this repository and the py equivalent of this which has the 
simulator(https://github.com/nupsea/cric-chase-pred/tree/master) and open it in your IDE.

Install Apache Flink 1.19.2 (preferably) and set up a standalone cluster.
You can follow the official [Flink documentation](https://nightlies.apache.org/flink/flink-docs-release-1.19/docs/deployment/cluster_setup/) for instructions on how to set up a standalone cluster.


## Build
To build the project, navigate to the root directory of the project and run the following command:

```bash
mvn clean package
```
This will compile the code and create a JAR file in the `target` directory.
The JAR file will be named `flink-score-publisher-1.0-SNAPSHOT.jar`.
The JAR file will contain all the necessary dependencies to run the Flink job.


## Run
To start Flink cluster, navigate to the directory of your Flink installation and run the following command:

```bash
bin/start-cluster.sh
```

Copy the jar into the Flink installation directory and run the following command to submit the job to the Flink cluster:

```bash
cp <path-to-jar>/flink-score-publisher-1.0-SNAPSHOT.jar <flink-installation-directory>
bin/flink run -c flink-score-publisher-1.0-SNAPSHOT.jar
```
This will start the Flink job and it will begin to consume messages from the Kafka topic and publish scores to the Redis store.