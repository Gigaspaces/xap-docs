---
type: post97
title:  Polling Container
categories: XAP97
parent: event-processing.html
weight: 300
---

{{% ssummary  %}}{{%/ssummary%}}

{{% section %}}
{{% column width="70%" %}}
The polling event container is an implementation of the [polling consumer pattern](http://enterpriseintegrationpatterns.com/PollingConsumer.html)  which uses the space to receive events. It performs polling receive operations against the space. If a receive operation succeeds (a value is returned from the receive operation), the [Data Event Listener](./data-event-listener.html) is invoked with the event. A polling event operation is mainly used when simulating Queue semantics or when using the master-worker design pattern.
{{% /column %}}
{{% column width="25%" %}}
![polling_container_basic.jpg](/attachment_files/polling_container_basic.jpg)
{{% /column %}}
{{% /section %}}


# Life Cycle Events

The polling container life cycle events described below. You may implement each of of these to perform the desired activity.
![dynamic_polling_container_life_cycle.jpg](/attachment_files/dynamic_polling_container_life_cycle.jpg)

## Configuration

Here is a simple example of polling event container configuration:

{{%tabs%}}
{{%tab "  Annotation "%}}


```xml

<!-- Enable scan for OpenSpaces and Spring components -->
<context:component-scan base-package="com.mycompany"/>

<!-- Enable support for @Polling annotation -->
<os-events:annotation-support />

<os-core:space id="space" url="/./space" />

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
{{%tab "  Namespace "%}}


```xml

<os-core:space id="space" url="/./space" />
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
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.UrlSpaceFactoryBean">
    <property name="url" value="/./space" />
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
{{%tab "  Code "%}}


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

// start the listner
pollingEventListenerContainer.start();

..

// when needed dispose of the notification container
pollingEventListenerContainer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

{{% tip %}}
 `@EventDriven` , `@Polling` , `@Notify` can't be placed on interface classes. You should place these on the implementation class.
{{% /tip %}}

The example above performs single take operations (see below) using the provided template (a `Data` object with its processed flag set to `false`). If the take operation succeeds (a value is returned), the `SimpleListener` is invoked. The operations are performed on the configured [GigaSpace](./the-gigaspace-interface.html) bean (in this case, if working in a clustered topology, it is performed directly on the cluster member).

# Primary/Backup

The polling event container, by default, performs receive operations only when the relevant space it is working against is in primary mode. When the space is in backup mode, no receive operations are performed. If the space moves from backup mode to primary mode, the receive operations are started.

{{% info %}}
This mostly applies when working with an embedded space directly with a cluster member. When working with a clustered space (performing operations against the whole cluster), the mode of the space is always primary.
{{%/info%}}

# FIFO Grouping

The FIFO Grouping designed to allow efficient processing of events with partial ordering constraints. Instead of maintaining a FIFO queue per class type, it lets you have a higher level of granularity by having FIFO queue maintained according to a specific value of a specific property. For more details see [FIFO grouping](./fifo-grouping.html).

# Concurrent Consumers

By default, the polling event container starts a single thread that performs the receive operations and invokes the event listener. It can be configured to start several concurrent consumer threads and have an upper limit to the concurrent consumer threads. This provides faster processing of events, however, any FIFO behavior that might be configured in the space and/or template is lost.

In order to receive events using multiple consumer threads, in the same order they are written to the Space:

- You may start multiple polling containers consuming data in FIFO manner , each running a single consumer thread using different (mutually exclusive) templates. Each thread may potentially consume a batch of space objects at a time.
- You may use one polling container with multiple consumer threads and enable FIFO Grouping. The FIFO order of each value is not broken. See [FIFO Grouping](./fifo-grouping.html) page for more details.  Each thread may potentially consume a batch of space objects at a time.

Here is an example of a polling container with 3 concurrent consumers and 5 maximum concurrent consumers:

{{%tabs%}}
{{%tab "  Annotation "%}}


```java
@EventDriven @Polling(concurrentConsumers = 3, maxConcurrentConsumers = 5)
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
{{%tab "  Namespace "%}}


```xml

<os-events:polling-container id="eventContainer" giga-space="gigaSpace"
                             concurrent-consumers="3" max-concurrent-consumers="5">
    <!-- ... -->
</os-events:polling-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">
    <property name="concurrentConsumers" value="3" />
    <property name="maxConcurrentConsumers" value="5" />
    <!-- ... -->
</bean>
```

{{% /tab %}}
{{% /tabs %}}

Sometimes, it is very convenient to have a listener instance per concurrent polling thread. This allows a thread-safe instance variable to be constructed without worrying about concurrent access. In such a case, the `prototype` Spring scope can be used in conjunction with a `listenerRef` bean name injection. Here is an example:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<bean id="listener" class="eg.SimpleListener" scope="prototype" />

<os-events:annotation-adapter id="adapterListener" scope="prototype">
    <os-events:delegate ref="listener"/>
</os-events:annotation-adapter>

<os-events:polling-container id="eventContainer" giga-space="gigaSpace" concurrent-consumers="2">
    <!-- ... -->
    <os-events:listener ref="adapterListener" />
</os-events:polling-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="listener" class="eg.SimpleListener" scope="prototype" />

<bean id="adapterListener" class="org.openspaces.events.adapter.AnnotationEventListenerAdapter" scope="prototype">
    <property name="delegate" ref="listener" />
</bean>

<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">
    <property name="concurrentConsumers" value="2" />
    <property name="eventListenerRef" value="adapterListener" />
    <!-- ... -->
</bean>
```

{{% /tab %}}
{{% /tabs %}}

# Static Template Definition

When performing receive operations, a template is defined, creating a virtualized subset of data within the space that matches it. GigaSpaces supports templates based on the actual domain model (with `null` values denoting wildcards), which are shown in the examples. GigaSpaces allows the use of [SQLQuery](./query-sql.html) in order to query the space, which can be easily used with the event container as the template. Here is an example of how it can be defined:

{{%tabs%}}
{{%tab "  Annotation "%}}


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
{{%tab "  Namespace "%}}


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
{{%tab "  Plain XML "%}}


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

{{% tip %}}
A polling container or notify container could have only one template. If you need multiple event handlers you will need to create another polling container or notify container. If you use multiple polling containers make sure the different templates does not overlap each other.
{{% /tip %}}


# Multiple Event Handlers

It is possible to define multiple event handlers for a polling container. If you have a superclass that has   subclasses, and you want to define event handlers for each subclass, you can define the
event template for the superclass and a @SpaceDataEvent for each subclass.

Here is an Example where HostInfo, MachineInfo and LdapInfo are subclasses of the MonitorInfo class:

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

You may use a `SQLQuery` having `IN` operator with multiple values to register a Template with multiple values. This can be a simple alternative avoiding using multiple polling containers. See below example:


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

The Template registration:


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

# Dynamic Template Definition

When performing polling receive operations, a dynamic template can be used. A method providing a dynamic template is called before each receive operation, and can return a different object in each call.
The event template object has the same syntax rules as with @EventTemplate.

{{%tabs%}}
{{%tab "  Annotation "%}}


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
{{%tab "  Namespace "%}}


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
{{%tab "  Plain XML "%}}


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

{{% tip %}}
Only polling containers support dynamic templates. Notify containers do not support dynamic templates.
{{% /tip %}}

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

OpenSpaces comes with several built-in receive operation-handler implementations:


|Receive Operation Handler|Description|
|:------------------------|:----------|
|`SingleTakeReceiveOperationHandler`|Performs a single blocking take operation with the receive timeout.|
|`SingleReadReceiveOperationHandler`|Performs a single blocking read operation with the receive timeout.|
|`ExclusiveReadReceiveOperationHandler`|Performs a single read operation under an exclusive read lock (similar to "select for update" in databases) with the receive timeout. Exclusive read lock mimics the take operation without actually taking the Entry from the space. {{% exclamation %}} This receive operation handler must be used within a transaction.|
|`MultiTakeReceiveOperationHandler`|First tries to perform takeMultiple (using a configured max Entries). If no values are returned, performs a blocking take operation with the receive timeout.|
|`MultiReadReceiveOperationHandler`|First tries to perform readMultiple (using a configured max Entries). If no values are returned, performs a blocking read operation with the receive timeout.|
|`MultiExclusiveReadReceiveOperationHandler`|First tries to perform readMultiple (using a configured max Entries). If no values are returned, performs a blocking read operation with the receive timeout. Both read operations are performed under an exclusive read lock (similar to "select for update" in databases) which mimics a take operation without actually taking the Entry from the space. Note, this receive operation handler must be used within a transaction. |

{{% info %}}
When using the `ExclusiveReadReceiveOperationHandler` or even the `SingleReadReceiveOperationHandler`, it is important to remember that the actual event still remains in the space. If the data event is not taken from the space, or one of its properties changes in order **not** to match the container template, the same data event is read again.
{{%/info%}}

Here is an example of how the receive operation handler can be configured with `MultiTakeReceiveOperationHandler`:

{{%tabs%}}
{{%tab "  Annotation "%}}


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
{{%tab "  Namespace "%}}


```xml

<os-core:space id="space" url="/./space" />

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
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.UrlSpaceFactoryBean">
    <property name="url" value="/./space" />
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

When working with a partitioned cluster and configuring the remote polling container to work against the [whole cluster](./the-gigaspace-interface.html#Clustered Flag), blocking operations (take with a timeout>0) are not allowed (when the routing field is not set on the template or SQLQuery). The default receive operation handlers support performing the receive operation in a non-blocking manner, by sleeping between non-blocking operations. For example, the `SingleTakeReceiveOperationHandler` performs a non-blocking take operation against the space and then sleeps for a configurable amount of time. A classic scenario where the Non-Blocking mode would be used is the [Master-Worker Pattern](/sbp/master-worker-pattern.html).

{{% section %}}
{{% column width="50%" %}}
Step 1 - Master sending requests to process by the workers implemented using the polling container:

{{% indent %}}
![master_worker_rr1.jpg](/attachment_files/master_worker_rr1.jpg)
{{% /indent %}}

{{% /column %}}
{{% column width="50%" %}}
Step 2  - Workers generating results which are consumed by the Master:

{{% indent %}}
![master_worker_rr2.jpg](/attachment_files/master_worker_rr2.jpg)
{{% /indent %}}

{{% /column %}}
{{% /section %}}

Here is an example of how a Non-Blocking mode can be configured:

{{%tabs%}}
{{%tab "  Annotation "%}}


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
{{%tab "  Namespace "%}}


```xml

<os-core:space id="space" url="/./space" />

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
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.UrlSpaceFactoryBean">
    <property name="url" value="/./space" />
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

Processing data in batches may improve the processing throughput performance. Instead of consuming object one at a time from the space and processing it, you may consume a batch with multiple objects and process these in one transaction. This may improve the overall throughput rate, but may impact the latency of the individual object processing time.

See below a simple benchmark results comparing the different options:

{{% align center%}}
![poll_bench.jpg](/attachment_files/poll_bench.jpg)
{{% /align%}}


See full benchmark description on the [How to Implement my Processor? - The Polling Container Benchmark](https://blog.gigaspaces.com/how-to-implement-my-processor-the-polling-container-benchmark/) GigaSpaces blog site.

You may use batching via the `MultiTakeReceiveOperationHandler`. The `MultiTakeReceiveOperationHandler.setMaxEntries(integer)` allows you to set the maximum amount of objects to be consumed with each polling event. If the space does not have sufficient number of matching objects during the polling point in time, the event listener method will be called with the existing number of matching objects (will be smaller than the `MaxEntries` value. There will be no delay in such a case and the polling container will not wait until there will be exact amount of matching objects to consume as specified via the `MaxEntries`.

Certain receive operation handlers might return an array as a result of the receive operation. A prime example is the `MultiTakeReceiveOperationHandler`, which might return an array as a result of a `takeMultiple` operation called by the polling container. By default, the polling container serializes the execution of the array into invocation of the event listener method for each element in the array. If you want the event to operate on the whole array (receive the array as a parameter into the event listener method), the `passArrayAsIs` annotation should be set to **true**.

Here is an example for batch processing using the `passArrayAsIs` - with this example the polling container will consume a batch of objects using `takeMultiple`, modify these and write these back into the space in one operation using `writeMultiple`:

{{%tabs%}}
{{%tab "  Annotation "%}}


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
{{%tab "  Plain XML "%}}

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

To free the resources used by the polling container make sure you close it properly. A good life cycle event to place the `destroy()` call would be within the `@PreDestroy` or `DisposableBean.destroy()` method.

# Transaction Support

Both the receive operation and the actual event action can be configured to be performed under a transaction. Transaction support is required when, for example, an exception occurs in the event listener, and the receive operation needs to be to rolled back (and the actual data event is returned to the space). Adding transaction support is very simple in the polling container, and can be done by injecting a transaction manager into it. For example:

{{%tabs%}}
{{%tab "  Annotation "%}}


```xml

<!-- Enable scan for OpenSpaces and Spring components -->
<context:component-scan base-package="com.mycompany"/>

<!-- Enable support for @Polling annotation -->
<os-events:annotation-support />

<os-core:space id="space" url="/./space" />

<os-core:distributed-tx-manager id="transactionManager" />

<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>
```


```java
@EventDriven @Polling @TransactionalEvent
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
{{%tab "  Namespace "%}}


```xml

<os-core:space id="space" url="/./space" />

<os-core:distributed-tx-manager id="transactionManager" />

<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

<bean id="simpleListener" class="SimpleListener" />

<os-events:polling-container id="eventContainer" giga-space="gigaSpace">
    <os-events:tx-support tx-manager="transactionManager"/>

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
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.UrlSpaceFactoryBean">
    <property name="url" value="/./space" />
</bean>

<bean id="transactionManager" class="org.openspaces.core.transaction.manager.DistributedJiniTransactionManager"/>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
    <property name="space" ref="space" />
	<property name="transactionManager" ref="transactionManager" />
</bean>

<bean id="simpleListener" class="SimpleListener" />

<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">
    <property name="transactionManager" ref="transactionManager" />

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
{{%tab "  Code "%}}


```java

GigaSpace gigaSpace = ...//create a GigaSpace instance

//creating a transaction manager. For more details please refer to the [Transaction Management] section
PlatformTransactionManager ptm = new DistributedJiniTxManagerConfigurer().transactionManager();

//creating a listener
SimpleListener listener = new SimpleListener();

//creating a polling container which will automatically start receiving event from the space
SimplePollingEventListenerContainer pollingContainer configurer = new SimplePollingContainerConfigurer(gigaSpace)
                .template(listener.getTemplate()).eventListenerAnnotation(listener)
                .transactionManager(ptm)
                .receiveTimeout(1000)
                .pollingContainer();

pollingContainer.start();

```

{{% /tab %}}
{{% /tabs %}}

When using transactions with polling container a special care should be taken with timeout values. Transactions started by the polling container can have a timeout value associated with them (if not set will default to the default timeout value of the transaction manager, which is 90 Sec). If setting a specific timeout value, make sure the timeout value is higher than the timeout value for blocking operations and includes the expected execution time of the associated listener.

{{% info %}}
Note the timeout value is in seconds as per Spring spec for TransactionDefinition.
{{%/info%}}

Here is an example how timeout value (and transaction isolation) can be set with polling container:

{{%tabs%}}
{{%tab "  Annotation "%}}


```xml

<!-- Enable scan for OpenSpaces and Spring components -->
<context:component-scan base-package="com.mycompany"/>

<!-- Enable support for @Polling annotation -->
<os-events:annotation-support />

<os-core:space id="space" url="/./space" />

<os-core:distributed-tx-manager id="transactionManager"/>

<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>
```


```java
@EventDriven @Polling @TransactionalEvent(isolation = Isolation.READ_COMMITTED, timeout = 1000)
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
{{%tab "  Namespace "%}}


```xml

<os-core:space id="space" url="/./space" />

<os-core:giga-space id="gigaSpace" space="space"/>

<os-core:distributed-tx-manager id="transactionManager" />

<bean id="simpleListener" class="SimpleListener" />

<os-events:polling-container id="eventContainer" giga-space="gigaSpace">
    <os-events:tx-support tx-manager="transactionManager" tx-timeout="1000" tx-isolation="READ_COMMITTED" />

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
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.UrlSpaceFactoryBean">
    <property name="url" value="/./space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="transactionManager" class="org.openspaces.core.transaction.manager.DistributedJiniTransactionManager" />

<bean id="simpleListener" class="SimpleListener" />

<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="transactionManager" ref="transactionManager" />
	<property name="transactionTimeout" value="1000" />
	<property name="transactionIsolationLevelName" value="READ_COMMITTED" />

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

# Trigger Receive Operation

When configuring the polling event container to perform its receive operation and event actions under a transaction, a transaction is started and rolled back for each unsuccessful receive operation, which results in a higher load on the space. The polling event container allows pluggable logic to be used in order to decide if the actual receive operation should be performed or not. This logic, called the trigger receive operation, is performed outside the receive transaction boundaries. The following interface is provided for custom implementation of this logic:


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

OpenSpaces comes with a built-in implementation of this interface, called `ReadTriggerOperationHandler`. It performs a single blocking read operation (using the provided receive timeout), thus "peeking" into the space for relevant event data. If the read operation returns a value, this means that there is higher probability that the receive operation will succeed, and the transaction won't be started without a purpose. Here is how it can be configured:

{{%tabs%}}
{{%tab "  Annotation "%}}


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
{{%tab "  Namespace "%}}


```xml

<os-core:space id="space" url="/./space" />

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
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.UrlSpaceFactoryBean">
    <property name="url" value="/./space" />
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

{{% tip %}}
Learn more using about `TriggerOperationsHandler` using an example application on the [Patterns wiki](/sbp/dynamic-polling-container-templates-using-triggeroperationhandler.html)
{{% /tip %}}

# Default Values of Polling Container Configuration Parameters

The default values for all of the polling container configuration parameters such as `concurrent-consumers, active-when-primary, receive-timeout` and others can be found in the JavaDoc (and sources) of the class [SimplePollingEventListenerContainer]({{% api-javadoc %}}/org/openspaces/events/polling/SimplePollingEventListenerContainer.html) and its super class, namely [AbstractEventListenerContainer]({{% api-javadoc %}}/org/openspaces/events/AbstractEventListenerContainer.html).

For example, `concurrent-consumers` default value is documented in the method `SimplePollingEventListenerContainer.setConcurrentConsumers(int)`


{{% include "/COM/notify-verses-polling.markdown" %}}

# Notify verses Polling Container

The `Polling container` behavior is different than the `Notify Container`. Comparing the matching phase (that is somehow similar) that both conduct, is not enough.

## Notify Container

The Notify Container is triggered without any feedback loop control - it may be called concurrently without any control by many threads.
This may reduce the latency but generate scenarios where you overload the Space and client without any ability to throttle the activity that will increase the latency.
It may also cause locking issues in case the Notify Container logic need to update the same data that will increase the latency.
It also has issues with durability (may loose events on failure) - that can be handled with guaranteed notifications that impose some overhead and additional latency.
It does support remote Spaces without major issues.


## Polling Container

The Polling Container acts like a queue. If you have one concurrent consumer thread this may impact the overall latency.
Still , you may control the concurrency , its always guaranteed and won't generate locking when updating objects.
It won't support remote Spaces as you can't perform blocking take with a null routing value against a remote partitioned Space .
This means you should run in non-blocking mode. This may introduce additional latency. Reducing it (high frequency sampling rate) will impact CPU utilization and overall system overhead as the client will perform a non-blocking take operation in a round robin fashion against the different partitions.
The more partitions you have and non-even distribution split of the data will result latency with the Polling Container invocation.

You may construct an array of Polling Containers - each with a specific routing that will allow you to use blocking take that will reduce the latency.

{{%refer%}}
You can find an example [here](/sbp/master-worker-pattern.html#example-2-designated-workers)
{{%/refer%}}
