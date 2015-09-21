---
type: post97
title:  Base PU Container
categories: XAP97NET
parent: processing-units.html
weight: 200
---



{{% ssummary %}}  {{% /ssummary %}}


A [processing unit container](./processing-unit-container.html) is a component implemented by the user and deployed and managed by the service grid. XAP.NET comes with a built-in type implementation of the processing unit container called the `BasicProcessingUnitContainer` which provides basic implementations for common activities, and allows the user to focus on business logic and less with GigaSpaces internals.


# Using The Container

The basic container simplifies the actual implementation of the processing unit by managing on its own GigaSpaces related components which are commonly used when developing application which are deployed into the grid.


# Integrating The Container Into Your Project

In order to use the container as part of the processing unit project, you need a configuration file, which is used to deploy the Processing Unit Container. This config file must be named `pu.config` and needs to be placed together with your processing unit container implementation assemblies.

The `pu.config` file should be as follows:

{{%tabs%}}
{{%tab " XAP 9.7.1 "%}}

```xml
 <configSections>
    <section name="GigaSpaces.XAP" type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
		<ProcessingUnit>
		</ProcessingUnit>
  </GigaSpaces.XAP>
</configuration>
```
{{%/tab%}}
{{%tab " XAP 9.7.0 "%}}

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP"
      type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
    <ProcessingUnitContainer
      Type="GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer.BasicProcessingUnitContainer, GigaSpaces.Core"/>
  </GigaSpaces.XAP>
</configuration>
```
{{%/tab%}}
{{%/tabs%}}

This configuration file specifies that the container that should be deployed is the `BasicProcessingUnitContainer`, in the same manner any other custom container implementation would have been deployed.

# Automatic Space Proxy Creation And Management

The container can create and manage the lifecycle of space proxies, and reduces the need from the user to properly dispose proxies or shutdown embedded spaces when the container is un deployed. A managed space proxy can also be created in the container by configuring it with a configuration file.

The following config file will cause the container to create and manage an embedded space proxy:


{{%tabs%}}
{{%tab " XAP 9.7.1 "%}}

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP" type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
		<ProcessingUnit>
			<SpaceProxies>
				<add Name="mySpace" Url="/./mySpace"/>
			</SpaceProxies>
		</ProcessingUnit>
  </GigaSpaces.XAP>
</configuration>
```
{{%/tab%}}
{{%tab " XAP 9.7.0 "%}}

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP"
     type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
    <ProcessingUnitContainer
     Type="GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer.BasicProcessingUnitContainer, GigaSpaces.Core">
      <BasicContainer>
        <SpaceProxies>
          <add Name="MySpace" Url="/./mySpace"/>
        </SpaceProxies>
      </BasicContainer>
    </ProcessingUnitContainer>
  </GigaSpaces.XAP>
</configuration>
```
{{%/tab%}}
{{%/tabs%}}

{{% anchor basiccomponents %}}

# Basic Processing Unit Components

There can be different user components that are part of the processing unit. Such components can be automatically generated and managed by the container by creating a class, which represent the logical component, and marking it with the \[BasicProcessingUnitComponent\] attribute. Optionally, the component can mark methods with the \[ContainerInitializing\] and \[ContainerInitialized\] which will be called when the managing basic container is initializing and after it is initialized correspondingly. Moreover, it can implement the `IDisposable` interface which will be called once the managing container is disposing upon undeployment.

Here's an example of a basic component which keeps a reference to a space proxy which is managed by the container:


```csharp
[BasicProcessingUnitComponent(Name="MyComponent")]
public class MyComponent : IDisposable
{
  private ISpaceProxy _proxy;

  [ContainerInitialized]
  public void Initialize(BasicProcessingUnitContainer container)
  {
    _proxy = container.GetSpaceProxy("MySpace");
    [..]
    Console.WriteLine("MyComponent initialized");
  }

  public void Dispose()
  {
    Console.WriteLine("MyComponent Disposing");
  }
}
```

{{% note %}}
The method which has one of the attributes \[ContainerInitialized\] or \[ContainerInitializing\] can have zero arguments or one argument which will be injected with the managing container.
{{%/note%}}

The container automatically detects components by scanning all the assembly (dll) files in the processing unit's folder.

{{%tabs%}}
{{%tab " XAP 9.7.1 "%}}

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP" type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
		<ProcessingUnit>
			<SpaceProxies>
				<add Name="mySpace" Url="/./mySpace"/>
			</SpaceProxies>
		</ProcessingUnit>
  </GigaSpaces.XAP>
</configuration>
```
{{%/tab%}}
{{%tab " XAP 9.7.0 "%}}

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP"
     type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
    <ProcessingUnitContainer
     Type="GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer.BasicProcessingUnitContainer, GigaSpaces.Core">
      <BasicContainer>
        <SpaceProxies>
          <add Name="MySpace" Url="/./mySpace"/>
        </SpaceProxies>
      </BasicContainer>
    </ProcessingUnitContainer>
  </GigaSpaces.XAP>
