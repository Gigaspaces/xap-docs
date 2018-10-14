---
type: post123
title:  MemoryXtend for Disk (SSD/HDD)
categories: XAP123ADM, ENT
parent: memoryxtend-overview.html
weight: 200
---

# Overview

XAP MemoryXtend for disk delivers built-in, high-speed persistence that leverages local or attached SSD devices, all-flash-arrays (AFA), or any other disk or memory option. It delivers low latency write and read performance, as well as fast data recovery.  XAP MemoryXtend for disk is based on {{%exurl "RocksDB""http://rocksdb.org/"%}}, which is a persistent key/value store optimized for fast storage environments. 


## Architecture and Components

When configured for the disk storage driver, the MemoryXtend architecture tiers the storage of each Space partition instance across two components: a Space partition instance (managed JVM heap) and an embedded key/value store (the blobstore) as shown in the diagram below. 

{{%align center%}}
![image](/attachment_files/blobstore/memoryxtend-rocksdb-architecture.png)
{{%/align%}}
 
 {{%note "Note"%}}
XAP requires Read/Write permissions to mounted devices/partitions.
{{%/note%}}
 
 
 
### Space Partition Instance

The Space partition instance is a JVM heap that acts as an LRU cache against the underlying blobstore. This tier in the architecture stores indexes, Space class metadata, transactions, replication redo logs, leases, and statistics. 
Upon a Space read operation, if the object exists in the JVM heap (i.e. a cache <i>hit</i>) it is immediately returned to the Space proxy client. Otherwise, the Space loads it from the underlying blobstore and places it on the JVM heap (known as a cache <i>miss</i>). 


### Blobstore

The blobstore is based on a log-structured merge tree architecture (similar to popular NoSQL databases such as: {{%exurl "HBase""https://hbase.apache.org/"%}}, {{%exurl "BigTable""https://cloud.google.com/bigtable/"%}}, or {{%exurl "Cassandra""https://cassandra.apache.org/"%}}). There are three main components in the blobstore: 

- **MemTable**: An in-memory data structure (residing on off-heap RAM) where all incoming writes are stored. When a MemTable fills up, it is flushed to an SST file on storage.
- **Log**: A write-ahead log (WAL) that serializes MemTable operations to a persistent medium as log files. In the event of failure, WAL files can be used to recover the key/value store to its consistent state, by reconstructing the MemTable from the logs.
- **Sorted String Table (SST) files**: SSTable is a data structure (residing on disk) that efficiently stores a large data footprint while optimizing for high throughput, and sequential read/write workloads. When a MemTable fills up, it is flushed to an SST file on storage and the corresponding WAL file can be deleted.
- **Built-in Cache**: MemoryXtend comes with a built-in cache. This cache is part of the Space partition tier, and stores objects in their native form.

# Basic Configuration

Any existing XAP Space can be configured to integrate a blobstore with it. As with a typical Processing Unit, configuration is done using either the `pu.xml` configuration file, or in the code. For example: 

{{%tabs%}}
{{%tab "Namespace"%}}
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
        <os-core:blob-store-data-policy blob-store-handler="rocksDbBlobstore" persistent="true"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}
{{%tab "Plain XML"%}}
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd">

    <bean id="rocksDbBlobstore" class="com.gigaspaces.blobstore.rocksdb.config.RocksDBBlobStoreDataPolicyFactoryBean">
        <property name="paths" value="[/mnt/db1,/mnt/db2]"/>
        <property name="mappingDir" value="/tmp/mapping"/>
    </bean>

    <os-core:embedded-space id="space" space-name="mySpace">
        <os-core:blob-store-data-policy blob-store-handler="rocksDbBlobstore" persistent="true"/>
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
                .setPersistent(true));
GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
```
{{% /tab %}}
{{% /tabs %}}

The following table describes the configuration options used in `rocksdb-blob-store` above.

| Property               | Description                                               | Default | Use |
|:-----------------------|:----------------------------------------------------------|:--------|:--------|
| paths | A comma-separated array of mount paths used for each Space partition's blobstore. The number of paths in the array should correspond to the number of partition instances in the Space (primaries and backups). For instance, for a two-partition Space with no backups, `/mnt/db1` corresponds to the first partition, and `/mnt/db2` to the second one.  | | required |
| mapping-dir | A directory in the file system that contains the "partition to blobstore" mapping file. This file is automatically generated by MemoryXtend. If a partition is relocated, the mapping file is updated.  |  | required |
| central-storage | Specifies whether the deployment strategy is for [central storage](./memoryxtend-rocksdb-ssd.html#central-storage) (i.e. SAN configuration) or [local storage](./memoryxtend-rocksdb-ssd.html#local-storage) on each grid machine (default).| false | optional |
| db-options | Specifies the tuning parameters for the persistent data store in the underlying blobstore. This includes SST formats, compaction settings and flushes. See the [Performance Tuning](./memoryxtend-rocksdb-ssd.html#performance-tuning) section for details. | | optional |  
| <nobr>data-column-family-options<nobr> | Specifies the tuning parameters for the LSM logic and memory tables. See the [Performance Tuning](./memoryxtend-rocksdb-ssd.html#performance-tuning) section for details.| | optional |
| blob-store-handler | Blobstore implementation.|  | required |

{{%note%}}
For an example of how to configure the on-heap cache properties, see the [MemoryXtend overview](./memoryxtend-overview.html#on-heap-cache) topic.
{{%/note%}}

# Off-Heap Cache

XAP can cache the values of indexed fields in the process native (off-heap) memory. This optimizes operations that require only indexed values in order to execute, by fetching the data from off-heap instead of disk. The operations that benefit from this optimization are:

- Read with projection and only indexed fields in query and projection - primary instance optimization
- Take with only indexed fields in query - backup optimization
- Clear with only indexed fields in query - primary and backup instance optimization

{{%note "Note"%}}
This feature increases the overall memory consumption of the Space by several bytes (depending on the indexed fields) per entry.
{{%/note%}}

The off-heap cache is disabled by default. To enable it, simply set the `off-heap-cache-memory-threshold` property in `rocksdb-blob-store` to the amount of memory you want to allocate for off-heap caching, for example `20m`. Use the following sizing units:

* `b` - Bytes
* `k`, `kb` - Kilobytes
* `m`, `mb` - Megabytes
* `g`, `gb` - Gigabytes

Before any operation that requires memory allocation (write, update, and initial load), the memory manager checks how much of the allocated memory has been used. If the threshold has been breached, an `OffHeapMemoryShortageException` is thrown. Read, take, and clear operations are always allowed.

{{%warning "Important"%}}
If the used memory is below the configured threshold, then a large write operation may exceed the threshold without being blocked or throwing an exception. Users should take this into account when setting the maximum memory size. This behavior is similar to that of the regular [memory manager](../dev-java/memory-management-overview.html).
{{%/warning%}}

The amount of used off-heap memory can be tracked using the following:

* Metrics - The `space_blobstore_off-heap-cache_used-bytes_total` metric, as described on the [Metrics](./metrics-bundled.html#blobstore-operations) page.
* Administration API - Thru [SpaceInstanceStatistics.getBlobStoreStatistics()]({{% api-javadoc %}}/index.html?org/openspaces/admin/space/SpaceInstanceStatistics.html#getBlobStoreStatistics)
* Web Management Console - In the Space instances view, right-click any of the columns in the table and add the **Used Off-Heap Cache** column.

When a Processing Unit is undeployed or a Space is killed, the off-heap memory manager frees the allocated off-heap memory.

See the following example:

```java
    public static class TestSpaceClass {

        private Integer id;
        private Integer age;
        private Boolean active;

        public TestSpaceClass() {
        }

        @SpaceId
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @SpaceIndex
        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }
    }
