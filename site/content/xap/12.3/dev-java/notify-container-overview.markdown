---
type: post123
title:  Notify Container
categories: XAP123, OSS
parent: event-processing.html
weight: 200
---


{{% imagertext   "/attachment_files/notify_container_basic.jpg"%}}
The notify event container adopts the Space-inherited support for notifications (continuous query), using a XAP unified event session API. If a notification occurs, the [data event listener](./data-event-listener.html) is invoked with the event. A notify event operation is mainly used when simulating topic semantics.
{{% /imagertext %}}


# Life Cycle Events
The notify container life cycle events are described below. You can implement each one to perform the required activity.

{{%align center%}}
![polling_container_basic.jpg](/attachment_files/notify_container_life_cycle.jpg)
{{%/align%}}


# Configuration

Here is a simple example of how to configure the notify event container:

{{%tabs%}}
{{%tab "  Annotation "%}}
```xml
<!-- Enable scan for OpenSpaces and Spring components -->
<context:component-scan base-package="com.mycompany"/>

<!-- Enable support for @Notify annotation -->
<os-events:annotation-support />
<os-core:embedded-space  id="space" space-name="mySpace"/>
<os-core:giga-space id="gigaSpace" space="space"/>
```

```java
@EventDriven @Notify
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
<os-core:embedded-space  id="space" space-name="mySpace"/>
<os-core:giga-space id="gigaSpace" space="space"/>
<bean id="simpleListener" class="SimpleListener" />
<os-events:notify-container id="eventContainer" giga-space="gigaSpace">

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
</os-events:notify-container>
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
<bean id="eventContainer" class="org.openspaces.events.notify.SimpleNotifyEventListenerContainer">
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

SimpleNotifyEventListenerContainer notifyEventListenerContainer = new SimpleNotifyContainerConfigurer(gigaSpace)
                .template(new Data(false))
                .eventListenerAnnotation(new Object() {
                    @SpaceDataEvent
                    public void eventHappened() {
                        eventCalled.set(true);
                    }
                }).notifyContainer();

// start the listener
notifyEventListenerContainer.start();

.......

// when needed dispose of the notification container
notifyEventListenerContainer.destroy();
```
{{% /tab %}}
{{% /tabs %}}

{{% note "Restrictions"%}}
`@EventDriven`, `@Polling`, and `@Notify` cannot be placed on interface classes. Place them on the implementation class instead.
{{% /note %}}

The above example registers with the Space for write notifications using the provided template (a `Data` object with its processed flag set to **false**). If a notification occurs, the `SimpleListener` is invoked. Registration for notifications is performed on the configured [GigaSpace](./the-gigaspace-interface-overview.html) bean. (In this case, if working in a clustered topology, the notification is performed directly on the cluster member.)


# General Guidelines

