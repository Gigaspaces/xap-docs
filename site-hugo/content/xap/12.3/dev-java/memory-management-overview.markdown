---
type: post123
title:  Memory Management
categories: XAP123, OSS
weight: 1150
parent: none
---



The Memory Management facility is used to prevent situations where a Space server gets into an out-of-memory failure scenario. Based on the configured cache policy, the memory manager protects the Space (and the application, if it is running co-located with the Space) from consuming memory beyond a defined threshold.

{{% warning "Important"%}}
The client application is expected to have business logic that handles any `org.openspaces.core.SpaceMemoryShortageException` that may be thrown (when using the OpenSpaces API). When the legacy `IJSpace` interface is used, a `com.j_spaces.core.MemoryShortageException` will be thrown instead. Without this business logic, the Space server or a client local cache may eventually exhaust all the parent process available memory resources.
{{% /warning %}}

{{% tip "Tip"%}}
Most of the considerations described in this topic are also relevant for the client application when running a Local Cache with an LRU Cache policy.
{{% /tip %}}

The space memory can be managed using the following mechanisms:

- Eviction policy - You can set the `ALL IN CACHE` or `LRU` (Least Recently Used) eviction policy, or a custom implementation.
- Memory Manager - Provides options for controlling the JVM that is hosting the Space memory utilization. It allows you to define thresholds for situations where the memory becomes over-utilized.

# Cache Eviction Policies

The space supports the following two cache eviction policies:

- [ALL IN CACHE Policy](./all-in-cache-cache-policy.html) (code 1) - Assumes the JVM hosting the Space instance has enough heap to hold all data in memory.
- [LRU Cache Policy](./lru-cache-policy.html) (code 0) - Assumes the JVM hosting the Space instance does not have enough heap to hold all data in memory.

By default, the ALL IN CACHE policy is used for an in-memory data grid, and LRU Cache policy is used for a persistent Space with [Space Persistency](./space-persistency-overview.html) enabled.

These policies are defined via the the `space-config.engine.cache_policy` property. The following example describes how it is configured:


```xml
<os-core:embedded-space id="space" space-name="mySpace">
    <os-core:properties>
        <props>
            <prop key="space-config.engine.cache_policy">0</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

# Calculating the Available Memory

Both the ALL_IN_CACHE and LRU Cache policies calculate the JVM's available memory to determine if there is a need to throw a `SpaceMemoryShortageException` or to start evicting objects. Calculating the available memory is performed when the following operations are called: `abort`, `changeReplicationState`, `clear`, `commit`, `count`, `getReplicationStatus`, `getRuntimeInfo`, `getSpacePump`, `getTemplatesInfo`, `joinReplicationGroup`, `leaveReplicationGroup`, `notify`, `prepare`, `prepareAndCommit`, `execute`, `read`, `readMultiple`, `replace`, `spaceCopy`, `update`, `updateMultiple`, and `write`.

Before throwing `SpaceMemoryShortageException` or `MemoryShortageException`, the local cache/local view/Space performs an explicit garbage collection call ({{%exurl "System.gc()" "http://download.oracle.com/javase/{{%version "java-version"%}}/docs/api/java/lang/System.html#gc())"%}}), allowing the JVM to reclaim any unused heap memory. This activity may happen on the client side when running a local cache or a local view, or on the Space side (JVM hosting the GSC).

The explicit garbage collection call reduces the probability of throwing `SpaceMemoryShortageException` or `MemoryShortageException` if the JVM does have some available memory left. Still, such a call might impact the client side (when running local cache/view) or Space-side responsiveness because during the garbage collection activity, JVM threads cannot perform their activities. For clients or Space with a large heap size, this _might_ introduce a long pause.

To avoid this behavior, add one of the following to your client-side or Space-side JVM parameter list:

- XX:+DisableExplicitGC<br>
- XX:+ExplicitGCInvokesConcurrent (only available when using Concurrent Mark Sweep) 

{{% note "Note"%}}
Only do this if you've determined that it is absolutely necessary, because it disables a feature designed to protect your application.
{{% /note %}}


# Handling Large JVM Heap Sizes

When configuring the JVM to use large heap sizes (over 10GB), we recommend using the following values:


```xml
<os-core:embedded-space id="space" space-name="mySpace">
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

