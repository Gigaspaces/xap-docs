---
type: post122
title:  Starting InsightEdge
categories: XAP122GS, IEE
weight: 100
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

This topic explains how to start InsightEdge and run examples locally. You can download the latest InsightEdge distribution from [here](http://insightedge.io/#download) and unpack it to any location.

The InsightEdge cluster consists of Spark and a Data Grid. To start the minimum cluster locally, run the relevant command provided below. This will start Spark, the Data Grid and an interactive Web Notebook with the following URLs:

* Spark Master web UI: {{%exurl "127.0.0.1:8080""http://127.0.0.1:8080"%}}
* Web Notebook: {{%exurl "127.0.0.1:8090""http://127.0.0.1:8090"%}}

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

{{%refer%}}
If you are not familiar with the Apache Zeppelin web-based notebook, refer to [Apache Zeppelin Web Notebook](./notebook.html).
{{%/refer%}}

# Running InsightEdge Examples

Open the web notebook at {{%exurl "127.0.0.1:8090""http://127.0.0.1:8090"%}} and run any of the available examples.

{{%align center%}}
![image](/attachment_files/Zeppelin_examples_100.png)
{{%/align%}}

After you have finished exploring the examples, you can shut down the local environment with the following command:

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