A notify listener configured in [non-FIFO](#fifo-events) mode uses a thread that is maintained via a [thread pool](#scaling-notification-delivery). If the notify container [template/query](#template-definition) matches a large number of Space objects within a short period of time, you may consume the entire thread pool. 

If the notify listener implementation performs a write/update/change/removal of an object from the same Space, this may trigger additional notifications (create a cascading affect), which may consume the thread pool faster.
 
This behavior should be avoided, as it may throttle the activity or impose large concurrent replication activity (if a backup/replica is running). This in turn can consume a large amount of CPU and network resources within a short period of time. This can be observed with garbage created very quickly, which may cause long pauses due to garbage collection activity.
 
To prevent this, the notify listener implementation should have [batching enabled](#batch-events), where minimal write/update/change/removal operations should be conducted (if any). If the listener performs a large number of Space operations, a [polling container](./polling-container-overview.html#notify-verses-polling-container) should be considered, as this is a more controlled event handler.


# Template Definition

When performing receive operations a template is defined, creating a virtualized subset of data in the Space that matches it. XAP supports templates based on the actual domain model (with `null` values denoting wildcards), which are shown in the examples. XAP allows the use of [SQLQuery](./query-sql.html) in order to query the Space, which can be easily used with the event container as the template. The following is an example of how it can be defined:

{{%tabs%}}
{{%tab "  Annotation "%}}
```java
@EventDriven @Notify
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
<os-events:notify-container id="eventContainer" giga-space="gigaSpace">

    <os-core:sql-query where="processed = true" class="org.openspaces.example.data.common.Data"/>

    <os-events:listener>
        <os-events:annotation-adapter>
            <os-events:delegate ref="simpleListener"/>
        </os-events:annotation-adapter>
    </os-events:listener>

</os-events:notify-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}

```xml
<bean id="eventContainer" class="org.openspaces.events.notify.SimpleNotifyEventListenerContainer">

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

{{% note "Note"%}}
A polling or notify container can have only one template. If you need multiple event handlers, you must either create another container or use the [Session Based Messaging API](./session-based-messaging-api.html#Registering Large Number of Listeners), which supports multiple listeners with a single session. If you use multiple polling containers, make sure the different templates don't overlap each other.
{{% /note %}}

# Multiple Event Handlers

YOu can define multiple event handlers for a notify container. If you have a superclass with subclasses, and you want to define event handlers for each subclass, define the
event template for the superclass and a `@SpaceDataEvent` for each subclass.

The following is an example where `HostInfo`, `MachineInfo`, and `LdapInfo` are subclasses of the `MonitorInfo` class:

```java
@EventDriven
@Notify
public class NotifyExample {

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

You can use a `SQLQuery` that has an `IN` operator with multiple values to register a template with multiple values. This is a simple alternative to using multiple notify containers. See the following example:


{{%tabs%}}
{{%tab "Data class"%}}
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

{{%tab "The Template registration"%}}
```java
SQLQuery<MyData> query = new SQLQuery<MyData> (
		MyData.class , "key IN('A' , 'B' , 'C')");

SimpleNotifyEventListenerContainer notifyEventListenerContainer =
	new SimpleNotifyContainerConfigurer(space).template(query)
	.eventListenerAnnotation(new Object() {
    @SpaceDataEvent
    public void eventHappened(MyData data) {
        System.out.println("Got matching event! - " + data);
    }
}).notifyContainer();

//With Java8 lambda syntax
SimpleNotifyEventListenerContainer notifyEventListenerContainer =
	new SimpleNotifyContainerConfigurer(space).template(query)
    .eventListener((data, gigaSpace, txStatus, source) -> {
        System.out.println("Got matching event! - " + (MyData)data);
    })
    .notifyContainer();
```
{{%/tab%}}
{{%/tabs%}}

# Freeing Up Notify Container Resources

To free up the resources used by the notify container, make sure you close it properly. Some good life cycle events within which to place the `destroy()` call are the `@PreDestroy` or `DisposableBean.destroy()` methods.

# Notify Container Life Cycle

The Notify Event Listener container supports several life cycle methods. These methods allow you to create, start, stop, and destroy the listener programmatically.  There is also the `setActiveWhenPrimary` life cycle mode that binds the container to the Space mode when set to **TRUE**, starting it when the Space mode is in primary mode and stopping it otherwise.
 
You can get the exact status of the Notify Event Listener container using the `isActive()`  and `isRunning()` methods. The following is a simple example that illustrates the different Notify Event Listener container life cycle methods:

{{%tabs%}}
{{%tab NotifyContainerLifeCycle%}}
```java
import java.util.Calendar;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.EmbeddedSpaceConfigurer;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.SimpleNotifyContainerConfigurer;
import org.openspaces.events.notify.SimpleNotifyEventListenerContainer;

public class NotifyContainerLifeCycleMain {
	static SimpleNotifyEventListenerContainer notifyEventListenerContainer;
	static GigaSpace gigaSpace;

	public static void main(String[] args) throws Exception {

		gigaSpace = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer("mySpace")).gigaSpace();

		say("notifyContainer about to be created");
		// create a polling listener
		notifyEventListenerContainer = new SimpleNotifyContainerConfigurer(gigaSpace).template(new Data())
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
				}).notifyContainer();

		say("notifyContainer created");
		Thread.sleep(1000);
		say("notifyContainer about to be started");
		notifyEventListenerContainer.start();
		say("notifyContainer started");
		Thread.sleep(1000);

        // Write data to the space
		gigaSpace.write(new Data());
		say("wrote object to space");
		
		say("notifyContainer about to be stopped");
		notifyEventListenerContainer.stop();
		say("notifyContainer stoped");
		Thread.sleep(1000);

		say("notifyContainer about to be restarted");
		notifyEventListenerContainer.start();
		say("notifyContainer started");
		Thread.sleep(1000);

		say("notifyContainer about to be destroyed");
		notifyEventListenerContainer.destroy();
		say("notifyContainer destroyed");
		System.exit(0);
	}

	static public void say(String mes) {
		Calendar d = Calendar.getInstance();

		int ms = Calendar.getInstance().get(Calendar.MILLISECOND);
		String t = d.getTime() + ":" + ms;

		if (notifyEventListenerContainer == null)
			System.out.println(t + " - " + " isActive:" + "false" + " isRunning:" + "false" + " " + mes);
		else
			System.out.println(t + " - " + " isActive:" + notifyEventListenerContainer.isActive() + " isRunning:"
					+ notifyEventListenerContainer.isRunning() + " " + mes);
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
Mon Aug 01 08:54:35 CAT 2016:919 -  isActive:false isRunning:false notifyContainer about to be created
Mon Aug 01 08:54:35 CAT 2016:947 -  isActive:true isRunning:false notifyContainer created
Mon Aug 01 08:54:36 CAT 2016:948 -  isActive:true isRunning:false notifyContainer about to be started
Mon Aug 01 08:54:36 CAT 2016:988 -  isActive:true isRunning:true notifyContainer started
Mon Aug 01 08:54:37 CAT 2016:4 -  isActive:true isRunning:true wrote object to space
Mon Aug 01 08:54:37 CAT 2016:6 -  isActive:true isRunning:true event consumed
Mon Aug 01 08:54:38 CAT 2016:4 -  isActive:true isRunning:true notifyContainer about to be stopped
Mon Aug 01 08:54:38 CAT 2016:8 -  isActive:true isRunning:false notifyContainer stopped
Mon Aug 01 08:54:39 CAT 2016:9 -  isActive:true isRunning:false notifyContainer about to be restarted
Mon Aug 01 08:54:39 CAT 2016:10 -  isActive:true isRunning:true notifyContainer started
Mon Aug 01 08:54:40 CAT 2016:10 -  isActive:true isRunning:true notifyContainer about to be destroyed
Mon Aug 01 08:54:40 CAT 2016:10 -  isActive:false isRunning:false notifyContainer destroyed
```



# Masking Notifications

The notify container allows you to mask the operations (which are performed against the Space) that should cause notifications. By default, if none are defined then notifications are sent for write operations. The possible operations are:


|Operation| Description |
|:--------|:------------|
|write| An entry matching the template has been written to the Space.|
|update| An entry matching the template has been updated in the Space.|
|take| An entry matching the template has been taken from the Space.|
|leaseExpire| An entry matching the template lease has been expired.|
|unmatched| An entry matching the template no longer matches the template.|
|all| All the above.|

{{% note "Note"%}}
The leaseExpire notification does not support FIFO.
{{% /note %}}

The following is an example of the notify container configured to trigger notifications for both write and update operations:

{{%tabs%}}
{{%tab "  Annotation "%}}


```java
@EventDriven @Notify
@NotifyType(write = true, update = true)
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
<os-events:notify-container id="eventContainer" giga-space="gigaSpace">

    <os-events:notify write="true" update="true"/>

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
</os-events:notify-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="eventContainer" class="org.openspaces.events.notify.SimpleNotifyEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="notifyWrite" value="true" />
    <property name="notifyUpdate" value="true" />

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

## The UNMATCHED NotifyActionType

The `UNMATCHED` `NotifyActionType` should be used when you want to receive notifications for objects whose value has been updated, where the object that initially matched the template no longer matches the template.

**Example**

Scenario: You want to maintain, on the client side, a list of all the objects whose status is `true`. You register for notifications using a template where the status field has a value of `true`.

An object with a status of `true` is written to the Space. This triggers a notification that the object exists. At some point, the object is read from the Space and the status field value changes from `true` to `false`, then the object is written back into the Space with its updated data.

Registering a notification using a template that has `status=true` that has `NOTIFY_UPDATE` and `NotifyActionType` but **does not have** `UNMATCHED NotifyActionType` will *not* trigger a notification when the `status` field value changes from `true` to `false`.

You must include `UNMATCHED NotifyActionType` to receive a notification when the status of an object changes from `true` to `false`.

# Scaling Notification Delivery

To increase the number of Space threads that handle notification delivery, increase the value of the `space-config.engine.notify_max_threads` Space property. The default is 128 threads.

The following is an example of how to configure the minimum and the maximum threads in the thread pool that is responsible for delivering notifications:


```xml
<os-core:embedded-space  id="space" space-name="mySpace">
    <os-core:properties>
        <props>
            <prop key="space-config.engine.notify_min_threads">128</prop>
            <prop key="space-config.engine.notify_max_threads">512</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>

<os-core:giga-space id="gigaSpace" space="space"/>

<bean id="simpleListener" class="SimpleListener" />

<os-events:notify-container id="eventContainer" giga-space="gigaSpace">

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
</os-events:notify-container>
```

You can always delegate the notification processing to a separate application-defined thread pool, if you want to manage the processing thread pool yourself. This can be done by configuring a separate thread pool within your application, for example using {{%exurl "Spring's TaskExecutor abstraction" "http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/scheduling.html"%}}.
In your `pu.xml` file you can define a `ThreadPoolTaskExecutor` and an async listener:

{{%tabs%}}
{{%tab "pu.xml"%}}
```xml
<os-core:embedded-space id="space" space-name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<bean id="asyncListener" class="AsyncListener">
    <property name="taskExecutor" ref="taskExecutor"/>
</bean>

<os-events:notify-container id="eventContainer" giga-space="gigaSpace">

    <os-core:template>
        <bean class="org.openspaces.example.data.common.Data">
            <property name="processed" value="false"/>
        </bean>
    </os-core:template>

    <os-events:listener>
        <os-events:annotation-adapter>
            <os-events:delegate ref="asyncListener"/>
        </os-events:annotation-adapter>
    </os-events:listener>
</os-events:notify-container>

<bean id="taskExecutor" class="org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor">
  <property name="corePoolSize" value="5" />
  <property name="maxPoolSize" value="10" />
  <property name="queueCapacity" value="25" />
</bean>
```
{{%/tab%}}

{{%tab "AsyncListener"%}}
```java
public class AsyncListener {
    private TaskExecutor taskExecutor;

    public void setExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @SpaceDataEvent
    public Data eventListener(Data event) {
        taskExecutor.execute(new Runnable() {
           public void run() {
               //process event
           }
        }
    }
}
```
{{%/tab%}}
{{%/tabs%}}

# Batch Events

OpenSpaces, through the [Session Based Messaging API](./session-based-messaging-api.html), allows batch notifications. Batching causes the Space to accumulate the notifications on the Space side for the specified operation(s) with objects that match the template/SQL criteria. When a certain amount of time has elapsed or a certain amount of objects have accumulated, the events are sent to the client.

Batching is very useful when working with a remote Space, because it reduces the network roundtrip operations.  The downside of this approach is a potential delay when events are delivered to the client.

The following is an example of how to configure batch notification. In this example, the `eventListener` method is called having one event passed as an argument each time (because the `pass-array-as-is` is not enabled) that 10 matching objects have accumulated within the Space, or every 5000 milliseconds after the last batch was sent to the client. In this case (notification delivery based on elapsed time), there may be fewer than 10 objects sent to the client.

{{% note "Note"%}}
Make sure you set a reasonable batch size, to avoid overloading the listener with large bursts of events to process. A value under 100 is acceptable for most cases.
{{% /note %}}

{{%tabs%}}
{{%tab "  Annotation "%}}


```java
@EventDriven @Notify
@NotifyBatch(size = 10, time = 5000)
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
<os-events:notify-container id="eventContainer" giga-space="gigaSpace">

    <os-events:batch size="10" time="5000"/>

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
</os-events:notify-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="eventContainer" class="org.openspaces.events.notify.SimpleNotifyEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="batchSize" value="10" />
    <property name="batchTime" value="5000" />

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

## Passing an Array As Is

When batching is enabled, an array of Entries is received from the notification. By default, the notify container serializes the execution of the array into an invocation of the event listener for each element in the array. If you want the event to operate on the entire array (receive the array as a parameter), the `pass-array-as-is` configuration attribute should be set to `true`.

{{% note "Note"%}}
The Listener argument must be defined as `Object[]` when using `passArrayAsIs`, and converted to the appropriate class as part of the listener code.
{{% /note %}}

For the above example, when using `passArrayAsIs` the equivalent listener code is:


```java
@EventDriven @Notify
@NotifyBatch(size = 10, time = 5000, passArrayAsIs = true)
public class SimpleListener {

    @EventTemplate
    Data unprocessedData() {
        Data template = new Data();
        template.setProcessed(false);
        return template;
    }

    @SpaceDataEvent
    public Data eventListener(Object[] events) {
        //Convert events to Data[] and process here

    }
}
```

# FIFO Events

By default, the notify event container runs in non-FIFO mode. This means the event listener is called simultaneously by multiple threads if there are concurrent notifications sent from the Space. To have sequential event listener calls, run the notify event container in FIFO mode.

{{% note "Note"%}}
FIFO is not supported by the leaseExpire notification.
{{% /note %}}

The following is an example of how to configure FIFO events with the notify container:

{{%tabs%}}
{{%tab "  Annotation "%}}


```java
@EventDriven @Notify(fifo = true)
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
<os-events:notify-container id="eventContainer" giga-space="gigaSpace" fifo="true">

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
</os-events:notify-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="eventContainer" class="org.openspaces.events.notify.SimpleNotifyEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="fifo" value="true" />

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

{{% refer%}}
For full FIFO support, the actual template also has to be marked as FIFO. For more details, refer to the [Space FIFO support](./fifo-support.html) section.
{{%/refer%}}


# Re-Registering after Complete Space Shutdown and Restart

The Notify Container can recover from partial disconnections such as failover automatically, but if the entire cluster is shut down and restarted, the event registrations are lost and the notify container must be restarted. 

To get a notification about these disconnections, the *Auto Renew* feature must be enabled, and the container must implement a `LeaseListener` and register so it will be triggered when the container becomes disconnected. For example:

{{%tabs%}}

{{%tab "  Notify Container Creation "%}}


```java
public class NotifyHAMain {

    static GigaSpace space;

    public static void main(String[] args) throws Exception {

        while (space == null) {
            getSpace();
        }

        MyLeaseListener leaseListener = new MyLeaseListener(space);
        register(leaseListener);
        System.out.println("Notification Registration Done!");
        while (true) {
            Thread.sleep(10000);
        }
    }

    static void getSpace() {
        try {
            space = new GigaSpaceConfigurer(new SpaceProxyConfigurer("space")).create();
        } catch (Exception e) {
            System.out.println("Cannot find space: " + e.getMessage());
        }
    }

    static SimpleNotifyEventListenerContainer register(MyLeaseListener leaseListener) {
        return new SimpleNotifyContainerConfigurer(space)
            .template(new MyData())
            .leaseListener(leaseListener)
            .autoRenew(true)
            .eventListenerAnnotation(new Object() {
                @SpaceDataEvent
                public void eventHappened(MyData data) {
                    System.out.println("Got notification: " + data.getData());
                }
            })
            .notifyContainer();
    }
}
```

{{% /tab %}}

{{%tab "  The LeaseListener Implementation "%}}
```java
public class MyLeaseListener implements LeaseListener {

	GigaSpace space ;

	public MyLeaseListener (GigaSpace space) {
		this.space = space;
	}

	// Called when the event registration's lease cannot be renewed
	public void notify(LeaseRenewalEvent event) {
		System.out.println("Can't renew - try to re-register");
		SimpleNotifyEventListenerContainer container = null;
		while (container == null) {
            NotifyHAMain.getSpace();
			container = NotifyHAMain.register(this);
		}
		System.out.println("Notify ReRegistration Done!");
	}
}
```

{{% /tab %}}

{{% /tabs %}}

{{% note "Note"%}}
To ensure that the client application reconnects automatically to the Space cluster after it is restarted, you may have to change the default configuration. Refer to [Proxy Connectivity](../admin/tuning-proxy-connectivity.html) for details.
{{% /note %}}

For more information, see [Auto Renew](./session-based-messaging-api.html#disconnections).

# Resending Notifications after a Space-Client Disconnection

When a network failure occurs and the Space can't communicate with the client, the Space attempts to trigger the remote client listener several times. The `space-config.notifier-retries` property controls the number of re-try attempts. The default value is 3 attempts.


```java
<os-core:embedded-space id="space" space-name="mySpace">
    <os-core:properties>
        <props>
            <prop key="space-config.notifier-retries">10</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

# Durable Notifications

Due to the asynchronous nature of notification delivery, when a primary Space fails right after replicating an operation to the backup Space and before sending the notification to the registered client, the backup Space may not be able to send the missing notifications because it is in the process of moving to primary mode. As a result, during this very short period of time the registered client might not receive events that occurred in the primary Space and were replicated to the backup Space.

Durable notifications allows configuring the notify container to withstand failover and short network disconnections without losing notifications.

{{%tabs%}}
{{%tab "  Annotation "%}}


```java
@EventDriven @Notify(durable = true)
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
<os-events:notify-container id="eventContainer" giga-space="gigaSpace" durable="true">

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
</os-events:notify-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="eventContainer" class="org.openspaces.events.notify.SimpleNotifyEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="durable" value="true" />

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


{{%warning "Important"%}}
Durable Notification does not support embedded Spaces, and can only be used with a remote proxy.  As an alternative, use a `Polling Container` with [SingleReadReceiveOperationHandler](./polling-container-overview.html#trigger-receive-operation).
{{%/warning%}}

Durable notifications are based on the replication mechanism, and therefore have some different semantics regarding other notify container configuration parameters.

{{%refer%}}
For more information, refer to [Durable Notifications](./durable-notifications.html).
{{%/refer%}}


# Take on Notify

The notify event container can be configured to automatically perform a take on the notification data event. The container can also be configured to filter out events if the take operation returns `null`. (This usually happens when several clients receive this event, and only one succeeds with the take.)

{{%note "Note"%}}
POJO properties should implement `hashcode` and `equals` methods for matching to succeed. Avoid using take on notify when one of the object fields is a primitive array.
{{%/note%}}

The following example shows on way to configure the notify container:

{{%tabs%}}
{{%tab "  Annotation "%}}


```java
@EventDriven @Notify(performTakeOnNotify = true, ignoreEventOnNullTake = true)
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
<os-events:notify-container id="eventContainer" giga-space="gigaSpace"
                            perform-take-on-notify="true" ignore-event-on-null-take="true">

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
</os-events:notify-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="eventContainer" class="org.openspaces.events.notify.SimpleNotifyEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="performTakeOnNotify" value="true" />
    <property name="ignoreEventOnNullTake" value="true" />

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

# Event Metadata

When registering for notifications using the unified event session API (or using plain notify registration), the following interface must be implemented:


```java
public interface RemoteEventListener extends java.rmi.Remote, java.util.EventListener {

    void notify(net.jini.core.event.RemoteEvent event)
                throws net.jini.core.event.UnknownEventException, java.rmi.RemoteException;
}
```

XAP extends this interface by providing the `EntryArrivedRemoteEvent`, which holds additional information regarding the event that occurred. The notify container, by default, uses the `EntryArrivedRemoteEvent` to extract the actual data event represented by the event, and passes it as the first parameter. If access to the `EntryArrivedRemoteEvent` is still needed, it is passed as the last parameter to the Space data event listener. The following is an example of how it can be used:


```java
public class SimpleListener implements SpaceDataEventListener {

    public void onEvent(Object data, GigaSpace gigaSpace, TransactionStatus txStatus, Object source) {
        EntryArrivedRemoteEvent entryArrivedRemoteEvent = (EntryArrivedRemoteEvent) source;
        // ...
    }
}
```

When using different listener adapters, such as the annotation adapter, it can be accessed in the following manner (adapters use reflection, so there is no need to cast to `EntryArrivedRemoteEvent`):


```java
@EventDriven @Notify
public class SimpleListener {
    ...
    @SpaceDataEvent
    public void myEventHandler(Trade data, GigaSpace gigaSpace, TransactionStatus txStatus,
                               EntryArrivedRemoteEvent entryArrivedRemoteEvent) {
        // process event
    }
}
```

Or the following when using batch notification:


```java
@EventDriven @Notify
@NotifyBatch(size = 10, time = 5000, passArrayAsIs = true)
public class BatchListener {
    ...
    @SpaceDataEvent
    public void myEventHandler(Trade[] data, GigaSpace gigaSpace, TransactionStatus txStatus, BatchRemoteEvent batchRemoteEvent) {
        // process event
    }
}
```

#  Notify Container Configuration Parameters- Default Values

The default values for all of the notify container configuration parameters, such as `perform-take-on-notify`, `ignore-event-on-null-take` and others, can be found in the API reference guide (and sources) of the class [SimpleNotifyEventListenerContainer]({{% api-javadoc %}}/org/openspaces/events/notify/SimpleNotifyEventListenerContainer.html) and its super class, namely [AbstractNotifyEventListenerContainer]({{% api-javadoc %}}/org/openspaces/events/notify/AbstractNotifyEventListenerContainer.html).

For example, the `perform-take-on-notify` default value is documented in the method `SimpleNotifyEventListenerContainer.setPerformTakeOnNotify(boolean)`.


 

# Additional Resources

{{%youtube "GwLfDYgl6f8"  "Event Processing"%}}
