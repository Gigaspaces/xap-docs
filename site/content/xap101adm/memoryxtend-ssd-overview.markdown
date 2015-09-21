---
type: post101
title:  Overview
categories: XAP101ADM
parent: memoryxtend-ssd.html
weight: 200
---


{{% ssummary %}}  {{% /ssummary %}}

# Supported SSD Devices

All Enterprise flash drives are supported. SanDisk, Fusion-IO, IntelÂ® SSD , etc are supported with the IMDG storage technology. Central SSD (RAID) devices such as Tegile, Cisco Whiptail, DSSD, and Violin Memory are also supported.

# Supported XAP APIs

All XAP APIs are supported with the BlobStore configuration. This includes the Space API (POJO and Document), JDBC API, JPA API, JMS API, and Map API. 

# How It Works?

XAP is using [SanDisk ZetaScale](http://www.sandisk.com/enterprise/zetascale) library, which uses direct flash access. It circumvents OS level storage interfaces when writing an object to the space, its indexed data maintained on Heap where the Storage interface implementing using the ZetaScale libraries to interact with the underlying flash drive.

![blobstore2.jpg](/attachment_files/blobstore2.jpg)

The indexes maintain in RAM (on-heap) allowing the XAP query engine to evaluate the query without accessing the raw data stored on the flash device. This allows XAP to execute SQL based queries extremely efficiently even across large number of nodes. All XAP Data Grid APIs are supported including distributed transactions, leasing (Time To live) , FIFO , batch operations , etc. All clustering topologies supported. All client side cache options are supported.

# The BlobStore Configuration

The BlobStore settings includes the following options:


| Property               | Description                                               | Default | Use |
|:-----------------------|:----------------------------------------------------------|:--------|:--------|
| devices | Flash devices. Comma separated available devices. The list used as a search path from left to right. The first one exists will be used. |  | required |
| volume-dir | Directory path contains a symbolic link to the the SSD device. | | required |
| blob-store-capacity-GB | Flash device allocation size in Gigabytes. | 200 | optional |
| <nobr>blob-store-cache-size-MB</nobr> | ZetaScale internal LRU based off-heap in-process cache size in Megabytes. Keeps data in serialized format. | 100 | optional |
| write-mode | `WRITE_THRU` - the data grid writes the data immediately into the blobstore and synchronously acknowledge the write after ZetaScale fully commits the operation. `WRITE_BEHIND` - the data grid writes the data immediately into the blobstore. ZetaScale asynchronously commits the operation to the SSD. This option improves write performance but may have a consistency issue with a sudden hardware failure.| `WRITE_THRU` | optional |
| enable-admin | ZetaScale admin provides a simple command line interface (CLI) through a TCP port. ZetaScale CLI uses port 51350 by default. This port can be changed through the configuration parameter `FDF_ADMIN_PORT`. | false |
| statistics-interval | Applications can optionally enable periodic dumping of statistics to a specified file (XAP_HOME/logs). The value represents the statistics dump interval, dump stats after X operations.| disabled| optional |
| durability-level | `SW_CRASH_SAFE` - Guarantees no data loss in the event of software crashes. But some data might be lost in the event of hardware failure.{{%wbr%}}`HW_CRASH_SAFE`- Guarantees no data loss if the hardware crashes.Since there are performance implication it is recommended to work with NVRAM device and configure log-flash-dir to a folder on this device. | SW_CRASH_SAFE | optional |
| log-flush-dir | When `HW_CRASH_SAFE` used , point to a directory in a file system on top of NVRAM backed disk. This directory must be unique per space, you can add ${clusterInfo.runningNumber} as suffix to generate a unique name | as volume-dir | optional |
| devices-mapping-dir | Point to a directory in a file system. This directory contains file which contains a mapping between space name and a flash device | /tmp/blobstore/devices | optional |
| central-storage | Enable in case you have a centralized. in this case each space is connected to a predefined device| false | optional |

The IMDG BlobStore settings includes the following options:{{%wbr%}}

| Property | Description | Default | Use |
|:---------|:------------|:--------|:--------|
| blob-store-handler | BlobStore implementation |  | required |
| <nobr>cache-entries-percentage</nobr> | On-Heap cache stores objects in their native format. This cache size determined based on the percentage of the GSC JVM max memory(-Xmx). If `-Xmx` is not specified the cache size default to `10000` objects. This is an LRU based data cache.| 20% | optional |
| avg-object-size-KB |  Average object size. | 5KB | optional |
| recover-from-blob-store |  Whether to recover from blob store or not | true | optional |

# Prerequisites

- MemoryXtend currently supports Linux CentOS 6.x only. 

- Check that your user is part of disk groups.

```bash
$ groups
```

If your user is not part of disk groups, add it by calling:

```bash
$ sudo usermod -G disk <username>
```

and re-login.

The number of of flash devices/partitions should be aligned with the space instances number that you want to deploy on a machine.
For creating partitions you can use fdisk like explained [here](http://www.howtogeek.com/106873/how-to-use-fdisk-to-manage-partitions-on-linux/).

- Make sure your user has read/write permissions to flash devices
- Make sure your user has read/write permissions to /tmp


# Installation

Step 1. 
Download the XAP 10 distribution and the `MemoryXtend` RPM with the ZetaScale libraries.

Step 2. 
build-filename
Install XAP as usual. Unzip the `{{%version "build-filename"%}}`.

Step 3. 
Install ZetaScale libraries:


```bash
blobstore
$ sudo XAP_HOME=<XAP HOME> sh -c "yum -y install {{%version "blobstore"%}}.rpm"
```

If the RPM installation fails , please run  the following `yum` install commands using `root` user:

```bash
sudo yum -y install snappy
sudo yum -y install snappy-devel
sudo yum -y install libaio
sudo yum -y install libaio-devel
sudo yum -y install libevent
sudo yum -y install libevent-devel
sudo yum -y install glibc-devel
```

Blobstore rpm installs SanDisk license at XAP HOME/lib/platform/blobstore/fdf-license.txt, in case this license is expired, a valid license is avalable for download.

Step 4. 
Use the `XAP HOME\bin\gs-agent-blobstore.sh` to start GigaSpaces Grid Agent that configured to load the ZetaScale libraries.

# Uninstall

To uninstall the blobstore libraries run the following command:

```bash
blobstore
$ sudo XAP_HOME=<XAP HOME> sh -c "yum -y remove {{%version "blobstore"%}}"
```

# Configuration
Configuring an IMDG (Space) with BlobStore should be done via the `SanDiskBlobStoreDataPolicyFactoryBean`, or the `SanDiskBlobStoreConfigurer`. For example:

## PU XML Configuration
{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/blob-store"
spring
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/blob-store http://www.openspaces.org/schema/{{%currentversion%}}/blob-store/openspaces-blobstore.xsd">

    <bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"/>

    <blob-store:sandisk-blob-store id="myBlobStore" blob-store-capacity-GB="100" blob-store-cache-size-MB="100" statistics-interval="5000"
                                            devices="[/dev/sdb1,/dev/sdc1]" volume-dir="/tmp/data${clusterInfo.runningNumber}" durability-level="SW_CRASH_SAFE">

    </blob-store:sandisk-blob-store>

    <os-core:embedded-space id="space" name="mySpace" >
        <os-core:blob-store-data-policy blob-store-handler="myBlobStore" cache-entries-percentage="10" avg-object-size-KB="5" recover-from-blob-store="true"/>
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
spring
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd">

    <bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"/>

    <bean id="blobstoreid" class="com.gigaspaces.blobstore.storagehandler.SanDiskBlobStoreHandler">
        <property name="blobStoreCapacityGB" value="200"/>
        <property name="blobStoreCacheSizeMB" value="50"/>
        <property name="blobStoreDevices" value="[/dev/xvdb,/dev/xvdc]"/>
        <property name="blobStoreVolumeDir" value="/tmp/data${clusterInfo.runningNumber}"/>
        <property name="blobStoreDurabilityLevel" value="SW_CRASH_SAFE"/>
    </bean>

    <os-core:embedded-space id="space" name="mySpace">
        <os-core:blob-store-data-policy blob-store-handler="blobstoreid" cache-entries-percentage="10"
            avg-object-size-KB="5" recover-from-blob-store="true"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```

{{% /tab %}}
{{%tab "  Code "%}}


## Programmatic API
Programmatic approach to start a BlobStore space:

```java
SanDiskBlobStoreConfigurer configurer = new SanDiskBlobStoreConfigurer();
configurer.addDevices("[/dev/xvdb,/dev/xvdc]");
configurer.addVolumeDir("/tmp");
configurer.setBlobStoreCapacityGB(200);
configurer.setBlobStoreCacheSizeMB(50);
configurer.setDurabilityLevel(DurabilityLevel.SW_CRASH_SAFE);

SanDiskBlobStoreHandler blobStoreHandler = configurer.create();
BlobStoreDataCachePolicy cachePolicy = new BlobStoreDataCachePolicy();
cachePolicy.setAvgObjectSizeKB(5l);
cachePolicy.setCacheEntriesPercentage(10);
cachePolicy.setRecoverFromBlobStore(true);
cachePolicy.setBlobStoreHandler(blobStoreHandler);

EmbeddedSpaceConfigurer urlConfig = new EmbeddedSpaceConfigurer("mySpace");
urlConfig.cachePolicy(cachePolicy);

gigaSpace = new GigaSpaceConfigurer(urlConfig).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

The above example:

- Configures the SanDisk BlobStore bean.
- Configures the Space bean (Data Grid) to use the blobStore implementation. 

### Consistency Level

In case you are deploying a highly available data grid (Space with backups) make sure you configure the [consistency-level](./consistency-level.html)  to all.

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
    ...	
    <os-core:embedded-space id="space" name="mySpace" >
        <os-core:blob-store-data-policy blob-store-handler="blobstoreid" cache-entries-percentage="10" avg-object-size-KB="5" recover-from-blob-store="true"/>
	 <os-core:properties>
            <props>
                <prop key="cluster-config.groups.group.repl-policy.sync-replication.consistency-level">all</prop>
            </props>
        </os-core:properties>
    </os-core:embedded-space>
   ...
```
{{% /tab %}}

{{%tab "  Code "%}}


## Programmatic API
Programmatic approach to start a BlobStore space:

```java
...
 SanDiskBlobStoreHandler sanDiskBlobStoreHandler = configurer.create();
 BlobStoreDataCachePolicy cachePolicy = new BlobStoreDataCachePolicy();
 cachePolicy.setAvgObjectSizeKB(10l);
 cachePolicy.setCacheEntriesPercentage(0);
 cachePolicy.setRecoverFromBlobStore(true);
 cachePolicy.setBlobStoreHandler(sanDiskBlobStoreHandler);

 UrlSpaceConfigurer urlConfig = new UrlSpaceConfigurer(spaceURL);
 urlConfig.cachePolicy(cachePolicy);
 urlConfig.addProperty("cluster-config.groups.group.repl-policy.sync-replication.consistency-level", "all");
 gigaSpace = new GigaSpaceConfigurer(urlConfig.space()).gigaSpace();
...
```

{{% /tab %}}
{{% /tabs %}}

# Automatic Data Recovery and ReIndexing

Once the Data grid is shutdown and redeployed it may reload its entire data from its flash drive store. Loading data from a local drive may provide fast data recovery - much faster than loading data from a central database. The data reload process iterate the data on the flash drive and generate the indexed data based on the indexed data list per space class. As each data grid partition perform this reload and reindexing process in parallel across multiple servers it may complete this indexing process relatively fast. With a single machine 8 cores, running 4 partitions data grid with four SSD drives , 100,000 items / second (1K payload) may be scanned and re-indexed. To enable the Data Recovery and ReIndexing activity the `recover-from-blob-store` should be set to `true`.

To allow the data grid to perform an automatic data recovery from the right flash device on startup you should use [Instance level SLA](./the-sla-overview.html) .

With this SLA you control where a specific space instance will be provisioned. You may find a bloblstore data grid processing unit template at `XAP_HOME/deploy/templates/blobstore-datagrid`. Within this template there is an sla configuration file `blobstore-datagrid/META_INF/spring/sla.xml` you may use.

You can copy the `XAP_HOME/deploy/templates/blobstore-datagrid` into `XAP_HOME/deploy` with the same folder name or a different name to have a customized blobstore-datagrid PU. 

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


## Device Allocation

The device allocation per a machine is managed via the `/tmp/blobstore/devices/device-per-space.properties` file. You can specify this file location using the `com.gs.blobstore-devices` system property when setting the `GSC_JAVA_OPTIONS`. Each time a new blobstore space is deployed an Entry is added to this file listing the data grid instances provisioned on the machine.

# Central Storage Support

BlobStore supports [`storage area network (SAN)`](http://en.wikipedia.org/wiki/Storage_area_network) which means the disk drive devices are in another machine but appear like locally attached.
Most storage networks use the iSCSI or Fibre Channel protocol for communication between servers and disk drive devices.


In central storage mode each space is attached to a pre defined device as explained on these examples:

#### Single storage array

{{%section%}}
{{%column width="80%" %}}
If we deploy a partitioned Space with a single backup (2,1), the first primary will be attached to `/dev/sda1`, the second primary will be attached to `/dev/sdb1`, the first backup will be attached to `/dev/sda2` and the second backup will be attached to `/dev/sdb2`.

{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/ssd/ssd-single-device.png"%}}
{{%/column%}}
{{%/section%}}

Configuration :


```xml
<blob-store:sandisk-blob-store id="myBlobStore" blob-store-capacity-GB="100"
    blob-store-cache-size-MB="100"
    devices="[/dev/sda1,/dev/sdb1,/dev/sda2,/dev/sdb2]"
    volume-dir="/tmp/data${clusterInfo.runningNumber}"
    central-storage="true">
</blob-store:sandisk-blob-store>
```




The BlobStore also supports deployment with 2 different storage arrays. With this feature you can ensure that a primary and its backup(s)
cannot be provisioned to the same storage array.

#### Two storage arrays:

{{%section%}}
{{%column width="80%" %}}
If we deploy a partitioned Space with a single backup (2,1), the first primary will be attached to `/dev/sda1`, the second primary will be attached to `/dev/sdb1`, the first backup will be attached to `/dev/sdc1` and the second backup will be attached to `/dev/sdc2`.
{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/ssd/ssd-multiple-device.png"%}}
{{%/column%}}
{{%/section%}}

Configuration :


```xml
<blob-store:sandisk-blob-store id="myBlobStore"
    blob-store-capacity-GB="100"
    blob-store-cache-size-MB="100"
    devices="[/dev/sda1,/dev/sdb1],[/dev/sdc1,/dev/sdd1]"
    volume-dir="/tmp/data${clusterInfo.runningNumber}"
    central-storage="true">
</blob-store:sandisk-blob-store>

```



# BlobStore Space re-deploy

When you undeploy a blobstore space use the `XAP_HOM/bin/undeploy-grid.groovy` that comes with the RPM. It undeploys the blobstore space and restarts all its GSCs.

```bash
export PATH=$PATH:/gigaspaces-xap-premium-{{%currentversion%}}.0/bin/tools/groovy/bin/
cd /gigaspaces-xap-premium-{{%currentversion%}}/bin/bin
$ groovy undeploy-grid.groovy <LUS HostName> <BlobStore-Space-Name>
```


# Controlling blobStore mode at the Space Class Level
By default any Space Data Type is `blobStore` enabled. When decorating the space class with its meta data you may turn off the `blobStore` behavior using the `@SpaceClass blobstoreEnabled` annotation or gs.xml `blobstoreEnabled` tag.

Here is a sample annotation disabling `blobStore` mode:


```java
@SpaceClass(blobstoreEnabled = false)
public class Person {
    .......
}
```

Here is a sample xml decoration for a POJO class disabling `blobStore` mode
