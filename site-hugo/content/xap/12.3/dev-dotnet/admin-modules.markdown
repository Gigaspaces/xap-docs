---
type: post123
title:  Administration API
categories: XAP123NET, PRM
parent: none
weight: 1400
---

The Service Grid Admin API provides a way to administer and monitor all of the XAP services and components, using a simple API. The Admin API provides information and the ability to operate on the currently running Service Grid Agents, Service Grid Managers, Service Grid Containers, Lookup Services, Processing Units, and Spaces.

The following code examples show how the Admin API can be used to display information on the currently deployed services/components:

{{%tabs%}}

{{%tab "  GSA "%}}


```csharp
ServiceGridAdminBuilder adminBuilder = new ServiceGridAdminBuilder();
adminBuilder.Groups.Add("myGroup");
IServiceGridAdmin admin = adminBuilder.CreateAdmin();
...
// wait till things get discovered (you can also use specific WaitFor)

foreach (IGridServiceAgent gsa in admin.GridServiceAgents)
{
    Console.WriteLine("GSA [" + gsa.Uid + "] running on Machine [" + gsa.Machine.HostAddress);
    foreach (IAgentProcessDetails processDetails in gsa.ProcessesDetails)
    {
        Console.WriteLine("   -> Process [" + String.Join(",", processDetails.Command.ToArray()) + "]");
    }
}
```

{{% /tab %}}

{{%tab "  GSM "%}}


```csharp
ServiceGridAdminBuilder adminBuilder = new ServiceGridAdminBuilder();
adminBuilder.Groups.Add("myGroup");
IServiceGridAdmin admin = adminBuilder.CreateAdmin();
...
// wait till things get discovered (you can also use specific WaitFor)
foreach (IGridServiceManager gsm in admin.GridServiceManagers)
{
    Console.WriteLine("GSM [" + gsm.Uid + "] running on Machine " + gsm.Machine.HostAddress);
}
```

{{% /tab %}}

{{%tab "  GSC "%}}


```csharp
ServiceGridAdminBuilder adminBuilder = new ServiceGridAdminBuilder();
adminBuilder.Groups.Add("myGroup");
IServiceGridAdmin admin = adminBuilder.CreateAdmin();
...
// wait till things get discovered (you can also use specific WaitFor)

foreach (IGridServiceContainer gsc in admin.GridServiceContainers)
{
    Console.WriteLine("GSC [" + gsc.Uid + "] running on Machine " + gsc.Machine.HostAddress);
    foreach (IProcessingUnitInstance puInstance in gsc)
    {
        Console.WriteLine("   -> PU [" + puInstance.Name + "][" +
        puInstance.InstanceId + "][" + puInstance.BackupId + "]");
    }
}
```

{{% /tab %}}

{{%tab "  Processing Unit "%}}


```csharp
ServiceGridAdminBuilder adminBuilder = new ServiceGridAdminBuilder();
adminBuilder.Groups.Add("myGroup");
IServiceGridAdmin admin = adminBuilder.CreateAdmin();
...
// wait till things get discovered (you can also use specific WaitFor)

foreach (IProcessingUnit processingUnit in admin.getProcessingUnits())
{
    Console.WriteLine("Processing Unit: " + processingUnit.Name + " status: " + processingUnit.Status);
    if (processingUnit.Managed)
    {
        Console.WriteLine("   -> Managing GSM: " + processingUnit.ManagingGridServiceManager.Uid);
    }
    else
    {
        Console.WriteLine("   -> Managing GSM: NA");
    }
    foreach (IGridServiceManager backupGSM in processingUnit.BackupGridServiceManagers)
    {
        Console.WriteLine("   -> Backup GSM: " + backupGSM.Uid);
    }
    foreach (IProcessingUnitInstance processingUnitInstance in processingUnit)
    {
        Console.WriteLine("   [" + processingUnitInstance.ClusterInfo + "] on GSC [" +
          processingUnitInstance.getGridServiceContainer().Uid + "]");
        if (processingUnitInstance.EmbeddedSpaces)
        {
            Console.WriteLine("      -> Embedded Space [" + processingUnitInstance.SpaceInstance.Uid + "]");
        }
        foreach (IServiceDetails details in processingUnitInstance)
        {
            Console.WriteLine("      -> Service " + details);
        }
    }
}
```

