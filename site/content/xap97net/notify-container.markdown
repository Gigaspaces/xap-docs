---
type: post97net
title:  Notify Container
categories: XAP97NET
parent: event-processing.html
weight: 200
---



{{% ssummary%}} {{% /ssummary %}}

{{%section%}}
{{%column width="70%" %}}
The notify event container implements the [IEventListenerContainer](./event-listener-container.html) interface, and uses the space inheritance support for notifications, using a GigaSpaces data event session API. If a notification occurs, the [DataEventArrived](./event-listener-container.html#DataEventArrived) event is invoked with the event. A notify event operation is mainly used when simulating Topic semantics.
{{%/column%}}
{{%column width="30%" %}}
<img src="/attachment_files/dotnet/Net_notify_cont.jpg" width="150" height="200">

{{%/column%}}
{{%/section%}}


The examples in this page follow a certain pattern -- each code example has two tabs: Using EventListenerContainerFactory and NotifyEventListenerContainer Code Construction.

The first tab demonstrates how to create and configure a notify container using the `EventListenerContainerFactory`, and the second tab demonstrates how to build and configure a `NotifyEventListenerContainer` with a constructor and setting the different properties.

Here is a simple example of polling event container construction:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven]
public class SimpleListener
{
    [EventTemplate]
    public Data UnprocessedData
    {
        get
        {
            Data template = new Data();
            template.Processed = false;
            return template;
        }
    }

    [DataEventHandler]
    public Data ProcessData(Data event)
    {
        //process Data here and return processed data
    }
}
```

Constructing the notify container that uses the `SimpleListener` class as the event listener, and starting it.


```csharp
ISpaceProxy spaceProxy = // either create the SpaceProxy or obtain a reference to it
IEventListenerContainer<Data> eventListenerContainer = EventListenerContainerFactory.CreateContainer<Data>(spaceProxy, new SimpleListener());

eventListenerContainer.Start();

// when needed dispose of the container
eventListenerContainer.Dispose()
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
ISpaceProxy spaceProxy = // either create the SpaceProxy or obtain a reference to it

NotifyEventListenerContainer<Data> notifyEventListenerContainer = new NotifyEventListenerContainer<Data>(spaceProxy);

notifyEventListenerContainer.Template = new Data(false);
notifyEventListenerContainer.DataEventArrived += new DelegateDataEventArrivedAdapter<Data,Data>(ProcessData).WriteBackDataEventHandler;

// when needed dispose of the container
notifyEventListenerContainer.Dispose();
```

Event processing method


```csharp
public Data ProcessData(IEventListenerContainer<Data> sender, DataEventArgs<Data> e)
{
	Data data = e.Data;
	//process Data here and return processed data
}
```

{{% info %}}
[DelegateDataEventArrivedAdapter](./event-listener-container.html#DelegateDataEventArrivedAdapter) is a class that adapts the supplied user method to the [DataEventHandler](./event-listener-container.html#DataEventHandler) delegate, and contains a built in logic of writing back event results to the space
{{% /info %}}

{{% /tab %}}

{{% /tabs %}}

The above example registers with the space for write notifications using the provided template, which can be any .NET object (in this case a `Data` object with its processed flag set to `false`). If a notification occurs, the `SimpleListener` is invoked. Registration for notifications is performed on the supplied space proxy.

# Primary/Backup

The notify event container registers for notifications only when the relevant space it is working against is in primary mode. When the space is in backup mode, no registration occurs. If the space moves from backup mode to primary mode, the container registers for notifications, and if it moved to backup mode, the registrations are canceled.

{{% info %}}
This mostly applies when working with an embedded space directly with a cluster member. When working with a clustered space (performing operations against the whole cluster), the mode of the space is always primary.
{{%/info%}}

# Template Definition

When performing receive operations, a template is defined, creating a virtualized subset of data in the space, matching it. GigaSpaces supports templates based on the actual domain model (with `null` values denoting wildcards), which are shown in the examples. GigaSpaces allows the use of [SqlQuery](./query-sql.html) in order to query the space, which can be easily used with the event container as the template. Here is an example of how it can be defined:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven]
public class SimpleListener
{
    [EventTemplate]
    public SqlQuery<Data> UnprocessedData
    {
        get
        {
            SqlQuery<Data> templateQuery = new SqlQuery<Data>(template, "Processed = ?");
	        templateQuery.SetParameter(1, false);

            return templateQuery;
        }
    }

    [DataEventHandler]
    public Data ProcessData(Data event)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
NotifyEventListenerContainer<Data> notifyEventListenerContainer = // create or obtain a reference to a notify container

SqlQuery query = new SqlQuery<Data>("Processed = ?");
query.SetParameter (1, false);
notifyEventListenerContainer.Template = query;

```

