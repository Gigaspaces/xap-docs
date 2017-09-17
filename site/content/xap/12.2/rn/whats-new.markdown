---
type: post122
title:  What's New
categories: XAP122RN
parent: none
weight: 100
---

This section describes product changes, along with new features and functionality for InsightEdge Platform release 12.2.

# InsightEdge Platform

XAP and InsightEdge have been unified in a single platform, and are now available as the InsightEdge Platform. This enables both application developers and data scientists to use the same environment to accomplish the following business goals:

- Develop innovative customer applications that levereage the XAP in-memory data grid for low latency and high-throughput workloads.
- Perform analytics to analyze data in motion, identify hidden insights in user data, and perform predictive analytics.

{{%note "Note..."%}} The InsightEdge package contains an Apache Spark distribution, but users may override it and use any Spark distribution that is standard and compatible with the Apache Spark distribution. {{% /note%}}

As part of this repackaging, the InsightEdge Platform has adopted the versioning scheme used by XAP, and is being released as version 12.2. The InsightEdge Platform combines InsightEdge, Apache Spark and XAP components, to serve both transactional and analytical data use cases.

The following editions are available:

- Full InsightEdge Platform (license required)
- XAP Enterprise (license required)
- XAP Premium (license required)
- InsightEdge/XAP open source (free)

{{%panel "Important Licensing Information"%}}
The licensing policy has been updated, and each minor version (12.2, 12.x, etc.) now requires its own license key. If you need to upgrade from a previous version of 12.x, the original upgrade support policy is still valid, however you must contact GigaSpaces support to request a new license key. 
{{% /panel %}}  

# Administration Features

Several XAP administration features have been updated and can now be used with the InsightEdge Platform. Additionally, functionality has been expanded to provide even greater flexibility for developing customer applications.

## REST Manager API

The XAP Manager RESTful API can now be used with the full InsightEdge Platform package, and is now extensible. Therefore, it has been renamed to REST Manager API. This extended functionality enaables customers to develop custom management interfaces (to deploy applications, troubleshoot problems, and monitor system health) in order to simplify day-to-day system management tasks.

To add an operation, simply implement a plain Java class with [JAX-RS](https://github.com/jax-rs) annotations, build it, and drop the jar in `$XAP_HOME/lib/platform/manager/plugins` folder. 
 
{{<infosign>}} For more information, see [Pluggable Manager Operations](/xap/12.2/admin/xap-manager-rest-pluggable.html).

## Spark Standalone Management

The Spark Standalone cluster is now fully integrated into the InsightEdge Platform/XAP management environment and tools. In addition to starting spark master/worker instances via Spark's standard scripts, you can use the InsightEdge Platform process manager (`gs-agent`) to start a master and/or worker instance.

- `gs-agent --manager --spark-master` will start a spark master instance alongside the XAP Manager instance. If you require high availability,  start 3 servers using this command, and the spark master instances will automatically connect to the manager's Zookeeper cluster and elect a leader.

- `gs-agent --spark-worker` will start a spark worker (a.k.a. slave) instance, and automatically set its master URL to the spark master's URL (assuming there is a spark master on each XAP Manager instance).

In addition, developers can leverage the `manager-local` option to run locally with zero configuration: `gs-agent --manager-local --spark_master --spark-worker` will run a local manager, spark master and worker on the local host.

{{<infosign>}} For more information, see [Cluster Setup for InsightEdge](/xap/12.2/admin/cluster_setup.html).

# SQL-99 Support

This new InsightEdge Platform feature provides in-grid SQL capability. This provides a familiar interface for querying the XAP in-memory data grid as if it were a traditional SQL database. It also enables the use of existing plug-in visualization tools on top of XAP, for faster and easier adoption within existing frameworks.

{{<infosign>}} For more information, see [In-Grid SQL Query](/xap/12.2/dev-java/sql-query-intro.html).

# Third Party Upgrades

* {{%exurl "RocksDB" "http://rocksdb.org/"%}} integration has been upgraded to `5.5.1`.
* {{%exurl "Spark" "https://spark.apache.org/"%}} integration has been upgraded to `2.2`.
