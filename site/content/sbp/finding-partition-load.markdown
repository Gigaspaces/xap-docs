---
type: post
title:  Finding Partition Load
categories: SBP
parent: data-access-patterns.html
weight: 600
---



{{% tip %}}
**Summary:**  This page describes two ways to determine the partition load in a Gigaspaces grid programmatically. <br/>
**Author**: Joe Ottinger, Technology Evangelist, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 7.1.2<br/>


{{% /tip %}}

# Overview
When using a GigaSpaces cluster as a task queue there are times you will need to determine how the space instances are loaded. This could be to determine where you can route the next task (minimum load partition/instance) or where you want to launch more processors (heavily loaded partition/instance).

There are couple of approaches you could use to identify these partitions: [task executors](#taskexecutor) and an [executor service](#serviceexecutor). You can also see the [Executors example](./map-reduce-pattern---executors-example.html) page for other similar examples.

{{% anchor taskexecutor %}}

# Using a Task Executor

The [first example](/attachment_files/sbp/GetMinLoadPartition-TaskExecutors.zip) shows an implementation of DistributedTask that can be used in scenarios where you want to run ad-hoc queries. Usage instructions are similar to the Task Executors Example. Business logic does not have to be on the cluster, it is dynamically transported to the server side and executed remotely.

Example is trying to find a partition with least number of objects and uses GigaSpaces SpaceRuntimeInfo API to get the count of objects. This API is lot faster compared to the count API and is preferred way of getting object counts.

{{%tabs%}}

{{%tab "  Client Code "%}}


```java
...
IJSpace ijs = new UrlSpaceConfigurer("jini://*/*/space").space();
GigaSpace gigaSpace = new GigaSpaceConfigurer(ijs).gigaSpace();

AsyncFuture<Integer> future = gigaSpace.execute(new MyDistributedTask());
try {
  Integer result = future.get();

  System.out.println(" Partition :" + result.intValue());
} catch (InterruptedException e) {
  e.printStackTrace();
} catch (ExecutionException e) {
  e.printStackTrace();
}
...
```

{{% /tab %}}

{{%tab "  Task Implementation "%}}


```java
import java.rmi.RemoteException;
import java.util.List;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.cluster.ClusterInfo;
import org.openspaces.core.cluster.ClusterInfoAware;
import org.openspaces.core.executor.DistributedTask;
import org.openspaces.core.executor.TaskGigaSpace;

import com.gigaspaces.async.AsyncResult;
import com.j_spaces.core.IJSpace;
import com.j_spaces.core.admin.IRemoteJSpaceAdmin;
import com.j_spaces.core.admin.SpaceRuntimeInfo;

public class MyDistributedTask implements
    DistributedTask<PartitionCount, Integer>, ClusterInfoAware {

  @TaskGigaSpace
  private transient GigaSpace gigaSpace;
  private transient ClusterInfo clusterInfo;

  @Override
  public PartitionCount execute() throws Exception {
    Integer totalCount = 0;
    IJSpace ijs = gigaSpace.getSpace();
    PartitionCount pc = new PartitionCount();
    pc.setCount(0);
    pc.setPartitionId(0);

    // pc.setPartitionId(clusterInfo.getInstanceId());
    try {
      IRemoteJSpaceAdmin spaceAdmin = (IRemoteJSpaceAdmin) ijs.getAdmin();
      SpaceRuntimeInfo rtInfo = spaceAdmin.getRuntimeInfo();
      for (int i = 0; i < rtInfo.m_ClassNames.size(); i++) {
        String className = rtInfo.m_ClassNames.get(i);
        Integer count = rtInfo.m_NumOFEntries.get(i);

        System.out.println(System.currentTimeMillis()
                           + "Partition: " + clusterInfo.getInstanceId()
                           + " ClassName: " + className + ", Number of entries: "
                           + count);

        totalCount += rtInfo.m_NumOFEntries.get(i);
      }

      System.out.println(System.currentTimeMillis()
                         + "Partition: " + clusterInfo.getInstanceId()
                         + ", Total entries: " + totalCount);

      pc.setCount(totalCount);
      pc.setPartitionId(clusterInfo.getInstanceId());
    } catch (RemoteException e) {
      e.printStackTrace();
    }

    return pc;
  }
  @Override
  public Integer reduce(List<AsyncResult<PartitionCount>> results)
    throws Exception {
    PartitionCount minPart = null;
    for (AsyncResult<PartitionCount> result : results) {
      if (result.getException() != null) {
        throw result.getException();
      }
      if (minPart != null) {
        if (minPart.getCount() > result.getResult().getCount()) {
          minPart = result.getResult();
        }
      } else {
        minPart = result.getResult();
      }
    }
    return minPart.getPartitionId();
  }

  @Override
  public void setClusterInfo(ClusterInfo clusterInfo) {
    this.clusterInfo = clusterInfo;
  }
}
```

{{% /tab %}}

{{% /tabs %}}

{{% anchor serviceexecutor %}}

# Using an Executor Service

Another [example](/attachment_files/sbp/GetMinLoadPartition-ExecutorService.zip) shows an implementation using Executor Service. This approach should be used when this functionality is intrinsic part of the system and not needed on ad-hoc basis. Usage instructions are similar to the [Executor Service Example](./map-reduce-pattern---executors-example.html#ExecutorsExample-ServiceExecutorsExample).

In this example, we're trying to find the partition with the least number of objects, using GigaSpaces' SpaceRuntimeInfo API to get the count of objects. This API is lot faster than the count API and is the preferred way of getting object counts.

{{%tabs%}}

{{%tab "  Service Interface "%}}
The Service Interface includes only one method, used to invoke the Service method in Synchronous mode:


```java
public interface IDataProcessor {
  PartitionCount processData();
}
```

{{% /tab %}}

{{%tab "  Service Implementation "%}}
The Service Implementation includes business logic to determine the load (in this case number of objects in the partition):


```java
import java.rmi.RemoteException;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.cluster.ClusterInfo;
import org.openspaces.core.cluster.ClusterInfoContext;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.remoting.RemotingService;

import com.j_spaces.core.IJSpace;
import com.j_spaces.core.admin.IRemoteJSpaceAdmin;
import com.j_spaces.core.admin.SpaceRuntimeInfo;

@RemotingService
public class DataProcessorService implements IDataProcessor {
  @ClusterInfoContext
  public ClusterInfo clusterInfo;

  @GigaSpaceContext
  transient GigaSpace gigaSpace;

  public PartitionCount processData() {
    Integer totalCount = 0;
    IJSpace ijs = gigaSpace.getSpace();
    PartitionCount pc = new PartitionCount();
    pc.setCount(0);
    pc.setPartitionId(0);

    //pc.setPartitionId(clusterInfo.getInstanceId());

    try {
      IRemoteJSpaceAdmin spaceAdmin = (IRemoteJSpaceAdmin) ijs.getAdmin();
      SpaceRuntimeInfo rtInfo = spaceAdmin.getRuntimeInfo();
      for (int i = 0; i < rtInfo.m_ClassNames.size(); i++) {
        String className = rtInfo.m_ClassNames.get(i);
        Integer count = rtInfo.m_NumOFEntries.get(i);
        System.out.println("ClassName: " + className
                           + ", number of entries: " + count);
        totalCount += rtInfo.m_NumOFEntries.get(i);
      }
      pc.setCount(totalCount);
      pc.setPartitionId(clusterInfo.getInstanceId());
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return pc;
  }
}
```

{{% /tab %}}

{{%tab "  Reducer "%}}
The Result Reducer applies the additional logic (finding the partition with the least number of objects, in this case).


```java
import org.openspaces.remoting.RemoteResultReducer;
import org.openspaces.remoting.SpaceRemotingInvocation;
import org.openspaces.remoting.SpaceRemotingResult;

public class DataProcessorServiceReducer implements RemoteResultReducer<PartitionCount, PartitionCount >{
  public PartitionCount reduce(SpaceRemotingResult<PartitionCount>[] results,
                               SpaceRemotingInvocation sri) throws Exception {
    PartitionCount minPart = null;
    for (SpaceRemotingResult<PartitionCount> result : results) {
      if (result.getException() != null) {
        System.out.println("Error in one of the partitions");
      }
      if (minPart != null) {
          if (minPart.getCount() > result.getResult().getCount()) {
            minPart = result.getResult();
          }
        }
      } else {
        minPart = result.getResult();
      }
    }
    return minPart;
  }
}
```

{{% /tab %}}

{{%tab "  The Client "%}}
This is how a client might invoke the service:


```java
space = new UrlSpaceConfigurer("jini://*/*/space").space();
gigaSpace = new GigaSpaceConfigurer(space).gigaSpace();

dataProcessor = new ExecutorRemotingProxyConfigurer<IDataProcessor>(
                gigaSpace, IDataProcessor.class).broadcast(
                new DataProcessorServiceReducer()).proxy();
PartitionCount result = dataProcessor.processData();
```

{{% /tab %}}

{{% /tabs %}}