{{% /tab %}}

{{% /tabs %}}

# Transaction Support

The notify container can be configured with transaction support, so the event action can be performed under a transaction. Exceptions thrown by the event listener cause the operations performed within the listener to be rolled back automatically.

{{% note %}}
When using transactions, only the event listener operations are rolled back. The notifications are not sent again in case of a transaction rollback. If this behavior is required, please consider using the [Polling Event Container](./polling-container.html). Adding transaction support to the polling container is very simple. It is done by setting the `TransactionType` property. There are two transaction types: Distributed and Manual.
{{%/note%}}

- Distributed transaction - an embedded distributed transaction manager will be created and it will be used for creating transaction (Only one transaction manager will be created per AppDomain).
- Manual transaction - transactions will be created by the transaction manager that is stored in the `TransactionManager` property. By default no transaction manager is stored and therefore, no transaction will be used. For example:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven]
[TransactionalEvent(TransactionType = TransactionType.Distributed)]
public class SimpleListener
{
 ...
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
ISpaceProxy spaceProxy = // either create the SpaceProxy or obtain a reference to it

NotifyEventListenerContainer<Data> notifyEventListenerContainer = new NotifyEventListenerContainer<Data>(spaceProxy);
notifyEventListenerContainer.TransactionType = TransactionType.Distributed;
```

{{% /tab %}}

{{% /tabs %}}

It is possible to receive a reference to the on going transaction as part of the event handling method, if for instance, the event handling is doing some extra space operations which should be executed under the same transaction context and rolled back upon failure. This is done be adding a transaction parameter to the event handler method. For example:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven]
[TransactionalEvent(TransactionType = TransactionType.Distributed)]
public class SimpleListener
{
 ...

    [DataEventHandler]
    public Data ProcessData(Data event, ISpaceProxy spaceProxy, ITransaction transaction)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
ISpaceProxy spaceProxy = // either create the SpaceProxy or obtain a reference to it

NotifyEventListenerContainer<Data> notifyEventListenerContainer = new NotifyEventListenerContainer<Data>(spaceProxy);
notifyEventListenerContainer.TransactionType = TransactionType.Distributed;
notifyEventListenerContainer.DataEventArrived += new DelegateDataEventArrivedAdapter<Data,Data>(ProcessData).WriteBackDataEventHandler;
notifyEventListenerContainer.Start();
```

{{% /tab %}}

{{% /tabs %}}

{{% refer %}}The order of parameters of the event handling method is strict, please refer to [Dynamic Data Event Handler Adapter](./event-listener-container.html#eventhandleradapter) for more information about it.{{% /refer %}}

# Masking Notifications

The notify container allows you to mask which operations performed against the space, should cause notifications. By default (if none is defined), notifications are sent for write operations. The operations are: `write` (an entry matching the template has been written to the space), `update` (an entry matching the template has been updated in the Space), `take` (an entry matching the template has been taken from the Space), `lease expiration` (an entry matching the template lease has been expired), and `all`. Here is an example of the notify container configured to trigger notifications for both write and update operations:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven(NotifyType = DataEventType.Write | DataEventType.Update)]
public class SimpleListener
{
    [EventTemplate]
    public Data UnprocessedData
    {
        get
        {
            Data template = new Data();
            template.Processed = false;
            return template;
        }
    }

