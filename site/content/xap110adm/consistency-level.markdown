---
type: post110
title:  Consistency Level
categories: XAP110ADM
parent: data-grid-clustering.html
weight: 100
---

{{% ssummary %}} {{% /ssummary %}}



In some scenarios, one may want data modifying operations (write , update , change , remove) not to be accepted if temporarily that operation cannot be replicated to multiple members, e.g. the backup space instance. This behavior can be specified by changing the default consistency level of the space. Consistency level affects primary backup topologies and it is used to specify how many of each partition members should be available before allowing to modify the state of the data in side the space. Modification are operations which are replicated, such as write, take etc.

# Consistency Levels


|Level|Description|
|:----|:----------|
|ANY|Ensure that at least 1 member is available, otherwise the modification is rejected and a `ConsistencyLevelViolationException` is thrown. In a primary backup topology that member is the primary (Default).|
|QUORUM|Ensure that at least N / 2 + 1 members are available, otherwise the modification is rejected and a `ConsistencyLevelViolationException` is thrown. In a primary backup topology one of the members is the primary, N includes the primary, in X,2 topology this means at least the primary and 1 backup is available (in X,1 it is equivalent to ALL).|
|ALL|Ensure that all members are available, otherwise the modification is rejected and a `ConsistencyLevelViolationException` is thrown.|

1. Currently this is supported only in partitioned topology.
1. The consistency level is checked per partition, which means that operation which is routed to a partition which satisfy the consistency level will be accepted while other operation which is delegated to a partition that doesn't satisfy the consistency level will be rejected.
1. A backup is considered available if it is connected to the primary space instance and the replication state between the two is operating in fully synchronous state.

# Usage

The space should be configured with the following property, specifying which consistency level is required:


```java
cluster-config.groups.group.repl-policy.sync-replication.consistency-level=<any/all/quorom>
```

