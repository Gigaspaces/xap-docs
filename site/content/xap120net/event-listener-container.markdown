---
type: post120
title:  Event Listener Container
categories: XAP120NET, PRM
parent: event-processing.html
weight: 100
---

{{%ssummary%}}{{%/ssummary%}}

{{%section%}}
{{%column width="70%" %}}
The `IEventListenerContainer` interface is an abstraction that allows subscribing to, and receiving events from the space, where in most cases, the business logic doesn't need to be aware of the actual container implementation. The benefit of doing this, is the loose coupling between how the events are received (the different containers), and what to do with a received event (the listener). This interface has two out-of-the-box implementers: [PollingEventListenerContainer](./polling-container.html) and [NotifyEventListenerContainer](./notify-container.html).
{{%/column%}}
{{%column width="30%" %}}
![Net_polling_notify_cont.jpg](/attachment_files/dotnet/Net_polling_notify_cont.jpg)
{{%/column%}}
{{%/section%}}

{{% anchor  DataEventArrived %}}
{{% anchor  DataEventHandler %}}

# DataEventArrived

The `IEventListenerContainer` interface exposes the `DataEventArrived` event, where the various implementers invoke (according to their internal logic, which determines when a space data event arrived occurs). The event argument contains the `data` object, which can be any .NET object, that triggered this event. The event subscription is done with a listener method that has the `DataEventHandler` delegate constraints, which follows the .NET event handler's conventions.

Here is a simple example of event subscription:


```csharp
IEventListenerContainer<Data> container = // obtain a reference to a container

container.DataEventArrived += MyEventHandler;
```

The event handler method:


```csharp
void MyEventHandler(object sender, DataEventArgs<Data> e)
{
    // handle event
}
```

The sender is the actual container that sent this event (Can be casted to IEventListenerContainer), the DataEventArgs contains the argument of the event, such as the event data object, the space proxy the event received from and so on.

{{% anchor  BatchDataEventArrived %}}

## Batch Event

Sometimes it is better to use batch events, for instance to improve network traffic. This is done by subscribing to the `BatchDataEventArrived` event. This event invokes a listener method that receives a batch of event data in one invocation. The different implementers of the `IEventListenerContainer` interface have their own logic of when to use batch events and when to use regular events.

Here is a simple example of event subscription:


```csharp
IEventListenerContainer<Data> container = // obtain a reference to a container

container.BatchDataEventArrived += MyBatchEventHandler;
```

The event handler method:


```csharp
void MyEventHandler(object sender, BatchDataEventArgs<Data> e)
{
    Data[] batch = e.Data;
    // handle event
}
```

{{% anchor EventListenerContainerFactory %}}

# Event Listener Container Factory

One of the ways to create an event listener container is to use the `EventListenerContainerFactory.CreateContainer` method. The factory creates a container from a supplied listener class, which is marked with attributes that are used to configure and create the container. This is demonstrated in the [Polling Container Component page](./polling-container.html) and the [Notify Container Component page](./notify-container.html).

# Data Event Handler Adapters

In many scenarios, the event triggers a processing operation and its result should be written or updated back to the space. Since this scenario is very common, there are two built-in adapters that adapt a user method, which has a return value, and then automatically writes (or updates) the result back to the space.

{{% anchor eventhandleradapter %}}

## DynamicMethod DataEventArrived Adapter

The `DynamicMethodDataEventArrivedAdapter<TData>` is an internal class that is used by the `EventListenerContainerFactory`. This class dynamically creates a wrapper method over user methods that are marked with the [DataEventHandler attribute](#dataeventhandler-attribute). If the user method has a return value which is not null, the wrapper is automatically written to the space, with configurable parameters (see  [DataEventHandler attribute](#dataeventhandler-attribute)). This adapter gives you the ability to write the event listening method receiving only the parameters that you need.

Here are a few examples:

**An event listening method that processes the data, and returns a processed result**


```csharp
public class SimpleListener
{
    ...

    [DataEventHandler]
    public Data ProcessData(Data event)
    {
        //process Data here and return processed data
    }
}
```

**An event listening method that processes the data, executes an additional space operation that is needed to enrich the data, and returns an enriched data result**


```csharp
public class SimpleListener
{
    ...

    [DataEventHandler]
    public EnrichedData ProcessData(Data event, ISpaceProxy proxy)
    {
        //process Data here and return processed enriched data
    }
}
```

The user methods can receive different parameters and be either a void method, or return a result. The parameters can be between 0 to 5 and must follow a certain order:

- The first parameter is the event data.
- The second parameter is the space proxy the event arrived from.
- The third parameter is a transaction if the event is executed within a transaction context.
- The fourth parameter is the custom event args that each container creates (for example, see [Notify Container SpaceDataEventArgs](./notify-container.html#space-data-event-args)).
- The fifth parameter is the `IEventListenerContainer` that triggered this event.

The return parameter, if not void, is the result that is written back to the space (when the result is not null).

For example:


```csharp
public EnrichedData ProcessData(Data event, ISpaceProxy proxy, ITransaction tx, SpaceDataEventArgs<object> args, IEventListenerContainer container)
```

{{% anchor DelegateDataEventArrivedAdapter %}}

## Delegate DataEventArrived Adapter

The `DelegateDataEventArrivedAdapter<TData, TResult>` receives a delegate to a method that receives similar event args as the `DataEventHandler`, but also returns a result of type `TResult`. If the result is not null, it is automatically written to the space, with configurable parameters (see [DataEventHandler attribute](#dataeventhandler-attribute)). After the adapter is created, its `WriteBackDataEventHandler` method adapts the supplied method, while adding the write back logic, and it can be used as the delegate when subscribing to the [DataEventArrived event](#DataEventArrived).

Here is a simple example:


```csharp
IEventListenerContainer<Data> container = // obtain a reference to a container

container.DataEventArrived = new DelegateDataEventArrivedAdapter<Data, Data>(MyEventHandler).WriteBackDataEventHandler;
```

The event handler method:


```csharp
Data MyEventHandler(IEventListenerContainer sender, DataEventArgs<Data> e)
{
    // handle event and return processed data to be written back to the space
}
```

The result can also be an array, and the appropriate multiple operation is executed (WriteMultiple or UpdateMultiple).

{{% anchor DataEventHandlerAttribute %}}

## DataEventHandler attribute

The DataEventHandler attribute has two roles. The first is to mark a method to be subscribed to the [DataEventArrived event](#DataEventArrived), when using the [EventListenerContainerFactory](#EventListenerContainerFactory). The second role is to determine the write back behavior of the marked method result. This behavior has three configurable parameters:

- WriteOrUpdate -- states whether to write or update the result back to the space, or only to write, which means if there's a matching object in the space to the result object, an `EntryAlreadyInSpaceException` is thrown (the default is true).
- WriteLease -- the lease of the result object that is being written to the space (the default is lease forever).
- UpdateTimeout -- if the operation is an update operation, determines how long to wait if the object is locked for updates (the default is 0).

{{% info %}}
The write back behavior is modified when using the `DelegateDataEventArrivedAdapter` by this attribute as well.
{{%/info%}}

Here is a simple example:


```csharp
[DataEventHandler(WriteOrUpdate = false, WriteLease = 10000)]
public EnrichedData MyEventHandler(Data data)
{
    // handle the event
}
```
