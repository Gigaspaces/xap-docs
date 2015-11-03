---
type: post110
title:  MemoryXtend
categories: XAP110ADM
parent: none
weight: 430
---

By default, XAP entries are stored in-memory (actually, in the JVM heap) to provide the fastest performance possible. As data grows in size and numbers, however, we encounter a few problems:

- **Price** - While RAM performs much better than hard drive, its also much more expensive. As SSD gains popularity, we see new scenarios where storing part of the data in SSD and part in RAM provides great business value.
- **Garbage Collection** - The bigger the JVM heaps get, the harder the garbage collector works. Storing some of the data off-heap (i.e. in the native heap instead of the managed JVM heap) and managing it manually will allow using a smaller JVM heap and relieving the pressure off the garbage collector.

Obviously, simply storing the data in SSD means XAP will no longer be an In-Memory Data Grid. The solution is a hybrid storage model offered by XAP called **MemoryXtend**, which uses the best of both worlds.

# How It Works

In MemoryXtend, the entry's data is stored off-heap (e.g. in the native heap or on a file in SSD), but the indexes are stored in the managed JVM heap. This allows queries which leverage indexes to minimize off-heap penalty, since most of the work is done in-memory and only matched entries are loaded from the off-heap storage. 
In addition, MemoryXtend uses an LRU cache for data entries, so that entries which are read frequently can be retrieved directly from the in-memory storage.

MemoryXtend is designed as a pluggable architecture, supporting multiple implementations of an off-heap storage (also called **BlobStore**). XAP is bundled with two such plug-ins, or add-ons:

- For storing data on an SSD device use the [MemoryXtend RocksDB add-on](./memoryxtend-rocksdb-ssd.html).
- For storing data in RAM on the unmanaged heap use the [MemoryXtend MapDB add-on](./memoryxtend-ohr.html).

This page explains the general concepts and settings which apply to any MemoryXtend add-on. In addition, each MemoryXtend add-on has a specific page for it's additional settings and options.

# Supported XAP APIs

All XAP APIs are supported with the BlobStore configuration. This includes the Space API (POJO and Document), JDBC API, JPA API, JMS API, and Map API. 

# Class Level Settings

Once MemoryXtend is configured for a space, all entries stored in that space will be stored using the MemoryXtend settings. This is obviously somewhat slower than entries stored in-memory, in the traditional XAP storage mechanism. In some scenarios it makes sense to use MemoryXtend for some classes but not for others. For example, a user might say: "I have a limited amount of `Customer` entries, but tons of `Order` entries, and I want to disable MemoryXtend for the `Customer` entries". This can be done via the space class metadata. For example:

{{%tabs%}}
{{%tab "Annotation"%}}

```java
@SpaceClass(blobstoreEnabled = false)
public class Customer {
    //
}
```

{{%/tab%}}

{{%tab "gs.xml"%}}
```xml
<gigaspaces-mapping>
    <class name="com.test.Customer" "blobstoreEnabled"="false">
     </class>
</gigaspaces-mapping>
```
{{%/tab%}}

{{%/tabs%}}

# Persistency

Unlike the traditional XAP persistence model, where the IMDG backing store is usually a database with relatively slow response time, located in a central location, the storage interface assumes very fast access for write and read operations, and a local, dedicated data store that supports a key/value interface. Each IMDG primary and backup instance across the grid is interacting with its dedicated storage medium (in our case SSD drive) independently in an atomic manner. 

When running a traditional persistence configuration, the IMDG serves as a front-end to the database, and is acting as a transactional cache storing a subset of the data. In this configuration data is loaded in a lazy manner to the IMDG with the assumption that the heap capacity is smaller than the entire data set.

With the external storage medium mode, the entire data set is kept on the external SSD drive. The assumption is the SSD capacity across the grid is large enough to accommodate for the entire data set of the application.