```

```
gigaSpace.readMultiple(new SQLQuery<TestSpaceClass>(TestSpaceClass.class, "age = 3").setProjections("id", "age"));
```
To see whether your query can benefit from this optimization behavior, you can set `com.gigaspaces.cache` to FINE and look for the following log entry: 

```property
2017-11-28 07:57:49,115  FINE [com.gigaspaces.cache] - BlobStore - enabled optimization for query: SELECT * FROM com.gigaspaces.test.blobstore.rocksdb.AbstractRocksDBOptimizationTest$TestSpaceClass 
2017-11-28 07:57:49,115  FINE [com.gigaspaces.cache] - BlobStore - disabled optimization for query: SELECT * FROM com.gigaspaces.test.blobstore.rocksdb.AbstractRocksDBOptimizationTest$TestSpaceClass 
```

When set to FINER, you can see where the entries were fetched from. Look for the following log entry: 

```property
2017-11-28 07:57:49,117  FINER [com.gigaspaces.cache] - container [mySpace_container1_1:mySpace] Blobstore- entry loaded from off heap, uid=-1850388785^84^98^0^0
2017-11-28 07:57:49,117  FINER [com.gigaspaces.cache] - container [mySpace_container1_1:mySpace] Blobstore- entry loaded from disk, uid=-1850388785^84^98^0^0
```

# Advanced Configuration

As part of fine-tuning the MemoryXtend functionality, you can control the balance between memory utilization and system performance. This is useful because there are two ways to update the value of an object in the off-heap memory:

* Free up the old memory allocation, and reallocate new memory.
* Overwrite the old value using the same allocated memory.

Deleting and reallocating memory takes longer then overwriting the same memory area, but if the size of the new value is smaller then the old value, then the overwrite option leaves part of the original allocated memory underutilized. You can use the `BLOBSTORE_OFF_HEAP_MIN_DIFF_TO_ALLOCATE_PROP` Space property to control when to trade off system performance for maximizing the memory usage. 

This Space property works as follows: if the oldValueSize - newValueSize > blobstore.off-heap.update_threshold, then delete and reallocate memory for the object. Otherwise overwrite the old object value with the new object value.

The default threshold is 50B.

## Example

{{%tabs%}}
{{%tab "pu.xml"%}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/off-heap-blob-store"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/off-heap-blob-store http://www.openspaces.org/schema/{{% currentversion %}}/off-heap-blob-store/openspaces-off-heap-blob-store.xsd">

    <blob-store:off-heap-blob-store id="offheapBlobstore" memory-threshold="20g"/>

    <os-core:embedded-space id="space" name="mySpace">
       <os-core:blob-store-data-policy persistent="false" blob-store-handler="offheapBlobstore"/>
       <os-core:properties>
          <props>
             <prop key="blobstore.off-heap.update_threshold">1M</prop>
          </props>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```

{{% /tab %}}
{{%tab "Java Code"%}}

