---
type: post
title:  Administration API
weight: 500
parent: admin-tools.html
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

![admin_DomainModel.jpg](/attachment_files/admin_DomainModel.jpg)

{{% note "Note"%}}
The `Admin` functionality is designed to interact with a service grid deployment. For `StandaloneProcessingUnitContainer` and `IntegratedProcessingUnitContainer`, components such as `GridServiceAgent` and `GridServiceManager` are not started by default, therefore portions of the Admin functionality aren't available. 
{{%/note%}}


## Accessing the Domain Model

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