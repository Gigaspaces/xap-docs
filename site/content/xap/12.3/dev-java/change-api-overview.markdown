---
type: post123
title:  Change API
categories: XAP123, OSS
weight: 400
parent: the-gigaspace-interface-overview.html
---




The [GigaSpace.change]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html) and the [ChangeSet]({{% api-javadoc %}}/index.html?com/gigaspaces/client/ChangeSet.html) allows updating existing objects in Space, by specifying only the required change instead of passing the entire updated object.


### Additional Resources

{{%youtube "k2zGdUi_ntU"  "Custom Change API"%}}


{{%imagertext "/attachment_files/change-api.jpg"  "Change API"%}}
The `GigaSpace.change` and the `ChangeSet`  allow updating existing objects in the Space, by specifying only the required change instead of passing the entire updated object.
This reduces the required network traffic between the client and the Space, and the network traffic generated from replicating changes between Space instances (for example, between the primary Space instance and its backup).
{{%/imagertext%}}



Moreover, using this API also can prevent having to read the existing object prior to the change operation, because the change operation can specify how to change the existing property without knowing its current value. For instance, implementing atomic [Counters](./the-space-counters.html) can be done by increasing a counter property of an integer property by some delta. Another example is adding a value to a collection.
The change API supports [transactions](./transaction-overview.html) in the same way the other Space operation supports it, using a transactional `GigaSpace` instance.

{{%anchor change%}}

# Example

The following example demonstrates how to increment by one the `count` property of an object of type `WordCount`, with ID `myID`.


```java
GigaSpace space = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(WordCount.class, id, routing);
space.change(idQuery, new ChangeSet().increment("count", 1));
```

# Query Template

The change operation can receive any [query template](./querying-the-space.html) for matching a single object or multiple objects that need to be changed by the operation.

# Change Set

The change operation requires a [ChangeSet]({{% api-javadoc %}}/index.html?com/gigaspaces/client/ChangeSet.html) that describes the changes that have to be made after locating the object specified by the query template.
The [ChangeSet]({{% api-javadoc %}}/index.html?com/gigaspaces/client/ChangeSet.html) contains a predefined set of operations that can be invoked to alter the object. The set can contain one or more changes that will be applied sequentially to the object.
Each specified change can operate on any level of properties of the specified object. This is defined by specifying the path to the property that needs to be changed, where '.' in the path specifies
that this change is done on a nested property. For instance:


```java
@SpaceClass
public class Account
{
  ...
  @SpaceId
  Uuid getId();
  Balance getBalance();
  void setBalance(Balance balance){...}
}

public class Balance
{
  private double euro;
  private double usDollar;
  ...
  public double getEuro();
  public void setEuro(double euro) { this.euro = euro; }
  public double getUsDollar();
  public void setUsDollar(double usDollar) { this.usDollar = usDollar; }
}
```


```java
GigaSpace space = // ... obtain a space reference
Uuid id = ...;
IdQuery<Account> idQuery = new IdQuery<Account>(Account.class, id, routing);
space.change(idQuery, new ChangeSet().increment("balance.euro", 5.2D));
```

## Specifying the Path

Each operation in the change set acts on a specified string path. This path points to the property that needs to be changed, and has the following semantic:

1. **First level property** - A path with no '.' character in it points to a first-level property. If the property specified by this path is not part of the Object, it is treated as a dynamic property (see [Dynamic Properties](./dynamic-properties.html)). If the object doesn't support dynamic properties, an exception is generated.
1. **Nested property** - A path that contains a '.' character is considered a path to a nested property. The location process of the final property that needs to be changed is done recursively by activating the properties, specified by the split of the path using the '.' character, one at a time until reaching the targeted end property.
1. **Nested Map property** - A path that contains a '.' character may also point to keys inside a map, meaning the following path - 'attributes.color' looks for a key named 'color' if the property named 'attribute' in the object is actually a map. This affects nested properties as well.

The following demonstrates how the path works with a map property instead of concrete properties:


```java
@SpaceClass
public class Account
{
  ...
  @SpaceId
  Uuid getId();
  Map<String, double> getBalance();
  void setBalance(Map<String, double>  balance){...}
}
```


```java
GigaSpace space = // ... obtain a space reference
Uuid id = ...;
IdQuery<Account> idQuery = new IdQuery<Account>(Account.class, id, routing);
space.change(idQuery, new ChangeSet().increment("balance.euro", 5.2D));
```

In this case, the key `euro` inside the map behind the balance will be increased by 5.2.

## Available Operations


