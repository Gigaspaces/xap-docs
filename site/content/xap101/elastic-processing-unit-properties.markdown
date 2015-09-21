---
type: post100
title:  Configuration Properties
categories: XAP101
parent: elastic-processing-unit-overview.html
weight: 500
---

{{% ssummary %}}{{% /ssummary %}}




# Elastic Deployment Topology Configuration

Here are the main configuration properties you may use with the [ElasticSpaceDeployment](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/space/ElasticSpaceDeployment.html) and the [ElasticStatefulProcessingUnitDeployment](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/pu/elastic/ElasticStatefulProcessingUnitDeployment.html):


|Property| Type | Description| Default | Mandatory |
|:--------------|:-----------|:--------|:----------|:----------|
|highlyAvailable| boolean | Specifies if the space should duplicate each information on two different machines.| true|No|
|memoryCapacityPerContainer| int , MemoryUnit |Specifies the the heap size per container (operating system process)| |Yes|
|minNumberOfCpuCoresPerMachine | double  | Overrides the minimum number of CPU cores per machine assumption.| |No|
|maxMemoryCapacity| int , MemoryUnit  | Specifies an estimate of the maximum memory capacity for this processing unit.| |Yes|
|maxNumberOfCpuCores| int | Specifies an estimate for the maximum total number of cpu cores used by this processing unit.| |No|
|numberOfPartitions| int | Defines the number of processing unit partitions.| |No|
|numberOfBackupsPerPartition| int | Overrides the number of backup processing unit instances per partition.| 1 |No|
|secured|boolean | deploy a secured processing unit.| false|No|
|singleMachineDeployment | | Allows deployment of the processing unit on a single machine, by lifting the limitation for primary and backup processing unit instances from the same partition to be deployed on different machines.| false|No|
|userDetails| UserDetails | Advanced: Sets the security user details for authentication and authorization of the processing unit.| |No|
|scale| [EagerScaleConfig](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/pu/elastic/config/EagerScaleConfig.html) or [ManualCapacityScaleConfig](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/pu/elastic/config/ManualCapacityScaleConfig.html) |Enables the specified scale strategy, and disables all other scale strategies.| |No|
|useScriptToStartContainer| | Allow the GridServiceContainer to be started using a script and not a pure Java process.| |No|

# Scale Strategy Configuration

Here are the main configuration properties you may use with the [EagerScaleConfig](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/pu/elastic/config/EagerScaleConfig.html) and the [ManualCapacityScaleConfig](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/pu/elastic/config/ManualCapacityScaleConfig.html):


|Property| Type | Description| Default |Mandatory |
|:-------|:-----|:-----------|:--------|:---------|
|memoryCapacityInMB|int|Specifies the total memory capacity of the processing unit.| |Yes|
|numberOfCpuCores|int|Specifies the total CPU cores for the processing unit.| |No|
|maxConcurrentRelocationsPerMachine|int|Specifies the number of processing unit instance relocations each machine can handle concurrently. By setting this value higher than 1, processing unit re balancing completes faster, by using more machine cpu and network resources|1|No|


# Machine Provisioning Configuration

Here are the main configuration properties you may use with the [DiscoveredMachineProvisioningConfigurer](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/pu/elastic/config/DiscoveredMachineProvisioningConfigurer.html) that is passed to the `dedicatedMachineProvisioning` and `sharedMachineProvisioning` methods:

 
|Property| Type | Description| Default |Mandatory |
|:-------|:-----|:-----------|:--------|:---------|
|reservedMemoryCapacityPerMachine|int|Sets the expected amount of memory (total RAM) per machine that is reserved for processes other than grid containers (GSCs created dynamically). These include Grid Agent, Grid Service Manager, Lookup Service , Elastic Service  Manager , Web UI , GS-UI and also any other daemon/process running on the machine (Non-XAP related process).{{%wbr%}}In most cases the default value will be **too small** as you may choose to allocate more than 1GB for the XAP grid management processes (GSA, GSM, LUS, ESM, Web-UI, GS-UI) and other processes running on the machine.{{%wbr%}}Recommended value should be total XAP grid management processes required RAM + 20% of the total machine RAM + total memory required for all non-XAP processes running on the machine.   |1024 MB.  |No|
|reservedMemoryCapacityPerManagementMachine|int| This property is similar to `reservedMemoryCapacityPerMachine`:{{% wbr %}}It sets the expected amount of memory (total RAM) per management machine (which runs the ESM) that is reserved for processes other than grid containers (GSCs created dynamically). These include Grid Agent, Grid Service Manager, Lookup Service , Elastic Service  Manager , Web UI , GS-UI and also any other daemon/process running on the machine (Non-XAP related process).{{% wbr %}}In most cases the default value will be **too small** as you may choose to allocate more than 1GB for the XAP grid management processes (GSA, GSM, LUS, ESM, Web-UI, GS-UI) and other processes running on the machine.{{% wbr %}}Recommended value should be total XAP grid management processes required RAM + 20% of the total machine RAM + total memory required for all non-XAP processes running on the machine.   |1024 MB.  |No|

