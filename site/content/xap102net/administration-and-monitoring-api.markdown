---
type: post102net
title:  Administration API
categories: XAP102NET
parent: admin-modules.html
weight: 100
---


{{%ssummary%}}{{%/ssummary%}}

The Service Grid Admin API provides a way to administer and monitor all of GigaSpaces services and components, using a simple API. The API provides information and the ability to operate on the currently running Service Grid Agents, Service Grid Managers, Service Grid Containers, Lookup Services, Processing Units and Spaces.

Before diving into the Service Grid Admin API, here are some code examples showing how the API can be used to display information on the currently deployed services/components:

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
See a fully running example of a [Scaling Agent](./scaling-agent-example.html) which comes with the product.
{{% /refer %}}

# Admin Construction

Use `ServiceGridAdminBuilder` in order to create `IServiceGridAdmin` instances. Once working with the `IServiceGridAdmin` is done, its `IServiceGridAdmin.Dispose()` method should be called.

The admin discovers all the advertised services from the Lookup Services. In order to define which lookup groups are used, the `ServiceGridAdminBuilder.Groups.Add` can be used. The lookup locators can also be used for a non-multicast-enabled environment, using `ServiceGridAdminBuilder.Locators.Add`. If the services started are secured, the username and password can be set on the admin as well.

# Discovery Process

Once the `IServiceGridAdmin` is created, it starts to receive discovery events of all the advertised services/components within its lookup groups/lookup locators. Note that the events occur asynchronously, and the data model within the admin gets initialized in the background with services coming and going.

This means that just creating the `IServiceGridAdmin`, and calling a specific get property for a data structure, might not return what is currently deployed, and one should wait until the structures are filled. Some components have a `WaitFor` method that allows them to wait for a specific number of services to be up. When navigating the data model, the admin API provides its most up-to-date state of the system that it is monitoring.

# Domain Model

The Service Grid Admin Domain Model has representation to the main actors at different GigaSpaces levels. They include:


