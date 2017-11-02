---
type: post123
title:  Viewing Clustered Space Status
categories: XAP123ADM, PRM
parent: troubleshooting.html
weight: 300
---

{{% ssummary%}}{{% /ssummary %}}



The status of clustered spaces can be viewed using different logging levels. This status is managed by two threads - a `Monitor` and a `Detector`.

The `Monitor` monitors the status of live spaces and reports disconnections, while the `Detector` detects the status of disconnected spaces and reports reconnection. The space status frequency can be configured using the `CONFIG` logging level.

{{% note "Note"%}}
See the [Proxy Connectivity]({{%currentadmurl%}}/tuning-proxy-connectivity.html) for details how to tune the Monitor and Detector behavior.
{{%/note%}}

The `com.gigaspaces.client.cluster.liveness` logger is set by default (in `xap_logging.properties`) to `INFO`:


```bash
com.gigaspaces.client.cluster.liveness.level = INFO
```

The different logging levels are detailed below:


| Level | Description |
|:------|:------------|
| CONFIG | Displays default configurations and available configuration options for both `Monitor` and `Detector` threads. |
| FINE | In addition to the `CONFIG` level, displays disconnections logged by the `Monitor` and reconnections logged by the `Detector`. |
| FINER | In addition to the `FINE` level, displays disconnections logged by user operations and by the detector. |
| FINEST | In addition to the `FINEST`, displays reconnections logged by user operations and by the detector. |

Logging granularity and information assists in tracing the availability of cluster members when using load-balancing or failover policies.

# Examples


```bash
May 30, 2007 7:13:42 PM
CONFIG [com.gigaspaces.client.cluster.liveness]: [fooSpace] Space status Monitor frequency is configured to 10000 ms.
Use -Dcom.gs.cluster.livenessMonitorFrequency=<value in milliseconds> to configure differently.
May 30, 2007 7:13:42 PM
CONFIG [com.gigaspaces.client.cluster.liveness]: [fooSpace] Space status detector frequency is configured to 5000 ms.
Use -Dcom.gs.cluster.livenessDetectorFrequency=<value in milliseconds> to configure differently.
May 30, 2007 7:14:22 PM
FINE [com.gigaspaces.client.cluster.liveness]: [fooSpace] Space status Monitor disconnected from member: [fooSpace_container1_1:fooSpace]
URL: [jini://*/fooSpace_container1_1/fooSpace?groups=foo-group&ignoreValidation=true]
Caused by: java.rmi.ConnectException: An existing connection was forcefully closed by the remote host; nested exception is:
java.io.IOException: An existing connection was focefully closed by the remote host
May 30, 2007 7:15:27 PM
FINE [com.gigaspaces.client.cluster.liveness]: [fooSpace] Liveness-detector reconnected with Member: [fooSpace_container1_1:foSpace]
URL: [jini://*/fooSpace_container1_1/foSpace?groups=foo-group&ignoreValidation=true]
```

# Configuration


| System Property | Description | Default Value |
|:----------------|:------------|:--------------|
| -Dcom.gs.cluster.livenessMonitorFrequency | Defines the frequency in which liveness of 'live' members in a cluster is monitored. (Checks if available members become unavailable). | 10000 ms |
| -Dcom.gs.cluster.livenessDetectorFrequency | Defines the frequency in which liveness of members in a cluster is detected. (Detects if an unavailable member becomes available). | 5000 ms |

In most cases, `livenessDetectorFrequency` is the property you need to alter, since it is responsible for detecting spaces that are unavailable.

The unavailability of cluster members is noticed when a direct operation is performed on them. The `livenessMonitorFrequency` property timely monitors all live cluster members. Usually, when you have backup-only spaces, this is most important. The only time an operation is performed on a _backup-only_ space is when it becomes active (i.e., primary), and thus you want it's availability status to be noticed beforehand.

{{% note "Note"%}}
It is recommended to **increase the value of the `Monitor` thread to a maximum** if there are **many clients** or if a large cluster is used.
{{%/note%}}

Usually, when there is no **failover** or when there are no `backup-only` spaces, the `Monitor` thread can be safely set to its maximum value, since clients directly interact with the space members. If either is detected as unavailable, the `Detector` thread is responsible for detecting whether they become available again.

