---
type: post140
title:  MemoryXtend
categories: XAP140ADM, ENT
parent: none
weight: 430
---

# Overview

In a traditional deployment, all XAP entries are stored in JVM heaps to provide the fastest performance possible. However, as data grows in size and numbers, the following issues become noticeable:

- **Price** - While RAM performs much better than a magnetic hard drive, it is also much more expensive. As SSD gains in popularity, we see new scenarios where storing part of the data in SSD and part in RAM provides great business value.
- **Garbage Collection** - The bigger the JVM heaps get, the harder the garbage collector works. Storing some of the data off-heap (i.e. in the native heap instead of the managed JVM heap) and managing it manually allows using a smaller JVM heap and relieves the pressure on the garbage collector.

The MemoryXtend (blobstore) storage model allows an external storage medium (one that does not reside on the JVM heap) to store the Space data. This section describes the general MemoryXtend architecture and functionality, and its off-heap RAM and hSSD implementations.

# Implementation

MemoryXtend is designed as a pluggable architecture, supporting multiple implementations of off-heap storage (also called blobstore). Space entries are stored in the blobstore (a wrapper around SSD/HDD storage or direct memory buffer regions), while the indexes are stored in the managed JVM heap. This allows queries that leverage indexes to minimize off-heap penalty, because most of the work is done in-memory and only matched entries are loaded from the off-heap storage. 

In addition, MemoryXtend uses an LRU cache for data entries, so entries that are read frequently can be retrieved directly from the in-memory storage.

{{%align center%}}
![memstorage](/attachment_files/blobstore/xap-memoryxtend-howitworks.png)
{{%/align%}}

MemoryXtend offers the following storage driver options:

* [MemoryXtend for Disk (SSD/HDD Storage](./memoryxtend-rocksdb-ssd.html) - For storing data on SSD or any other disk device.
* [MemoryXtend for Off-Heap Memory](./memoryxtend-ohr.html) - For storing data in off-heap memory (also known as native heap).
* [MemoryXtend for Pmem](./memoryxtend-pmem.html) - For storing data in pmem memory.

# Class-Level Settings

When MemoryXtend is configured for a Space, all entries stored in that Space are stored using the MemoryXtend settings. This is somewhat slower than entries stored in-memory with the traditional XAP storage mechanism. In certain scenarios, it makes sense to use MemoryXtend for some classes but not for others. For example, a user may have a limited amount of `Customer` entries, but a lot of `Order` entries, and therefore may want to disable MemoryXtend for the `Customer` entries. This can be done via the Space class metadata. For example:

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

# On-Heap Cache

MemoryXtend comes with a built-in, LRU-based cache that stores objects on-heap for faster access. The following table describes the blobstore cache configuration options.

| Property               | Description                                               | Default | Use |
|:-----------------------|:----------------------------------------------------------|:--------|:--------|
| <nobr>cache-entries-percentage</nobr> | The cache size is determined based on the percentage of the GSC JVM max memory(-Xmx). If `-Xmx` is not specified, the default cache size is `10000` objects. | 20% | optional |
| avg-object-size-KB |  Average object size, in KB. `avg-object-size-bytes` and `avg-object-size-KB` cannot be used together. | 5 | optional |
| avg-object-size-bytes |  Average object size, in bytes. `avg-object-size-bytes` and `avg-object-size-KB` cannot be used together. | 5000 | optional |
| persistent |  Data is written to external storage, and the Space performs recovery from the external storage if needed. |  | required |
| blob-store-cache-query | One or more SQL queries that determine which objects will be stored in cache. |  | optional |

## Example

{{%tabs%}}
{{%tab "pu.xml"%}}
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/rocksdb-blob-store"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/rocksdb-blob-store http://www.openspaces.org/schema/{{%currentversion%}}/rocksdb-blob-store/openspaces-rocksdb-blobstore.xsd">

    <blob-store:rocksdb-blob-store id="rocksDbBlobstore" paths="[/mnt/db1,/mnt/db2]" mapping-dir="/tmp/mapping"/>

    <os-core:embedded-space id="space" space-name="mySpace" >
        <os-core:blob-store-data-policy blob-store-handler="rocksDbBlobstore" persistent="true" avg-object-size-KB="5" cache-entries-percentage="20" />
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}
{{%tab "Java Code"%}}
```java
// Create RocksDB storage driver:
BlobStoreStorageHandler blobStore = new RocksDBBlobStoreConfigurer()
        .setPaths("[/mnt/db1,/mnt/db2]")
        .setMappingDir("/tmp/mapping")
        .create();
// Create space with that storage driver:
EmbeddedSpaceConfigurer spaceConfigurer = new EmbeddedSpaceConfigurer("mySpace")
        .cachePolicy(new BlobStoreDataCachePolicy()
                .setBlobStoreHandler(blobStore)
                .setPersistent(true)
                .setAvgObjectSizeKB(5)
                .setCacheEntriesPercentage(20));
GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
```
{{% /tab %}}
{{% /tabs %}}