    [DataEventHandler]
    public Data ProcessData(Data event)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
NotifyEventListenerContainer<Data> notifyEventListenerContainer = // create or obtain a reference to a notify container

notifyEventListenerContainer.NotifyType = DataEventType.Write | DataEventType.Update;
```

{{% /tab %}}

{{% /tabs %}}

# Batch Events

The notify container, through the unified event API, allows batching of notifications. Batching causes the space to accumulate the notifications, and once a certain amount of time has passed or a certain size is reached, causes the events to be raised to the client. Batching is very useful when working with a remote space, since it reduces the network roundtrip operations. Moreover, when using Batch notification, it is possible (but not mandatory) to work with the [BatchDataEventArrived](./event-listener-container.html#BatchDataEventArrived) instead, and handle a batch of notifications at once.

Below is an example of batching, where if the number of notifications has passed 10, or the time passed is 5 seconds (since the last batch was sent), a batch of notifications is sent to the client:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven]
public class SimpleListener
{
    [EventSessionConfig]
    public EventSessionConfig BatchConfig
    {
        get
        {
            EventSessionConfig sessionConfig = new EventSessionConfig();
            sessionConfig.Batch = true;
            sessionConfig.BatchSize = 10;
            sessionConfig.BatchTime = 5000;
            return config;
        }
    }

    [EventTemplate]
    public Data UnprocessedData
    {
        get
        {
            Data template = new Data();
            template.Processed = false;
            return template;
        }
    }

    [DataEventHandler]
    public Data[] ProcessData(Data[] event)
    {
        //process batch Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
NotifyEventListenerContainer<Data> notifyEventListenerContainer = // create or obtain a reference to a notify container

EventSessionConfig sessionConfig = new EventSessionConfig();
sessionConfig.Batch = true;
sessionConfig.BatchSize = 10;
sessionConfig.BatchTime = 5000;
notifyEventListenerContainer.EventSessionConfig = sessionConfig;
notifyEventListenerContainer.BatchDataEventArrived += new DelegateDataEventArrivedAdapter<Data,Data[]>(ProcessData).WriteBackBatchDataEventHandler;
```

{{% /tab %}}

{{% /tabs %}}

# FIFO Events

The notify event container can register for events or notifications, and have the events delivered in a FIFO order.

{{% info %}}
For full FIFO support, the actual template also has to be marked as FIFO. For more details, refer to the [FIFO Support](./fifo-support.html) section.
{{%/info%}}

Here is an example of how FIFO events can be configured with the notify container:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven]
public class SimpleListener
{
    [EventSessionConfig]
    public EventSessionConfig FifoConfig
    {
        get
        {
            EventSessionConfig sessionConfig = new EventSessionConfig();
            sessionConfig.Fifo = true;
            return config;
        }
    }

    [EventTemplate]
    public Data UnprocessedData
    {
        get
        {
            Data template = new Data();
            template.Processed = false;
            return template;
        }
    }

    [DataEventHandler]
    public Data ProcessData(Data event)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
NotifyEventListenerContainer<Data> notifyEventListenerContainer = // create or obtain a reference to a notify container

EventSessionConfig sessionConfig = new EventSessionConfig();
sessionConfig.Fifo = true;
notifyEventListenerContainer.EventSessionConfig = sessionConfig;
```

{{% /tab %}}

{{% /tabs %}}

# Durable Notifications

Durable notifications allows configuring the notify container to withstand failover and short network disconnections with no notifications lost.

Here is an example of how Durable Notifications can be configured with the notify container:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven]
public class SimpleListener
{
    [EventSessionConfig]
    public EventSessionConfig DurableNotificationConfig
    {
        get
        {
            EventSessionConfig sessionConfig = new EventSessionConfig();
            sessionConfig.DurableNotifications = true;
            return config;
        }
    }

    [EventTemplate]
    public Data UnprocessedData
    {
        get
        {
            Data template = new Data();
            template.Processed = false;
            return template;
        }
    }

    [DataEventHandler]
    public Data ProcessData(Data event)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
NotifyEventListenerContainer<Data> notifyEventListenerContainer = // create or obtain a reference to a notify container

EventSessionConfig sessionConfig = new EventSessionConfig();
sessionConfig.DurableNotifications = true;
notifyEventListenerContainer.EventSessionConfig = sessionConfig;
```

{{% /tab %}}

{{% /tabs %}}

# Take on Notify

The notify event container can be configured to automatically perform a take on the notification data event. It can also be further configured to filter out events if the take operation returned `null`. (This usually happens when several clients receive this event, and only one succeeds with the take.)

Here is how the notify container can be configured:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven(PerformTakeOnNotify = true, IgnoreEventOnNullTake = true)]
public class SimpleListener
{
    [EventTemplate]
    public Data UnprocessedData
    {
        get
        {
            Data template = new Data();
            template.Processed = false;
            return template;
        }
    }

