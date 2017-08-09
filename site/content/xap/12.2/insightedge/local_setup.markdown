---
type: post122
title:  Local Machine Setup
categories: XAP122I9E
weight: 1000
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

In this tutorial, you will learn how to install and run InsightEdge on a local machine.


# Installation

The InsightEdge environment consists of Spark and the Data Grid. You have two options to run InsightEdge locally.

{{%warning%}}
Currently, only demo mode is available for Windows. This mode runs local Spark and Datagrid cluster, which is enough for most dev environments. Starting `demo` mode on Windows described in [Quick Start Guide](quick_start.html).
{{%/warning%}} 

The first option is to start the full InsightEdge environment:

* Spark `master` and `worker`
* Data Grid `manager` and `node`
* Data Grid with `empty space`
* Spark WebUI will be available at {{%exurl "127.0.0.1:8080""http://127.0.0.1:8080"%}}
* Spark Master connection endpoint will be at {{%exurl "spark://127.0.0.1:7077""spark://127.0.0.1:7077"%}}


```bash
./insightedge/sbin/insightedge.sh --mode master --master 127.0.0.1
./insightedge/sbin/insightedge.sh --mode slave --master 127.0.0.1
./insightedge/sbin/insightedge.sh --mode deploy --master 127.0.0.1
```


The second option is to start a Data Grid and use local mode to run Spark applications:



```bash
./insightedge/sbin/start-datagrid-master.sh --master 127.0.0.1
./insightedge/sbin/start-datagrid-slave.sh --master 127.0.0.1
./insightedge/sbin/deploy-datagrid.sh --master 127.0.0.1
```

When you run your applications using this option, you should specify `local[*]` instead of the Spark master url.

Both options run the Data Grid with default configuration:


* Data Grid consists of manager and 2 containers (`1G` heap each)
* Data Grid lookup locator is `127.0.0.1:4174` (lookup service is started on `4174` port)
* Data Grid lookup group is `insightedge`
* Data Grid has `insightedge-space` deployed on it with `2,0` topology (`2 primary` and `0 backup` partitions)


# Restarting or stopping local environment

The simplest way to restart the local environment is to use `insightedge.sh` script:



```bash
./insightedge/sbin/insightedge.sh --mode undeploy --master 127.0.0.1
./insightedge/sbin/insightedge.sh --mode master --master 127.0.0.1
./insightedge/sbin/insightedge.sh --mode slave --master 127.0.0.1
./insightedge/sbin/insightedge.sh --mode deploy --master 127.0.0.1
```



If necessary, the `master` and `slave` modes will stop the currently running components and start new ones.

To stop the environment, you can use `shutdown` mode:

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

Alternatively, you can execute the following component-specific scripts:


```bash
./insightedge/sbin/undeploy-datagrid.sh --master 127.0.0.1
./insightedge/sbin/stop-master.sh
./insightedge/insightedgesbin/stop-slave.sh
./insightedge/sbin/stop-datagrid-master.sh
./insightedge/sbin/stop-datagrid-slave.sh
```

You can skip `undeploy-datagrid.sh` if you just want to stop everything.



# Running demo

You can run the entire local environment in this mode, which will start the `Web Notebook` in addition to the other components mentioned previously:

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
For more details on the demo mode refer to [Interactive web notebook](./notebook.html).
{{%/refer%}}
