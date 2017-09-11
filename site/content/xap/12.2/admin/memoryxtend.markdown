---
type: post122
title:  Overview  
categories: XAP122ADM, ENT
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

If a GSC or a machine running a GSC restarts, there's no guarantee the IMDG instance running within the GSC will be provisioned into the same machine it was running before. When MemoryXtend is used in a non-persitent manner, this will not introduce a problem as the instance recovers from the primary, but if MemoryXtend is set to `persistent=true`, we must ensure the instance is provisioned on the same machine it was before so it can recover from the correct device, which is usually local to the machine.
 
{{% note "Central Storage"%}}
Central Storage mode will allow you to use MemoryXtend without having Machine-Instance Affinity configuration.
{{% /note %}}


To ensure the Service Grid deploys IMDG instances on the correct machines, [Instance level SLA](./the-sla-overview.html) should be used. For exmaple:

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
  
To overcome that problem, the space can be configured with **Attribute Store** which will be updated whenever a new primary space is elected, so that when the system restarts, instead of electing the first available instance, the system will wait for the last primary space to become available and re-elect it. If the last primary space cannot be restarted, the user can manually remove the last primary entry from the attribute store, thus allowing the backup space to become the primary.

XAP is bundled with 2 implementations:
 
* File-based implementation of an attribute store which can be used in conjunction with an NFS filesystem to maintain the last primary.
* [Zookeeper](./zookeeper.html).

The following examples demonstrate how to configure a persistent SSD RocksDB add-on with such an attribute store:
 
{{%tabs%}}
{{%tab "File based"%}}
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
{{%tab "Zookeeper based"%}}
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
<br/>
<br/>

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


