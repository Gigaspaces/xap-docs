---
type: post
title:  What's New
categories: RELEASE_NOTES
parent: xap122.html
weight: 100
---

This section describes product changes, along with new features and functionality for InsightEdge Platform release 12.2.

# InsightEdge Platform

XAP and InsightEdge have been unified in a single platform, and are now available as the InsightEdge Platform. This enables both application developers and data scientists to use the same environment to accomplish the following business goals:

- Develop innovative customer applications that levereage the XAP in-memory data grid for low latency and high-throughput workloads.
- Perform analytics to analyze data in motion, identify hidden insights in user data, and perform predictive analytics.

As part of this repackaging, the InsightEdge Platform has adopted the versioning scheme used by XAP, and is being released as version 12.2. The InsightEdge Platform combines InsightEdge, Apache Spark and XAP components, to serve both transactional and analytical data use cases.

The following licensing options are available:

- Full InsightEdge Platform
- InsightEdge open source
- XAP Enterprise
- XAP Premium
- XAP open source


{{%note “Note…..”%}} The InsightEdge package contains an Apache Spark distribution, but users may override it and use any Spark distribution that is standard and compatible with the Apache Spark distribution. {{% /note%}}

# Administration Features

Several XAP administration features have been updated and can now be used with the InsightEdge Platform. Additionally, functionality has been expanded to provide even greater flexibility for developing customer applications.

## REST Manager API

The XAP Manager RESTful API can now be used with the full InsightEdge Platform package, and is now extensible. Therefore, it has been renamed to REST Manager API. This extended functionality enaables customers to develop custom management interfaces (to deploy applications, troubleshoot problems, and monitor system health) in order to simplify day-to-day system management tasks.

To add an operation, simply implement a plain Java class with [JAX-RS](https://github.com/jax-rs) annotations, build it, and drop the jar in `$XAP_HOME/lib/platform/manager/plugins`. For example:

```java
@CustomManagerResource
@Path("/demo")
public class BasicPluggableOperationTest {
    @Context public Admin admin;

    @GET
    @Path("/report")
    public String report(@QueryParam("hostname") String hostname) {
        final Machine machine = admin.getMachines().getMachineByHostName(hostname);
        return "Custom report: host=" + hostname + 
                ", containers=" + machine.getGridServiceContainers() + 
                ", PU instances=" + machine.getProcessingUnitInstances();
    }
}
```

In the above example, the class maps an HTTP `GET` operation in the path `/demo/report` to a `report` method. It accepts a query parameter, and uses an injected `Admin` instance to run user-defined code and generate a custom report.

{{<infosign>}} For more information, see [Pluggable Manager Operations](/xap/12.2/admin/xap-manager-rest-pluggable.html).

## Spark Standalone Management

The Spark Standalone cluster is now fully integrated into the InsightEdge Platform/XAP management environment and tools. In addition to starting spark master/worker instances via Spark's standard scripts, you can use the InsightEdge Platform process manager (`gs-agent`) to start a master and/or worker instance.

- `gs-agent --manager --spark_master` will start a spark master instance alongside the XAP Manager instance. If you require high availability,  start 3 servers using this command, and the spark master instances will automatically connect to the manager's Zookeeper cluster and elect a leader.

- `gs-agent --spark_worker` will start a spark worker (a.k.a. slave) instance, and automatically set its master URL to the spark master's URL (assuming there is a spark master on each XAP Manager instance).

In addition, developers can leverage the `manager-local` option to run locally with zero configuration: `gs-agent --manager-local --spark_master --spark_worker` will run a local manager, spark master and worker on the local host.

# SQL Data Management

This new feature provides a familiar interface for developers that need to query the XAP in-memory data grid as if it were a traditional SQL database. This enables the use of existing plug-in visualization tools on top of XAP for faster and easier adoption within existing frameworks.


# Third Party Upgrades

* {{%exurl "RocksDB" "http://rocksdb.org/"%}} integration has been upgraded to `5.5.1`.