These values represent a 400MB difference between the `high_watermark_percentage` and the `low_watermark_percentage` with a 10GB max heap size. The above values will ensure that the memory manager doesn't waste memory, but throws a  `MemoryShortageException` when running in ALL_IN_CACHE policy mode, or evicts objects when running in LRU Cache policy mode, when the absolute amount of JVM available memory is low.

{{% tip "Tip"%}}
With a large JVM heap size, we recommend using the CMS Garbage collector to prevent long Garbage collector pauses.
{{% /tip %}}

# Memory Manager Activity when Initializing the Space

In this phase of the Space life cycle, the Space checks for the amount of available memory. This is relevant when the Space performs a warm start, such as `ExternalDataSource.initialLoad()`.

# Memory Manager and Transient Objects

Transient objects are specified using the `@SpaceClass (persist=false)` decoration. You can specify transient decoration at the class or object level (field method level decoration).
When using transient objects, note that they are:

- Included in the free heap size calculation.
- Included in the count of total objects (for max cache size).
- Not evicted when running in LRU cache policy mode.

{{% tip "Tip"%}}
Use the transient object option to prevent the Space from evicting objects when running in LRU cache policy mode.
{{% /tip %}}

# Memory Manager's Synchronous Eviction

LRU eviction can be costly, therefore it is done asynchronously by the memory manager. However, when the amount of used memory reaches a certain threshold, LRU eviction is done by the memory manager synchronously and API calls to the Space are blocked. The synchronous eviction watermark can be set by the `space-config.engine.memory_usage.synchronous_eviction_watermark_percentage` memory manager parameter.

# Explicit Eviction of Objects from the Space

Objects can be evicted explicitly from the Space by calling a `takeMultiple` or `clear` operation on [the GigaSpace interface](./the-gigaspace-interface-overview.html), combined with the [TakeModifiers.EVICT_ONLY]({{% api-javadoc %}}/com/j_spaces/core/client/TakeModifiers.html) modifier. The `clear` operation only returns the number of objects actually evicted from the Space. The `takeMultiple` operation returns the actual objects that were evicted. See the following usage example:

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

{{% note "Info"%}}
Note the following important information about the `TakeModifiers.EVICT_ONLY` modifier:

- It can be used with any take operation - `take`, `takeById`, `takeMultiple`, etc.
- It can be used only with LRU policy.
- When using this modifier, the timeout argument in operations that allow specifying a timeout is ignored. The operations will always return immediately.
- When using this modifier, the `take` or `clear` calls will never be propagated to the underlying database (EDS layer) when running in synchronous or asynchronous persistency mode. A `take` operation, for example, might return a `null` while a matching object exists in the underlying database.
- The `TakeModifiers.EVICT_ONLY` is **ignored when used in a transactional operation** - A `take` or `clear` in the context of a transaction will not result in eviction.
{{%/note%}}

# Exceeding the Physical Memory Capacity

The overall Space capacity is not necessarily limited to the capacity of its physical memory. Currently, there are two options for exceeding this limit:

- **Using an LRU and [Space Persistency](./space-persistency-overview.html)** - in this mode, all the Space data is kept in the database so the Space capacity is dependent on database capacity rather than the memory capacity. The Space maintains (in memory) a partial image of the persistent view in an LRU basis.
- **Using [Partitioned Space](../overview/terminology.html)** - in this mode, the Space utilizes the physical memory of multiple JVMs. This means the application using the Space is able to access all the Space instances transparently, as if they were a single Space with a higher memory capacity.

# Memory Manager Parameters

The following properties are used to control the memory manager.


