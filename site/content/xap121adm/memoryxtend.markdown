---
type: post121
title:  Overview  
categories: XAP121ADM, ENT
parent: memoryxtend-overview.html
weight: 100
---

By default, XAP entries are stored in-memory (actually, in the JVM heap) to provide the fastest performance possible. However, as data grows in size and numbers, the following issues become noticable:

- **Price** - While RAM performs much better than hard drive, it's also much more expensive. As SSD gains popularity, we see new scenarios where storing part of the data in SSD and part in RAM provides great business value.
- **Garbage Collection** - The bigger the JVM heaps get, the harder the garbage collector works. Storing some of the data off-heap (i.e. in the native heap instead of the managed JVM heap) and managing it manually will allow using a smaller JVM heap and relieving the pressure off the garbage collector.

Obviously, simply storing the data in SSD means XAP will no longer be an In-Memory Data Grid. The solution is a hybrid storage model offered by XAP called **MemoryXtend**, which uses the best of both worlds.

# How it works

In MemoryXtend, the entry's data is stored off-heap (e.g. in the native heap or on a file in SSD), but the indexes are stored in the managed JVM heap. This allows queries which leverage indexes to minimize off-heap penalty, since most of the work is done in-memory and only matched entries are loaded from the off-heap storage. 
In addition, MemoryXtend uses an LRU cache for data entries, so that entries which are read frequently can be retrieved directly from the in-memory storage.

MemoryXtend is designed as a pluggable architecture, supporting multiple implementations of an off-heap storage (also called **BlobStore**). XAP is bundled with two such plug-ins, or add-ons:

- For storing data on an SSD device use the [MemoryXtend RocksDB add-on](./memoryxtend-rocksdb-ssd.html).
- For storing data in RAM on the unmanaged heap use the [MemoryXtend MapDB add-on](./memoryxtend-ohr.html).


{{%align center%}}
![memstorage](/attachment_files/blobstore/ssd-overview.png)
{{%/align%}}


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

    <blob-store:rocksdb-blob-store id="myBlobStore" paths="[/mnt/db1,/mnt/db2]" mapping-dir="/tmp/mapping"/>

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

gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
```
{{% /tab %}}
{{% /tabs %}}

The following configuration options are supported:

| Property | Description | Default | Use |
|:---------|:------------|:--------|:--------|
| blob-store-handler | BlobStore implementation |  | required |
| <nobr>cache-entries-percentage</nobr> | On-Heap cache stores objects in their native format. This cache size determined based on the percentage of the GSC JVM max memory(-Xmx). If `-Xmx` is not specified the cache size default to `10000` objects. This is an LRU based data cache.| 20% | optional |
| avg-object-size-KB |  Average object size in KB. avg-object-size-bytes and avg-object-size-KB cannot be used together. | 5 | optional |
| avg-object-size-bytes |  Average object size in bytes. avg-object-size-bytes and avg-object-size-KB cannot be used together. | 5000 | optional |
| persistent |  data is written to flash, space will perform recovery from flash if needed.  |  | required |

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

# Asynchronous Persistency - Write Behind

MemoryXtend can work together with the [XAP Mirror Service]({{%currentjavaurl%}}/asynchronous-persistency-with-the-mirror.html) which provides reliable asynchronous persistency that asynchronously delegates the operations conducted against the IMDG into a backend database.

{{%align center%}}
![image](/attachment_files/blobstore/ssd-rocksdb-mirror.png)
{{%/align%}}


## Initial Load
The MemoryXtend initial-load fearure speed up read operations avoiding cache miss that force the space to fetch the data from file. Instead, some or all the data can be loaded (pre-fetch) from file once the space bootstrap itself before it is available for users to access.  

By default, when MemoryXtend configured with `persistent=true` and the IMDG is restarted, it reloads from file only the indexes data. The actual raw data will be loaded into heap upto the `cache-entries-percentage` setting based on users activity. MemoryXtend can load raw data from a database (when using write-behind) or attached storage directly into the IMDG instances. This feature is called [Initial Load]({{%currentjavaurl%}}/space-persistency-initial-load.html).<br/>

When configuring `persistent=true` each IMDG instance will start the initial load process from it's attached storage (flash device),  in case it is the empty, initial load will be performed from the database based on the `space-data-source` configuration. When configuring `os-core:blob-store-data-policy` with `persistent=false`, initial load will be performed using the `space-data-source` settings.

{{%tabs%}}
{{%tab "Data-Grid Space settings"%}}
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

    <!--
        Spring property configurer which allows us to use system properties (such as user.name).
    -->
    <bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"/>

    <!--
        Enables the usage of @GigaSpaceContext annotation based injection.
    -->
    <os-core:giga-space-context/>

    <!--
        A JDBC pooled data source that connects to the HSQL server the mirror starts.
    -->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
        <property name="driverClassName" value="org.hsqldb.jdbcDriver"/>
        <property name="url" value="jdbc:hsqldb:hsql://localhost/testDB"/>
        <property name="username" value="sa"/>
        <property name="password" value=""/>
    </bean>

    <!--
        Hibernate SessionFactory bean. Uses the pooled data source to connect to the database.
    -->
    <bean id="sessionFactory" class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="annotatedClasses">
            <list>
                <value>com.mycompany.app.common.Data</value>
            </list>
        </property>
        <property name="hibernateProperties">
            <props>
                <prop key="hibernate.dialect">org.hibernate.dialect.HSQLDialect</prop>
                <prop key="hibernate.cache.provider_class">org.hibernate.cache.NoCacheProvider</prop>
                <prop key="hibernate.cache.use_second_level_cache">false</prop>
                <prop key="hibernate.cache.use_query_cache">false</prop>
            </props>
        </property>
    </bean>

    <bean id="hibernateSpaceDataSource" class="org.openspaces.persistency.hibernate.DefaultHibernateSpaceDataSourceFactoryBean">
        <property name="sessionFactory" ref="sessionFactory"/>
        <property name="initialLoadChunkSize" value="2000"/>
    </bean>

    <bean id="attributeStoreHandler" class="com.gigaspaces.attribute_store.PropertiesFileAttributeStore" >
        <constructor-arg name="path" value="/home/xap/11.0.0/lastprimary/lastprimary.properties"/>
    </bean>

    <blob-store:rocksdb-blob-store id="rocksdb" paths="[/mnt/sdb1,/mnt/sdb2,/mnt/sdb3,/mnt/sdb4]" mapping-dir="/tmp/mapping"/>

    
    <os-core:space id="space" url="/./space" schema="persistent"
                   mirror="true" space-data-source="hibernateSpaceDataSource">
        <os-core:blob-store-data-policy  blob-store-handler="rocksdb" persistent="true"/>
        <os-core:attribute-store store-handler="attributeStoreHandler"/>
        <os-core:properties>
            <props>
                <prop key="space-config.external-data-source.usage">read-only</prop>
                <prop key="cluster-config.cache-loader.external-data-source">true</prop>
                <prop key="cluster-config.cache-loader.central-data-source">true</prop>
            </props>
        </os-core:properties>
    </os-core:space>

    <!--
        OpenSpaces simplified space API built on top of IJSpace/JavaSpace.
    -->
    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}
{{%tab "Mirror configuration"%}}
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd">

    <bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    </bean>

    <!--
        A JDBC datasource pool that connects to HSQL.
    -->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
        <property name="driverClassName" value="org.hsqldb.jdbcDriver"/>
        <property name="url" value="jdbc:hsqldb:hsql://localhost/testDB"/>
        <property name="username" value="sa"/>
        <property name="password" value=""/>
    </bean>

    <!--
        Hibernate SessionFactory bean. Uses the pooled data source to connect to the database.
    -->
    <bean id="sessionFactory" class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="annotatedClasses">
            <list>
                <value>com.mycompany.app.common.Data</value>
            </list>
        </property>
        <property name="hibernateProperties">
            <props>
                <prop key="hibernate.dialect">org.hibernate.dialect.HSQLDialect</prop>
                <prop key="hibernate.cache.provider_class">org.hibernate.cache.NoCacheProvider</prop>
                <prop key="hibernate.cache.use_second_level_cache">false</prop>
                <prop key="hibernate.cache.use_query_cache">false</prop>
                <prop key="hibernate.hbm2ddl.auto">update</prop>
            </props>
        </property>
    </bean>

    <!--
        An external data source that will be responsible for persisting changes done on the cluster that
        connects to this mirror using Hibernate.
    -->
    <bean id="hibernateSpaceSynchronizationEndpoint" class="org.openspaces.persistency.hibernate.DefaultHibernateSpaceSynchronizationEndpointFactoryBean">
        <property name="sessionFactory" ref="sessionFactory"/>
    </bean>

    <!--
        The mirror space. Uses the Hibernate external data source. Persists changes done on the Space that
        connects to this mirror space into the database using Hibernate.
    -->
    <os-core:mirror id="mirror" url="/./mirror-service" space-sync-endpoint="hibernateSpaceSynchronizationEndpoint" operation-grouping="group-by-space-transaction">
        <os-core:source-space name="space" partitions="2" backups="1"/>
    </os-core:mirror>

</beans>
```
{{% /tab %}}
{{% /tabs %}}
     

# User-defined query for initial Load
 
MemoryXtend saves only indexes in RAM and the rest of the objects on disk. If you want to store objects and indexes in RAM, you can use queries to accomplish this.

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





# Limitation
- MemoryXtend and [Direct Persistency]({{%currentjavaurl%}}/direct-persistency.html) configuration is not supported.
- Supported only for `ALL_IN_CACHE` cache policy, not supported for `LRU` and other evictable cache policies.
- All classes that belong to types that are to be introduced to the space during the initial metadata load must exist on the classpath of the Space JVM.
- MemoryXtend and `ESM` is not supported.


