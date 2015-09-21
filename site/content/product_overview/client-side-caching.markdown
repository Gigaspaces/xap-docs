---

title:  Client Side Caching
categories: PRODUCT_OVERVIEW
parent: the-in-memory-data-grid.html
weight: 200
menu: product
---
{{%  ssummary %}}  {{%  /ssummary %}}



A client application may run a local cache (near cache), which caches data in local memory of the client application. There are two variations provided: local cache and local view. Both allow the client application to cache specific or recently used data within client JVM and have it updated automatically by the space when that data changes.

GigaSpaces supports client side caching of space data within the client application's JVM. When using client-side caching, the user essentially uses a two-layer cache architecture: The first layer is stored locally, within the client's JVM, and the second layer is stored within the remote master space. The remote master space may be used with any of the supported deployment topologies.

**In-line cache with a client cache**:
![in-line_cache-local-cache.jpg](/attachment_files/in-line_cache-local-cache.jpg)

**Side cache with a client cache**:
![side-cache-local-cache.jpg](/attachment_files/side-cache-local-cache.jpg)

The client-side cache size is limited to the heap size of the client application's JVM. The client-side cache is updated automatically when the master copy of the object within the master space is updated.


There are two variations provided:

- Local Cache {{%latestjavanet "local-cache.html"%}} - This client side cache maintains any object used by the application. The cache data is loaded on demand (lazily), based on the client application's read activities.
- Local View  {{%latestjavanet "local-view.html"%}} - This client side cache maintains a specific subset of the entire data, and client side cache is populated when the client application is started.

In both cases, once updates are performed (objects are added/updated/removed) on the master space, the master space propagates the changes to all relevant local views and caches.


{{%  tip %}}
Client cache is not enabled by default.
{{%  /tip %}}

# When to use a Client-Side Cache?

Client-side cache should be used when the application performs repetitive read operations on the same data. You should not use client-side caching when the data in the master is very frequently updated or when the read pattern of the client tends to be random (as opposed to repetitive or confined to a well-known data set).

In some cases where the relevant data set size fits a single JVM (64 Bit JVM may also be utilized) , the data may be maintained in multiple locations (JVMs) having it collocated to the application code (client or a service). See example below:

![local-cache-real-life.jpg](/attachment_files/local-cache-real-life.jpg)

With the above architecture the client or the remote service have a local cache/view proxy that is maintaining a data set that its master copy distributed across the different partitions. In such a case , `readbyId` or `readByIds` calls will be VERY fast since these are actually a local call (semi-reference object access) that does not involve network utilization or serialization.

## When to use a local view?

Use local view in case you can encapsulate the information you need to distribute in predefined query(ies). The local view is based on the replication mechanism and ensures your data synchronization and consistency with the remote space. The local view is read only.

## When to use a local cache?

Use local cache in case you are not sure which information you need in the client cache and you want to read it in a dynamic manner. Therefore the local cache is more suitable for query by ID {{% latestjavanet "query-by-id.html"%}} scenarios.



