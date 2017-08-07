---
type: post
title:  What's New
categories: RELEASE_NOTES
parent: xap122.html
weight: 100
---

This page lists the main new features in XAP 12.2.

It's not an exhaustive list of all new features. For a full change log of 12.2 please refer to the [new features](./122new-features.html) and [fixed issues](./122fixed-issues.html) pages.

# The InsightEdge Platform

Starting with version 12.2, {{%exurl "InsightEdge" "https://insightedge.io/"%}} is transforming from a Spark Distribution to a **Platform** which combines XAP, Apache Spark and the InsightEdge components, to serve both transactional and analytical use cases.

InsightEdge is released as a new package, alongside the existing XAP packages (OSS, Premium and Enterprise) and uses XAP's versioning scheme. The InsightEdge package contains an Apache Spark distribution, 
but users may override it and use any Spark distribution, as long as its standard and compatible with the Apache Spark distribution.

# Functionality

More info coming soon...

# Administration

## Pluggable XAP Manager RESTful operations

The XAP Manager RESTful API is now extensible: To add an operation, simply implement a plain Java class with [JAX-RS](https://github.com/jax-rs) annotations, build it, and drop the jar in `$XAP_HOME/lib/platform/manager/plugins`. For example:

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

This class maps an HTTP `GET` operation on path `/demo/report` to a `report` method. It accepts a query parameter, and uses an injected `Admin` instance to perform some user-define code (in this case, a custom report).

{{<infosign>}} For more information see [Pluggable Manager Operations](/xap/12.2/admin/xap-manager-rest-pluggable.html)

## Spark Standalone Management

Spark Standalone cluster is fully integrated into XAP management environment and tools. In addition to starting spark master/worker via Spark's standard scripts, you can use XAP's process manager (`gs-agent`) to start a master and/or worker.

`gs-agent --manager --spark_master` will start a spark master alongside XAP Manager. If you require high availability, simply start 3 servers like that, and the spark masters will automatically connect to the managers Zookeeper cluster and elect a leader.

`gs-agent --spark_worker` will start a spark worker (a.k.a. slave), and automatically set it's master url to the masters (assuming there's a master on each XAP manager).

In addition, developers can leverage the `manager-local` option to run locally with zero configuration: `gs-agent --manager-local --spark_master --spark_worker` will run a local manager, spark master and worker on the local host.

# Third Party Upgrades

* {{%exurl "RocksDB" "http://rocksdb.org/"%}} integration has been upgraded to `5.5.1`
