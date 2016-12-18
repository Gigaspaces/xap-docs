---
type: post110
title:  Overview
categories: XAP110ADM
parent: memory-management-overview.html
weight: 100
---

{{%ssummary%}}{{%/ssummary%}}


The Memory Management facility is used to assist the client in avoiding a situation where a space server gets into an out-of-memory failure scenario. Based on the configured cache policy, the memory manager protects the space (and the application, in the case it is running collocated with the space) from consuming memory beyond a defined threshold.

{{% warning %}}
The client/Application is expected to have some business logic that will handle `org.openspaces.core.SpaceMemoryShortageException` that might be thrown (when using the openspaces API). When the legacy `IJSpace` interface is used, `com.j_spaces.core.MemoryShortageException` will be thrown instead. Without such business logic, the space server or a client local cache may eventually exhaust all their parent process available memory resources.
{{% /warning %}}

{{% tip %}}
Most of the considerations described in this topic are also relevant for the client application when running a Local Cache running with a LRU Cache policy.
{{% /tip %}}

The space memory can be managed using the following mechanisms:

- Eviction policy - You can set the policy to run `ALL IN CACHE` or `LRU` (Least Recently Used) or custom implementation.
- Memory Manager - Provides options for controlling the JVM that is hosting the space memory utilization. It allows you to define thresholds for situations where the memory becomes over-utilized.

# Cache Eviction Policies

The space supports two cache eviction policies: [LRU-Cache Policy](./lru-cache-policy.html) (code 0) and [ALL IN CACHE-Cache Policy](./all-in-cache-cache-policy.html) (code 1) defined via the the `space-config.engine.cache_policy` property. See below example how you can configure it:


```xml
<os-core:embedded-space id="space" name="mySpace">
    <os-core:properties>
        <props>
            <prop key="space-config.engine.cache_policy">0</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

- [ALL IN CACHE-Cache Policy](./all-in-cache-cache-policy.html) - Assumes the JVM hosting the space instance has enough heap to hold all data in memory.
- [LRU-Cache Policy](./lru-cache-policy.html) - Assumes the JVM hosting the space instance does not have enough heap to hold all data in memory.
By default ALL IN CACHE policy is used for an in-memory data grid,and LRU-Cache Policy is used for a persistent space with [Space Persistency]({{%currentjavaurl%}}/space-persistency.html) enabled.

# Calculating the Available Memory

Both the `ALL_IN_CACHE` and `LRU` cache policies calculate the JVM's available memory to determine if there is a need to throw `SpaceMemoryShortageException` or to start to evict objects. Calculating the available memory is performed when the following operations are called: abort, changeReplicationState, clear, commit, count, getReplicationStatus, getRuntimeInfo, getSpacePump, getTemplatesInfo, joinReplicationGroup, leaveReplicationGroup, notify, prepare, prepareAndCommit, execute, read, readMultiple, replace, spaceCopy, update, updateMultiple, write.

Before throwing `SpaceMemoryShortageException` or `MemoryShortageException` the local cache/local view/space performs an explicit garbage collection call ([System.gc()](http://download.oracle.com/javase/{{%version "java-version"%}}/docs/api/java/lang/System.html#gc())), allowing the JVM to reclaim any unused heap memory. This activity may happen at the client side when running a local cache or a local view, or at the space side (JVM hosting the GSC).

The explicit garbage collection call reduces the probability of throwing `SpaceMemoryShortageException` or `MemoryShortageException` in case the JVM does have some available memory left. Still, such a call might impact the client side (when running local cache/view) or space-side responsiveness since during the garbage collection activity, no JVM thread will manage to perform its activity. With a client or space using a large heap size, this _might_ introduce a long pause.

To avoid this behavior, you can add one of the following into your client or space side JVM parameters list:<br>
-XX:+DisableExplicitGC<br>
-XX:+ExplicitGCInvokesConcurrent

Only do so if you've determined it necessary, as this disables a feature designed to protect your application.

# Handling Large JVM Heap Sizes

When configuring the JVM to use large heap sizes (over 10GB), it is recommended to use the following values:


```xml
<os-core:embedded-space id="space" name="mySpace">
    <os-core:properties>
        <props>
            <prop key="space-config.engine.memory_usage.high_watermark_percentage">97</prop>
            <prop key="space-config.engine.memory_usage.write_only_block_percentage">96</prop>
            <prop key="space-config.engine.memory_usage.write_only_check_percentage">95</prop>
            <prop key="space-config.engine.memory_usage.low_watermark_percentage">94</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

