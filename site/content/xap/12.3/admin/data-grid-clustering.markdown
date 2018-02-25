---
type: post123
title:  Data Grid Clustering
categories: XAP123ADM, OSS
weight: 300
parent: none
---

# Overview

XAP's data grid clustering, scalability and high availability are based on the following concepts:


**Scalability**

- Data is segmented into [partitions](./data-partitioning.html). Each partition includes a primary instance and ZERO or more backup instances. A partition (primary or a backup) is hosted within a single [Grid Service Container (GSC)]({{%currentadmurl%}}/the-runtime-environment.html).
- Data access is load balanced across the different partitions, using a routing field or a [routing value](../dev-java/routing-in-partitioned-spaces.html) specified as part of the Space object or as part of the read/execute request. This allows the application to control the data distribution in a transparent manner.
- Some operations, such as batch read or [execute](../dev-java/task-execution-overview.html), support [map-reduce](../dev-java/space-based-remoting-overview.html) behavior. This allows the application to access multiple partitions in parallel.
- The maximum partition  size is the GSC heap size. XAP supports a large heap size (up to 100GB in RAM). A GSC may host multiple partitions (primary or backup instances).
- A data grid may have an unlimited number of partitions. In reality, the number of partitions will be of the same magnitude as the number of GSCs or servers that are running the data grid. This allows the data grid to scale dynamically and re-balance itself across additional GSCs.
- The number of data grid partitions is determined at deployment time. The number of GSCs hosting the data grid partitions is dynamic and may change during runtime. It can scale in an [elastic](../dev-java/elastic-processing-unit-overview.html) manner when using the ESM.
 

**High Availability**

- Data has high availability using [synchronous replication](./synchronous-replication.html) to remote in-memory backups.
- Backup instances are always running on physical machines other than the machines running primary instances.
- Persistency to database/disk is done in a reliable [asynchronous manner](../dev-java/asynchronous-persistency-with-the-mirror.html).
- When an instance of the system [fails](./failover.html), a new one is recreated on the fly on an available machine. If the failed instance is a primary, the existing backup becomes the new primary and a another backup is created.
- You can have more than one backup copy per partition.
- Backup can be on the LAN or WAN. For remote WAN, a special replication module called the [replication Gateway](../dev-java/multi-site-replication-overview.html ) is provided.
- Backup instances cannot be accessed by clients for read/write. This ensures total data consistency and prevents conflicts.
- When a backup instance is not available, the primary instance logs all activities (on file or [overflow to disk](./controlling-the-replication-redo-log.html)) and sends them to the backup instance when it becomes available (only the delta). If If there is a long disconnection time, then a total recovery of the backup is conducted.
- The transaction boundary is preserved when data is replicated from a primary instance to the backup instance, when persisting the data or when replicating the data to a remote site over the WAN ([WAN Gateway](../dev-java/multi-site-replication-overview.html)).
 

# Consistency

## Under Concurrent Updates

To ensure consistency when there are concurrent updates on the same data record, each individual record is mapped to a single logical partition at each given point in time.  To ensure scalability, different records of the same logical table are written to multiple partitions in parallel, as described above. Each partition supports the various locking semantics (pessimistic, optimistic (versioning), dirty-read) etc. to control the concurrent access of the same record within the context of a single partition.

## Between Two or More Replicas

To ensure continuous high availability, XAP keeps one or more copies of the data.

In asynchronous replication, there may be scenarios where read and write operations hit two different nodes at the same time, and end up reading two separate versions of the same data. There are various algorithms available to handle these scenarios.

XAP uses synchronous backup to avoid the above-described scenario.  The performance overhead of synchronous replication is fixed, and is not related to the size of the cluster (each partition replicates data only to its backup replica). Data replication to the database is kept asynchronous to reduce the overhead of writing to disk.

# Transaction Consistency

Single operations or groups of operations can be executed under transactions. This ensures that the ACID properties are preserved. Transactions can be made local to each partition. If local, they are bound to the scope of a single partition and are highly optimized in terms of performance. Transactions can also span between nodes, however in this case the overhead is higher.

# Ordering

All operations are ordered based on the time they were written. This is specifically relevant to ensure the consistency between the in-memory cluster and the long-term storage, which is updated asynchronously.

# Availability

## Primary Failure

XAP keeps one or more replica nodes as a hot backup. The hot backup takes over immediately if a primary node fails. The hot backup nodes use synchronous replication to ensure that there is no data loss before failover takes place.

## Backup Failure

When a backup node fails, the primary continues to serve requests and log the operation in a redo-log. In parallel, a new backup is provisioned on demand to take over from the failed backup. This involves a provisioning process in which a new backup is created, and a recovery process during which the backup gets its state.

## Failure of Multiple Nodes

To increase availability, some of the NoSQL variants suggest at least three or more replicas per partition. This enables handling simultaneous failure of multiple nodes. This topology comes with a huge overhead; for each terabyte of data, there are two terabytes of redundant information for backup purposes. Additionally (and equally importantly), there is the consistent overhead of keeping all the replicas up to date.

An alternative approach is to use **on-demand backups**. On-demand backups are provisioned automatically as soon as one of the nodes fails. If spare capacity exists within the current pool of machines, the backup is provisioned to an alternate machine within the existing pool. This process can take up to a few seconds, depending on the amount of data per partition. If no machine is available, a completely new machine is started, and  new backup is provisioned to the new machine. The process of starting a new machine with its backup can take several minutes.

As soon the node starts, it first initiates a primary election protocol to assign the master node within its group, and only then boots up. The startup process includes a recovery stage, during which the node recovers its state from either the master node or the available replicas. The source node also stores all the updates that occurred from the time the recovery began in a redo log, and replays all updates to fill in the gap that was created when the node started its recovery process.

## Client Failure

