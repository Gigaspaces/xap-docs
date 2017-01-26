---
type: post121
title:  Local Cache
categories: XAP121, PRM
parent: client-side-caching.html
weight: 100
---

{{% ssummary page %}}{{% /ssummary %}}

A **Local Cache** is a Client Side Cache that maintains a subset of the master space's data based on the client application's recent activity. The local cache is created empty, and whenever the client application executes a query the local cache first tries to fulfill it from the cache, otherwise it executes it on the master space and caches the result locally for future queries.

{{% align center %}}
![local_cache.jpg](/attachment_files/local_cache.jpg)
{{% /align %}}


# Usage

Creating a local cache is similar to creating a GigaSpace, except that we wrap the space with a local cache before handing it to the GigaSpace. The local cache can be configured at design time using `LocalCacheSpaceFactoryBean`, or at runtime using `LocalCacheSpaceConfigurer`. For example:

{{%tabs%}}
{{%tab "   Namespace   "%}}


```xml
<os-core:space-proxy  id="space" name="mySpace"/>
<os-core:local-cache id="localCacheSpace" space="space">
    <os-core:properties>
	<props>
		<prop key="space-config.engine.cache_size">5000000</prop>
		<prop key="space-config.engine.memory_usage.high_watermark_percentage">75</prop>
		<prop key="space-config.engine.memory_usage.write_only_block_percentage">73</prop>
		<prop key="space-config.engine.memory_usage.write_only_check_percentage">71</prop>
		<prop key="space-config.engine.memory_usage.low_watermark_percentage">50</prop>
		<prop key="space-config.engine.memory_usage.eviction_batch_size">1000</prop>
		<prop key="space-config.engine.memory_usage.retry_count">20</prop>
		<prop key="space-config.engine.memory_usage.explicit">false</prop>
		<prop key="space-config.engine.memory_usage.retry_yield_time">200</prop>
	</props>
    </os-core:properties>
</os-core:local-cache>

<os-core:giga-space id="localCache" space="localCacheSpace"/>
```

