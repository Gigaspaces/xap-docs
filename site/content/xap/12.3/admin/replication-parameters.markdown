---
type: post123
title:  Replication Parameters
categories: XAP123ADM, OSS
parent: replication.html
weight: 700
---

{{% ssummary %}} {{% /ssummary %}}

# General Parameters




| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.replication-mode | Optional values: **sync**, **async**.{{<wbr>}}- The **async** mode replicates space operations to target space after the client receives the acknowledgment from the source space for the operation.{{<wbr>}}- The **sync** mode replicates space operations to target space before the client receives the acknowledgment from the source space for the operation. The client getting the acknowledgment for the operation only after all target spaces confirms the arrival of the replicated data.| **async** used with the async-replicated schema. {{<wbr>}}   **sync** used with the sync-replicated, primary-backup and partitioned cluster schema|
| cluster-config.groups.group.repl-policy.policy-type | Optional values:{{<wbr>}}  full-replication - all objects are replicated. {{<wbr>}}  partial-replication - Object that their @SpaceClass configured(replicate = false) will not be replicated. See the [POJO Metadata]({{%currentjavaurl%}}/the-space-configuration.html) page for details. This allows you to control replication at a class base level. | partial-replication |
| cluster-config.groups.group.repl-policy.repl-find-timeout | Timeout (in milliseconds) to wait for the lookup of a peer space. This parameter applies only when the space is searched in a Jini Lookup Service. | 5000 \[ms\] |
| cluster-config.groups.group.repl-policy.repl-full-take | If set to **true** the entire object is replicated when take operations is called. If set to **false** only the ID, class information and primitive fields are replicated. This option is valid only when replicating data into a Mirror Service or a backup in non-central DB topology. | false |
| cluster-config.groups.group.repl-policy.replicate-notify-templates  | Boolean value. If set to true, the notify templates are replicated to the target space. | true |
| cluster-config.groups.group.repl-policy.trigger-notify-templates | Boolean value. If set to true, the replicated operations will trigger the notify templates and send events to the registered listeners. | false |
| cluster-config.groups.group.repl-policy.on-conflicting-packets | Enum value. If set to **ignore**, the conflicting operations are ignored. If set to **override** the newest operation will override the data in the target. | ignore |


# Asynchronous Replication Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.async-replication.repl-chunk-size | Number of packets transmitted together on the network when the replication event is triggered. The maximum value you can assign for this property is `repl-interval-opers`. | 500 |
| cluster-config.groups.group.repl-policy.async-replication.repl-interval-millis | Time (in milliseconds) to wait between replication operations. | 3000 \[ms\] |
| cluster-config.groups.group.repl-policy.async-replication.repl-interval-opers | Number of destructive operations to wait before replicating. | 500 |
| cluster-config.groups.group.repl-policy.async-replication.async-channel-shutdown-timeout | Determines how long (in milliseconds) the primary space will wait for pending replication to be replicated to its targets before shutting down.| 300000 \[ms\]  |



{{% refer %}}For more info refer to [Asynchronous Replication](./asynchronous-replication.html){{% /refer %}}

# Synchronous Replication Parameters




| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.sync-replication.throttle-when-inactive | Boolean value. Set to **true** if you want to throttle replicated operations when the channel is in-active (disconnection) | **true** in primary backup **false** in full sync replicated |
| cluster-config.groups.group.repl-policy.sync-replication.max-throttle-tp-when-inactive | Integer value. If the above is true, this will specify the maximum operations per second the throttle will maintain when the channel is in-active (disconnection), if the last measured throughput when the channel was active was higher than that, the measured throughput will be used instead. | 50,000 operations/second |
| cluster-config.groups.group.repl-policy.sync-replication.min-throttle-tp-when-active | Integer value. this specifies the minimum operations per second the throttle can reduce to when the channel is active (during asynchronous state), the throttling when the channel is active is always adapted to the current throughput and size of redolog in order to keep the redolog size decreasing. | 1,000 operations/second |
| cluster-config.groups.group.repl-policy.sync-replication.target-consume-timeout | The timeout time in milliseconds that the target space waits for consuming replication packets. When the timeout expires, replication channel moves to asynchronous state. | 10000 \[ms\] |



{{% refer %}}For more info refer to [Synchronous Replication](./synchronous-replication.html){{% /refer %}}

# Recovery Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.recovery-chunk-size | Integer value. Defines how many operations are recovered is a single batch | 200 |
| cluster-config.groups.group.repl-policy.recovery-thread-pool-size | Integer value. Defines how many threads are recovering the data during the snapshot process. | 4 |


{{% refer %}}For more info refer to [Space Instance Recovery](./space-instance-recovery.html){{% /refer %}}

# Redo-log Parameters

