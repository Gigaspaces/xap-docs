---
type: post110adm
title:  Replication Parameters
categories: XAP110ADM
parent: replication.html
weight: 700
---

{{% ssummary %}} {{% /ssummary %}}

# General Parameters




| Property | Description | Default Value |
|:---------|:------------|:--------------|
| replication-mode | Optional values: **sync**, **async**.{{% wbr %}}- The **async** mode replicates space operations to target space after the client receives the acknowledgment from the source space for the operation.{{% wbr %}}- The **sync** mode replicates space operations to target space before the client receives the acknowledgment from the source space for the operation. The client getting the acknowledgment for the operation only after all target spaces confirms the arrival of the replicated data.| **async** used with the async-replicated schema. {{% wbr %}}   **sync** used with the sync-replicated, primary-backup and partitioned-sync2backup cluster schema|
| policy-type | Optional values:{{% wbr %}}  full-replication - all objects are replicated. {{% wbr %}}  partial-replication - Object that their @SpaceClass configured(replicate = false) will not be replicated. See the [POJO Metadata]({{%currentjavaurl%}}/the-space-configuration.html) page for details. This allows you to control replication at a class base level. | partial-replication |
| repl-find-timeout | Timeout (in milliseconds) to wait for the lookup of a peer space. This parameter applies only when the space is searched in a Jini Lookup Service. | 5000 \[ms\] |
| repl-full-take | If set to **true** the entire object is replicated when take operations is called. If set to **false** only the ID, class information and primitive fields are replicated. This option is valid only when replicating data into a Mirror Service or a backup in non-central DB topology. | false |
| <nobr>replicate-notify-templates</nobr> | Boolean value. If set to true, the notify templates are replicated to the target space. | true |
| trigger-notify-templates | Boolean value. If set to true, the replicated operations will trigger the notify templates and send events to the registered listeners. | false |
| on-conflicting-packets | Enum value. If set to ignore, the conflicting operations are ignored. If set to override the newest operation will override the data in the target. | ignore |

{{%note%}}
Prefix the property with `cluster-config.groups.group.repl-policy.`
{{%/note%}}

# Asynchronous Replication Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| repl-chunk-size | Number of packets transmitted together on the network when the replication event is triggered. The maximum value you can assign for this property is `repl-interval-opers`. | 500 |
| repl-interval-millis | Time (in milliseconds) to wait between replication operations. | 3000 \[ms\] |
| repl-interval-opers | Number of destructive operations to wait before replicating. | 500 |
| <nobr>async-channel-shutdown-timeout</nobr> | Determines how long (in milliseconds) the primary space will wait for pending replication to be replicated to its targets before shutting down.| 300000 \[ms\]  |

{{%info%}}
Prefix the property with `cluster-config.groups.group.repl-policy.async-replication.`
{{%/info%}}

{{% refer %}}For more info refer to [Asynchronous Replication](./asynchronous-replication.html){{% /refer %}}

# Synchronous Replication Parameters




| Property | Description | Default Value |
|:---------|:------------|:--------------|
| throttle-when-inactive | Boolean value. Set to **true** if you want to throttle replicated operations when the channel is in-active (disconnection) | **true** in primary backup **false** in full sync replicated |
| <nobr>max-throttle-tp-when-inactive</nobr> | Integer value. If the above is true, this will specify the maximum operations per second the throttle will maintain when the channel is in-active (disconnection), if the last measured throughput when the channel was active was higher than that, the measured throughput will be used instead. | 50,000 operations/second |
| min-throttle-tp-when-active | Integer value. this specifies the minimum operations per second the throttle can reduce to when the channel is active (during asynchronous state), the throttling when the channel is active is always adapted to the current throughput and size of redolog in order to keep the redolog size decreasing. | 1,000 operations/second |
| target-consume-timeout | The timeout time in milliseconds that the target space waits for consuming replication packets. When the timeout expires, replication channel moves to asynchronous state. | 10000 \[ms\] |

{{%info%}}
Prefix the property with `cluster-config.groups.group.repl-policy.sync-replication.`
{{%/info%}}

{{% refer %}}For more info refer to [Synchronous Replication](./synchronous-replication.html){{% /refer %}}

# Recovery Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| recovery-chunk-size | Integer value. Defines how many operations are recovered is a single batch | 200 |
| <nobr>recovery-thread-pool-size</nobr> | Integer value. Defines how many threads are recovering the data during the snapshot process. | 4 |


{{%info%}}
Prefix the property with `cluster-config.groups.group.repl-policy.`
{{%/info%}}

{{% refer %}}For more info refer to [Space Instance Recovery](./space-instance-recovery.html){{% /refer %}}

# Redo-log Parameters

