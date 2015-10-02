---
type: post97net
title:  PU Container
categories: XAP97NET
parent: processing-units.html
weight: 100
---



{{% ssummary %}}{{% /ssummary %}}



The .NET Processing Unit Container is based on the [OpenSpaces Processing Unit](./processing-units.html), and allows you to write a .NET component that can be managed by the service grid.

The Processing Unit Container is aware of the [cluster info](#ClusterInfo). This allows you to write your code decoupled from cluster topologies considerations.


{{% tip %}}
 Most users prefer to work with the [Basic Processing Unit Container](./basic-processing-unit-container.html), since it simplifies many common tasks associated with implementing a processing unit from scratch.
However, if you are not familiar with processing unit and event container concepts, the basic processing unit container may be confusing. We recommend you read and experiment with the processing unit container explained below for a while, then continue to read and benefit from the basic processing unit container.
{{% /tip %}}

# AbstractProcessingUnitContainer

The `AbstractProcessingUnitContainer` class implements `IDisposable`, and consists of one additional method and two properties:


```csharp
public abstract class AbstractProcessingUnitContainer
{
    // Cluster information is set into this property at deploy-time.
    ClusterInfo ClusterInfo { get; set; }

    // Properties are set into this properties at deploy-time.
    IDictionary<String, String> Properties { get; set; }

    // Invoked by the Service Grid to initialize the processing unit container.
    virtual void Initialize();

    // Invoked by the Service Grid to terminate the processing unit container.
    virtual void Dispose();
}
```

The Processing Unit Container lifecycle consists only of these two methods: `Initialize` is called when the Processing Unit Container is constructed, and `Dispose` is called when it is removed. Before the initialization, the ClusterInfo and Properties are set with the deploy-time data.

# ClusterInfo

One of the core ideas of the Processing Unit is the determination of the deployment topology in deploy-time. Therefore, when building a Processing Unit, there is almost no need to be aware of the actual cluster topology the Processing Unit is deployed under.

{{% info %}}
There are two aspects that are important to remember when building a Processing Unit, for it to be cluster topology-independent:
{{%/info%}}

- Define a routing index on the domain model written to the space, so the partitioned topology can work.
- When working directly against a cluster member, make sure you don't perform operations against the backup member.

The `ClusterInfo` class holds the following information:


|Name|Description|
|-------------|-------|
| `Schema` | The cluster topology. |
| `NumberOfInstances` |The number of primary instances this cluster holds. |
| `NumberOfBackups` |The number of backups per primary instance.|
| `InstanceId` | 1 to the `NumberOfInstances` value, denoting the primary instance ID of the current Processing Unit instance. |
| `BackupId` | 1 to the `NumberOfBackups` value, denoting the backup ID of the `InstanceId` of the current Processing Unit instance. |
| `RunningNumber` |A running number of the cluster instance. Takes into account different topologies and provides a unique identifier (starting from `0`) of the cluster member. |

{{% note %}}
Defining a `null` value in one of these properties means that they are not set.
{{%/note%}}

# Creating Your Own Processing Unit Container


## Step 1 -- Create the Processing Unit Container

A processing unit container is an extension of the `GigaSpaces.XAP.ProcessingUnit.Containers.AbstractProcesingUnitContainer` class, which is deployed and executed inside the Service Grid. You need to create your own library with your own extension of the `GigaSpaces.XAP.ProcessingUnit.Containers.AbstractProcesingUnitContainer` class.

## Step 2 -- Create a Deployment pu.config File

You need a config file, which is used to deploy the Processing Unit Container. This config file must be named `pu.config` and needs to be placed together with your processing unit container implementation assemblies.

{{% anchor puconfig %}}

## Step 3 -- Configure the Deployment pu.config File

The `pu.config` you've created needs to be edited to point to your Processing Unit Container implementation. The file should contain the following data:

{{% note %}}
It is recommended to use the `pu.config` file located in `<GigaSpaces Root>\Examples\ProcessingUnit\Feeder\Deployment` as a template.
{{%/note%}}


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP" type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <appSettings>
    <add key="[customkey1]" value="[customvalue1]"/>
  </appSettings>
  <GigaSpaces.XAP>
    <ProcessingUnitContainer Type="[Assembly Qualified Name]"/>
  </GigaSpaces.XAP>
</configuration>
```


{{% refer %}}It is possible to create a processing unit of mixed languages, for instance part Java, part .NET. This topic is covered in [Interop Processing Unit](./interop-processing-unit.html) page.{{% /refer %}}

# SLA Definition

In order to define the service layer agreement of your processing unit, an SLA file needs to be created.
That file should be named `sla.xml`, and should be placed in the root directory of the processing unit.

{{% refer %}}Read about SLA in [Service Grid Processing Unit Container](./basic-processing-unit-container.html).{{% /refer %}}


# Deployment

There are several ways to deploy the Processing Unit Container into the Service Grid. Are all detailed extensively in the [.NET Processing Unit Data Example](./dotnet-your-first-xtp-application.html#Deployment) section.

The most straightforward way is to use the GigaSpaces Management Center for deployment.

