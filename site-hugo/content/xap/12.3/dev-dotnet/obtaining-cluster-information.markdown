---
type: post123
title:  Cluster Information
categories: XAP123NET, PRM
parent: the-processing-unit-overview.html
weight: 300
---

 

One of the core concepts of GigaSpaces processing units is the fact that the clustering topology of the processing unit is determined at deployment time. Therefore, when building a processing unit, there is almost no need to be aware of the actual clustering topology with which the processing unit is deployed.
However, in some cases you may want your processing unit to be aware of it.
This mechanism is also used internally by the platform to maintain the clustering information when deploying a space for example.

# Available Cluster Information

As mentioned above the cluster information is passed to the components of each processing unit instance via an instance of the `GigaSpaces.Core.ClusterInfo` class, which holds the following information:


| Attribute Name | Description |
|:---------------|:------------|
| Schema | If the processing unit contains an embedded space, the cluster schema of that space |
| NumberOfInstances | The number of instances of the processing unit. If the processing unit contains an embedded space, denotes the number of primary instances |
| NumberOfBackups |If the processing unit contains an embedded space with backups, denotes the number of backups per primary instance |
| InstanceId | A value between 1 to `numberOfInstances`, denoting the instance ID of the processing unit instance |
| BackupId | If the processing unit contains an embedded space with backups, a value between on 1 to `numberOfBackups`, denoting the backup ID of the processing instance. The primary instance ID which the processing unit instance is backing up is denoted by `instanceId`. If the processing unit instance is not a backup instance, `null` is returned |
| RunningNumber | A running number of the processing unit instance instance. Takes into account different topologies and provides a unique identifier (starting from `0`) of the processing unit instance within the cluster |

{{% note %}}
`null` value for one of these properties means that they are not applicable for the processing unit instance at hand or the used deployment topology.
{{%/note%}}

# Obtaining the Cluster Information

The cluster information is injected to the processing unit container at runtime and is available throughout it's lifecycle using the singleton `ProcessingUnitContainer.Current.ClusterInfo`.

