---
type: post140
title:  FIFO Support
categories: XAP140NET, PRM
parent: fifo-overview.html
weight: 100
---

 

Supporting FIFO (First In, First Out) behavior for Entries is a critical requirement when building messaging systems or implementing master-worker patterns. Users should be able to get Entries in the same order in which they were written. GigaSpaces supports both non-ordered Entries and FIFO ordered Entries when performing space operations.

# Enabling FIFO

The default space behavior is non-FIFO. The reason is that FIFO support comes at a cost: the space needs to organize and maintain the entries internally in a more complex fashion to support FIFO queries, thus slowing down concurrent write operations. To enable FIFO operations users need to turn on FIFO support for classes which will participate in such operations. If a FIFO operation is performed on an entry whose class does not support FIFO, an exception will be thrown.

Setting FIFO support for a class can be done via the `FifoSupport` property on the `[SpaceClass]` attribute:


```csharp
[SpaceClass(FifoSupport = FifoSupport.Operation)]
public class Person
{
    ...
}
```

or when using gs.xml via the `fifo-support` attribute on the `class` element:


```xml
<gigaspaces-mapping>
    <class name="GigaSpaces.Examples.Person" fifo-support="operation">
        ...
    </class>
</gigaspaces-mapping>
```

The `FifoSupport` modes are:

1. `Off` - This class does not support FIFO. Attempting to execute FIFO operations will throw an exception.
2. `Operation` - FIFO support is enabled. An operation's FIFO behavior is determined by its modifiers or settings.
3. `All` - FIFO support is enabled. Any operation will be FIFO, overriding its other modifier or settings.
4. `Default` - FIFO support will be derived from the base class recursively.

# Space Operations

## Query Operations with FIFO

To execute Read/Take operations with FIFO, use `ReadModifiers.Fifo` / `TakeModifiers.Fifo` respectively. For example:


```csharp
Person result = space.Take(new Person(), transaction, timeout, TakeModifiers.FIFO);
```

If class `Person` is not set to support FIFO, an exception will be thrown.

## Read with FIFO

Reading without FIFO looks for a matching entry in the space and returns the first one found (if any), but not necessarily the one that was written first. Reading with FIFO guarantees that if there is more than one matching entry, the one that was written first will be returned (the write time is determined based on internal sequencing that ensures that a client receives Entries in FIFO order). Executing read with FIFO again with the same template will return the same result, assuming the entry hasn't been changed or deleted.

## Take with FIFO

Similar to read, executing take with FIFO guarantees that if there is more than one matching entry, the one that was written first will be taken. Executing take with FIFO again with the same template will return the next matching entry by order, and so on.

A take operation using FIFO might be critical when building a Point-to-Point (P2P) messaging system which needs to have message senders and receivers, where the receivers must get the sent messages in the exact order these have been sent by the senders.

## Batch operations with FIFO

When using the `ReadMultiple` or `TakeMultiple` with FIFO, the returned array will be ordered according to the order the entries were written to the space.

## Transactions and FIFO

FIFO Space operations can be performed using transactions. When transaction is rolled back, data is written back to the top of the FIFO queue and will be made available for next operation.

For example, if a transactional polling container consumes data and throws an exception which results into a rollback of transaction, same data will be processed by the polling container in the next attempt.

# Events

When registering for events, use `EventSessionConfig.Fifo` to instruct the space that events should be sent to the client in FIFO order. For example:


```csharp
// Create an event session configuration with FIFO:
EventSessionConfig sessionConfig = new EventSessionConfig();
sessionConfig.Fifo = true;
// Create a data event session using the configuration:
IDataEventSession session = space.CreateDataEventSession(sessionConfig);
// Subscribe to an event:
session.AddListener(new Person(), new EventHandler<SpaceDataEventArgs<Person>>(OnPersonEvent));
```

{{% note%}}
When using FIFO the client will use a single thread to invoke the listener callback method, so the events are both received and processed in FIFO order (i.e. if the client receives an event but the callback method haven't finished processing the previous event, the new event will be blocked until the previous one finishes). This is contrary to non-FIFO events, which are forwarded to the callback method as soon as they arrive, and thus might invoke the callback methods in parallel via multiple threads.

Registering for FIFO notifications on a class that was not FIFO-enabled will throw an exception.

{{%/note%}}

# Persistent Space

When a space includes FIFO-enabled classes and is defined as persistent, the persistent store (RDBMS) includes the relevant information that enables FIFO operations. This might impact the performance, since an additional index is created for each table storing the Entry Class instances.

# FIFO Grouping

The FIFO Grouping designed to allow efficient processing of events with partial ordering constraints.
Instead of maintaining a FIFO queue per class type, it lets you have a higher level of granularity by having FIFO queue maintained according to a specific value of a specific property.
For more details see [FIFO grouping](./fifo-grouping.html).

# Limitations

FIFO ordering is maintained per class per space. Therefore the following limitations should be observed:

- Order across class inheritance is not supported - When executing a FIFO operation which involves entries of different classes, the result set is not guaranteed to be in FIFO order (however, each subset of a specific class will maintain order).
- Order across cluster partitions is not supported - When executing a FIFO operation which involves more than one partition, the result set is not guaranteed to be in FIFO order (however, each subset of a specific partition will maintain order).
