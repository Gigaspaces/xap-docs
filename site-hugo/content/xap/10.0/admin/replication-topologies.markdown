---
type: post100
title:  Topologies
categories: XAP100ADM
parent: replication.html
weight: 100
canonical: auto
---

{{% ssummary %}} {{% /ssummary %}}


# Primary Backup Topology

A Primary-Backup topology (also known as Active-Passive) consists of a group of space instances that are part of the same replication group, one of the space instances is elected and serves as the primary space instance of the group. The application interaction is done with the primary instance while the other instances are serving as the backups of the primary. The backup space instances are an exact replica of the primary and if the primary instance fails they are in place to replace it when such failure occurs without losing any data and in a transparent manner to the proxy connected to the spaces. In order to maintain this reliability with no data loss, the default replication mode between the space instances in this key is [Synchronous Replication](./synchronous-replication.html). The common usage of this topology is to have it together with a partitioned space, such that each partition is isolated in terms of replication. This means that each partition consists a **separate** replication group of space instances such that in each partition only one primary will be elected and all the other members of the partition will serve as its backups. There is no replication taking place across partitions. After a primary is elected, each remaining backup member will first recover its state from the primary before becoming available.

{{% refer %}}For more details about the recovery process refer to [Space Instance Recovery](./space-instance-recovery.html){{% /refer %}}

# Primary Backup with a Mirror Topology

This topology extends the previous one by having a mirror service that replicates all the modifying operations (Write/Update/Take in and out of transaction) to an external data source. The most common external data source is a database that is used for storing all some or all of the data in the cluster.
The replication group consists of the elected primary instance, its backup instances and additionally the mirror service instance. In such case, the mirror serves **all of the partitions**, which means it is part of each of the partitions' replication groups. However, each replication bulk from each of the various partitions that is received by the mirror is processed separately. In this topology there is a mixture of two replication modes, one per each target type. By default, replication to the backup space instance is [Synchronous](./synchronous-replication.html), and the replication to the mirror service is [Asynchronous](./asynchronous-replication.html). This is done in order decouple the performance of the space instance from that of the external data source (e.g database). This is commonly known is write behind. An important feature in this context, is that even though the replication to the mirror is asynchronous, it is a reliable in a way that even if the primary space instance fails and did not manage to send all pf its replication queue to the mirror, ones of the remaining instance, which is elected to become primary, will send the missing replication data to the mirror service, and in doing so will prevent **any** data loss at the external data source side.

{{% refer %}}For more details about this topology refer to [Asynchronous Persistency with the Mirror]({{%currentjavaurl%}}/asynchronous-persistency-with-the-mirror.html){{% /refer %}}

# Active-Active Topology

This topology consists of a group of space instances that are part of the same replication group. Unlike primary backup, all of the space instances are active and the application interacts with all of them. The common use case for this topology is to share reference data across instances. Each space instance replicates its data to all the other members, thus making sure that all the space instances will contain the same data set. This topology is more commonly used with [Asynchronous Replication](./asynchronous-replication.html). Since each space instance is active, it is up to the application to make sure that there is no [conflicting operations](./replication-operations.html#Conflicting Operation), by not updating/changing the same object in two different space instances at the same time. With this topology, each new space instance that is started, first tries to find an already existing active member in the replication group, and if such a member is found it will first perform recovery from that member before becoming active and available.

{{% refer %}}For more details about the recovery process refer to [Space Instance Recovery](./space-instance-recovery.html){{% /refer %}}

## Load Balancing

Since all the space instances hold the same data, there are multiple load balancing strategies the space proxy can choose from. By default a **sticky** strategy is used, i.e. all operations are performed on the same space instance. For more information on load balance strategy selection and configuration, see [Proxy Connectivity]({{%currentadmurl%}}/tuning-proxy-connectivity.html#Load balancing).
