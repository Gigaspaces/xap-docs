---
type: post101
title:  Split Brain and Primary Resolution
categories: XAP101ADM
parent: data-grid-clustering.html
weight: 500
---

{{% ssummary %}}{{% /ssummary %}}



Split brain occurs when there are two or more primary instances running for the same partition. In most cases the reason for such behavior would be a network disruption that does not allow each space instance to communicate with all lookup services running. Usually you will have each space instance communicating with a different lookup service rather all of them (two in most cases).

During this split clients may communicate with each primary considering it as its master data copy updating the data within the space. If the active GSM also loosing connectivity with both lookup service it may provision a new backup.

Once the connection between all instances of a given partition and all lookup services occurs and a split brain identified (more than a single primary instance identified for a given partition) the system determines which instance will remain the primary and which will turn into a backup instance. The data within the instance that was elected to a backup will be dropped.

The primary resolution may involves few steps. Each tries to calculate the most recent primary and its data consistency level. If the first step can't determine who is primary (tie) the second step executed (tie break). If the second step can't determine who is primary the third one is executed (second tie break). Once a primary is elected the other instance moved into a backup mode and recover its entire data from the existing primary.

{{% refer %}}
Data resolution occurs between two different clusters using the WAN gateway as a replication channel. In this case a [conflict resolution]({{%currentjavaurl%}}/multi-site-conflict-resolution.html) is executed.
{{% /refer %}}

# Primary Resolution

## Resolution - Step One

Each primary inspected by checking for multiple properties. As a result of this process an inconsistency ranking is calculated. The primary with the lowest ranking will be elected as the primary. If both primaries end up having the same inconsistency level, step two is executed.

The inconsistency level calculated using the mirror active primary identity and various replication statistics. Since the mirror will not allow multiple primaries for the same partition it can be useful with the inconsistency level calculation.

## Resolution - Step Two

Each primary is inspected for the exact time is was elected to be a primary. The election time is stored within the lookup service. All lookup services are inspected during this process. The one which has been elected to be a primary first will be elected to be the primary. If both primary have been elected in the same time - step three is executed.

## Resolution - Step Three

The system reviewing the primary instance names and choosing the one with the lowest lexical value to be the primary.

# Common Causes For a Split-Brain

Below are the most common causes for Split-Brain scenarios and ways to detect them.

- **JVM Garbage Collection spikes** - when a full GC happens it "stops-the-world" of both the GigaSpaces and application components (and holding internal clocks and timing) and external interactions.
    - Using JMX or other monitoring tools you can monitor the JVM Garbage Collection activity. Once it gets into a full GC of longer than 30 seconds you should be alerted. You can use the Admin API and fetch the full GC events. If the GC takes more than 10 seconds, it will be logged as a warning in the GSM/GSC/GSA log file.

- **High (>90%) CPU utilization** - As discussed, that can cause to various components (also external to GigaSpaces) to strive for CPU clock resources, such as keep alive mechanisms (which can miss events and therefore trigger initialization of redundant services or false alarms), IO/network lack of available sockets, OS fails to release resources etc. One should avoid getting into scenarios of constant (more than a minute) utilization of over 90% CPU.
    - You can use the out-of-the-box CPU monitoring component (which uses [SIGAR](http://www.hyperic.com/products/sigar)) for measuring the OS and JVM resources. It is easily accessible through the GigaSpaces Admin API.

- **Network outages/disconnections** - As discussed, disconnections between the GSMs or GSMs and GSCs can cause any of the GSMs to get into what is called "islands".
    - You should be using a network monitoring tool to monitor network outages/disconnections and re connections on machines which run the GSMs and GSCs. Such tool should report and alert on exact datetime of the event.

# Islands

In events of network or failures, the system might get into unexpected behavior, also called Islands, which are extreme and need special handling. Here are two islands scenarios you might encounter:

- When a server running one of the GSMs that is managing some of the PUs is disconnected from the network, and later reconnects with the network. The GSM (that was still a primary) tried to redeploy the failed PUs. It depends which of the two GSMs is the primary at the point of disconnection. If the primary GSM is on the island with the GSCs, its backup GSM will become a primary until the network disconnection is resolved. When the network is repaired, the GSM will realize that the 'former' primary is still managing the services, and return to its backup state. But if it was the other way around - and the primary GSM lost connection with its PUs either due-to network disconnection or any other failure - it will behave as primary and try to redeploy as soon as a GSC is available. That will lead obviously into inconsistent mapping of services and an inconsistent system.
- A more complex form of "islands" would be if on both islands GSCs are available, leading both GSMs to behave as primaries and deploy the failed PUs. Reconciling at this point will need to take data integrity into account.

The solution for these scenarios would be to manually reconcile the cluster. Terminate the GSM, with only one remaining managing GSM, restart the GSCs hosting the backup space instances, and as a last step, start the second GSM (will be the backup GSM).
