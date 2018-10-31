---
type: post140
title:  Clustered vs Non-Clustered Proxies
categories: XAP140ADM, OSS
parent: data-grid-clustering.html
weight: 200
---

 



When deploying a Processing Unit(PU) configured with an embedded [Space](../dev-java/the-space-configuration.html) with a clustered SLA or when running a remote clustered space, a clustered `GigaSpace` proxy is created.

A clustered proxy is a smart proxy that performs operations against the entire cluster when needed.

- The `write` operation will be routed based on the routing field value to the relevant partition (using the routing field hashcode to calculate the the target partition).
- The `read` operation will do the same by routing the operation to the relevant partition.
- The `writeMultiple` will generate an entries bucket per partition for all entries that should be placed within the same partition and perform a parallel write to all relevant partitions.
- The `readMultiple` and `clear` operations will access all cluster partitions in a map-reduce fashion in case the query/template routing value is not specified.
- The `execute` operation will route the `Task` to the relevant partition based on the routing value.
- The `execute` operation will route the `DistributedTask` to all partitions if no routing value been specified or to a specific partitions in case a routing value been specified.
 

Many times, especially when working with a PU that starts an embedded space, operations against the space should be performed directly on the cluster member without interacting with the other space cluster members (partitions). This is a core concept of the SBA and Processing Unit, where most if not all the operations should be performed in-memory without leaving the Processing Unit boundaries, when a Processing Unit starts an embedded space.


{{% align center%}}
![clustered-vs-non-clustered-proxy.jpg](/attachment_files/clustered-vs-non-clustered-proxy.jpg)

{{% /align%}}