{{% /tab %}}
{{%tab "  Plain   XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.SpaceProxyFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="localCacheSpace"
    class="org.openspaces.core.space.cache.LocalCacheSpaceFactoryBean">
    <property name="space" ref="space" />
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
// Initialize remote space configurer:
SpaceProxyConfigurer urlConfigurer = new SpaceProxyConfigurer("mySpace");

// Initialize local cache configurer
LocalCacheSpaceConfigurer localCacheConfigurer =
    new LocalCacheSpaceConfigurer(urlConfigurer);
// Create local cache:
GigaSpace localCache = new GigaSpaceConfigurer(localCacheConfigurer).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

# Transactional Operations

Transactional operations are **always** executed on the master space.

# Space Persistency

The local cache uses the entry's version to determine whether the local entry is up-to-date or stale when handling updates. Usually this mechanism is transparent to the user and does not require declaring a version property, since the master space and local cache maintain version information for each entry implicitly.

The exception is a master space with an [Space Persistency](./space-persistency.html) enabled - Suppose that an entry in the master space has version 7, and gets cached in the local cache, then evicted from the master space and later reloaded from the data source. If the version is not persisted and reloaded, it will be set to 1 and the local cache will ignore upcoming updates on that entry since its cached version is 7. In that case it is the user's responsibility to make sure that the entry's version is persisted and loaded in the data source, using the following rules:

1. Each POJO class should declare a [SpaceVersion](./pojo-support.html#JavaSpacesPOJO-POJOAnnotationsandXMLSpaceMappingFileElements) property.
1. In the database, you should add a `VERSION_ID` column that will be mapped to this property in the corresponding tables. If you are using Hibernate, add the versionId property to the hbm.xml file or the `@Version` annotation to the mapped class.
1. When writing new entries the space, it is recommended to use version 0 and let the space implicitly initialize to version 1.
1. When using template matching to query the space, make sure that the template used has the version ID field set to ZERO to make sure the space will ignore it.

## Multiple Cache Instances within the Same Client

Running multiple local cache instances (for different master spaces) within the same client may cause problems unless you allocate reasonable headroom for the local cache to operate. Such issues will be manifested in `MemoryShortageException` being thrown sporadically.

The main reason for such issues is the interdependency each cache has on the other caches' utilized memory.  Since the `MemoryShortageException` is thrown when the virtual machine's **total utilized** memory is above a threshold (and not when a specific cache's utilized memory is above a given threshold), an "over-utilized" cache may impact other caches running within the same client.

The recommended approach to ensure a deterministic behavior is to limit the local cache size (the default size is very large and may not be appropriate for all situations).

For example: Client X uses two local cache instances, LC1 for master space A, with the local cache containing 100,000 objects, and LC2 for master space B, with the local cache containing 100 objects. An external client (client Y) writes data into space A. LC1 receives these updates automatically (via notifications). In some point, other clients write data into space B. LC2 is also updated with these objects.

If client X's JVM's available memory breaches the `write_only_block_percentage` threshold, and the client tries to read 1000 objects from Space B that were not cached in LC2, existing LC2 objects will be evicted to clear some memory for the loaded objects.

Since there are only 100 objects to clear from LC2, not all of the 1000 objects that are read from space B will be loaded into LC2. A `MemoryShortageException` might be thrown in such a case if the client tries to perform rapid and repeated reads from space B.

# Operations Flow

## Read Operations

1. The local cache is checked. If the requested object is found in the local cache, it is returned back to the application.
1. Otherwise, the master space is queried.
1. If the object is found within the master space, the object is stored within the local cache and returned back to the application.

- **Blocking Read** - read with timeout first checks the local cache (without blocking - i.e. timeout=0), and if requested object is not found, the master space is checked.

## ReadMultiple Operations

1. The local cache is checked. If all the requested objects are found within the local cache (based on the amount of the `maxObjects` parameter of the `readMultiple` call), they are returned back to the application.
1. Otherwise, the master space is queried.
1. All relevant matching objects are stored into the local cache and returned back to the application.

{{% info %}}
To avoid a `readMultiple` call from the master space make sure you do not use `Integer.MAX_VALUE` as the `max_objects` value.
{{%/info%}}

## Take/TakeMultiple Operations

Take is always executed on both the local space and the master space. Blocking Take (take with timeout > 0) will block until an object is available on the master space, just like regular take.

## Write/Update Operations

Writes are always executed on the master space. Updates are executed both on the master space and the local cache, to make sure the cache is consistent if the object is cached.

{{% info %}}
**Transactional** object update - updates the object in the master space. It also removes any old copies from the local cache. This is done to provide transactional consistency. Once a transaction is committed, the data is updated within the local cache according to the update policy.
{{%/info%}}

# Synchronization

## Update Policy

Each change on the master space triggers a notification at the local cache. The change is processed according to one of the following update policies:

{{%imagertext  "/attachment_files/local_cache_pull.jpg"%}}
- `Pull` - When the local cache is notified about an update, it removes the stale object from the local cache (invalidation). The next time the client tries to read the object, it will be reloaded from the master space and stored in the local cache.
{{%/imagertext%}}

{{%imagertext  "/attachment_files/local_cache_push.jpg"%}}
- `Push` - When the local cache is notified about an update, it loads the recent copy of the object from the master space and 'pushes' it into the local cache. The next time the client tries to read the object, it will be returned from the local cache without accessing the master space.
{{%/imagertext%}}

- `None` - Do not register for master space updates - If an object is changed in the master space, it will remain stale in the local cache until its lease expires.





{{% info %}}
Only **actual** object changes in master space are propagated to the cache - *update* and *take**. Object evictions or reloads from the data source do not update the local cache.
When the local cache updates the cached object (i.e. PUSH policy), it creates a new object with the relevant data and updates the cache to the new reference. If an application is holding a reference to the previous object it will not be changed - to get the changes the application should read it from the local cache again.
{{%/info%}}

In general, `Push` is usually recommended for applications that perform more reads than updates (when reducing cache misses is important), whereas `Pull` is usually recommended for applications that perform more updates than reads (when protecting the cache from abundant updates is important and cache misses are acceptable).

The update policy can be configured using `LocalCacheSpaceFactoryBean` for Spring, or using `LocalCacheSpaceConfigurer` at runtime. The default update policy is "pull." For example:


```xml
<os-core:local-cache id="localCacheSpace" space="space" update-mode="PULL"/>
```

## Batch Synchronization

Changes in the server are grouped and sent to the client in batches. The following configuration settings control synchronization batching:

- Batch Size - When the batch size reaches the configured value, the batch is sent to the client. The default is 1000 objects in a batch.
- Batch timeout - When the oldest event in the batch reaches the configured value, the batch is sent to the client. The default is 100 milliseconds.

Setting lower values for batch size and timeout will reduce data staleness but increase network load, and vice versa.

Batch settings can be configured using `LocalCacheSpaceFactoryBean` for Spring, or using `LocalCacheSpaceConfigurer` at runtime. For example:


```xml
<os-core:local-cache id="localCacheSpace" space="space"
    batch-size="1000" batch-timeout="100"/>
```

#### Recovering From Disconnection

When the connection between a local cache and remote master space is disrupted, the local cache starts trying to reconnect with the remote space. If the disconnection duration exceeds the **maximum disconnection duration**, the local cache enters a **disconnected** state, wherein each operation throws an exception stating the cache is disconnected. When the connection to the remote master space is restored, the local cache restarts with an empty cache (same as in the initialization process) before restoring the state to **connected**, ensuring the cache does not contain stale data after reconnection.

The maximum disconnection duration can be configured using `LocalCacheSpaceFactoryBean` for Spring, or using `LocalCacheSpaceConfigurer` at runtime (default is one minute, or 60000 milliseconds). For example:


```xml
<!-- duration time is given in milliseconds -->
<os-core:local-cache id="localCacheSpace"
    space="space" max-disconnection-duration="60000"/>
```

## Advanced Notification

The  round-trip-time setting can be configured using the `space-config.dist-cache.events.lease-renew.round-trip-time` custom property. For more information about this setting refer to [Session Based Messaging API](./session-based-messaging-api.html).

# Memory Eviction

## Cache Policy

When using a local cache with `GigaSpace`, the cache policy is set to `LRU` and cannot be changed. When using the local cache with `GigaMap`, the default cache policy is `com.j_spaces.map.eviction.FIFOEvictionStrategy`, and other policies may be used by setting the `space-config.dist-cache.eviction-strategy` custom property. For more details refer to the [Map API](./map-api.html#GigaMap with a Local (Near) Cache).

## Memory Configuration Properties

In order to properly configure the local cache eviction mechanism, you should consider tuning the following configuration elements:


|Parameter|Suggested Value|
|:--------|:--------------|
|space-config.engine.cache_size|5000000|
|space-config.engine.memory_usage.high_watermark_percentage|75 |
|space-config.engine.memory_usage.write_only_block_percentage|73|
|space-config.engine.memory_usage.write_only_check_percentage|71|
|space-config.engine.memory_usage.low_watermark_percentage|50|
|space-config.engine.memory_usage.eviction_batch_size|1000|
|space-config.engine.memory_usage.retry_count |20|
|space-config.engine.memory_usage.explicit-gc|false|
|space-config.engine.memory_usage.retry_yield_time|100|

{{%refer%}}
See the [Memory Management Facilities]({{%currentadmurl%}}/memory-management-facilities.html) for additional details on these configuration properties.
{{%/refer%}}

{{% tip %}}
Having the property `space-config.engine.memory_usage.explicit-gc` set to 'enabled' is recommended only in extreme cases when there is high load on the system, with large amount of concurrent users accessing the local cache and when the amount of CPUs/Cores is relatively small.
{{%/tip%}}

## Handling Memory Shortage

There might be cases when the local cache would not be able to evict its data fast enough. This will result in an exception thrown on the client side. The reasons for this behavior might be very large objects stored within the local cache, large amount of concurrent access to the local cache or relatively small JVM heap. In such a case a `RemoteException` will be thrown.

You should catch this Exception and check its cause. If the cause is `MemoryShortageException` you should sleep for a while and let the client JVM release the evicted memory and retry the operation. See below an example for this:


```java
GigaSpace gigaspace;
while(true)
{
    try
    {
        Object obj = gigaspace.read(template);
        break;
    } catch (Exception e){
        if (e.getCause() instanceof MemoryShortageException)
            Thread.sleep(1000);
    }
}
```

## Monitoring the Client Local Cache Eviction

The Client Local Cache eviction can be monitored by setting the client application `com.gigaspaces.core.memorymanager` logging entry level to `FINE`. Once changed, log entries will be displayed when objects are evicted from the local cache (starting, during, and when completing the eviction cycle), and when waiting for incoming activities.

{{% note %}}
The logging level of `com.gigaspaces.core.memorymanager` can be changed while the client application is running using JConsole.
{{%/note%}}

{{% include "/COM/jconsolejmapwarning.markdown" %}}

# Local Cache Performance

Below is the result of a simple benchmark comparing {{%exurl "Ehcache" "http://ehcache.org"%}}'s `get()` operation with the local cache using the `GigaSpace.readById()` operation. With this benchmark the local cache/ehcache size can accommodate the entire data set for the application.

![ehcache_vs_localcache.jpg](/attachment_files/ehcache_vs_localcache.jpg)



# Local Cache Properties


| Property | Description | Default Value | Unit |
|:---------|:------------|:--------------|:-----|
|<nobr>max-disconnection-duration</nobr>| If local cache disconnection duration exceeds this value, the local cache enters a **disconnected** state, wherein each operation throws an exception stating the cache is disconnected| 60000 | milliseconds |
|batch-size| When the batch size reaches this value, the batch is sent to the client| 1000 | operations|
|batch-timeout| When the oldest event in the batch reaches this value, the batch is sent to the client.|100 | milliseconds |
|update-mode | Local cache update mode. Options: PULL , PUSH , None | PULL| |
|max-time-to-live | Time to live for objects within the local cache | 300000| milliseconds|