```java
// Create off-heap storage driver:
String memoryThreshold = "5g";
BlobStoreStorageHandler blobStore = new OffHeapBlobStoreConfigurer()
        .setMemoryThreshold(memoryThreshold)
        .create();
// Create space with that storage driver:
String spaceName = "mySpace";
EmbeddedSpaceConfigurer spaceConfigurer = new EmbeddedSpaceConfigurer(spaceName)
        .cachePolicy(new BlobStoreDataCachePolicy()
                .setBlobStoreHandler(blobStore)
                .setPersistent(false));
spaceConfigurer.addProperty("blobstore.off-heap.update_threshold", "1M");
GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

# Deployment Strategies

## Local Storage

This configuration allows each Space partition instance (primary or backup) to use a machine-local storage device (SSD/HDD). With this approach, data locality is leveraged so that the devices local to the machine are used for reads/writes. The local storage deployment strategy provides an easy way to implement a "local persistent store" (also known as native persistence) pattern. 


{{%align center%}}
![image](/attachment_files/blobstore/memoryxtend-local-storage.png)
{{%/align%}}


## Central Storage

This deployment strategy works well with {{%exurl "storage area networks (SAN)" "http://en.wikipedia.org/wiki/Storage_area_network"%}}, which means that the disk drive devices are installed in a remote storage array but behave as if they're attached to the local machine. Most storage networks use the iSCSI or Fibre Channel protocol for communication between servers and disk drive devices. This configuration breaks the coupling between a partition instance and a local machine device, allowing seamless relocation of partitions across data grid machines. 

{{%align center%}}
![image](/attachment_files/blobstore/memoryxtend-central-storage.png)
{{%/align%}}

Tiering storage between Space partition instances and attached storage can be applied across one or more storage arrays, as shown in the configurations below: 

### Single Storage Array

{{%section%}}
{{%column width="80%" %}}
The following example deployes a 2 partitions space with a single backup (2,1) in the following manner:

- `/mnt/db1` will be mounted to the 1st primary.
- `/mnt/db2` will be mounted to the 2nd primary.
- `/mnt/db3` will be mounted to the 1st backup.
- `/mnt/db4` will be mounted to the 2nd backup.

{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/blobstore/rockdb2.png" "Single storage arrays"%}}
{{%/column%}}
{{%/section%}}

```xml
<blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt/db1,/mnt/db2,/mnt/db3,/mnt/db4]"/>
```

### Dual Storage Array

{{%section%}}
{{%column width="80%" %}}
The following example deploys a 2-partition Space with a single backup (2,1) in the following manner: 

- `/mnt1/db1` will be mounted to the 1st primary.
- `/mnt1/db2` will be mounted to the 2nd primary.
- `/mnt2/db1` will be mounted to the 1st backup.
- `/mnt2/db2` will be mounted to the 2nd backup.

{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/blobstore/rockdb3.png" "Two storage arrays"%}}
{{%/column%}}
{{%/section%}}

```xml
<blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt1/db1,/mnt1/db2],[/mnt2/db1,/mnt2/db2]"/>
```

# Performance Tuning

## Persistent Data Store Tuning Parameters

XAP uses the default DBOptions class `com.com.gigaspaces.blobstore.rocksdb.config.XAPDBOptions`.

{{%refer%}}
A list of configuration properties can be found in  the {{%exurl "org.rocksdb.DBOptionsInterface" "https://github.com/facebook/rocksdb/blob/master/java/src/main/java/org/rocksdb/DBOptionsInterface.java"%}} Java docs.
{{%/refer%}}


| Property               | Description                                               | Value |
|:-----------------------|:----------------------------------------------------------|:--------|
| createIfMissing | Configure whether to create the database if it is missing. This value is always overrode with `true`. | true |
| maxBackgroundCompactions | Specifies the maximum number of concurrent background compaction jobs, submitted to the default LOW priority thread pool. <br /> If you're increasing this, also consider increasing the number of threads in the LOW priority thread pool | 8 |
| maxBackgroundFlushes | Specifies the maximum number of concurrent background flush jobs. <br />If you're increasing this, also consider increasing the number of threads in the HIGH priority thread pool. | 8 |
| maxOpenFiles | Number of open files that can be used by the database.  You may need to increase this if your database has a large working set. When the value is set to -1, files that are opened are always kept open. | -1 |
| tableFormatConfig | Set the configuration for the table format. <br/> The default is BlockBasedTableConfig with <ul><li>noBlockCache = opposite of `useCache`</li><li>blockCacheSize = `cacheSize`</li><li>blockSize = `blockSize`</li><li> filter = BloomFilter(10,false)</li><li>formatVersion = 0</li></ul>The highlighted values are taken from the `rocksdb-blob-store` namespace / `RocksDBBlobStoreConfigurer` if provided, otherwise the following defaults are used: <ul><li>useCache = true</li><li>cacheSize = 100MB</li><li>blockSize = 16KB</li></ul>If a custom tableFormatConfig is provided, the values from the namespace/configurer are ignored. |  |

## LSM Logic and MemTables Tuning Parameters

Below are the values for the default class `com.com.gigaspaces.blobstore.rocksdb.config.XAPColumnFamilyOptions`.

{{%refer%}}
A list of configuration properties can be found in  the [org.rocksdb.ColumnFamilyOptionsInterface](https://github.com/facebook/rocksdb/blob/v5.5.1/java/src/main/java/org/rocksdb/AdvancedColumnFamilyOptionsInterface.java) Java docs.
{{%/refer%}}

| Property               | Description                                               | Value |
|:-----------------------|:----------------------------------------------------------|:--------|
| writeBufferSize | Amount of data to build up in memory (backed by an unsorted log on disk) <br/>before converting to a sorted on-disk file. Should be in bytes. | 64 MB |
| levelZeroSlowdownWritesTrigger | Soft limit on number of level-0 files. XAP begins to slow down writes at this point. <br />A value < 0 means that no writing slowdown is triggered by the number of files in level-0. | 8 |
| maxWriteBufferNumber | The maximum number of write buffers that are built up in memory. | 4 |
| targetFileSizeBase | The target file size for compaction. Should be in bytes. | 64 MB |
| softPendingCompactionBytesLimit | The soft limit to impose on compaction. | 0 | 
| hardPendingCompactionBytesLimit | The hard limit to impose on compaction. | 0 |
| levelCompactionDynamicLevelBytes | If true, RocksDB will pick the target size of each level dynamically. | false |
| maxBytesForLevelBase | The upper limit of the total size of level-1 files, in bytes. | 512 MB |
| compressionPerLevel | Sets the compression policy for each level. | [NO_COMPRESSION, <br /> NO_COMPRESSION, <br/>SNAPPY_COMPRESSION]  |
| mergeOperatorName | Sets the merge operator to be used for merging two merge operands of the same key. | put |
| fsync | By default, each write returns after the data is pushed to the operating system. The transfer from operating system memory to the underlying persistent storage happens asynchronously. When sync is configured to true, each write operation doesn't return until the data being written has been pushed all the way to persistent storage. <br/> This parameter should be set to true while storing data to file systems like ext3, which can lose files after a reboot. <br />If this property is set, the `fsync` that is passed to the `rocksdb-blob-store` namespace/`RocksDBBlobStoreConfigurer` (if any) is ignored. |  false |

## Examples


{{%tabs%}}
{{%tab "Namespace" %}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/rocksdb-blob-store"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/rocksdb-blob-store http://www.openspaces.org/schema/{{%currentversion%}}/rocksdb-blob-store/openspaces-rocksdb-blobstore.xsd">

 	<bean id="dbOptions" class="com.gigaspaces.blobstore.rocksdb.config.XAPDBOptions">
        <!-- This will override the default of 8 -->
        <property name="maxBackgroundCompactions" value="4" />
        <property name="maxBackgroundFlushes" value="4" />
    </bean>
    
    <bean id="dataColumnFamilyOptions" class="com.gigaspaces.blobstore.rocksdb.config.XAPColumnFamilyOptions">
        <property name="maxBytesForLevelMultiplier" value="20"/>
    </bean>

    <blob-store:rocksdb-blob-store id="myBlobStore" fsync="false"
        cache-size-mb="100" block-size-kb="16"
        db-options="dbOptions" data-column-family-options="dataColumnFamilyOptions"
        paths="[/tmp/rocksdb,/tmp/rocksdb2]" mapping-dir="/tmp/mapping"/>

    <os-core:embedded-space id="space" space-name="mySpace" >
        <os-core:blob-store-data-policy blob-store-handler="myBlobStore"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```