|      |     |  |
|----|-----|-----|
|cluster-config.groups.group.repl-policy.redo-log-capacity | Specifies the total capacity of replication packets the redo log can hold for a standard replication target.|150000|
|cluster-config.groups.group.repl-policy.redo-log-memory-capacity | Specifies the maximum number of replication packets the redo log keeps in memory.|150000|
|cluster-config.groups.group.repl-policy.redo-log-recovery-capacity | Specifies the total capacity of replication packets the redo log can hold for a standard replication target while it is undergoing a recovery process.|5000000|
|cluster-config.groups.group.repl-policy.on-redo-log-capacity-exceeded | See the [Handling an Increasing Redo Log](./controlling-the-replication-redo-log.html#Handling an Increasing Redo Log) for details. | drop-oldest |
|cluster-config.groups.group.repl-policy.on-missing-packets | Options: ignore , recover. See the [Handling Dropped Replication Packets](./controlling-the-replication-redo-log.html#Handling Dropped Replication Packets) for details. | recover|
|cluster-config.mirror-service.redo-log-capacity | Specifies the total capacity of replication packets the redo log can hold for a mirror service replication target.|1000000|
|cluster-config.mirror-service.on-redo-log-capacity-exceeded | See the [Handling an Increasing Redo Log](./controlling-the-replication-redo-log.html#Handling an Increasing Redo Log) for details. | block-operations|



The following parameters are low level configuration that relates to the swap redo log mechanism:


| Space Cluster Property | Description | Default Value|
|:-----------------------|:------------|:-------------|
| cluster-config.groups.group.repl-policy.swap-redo-log.flush-buffer-packet-count | Specifies the number of packets buffer size that the swap redo log is using when flushing packets to the disk. | 500 |
| cluster-config.groups.group.repl-policy.swap-redo-log.swap-redo-log.segment-size | Specifies the size in bytes of each swap redo log segment file. | 10MB |
| cluster-config.groups.group.repl-policy.swap-redo-log.fetch-buffer-packet-count | Specifies the number of packets buffer size that the swap redo log is using when retrieving packets from the disk to the memory. | 500 |
| cluster-config.groups.group.repl-policy.swap-redo-log.max-scan-length | Specifies the maximum allowed scan length in bytes in swap redo log file in order to locate a packet. | 50KB |
| cluster-config.groups.group.repl-policy.swap-redo-log.max-open-cursors | Specifies the maximum number of open file descriptor that the swap redo log will use. | 10 |


{{% refer %}}For more information, refer to [Controlling the Replication Redo Log](./controlling-the-replication-redo-log.html).{{% /refer %}}

# Mirror Service Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.mirror-service.url | used to locate the Mirror Service. In case you change the name of the Mirror Service specified as part of the Mirror PU, you should modify this parameter value to facilitate the correct Mirror service URL. | jini://*/mirror-service_container/mirror-service |
| cluster-config.mirror-service.bulk-size | The amount of operations to be transmitted in one bulk (in quantity and not actual memory size) from an active IMDG primary to the Mirror Service. | 100 |
| cluster-config.mirror-service.interval-millis | The replication frequency - Replication will happen every `interval-millis` milliseconds | 2000 |
| cluster-config.mirror-service.interval-opers | The replication buffer size - Replication will happen every `interval-opers` operations. | 100 |
| cluster-config.mirror-service.on-redo-log-capacity-exceeded | Available options:{{<wbr>}}`block-operations` - all cluster operations that need to be replicated (write/update/take) are blocked until the redo log size decreases below the capacity. (Users get RedoLogCapacityExceededException exceptions while trying to execute these operations.){{<wbr>}}`drop-oldest` - the oldest packet in the redo log is dropped.{{<wbr>}}See the [Controlling the Replication Redo Log](./controlling-the-replication-redo-log.html) for details. | block-operations |
| cluster-config.mirror-service.redo-log-capacity | Specifies the total capacity of replication packets the redo log can hold for a mirror service replication target.


{{% refer %}}For more information, refer to the [Controlling the Replication Redo Log](./controlling-the-replication-redo-log.html) topic. {{% /refer %}}

# Durable Notifications Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.redo-log-durable-notification-capacity | Specifies the total capacity of replication packets the redo log can hold for a durable notification replication target | 150000 |
| cluster-config.groups.group.repl-policy.durable-notification-max-disconnection-time| Specifies the maximum amount of time (in milliseconds) the space will wait for the durable notification replication target before it is considered disconnected, after which the target will be dropped. | 300000 |

{{% refer %}}For more information about Durable Notfications, refer to [Durable Notifications](../dev-java/durable-notifications.html) in the Developer guide.{{% /refer %}}

# Local View Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.redo-log-local-view-capacity | Specifies the total capacity of replication packets the redo log can hold for a local view replication target | 150000 |
| cluster-config.groups.group.repl-policy.redo-log-local-view-recovery-capacity | Specifies the total capacity of replication packets the redo log can hold for a local view replication target while the local view is in recovery state (initial load process)| 1000000 |
| cluster-config.groups.group.repl-policy.local-view-max-disconnection-time | Specifies the maximum amount of time (in milliseconds) the space will wait for the local view replication target before it is considered disconnected, after which the target will be dropped. | 300000 |

