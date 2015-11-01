---
type: post110
title:  RocksDB
categories: XAP110ADM
parent: memoryxtend.html
weight: 110
---

{{% ssummary %}}  {{% /ssummary %}}

# How It Works

XAP is using [RocksDB](http://rocksdb.org/) an embeddable persistent key-value store for fast storage. Keys and values are arbitrary byte streams.It was developed at Facebook based on LevelDB , RocksDB can be configured to run on data on pure memory, flash, hard disks or on HDFS.
XAP indexes are maintained in RAM (on-heap) allowing the XAP query engine to evaluate the query without accessing the raw data stored on the mounted flash device. This allows XAP to execute SQL based queries extremely efficiently even across large number of nodes. All XAP Data Grid APIs are supported including distributed transactions, leasing (Time To live) , FIFO , batch operations , etc. All clustering topologies supported. All client side cache options are supported.

![ssdRocksDB.png](/attachment_files/ssd/ssdRocksDB.png)

# Prerequisites

- Java 7 (or later)
- Currently supports Linux only - Windows support is not available yet
- Read/Write permissions to mounted flash devices

For best performance results the number of mounted flash devices/partitions should be aligned with the space instances number that you want to deploy on a machine.
For creating partitions you can use fdisk like explained [here](http://www.howtogeek.com/106873/how-to-use-fdisk-to-manage-partitions-on-linux/).

# The BlobStore Configuration

The BlobStore settings includes the following options:


| Property               | Description                                               | Default | Use |
|:-----------------------|:----------------------------------------------------------|:--------|:--------|
| paths | Comma separated available or new RocksDB folder locations.A path is a mounting point to a flash device.The list used as a search path from left to right.The first one exists will be used. |  | required |
| mapping-dir | Point to a directory in a file system. This directory contains file which contains a mapping between space name and a RocksDB location. |  | required |
| central-storage | Enable in case you have a centralized storage. in this case each space is connected to a predefined RocksDB mounted location. | false | optional |
| options | RocksDB configuration options | | optional |  
| strategy-type |  Merge or Override given options with XAP default RocksDB options. | merge | optional | 
| sync |  By default, each write returns after the data is pushed into the operating system. The transfer from operating system memory to the underlying persistent storage happens asynchronously. When configuring sync to true each write operation not return until the data being written has been pushed all the way to persistent storage. | false | optional |                                

The IMDG BlobStore settings includes the following options:{{<wbr>}}


| Property | Description | Default | Use |
|:---------|:------------|:--------|:--------|
| blob-store-handler | BlobStore implementation |  | required |
| <nobr>cache-entries-percentage</nobr> | On-Heap cache stores objects in their native format. This cache size determined based on the percentage of the GSC JVM max memory(-Xmx). If `-Xmx` is not specified the cache size default to `10000` objects. This is an LRU based data cache.| 20% | optional |
| avg-object-size-KB |  Average object size. | 5KB | optional |
| persistent |  data is written to flash, space will perform recovery from flash if needed.  | | required |

# Configuration
Configuring an IMDG (Space) with BlobStore should be done via the `RocksDBBlobStoreDataPolicyFactoryBean`, or the `RocksDBBlobStoreConfigurer`. For example:

## PU XML Configuration
{{%tabs%}}
{{%tab "  Namespace "%}}


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

    <blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt/db1,/mnt/db2]"/>

    <os-core:embedded-space id="space" name="mySpace" >
        <os-core:blob-store-data-policy blob-store-handler="myBlobStore" cache-entries-percentage="10" avg-object-size-KB="5" persistent="true"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}

