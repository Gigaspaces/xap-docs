---
type: post102
title:  Overview
categories: XAP102
parent: polling-container-overview.html
weight: 100
---

{{% ssummary  %}}{{%/ssummary%}}


{{% section %}}
{{% column width="80%" %}}
The polling event container is an implementation of the [polling consumer pattern](http://enterpriseintegrationpatterns.com/PollingConsumer.html){:target="_blank"} which uses the space to receive events. It performs polling receive operations against the space. If a receive operation succeeds (a value is returned from the receive operation), the [Data Event Listener](./data-event-listener.html) is invoked with the event. A polling event operation is mainly used when simulating Queue semantics or when using the master-worker design pattern.
{{% /column %}}
{{% column width="20%" %}}
{{%popup   "/attachment_files/polling_container_basic.jpg"%}}
{{% /column %}}
{{% /section %}}



## Life Cycle Events

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

<os-core:embedded-space id="space" name="mySpace"/>

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

<os-core:embedded-space id="space" name="mySpace"/>
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

// start the notification
pollingEventListenerContainer.start();

......

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

XAP comes with several built-in receive operation-handler implementations:


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

<os-core:embedded-space id="space" name="mySpace"/>

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

When working with a partitioned cluster and configuring the remote polling container to work against the [whole cluster](./the-gigaspace-interface.html#Clustered Flag), blocking operations (take with a timeout>0) are not allowed (when the routing field is not set on the template or SQLQuery). The default receive operation handlers support performing the receive operation in a non-blocking manner, by sleeping between non-blocking operations. For example, the `SingleTakeReceiveOperationHandler` performs a non-blocking take operation against the space and then sleeps for a configurable amount of time. A classic scenario where the Non-Blocking mode would be used is the [Master-Worker Pattern](/sbp/master-worker-pattern.html).

{{% section %}}
{{% column width="50%" %}}
Step 1 - Master sending requests to process by the workers implemented using the polling container:

{{% align center %}}
![master_worker_rr1.jpg](/attachment_files/master_worker_rr1.jpg)
{{% /align %}}

{{% /column %}}
{{% column width="50%" %}}
Step 2  - Workers generating results which are consumed by the Master:

{{% align center %}}
![master_worker_rr2.jpg](/attachment_files/master_worker_rr2.jpg)
{{% /align %}}

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

<os-core:embedded-space id="space" name="mySpace"/>

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

Processing data in batches may improve the processing throughput performance. Instead of consuming object one at a time from the space and processing it, you may consume a batch with multiple objects and process these in one transaction. This may improve the overall throughput rate, but may impact the latency of the individual object processing time.

See below a simple benchmark results comparing the different options:

{{% align center%}}
![poll_bench.jpg](/attachment_files/poll_bench.jpg)
{{% /align%}}


See full benchmark description on the [How to Implement my Processor? - The Polling Container Benchmark](http://blog.gigaspaces.com/2008/10/03/how-to-implement-my-processor-the-polling-container-benchmark) GigaSpaces blog site.

You may use batching via the `MultiTakeReceiveOperationHandler`. The `MultiTakeReceiveOperationHandler.setMaxEntries(integer)` allows you to set the maximum amount of objects to be consumed with each polling event. If the space does not have sufficient number of matching objects during the polling point in time, the event listener method will be called with the existing number of matching objects (will be smaller than the `MaxEntries` value. There will be no delay in such a case and the polling container will not wait until there will be exact amount of matching objects to consume as specified via the `MaxEntries`.

Certain receive operation handlers might return an array as a result of the receive operation. A prime example is the `MultiTakeReceiveOperationHandler`, which might return an array as a result of a `takeMultiple` operation called by the polling container. By default, the polling container serializes the execution of the array into invocation of the event listener method for each element in the array. If you want the event to operate on the whole array (receive the array as a parameter into the event listener method), the `passArrayAsIs` annotation should be set to **true**.

Here is an example for batch processing using the `passArrayAsIs` - with this example the polling container will consume a batch of objects using `takeMultiple`, modify these and write these back into the space in one operation using `writeMultiple`:


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

# Free Polling Container Resources

To free the resources used by the polling container make sure you close it properly. A good life cycle event to place the `destroy()` call would be within the `@PreDestroy` or `DisposableBean.destroy()` method.


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

<os-core:embedded-space id="space" name="mySpace"/>

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

The default values for all of the polling container configuration parameters such as `concurrent-consumers, active-when-primary, receive-timeout` and others can be found in the JavaDoc (and sources) of the class [SimplePollingEventListenerContainer](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/events/polling/SimplePollingEventListenerContainer.html) and its super class, namely [AbstractPollingEventListenerContainer](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/events/polling/AbstractPollingEventListenerContainer.html).

For example, `concurrent-consumers` default value is documented in the method `SimplePollingEventListenerContainer.setConcurrentConsumers(int)`



{{% include "/COM/notify-verses-polling.markdown" %}}
