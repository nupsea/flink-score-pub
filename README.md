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

In addition, follow the quickstart guide to run the model/streamlit realtime app (`rt_redis_app.py`) in for dependent prediction service.
```
pipenv shell
(ipl_infer) streamlit run rt_redis_app.py
```

## Build
To build the project, navigate to the root directory of the project and run the following command:

```bash
mvn clean package
```
This will compile the code and create a JAR file in the `target` directory.
The JAR file will be named `flink-score-publisher-1.0-SNAPSHOT.jar`.
The JAR file will contain all the necessary dependencies to run the Flink job.


## Run
Ensure, kafka and redis are running and the topic `cric-chase-pred` is created in the kafka cluster.
On macOS, you can start the Kafka and Redis servers using the following commands:
```bash
redis-server /opt/homebrew/etc/redis.conf

bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties

bin/kafka-topics.sh --create --topic t20-deliveries --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

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

Run the `inn1_simulator.py` file under src/feed to start the simulator for the first innings.
This will start the simulator and it will begin to produce messages to the kafka topic `t20-deliveries`
and the Flink job will consume these messages and print out the target score which is persisted as
a state in Flink to enrich Innings 2 messages.

Run the `inn2_simulator.py` file under src/feed to start the simulator for the second innings.
This will start the simulator and it will begin to produce messages to Redis Store `t20-deliveries`
and the Flink job will consume these messages and publish the match updates (enriched messages) to the redis `model-input-json` store.

The rt_redis_app.py streamlit app will consume the messages from the redis store and display the probability of a team winning a match.