---
type: post102
title:  Tuning XAP
categories: XAP102ADM
parent:  tuning-gigaspaces-performance-overview.html
weight: 100
canonical: auto
---


This section lists helpful recommendations for tuning your application when using XAP to boost its performance, and improving its scalability.


{{%panel%}}

- [Better Data Model](#1)
- [Use readById](#2)
- [Use Paging](#3)
- [Use Delta Update](#4)
- [Design Your Space Class](#5)
- [Make proper use of Indexes](#6)
- [Use Asynchronous Operation](#7)
- [Use Delta Read](#8)
- [Colocate Data and Business Logic](#9)
- [Intelligent Partitioning](#10)
- [Blocking Take and Thread Consumption](#11)
- [Use Batch Operations](#12)
- [Use Transactions Cautiously](#13)
- [Query Optimizations](#14)
- [Use an Embedded Space if Possible](#15)
- [Distribute Data and User Requests among Several Partitions](#16)
- [Memory Usage Considerations](#17)
- [Using Snapshot to Reduce Object Creation and Space Object Data Inspection](#18)
- [Determine Cache Size](#19)
- [Determine Database Connection Pools](#20)
- [Benchmarking Your Tuning](#21)

{{%/panel%}}


{{%anchor 1%}}

# Better Data Model

Consider embedded relationship model when possible instead of having separate space objects.

{{%anchor 2%}}

# Use readById

This will will avoid query the data. Will provide access directly to the data without any broadcast calls to the entire cluster.

{{%anchor 3%}}

# Use Paging

Use the **IteratorBuilder** when accessing large amount of objects.

{{%anchor 4%}}

# Use Delta Update

Consider the **Change** operation when updating space objects.

{{%anchor 5%}}

# Design Your Space Class

Pay attention to the size of your space class -- do you really need all this information in the space? The bigger your space objects, the longer it takes to move them around, store them to disk, and fetch them back. Consider replacing a heavyweight blob field with a simple string URL, and use it later for fetching on demand. Contact GigaSpaces support for an example of this pattern. If you are using user-defined classes for your Space Class fields, try efficiently implementing **java.io.Externalizable** with these classes. This will reduce the amount of data transferred over the network, saving both time and memory space. Use binary Serialization with large collections.

{{%anchor 6%}}

# Make proper use of Indexes

XAP includes a sophisticated built-in real-time indexing engine (regardless whether the space is persistent or not) that maintains a hash and btree like indexes for each indexed Space Class attribute. If you store a large number of Space objects from the same class type in the space, consider defining one or more indexes for attributes used with [template matching]({{%currentjavaurl%}}/query-template-matching.html) or [SQL Query]({{%currentjavaurl%}}/query-sql.html). Defining indexes will improve the `read/take/readMultiple/takeMultiple/clear/count` operations response time significantly. Remember, indexes impact `write` and `take` operations response time, so choose your indexed fields carefully - each index has an overhead. GigaSpaces support index for equality , comparison (bigger/less than) queries and support [Regular Index]({{%currentjavaurl%}}/indexing.html) for a specific field and a [Compound Index]({{%currentjavaurl%}}/indexing.html#Compound Indexing) for multiple fields. Indexes can be defined for space class root level object or for a nested field allowing you to query different type of objects ("join") using the same query without any performance penalty. For bigger/less than/between queries use the **Extended index**.

{{%anchor 7%}}

# Use Asynchronous Operation

Consider using **asyncChange** , ONE WAY write modifier , etc when available.

{{%anchor 8%}}

# Use Delta Read

Consider using query projections to retrieve only the specific portions needed when reading space objects.

{{%anchor 9%}}

# Colocate Data and Business Logic

Implement Task / Distributed Task to be used with the **execute** operation or use colocated notify/polling container to move processing business logic to the data side. This will avoid serialization and network usage.

{{%anchor 10%}}

# Intelligent Partitioning

Partition data based on the business logic and not based on some unique value. This will allow the collocated logic to access its data without any network calls. If needed , run a local cache/local view to store "reference data" within each PU instance together with the transnational data.

{{%anchor 11%}}

# Blocking Take and Thread Consumption

When performing blocking operations -- read or take with timeout >0, it is recommended to set the operation timeout for short duration (5-30 seconds), and not to `FOREVER`.  This allows the space's internal thread pool to balance the different requests without exhausting all pending operations thread pool.

{{%anchor 12%}}

# Use Batch Operations

Batch operations (`writeMultiple, readMultiple, takeMultiple, clear, change`) perform actions on groups of space objects in one call. Instead of paying a penalty for every space object (remote call, database access, ...) you pay it only once. Try to design your hot spots around batch operations - this can drastically improve your application performance, up to ten to fifty times faster.

{{%anchor 13%}}

# Use Transactions Cautiously

Each transaction has an overhead. Do not use read under a transaction if you do not have a very good reason to do so. Use non-transactional read instead. This reduces database access for persistent spaces and eliminates transaction locks. If you really need to do some operations inside a transaction, use batch operations with transactions.

{{%anchor 14%}}

# Query Optimizations

When using the or logical operation together with and operations as part of your query (JDBC , JavaSpaces with SQLQuery) you can speed up the query execution by having the and conditions added to each or condition.
For example:


```java
select uid,* from table where (A = 'X' or A = 'Y') and (B > '2000-10-1' and B < '2003-11-1')
```

would be executed much faster when changing it to be:


```java
select uid,* from table where (A = 'X' and B > '2000-10-1' and B < '2003-11-1')
or (A = 'Y' and B > '2000-10-1' and B < '2003-11-1')
```

{{%anchor 15%}}

# Use an Embedded Space if Possible

If you access the space from a single JVM, or access it a large number of times from one JVM, use the embedded space mode. This eliminates the overhead of remote calls to the space. The slower your network compared to other resources (for example, a disk), the greater will be the noticeable improvement.

{{%anchor 16%}}

# Distribute Data and User Requests among Several Partitions

A single machine is always limited in the amount of data and user requests it can handle.
You can use [Data-Partitioning](./data-partitioning.html) to distribute the data and the calculation co-located with each partition. In more advanced scenarios you should use the [Master-Worker pattern](/sbp/master-worker-pattern.html) to distribute the data and the calculation in a different ratio.

{{%anchor 17%}}

# Memory Usage Considerations

**Here are several guidelines to reduce the client and space server memory footprint:**

- In order to reduce memory consumption, you can store multiple `long/integer` space object attribute values as part of a `long/integer array`. If you have lots of space objects this will improve the server footprint.
- Use indexes only for attributes used for matching. Make sure your space uses the `-1` value for the space implicit indexing property. This will ensure that indexes will be created upon request only.
- Make sure the statistics filter is turned off.
- Make sure all space workers are turned off.
- Encapsulates all non-indexed field into an inner custom class and have all `primitive class` (Integer,Long,..) fields as part of the inner class with primitive types (int, long).
- Replace string space object fields with a custom implementation, which only supports basic ascii subset (backed with byte).
- Replaced string fields with a small number of possible (source for instance) values with enum.
- Indexes footprint can be reduced by directing the system to work in Economy mode. The downside of working in Economy mode is a performance penalty of up to 15% in embedded space operations. In order to work in Economy mode set `space-config.engine.use_economy_hashmap=true`.


{{%anchor 18%}}

# Using Snapshot to Reduce Object Creation and Space Object Data Inspection

When using the same template for matching, consider using a snapshot template. A snapshot template is the result object of the `GigaSpace.snapshot` call. The returned result includes GigaSpaces internal representation of the template object that does not need to undergo any inspection before it is sent to the GigaSpaces server.

{{% tip %}}
The snapshot returns an object you can use for subsequent matching as a template.
{{% /tip %}}

{{%anchor 19%}}

# Determine Cache Size

When using persistent space and reusing data, you must take caching into account. The cache manager caches space objects for use and performs an LRU (Least Recent Use) based cleanup on the cache. When searching for a space object , the cache is searched first. Set the cache size to the number of Space objects that your environment can reasonably contain as resident in the VM heap size. This will prevent unnecessary queries on your database. If you want the cache size to be based on the JVM running the space you may use the memory usage options.

{{%anchor 20%}}

# Determine Database Connection Pools

When using persistent space and a large number of users/threads access the space concurrently, each of them requires a database connection. Set enough connections in the connection pool so that users won't be blocked. You should calculate the number of concurrent requests the space needs to handle based on the number of users that will access the space simultaneously.

{{%anchor 21%}}

# Benchmarking Your Tuning

The Benchmark View provides a user interface for benchmarking the space.

{{% refer %}}
For more details, refer to:

- [Benchmark View](./benchmark-browser.html)
- [Benchmark Example](./benchmark-utility-cli.html)
{{% /refer %}}

