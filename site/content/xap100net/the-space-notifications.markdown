---
type: post100net
title:  Notifications
categories: XAP100NET
parent: the-gigaspace-interface-overview.html
weight: 600
---

{{% ssummary %}}{{% /ssummary %}}


It is possible to subscribe to different space events and be notified of changes inside the space that match the event subscription. These notifications are pushed from the space to the proxy, unlike the opposite way, where the proxy executes queries on the space.

This page demonstrates a basic event usage scenario.

# Event Registration

Subscribing to an event is done using an `IDataEventSession` with a [Query Template Types], an event type and a callback method. `ISpaceProxy` has a default data event session that can be used for subscription.

The following example demonstrates simple events usage:


```csharp
public class Person
{
  private String _userId;

  public String UserId
  {
    get { return _userId; }
    set { _userId = value; }
  }

  public Person()
  {
  }
}

//Callback method that is triggered when the event fires
void Space_PersonChanged(object sender, SpaceDataEventArgs<Person> e)
{
  Console.WriteLine("Person with UserId: " + e.Object.UserId + " was written to the space);
}

//Event subscription
IEventRegistration registration = proxy.DefaultDataEventSession.AddListener(new Person(),
                                                                            Space_PersonChanged,
                                                                            DataEventType.Write);
```

Any new person entries that are written to the space, trigger the event and execute the `Space_PersonChanged` callback method at the client side.
The DataEventType dictates which type of events to listen for. It's a flag enum that can have more than one value -- for example, listening to `Write` and `Update` events looks like this:


```csharp
IEventRegistration registration = proxy.DefaultDataEventSession.AddListener(new Person(),
                                                                            Space_PersonChanged,
                                                                            DataEventType.Write | DataEventType.Update);
```

When the events are no longer relevant, the registration for the events should be removed, to reduce the load on the space.


```csharp
proxy.DefaultDataEventSession.RemoveListener(registration);
```

In most cases, using the `DefaultDataEventSession` is enough, however, in some scenarios the `DataEventSession` needs to be customized.


# Event Mechanism in General

Event registration is done using a supplied [Query Template Types], and a callback method. The registration notify template can be stored in one or more spaces, depending on the template and cluster topology.

Every time an event occurs in a space, which matches the given template and event type in that space, the event is triggered at the proxy, which activates the callback method. What happens if the space or the proxy is no longer available? What happens if the proxy can't manage the events overload? These issues can be addressed by customizing a data event session, with appropriate behaviors to handle these issues.

# Customizing DataEventSession Behavior

In order to customize the behavior of an [`IDataEventSession`](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_Events_IDataEventSession.htm), a new one needs to be created, using a specific [`DataEventConfig` ](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_Events_EventSessionConfig.htm) that configures the behavior. This section describes different scenarios, and how to address them, by customizing the data event session


```csharp
EventSessionConfig eventSessionConfig = new EventSessionConfig();
//Customize the eventSessionConfig
...

//Create a customized data event session
IDataEventSession dataEventSession = proxy.CreateDataEventSession(eventSessionConfig);
```

## Detecting an Event Listener Failure/Disconnection

An event listener that is registered for an event might be disconnected for the following reasons:

- The space that holds the listener registration template, is no longer available.
- The proxy that receives the notifications can't handle the amount of incoming events, and creates a [slow consumer]({{% currentadmurl %}}/slow-consumer.html) scenario, which causes the space to disconnect the listener.

In order to detect and handle listener disconnection, the **auto renewal** mechanism can be used.

When adding a new listener, one of the parameters is the leaseTime. A leaseTime is a concept that appears in many places in the API. The idea behind it, is that it allows you to specify how long to keep the object alive in the space. In a listener context, it means how long the event registration should remain active (in milliseconds). By default, the lease time is forever (Long.MaxValue).

The auto renewal idea is that the listener is added with a limited lease, for example 10 seconds, and the proxy automatically renews the lease before it expires. The client can register to the `EventSessionConfig.AutoRenewalFailed` event, and be notified if the auto renewal failed. This approach solves the following issues:

- The client process terminated unexpectedly, and didn't un register its listener. After the lease expires, the notify template is removed from the space, instead of staying alive forever.
- If the space is no longer available, then the client is notified that it couldn't renew the listener lease.
- If the client causes a [slow consumer]({{% currentadmurl %}}/slow-consumer.html) scenario, and as a result its listener has been disconnected by the space, then the client is notified that it couldn't renew the listener lease, and it can reregister to the event.

**Configuring data event session with auto renewal**
Auto renewal behavior is determined by the `EventSessionConfig.AutoRenew` property.


```csharp
EventSessionConfig eventSessionConfig = new EventSessionConfig();
eventSessionConfig.AutoRenew = true;
```

The auto renewal process uses a few parameters that dictate the timing of its behavior. These values have proper defaults, but can be altered, for example:


```csharp
//Auto renewal is active for 1 minute
eventSessionConfig.AutoRenewTotalDuration = 60000;
//Each time renew the lease for 10 seconds
eventSessionConfig.AutoRenewLeaseDuration = 10000;
//The network latency can reach 5 seconds, send renewal request 5 seconds before the lease expires.
eventSessionConfig.AutoRenewRTT = 5000;
```

## Managing High Notifications Throughput

When a notification is sent from the space to the client, the callback method is executed inside a thread that belongs to the resource pool of the proxy. As a result, this thread is occupied until the callback method returns. As a good practice, it is recommended to create the callback method that returns as fast as possible, otherwise the resources pool of the proxy can be choked, and cause a [slow consumer]({{% currentadmurl %}}/slow-consumer.html) scenario. If the notifications should trigger a long running job, it is better to put this job in a queue, and handle it in a client thread later on.

It is possible to reduce network traffic, and concurrent threads that handle notifications, by using the batch notification mechanism. Instead of sending each notification separately, notifications are grouped together in the space, and sent as one batch.

**Configuring data event session with batch notifications**
Batch notification behavior is determined by the `EventSessionConfig.Batch` property, in conjunction with the `EventSessionConfig.BatchSize` and `EventSessionConfig.BatchTime` properties.

The batch notification is sent when either one of these two parameters has been reached or exceeded. Either the pending notification size has reached the `BatchSize`, or the time, in milliseconds, that elapsed from the last sent notification batch, exceeds `BatchTime`.


```csharp
EventSessionConfig eventSessionConfig = new EventSessionConfig();
eventSessionConfig.Batch = true;
//Send notifications when the batch size reached 100
eventSessionConfig.BatchSize = 100;
//Send notifications at maximum every 1 seconds.
eventSessionConfig.BatchTime = 1000;
```
