---
type: post97
title:  Detailed PU Container
categories: XAP97NET
parent: processing-units.html
weight: 300
---



{{% ssummary %}}  {{% /ssummary %}}

{{% info %}}
This page covers additional details about the BasicProcessingUnitContainer which are not covered in [Basic Processing Unit Container](./basic-processing-unit-container.html) page, the previous page should be read before this one.
{{% /info %}}

# Configuring The Container Automatic Scanning

By default, the container will look for [basic processing unit components](#basiccomponents), [remote service](#services) and [event listener container](#eventcontainers) and instantiate and manage these components if found. This behavior can be enabled or disabled by configuring the container in the following manner:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP" type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
    <ProcessingUnitContainer Type="GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer.BasicProcessingUnitContainer, GigaSpaces.Core"/>
      <BasicContainer ScanRemotingServices="false" ScanBasicComponents="false" ScanEventContainer="false">
      </BasicContainer>
  </GigaSpaces.XAP>
</configuration>
```

By default, when given an assembly name to scan for components, the entire assembly will be scanned. It is possible to specify a certain namespace inside an assembly that should be scanned for components instead of the entire assembly, this can be configured as follows:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP" type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
    <ProcessingUnitContainer Type="GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer.BasicProcessingUnitContainer, GigaSpaces.Core"/>
      <BasicContainer>
        <ScanAssemblies>
          <add AssemblyName="MyAssembly", NameSpace="MyNameSpace1"/>
          <add AssemblyName="MyAssembly", NameSpace="MyNameSpace2"/>
        </ScanAssemblies>
      </BasicContainer>
  </GigaSpaces.XAP>
</configuration>
```

# Configuring Managed Space Proxies

The space proxies which are created and managed by the container can be configured regarding their cluster state.
They can be configured whether to be clustered proxies or a proxy to the direct cluster member. Moreover, they can be configured whether they should be created as part of a cluster by being aware to the cluster info the container received at deploy time, or being created as a single space. Both of these configurations are only relevant for space proxies which are embedded and not remote.


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP" type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
    <ProcessingUnitContainer Type="GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer.BasicProcessingUnitContainer, GigaSpaces.Core"/>
      <BasicContainer>
        <SpaceProxies>
          <add Name="MySpace" Url="/./mySpace" ClusterInfoAware="false"/>
          <add Name="MyClusteredSpace" Url="/./myClusteredProxy" Mode="Clustered"/>
        </SpaceProxies>
      </BasicContainer>
  </GigaSpaces.XAP>
</configuration>
```

This configuration file will create a container with two embedded spaces, one will not be aware to the cluster info which the container received, and as a result will be a single embedded space not part of any cluster.
The second proxy will be embedded and part of the cluster which was specified by the cluster info, moreover, the proxy that is kept in the container under MyClusteredSpace name will be a proxy to the entire cluster and not just the direct cluster member that this container created.

The default values for these properties are `Direct` for `Mode` and `true` for `ClusterInfoAware`.

In order to have a space proxy with custom property the following configuration block should be used:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP" type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
    <ProcessingUnitContainer Type="GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer.BasicProcessingUnitContainer, GigaSpaces.Core"/>
      <BasicContainer>
        <SpaceProxies>
          <add Name="MySpaceWithCustom" Url="/./mySpaceWithCustom">
            <Properties>
              <add Name="space-config.engine.cache_policy" Value="0"/>
              <add Name="space-config.engine.cache_size" Value="100"/>
            </Properties>
          </add>
        </SpaceProxies>
      </BasicContainer>
  </GigaSpaces.XAP>
</configuration>
```

This will create an embedded space with the additional provided custom properties.

## Controliing the Mmory Manager

The following will control the memory manager settings:

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
<configSections>
<section name="GigaSpaces.XAP" type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
</configSections>
	<GigaSpaces.XAP>
		<ProcessingUnitContainer Type="GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer.BasicProcessingUnitContainer, GigaSpaces.Core">
			<BasicContainer>
				<SpaceProxies>
					<add Name="MySpaceWithCustom" Url="/./mySpaceWithCustom">
						<Properties>
							<add Name="space-config.engine.memory_usage.high_watermark_percentage" Value="99"/>
							<add Name="space-config.engine.memory_usage.write_only_block_percentage" Value="98"/>
							<add Name="space-config.engine.memory_usage.write_only_check_percentage" Value="97"/>
							<add Name="space-config.engine.memory_usage.low_watermark_percentage" Value="96"/>
						</Properties>
					</add>        
				</SpaceProxies>
			</BasicContainer>
		</ProcessingUnitContainer>
	</GigaSpaces.XAP>
</configuration>
```


You should use the above settings with large heap size (above 10GB).

# Basic Container Initialization Events

The container exposes some events that can be used to be notified at the different stages of the container initialization.
**`ContainerInitializing`** - Triggered when the container it initializing.
**`ContainerInitialized`** - Triggered once the container finished its initialization process.

Here's a simple example of using this events in a `BasicProcessingUnitComponent` which acts as a feeder


```java
[BasicProcessingUnitComponent(Name="Feeder")]
public class Feeder
{
  private ISpaceProxy _proxy;
  private Thread _feedingThread;
  private volatile bool _stopped = false;

  [ContainerInitializing]
  public void Initialize()
  {
    [..]
    Console.WriteLine("Initialized");
  }

  [ContainerInitialized]
  public void StartFeeding(BasicProcessingUnitContainer container)
  {
    _proxy = container.GetSpaceProxy("MySpace");
    [..]
    _feedingThread = new Thread(Feed);
    _feedingThread.Start();
    Console.WriteLine("Feeder started");

  }

  public void Feed()
  {
    while(!_stopped)
    [..]
  }

  public void Dispose()
  {
    _stopped = true;
    _feedingThread.Join();
    Console.WriteLine("Feeder Disposed");
  }
}
```

{{% note %}}
The method which has one of the attributes \[ContainerInitialized\] or \[ContainerInitializing\] can have zero arguments or one argument which will be injected with the managing container
{{%/note%}}

# Creating a Space Proxy Programmatically

In same cases, the `SpaceProxies` config element does not suffice in order to construct a space proxy. For instance if you want to start a space with an external data source, or some other custom properties. In these cases, the `CreateSpaceProxy` method should be used in one of the container initialization events. If this proxy is used by other components in the processing unit, it should be put in the `ContainerInitializing` event in order for it to be available for the other components when they are created, otherwise you can put it in the `ContainerInitialized` just as well. Here's a simple example of how to use the `CreateSpaceProxy`:


```java
[ContainerInitializing]
public void Initialize(BasicProcessingUnitContainer container)
{
  SpaceConfig config = new SpaceConfig();
  //Add custom properties
  spaceConfig.CustomProperties = new Dictionary<string, string>();
  spaceConfig.CustomProperties.Add("space-config.engine.max_threads", "128");
  //Create the proxy and let the container manage it
  ISpaceProxy spaceProxy = container.CreateSpaceProxy("MySpace", "/./mySpace?groups=$(DefaultLookupGroup)", spaceConfig);
}
```

# Basic Container Programatic API

The container exposes API for creating managed space proxies and receiving its managed components.

Here are a few samples of how to obtain managed components from the container:

**Create a managed space proxy**


```java
BasicProcessingUnitContainer container = //Obtain a reference to the container

ISpaceProxy spaceProxy = container.CreateSpaceProxy("ColocatedSpace", "/./mySpace");
```

**Get a managed space proxy**


```java
BasicProcessingUnitContainer container = //Obtain a reference to the container

ISpaceProxy spaceProxy = container.GetSpaceProxy("ColocatedSpace");
```

**Get a managed basic processing unit component**


```java
BasicProcessingUnitContainer container = //Obtain a reference to the container

Object component = container.GetProcessingUnitComponent("Feeder");
```

**Get a managed event listener container**


```java
BasicProcessingUnitContainer container = //Obtain a reference to the container

IEventListenerContainer<Data> eventListenerContainer = container.GetEventListenerContainer<Data>("DataProcessor");
```

{{% note %}}
For full API please read the API documentation file.
{{%/note%}}

# Security

When the processing unit is deployed with injected security properties (Using the Management Center or the [Service Grid admin api](./administration-and-monitoring-api.html)), the basic processing unit container will automatically attach this security context to all the proxies that it manages. Both for proxies that are created automatically from the configuration and proxies that are created programmatically using the container `CreateSpaceProxy` method.
