---
type: post122
title:  MemoryXtend for Flash/SSD
categories: XAP122ADM, ENT
parent: memoryxtend-overview.html
weight: 200
---

# Introduction
XAP MemoryXtend for Flash/SSD delivers built-in high speed persistence leveraging local or attached SSD devices and all-flash-arrays (AFA). It delivers low latency write and read performance, as well as fast data recovery.  XAP MemoryXtend for Flash/SSD is based on {{%exurl "RocksDB""http://rocksdb.org/"%}} which is a persistent key/value store optimized for fast storage environments. 


# Architecture and Components
When configured for Flash/SSD, the MemoryXtend architecture tiers the storage of each space partition instance across two components: a space partition instance (managed JVM heap) and an embedded key/value store (the blob store) as shown in the diagram down below. 

{{%align center%}}
![image](/attachment_files/blobstore/memoryxtend-rocksdb-architecture.png)
{{%/align%}}
 
 
## Space Partition Instance
The space partition instance is a JVM heap which acts as a LRU cache against the underlying blob store. This tier in the architecture stores indexes, space class metadata, transactions, replciation redo log, leases and statistics. 
Upon a space read operation, if the object exists in the JVM heap (i.e. a cache <i>hit</i>) it will be immediately returned to the space proxy client. Otherwise, the space will load it from the underlying blob store and place it on the JVM heap (known as a cache <i>miss</i>). 


## Blob Store
The blob store is based on a log-structured merge tree architecture (similar to popular NoSQL databases such as: {{%exurl "HBase""https://hbase.apache.org/"%}}, {{%exurl "BigTable""https://cloud.google.com/bigtable/"%}}, or {{%exurl "Cassandra""https://cassandra.apache.org/"%}}). There are three main components in the blob store: 

- <b>MemTable</b>: An in-memory data structure (residing on off-heap RAM) where all incoming writes are stored. When a MemTable fills up, itis flushed to a SST file on storage. 
- <b>Log</b>: A write ahead log (WAL) which serializes MemTable operations to persistent medium as log files. In the event of failure, WAL files can be used to recover the key/value store to its consistent state, by reconstructing the MemTable from teh logs. 
- <b>Sorted String Table (SST) files</b>: SSTable is a data structure (residing on disk) to efficiently store a large data footprint while optimizing for high throughput, sequential read/write workloads. When a MemTable fills up, it is flushed to a SST file on storage and the corresponding write ahead log file can be deleted. 





# Configuration and Deployment
Any existing XAP space can be configured to integrate a blob store with it. As with a typical processing unit, configuration is done through `pu.xml` or code. For example: 

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

    <blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt/db1,/mnt/db2]" mapping-dir="/tmp/mapping"/>

    <os-core:embedded-space id="space" space-name="mySpace" >
        <os-core:blob-store-data-policy blob-store-handler="myBlobStore" persistent="true"/>
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

    <bean id="blobstoreid" class="com.gigaspaces.blobstore.rocksdb.config.RocksDBBlobStoreDataPolicyFactoryBean">
        <property name="paths" value="[/mnt/db1,/mnt/db2]"/>
        <property name="mappingDir" value="/tmp/mapping"/>
    </bean>

    <os-core:embedded-space id="space" space-name="mySpace">
        <os-core:blob-store-data-policy blob-store-handler="blobstoreid" persistent="true"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}
{{%tab "Code"%}}

```java
RocksDBBlobStoreConfigurer rocksDbConfigurer = new RocksDBBlobStoreConfigurer();
rocksDbConfigurer.setPaths("[/mnt/db1,/mnt/db2]");
rocksDbConfigurer.setMappingDir("/tmp/mapping");

BlobStoreDataCachePolicy blobStorePolicy = new BlobStoreDataCachePolicy();
blobStorePolicy.setBlobStoreHandler(rocksDbConfigurer.create());
blobStorePolicy.setPersistent(true);


EmbeddedSpaceConfigurer spaceConfigurer = new EmbeddedSpaceConfigurer("mySpace");
spaceConfigurer.cachePolicy(blobStorePolicy);

GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
```
{{% /tab %}}
{{% /tabs %}}

The following tables describes the configuration options used in `rocksdb-blob-store` above, along with other parameters that may be specified:

