---
type: post110
title:  MemoryXtend Overview
categories: XAP110ADM
parent: memoryxtend-overview.html
weight: 100
---

By default, XAP entries are stored in-memory (actually, in the JVM heap) to provide the fastest performance possible. However, as data grows in size and numbers, the following issues become noticable:

- **Price** - While RAM performs much better than hard drive, it's also much more expensive. As SSD gains popularity, we see new scenarios where storing part of the data in SSD and part in RAM provides great business value.
- **Garbage Collection** - The bigger the JVM heaps get, the harder the garbage collector works. Storing some of the data off-heap (i.e. in the native heap instead of the managed JVM heap) and managing it manually will allow using a smaller JVM heap and relieving the pressure off the garbage collector.

Obviously, simply storing the data in SSD means XAP will no longer be an In-Memory Data Grid. The solution is a hybrid storage model offered by XAP called **MemoryXtend**, which uses the best of both worlds.

# How It Works

In MemoryXtend, the entry's data is stored off-heap (e.g. in the native heap or on a file in SSD), but the indexes are stored in the managed JVM heap. This allows queries which leverage indexes to minimize off-heap penalty, since most of the work is done in-memory and only matched entries are loaded from the off-heap storage. 
In addition, MemoryXtend uses an LRU cache for data entries, so that entries which are read frequently can be retrieved directly from the in-memory storage.

MemoryXtend is designed as a pluggable architecture, supporting multiple implementations of an off-heap storage (also called **BlobStore**). XAP is bundled with two such plug-ins, or add-ons:

- For storing data on an SSD device use the [MemoryXtend RocksDB add-on](./memoryxtend-rocksdb-ssd.html).
- For storing data in RAM on the unmanaged heap use the [MemoryXtend MapDB add-on](./memoryxtend-ohr.html).

This page explains the general concepts and settings which apply to any MemoryXtend add-on. In addition, each MemoryXtend add-on has a specific page for it's additional settings and options.

## Supported XAP APIs

All XAP APIs are supported with the BlobStore configuration. This includes the Space API (POJO and Document), JDBC API, JPA API, JMS API, and Map API. 

# Configuration

Creating a space with a MemoryXtend add-on can be done via `pu.xml` or code. The following example creates a space with the [MemoryXtend RocksDB add-on](./memoryxtend-rocksdb-ssd.html):

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

    <blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt/db1,/mnt/db2]"/>

    <os-core:embedded-space id="space" name="mySpace" >
        <os-core:blob-store-data-policy blob-store-handler="myBlobStore"/>
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

    <bean id="blobstoreid" class="com.gigaspaces.blobstore.rocksdb.RocksDBBlobStoreHandler">
        <property name="paths" value="[/mnt/db1,/mnt/db2]"/>
    </bean>

    <os-core:embedded-space id="space" name="mySpace">
        <os-core:blob-store-data-policy blob-store-handler="blobstoreid"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}
{{%tab "Code"%}}

```java
RocksDBBlobStoreConfigurer rocksDbConfigurer = new RocksDBBlobStoreConfigurer();
rocksDbConfigurer.addPaths("[/mnt/db1,/mnt/db2]");

BlobStoreDataCachePolicy blobStorePolicy = new BlobStoreDataCachePolicy();
blobStorePolicy.setBlobStoreHandler(rocksDbConfigurer.create());

EmbeddedSpaceConfigurer spaceConfigurer = new EmbeddedSpaceConfigurer("mySpace");
spaceConfigurer.cachePolicy(blobStorePolicy);

gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
```
{{% /tab %}}
{{% /tabs %}}

The following configuration options are supported:

| Property | Description | Default | Use |
|:---------|:------------|:--------|:--------|
| blob-store-handler | BlobStore implementation |  | required |
| <nobr>cache-entries-percentage</nobr> | On-Heap cache stores objects in their native format. This cache size determined based on the percentage of the GSC JVM max memory(-Xmx). If `-Xmx` is not specified the cache size default to `10000` objects. This is an LRU based data cache.| 20% | optional |
| avg-object-size-KB |  Average object size. | 5KB | optional |
| persistent |  data is written to flash, space will perform recovery from flash if needed.  | false | optional |

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

# Persistence & Recovery

When using a cluster with backups for high availability, if one of the nodes fails and restarts, it automatically locates the primary node and copies all the data from it so it can serve as a backup again. This process is called **Recovery**. The more data in the space, the longer recovery takes, and if MemoryXtend is used this is no longer a RAM only process - the primary space must iterate thtough its MemoryXtend instance to fetch all the data for the backup node performing the recovery...

However, when using a MemoryXtend add-on which is based on non-volatile technology (for example, SSD), the backup can use the persisted data for the recovery process, and instead of recovering *everything* from the primary, it can recover only the *delta* which it missed while it was down. In addition, the backup can rebuild the indexes for the persisted data without the primary's assitance.

Persistency is off by default, and needs to be explicitly enabled. For example:

```xml
<os-core:embedded-space id="space" name="mySpace" >
    <os-core:blob-store-data-policy blob-store-handler="myBlobStore" persistent="true"/>
</os-core:embedded-space>
```

In addition, persistency requires the following settings:

## Machine-Instance Affinity

If a container or a machine restarts, there's no guarantee the instance will be provisioned on the same machine it had before. When MemoryXtend is used in a non-persitent manner, this is no problem since the instance recovers from the primary, but if MemoryXtend is set to `persistent=true`, we must ensure the instance is provisioned on the same machine it was before so it can recover from the correct device, which is usually local to the machine.

To ensure the Service Grid deploys data grid instances on the correct machines, [Instance level SLA](./the-sla-overview.html) should be used. For exmaple:

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
Make sure you provide the `sla.xml` location at the deploy time (`-sla` deploy command option) or locate it at the root of the processing unit jar or under the `META-INF/spring` directory, alongside the processing unit's `pu.xml` file.
{{% /note %}}

## Last Primary

When a space instance starts as part of a primary-backup cluster, it goes through a process called **Active Election** to determine if it should be a primary or a backup instance. Generally speaking, the first instance which is loaded is primary, and the rest are backups. If a persistent system is restarted in an orderly manner (i.e. all data was flushed to both primary and backup before shutting down) it doesn't matter which instance becomes primary, since they are identical. However, if both primary and backup crashed unexpectedly for some reason and then restart, it is important to ensure that the last instance which was primary before the crash will be elected primary again, since it holds a more accurate version of the data.

To overcome that problem, the space can be configured with an **Attribute Store** which will be updated whenever a new primary space is elected, so that when the system restarts, instead of electing the first available instance, the system will wait for the last primary space to become available and re-elect it. If the last primary space cannot be restarted, the user can manually remove the last primary entry from the attribute store, thus allowing the backup space become primary.

XAP is bundled with a file-based implementation of an attribute store which can be used in conjunction with an NFS filesystem to maintain the last primary. The following example demonstrates configuring a persistent SSD RocksDB add-on with such an attribute store:

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
        <os-core:blob-store-data-policy blob-store-handler="myBlobStore" persistent="true"/>
        <os-core:attribute-store store-handler="attributeStoreHandler"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