|Operation Name|Description|Semantics|
|:-------------|:----------|:--------|
|**Set**|Sets a property value.|Sets the value of the given property.|
|**Unset**|Unsets a property value.|If the property is fixed, it is set to null (null value for primitives). If the property is dynamic, it is removed from the dynamic properties. For dynamic properties, this operation is **not** equivalent to the set operation with a `null` value.|
|**Increment**|Increases a numeric property by the given delta.|This operation can only be performed on a numeric property (`byte`, `short`, `int`, `long`, `float`, `double` or the corresponding Boxed variation). If the property does not exist, the +delta is set as the initial state.|
|**Decrement**|Decreases a numeric property by the given delta.|This operation can only be performed on a numeric property (`byte`, `short`, `int`, `long`, `float`, `double` or the corresponding Boxed variation). If the property does not exist, the -delta is set as the initial state.|
|**AddToCollection**|Adds an item to a collection property.|The item is added to the collection by applying the `Collection.add` operation with the given item on the collection behind the property. if the property do not exist, an exception is thrown.|
|**AddAllToCollection**|Adds a list of items to a collection property.|The items are added to the collection by applying the `Collection.addAll` operation with the given items on the collection behind the property. If the property does not exist, an exception is thrown.|
|**RemoveFromCollection**|Removes an item from a collection property.|The item is removed from the collection by applying the `Collection.remove` operation with the given item on the collection behind the property. If the property does not exist, an exception is thrown.|
|**PutInMap**|Puts a key value pair in a map property.|The key and value are put into a map by applying the `Map.put` operation with the given key and value on the map behind the property. If the property does not exist, an exception is thrown.|
|**RemoveFromMap**|Removes a key and its associated value from a map property.|The key is removed from a map by applying the `Map.remove` operation with the given key on the map behind the property. If the property does not exist, an exception is thrown.|

# Change API with the Embedded Model

