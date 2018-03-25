---
type: post123
title:  Local Machine Setup
categories: XAP123GS, OSS
parent: insightedge-basics.html
weight: 100
---

This section explains how to start an InsightEdge environment on a local machine.

# Prerequisites

Before running InsightEdge, verify that you have the following installed in your environment:

* Java 8
* Scala 2.11
* Maven 3.1+

# Starting the Local Environment

The InsightEdge environment consists of Spark and the XAP Data Grid.

## Starting Full Environment

To start a complete InsightEdge environment on your local machine, run the following command from the `<XAP_HOME>/insightedge/bin` directory:

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
    * A Space called `insightedge-space` with 2 partitions



# Stopping Local Environment

To stop the environment, you can use `shutdown` mode that sends kill signal to all relevant processes:

```bash
insightedge host kill-agent
```

# What's Next?

* To get started with the Apache Zeppelin Web Notebook, see [Web notebook](insightedge-zeppelin.html).
* To run the examples on your local InsightEdge environment, see [Running the examples](insightedge-examples.html)
* To create your first InsightEdge application, see [Developing Your First Application](insightedge-first-app.html)
* To learn more on the `insightedge` script, see [InsightEdge Script](insightedge-script.html)
* To setup a cluster on a set of machines, see [Cluster Setup For InsightEdge](../admin/cluster_setup.html)
