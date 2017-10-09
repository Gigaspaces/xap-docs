---
type: post
title:  Deep Learning
categories: SBP
parent: insightedge.html
weight: 200
---

This topic explains how to combine real-time speech recognition with real-time speech classificationm based on Intel's BigDL library and Insightedge.

## What is BigDL?
BigDL is a distributedm deep-learning library for Apache Spark. To learn more about deep learning and neural networks, refer to {{%exurl "Coursera""https://www.coursera.org/specializations/deep-learning"%}}.

With BigDL, it is possible to write deep-learning applications as standard Spark programs, which enables leveraging Spark during model training, prediction, and tuning. High performance and throughput is achieved using the {{%exurl "Intel Math Kernel Library""https://software.intel.com/en-us/mkl"%}}.
 
{{%refer%}} 
Read more about {{%exurl "Distributed Deep Learning on Apache Spark""https://github.com/intel-analytics/BigDL"%}}.
{{%/refer%}}

## Use Case
As a sample use case, consider a big company with a very large client base that continually contacts the company call centers for customer service, technical support, etc. In order to serve the clients correctly and maintain high customer satisfaction, it is critical to direct calls to the appropriate specialist. The current demo takes advantage of cutting-edge technologies to handle this task effectively in under 100 ms.
Here is a general workflow:

![Application flow](/attachment_files/sbp/bigdl/flow.png)


## Architecture
The following diagram provides a high-level view of the application components.

![Architecture](/attachment_files/sbp/bigdl/architecture.png)

{{%info "Info"%}} 
Additional documentation about Intel's BigDL is available at https://bigdl-project.github.io/master/ and https://software.intel.com/en-us/ai-academy/frameworks/bigdl. 
 {{%/info%}}

## Running BigDL with InsightEdge

In this demo, the follwoing software was used:

- Scala version 2.10.4
- Java version 1.8.x
- Kafka version 0.8.2.2
- InsightEdge version 1.0.0
- BigDL version 0.2.0>
- sbt
- Maven version 3.x

### Prerequisites

Before running the demo, do the following to prepare your environment:

* Download and extract data ( the first three steps as described in {{%exurl "here""https://github.com/intel-analytics/BigDL/tree/master/spark/dl/src/main/scala/com/intel/analytics/bigdl/example/textclassification"%}}).
* Set the `INSIGHTEDGE_HOME` and `KAFKA_HOME` environment variables.
* Verify that Scala is installed: `scala -version` 
* Modify the variables according to your system requirments using `runModelTrainingJob.sh`, `runTextPredictionJob.sh`, and `runKafkaProducer.sh`.

### Launching the Demo


There are three stages to running the BigDL demo.

**Stage 1 - Build the project and start the components**

1. Clone this repo.
1. Navigate to the InsightEdge directory: `cd BigDL/insightedge`
1. Build the project: `sh build.sh`
1. Start Apache Zookeeper and the Kafka server: `sh kafka-start.sh`
1. Create a Kafka topic: `sh kafka-create-topic.sh` (to verify that the topic was created, run `sh kafka-topics.sh`).
1. Start Insightedge in demo mode: `sh ie-demo.sh`
1. Deploy the processor-0.2.0-jar-with-dependencies.jar in the XAP Management Center.

**Stage 2 - Train the BigDL model**

- Train the text classifier model: `sh runModelTrainingJob.sh`

**Stage 3 - Run the Spark streaming job with the trained BigDL classification model**

1. In separate terminal tab, start Spark streaming for predictions: `sh runTextClassificationJob.sh`.
1. Start the web server: `cd BigDL/web and sh runWeb.sh`.

### Testing the Demo

To use the demo and get a feel for how deep learning occurs with BigDL, open a browser window at {{%exurl "https://localhost:9443""https://localhost:9443"%}}. We suggest performing the following test steps:

1. Click the microphone button and begin talking. 
1. Click the microphone button again to stop recording and send the recorded speech to Kafka.
1. Wait for a new record to appear in the "In-process calls" table. This indicates that the call is currently being processed.
1. When the row moves from the "In-process call" table to the "Call sessions" table, check the following:
 - In the "Category" column, you can see how the speech was classified by the BigDL model.
 - In the "Time" column, you can see how much time (in milliseconds) it took to classify the speech. 

## Shutting Down InsightEdge

There are two steps to the shutdown process; first stop Kafka, and then stop InsightEdge.

To stop Kafka, use the following command:

```bash
./kafka-stop.sh
```

To stop Insightedge, use the following command:

```bash
./ie-shutdown.sh
```