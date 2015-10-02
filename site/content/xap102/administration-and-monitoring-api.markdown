---
type: post102
title:  Overview
categories: XAP102
parent: administration-and-monitoring-overview.html
weight: 100
---


{{% ssummary%}}{{% /ssummary %}}



Before diving into the Admin API, here are some code examples showing how the Admin API can be used to display information on the currently deployed services / components:

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


Obtaining information about the currently deployed services / components via the started `GridServiceAgents`.

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

```
{{%/tab%}}

{{%/tabs%}}


{{% refer %}}
See a fully running example of a [Scaling Agent](/sbp/scaling-agent.html) as part of the Solutions & Patterns section.
{{% /refer %}}

# Admin Construction

The Admin API uses the `AdminFactory` in order to create `Admin` instances. Once working with the `Admin` is done, its `Admin#close()` method should be called.

The Admin discovers all the advertised services from the [Lookup Service](/product_overview/service-grid.html#lus). In order to define which lookup groups the `AdminFactory#addGroup` can be used. The lookup locators can also be used for non multicast enabled environment using `AdminFactory#addLocator` can be used. If the services started are secured, the username and password can be set on the Admin API as well.


# Discovery Process

Once the `Admin` is created, it will start to receive discovery events of all the advertised services / components within its lookup groups / lookup locators. Note, the events occur asynchronously and the data model within the Admin gets initialized in the background with services coming and going.

This means that just creating the `Admin` and calling a specific "getter" for a data structure might not return what is currently deployed, and one should wait till the structures are filled. Some components has a waitFor method that allow to wait for specific number of services to be up. When navigating the data model, the Admin API will provide its most up to date state of the system it is monitoring.

# Domain Model

The Admin Domain Model has representation to all GigaSpaces level main actors. They include:

![admin_DomainModel.jpg](/attachment_files/admin_DomainModel.jpg)


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
|Name            |[GridServiceAgent](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/gsa/GridServiceAgent.html)|
|Description     |A process manager that manages Service Grid processes such as GSM, GSC and LUS. More info [here](/product_overview/service-grid.html#gsa).|
|Main Operations |Allows to list all the currently managed processes.- Start processes (GSM, GSC, LUS).       |
|Runtime Events  | |

{{%anchor GridServiceAgentsLink%}}

|     |  |
|----|----|
|Name            |[GridServiceAgents](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/gsa/GridServiceAgents.html)|
|Description     |Holds all the currently discovered Grid Service Agents.|
|Main Operations |Get all the currently discovered Grid Service Agents.Wait for X number of Grid Service Agents to be up.|
|Runtime Events  |Register for Grid Service Agent addition (discovery) and removals events. |

{{%anchor GridServiceManagerLink%}}

|     |  |
|----|----|
|Name            | [GridServiceManager](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/gsm/GridServiceManager.html)|
|Description     |Managing Processing Unit deployments and Grid Service Containers. More info [here](/product_overview/service-grid.html#gsm).|
|Main Operations |Deploy Processing Units. Deploy pure Space Processing Units. Get the Grid Service Agent Managing it. Restart itself (if managed by a Grid Service Agent).Kill itself (if managed by a Grid Service Agent).|
|Runtime Events  |  |

{{%anchor GridServiceManagersLink%}}

|     |  |
|----|----|
|Name            |[GridServiceManagers](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/gsm/GridServiceManagers.html)|
|Description     |Holds all the currently discovered Grid Service Managers.|
|Main Operations |Deploy Processing Units on a random Grid Service Manager. Deploy pure Space Processing Units on a random Grid Service Manager. Get all the currently discovered Grid Service Managers. Wait for X number of Grid Service Managers to be up.|
|Runtime Events  |Register for Grid Service Manager addition (discovery) and removals events.|

{{%anchor GridServiceContainerLink%}}

|     |  |
|----|----|
|Name            |[GridServiceContainer](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/gsc/GridServiceContainer.html)|
|Description     | Container hosting Processing Unit Instances deployed from the GSM. More info [here](/product_overview/service-grid.html#gsc).|
|Main Operations | List currently running Processing Units Instances.|
|Runtime Events  | Register for Processing Unit Instance additions and removals events.|


{{%anchor GridServiceContainersLink%}}

|     |  |
|----|----|
|Name            | [GridServiceContainers](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/gsc/GridServiceContainers.html)|
|Description     |Holds all the currently discovered Grid Service Containers.|
|Main Operations |Get all the currently discovered Grid Service Containers.{{<wbr>}}- Wait for X number of Grid Service Containers to be up.|
|Runtime Events  |Register for Grid Service Container addition (discovery) and removals events.  |


{{%anchor LookupServiceLink%}}

|     |  |
|----|----|
|Name            | [LookupService](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/lus/LookupService.html)|
|Description     | A registry of services (GSM, GSC, Space Instances, Processing Unit Instances) that can be lookup up using it. More info [here](/product_overview/the-lookup-service.html#lus).|
|Main Operations | Get the Lookup Groups and Locator it was started with.|
|Runtime Events  |  |


{{%anchor LookupServicesLink%}}

|     |  |
|----|----|
|Name            |[LookupServices](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/lus/LookupServices.html)|
|Description     |Holds all the currently discovered Lookup Services.|
|Main Operations |Get all the currently discovered Lookup Services.Wait for X number of Lookup Services to be up.|
|Runtime Events  |Register for Lookup Service addition (discovery) and removals events. |


{{%anchor ProcessingUnitLink%}}

|     |  |
|----|----|
|Name            |  [ProcessingUnit](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/pu/ProcessingUnit.html)|
|Description     | A deployable processing unit running one or more Processing Unit Instances. Managed by the Grid Service Manager.|
|Main Operations |Undeploy the Processing Unit{{<wbr>}}- Increase the number of Processing Units Instances (if allowed).{{<wbr>}}- Decrease the number of Processing Unit Instances (if allowed).{{<wbr>}}- Get the deployment status of the Processing Unit.{{<wbr>}}- Get the managing Grid Service Manager.{{<wbr>}}- Get the list of backup Grid Service Managers.{{<wbr>}}- List all the currently running Processing Unit Instances.{{<wbr>}}- Wait for X number of Processing Unit Instances or be up.{{<wbr>}}- Get an embedded Space that the Processing Unit has.{{<wbr>}}- Wait for an embedded Space to be correlated (discovered) with the Processing Unit.|
|Runtime Events  |Register for Processing Unit Instances additions and removals events.{{<wbr>}}- Register for Processing Unit Instance provision attempts, failures, success and pending events.{{<wbr>}}- Register for Managing Grid Service Manager change events.{{<wbr>}}- Register for Space correlation events.{{<wbr>}}- Register for deployment status change events.{{<wbr>}}- Register for backup Grid Service Manager change events.|


{{%anchor ProcessingUnitInstanceLink%}}

|     |  |
|----|----|
|Name            | [ProcessingUnitInstance](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/pu/ProcessingUnitInstance.html)|
|Description     | An actual instance of a Processing Unit running within a Grid Service Container.|
|Main Operations | Destroy itself (if SLA is breached, will be instantiated again).{{<wbr>}}- Decrease itself (and destroying itself in the process). Will not attempt to create it again.{{<wbr>}}- Relocate itself to a different Grid Service Container.{{<wbr>}}- List all its inner services (such as event containers).{{<wbr>}}- Get the embedded Space Instance running within it (if there is one).{{<wbr>}}- Get the JEE container details if it is a web processing unit.|
|Runtime Events  | [Service Monitors](#servicemonitors)|


{{%anchor ProcessingUnitsLink%}}

|     |  |
|----|----|
|Name            |[ProcessingUnits](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/pu/ProcessingUnits.html)|
|Description     |  Holds all the currently deployed Processing Units|
|Main Operations |Get all the currently deployed Processing Units.{{<wbr>}}- Wait for (and return) a Processing by a specific name.|
|Runtime Events  |Register for Processing Unit deployments and undeployment events.{{<wbr>}}- Register for all Processing Unit Instance addition and removal events (across all Processing Units).{{<wbr>}}- Register for all Processing Unit Instance provision attempts, failures, success and pending events (across all Processing Units).{{<wbr>}}- Register for Managing Grid Service Manager change events on all Processing Units.{{<wbr>}}- Register for backup Grid Service Manager change events on all Processing Units.|


{{%anchor SpaceLink%}}

|     |  |
|----|----|
|Name            | [Space](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/space/Space.html)|
|Description     | Composed of one or more Space Instances to form a Space topology (cluster)|
|Main Operations | Get all the currently running Space Instance that are part of the Space.{{<wbr>}}- Wait for X number of Space Instances to be up.{{<wbr>}}- Get aggregated Space statistics.{{<wbr>}}- Get a clustered [GigaSpace](./the-gigaspace-interface.html) to perform Space operations.|
|Runtime Events  | Register for Space Instance additions and removals events.{{<wbr>}}- Register for Space Instance change mode events (for all Space Instances that are part of the Space).{{<wbr>}}- Register for Space Instance replication status change events (for all Space Instances that are part of the Space).{{<wbr>}}- Register for aggregated Space statistics events (if monitoring).|


{{%anchor SpaceInstanceLink%}}

|     |  |
|----|----|
|Name            |[SpaceInstance](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/space/SpaceInstance.html)|
|Description     |An actual instance of a Space that is part of a topology (cluster), usually running within a Processing Unit Instance|
|Main Operations | Get its Space Mode (primary or backup).{{<wbr>}}- Get its replication targets.{{<wbr>}}- Get a direct [GigaSpace](./the-gigaspace-interface.html) to perform Space operations.{{<wbr>}}- Get Space Instance statistics.|
|Runtime Events  |Register for replication status change events.{{<wbr>}}- Register for Space Mode change events{{<wbr>}}- Register for Space Instance statistics (if monitoring).|


{{%anchor SpacesLink%}}

|     |  |
|----|----|
|Name            |[Spaces](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/space/Spaces.html)|
|Description     | Holds all the currently running Spaces|
|Main Operations |Get all the currently running Spaces.{{<wbr>}}- Wait for (and return) a specific Space by name.|
|Runtime Events |Register for Space additions and removal events.{{<wbr>}}- Register for Space Instance additions and removal events (across all Spaces).{{<wbr>}}- Register for Space Instance Mode change events (across all Space Instances).{{<wbr>}}- Register for Space Instance replication change events (across all Space Instances).{{<wbr>}}- Register for aggregated Space level statistics change events (across all Spaces, if monitoring).{{<wbr>}}- Register for Space Instance statistics change events (across all Space Instances, if monitoring).|


{{%anchor VirtualMachineLink%}}

|     |  |
|----|----|
|Name            |  [VirtualMachine](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/vm/VirtualMachine.html)|
|Description     | A virtual machine (JVM) that is currently running at least one GigaSpaces component / service.|
|Main Operations |Get the Grid Service Agent (if exists).{{<wbr>}}- Get the Grid Service Manager (if exists).{{<wbr>}}- Get the Grid Service Container (if exists).{{<wbr>}}- Get all the Processing Unit Instances that are running within the Virtual Machine.{{<wbr>}}- Get all the Space Instances that are running within the Virtual Machine.{{<wbr>}}- Get the details of the Virtual Machine (min/max memory, and so on).{{<wbr>}}- Get the statistics of the Virtual Machine (heap used, and so on).|
|Runtime Events  | Register for Processing Unit Instance additions and removals events.{{<wbr>}}- Register for Space Instance additions and removals events.{{<wbr>}}- Register for statistics change events (if monitoring).|

{{%anchor VirtualMachinesLink%}}

|     |  |
|----|----|
|Name            |[VirtualMachines](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/vm/VirtualMachines.html)|
|Description     | Holds all the currently discovered Virtual Machines|
|Main Operations | Get all the currently discovered Virtual Machines.{{<wbr>}}- Get aggregated Virtual Machines details.{{<wbr>}}- Get aggregated Virtual Machines statistics.|
|Runtime Events  | Register for Virtual Machines additions and removals events.{{<wbr>}}- Register for aggregated Virtual Machines statistics events (if monitoring).{{<wbr>}}- Register for Virtual Machine level statistics change events (across all Virtual Machines, if monitoring).|


{{%anchor MachineLink%}}

|     |  |
|----|----|
|Name            | [Machine](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/machine/Machine.html)|
|Description     | An actual Machine (identified by its host address) running one or more GigaSpaces components / services in one or more Virtual Machines. Associated with one Operating System|
|Main Operations | Get all the Grid Service Agents running on the Machine.{{<wbr>}}- Get all the Grid Service Containers running on the Machine.{{<wbr>}}- Get all the Grid Service Managers running on the Machine.{{<wbr>}}- Get all the Virtual Machines running on the Machine.{{<wbr>}}- Get all the Processing Unit Instances running on the Machine.{{<wbr>}}- Get all the Space Instances running on the Machine.{{<wbr>}}- Get the Operating System the Machine is running on.|
|Runtime Events  | Register for Space Instances additions and removals events from the Machine.{{<wbr>}}- Register for Processing Unit Instance additions and removals events from the Machine.|


{{%anchor MachinesLink%}}

|     |  |
|----|----|
|Name            | [Machines](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/machine/Machines.html)|
|Description     | Holds all the currently discovered Machines|
|Main Operations |  * Get all the currently running Machines.{{<wbr>}}- Wait for X number of Machines or be up.|
|Runtime Events  | * Register for Machine additions and removals events.|


{{%anchor OperatingSystemLink%}}

|     |  |
|----|----|
|Name            | [OperatingSystem](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/os/OperatingSystem.html)|
|Description     | The Operating System GigaSpaces components / services are running on. Associated with one Machine.|
|Main Operations | Get the details of the Operating System.{{<wbr>}}- Get the operating system statistics.|
|Runtime Events  | Register for statistics change events (if monitoring).|

{{%anchor OperatingSystemsLink%}}

|     |  |
|----|----|
|Name            |[OperatingSystems](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/os/OperatingSystems.html)|
|Description     |Holds all the currently discovered Operating Systems|
|Main Operations |Get all the current Operating Systems.{{<wbr>}}- Get the aggregated Operating Systems details.{{<wbr>}}- Get the aggregated Operating Systems statistics.|
|Runtime Events  | Register for aggregated Operating Systems statistics change events (if monitoring).{{<wbr>}}- Register for Operating System level statistics change events (across all Operating Systems, if monitoring).|

{{%anchor TransportLink%}}

|     |  |
|----|----|
|Name            |[Transport](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/transport/Transport.html)|
|Description     | The communication layer each GigaSpaces component / service uses|
|Main Operations | Get the Transport details (host, port).{{<wbr>}}- Get the Transport statistics.|
|Runtime Events  | Register for Transport statistics change events (if monitoring).|

{{%anchor TransportsLink%}}

|     |  |
|----|----|
|Name            | [Transports](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/transport/Transports.html)|
|Description     | Holds all the currently discovered Transports|
|Main Operations |  Get all the current Transports.{{<wbr>}}- Get the aggregated Transports details.{{<wbr>}}- Get the aggregated Transports statistics.|
|Runtime Events  |Register for aggregated Transports statistics change events (if monitoring).{{<wbr>}}- Register for Transport level statistics change events (across all Transports, if monitoring).|




# Accessing the Domain Model

There are two ways the Admin API can be used to access information the Admin API can provide.

- Call specific "getters" for the data and iterate over them (as shown in the example at the top of the page).
- Register for specific events using the Admin API. Events are handled by different components of the Admin API in similar manner. We will take one of them and use it as a reference example.

If we want to register, for example, for Grid Service Container additions, we can use the following code (note, removing the event listener is not shown here for clarity):


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

Since both removals and additions are common events that we would like to register for in one go, we can use:


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

All other data structures use similar API to register for events. Some might have specific events that goes beyond just additions and removals, but they still use the same model. For example, here is how we can register for Space Mode change events across all currently running Space topologies and Space Instances:


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

Of course, we can register the same listener on a specific `Space` topology or event on a specific `SpaceInstance`.

Last, the `Admin` interface provides a one stop method called `addEventListener` that accepts an `AdminListener`. Most events listener implement this interface. One can create a class that implements several chosen listener interfaces, call the `addEventListener` method, and they will automatically be added to their respective components. For example, if our listener implements `GridServiceContainerAddedEventListener` and `GridServiceManagerAddedEventListener`, the listener will automatically be added to the `GridServiceManagers` and `GridServiceContainers`.

# Details and Statistics

- Some components in the Admin API can provide statistics. For example, a `SpaceInstance` can provide statistics on how many times the read API was called on it. Statistics change over time, and in order to get them either the "getter" for the Statistics can be used, or a statistics listener can be registered for statistics change events.

- Details of a specific component provide information that does not change over time, but can be used to provide more information regarding the component, or to compute statistics. For example, the VirtualMachine provides in its details the minimum and maximum heap memory size, which the VirtualMachine statistics provide the currently used heap memory size. The detailed information is used to provide the percentage used in the Virtual Machine statistics.

- The Admin API also provide aggregated details and statistics. For example, the `Space` provides `SpaceStatistics` allowing to get the aggregated statistics of all the different Space Instances that belong to it.

- Each component in the Admin API that can provide statistics (either direct statistics, or aggregated statistics) implements the `StatisticsMonitor` interface. The statistics monitor allows to start to monitor statistics and stop to monitor statistics. Monitoring for statistics is required if one wishes to register for statistics change events. The interval that statistics will be polled is controlled using the statistics interval.

- The statistics interval is important event when the Admin API is not actively polling for statistics. Each call to a "getter" of statistics will only perform a remote call to the component if the last statistics fetch happened **before** the statistics interval. This behavior allows for users of the Admin API to not worry about "hammering" different components for statistics since the Admin will make sure that statistics calls are cached internally for the statistics interval period.

- A `SpaceInstance` implements the `StatisticsMonitor` interface. Calling `startMonitor` and `stopMonitor` on it will cause monitoring of statistics to be enabled or disabled on it.

- `Space` also implements the `StatisticsMonitor` interface. Calling `startMonitor` on it will cause it to start monitoring all its `SpaceInstance` s. If a `SpaceInstance` is discovered after the the call to `startMonitor` occurred, it will start monitoring itself automatically. This means that if the a `SpaceInstanceStatisticsChangedEventListener` was registered on the `Space`, it will automatically start to get Space Instance statistics change events for the newly discovered `SpaceInstance`.

- `Spaces` also implements the `StatisticsMonitor` interface. Calling `startMonitor` on it will cause it to start monitoring all the `Space` s it has (and as a result, also `SpaceInstance` s, see the paragraph above). A `SpaceInstanceStatisticsChangedEventListener` can also be registered on the `Spaces` level as well.

- The above Space level statistics behavior works in much the way with other components. For example, the `VirutalMachine` and `VirtualMachines`, `Transport` and `Transports`, `OperatingSystem` and `OperatingSystems`.

- The `Admin` interface also implements the `StatisticsMonitor` interface. Calling `startMonitor` on it will cause all holders to start monitoring. These include: `Spaces`, `VirtualMachines`, `Transports`, and `OperatingSystems`.

# Space Runtime Statistics

The space maintains statistics information about all the data types (e.g. space class types) it stores and the amount of space objects stored in memory for each data type. Below example how to retrieve this data. This approach avoiding the need to use the `GigaSpace.Count()` that is relatively expensive with spaces that store large number of objects.

If you are monitoring the space class instance count with some monitoring tool you should use the approach below rather calling the `GigaSpace.Count()`.


```java
public static void printAllClassInstanceCountForAllPartitions (Admin admin , String spaceName) throws Exception
{
	Space space = admin.getSpaces().waitFor(spaceName, 10 , TimeUnit.SECONDS);
	SpacePartition spacePartitions[] = space.getPartitions();
	System.out.println(spaceName + " have " +spacePartitions.length + " Partitions , Total " + space.getTotalNumberOfInstances() + " Instances");
	Arrays.sort(spacePartitions, new SpacePartitionsComperator ());

	for (int i = 0; i < spacePartitions.length; i++) {
		SpacePartition partition = spacePartitions [i];
		while (partition.getPrimary()==null)
		{
			Thread.sleep(1000);
		}

		SpaceInstance primaryInstance = partition.getPrimary();
		System.out.println ("Partition " + partition.getPartitionId() + " Primary:");
		Map<String, Integer> classInstanceCountsPrimary = primaryInstance.getRuntimeDetails().getCountPerClassName();
		Iterator<String> keys = classInstanceCountsPrimary.keySet().iterator();
		while (keys.hasNext())
		{
			String className = keys.next();
			System.out.println ("Class:" + className +" Instance count:" + classInstanceCountsPrimary.get(className));
		}

		SpaceInstance backupInstance = partition.getBackup();
		System.out.println ("Partition " + partition.getPartitionId() + " Backup:");

		Map<String, Integer> classInstanceCountsBackup = backupInstance .getRuntimeDetails().getCountPerClassName();
		keys = classInstanceCountsPrimary.keySet().iterator();
		while (keys.hasNext())
		{
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

# Monitoring the Mirror Service

You may monitor various aspects of the mirror service using the administration and monitoring API.
The mirror statistics are available using the `SpaceInstance` statistics. They can be used to monitor the state of the mirror space and whether or not it is functioning properly. These statistics are relevant only for a mirror space instance, and are not available for ordinary space instances. The code below traverses all the space instances and finds the mirror space by retrieving the mirror statistics object (if it isn't null this means it's a mirror space). It then prints out some of the available statistics.


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
         if(mirrorStat != null)
         {

            System.out.println("Mirror Stats:");
            System.out.println("total operation count:" + mirrorStat.getOperationCount());
            System.out.println("successful operation count:" + mirrorStat.getSuccessfulOperationCount());
            System.out.println("failed operation count:" + mirrorStat.getFailedOperationCount());
         }
    }

}

```

{{%refer%}}
For more information please refer to the API documentation: **[MirrorStatistics](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/com/gigaspaces/cluster/replication/async/mirror/MirrorStatistics.html)**
{{%/refer%}}

# Monitoring the Remote Transport Activity


# PU Status Changed Events

It is possible to receive notifications about the deployment status of a Processing Unit. An event listener can be attached to the Admin that delivers events describing the current and previous [DeploymentStatus](http://www.gigaspaces.com/docs/JavaDoc{{%currentversion%}}/index.html?org/openspaces/admin/pu/DeploymentStatus.html) of a Processing Unit.
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

# Processing Unit health monitoring

For monitoring purposes, the processing unit can be queried to show its deployment status, number of actual instances vs. planned, and more details per processing unit instance. Note that the plan vs. actual represent current state, and querying this API returns an updated result each time a scheduled update is done by the underlying Admin. A plan changes on increments, decrements, restart and relocation of instances. 


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

The admin API also allows you to monitor XAP services. The information is available through the [ProcessingUnitInstanceStatistics](http://www.gigaspaces.com/docs/JavaDoc{{%currentversion%}}/index.html?org/openspaces/admin/pu/ProcessingUnitInstanceStatistics.html).


| Service | Description |
|:--------|:------------|
|[WebRequestsServiceMonitors](http://www.gigaspaces.com/docs/JavaDoc{{%currentversion%}}/index.html?org/openspaces/pu/container/jee/stats/WebRequestsServiceMonitors.html) |Statistics monitor information for JEE servlet requests.|
|[RemotingServiceMonitors](http://www.gigaspaces.com/docs/JavaDoc{{%currentversion%}}/index.html?org/openspaces/remoting/RemotingServiceDetails.html) | Generic remoting service details.|
|[EventContainerServiceMonitors](http://www.gigaspaces.com/docs/JavaDoc{{%currentversion%}}/index.html?org/openspaces/events/EventContainerServiceMonitors.html) | A generic event container service monitors.|
|[PollingEventContainerServiceMonitors](http://www.gigaspaces.com/docs/JavaDoc{{%currentversion%}}/index.html?org/openspaces/events/polling/PollingEventContainerServiceMonitors.html) |Polling container service monitors.|
|[NotifyEventContainerServiceMonitors](http://www.gigaspaces.com/docs/JavaDoc{{%currentversion%}}/index.html?org/openspaces/events/notify/NotifyEventContainerServiceMonitors.html) |Notify container service monitors.|
|[AsyncPollingEventContainerServiceMonitors](http://www.gigaspaces.com/docs/JavaDoc{{%currentversion%}}/index.html?org/openspaces/events/asyncpolling/AsyncPollingEventContainerServiceMonitors.html)  |Async Polling container service monitors.|


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


You can also attach a [ProcessingUnitInstanceStatisticsChangedEventListener](http://www.gigaspaces.com/docs/JavaDoc{{%currentversion%}}/index.html?org/openspaces/admin/pu/events/ProcessingUnitInstanceStatisticsChangedEventListener.html) to process units that will fire by default every 5 second.


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

By default the listener will fire  every 5 second. You can change this in the following general way:


```java
public void changeServiceMonitorsTimer() {
	Admin admin = new AdminFactory().createAdmin();
    admin. setStatisticsInterval(30l, TimeUnit.SECONDS);
}
```

This will set the timer on the following objects: `Spaces`,`VirtualMachines`,`Transports`,`OperatingSystems`,`ProcessingUnits`


You can also change the intervals individually:


```java
Spaces.setStatisticsInterval(30l, TimeUnit.SECONDS);

VirtualMachines.setStatisticsInterval(10l, TimeUnit.MINUTES);

Transports.setStatisticsInterval(10l, TimeUnit.SECONDS);

OperatingSystems.setStatisticsInterval(3l, TimeUnit.MINUTES);

ProcessingUnits.setStatisticsInterval(30l, TimeUnit.SECONDS);
```