{{% /tab %}}

{{%tab "  Space "%}}


```csharp
ServiceGridAdminBuilder adminBuilder = new ServiceGridAdminBuilder();
adminBuilder.Groups.Add("myGroup");
IServiceGridAdmin admin = adminBuilder.CreateAdmin();
...
// wait till things get discovered (you can also use specific WaitFor)

foreach (ISpace space in admin.Spaces)
{
    Console.WriteLine("Space [" + space.Uid + "] numberOfInstances [" +
     space.getNumberOfInstances() + "] numberOfbackups [" +
     space.getNumberOfBackups() + "]");
    Console.WriteLine("  Stats: Write [" + space.Statistics.WriteCount + "/" +
    space.Statistics.WritePerSecond + "]");
    foreach (ISpaceInstance spaceInstance in space)
    {
        Console.WriteLine("   -> INSTANCE [" + spaceInstance.Uid + "] instanceId [" + spaceInstance.InstanceId +
        "] backupId [" + spaceInstance.BackupId + "] Mode [" + spaceInstance.Mode + "]");
        Console.WriteLine("         -> Host: " + spaceInstance.Machine.HostAddress);
        Console.WriteLine("         -> Stats: Write [" + spaceInstance.Statistics.WriteCount + "/" +
        spaceInstance.Statistics.WritePerSecond + "]");
    }
    foreach (ISpacePartition spacePartition in space.Partitions)
    {
        Console.WriteLine("   -> Partition [" + spacePartition.PartitiondId + "]");
        for (ISpaceInstance spaceInstance in spacePartition)
        {
            Console.WriteLine("      -> INSTANCE [" + spaceInstance.Uid + "]");
        }
    }
}
```

{{% /tab %}}

{{%tab "  Virtual Machine "%}}


```csharp
ServiceGridAdminBuilder adminBuilder = new ServiceGridAdminBuilder();
adminBuilder.Groups.Add("myGroup");
IServiceGridAdmin admin = adminBuilder.CreateAdmin();
...
// wait till things get discovered (you can also use specific WaitFor)

Console.WriteLine("VM TOTAL STATS: Heap Committed [" +
	admin.VirtualMachines.Statistics.MemoryHeapCommittedInGB +"GB]");
	Console.WriteLine("VM TOTAL STATS: GC PERC [" +
	admin.VirtualMachines.Statistics.GcCollectionPerc + "], Heap Used [" +
 	admin.VirtualMachines.Statistics.MemoryHeapPerc + "%]");
foreach (IVirtualMachine virtualMachine in admin.VirtualMachines)
{
    Console.WriteLine("VM [" + virtualMachine.Uid + "] " +
			"Host [" + virtualMachine.Machine.HostAddress + "] " +
            "GC Perc [" + virtualMachine.Statistics.GcCollectionPerc + "], " +
            "Heap Usage [" + virtualMachine.Statistics.MemoryHeapPerc + "%]");

    foreach (IProcessingUnitInstance processingUnitInstance in virtualMachine.ProcessingUnitInstances)
    {
        Console.WriteLine("   -> PU [" + processingUnitInstance.Uid + "]");
    }
    foreach (ISpaceInstance spaceInstance in virtualMachine.SpaceInstances)
    {
        Console.WriteLine("   -> Space [" + spaceInstance.Uid + "]");
    }
}
```

{{% /tab %}}

{{%tab "  Machine "%}}