</configuration>
```
{{%/tab%}}
{{%/tabs%}}

{{% note %}}
The assembly name is the actual name and not a file path, the assembly should be part of the processing unit project output directory and be placed beside the `pu.config` file.
{{%/note%}}

{{% anchor services %}}

# Automatic Remote Services Creation And Hosting

One of GigaSpaces grid component capabilities is [remote services](./space-based-remoting.html), which can be hosted in the grid. The basic container automatically detects , creates, hosts and manages such services' life cycle. This is done by marking the remote service with the \[SpaceRemotingService\] attribute.


```java
[SpaceRemotingService]
public class MyService : IService
{
  [..]
}
```

{{% anchor eventcontainers %}}

# Automatic Event Listener Creation And Management

An [event listener container](./event-processing.html) is one of the most commonly used GigaSpaces components as part of a processing unit. Similarly to the other components, such event containers can be automatically detected, created and managed by the basic container. The basic container will automatically detect classes that need to be wrapped with the proper event listener container via the corresponding `EventDriven` attributes (`PollingEventDriven` or `NotifyEventDriven`) that mark them.

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

An event listener container needs a space proxy that will listen for events. If the basic container is managing a single proxy then that proxy will be supplied to the event listener container. If more than one proxy exists, then the proxy name needs to be specified in the configuration file for that specific event listener container.

The following basic container config will start two space proxies and supply the colocated proxy, named MySpace, to the event listener container:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP"
     type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
    <ProcessingUnitContainer
     Type="GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer.BasicProcessingUnitContainer, GigaSpaces.Core">
      <BasicContainer>
        <SpaceProxies>
          <add Name="MySpace" Url="/./mySpace"/>
          <add Name="MyRemoteSpace" Url="jini://*/*/myRemoteSpace"/>
        </SpaceProxies>
        <EventContainers>
          <add Name="MyEventListener" SpaceProxyName="MySpace"/>
        </EventContainers>
      </BasicContainer>
    </ProcessingUnitContainer>
  </GigaSpaces.XAP>
</configuration>
```

{{% anchor lifecycle %}}

# Space Life-Cycle Events

In a topology with backup spaces, it is quite common to have a business logic co-located with an embedded space instance, that should be activated only when the embedded space instance mode is primary. The built-in event listener container work that way; they only start to operate when the co-located embedded space becomes primary. It is quite common to have different custom logic that should be notified upon space mode change events and act accordingly (for instance, start some monitoring process of the co-located space instance). The container will detect automatically methods marked with a space mode changed attribute (\[PostPrimary\], \[BeforePrimary\], \[PostBackup\] and \[BeforeBackup\]) and it will invoke these methods once the space instance mode is changed.

Here's an example of monitoring logic that will start to monitor the embedded space when it becomes primary:


```java
[BasicProcessingUnitComponent(Name="MyComponent")]
public class MyComponent : IDisposable
{
  ...
  [PostPrimary]
  public void StartMonitoring(ISpaceProxy proxy)
  {
    //Start monitoring the proxy state
    ..
  }
}
```

The event listening method can be one of the following formats:


```java
//No parameters method
[PostPrimary]
public void MyEventListener()
```


```java
//Single space proxy parameter
[PostPrimary]
public void MyEventListener(ISpaceProxy spaceProxy)
```


```java

//Two parameter, space proxy and space mode
[PostPrimary]
public void MyEventListener(ISpaceProxy spaceProxy, SpaceMode spaceMode)
```

If there are multiple space proxies in the container, the name of the space needs to be specified to the corresponding space mode changed attribute. For example:


```java
[PostPrimary(SpaceProxyName="MySpace")]
public void MyEventListener(ISpaceProxy spaceProxy)
```

When registering for the \[BeforePrimary\] or \[BeforeBackup\], special care should be taken. The event handling of these listeners will **delay the space instance life cycle completion** for a co-located space instance - i.e., a primary space instance will be blocked from fully becoming a primary space until it completes all the invocations of the \[BeforePrimary\] subscribers. There is no guarantee for receiving a corresponding Before event always prior to a Post event. When the processing unit starts, the event subscription is asynchronous to the space instance active election; in this case it is quite reasonable not to receive the Before events and only to receive the Post events.


{{% refer %}}
For more details about the Basic Processing Unit Container please refer to the [Detailed Basic Processing Unit Container](./detailed-basic-processing-unit-container.html) page.
{{% /refer %}}
