---
type: post140
title:  Predefined Metrics
categories: XAP140ADM, PRM
parent: metrics-overview.html
weight: 500
---



The following metrics are bundled with the product:

# Operating System

Operating system metrics are reported with the following tags:

* `host` - Host name.
* `ip` - IP address.
* `pid` - Process ID of the `gs-agent` that reported this metric.

## Memory Metrics


| Metric | Description | Type |
|:-------|:------------|:-----|
| os_memory_free-bytes | Free memory, in bytes. | Long |
| os_memory_actual-free-bytes | Actual free memory, in bytes. | Long |
| os_memory_used-bytes | Used memory, in bytes. | Long |
| os_memory_actual-used-bytes | Actual used memory, in bytes. | Long |
| os_memory_used-percent | Used memory (%). | Double |

## Swap Metrics


| Metric | Description | Type |
|:-------|:------------|:-----|
| os_swap_free-bytes | Free swap, in bytes. | Long |
| os_swap_used-bytes | Used swap, in bytes. | Long |
| os_swap_used-percent | Used swap (%). | Long |

## CPU Metrics


| Metric | Description | Type |
|:-------|:------------|:-----|
| os_cpu_used-percent | CPU Usage (%). | Double |

## Network Metrics

Network metrics are reported for each network interface card with an IP address. In addition to the tags listed above, the `nic` tag is also reported to indicate which network interface card the metric was sampled from.


| Metric | Description | Type |
|:-------|:------------|:-----|
| os_network_rx-bytes | Total bytes received. | Long |
| os_network_tx-bytes | Total bytes transmitted. | Long |
| os_network_rx-packets | Total packets received. | Long |
| os_network_tx-packets | Total packets transmitted. | Long |
| os_network_rx-errors | Receive errors. | Long |
| os_network_tx-errors | Transmit errors. | Long |
| os_network_rx-dropped | Receive dropped. | Long |
| os_network_tx-dropped | Transmit dropped. | Long |

# Process

Process metrics are reported with the following tags:

* `host` - Host name.
* `ip` - IP address.
* `pid` - Process ID.
* `process_name` - Process name (`gsa` \| `lus` \| `gsm` \| `esm` \| `gsc`).

## Process CPU Metrics


| Metric | Description | Type |
|:-------|:------------|:-----|
| process_cpu_time-total | Process CPU total time. | Long |
| process_cpu_used-percent | Process CPU Usage (%). | Double |

# JVM 

JVM metrics are reported with the following tags:

* `host` - Host name.
* `ip` - IP address.
* `pid` - Process ID.
* `process_name` - Process name (`gsa` \| `lus` \| `gsm` \| `esm` \| `gsc`).


| Metric | Description | Type |
|:-------|:------------|:-----|
| jvm_uptime | Uptime of the Java virtual machine, in milliseconds. | Long |
| jvm_threads_count | Current number of live threads, including both daemon and non-daemon threads. | Integer |
| jvm_threads_daemon | Current number of live daemon threads. | Integer |
| jvm_threads_peak | Peak live thread count since the Java virtual machine started or peak was reset. | Integer |
| jvm_threads_total-started | Total number of threads created and also started since the Java virtual machine started. | Long |
| jvm_memory_heap_used-bytes | Amount of used memory, in bytes. | Long |
| jvm_memory_heap_committed-bytes | Amount of memory that is committed for the Java virtual machine to use, in bytes.  | Long |
| jvm_memory_non-heap_used-bytes | Amount of used memory, in bytes | Long |
| jvm_memory_non-heap_committed-bytes | Amount of memory that is committed for the Java virtual machine to use, in bytes. | Long |
| jvm_memory_gc_count | Total number of garbage collections that have occurred. | Long |
| jvm_memory_gc_time | Approximate accumulated collection elapsed time, in milliseconds. | Long |

# LRMI

LRMI metrics are reported with the following tags:

* `host` - Host name.
* `ip` - IP address.
* `pid` - Process ID.
* `process_name` - Process name (`gsa` \| `lus` \| `gsm` \| `esm` \| `gsc`).


