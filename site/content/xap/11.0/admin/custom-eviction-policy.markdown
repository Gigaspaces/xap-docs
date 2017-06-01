---
type: post110
title:  Custom Eviction Policy
categories: XAP110ADM
parent: lru-cache-policy.html
weight: 300
---


{{% ssummary %}} {{% /ssummary %}}



To manage the eviction activity when running in LRU cache policy mode, you can use the Custom eviction policy API.

Configuring an LRU cache means you should be able to estimate certain characteristics of the in-memory data grid activity, such as the maximum frequency of the number of newly added data, and the amount of memory space available. All these inputs, which might change over time, go into defining the number of objects stored in-memory. This means LRU becomes a heuristic algorithm that requires you to change the different values and settings as the business grows. This calls for better control over what gets evicted.

# Implementing a Custom Space Eviction Policy

The [SpaceEvictionStrategy]({{% api-javadoc %}}/com/gigaspaces/server/eviction/SpaceEvictionStrategy.html) is an abstract class you can extend and decide which methods to override in order to implement the required behavior. You don't need to override all the methods if they are not required for your eviction policy.


```java
public abstract class SpaceEvictionStrategy
{
	// Called during the space initialization.
	public void initialize(SpaceEvictionManager evictionManager, SpaceEvictionStrategyConfig config){}

	// Called when the space is closed.
	public void close() {}

	 // Called when a new entry is written to the space.
	public void onInsert(EvictableServerEntry entry) {}

	 // Called when an entry is loaded into the space from an external data source.
	public void onLoad(EvictableServerEntry entry) {}

	// Called when an entry is read from the space.
	public void onRead(EvictableServerEntry entry) {}

	// Called when an entry is updated in the space.
	public void onUpdate(EvictableServerEntry entry) {}

	// Called when an entry is removed from the space.
	public void onRemove(EvictableServerEntry entry) {}

	/**
	 * Called when the space requires entries to be evicted.
	 * @param numOfEntries Number of entries to be evicted
	 * @return The number of entries actually evicted
	 */
	public abstract int evict(int numOfEntries);
        ...
}
```

## The Life Cycle Methods

The `initialize` method is called when the space is started. When overriding this method the user can initialize some state that depends on the `SpaceEvictionStrategyConfig` for instance. All the other structures can be initialized in the constructor.


```java
public void initialize(SpaceEvictionManager evictionManager, SpaceEvictionStrategyConfig config){
	super.initialize(evictionManager, config);
	priorities = new ConcurrentSkipListMap<Priority,
                                        ConcurrentHashMap<Integer, SpaceEvictionStrategy>>();
}
```

The `SpaceEvictionStrategy.close` method is called when the space shuts down. The user can do housekeeping steps within this method call before the space is terminated.

## The Evict Method

The `SpaceEvictionStrategy.evict` method is the most important method you should implement. This method gets the number of entries needed for eviction from the cache manager. According to your defined logic, you should call `getEvictionManager().tryEvict(entry)`, if it returned true it means the entry was evicted, otherwise it wasn't (e.g. when the entry is locked in an undergoing transaction). It is up to the implementation to return how many entries were evicted, therefore the implementation should keep track of the `tryEvict` result and finally return the actual number of entries that were successfully evicted.

## Hooks to Space Actions

The `SpaceEvictionStrategy` class consists of several callback methods that are invoked whenever an action is performed on one of the entries in the In-Memory Data Grid:

- onLoad - Called when an entry is loaded into the space from an external data source.
- onInsert - Called when a new entry is written to the space.
- onRemove - Called when an entry is removed from the space.
- onRead - Called when an entry is read from the space.
- onUpdate - Called when an entry is updated in the space.

These methods are used to specify what your strategy should do in the event a specific entry is inserted or read from the In-Memory Data Grid. For example, if you want to implement an LRU policy, you should update the entry's index in your supporting data structure when the object is being accessed. When implementing your strategy, keep in mind that these methods should provide a high level of concurrency. The cache manager will not call two methods with the same entry concurrently in most cases so using Java's concurrent package should suffice for most implementations, however due to the non blocking read nature of the system, `onRead()` can be called in parallel to other `onRead()` invocations and also with `onUpdate()` invocation on the same entry.

# Configuring a Space With Custom Eviction Strategy

In order to start a space with custom eviction policy it should be configured as follows:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<bean id="myCustomEvictionStrategy" class="org.mypackage.MyCustomEvictionStrategy" />

<os-core:embedded-space id="space" name="mySpace">
  <os-core:custom-cache-policy size="1000" initial-load-percentage="20"	space-eviction-strategy="myCustomEvictionStrategy"/>
</os-core:embedded-space>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java

GigaSpace gigaSpace = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer("mySpace"))
        .cachePolicy(new CustomCachePolicy()
        .evictionStrategy(new MyCustomEvictionStrategy())
        .size(1000)
        .initialLoadPercentage(20)))
        .create();
```

{{% /tab %}}
{{% /tabs %}}

- size - specifies how many items needs to be kept in memory which will be the main trigger for eviction calls
- initial-load-percentage - specifies how many entries should be loaded into the memory of the space when it is started, the percentage is from the defined size (e.g. in our example, it will be 200 entries).

# Custom Strategy Examples

## Eviction by Priority

The [ClassBasedEvictionFIFOStrategy](https://github.com/OpenSpaces/PrioritizedEvictionStrategy/blob/master/src/main/java/org/openspaces/eviction/singleorder/ClassBasedEvictionFIFOStrategy.java) evicts entries first by priority. This means it goes through all the priority numbers in the space in descending order (priorities must be positive integers, which means priority 0 is the most valuable and should get evicted last). After selecting the least valuable priority available, it tries to evict objects that belong to this priority by FIFO (First In, First Out). Here, you can see the way entries are inserted into the strategy class's data structures with an index value which helps later with the order of eviction:


```java
protected void add(EvictableServerEntry entry) {
	//handle new priority value in space
	if(getPriorities().putIfAbsent(getPriority(entry),
              new ConcurrentSkipListMap<IndexValue, EvictableServerEntry>()) == null)
        ...
	IndexValue key = getIndex().incrementAndGet();
	entry.setEvictionPayLoad(key);
	getPriorities().get(getPriority(entry)).put(key, entry);

	...
}
```

## Custom LRU by Class Eviction

The [ClassBasedEvictionLRUStrategy](https://github.com/OpenSpaces/PrioritizedEvictionStrategy/blob/master/src/main/java/org/openspaces/eviction/singleorder/ClassBasedEvictionLRUStrategy.java) acts exactly the same as the previous one except it updates the entry's index when it is being accessed either by being read or by being modified. This fact transforms this strategy to an LRU strategy. Notice the fact that we try and remove the entry from the data structure and only perform actions if we are successful. This is because `onRead()` and `onUpdate()` can be called concurrently with the same entry.


```java
protected void updateEntryIndex(EvictableServerEntry entry) {
	if(getPriorities().get(getPriority(entry))
                          .remove(entry.getEvictionPayLoad(), entry)){
		IndexValue key = getIndex().incrementAndGet();
		getPriorities().get(getPriority(entry)).put(key, entry);
                ...
		entry.setEvictionPayLoad(key);
	}
}
```
