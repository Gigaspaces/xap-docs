---
type: post100net
title:  Overview
categories: XAP100NET
weight: 100
parent: change-api-overview.html
---


{{% ssummary %}}{{% /ssummary %}}


{{%section%}}
{{%column width="80%" %}}
The `ISpaceProxy.Change` and the `ChangeSet` allows updating existing objects in Space, by specifying only the required change instead of passing the entire updated object. Thus reducing required network traffic between the client and the space, and the network traffic generated from replicating the changes between the space instances (e.g between the primary space instance and its backup).
{{%/column%}}
{{%column width="20%" %}}
{{%popup "/attachment_files/change-api.jpg"  "Change API"%}}
{{%/column%}}
{{%/section%}}

Moreover, using this API also can prevent the need of reading the existing object prior to the change operation because the change operation can specify how to change the existing property without knowing its current value. For instance, implementing atomic counters can be done by increasing a counter property of an integer property by some delta. Another example would be to add a value to a collection and so on.
The change API supports transactions in the same way the other space operation supports it.

# Example

The following example demonstrates how to increment the property `Count` of an object of type `WordCount` with id 'myID' by one.


```csharp
ISpaceProxy space = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(id, routing);
space.Change(idQuery, new ChangeSet().Increment("Count", 1));
```

# The Query Template

The change operation may receive any [query template](./querying-the-space.html) for matching a single or multiple objects that needs to be changed by the operation.

# The Change Set

The change operation requires a `ChangeSet` which described the changes that needs to be done once locating the object specified by the query template.
The `ChangeSet` contains a predefined set of operations that can be invoked to alter the object, the set may contain one or more changes that will be applied sequentially to the object.
Each specified change may operate on any level of properties of the specified object, this is defined by specifying the path to the property that needs to be changed where '.' in the path specifies
that this change is done on a nested property. For instance:


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

1. **First level property** - A path with no '.' character in it points to a first level property, If the property specified by this path is not part of the Object it will be treated as a dynamic property (see [Dynamic Properties](./poco-dynamic-properties.html)) if the object does not support dynamic properties, an exception will be generated.
2. **Nested property** - A path that contains '.' character is considered a path to a nested property, the location process of the final property which needs to be changed is done recursively by activating the properties, specified by the split of the path using the '.' character, one at a time until reaching the targeted end property.
3. **Nested Dictionary property** - A path that contains '.' may also point to keys inside a dictionary as, meaning the following path - 'Attributes.Color' will look for key named 'Color' if the property named 'Attribute' in the object is actually a dictionary. This affects nested properties as well.

The following demonstrates how the path works with a dictionary property instead of concrete properties:


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

In this case the key euro inside the dictionary behind the balance will be increased by 5.2.

## Available Change Set Operations


|Operation Name|Description|Semantics|
|:-------------|:----------|:--------|
|**Set**|sets a property value|sets value of the given property|
|**Unset**|unsets a property value|If the property is a fixed property, it will be set with null (null value for primitives), if it is a dynamic property it will be removed from the dynamic properties, note that for dynamic properties this is **not** equivalent to the set operation with `null` value|
|**Increment**|increase a numeric property by the given delta|This change may operate on a numeric property only (byte,short,int,long,float,double or their corresponding Boxed variation), if the property does not exists, the delta will be set as its initial state|
|**Decrement**|decrease a numeric property by the given delta|This change may operate on a numeric property only (byte,short,int,long,float,double or their corresponding Boxed variation), if the property does not exists, the -delta will be set as its initial state|
|**AddToCollection**|adds an item to a collection property|if the property do not exists an exception will be thrown|
|**AddRangeToCollection**|adds a list of items to a collection property|if the property do not exists an exception will be thrown|
|**RemoveFromCollection**|removes an item from a collection property|if the property do not exists an exception will be thrown|
|**SetInDictionary**|Sets a key value pair in a dictionary property|if the property do not exists an exception will be thrown|
|**RemoveFromDictionary**|removes a key and its associated value from a dictionary property|if the property do not exists an exception will be thrown|

# Using Change with the Embedded model

