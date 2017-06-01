---
type: post110
title:  Machine Provisioning
categories: XAP110
parent: elastic-processing-unit-overview.html
weight: 400
---

{{% ssummary %}}{{% /ssummary %}}



# System Bootstrapping

Each machine requires a single running [GigaSpaces Agent](/product_overview/service-grid.html#gsa). The example below shows how to start a new XAP agent. The command line parameters instruct the agents to communicate with each other and start the specified amount of managers. It does not start any containers automatically. The EPU starts containers on demand.

That means that potentially any machine could be a management machine:

{{%tabs%}}
{{%tab "Windows"%}}


```bash
rem Agent deployment that potentially can start management processes
set XAP_LOOKUP_GROUPS=myGroup
set XAP_HOME=d:\gigaspaces
start cmd /c "%XAP_HOME%\bin\gs-agent.bat gsa.global.esm 1 gsa.gsc 0 gsa.global.gsm 2 gsa.global.lus 2"
```

{{% /tab %}}

{{%tab "Linux"%}}


```bash
1. Agent deployment that potentially can start management processes
export XAP_LOOKUP_GROUPS=myGroup
export XAP_HOME=~/gigaspaces
nohup ${XAP_HOME}/bin/gs-agent.sh gsa.global.esm 1 gsa.gsc 0 gsa.global.gsm 2 gsa.global.lus 2 > /dev/null 2>&1 &
```

{{% /tab %}}
{{% /tabs %}}

{{%note%}} Basic assumption: When using the ESM component, each machine must have exactly one Grid Service Agent{{%/note%}}

# Dedicated Management Machines

In case you prefer having dedicated management machines, start GigaSpaces agents with the above settings on two machines, and start the rest of the GigaSpaces agents with the settings below. The command line parameters instruct the GigaSpaces agents not to start managers. It does not start any containers automatically. The EPU starts containers on demand:

{{%tabs%}}
{{%tab "Windows"%}}


```bash
rem Agent that does not start management processes
set XAP_LOOKUP_GROUPS=myGroup
set XAP_HOME=d:\gigaspaces
start cmd /c "%XAP_HOME%\bin\gs-agent.bat gsa.global.esm 0 gsa.gsc 0 gsa.global.gsm 0 gsa.global.lus 0"
```

{{% /tab %}}

{{%tab "Linux"%}}


```bash
1. Agent that does not start management processes
export XAP_LOOKUP_GROUPS=myGroup
export XAP_HOME=~/gigaspaces
nohup ${XAP_HOME}/bin/gs-agent.sh gsa.global.esm 0 gsa.gsc 0 gsa.global.gsm 0 gsa.global.lus 0 > /dev/null 2>&1 &
```

{{% /tab %}}
{{% /tabs %}}

Configure the EPU scale config to use `dedicatedManagementMachines`, and reduce the `reservedMemoryCapacityPerMachine`. For more information consult the [discovered machine provisioning configuration JavaDoc]({{% api-javadoc %}}/org/openspaces/admin/pu/elastic/config/DiscoveredMachineProvisioningConfigurer.html).

# Zone Based Machine Provisioning

The EPU can be deployed into specific zone. This allows you to determine the specific locations of the EPU instances. You may have multiple EPU deployed, each into a difference zone. To specify the agent zone you should set the zone name before starting the agent - here is an example how to set the agent zone to **ZoneX**:


```bash
export GSA_JAVA_OPTIONS="-Dcom.gs.zones=zoneX ${GSA_JAVA_OPTIONS}"
gs-agent.sh gsa.global.lus 1 gsa.lus 0 gsa.global.gsm 1 gsa.gsm 0 gsa.gsc 0 gsa.global.esm 1
```

When deploying the EPU you should specify the zone you would like the EPU will be deployed into:


```java
ProcessingUnit pu1 = gsm.deploy(
		new ElasticSpaceDeployment("mySpace")
		.maxMemoryCapacity(512, MemoryUnit.GIGABYTES)
		.memoryCapacityPerContainer(10, MemoryUnit.GIGABYTES)
		// discover only agents with "zoneX"
		.dedicatedMachineProvisioning(new DiscoveredMachineProvisioningConfigurer()
	        .addGridServiceAgentZone("zoneX")
	        .removeGridServiceAgentsWithoutZone()
	        .create())
			.scale(new ManualCapacityScaleConfigurer()
	         	.memoryCapacity(256,MemoryUnit.GIGABYTES)
	         	.create())
```

With the above the `mySpace` EPU will be deployed only into agents associated with **zoneX** where other agents without a zone specified will be ignored.

# Automatic Machine Provisioning


When deploying an EPU pass an instance of [ElasticMachineProvisioningConfig]({{% api-javadoc %}}/org/openspaces/admin/pu/elastic/ElasticMachineProvisioningConfig.html) as the [machineProvisioning]({{% api-javadoc %}}/org/openspaces/admin/pu/elastic/topology/ElasticDeploymentTopology.html) deployment property.


```java
ProcessingUnit pu = gsm.deploy(
        new ElasticStatefulProcessingUnitDeployment(new File("myPU.jar"))
           .memoryCapacityPerContainer(16,MemoryUnit.GIGABYTES)
           .maxMemoryCapacity(512,MemoryUnit.GIGABYTES)
           .maxNumberOfCpuCores(32)
           //automatically start new virtual machines on demand
           .dedicatedMachineProvisioning(new XenServerMachineProvisioning("xenserver.properties"))
);
```

When deploying Gigaspaces XAP on the management machine(s) place the plug-in JAR file under `/gigaspaces-xap/lib/platform/esm` folder. The ESM then loads classes specified by the `machineProvisioning` configuration. These classes need to implement either [ElasticMachineProvisioning]({{% api-javadoc %}}/org/openspaces/grid/gsm/machines/plugins/ElasticMachineProvisioning.html) or [NonBlockingElasticMachineProvisioning]({{% api-javadoc %}}/org/openspaces/grid/gsm/machines/plugins/NonBlockingElasticMachineProvisioning.html). That class must also implement [Bean]({{% api-javadoc %}}/org/openspaces/core/bean/Bean.html) which has resemblance to the Spring Bean.

# Automatic Rebalancing

Each stateful processing unit (embedding a space) has a fixed number of logical partitions. The number of logical partitions can be specified by the user or calculated by GigaSpaces using the memory capacity requirements during the deployment time. Each logical partition has by default two instances - a primary and a backup. Instances of an EPU are automatically relocated between containers until the following criteria is met:

- Number of instances per container is nearly the same across all containers.
- Number primary instances per cores is nearly the same across all machines.

{{% align center%}}
![rebalance_util.jpg](/attachment_files/rebalance_util.jpg)
{{% /align %}}

## How Rebalancing works

GigaSpaces runtime environment differentiate between a Container (GSC) or **Grid node** that is running within a single JVM instance and an **IMDG node**, also called a logical partition. A partition has one primary instance and zero or more backup instances.

A grid node hosts zero or more logical partitions, these may be primary or backup instances belong to different logical partitions. Logical partitions (primary or backup instances) may relocate between Grid nodes during runtime. A deployed stateful PU may expand its capacity or shrink its capacity in real-time by adding or removing Grid nodes and relocating existing logical partitions to the newly started Grid nodes.

If the system selects to relocate a primary instance, it will first switch its activity mode into a backup mode and the existing backup instance of the same logical partition will be switched into a primary mode. Once the new backup will be relocated, it will recover its data from the existing primary. This how the PU expands its capacity without disruption to the client application.

GigaSpaces rebalancing controls which logical partition is moving, where it is moving to and whether to move a primary to a backup instance. Without such control, the system might choose to move partitions into containers that are fully consumed or move too many instances into the same container. This will obviously crash the system.

# Adaptive SLA

GigaSpaces adjust the high-availability SLA dynamically to cope with the current system memory resources. This means that if there is not sufficient memory capacity to instantiate all the backup instances, GigaSpaces will relax the SLA in runtime to allow the system to continue and run. Once the system identifies that there are enough resources to accommodate all the backups, it will start the missing backups automatically.

## Re-balancing Automatic Process Considerations

- It assumes that all partitions are equal (in terms of memory and CPU consumption).
- When manual capacity scale is used, the specified memory capacity and CPU cores must be provisioned before the re-balancing takes place. As long as the available machines do not meet the specified memory capacity or CPU cores re-balancing does not take place.
- The maximum number of CPU cores that can be occupied by the Processing Unit primary instances is:


```java
numberOfPartitions X numberOfCpuCoresPerMachine
```

- The maximum amount of memory that can be used by the primary and backup instances is:


```java
numberOfPartitions X ( 1 + numberOfBackupsPerPartition ) X memoryCapacityPerContainer
```

- During relocation of a specific instance, primary election takes place. For a few seconds, operations on that partition and operations on the whole cluster is denied. Internally, the client proxy retries the operation until the primary election takes place and masks the failure, but the delay exists.
This delay can be reduced by modifying configuration settings as explained in [Failure Detection]({{%currentadmurl%}}/troubleshooting-failure-detection.html). Overriding the default value of these context properties is achieved with the [addContextProperty]({{% api-javadoc %}}/org/openspaces/admin/pu/elastic/topology/ElasticDeploymentTopology.html) deployment property. For example:


```java
ProcessingUnit pu = gsm.deploy(
 new ElasticStatefulProcessingUnitDeployment(new File("myPU.jar"))
  .memoryCapacityPerContainer(16,MemoryUnit.GIGABYTES)
  .maxMemoryCapacity(512,MemoryUnit.GIGABYTES)
  .maxNumberOfCpuCores(32)
  .addContextProperty("cluster-config.groups.group.fail-over-policy.active-election.yield-time","300")
);
```

# Shared Machine Provisioning


In the code examples above, we showed the usage of `.dedicatedMachineProvisioning`. 'Dedicated' machine provisioning means that the whole machine is 'reserved' for instances of a single processing unit. In other words, other processing units are denied to co-exist on the same machine. This policy is useful if one processing unit should not be affected by other resource consuming processes.

For sharing a machine between processing units, you would need to specify a **_sharing ID_** - a simple string identifying your sharing policy. When a processing unit is requested to scale-out, a new machine is marked with the _sharing ID_ of the processing unit. This ensures that no other processing unit will race to occupy the machine. This _sharing ID_ is later used to match against other processing unit's _sharing ID_ - to allow or deny sharing the same machine resource.

To simulate a 'public' sharing policy, use the same _sharing ID_ for all deployments. For example `.sharedMachineProvisioning("public")`.

The following example, shows two elastic stateless processing units that may share each others machine resources.


```java
// Deploy the Elastic Stateless Processing Unit on "site1"
ProcessingUnit puA = gsm.deploy(
	new ElasticStatelessProcessingUnitDeployment("servlet.war")
           .name("servlet-A")
           .memoryCapacityPerContainer(4,MemoryUnit.GIGABYTES)
           //initial scale
           .scale(
                new ManualCapacityScaleConfigurer()
         	.memoryCapacity(1,MemoryUnit.GIGABYTES)
         	.create())
           .sharedMachineProvisioning("site1", machineProvisioningConfig)
);

// Deploy the Elastic Stateless Processing Unit on "site1"
ProcessingUnit puB = gsm.deploy(
	new ElasticStatelessProcessingUnitDeployment("servlet.war")
           .name("servlet-B")
           .memoryCapacityPerContainer(4,MemoryUnit.GIGABYTES)
           //initial scale
           .scale(
                new ManualCapacityScaleConfigurer()
         	.memoryCapacity(1,MemoryUnit.GIGABYTES)
         	.create())
           .sharedMachineProvisioning("site1", machineProvisioningConfig)
);
```

