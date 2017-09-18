---
type: post122
title:  Local Machine Setup
categories: XAP122GS, IEE
weight: 1200
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

This section explains how to install and run InsightEdge on a local machine.

Please refer to [InsightEdge Script](./insightedge_script.html) page for more info about the commands that will be used in this page.

# Installation

The InsightEdge environment consists of Spark and the Data Grid. There are two options for running InsightEdge locally.

The first option is to start the full InsightEdge environment:

* Spark `master` and `worker`
* Data Grid `manager` and `container`
* Data Grid with `empty space`
* Spark WebUI will be available at {{%exurl "127.0.0.1:8080""http://127.0.0.1:8080"%}}
* Spark Master connection endpoint will be at {{%exurl "spark://127.0.0.1:7077""spark://127.0.0.1:7077"%}}

Running XAP local manager is not supported via the `insightedge` script therefore we will be using the `gs-agent` script directly.

```bash
# Start Spark Master, Spark Worker, XAP Manager (local) and two XAP Containers
./bin/gs-agent.sh --manager-local --gsc=2 --spark-master --spark-worker
# Deploy a partitioned space named insightedge-space with 2 partitions and 1 backup each
./insightedge/bin/insightedge deploy-space --partitions=2 --backups insightedge-space
```


The second option is to start a Data Grid and use local mode to run Spark applications:



```bash
./bin/gs-agent.sh --manager-local --gsc=2
./insightedge/bin/insightedge deploy-space --partitions=2 --backups insightedge-space
```

When you run your applications using this option, you should specify `local[*]` instead of the Spark master url.

Both options run the Data Grid with default configuration:


* Data Grid consists of manager and 2 containers (`512m` heap each)
* Data Grid lookup locator is `127.0.0.1:4174` (lookup service is started on `4174` port)
* Data Grid lookup group is `xap-12.2.0`
* Data Grid has `insightedge-space` deployed on it with `2,1` topology (`2 primary` and `1 backup` partitions)


# Stopping local environment

To stop the environment, you can use `shutdown` mode that sends kill signal to all relevant processes:


{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/bin/insightedge shutdown
```
{{%/tab%}}

{{%tab Windows%}}
```bash
insightedge\bin\insightedge shutdown
```
{{%/tab%}}
{{%/tabs%}}

Alternatively, you can use the Ctrl+C combination to abort the `gs-agent` process.


# Running demo

You can run the entire local environment in this mode, which will start the `Web Notebook` in addition to the other components mentioned previously:

{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/bin/insightedge demo
```
{{%/tab%}}

{{%tab Windows%}}
```bash
insightedge\bin\insightedge demo
```
{{%/tab%}}
{{%/tabs%}}

{{%refer%}}
For more details on the demo mode refer to [Interactive web notebook](./notebook.html).
{{%/refer%}}
