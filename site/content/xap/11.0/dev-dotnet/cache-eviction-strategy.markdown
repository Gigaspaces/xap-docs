---
type: post110
title:  Cache Eviction Strategy
categories: XAP110NET
parent: local-cache.html
weight: 100
---

The local cache storage is managed by an eviction strategy. The strategy determines when and if Entries needs to be evicted from the space, and is also in charge of deciding which specific Entries to evict.

# Choosing and configuring an eviction strategy

XAP.NET comes with a number of eviction strategies. To choose an eviction strategy, you'll need to configure a `IdBasedLocalCacheConfig` object with the desired eviction strategy builder. A `IdBasedLocalCache` instance can be created with this config object.

Here's a simple example of configuring a cache to use the FIFO eviction strategy, where the size limit of the cache is 50,000 entries and each time an eviction is required, 1,000 Entries are evicted:


```java
ISpaceProxy spaceProxy = // create or obtain a reference to a space proxy

IdBasedLocalCacheConfig cacheConfig = new IdBasedLocalCacheConfig ();
cacheConfig.EvictionStrategyBuilder = new FifoSegmentEvictionStrategyBuilder(50000, 1000);

ILocalCache localCache = new GigaSpacesFactory.CreateIdBasedLocalCache(spaceProxy, cacheConfig);
```

## Eviction strategies


|Strategy Builder Name|Description|
|:--------------------|:----------|
|LruSegmentEvictionStrategyBuilder | Least recently used, used by default. |
|FifoSegmentEvictionStrategyBuilder | First in first out, more concurrent because it doesn't need to update anything when Entries are touched inside the cache. |
|LfuSegmentEvictionStrategyBuilder | Least frequently used. |
|TimeBasedEvictionStrategyBuilder | Entries that weren't touched for a certain period of time are cleared. |

Each of these strategies also contains a logic for periodic clearing; by default every 30 seconds the local cache is cleared. This behavior can be changed or turned off when constructing and configuring the specific eviction strategy builder, however it is not recommended to turn it off.

# Custom eviction strategy

A custom eviction strategy can be implemented. To do this, you first need to implement an eviction strategy by implementing the `IEvictionStrategy` interface. Then, implement a builder for it by implementing the `ISegmentEvictionStrategyBuilder` interface.
