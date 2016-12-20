---
type: post
title:  Custom Eviction
categories: SBP
parent: data-access-patterns.html
weight: 200
---



|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
|Shravan (Sean) Kumar| 7.1.2| January 2009 |           |          |



# Overview
GigaSpaces being an in memory grid, is limited by the amount of memory allocated to the JVM's that make the cluster. Applications that are built using GigaSpaces and use it as a run time environment should be designed to work with this constraint. This article shows common strategies GigaSpaces applications use for Evicting old objects and make room for new data.

GigaSpaces supports two cache policies, [LRU]({{%latestadmurl%}}/lru-cache-policy.html) and [ALL_IN_CACHE]({{%latestadmurl%}}/all-in-cache-cache-policy.html). GigaSpace evicts data only in the [LRU]({{%latestadmurl%}}/lru-cache-policy.html) mode, where "oldest" objects are evicted from memory when the configured thresholds are reached.

As the name suggests LRU is "least recently used" data and any data written first into the space become a candidate for eviction when there is no more room in the JVM. This policy works well for [Side Cache scenarios](/product_overview/caching-scenarios.html#cachingscenarios-sideCache) where the purpose of GigaSpaces is to cache frequently used data.


# Eviction Strategies

## Eviction using Leases

In this approach, application writes the objects into cluster with a limited Lease. Also limited Lease will be used selectively, reference data will be written with Lease.FOREVER and stay for the life of cluster but transient data will be written with limited lease.


```java
// connect to the space using its URL
IJSpace space = new UrlSpaceConfigurer(url).space();
// use gigaspace wrapper to for simpler API
this.gigaSpace = new GigaSpaceConfigurer(space).gigaSpace();

...
// single object write - Object Lease will expire after 1 hour and get evicted after this
gigaSpace.write(singleOrder, 1000 * 60 * 60 );

...
// or write multiple - Object Lease will expire after 1 hour and get evicted after this
gigaSpace.writeMultiple(batchOfOrders, 1000 * 60 * 60 );

...
gigaSpace.write(referenceData, Lease.FOREVER);
```

Lease reaper functionality on the GigaSpace runs periodically and evicts objects whose lease is up.

{{% note %}}
Lease amount should be tuned based on available memory, expected throughput rate and most importantly available memory in the cluster
{{% /note %}}

{{% tip %}}
This approach is most suitable for application which have data coming in at a steady and predictable rate.
{{% /tip %}}

## Manually managing Object Leases

GigaSpaces API returns the `LeaseContext` after every write operation/update operation. In this approach application evicts the data by cancelling the Lease on objects in space that are eligible for eviction.


```java
LeaseContext<Order> lease;

...
public void writeOrder() {
...
//Save lease from write operation
lease = gigaSpace.write(singleOrder);
...

public void cancelLease() {
...
lease.cancel();
```

Another alternative to saving the Lease is to retrieve the objects that can be evicted and updating the Lease to a very short duration so that they become eligible for eviction.


```java
//Retrieve all processed low priority orders and expire the lease
Order template = new Order();

template.setType(OrderType.LOW);
template.setProcessed(true);

Order[] processedLowPriorityOrders = gigaSpace.readMultiple(template, 1000);

//Update the lease to expire in 1 second
gigaSpace.writeMultiple(processedLowPriorityOrders,
		1000,					// Update the Lease to 1 second
		UpdateModifiers.UPDATE_OR_WRITE); // Update existing object
```

{{% tip %}}
This approach is most suitable for application which have fluctuating data loads. Eviction logic can be triggered by monitoring the object count on the cluster and selectively evicting data like the example above.
{{% /tip %}}

## Using a Polling Container

Sometimes applications data usage is based on the age of the data. After end of a business day any data for that day is not needed by the application and can be evicted. Applications like these use a [polling container]({{%latestjavaurl%}}/polling-container.html) to perform eviction logic.

Eviction logic will be defined as listener logic. Eviction candidates will be selected using a [Trigger Receive Handler]({{%latestjavaurl%}}/polling-container.html#pollingcontainer-triggerReceiveOperation) feature which will modify the query dynamically for each invocation.

Another variation of this approach is listener logic will wait for a command object (something like a close of business day event). When this command object is written into space eviction logic starts and cleans up data.

Download the example {{%zip "/attachment_files/sbp/PollingEvictor.zip"%}} that uses a polling container for eviction of Orders.

In this simplistic example (created using hello-world example included in the product distribution),

- Application is processing new `Order`'s that are written into the space using a Polling Container. Any `Order`'s that are in `processed=false` status are candidates for Polling container.
- Application also defines another Polling Container for Eviction, `Evictor`, which is polling for `Order`'s in processed status.
- `Evictor` polling container is also configured with a `TriggerReceiveOperationHandler` which modifies the query dynamically per each invocation. Purge age of the orders is used as the criterion for eviction.
- Currently example is evicting any orders older than 1 minute old.
- A TestClient loads Orders into the space. After 1 minute `Evictor` gets into action and starts evicting any data older than 1 minute old.

Some relevant code from the example,

{{%tabs%}}

{{%tab "  Configuring Eviction logic in pu.xml "%}}


```xml
	<!--
		Eviction Polling container
	-->
	<os-events:polling-container id="evictionPollingEventContainer"
		giga-space="gigaSpace" pass-array-as-is="true" receive-timeout="1000">
		<os-events:tx-support tx-manager="transactionManager" />
		<os-events:receive-operation-handler>
			<bean
				class="org.openspaces.events.polling.receive.MultiTakeReceiveOperationHandler">
				<property name="maxEntries">
					<value>1000</value>
				</property>
			</bean>
		</os-events:receive-operation-handler>
		<os-events:trigger-operation-handler
			ref="trigger" />
		<os-events:listener>
			<os-events:annotation-adapter>
				<os-events:delegate ref="evictor" />
			</os-events:annotation-adapter>
		</os-events:listener>
	</os-events:polling-container>

	<!--
		The Trigger Receive operation handler used for dynamic eviction criterion
	-->
	<bean id="trigger" class="com.gigaspaces.eviction.EvictionTrigger">
		<property name="useTriggerAsTemplate">
			<value>true</value>
		</property>
		<property name="purgeAge">
			<value>60000</value>
		</property>
	</bean>

	<!--
        The Evictor bean
    -->
	<bean id="evictor" class="com.gigaspaces.eviction.Evictor"/>
```

{{% /tab %}}

{{%tab "  Trigger Receive Operation Handler "%}}


```java
public class EvictionTrigger implements TriggerOperationHandler {

...

	public Object triggerReceive(Object template, GigaSpace gigaSpace,
			long receiveTimeout) throws DataAccessException {

		// Create the query to get the orders
		SQLQuery<Order> newQuery = new SQLQuery<Order>(Order.class,
				" processed = true and orderTime < ?");

		oldestOrderInSpace = System.currentTimeMillis() - getPurgeAge();
		newQuery.setParameter(1, oldestOrderInSpace);

		Object snap = gigaSpace.snapshot(newQuery);

		Order order = (Order) gigaSpace.read(snap, receiveTimeout);

		// There are orders that can be evicted
		if (order != null) {
			return snap;
		} else {
			return null;
		}
	}
```

{{% /tab %}}

{{%tab "  Evictor "%}}


```java
public class Evictor {

...

    @SpaceDataEvent
    public void evictOrder(Order[] orders) {

    	if (orders.length > 0) {
    		logger.info(" ------ EVICTED " + orders.length + " objects. First order " + orders[0] + " Last order " + orders[orders.length - 1]);
    	} else {
    		logger.info(" ------ Nothing to EVICT");
    	}

    }

}
```

{{% /tab %}}

{{% /tabs %}}

### Running the example

{{% note %}}
This example is using Maven for packaging and build.

- Please [install the OpenSpaces Maven plugin]({{%latestjavaurl%}}/installation-maven-overview.html#MavenPlugin-Installation) before you run this example.
- Please update the GigaSpaces and Spring versions to appropriate versions in the pom.xml file.
{{% /note %}}

Extract the [example](/attachment_files/sbp/PollingEvictor.zip) archive into a folder. Navigate to the folder

Run maven clean using following command


```java
mvn clean
```

Run maven package (skip the tests) using following command


```java
mvn package -DskipTests
```

Start an instance of gs-agent using the script inside GigaSpacesRoot/bin/gs-agent.bin(sh). If your objective is to run the prototype and see it in action, please update your GSC_JVM_OPTIONS to have a Xmx and Xms of 128M.

Deploy the application using following command


```java
mvn os:deploy -Dmodule=pollingEvictor
```

Invoke a gs-ui session to monitor the space.

Also Launch a JConsole session for the space that just started.

Run the test client using the following command


```java
mvn exec:java -Dexec.classpathScope=compile -Dexec.mainClass="com.gigaspaces.client.TestClient"
-Dexec.args="jini://*/*/mySpace"
```

Start monitoring the GSC logs. After about a minute you will see that the eviction logic will trigger and clear the old `Order` objects from space.

## Using the JVM Memory Notification API

Another strategy used by some of the very complex GigaSpaces applications is to rely on the JVM Memory Notification API. Java 5 introduced [MemoryPoolMXBean](http://download.oracle.com/javase/1.5.0/docs/api/java/lang/management/MemoryPoolMXBean.html) API and this API lets you register for usage threshold notifications.
An application can define custom eviction logic and register this functionality to be triggered when the usage exceeds the threshold.

Some advantages with this approach are,

- Lets you effectively utilize all the available memory in GigaSpaces cluster.
- Notification based instead of querying the cluster for types/counts.

Attached is an [example](/attachment_files/sbp/MyCustomEvictor.zip) that uses Memory Notification feature for triggering eviction logic.

In this simplistic example (created using hello-world example included in the product distribution),

- Application is processing new `Order`'s that are written into the space using a Polling Container. Any `Order`'s that are in `processed=false` status are candidates for Polling container.
- Application also defines an EvictionManager Bean that implements InitializingBean, NotificationListener.
- As part of the initialization logic, EvictionManager registers for Memory Threshold Notification.
- Initialization logic also defines `Watermark` objects in the space.
- Watermark pattern is useful where multiple parts of functionality are interdependent. Boundary condition is exposed using a special type of object in the space (`Watermark`). Changes to boundary definition are maintained and communicated using this special entry.
- A TestClient loads Orders in the space.
- When the usage reaches the limit eviction logic is triggered. This example uses a logic where all processed orders are removed from the space using GigaSpaces `clear` API.
- The oldest Order object that is available in space is updated each time eviction logic is triggered using the `Watermark` object. If there were other parts of application like query logic or any monitoring application, it could easily know the status by looking at the `boundaryValue` of `Watermark` object.

Some relevant code from the example,

{{%tabs%}}

{{%tab "  Configuring Eviction logic in pu.xml "%}}


```xml
<bean id="ec" class="com.gigaspaces.domain.EvictionConfig">
	<property name="evictionStartThreshold"><value>70</value></property>
	<property name="evictionStopThreshold"><value>60</value></property>
	<property name="evictionClasses">
		<list>
			<value>com.gigaspaces.domain.Order</value>
		</list>
	</property>
</bean>

<bean id="myEvictor" class="com.gigaspaces.eviction.EvictionManager">
	<property name="gs" ref="gigaSpace"></property>
	<property name="tm" ref="transactionManager"></property>
	<property name="ec" ref="ec"></property>
</bean>

<!--
	The order processor bean
-->
<bean id="orderProcessor" class="com.gigaspaces.processor.Processor">
	<property name="workDuration"><value>10</value></property>
</bean>
```

{{% /tab %}}

{{%tab "  Eviction Manager "%}}


```java
public class EvictionManager implements InitializingBean, NotificationListener {

...

@Override
public void afterPropertiesSet() throws Exception {

...

@Override
public void handleNotification(Notification n, Object handback) {

}
```

{{% /tab %}}

{{%tab "  Watermark Object "%}}


```java
package com.gigaspaces.domain;

import java.util.HashMap;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class Watermark {

	private Class spaceClass = null;

	private String spaceClassName = null;

	private Long boundaryValue = null;

	private HashMap<Long,String> evictionStatistics = null;

	private Boolean evictionInProgress = null;

	@SpaceId(autoGenerate = false)
	public String getSpaceClassName() {
		return spaceClassName;
	}

	public void setSpaceClassName(String spaceClassName) {
		this.spaceClassName = spaceClassName;
	}

	public Class getSpaceClass() {
		return spaceClass;
	}

	public void setSpaceClass(Class spaceClass) {
		this.spaceClass = spaceClass;
	}

	public Long getBoundaryValue() {
		return boundaryValue;
	}

	public void setBoundaryValue(Long boundaryValue) {
		this.boundaryValue = boundaryValue;
	}

	public Boolean getEvictionInProgress() {
		return evictionInProgress;
	}

	public void setEvictionInProgress(Boolean evictionInProgress) {
		this.evictionInProgress = evictionInProgress;
	}

	public HashMap<Long, String> getEvictionStatistics() {
		return evictionStatistics;
	}

	public void setEvictionStatistics(HashMap<Long, String> evictionStatistics) {
		this.evictionStatistics = evictionStatistics;
	}
}
```

{{% /tab %}}

{{%tab "  Registering for JVM usage Threshold "%}}


```java
private void setJVMUsageThreshold() throws RuntimeException {

	// Get Memory MXBean
	MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();

	// Add current object as the listener
	NotificationEmitter ne = (NotificationEmitter) memBean;
	ne.addNotificationListener(this, null, null);

	// Get the memory pools supported by the JVM and size of each pool
	List<MemoryPoolMXBean> memPools = ManagementFactory
			.getMemoryPoolMXBeans();

	for (MemoryPoolMXBean pool : memPools) {

		if (pool.getType() == MemoryType.HEAP
				&& pool.isUsageThresholdSupported()) {

			heapMemory = pool;

			long maxMemory = pool.getUsage().getMax();
			evictionStartValue = (long) (maxMemory * (ec
					.getEvictionStartThreshold() / 100));
			evictionStopValue = (long) (maxMemory * (ec
					.getEvictionStopThreshold() / 100));

			logger.info("Maximum Heap Memory : " + maxMemory);
			logger.info("Eviction Starts at heap size : "
					+ evictionStartValue);

			try {
				pool.setUsageThreshold(evictionStartValue);
			} catch (Exception e) {
				logger
						.severe("***** Setting usage Threshold on Old Gen pool failed "
								+ e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
```

{{% /tab %}}

{{%tab "  Eviction Logic "%}}


```java
// Update the Watermark object to indicate eviction is in progress
wMark.setEvictionInProgress(true);
gs.write(wMark, Lease.FOREVER, 5000, UpdateModifiers.UPDATE_OR_WRITE);

evictStartTime = System.currentTimeMillis();

// Keep evicting until eviction limit is reached
// Some JVM's this may be expensive and might need
// to switch to a limit that is based on heuristics
while (currentMemoryUsage > evictionStopValue
		&& evictionObjectsAvailable) {

	long nextWindow = wMark.getBoundaryValue() + EVICTION_WINDOW_SIZE;

	String qryStr = "orderTime " + " < " + nextWindow + "L and processed = true";
	logger.info("SQL Query string \"" + qryStr + "\"");

	// Configure Transaction definition ...
	DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
	TransactionStatus status = tm.getTransaction(definition);

	boolean moreDataInBatch = true;
	try {
		SQLQuery template;

		template = new SQLQuery(wMark.getSpaceClass(),
				qryStr);

		// Clear data in smaller batches in order to not create lots of
		// garbage in space and trigger GC activity
		// Execute the Space.clear() with the SQL Query template
		gs.clear(template);

	} catch (Exception e) {
		e.printStackTrace();
	}

	// Update EvictionWaterMarkEntry with current eviction statistics
	wMark.setBoundaryValue(nextWindow);

	gs.write(wMark, Lease.FOREVER, 5000,
			UpdateModifiers.UPDATE_OR_WRITE);

	// Commit the Transaction
	tm.commit(status);

	if (nextWindow >= evictionMaxKey) {
		evictionObjectsAvailable = false;
	}

	currentMemoryUsage = heapMemory.getUsage().getUsed();
	logger.info("Current Memory Usage [" + currentMemoryUsage + "]");
}

evictEndTime = System.currentTimeMillis();

// Update the Watermark object with statistics and mark that eviction is
// complete
HashMap<Long, String> wMarkStatistics = wMark.getEvictionStatistics();

wMarkStatistics.put(evictStartTime, "startingMemoryUsage["
		+ evictionStartMemoryUsage + "],endingMemoryUsage["
		+ currentMemoryUsage + "],ended[" + evictEndTime + "]");

wMark.setEvictionStatistics(wMarkStatistics);

wMark.setEvictionInProgress(false);
gs.write(wMark, Lease.FOREVER, 5000, UpdateModifiers.UPDATE_OR_WRITE);
```

{{% /tab %}}

{{% /tabs %}}

### Running the example

{{% note %}}
This example is using Maven for packaging and build.

- Please [install the OpenSpaces Maven plugin]({{%latestjavaurl%}}/installation-maven-overview.html#MavenPlugin-Installation) before you run this example.
- Please update the GigaSpaces and Spring versions to appropriate versions in the pom.xml file.
{{% /note %}}

Extract the [example](/attachment_files/sbp/MyCustomEvictor.zip) archive into a folder. Navigate to the folder

Run maven clean using following command


```java
mvn clean
```

Run maven package (skip the tests) using following command


```java
mvn package -DskipTests
```

Start an instance of gs-agent using the script inside GigaSpacesRoot/bin/gs-agent.bin(sh). If your objective is to run the prototype and see it in action, please update your GSC_JVM_OPTIONS to have a Xmx and Xms of 128M.

Deploy the application using following command


```java
mvn os:deploy -Dmodule=customEviction
```

Invoke a gs-ui session to monitor the space.

Also Launch a JConsole session for the space that just started.

Run the test client using the following command


```java
mvn exec:java -Dexec.classpathScope=compile -Dexec.mainClass="com.gigaspaces.client.TestClient"
-Dexec.args="jini://*/*/mySpace"
```

Start monitoring the JVM Tenured pool in JConsole and GSC logs. After few seconds you will see that the memory threshold is breached and eviction logic will trigger and clear the old `Order` objects from space. Once the memory usage reached the eviction stop limit, eviction logic stops. You will see that the orders flow into the space constantly and the eviction logic triggers as and when needed making this a self healing application.

Below is a screenshot of Tenured pool of the JVM,
![HeapUsageGraph.png](/attachment_files/sbp/HeapUsageGraph.png)

# Important Considerations

- Defining a Eviction Start Threshold value that is proportionate to data load rates of the application and gives enough time for eviction logic. If eviction is not given enough time, application might reach Write Block Percentage which will result into  Memory Shortage Exceptions making things worse. Perform some tests and tune this threshold to fit your needs.
- When evicting data, evict data in small batches. Evicting large number of objects using a clear or take operations will sometimes overwhelm the JVM and trigger GC logic and might result application pauses.
- Don't overdo eviction and evict everything. Define a Eviction Stop Threshold value and leave important data in space for faster access. Eviction limit can either be count of objects, ratio of number of objects left in space or available memory. Example above uses available memory but you could one of the above options.
- Maintaining Statistics can help in troubleshooting. Example above stores the statistics as part of the Watermark object.
- Identifying memory usage of a space partition becomes tricky once more than one space partition shares the same GSC. JVM Memory Notification API strategy as described above may not work in those scenarios, you will need to build additional functionality to identify other cluster members and coordinate eviction across cluster members in order to make this work.

# External Data Source (EDS) Considerations

Applications using External Data Source Integration will need some changes to the eviction functionality. Because of EDS integration the take/clear will remove the data from the database also which might not be intended.

In cases where the Space is using [LRU]({{%latestadmurl%}}/lru-cache-policy.html) mode,

- Take or clear operation can be triggered using the [TakeModifiers.EVICT_ONLY]({{%latestadmurl%}}/lru-cache-policy.html#LRU-CachePolicy-ExplicitEvictionofObjectsfromtheSpace) which will remove the data only from space and not from the DB.

In [ALL_IN_CACHE]({{%latestadmurl%}}/all-in-cache-cache-policy.html) mode,

- Take or clear operations will remove the data from space and database (in read-write mode), you should use
    - Lease expiration option to remove the entries from the space and free up memory. When you restart the cluster, expired data will get loaded again and fill up the entire cluster. In order to avoid this you have to propagate the lease information into the DB (using the [SpaceLease]({{%latestjavaurl%}}/modeling-your-data.html) property).
    Objects which are cleared from the space using Lease expiration are not loaded automatically when someone queries for them, you will need to build custom functionality to retrieve this data.
    - Custom EDS mechanism that intercepts the eviction requests and stops propagating them into DB.
