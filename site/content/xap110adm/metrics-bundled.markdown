---
type: post110
title:  Predefined Metrics
categories: XAP110ADM
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

### Memory Metrics


| Metric | Description |
|:-------|:------------|
| `os_memory_free-bytes` | Free memory (bytes) |
| `os_memory_actual-free-bytes` | Actual free memory (bytes) |
| `os_memory_used-bytes` | Used memory (bytes) |
| `os_memory_actual-used-bytes` | Actual used memory (bytes) |
| `os_memory_used-percent` | Used memory (%) |

### Swap Metrics


| Metric | Description |
|:-------|:------------|
| `os_swap_free-bytes` | Free swap (bytes) |
| `os_swap_used-bytes` | Used swap (bytes) |
| `os_swap_used-percent` | Used swap (%) |

### CPU Metrics


| Metric | Description |
|:-------|:------------|
| `os_cpu_used-percent` | CPU Usage (%) |

### Network Metrics

Network metrics are reported for each network interface card with an IP address. In addition to the tags listed above, the `nic` tag is also reported to indicate which network interface card the metric was sampled from.


| Metric | Description |
|:-------|:------------|
| `os_network_rx-bytes` | Total bytes received |
| `os_network_tx-bytes` | Total bytes transmitted |
| `os_network_rx-packets` | Total packets received |
| `os_network_tx-packets` | Total packets transmitted |
| `os_network_rx-errors` | Receive errors |
| `os_network_tx-errors` | Transmit errors |
| `os_network_rx-dropped` | Receive dropped |
| `os_network_tx-dropped` | Transmit dropped |

# Process

Process metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID.
* `process_name` - The process name (`gsa` \| `lus` \| `gsm` \| `esm` \| `gsc`).

### Process CPU Metrics


| Metric | Description |
|:-------|:------------|
| `process_cpu_time-total` | Process CPU total time |
| `process_cpu_used-percent` | Process CPU Usage (%) |

# JVM 

JVM metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID.
* `process_name` - The process name (`gsa` \| `lus` \| `gsm` \| `esm` \| `gsc`).


| Metric | Description |
|:-------|:------------|
| `jvm_uptime` | Uptime of the Java virtual machine (in milliseconds) |
| `jvm_threads-count` | Current number of live threads including both daemon and non-daemon threads |
| `jvm_threads-daemon` | Current number of live daemon threads |
| `jvm_threads-peak` | Peak live thread count since the Java virtual machine started or peak was reset |
| `jvm_threads-total-started` | Total number of threads created and also started since the Java virtual machine started |
| `jvm_memory_heap_used-bytes` | Amount of used memory in bytes |
| `jvm_memory_heap_committed-bytes` | Amount of memory in bytes that is committed for the Java virtual machine to use |
| `jvm_memory_non-heap_used-bytes` | Amount of used memory in bytes |
| `jvm_memory_non-heap_committed-bytes` | Amount of memory in bytes that is committed for the Java virtual machine to use |
| `jvm_memory_gc_count` | Total number of garbage collections that have occurred |
| `jvm_memory_gc_time` | Approximate accumulated collection elapsed time in milliseconds |

# LRMI

LRMI metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID.
* `process_name` - The process name (`gsa` \| `lus` \| `gsm` \| `esm` \| `gsc`).


| Metric | Description |
|:-------|:------------|
| `lrmi_connections` | Number of LRMI Connections |
| `lrmi_active-connections` | Number of active LRMI Connections |
| `lrmi_generated-traffic` | Total generated traffic (in bytes) |
| `lrmi_received-traffic` | Total received traffic (in bytes) |
| `lrmi_connection-pool_active-threads` | Number of active LRMI threads in the default connection pool |
| `lrmi_liveness-pool_active-threads` | Number of active LRMI threads in the liveness pool |
| `lrmi_monitoring-pool_active-threads` | Number of active LRMI threads in the monitoring pool |
| `lrmi_custom-pool_active-threads` | Number of active LRMI threads in the custom pool |

# Lookup Service

Lookup Service metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID.