{{% /tab %}}
{{%tab "Java Code"%}}

```java
RocksDBBlobStoreConfigurer configurer = new RocksDBBlobStoreConfigurer()
        .setPaths("[/tmp/rocksdb,/tmp/rocksdb2]")
        .setMappingDir("/tmp/mapping")
        .setCacheSizeMB(100)
        .setBlockSizeKB(16)
        .setFsync(false);

XAPDBOptions dbOptions = new XAPDBOptions();
configurer.setDBOptions(dbOptions); // optional

XAPColumnFamilyOptions dataColumnFamilyOptions = new XAPColumnFamilyOptions();
configurer.setDataColumnFamilyOptions(dataColumnFamilyOptions); // optional

RocksDBBlobStoreHandler rocksDBBlobStoreHandler = configurer.create();

BlobStoreDataCachePolicy cachePolicy = new BlobStoreDataCachePolicy();
cachePolicy.setBlobStoreHandler(rocksDBBlobStoreHandler);

EmbeddedSpaceConfigurer embeddedSpaceConfigurer = new EmbeddedSpaceConfigurer("mySpace");
embeddedSpaceConfigurer.cachePolicy(cachePolicy);

gigaSpace = new GigaSpaceConfigurer(embeddedSpaceConfigurer.space()).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}
