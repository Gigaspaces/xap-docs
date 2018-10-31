---
type: post140
title:  Change API
categories: XAP140NET, PRM
weight: 400
parent: the-gigaspace-interface-overview.html
---


The `ISpaceProxy.Change` and the `ChangeSet` allow updating existing objects in the Space, by specifying only the required change instead of passing the entire updated object. This reduces the required network traffic between the client and the Space, and the network traffic generated from replicating the changes between the Space instances (for example, between the primary Space instance and its backup).

Moreover, using this API also can eliminate the need to read the existing object prior to the change operation, because the change operation can specify how to change the existing property without knowing its current value. For instance, implementing atomic counters can be done by increasing a counter property of an integer property by some delta. Another example would be to add a value to a collection, and so on.
The change API supports transactions in the same way the other Space operation supports it.

# Example

The following example demonstrates how to increment the property 'Count' of an object of type 'WordCount' with ID 'myID' by one.


```csharp
ISpaceProxy space = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(id, routing);
space.Change(idQuery, new ChangeSet().Increment("Count", 1));
```

# The Query Template

The change operation can receive any [query template](./querying-the-space.html) for matching a single or multiple objects that have to be changed by the operation.

# The Change Set

The change operation requires a `ChangeSet` that describes the changes that have to be done after locating the object specified by the query template. The `ChangeSet` contains a predefined set of operations that can be invoked to alter the object, and the set may contain one or more changes that will be applied sequentially to the object.

Each specified change can operate on any level of properties of the specified object; this is defined by specifying the path to the property that needs to be changed where '.' in the path specifies that this change is done on a nested property. For example:


```csharp
[SpaceClass]
public class Account
{
  ...
  [SpaceID]
  Guid Id {get; set;}
  Balance Balance {get; set;}

}

public class Balance
{
  ...
  public double Euro {get; set;}
  public double UsDollar {get; set;}

}
```


```csharp
ISpaceProxy space = // ... obtain a space reference
Guid id = ...;
IdQuery<Account> idQuery = new IdQuery<Account>(id, routing);
space.Change(idQuery, new ChangeSet().Increment("Balance.Euro", 5.2D));
```

## Change Path Specification

Each operation in the change set acts on a specified string path. This path points to the property that needs to be changed and it has the following semantic:

1. **First-level property** - A path with no '.' character in it points to a first-level property, If the property specified by this path is not part of the object, it is treated as a dynamic property (see [Dynamic Properties](./poco-dynamic-properties.html)). If the object doesn't support dynamic properties, an exception is generated.
2. **Nested property** - A path that contains a '.' character is considered a path to a nested property. The location process of the final property that needs to be changed is done recursively by activating the properties, specified by the split of the path using the '.' character, one at a time until reaching the targeted end property.
3. **Nested Dictionary property** - A path that contains '.' may also point to keys inside a dictionary, meaning the following path - 'Attributes.Color' - will look for key named 'Color' if the property named 'Attribute' in the object is actually a dictionary. This also affects nested properties.

The following example shows how the path works with a dictionary property instead of concrete properties:


```csharp
[SpaceClass]
public class Account
{
  ...
  [SpaceID]
  Guid getId {get; set;}
  IDictionary<String, double> Balance {get; set;}
}
```


```csharp
ISpaceProxy space = // ... obtain a space reference
Guid id = ...;
IdQuery<Account> idQuery = new IdQuery<Account>(id, routing);
space.Change(idQuery, new ChangeSet().Increment("Balance.Euro", 5.2D));
```

In this case, the key euro inside the dictionary behind the balance is increased by 5.2.

## Available Change Set Operations


|Operation Name|Description|Semantics|
|:-------------|:----------|:--------|
|**Set**|Sets a property value.|Sets value of the given property.|
|**Unset**|Unsets a property value.|If the property is a fixed property, it will be set with null (null value for primitives). If it is a dynamic property, it will be removed from the dynamic properties. For dynamic properties, this is **not** equivalent to the set operation with `null` value.|
|**Increment**|Increases a numeric property by the given delta.|This change can operate on a numeric property only (byte, short, int, long, float, double or their corresponding Boxed variation). If the property does not exist, the delta is set as its initial state.|
|**Decrement**|Decreases a numeric property by the given delta.|This change can operate on a numeric property only (byte, short, int, long, float, double or their corresponding Boxed variation). If the property does not exist, the -delta is set as its initial state.|
|**AddToCollection**|Adds an item to a collection property.|If the property do not exist, an exception is thrown.|
|**AddRangeToCollection**|Adds a list of items to a collection property.|If the property do not exist, an exception is thrown.|
|**RemoveFromCollection**|Removes an item from a collection property.|If the property do not exist, an exception is thrown.|
|**SetInDictionary**|Sets a key value pair in a dictionary property.|If the property do not exist, an exception is thrown.|
|**RemoveFromDictionary**|Removes a key and its associated value from a dictionary property.|If the property do not exist, an exception is thrown.|

