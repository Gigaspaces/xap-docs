---
type: post
title:  The In-Memory Data Grid
categories: PRODUCT_OVERVIEW
parent: none
weight: 560
menu: product
---


{{%  ssummary  %}}   {{%  /ssummary %}}


The Space enables your application to read data from it, and write data to it in various ways. It also deals with various configuration aspects, such as space topologies, persistency to an external data source and memory management facilities.


# The Space as the System of Record

One of the unique concepts of GigaSpaces is that its In-Memory-Data-Grid (IMDG or the Space) serves as the system of record for your application.
This means that all or major parts of your application's data are stored in the space and your data access layer interacts with it via the various space APIs. This allows for ultra-fast read and write performance, while still maintaining a high level of reliability and fault tolerance via data replication to peer space instances in the cluster, and eventual persistency to a relational database if needed.

# The Space as a cache

GigaSpaces IMDG support variety of caching scenarios. Using GigaSpaces IMDG as a cache provides you the following benefits:

- Low latency - In-Memory Data access time without any disk usage.
- Data access layer elasticity - Scale out/up on demand to leverage additional machine resources.
- Less load on the database layer - Since the cache will offload the database, you will have less contention generated at the database layer.
- Continuous High-Availability - Zero downtime of your data access layer with the ability to survive system failures without any data loss.

The [Caching Scenarios](./caching-scenarios.html) describes the different caching options GigaSpaces support.

# Characteristics of a Space

The space has a number of determining characteristics that should be configured when it is created, as described below:



## The Space Clustering Topology

The space can have a single instance, in which case it runs on a single JVM, or multiple instances, in which case it can run on multiple JVMs.
When it has multiple instances, the space can run in a number of [topologies](./space-topologies.html) which determine how the data is distributed across those JVMs. In general, the data can be either **replicated**, which means it resides on all of the JVMs in the cluster, or **partitioned**, which means that the data is distributed across all of the JVMs, each containing a different subset of it. With a partitioned topology you can also assign one or more backup space instances for each partition.

![topologies.jpg](/attachment_files/topologies.jpg)

## Master-Local Space

Regardless of the space's topology, you can also define a "local cache" for space clients, which caches space entries recently used by the client, or a predefined subset of the central space's data (this is often referred to as **Continuous Query**).
The data cached on the client side is kept up-to-date by the server, so whenever another space client changes a space entry that resides in a certain client's local cache, the space makes sure to update that client.

## The Replication Mode

When running multiple space instances, in many cases the data should be replicated from one space instance to another. This can happen in a replicated topology (in which case every change to the data is replicated to all of the space instances that belong to the space) or in a partitioned topology (in this case you choose to have backups for each partition).
There are two replication modes - synchronous and asynchronous. With synchronous replication, data is replicated to the target instance as it is written. So the client code which writes, updates or deletes data, waits until replication to the target is completed.
With asynchronous replication, this replication is done in a separate thread, and the calling client does not wait for the replication to complete.

## Persistency Configuration

The space is an in-memory data grid. As such its capacity is limited to the sum of the memory capacity of all the JVMs on which the space instances run.
In many cases, you have to deal with larger portions of data, or load a subset of a larger data set, which resides in an external data source such as a relational database, into the space.
The space supports many [persistency options](./database-integration.html), allowing you to easily configure how it interacts with an external relational database, or a more exotic source of data.
It supports the following options, from which you can choose:

- Cache warm-up: load data from an external data source on startup.
- Cache read through: read data from the external data source when it is not found in the space.
- Cache write through: write data to the external data source when it is written to the space.
- Cache write behind (also known as asynchronous persistency): write data to the external data source asynchronously (yet reliably) to avoid the performance penalty.

## Eviction Policy and Memory Management

Since the space is memory-based, it is essential to verify that it does not overflow and crash. The space has a number of [facilities]({{% latestadmurl%}}/memory-management-overview.html) to manage its memory and make sure it does not overflow.
The first one is the eviction policy. The space supports two eviction policies: `ALL_IN_CACHE` and `LRU` (Least Recently Used). With the `LRU` policy, the space starts to evict the least used entries when it becomes full. The `ALL_IN_CACHE` policy never evicts anything from the space.
The memory manager allows you to define numerous thresholds that control when entries are evicted (in case you use `LRU`), or when the space simply blocks clients from adding data to it.
Combined, these two facilities enable you to better control your environment and make sure that the memory of the space instances in your cluster does not overflow.

## Reactive Programming