{{%tab "  Plain XML "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd">

    <bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"/>

    <bean id="blobstoreid" class="com.gigaspaces.blobstore.rocksdb.RocksDBBlobStoreHandler">
        <property name="paths" value="[/mnt/db1,/mnt/db2]"/>
    </bean>

    <os-core:embedded-space id="space" name="mySpace">
        <os-core:blob-store-data-policy blob-store-handler="blobstoreid" cache-entries-percentage="10"
            avg-object-size-KB="5" persistent="true"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```

{{% /tab %}}
{{%tab "  Code "%}}


## Programmatic API
Programmatic approach to start a BlobStore space:

```java
RocksDBBlobStoreConfigurer configurer = new RocksDBBlobStoreConfigurer();
configurer.addPaths("[/mnt/db1,/mnt/db2]");

RocksDBBlobStoreConfigurer blobStoreHandler = configurer.create();
BlobStoreDataCachePolicy cachePolicy = new BlobStoreDataCachePolicy();
cachePolicy.setAvgObjectSizeKB(5l);
cachePolicy.setCacheEntriesPercentage(10);
cachePolicy.setPersistent(true);
cachePolicy.setBlobStoreHandler(blobStoreHandler);

EmbeddedSpaceConfigurer urlConfig = new EmbeddedSpaceConfigurer("mySpace");
urlConfig.cachePolicy(cachePolicy);

gigaSpace = new GigaSpaceConfigurer(urlConfig).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

The above example:

- Configures the RocksDB BlobStore bean.
- Configures the Space bean (Data Grid) to use the blobStore implementation. 

# Automatic Data Recovery and ReIndexing

Once the Data grid is shutdown and redeployed it may reload its entire data from its flash drive store. Loading data from a local drive may provide fast data recovery - much faster than loading data from a central database. The data reload process iterate the data on the flash drive and generate the indexed data based on the indexed data list per space class. As each data grid partition perform this reload and reindexing process in parallel across multiple servers it may complete this indexing process relatively fast. With a single machine 8 cores, running 4 partitions data grid with four SSD drives , 100,000 items / second (1K payload) may be scanned and re-indexed. To enable persistency data recovery and ReIndexing activity the `persistent` should be set to `true`.

To allow the data grid to perform an automatic data recovery from the right mounted flash device on startup you should use [Instance level SLA](./the-sla-overview.html) .

## SLA Examples

### Partitioned with a backup SLA
With the following `sla.xml` example we have a single partition with a backup where the first instance is provisioned into `HostA` , and the second instance for the same partition is provisioned into `HostB`.

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

### Partitioned without a backup SLA

With the following `sla.xml` we have a partitioned (2 partitions) data grid without backups SLA example where both instances are provisioned into the `HostA`:

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


{{% note %}} Make sure you provide the `sla.xml` location at the deploy time (`-sla` deploy command option) or locate it at the root of the processing unit jar or under the `META-INF/spring` directory, alongside the processing unit's `pu.xml` file. {{% /note %}}

## Last Primary

ln order to prevent loss of data by selecting the least-updated space as primary the system keeps the id of the primary space for each partition. When a partition is brought up the primary election mechanism will elect a primary space randomly (or on basis of first-ready) but wait for the last primary to take the role of primary space. If the last primary cannot be resolved manual user intervention is required.
The current default implementation is based on shared file on NFS.


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

    <blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt/db1,/mnt/db2]"/>

    <os-core:embedded-space id="space" name="mySpace" >
        <os-core:blob-store-data-policy blob-store-handler="myBlobStore" cache-entries-percentage="10" avg-object-size-KB="5" persistent="true"/>
        <os-core:attribute-store store-handler="attributeStoreHandler"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```

The above example:

- Configures the RocksDB BlobStore bean.
- Configures the Space bean (Data Grid) to use the blobStore implementation, Last Primary state is kept at shared a file lastprimary.properties. 


# Central Storage Support

BlobStore supports [`storage area network (SAN)`](http://en.wikipedia.org/wiki/Storage_area_network) which means the disk drive devices are in another machine but appear like locally attached.
Most storage networks use the iSCSI or Fibre Channel protocol for communication between servers and disk drive devices.


In central storage mode each space is attached to a pre defined device as explained on these examples:

#### Single storage array

{{%section%}}
{{%column width="80%" %}}
If we deploy a partitioned Space with a single backup (2,1), the first primary will use `/mnt/db1`, the second primary will use `/mnt/db2`, the first backup will use `/mnt/db3` and the second backup will use `/mnt/db4`.

{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/ssd/ssdRocksDBCentral.png"%}}
{{%/column%}}
{{%/section%}}

Configuration :


```xml
<blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt/db1,/mnt/db2,/mnt/db3,/mnt/db4]"/>
```

The BlobStore also supports deployment with 2 different storage arrays. With this feature you can ensure that a primary and its backup(s)
cannot be provisioned to the same storage array.

#### Two storage arrays:

{{%section%}}
{{%column width="80%" %}}
If we deploy a partitioned Space with a single backup (2,1), the first primary will use `/mnt/db1`, the second primary will use `/mnt/db2`, the first backup will use `/mnt/db3` and the second backup will use `/mnt/db4`.
{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/ssd/ssdRocksDB.png"%}}
{{%/column%}}
{{%/section%}}

Configuration :


```xml
<blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt/db1,/mnt/db2],[/mnt/db3,/mnt/db4]"/>

```

## Allocation

RocksDB is created on  a given directory path, RocksDB path allocation per a machine is managed via the `/tmp/blobstore/paths/path-per-space.properties` file. Each time a new blobstore space is deployed an entry is added to this file listing the data grid instances provisioned on the machine.

If 2 arrays are configured in central storage, a primary and it's backup will not attached to devices from the same array.  