| Metric | Description | Type |
|:-------|:------------|:-----|
| lrmi_connections | Number of LRMI connections. | Long |
| lrmi_active-connections | Number of active LRMI connections. | Long |
| lrmi_generated-traffic | Total generated traffic, in bytes. | Long |
| lrmi_received-traffic | Total received traffic, in bytes. | Long |
| lrmi_connection-pool_active-threads | Number of active LRMI threads in the default connection pool. | Integer |
| lrmi_liveness-pool_active-threads | Number of active LRMI threads in the liveness pool. | Integer |
| lrmi_monitoring-pool_active-threads | Number of active LRMI threads in the monitoring pool. | Integer |
| lrmi_custom-pool_active-threads | Number of active LRMI threads in the custom pool. | Integer |

# Lookup Service

Lookup Service metrics are reported with the following tags:

* `host` - Host name.
* `ip` - IP address.
* `pid` - Process ID.


| Metric | Description |
|:-------|:------------|
| lus_items | Number of registered services. |
| lus_listeners | Number of registered notification listeners. |
| lus_event-task-pool_threads-count | Number of threads currently allocated (less or equal to maximum threads). |
| lus_event-task-pool_total-tasks | Total number of tasks to be executed and currently being executed. |
| lus_pendingEvents | Number of events pending to be notified to event listeners. |
| lus_serviceById | Number of unique services. |
| lus_serviceByTime | Number of unique services whose lease hasn't expired yet. |
| lus_eventByID | Number of registered event listeners. |
| lus_eventByTime | Number of registered event listeners whose registration lease hasn't expired yet. |

# Processing Unit

Processing Unit metrics are reported with the following tags:

* `host` - Host name.
* `ip` - IP address.
* `pid` - Process ID of the process (`GSC`) hosting the Processing Unit.
* `pu_name` - Processing Unit name.
* `pu_instance_id` - Processing Unit instance ID.

## Event Containers

| Metric | Description |
|:-------|:------------|
| pu_{event-container-name}_processed-events | Total number of entries in the Space. |
| pu_{event-container-name}_failed-events | Total number of entries in the Space of type `type`. |

# Space 

Space metrics are reported with the following tags:

* `host` - Host name.
* `ip` - IP address.
* `pid` - Process ID of the process (`GSC`) hosting the Space.
* `pu_name` - Name of Processing Unit hosting this Space.
* `pu_instance_id` - Instance ID of Processing Unit instance hosting this Space instance.
* `space_name` - Space name.
* `space_instance_id` - Space instance ID.

## Data


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_data_entries_total | Total number of entries in the Space. | Integer |
| space_data_entries\_{type-name} | Total number of entries in the Space of that data type. | Integer |
| space_data_notify_templates_total | Total number of notify templates in the Space. | Integer |
| space_data_notify_templates\_{type-name} | Total number of notify templates in the Space of that data type. | Integer |

## Operations


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_operations_execute-total | Number of task execution operations. | Long |
| space_operations_write-total | Number of write operations. | Long |
| space_operations_update-total  | Number of update operations. | Long |
| space_operations_change-total | Number of change operations. | Long |
| space_operations_read-total | Number of read operations. | Long |
| space_operations_read-multiple-total | Number of read multiple operations. | Long |
| space_operations_take-total | Number of take operations. | Long |
| space_operations_take-multiple-total | Number of take multiple operations. | Long |
| space_operations_lease-expired-total | Number of entry lease expirations. | Long |
| space_operations_register-listener-total | Number of event listener registrations. | Long |
| space_operations_before-listener-trigger-total | Number of triggered events (before trigger). | Long |
| space_operations_after-listener-trigge-totalr | Number of triggered events (after trigger). | Long |
| space_operations_execute-tp | Task executions per second. | Double |
| space_operations_write-tp |  Write operations per second. | Double |
| space_operations_update-tp  | Update operations per second. | Double |
| space_operations_change-tp | Change operations per second. | Double |
| space_operations_read-tp | Read operations per second. | Double |
| space_operations_read-multiple-tp | Read multiple operations per second. | Double |
| space_operations_take-tp | Take operations per second. | Double |
| space_operations_take-multiple-tp | Take multiple operations per second. | Double |
| space_operations_lease-expired-tp | Entry lease expirations per second. | Double |
| space_operations_register-listener-tp | Event listener registrations per second. | Double |
| space_operations_before-listener-trigger-tp | Triggered events (before trigger) per second. | Double |
| space_operations_after-listener-trigge-tp | Triggered events (after trigger) per second. | Double |