## Default Cache - LRU

By default the cache uses an LRU strategy to cache and evict entries. The cache size is controlled by two settings:

* Cache Entries Percentage - determines how much RAM out of the JVM heap should be used for caching (in percentage)
* Average object size - determines the average entry object size in RAM, either in bytes or kilobytes.

These settings determine the maximum number of objects in cache, according to the following calculation:

`Number of objects = ((GSC Xmx) * (cache-entries-percentage/100))/average-object-size-bytes`

Once that number is reached, adding an entry to the cache will automatically evict the least recently used one.

For example, On a 10GB GSC, if we set a cache entries percentage to 20, and an average object size of 2KB (the default): 

10GB * 20% / 2KB = ((10 * 1024 * 1024 * 1024) * 0.2) / (2 * 1024) = **1,048,576**

## User-Defined Cache

The `blob-store-cache-query` option enables customizing the cache contents. You can define a set of SQL queries, so that only objects that fit the queries:

- Are pre-loaded into the JVM heap upon data grid initialization/restart.
- Are stored in the JVM heap after Space operations.

This guarantees that any subsequent read requests will hit RAM, providing predictable latency (and avoiding cache misses). This customization is useful when read latencies for specific class types (such as hot data, current day stocks) must be predictable.

The SQL queries are static and cannot be changed dynamically. To update them, make the necessary modifications and then restart the application.

**Example**

In this example, an online trading platform defines the following criteria for "hot" data:

- `Stock` instances where the price > 1000
- `Trade` instances with volume > 10000
- `Account` instances with platinum rating

{{%tabs%}}
{{%tab "Java Code"%}}

```java
BlobStoreStorageHandler blobStore = ...;
EmbeddedSpaceConfigurer spaceConfigurer = new EmbeddedSpaceConfigurer("mySpace")
    .cachePolicy(new BlobStoreDataCachePolicy()
        .setBlobStoreHandler(blobStore)
        .setPersistent(true)
        .addCacheQuery(new SQLQuery(Stock.class, "price > 1000"))
        .addCacheQuery(new SQLQuery(Trade.class, "volume > 10000"))
        .addCacheQuery(new SQLQuery(Account.class, "rating = 'platinum'")));
GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
```
{{%/tab%}}
{{%tab "pu.xml"%}}

```xml
<blob-store:rocksdb-blob-store id="myBlobStore" paths="[/tmp/rocksdb]" mapping-dir="/tmp/mapping"/>

<os-core:embedded-space id="space" name="mySpace">
    <os-core:blob-store-data-policy persistent="true" blob-store-handler="myBlobStore">
        <os-core:blob-store-cache-query class="com.gigaspaces.blobstore.rocksdb.Stock" where="price > 1000"/>
        <os-core:blob-store-cache-query class="com.gigaspaces.blobstore.rocksdb.Trade" where="volume > 10000"/>
        <os-core:blob-store-cache-query class="com.gigaspaces.blobstore.rocksdb.Account" where="rating = 'platinum'"/>
    </os-core:blob-store-data-policy>
</os-core:embedded-space>

     <os-core:giga-space id="gigaSpace" space="space"/>
 ```
 {{%/tab%}}

{{%/tabs%}}

When the `com.gigaspaces.cache` logging is turned on, the following output is generated:

```bash
2016-12-26 07:57:56,378  INFO [com.gigaspaces.cache] - BlobStore internal cache recovery:
blob-store-queries: [SELECT * FROM com.gigaspaces.blobstore.rocksdb.Stock WHERE price > 1000, SELECT * FROM com.gigaspaces.blobstore.rocksdb.Trade WHERE volume > 10000, SELECT * FROM com.gigaspaces.blobstore.rocksdb.Account WHERE rating = 'platinum'].
Entries inserted to blobstore cache: 80.
```

## Cache Metrics