```csharp
ServiceGridAdminBuilder adminBuilder = new ServiceGridAdminBuilder();
adminBuilder.Groups.Add("myGroup");
IServiceGridAdmin admin = adminBuilder.CreateAdmin();
...
// wait till things get discovered (you can also use specific WaitFor)

foreach (IMachine machine in admin.Machines)
{
    Console.WriteLine("Machine [" + machine.Uid + "], " +
            "TotalPhysicalMem [" + machine.OperatingSystem.Details.TotalPhysicalMemorySizeInGB + "GB], " +
            "FreePhysicalMem [" + machine.OperatingSystem.Statistics.FreePhysicalMemorySizeInGB + "GB]]");
    foreach (ISpaceInstance spaceInstance in machine.SpaceInstances)
    {
        Console.WriteLine("   -> Space [" + spaceInstance.Uid + "]");
    }
    for (IProcessingUnitInstance processingUnitInstance in machine.ProcessingUnitInstances)
    {
        Console.WriteLine("   -> PU [" + processingUnitInstance.Uid + "]");
    }
}
```

{{% /tab %}}

{{% /tabs %}}

{{% refer %}}
Refer to [Scaling Agent Example](./scaling-agent-example.html) for a fully running example that comes with the product.
{{% /refer %}}

# Admin Construction

Use `ServiceGridAdminBuilder` to create `IServiceGridAdmin` instances. After working with the `IServiceGridAdmin` is done, its `IServiceGridAdmin.Dispose()` method should be called.

The Admin API discovers all the advertised services from the Lookup Services. In order to define which lookup groups are used, the `ServiceGridAdminBuilder.Groups.Add` can be used. The lookup locators can also be used for a non-multicast-enabled environment, using `ServiceGridAdminBuilder.Locators.Add`. If the services started are secured, the username and password can also be set in the Admin API.

# Discovery Process

When the `IServiceGridAdmin` is created, it starts to receive discovery events of all the advertised services/components within its lookup groups/lookup locators. The events occur asynchronously, and the data model within the Admin API gets initialized in the background with services coming and going.

This means that just creating the `IServiceGridAdmin`, and calling a specific get property for a data structure, might not return what is currently deployed, and you should wait until the structures are filled. Some components have a `WaitFor` method that allows them to wait for a specific number of services to be up. When navigating the data model, the Admin API provides the most up-to-date state of the system that it is monitoring.

# Domain Model

The Service Grid Admin Domain Model has representation to the main actors at different XAP levels. They include:


