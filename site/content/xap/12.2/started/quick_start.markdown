---
type: post122
title:  Quick Start Guide
categories: XAP122GS, IEE
weight: 100
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

In this tutorial, you will learn how to start InsightEdge and run examples locally. You can download the latest InsightEdge distribution from [here](http://insightedge.io/#download) and unpack it to any location.


# Starting InsightEdge

The InsightEdge cluster consists of Spark and a Data Grid. To start the minimum cluster locally, run the following command:

{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/sbin/insightedge.sh --mode demo
```
{{%/tab%}}

{{%tab Windows%}}
```bash
insightedge\sbin\insightedge.cmd --mode demo
```
{{%/tab%}}
{{%/tabs%}}


This will start Spark, the Data Grid and an interactive Web Notebook:

* Spark Master web ui is running at {{%exurl "localhost:8080""http://localhost:8080"%}}
* Web Notebook is running at {{%exurl "localhost:9090""http://localhost:9090"%}}

# Running the examples

Open the web notebook at {{%exurl "localhost:9090""http://localhost:9090"%}} and run any of the available examples.

{{%align center%}}
![image](/attachment_files/Zeppelin_examples_100.png)
{{%/align%}}

After you are done with examples, you can shutdown local environment with next command:

{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/sbin/insightedge.sh --mode shutdown
```
{{%/tab%}}

{{%tab Windows%}}
```bash
insightedge\sbin\insightedge.cmd --mode shutdown
```
{{%/tab%}}
{{%/tabs%}}