| Metric | Description |
|:-------|:------------|
| `lus_items` | Number of registered services |
| `lus_listeners` | Number of event notification listeners |
| `lus_pending-events` | Size of the pending event notification queue |

# Processing Unit

Processing Unit metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID of the process (`GSC`) hosting the processing unit.
* `pu_name` - Processing Unit name.
* `pu_instance_id` - Processing Unit instance id.

### Event Containers

| Metric | Description |
|:-------|:------------|
| `pu_{event-container-name}_processed-events` | Total number of entries in the space. |
| `pu_{event-container-name}_failed-events` | Total number of entries in the space of type `type`. |

# Space 

Space metrics are reported with the following tags:

* `host` - The host name.
* `ip` - The IP address.
* `pid` - The process ID of the process (`GSC`) hosting the space.
* `pu_name` - Name of processing unit hosing this space.
* `pu_instance_id` - Instance id of processing unit instance hosting this space instance.
* `space_name` - Space name.
* `space_instance_id` - Space instance id.

### Data


| Metric | Description |
|:-------|:------------|
| `space_data_entries_total` | Total number of entries in the space. |
| `space_data_entries_{type-name}` | Total number of entries in the space of type. |
| `space_data_notify-templates_total` | Total number of notify templates in the space. |
| `space_data_notify-templates_{type-name}` | Total number of notify templates in the space of type. |

### Operations


| Metric | Description |
|:-------|:------------|
| `space_operations_execute` | Number of task execution operations |
| `space_operations_write` | Number of write operations |
| `space_operations_update`  | Number of update operations |
| `space_operations_change` | Number of change operations |
| `space_operations_read` | Number of read operations |
| `space_operations_read-multiple` | Number of read multiple operations |
| `space_operations_take` | Number of take operations |
| `space_operations_take-multiple` | Number of take multiple operations |
| `space_operations_lease-expired` | Number of entry lease expirations |
| `space_operations_register-listener` | Number of event listener registrations |
| `space_operations_before-listener-trigger` | Number of triggered events (before trigger) |
| `space_operations_after-listener-trigger` | Number of triggered events (after trigger) |

### Blobstore Operations


| Metric | Description |
|:-------|:------------|
| `space_operations_remove` | Number of remove operations |
| `space_operations_add` | Number of add operations |
| `space_operations_replace`  | Number of replace operations |
| `space_operations_get` | Number of get operations |
| `space_operations_remove-tp` | remove per second  |
| `space_operations_add-tp` | add per second  |
| `space_operations_replace-tp`  | replace per second |
| `space_operations_get-tp` | get operations per second |
| `space_blobstore_cache_miss`  | blobstore cache miss |
| `space_blobstore_cache_hit` | blobstore cache hit |

### Connections


| Metric | Description |
|:-------|:------------|
| `space_connections_incoming_active` | Number of active incoming connections |

### Transactions


| Metric | Description |
|:-------|:------------|
| `space_transactions_active` | Number of active transactions |

### Replication

#### Replication Redo Log


| Metric | Description |
|:-------|:------------|
| `space_replication_redo-log_size` | The number of replication packets held in the file |
| `space_replication_redo-log_memory-packets` | The number of packets held in memory |
| `space_replication_redo-log_external-storage-packets` | Number of packets held in storage |
| `space_replication_redo-log_external-storage-bytes` | Used space in bytes which is external to the JVM |
| `space_replication_redo-log_first-key-in-backlog` | First key in the backlog (-1 if empty) |
| `space_replication_redo-log_last-key-in-backlog` | Last key in the backlog (-1 if empty) |

#### Replication Channels


| Metric | Description |
|:-------|:------------|
| `space_replication_channel_total-replicated-packets` | Total number of packets replicated by channel `channel` |
| `space_replication_channel_generated-traffic-bytes` | Traffic generated by channel `channel` in bytes |
| `space_replication_channel_received-traffic-bytes` | Traffic received by channel `channel` in bytes |
| `space_replication_channel_retained-size-packets` | Number of packets retained by channel `channel`|
