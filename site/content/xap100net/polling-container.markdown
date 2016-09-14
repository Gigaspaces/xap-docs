---
type: post100
title:  Overview
categories: XAP100NET
parent: polling-container-overview.html
weight: 100
---



{{% ssummary %}} {{% /ssummary %}}



{{%section%}}
{{%column width="70%" %}}
The polling event container implements the [IEventListenerContainer](./event-listener-container.html) interface. Its life-cycle consists of performing polling receive operations against the space. If a receive operation succeeds (a value is returned from the receive operation), the [DataEventArrived](./event-listener-container.html#DataEventArrived) event is invoked. A polling event operation is mainly used when simulating Queue semantics, or when using the master-worker design pattern.
{{%/column%}}
{{%column width="30%" %}}
![Net_polling_cont.jpg](/attachment_files/dotnet/Net_polling_cont.jpg)
{{%/column%}}
{{%/section%}}

The examples in this page follow a certain pattern. Each code example has two tabs: Using EventListenerContainerFactory, and PollingEventListenerContainer Code Construction.

The first tab demonstrates how to create and configure a polling container using the `EventListenerContainerFactory`, and the second tab demonstrates how to build and configure a `PollingEventListenerContainer` with a constructor and setting the different properties.

Here is a simple example of polling event container construction:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```java
[PollingEventDriven]
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
    public Data ProcessData(Data inputObject)
    {
        //process Data here and return processed data
    }
}
```

Constructing the polling container that uses the `SimpleListener` class as the event listener, and starting it.


```csharp
ISpaceProxy spaceProxy = // either create the SpaceProxy or obtain a reference to it
IEventListenerContainer<Data> eventListenerContainer = EventListenerContainerFactory.CreateContainer<Data>(spaceProxy, new SimpleListener());

eventListenerContainer.Start();

// when needed to dispose of the container
eventListenerContainer.Dispose()
```

{{% /tab %}}

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
ISpaceProxy spaceProxy = // either create the SpaceProxy or obtain a reference to it

PollingEventListenerContainer<Data> pollingEventListenerContainer = new PollingEventListenerContainer<Data>(spaceProxy);

pollingEventListenerContainer.Template = new Data(false);
pollingEventListenerContainer.DataEventArrived += new DelegateDataEventArrivedAdapter<Data,Data>(ProcessData).WriteBackDataEventHandler;

// when needed dispose of the container
pollingEventListenerContainer.Dispose();
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
[DelegateDataEventArrivedAdapter](./event-listener-container.html#DelegateDataEventArrivedAdapter) is a class that adapts the supplied user method to the [SpaceDataEventHandler](./event-listener-container.html#SpaceDataEventHandler) delegate and contains a built in logic of writing back event results to the space
{{% /info %}}

{{% /tab %}}

{{% /tabs %}}

The example above performs single take operations (see [below](#receive-operation-handler)), using the provided template, which can be any .NET object (in this case a `Data` object with its processed flag set to `false`). If the take operation succeeds (a value is returned), the `SimpleListener.ProcessData` method is invoked. The operations are performed on the supplied space proxy.

# Primary/Backup

The polling event container performs receive operations only when the relevant space it is working against is in primary mode. When the space is in backup mode, no receive operations are performed. If the space moves from backup mode to primary mode, the receive operations are started.

{{% info %}}
This mostly applies when working with an embedded space directly with a cluster member. When working with a clustered space (performing operations against the whole cluster), the mode of the space is always primary.
{{%/info%}}

# FIFO Grouping

The FIFO Grouping designed to allow efficient processing of events with partial ordering constraints. Instead of maintaining a FIFO queue per class type, it lets you have a higher level of granularity by having FIFO queue maintained according to a specific value of a specific property. For more details see [FIFO grouping](./fifo-grouping.html).


# Static Template Definition

When performing receive operations, a template is defined, creating a virtualized subset of data within the space that matches it. GigaSpaces supports templates, based on the actual domain model (with `null` values denoting wildcards), which are shown in the examples. GigaSpaces allows the use of [SqlQuery](./query-sql.html) in order to query the space, which can be easily used with the event container as the template. Here is an example of how it can be defined:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven]
public class SimpleListener
{
    [EventTemplate]
    public SqlQuery<Data> UnprocessedData
    {
        get
        {
            SqlQuery<Data> templateQuery = new SqlQuery<Data>("Processed = true");

            return templateQuery;
        }
    }

    [DataEventHandler]
    public Data ProcessData(Data ev)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
PollingEventListenerContainer<Data> pollingEventListenerContainer = // create or obtain a reference to a polling container

pollingEventListenerContainer.Template = new SqlQuery<Data>("Processed = false");
```

{{% /tab %}}

{{% /tabs %}}





# Dynamic Template Definition

When performing polling receive operations, a dynamic template can be used. A method providing a dynamic template is called before each receive operation, and can return a different object in each call.
The event template object needs to be of IQuery<TData> type, which means if you want to use an object based template you need to wrap it with the `ObjectQuery` wrapper.

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven]
public class SimpleListener
{

    [DynamicEventTemplate]
    public SqlQuery<Data> UnprocessedExpiredData()
    {
        get
        {
          long expired = DateTime.Now.Millisecond - 60000;
          SqlQuery<Data> dynamicTemplate =
            new SqlQuery<Data>("Processed = true AND Timestamp < " + expired);
          return dynamicTemplate;
        }
    }

    [DataEventHandler]
    public Data EventListener(Data ev)
    {
        //process Data here
    }
}
```

{{% /tab %}}

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
PollingEventListenerContainer<Data> pollingEventListenerContainer = // create or obtain a reference to a polling container

pollingEventListenerContainer.DynamicTemplate = new ExpiredDataTemplateProvider.GetDynamicTemplate;

...

public class ExpiredDataTemplateProvider
{

    public SqlQuery<Data> GetDynamicTemplate()
    {
        long expired = DateTime.Now.Millisecond - 60000;
        SqlQuery<Data> dynamicTemplate =
          new SqlQuery<Data>("Processed = true AND Timestamp < " + expired);
        return dynamicTemplate;
    }
}
```

{{% /tab %}}

{{% /tabs %}}

{{% tip %}}
Only polling containers support dynamic templates. Notify containers do not support dynamic templates.
{{% /tip %}}

# Receive Operation Handler

The polling receive container performs receive operations. The actual implementation of the receive operation is abstracted using the following interface:


```csharp
public interface IReceiveOperationHandler<TData>
{
    /// <summary>
    /// Performs the actual receive operation. Return values allowed are single object or an array of objects.
    /// </summary>
    /// <param name="template">The template for the receive operation.</param>
    /// <param name="proxy">The proxy to execute the operation on.</param>
    /// <param name="tx">Operation's transaction context, can be null.</param>
    /// <param name="receiveTimeout">Operation's receive timeout</param>
    /// <returns>An object that is passed to the event listener, null result doesn't trigger an event.</returns>
    TData Receive(IQuery<TData> template, ISpaceProxy proxy, ITransaction tx, long receiveTimeout);
    /// <summary>
    /// Performs the actual receive operation. Return value is an array of data objects.
    /// </summary>
    /// <param name="template">The template for the receive operation.</param>
    /// <param name="proxy">The proxy to execute the operation on.</param>
    /// <param name="tx">Operation's transaction context, can be null.</param>
    /// <param name="receiveTimeout">Operation's receive timeout</param>
    /// <param name="batchSize">operation's batch size</param>
    /// <returns>An batch of objects that will be passed to the event listener, null result doesn't trigger an event.</returns>
    TData[] ReceiveBatch(IQuery<TData> template, ISpaceProxy proxy, ITransaction tx, long receiveTimeout, int batchSize);
}
```

XAP.NET comes with several built-in receive operation-handler implementations:


|Receive Operation Handler|Description|
|--------------------------|-----------|
| `TakeReceiveOperationHandler` |Performs a single blocking Take operation, with the receive timeout. When used in conjunction with batch events, first tries to perform TakeMultiple. If no values are returned, performs a blocking Take operation, with the receive timeout.|
| `ReadReceiveOperationHandler` |Performs a single blocking Read operation, with the receive timeout. When used in conjunction with batch events, first tries to perform ReadMultiple. If no values are returned, performs a blocking Read operation, with the receive timeout.|
| `ExclusiveReadReceiveOperationHandler` |Performs a single Read operation, under an exclusive read lock (similar to "select for update" in databases), with the receive timeout. Exclusive read lock mimics the Take operation, without actually taking the Entry from the space. When used in conjunction with batch events, First tries to perform ReadMultiple. If no values are returned, performs a blocking Read operation, with the receive timeout.

{{% warning %}}
This receive operation handler must be used within a transaction.|
{{%/warning%}}

{{% info %}}
When using the `ExclusiveReadReceiveOperationHandler`, or even the `ReadReceiveOperationHandler`, it is important to remember that the actual event still remains in the space. If the data event is not taken from the space, or one of its properties changes in order **not** to match the container template, the same data event is read again.
{{%/info%}}

Here is an example of how the receive operation handler can be configured with `ExclusiveReadReceiveOperationHandler`:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven]
public class SimpleListener
{
    [ReceiveHandler]
    public IReceiveOperationHandler<Data> ReceiveHandler()
    {
        ExclusiveReadReceiveOperationHandler<Data> receiveHandler = new ExclusiveReadReceiveOperationHandler<Data>();
        return receiveHandler;
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
    public Data ProcessData(Data ev)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
PollingEventListenerContainer<Data> pollingEventListenerContainer = // create or obtain a reference to a polling container

ExclusiveReadReceiveOperationHandler<Data> receiveHandler = new ExclusiveReadReceiveOperationHandler<Data>();
pollingEventListenerContainer.ReceiveOperationHandler = receiveHandler;
```

{{% /tab %}}

{{% /tabs %}}

## Non-Blocking Receive Handler

When working with a partitioned cluster, and configuring the polling container to work against the whole cluster, blocking operations are not allowed (when the routing index is not set in the template). The default receive operation handlers support performing the receive operation in a non-blocking manner, by sleeping between non-blocking operations. For example, the `TakeReceiveOperationHandler` performs a non-blocking Take operation against the space, and then sleeps for a configurable amount of time. Here is an example of how it can be configured:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven]
public class SimpleListener
{
    [ReceiveHandler]
    public IReceiveOperationHandler<Data> ReceiveHandler()
    {
        TakeReceiveOperationHandler<Data> receiveHandler = new TakeReceiveOperationHandler<Data>();
        receiveHandler.NonBlocking = true;
        receiveHandler.NonBlockingFactor = 10;
        return receiveHandler;
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
    public Data ProcessData(Data ev)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
PollingEventListenerContainer<Data> pollingEventListenerContainer = // create or obtain a reference to a polling container

TakeReceiveOperationHandler<Data> receiveHandler = new TakeReceiveOperationHandler<Data>();
receiveHandler.NonBlocking = true;
receiveHandler.NonBlockingFactor = 10;
pollingEventListenerContainer.ReceiveOperationHandler = receiveHandler;
```

{{% /tab %}}

{{% /tabs %}}

The above example uses a receive timeout of 10 seconds (10000 milliseconds). The `TakeReceiveOperationHandler` is configured to be non-blocking, with a non-blocking factor of 10. This means that the receive handler performs 10 non-blocking takes within 10 seconds, and sleeps the rest of the time (~1 second each time).

## Batch Events

Sometimes it is better to use batch events, for instance to improve network traffic. This is done by subscribing to the [BatchDataEventArrived event](./event-listener-container.html#BatchDataEventArrived). This event receives a batch of event data objects in one invocation.

A prime example is the `TakeReceiveOperationHandler`, which when [BatchDataEventArrived event](./event-listener-container.html#BatchDataEventArrived) are used, returns an array as a result of a `TakeMultiple` operation . The batch size is determined by the `ReceiveBatchSize` configuration attribute or property, it is set similar to the above properties modifications.

Here is an example of batch notifications using `ReadReceiveOperationHandler`:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven(ReceiveBatchSize = 100)]
public class SimpleListener
{
    [ReceiveHandler]
    public IReceiveOperationHandler<Data> ReceiveHandler()
    {
        ReadReceiveOperationHandler<Data> receiveHandler = new ReadReceiveOperationHandler<Data>();
        return receiveHandler;
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
    public Data[] ProcessData(Data[] ev)
    {
        //process batch Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
PollingEventListenerContainer<Data> pollingEventListenerContainer = // create or obtain a reference to a polling container

ReadReceiveOperationHandler<Data> receiveHandler = new ReadReceiveOperationHandler<Data>();
pollingEventListenerContainer.ReceiveOperationHandler = receiveHandler;
pollingEventListenerContainer.ReceiveBatchSize = 100;
pollingEventListenerContainer.Template = new Data(false);
pollingEventListenerContainer.BatchDataEventArrived += new DelegateDataEventArrivedAdapter<Data,Data[]>(ProcessData).WriteBackBatchDataEventHandler;

// when needed dispose of the container
pollingEventListenerContainer.Dispose();
```

{{% /tab %}}

{{% /tabs %}}


# Trigger Receive Operation

When configuring the polling event container to perform its receive operation, and event actions under a transaction, a transaction is started and committed for each unsuccessful receive operation, which results in a higher load on the space. The polling event container allows pluggable logic to be used in order to decide if the actual receive operation should be performed or not. This logic, called the trigger receive operation, is performed outside the receive transaction boundaries. The following interface is provided for custom implementation of this logic:


```csharp
public interface ITriggerOperationHandler<TData>
{
    /// <summary>
    /// Allows you to perform a trigger receive operation, which controls if the active receive operation
    /// is performed in a polling event container. This feature is mainly used when having polling event
    /// operations with transactions where the trigger receive operation is performed outside of a
    /// transaction thus reducing the creation of transactions did not perform the actual receive
    /// operation.
    ///
    /// If this method returns a non null value, it means
    /// that the receive operation should take place. If it returns a null value, no receive operation is
    /// attempted, thus no transaction is created.
    /// </summary>
    /// <param name="template">The template for the receive operation.</param>
    /// <param name="proxy">The proxy to execute the operation on.</param>
    /// <param name="receiveTimeout">Operation's receive timeout</param>
    /// <returns>Null value when the receive operation should not be triggered, otherwise a non null value that can also
    /// be used as the receive template if <see cref="UseTriggerAsTemplate"/> is set to true.</returns>
    IQuery<TData> TriggerReceive(IQuery<TData> template, ISpaceProxy proxy, long receiveTimeout);
    /// <summary>
    /// Gets if the object that return from the <see cref="TriggerReceive"/> operation should be used as the
    /// receive template instead of the configured template.
    /// </summary>
    bool UseTriggerAsTemplate { get; }
}
```

XAP.NET comes with a built-in implementation of this interface, called `ReadTriggerOperationHandler`. It performs a single blocking Read operation (using the provided receive timeout), thus "peeking" into the space for relevant event data. If the Read operation returns a value, this means that there is a higher probability that the receive operation will succeed, and the transaction won't be started without a purpose. Here is how it can be configured:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven]
public class SimpleListener
{
    [TriggerHandler]
    public ITriggerOperationHandler<Data> TriggerHandler()
    {
        ReadTriggerOperationHandler<Data> triggerHandler = new ReadTriggerOperationHandler<Data>();
        return triggerHandler;
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
    public Data ProcessData(Data ev)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
PollingEventListenerContainer<Data> pollingEventListenerContainer = // create or obtain a reference to a polling container

ReadTriggerOperationHandler<Data> triggerHandler = new ReadTriggerOperationHandler<Data>();
pollingEventListenerContainer.ReceiveOperationHandler = receiveHandler;
```

{{% /tab %}}

{{% /tabs %}}

## Non-Blocking Trigger Handler

The `ReadTriggerOperationHandler` can be set to be non-blocking, in the same way as described in [Non-Blocking Receive Handler](#non-blocking-receive-handler).

# Handling Exceptions

During the life-cycle of the polling container, two types of exceptions might be thrown:

- User Exception
- Container Exception

The User Exception is an exception that occurs during the invocation of the user event listener. The Container Exception is an exception that occurs anywhere else during the life-cycle of the container (e.g. during the receive or trigger operation handler).

## Subscribing to Container Exception Occurred Event

It is possible to be notified when a container exception occurred, by subscribing to the ContainerExceptionOccurred event, and get a reference to the exception.

Here is an example of how to subscribe to this event:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven]
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

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
PollingEventListenerContainer<Data> pollingEventListenerContainer = // create or obtain a reference to a polling container

pollingEventListenerContainer.ContainerExceptionOccurred += ExceptionHandler;
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

## Subscribing to User Exception Occurred Event

It is possible to be notified when a user exception occurred, by subscribing to the UserExceptionOccurred event. This arguments of this event contain the entire DataEventArgs of the original DataEventArrived. By default, any event that is thrown inside the event listener scope results in a transaction rollback, if the container is set to be transactional. This can be overridden if the user exception handler sets the event state to: ignored.

Here is an example of how to subscribe to this event:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven]
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

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
PollingEventListenerContainer<Data> pollingEventListenerContainer = // create or obtain a reference to a polling container

pollingEventListenerContainer.UserExceptionOccurred += ExceptionHandler;
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

# Default Values of Polling Container Configuration Parameters

The default values for all of the polling container properties such as `min-concurrent-consumers, receive-operation-handler, receive-timeout` and others can be found in the API docs. Each property has a corresponding Default<property name> const field that sets the default value of the property.


{{% include "/COM/notify-verses-polling.markdown" %}}