    [DataEventHandler]
    public Data ProcessData(Data event)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
NotifyEventListenerContainer<Data> notifyEventListenerContainer = // create or obtain a reference to a notify container

notifyEventListenerContainer.PerformTakeOnNotify = true;
notifyEventListenerContainer.IgnoreEventOnNullTake = true;
```

{{% /tab %}}

{{% /tabs %}}

{{% anchor SpaceDataEventArgs %}}

# Space Data Event Args

The notify container uses GigaSpaces [data event session API](./the-space-notifications.html) under the hood. When a notification is triggered, it contains SpaceDataEventArgs, which holds more information about the notification itself, such as the template and the DataEventType (e.g. was this notification triggered by a write or an update operation?). When using the notify container, it is possible to receive that additional information as a parameter of the event listener method:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven]
public class SimpleListener
{
    [EventTemplate]
    public Data UnprocessedData
    {
        get
        {
            Data template = new Data();
            template.Processed = false;
            return template;
        }
    }

    [DataEventHandler]
    public Data ProcessData(Data event, ISpaceProxy proxy, ITransaction tx, SpaceDataEventArgs<object> e)
    {
     	DataEventType eventType = e.EventType;
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
ISpaceProxy spaceProxy = // either create the SpaceProxy or obtain a reference to it

NotifyEventListenerContainer<Data> notifyEventListenerContainer = new NotifyEventListenerContainer<Data>(spaceProxy);

notifyEventListenerContainer.Template = new Data(false);
notifyEventListenerContainer.DataEventArrived += new DelegateDataEventArrivedAdapter<Data,Data>(ProcessData).WriteBackDataEventHandler;

// when needed dispose of the container
notifyEventListenerContainer.Dispose();
```

Event processing method


```csharp
public Data ProcessData(IEventListenerContainer sender, DataEventArgs<Data> e)
{
    SpaceDataEventArgs eventArgs = (SpaceDataEventArgs)e.CustomEventArgs;
	Data data = e.Data;
	//process Data here and return processed data
}
```

{{% info %}}
[DelegateDataEventArrivedAdapter](./event-listener-container.html#DelegateDataEventArrivedAdapter) is a class that adapts the supplied user method to the [DataEventHandler](./event-listener-container.html#DataEventHandler) delegate, and contains a built-in logic of writing event results back to the space
{{% /info %}}

{{% /tab %}}

{{% /tabs %}}

# Queued Event Handling

When a notification is received, it occurs asynchronously in a separate thread. That thread is part of the space proxy resource pool that is in charge of receiving and executing notifications code. As a result, special care should be taken when the event listening method execution is not very short, because it could hold the proxy resource for too long, and eventually exhaust it, which causes notifications to get lost. If the event listening method needs to execute code that takes some time, it is recommended to use the `QueuedEventHandling` feature.

When this feature is enabled, each event that is received is put into a queue, and the notifying thread is released immediately. The queue is processed by a different thread or threads. Doing this keeps the proxy resource pool free. The number of threads that are processing the events together, can be determined using the `QueuedEventHandlersPoolSize` property. The queue size limit is configured using the `QueuedEventsSizeLimitProperty`. When the limit is reached, the notify thread blocks until it can insert the event into the queue. This is done in order to avoid the client running out of memory when it process events too slowly, and the queue keeps accumulating.

Here is how the notify container can be configured:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven(QueuedEventHandling = true, QueuedEventHandlersPoolSize = 3)]
public class SimpleListener
{
    [EventTemplate]
    public Data UnprocessedData
    {
        get
        {
            Data template = new Data();
            template.Processed = false;
            return template;
        }
    }

