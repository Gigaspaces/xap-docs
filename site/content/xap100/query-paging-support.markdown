---
type: post100
title:  Paging Support
categories: XAP100
parent: querying-the-space.html
weight: 700
---

{{% ssummary %}}{{% /ssummary %}}


The [IteratorBuilder](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/core/IteratorBuilder.html) with the [GSIterator](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?com/j_spaces/core/client/GSIterator.html)  provides the ability to exhaustively read through all of the objects from the space that match one or more SQLQuery/templates.

There are scenarios where the conventional read operation that returns a single space object does not fit and there is a need to return a collection of entries from the space. Generally, an iterator should be used in cases where returning all the entries in one result with the `readMultiple` operation will consume too much memory on the client or introduce too much latency before the first space object could be processed.

The iterator constructs a match set (a collection of space objects instances) that incrementally returns the necessary objects in chunks or pages. The `GSIterator` constructs a proxy object that can be used to access a match set created by a space. The `GSIterator` will initially contain some population of objects specified by the operation that created it. These objects can be retrieved by calling the `next` method. A successful call to `next` will remove the returned object from the match set. Match sets can end up in one of two terminal states: `exhausted` or `invalidated`.

Simple usage example for the `IteratorBuilder` with the `GSIterator`:


```java
GigaSpace gigaspace = new GigaSpaceConfigurer( new UrlSpaceConfigurer("jini://*/*/mySpace")).gigaSpace();

SQLQuery<MySpaceClass> query1 = new SQLQuery<MySpaceClass>(MySpaceClass.class,"fName like 'f%'");
SQLQuery<MySpaceClass> query2 = new SQLQuery<MySpaceClass>(MySpaceClass.class,"lName like 'l%'");

IteratorBuilder iteratorBuilder = new IteratorBuilder(gigaspace)
	.addTemplate(query1)
	.addTemplate(query2)
	.bufferSize(100) // Limit of the number of objects to store for each iteration.
	.iteratorScope(IteratorScope.CURRENT_AND_FUTURE) ;
// Indicates that this iterator will be first pre-filled with existing matching objects anf future matching objects,
// otherwise it will start iterating only on newly arriving objects to the space.

GSIterator gsIterator = iteratorBuilder.iterate();
int count = 0;

for (;;)
{
        try
        {
	    MySpaceClass o = (MySpaceClass)gsIterator.next(60000);
	    System.out.println((count ++ ) + " " + o);
            } catch (NoSuchElementException e) {
             // will be thrown if there is no matching object and 60000 ms gone by
            }
}
```

# The IteratorBuilder

The [IteratorBuilder](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/core/IteratorBuilder.html) is a utility builder class for the GSIterator. It allows to use method chaining for simple configuration of an iterator and then call iterate() to get the actual iterator.

By default, when no template is added (using `addTemplate`), a `null` template will be used to iterate over all the content of the Space.

The iterator can iterate on objects **currently** in the space, **future** entries or **both**. By default it will only iterate on **future** objects in the Space (objects that match the given template(s)). Use iteratorScope(IteratorScope) to set the iterator's scope.

Lease for the iterator can be controlled using `leaseDuration(long)`. A leased iterator which expires is considered as invalidated. A canceled iterator is an exhausted iterator and will have no more entities added to it. Calling next on an iterator with either state always returns `null` or it may throw one of the allowed exceptions. In particular `next(timeout)` may throw `NoSuchObjectException` to indicate that no object has been found during the allowed timeout. There is no guarantee that once `next(timeout)` throws a `NoSuchObjectException`, or next returns `null`, all future calls on that instance will do the same.

If there is a possibility that an iterator may become invalidated, it must be leased. If there is no possibility that the iterator will become invalidated, implementations should not lease it (i.e. use `Lease.FOREVER`). If there is no further interest an iterator may be canceled.

An active lease on an iterator serves as a hint to the space that the client is still interested in matching objects, and as a hint to the client that the iterator is still functioning. There are cases, however, where this **may not** be possible in particular, it is not expected that iteration will maintain across crashes. If the lease expires or is canceled, the iterator is invalidated. Clients should not assume that the resources associated with a leased GSIterator will be freed if the GSIterator reaches the exhausted state, and should instead cancel the lease.

