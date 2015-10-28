---
type: post110
title:  ZooKeeper
categories: XAP110ADM
parent: runtime-configuration.html
weight: 500
---

{{% ssummary %}}{{% /ssummary %}}

{{% anchor GSRuntimeEnv %}}

XAP uses [Zookeeper](http://zookeeper.apache.org/) for storing keeps the id of the primary space for each partition, this is used for MemoryXtend grid only.When a partition is brought up the primary election mechanism will elect a primary space randomly (or on basis of first-ready) but wait for the last primary to take the role of primary space.

# Starting a ZooKeeper single instance

To start a Zookeeper instance on a machine, launch the `gs-agent` utility located in the `<GSHOME>/bin` folder. This will start the [Grid Service Agent](/product_overview/service-grid.html#gsa), which is responsible of starting and managing the other Service Grid components (GSC, GSM, etc.). For example, to start two GSCs, two global GSMs and two global LUSs and a zookeeper, use the following command:


```xml
gs-agent gsa.gsc 2 gsa.global.gsm 2 gsa.global.lus 2 gsa.zk 1
```

# Starting clustered ZooKeeper 

For reliable ZooKeeper service, you should deploy ZooKeeper in a cluster known as an ensemble. As long as a majority of the ensemble are up, the service will be available. Because Zookeeper requires a majority, it is best to use an odd number of machines. 

Since every machine that is part of the ZooKeeper ensemble should know about every other machine in the ensemble, ZooKeeper instances are using the `ZK_SERVERS` environment variable or the `-Dorg.openspaces.grid.zk.servers` system property. It accepts a comma separated list of host:port:port, the first port is used by followers to connect to the leader, and the second is for leader election. By default it is configured with `hostname:2888:3888`. 

Then start a Zookeeper instances on an odd number of machines like above:

```xml
Machine A:
gs-agent gsa.gsc 0 gsa.global.gsm 2 gsa.global.lus 2 gsa.zk 1

Machine B:
gs-agent gsa.gsc 0 gsa.global.gsm 2 gsa.global.lus 2 gsa.zk 1

Machine C:
gs-agent gsa.gsc 2 gsa.global.gsm 0 gsa.global.lus 0 gsa.zk 1
```

# Configuration

Like other The Service Grid components you can use the component-specific configuration for specifying Zookeeper settings. Theis is set using environment variable: `ZK_JAVA_OPTIONS`.

For example:

{{% section %}}
{{% column width="50%" %}}

```xml
Linux
export ZK_JAVA_OPTIONS=-Xmx256m

./gs-agent.sh
```
{{% /column %}}

{{% column width="45%" %}}

```xml
Windows
set ZK_JAVA_OPTIONS=-Xmx256m

call gs-agent.bat
```
{{% /column %}}
{{% /section %}}


# ZooKeeper Configuration

ZooKeepr configuration is provided by configuration file which is located by default at `<XAP Root>\config\zk\zoo.cfg`, you can change it location using `ZK_SERVER_CONFIG_FILE` environment variable or the `-Dorg.openspaces.grid.zk.config-file` system property.
More information on ZooKeeper configuration you can find [here](https://zookeeper.apache.org/doc/r3.4.2/zookeeperAdmin.html#sc_configuration).