These values represent 400MB difference between the `high_watermark_percentage` and the `low_watermark_percentage` when having 10GB max heap size. The above values will make sure the memory manager will not waste memory, but throw `MemoryShortageException` when running in `ALL_IN_CACHE` or evict objects when running in `LRU` cache policy mode when the absolute amount of JVM available memory is low.

{{% tip %}}
With large JVM heap size it is recommended to use the CMS Garbage collector. This will avoid long Garbage collector pauses.
{{% /tip %}}

# Memory Manager Activity when Initializing the Space

In this phase of the space life cycle, the space checks for the amount of available memory. This is relevant when the space performs a warm start, such as `ExternalDataSource.initialLoad()`.

# Memory Manager and Transient Objects

Transient Objects are specified using the `@SpaceClass (persist=false)` decoration. You may specify transient decoration at the class or object level (field method level decoration).
When using transient objects, note that they are:

- Included in the free heap size calculation.
- Included in the count of total objects (for max cache size).
- Not evicted when running in LRU cache policy mode.

{{% tip %}}
You may use the transient object option to prevent the space from evicting objects  when running in LRU cache policy mode.
{{% /tip %}}

# Memory Manager's Synchronous Eviction

Since LRU eviction can be costly, it is done in asynchronously by the memory manager. However, when the amount of used memory reaches a threshold, LRU eviction by the memory manager is done synchronously and API calls to the space are blocked. The synchronous eviction watermark can be set by the `space-config.engine.memory_usage.synchronous_eviction_watermark_percentage` memory manager parameter.

# Explicit Eviction of Objects from the Space

Objects can be evicted explicitly from the space by calling the `takeMultiple` or `clear` operations on [the GigaSpace interface]({{%currentjavaurl%}}/the-gigaspace-interface.html) combined with the [TakeModifiers.EVICT_ONLY](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/com/j_spaces/core/client/TakeModifiers.html) modifier. The `clear` operation only returns the number of objects actually evicted from the space. The `takeMultiple` operation returns the actual objects that were evicted. Here's usage example:

{{%tabs%}}
{{%tab "  Using clear "%}}


```java
GigaSpace gigaSpace = ...;
User template = new User();
// Using clear - evicts all the objects of type User from the space
int numEvicted = gigaSpace.clear(template, ClearModifiers.EVICT_ONLY);
```

{{% /tab %}}
{{%tab "  Using takeMultiple "%}}


```java
GigaSpace gigaSpace = ...;
User template = new User();
// Using takeMultiple - evicts all the objects of type User from the space
User[] evictedUsers = gigaSpace.takeMultiple(template, Integer.MAX_VALUE, TakeModifiers.EVICT_ONLY);
```

{{% /tab %}}
{{% /tabs %}}

{{% info %}}
It's important to note the following about the `TakeModifiers.EVICT_ONLY` modifier:
{{%/info%}}

- It can be used with any take operation - `take`, `takeById`, `takeMultiple`, etc.
- It can be used only with LRU policy.
- When using this modifier, the timeout argument in operations that allow to specify a timeout is ignored. The operations will always return immediately.
- When using this modifier, the `take` or `clear` calls will never be propagated to the underlying database (EDS layer) when running in synchronous or asynchronous persistency mode. A `take` operation for example might return a `null` while a matching object exists in the underlying database.
- The `TakeModifiers.EVICT_ONLY` is **ignored when used in a transactional operation** - A `take` or `clear` in the context of a transaction will not result eviction.

# Exceeding Physical Memory Capacity

The overall space capacity is not necessarily limited to the capacity of its physical memory. Currently there are two options for exceeding this limit:

- **Using an LRU and [Space Persistency]({{%currentjavaurl%}}/space-persistency.html)** - in this mode, all the space data is kept in the database and therefore the space capacity is dependent on the database capacity rather than the memory capacity. The space maintains in memory, a partial image of the persistent view in an LRU basis.
- **Using [Partitioned Space](/product_overview/terminology.html)** - in this mode, the space utilizes the physical memory of multiple JVMs. This means the application using the space is able to access all the space instances transparently, as if they were a single space with higher memory capacity.

# Memory Manager Parameters

The following properties used to control the memory manager.


