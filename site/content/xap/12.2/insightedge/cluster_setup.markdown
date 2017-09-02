---
type: post122
title:  Cluster Setup
categories: XAP122I9E, IEE
weight: 1100
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

In this tutorial, you will learn how to install and run the InsightEdge on a cluster.


# Starting the whole cluster

Your cluster should consist of one master and a bunch of slaves:

* Master nodes usually have the Spark master and the Data Grid management running
* Slave nodes have Spark workers and Data Grid cluster members running on them

{{%warning%}}
Currently, only demo mode is available for Windows.
{{%/warning%}}

The `insightedge` scripts makes it easy to install and run/restart a remote cluster.
When you are running the following commands for the first time, append `--install` flag to install the InsightEdge on remote hosts.
After that, ommit the `--install` flag to start or restart the whole cluster.

Here is an example of the full automation script with the `--install` flag:


```bash
export MASTER=10.0.0.1
export SLAVES=10.0.0.2,10.0.0.3,10.0.0.4
export IEPATH=/home/insightedge/insightedge

./insightedge/sbin/insightedge.sh --mode undeploy \
  --master $MASTER --group dev-env

./insightedge/sbin/insightedge.sh --install --mode remote-master --hosts $MASTER \
  --user insightedge --key ~/.shh/dev.pem \
  --path $IEPATH --master $MASTER

./insightedge/sbin/insightedge.sh --install --mode remote-slave --hosts $SLAVES \
  --user insightedge --key ~/.shh/dev.pem \
  --path $IEPATH --master $MASTER

./insightedge/sbin/insightedge.sh --mode deploy \
  --master $MASTER --group dev-env --topology 3,1
```


After installation you can verify that Spark slaves are up and running on the Spark master web UI at `http://your-master-ip-here:8080`.

Let's now see the above example in detail.


# Running InsightEdge master

InsightEdge master starts Spark and Data Grid master on the node.

Master can be launched with `--mode master` or `--mode remote-master`. First mode will run/restart the Master locally, second one will connect to a remote host to run/restart the Master.

Here is the syntax and an example of running the master on the remote node:

```bash
# syntax
./insightedge/sbin/insightedge.sh \
  --mode remote-master --hosts {node ip} --user {ssh user} --key {optional ssh access key} \
  --install --path {absolute path to install to} --master {master cluster ip}

# example:
#   connects to remote node
#   downloads and unpacks fresh InsightEdge distribution
#   runs Spark and DataGrid master
./insightedge/sbin/insightedge.sh
  --mode remote-master --hosts 10.0.0.1 --user insightedge --key ~/.ssh/dev.pem \
  --install --path /home/insightedge/insightedge --master 10.0.0.1
```


# Running InsightEdge slave

InsightEdge slave starts Spark worker and Data Grid container on the node.

Slave can be launched with `--mode slave` or `--mode remote-slave`. First mode will run/restart the Slave locally, second one will connect to a remote host to run/restart the Slave.

Here is the syntax and an example of running the slave on the remote node:


```bash
# syntax
./insightedge/sbin/insightedge.sh \
  --mode remote-slave --hosts {node ips} --user {ssh user} --key {optional ssh access key} \
  --install --path {absolute path to install to} --master {master cluster ip}

# example:
#   connects to remote nodes, one by one
#   downloads and unpacks fresh InsightEdge distribution
#   runs one Spark worker and one DataGrid container per node
./insightedge/sbin/insightedge.sh
  --mode remote-slave --hosts 10.0.0.2,10.0.0.3,10.0.0.4 --user insightedge --key ~/.ssh/dev.pem \
  --install --path /home/insightedge/insightedge --master 10.0.0.1
```



# Deploying empty Data Grid space



```bash
# syntax
./insightedge/sbin/insightedge.sh --mode deploy --master {master cluster ip}

# example:
#   topology 3,1 starts 3 primary partitions with 1 backup partition for each primary
./insightedge/sbin/insightedge.sh --mode deploy --master 10.0.0.1 --topology 3,1
```




# Configuration

You can specify a number of settings when using `insightedge.sh` script: e.g. Data Grid containers size, lookup locators and groups. To see the full list of settings, run the script without arguments:

```bash
./insightedge/sbin/insightedge.sh
```


# Starting and stopping InsightEdge components

The `insightedge.sh` is just a wrapper script around more fine-grained scripts that would start and stop specific components.

You can start/stop Spark master and slave with these scripts:

```bash
./insightedge/sbin/start-master.sh
./insightedge/sbin/stop-master.sh
./insightedge/sbin/start-slave.sh {master url}
./insightedge/sbin/stop-slave.sh
```

Using these specific scripts comes at a cost of complexity when running Data Grid slaves in Community edition.
To run a component, you must specify which partition it corresponds to and if it's a primary or a backup partition.
The `--mode describe` simplifies allocating given topology on a given number of nodes.

The following scripts allow you to start/stop Data Grid master and slave:


```bash
./insightedge/sbin/start-datagrid-master.sh --master {master ip}
./insightedge/sbin/stop-dagagrid-master.sh
./insightedge/sbin/start-datagrid-slave.sh --master {master ip}
./insightedge/sbin/stop-datagrid-slave.sh

# deploy/undeploy space
./insightedge/sbin/deploy-datagrid.sh --master {master ip}
./insightedge/sbin/undeploy-datagrid.sh --master {master ip}
```

