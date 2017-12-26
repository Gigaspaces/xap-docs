---
type: post123
title:  Predefined Metrics
categories: XAP123ADM, PRM
parent: metrics-overview.html
weight: 500
---

{{% ssummary %}}{{% /ssummary %}}

The following metrics are bundled with the product:

# Operating System

Operating system metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID of the `gs-agent` which reported this metric.

## Memory Metrics


| Metric | Description | Type |
|:-------|:------------|:-----|
| os_memory_free-bytes | Free memory (bytes) | Long |
| os_memory_actual-free-bytes | Actual free memory (bytes) | Long |
| os_memory_used-bytes | Used memory (bytes) | Long |
| os_memory_actual-used-bytes | Actual used memory (bytes) | Long |
| os_memory_used-percent | Used memory (%) | Double |

## Swap Metrics


| Metric | Description | Type |
|:-------|:------------|:-----|
| os_swap_free-bytes | Free swap (bytes) | Long |
| os_swap_used-bytes | Used swap (bytes) | Long |
| os_swap_used-percent | Used swap (%) | Long |

## CPU Metrics


| Metric | Description | Type |
|:-------|:------------|:-----|
| os_cpu_used-percent | CPU Usage (%) | Double |

## Network Metrics

Network metrics are reported for each network interface card with an IP address. In addition to the tags listed above, the `nic` tag is also reported to indicate which network interface card the metric was sampled from.


| Metric | Description | Type |
|:-------|:------------|:-----|
| os_network_rx-bytes | Total bytes received | Long |
| os_network_tx-bytes | Total bytes transmitted | Long |
| os_network_rx-packets | Total packets received | Long |
| os_network_tx-packets | Total packets transmitted | Long |
| os_network_rx-errors | Receive errors | Long |
| os_network_tx-errors | Transmit errors | Long |
| os_network_rx-dropped | Receive dropped | Long |
| os_network_tx-dropped | Transmit dropped | Long |

# Process

Process metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID.
* `process_name` - The process name (`gsa` \| `lus` \| `gsm` \| `esm` \| `gsc`).

## Process CPU Metrics


| Metric | Description | Type |
|:-------|:------------|:-----|
| process_cpu_time-total | Process CPU total time | Long |
| process_cpu_used-percent | Process CPU Usage (%) | Double |

# JVM 

JVM metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID.
* `process_name` - The process name (`gsa` \| `lus` \| `gsm` \| `esm` \| `gsc`).


| Metric | Description | Type |
|:-------|:------------|:-----|
| jvm_uptime | Uptime of the Java virtual machine (in milliseconds) | Long |
| jvm_threads_count | Current number of live threads including both daemon and non-daemon threads | Integer |
| jvm_threads_daemon | Current number of live daemon threads | Integer |
| jvm_threads_peak | Peak live thread count since the Java virtual machine started or peak was reset | Integer |
| jvm_threads_total-started | Total number of threads created and also started since the Java virtual machine started | Long |
| jvm_memory_heap_used-bytes | Amount of used memory in bytes | Long |
| jvm_memory_heap_committed-bytes | Amount of memory in bytes that is committed for the Java virtual machine to use | Long |
| jvm_memory_non-heap_used-bytes | Amount of used memory in bytes | Long |
| jvm_memory_non-heap_committed-bytes | Amount of memory in bytes that is committed for the Java virtual machine to use | Long |
| jvm_memory_gc_count | Total number of garbage collections that have occurred | Long |
| jvm_memory_gc_time | Approximate accumulated collection elapsed time in milliseconds | Long |

# LRMI

LRMI metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID.
* `process_name` - The process name (`gsa` \| `lus` \| `gsm` \| `esm` \| `gsc`).


| Metric | Description | Type |
|:-------|:------------|:-----|
| lrmi_connections | Number of LRMI Connections | Long |
| lrmi_active-connections | Number of active LRMI Connections | Long |
| lrmi_generated-traffic | Total generated traffic (in bytes) | Long |
| lrmi_received-traffic | Total received traffic (in bytes) | Long |
| lrmi_connection-pool_active-threads | Number of active LRMI threads in the default connection pool | Integer |
| lrmi_liveness-pool_active-threads | Number of active LRMI threads in the liveness pool | Integer |
| lrmi_monitoring-pool_active-threads | Number of active LRMI threads in the monitoring pool | Integer |
| lrmi_custom-pool_active-threads | Number of active LRMI threads in the custom pool | Integer |

# Lookup Service

Lookup Service metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID.


| Metric | Description |
|:-------|:------------|
| lus_items | Number of registered services |
| lus_listeners | Number of registered notification listeners |
| lus_event-task-pool_threads-count | Number of threads currently allocated (less or equal to maximum threads) |
| lus_event-task-pool_total-tasks | Total number of tasks to be executed and currently being executed |
| lus_pendingEvents | Number of events pending to be notified to event listeners |
| lus_serviceById | Number of unique services |
| lus_serviceByTime | Number of unique services whose lease hasn't expired yet |
| lus_eventByID | Number of registered event listeners |
| lus_eventByTime | Number of registered event listeners whose registration lease hasn't expired yet |