| Property | Description | Default value | Supported with Cache Policy |
|:---------|:------------|:--------------|:----------------------------|
|space-config.engine.cache_size | Defines the maximum size of the space cache. This is the total number of space objects across all space class instances, within a single space. This parameter is ignored when running an `ALL_IN_CACHE` cache policy. | 100,000 | LRU |
|space-config.engine.memory_usage.{{<wbr>}}high_watermark_percentage | Specifies a maximum threshold for memory use. If the space container's memory usage exceeds this threshold, a `com.j_spaces.core.MemoryShortageException` is thrown. | 95 | ALL_IN_CACHE, LRU |
|space-config.engine.memory_usage.{{<wbr>}}low_watermark_percentage | Specifies the recommended lower threshold for the JVM heap size that should be occupied by the space container. When the system reaches the high_watermark_percentage, it evicts objects on an LRU basis, and attempts to reach this `low_watermark_percentage`. This process continues until there are no more objects to be evicted, or until memory use reaches the `low_watermark_percentage`. | 75 | LRU |
|space-config.engine.memory_usage.{{<wbr>}}synchronous_eviction_watermark_percentage | Specifies a threshold from which LRU eviction is synchronous. | high_watermark{{<wbr>}}_percentage+{{<wbr>}}(100 - high_watermark{{<wbr>}}_percentage)/2 | LRU |
|space-config.engine.memory_usage.{{<wbr>}}eviction_batch_size | Specifies the amount of objects to evict each time. This option is relevant only in LRU cache management policy. | 500 | LRU |
|space-config.engine.memory_usage.{{<wbr>}}write_only_block_percentage | Specifies a lower threshold for blocking `write-type` operations. Above this level, only read/take operations are allowed. | 85 | ALL_IN_CACHE,LRU |
|space-config.engine.memory_usage.{{<wbr>}}write_only_check_percentage | Specifies an upper threshold for checking only `write-type` operations. Above this level, all operations are checked. | 76 | ALL_IN_CACHE |
|space-config.engine.memory_usage.{{<wbr>}}retry_count | Number of retries to lower the memory level below the `Low_watermark_percentage`. If after all retries, the memory level is still above the `space-config.engine.memory_usage.write_only_block_percentage`, a `com.j_spaces.core.MemoryShortageException` is thrown for that write request. | 5 | LRU |
|space-config.engine.memory_usage.{{<wbr>}}explicit-gc | If `true`, the garbage collector is called explicitly before trying to evict.{{<wbr>}}{{% exclamation %}} When using the LRU cache policy, `space-config.engine.memory_usage.explicit-gc=false` means that the garbage collector might evict fewer objects than the defined minimum (low watermark percentage). This tag is `false` by default, because setting the garbage collector explicitly consumes a large amount of CPU, thus affecting performance. Therefore, it is recommended that you set this to `true` only if you want to ensure that the minimum amount of objects are evicted from the space (and not less than the minimum). | false | LRU |
|space-config.engine.memory_usage.{{<wbr>}}gc-before-shortage | If `true` GC will be called before throwing `MemoryShortageException` .{{<wbr>}} {{% exclamation %}}  This settings should be set to **false** when running large heap size (above 4GB) as it may cause a long pause with the JVM response time due-to long garbage collection. | true | LRU , ALL_IN_CACHE |
|space-config.engine.memory_usage.{{<wbr>}}retry_yield_time | Time (in milliseconds) to wait after evicting a batch of objects, before measuring the current memory utilization. | 50 | LRU |
|space-config.engine.initial_load | When a persistent space running in LRU cache policy mode is started/deployed, it loads data from the underlying data source before being available for clients to access. The default behavior is to load data up to 50% of the `space-config.engine.cache_size value`. See the [Reloading Data](./lru-cache-policy.html#Reloading Data) section for details. | 50 | LRU |
|space-config.engine.memory_usage.{{<wbr>}}lruTouchThreshold | LRU touch activity kicks-in when the percentage of objects within the space exceeds `space-config.engine.memory_usage.lruTouchThreshold` where the `space-config.engine.cache_size` is the max amount. This avoid the overhead involved with the LRU activity. A 0 value means always touch, 100 means no touch at all.{{<wbr>}}The default value of the `space-config.engine.memory_usage.lruTouchThreshold` is 50 which means the LRU touch activity will kick-in when the amount of objects within the space will cross half of the amount specified by the `space-config.engine.cache_size` value. | 50 | LRU |

{{% note %}}
A `com.j_spaces.core.MemoryShortageException` or an `org.openspaces.core.SpaceMemoryShortageException` are thrown only when the JVM garbage collection and the eviction mechanism do not evict enough memory. This can happen if the `space-config.engine.memory_usage.low_watermark_percentage` value is too high.
{{%/note%}}