|Name|Description|Main Operations|
|:---|:----------|:--------------|
|GridServiceAgent|A process manager that manages Service Grid processes such as GSM, GSC and LUS. More info [here](/product_overview/service-grid.html#gsa).|* Allows you to list all the currently managed processes.<br/>- Start processes (GSM, GSC, LUS).|
|GridServiceAgents|Holds all the currently discovered Grid Service Agents.|* Get all the currently discovered Grid Service Agents.<br/>- Wait for X number of Grid Service Agents to be up.<br/>- Register for Grid Service Agent addition (discovery) and removals events.|
|GridServiceManager|Managing Processing Unit deployments and Grid Service Containers. More info [here](/product_overview/service-grid.html#gsm).|* Deploy Processing Units.<br/>- Deploy pure Space Processing Units.<br/>- Get the Grid Service Agent Managing it.<br/>- Restart itself (if managed by a Grid Service Agent).<br/>- Kill itself (if managed by a Grid Service Agent).|
|GridServiceManagers|Holds all the currently discovered Grid Service Managers.|* Deploy Processing Units on a random Grid Service Manager.<br/>- Deploy pure Space Processing Units on a random Grid Service Manager.<br/>- Get all the currently discovered Grid Service Managers.<br/>- Wait for X number of Grid Service Managers to be up.<br/>- Register for Grid Service Manager addition (discovery) and removals events.|
|GridServiceContainer|Container hosting Processing Unit Instances deployed from the GSM. More info [here](/product_overview/service-grid.html#gsc).|* List currently running Processing Units Instances.<br/>- Register for Processing Unit Instance additions and removals events.|
|GridServiceContainers|Holds all the currently discovered Grid Service Containers.|* Get all the currently discovered Grid Service Containers.<br/>- Wait for X number of Grid Service Containers to be up.<br/>- Register for Grid Service Container addition (discovery) and removals events.|
|LookupService|A registry of services (GSM, GSC, Space Instances, Processing Unit Instances) that can be lookup up using it. More info [here](/product_overview/service-grid.html#lus).|* Get the Lookup Groups and Locator it was started with.|
|LookupServices|Holds all the currently discovered Lookup Services.|* Get all the currently discovered Lookup Services.<br/>- Wait for X number of Lookup Services to be up.<br/>- Register for Lookup Service addition (discovery) and removals events.|
|ProcessingUnit|A deployable processing unit running one or more Processing Unit Instances. Managed by the Grid Service Manager.| * Undeploy the Processing Unit.<br/>- Increase the number of Processing Units Instances (if allowed).<br/>- Decrease the number of Processing Unit Instances (if allowed).<br/>- Get the deployment status of the Processing Unit.<br/>- Register for deployment status change events.<br/>- Get the managing Grid Service Manager.<br/>- Register for Managing Grid Service Manager change events.<br/>- Get the list of backup Grid Service Managers.<br/>- Register for backup Grid Service Manager change events.<br/>- List all the currently running Processing Unit Instances.<br/>- Register for Processing Unit Instances additions and removals events.<br/>- Wait for X number of Processing Unit Instances to be up.<br/>- Get an embedded Space that the Processing Unit has.<br/>- Wait for an embedded Space to be correlated (discovered) with the Processing Unit.<br/>- Register for Space correlation events.|
|ProcessingUnitInstance|An actual instance of a Processing Unit running within a Grid Service Container.|* Destroy itself (if SLA is breached, it is instantiated again).<br/>- Decrease itself (and destroy itself in the process). It does not attempt to create it again.<br/>- Relocate itself to a different Grid Service Container.<br/>- List all its inner services (such as event containers).<br/>- Get the embedded Space Instance running within it (if there is one).<br/>- Get the JEE container details if it is a web processing unit.|
|ProcessingUnits|Holds all the currently deployed Processing Units.|* Get all the currently deployed Processing Units.<br/>- Wait for (and return) a Processing Unit by a specific name.<br/>- Register for Processing Unit deployments and undeployment events.<br/>- Register for all Processing Unit Instance addition and removal events (across all Processing Units).<br/>- Register for Managing Grid Service Manager change events on all Processing Units.<br/>- Register for backup Grid Service Manager change events on all Processing Units.|
|Space|Composed of one or more Space Instances, to form a Space topology (cluster).|* Get all the currently running Space Instances that are part of the Space.<br/>- Wait for X number of Space Instances to be up.<br/>- Register for Space Instance additions and removals events.<br/>- Register for Space Instance change mode events (for all Space Instances that are part of the Space).<br/>- Register for Space Instance replication status change events (for all Space Instances that are part of the Space).<br/>- Get aggregated Space statistics.<br/>- Register for aggregated Space statistics events (if monitoring).<br/>- Get a clustered [GigaSpace](./the-gigaspace-interface.html) to perform Space operations.|
|SpaceInstance|An actual instance of a Space that is part of a topology (cluster), usually running within a Processing Unit Instance.|* Get its Space Mode (primary or backup).<br/>- Register for Space Mode change events.<br/>- Get its replication targets.<br/>- Register for replication status change events.<br/>- Get a direct [GigaSpace](./the-gigaspace-interface.html) to perform Space operations.<br/>- Get Space Instance statistics.<br/>- Register for Space Instance statistics (if monitoring).|
|Spaces|Holds all the currently running Spaces.|* Get all the currently running Spaces.<br/>- Wait for (and return) a specific Space by name.<br/>- Register for Space additions and removal events.<br/>- Register for Space Instance additions and removal events (across all Spaces).<br/>- Register for Space Instance Mode change events (across all Space Instances).<br/>- Register for Space Instance replication change events (across all Space Instances).<br/>- Register for aggregated Space level statistics change events (across all Spaces, if monitoring).<br/>- Register for Space Instance statistics change events (across all Space Instances, if monitoring).|
|VirtualMachine|A virtual machine (JVM) that is currently running at least one GigaSpaces component/service.|* Get the Grid Service Agent (if it exists).<br/>- Get the Grid Service Manager (if it exists).<br/>- Get the Grid Service Container (if it exists).<br/>- Get all the Processing Unit Instances that are running within the Virtual Machine.<br/>- Register for Processing Unit Instance additions and removals events.<br/>- Get all the Space Instances that are running within the Virtual Machine.<br/>- Register for Space Instance additions and removals events.<br/>- Get the details of the Virtual Machine (min/max memory, and so on).<br/>- Get the statistics of the Virtual Machine (heap used, and so on).<br/>- Register for statistics change events (if monitoring).|
|VirtualMachines|Holds all the currently discovered Virtual Machines.|* Get all the currently discovered Virtual Machines.<br/>- Register for Virtual Machines additions and removals events.<br/>- Get aggregated Virtual Machines details.<br/>- Get aggregated Virtual Machines statistics.<br/>- Register for aggregated Virtual Machines statistics events (if monitoring).<br/>- Register for Virtual Machine level statistics change events (across all Virtual Machines, if monitoring).|
|Machine|An actual Machine (identified by its host address) running one or more GigaSpaces components/services in one or more Virtual Machines. Associated with one Operating System.|* Get all the Grid Service Agents running on the Machine.<br/>- Get all the Grid Service Containers running on the Machine.<br/>- Get all the Grid Service Managers running on the Machine.<br/>- Get all the Virtual Machines running on the Machine.<br/>- Get all the Processing Unit Instances running on the Machine.<br/>- Register for Processing Unit Instance additions and removals events from the Machine.<br/>- Get all the Space Instances running on the Machine.<br/>- Register for Space Instances additions and removals events from the Machine.<br/>- Get the Operating System the Machine is running on.|
|Machines|Holds all the currently discovered Machines.|* Get all the currently running Machines.<br/>- Wait for X number of Machines to be up.<br/>- Register for Machine additions and removals events.|
|OperatingSystem|The Operating System GigaSpaces components/services are running on. Associated with one Machine.|* Get the details of the Operating System.<br/>- Get the Operating System statistics.<br/>- Register for statistics change events (if monitoring).|
|OperatingSystems|Holds all the currently discovered Operating Systems.|* Get all the current Operating Systems.<br/>- Get the aggregated Operating Systems details.<br/>- Get the aggregated Operating Systems statistics.<br/>- Register for aggregated Operating Systems statistics change events (if monitoring).<br/>- Register for Operating System level statistics change events (across all Operating Systems, if monitoring).|
|Transport|The communication layer each GigaSpaces component/service uses.|* Get the Transport details (host, port).<br/>- Get the Transport statistics.<br/>- Register for Transport statistics change events (if monitoring).|
|Transports|Holds all the currently discovered Transports.|* Get all the current Transports.<br/>- Get the aggregated Transports details.<br/>- Get the aggregated Transports statistics.<br/>- Register for aggregated Transports statistics change events (if monitoring).<br/>- Register for Transport level statistics change events (across all Transports, if monitoring).|

# Accessing the Domain Model

There are two ways the Service Grid Admin API can be used to access information the Admin API can provide:

- Call specific properties for the data, and enumerate over them (as shown in the example at the top of the page).
- Register for specific events using the Service Grid Admin API. Events are handled by different components of the Admin API in similar manner. We will take one of them and use it as a reference example.

If we want to register, for example, for Grid Service Container additions, we can use the following code (note, removing the event listener is not shown here for clarity):


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

All other data structures use a similar API to register for events. Some might have specific events that go beyond just additions and removals, but they still use the same model. For example, here is how we can register for Space Mode change events across all currently running Space topologies and Space Instances:


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
- The Admin API also provides aggregated details and statistics. For example, `ISpace` provides `ISpaceStatistics`, allowing you to get the aggregated statistics of all the different Space Instances that belong to it.
- Each component in the Admin API that can provide statistics (either direct or aggregated statistics) implements the `IStatisticsMonitor` interface. The statistics monitor allows you to start or stop monitoring statistics. Monitoring for statistics is required if one wishes to register for statistics change events. The interval at which statistics are polled, is controlled using the statistics interval.
- The statistics interval is an important event when the Admin API is not actively polling for statistics. Each call to a property of statistics only performs a remote call to the component, if the last statistics fetch happened **before** the statistics interval. This behavior means that users of the Admin API do not have to worry about "hammering" different components for statistics, since the Admin makes sure that statistics calls are cached internally for the statistics interval period.
- An `ISpaceInstance` implements the `IStatisticsMonitor` interface. Calling `StartMonitor` and `StopMonitor` on it, causes monitoring of statistics to be enabled or disabled on it.
- `ISpace` also implements the `IStatisticsMonitor` interface. Calling `StartMonitor` on it, causes it to start monitoring all its `ISpaceInstance` s. If an `ISpaceInstance` is discovered after the the call to `startMonitor` occurred, it starts monitoring itself automatically. This means that if the  `SpaceInstanceStatistics` event was registered on the `ISpace`, it automatically starts to get Space Instance statistics change events for the newly discovered `ISpaceInstance`.
- `ISpaces` also implements the `IStatisticsMonitor` interface. Calling `StartMonitor` on it, causes it to start monitoring all the `ISpace` s it has (and as a result, also `ISpaceInstance` s, - see the paragraph above). A `SpaceInstanceStatistics` can also be registered on the `ISpaces` level as well.
- The above Space level statistics behavior works in much the same way with other components. For example, `IVirutalMachine` and `IVirtualMachines`, `ITransport` and `ITransports`, `IOperatingSystem` and `OperatingSystems`.
- The `IServiceGridAdmin` interface also implements the `IStatisticsMonitor` interface. Calling `StartMonitor` on it, causes all holders to start monitoring. These include: `ISpaces`, `IVirtualMachines`, `ITransports`, and `IOperatingSystems`.
