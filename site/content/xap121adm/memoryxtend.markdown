---
type: post121
title:  Overview  
categories: XAP121ADM, ENT
parent: memoryxtend-overview.html
weight: 100
---

In a traditional deployment, all XAP entries are stored in JVM heaps to provide the fastest performance possible. However, as data grows in size and numbers, the following issues become noticable:

- **Price** - While RAM performs much better than hard drive, it's also much more expensive. As SSD gains popularity, we see new scenarios where storing part of the data in SSD and part in RAM provides great business value.
- **Garbage Collection** - The bigger the JVM heaps get, the harder the garbage collector works. Storing some of the data off-heap (i.e. in the native heap instead of the managed JVM heap) and managing it manually will allow using a smaller JVM heap and relieving the pressure off the garbage collector.

# How it works

In MemoryXtend, space entries are stored in the "Blob Store" (a wrapper around SSD/Flash storage or direct memory buffer regions), while the indexes are stored in the managed JVM heap. This allows queries which leverage indexes to minimize off-heap penalty, since most of the work is done in-memory and only matched entries are loaded from the off-heap storage. 
In addition, MemoryXtend uses an LRU cache for data entries, so that entries which are read frequently can be retrieved directly from the in-memory storage.


{{%align center%}}
![memstorage](/attachment_files/blobstore/xap-memoryxtend-howitworks.png)
{{%/align%}}

MemoryXtend is designed as a pluggable architecture, supporting multiple implementations of an off-heap storage (also called **BlobStore**). XAP provides two options for a blob store:

- For storing data on SSD or Flash use [MemoryXtend for Flash/SSD](./memoryxtend-rocksdb-ssd.html).
- For storing data on Off-Heap Memory (also known as native heap) [MemoryXtend for Off-Heap Memory](./memoryxtend-ohr.html).


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



# System Requirements
- Java 7 (or later)
- Linux, Mac OS X, or Windows operating system
- Read/Write permissions to mounted devices/partitions (requied for MemoryXtend over Flash/SSD)



# Supported XAP APIs

All XAP APIs are supported with the BlobStore configuration. This includes the Space API (POJO and Document), JDBC API, JPA API, JMS API, and Map API. In addition, all co-located business logic functionality (Event Containers, Task Executors, Remoting Services, Aggregators...etc.) are fully supported. 


# Limitations
- MemoryXtend and [Direct Persistency]({{%currentjavaurl%}}/direct-persistency.html) configuration is not supported.
- MemoryXtend only is only supported through the `ALL_IN_CACHE` space policy. `LRU` and other evictable cache policies are not supported. 
- MemoryXtend is not supported with the `ESM`.