The maximum number of objects to pull from the space can be controlled using `bufferSize(int)` and defaults to `100`.

# The IteratorScope

The [IteratorScope](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?com/gigaspaces/client/iterator/IteratorScope.html) determines the scope of a GSIterator. Here are the supported options:

- `CURRENT` - Indicates that the iterator will process entries currently in the space, and ignores future changes.
- `CURRENT_AND_FUTURE` - Indicates that the iterator will process both entries currently in the space and future changes.
- `FUTURE` - Indicates that the iterator will ignore entries currently in the space, and process future changes.

# The GSIterator

The [GSIterator](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?com/j_spaces/core/client/GSIterator.html) will initially contain some population of objects. These objects can be retrieved by calling `next` method. A successful call to `next` method will remove the returned object from the iteration result set. An iterator can end up in one of two terminal states, `invalidated` or `exhausted`.

A leased iterator which expires is considered as invalidated. A canceled iterator is an exhausted iterator and will have no more entities added to it. Calling next on an iterator with either state always returns null or it may throw one of the allowed exceptions. In particular next(timeout) may throw NoSuchObjectException to indicate that no object has been found during the allowed timeout. There is no guarantee that once next(timeout) throws a NoSuchObjectException, or next returns null, all future calls on that instance will do the same.

Between the time an iterator is created and the time it reaches a terminal state, objects may be added by the space. However, an object that is removed by a next call may be added back to the iterator if its uniqueness is equivalent. The space may also update or remove objects that haven't yet been returned by a `next` call, and are not part of the buffered set.


## Using GSIterator with SQLQuery

When using the `GSIterator` with SQLQuery only [simple SQL queries](./query-sql.html#Simple Queries - Supported and Non-Supported Operators) are supported:
    (field1 < value1) AND (field2 > value2) AND (field3 == values3)...
The following operators **are not supported** when using `GSIterator`:

- `NOT LIKE`
- `OR`
- `IN`
- `GROUP BY`
- `ORDER BY`


## Initialization

When a `GSIterator` is created, a match set is formulated. The match set initially contains all of the objects within the space that match one or more of the collection templates and are not locked by conflicting transactions (unless using the `FUTURE` IteratorScope mode, i.e., no initial contents). Each element of the matched set will be returned at most once.

## hasNext(), next() and next(timeout)

Calling `hasNext()` returns `true` if next returns a non-null element rather than throwing an exception. Calling next removes one element from the matched set and returns it to the caller. Calling `next(timeout)` blocks next. The iteration is said to be complete if the match set becomes empty or next calls limit (buffer size) has removed Entries from the match set. A `next` call returns `null` only if the iteration is complete.

## take() and Space Object Lease Expiration

A space object may be, but is not required to be, removed from the match set without being returned by a `next` call if it is removed from the space or is locked by a conflicting transaction. `GSIterator` **does not remove** the object after it has been buffered.

## Notifications

GSIterator register each of the templates in the templates collection for notification. If a matching object was written to the space after the GSIterator was created, the object will be added to the result set. An object that was locked under a conflicting transaction before or after the GSIterator was created and the lock was released before the iteration was complete will also be added to the result set.

A matching that arrived from a notification event will interrupt any blocking `next(timeout)` operation. If a take operation was called or an object lease timeout, the object will be removed from the next iteration result set.

## Iterator Lease

In most cases, the iterator will be leased and the lease proxy object can be obtained by calling the `getLease()` method. Cancelling or letting the lease expire will destroy the iterator; thus no notifications from here on will be accounted for, and all subsequent calls to `hasNext()` will return `false`. If there is a lease associated with the iterator, clients should not assume that completing the iteration will destroy it and should instead call cancel or let the lease expire when the end of the iteration is reached. A lease `renewal(timeout)` is used to renew a lease for an additional period of time. This duration is not added to the original lease, but is used to determine a new expiration time for the existing lease. If a lease has expired or has been canceled, a renewal is not allowed.

## Transactions

Iterating through the matched set does not lock the object. Objects that are under transaction and match the specified template/SQLQuery will not be included as part of the matched set.

