---
type: post140
title:  Replication Parameters
categories: XAP140ADM, OSS
parent: replication.html
weight: 700
---

 

# General Parameters




| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.replication-mode | Optional values: `sync`, `async`.<br>- The **async** mode replicates Space operations to the target Space after the client receives an acknowledgment from the source Space for the operation.<br>- The **sync** mode replicates Space operations to the target Space before the client receives the acknowledgment from the source Space for the operation. The client gets the acknowledgment for the operation only after all target Spaces have confirmed the arrival of the replicated data.|`async` is used with the async-replicated schema.<br>`sync` is used with the sync-replicated, primary-backup and partitioned cluster schema.|
| cluster-config.groups.group.repl-policy.policy-type | Optional values:<br>* Full-replication - all objects are replicated. <br>*  Partial-replication - objects with the `@SpaceClass` configured (replicate = false) will not be replicated.This allows you to control replication at a class base level.<br>See the [POJO Metadata](../dev-java/the-space-configuration.html) page for details.  | partial-replication |
| cluster-config.groups.group.repl-policy.repl-find-timeout | Timeout (in milliseconds) to wait for the lookup of a peer Space.<br>This parameter applies only when the Space is searched in a Jini Lookup Service. | 5000 \[ms\] |
| cluster-config.groups.group.repl-policy.repl-full-take | If set to `true`, the entire object is replicated when a take operation is called. If set to `false`, only the ID, class information, and primitive fields are replicated. <br>**This option is valid only when replicating data to an internal target as backup, Local View, or durable notify container (the mirror gets the full entry)**. | false |
| cluster-config.groups.group.repl-policy.replicate-notify-templates  | Boolean value. If set to `true`, the notify templates are replicated to the target Space. | true |
| cluster-config.groups.group.repl-policy.trigger-notify-templates | Boolean value. If set to `true`, the replicated operations will trigger the notify templates and send events to the registered listeners. | false |
| cluster-config.groups.group.repl-policy.on-conflicting-packets | Enum value. If set to `ignore`, the conflicting operations are ignored. If set to `override`, the newest operation will override the data in the target. | ignore |


# Asynchronous Replication Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.async-replication.repl-chunk-size | Number of packets transmitted together on the network when the replication event is triggered. The maximum value you can assign for this property is `repl-interval-opers`. | 500 |
| cluster-config.groups.group.repl-policy.async-replication.repl-interval-millis | Time (in milliseconds) to wait between replication operations. | 3000 \[ms\] |
| cluster-config.groups.group.repl-policy.async-replication.repl-interval-opers | Number of destructive operations to wait before replicating. | 500 |
| cluster-config.groups.group.repl-policy.async-replication.async-channel-shutdown-timeout | Determines how long (in milliseconds) the primary Space will wait for a pending replication to be replicated to its targets before shutting down.| 300000 \[ms\]  |


{{% refer %}}For more information, see the [Asynchronous Replication](./asynchronous-replication.html) page.{{% /refer %}}

# Synchronous Replication Parameters

| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.sync-replication.throttle-when-inactive | Boolean value. Set to `true` if you want to throttle replicated operations when the channel is inactive (disconnection). |`true` in primary backup.<br>`false` in full sync replicated. |
| cluster-config.groups.group.repl-policy.sync-replication.max-throttle-tp-when-inactive | Integer value. If the above is true, this will specify the maximum operations per second the throttle will maintain when the channel is in-active (disconnection), if the last measured throughput when the channel was active was higher than that, the measured throughput will be used instead. | 50,000 operations/second |
| cluster-config.groups.group.repl-policy.sync-replication.min-throttle-tp-when-active | Integer value. This specifies the minimum operations per second the throttle can reduce to when the channel is active (during asynchronous state). The throttling when the channel is active is always adapted to the current throughput and size of the redo log in order to keep the redo log size decreasing. | 1,000 operations/second |
| cluster-config.groups.group.repl-policy.sync-replication.target-consume-timeout | The timeout time (in milliseconds) that the target Space waits for consuming replication packets. When the timeout expires, the replication channel moves to asynchronous state. | 10000 \[ms\] |



{{% refer %}}For more info refer to [Synchronous Replication](./synchronous-replication.html){{% /refer %}}

# Recovery Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.recovery-chunk-size | Integer value. Defines how many operations are recovered in a single batch. | 200 |
| cluster-config.groups.group.repl-policy.recovery-thread-pool-size | Integer value. Defines how many threads are recovering the data during the snapshot process. | 4 |


{{% refer %}}For more info refer to [Space Instance Recovery](./space-instance-recovery.html){{% /refer %}}

# Redo Log Parameters