| Property               | Description                                               | Default | Use |
|:-----------------------|:----------------------------------------------------------|:--------|:--------|
| paths | A comma-separated array of mount paths used for each space partition's blob store. Th number of paths in the array shall correspond to the number of partition instances in the space (primaries and backups). For instance, for a two-partition space with no backups, `/mnt/db1` corresponds to the first partition, while `/mnt/db2` to the second one.  | | required |
| mapping-dir | A directory on the filesystem which contains the "partition to blob store" mapping file. This file is automatically generated by MemoryXtend. In the event of a parition re-location, the mapping file will be updated.  |  | required |
| central-storage | Specifies whether the deployment strategy is for [central storage](./memoryxtend-rocksdb-ssd.html#central-storage) (i.e. SAN configuration) or [local storage](./memoryxtend-rocksdb-ssd.html#local-storage) on each grid machine (default)| false | optional |
| db-options | Specifies the tuning parameters for the persistent data store in the underlying blob store. This includes SST formats, compaction settings and flushes. See [Performance Tuning](./memoryxtend-rocksdb-ssd.html#performance-tuning) section for details. | | optional |  
| <nobr>data-column-family-options<nobr> | Specifies the tuning parameters for the LSM logic and memory tables. See [Performance Tuning](./memoryxtend-rocksdb-ssd.html#performance-tuning) section for details.| | optional |



# Deployment Strategies
## Local Storage

This configuration allows each space partition instance (primary or backup) to use a machine-local storage device (SSD/HDD). With this approach, data locality is leveraged so that the devices local to the machine are used for reads/writes. The local storage deployment strategy provides an easy way to implement a "local persistent store" (also known as native persistence) pattern. 

<br>

{{%align center%}}
![image](/attachment_files/blobstore/memoryxtend-local-storage.png)
{{%/align%}}

<br>

## Central Storage

This deployment strategy works well with {{%exurl "storage area networks (SAN)" "http://en.wikipedia.org/wiki/Storage_area_network"%}}, which means that the disk drive devices are installed in a remote storage array but behave as if they're attached the the local machine. Most storage networks use the iSCSI or Fibre Channel protocol for communication between servers and disk drive devices. This configuration breaks the coupling between a partition instance and a local machin device, thereby allowing seamless relocation of paritions across data grid machines. 

{{%align center%}}
![image](/attachment_files/blobstore/memoryxtend-central-storage.png)
{{%/align%}}

Tiering storage between space partition instances and attached storage can be applied across one or more storage arrays, as shown in the configurations below: 

### Single storage array

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

### Two storage arrays

{{%section%}}
{{%column width="80%" %}}
The following example deployes a 2 partitions space with a single backup (2,1) in the following manner: 

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


# Data Recovery on Restart
The MemoryXtend architecture allows for data persisted on the blob store to be available for the data grid upon restart. There are two approaches for pre-loading data into the space: lazy and custom initial load. 

## Lazy Load
 
In this approach, MemoryXtend saves only indexes in RAM and the rest of the objects on disk. Meaning, no data is loaded into the JVM heap upon a restart. As read throughput increases from clients, most of the data will eventually load into the data grid RAM tier. This is a preferred approach when the volume of data persisted on flash far exceeds what can fit into memory. To enable lazy load, all what's needed is to enable the `persistent` option on the blobstore policy. 

```xml
  <os-core:blob-store-data-policy persistent="true" blob-store-handler="myBlobStore">
```

 

## Custom Initial Load
The `blob-store-cache-query` option provides a SQL criteria to pre-load all or specific space objects into the JVM heap upon data grid initialization. This guarantees any subsequent read request will hit RAM, thereby providing predictable latency (avoiding cache misses). This preload pattern is useful in two scenarios:

- The persisted data capacity equals what can fit into RAM (eager pre-load). 
- When read latencies for specific class type (e.g. hot data, current day stocks) need to be predictable upfront. 

In the example below we are loading `Stock` instances where the name=a1000 and `Trade` instances with id > 10000.

{{%tabs%}}
{{%tab "Namespace"%}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
 <beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:os-core="http://www.openspaces.org/schema/core"
        xmlns:os-events="http://www.openspaces.org/schema/events"
        xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
        xmlns:blob-store="http://www.openspaces.org/schema/rocksdb-blob-store"
        xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
        http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
        http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{%currentversion%}}/events/openspaces-events.xsd
        http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{%currentversion%}}/remoting/openspaces-remoting.xsd
        http://www.openspaces.org/schema/rocksdb-blob-store http://www.openspaces.org/schema/{{%currentversion%}}/rocksdb-blob-store/openspaces-rocksdb-blobstore.xsd">
 
     <blob-store:rocksdb-blob-store id="myBlobStore" paths="[/tmp/rocksdb]" mapping-dir="/tmp/mapping"/>
 
     <os-core:embedded-space id="space" name="mySpace">
         <os-core:blob-store-data-policy persistent="true" blob-store-handler="myBlobStore">
             <os-core:blob-store-cache-query class="com.gigaspaces.blobstore.rocksdb.Stock" where="name = a1000"/>
             <os-core:blob-store-cache-query class="com.gigaspaces.blobstore.rocksdb.Trade" where="id > 10000"/>
         </os-core:blob-store-data-policy>
     </os-core:embedded-space>
 
     <os-core:giga-space id="gigaSpace" space="space"/>
 ```
 {{%/tab%}}
 
 {{%tab "Code"%}}
 
```java
 BlobStoreDataCachePolicy blobStorePolicy = new BlobStoreDataCachePolicy();
 blobStorePolicy.setBlobStoreHandler(rocksDbConfigurer.create());
 blobStorePolicy.setPersistent(true);
 blobStorePolicy.addCacheQuery(new SQLQuery(Stock.class, "name = a1000"));
 blobStorePolicy.addCacheQuery(new SQLQuery(Trade.class, "id > 10000"));
 
 EmbeddedSpaceConfigurer spaceConfigurer = new EmbeddedSpaceConfigurer("mySpace");
 spaceConfigurer.cachePolicy(blobStorePolicy);
 GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer.space()).gigaSpace();
```
{{%/tab%}}
{{%/tabs%}}

When the logging `com.gigaspaces.cache` is turned on the following output is generated:

```bash
2016-12-26 07:57:56,378  INFO [com.gigaspaces.cache] - BlobStore internal cache recovery:
blob-store-queries: [SELECT * FROM com.gigaspaces.blobstore.rocksdb.Stock WHERE name = 'a1000', SELECT * FROM com.gigaspaces.blobstore.rocksdb.Stock.Trade WHERE id > 10000].
Entries inserted to blobstore cache: 80.
```




# Performance Tuning

## Persistent Data Store Tuning Parameters
XAP uses the default DBOptions class `com.com.gigaspaces.blobstore.rocksdb.config.XAPDBOptions`.

{{%refer%}}
A list of configuration properties can be found in  the {{%exurl "org.rocksdb.DBOptionsInterface" "https://github.com/facebook/rocksdb/blob/master/java/src/main/java/org/rocksdb/DBOptionsInterface.java"%}} Java docs.
{{%/refer%}}


| Property               | Description                                               | Value |
|:-----------------------|:----------------------------------------------------------|:--------|
| createIfMissing | Configure whether to create the database if it is missing. Note that this value is always overriden with `true`. | true |
| maxBackgroundCompactions | Specifies the maximum number of concurrent background compaction jobs, submitted to the default LOW priority thread pool. <br /> If you're increasing this, also consider increasing number of threads in LOW priority thread pool | 8 |
| maxBackgroundFlushes | Specifies the maximum number of concurrent background flush jobs. <br />If you're increasing this, also consider increasing number of threads in HIGH priority thread pool. | 8 |
| maxOpenFiles | Number of open files that can be used by the DB.  You may need to increase this if your database has a large working set. Value -1 means files opened are always kept open. | -1 |
| tableFormatConfig | Set the config for table format. <br/> Default is BlockBasedTableConfig with <ul><li>noBlockCache = opposite of `useCache`</li><li>blockCacheSize = `cacheSize`</li><li>blockSize = `blockSize`</li><li> filter = BloomFilter(10,false)</li><li>formatVersion = 0</li></ul>The highlighted values are taken from the `rocksdb-blob-store` namespace / `RocksDBBlobStoreConfigurer` if provided, otherwise the following defaults will be used: <ul><li>useCache = true</li><li>cacheSize = 100MB</li><li>blockSize = 16KB</li></ul>If a custom tableFormatConfig is provided, the values from the namespace/configurer are ignored. |  |

## LSM Logic and MemTables Tuning Parameters
Below are the values for the default class `com.com.gigaspaces.blobstore.rocksdb.config.XAPColumnFamilyOptions`.

{{%refer%}}
A list of configuration properties can be found in  the [org.rocksdb.ColumnFamilyOptionsInterface](https://github.com/facebook/rocksdb/blob/master/java/src/main/java/org/rocksdb/ColumnFamilyOptionsInterface.java) Java docs.
{{%/refer%}}

| Property               | Description                                               | Value |
|:-----------------------|:----------------------------------------------------------|:--------|
| writeBufferSize | Amount of data to build up in memory (backed by an unsorted log on disk) <br/>before converting to a sorted on-disk file. Should be in bytes. | 64 MB |
| levelZeroSlowdownWritesTrigger | Soft limit on number of level-0 files. We start slowing down writes at this point. <br />A value < 0 means that no writing slow down will be triggered by number of files in level-0. | 8 |
| maxWriteBufferNumber | The maximum number of write buffers that are built up in memory. | 4 |
| targetFileSizeBase | The target file size for compaction, should be in bytes. | 64 MB |
| softRateLimit | The soft-rate-limit of a compaction score for put delay. | 0 | 
| hardRateLimit | The hard-rate-limit of a compaction score for put delay. | 0 |
| levelCompactionDynamicLevelBytes | If true, RocksDB will pick target size of each level dynamically. | false |
| maxBytesForLevelBase | The upper-bound of the total size of level-1 files in bytes. | 512 MB |
| compressionPerLevel | Sets the compression policy for each level | [NO_COMPRESSION, <br /> NO_COMPRESSION, <br/>SNAPPY_COMPRESSION]  |
| mergeOperatorName | Set the merge operator to be used for merging two merge operands of the same key. | put |
| fsync | By default, each write returns after the data is pushed into the operating system. The transfer from operating system memory to the underlying persistent storage happens asynchronously. When configuring sync to true each write operation not return until the data being written has been pushed all the way to persistent storage. <br/> This parameter should be set to true while storing data to filesystem like ext3 that can lose files after a reboot. <br />Default is false. If this property is set, the `fsync` that is passed to the `rocksdb-blob-store` namespace/`RocksDBBlobStoreConfigurer` (if any) will be ignored. |  false |

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
{{%tab "Code"%}}
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
