---
type: post121
title:  ZooKeeper
categories: XAP121ADM, PRM
parent: runtime-configuration.html
weight: 400
---


# Overview

XAP uses {{%exurl "ZooKeeper""http://zookeeper.apache.org/"%}} for storing the id of the primary Space for each partition, this is used for MemoryXtend grid only.When a partition is started, the primary election mechanism will elect a primary Space randomly (or on basis of first-ready) but will wait for the last primary to take the role of primary the Space.

# Single instance

To start a ZooKeeper instance on a machine, launch the `gs-agent` script located in the `<XAPHOME>/bin` folder. This will start the [Grid Service Agent](/product_overview/service-grid.html#gsa), which is responsible for starting and managing the other Service Grid components (GSC, GSM, etc.). For example, to start two GSCs, two global GSMs and two global LUSs and a ZooKeeper, use the following command:


```bash
gs-agent gsa.gsc 2 gsa.global.gsm 2 gsa.global.lus 2 gsa.zk 1
```

# Cluster

For reliable ZooKeeper services, you should deploy ZooKeeper in a cluster known as an **Ensemble** . As long as a majority of the Ensembles are up, the service will be available. ZooKeeper requires a majority, it is best to use an odd number of machines.

Since every machine that is part of the ZooKeeper ensemble should know about every other machine in the ensemble, ZooKeeper instances are using the `XAP_ZOOKEEPER_SERVERS` environment variable or the `-Dorg.openspaces.grid.zookeeper.servers` system property. It accepts a comma separated list of `host:port:port`, the first port is used by followers to connect to the leader, and the second is for the leader election. By default it is configured with `hostname:2888:3888`.

For example:
```bash
XAP_ZOOKEEPER_SERVERS=host1:2888:3888,host3:2888:3888,host3:2888:3888
```

{{% note Note%}}
The ip/hostname above should be the same as it configured in `XAP_NIC_ADDR` as it explained [here](./network-multi-nic.html).
{{%/note%}}
<br>
Then start a ZooKeeper instances on an odd number of machines like the example below:

```bash
Machine A:
gs-agent gsa.gsc 0 gsa.global.gsm 2 gsa.global.lus 2 gsa.zk 1

Machine B:
gs-agent gsa.gsc 0 gsa.global.gsm 2 gsa.global.lus 2 gsa.zk 1

Machine C:
gs-agent gsa.gsc 2 gsa.global.gsm 0 gsa.global.lus 0 gsa.zk 1
```

# Configuration

Like other Grid Service Components you can use the component-specific configuration for specifying ZooKeeper settings. This is set using the environment variable: `ZOOKEEPER_JAVA_OPTIONS`.

For example:
{{% tabs %}}
{{% tab linux %}}

```bash
export XAP_ZOOKEEPER_JAVA_OPTIONS=-Xmx256m
./gs-agent.sh
```

{{% /tab %}}
{{% tab Windows %}}
```bash
set XAP_ZOOKEEPER_JAVA_OPTIONS=-Xmx256m
call gs-agent.bat
```
{{% /tab %}}
{{% /tabs %}}


The ZooKeeper configuration is provided by configuration file which is located by default at `<XAP Root>\config\zookeeper\zoo.cfg`, you can change it location using `XAP_ZOOKEEPER_SERVER_CONFIG_FILE` environment variable or the `-Dorg.openspaces.grid.zookeeper.config-file` system property.

{{%refer%}}
More information on how to configure ZooKeeper can be found {{%exurl "ZooKeeper configuration""https://zookeeper.apache.org/doc/r3.4.2/zookeeperAdmin.html#sc_configuration"%}}.
{{%/refer%}}
