---
type: post
title:  Storing Partition Summary Information
categories: SBP
parent: data-access-patterns.html
weight: 650
---

|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Dixson Huie| 9.7 | March 2016 |    |    |
 

# Overview
You may have a scenario where computation is done on a partition by partition basis and you wish to save the results, instead of recalculating them each time. This example calculates values per partition and saves it to the current partition. It will work regardless of the number of partitions that are running in the space. The example uses an Executor Service to make the calcuations on each partition. The `ClusterInfo` object is used to get information about the partition the service is currently executing in. The results are then saved to a PartitionSummary object.

The following code demonstrates Space Based Remoting, aka Service Executor. A complete example can be found [here](/attachment_files/sbp/Store-Partition-Summary.zip).

{{% tabs %}}

{{% tab "Service Interface" %}}

```java
public interface ISummary {
    public void summary();
}
```

{{% /tab %}}

{{% tab "Service Implementation" %}}
The Service Implementation includes the code that is to be run on each partition and saves the summary information. The `ClusterInfo` object is used to gather the partition information.

```java
package com.samples.remoting;

import javax.annotation.Resource;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.cluster.ClusterInfo;
import org.openspaces.core.cluster.ClusterInfoContext;
import org.openspaces.remoting.RemotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.samples.model.ISummary;
import com.samples.model.PartitionSummary;

@RemotingService
@Component
public class SummaryService implements ISummary {

    
    final Logger logger = LoggerFactory.getLogger(SummaryService.class);
    
    @Resource
    GigaSpace gigaSpace;
    
    @ClusterInfoContext 
    private ClusterInfo clusterInfo; 
    
    @Override
    public void summary() {
        Integer instanceId = clusterInfo.getInstanceId();
        PartitionSummary partitionSummary = new PartitionSummary();
        partitionSummary.setId(instanceId);
        partitionSummary.setRoutingId(instanceId);
        
        logger.info("instanceId is " + instanceId);
        // ... do calculations ... save to PartitionSummary object
        gigaSpace.write(partitionSummary);
        
    }

}

```
{{% /tab %}}

{{% tab "The Client" %}}
This is how a client might invoke the service:

```java
        SpaceProxyConfigurer spaceProxyConfigurer = new SpaceProxyConfigurer(spaceName);
        gigaSpace = new GigaSpaceConfigurer(spaceProxyConfigurer).gigaSpace();

        iSummary = new ExecutorRemotingProxyConfigurer<ISummary>(gigaSpace, ISummary.class).broadcast(true).proxy();
        
        iSummary.summary();

```
{{% /tab %}}

{{% /tabs %}}

# ProtectiveMode Exception
The second part of this example explains how to correct the `ProtectiveMode` exception that may occur.

In XAP 9.7, a `ProtectiveMode` was added to protect against writing an object directly to a partition that is assigned the wrong routing value.

If an application writes directly to one of the partitions and assigns the wrong routing value you’ll get the following exception:

```java
com.gigaspaces.client.protective.ProtectiveModeException: Operation is rejected - the routing value in the written entry of type 'PartitionSummary' does not match this space partition id. The value within the entry's routing property named 'routingId' is 1 which matches partition id 2 while current partition id is 1...
```

This ProtectiveMode Exception can be suppressed, by setting the `-Dcom.gs.protectiveMode.wrongEntryRoutingUsage=false` System property. However, the code example given corrects the `ProtectiveMode` exception, without suppressing exception message. This is done by adjusting the calculation of the routing id field in the PartitionSummary object.

```java
@SpaceClass
public class PartitionSummary implements Serializable {

...

    @SpaceRouting
    public Integer getRoutingId() {
        return routingId;
    }
    public void setRoutingId(Integer routingId) {
        this.routingId = (routingId - 1);   // need to subtract 1 because objects are routed by routing value.hashCode % num. of partitions + 1
    }
```

