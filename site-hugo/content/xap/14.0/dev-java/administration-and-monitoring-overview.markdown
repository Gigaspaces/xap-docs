---
type: post140
title:  Admin API
categories: XAP140, PRM
parent: none
weight: 1900
---

The Admin API provides a way to administer and monitor all of XAP services and components using a simple API. The API provides information and the ability to operate on the currently running [XAP Agent](../overview/the-runtime-environment.html#gsa), [XAP Manager](../overview/the-runtime-environment.html#gsm), [XAP Container](../overview/the-runtime-environment.html#gsc), [Lookup Service](../overview/the-runtime-environment.html#lus), [Processing Unit](./the-processing-unit-overview.html) and Spaces.

**Dependencies**

In order to use this feature, include the `${XAP_HOME}/lib/platform/service-grid/xap-admin.jar` file on your classpath or use maven dependencies:

```xml
<dependency>
    <groupId>com.gigaspaces</groupId>
    <artifactId>xap-admin</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
```
{{%refer%}}
For more information on dependencies, refer to [Maven Artifacts](../started/maven-artifacts.html).
{{%/refer%}} 

# Examples

Tee following code examples show how the Admin API can be used to display information on the currently deployed services/components:

{{%tabs%}}
{{%tab "  GSA "%}}


```java
public void gsa() {
	Admin admin = new AdminFactory().addGroup("myGroup").createAdmin();
	// wait till things get discovered (you can also use specific waitFor)
	for (GridServiceAgent gsa : admin.getGridServiceAgents()) {
		System.out.println("GSA [" + gsa.getUid()
				+ "] running on Machine ["
				+ gsa.getMachine().getHostAddress());
		for (AgentProcessDetails processDetails : gsa.getProcessesDetails()) {
			System.out.println("   -> Process ["
					+ Arrays.toString(processDetails.getCommand()) + "]");
		}
	}
}
```

{{% /tab %}}
{{%tab "  GSM "%}}


```java
public void gsm() {
	Admin admin = new AdminFactory().addGroup("myGroup").createAdmin();
	// wait till things get discovered (you can also use specific waitFor)
	for (GridServiceManager gsm : admin.getGridServiceManagers()) {
		System.out.println("GSM [" + gsm.getUid() + "] running on Machine "
				+ gsm.getMachine().getHostAddress());
	}
}
```

{{% /tab %}}
{{%tab "  GSC "%}}


```java
public void gsc() {
	Admin admin = new AdminFactory().addGroup("myGroup").createAdmin();
	// wait till things get discovered (you can also use specific waitFor)
	for (GridServiceContainer gsc : admin.getGridServiceContainers()) {
		System.out.println("GSC [" + gsc.getUid() + "] running on Machine "
				+ gsc.getMachine().getHostAddress());
		for (ProcessingUnitInstance puInstance : gsc
				.getProcessingUnitInstances()) {
			System.out.println("   -> PU [" + puInstance.getName() + "]["
					+ puInstance.getInstanceId() + "]["
					+ puInstance.getBackupId() + "]");
		}
	}
}
```

{{% /tab %}}
{{%tab "  Processing Unit "%}}


```java

public void pu() {
	Admin admin = new AdminFactory().addGroup("myGroup").createAdmin();
	// wait till things get discovered (you can also use specific waitFor)
	for (ProcessingUnit processingUnit : admin.getProcessingUnits()) {
		System.out.println("Processing Unit: " + processingUnit.getName()
					+ " status: " + processingUnit.getStatus());
		if (processingUnit.isManaged()) {
				System.out.println("   -> Managing GSM: "
						+ processingUnit.getManagingGridServiceManager()
								.getUid());
		} else {
				System.out.println("   -> Managing GSM: NA");
		}

		for (GridServiceManager backupGSM : processingUnit
					.getBackupGridServiceManagers()) {
				System.out.println("   -> Backup GSM: " + backupGSM.getUid());
		}

		for (ProcessingUnitInstance processingUnitInstance : processingUnit) {
				System.out.println("   ["
						+ processingUnitInstance.getClusterInfo()
						+ "] on GSC ["
						+ processingUnitInstance.getGridServiceContainer()
								.getUid() + "]");
				if (processingUnitInstance.isEmbeddedSpaces()) {
					System.out.println("      -> Embedded Space ["
							+ processingUnitInstance.getSpaceInstance()
									.getUid() + "]");
				}

				Map<String, ServiceDetails> services = processingUnitInstance.getServiceDetailsByServiceId();

				for (ServiceDetails details : services.values()) {
					System.out.println("      -> Service " + details);
				}
		}
	}
}
```

{{% /tab %}}
{{%tab "  Space "%}}


```java

public void space() {
	Admin admin = new AdminFactory().addGroup("myGroup").createAdmin();

	for (Space space : admin.getSpaces()) {
		System.out.println("Space [" + space.getUid()
					+ "] numberOfInstances [" + space.getNumberOfInstances()
					+ "] numberOfbackups [" + space.getNumberOfBackups() + "]");
		System.out.println("  Stats: Write ["
					+ space.getStatistics().getWriteCount() + "/"
					+ space.getStatistics().getWritePerSecond() + "]");
		for (SpaceInstance spaceInstance : space) {
				System.out.println("   -> INSTANCE [" + spaceInstance.getUid()
						+ "] instanceId [" + spaceInstance.getInstanceId()
						+ "] backupId [" + spaceInstance.getBackupId()
						+ "] Mode [" + spaceInstance.getMode() + "]");
				System.out.println("         -> Host: "
						+ spaceInstance.getMachine().getHostAddress());
				System.out.println("         -> Stats: Write ["
						+ spaceInstance.getStatistics().getWriteCount() + "/"
						+ spaceInstance.getStatistics().getWritePerSecond()
						+ "]");
			}
		for (SpacePartition spacePartition : space.getPartitions()) {
				System.out.println("   -> Partition ["
						+ spacePartition.getPartitionId() + "]");
				for (SpaceInstance spaceInstance : spacePartition) {
					System.out.println("      -> INSTANCE ["
							+ spaceInstance.getUid() + "]");
				}
		}
	}
}
```

{{% /tab %}}
{{%tab "  Virtual Machine "%}}


```java
public void virtualMachine() {
	Admin admin = new AdminFactory().addGroup("myGroup").createAdmin();
		// wait till things get discovered (you can also use specific waitFor)
	System.out.println("VM TOTAL STATS: Heap Committed ["
				+ admin.getVirtualMachines().getStatistics()
						.getMemoryHeapCommittedInGB() + "GB]");
	System.out.println("VM TOTAL STATS: GC PERC ["
				+ admin.getVirtualMachines().getStatistics()
						.getGcCollectionPerc()
				+ "], Heap Used ["
				+ admin.getVirtualMachines().getStatistics()
						.getMemoryHeapUsedPerc() + "%]");

	for (VirtualMachine virtualMachine : admin.getVirtualMachines()) {
			System.out.println("VM [" + virtualMachine.getUid() + "] "
					+ "Host [" + virtualMachine.getMachine().getHostAddress()
					+ "] " + "GC Perc ["
					+ virtualMachine.getStatistics().getGcCollectionPerc()
					+ "], " + "Heap Usage ["
					+ virtualMachine.getStatistics().getMemoryHeapUsedPerc()
					+ "%]");

			for (ProcessingUnitInstance processingUnitInstance : virtualMachine
					.getProcessingUnitInstances()) {
				System.out.println("   -> PU ["
						+ processingUnitInstance.getUid() + "]");
			}
			for (SpaceInstance spaceInstance : virtualMachine
					.getSpaceInstances()) {
				System.out.println("   -> Space [" + spaceInstance.getUid()
						+ "]");
			}
	}
}
```

{{% /tab %}}
{{%tab "  Machine "%}}


```java
public void machine() {
	Admin admin = new AdminFactory().addGroup("myGroup").createAdmin();
		// wait till things get discovered (you can also use specific waitFor)
	for (Machine machine : admin.getMachines()) {
			System.out.println("Machine ["
					+ machine.getUid()
					+ "], "
					+ "TotalPhysicalMem ["
					+ machine.getOperatingSystem().getDetails()
							.getTotalPhysicalMemorySizeInGB()
					+ "GB], "
					+ "FreePhysicalMem ["
					+ machine.getOperatingSystem().getStatistics()
							.getFreePhysicalMemorySizeInGB() + "GB]]");

			for (SpaceInstance spaceInstance : machine.getSpaceInstances()) {
				System.out.println("   -> Space [" + spaceInstance.getUid()
						+ "]");
			}
			for (ProcessingUnitInstance processingUnitInstance : machine
					.getProcessingUnitInstances()) {
				System.out.println("   -> PU ["
						+ processingUnitInstance.getUid() + "]");
			}
	}
}
```

{{% /tab %}}
{{% /tabs %}}


Obtaining information about the currently deployed services/components via the started `GridServiceAgents`:

{{%tabs%}}

{{%tab " GSC "%}}

```java
	public void findStartedGridServiceContainers() {
		Admin admin = new AdminFactory().createAdmin();

		// wait at least for one agent
		admin.getGridServiceAgents().waitForAtLeastOne();

		for (GridServiceAgent gsa : admin.getGridServiceAgents()) {
			for (GridServiceContainer gsc : gsa.getGridServiceContainers()) {
				// ....
			}
		}
	}
```
{{%/tab%}}

{{%tab " GSM "%}}

```java
	public void findStartedGridServiceManagers() {
		Admin admin = new AdminFactory().createAdmin();

		// wait at least for one agent
		admin.getGridServiceAgents().waitForAtLeastOne();

		for (GridServiceAgent gsa : admin.getGridServiceAgents()) {
			for (GridServiceManager gsm : gsa.getGridServiceManagers()) {
				// ....
			}
		}
	}
```
{{%/tab%}}

{{%tab " LUS "%}}

```java
	public void findStartedLookupServices() {
		Admin admin = new AdminFactory().createAdmin();

		// wait at least for one agent
		admin.getGridServiceAgents().waitForAtLeastOne();

		for (GridServiceAgent gsa : admin.getGridServiceAgents()) {
			for (LookupService ls : gsa.getLookupServices()) {
				// ....
			}
		}
	}
```
{{%/tab%}}

{{%tab " ESM "%}}

```java
	public void findStartedElasticServiceManagers() {
		Admin admin = new AdminFactory().createAdmin();

		// wait at least for one agent
		admin.getGridServiceAgents().waitForAtLeastOne();

		for (GridServiceAgent gsa : admin.getGridServiceAgents()) {
			for (ElasticServiceManager esm : gsa.getElasticServiceManagers()) {
				// ....
			}
		}
	}
```
{{%/tab%}}

{{%/tabs%}}


{{% refer %}}
A fully running example of a [Scaling Agent](/sbp/scaling-agent.html) is available in the Solutions & Patterns section.
{{% /refer %}}

# Admin Construction

The Admin API uses the `AdminFactory` in order to create `Admin` instances. After you finish working with the `Admin`, its `Admin#close()` method should be called.

The Admin API discovers all the advertised services from the [Lookup Service](../overview/the-runtime-environment.html#lus). Use the `AdminFactory#addGroup` to define which lookup groups. The lookup locators can also be used for non-multicast enabled environments using `AdminFactory#addLocator`. If the services started are secured, the username and password can be set on the Admin API as well.


# Discovery Process

When the `Admin` is created, it starts to receive discovery events from all the advertised services/components within its lookup groups and lookup locators. The events occur asynchronously and the data model within the Admin API gets initialized in the background with services coming and going.

This means that just creating the `Admin` and calling a specific "getter" for a data structure might not return what is currently deployed, and you should wait until the structures are filled. Some components have a `waitFor` method that allows waiting for a specific number of services to be up. When navigating the data model, the Admin API provides its most up-to-date state of the system it is monitoring.

# Domain Model

The Admin Domain Model has representation to all the XAP-level main actors. They include:

<img src="/attachment_files/admin/admin_domain_model.png" width=569" height="388" />

{{%section%}}
{{%column width="45%" %}}
- [GridServiceAgent](#GridServiceAgentLink)
- [GridServiceAgents](#GridServiceAgentsLink)
- [GridServiceManager](#GridServiceManagerLink)
- [GridServiceManagers](#GridServiceManagersLink)
- [GridServiceContainer](#GridServiceContainerLink)
- [GridServiceContainers](#GridServiceContainersLink)
- [LookupService](#LookupServiceLink)
- [LookupServices](#LookupServicesLink)
- [ProcessingUnit](#ProcessingUnitLink)
- [ProcessingUnitInstance](#ProcessingUnitInstanceLink)
- [ProcessingUnits](#ProcessingUnitsLink)
- [ServiceMonitors](#servicemonitors)

{{%/column%}}
{{%column width="45%" %}}

- [Space](#SpaceLink)
- [SpaceInstance](#SpaceInstanceLink)
- [Spaces](#SpacesLink)
- [VirtualMachine](#VirtualMachineLink)
- [VirtualMachines](#VirtualMachinesLink)
- [Machine](#MachineLink)
- [Machines](#MachinesLink)
- [OperatingSystem](#OperatingSystemLink)
- [OperatingSystems](#OperatingSystemsLink)
- [Transport](#TransportLink)
- [Transports](#TransportsLink)
{{%/column%}}
{{%/section%}}



{{%anchor GridServiceAgentLink%}}

|     |  |
|----|----|
|Name            |[GridServiceAgent]({{% api-javadoc %}}/org/openspaces/admin/gsa/GridServiceAgent.html)|
|Description     |A process manager that manages Service Grid processes such as GSM, GSC and LUS. More information is available [here](../overview/the-runtime-environment.html#gsa).|
|Main Operations |Allows listing all the currently managed processes. Start processes (GSM, GSC, LUS).|
|Runtime Events  | |

{{%anchor GridServiceAgentsLink%}}

|     |  |
|----|----|
|Name            |[GridServiceAgents]({{% api-javadoc %}}/org/openspaces/admin/gsa/GridServiceAgents.html)|
|Description     |Holds all the currently discovered Grid Service Agents.|
|Main Operations |Gets all the currently discovered Grid Service Agents. Wait for X number of Grid Service Agents to be up.|
|Runtime Events  |Register for Grid Service Agent addition (discovery) and removal events. |

{{%anchor GridServiceManagerLink%}}

|     |  |
|----|----|
|Name            |[GridServiceManager]({{% api-javadoc %}}/org/openspaces/admin/gsm/GridServiceManager.html)|
|Description     |Manages Processing Unit deployments and Grid Service Containers. More information is available [here](../overview/the-runtime-environment.html#gsm).|
|Main Operations |Deploy Processing Units. Deploy pure Space Processing Units. Get the Grid Service Agent Managing it. Restart itself (if managed by a Grid Service Agent). Kill itself (if managed by a Grid Service Agent).|
|Runtime Events  |  |

{{%anchor GridServiceManagersLink%}}

|     |  |
|----|----|
|Name            |[GridServiceManagers]({{% api-javadoc %}}/org/openspaces/admin/gsm/GridServiceManagers.html)|
|Description     |Holds all the currently discovered Grid Service Managers.|
|Main Operations |Deploy Processing Units on a random Grid Service Manager. Deploy pure Space Processing Units on a random Grid Service Manager. Get all the currently discovered Grid Service Managers. Wait for X number of Grid Service Managers to be up.|
|Runtime Events  |Register for Grid Service Manager addition (discovery) and removal events.|

{{%anchor GridServiceContainerLink%}}

|     |  |
|----|----|
|Name            |[GridServiceContainer]({{% api-javadoc %}}/org/openspaces/admin/gsc/GridServiceContainer.html)|
|Description     |Container that hosts Processing Unit instances deployed from the GSM. More information is available [here](../overview/the-runtime-environment.html#gsc).|
|Main Operations |List currently running Processing Unit instances.|
|Runtime Events  |Register for Processing Unit instance additions and removal events.|


{{%anchor GridServiceContainersLink%}}

|     |  |
|----|----|
|Name            |[GridServiceContainers]({{% api-javadoc %}}/org/openspaces/admin/gsc/GridServiceContainers.html)|
|Description     |Holds all the currently discovered Grid Service Containers.|
|Main Operations |Get all the currently discovered Grid Service Containers. Wait for X number of Grid Service Containers to be up.|
|Runtime Events  |Register for Grid Service Container addition (discovery) and removal events.  |


{{%anchor LookupServiceLink%}}

|     |  |
|----|----|
|Name            |[LookupService]({{% api-javadoc %}}/org/openspaces/admin/lus/LookupService.html)|
|Description     |A registry of services (GSM, GSC, Space instances, Processing Unit instances) that can be looked up using it. More information is available [here](../overview/the-runtime-environment.html#lus).|
|Main Operations |Get the Lookup Groups and Locator it was started with.|
|Runtime Events  |  |


{{%anchor LookupServicesLink%}}

|     |  |
|----|----|
|Name            |[LookupServices]({{% api-javadoc %}}/org/openspaces/admin/lus/LookupServices.html)|
|Description     |Holds all the currently discovered Lookup Services.|
|Main Operations |Get all the currently discovered Lookup Services. Wait for X number of Lookup Services to be up.|
|Runtime Events  |Register for Lookup Service addition (discovery) and removal events. |


{{%anchor ProcessingUnitLink%}}

|     |  |
|----|----|
|Name            |[ProcessingUnit]({{% api-javadoc %}}/org/openspaces/admin/pu/ProcessingUnit.html)|
|Description     |Deployable Processing unit running one or more Processing Unit Instances. Managed by the Grid Service Manager.|
|Main Operations |- Undeploy the Processing Unit<br>- Increase the number of Processing Unit instances (if allowed).<br>- Decrease the number of Processing Unit instances (if allowed).<br>- Get the deployment status of the Processing Unit.<br>- Get the managing Grid Service Manager.<br>- Get the list of backup Grid Service Managers.<br>- List all the currently running Processing Unit instances.<br>- Wait for X number of Processing Unit instances to be up.<br>- Get an embedded Space that the Processing Unit has.<br>- Wait for an embedded Space to be correlated (discovered) with the Processing Unit.|
|Runtime Events  |- Register for Processing Unit instance additions and removal events.<br>- Register for Processing Unit instance provision attempts, failures, successes and pending events.<br>- Register for Managing Grid Service Manager change events.<br>- Register for Space correlation events.<br>- Register for deployment status change events.<br>- Register for backup Grid Service Manager change events.|


{{%anchor ProcessingUnitInstanceLink%}}

|     |  |
|----|----|
|Name            |[ProcessingUnitInstance]({{% api-javadoc %}}/org/openspaces/admin/pu/ProcessingUnitInstance.html)|
|Description     |Actual instance of a Processing Unit running within a Grid Service Container.|
|Main Operations |- Destroy itself (if SLA is breached, will be instantiated again).<br>- Decrease itself (destroying itself in the process). Will not attempt to create it again.<br>- Relocate itself to a different Grid Service Container.<br>- List all its inner services (such as event containers).<br>- Get the embedded Space instance running within it (if there is one).<br>- Get the JEE container details if it is a web Processing Unit.|
|Runtime Events  | [Service Monitors](#servicemonitors)|


{{%anchor ProcessingUnitsLink%}}

|     |  |
|----|----|
|Name            |[ProcessingUnits]({{% api-javadoc %}}/org/openspaces/admin/pu/ProcessingUnits.html)|
|Description     | Holds all the currently deployed Processing Units|
|Main Operations |Get all the currently deployed Processing Units.<br>- Wait for (and return) a Processing by a specific name.|
|Runtime Events  |- Register for Processing Unit deployments and undeployment events.<br>- Register for all Processing Unit instance addition and removal events (across all Processing Units).<br>- Register for all Processing Unit instance provision attempts, failures, success and pending events (across all Processing Units).<br>- Register for Managing Grid Service Manager change events on all Processing Units.<br>- Register for backup Grid Service Manager change events on all Processing Units.|


{{%anchor SpaceLink%}}

|     |  |
|----|----|
|Name            |[Space]({{% api-javadoc %}}/org/openspaces/admin/space/Space.html)|
|Description     |Composed of one or more Space Instances to form a Space topology (cluster).|
|Main Operations |- Get all the currently running Space Instances that are part of the Space.<br>- Wait for X number of Space instances to be up.<br>- Get aggregated Space statistics.<br>- Get a clustered [Space](./the-gigaspace-interface-overview.html) to perform Space operations.|
|Runtime Events  |- Register for Space instance addition and removal events.<br>- Register for Space instance change mode events (for all Space instances that are part of the Space).<br>- Register for Space instance replication status change events (for all Space instances that are part of the Space).<br>- Register for aggregated Space statistics events (if monitoring).|


{{%anchor SpaceInstanceLink%}}

|     |  |
|----|----|
|Name            |[SpaceInstance]({{% api-javadoc %}}/org/openspaces/admin/space/SpaceInstance.html)|
|Description     |Actual instance of a Space that is part of a topology (cluster), usually running within a Processing Unit Instance.|
|Main Operations |- Get its Space mode (primary or backup).<br>- Get its replication targets.<br>- Get a direct [Space](./the-gigaspace-interface-overview.html) to perform Space operations.<br>- Get Space instance statistics.|
|Runtime Events  |- Register for replication status change events.<br>- Register for Space mode change events<br>- Register for Space instance statistics (if monitoring).|


{{%anchor SpacesLink%}}

|     |  |
|----|----|
|Name            |[Spaces]({{% api-javadoc %}}/org/openspaces/admin/space/Spaces.html)|
|Description     |Holds all the currently running Spaces.|
|Main Operations |- Get all the currently running Spaces.<br>- Wait for (and return) a specific Space by name.|
|Runtime Events |- Register for Space addition and removal events.<br>- Register for Space instance addition and removal events (across all Spaces).<br>- Register for Space instance mode change events (across all Space instances).<br>- Register for Space instance replication change events (across all Space Instances).<br>- Register for aggregated Space level statistics change events (across all Spaces, if monitoring).<br>- Register for Space instance statistics change events (across all Space Instances, if monitoring).|


{{%anchor VirtualMachineLink%}}

|     |  |
|----|----|
|Name            |[VirtualMachine]({{% api-javadoc %}}/org/openspaces/admin/vm/VirtualMachine.html)|
|Description     |A Virtual Machine (JVM) that is currently running at least one XAP component/service.|
|Main Operations |- Get the Grid Service Agent (if it exists).<br>- Get the Grid Service Manager (if it exists).<br>- Get the Grid Service Container (if it exists).<br>- Get all the Processing Unit instances that are running within the Virtual Machine.<br>- Get all the Space instances that are running within the Virtual Machine.<br>- Get the details of the Virtual Machine (min/max memory, etc.).<br>- Get the statistics of the Virtual Machine (heap used), and so on).|
|Runtime Events  |- Register for Processing Unit instance addition and removal events.<br>- Register for Space instance addition and removal events.<br>- Register for statistics change events (if monitoring).|

{{%anchor VirtualMachinesLink%}}

|     |  |
|----|----|
|Name            |[VirtualMachines]({{% api-javadoc %}}/org/openspaces/admin/vm/VirtualMachines.html)|
|Description     |Holds all the currently discovered Virtual Machines.|
|Main Operations |- Get all the currently discovered Virtual Machines.<br>- Get aggregated Virtual Machine details.<br>- Get aggregated Virtual Machine statistics.|
|Runtime Events  |- Register for Virtual Machine addition and removal events.<br>- Register for aggregated Virtual Machine statistics events (if monitoring).<br>- Register for Virtual Machine level statistics change events (across all Virtual Machines, if monitoring).|


{{%anchor MachineLink%}}

|     |  |
|----|----|
|Name            |[Machine]({{% api-javadoc %}}/org/openspaces/admin/machine/Machine.html)|
|Description     |Actual Machine (identified by its host address) running one or more XAP components/services in one or more Virtual Machines. Associated with one operating system.|
|Main Operations |- Get all the Grid Service Agents running on the Machine.<br>- Get all the Grid Service Containers running on the Machine.<br>- Get all the Grid Service Managers running on the Machine.<br>- Get all the Virtual Machines running on the Machine.<br>- Get all the Processing Unit instances running on the Machine.<br>- Get all the Space instances running on the Machine.<br>- Get the operating system the Machine is running on.|
|Runtime Events  |- Register for Space instance addition and removal events from the Machine.<br>- Register for Processing Unit instance additions and removals events from the Machine.|


{{%anchor MachinesLink%}}

|     |  |
|----|----|
|Name            |[Machines]({{% api-javadoc %}}/org/openspaces/admin/machine/Machines.html)|
|Description     |Holds all the currently discovered Machines|
|Main Operations |- Get all the currently running Machines.<br>- Wait for X number of Machines or be up.|
|Runtime Events  |- Register for Machine addition and removal events.|


{{%anchor OperatingSystemLink%}}

|     |  |
|----|----|
|Name            |[OperatingSystem]({{% api-javadoc %}}/org/openspaces/admin/os/OperatingSystem.html)|
|Description     |The operating system that XAP components/services are running on. Associated with one Machine.|
|Main Operations |- Get the details of the operating system.<br>- Get the operating system statistics.|
|Runtime Events  |Register for statistics change events (if monitoring).|

{{%anchor OperatingSystemsLink%}}

|     |  |
|----|----|
|Name            |[OperatingSystems]({{% api-javadoc %}}/org/openspaces/admin/os/OperatingSystems.html)|
|Description     |Holds all the currently discovered operating systems.|
|Main Operations |- Get all the current operating systems.<br>- Get the aggregated operating system details.<br>- Get the aggregated operating system statistics.|
|Runtime Events  |- Register for aggregated operating system statistics change events (if monitoring).<br>- Register for operating system level statistics change events (across all operating systems, if monitoring).|

{{%anchor TransportLink%}}

|     |  |
|----|----|
|Name            |[Transport]({{% api-javadoc %}}/org/openspaces/admin/transport/Transport.html)|
|Description     |Communication layer that each XAP component/service uses.|
|Main Operations |- Get the transport details (host, port).<br>- Get the transport statistics.|
|Runtime Events  |Register for transport statistics change events (if monitoring).|

{{%anchor TransportsLink%}}

|     |  |
|----|----|
|Name            |[Transports]({{% api-javadoc %}}/org/openspaces/admin/transport/Transports.html)|
|Description     |Holds all the currently discovered transports.|
|Main Operations |- Get all the current transports.<br>- Get the aggregated transport details.<br>- Get the aggregated transport statistics.|
|Runtime Events  |- Register for aggregated transport statistics change events (if monitoring).<br>- Register for transport level statistics change events (across all transports, if monitoring).|

{{% note "Note"%}}
The `Admin` functionality is designed to interact with a service grid deployment. For `StandaloneProcessingUnitContainer` and `IntegratedProcessingUnitContainer`, components such as `GridServiceAgent` and `GridServiceManager` are not started by default, therefore portions of the Admin functionality aren't available. 
{{%/note%}}


# Accessing the Domain Model

There are two ways to use the Admin API to access information:

- Call specific "getters" for the data and iterate over them (as shown in the example at the top of the page).
- Register for specific events using the Admin API. Events are handled by different components of the Admin API in similar manner. We will take one of them and use it as a reference example.

If we want to register, for example, for Grid Service Container additions, we can use the following code (removing the event listener is not shown here for clarity):


```java
admin.getGridServiceContainers().getGridServiceContainerAdded().add(new GridServiceContainerAddedEventListener() {
    public void gridServiceContainerAdded(GridServiceContainer gridServiceContainer) {
        // do something here
    }
});
```

Removals are done in similar manner:


```java
admin.getGridServiceContainers().getGridServiceContainerRemoved().add(new GridServiceContainerRemovedEventListener() {
    public void gridServiceContainerRemoved(GridServiceContainer gridServiceContainer) {
        // do something here
    }
});
```

Since both removals and additions are common events that we would like to register for at the same time, we can use:


```java
admin.getGridServiceContainers().addLifecycleListener(new GridServiceContainerLifecycleEventListener() {
    public void gridServiceContainerAdded(GridServiceContainer gridServiceContainer) {
        // do something here
    }

    public void gridServiceContainerRemoved(GridServiceContainer gridServiceContainer) {
        // do something here
    }
});
```

All other data structures use similar APIs to register for events. Some might have specific events that go beyond additions and removals, but they still use the same model. For example, you can register for Space mode change events across all currently running Space topologies and Space instances:


```java
admin.getSpaces().getSpaceModeChanged().add(new SpaceModeChangedEventListener() {
    public void spaceModeChanged(SpaceModeChangedEvent event) {
        System.out.println("Space [" + event.getSpaceInstance().getSpace().getName() + "] " +
		"Instance [" + event.getSpaceInstance().getInstanceId() + "/" +
                event.getSpaceInstance().getBackupId() + "] " +
		"changed mode from [" + event.getPreviousMode() + "] to [" + event.getNewMode() + "]");
    }
});
```

Of course, you can register the same listener on a specific `Space` topology, or event on a specific `SpaceInstance`.

Last, the `Admin` interface provides a one-stop method called `addEventListener` that accepts an `AdminListener`. Most event listeners implement this interface. You can create a class that implements several chosen listener interfaces, call the `addEventListener` method, and they will automatically be added to their respective components. For example, if our listener implements `GridServiceContainerAddedEventListener` and `GridServiceManagerAddedEventListener`, the listener will automatically be added to the `GridServiceManagers` and `GridServiceContainers`.

# Details and Statistics

- Some components in the Admin API can provide statistics. For example, a `SpaceInstance` can provide statistics on how many times the read API was called on it. Statistics change over time, and in order to get them either the "getter" for the Statistics can be used, or a statistics listener can be registered for statistics change events.

- Details of a specific component provide information that doesn't change over time, but can be used to provide more information regarding the component, or to compute statistics. For example, the VirtualMachine provides in its details the minimum and maximum heap memory size, while the VirtualMachine statistics provide the currently used heap memory size. The detailed information is used to provide the percentage used in the Virtual Machine statistics.

- The Admin API also provide aggregated details and statistics. For example, the `Space` provides `SpaceStatistics` allowing to get the aggregated statistics of all the different Space Instances that belong to it.

- Each component in the Admin API that can provide statistics (either direct or aggregated) implements the `StatisticsMonitor` interface. The statistics monitor allows starting to monitor statistics and stop to monitor statistics. Monitoring for statistics is required if you want to register for statistics change events. The interval at which statistics are polled is controlled using the statistics interval.

- The statistics interval is an important event when the Admin API isn't actively polling for statistics. Each call to a statistics "getter" only performs a remote call to the component if the last statistics fetch happened **before** the statistics interval. This behavior allows for users of the Admin API to not worry about "hammering" different components for statistics, because the Admin API ensures that statistics calls are cached internally for the statistics interval period.

- A `SpaceInstance` implements the `StatisticsMonitor` interface. Calling `startMonitor` and `stopMonitor` on it causes monitoring of statistics to be enabled or disabled.

- `Space` also implements the `StatisticsMonitor` interface. Calling `startMonitor` on it will cause it to start monitoring all its `SpaceInstance` s. If a `SpaceInstance` is discovered after the the call to `startMonitor` occurred, it starts monitoring itself automatically. This means that if a `SpaceInstanceStatisticsChangedEventListener` was registered on the `Space`, it automatically starts getting Space instance statistics change events for the newly discovered `SpaceInstance`.

- `Spaces` also implements the `StatisticsMonitor` interface. Calling `startMonitor` on it will cause it to start monitoring all the `Space` s it has (and as a result, also `SpaceInstance` s, see the paragraph above). A `SpaceInstanceStatisticsChangedEventListener` can also be registered on the `Spaces` level.

- The above Space level statistics behavior works in much the way with other components. For example, for `VirutalMachine` and `VirtualMachines`, `Transport` and `Transports`, `OperatingSystem` and `OperatingSystems`.

- The `Admin` interface also implements the `StatisticsMonitor` interface. Calling `startMonitor` on it will cause all holders to start monitoring. These include `Spaces`, `VirtualMachines`, `Transports`, and `OperatingSystems`.

# Space Runtime Statistics

The Space maintains statistics information about all the data types (for example, Space class types) it stores, and the amount of Space objects stored in memory for each data type. The following example shows how to retrieve this data. This approach eliminates the need to use the `GigaSpace.Count()`, which is relatively expensive, with Spaces that store large number of objects.

If you are monitoring the Space class instance count with a monitoring tool, you should use the approach below rather calling the `GigaSpace.Count()`.


```java
public static void printAllClassInstanceCountForAllPartitions (Admin admin , String spaceName) throws Exception
{
	Space space = admin.getSpaces().waitFor(spaceName, 10 , TimeUnit.SECONDS);
	SpacePartition spacePartitions[] = space.getPartitions();
	System.out.println(spaceName + " have " +spacePartitions.length + " Partitions , Total " + space.getTotalNumberOfInstances() + " Instances");
	Arrays.sort(spacePartitions, new SpacePartitionsComperator ());

	for (int i = 0; i < spacePartitions.length; i++) {
		SpacePartition partition = spacePartitions [i];
		while (partition.getPrimary()==null){
			Thread.sleep(1000);
		}

		SpaceInstance primaryInstance = partition.getPrimary();
		System.out.println ("Partition " + partition.getPartitionId() + " Primary:");
		Map<String, Integer> classInstanceCountsPrimary = primaryInstance.getRuntimeDetails().getCountPerClassName();
		Iterator<String> keys = classInstanceCountsPrimary.keySet().iterator();
		while (keys.hasNext()){
			String className = keys.next();
			System.out.println ("Class:" + className +" Instance count:" + classInstanceCountsPrimary.get(className));
		}

		SpaceInstance backupInstance = partition.getBackup();
		System.out.println ("Partition " + partition.getPartitionId() + " Backup:");

		Map<String, Integer> classInstanceCountsBackup = backupInstance .getRuntimeDetails().getCountPerClassName();
		keys = classInstanceCountsPrimary.keySet().iterator();
		while (keys.hasNext()){
			String className = keys.next();
			System.out.println ("Class:" + className +" Instance count:" + classInstanceCountsBackup.get(className));
		}
	}
}

static class SpacePartitionsComperator implements Comparator<SpacePartition>{
	public int compare(SpacePartition o1,SpacePartition o2) {
		if (o2.getPartitionId() > o1.getPartitionId())
			return 0;
		else
			return 1;
	}
}
```

# MemoryXtend Statistics

If you have a system that utilizes MemoryXtend, you can monitor the performance of the off-heap storage and the external disk storage. For a list of available blobstore operation statistics, see the [Predefined Metrics](../admin/metrics-bundled.html#blobstore-operations) page in the Administration guide.

The following example (relevant for the disk storage driver) demonstrates how to retrieve RocksDB statistics using the Admin API.

```java

SpaceInstance spaceInstance =
	admin.getSpaces().getSpaceByName( "MySpace" ).getInstances()[0];
SpaceInstanceStatistics statistics = spaceInstance.getStatistics();
if( statistics != null && statistics.getBlobStoreStatistics() != null ) {
    BlobStoreStatistics blobStoreStatistics = statistics.getBlobStoreStatistics();
   Collection<BlobStoreStorageStatistics> storageStatistics =
                            blobStoreStatistics.getStorageStatistics();
    if( storageStatistics != null && !storageStatistics.isEmpty() ) {
        BlobStoreStorageStatistics blobStoreStorageStatistics =
                                    storageStatistics.iterator().next();
if (blobStoreStorageStatistics instanceof RocksDBBlobStoreStatistics) {
 	RocksDBBlobStoreStatistics rocksDBStatistics =
    (RocksDBBlobStoreStatistics) blobStoreStorageStatistics;
	long memtableHit = rocksDBStatistics.getMemtableHit();
	long memtableMiss = rocksDBStatistics.getMemtableMiss();
	long getHitL0 = rocksDBStatistics.getGetHitL0();
	long getHitL1 = rocksDBStatistics.getGetHitL1();
	long getHitL2AndUp = rocksDBStatistics.getHitL2AndUp();
	long numberKeysWritten = rocksDBStatistics.getNumberKeysWritten();
	long numberKeysRead = rocksDBStatistics.getNumberKeysRead();
	long numberKeysUpdated = rocksDBStatistics.getNumberKeysUpdated();
	long bytesWritten = rocksDBStatistics.getBytesWritten();
	long bytesRead = rocksDBStatistics.getBytesRead();
	long iterBytesRead = rocksDBStatistics.getIterBytesRead();
	long numberMultigetCalls = 		rocksDBStatistics.getNumberMultigetCalls();
	long numberMultigetKeysRead = 		rocksDBStatistics.getNumberMultigetKeysRead();
	long numberMultigetBytesRead = 	rocksDBStatistics.getNumberMultigetBytesRead();
      }
}
``` 


# Monitoring the Mirror Service

You can monitor various aspects of the mirror service using the Admin API.

The mirror statistics are available using the `SpaceInstance` statistics. They can be used to monitor the state of the mirror Space and whether or not it is functioning properly. These statistics are relevant only for a mirror Space instance, and are not available for ordinary Space instances. The code below traverses all the Space instances and finds the mirror Space by retrieving the mirror statistics object (if it isn't null, this means it is a mirror Space). It then prints out some of the available statistics.


```java
for (Space space : admin.getSpaces()) {
    System.out.println("Space [" + space.getUid() + "] numberOfInstances [" +
    space.getNumberOfInstances() + "] numberOfbackups [" +
    space.getNumberOfBackups() + "]");

    for (SpaceInstance spaceInstance : space) {
        System.out.println("   -> INSTANCE [" + spaceInstance.getUid() + "] instanceId [" + spaceInstance.getInstanceId() +
        "] backupId [" + spaceInstance.getBackupId() + "] Mode [" + spaceInstance.getMode() + "]");
        System.out.println("         -> Host: " + spaceInstance.getMachine().getHostAddress());

         MirrorStatistics mirrorStat = spaceInstance.getStatistics().getMirrorStatistics();
         // check if this instance is mirror
         if(mirrorStat != null){
            System.out.println("Mirror Stats:");
            System.out.println("total operation count:" + mirrorStat.getOperationCount());
            System.out.println("successful operation count:" + mirrorStat.getSuccessfulOperationCount());
            System.out.println("failed operation count:" + mirrorStat.getFailedOperationCount());
         }
    }
}

```

{{%refer%}}
For more information, refer to the API documentation on [MirrorStatistics]({{% api-javadoc %}}/com/gigaspaces/cluster/replication/async/mirror/MirrorStatistics.html).
{{%/refer%}}


# PU Status Changed Events

You can receive notifications about the deployment status of a Processing Unit. An event listener can be attached to the Admin API that delivers events describing the current and previous [DeploymentStatus]({{% api-javadoc %}}/index.html?org/openspaces/admin/pu/DeploymentStatus.html) of a Processing Unit.
Here is an example:

{{%tabs%}}
{{%tab " ChangeStatus"%}}

```java
public class ProcessingUnitStatusChanged {

	public void processingUnitStatusChanged() {
		Admin admin = new AdminFactory().addGroup("myGroup").createAdmin();
		MyProcessingUnitStatusChangedEventListener listener = new MyProcessingUnitStatusChangedEventListener();
		admin.addEventListener(listener);
	}
}
```
{{%/tab%}}

{{%tab " Listener"%}}

```java
public class MyProcessingUnitStatusChangedEventListener implements
		ProcessingUnitStatusChangedEventListener {

	@Override
	public void processingUnitStatusChanged(
			ProcessingUnitStatusChangedEvent event) {

		DeploymentStatus newStatus = event.getNewStatus();
		if (newStatus.equals(DeploymentStatus.INTACT))
		{
			// All has settled down
		}
	}
}
```
{{%/tab%}}
{{%/tabs%}}

# Monitoring Processing Unit Health 

For monitoring purposes, the Processing Unit can be queried to show its deployment status, number of actual instances vs. planned, and more details per Processing Unit instance. The planned vs. actual represents the current state, and querying this API returns an updated result each time a scheduled update is done by the underlying Admin. A plan changes on increments, decrements, restarts and relocation of instances. 


```java
    ProcessingUnit processingUnit = admin.getProcessingUnits().getProcessingUnit("myProceessingUnitName");
    Properties properties = new Properties();
    properties.put("Name", processingUnit.getName());
    properties.put("Status", processingUnit.getStatus());
    properties.put("Type", processingUnit.getType());
    properties.put("Actual/Planned", processingUnit.getInstances().length + "/" + processingUnit.getPlannedNumberOfInstances());
    properties.put("SLA", processingUnit.getNumberOfInstances() + (processingUnit.getNumberOfBackups()>0 ? "," + processingUnit.getNumberOfBackups(): ""));
```

{{%anchor servicemonitors%}}

# Service Monitors

The Admin API allows you to monitor XAP services. The information is available through the [ProcessingUnitInstanceStatistics]({{% api-javadoc %}}/index.html?org/openspaces/admin/pu/ProcessingUnitInstanceStatistics.html).


| Service | Description |
|:--------|:------------|
|[WebRequestsServiceMonitors]({{% api-javadoc %}}/index.html?org/openspaces/pu/container/jee/stats/WebRequestsServiceMonitors.html) |Statistics monitor information for JEE servlet requests.|
|[RemotingServiceMonitors]({{% api-javadoc %}}/index.html?org/openspaces/remoting/RemotingServiceDetails.html) | Generic remoting service details.|
|[EventContainerServiceMonitors]({{% api-javadoc %}}/index.html?org/openspaces/events/EventContainerServiceMonitors.html) | A generic event container service monitors.|
|[PollingEventContainerServiceMonitors]({{% api-javadoc %}}/index.html?org/openspaces/events/polling/PollingEventContainerServiceMonitors.html) |Polling container service monitors.|
|[NotifyEventContainerServiceMonitors]({{% api-javadoc %}}/index.html?org/openspaces/events/notify/NotifyEventContainerServiceMonitors.html) |Notify container service monitors.|
|[AsyncPollingEventContainerServiceMonitors]({{% api-javadoc %}}/index.html?org/openspaces/events/asyncpolling/AsyncPollingEventContainerServiceMonitors.html)  |Async Polling container service monitors.|


Here is an example how you can obtain this information:


```java
public void serviceMonitors() {
    Admin admin = new AdminFactory().createAdmin();

	for (ProcessingUnit processingUnit : admin.getProcessingUnits()) {
		for (ProcessingUnitInstance processingUnitInstance : processingUnit.getInstances()) {
			ProcessingUnitInstanceStatistics processingUnitInstanceStatistics = processingUnitInstance.getStatistics();
			if (processingUnitInstanceStatistics != null) {
				WebRequestsServiceMonitors webRequestsServiceMonitors = processingUnitInstanceStatistics.getWebRequests();
				RemotingServiceMonitors remoting = processingUnitInstanceStatistics.getRemoting();
				Map<String, EventContainerServiceMonitors> eventContainers = processingUnitInstanceStatistics.getEventContainers();
				Map<String, PollingEventContainerServiceMonitors> pollingEventContainers = processingUnitInstanceStatistics.getPollingEventContainers();
				Map<String, NotifyEventContainerServiceMonitors> notifyEventContainers = processingUnitInstanceStatistics.getNotifyEventContainers();
				Map<String, AsyncPollingEventContainerServiceMonitors> asyncPollingEventContainers = processingUnitInstanceStatistics.getAsyncPollingEventContainers();
			}
		}
	}
}
```


You can also attach a [ProcessingUnitInstanceStatisticsChangedEventListener]({{% api-javadoc %}}/index.html?org/openspaces/admin/pu/events/ProcessingUnitInstanceStatisticsChangedEventListener.html) to Processing Units that will fire by default every 5 seconds.


```java
public void serviceMonitorsEventListeners() {
	Admin admin = new AdminFactory().createAdmin();

	admin.getProcessingUnits()
	    .getProcessingUnit("myProcessingUnit")
		.getProcessingUnitInstanceStatisticsChanged()
		.add(new ProcessingUnitInstanceStatisticsChangedEventListener() {

		@Override
		public void processingUnitInstanceStatisticsChanged(ProcessingUnitInstanceStatisticsChangedEvent event) {
		    ProcessingUnitInstanceStatistics statistics = event.getStatistics();
			Map<String, EventContainerServiceMonitors> eventContainers = statistics.getEventContainers();
			WebRequestsServiceMonitors webRequests = statistics.getWebRequests();
			// .....
		}
	});
}
```

By default, the listener fires every 5 seconds. You can change all the intervals at once as follows. This will set the timer on the following objects: `Spaces`,`VirtualMachines`,`Transports`,`OperatingSystems`, and `ProcessingUnits`.


```java
public void changeServiceMonitorsTimer() {
	Admin admin = new AdminFactory().createAdmin();
    admin. setStatisticsInterval(30l, TimeUnit.SECONDS);
}
```




You can also change the intervals individually:


```java
Spaces.setStatisticsInterval(30l, TimeUnit.SECONDS);
VirtualMachines.setStatisticsInterval(10l, TimeUnit.MINUTES);
Transports.setStatisticsInterval(10l, TimeUnit.SECONDS);
OperatingSystems.setStatisticsInterval(3l, TimeUnit.MINUTES);
ProcessingUnits.setStatisticsInterval(30l, TimeUnit.SECONDS);
```
