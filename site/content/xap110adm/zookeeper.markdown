---
type: post110
title:  ZooKeeper
categories: XAP110ADM
parent: runtime-configuration.html
weight: 500
---

{{% ssummary %}}{{% /ssummary %}}

{{%warning "Technical Preview"%}}
This feature is new in 11.0 and is currently a technical preview, i.e. it is subject to breaking changes until 11.0 is released.
{{%/warning%}}


{{% anchor GSRuntimeEnv %}}

# Overview

XAP uses [ZooKeeper](http://zookeeper.apache.org/) for storing the id of the primary Space for each partition, this is used for MemoryXtend grid only.When a partition is started, the primary election mechanism will elect a primary Space randomly (or on basis of first-ready) but will wait for the last primary to take the role of primary the Space.

# Single instance

To start a ZooKeeper instance on a machine, launch the `gs-agent` script located in the `<GSHOME>/bin` folder. This will start the [Grid Service Agent](/product_overview/service-grid.html#gsa), which is responsible for starting and managing the other Service Grid components (GSC, GSM, etc.). For example, to start two GSCs, two global GSMs and two global LUSs and a ZooKeeper, use the following command:


```xml
gs-agent gsa.gsc 2 gsa.global.gsm 2 gsa.global.lus 2 gsa.zk 1
```

# Cluster

For reliable ZooKeeper services, you should deploy ZooKeeper in a cluster known as an **Ensemble** . As long as a majority of the Ensembles are up, the service will be available. ZooKeeper requires a majority, it is best to use an odd number of machines.

Since every machine that is part of the ZooKeeper ensemble should know about every other machine in the ensemble, ZooKeeper instances are using the `ZK_SERVERS` environment variable or the `-Dorg.openspaces.grid.zk.servers` system property. It accepts a comma separated list of `host:port:port`, the first port is used by followers to connect to the leader, and the second is for the leader election. By default it is configured with `hostname:2888:3888`.

Then start a ZooKeeper instances on an odd number of machines like the example below:

```xml
Machine A:
gs-agent gsa.gsc 0 gsa.global.gsm 2 gsa.global.lus 2 gsa.zk 1

Machine B:
gs-agent gsa.gsc 0 gsa.global.gsm 2 gsa.global.lus 2 gsa.zk 1

Machine C:
gs-agent gsa.gsc 2 gsa.global.gsm 0 gsa.global.lus 0 gsa.zk 1
```

# Configuration

Like other Grid Service Components you can use the component-specific configuration for specifying ZooKeeper settings. This is set using the environment variable: `ZK_JAVA_OPTIONS`.

For example:

{{% tabs %}}
{{% tab linux %}}

```bash
export ZK_JAVA_OPTIONS=-Xmx256m

./gs-agent.sh
```

{{% /tab %}}

{{% tab Windows %}}

```xml
set ZK_JAVA_OPTIONS=-Xmx256m

call gs-agent.bat
```
{{% /tab %}}
{{% /tabs %}}


The ZooKeeper configuration is provided by configuration file which is located by default at `<XAP Root>\config\zk\zoo.cfg`, you can change it location using `ZK_SERVER_CONFIG_FILE` environment variable or the `-Dorg.openspaces.grid.zk.config-file` system property.

{{%refer%}}
More information on how to configure ZooKeeper can be found [here](https://zookeeper.apache.org/doc/r3.4.2/zookeeperAdmin.html#sc_configuration).
{{%/refer%}}
