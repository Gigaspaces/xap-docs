---
type: post121
title:  Transaction support
categories: XAP121NET, PRM
parent: polling-container-overview.html
weight: 300
canonical: auto
---






Both the receive operation, and the actual event action can be configured to be performed under a transaction. Transaction support is required for example, when an exception occurs in the event listener, and the receive operation needs to be to rolled back (and the actual data event is returned to the space). Adding transaction support to the polling container is very simple. It is done by setting the `TransactionType` property. There are two transaction types: Distributed and Manual.

- Distributed transaction - an embedded distributed transaction manager will be created and it will be used for creating transaction (Only one transaction manager will be created per AppDomain).
- Manual transaction - transactions will be created by the transaction manager that is stored in the `TransactionManager` property. By default no transaction manager is stored and therefore, no transaction will be used. For example:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven]
[TransactionalEvent(TransactionType = TransactionType.Distributed)]
public class SimpleListener
{
 ...
}
```

{{% /tab %}}

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
ISpaceProxy spaceProxy = // either create the SpaceProxy or obtain a reference to it

PollingEventListenerContainer<Data> pollingEventListenerContainer = new PollingEventListenerContainer<Data>(spaceProxy);
pollingEventListenerContainer.TransactionType = TransactionType.Distributed;
```

{{% /tab %}}

{{% /tabs %}}

When using transactions with the polling container, special care should be taken with timeout values. Transactions started by the polling container can have a timeout value associated with them (if this is not set, it defaults to the default timeout value of the transaction manager). If setting a specific timeout value, make sure the timeout value is higher than the timeout value for blocking operations, and includes the expected execution time of the associated listener.

Here is an example how timeout value can be set with the polling container:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven]
[TransactionalEvent(TransactionType = TransactionType.Distributed, TransactionTimeout = 1000)]
public class SimpleListener
{
 ...
}
```

{{% /tab %}}

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
PollingEventListenerContainer<Data> pollingEventListenerContainer = // create or obtain a reference to a polling container

pollingEventListenerContainer.TransactionTimeout = 1000;
```

{{% /tab %}}

{{% /tabs %}}

It is possible to receive a reference to the on going transaction as part of the event handling method, if for instance, the event handling is doing some extra space operations which should be executed under the same transaction context and rolled back upon failure. This is done be adding a transaction parameter to the event handler method. For example:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven]
[TransactionalEvent(TransactionType = TransactionType.Distributed)]
public class SimpleListener
{
 ...

    [DataEventHandler]
    public Data ProcessData(Data ev, ISpaceProxy spaceProxy, ITransaction transaction)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
ISpaceProxy spaceProxy = // either create the SpaceProxy or obtain a reference to it

PollingEventListenerContainer<Data> pollingEventListenerContainer = new PollingEventListenerContainer<Data>(spaceProxy);
pollingEventListenerContainer.TransactionType = TransactionType.Distributed;
pollingEventListenerContainer.DataEventArrived += new DelegateDataEventArrivedAdapter<Data,Data>(ProcessData).WriteBackDataEventHandler;
```

{{% /tab %}}

{{% /tabs %}}

{{% refer %}}The order of parameters of the event handling method is strict, please refer to [Dynamic Data Event Handler Adapter](./event-listener-container.html#eventhandler-adapter) for more information about it.{{% /refer %}}