|      |     |  |
|----|-----|-----|
|* redo-log-capacity | Specifies the total capacity of replication packets the redo log can hold for a standard replication target.|150000|
|* redo-log-memory-capacity | Specifies the maximum number of replication packets the redo log keeps in memory.|150000|
|* redo-log-recovery-capacity | Specifies the total capacity of replication packets the redo log can hold for a standard replication target while it is undergoing a recovery process.|5000000|
|* on-redo-log-capacity-exceeded | See the [Handling an Increasing Redo Log](./controlling-the-replication-redo-log.html#Handling an Increasing Redo Log) for details. | drop-oldest |
|* on-missing-packets | Options: ignore , recover. See the [Handling Dropped Replication Packets](./controlling-the-replication-redo-log.html#Handling Dropped Replication Packets) for details. | recover|
| cluster-config.mirror-service.redo-log-capacity | Specifies the total capacity of replication packets the redo log can hold for a mirror service replication target.|1000000|
| <nobr>cluster-config.mirror-service.on-redo-log-capacity-exceeded</nobr> | See the [Handling an Increasing Redo Log](./controlling-the-replication-redo-log.html#Handling an Increasing Redo Log) for details. | block-operations|

{{%info%}}
* Prefix the property with `cluster-config.groups.group.repl-policy.`
{{%/info%}}

The following parameters are low level configuration that relates to the swap redo log mechanism:


| Space Cluster Property | Description | Default Value|
|:-----------------------|:------------|:-------------|
| flush-buffer-packet-count | Specifies the number of packets buffer size that the swap redo log is using when flushing packets to the disk. | 500 |
| <nobr>swap-redo-log.segment-size</nobr> | Specifies the size in bytes of each swap redo log segment file. | 10MB |
| fetch-buffer-packet-count | Specifies the number of packets buffer size that the swap redo log is using when retrieving packets from the disk to the memory. | 500 |
| max-scan-length | Specifies the maximum allowed scan length in bytes in swap redo log file in order to locate a packet. | 50KB |
| max-open-cursors | Specifies the maximum number of open file descriptor that the swap redo log will use. | 10 |


{{%info%}}
Prefix the property with `cluster-config.groups.group.repl-policy.swap-redo-log.`
{{%/info%}}

{{% refer %}}For more info refer to [Controlling the Replication Redo Log](./controlling-the-replication-redo-log.html){{% /refer %}}

# Mirror Service Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| url | used to locate the Mirror Service. In case you change the name of the Mirror Service specified as part of the Mirror PU, you should modify this parameter value to facilitate the correct Mirror service URL. | jini://*/mirror-service_container/mirror-service |
| bulk-size | The amount of operations to be transmitted in one bulk (in quantity and not actual memory size) from an active IMDG primary to the Mirror Service. | 100 |
| interval-millis | The replication frequency - Replication will happen every `interval-millis` milliseconds | 2000 |
| interval-opers | The replication buffer size - Replication will happen every `interval-opers` operations. | 100 |
| <nobr>on-redo-log-capacity-exceeded</nobr> | Available options:{{% wbr %}}block-operations - all cluster operations that need to be replicated (write/update/take) are blocked until the redo log size decreases below the capacity. (Users get RedoLogCapacityExceededException exceptions while trying to execute these operations.){{% wbr %}}drop-oldest - the oldest packet in the redo log is dropped.{{% wbr %}}See the [Controlling the Replication Redo Log](./controlling-the-replication-redo-log.html) for details. | block-operations |
| redo-log-capacity | Specifies the total capacity of replication packets the redo log can hold for a mirror service replication target.

{{%info%}}
Prefix the property with `cluster-config.mirror-service.`
{{%/info%}}

See the [Controlling the Replication Redo Log](./controlling-the-replication-redo-log.html) for details. | 1000000 |

# Durable Notifications Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| redo-log-durable-notification-capacity | Specifies the total capacity of replication packets the redo log can hold for a durable notification replication target | 150000 |
| <nobr>durable-notification-max-disconnection-time </nobr>| Specifies the maximum amount of time (in milliseconds) the space will wait for the durable notification replication target before it is considered disconnected, after which the target will be dropped. | 300000 |

{{%refer%}}
Prefix the property with `cluster-config.groups.group.repl-policy.`
{{%/refer%}}

# Local View Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| redo-log-local-view-capacity | Specifies the total capacity of replication packets the redo log can hold for a local view replication target | 150000 |
| <nobr>redo-log-local-view-recovery-capacity</nobr> | Specifies the total capacity of replication packets the redo log can hold for a local view replication target while the local view is in recovery state (initial load process)| 1000000 |
| local-view-max-disconnection-time | Specifies the maximum amount of time (in milliseconds) the space will wait for the local view replication target before it is considered disconnected, after which the target will be dropped. | 300000 |

{{%info%}}
Prefix the property with `cluster-config.groups.group.repl-policy.`
{{%/info%}}