With the [embedded model](/sbp/modeling-your-data.html#Embedded vs. Non-Embedded Relationships), updating (as well adding or removing) a nested collection with large number of elements **must use the change API** since the default behavior would be to replicate the entire space object and its nested collection elements from the primary to the backup (or other replica primary copies when using the sync-replicate or the async-replicated cluster schema). The Change API reduces the CPU utilization at the primary side, reduce the serialization overhead and reduce the garbage collection activity both at the primary and backup. This improves the overall system stability significantly.

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

The `IChangeResult` contains the `NumberOfChangedEntries` which specifies how many objects where changed by this operation where 0 means none were changed. The `Results` property gives further details about the objects that were actually changes by providing a collection which gives details for each of the objects that were changed, such as their id and version after the change took affect. By default, in order to reduce network overhead, calling the Results will throw `NotSupportedException`. In order to get the more detailed result, the `ChangeModifiers.ReturnDetailedResults` should be passed to the `Change` operation.

{{% refer %}}For more information please refer to [Change API Advanced](./change-api-advanced.html){{% /refer %}}

# ChangeException

Upon any error a `ChangeException` will be thrown containing the following details:


```csharp
public class ChangeException
{
  public ICollection<Exception> Errors {get;}

  public ICollection<IFailedChangedEntryDetails> FailedChanges {get;}

  public ICollection<IChangedEntryDetails<object>> SuccessfulChanges {get;}

  public int NumOfSuccessfulChanges {get;}

}
```

The `NumSuccesfullChanges` property contains the number of entries that were successfully changed.
The `SuccesfullChanges` property contains details for objects that were successfully changed just like the `IChangeResult.Results` property. This property can only be used if the change operation was executed using the `ChangeModifiers.ReturnDetailedResults` modifier.
The `FailedChanges` property contains details for objects that failed being changed, the details contains information about id, version and the actual cause for failure.
The `Errors` property contains general failure reason for executing the change operation which do not apply to a specific object, such as not being able to access the space.

# Multiple Changes in One Operation

One may apply multiple changes in one `Change` operation by setting up multiple operation in the change set, this is done simply by chaining changes as follows:


```csharp
ISpaceProxy space = // ... obtain a space reference
IdQuery<MyObject> idQuery = new IdQuery<MyObject>(id, routing);
space.Change(idQuery, new ChangeSet().Increment("SomeIntProperty", 1)
                                     .Set("SomeStringProperty", "NewStringValue)
                                     .SetInDictionary("SomeNestedProperty.SomeDictionaryProperty", "MyKey", 2));
```

The changes will applied to the object sequentially (and atomically) keeping the order applied on the `ChangeSet`.

# Changing the Object's Lease

By default, the change operation will not modify the existing remaining lease of the changed entries. In order to change the lease, the new lease should be specified on the `ChangeSet` using the `lease` operation.


```csharp
ISpaceProxy space = // ... obtain a space reference
space.Change(idQuery, new ChangeSet().Lease(1000)...);
```

The lease can be changed as part of other changes applied to the object, as well as having the `ChangeSet` include only the lease modification without any property changes.
The lease time specified will override the existing lease with the new value relative to the current time while ignoring the current lease.
The above example will set the lease of the changed object to be one second from the time the change operation took affect.

# Change with Timeout

A timeout can be passed to the `change` operation, this timeout will only be used if any of the objects that needs to be changed is locked under a transaction which is not from the
current thread context. In that case, all objects which are not locked will be changed and the operation will block until either one of the two happens, which ever comes first:

1. the transaction lock is released - in that case the the change operation will be applied on the objects that were locked but now available.
2. the timeout elapsed - the change operation will return with an exception. Like all other failures, the exception will be a `ChangeException` which will contain the successful changes, and all the objects that remained locked when the timeout elapsed will be part of the `FailedChanges` property of the exception, each with a failure reason of `OperationTimeoutException`.

If there were no matching objects for the specified template, the operation will return immediately without waiting for the timeout to elapse. This is similar to the `(Read/Take)IfExists` operation semantic.


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

The `Change` operation has the same semantics as regular space `Update` operation when it comes to [Optimistic Locking](./transaction-optimistic-locking.html). It will increase the version of the changed object and the expected version can be specified in the id query when optimistic locking is needed.


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

{{% note %}}
In order to prevent constructor overload ambiguity, when using id query with version, the space routing property needs to be specified as well. If the object has no space routing then its space id property is the routing property and it should be used as shown in the previous example.
{{%/note%}}

# Change and Notifications

Change will be delivered as a regular update notification, with the state of the object after the change was applied.

# Change Modifiers

The following modifiers can be used with the change operation

1. **`ChangeModifiers.ReturnDetailedResults`** - Provide details change result containing more information about the objects that were changed, requires more network traffic.
2. **`ChangeModifiers.OneWay`** - Change is executed in one way mode, which means the operation will not wait for the change operation to reach the server, the result will always be null and
there is no guarantee whether the operation succeeded or not as this mode does not guarantee any exceptions upon failure. The only guarantee is that the operation was successfully written to the local network buffer.

3. **`ChangeModifiers.MemoryOnlySearch`** - Search for matching entries in cache memory only (do not use the underlying external data source). However, any changes done on the matches entries
will propagate to the underlying external data source.


# Considerations

1. When replicated to a gateway and a conflict occurs, change operation only supports the built in `abort` resolution as `override` in change case may result with an inconsistent state of the object.
2. The change operation is converted to a regular update when delegated to a data source.