    [DataEventHandler]
    public Data ProcessData(Data event)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
NotifyEventListenerContainer<Data> notifyEventListenerContainer = // create or obtain a reference to a notify container

notifyEventListenerContainer.QueuedEventHandling = true;
notifyEventListenerContainer.QueuedEventHandlersPoolSize = 3;
```

{{% /tab %}}

{{% /tabs %}}

Sometimes, it is very convenient to have a listener instance per concurrent queue processing thread. This allows a thread-safe instance variable to be constructed without worrying about concurrent access. In such a case, the event listener containing class should implement `System.ICloneable`, and the `CloneEventListenersPerThread` property should be set to true. Here is an example:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven(QueuedEventHandling = true, QueuedEventHandlersPoolSize = 3, CloneEventListenersPerThread = true]
public class SimpleListener : ICloneable
{
 ...
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
NotifyEventListenerContainer<Data> notifyEventListenerContainer = // create or obtain a reference to a notify container

notifyEventListenerContainer.QueuedEventHandling = true;
notifyEventListenerContainer.QueuedEventHandlersPoolSize = 3;
notifyEventListenerContainer.CloneEventListenersPerThread = true;
```

{{% /tab %}}

{{% /tabs %}}

# Handling Exceptions

During the life-cycle of the polling container, two types of exceptions might be thrown:

- User Exception
- Container Exception

A User Exception is an exception that occurs during the invocation of the user event listener. A Container Exception is an exception that occurs anywhere else during the life-cycle of the container (for example, during the receive or trigger operation handler).

## Subscribing to the ContainerExceptionOccurred Event

It is possible to be notified when a container exception occurred, by subscribing to the ContainerExceptionOccurred event, and get a reference to the exception.

Here is an example of how to subscribe to this event:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven]
public class SimpleListener
{
    [ContainerException]
    public void ExceptionHandler(IEventListenerContainer<Data> sender, ContainerExceptionEventArgs e)
    {
         Console.WriteLine("Container Name: " + ((IEventListenerContainer<Data>)sender).Name);
         Console.WriteLine(e.Exception.Message);
    }

    ...
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
NotifyEventListenerContainer<Data> notifyEventListenerContainer = // create or obtain a reference to a notify container

notifyEventListenerContainer.ContainerExceptionOccured += ExceptionHandler;
```


```csharp
public void ExceptionHandler(object sender, ContainerExceptionEventArgs e)
{
     Console.WriteLine("Container Name: " + ((IEventListenerContainer<Data>)sender).Name);
     Console.WriteLine(e.Exception.Message);
}
```

{{% /tab %}}

{{% /tabs %}}

## Subscribing to the UserExceptionOccured Event

It is possible to be notified when a user exception occurred, by subscribing to the UserExceptionOccurred event. This arguments of this event contain the entire DataEventArgs of the original DataEventArrived. By default, any event that is thrown inside the event listener scope, results in transaction rollback if the container is set to be transactional. This can be overridden if the user exception handler sets the event state to: ignored.

Here is an example of how to subscribe to this event:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[NotifyEventDriven]
public class SimpleListener
{
    [UserException]
    public void ExceptionHandler(IEventListenerContainer<Data> sender, UserExceptionEventArgs<Data> e)
    {
        if (e.Exception is MySpecialException)
     	    e.Ignore = true;
    }

    ...
}
```

{{% /tab %}}

{{%tab "  NotifyEventListenerContainer Code Construction "%}}


```csharp
NotifyEventListenerContainer<Data> notifyEventListenerContainer = // create or obtain a reference to a notify container

notifyEventListenerContainer.UserExceptionOccured += ExceptionHandler;
```


```csharp
public void ExceptionHandler(object sender, UserExceptionEventArgs<Data> e)
{
     if (e.Exception is MySpecialException)
     	 e.Ignore = true;
}
```

{{% /tab %}}

{{% /tabs %}}

# Default Values of Notify Container Configuration Parameters

The default values for all of the notify container configuration parameters, such as `perform-take-on-notify, ignore-event-on-null-take`, and others, can be found in the API docs. Each property has a corresponding Default<property name> const field that sets the default value of the property.


{{% include "/COM/notify-verses-polling.markdown" %}}