## Blobstore Operations


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_blobstore_add | Number of add operations.  | Long |
| space_blobstore_add-tp | Number of add operations per second.  | Double |
| space_blobstore_get | Number of get operations. | Long |
| space_blobstore_get-tp | Number of get operations per second. | Double |
| space_blobstore_remove | Number of remove operations. | Long |
| space_blobstore_remove-tp| Number of remove operations per second.  | Double |
| space_blobstore_compaction_avg | Compaction time on average | Double |
| space_blobstore_cache-size  | Number of objects stored in the blobstore cache. | Long |
| space_blobstore_cache-hit | Number of blobstore cache hits. | Long |
| space_blobstore_cache-miss  | Total number of blobstore cache misses. | Long |
| space_blobstore_hot-data-cache-miss  | Subgroup of cache misses - misses of hot data not found in cache. | Long |
| space_blobstore_off-heap_used-bytes_total | Total used off-heap memory, in bytes. | Long |
| space_blobstore_off-heap_used-bytes\_{type_name} | Used off-heap memory per type, in bytes. | Long |
| space_blobstore_off-heap-cache_used-bytes_total | Total used off-heap memory for caching, in bytes. | Long |
| space_blobstore_off-heap-cache_used-bytes\_{type_name} | Used off-heap memory for caching per type, in bytes. | Long |
| space_blobstore_memtable-hit | Number of MemTable hits. | Long |
| space_blobstore_memtable-miss | Number of MemTable misses. | Long |
| space_blobstore_get-hit-l0 | Number of Get queries served by L0. | Long |
| space_blobstore_get-hit-l1 | Number of Get queries served by L1. | Long |
| space_blobstore_get-hit-l2-and-up | Number of Get queries served by L2 and up. | Long |
| space_blobstore_number-keys-written | Number of keys written to RocksDB. | Long |
| space_blobstore_number-keys-read | Number of keys read from RocksDB. | Long |
| space_blobstore_number-keys-updated | Number of keys updated, if in-place update is enabled. | Long |
| space_blobstore_bytes-written | Number of uncompressed bytes issued by Put, Delete, Merge, and Write operations. | Long |
| space_blobstore_bytes-read | Number of uncompressed bytes read from Get operations. | Long |
| space_blobstore_iter-bytes-read | Number of uncompressed bytes read from an iterator (during initial load). | Long |
| space_blobstore_number-multiget-calls | Number of MultiGet API calls. | Long |
| space_blobstore_number-multiget-keys-read | Number of keys read via MultiGet API calls. | Long |
| space_blobstore_number-multiget-bytes-read | Number of bytes received via MultiGet API calls. | Long |

## Connections


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_connections_incoming_active | Number of active incoming connections. | Integer |

## Transactions


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_transactions_active | Number of active transactions. | Integer |

## Replication

### Replication Redo Log


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_replication_redo-log_size | Number of replication packets held in the file. | Long |
| space_replication_redo-log_memory-packets | Number of packets held in memory. | Long |
| space_replication_redo-log_external-storage-packets | Number of packets held in storage. | Long |
| space_replication_redo-log_external-storage-bytes | Used Space that is external to the JVM, in bytes. | Long |
| space_replication_redo-log_first-key-in-backlog | First key in the backlog (-1 if empty). | Long |
| space_replication_redo-log_last-key-in-backlog | Last key in the backlog (-1 if empty). | Long |

### Replication Channels


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_replication_channel_total-replicated-packets | Total number of packets replicated by channel `channel`. | Long |
| space_replication_channel_generated-traffic-bytes | Traffic generated by channel `channel` in bytes. | Long |
| space_replication_channel_received-traffic-bytes | Traffic received by channel `channel`, in bytes. | Long |
| space_replication_channel_retained-size-packets | Number of packets retained by channel `channel`. Long |