With the [embedded model](/sbp/modeling-your-data.html#Embedded vs. Non-Embedded Relationships), updating (as well adding or removing) a nested collection with large number of elements **must use the Change API** because the default behavior is to replicate the entire Space object and its nested collection elements from the primary to the backup (or other replica primary copies when using the sync-replicate or the async-replicated cluster schema). The Change API reduces the CPU utilization on the primary side, the serialization overhead, and the garbage collection activity on both the primary and backup instances. This significantly improves the overall system stability.

# Change Result

The change operations returns a [ChangeResult]({{% api-javadoc %}}/index.html?com/gigaspaces/client/ChangeResult.html) object that provides information regarding the effect of the change operation.


```java
public interface ChangeResult<T>
        extends Serializable
{
    /**
     * Returns a collection of {@link ChangedEntryDetails} of the changed
     * entries. <note>This is only supported if the
     * {@link ChangeModifiers#RETURN_DETAILED_RESULTS} modifier was used,
     * otherwise this method will throw unsupported operation exception.
     *
     * @throws UnsupportedOperationException if the corresponding change
     *             operation was not used with the
     *             {@link ChangeModifiers#RETURN_DETAILED_RESULTS} modifier.
     */
    Collection<ChangedEntryDetails<T>> getResults();

    /**
     * Returns the number of changed entries
     */
    int getNumberOfChangedEntries();
}
```


```java
GigaSpace space = // ... obtain a space reference
Uuid id = ...;
IdQuery<Account> idQuery = new IdQuery<Account>(Account.class, id, routing);
ChangeResult<Account> changeResult = space.change(idQuery, new ChangeSet().increment("balance.euro", 5.2D));
if (changeResult.getNumberOfChangedEntries() == 0)
{
  // ... handle no entry found for change
}
```

The `ChangeResult` contains the `getNumberOfChangedEntries`, which specifies how many objects were changed by this operation (where 0 means none were changed). The `getResults` property gives further details about the objects that were actually changed by providing a collection that gives details for each of the objects that were changed, such as their ID and version after the change took effect. By default, in order to reduce network overhead, calling  `getResults` will throw `UnsupportedOperationException`. In order to get the more detailed result, the `ChangeModifiers.RETURN_DETAILED_RESULTS` should be passed to the `change` operation.


{{% refer %}}For more information, refer to [Advanced Implementations](./change-api-advanced.html). {{% /refer %}}

# ChangeException

Upon any error, a `ChangeException` is thrown containing the following details:


```java
public class ChangeException extends InvalidDataAccessResourceUsageException
{

    /**
     * Returns the number of successfully changes.
     */
    public int getNumSuccesfullChanges()

    /**
     * Returns the successfully done changes.
     */
    public Collection<ChangedEntryDetails<?>> getSuccesfullChanges()

    /**
     * Returns the entries that failed to change result.
     */
    public Collection<FailedChangedEntryDetails> getFailedChanges()

    /**
     * Returns the failed changes.
     */
    public Collection<Throwable> getErrors()

}
```

The `getNumSuccesfullChanges` property contains the number of entries that were successfully changed.

The `getSuccesfullChanges` property contains details about objects that were successfully changed, just like the `ChangeResult.getResults` property. This property can only be used if the change operation was executed using the `ChangeModifiers.RETURN_DETAILED_RESULTS` modifier.

The `getFailedChanges` property contains details about objects for whom the change operation failed. These details include information about ID, version, and the actual cause for failure.

The `getErrors` property contains the general reasons for failed change operations that do not apply to a specific object, such as not being able to access the Space.

{{%anchor changeMultiple%}}

# Multiple Changes in a Single Operation

Multiple changes can be applied in one `change` operation by setting up multiple operations in the change set. Do this by chaining the changes as follows:


```java
GigaSpace space = // ... obtain a space reference
IdQuery<MyPojo> idQuery = new IdQuery<MyPojo>(MyPojo.class, id, routing);
space.change(idQuery, new ChangeSet().increment("someIntProperty", 1)
                                     .set("someStringProperty", "newStringValue)
                                     .putInMap("someNestedProperty.someMapProperty", "myKey", 2));
```

The changes are applied to the object sequentially (and atomically), keeping the order applied in the `ChangeSet`.

# Changing the Object's Lease

By default, the change operation doesn't modify the existing remaining lease of the changed entries. In order to change the lease, a new lease should be specified in the `ChangeSet` using the `lease` operation.


```java
GigaSpace space = // ... obtain a space reference
space.change(idQuery, new ChangeSet().lease(1000)...);
```

The lease can be changed as part of other changes applied to the object, or the `ChangeSet` can include only the lease modification without any property changes.

The lease time specified will override the existing lease with the new value, relative to the current time, while ignoring the current lease.

The above example sets the lease of the changed object to one second from the time the change operation takes effect.

# Change with Timeout

A timeout can be passed to the `change` operation. This timeout will only be used if any of the objects to be changed is locked under a transaction that is not from the current thread context. In this case, all objects that are not locked are  changed, and the operation is blocked until one of the following two events happens:

- The transaction lock is released - in this case, the change operation is applied on the objects that were previously locked but are now available.
- A timeout occurs - in this case, the change operation returns with an exception. Like all other failures, the exception is a `ChangeException` that contains the successful changes. All the objects that were still locked when the timeout period elapsed are part of the `getFailedChanges` property of the exception, each with a failure reason of `UpdateOperationTimeoutException`.

If there are no matching objects for the specified template/Query, the operation returns immediately without waiting for the timeout to elapse. This is similar to the `(read/take)IfExists` operation semantic.


```java
GigaSpace space = // ... obtain a space reference
Uuid id = ...;
IdQuery<Account> idQuery = new IdQuery<Account>(Account.class, id, routing);
long timeout = 1000; //1 seconds
try
{
  space.change(idQuery, new ChangeSet().increment("balance.euro", 5.2D), timeout);
}
catch(ChangeException e)
{
  if (!e.getFailedChanges().isEmpty())
  {
    for(FailedChangedEntryDetails failedChangedEntry : e.getFailedChanges())
    {
      if (id.equals(failedChangedEntry.getId()) && failedChangedEntry.getCause() instanceof UpdateOperationTimeoutException)
      {
       //.. Indicate the object is still locked under a transaction, maybe retry the operation?
      }
    }
  }
}
```

# Optimistic Locking

The `change` operation has the same semantics as a regular Space `update` operation regarding [Optimistic Locking](./transaction-optimistic-locking.html). It increases the version of the changed object, and the expected version can be specified in the ID query when optimistic locking is needed.


```java
GigaSpace space = // ... obtain a space reference
Uuid id = ...;
Object routing = id; // In our case the space routing property is the space id property.
int version = 2; // We only want to change the object if the object's version in the space is 2.
IdQuery<Account> idQuery = new IdQuery<Account>(Account.class, id, routing, version);
try
{
  space.change(idQuery, new ChangeSet().increment("balance.euro", 5.2D), timeout);
}
catch(ChangeException e)
{
  if (!e.getFailedChanges().isEmpty())
  {
    for(FailedChangedEntryDetails failedChangedEntry : e.getFailedChanges())
    {
      if (id.equals(failedChangedEntry .getId()) failedChangedEntry .getCause() instanceof SpaceOptimisticLockingFailureException)
      {
        //.. Indicate optimistic locking failure, get the updated version for instance and maybe read updated object and retry?
        int updatedVersion = failedChangedEntry.getVersion();
      }
    }
  }
}
```

{{% note "Info"%}}
In order to prevent constructor overload ambiguity, when using an ID query with version, the Space routing property must also be specified. If the object has no Space routing, then its Space ID property is the routing property and it should be used, as shown in the previous example.
{{%/note%}}

# Notifications

Change is delivered as a regular update notification, with the state of the object after the change was applied.

# Modifiers

The following modifiers can be used with the change operation:

- `ChangeModifiers.RETURN_DETAILED_RESULTS` - Provides details of the change result, containing more information about the objects that were changed. Requires more network traffic.
- `ChangeModifiers.ONE_WAY` - Change is executed in one-way mode, which means the operation doesn't wait for the change operation to reach the server. The result is always  null and
there is no guarantee that the change operation is successful, as this mode doesn't guarantee any exceptions upon failure. The only guarantee is that the operation is successfully written to the local network buffer.
- `ChangeModifiers.MEMORY_ONLY_SEARCH` - Searches for matching entries in cache memory only (doesn't use the underlying external data source). However, any changes done on the matching entries
are propagated to the underlying external data source.

{{%anchor asynchronousChange%}}

# Asynchronous Change
{{%section%}}
{{%column width="65%" %}}
The `change` operation has also an asynchronous API, in which the operation is dispatched to the server and the result is returned asynchronously via a listener or a future.
{{%/column%}}
{{%column width="30%" %}}
![changeAsync-api.jpg](/attachment_files/changeAsync-api.jpg)
{{%/column%}}
{{%/section%}}

This operation behaves exactly like the synchronous `change` except for the asynchronous result, and it follows Java standard asynchronous semantics.


```java
//Using future to get the result
GigaSpace space = // ... obtain a space reference
Uuid id = ...;
IdQuery<Account> idQuery = new IdQuery<Account>(Account.class, id, routing);
Future<ChangeResult<Account>> future = space.asyncChange(idQuery, new ChangeSet().increment("balance.euro", 5.2D));
// ... do some other stuff
ChangeResult<Account> changeResult = future.get();
```


```java
//Using a listener to handle the result
GigaSpace space = // ... obtain a space reference
Uuid id = ...;
IdQuery<Account> idQuery = new IdQuery<Account>(Account.class, id, routing);
AsyncFutureListener<ChangeResult<Account>> myListener = new AsyncFutureListener<ChangeResult<Account>>()
  {
    void onResult(AsyncResult<Account> result)
    {
      if (result.getException() != null)
      {
        // ... Handle exception
      }
      else
      {
        ChangeResult<Account> changeResult = result.getResult();
        // ... handle result
      }
    }
  }
space.asyncChange(idQuery, new ChangeSet().increment("balance.euro", 5.2D), myListener);
```

# SpaceSynchronizationEndpoint

A SpaceSynchronizationEndpoint implementation can support change operations, and make use of lighter replication between the Space and the mirror.
For more information on how to implement this, refer to the [Advanced Implementations](./change-api-advanced.html) topic.

# Replication Filter

Change passes through the [Replication Filter](../admin/cluster-replication-filters.html) like other operations, and can be discarded on the replication level, for example.

{{% refer %}}For more information on how to handle change in replication filters, refer to the [Advanced Implementations](./change-api-advanced.html) topic.{{% /refer %}}

# Add and Get Operations

A common usage pattern is to increment a numeric property of a specific entry, and then to need the updated value after the increment was applied.
Using the `addAndGet` operation, you can meet this need using a single method call to get atomic add and get operation semantics.

The following is an example of incrementing a property called `counter` inside an entry of type `WordCount`:


```java
GigaSpace space = // ... obtain a space reference
Uuid id = ...;
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(WordCount.class, id, routing);
Integer newCounter = ChangeExtension.addAndGet(space, idQuery, "counter", 1);
```

You should use the primitive wrapper types, as the operation semantic is to return null if there is no object matching the provided ID query.

{{% note "Info"%}}You can use `import static org.openspaces.extensions.ChangeExtension.` so that you don't have to prefix the call with `ChangeExtension'.{{% /note %}}

# Considerations

1. If replicated to a gateway and a conflict occurs, the change operation only supports the built-in `abort` resolution as `override` in change case may result with an inconsistent state of the object.
1. The change operation is converted to a regular update when delegated to a synchronous data source.


# Limitations

`rownum`   is not supported with the Changeset SQLQuery operation.