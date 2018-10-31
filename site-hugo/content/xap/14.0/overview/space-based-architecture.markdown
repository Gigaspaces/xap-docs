---
type: post140
title: Space-Based Architecture
categories: XAP140OVW
parent: the-in-memory-data-grid.html
weight: 800
---





{{%  anchor Space Based Architecture %}}


**A Space-Based Architecture (SBA) implementation** is a set of Processing Units, with the following properties:

- Each processing unit instances holds a [partitioned](./terminology.html#partitioned-data-grid) space instance and one or more services that are registered on events on that specific partition. Together they form an application cluster. If the cluster is required to be highly available, each primary partition has one or more backup partitions, which run in their own processing unit instances. These instances are inactive, and become active only when their primary partition fails.

- Each Processing Unit instance handles only the data sent to the space partition it runs.

- Clients interact with the system by writing and updating objects in the space cluster, and the services on each processing unit instance react to object written to that specific instance. In an SBA application, the data will be partitioned in such a way that the services that is triggered as a result will not have to read or write data from other partitions, thus achieving data affinity and in memory read and write speeds. .

- The system can be scaled by simply increasing the number of space partitions and their corresponding processing unit instances.

- When deployed onto the [Service Grid](./terminology.html#service-grid), self-healing and SLA capabilities are added.

- Full monitoring and management during runtime are available through the [Management UI](./terminology.html#management-ui).

**SBA Implementation with 3 Primary Instances and 1 Backup for Each, Accessed by 2 Client Applications**

{{% align center%}}
![sba_with_backup.jpg](/attachment_files/sba_with_backup.jpg)
{{% /align%}}


# Space-Based Architecture Artifacts

{{%  anchor Space Based Architecture Artifacts %}}

When a [Processing Unit library](../dev-java/the-processing-unit-structure-and-configuration.html) is deployed, a processing unit instance is created. The processing unit instance is hosted within the [GigaSpaces container](./the-runtime-environment.html#gsc). If the processing unit instance includes a space, all the collocated beans within the processing unit instance inherit the space primary/backup mode. If the space is running in primary mode, they will be active; if the space is running in backup mode, they will be in standby mode.

The following diagram illustrates a deployed Processing Unit with 2 partitions and a backup. The Processing Unit includes a Space, polling container and 2 User Beans. The deployed Processing Unit is hosted within 2 GigaSpaces containers.

{{% align center%}}
![term_sba_artifacts.jpg](/attachment_files/term_sba_artifacts.jpg)
{{% /align%}}
 
**Legend**

- JVM - Java process. Native OS process.
- Processing unit - Deployable package (bundle).
- GigaSpaces Container - Hosting deployed PU.
- [Processing unit Instance](../dev-java/the-processing-unit-overview.html) - deployed instantiated PU.
- [Space Partition](./terminology.html) - Data-Grid Member. Store data in-memory.
- Data-Grid - Collection of Space Partitions.
- Local Proxy - Access to collocated space partition.
- Clustered Proxy - Access to all space partitions.
- Primary Instance - Active space partition.
- Backup Instance - standby space partition.
- Standby Bean - Bean collocated with a backup instance.
- Partition - Collection of primary and backup processing unit instances
- [Polling container](../dev-java/polling-container-overview.html) - Execution queue. Consumes objects from the space.
 
