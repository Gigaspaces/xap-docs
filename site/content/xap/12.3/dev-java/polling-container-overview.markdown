---
type: post123
title:  Polling Container
categories: XAP123, OSS
parent: event-processing.html
weight: 300
---


{{% imagertext  "/attachment_files/polling_container_basic.jpg"%}}
The polling event container is an implementation of the [polling consumer pattern](http://enterpriseintegrationpatterns.com/PollingConsumer.html)  which uses the space to receive events. The container performs polling receive operations against the Space. If a receive operation succeeds (a value is returned from the receive operation), the [Data Event Listener](./data-event-listener.html) is invoked with the event. A polling event operation is mainly used when simulating Queue semantics, or when using the master-worker design pattern.
{{%/imagertext%}}


# Life Cycle Events

The polling container life cycle events are described below. You can implement each one to perform the required activity.

{{%align center%}}
![dynamic_polling_container_life_cycle.jpg](/attachment_files/dynamic_polling_container_life_cycle.jpg)
{{%/align%}}

# Configuration

The following is a simple example of how to configure the polling event container:

{{%tabs%}}
{{%tab "Annotation"%}}
```xml
<!-- Enable scan for OpenSpaces and Spring components -->
<context:component-scan base-package="com.mycompany"/>

<!-- Enable support for @Polling annotation -->
<os-events:annotation-support />

<os-core:embedded-space id="space" space-name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>
```

```java
@EventDriven @Polling
public class SimpleListener {

    @EventTemplate
    Data unprocessedData() {
        Data template = new Data();
        template.setProcessed(false);
        return template;
    }

    @SpaceDataEvent
    public Data eventListener(Data event) {
        //process Data here
    }
}
```

{{% /tab %}}
{{%tab "Namespace"%}}
```xml
<os-core:embedded-space id="space" space-name="mySpace"/>
<os-core:giga-space id="gigaSpace" space="space"/>
<bean id="simpleListener" class="SimpleListener" />
<os-events:polling-container id="eventContainer" giga-space="gigaSpace">

    <os-core:template>
        <bean class="org.openspaces.example.data.common.Data">
            <property name="processed" value="false"/>
        </bean>
    </os-core:template>

    <os-events:listener>
        <os-events:annotation-adapter>
            <os-events:delegate ref="simpleListener"/>
        </os-events:annotation-adapter>
    </os-events:listener>
</os-events:polling-container>
```

{{% /tab %}}
{{%tab "Plain XML"%}}
```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="simpleListener" class="SimpleListener" />

<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="template">
        <bean class="org.openspaces.example.data.common.Data">
            <property name="processed" value="false"/>
        </bean>
    </property>

    <property name="eventListener">
    	<bean class="org.openspaces.events.adapter.AnnotationEventListenerAdapter">
    	    <property name="delegate" ref="simpleListener" />
    	</bean>
    </property>
</bean>
```
{{% /tab %}}
{{%tab "Code"%}}


```java
GigaSpace gigaSpace = // either create the GigaSpace or get it by injection

SimplePollingEventListenerContainer pollingEventListenerContainer = new SimplePollingContainerConfigurer(gigaSpace)
                .template(new Data(false))
                .eventListenerAnnotation(new Object() {
                    @SpaceDataEvent
                    public void eventHappened() {
                        eventCalled.set(true);
                    }
                }).pollingContainer();

// start the notification
pollingEventListenerContainer.start();
......
// when needed dispose of the notification container
pollingEventListenerContainer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

{{% note "Restrictions"%}}
 `EventDriven`, `@Polling`, and `@Notify` cannot be placed on interface classes. Place them on the implementation class instead.
{{% /note %}}

The above example performs single take operations (see below) using the provided template (a `Data` object with its processed flag set to `false`). If the take operation succeeds (a value is returned), the `SimpleListener` is invoked. The operations are performed on the configured [GigaSpace](./the-gigaspace-interface-overview.html) bean (in this case, if working in a clustered topology, it is performed directly on the cluster member).

# Primary/Backup

By default, the polling event container performs receive operations only when the relevant Space it is working against is in primary mode. When the Space is in backup mode, no receive operations are performed. If the Space changes from backup mode to primary mode, the receive operations are started.

{{% note "Note"%}}
This behavior applies when working with an embedded Space directly with a cluster member. When working with a clustered Space (performing operations against the whole cluster), the mode of the Space is always primary.
{{%/note%}}

# FIFO Grouping

The FIFO grouping is designed to allow efficient processing of events with partial ordering constraints. Instead of maintaining a FIFO queue per class type, the grouping provides a more granularity by having the FIFO queue maintained according to a specific value of a specific property.

{{%refer%}}
For more details, refer to [FIFO Grouping](./fifo-grouping.html).
{{%/refer%}}


# Static Template Definition

When performing receive operations, a template is defined. This creates a virtualized subset of data within the Space that matches it. XAP supports templates based on the actual domain model (with `null` values denoting wildcards), which are shown in the examples. XAP allows the use of [SQLQuery](./query-sql.html) in order to query the Space, which can be easily used with the event container as the template. The following is an example of how it can be defined:

{{%tabs%}}
{{%tab "Annotation"%}}
```java
@EventDriven @Polling
public class SimpleListener {

    @EventTemplate
    SQLQuery<Data> unprocessedData() {
        SQLQuery<Data> template = new SQLQuery<Data>(Data.class, "processed = true");
        return template;
    }

    @SpaceDataEvent
    public Data eventListener(Data event) {
        //process Data here
    }
}
```

{{% /tab %}}
{{%tab "Namespace"%}}


```xml
<os-events:polling-container id="eventContainer" giga-space="gigaSpace">

    <os-core:sql-query where="processed = true" class="org.openspaces.example.data.common.Data"/>

    <os-events:listener>
        <os-events:annotation-adapter>
            <os-events:delegate ref="simpleListener"/>
        </os-events:annotation-adapter>
    </os-events:listener>

</os-events:polling-container>
```

{{% /tab %}}
{{%tab "Plain XML"%}}


```xml
<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="template">
        <bean class="com.j_spaces.core.client.SQLQuery">
            <constructor index="0" value="org.openspaces.example.data.common.Data" />
            <constructor index="0" value="processed = true" />
        </bean>
    </property>

    <property name="eventListener">
    	<bean class="org.openspaces.events.adapter.AnnotationEventListenerAdapter">
    	    <property name="delegate" ref="simpleListener" />
    	</bean>
    </property>
</bean>
```

{{% /tab %}}
{{% /tabs %}}

{{% note "Restrictions"%}}
A polling or notify container can have only one template. If you need multiple event handlers, you must create another polling or notify container. If you use multiple polling containers, make sure the different templates don't overlap each other.
{{% /note %}}


# Multiple Event Handlers

You can define multiple event handlers for a polling container. If you have a superclass with subclasses and you want to define event handlers for each subclass, you can define the
event template for the superclass and a `@SpaceDataEvent` for each subclass.

The following is an example where HostInfo, MachineInfo and LdapInfo are subclasses of the MonitorInfo class:

```java
@EventDriven
@Polling
public class PollingExample {

	@EventTemplate
	public SQLQuery<MonitorInfo> dataTemplate() {
		return new SQLQuery<MonitorInfo>(MonitorInfo.class, "");
	}

	@SpaceDataEvent
	public MachineInfo eventListener(final MachineInfo event) {
		// ..........
		return null;
	}

	@SpaceDataEvent
	public MachineInfo eventListener(final HostInfo event) {
		// ..........
		return null;
	}

	@SpaceDataEvent
	public MachineInfo eventListener(final LdapInfo event) {
		// ..........
		return null;
	}
}
```

# Multiple Values Template

You can use a `SQLQuery` that contains an `IN` operator with multiple values to register a template with multiple values. This is a simple alternative to using multiple polling containers. See the following example:

{{%tabs%}}
{{%tab "Space Class"%}}

```java
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;

public class MyData {
	String id;
	String key;

	@SpaceId(autoGenerate=false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@SpaceIndex
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public String toString() {
		return "MyData [id=" + id + ", key=" + key + "]";
	}
}
```
{{%/tab%}}

{{%tab "Template registration:"%}}
```java
SimplePollingEventListenerContainer pollingEventListenerContainer =
	new SimplePollingContainerConfigurer(space)
		.template(query)
		.eventListenerAnnotation(new Object() {
    @SpaceDataEvent
    public void eventHappened(MyData data) {
        System.out.println("Polling Container Got matching event! - " +data);
    }
}).pollingContainer();
```
{{%/tab%}}
{{%/tabs%}}


# Dynamic Template Definition

When performing polling receive operations, a dynamic template can be used. A method providing a dynamic template is called before each receive operation, and can return a different object in each call.
The event template object has the same syntax rules as with `@EventTemplate`.

{{%tabs%}}
{{%tab "Annotation"%}}
```java
@EventDriven @Polling
public class SimpleListener {

    @DynamicEventTemplate
    SQLQuery<Data> unprocessedExpiredData() {
        long expired = System.currentTimeMillis() - 60000;
        SQLQuery<Data> dynamicTemplate =
          new SQLQuery<Data>(Data.class, "processed = true AND timestamp < " + expired);
        return dynamicTemplate;
    }

    @SpaceDataEvent
    public Data eventListener(Data event) {
        //process Data here
    }
}
```

{{% /tab %}}
{{%tab "Namespace"%}}


```xml
<os-events:polling-container id="eventContainer" giga-space="gigaSpace">

    <os-events:dynamic-template ref="dynamicTemplate" />

    <os-events:listener>
        <os-events:annotation-adapter>
            <os-events:delegate ref="simpleListener"/>
        </os-events:annotation-adapter>
    </os-events:listener>

</os-events:polling-container>
<bean id="dynamicTemplate" class="ExpiredDataTemplateProvider"/>
```


```java
public class ExpiredDataTemplateProvider implements DynamicEventTemplateProvider {

    @Override
    public Object getDynamicTemplate() {
        long expired = System.currentTimeMillis() - 60000;
        SQLQuery<Data> dynamicTemplate =
          new SQLQuery<Data>(Data.class, "processed = true AND timestamp < " + expired);
        return dynamicTemplate;
    }
}
```

{{% /tab %}}
{{%tab "Plain XML"%}}
```xml
<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />
    <property name="dynamicTemplate" ref="dynamicTemplate" />

    <property name="eventListener">
    	<bean class="org.openspaces.events.adapter.AnnotationEventListenerAdapter">
    	    <property name="delegate" ref="simpleListener" />
    	</bean>
    </property>
</bean>
<bean id="dynamicTemplate" class="ExpiredDataTemplateProvider"/>
```


```java
public class ExpiredDataTemplateProvider implements DynamicEventTemplateProvider {

    @Override
    public Object getDynamicTemplate() {
        long expired = System.currentTimeMillis() - 60000;
        SQLQuery<Data> dynamicTemplate =
          new SQLQuery<Data>(Data.class, "processed = true AND timestamp < " + expired);
        return dynamicTemplate;
    }
}
```

{{% /tab %}}
{{% /tabs %}}

{{% note "Note"%}}
Only polling containers support dynamic templates. Notify containers do not support dynamic templates.
{{% /note %}}

# Receive Operation Handler

The polling receive container performs receive operations. The actual implementation of the receive operation is abstracted using the following interface:


```java
public interface ReceiveOperationHandler {
    /**
     * Performs the actual receive operation. Return values allowed are single object or an array of
     * objects.
     *
     * @param template
     *            The template to use for the receive operation.
     * @param gigaSpace
     *            The GigaSpace interface to perform the receive operations with
     * @param receiveTimeout
     *            Receive timeout value
     * @return The receive result. <code>null</code> indicating no receive occured. Single object
     *         or an array of objects indicating the receive operation result.
     * @throws DataAccessException
     */
    Object receive(Object template, GigaSpace gigaSpace, long receiveTimeout) throws DataAccessException;
}
```

XAP comes with several built-in receive operation-handler implementations:


|Receive Operation Handler|Description|
|:------------------------|:----------|
|SingleTakeReceiveOperationHandler|Performs a single blocking take operation with the receive timeout.|
|SingleReadReceiveOperationHandler|Performs a single blocking read operation with the receive timeout.|
|ExclusiveReadReceiveOperationHandler|Performs a single read operation under an exclusive read lock (similar to "select for update" in databases) with the receive timeout. Exclusive read lock mimics the take operation without actually taking the Entry from the Space.<br>{{% exclamation %}} This receive operation handler must be used within a transaction.|
|MultiTakeReceiveOperationHandler|First tries to perform `takeMultiple` (using a configured max Entries). If no values are returned, performs a blocking take operation with the receive timeout.|
|MultiReadReceiveOperationHandler|First tries to perform `readMultiple` (using a configured max Entries). If no values are returned, performs a blocking read operation with the receive timeout.|
|MultiExclusiveReadReceiveOperationHandler|First tries to perform `readMultiple` (using a configured max Entries). If no values are returned, performs a blocking read operation with the receive timeout. Both read operations are performed under an exclusive read lock (similar to "select for update" in databases), which mimics a take operation without actually taking the Entry from the Space.<br>This receive operation handler must be used within a transaction. |

{{% note "Note"%}}
When using the `ExclusiveReadReceiveOperationHandler` or even the `SingleReadReceiveOperationHandler`, the actual event remains in the Space. If the data event is not taken from the Space, or one of its properties changes in order **not** to match the container template, the same data event is read again.
{{%/note%}}

The following is an example of how to configure the receive operation handler with `MultiTakeReceiveOperationHandler`:

{{%tabs%}}
{{%tab "Annotation"%}}
```java
@EventDriven @Polling
public class SimpleListener {

    @ReceiveHandler
    ReceiveOperationHandler receiveHandler() {
        MultiTakeReceiveOperationHandler receiveHandler = new MultiTakeReceiveOperationHandler();
        receiveHandler.setMaxEntries(100);
        return receiveHandler;
    }

    @EventTemplate
    Data unprocessedData() {
        Data template = new Data();
        template.setProcessed(false);
        return template;
    }

    @SpaceDataEvent
    public Data eventListener(Data event) {
        //process Data here
    }
}
```
{{% /tab %}}
{{%tab "Namespace"%}}
```xml
<os-core:embedded-space id="space" space-name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<bean id="simpleListener" class="SimpleListener" />

<os-events:polling-container id="eventContainer" giga-space="gigaSpace">
    <os-events:receive-operation-handler>
        <bean class="org.openspaces.events.polling.receive.MultiTakeReceiveOperationHandler" />
    </os-events:receive-operation-handler>

    <os-core:template>
        <bean class="org.openspaces.example.data.common.Data">
            <property name="processed" value="false"/>
        </bean>
    </os-core:template>

    <os-events:listener>
        <os-events:annotation-adapter>
            <os-events:delegate ref="simpleListener"/>
        </os-events:annotation-adapter>
    </os-events:listener>
</os-events:polling-container>
```

{{% /tab %}}
{{%tab "Plain XML"%}}
```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="simpleListener" class="SimpleListener" />

<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="receiveOperationHandler">
	    <bean class="org.openspaces.events.polling.receive.MultiTakeReceiveOperationHandler" />
    </property>

    <property name="template">
        <bean class="org.openspaces.example.data.common.Data">
            <property name="processed" value="false"/>
        </bean>
    </property>

    <property name="eventListener">
    	<bean class="org.openspaces.events.adapter.AnnotationEventListenerAdapter">
    	    <property name="delegate" ref="simpleListener" />
    	</bean>
    </property>
</bean>
```
{{% /tab %}}
{{% /tabs %}}

## Non-Blocking Receive Handler

When working with a partitioned cluster and configuring the remote polling container to work against the [whole cluster](./the-gigaspace-interface-overview.html#Clustered Flag), blocking operations (take with a timeout>0) are not allowed (when the routing field is not set on the template or SQLQuery). The default receive operation handlers support performing the receive operation in a non-blocking manner, by sleeping between non-blocking operations. For example, the `SingleTakeReceiveOperationHandler` performs a non-blocking take operation against the Space and then sleeps for a configurable amount of time. The [Master-Worker Pattern](/sbp/master-worker-pattern.html) is a classic scenario where non-blocking mode is used.

{{% section %}}
{{% column width="50%" %}}
**Step 1**: Master sends requests to be processed by the workers implemented using the polling container.

{{% indent %}}
![master_worker_rr1.jpg](/attachment_files/master_worker_rr1.jpg)
{{% /indent %}}

{{% /column %}}
{{% column width="50%" %}}
**Step 2**: Workers generate results that are consumed by the Master.

{{% indent %}}
![master_worker_rr2.jpg](/attachment_files/master_worker_rr2.jpg)
{{% /indent %}}

{{% /column %}}
{{% /section %}}

The following is an example of how to configure non-blocking mode:

{{%tabs%}}
{{%tab "Annotation"%}}
```java
@EventDriven @Polling (receiveTimeout=10000)
public class SimpleListener {

    @ReceiveHandler
    ReceiveOperationHandler receiveHandler() {
        SingleTakeReceiveOperationHandler receiveHandler = new SingleTakeReceiveOperationHandler();
        receiveHandler.setNonBlocking(true);
        receiveHandler.setNonBlockingFactor(10);
        return receiveHandler;
    }

    @EventTemplate
    Data unprocessedData() {
        Data template = new Data();
        template.setProcessed(false);
        return template;
    }

    @SpaceDataEvent
    public Data eventListener(Data event) {
        //process Data here
    }
}
```

{{% /tab %}}
{{%tab "Namespace"%}}
```xml
<os-core:embedded-space id="space" space-name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<bean id="simpleListener" class="SimpleListener" />

<os-events:polling-container id="eventContainer" giga-space="gigaSpace" receive-timeout="10000">
    <os-events:receive-operation-handler>
        <bean class="org.openspaces.events.polling.receive.SingleTakeReceiveOperationHandler">
            <property name="nonBlocking" value="true" />
            <property name="nonBlockingFactor" value="10" />
        </bean>
    </os-events:receive-operation-handler>

    <os-core:template>
        <bean class="org.openspaces.example.data.common.Data">
            <property name="processed" value="false"/>
        </bean>
    </os-core:template>

    <os-events:listener>
        <os-events:annotation-adapter>
            <os-events:delegate ref="simpleListener"/>
        </os-events:annotation-adapter>
    </os-events:listener>
</os-events:polling-container>
```
{{% /tab %}}
{{%tab "Plain XML"%}}
```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="simpleListener" class="SimpleListener" />

<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="receiveTimeout" value="10000" />

    <property name="receiveOperationHandler">
        <bean class="org.openspaces.events.polling.receive.SingleTakeReceiveOperationHandler">
            <property name="nonBlocking" value="true" />
            <property name="nonBlockingFactor" value="10" />
        </bean>
    </property>

    <property name="template">
        <bean class="org.openspaces.example.data.common.Data">
            <property name="processed" value="false"/>
        </bean>
    </property>

    <property name="eventListener">
    	<bean class="org.openspaces.events.adapter.AnnotationEventListenerAdapter">
    	    <property name="delegate" ref="simpleListener" />
    	</bean>
    </property>
</bean>
```
{{% /tab %}}
{{% /tabs %}}

The above example uses a receive timeout of 10 seconds (10000 milliseconds). The `SingleTakeReceiveOperationHandler` is configured to be non-blocking with a non-blocking factor of 10. This means that the receive handler performs 10 non-blocking takes within 10 seconds and sleeps the rest of the time (~1 second each time).

# Batch Operations and passArrayAsIs

Processing data in batches may improve the processing throughput performance. Instead of consuming one object at a time from the Space and processing it, you can consume a batch with multiple objects and process them in a single transaction. This can improve the overall throughput rate, but may also affect the latency of the individual object processing time.

See the following simple benchmark results comparing the different options:

{{% align center%}}
![poll_bench.jpg](/attachment_files/poll_bench.jpg)
{{% /align%}}


A full description of the benchmark is available in the {{%exurl "How to Implement my Processor? - The Polling Container Benchmark" "https://blog.gigaspaces.com/how-to-implement-my-processor-the-polling-container-benchmark/"%}}  GigaSpaces blog post.

You can use batching via the `MultiTakeReceiveOperationHandler`. The `MultiTakeReceiveOperationHandler.setMaxEntries(integer)` allows you to set the maximum amount of objects to be consumed with each polling event. If the Space does not have a  sufficient number of matching objects during the polling point in time, the event listener method is called with the existing number of matching objects (which is smaller than the `MaxEntries` value). THis doesn't cause any delay,  and the polling container doesn't wait until there is an exact amount of matching objects to consume as specified via the `MaxEntries` value.

Certain receive operation handlers may return an array as a result of the receive operation. A prime example is the `MultiTakeReceiveOperationHandler`, which may return an array as a result of a `takeMultiple` operation called by the polling container. By default, the polling container serializes the execution of the array into an invocation of the event listener method for each element in the array. If you want the event to operate on the entire array (receive the array as a parameter into the event listener method), the `passArrayAsIs` annotation should be set to **true**.

The following is an example of batch processing using the `passArrayAsIs`. In this example, the polling container consumes a batch of objects using `takeMultiple`, modifies them, and writes them back into the Space in a single operation using `writeMultiple`:

{{%tabs%}}
{{%tab "Annotation"%}}
```java
@EventDriven
@Polling(passArrayAsIs = true)
public class SimpleBatchListener {

    @ReceiveHandler
    ReceiveOperationHandler receiveHandler() {
        MultiTakeReceiveOperationHandler receiveHandler = new MultiTakeReceiveOperationHandler();
        receiveHandler.setMaxEntries(100);
        return receiveHandler;
    }

    @EventTemplate
    Data unprocessedData() {
        Data template = new Data();
        template.setProcessed(false);
        return template;
    }

    @SpaceDataEvent
    public Data[] eventListener(Data events[]) {
        //process Data within a loop
	    for (int i = 0; i < events.length; i++) {
		   events[i].setProcessed(true);
	    }
        return events;
    }
}
```
{{% /tab %}}
{{%tab "Plain XML"%}}
```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
    <property name="space" ref="space" />
</bean>
<bean id="simpleBatchListener" class="SimpleBatchListener" />

<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="passArrayAsIs" value="true" />

    <property name="receiveOperationHandler">
        <bean class="org.openspaces.events.polling.receive.MultiTakeReceiveOperationHandler">
            <property name="maxEntries" value="100" />
        </bean>
    </property>

    <property name="template">
        <bean class="org.openspaces.example.data.common.Data">
            <property name="processed" value="false"/>
        </bean>
    </property>

    <property name="eventListener">
    	<bean class="org.openspaces.events.adapter.AnnotationEventListenerAdapter">
    	    <property name="delegate" ref="simpleBatchListener" />
    	</bean>
    </property>
</bean>

```
{{% /tab %}}
{{% /tabs %}}

# Free Polling Container Resources

To free the resources used by the polling container, make sure you close it properly. A good life cycle event to place the `destroy()` call is within the `@PreDestroy` or `DisposableBean.destroy()` method.



# Polling Container Life Cycle

The Polling Event Listener container supports several life cycle methods. These allow you to create, start, stop, and destroy the listener programmatically. There is also the `setActiveWhenPrimary` life cycle mode, which binds the container to the Space mode when set to **TRUE**, starting it when the Space mode is PRIMARY and stopping it otherwise.
 
You can get the exact status of the Polling Event Listener container using the `isActive()`  and `isRunning()` methods. The following is a simple example that illustrates the different Polling Event Listener container life cycle methods:


{{%tabs%}}
{{%tab PollingContainerLifeCycle%}}
```java
import java.util.Calendar;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.EmbeddedSpaceConfigurer;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.SimplePollingContainerConfigurer;
import org.openspaces.events.polling.SimplePollingEventListenerContainer;

public class PollingContainerLifeCycleMain {
	static SimplePollingEventListenerContainer pollingEventListenerContainer;
	static GigaSpace gigaSpace;

	public static void main(String[] args) throws Exception {

		gigaSpace = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer("mySpace")).gigaSpace();

		// Write data to the space
		gigaSpace.write(new Data());
		say("wrote object to space");
		say("pollingContainer about to be created");

		// create a polling listener
		pollingEventListenerContainer = new SimplePollingContainerConfigurer(gigaSpace).template(new Data())
				.autoStart(false).eventListenerAnnotation(new Object() {
					@SpaceDataEvent
					public void eventHappened() {
						say("event consumed");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).pollingContainer();

		say("pollingContainer created");
		Thread.sleep(1000);
		say("pollingContainer about to be started");
		pollingEventListenerContainer.start();
		say("pollingContainer started");
		Thread.sleep(1000);

		say("pollingContainer about to be stopped");
		pollingEventListenerContainer.stop();
		say("pollingContainer stoped");
		Thread.sleep(1000);

		say("pollingContainer about to be restarted");
		pollingEventListenerContainer.start();
		say("pollingContainer started");
		Thread.sleep(1000);

		say("pollingContainer about to be destroyed");
		pollingEventListenerContainer.destroy();
		say("pollingContainer destroyed");
		System.exit(0);
	}

	static public void say(String mes) {
		Calendar d = Calendar.getInstance();

		int ms = Calendar.getInstance().get(Calendar.MILLISECOND);
		String t = d.getTime() + ":" + ms;

		if (pollingEventListenerContainer == null)
			System.out.println(t + " - " + " isActive:" + "false" + " isRunning:" + "false" + " " + mes);
		else
			System.out.println(t + " - " + " isActive:" + pollingEventListenerContainer.isActive() + " isRunning:"
					+ pollingEventListenerContainer.isRunning() + " " + mes);
	}
}
```
{{%/tab%}}
{{%tab Data%}}
```java
import com.gigaspaces.annotation.pojo.SpaceId;

public class Data {

	private String id;

	public Data() {
	}

	@SpaceId(autoGenerate = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
```
{{%/tab%}}
{{%/tabs%}}

When running the above example,the following output is displayed:

```bash
Sun Jul 31 13:24:24 CAT 2016:466 -  isActive:false isRunning:false wrote object to space
Sun Jul 31 13:24:24 CAT 2016:466 -  isActive:false isRunning:false pollingContainer about to be created
Sun Jul 31 13:24:24 CAT 2016:484 -  isActive:true isRunning:false pollingContainer created
Sun Jul 31 13:24:25 CAT 2016:484 -  isActive:true isRunning:false pollingContainer about to be started
Sun Jul 31 13:24:25 CAT 2016:484 -  isActive:true isRunning:true pollingContainer started
Sun Jul 31 13:24:25 CAT 2016:511 -  isActive:true isRunning:true event consumed
Sun Jul 31 13:24:26 CAT 2016:486 -  isActive:true isRunning:true pollingContainer about to be stopped
Sun Jul 31 13:24:26 CAT 2016:486 -  isActive:true isRunning:false pollingContainer stopped
Sun Jul 31 13:24:27 CAT 2016:486 -  isActive:true isRunning:false pollingContainer about to be restarted
Sun Jul 31 13:24:27 CAT 2016:486 -  isActive:true isRunning:true pollingContainer started
Sun Jul 31 13:24:28 CAT 2016:487 -  isActive:true isRunning:true pollingContainer about to be destroyed
Sun Jul 31 13:24:28 CAT 2016:489 -  isActive:false isRunning:false pollingContainer destroyed
```


# Trigger Receive Operation

When configuring the polling event container to perform its receive operation and event actions under a transaction, a transaction is started and rolled back for each unsuccessful receive operation. This results in a higher load on the Space. The polling event container allows pluggable logic to be used in order to decide if the actual receive operation should be performed or not. This logic, called the trigger receive operation, is performed outside the receive transaction boundaries. The following interface is provided for custom implementation of this logic:


```java
public interface TriggerOperationHandler {
    /**
     * Allows to perform a trigger receive operation which control if the active receive operation
     * will be performed in a polling event container. This feature is mainly used when having
     * polling event operations with transactions where the trigger receive operation is performed
     * outside of a transaction thus reducing the creation of transactions did not perform the
     * actual receive operation.
     *
     * <p>
     * If this operation returns a non <code>null</code> value, it means that the receive
     * operation should take place. If it returns a <code>null</code> value, no receive operation
     * will be attempted.
     *
     * @param template
     *            The template to use for the receive operation.
     * @param gigaSpace
     *            The GigaSpace interface to perform the receive operations with
     * @param receiveTimeout
     *            Receive timeout value
     * @throws DataAccessException
     *
     */
    Object triggerReceive(Object template, GigaSpace gigaSpace, long receiveTimeout) throws DataAccessException;

    /**
     * Controls if the object returned from
     * {@link #triggerReceive(Object,org.openspaces.core.GigaSpace,long)} will be used as the
     * template for the receive operation by returning <code>true</code>. If <code>false</code>
     * is returned, the actual template configured in the polling event container will be used.
     */
    boolean isUseTriggerAsTemplate();
}
```

OpenSpaces comes with a built-in implementation of this interface, called `ReadTriggerOperationHandler`. It performs a single blocking read operation (using the provided receive timeout), "peeking" into the space for relevant event data. If the read operation returns a value, this means that there is higher probability that the receive operation will succeed, and the transaction won't be started without a purpose. You can configure it as follows:

{{%tabs%}}
{{%tab "Annotation"%}}
```java
@EventDriven @Polling @TransactionalEvent
public class SimpleListener {

    @TriggerHandler
    TriggerOperationHandler receiveHandler() {
        ReadTriggerOperationHandler triggerHandler = new ReadTriggerOperationHandler();
        return triggerHandler;
    }

    @EventTemplate
    Data unprocessedData() {
        Data template = new Data();
        template.setProcessed(false);
        return template;
    }

    @SpaceDataEvent
    public Data eventListener(Data event) {
        //process Data here
    }
}
```

{{% /tab %}}
{{%tab "Namespace"%}}
```xml
<os-core:embedded-space id="space" space-name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<os-core:distributed-tx-manager id="transactionManager" />

<bean id="simpleListener" class="SimpleListener" />

<os-events:polling-container id="eventContainer" giga-space="gigaSpace">
    <os-events:tx-support tx-manager="transactionManager"/>

    <os-events:trigger-operation-handler>
        <bean class="org.openspaces.events.polling.trigger.ReadTriggerOperationHandler" />
    </os-events:trigger-operation-handler>

    <os-core:template>
        <bean class="org.openspaces.example.data.common.Data">
            <property name="processed" value="false"/>
        </bean>
    </os-core:template>

    <os-events:listener>
        <os-events:annotation-adapter>
            <os-events:delegate ref="simpleListener"/>
        </os-events:annotation-adapter>
    </os-events:listener>
</os-events:polling-container>
```

{{% /tab %}}
{{%tab "Plain XML"%}}
```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="transactionManager" class="org.openspaces.core.transaction.manager.DistributedJiniTransactionManager" />

<bean id="simpleListener" class="SimpleListener" />

<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">
    <property name="transactionManager" ref="transactionManager" />

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="triggerOperationHandler">
        <bean class="org.openspaces.events.polling.trigger.ReadTriggerOperationHandler" />
    </property>

    <property name="template">
        <bean class="org.openspaces.example.data.common.Data">
            <property name="processed" value="false"/>
        </bean>
    </property>

    <property name="eventListener">
    	<bean class="org.openspaces.events.adapter.AnnotationEventListenerAdapter">
    	    <property name="delegate" ref="simpleListener" />
    	</bean>
    </property>
</bean>
```
{{% /tab %}}
{{% /tabs %}}

{{% refer %}}
Learn more using about `TriggerOperationsHandler` using an example application on the [Patterns wiki](/sbp/dynamic-polling-container-templates-using-triggeroperationhandler.html)
{{% /refer %}}

# Default Values of Polling Container Configuration Parameters

The default values for all of the polling container configuration parameters such as `concurrent-consumers, active-when-primary, receive-timeout` and others can be found in the JavaDoc (and sources) of the class [SimplePollingEventListenerContainer]({{% api-javadoc %}}/org/openspaces/events/polling/SimplePollingEventListenerContainer.html) and its super class, namely [AbstractEventListenerContainer]({{% api-javadoc %}}/org/openspaces/events/AbstractEventListenerContainer.html).

For example, `concurrent-consumers` default value is documented in the method `SimplePollingEventListenerContainer.setConcurrentConsumers(int)`



{{% include "/COM/notify-verses-polling.markdown" %}}

# Notify vs. Polling Container

The `Polling container` behavior is different than the `Notify Container` behavior. Comparing the matching phase (which is somewhat similar) that both conduct, is not enough.

## Notify Container

The Notify Container is triggered without any feedback loop control; it may be called concurrently without any control by many threads. This can reduce latency. but can also cause a scenario where the Space and client get overloaded without any ability to throttle the activity, which increases the latency. It may also cause locking issues if the Notify Container logic has to update the same data, which also increases latency.

Durability can also be affected (may lose events on failure), which can be handled with guaranteed notifications that impose some overhead and additional latency. The Notify container supports remote Spaces without major issues.


## Polling Container

The Polling Container acts like a queue. If you have one concurrent consumer thread, this can affect the overall latency. However, you can control the concurrency; it is always guaranteed and won't generate locking when updating objects.

The Polling container behavior towards remote Spaces is different from that of the Notify container, because you can't broadcast a blocking take operation against multiple Spaces. In order to perform operations on remote Spaces, the Polling container must run in non-blocking mode, which can introduce additional latency.

Trying to reduce latency by adjusting the frequency of the sampling rate can affect CPU utilization and overall system overhead, because the client performs non-blocking take operations in a round-robin fashion against the different partitions.
The number of partitions and the distribution of data affect latency; many partitions and uneven data distribution create additional latency with the Polling Container invocation.

As a workaround to using non-blocking take operations, you can construct an array of Polling Containers, each with a specific routing, which allows you to use a blocking take to reduce latency.

{{%refer%}}
An example is described [here](/sbp/master-worker-pattern.html#example-2-designated-workers) in the Solutions and Patterns section.
{{%/refer%}}


# Additional Resources

{{%youtube "GwLfDYgl6f8"  "Event Processing"%}}