|Name|Description|Main Operations|
|:---|:----------|:--------------|
|GridServiceAgent|Process manager that manages Service Grid processes such as GSM, GSC and LUS. More info [here](../overview/the-runtime-environment.html#gsa).| - Allows you to list all the currently managed processes.<br/>- Start processes (GSM, GSC, LUS).|
|GridServiceAgents|Holds all the currently discovered Grid Service Agents.| - Get all the currently discovered Grid Service Agents.<br/>- Wait for X number of Grid Service Agents to be up.<br/>- Register for Grid Service Agent addition (discovery) and removal events.|
|GridServiceManager|Manages Processing Unit deployments and Grid Service Containers. More info [here](../overview/the-runtime-environment.html#gsm).| - Deploy Processing Units.<br/>- Deploy pure Space Processing Units.<br/>- Get the Grid Service Agent managing it.<br/>- Restart itself (if managed by a Grid Service Agent).<br/>- Kill itself (if managed by a Grid Service Agent).|
|GridServiceManagers|Holds all the currently discovered Grid Service Managers.| - Deploy Processing Units on a random Grid Service Manager.<br/>- Deploy pure Space Processing Units on a random Grid Service Manager.<br/>- Get all the currently discovered Grid Service Managers.<br/>- Wait for X number of Grid Service Managers to be up.<br/>- Register for Grid Service Manager addition (discovery) and removal events.|
|GridServiceContainer|Container hosting Processing Unit Instances deployed from the GSM. More info [here](../overview/the-runtime-environment.html#gsc).| - List currently running Processing Unit instances.<br/>- Register for Processing Unit Instance additions and removals events.|
|GridServiceContainers|Holds all the currently discovered Grid Service Containers.|* Get all the currently discovered Grid Service Containers.<br/>- Wait for X number of Grid Service Containers to be up.<br/>- Register for Grid Service Container addition (discovery) and removal events.|
|LookupService|Registry of services (GSM, GSC, Space instances, Processing Unit instances) that can be looked up using it. More info [here](../overview/the-runtime-environment.html#lus).|Get the Lookup Groups and Locator it was started with.|
|LookupServices|Holds all the currently discovered Lookup Services.| - Get all the currently discovered Lookup Services.<br/>- Wait for X number of Lookup Services to be up.<br/>- Register for Lookup Service addition (discovery) and removal events.|
|ProcessingUnit|Deployable Processing Unit running one or more Processing Unit instances. Managed by the Grid Service Manager.| - Undeploy the Processing Unit.<br/>- Increase the number of Processing Unit instances (if allowed).<br/>- Decrease the number of Processing Unit instances (if allowed).<br/>- Get the deployment status of the Processing Unit.<br/>- Register for deployment status change events.<br/>- Get the managing Grid Service Manager.<br/>- Register for managing Grid Service Manager change events.<br/>- Get the list of backup Grid Service Managers.<br/>- Register for backup Grid Service Manager change events.<br/>- List all the currently running Processing Unit instances.<br/>- Register for Processing Unit instance addition and removal events.<br/>- Wait for X number of Processing Unit instances to be up.<br/>- Get an embedded Space that the Processing Unit has.<br/>- Wait for an embedded Space to be correlated (discovered) with the Processing Unit.<br/>- Register for Space correlation events.|
|ProcessingUnitInstance|Actual instance of a Processing Unit running within a Grid Service Container.|- Destroy itself (if SLA is breached, it is instantiated again).<br/>- Decrease itself (and destroy itself in the process). It does not attempt to create itself again.<br/>- Relocate itself to a different Grid Service Container.<br/>- List all its inner services (such as event containers).<br/>- Get the embedded Space instance running within it (if there is one).<br/>- Get the JEE container details if it is a web Processing Unit.|
|ProcessingUnits|Holds all the currently deployed Processing Units.| - Get all the currently deployed Processing Units.<br/>- Wait for (and return) a Processing Unit by a specific name.<br/>- Register for Processing Unit deployments and undeployment events.<br/>- Register for all Processing Unit instance addition and removal events (across all Processing Units).<br/>- Register for managing Grid Service Manager change events on all Processing Units.<br/>- Register for backup Grid Service Manager change events on all Processing Units.|
|Space|Composed of one or more Space instances, to form a Space topology (cluster).| - Get all the currently running Space instances that are part of the Space.<br/>- Wait for X number of Space instances to be up.<br/>- Register for Space instance addition and removal events.<br/>- Register for Space instance change mode events (for all Space instances that are part of the Space).<br/>- Register for Space instance replication status change events (for all Space instances that are part of the Space).<br/>- Get aggregated Space statistics.<br/>- Register for aggregated Space statistics events (if monitoring).<br/>- Get a clustered [Space](./the-gigaspace-interface-overview.html) to perform Space operations.|
|SpaceInstance|Actual instance of a Space that is part of a topology (cluster), usually running within a Processing Unit instance.| - Get its Space mode (primary or backup).<br/>- Register for Space mode change events.<br/>- Get its replication targets.<br/>- Register for replication status change events.<br/>- Get a direct [Space](./the-gigaspace-interface-overview.html) to perform Space operations.<br/>- Get Space instance statistics.<br/>- Register for Space instance statistics (if monitoring).|
|Spaces|Holds all the currently running Spaces.| - Get all the currently running Spaces.<br/>- Wait for (and return) a specific Space by name.<br/>- Register for Space addition and removal events.<br/>- Register for Space instance addition and removal events (across all Spaces).<br/>- Register for Space instance mode change events (across all Space instances).<br/>- Register for Space instance replication change events (across all Space instances).<br/>- Register for aggregated Space level statistics change events (across all Spaces, if monitoring).<br/>- Register for Space instance statistics change events (across all Space instances, if monitoring).|
|VirtualMachine|Virtual Machine (JVM) that is currently running at least one XAP component/service.| - Get the Grid Service Agent (if it exists).<br/>- Get the Grid Service Manager (if it exists).<br/>- Get the Grid Service Container (if it exists).<br/>- Get all the Processing Unit instances that are running within the Virtual Machine.<br/>- Register for Processing Unit instance addition and removal events.<br/>- Get all the Space instances that are running within the Virtual Machine.<br/>- Register for Space instance addition and removal events.<br/>- Get the details of the Virtual Machine (min/max memory, and so on).<br/>- Get the statistics of the Virtual Machine (heap used, and so on).<br/>- Register for statistics change events (if monitoring).|
|VirtualMachines|Holds all the currently discovered Virtual Machines.| - Get all the currently discovered Virtual Machines.<br/>- Register for Virtual Machines addition and removal events.<br/>- Get aggregated Virtual Machine details.<br/>- Get aggregated Virtual Machine statistics.<br/>- Register for aggregated Virtual Machine statistics events (if monitoring).<br/>- Register for Virtual Machine level statistics change events (across all Virtual Machines, if monitoring).|
|Machine|Actual Machine (identified by its host address) running one or more XAP components/services in one or more Virtual Machines. Associated with one operating system.| - Get all the Grid Service Agents running on the machine.<br/>- Get all the Grid Service Containers running on the machine.<br/>- Get all the Grid Service Managers running on the machine.<br/>- Get all the Virtual Machines running on the machine.<br/>- Get all the Processing Unit instances running on the machine.<br/>- Register for Processing Unit instance addition and removal events from the machine.<br/>- Get all the Space instances running on themMachine.<br/>- Register for Space instances addition and removal events from the machine.<br/>- Get the operating system the machine is running on.|
|Machines|Holds all the currently discovered machines.| - Get all the currently running machines.<br/>- Wait for X number of machines to be up.<br/>- Register for machine addition and removal events.|
|OperatingSystem|The operating system that the XAP components/services are running on. Associated with one machine.| - Get the details of the operating system.<br/>- Get the operating system statistics.<br/>- Register for statistics change events (if monitoring).|
|OperatingSystems|Holds all the currently discovered operating systems.| - Get all the current operating systems.<br/>- Get the aggregated operating system details.<br/>- Get the aggregated operating system statistics.<br/>- Register for aggregated operating system statistics change events (if monitoring).<br/>- Register for operating system level statistics change events (across all operating systems, if monitoring).|
|Transport|Communication layer each XAP component/service uses.| - Get the transport details (host, port).<br/>- Get the transport statistics.<br/>- Register for transport statistics change events (if monitoring).|
|Transports|Holds all the currently discovered transports.| - Get all the current transports.<br/>- Get the aggregated transport details.<br/>- Get the aggregated transport statistics.<br/>- Register for aggregated transport statistics change events (if monitoring).<br/>- Register for transport level statistics change events (across all transports, if monitoring).|

# Accessing the Domain Model

There are two ways the Service Grid Admin API can be used to access information the Admin API can provide:

- Call specific properties for the data, and enumerate over them (as shown in the example at the top of the page).
- Register for specific events using the Service Grid Admin API. Events are handled by different components of the Admin API in similar manner. We will take one of them and use it as a reference example.

If we want to register, for example, for Grid Service Container additions, we can use the following code (removing the event listener is not shown here for clarity):


```csharp
admin.GridServiceContainers.GridServiceContainerAdded += HandleGridServiceContainerAdded;

private void HandleGridServiceContainerAdded(object sender, GridServiceContainerEventArgs e)
{
    IGridServiceContainer gsc = e.GridServiceContainer;
    // do something here
}
```

Removals are done in a similar manner:


```csharp
admin.GridServiceContainers.GridServiceContainerRemoved += HandleGridServiceContainerRemoved;

void HandleGridServiceContainerRemoved(object sender, GridServiceContainerEventArgs e)
{
    IGridServiceContainer gsc = e.GridServiceContainer;
    // do something here
}
```

All other data structures use a similar API to register for events. Some might have specific events that go beyond just additions and removals, but they still use the same model. For example, the following example shows how to register for Space mode change events across all currently running Space topologies and Space instances:


```csharp
admin.Spaces.SpaceModeChanged += HandleSpaceModeChanged;

void Spaces_SpaceModeChanged(object sender, SpaceModeChangedEventArgs e)
{
    Console.WriteLine("Space [" + e.SpaceInstance.Space.Name + "] " +
				"Instance [" + e.SpaceInstance.InstanceId + "/" + e.SpaceInstance.BackupId + "] " +
				"changed mode from [" + e.PreviousMode + "] to [" + e.NewMode + "]");

}
```

Of course, we can register the same event listener on a specific `ISpace` topology, or event on a specific `ISpaceInstance`.

# Details and Statistics

- Some components in the Admin API can provide statistics. For example, an `ISpaceInstance` can provide statistics on how many times the read API was called on it. Statistics change over time, and in order to get them, either the property for the statistics can be used, or a statistics listener can be registered for statistics change events.
- Details of a specific component provide information that does not change over time, but can be used to provide more information regarding the component, or to compute statistics. For example, the `IVirtualMachine` provides in its details, the minimum and maximum heap memory size, from which the `IVirtualMachine` statistics provide the currently used heap memory size. The detailed information is used to provide the percentage used in the Virtual Machine statistics.
- The Admin API also provides aggregated details and statistics. For example, `ISpace` provides `ISpaceStatistics`, allowing you to get the aggregated statistics of all the different Space instances that belong to it.
- Each component in the Admin API that can provide statistics (either direct or aggregated statistics) implements the `IStatisticsMonitor` interface. The statistics monitor allows you to start or stop monitoring statistics. Monitoring for statistics is required to register for statistics change events. The interval at which statistics are polled is controlled using the statistics interval.
- The statistics interval is an important event when the Admin API is not actively polling for statistics. Each call to a property of statistics only performs a remote call to the component, if the last statistics fetch happened **before** the statistics interval. This behavior means that users of the Admin API do not have to worry about "hammering" different components for statistics, because the Admin API makes sure that statistics calls are cached internally for the statistics interval period.
- An `ISpaceInstance` implements the `IStatisticsMonitor` interface. Calling `StartMonitor` and `StopMonitor` on it causes monitoring of statistics to be enabled or disabled on it.
- `ISpace` also implements the `IStatisticsMonitor` interface. Calling `StartMonitor` on it causes it to start monitoring all its `ISpaceInstance`s. If an `ISpaceInstance` is discovered after the the call to `startMonitor` occurs, it starts monitoring itself automatically. This means that if the `SpaceInstanceStatistics` event was registered on the `ISpace`, it automatically starts to get Space instance statistics change events for the newly discovered `ISpaceInstance`.
- `ISpaces` also implements the `IStatisticsMonitor` interface. Calling `StartMonitor` on it causes it to start monitoring all the `ISpace`s it has (and as a result, also `ISpaceInstance`s, - see the paragraph above). A `SpaceInstanceStatistics` can also be registered on the `ISpaces` level.
- The above Space level statistics behavior works in much the same way with other components. For example, `IVirutalMachine` and `IVirtualMachines`, `ITransport` and `ITransports`, `IOperatingSystem` and `OperatingSystems`.
- The `IServiceGridAdmin` interface also implements the `IStatisticsMonitor` interface. Calling `StartMonitor` on it causes all holders to start monitoring. These include `ISpaces`, `IVirtualMachines`, `ITransports`, and `IOperatingSystems`.


