---
type: faq
title:  Multi Site Replication
categories: FAQ
parent: none
weight: 300

---


{{% wbr %}}

{{%panel%}}

- [What is the Replication flow?](#1)

- [What happens when the Gateway fails or is restarted?](#2)

- [What if two clients are writing data into a local cluster at the same time? Are these resolved by conflict resolution functionality?](#3)

- [How do you specify the local site cluster lookup service for the Gateway?](#4)

- [Should the schema for Space classes across the sites be identical?](#5)


{{%/panel%}}

{{%anchor 1%}}

### What's the Replication flow?
Replications starts with the local site cluster. Updates in the form of replication redo log packets are sent to the local delegator in an async manner which in turn writes them to the appropriate Sink(s) in a sync manner. This sink will perform operations corresponding to these packets on its local cluster.

{{%anchor 2%}}

### What happens when Gateway fails or is restarted?
Gateway component is stateless and does not save any state. When this PU is missing because of a failure (hardware, OS or process failure), GSM will restart the PU in a available container. Once it is active, it will start replicating the changes that where it left off.

{{%anchor 3%}}

### What if two clients are writing data into a local cluster at the same time? Are these resolved by conflict resolution functionality?
Data written to the same site by concurrent clients should be handled using transactions and appropriate isolation levels. Conflict Resolution logic is applicable only for data updated on two clusters at the same time (or within the replication window which is the network latency between the sites).

{{%anchor 4%}}

### How do you specify the local site cluster lookup service for the Gateway?
The Gateway will use both the local site lookup service and also its own lookup service to allow gateways deployed in different locations to find it.
The info about the local lookup service comes from the GSC hosting the Gateway where its `LOOKUPGROUP/LOOKUPLOCATORS` variable value injected into the deployed Gateway.

{{%anchor 5%}}

### Should the schema of Space classes across the sites be identical?
Yes. If you have a dynamic data model you should use the `SpaceDocument`. This will allow you to have a flexible data model.
