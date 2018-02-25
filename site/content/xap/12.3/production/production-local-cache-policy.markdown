---
type: post123
title:  Local Cache Policy
categories: XAP123PROD
parent: none
weight: 600
---

The [local cache]({{%currentjavaurl%}}/local-cache.html) is used as a client-side cache that stores objects read from the Space by the client application. Holding the objects in the local cache speeds up repeated read operations of the same data. The `readById`/`readByIds` operation has a special optimization with a local cache that speeds up the retrieval time of the object from the local cache, if the object is already stored there. The local cache evicts objects when a threshold is met. For client applications with a large heap size, you may want to configure the local cache eviction parameters as follows in order to control the eviction behavior:


```xml
<os-core:space-proxy id="space" space-name="mySpace" />

<os-core:local-cache id="localCacheSpace" space="space" update-mode="PULL" >
    <os-core:properties>
        <props>
            <prop key="space-config.engine.cache_size">5000000</prop>
            <prop key="space-config.engine.memory_usage.high_watermark_percentage">75</prop>
            <prop key="space-config.engine.memory_usage.write_only_block_percentage">73</prop>
            <prop key="space-config.engine.memory_usage.write_only_check_percentage">71</prop>
            <prop key="space-config.engine.memory_usage.low_watermark_percentage">45</prop>
            <prop key=" space-config.engine.memory_usage.eviction_batch_size">1000</prop>
            <prop key="space-config.engine.memory_usage.retry_yield_time">100</prop>
            <prop key="space-config.engine.memory_usage.retry_count">20</prop>
        </props>
    </os-core:properties>
</os-core:local-cache>
<os-core:giga-space id="gigaSpace" space="localCacheSpace"/>
```
With the above parameters:
 
- the local cache evicts data when either the client JVM memory utilization crosses the 75% threshold, or there are more than 5,000,000 objects within the local cache.
- Data is evicted in batches of 1,000 objects, trying to lower the memory utilization to 45%.
- If the eviction mechanism does not manage to lower the utilization to 45%, it tries 20 times and then stops.
- After each eviction activity, and before measuring the memory utilization, there is a pause of 100 ms, to allow the JVM to release the evicted objects.

{{% tip "Tip"%}}
The `space-config.engine.cache_size` property is set to a large value. This causes the local cache to evict data based on the available free memory, and not based on the total number of objects within the local cache.
{{% /tip %}}

