---
type: post110
title:  SSD - RocksDB Add-On
categories: XAP110ADM
parent: memoryxtend-overview.html
weight: 200
canonical: auto
---

# Native Persistence
XAP Native Persistence delivers built-in high speed persistence using HDD or SSD. It delivers low latency write and read performance & fast data recovery.  XAP Native Persistence is based on {{%exurl "RocksDB""http://rocksdb.org/"%}} that is an embeddable persistent key-value store for fast storage on regualr drives and flash devices. 
It was developed at Facebook and is now a popular {{%giturl "open source project""https://github.com/facebook/rocksdb"%}}.
XAP Native Persistence is part of the [MemoryXtend](./memoryxtend.html) add-on. 

{{%refer%}}
If you're not familiar with MemoryXtend, make sure you read its [documentation](./memoryxtend.html) before proceeding. We also recommend reading the [RocksDB documentation](https://github.com/facebook/rocksdb/wiki).
{{%/refer%}}

{{% info "License"%}}
The MemoryXtend add-on is available for free during the evaluation period, but is not included in the premium edition license. For information about purchasing this add-on please contact [GigaSpaces Customer Support](http://www.gigaspaces.com/content/customer-support-services).
{{% /info %}}



# Prerequisites
- Java 7 (or later)
- Currently supports Linux only (Windows support will be available in the future)
- Read/Write permissions to mounted devices/partitions
- The number of mounted devices/partitions should match the number of space instances that will be deployed on the machine.
  - For creating partitions you can use `fdisk` like explained [here](http://www.howtogeek.com/106873/how-to-use-fdisk-to-manage-partitions-on-linux/).




<br>

# Configuration


{{%align center%}}
![image](/attachment_files/blobstore/rockdb4.png)
{{%/align%}}

<br>

Creating a space with the RocksDB add-on can be done via `pu.xml` or code. For example:

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

    <blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt/db1,/mnt/db2]" 
    			mapping-dir="/tmp/mapping"/>

    <os-core:embedded-space id="space" name="mySpace" >
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

    <os-core:embedded-space id="space" name="mySpace">
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

In addition to the general [MemoryXtend configuration options](./memoryxtend.html#configuration), the RocksDB MemoryXtend add-on supports the following configuration options:

| Property               | Description                                               | Default | Use |
|:-----------------------|:----------------------------------------------------------|:--------|:--------|
| paths | Comma separated available or new RocksDB folder locations. A path is a mounting point to a flash device. The list used as a search path from left to right. The first one exists will be used. | | required |
| mapping-dir | Point to a directory in a file system. This directory contains file which contains a mapping between space name and a RocksDB location. |  | required |
| central-storage | Enable in case you have a centralized storage. In this case each space is connected to a predefined RocksDB mounted location. | false | optional |
| db-options | RocksDB db options <br/> See DB Options in the [configuration page](./memoryxtend-rocksdb-ssd-configuration.html).| | optional |  
| <nobr>data-column-family-options<nobr> | RocksDB column family options. <br/> See Column Family Options in the [configuration page](./memoryxtend-rocksdb-ssd-configuration.html).| | optional |
| fsync |  This value is passed to XAPDBOptions. If `useFsync` is provided to the XAPDBOptions then this value is ignored. <br/>See XAPDBOptions in [configuration page](./memoryxtend-rocksdb-ssd-configuration.html). | false | optional |   
| block-size-kb | This value is passed to the default TableFormatConfig, should be in KB. If a custom TableFormatConfig is provided, this value is ignored. <br/>See XAPDBOptions in [configuration page](./memoryxtend-rocksdb-ssd-configuration.html). | 16 | optional |
| cache-size-mb | This value is passed to the default TableFormatConfig, should be in MB. If a custom TableFormatConfig is provided, this value is ignored. <br/>See XAPDBOptions in [configuration page](./memoryxtend-rocksdb-ssd-configuration.html).<br />Zero value means no caching.  | 100 | optional |


## Allocation

RocksDB is created on a given directory path, RocksDB path allocation per a machine is managed via the `/tmp/blobstore/paths/path-per-space.properties` file. Each time a new blobstore space is deployed an entry is added to this file listing the data grid instances provisioned on the machine.


# Local Storage

This configuration allows each Space instance within a cluster (primary or backup) to use a dedicated storage device (SSD / HDD). With this approach, primary instances using their local storage media to preserve the data, replicating to backup instances as well use their local storage media to preserve the data. Each Space instance once provisioned performs its data recovery (if enabled) from its local storage. This configuration will work well for development and small / medium data grids.


<br>

{{%align center%}}
![image](/attachment_files/blobstore/rockdb1.png)
{{%/align%}}

<br>

Since each Space instance is using a local storage device, its survivability is low as there is no hardware level high-availability is running, keeping the data safe in another device in case the entire machine running the Space instance completely fails. The central storage option below is leveraging a single storage appliance or multiple appliances which offers much better data survivability and much larger data storage capacity.


# Central Storage

The RocksDB Add-on supports [storage area network (SAN)](http://en.wikipedia.org/wiki/Storage_area_network) which means the disk drive devices are installed in a remote machine but behave as if they're attached the the local machine. Most storage networks use the iSCSI or Fibre Channel protocol for communication between servers and disk drive devices.

In central storage mode each space is attached to a pre-defined device as explained on these examples:

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
It is also possible to define two storage arrays instead of one, which will guarantee that the primary and backup instance of a partition will not be provisioned to the same storage array. The following example deployes a 2 partitions space with a single backup (2,1) in the following manner: 

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
