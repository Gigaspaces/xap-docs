---
type: post123
title:  InsightEdge Script
categories: XAP123GS, OSS
parent: insightedge-basics.html
weight: 500
---

# Overview

The `insightedge` script facilitates getting started with InsightEdge. It wraps XAP's gs-agent script and runs it with the relevant parameters.

# Usage

The script is located under `<XAP Home>/bin` folder.

```
> ./insightedge help

    _____ _              _____
   / ____(_)            / ____|
  | |  __ _  __ _  __ _| (___  _ __   __ _  ___ ___  ___
  | | |_ | |/ _` |/ _` |\___ \| '_ \ / _` |/ __/ _ \/ __|
  | |__| | | (_| | (_| |____) | |_) | (_| | (_|  __/\__ \
   \_____|_|\__, |\__,_|_____/| .__/ \__,_|\___\___||___/
   _____     __/ | _       _  | |_   ______    _
  |_   _|   |___/ (_)     | | |_| | |  ____|  | |
    | |  _ __  ___ _  __ _| |__ | |_| |__   __| | __ _  ___
    | | | '_ \/ __| |/ _` | '_ \| __|  __| / _` |/ _` |/ _ \
   _| |_| | | \__ \ | (_| | | | | |_| |___| (_| | (_| |  __/
  |_____|_| |_|___/_|\__, |_| |_|\__|______\__,_|\__, |\___|
                      __/ |                       __/ |
                     |___/                       |___/

  Usage: insightedge [global-options] command [options] [parameters]

  Description:
  Options:
        --help                  Show the help information for this command
        --password=<password>   Password for secured environments
        --server=<server>       Name or IP address of the Manager server to
                                  connect to
        --timeout=<timeout>     Change the default timeout (60 sec) for the
                                  specified operation
        --username=<username>   Username for secured environments
  Commands:
    version    Platform version
    pu         List of available commands for Processing Unit operations
    space      List of available commands for Space operations
    demo       Run Spark in standalone mode (Master, Worker and Zeppelin) and
                 deploy a Space in high availability mode (2 primaries with
                 backup each).
    host       List of available commands for local host operations
    container  List of available commands for container operations
    info       Platform environment information


```

<br />

## The `demo` Command

The demo command will start a minimal environment that includes:

* Spark master with its monitoring UI at `http://localhost:8080`. The connection endpoint will be at `spark://localhost:7077`
* Spark worker
* XAP manager in local mode with the RESTful Manager API at `http://localhost:8090`
* Two XAP containers
* Empty space named `insightedge-space` with 2 partitions
* Zeppelin UI at `http://localhost:9090`
* Zookeeper with one node to demonstrate how InsightEdge components connects to Zookeeper for high availability

```bash
insightedge demo
```

## The `run` Command

The run command allows to start three components separately: InsightEdge Master, InsightEdge Worker and Zeppelin.

Since XAP Manager is started, you must configure the XAP_MANAGER_SERVERS  and SPARK_LOCAL_IP environment variables in every InsightEdge node. Please refer to the [XAP Manager](../admin/xap-manager.html) section.

## Running InsightEdge Master

Update the environment variable in setenv-overrides.sh XAP_MANAGER_SERVERS=localhost,
The command will start XAP Manager and Spark Master.


```bash
./insightedge host run-agent --manager --spark-master
```

## Running InsightEdge Worker

The command allows starting XAP Containers and Spark Worker. If the optional `--containers=n` option is not specified, no XAP Containers will be started.

```bash
./insightedge host run-agent --spark-worker [--containers=n]
```

## Running Apache Zeppelin

The command will start Zeppelin on the local machine. It will be accessible at {{%exurl "http://localhost:9090""http://localhost:9090"%}}

```bash
./insightedge host run-agent --zeppelin
```

## The `deploy-space` Command

This command enables you to deploy an empty space to the XAP Grid.

If the `--patitions=n` option is not specified, the command will deploy a single space.
Otherwise, it will deploy a partitioned space with n partitions without backups.
If you wish to have backups, you can use the `--backups` option.

```bash
./insightedge  space deploy  [--partitions=x [--backups]] <space-name>
```

## The `undeploy` Command

This command enables you to undeploy an already deployed space.

```bash
./insightedge pu undeploy <space-name>
```

## The `shutdown` Command

The shutdown command kills all components that are started via the insightedge script.

If you have deployed a space to one or more components, the space will be destroyed.

```bash
insightedge host kill-agent --all
```

# Configuration

By default, XAP components starts with default lookup groups and lookup locators (e.g. 12.2.0-ga and localhost:4174). These lookup groups and locators are used for the discovering the services in the network. InsightEdge scripts uses these groups and locators when starting Master, Worker and Zeppelin as well as when using the different scripts under `insightedge/bin` folder (e.g. insightedge-submit).

Please refer to the [Environment Variables](common-environment-variables.html) page if you wish to use different values.

InsightEdge uses the XAP Manager that starts Zookeeper for high availability. Please refer to [XAP Manager](../admin/xap-manager.html) page for info about its configuration.

