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

The InsightEdge environment consists of Spark and the XAP data grid. You can use the Command Line Interface tool to start InsightEdge as described below. 

## Starting a Complete Environment

To start a complete InsightEdge environment on your local machine, run the following command from the `$XAP_HOME/bin` directory:

```bash
insightedge host run-agent --auto gsc=2
```

This will start the following components on your machine:

* Spark Master UI at {{%exurl "localhost:8080""http://localhost:8080"%}}
   * URL for submitting spark jobs: `spark://127.0.0.1:7077`
* Spark Worker UI at {{%exurl "localhost:8081""http://localhost:8081"%}}
* Zeppelin UI at {{%exurl "localhost:9090""http://localhost:9090"%}}
* XAP service grid:
    * XAP Manager at {{%exurl "localhost:8090""http://localhost:8090"%}}
	* 2 Grid Service Containers (GSCs)
	
Then run the following command to deploy a Space called `insightedge-space` with 2 partitions on those GSCs:

```bash
insightedge space deploy --partitions=2 insightedge-space
```

# Stopping the Local Environment

To stop the local environment, use the `kill-agent` command that sends a kill signal to all relevant processes:

```bash
insightedge host kill-agent
```

# What's Next?

* To get started with the Apache Zeppelin Web Notebook, see [Web Notebook](insightedge-zeppelin.html).
* To run the examples on your local InsightEdge environment, see [Running the Examples](insightedge-examples.html)
* To create your first InsightEdge application, see [Developing Your First Application](insightedge-first-app.html)
* To learn more about the `insightedge` CLI script, use the `--help` option
* To set up a cluster on a set of machines, see [Cluster Setup For InsightEdge](../admin//admin-ie-cluster_setup.html)