Clients use a cluster-aware proxy to communicate with the cluster. This smart proxy ensures that a write or read operation is always routed to one of the available partitions. The routing happens implicitly, so the client is not exposed to a failover scenario.

## Lookup Discovery Protocol

The XAP cluster discovery mechanism is based on the Jini specification. Services use the discovery protocol to find nodes within the cluster, and to share the cluster state among all the nodes.

# Network Partition Tolerance

## Between Primary and Backup

If the connection between two nodes fails, the primary node logs all the transactions into a FIFO queue known as the redo log. As soon as communication betwween the nodes is re-established, all the data gets replayed to the backup. If the backup fails completely, the system starts a new instance as described above.

## With Long-Term Persistency

If communication with the long-term persistency data store fails, the replica logs all the operations until the connection is re-established. The log is also replicated to a backup node, to ensure that data isn't lost if the primary partition fails before the data was successfully committed to long-term data storage.

## Between Two or More Data Center Sites

The standard reference scenario for partition tolerance is one in which two sites reside in two separate geographic locations, and continue to work independently in case of network partition. Howevet, there are actually two types of multi-site deployments to consider, which are fundamentally different regarding the network partition:

- Disaster Recovery Site - In this scenario, the backup site is often located in close geographical proximity to the primary site, and is backed by a high-bandwidth, redundant network.
- Geographically Distributed Sites over Internet WAN - In this scenario, multiple sites are spread over the globe. Unlike disaster recovery sites, these sites tend to operate under lower SLAs and significantly higher latency.

The recommended approach for dealing with multi-site network partition is fairly different for each of these scenarios.

### Disaster Recovery Site

Disaster recovery sites are very much like any node in a local network, but often reside in different network segments and have higher latency than local networks. Nodes within a cluster can be tagged with a zone tag to mark their data-center affinity. The system can use this information to automatically provision primary and backup nodes between the two sites. It will use the zone tag to ensure that the primary and backup sites are always spread between two data centers.


- Consistency and availability - The same as in a LAN-based deployment (as described above), but the performance and throughput per partition is lower due to the higher latency associated with the synchronous replication.

- Partition Tolerance - The system will continue to function even in a scenario where an entire site fails or become disconnected. The system continues to work through the available site.  The system also rebalances itself as soon as  communication is re-established between both sites, so that the load is once again evenly distributed.

### Geographically Distributed Sites over Internet WAN

In this scenario, nodes are spread over Internet connections where the SLAs are lower and latency is significantly higher. Therefore, it is impractical to treat all of the nodes as a single cluster, as in the Disaster Recover Site scenario. Using a federated cluster deployment (a cluster of clusters) is preferable, with asynchronous replication to synchronize the multiple sites and avoid the extreme latency overhead.

In this mode, we can't achieve all three CAP properties. In most cases, AP is preferred over CA. However, even when AP is selected, it is still possible to maintain a certain level of consistency. The following recommended architecture can provide a reasonable degree of consistency while only slightly compromising on absolute availability and partition tolerance:

- Consistency - Each site is the sole owner of its data, meaning all updates to the data that belong to this site must be delegated to this site. In a more extreme scenario, define a master site that owns all the updates to the data by all other satellite sites. This will ensure consistency and read availability over write availability.
- Availability - Local failures are handled via the same model as described above. Updates on data that belong to other sites are blocked. This trades some degree of availability for consistency.
- Partition tolerance - All sites use asynchronous replication to maintain local copies of the entire data set. If there is a network partition between the sites, each site can continue to read/write its own data even when other sites are not available. A site can also read data on other sites, but cannot change any data that is owned by other sites. This scenario trades some level of partition tolerance for consistency.


{{% note "Note"%}}
Consistency under concurrent updates between two sites can also be handled using versioning. In this case it is assumed that the latest version wins, or that the system delegates the decision for resolving conflicting updates to the application.  This complex scenario is beyond the scope of this topic.
{{% /note %}}

# Handling Extreme Failure

## Multiple Node Failures at the Same Time

In a scenario where both the primary and backup instances of a given partition fail at the same time, this cluster is considered non-functional and operations to it are blocked until the cluster becomes functional again. In this case, it is acceptable to compromise on certain aspects of availability for the sake of consistency.

If your application can tolerate the potential consistency issues associated with this type of failure, you can configure the cluster to route all operations to currently available partitions. This approach trades availability for consistency, and can be useful in streaming scenarios where the system is used to pass through events.

{{% note "Note"%}}
When a proper disaster recovery site setup is implemented where primaries and their backups never run on the same site, the chance of experiencing this type of extreme failure is close to zero, and is the equivalent of having the two sites down at the same time.
{{% /note %}}

## Recovery from Total Failure

When the entire system crashes, it boots itself either from the long-term persistent storage or from a snapshot (new). If this happens, some of the data that was kept in memory and has not yet been delivered to the long-term persistent storage may be lost.

In a disaster recovery setup, this type of event can happen only when both sites go down at the same time.  In this case, the system recovers using the data that was last updated in the long-term storage.

# Additional Resources


{{%exurl "NoCAP" "http://natishalom.typepad.com/nati_shaloms_blog/2010/10/nocap.html" %}}<br>

{{%exurl "NoCAP - Part II Availability and Partition tolerance" "http://natishalom.typepad.com/nati_shaloms_blog/2010/11/nocap-part-ii-availability-and-partition-tolerance.html" %}}<br>

{{%exurl "NoCAP - Part III - GigaSpaces clustering explained" "http://natishalom.typepad.com/nati_shaloms_blog/2010/11/nocap-part-iii-gigaspaces-clustering-explained.html" %}}<br>

{{%exurl "Data Grid Clustering" "http://www.slideboom.com/presentations/615477/GigaSpaces_HA" %}}