The concept of cache *hit* and cache *miss* is very important for cache analysis. A hit occurs when querying data that is stored in the cache. A miss occurs when querying data that is stored on disk.

Custom caching distinguishes between hot data (that fits the custom queries) and cold data. Hot data is stored in cache and on disk, while cold data is stored only on disk.

Ideally, all hot data would be found in cache. However, the cache size is limited, and likely isn't able to store all the hot data. This means that data can exist in one of three states:

- Hot data *and* found in cache. Querying this data will result in a **_cache hit_**.
- Hot data *not* found in cache (because cache is full). Querying this data will result in a **_hot data cache miss_**.
- Cold data that is stored only on disk. Querying this data will result in a **_cold data cache miss_**.

Total cache misses = hot data cache misses + cold data cache misses.

By modifying the custom queries, the cache efficiency (maximizing hits and minimizing misses) can be improved. To keep track of the cache efficiency, key metrics are measured and stored, including hits, total misses, and hot data misses.
For information about XAP metrics and how to use them, refer to the [Metrics](./metrics-overview.html) section of this guide.

## Data Recovery on Restart

The MemoryXtend architecture allows for data persisted on the blobstore to be available for the data grid upon restart. To enable this, you have to enable the `persistent` option in the blobstore policy.

```xml
  <os-core:blob-store-data-policy persistent="true" blob-store-handler="myBlobStore">
```

## Lazy Load

If no custom queries are defined, the *lazy load* approach is used and no data is loaded into the JVM heap upon restart. MemoryXtend saves only the indexes in RAM, and the rest of the objects are stored on disk. As read throughput increases from clients, most of the data eventually loads into the data grid RAM tier. This is a preferred approach when the volume of data persisted on disk exceeds what can fit into memory.

# Persistence and Recovery

When using a cluster with backups for high availability, if one of the nodes fails and restarts, it automatically locates the primary node and copies all the data from it so it can serve as a backup again. This process is called *recovery*. The more data in the Space, the longer recovery takes, and if MemoryXtend is used this is no longer a RAM-only process. The primary Space must iterate through its MemoryXtend instance to fetch all the data for the backup node performing the recovery.

However, when using a MemoryXtend storage driver is based on non-volatile technology (for example, SSD), the backup can use the persisted data for the recovery process, and instead of recovering everything from the primary, it can recover only the delta that it missed while it was down. In addition, the backup can rebuild the indexes for the persisted data without intervention or assistance from the primary instance.

Persistency is disabled by default, and must be explicitly enabled. For example:

```xml
<os-core:embedded-space id="space" name="mySpace" >
    <os-core:blob-store-data-policy blob-store-handler="myBlobStore" persistent="true"/>
</os-core:embedded-space>
```

In addition, persistency requires the settings described in the sections below.

## Machine-Instance Affinity

If a GSC or a machine running a GSC restarts, there is no guarantee the XAP instance running within the GSC will be provisioned to the same machine where it was running before. When MemoryXtend is used in a non-persistent manner, this isn't problematic because the instance recovers from the primary, but if MemoryXtend is set to `persistent=true`, you must ensure that the instance is provisioned on the same machine where it was located before, so it can recover from the correct device, which is usually local to the machine.
 
{{% note "Info"%}}
Central Storage mode allows you to use MemoryXtend without configuring the Machine-Instance Affinity.
{{% /note %}}


To ensure that the Service Grid deploys the XAP instances on the correct machines, use [instance-level SLA](./the-sla-overview.html). For example:

{{%tabs%}}
{{%tab "Partitioned with a backup SLA"%}}
The following `sla.xml` example shows a single partition with a backup where the first instance is provisioned into `HostA`, and the second instance for the same partition is provisioned into `HostB`.

```xml
<os-sla:sla>
        <os-sla:instance-SLAs>
           <os-sla:instance-SLA instance-id="1">
               <os-sla:requirements>
                 <os-sla:host ip="HostA"/>
                </os-sla:requirements>
            </os-sla:instance-SLA>
		    <os-sla:instance-SLA instance-id="1" backup-id="1">
                <os-sla:requirements>
                    <os-sla:host ip="HostB"/>
                </os-sla:requirements>
            </os-sla:instance-SLA>
        </os-sla:instance-SLAs>
</os-sla:sla>
```
{{%/tab%}}
{{%tab "Partitioned without a backup SLA"%}}
The following `sla.xml` shows a partitioned (2 partitions) data grid without backups SLA example where both instances are provisioned into the `HostA`:

```xml
<os-sla:sla>
        <os-sla:instance-SLAs>
            <os-sla:instance-SLA instance-id="1">
                <os-sla:requirements>
                    <os-sla:host ip="HostA"/>
                </os-sla:requirements>
            </os-sla:instance-SLA>
	    <os-sla:instance-SLA instance-id="2">
                <os-sla:requirements>
                    <os-sla:host ip="HostA"/>
                </os-sla:requirements>
            </os-sla:instance-SLA>
        </os-sla:instance-SLAs>
</os-sla:sla>
```
{{%/tab%}}
{{%/tabs%}}

{{% note "Deployment with SLA"%}}
Make sure you provide the `sla.xml` location at deploy time (`-sla` deploy command option), or locate it at the root of the Processing Unit JAR or under the `META-INF/spring` directory, alongside the Processing Unit's `pu.xml` file.
{{% /note %}}


## Last Primary

When a Space instance starts as part of a primary-backup cluster, it undergoes a process called *active election* to determine if it should be a primary or a backup instance. Generally speaking, the first instance that is loaded is the primary, and the rest are backups. If a persistent system is restarted in an orderly manner (meaning all the data was flushed to both the primary and backup instances before shutting down), it doesn't matter which instance becomes primary, because they are all identical. However, if both the primary and backup instances crash unexpectedly for some reason and then restart, it is important to ensure that the last instance that was primary before the crash is elected primary again, because it holds a more accurate version of the data.
  
To overcome this problem, the Space can be configured with an *attribute store* that is updated whenever a new primary Space is elected. When the system restarts, instead of electing the first available instance, the system will wait for the last primary Space to become available and re-elect it. If the last primary Space cannot be restarted, the user can manually remove the last primary entry from the attribute store. This will allow a backup Space to become the primary.

XAP is bundled with two implementations:
 
* File-based implementation of an attribute store, which can be used in conjunction with an NFS file system to maintain the last primary.
* Storing the last primary automatically in Apache Zookeeper.
 

The following examples demonstrate how to configure a persistent disk-based storage driver with such an attribute store:
 
{{%tabs%}}
{{%tab "File-based"%}}
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/rocksdb-blob-store"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/rocksdb-blob-store http://www.openspaces.org/schema/{{%currentversion%}}/rocksdb-blob-store/openspaces-rocksdb-blobstore.xsd">

    <bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"/>
    <bean id="attributeStoreHandler" class="com.gigaspaces.attribute_store.PropertiesFileAttributeStore">
        <constructor-arg name="path" value="/your-shared-folder/lastprimary.properties"/>
    </bean>

    <blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt/db1,/mnt/db2]" mapping-dir="/tmp/mapping"/>
    <os-core:embedded-space id="space" name="mySpace" >
        <os-core:blob-store-data-policy blob-store-handler="myBlobStore" persistent="true"/>
        <os-core:attribute-store store-handler="attributeStoreHandler"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}
{{%tab "Zookeeper -based"%}}
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/rocksdb-blob-store"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/rocksdb-blob-store http://www.openspaces.org/schema/{{%currentversion%}}/rocksdb-blob-store/openspaces-rocksdb-blobstore.xsd">

    <bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"/>
    <bean id="attributeStoreHandler" class="org.openspaces.zookeeper.attribute_store.ZooKeeperAttributeStore" >
          <constructor-arg name="name" value="blobstore_lastPrimary"/>
      </bean>

    <blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt/db1,/mnt/db2]" mapping-dir="/tmp/mapping"/>
    <os-core:embedded-space id="space" name="mySpace" >
        <os-core:blob-store-data-policy blob-store-handler="myBlobStore" persistent="true"/>
        <os-core:attribute-store store-handler="attributeStoreHandler"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}
{{% /tabs %}}


# Supported XAP APIs

All XAP APIs are supported with the blobstore configuration. This includes the Space (POJO and Document), JDBC, JPA, JMS, and Map APIs. In addition, all co-located business logic functionality (event containers, task executors, remoting services, aggregators, etc.) are fully supported. 


# Limitations

- MemoryXtend and [Direct Persistency](../dev-java/direct-persistency.html) configuration is not supported.
- MemoryXtend is only supported with the Space caching policy set to ALL_IN_CACHE. LRU and other cache policies that use eviction are not supported. 
- MemoryXtend is not supported with the `ESM`.