| Property | Description | Default value | Supported with Cache Policy |
|:---------|:------------|:--------------|:----------------------------|
|space-config.engine.cache_size | Defines the maximum size of the Space cache. This is the total number of Space objects across all Space class instances, within a single Space. This parameter is ignored when running an `ALL_IN_CACHE` eviction policy. | 100,000 | LRU |
|space-config.engine.memory_usage.<br>high_watermark_percentage | Specifies a maximum threshold for memory use. If the Space container's memory usage exceeds this threshold, a `com.j_spaces.core.MemoryShortageException` is thrown. | 95 | ALL_IN_CACHE, LRU |
|space-config.engine.memory_usage.<br>low_watermark_percentage | Specifies the recommended lower threshold for the JVM heap size that should be occupied by the Space container. When the system reaches the `high_watermark_percentage` value, it evicts objects on an LRU basis, and attempts to reach the `low_watermark_percentage` value. This process continues until there are no more objects to be evicted, or until memory use reaches the `low_watermark_percentage` value. | 75 | LRU |
|space-config.engine.memory_usage.<br>synchronous_eviction_watermark_percentage | Specifies a threshold from which LRU eviction is synchronous. | high_watermark<br>_percentage+<br>(100 - high_watermark<br>_percentage)/2 | LRU |
|space-config.engine.memory_usage.<br>eviction_batch_size | Specifies the amount of objects to evict each time. This option is relevant only for LRU cache management policy. | 500 | LRU |
|space-config.engine.memory_usage.<br>write_only_block_percentage | Specifies a lower threshold for blocking `write-type` operations. Above this level, only read/take operations are allowed. | 85 | ALL_IN_CACHE,LRU |
|space-config.engine.memory_usage.<br>write_only_check_percentage | Specifies an upper threshold for checking only `write-type` operations. Above this level, all operations are checked. | 76 | ALL_IN_CACHE |
|space-config.engine.memory_usage.<br>retry_count | Number of retries to lower the memory level below the `Low_watermark_percentage` value. If after all retries, the memory level is still above the `space-config.engine.memory_usage.write_only_block_percentage` value, a `com.j_spaces.core.MemoryShortageException` is thrown for that write request. | 5 | LRU |
|space-config.engine.memory_usage.<br>explicit-gc | If `true`, the garbage collector is called explicitly before trying to evict.<br>{{% exclamation %}} When using the LRU cache policy, `space-config.engine.memory_usage.explicit-gc=false` means that the garbage collector might evict fewer objects than the defined minimum (low watermark percentage). This tag is `false` by default, because setting the garbage collector explicitly consumes a large amount of CPU, which affects performance. Therefore, we recommend that you set this to `true` *only* if you want to ensure that the minimum amount of objects are evicted from the Space (and not less than the minimum). | false | LRU |
|space-config.engine.memory_usage.<br>gc-before-shortage | If `true`, GC will be called before throwing `MemoryShortageException` .<br> {{% exclamation %}}  This settings should be set to **false** when running a large heap size (above 4GB) as it may cause a long pause with the JVM response time due to long garbage collection. | true | LRU , ALL_IN_CACHE |
|space-config.engine.memory_usage.<br>retry_yield_time | Time (in milliseconds) to wait after evicting a batch of objects, before measuring the current memory utilization. | 50 | LRU |
|space-config.engine.initial_load | When a persistent Space running in LRU cache policy mode is started/deployed, it loads data from the underlying data source before being available for clients to access. The default behavior is to load data up to 50% of the `space-config.engine.cache_size value`. See the [Reloading Data](./lru-cache-policy.html#Reloading Data) section for details. | 50 | LRU |
|space-config.engine.memory_usage.<br>lruTouchThreshold | LRU touch activity starts when the percentage of objects within the Space exceeds `space-config.engine.memory_usage.lruTouchThreshold` where the `space-config.engine.cache_size` is the maximum amount. This avoids the overhead involved with the LRU activity. A 0 value means always touch, 100 means no touch at all.<br>The default value of the `space-config.engine.memory_usage.lruTouchThreshold` is 50, which means the LRU touch activity will start when the amount of objects within the Space exceeds half of the amount specified by the `space-config.engine.cache_size` value. | 50 | LRU |

{{% note "Note"%}}
A `com.j_spaces.core.MemoryShortageException` or an `org.openspaces.core.SpaceMemoryShortageException` is thrown only when the JVM garbage collection and the eviction mechanism do not evict enough memory. This can happen if the `space-config.engine.memory_usage.low_watermark_percentage` value is too high.
{{%/note%}}
