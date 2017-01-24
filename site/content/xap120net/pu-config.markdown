---
type: post120
title:  Configuration
categories: XAP120NET, PRM
parent: the-processing-unit-overview.html
weight: 100
---

{{% ssummary %}}  {{% /ssummary %}}

# Getting Started

Creating a processing unit is simple:

1. In Visual Studio, Create a new `Class Library` project.
2. Add a reference to `GigaSpaces.Core.dll` from the product's `bin` folder.
3. Add an xml file called `pu.config` to the project.
4. Right-click `pu.config`, select **Properties**, and modify the [Copy to Output Directory](http://msdn.microsoft.com/en-us/library/0c6xyb66%28v=vs.100%29.aspx) to **Copy Always** (or **Copy If Newer**).
5. Copy the following configuration into `pu.config`:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="ProcessingUnit" type="GigaSpaces.XAP.Configuration.ProcessingUnitConfigurationSection, GigaSpaces.Core"/>
  </configSections>
  <ProcessingUnit>
    <!-- Processing unit configuration goes here -->
  </ProcessingUnit>
</configuration>
```

{{% note%}}
Upgrading from older versions The processing unit configuration scheme has been simplified in version 10.0, and the previous scheme is still supported for backwards compatibility but it depracated. If you're upgrading from older versions it is recommended to update your PU configuration accordingly.
{{% /note %}}

# Embedded Spaces

The processing unit can be configured to create an embedded space, which will be created when the PU is deployed and destroyed when the PU is undeployed:


```xml
<ProcessingUnit>
  <EmbeddedSpaces>
    <add Name="MySpace"/>
  </EmbeddedSpaces>
</ProcessingUnit>
```

### Cluster Mode

When creating an embedded space, the default proxy is created in **direct** mode, meaning it targets only the collocated cluster member. To target the entire cluster, set the `Mode` tag to **Clustered**:


```xml
<ProcessingUnit>
  <EmbeddedSpaces>
    <add Name="MySpace" Mode="Clustered"/>
  </EmbeddedSpaces>
</ProcessingUnit>
```

### Cluster aware

When a processing unit is deployed, the configured embedded spaces are created using the injected cluster information. To force an embedded space to ignore that cluster info, use the `ClusterInfoAware` tag:


```xml
<ProcessingUnit>
  <EmbeddedSpaces>
    <add Name="MySpace" ClusterInfoAware="false"/>
  </EmbeddedSpaces>
</ProcessingUnit>
```

### Space Properties

When creating a space you can override space properties values using the `Properties` tag:


```xml
<ProcessingUnit>
  <EmbeddedSpaces>
    <add Name="MySpace">
      <Properties>
        <add Name="space-config.engine.cache_policy" Value="0"/>
        <add Name="space-config.engine.cache_size" Value="100"/>
      </Properties>
    </add>
  </EmbeddedSpaces>
</ProcessingUnit>
```

# Space Proxies

The processing unit can be configured to connect to a remote space (hosted by another processing unit, for example):


```xml
<ProcessingUnit>
  <SpaceProxies>
    <add Name="MySpace"/>
  </SpaceProxies>
</ProcessingUnit>
```

{{% anchor eventcontainers %}}

# Event Listeners

An [event listener container](./event-processing.html) is one of the most commonly used GigaSpaces components as part of a processing unit. Similarly to the other components, such event containers can be automatically detected, created and managed by the container - 
it will automatically detect classes decorated with `EventDriven` attributes (`PollingEventDriven` or `NotifyEventDriven`) and create corresponding event containers for them.

{{% refer %}}
See [Polling Container Component](./polling-container.html) and [Notify Container Component](./notify-container.html) for more info regarding event listener containers.
{{% /refer %}}


```java
[PollingEventDriven(Name="MyEventListener")]
public class MyEventListener
{
    [..]
}
```

An event listener container needs a space proxy that will listen for events. If there's a single space proxy configured it will be selected automatically, otherwise event container should be configured with the relevant space name:


```xml
<ProcessingUnit>
  <EmbeddedSpaces>
    <add Name="Foo"/>
  </EmbeddedSpaces>
  <SpaceProxies>
    <add Name="Bar"/>
  </SpaceProxies>
  <EventContainers>
    <add Name="MyEventListener" SpaceProxyName="Foo"/>
  </EventContainers>
</ProcessingUnit>
```

# Security

To access a secured space the `Credentials` tag is used:  

```xml
<ProcessingUnit>
    <EmbeddedSpaces>
        <add Name="EmbeddedSpace">
           <Credentials Username="user" Password="pwd"/> 
        </add>
    </EmbeddedSpaces> 

    <SpacesProxy>
        <add Name="RemoteSpace">
            <Credentials Username="user2" Password="pwd2"/> 
        </add>
    </SpacesProxy>     
</ProcessingUnit>
```


# Assembly Scanning

By default, all the assemblies packaged with the processing unit will be scanned to automatically create processing unit components, event listener containers and remoting services. In some scenarios you may want to change this. For example:

* The application uses many 3rd party assemblies, and scanning all of them slows down the deployment.
* One of the assemblies contains troublesome code and you want to exclude it.
* You're sharing code between multiple processing units and want to control which component is loaded in which processing unit.

You can use the `ScanAssemblies` tag to specify a list of assemblies to be scanned (wildcards may be used). In addition, you may specify a namespace prefix to which indicates only classes with that prefix will be scanned. For example:


```xml
<ProcessingUnit>
  <ScanAssemblies>
    <!-- Scan all assemblies starting with 'Foo.Bar.' -->
    <add AssemblyName="Foo.Bar.*.dll" />
    <!-- Scan all assemblies starting with 'MyCompany.' for classes starting with 'MyCompany.MyProject.'  -->
    <add AssemblyName="MyCompany.*.dll", NameSpace="MyCompany.MyProject."/>
  </ScanAssemblies>
</ProcessingUnit>
```

It is also possible to completely disable assembly scanning:


```xml
<ProcessingUnit>
  <ScanAssemblies Disabled="true"/>
</ProcessingUnit>
```


Finally, it is possible to configure the processing unit to scan for certain type of classes (components, event listeners and remoting). For example, to scan for event containers only:


```xml
<ProcessingUnit ScanRemotingServices="false" ScanBasicComponents="false" ScanEventContainers="true">
    <!-- Can be used in combination with ScanAssemblies -->
</ProcessingUnit>
```
