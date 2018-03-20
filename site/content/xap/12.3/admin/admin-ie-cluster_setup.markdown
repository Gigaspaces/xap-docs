---
type: post123
title:  Cluster Setup for InsightEdge
categories: XAP123ADM,PRM
weight: 100
parent: admin-insightedge.html
---

In this tutorial, you will learn how to install and run the InsightEdge on a cluster.

Please refer to [InsightEdge Script](../started/insightedge-script.html) page for more info about the commands that will be used in this page.

# Starting the whole cluster

Your cluster should consist of one master and a bunch of slaves:

* Master nodes usually have the Spark master and the Data Grid management running
* Slave nodes have Spark workers and Data Grid cluster members running on them


The master nodes start XAP Manager along with Zookeeper for high availability. Therefore you need to configure the `XAP_MANAGER_SERVERS` in each machine. Please refer to the [XAP Manager](../admin/xap-manager.html) page for more information.

In addition, it is recommended to set the XAP_LOOKUP_GROUPS property that is used for discovering XAP component across the network.
Please refer to the [Environment Variables](../started/common-environment-variables.html) page for more advanced configurations.

These configuration can be set in the `<XAP_HOME>/bin/setenv-overrides.sh/bar` file.

## Starting Master Nodes

Master nodes are consisted of XAP Manager and a Spark Master.

On each Master node run the following:

```bash
insightedge run --master
```

## Starting Worker Nodes

Worker nodes are consisted of XAP Containers and a Spark Worker

On each Worker node run the following:

Use `--containers=n` if you wish to have XAP containers on the specific machine. If not specified, no XAP containers will be started

```bash
insightedge run --worker [--containers=n]
```

After installation you can verify that Spark slaves are up and running on the Spark master web UI at `http://your-master-ip-here:8080`.

# Deploying empty Data Grid space

```bash
#   topology 2,1 starts 2 primary partitions with 1 backup partition for each primary
insightedge deploy-space --partitions=2 --backups insightedge-space
```
