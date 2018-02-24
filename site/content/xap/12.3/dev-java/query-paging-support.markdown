---
type: post123
title:  Paging Support
categories: XAP123, OSS
parent: querying-the-space.html
weight: 700
---



In some scenarios there's a need to return a collection of entries from the space. This is usually carried out using one of the [readMultiple]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#readMultiple-T-) overloads in `GigaSpace`. However, if there are lots of matching entries, you may encounter several problems:

* Memory usage - Both the server and client need to allocate enough memory for the entire result set.
* Latency - Since all the results are returned in one bulk, the client must wait until the final result arrives before it can process the first one.

A better approach is to create an iterator that iterates over the matching entries one at a time. Under the hood, the server returns results in batches, and when the client's buffer is exhausted the next batch is implicitly returned from the server.

{{%info "GSIterator"%}}
This page describes the new space iterator which is intended to replace the old `GSIterator` starting 10.1. Information about the old GSIterator is available [here](./query-paging-support-old.html).{{%/info%}}

# Usage

Use the [GigaSpace]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html) `iterator(template)` method to create an iterator of all the objects in the space which match the template (either [SQLQuery](./query-sql.html) or [template](./query-template-matching.html)). This results in a `SpaceIterator<T>` which implements both `Iterator<T>` and `Iterable<T>`, so a simple [for-each loop](https://docs.oracle.com/javase/1.5.0/docs/guide/language/foreach.html) can be used to iterate the results. For example:


```java
private void demoIterator(GigaSpace gigaSpace) {
    SQLQuery<MySpaceClass> query = new SQLQuery(MySpaceClass.class,"lastName = 'Smith'");
    int counter = 0;
    for (MySpaceClass entry : gigaSpace.iterator(query)) {
        System.out.println((counter++ ) + " " + entry.getLastName());
    }
}
```

If you're using Java 8 or later, you may use the new 'forEach` extension with a lambda expression:


```java
private void demoForEach(GigaSpace gigaSpace) {
    SQLQuery<MySpaceClass> query = new SQLQuery(MySpaceClass.class,"lastName = 'Smith'");
    AtomicInteger counter = new AtomicInteger();
    gigaSpace.iterator(query).forEach((e) -> System.out.println(counter.incrementAndGet() + " " + e.getLastName()));
}
```

## Batch size

Under the hood the iterator uses batching to fetch entries from the server, then serve the entries from the batch to the client whenever it asks for the next entry. The default batch size is `100`. You can use a different batch size by calling the `gigaSpace.iterator(template, batchSize)` overload.

## Transactions

Iterating through the matched set does not lock the object. Objects that are under transaction and match the specified template will not be included as part of the matched set.

# Limitations

### Complex queries

The space iterator only supports simple SQL queries. For more information about the differences between simple and complex queries see [simple SQL queries](./query-sql.html#SimpleQueries).