The decision of working directly with a cluster member or against the whole cluster is done in the `GigaSpace` level. The `GigaSpacesFactoryBean` provides a clustered flag with the following logic as the default value: If the space is started in embedded mode (for example, `/./space`), the clustered flag is set to `false`. When the space is looked up in a remote protocol (i.e. `jini://*/*/space`, the clustered flag is set to `true`.

You can use the `clustered` property to control this behavior or use the API to use a non-clustered embedded proxy to create a clustered proxy. This allows the collocated business logic to access the entire cluster to perform cluster wide operations. Clustered and Non-Clustered proxies may be used with a [Task](../dev-java/task-execution-overview.html), [Service](../dev-java/executor-based-remoting.html), [Notify Container](../dev-java/notify-container-overview.html) , [Polling Container](../dev-java/polling-container-overview.html) and any other Collocated business logic.

# Creating a Clustered Proxy
You may use Spring based configuration or API to create a Clustered Proxy.

## Using Spring
When using a Spring based `pu.xml` to construct the [GigaSpace](../dev-java/the-gigaspace-interface-overview.html) bean to be injected into the other relevant beans the following should be used to create a clustered and a non-clustered `GigaSpace` bean:

```xml
<os-core:embedded-space id="space" space-name="mySpace"/>
<os-core:giga-space id="nonClusteredGigaSpace" space="space"/>
<os-core:giga-space id="clusteredGigaSpace" space="space" clustered="true"/>
```

## Using an API
The `GigaSpace.getClustered()` method allows you to get a cluster wide proxy from a non-clustered proxy. In such a case the `@GigaSpaceContext` should be used to inject the non-clustered `GigaSpace` bean or the `@TaskGigaSpace` when executing a `Task` that is invoked at the space side.

Another option is to use the `GigaSpaceConfigurer`:

```java
GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer("space")).clustered(true).gigaSpace();
```

An example of a Remoting Service with a clustered and non-clustered proxy:

```java
@RemotingService
public class MyService implements ClusterInfoAware, IMyService{
@GigaSpaceContext(name="gigaSpaceEmbedNonClustered")
GigaSpace gigaSpaceEmbedNonClustered;
GigaSpace gigaSpaceClustered;
...
}
```

Here is how the clustered proxy is constructed:

```java
@PostPrimary
public void postPrimary() {
gigaSpaceClustered=gigaSpaceEmbedNonClustered.getClustered();
...
}
```

An example of a `DistributedTask` implementation with a clustered and non-clustered proxy:

```java
public class MyTask implements ClusterInfoAware , DistributedTask<Integer, Integer>{
@TaskGigaSpace
transient GigaSpace gigaSpaceEmbedNonClustered;
transient GigaSpace gigaSpaceClustered;
...
}
```

Here is how the clustered proxy is constructed:

```java
public Integer execute() throws Exception {
gigaSpaceClustered=gigaSpaceEmbedNonClustered.getClustered();
...
}
```

# Why Do I @ant a Clustered Proxy?

A clustered proxy provides you access to the entire cluster rather than a specific partition. Once you will need to read/write/take/clear/execute against the entire cluster you must use a clustered proxy.

# Performance Considerations of a Clustered Proxy

With a remote client, serialization and network activity will impact the performance when writing/reading data. With a collocated non-clustered proxy serialization and network activity will not happen when the client code interacts with the embedded space so these should not be considered, but they will be considered when the embedded space has a backup pair. Here the replication activity will be impacted by the serialization and network activity. Still, this would be a single network hop rather than two when having a remote client.

# Protective Mode Exception when Using a Non-Clustered Proxy

To protect a user using a non-clustered proxy from writing or updating objects using a wrong routing field value, GigaSpaces runs by default in Protective Mode. This will throw the `ProtectiveModeException` and block users from getting into such scenarios. You can turn off this behavior by using the following: `com.gs.protectiveMode.wrongEntryRoutingUsage=false`.

The `com.gigaspaces.client.protective.ProtectiveModeException` is thrown when:

- Writing a new object using a wrong routing field with a non-clustered proxy.
- Changing/Updating an existing object modifying its routing field to a different value which doesn't match the partition it resides in.

The error message looks like this:

**The ProtectiveModeException Exception**<br>
Exception in thread "main" com.gigaspaces.client.protective.ProtectiveModeException: Operation is rejected - the routing value in the written entry of type 'com.test.Data' does not match this space partition id. The value within the entry's routing property named 'id' is 1 which matches partition id 2 while current partition id is 1. Having a mismatching routing value would result in a remote client not being able to locate this entry as the routing value will not match the partition the entry is located. (you can disable this protection, though it is not recommended, by setting the following system property: com.gs.protectiveMode.wrongEntryRoutingUsage=false)


# Using Both Clustered and Non-clustered Proxies

There are scenarios where a collocated component (Remote Service, Task, Event Container) will have both a clustered proxy and a non-clustered embedded proxy. The clustered proxy will be used to interact with the entire cluster where the non-clustered embedded proxy will be used to bootstrap the space and interact only with the local partition.

## Example

With our examples below we will use this simple Space Class:

```java
import com.gigaspaces.annotation.pojo.SpaceId;

public class Data {
public Data(){}
Integer id;

@SpaceId(autoGenerate=false)
public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}
}
```

{{%note%}}
When there is no explicit `@SpaceRouting` declared, the method annotated as the `@SpaceId` is used as the `@SpaceRouting` method.
{{%/note%}}

## Remote Service Usage of a Clustered and a Non-Clustered Proxy

With this example the `pu.xml` includes the following:


```xml
<os-core:embedded-space id="spaceEmbed" space-name="mySpace" />
<os-core:giga-space id="gigaSpaceEmbedNonClustered" space="spaceEmbed" />
<os-core:annotation-support />
<os-core:giga-space-context/>
<context:component-scan base-package="com.test"/>
<os-remoting:annotation-support />
<os-remoting:service-exporter id="serviceExporter" />
```

Our Service using the [@PostPrimary](../dev-java/the-space-notifications.html) to decorate the method that constructs the clustered proxy from the non-clustered proxy and also the [@ClusterInfoContext](../dev-java/obtaining-cluster-information.html) that provides information about the cluster topology and the local partition ID.

Here is how the service interface looks like:

```java
public interface IMyService {
public String myMethod(@Routing Integer routing);
}
```

The `@Routing` annotation is used to decorate the method specified as the routing field.

Here is how the service implementation looks like:

```java
package com.test;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.cluster.ClusterInfo;
import org.openspaces.core.cluster.ClusterInfoContext;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.core.space.mode.PostPrimary;

@RemotingService
public class MyService implements IMyService{

@GigaSpaceContext(name="gigaSpaceEmbedNonClustered")
GigaSpace gigaSpaceEmbedNonClustered;
GigaSpace gigaSpaceClustered;
@ClusterInfoContext
ClusterInfo clusterInfo;

@PostPrimary
public void postPrimary() {
String preMes = "From Service - partition " + clusterInfo.getInstanceId() ;
System.out.println(preMes+ " - Cluster info " + clusterInfo);
System.out.println(preMes + " writing object using embedded Non-Clustered proxy" );
Data d = new Data();
// We write a single dummy object to the local partition. Since the ID is also the routing field we are OK.
d.setId(clusterInfo.getInstanceId()-1); // Partition id = InstanceId - 1
gigaSpaceEmbedNonClustered.write(d);

System.out.println(preMes+ " Getting Remote Clustered proxy from the embedded Non-Clustered");
gigaSpaceClustered=gigaSpaceEmbedNonClustered.getClustered();
System.out.println(preMes+ " - gigaSpaceEmbed - total visible objects:" + gigaSpaceEmbedNonClustered.count(null));
}

public String myMethod(Integer routing)
{
int countEmbed = gigaSpaceClustered.count(null);
int countRemote = gigaSpaceEmbedNonClustered.count(null);

String mes = "Service call - routing " + routing + " partition " +
clusterInfo.getInstanceId() + " gigaSpaceRemote - total visible objects:" + countEmbed;
mes = mes + "\nService call - routing " + routing + " partition " +
clusterInfo.getInstanceId() + " gigaSpaceEmbed - total visible objects:" + countRemote;
return mes;
}
}
```

The service is called using the following:

```java
    GigaSpace space = new GigaSpaceConfigurer(new SpaceProxyConfigurer("space")).gigaSpace();

    IMyService service = new ExecutorRemotingProxyConfigurer<IMyService>(space , IMyService.class).proxy();
    String mes1 = service.myMethod(0);
    String mes2 = service.myMethod(1);
    System.out.println(mes1);
    System.out.println(mes2);
```

The service output:

```bash
...
From Service - partition 1 - Cluster info name[null] schema[partitioned] numberOfInstances[2] numberOfBackups[null] instanceId[1] backupId[null]
From Service - partition 1 writing object using embedded Non-Clustered proxy
From Service - partition 1 Getting Remote Clustered proxy from the embedded Non-Clustered
From Service - partition 1 - gigaSpaceEmbed - total visible objects:1
Got event! Data [id=0]
....
From Service - partition 2 - Cluster info name[null] schema[partitioned] numberOfInstances[2] numberOfBackups[null] instanceId[2] backupId[null]
From Service - partition 2 writing object using embedded Non-Clustered proxy
From Service - partition 2 Getting Remote Clustered proxy from the embedded Non-Clustered
Got event! Data [id=1]
From Service - partition 2 - gigaSpaceEmbed - total visible objects:1
...
partition 1 gigaSpaceRemote - total visible objects:2
partition 2 gigaSpaceRemote - total visible objects:2
```

The client output:

```bash
Service call - routing 0 partition 1 gigaSpaceRemote - total visible objects:2
Service call - routing 0 partition 1 gigaSpaceEmbed - total visible objects:1
Service call - routing 1 partition 2 gigaSpaceRemote - total visible objects:2
Service call - routing 1 partition 2 gigaSpaceEmbed - total visible objects:1
```

## DistributedTask Usage of a Clustered and a Non-Clustered Proxy

Our `DistributedTask` implements [ClusterInfoAware](../dev-java/obtaining-cluster-information.html). This allows it to be injected with the `ClusterInfo` that provides information about the cluster topology and the local partition ID. Here is how the `DistributedTask` looks:

The `pu.xml` includes the following:

```xml
<os-core:embedded-space id="spaceEmbed" space-name="mySpace" />
<os-core:giga-space id="gigaSpaceEmbedNonClustered" space="spaceEmbed" />
```

The `DistributedTask` looks like this:

```java
package com.test;

import java.util.List;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.cluster.ClusterInfo;
import org.openspaces.core.cluster.ClusterInfoAware;
import org.openspaces.core.executor.DistributedTask;
import org.openspaces.core.executor.TaskGigaSpace;

import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.async.AsyncResult;

public class MyTask implements ClusterInfoAware , DistributedTask<Integer, Integer>{
@TaskGigaSpace
transient GigaSpace gigaSpaceEmbedNonClustered;
transient GigaSpace gigaSpaceClustered;
transient ClusterInfo clusterInfo;
int routing;

public void setClusterInfo(ClusterInfo clusterInfo ) {
this.clusterInfo = clusterInfo;
}

public Integer execute() throws Exception {
String preMes = "From Task Execute - partition " + clusterInfo.getInstanceId() ;
System.out.println(preMes+ " - Cluster info " + clusterInfo);
System.out.println(preMes + " writing object using embedded Non-Clustered proxy" );
Data d = new Data();
// We write a single dummy object to the local partition. Since the ID is also the routing field we are OK.
d.setId(clusterInfo.getInstanceId()-1); // Partition id = InstanceId - 1
gigaSpaceEmbedNonClustered.write(d);
System.out.println(preMes+ " Getting Remote Clustered proxy from the embedded Non-Clustered");
gigaSpaceClustered= gigaSpaceEmbedNonClustered.getClustered();
System.out.println(preMes+ " - gigaSpaceEmbed - total visible objects:" +
gigaSpaceEmbedNonClustered.count(null));
System.out.println(preMes + " gigaSpaceRemote - total visible objects:" +
gigaSpaceClustered.count(null));
return null;
}

public Integer reduce(List<AsyncResult<Integer>> arg0) throws Exception {
return null;
}

@SpaceRouting
public int getRouting() {
return routing;
}

public void setRouting(int routing) {
this.routing = routing;
}
}
```


```java
GigaSpace space = new GigaSpaceConfigurer(new SpaceProxyConfigurer("space")).gigaSpace();
space.execute(new MyTask());
```

The `DistributedTask` execute output:

```java
From Task Execute - partition 1 - Cluster info name[null] schema[partitioned] numberOfInstances[2] numberOfBackups[null] instanceId[1] backupId[null]
From Task Execute - partition 2 - Cluster info name[null] schema[partitioned] numberOfInstances[2] numberOfBackups[null] instanceId[2] backupId[null]
From Task Execute - partition 1 writing object using embedded Non-Clustered proxy
From Task Execute - partition 2 writing object using embedded Non-Clustered proxy
From Task Execute - partition 1 Getting Remote Clustered proxy from the embedded Non-Clustered
From Task Execute - partition 2 Getting Remote Clustered proxy from the embedded Non-Clustered
From Task Execute - partition 2 - gigaSpaceEmbed - total visible objects:1
From Task Execute - partition 1 - gigaSpaceEmbed - total visible objects:1
From Task Execute - partition 1 gigaSpaceRemote - total visible objects:2
From Task Execute - partition 2 gigaSpaceRemote - total visible objects:2
```

## Event Container Usage of a Clustered and a Non-Clustered Proxy

With this example the `pu.xml` includes the following:

```xml
<os-core:embedded-space id="spaceEmbed" space-name="mySpace" />
<os-core:giga-space id="gigaSpaceEmbedNonClustered" space="spaceEmbed" />
<os-events:annotation-support />
<os-core:annotation-support />
<os-core:giga-space-context/>
<context:component-scan base-package="com.test"/>
```

Our Event Container (notify container) uses the [@PostPrimary](../dev-java/the-space-notifications.html#Primary Backup Notifications) to decorate the method that constructs the clustered proxy from the non-clustered proxy and also the [@ClusterInfoContext](../dev-java/obtaining-cluster-information.html) that provides information about the cluster topology and the local partition ID.

Here is how the event container looks:

```java
package com.test;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.cluster.ClusterInfo;
import org.openspaces.core.cluster.ClusterInfoAware;
import org.openspaces.core.cluster.ClusterInfoContext;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.core.space.mode.PostPrimary;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.Notify;

@EventDriven @Notify()
public class MyNotifyContainer {
@GigaSpaceContext(name="gigaSpaceEmbedNonClustered")
GigaSpace gigaSpaceEmbedNonClustered;
GigaSpace gigaSpaceClustered;

@ClusterInfoContext
ClusterInfo clusterInfo;

@EventTemplate
Data unprocessedData() {
Data template = new Data();
return template;
}

@SpaceDataEvent
public void eventListener(Data event) {
 //process Data here
 String preMes = "From Notify Container - partition " + clusterInfo.getInstanceId() ;
 System.out.println("Got event! " + event);
 System.out.println(preMes + " Counting total space objects using a Clustered proxy" );
 gigaSpaceEmbedNonClustered.count(null);
}

@PostPrimary
public void postPrimary() {
 String preMes = "From Notify Container - partition " + clusterInfo.getInstanceId() ;
 System.out.println(preMes+ " - Cluster info " + clusterInfo);
 System.out.println(preMes+ " Getting Remote Clustered proxy from the embedded Non-Clustered");
 gigaSpaceClustered= gigaSpaceEmbedNonClustered.getClustered();
}
}
```


```java
GigaSpace space = new GigaSpaceConfigurer(new SpaceProxyConfigurer("space")).gigaSpace();
Data d = new Data();
d.setId(0);
space.write(d);
d.setId(1);
space.write(d);
```

Event Container output:

```bash
...
From Notify Container - partition 1 - Cluster info name[null] schema[partitioned] numberOfInstances[2] numberOfBackups[null] instanceId[1] backupId[null]
From Notify Container - partition 1 Getting Remote Clustered proxy from the embedded Non-Clustered
...
From Notify Container - partition 2 - Cluster info name[null] schema[partitioned] numberOfInstances[2] numberOfBackups[null] instanceId[2] backupId[null]
From Notify Container - partition 2 Getting Remote Clustered proxy from the embedded Non-Clustered
....
Got event! Data [id=0]
From Notify Container - partition 1 Counting total space objects using a Clustered proxy :1
Got event! Data [id=1]
From Notify Container - partition 2 Counting total space objects using a Clustered proxy :1
```

# Writing New Objects from Co-located Business Logic

When writing new objects from a collocated business logic with a partitioned space, the routing field must "match" the collocated space instance. See below example loading data into the space using a `DistributedTask` injected with a collocated proxy.

With this example the routing field is a `String` data type. An `Integer` as a routing field data type is always preferred. Using such approach will make sure you will not get an error since *GigaSpaces will block writing objects with the wrong routing field value into the space* when a collocated proxy is used.

With such an approach you can load large number of objects into the space very quickly since it will be running in parallel across all the partitions. Using `writeMultiple` will make this even faster.


```java
public class LoadTask implements DistributedTask<Integer, Integer> ,ClusterInfoAware{

    int safeABS( int value)
    {
    return value == Integer.MIN_VALUE ? Integer.MAX_VALUE : Math.abs(value);
    }

    public LoadTask(int maxObjects)
    {
     this.maxObjects = maxObjects;
    }
    int maxObjects;
    int routing;

    @TaskGigaSpace
    transient GigaSpace space;

    transient ClusterInfo clusterInfo;

    @Override
    public Integer execute() throws Exception {
        int partitions = clusterInfo.getNumberOfInstances();
        int partitionId = clusterInfo.getInstanceId();

        int count = 0;
        for(int i=0;i<maxObjects;i++)
        {
            String id = "12345678901234567890123456789"+i;
            int targetPartition = safeABS(id.hashCode()) % (partitions);
            if (targetPartition == (partitionId -1))
            {
                count ++;
                MySpaceObject myobj=...
                myobj.setRouting(targetPartition);
                space.write(myobj);
            }
        }
        return null;
    }

    public Integer reduce(List<AsyncResult<Integer>> arg0) throws Exception {
        return null;
    }

    @Override
    public void setClusterInfo(ClusterInfo clusterInfo) {
       this.clusterInfo = clusterInfo;
    }

    public int getMax() {
      return maxObjects;
    }

    public void setMax(int maxObjects) {
      this.maxObjects = maxObjects;
    }

    @SpaceRouting
    public int getRouting() {
       return routing;
    }

    public void setRouting(int routing) {
        this.routing = routing;
    }
}
```