|      |     |  |
|----|-----|-----|
|cluster-config.groups.group.repl-policy.redo-log-capacity | Specifies the total amount of replication packets the redo log can hold for a standard replication target.|150000|
|cluster-config.groups.group.repl-policy.redo-log-memory-capacity | Specifies the maximum number of replication packets the redo log keeps in memory.|150000|
|cluster-config.groups.group.repl-policy.redo-log-recovery-capacity | Specifies the total amount of replication packets the redo log can hold for a standard replication target while it is undergoing a recovery process.|5000000|
|cluster-config.groups.group.repl-policy.on-redo-log-capacity-exceeded | See the [Handling an Increasing Redo Log](./controlling-the-replication-redo-log.html#Handling an Increasing Redo Log) page for details. | drop-oldest |
|cluster-config.groups.group.repl-policy.on-missing-packets | Options: `ignore`, `recover`. See the [Handling Dropped Replication Packets](./controlling-the-replication-redo-log.html#Handling Dropped Replication Packets) page for details. | recover|
|cluster-config.mirror-service.redo-log-capacity | Specifies the total amount of replication packets the redo log can hold for a mirror service replication target.|1000000|
|cluster-config.mirror-service.on-redo-log-capacity-exceeded | See the [Handling an Increasing Redo Log](./controlling-the-replication-redo-log.html#Handling an Increasing Redo Log) page for details. | block-operations|

The following  are low-level configuration parameters that relate to the swap redo log mechanism:

| Space Cluster Property | Description | Default Value|
|:-----------------------|:------------|:-------------|
| cluster-config.groups.group.repl-policy.swap-redo-log.flush-buffer-packet-count | Specifies the number of packets (buffer size) that the swap redo log uses when flushing packets to the disk. | 500 |
| cluster-config.groups.group.repl-policy.swap-redo-log.swap-redo-log.segment-size | Specifies the size (in bytes) of each swap redo log segment file. | 10MB |
| cluster-config.groups.group.repl-policy.swap-redo-log.fetch-buffer-packet-count | Specifies the number of packets (buffer size) that the swap redo log usES when retrieving packets from disk to memory. | 500 |
| cluster-config.groups.group.repl-policy.swap-redo-log.max-scan-length | Specifies the maximum allowed scan length (in bytes) in the swap redo log file in order to locate a packet. | 50KB |
| cluster-config.groups.group.repl-policy.swap-redo-log.max-open-cursors | Specifies the maximum number of open file descriptors that the swap redo log will use. | 10 |

{{% refer %}}For more information, see the [Controlling the Replication Redo Log](./controlling-the-replication-redo-log.html) page.{{% /refer %}}

# Mirror Service Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.mirror-service.url | Used to locate the Mirror Service. If you change the name of the Mirror Service specified as part of the Mirror Processing Unit, modify this parameter value to facilitate the correct Mirror service URL. | jini://*/mirror-service_container/mirror-service |
| cluster-config.mirror-service.bulk-size | The amount of operations to be transmitted in one bulk operation (in quantity and not actual memory size) from an active primary to the Mirror Service. | 100 |
| cluster-config.mirror-service.interval-millis | The replication frequency; replication will happen every `interval-millis` milliseconds. | 2000 |
| cluster-config.mirror-service.interval-opers | The replication buffer size; replication will happen every `interval-opers` operations. | 100 |
| cluster-config.mirror-service.on-redo-log-capacity-exceeded | Available options:<br>`block-operations` - all cluster operations that need to be replicated (write/update/take) are blocked until the redo log size drops below the maximum capacity. (Users get RedoLogCapacityExceededException exceptions while trying to execute these operations.)<br>`drop-oldest` - the oldest packet in the redo log is dropped.<br>See the [Controlling the Replication Redo Log](./controlling-the-replication-redo-log.html) page for details. | block-operations |
| cluster-config.mirror-service.redo-log-capacity | Specifies the total amount of replication packets the redo log can hold for a mirror service replication target.


{{% refer %}}For more information, refer to the [Controlling the Replication Redo Log](./controlling-the-replication-redo-log.html) topic. {{% /refer %}}

# Durable Notifications Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.redo-log-durable-notification-capacity | Specifies the total amount of replication packets the redo log can hold for a durable notification replication target. | 150000 |
| cluster-config.groups.group.repl-policy.durable-notification-max-disconnection-time| Specifies the maximum amount of time (in milliseconds) the Space will wait for the durable notification replication target before it is considered disconnected, after which the target will be dropped. | 300000 |

{{% refer %}}For more information about Durable Notfications, refer to the [Durable Notifications](../dev-java/durable-notifications.html) topic in the Developer guide.{{% /refer %}}

# Local View Parameters


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.redo-log-local-view-capacity | Specifies the total amount of replication packets the redo log can hold for a local view replication target. | 150000 |
| cluster-config.groups.group.repl-policy.redo-log-local-view-recovery-capacity | Specifies the total amount of replication packets the redo log can hold for a local view replication target while the local view is in recovery state (initial load process).| 1000000 |
| cluster-config.groups.group.repl-policy.local-view-max-disconnection-time | Specifies the maximum amount of time (in milliseconds) the Space will wait for the local view replication target before it is considered disconnected, after which the target will be dropped. | 300000 |