# Processing Unit

Processing Unit metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID of the process (`GSC`) hosting the processing unit.
* `pu_name` - Processing Unit name.
* `pu_instance_id` - Processing Unit instance id.

## Event Containers

| Metric | Description |
|:-------|:------------|
| pu_{event-container-name}_processed-events | Total number of entries in the space. |
| pu_{event-container-name}_failed-events | Total number of entries in the space of type `type`. |

# Space 

Space metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID of the process (`GSC`) hosting the space.
* `pu_name` - Name of processing unit hosing this space.
* `pu_instance_id` - Instance id of processing unit instance hosting this space instance.
* `space_name` - Space name.
* `space_instance_id` - Space instance id.

## Data


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_data_entries_total | Total number of entries in the space. | Integer |
| space_data_entries_{type-name} | Total number of entries in the space of type. | Integer |
| space_data_notify-templates_total | Total number of notify templates in the space. | Integer |
| space_data_notify-templates_{type-name} | Total number of notify templates in the space of type. | Integer |

## Operations


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_operations_execute-total | Number of task execution operations | Long |
| space_operations_write-total | Number of write operations | Long |
| space_operations_update-total  | Number of update operations | Long |
| space_operations_change-total | Number of change operations | Long |
| space_operations_read-total | Number of read operations | Long |
| space_operations_read-multiple-total | Number of read multiple operations | Long |
| space_operations_take-total | Number of take operations | Long |
| space_operations_take-multiple-total | Number of take multiple operations | Long |
| space_operations_lease-expired-total | Number of entry lease expirations | Long |
| space_operations_register-listener-total | Number of event listener registrations | Long |
| space_operations_before-listener-trigger-total | Number of triggered events (before trigger) | Long |
| space_operations_after-listener-trigge-totalr | Number of triggered events (after trigger) | Long |
| space_operations_execute-tp | ?task executions per second? | Double |
| space_operations_write-tp |  ?write operations per second? | Double |
| space_operations_update-tp  | ?update operations per second? | Double |
| space_operations_change-tp | ?change operations per second? | Double |
| space_operations_read-tp | ?read operations per second? | Double |
| space_operations_read-multiple-tp | ?read multiple operations per second? | Double |
| space_operations_take-tp | ?take operations per second? | Double |
| space_operations_take-multiple-tp | ?take multiple operations per second? | Double |
| space_operations_lease-expired-tp | ?entry lease expirations per second? | Double |
| space_operations_register-listener-tp | ?event listener registrations per second? | Double |
| space_operations_before-listener-trigger-tp | ?triggered events (before trigger) per second? | Double |
| space_operations_after-listener-trigge-tp | ?triggered events (after trigger) per second? | Double |

## Blobstore Operations


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_operations_remove | Number of remove operations | Long |
| space_operations_add | Number of add operations | Long |
| space_operations_replace  | Number of replace operations | Long |
| space_operations_get | Number of get operations | Long |
| space_operations_remove-tp | Remove per second  | Double |
| space_operations_add-tp | Add per second  | Double |
| space_operations_replace-tp  | Replace per second | Double |
| space_operations_get-tp | Get operations per second | Double |
| space_blobstore_cache-size  | Number of objects stored in blobstore cache | Long |
| space_blobstore_cache-hit | Blobstore cache hit | Long |
| space_blobstore_cache-miss  | Total of blobstore cache misses | Long |
| space_blobstore_hot-data-cache-miss  | Subgroup of cache misses - misses of hot data not found in cache | Long |
| space_blobstore_offheap_used-bytes  | Amount of used offheap memory in bytes | Long |
| space_blobstore_offheap_{type_name}  | Amount of used offheap memory in bytes of type | Long |

## Connections


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_connections_incoming_active | Number of active incoming connections | Integer |

## Transactions


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_transactions_active | Number of active transactions | Integer |

## Replication

### Replication Redo Log


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_replication_redo-log_size | The number of replication packets held in the file | Long |
| space_replication_redo-log_memory-packets | The number of packets held in memory | Long |
| space_replication_redo-log_external-storage-packets | Number of packets held in storage | Long |
| space_replication_redo-log_external-storage-bytes | Used space in bytes which is external to the JVM | Long |
| space_replication_redo-log_first-key-in-backlog | First key in the backlog (-1 if empty) | Long |
| space_replication_redo-log_last-key-in-backlog | Last key in the backlog (-1 if empty) | Long |

### Replication Channels


| Metric | Description | Type |
|:-------|:------------|:-----|
| space_replication_channel_total-replicated-packets | Total number of packets replicated by channel `channel` | Long |
| space_replication_channel_generated-traffic-bytes | Traffic generated by channel `channel` in bytes | Long |
| space_replication_channel_received-traffic-bytes | Traffic received by channel `channel` in bytes | Long |
| space_replication_channel_retained-size-packets | Number of packets retained by channel `channel`| Long |
