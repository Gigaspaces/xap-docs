---
type: post120
title:  Components
categories: XAP120NET, PRM
parent: the-processing-unit-overview.html
weight: 200
canonical: auto
---

{{% ssummary %}}  {{% /ssummary %}}

# Overview

Packaging user-defined code in a processing unit is done using components. A **Component** is essentially a class decorated with a `BasicProcessingUnitComponent` attribute:


```java
[BasicProcessingUnitComponent]
public class MyComponent
{
    public MyComponent()
    {
        Console.WriteLine("Hello World");
    }
}
```

When a processing unit is deployed, it scans all the assemblies in its working directory for classes decorated with `BasicProcessingUnitComponent` and instantiates them. In addition, the component instance is managed and tracked by the processing unit. 

# Life cycle

The supported life cycle events for the component are:

* *Initializing* - called during the container initialization process. To intercept this event, create a method decorated with `ContainerInitializing`. 
  * Optional: The method can include an argument of type `BasicProcessingUnitContainer` to intercept the container, which provides additional services.
* *Initialized* - called after the container initialization process completed. To intercept this event, create a method decorated with `ContainerInitialized`.
  * Optional: The method can include an argument of type `BasicProcessingUnitContainer` to intercept the container, which provides additional services.
* *Undeploy* - Called when the processing unit instance is undeployed. To intercept this event, implement the standard `IDisposable` interface, and the `Dispose()` method will be called upon undeployment.

The component name can be used by other components to obtain a reference to it. For example, an additional component `Foo` uses the `ContainerInitialized` method to obtain a reference to `MyComponent`:

# Multiple Components

If more than one component is used, the component can be assigned with a name which can be used to obtain a reference to its runtime instance. For example:


```java
[BasicProcessingUnitComponent(Name="Foo")]
public class Foo
{
    private Bar _bar;
	
    [ContainerInitialized]
    public void Initialize(BasicProcessingUnitContainer container)
    {
        _bar = (Bar)container.GetProcessingUnitComponent("Bar");
    }
}

[BasicProcessingUnitComponent(Name="Bar")]
public class Bar
{
	// ...
}

```

# Space Proxies

If the processing unit contains an embedded space or space proxy definition, you can use the container to obtain a reference to those proxies. For example:


```xml
<ProcessingUnit>
  <EmbeddedSpaces>
    <add Name="MySpace"/>
  </EmbeddedSpaces>
</ProcessingUnit>
```


```java
[BasicProcessingUnitComponent]
public class MyComponent
{
    private ISpaceProxy _mySpace;

    [ContainerInitialized]
    public void Initialize(BasicProcessingUnitContainer container)
    {
        _mySpace = container.GetSpaceProxy("MySpace");
    }
}
```

# Space Life Cycle Events

In a topology with backup spaces, it is quite common to have a business logic co-located with an embedded space instance, that should be activated only when the embedded space instance mode is primary. The built-in event listener container work that way; they only start to operate when the co-located embedded space becomes primary. It is quite common to have different custom logic that should be notified upon space mode change events and act accordingly (for instance, start some monitoring process of the co-located space instance). The container will detect automatically methods marked with a space mode changed attribute (\[PostPrimary\], \[BeforePrimary\], \[PostBackup\] and \[BeforeBackup\]) and it will invoke these methods once the space instance mode is changed.

Here's an example of monitoring logic that will start to monitor the embedded space when it becomes primary:


```java
[BasicProcessingUnitComponent]
public class MyComponent
{
    [PostPrimary]
    public void StartMonitoring(ISpaceProxy proxy)
    {
        //Start monitoring the proxy state
    }
}
```

The event listening method can be one of the following formats:


```java
//No parameters method
[PostPrimary]
public void MyEventListener()

//Single space proxy parameter
[PostPrimary]
public void MyEventListener(ISpaceProxy spaceProxy)

//Two parameter, space proxy and space mode
[PostPrimary]
public void MyEventListener(ISpaceProxy spaceProxy, SpaceMode spaceMode)

//If more than one space is defined, specify the name of the space to avoid ambiguity
[PostPrimary(SpaceProxyName="MySpace")]
public void MyEventListener(ISpaceProxy spaceProxy)
```

When registering for the \[BeforePrimary\] or \[BeforeBackup\], special care should be taken. The event handling of these listeners will **delay the space instance life cycle completion** for a co-located space instance - i.e., a primary space instance will be blocked from fully becoming a primary space until it completes all the invocations of the \[BeforePrimary\] subscribers. There is no guarantee for receiving a corresponding Before event always prior to a Post event. When the processing unit starts, the event subscription is asynchronous to the space instance active election; in this case it is quite reasonable not to receive the Before events and only to receive the Post events.