# Using Change with the Embedded Model

With the [embedded model](/sbp/modeling-your-data.html#embedded-vs-non-embedded-relationships), updating (as well adding or removing) a nested collection with large number of elements **must use the change API** because the default behavior is to replicate the entire Space object and its nested collection elements from the primary to the backup (or other replica primary copies when using the sync-replicated or the async-replicated cluster schema).

The Change API reduces the CPU utilization on the primary side, reduces the serialization overhead, and reduces the garbage collection activity on both the primary and backup. This significantly improves the overall system stability.

# Change Result

The change operations returns a `IChangeResult` object that provides information regarding the change operation affect.


```csharp
public interface IChangeResult<T>
{
  ICollection<IChangedEntryDetails<T>> Results { get;}
  int NumberOfChangedEntries { get; }
}
```


```csharp
ISpaceProxy space = // ... obtain a space reference
Guid id = ...;
IdQuery<Account> idQuery = new IdQuery<Account>(id, routing);
IChangeResult<Account> changeResult = space.Change(idQuery, new ChangeSet().Increment("Balance.Euro", 5.2D));
if (changeResult.NumberOfChangedEntries == 0)
{
  // ... handle no entry found for change
}
```

The `IChangeResult` contains the `NumberOfChangedEntries`, which specifies how many objects where changed by this operation (where 0 means none were changed). The `Results` property gives further details about the objects that were actually changes by providing a collection that gives details for each object that was changed, such as the ID and version after the change took effect.

By default, in order to reduce network overhead, calling the Results throws `NotSupportedException`. In order to get the more detailed result, the `ChangeModifiers.ReturnDetailedResults` should be passed to the `Change` operation.

{{% refer %}}For more information, refer to [Change API Advanced](./change-api-advanced.html).{{% /refer %}}

# ChangeException

Upon any error, a `ChangeException` is thrown containing the following details:


```csharp
public class ChangeException
{
  public ICollection<Exception> Errors {get;}

  public ICollection<IFailedChangedEntryDetails> FailedChanges {get;}

  public ICollection<IChangedEntryDetails<object>> SuccessfulChanges {get;}

  public int NumOfSuccessfulChanges {get;}

}
```

The `NumSuccesfullChanges` property contains the number of entries that were successfully changed. The `SuccesfullChanges` property contains details for objects that were successfully changed, just like the `IChangeResult.Results` property. This property can only be used if the change operation was executed using the `ChangeModifiers.ReturnDetailedResults` modifier.

The `FailedChanges` property contains details for objects that failed being changed, such as information about ID, version, and the actual cause for failure. The `Errors` property contains the general failure reason for executing the change operation that does not apply to a specific object, such as not being able to access the Space.

# Multiple Changes in a Single Operation

You can apply multiple changes in a single `Change` operation by setting up multiple operations in the change set. This is done by chaining the changes as follows:


```csharp
ISpaceProxy space = // ... obtain a space reference
IdQuery<MyObject> idQuery = new IdQuery<MyObject>(id, routing);
space.Change(idQuery, new ChangeSet().Increment("SomeIntProperty", 1)
                                     .Set("SomeStringProperty", "NewStringValue)
                                     .SetInDictionary("SomeNestedProperty.SomeDictionaryProperty", "MyKey", 2));
```

The changes are applied to the object sequentially (and atomically), keeping the order applied on the `ChangeSet`.

# Changing the Object's Lease

By default, the change operation doesn't modify the existing remaining lease of the changed entries. In order to change the lease, the new lease should be specified in the `ChangeSet` using the `lease` operation.


```csharp
ISpaceProxy space = // ... obtain a space reference
space.Change(idQuery, new ChangeSet().Lease(1000)...);
```

The lease can be changed as part of other changes applied to the object, as well as having the `ChangeSet` include only the lease modification without any property changes. The lease time specified overrides the existing lease with the new value, relative to the current time, while ignoring the current lease.

The above example sets the lease of the changed object to one second from the time the change operation took affect.

# Change with Timeout

A timeout can be passed to the `change` operation. This timeout is only used if any of the objects that have to be changed is locked under a transaction that is not from the current thread context. In this case, all objects that are not locked are changed, and the operation blocks until one of the following things happens (whichever comes first):

1. The transaction lock is released - in this case, the the change operation is applied on the objects that were locked but are now available.
2. The timeout elapsed - the change operation returns with an exception. Like all other failures, the exception is a `ChangeException` that contains the successful changes, and all the objects that remained locked when the timeout elapsed are part of the `FailedChanges` property of the exception, each with a failure reason of `OperationTimeoutException`.

If there are no matching objects for the specified template, the operation returns immediately without waiting for the timeout to elapse. This is similar to the `(Read/Take)IfExists` operation semantic.


```csharp
ISpaceProxy space = // ... obtain a space reference
Guid id = ...;
IdQuery<Account> idQuery = new IdQuery<Account>(id, routing);
long timeout = 1000; //1 seconds
try
{
  space.Change(idQuery, new ChangeSet().Increment("Balance.Euro", 5.2D), timeout);
}
catch(ChangeException e)
{
  if (e.FailedChanges.Count > 0))
  {
    foreach(IFailedChangedEntryDetails failedChangedEntry in e.FailedChanges)
    {
      if (id.Equals(failedChangedEntry.Id) && failedChangedEntry.Error is OperationTimeoutException)
      {
       //.. Indicate the object is still locked under a transaction, maybe retry the operation?
      }
    }
  }
}
```

# Change and Optimistic Locking

The `Change` operation has the same semantics as a regular Space `Update` operation when it comes to [Optimistic Locking](./transaction-optimistic-locking.html). It increases the version of the changed object, and the expected version can be specified in the ID query when optimistic locking is required.


```csharp
ISpaceProxy space = // ... obtain a space reference
Guid id = ...;
Object routing = id; // In our case the space routing property is the space id property.
int version = 2; // We only want to change the object if the object's version in the space is 2.
IdQuery<Account> idQuery = new IdQuery<Account>(id, routing, version);
try
{
  space.Change(idQuery, new ChangeSet().Increment("Balance.Euro", 5.2D), timeout);
}
catch(ChangeException e)
{
  if (e.FailedChanges.Count > 0))
  {
    foreach(IFailedChangedEntryDetails failedChangedEntry in e.FailedChanges)
    {
      if (id.Equals(failedChangedEntry.Id) && failedChangedEntry.Error is EntryVersionConflictException)
      {
        //.. Indicate optimistic locking failure, get the updated version for instance and maybe read updated object and retry?
        int updatedVersion = failedChangedEntry.Version;
      }
    }
  }
}
```

{{% note "Note"%}}
In order to prevent constructor overload ambiguity, when using an ID query with version, the Space routing property must also be specified. If the object has no Space routing, its space ID property is the routing property and it should be used, as shown in the previous example.
{{%/note%}}

# Change and Notifications

Change are delivered as a regular update notification, with the state of the object after the change was applied.

# Change Modifiers

The following modifiers can be used with the change operation:

1. `ChangeModifiers.ReturnDetailedResults` - Provide details of the change result, containing more information about the objects that were changed, requires more network traffic.
1. `ChangeModifiers.OneWay` - Execute change in one-way mode, which means the operation doesn't wait for the change operation to reach the server. The result is always null and there is no guarantee whether the operation succeeded or not, as this mode doesn't guarantee any exceptions upon failure. The only guarantee is that the operation was successfully written to the local network buffer.
1. `ChangeModifiers.MemoryOnlySearch` - Search for matching entries in cache memory only (do not use the underlying external data source). However, any changes done on the matches entries are propagated to the underlying external data source.


# Considerations

1. When replicated to a gateway and a conflict occurs, change operation only supports the built-in `abort` resolution, as `override` in change case can result in an inconsistent state of the object.
2. The change operation is converted to a regular update when delegated to a data source.