GigaSpaces and its Space-Based-Architecture embrace the [reactive programming](http://en.wikipedia.org/wiki/Reactive_programming) approach. The following falls under reactive programming with GigaSpaces:

- [Data Event Listener]({{% latestjavaurl%}}/data-event-listener.html) - [Polling Container]({{% latestjavaurl%}}/polling-container.html), [Notify Container]({{% latestjavaurl%}}/notify-container.html)
- [Local View and Local Cache](./caching-scenarios.html)
- [Mule ESB Integration]({{% latestjavaurl%}}/mule-esb.html)
- [Task Execution over the Space]({{% latestjavaurl%}}/task-execution-over-the-space.html)
- [Asynchronous Operations]({{% latestjavaurl%}}/the-gigaspace-interface.html#asynchronous-operations)
- [Drools Rule Engine Integration](/sbp/xap-drools-integration.html) - Available from a 3rd party.



# APIs to Access the Space

The space supports a number of APIs to allow for maximum flexibility to space clients when accessing the space:

- The core [Space API]({{<latestjavaurl>}}/the-gigaspace-interface.html), which is the most recommended, allows you to read objects from the space based on various criteria, write objects to it, remove objects from it and get notified about changes made to objects. It is inspired by the JavaSpaces specification and the tuple space model, although the basic data unit is a POJO, which means the space entries are simply Java objects. This API supports transactions.

{{%  info "Accessing the Space from Other Languages" %}}
The code space API is also supported in [.Net]({{<latestneturl>}}/). This allows clients to access the space via these languages. It also supports [interoperability]({{<latestjavaurl>}}/interoperability.html) between languages, so in effect you can write an object to the space using one language, say C++, and read it with another, say Java
{{%  /info %}}

- The [JPA API]({{<latestjavaurl>}}/jpa-api.html) allows you to use JPA annotations and execute JPQL queries on the space
- The [Document API]({{<latestjavaurl>}}/document-api.html) allows you to develop your application in a schema-less manner. Using map-like objects, you can add attributes to data types in runtime.
- The [Map API]({{<latestjavaurl>}}/map-api.html) allows you to access entries using a key/value approach. This is only recommended for specific scenarios where you only retrieve objects based on their IDs and would settle for the Map interface which is very limited in functionality compared to the core Space API. This API supports transactions.
- The [JDBC API]({{<latestjavaurl>}}/jdbc-driver.html) allows you to access the space in a similar way to how you would access a relational database (note that it has a number of limitations).

# Services on Top of the Space

Building on top of the core API, the Space also provides [higher level services](./services-on-top-of-the-data-grid.html) onto the application. These services, along with the space's basic capabilities, provide the full stack of middleware features that you can build your application with.
[The Task Execution API]({{% latestjavaurl%}}/task-execution-over-the-space.html) allows you send your code to the space and execute it on one or more  nodes in parallel, accessing the space data on each node locally.
[Event containers]({{% latestjavaurl%}}/messaging-support.html) use the core API's operations and abstract your code from all the low level details involved in handling the event, such as event registration with the space, transaction initiation, etc. This has the benefit of abstracting your code from the lower level API and allows it to focus on your business logic and the application behavior.
[Space-Based Remoting]({{% latestjavaurl%}}/space-based-remoting.html) allows you to use the space's messaging and code execution capabilities to enable application clients to invoke space side services transparently, using an application specific interface. Using the space as the transport mechanism for the remote calls, allows for location transparency, high availability and parallel execution of the calls, without changing the client code.

# Spring Integration

The space APIs are integrated tightly with the [Spring framework](http://www.springframework.org).
This gives you the ability to use all of the benefits that Spring brings to the table, such as dependency injection, declarative transaction management, and a well defined application life cycle model.
In addition, the higher level services (remoting and event processing), are also tightly integrated with Spring and follow the Spring framework proven design patterns. GigaSpaces XAP provides a set of well-defined Spring bindings, utilizing Spring's support for custom namespaces, which allows you to easily create and wire GigaSpaces components within Spring.

# The Space as the Foundation for Space-Based Architecture

Besides its ability to function as an in-memory data grid, the Space's core features and the services on top of it, form the foundation for [Space-Based Architecture (SBA)](/sbp/a-typical-sba-application.html). By using SBA, you can gain performance and scalability benefits not available with traditional tier-based architectures, even when these include an in-memory data grid, such as the Space.
The basic unit of scalability in SBA is the [Processing Unit]({{% latestjavaurl%}}/the-processing-unit-overview.html). The Space can be embedded into the processing unit, or accessed remotely from it. When embedded into the processing unit, local services, such as event handler and service bean exposed remotely over the space, can interact with the local space instance to achieve unparalleled performance and scalability. The Space's built-in support for data partitioning is used to distribute the data and processing across the nodes, and for scaling the application.

