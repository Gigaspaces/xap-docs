---
type: post123
title:  Storing Partition Summary Information
categories: XAP123, OSS
parent: task-execution-overview.html
weight: 300
---

You may have a scenario where computation is done on a partition by partition basis and you wish to save the results, instead of recalculating them each time. This example calculates values per partition and saves it to the current partition. It will work regardless of the number of partitions that are running in the space. The example uses an Executor Service to make the calcuations on each partition. The `ClusterInfo` object is used to get information about the partition the service is currently executing in. The results are then saved to a PartitionSummary object.

The following code demonstrates Space Based Remoting, aka Service Executor. A complete example can be found [here](/download_files/sbp/Store-Partition-Summary.zip).

# Service Interface

Configure the service interface as follows:

```java
public interface ISummary {
    public void summary();
}
```

# Service Implementation

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

# Client

This is how a client might invoke the service:

```java
        SpaceProxyConfigurer spaceProxyConfigurer = new SpaceProxyConfigurer(spaceName);
        gigaSpace = new GigaSpaceConfigurer(spaceProxyConfigurer).gigaSpace();

        iSummary = new ExecutorRemotingProxyConfigurer<ISummary>(gigaSpace, ISummary.class).broadcast(true).proxy();
        
        iSummary.summary();

```

