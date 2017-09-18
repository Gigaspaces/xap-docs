---
type: post122
title:  Using the InsightEdge Examples
categories: XAP122GS, IEE
weight: 1110
---

# Overview

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

This section explains how to use the sample code that has been provided with the InsightEdge package. You can download the latest InsightEdge distribution from [here](http://insightedge.io/#download) and unpack it to any location.

{{%info "Info..."%}}
The example sources are located at `$XAP_HOME/insightedge/examples`.

The example .jar is located at `$XAP_HOME/insightedge/examples/jars`.
{{%/info%}}

In order to get InsightEdge up and running with the provided examples, you will have to perform the following:

1. Start a local environment.
2. Build the examples.
3. Run the examples.
4. Shut down the local environment when you are done.


# Prerequisites

Before installing and running InsightEdge, verify that you have the following installed in your environment:

* Java 8
* Scala 2.11
* Maven 3.1+


# Starting the Local Environment

Before you can launch the sample application, you must start insightEdge. To do that, run the following from the `$XAP_HOME/insightedge/bin` directory:

```bash
insightedge demo
```

This will start the following components on your machine:

* Spark Master UI at {{%exurl "localhost:8080""http://localhost:8080"%}}
   * Url for submitting spark jobs: `spark://127.0.0.1:7077`
* Spark Worker UI at {{%exurl "localhost:8081""http://localhost:8081"%}}
* Zeppelin UI at {{%exurl "localhost:9090""http://localhost:9090"%}}
* XAP Service grid:
    * XAP Manager at {{%exurl "localhost:8090""http://localhost:8090"%}}
    * A Space called `insightedge-space` with 2 partitions (`1G` heap each)

# Building Examples with Maven

Next, you must install the InsightEdge artifacts in your local Maven repository, and then build the examples using Maven. To do that, run the following from the `$XAP_HOME/insightedge/tools/maven` directory:

```bash
insightedge-maven
```

Next, use the following command in Maven to build the examples from `$XAP_HOME/insightedge/examples`:

```bash
mvn clean package
```

The end result is an examples .jar located at `$XAP_HOME/insightedge/examples/target/insightedge-examples.jar`.

# Running the insightedge-examples.jar File

You can run the InsightEdge example .jar from any of the following:

* Apache Zeppelin Web Notebook
* An IDE
* A command line

## Using the Web Notebook

To run the examples from your web notebook, go to `http://localhost:9090/`.

## Using an IDE

You can run the examples from your preferred IDE. Every example has a `main` method, so it can be executed as standard application. Note these two important configuration requirements:

* Enable `run-from-ide` in the Maven profile. This will switch the required dependencies to `compile` scope, so they are available in the classpath.
* Pass the examples as `local[*]` and `insightedge-space` arguments.

## Using a Command Line


To run the examples from a command line, run the following from the `$XAP_HOME/insightedge/bin` directory:

```bash
insightedge-submit --class {main class name} --master {Spark master URL} {path/to/insightedge-examples.jar}
```

For example, `SaveRDD` can be submitted with the following syntax:

```bash
insightedge-submit --class org.insightedge.examples.basic.SaveRdd --master spark://127.0.0.1:7077 \
    ./$XAP_HOME/insightedge/examples/jars/insightedge-examples.jar
```

{{%note "Note..."%}}
Running the `TwitterPopularTags` example requires you to pass {{%exurl "Twitter app tokens" "https://apps.twitter.com/"%}} as arguments.
{{%/note%}}

### Python Examples

In addition to the above, you can run Python examples using the following command:
{{%tabs%}}

```bash
insightedge-submit --master {Spark master URL} {path/to/your-example.py}
```

For example:

```bash
insightedge-submit --master spark://127.0.0.1:7077 ./$XAP_HOME/insightedge/examples/python/sf_salaries.py
```

# Stopping the Local Environment

To stop all InsightEdge components, run the following from the `$XAP_HOME/insightedge/bin` directory:

```bash
insightedge shutdown
```
