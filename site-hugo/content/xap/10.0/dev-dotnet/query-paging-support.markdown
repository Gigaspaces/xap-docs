---
type: post100
title:  Paging Support
categories: XAP100NET
parent: querying-the-space.html
weight: 600
---

{{% ssummary %}}{{% /ssummary %}}

The [ISpaceIterator]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_ISpaceIterator_1.htm) with the [SpaceIteratorConfig]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_SpaceIteratorConfig.htm)  provides the ability to exhaustively read through all of the objects from the space that match one or more SQLQuery/templates.




There are scenarios where the conventional read operation that returns a single space object does not fit and there is a need to return a collection of entries from the space. Generally, an iterator should be used in cases where returning all the entries in one result with the `ReadMultiple` operation will consume too much memory on the client or introduce too much latency before the first space object could be processed.

The iterator constructs a match set (a collection of space objects instances) that incrementally returns the necessary objects in chunks or pages. The `ISpaceIterator` constructs a proxy object that can be used to access a match set created by a space. The `ISpaceIterator` will initially contain some population of objects specified by the operation that created it. These objects can be retrieved by calling `foreach` on the iterator.

Simple usage example for the `SpaceIteratorConfig` with the `ISpaceIterator`:


```csharp
SqlQuery<Employee> query = new SqlQuery<Employee>("Name='John'");

SpaceIteratorConfig config = new SpaceIteratorConfig();
config.BufferSize = 5000;
config.IteratorScope = IteratorScope.ExistingEntries;

ISpaceIterator<Employee> iter = spaceProxy.GetSpaceIterator<Employee> (query, config);

while(iter.MoveNext())
{
    Employee employee = iter.Current;
    Console.WriteLine("Got Employee: " + employee);
}
```


# The Iterator Configuration


The iterator can iterate on objects **currently** in the space, **future** entries or **both**. By default it will only iterate on **future** objects in the Space (objects that match the given template(s)). Use iteratorScope(IteratorScope) to set the iterator's scope.

Lease for the iterator can be controlled using `LeaseTime`. A leased iterator which expires is considered as invalidated. A canceled iterator is an exhausted iterator and will have no more entities added to it.

If there is a possibility that an iterator may become invalidated, it must be leased. If there is no possibility that the iterator will become invalidated, implementations should not lease it. If there is no further interest an iterator may be canceled.

An active lease on an iterator serves as a hint to the space that the client is still interested in matching objects, and as a hint to the client that the iterator is still functioning. There are cases, however, where this **may not** be possible in particular, it is not expected that iteration will maintain across crashes. If the lease expires or is canceled, the iterator is invalidated. Clients should not assume that the resources associated with a leased ISpaceIterator will be freed if the ISpaceIterator reaches the exhausted state, and should instead cancel the lease.

The maximum number of objects to pull from the space can be controlled using `BufferSize` and defaults to `100`.

# The IteratorScope

The [IteratorScope]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_IteratorScope.htm) determines the scope of a ISpaceIterator. Here are the supported options:

- `ExistingEntries` - Indicates that the iterator will process entries currently in the space, and ignores future changes.
- `ExistingAndFutureEntries` - Indicates that the iterator will process both entries currently in the space and future changes.
- `FutureEntries` - Indicates that the iterator will ignore entries currently in the space, and process future changes.



## Using ISpaceIterator with SQLQuery

When using the `ISpaceIterator` with SQLQuery only [simple SQL queries](./query-sql.html#Simple Queries - Supported and Non-Supported Operators) are supported:
    (field1 < value1) AND (field2 > value2) AND (field3 == values3)...
The following operators **are not supported** when using `ISpaceIterator`:

- `NOT LIKE`
- `OR`
- `IN`
- `GROUP BY`
- `ORDER BY`


## Initialization

When a `ISpaceIterator` is created, a match set is formulated. The match set initially contains all of the objects within the space that match one or more of the collection templates and are not locked by conflicting transactions (unless using the `Future` IteratorScope mode, i.e., no initial contents). Each element of the matched set will be returned at most once.


## Notifications

ISpaceIterator register each of the templates in the templates collection for notification. If a matching object was written to the space after the ISpaceIterator was created, the object will be added to the result set. An object that was locked under a conflicting transaction before or after the ISpaceIterator was created and the lock was released before the iteration was complete will also be added to the result set.


## Iterator Lease

In most cases, the iterator will be leased and the lease proxy object can be obtained by calling getting the `LeaseTime`. Cancelling or letting the lease expire will destroy the iterator; thus no notifications from here on will be accounted for, and all subsequent calls to `foreach` will return `null`. If there is a lease associated with the iterator, clients should not assume that completing the iteration will destroy it and should instead call cancel or let the lease expire when the end of the iteration is reached. The lease can be renewed by setting `LeaseTime` for an additional period of time. This duration is not added to the original lease, but is used to determine a new expiration time for the existing lease. If a lease has expired or has been canceled, a renewal is not allowed.

## Transactions

Iterating through the matched set does not lock the object. Objects that are under transaction and match the specified template/SQLQuery will not be included as part of the matched set.



