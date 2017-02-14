---
type: post121
title:  Configuration
categories: XAP121ADM, ENT
parent: memoryxtend-rocksdb-ssd.html 
weight: 100
---

The RocksDB based MemoryXtend add-on allows configuring the `DBOptions` and `ColumnFamilyOptions` that control the behavior of the database.

## XAPDBOptions
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

## XAPColumnFamilyOptions
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
