---
type: post97
title:  Large Scale Deployment
categories: XAP97ADM
parent: tuning-gigaspaces-performance.html
weight: 110
---

{{% ssummary%}}{{% /ssummary %}}

# Large Cluster Considerations



When designing a large cluster, there are several things that need to be taken into account to assure that the cluster will be able to handle heavy loads, and perform quickly and stably.

When speaking of a larger cluster, we are referring to more than **few hundreds**. If this is the size of cluster you intend to build, the following considerations are relevant for you.

## Unregistering Spaces "Disappear" from LUS

This occurs when a large amount of memory is consumed in the process, causing extensive JVM GC spikes. This results in high CPU usage and distracts the `LeaseRenewManager` (a long GC/CPU clock causes the `LeaseRenewManager` to miss the default 4 seconds, or to attempt to renew the lease, firing a space service un-registering event). If the LUS fires an event to unregister a space, the UI spaces tree node represents it using a specific icon. Additionally, specific logging is printed out in the UI.

To avoid the unregistering of spaces, add resources (memory, CPU) or spaces, or tune the `LeaseRenewal maxLeaseDuration` and `roundTripTime`. These two values can be configured using the system properties:


```bash
//Default value for roundTripTime 4 seconds
-Dcom.gs.jini.config.roundTripTime=4000

//Default value for maxLeaseDuration  8 seconds.
-Dcom.gs.jini.config.maxLeaseDuration=8000
```

{{% note %}}
It is recommended to **increase these values to 40000/80000 respectively** in case a large cluster is used.

Increasing these values causes a delay when the space recognizes failover, since the active election infrastructure is based on space un-registration.
{{% /note %}}

## Minimize RMI Registry Overhead

Since every space container starts an embedded `RMIRegistry` service, it creates a set of threads which consume some resources.

{{% note%}}
If the `RMIRegistry` service is not used, or if a full replication cluster or a large cluster is used; it is recommended to disable the `RMIRegistry` service in the space container and in the GSC/GSM.
{{%/note%}}

## Lookup Service

Do not start more than 2 Lookup Services per cluster. Preferably start these on your strongest machines.



# Many Clients Accessing Space

When attempting to run hundreds of clients, which need to find a space and perform operations; a few considerations need to be taken.

## Cluster Availability Monitoring

When there are many clients monitoring the availability of a cluster, it is recommended to increase the value of the `Monitor` thread to a maximum. Usually, when there is no failover or there are no backup-only spaces, the `Monitor` thread can be safely set to its maximum value; since clients directly interact with the space members. If either is detected as unavailable, the `Detector` thread is responsible for detecting their re-availability.

{{% refer %}}For more details, refer to the [Viewing Clustered Space Status](./cluster-view-gigaspaces-browser.html) section.{{% /refer %}}
