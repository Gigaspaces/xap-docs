---
type: post100
title:  Overview
categories: XAP100
parent: notify-container-overview.html
weight: 100
---

{{% ssummary  %}}{{%/ssummary%}}


{{% section %}}
{{% column width="80%" %}}
The notify event container uses the space inherited support for notifications (continuous query) using a XAPs unified event session API. If a notification occurs, the [data event listener](./data-event-listener.html) is invoked with the event. A notify event operation is mainly used when simulating Topic semantics.
{{% /column %}}
{{% column width="20%" %}}
{{%popup   "/attachment_files/notify_container_basic.jpg"%}}
{{% /column %}}
{{% /section %}}


### Life Cycle Events
The notify container life cycle events described below. You may implement each of of these to perform the desired activity.

![polling_container_basic.jpg](/attachment_files/notify_container_life_cycle.jpg)



# Configuration

Here is a simple example of a notify event container configuration:

{{%tabs%}}
{{%tab "  Annotation "%}}


```xml

<!-- Enable scan for OpenSpaces and Spring components -->
<context:component-scan base-package="com.mycompany"/>

<!-- Enable support for @Notify annotation -->
<os-events:annotation-support />
<os-core:embedded-space  id="space" name="mySpace"/>
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

<os-core:embedded-space  id="space" name="mySpace"/>
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

// Start the listener
notifyEventListenerContainer.start();

.......

// when needed dispose of the notification container
notifyEventListenerContainer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

{{% tip %}}
 `@EventDriven` , `@Polling` , `@Notify` can't be placed on interface classes. You should place these on the implementation class.
{{% /tip %}}

The above example registers with the space for write notifications using the provided template (a `Data` object with its processed flag set to `false`). If a notification occurs, the `SimpleListener` is invoked. Registration for notifications is performed on the configured [GigaSpace](./the-gigaspace-interface.html) bean. (In this case, if working in a clustered topology, the notification is performed directly on the cluster member.)

# Primary/Backup

By default, the notify event container registers for notifications only when the relevant space it is working against is in primary mode. When the space is in backup mode, no registration occurs. If the space moves from backup mode to primary mode, the container registers for notifications, and if it moved to backup mode, the registrations are canceled.

{{% info %}}
This mostly applies when working with an embedded space directly with a cluster member. When working with a clustered space (performing operations against the whole cluster), the mode of the space is always primary.
{{%/info%}}

{{% note %}}
Notifications for expired objects (NOTIFY_LEASE_EXPIRATION type) are sent both from the primary and the backup space. To avoid this, you should set the Notify Container `replicateNotifyTemplate` to `false` and run the notify container collocated with the space. This will start the Notify Container only with the primary and will avoid duplicated notifications.
{{%/note%}}

# Template Definition

When performing receive operations, a template is defined, creating a virtualized subset of data in the space, matching it. GigaSpaces supports templates based on the actual domain model (with `null` values denoting wildcards), which are shown in the examples. GigaSpaces allows the use of [SQLQuery](./query-sql.html) in order to query the space, which can be easily used with the event container as the template. Here is an example of how it can be defined:

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

{{% tip %}}
A polling container or notify container could have only one template. If you need multiple event handlers you will need to create another polling container or notify container or use the [Session Based Messaging API](./session-based-messaging-api.html#Registering Large Number of Listeners) that support multiple listeners with one Session. If you use multiple polling containers make sure the different templates does not overlap each other.
{{% /tip %}}


# Multiple Event Handlers

It is possible to define multiple event handlers for a notify container. If you have a superclass that has subclasses, and you want to define event handlers for each subclass, you can define the
event template for the superclass and a @SpaceDataEvent for each subclass.

Here is an example where HostInfo, MachineInfo and LdapInfo are subclasses of the MonitorInfo class:

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

You may use a `SQLQuery` having `IN` operator with multiple values to register a Template with multiple values. This can be a simple alternative avoiding using multiple notify containers. See below example:


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
```

# Free Notify Container Resources

To free the resources used by the notify container make sure you close it properly. A good life cycle event to place the `destroy()` call would be within the `@PreDestroy` or `DisposableBean.destroy()` method.


# Masking Notifications

The notify container allows you to mask which operations performed against the space, should cause notifications. By default (if none is defined), notifications are sent for write operations. The operations are:


|Operation| Description |
|:--------|:------------|
|write| An entry matching the template has been written to the space.|
|update| An entry matching the template has been updated in the space.|
|take| An entry matching the template has been taken from the space.|
|leaseExpire| An entry matching the template lease has been expired.|
|unmatched| An entry matching the template no longer matches the template.|
|all| All the above.|

Here is an example of the notify container configured to trigger notifications for both write and update operations:

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

The `UNMATCHED` `NotifyActionType` should be used when you would like to receive notifications for objects which got their value been updated where the object that was initially matches the template no longer matches the template.
Example:
You would like to maintain within the client side a list for all the objects which got their status as `true`. You register for notifications using a template which had the status field as `true`.

An object with status as `true` is written into the space. This triggers a notification about the existence of this object. In some point the object is read from the space, the status field value changed from `true` to `false` and the object is written back into the space with its updated data.

Registering a notification using a template which got `status=true` with the `NOTIFY_UPDATE` `NotifyActionType` without using the `UNMATCHED NotifyActionType` as well, **will not trigger** a notification when the `status` field value changed from `true` to `false`.

You have to include the `UNMATCHED NotifyActionType` to receive a notification when the object changing `status=true` to `status=false`.

# Scaling Notification Delivery

To increase the number of space threads that are dealing with notification delivery you should increase the `space-config.engine.notify_max_threads` space property. Its default is 128 threads.
Here is an example how you can configure the minimum and the maximum of the thread pool responsible for notification delivery:


```xml
<os-core:embedded-space  id="space" name="mySpace">
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

Note that you can always delegate the notification processing to a separate application defined thread pool, if you'd like to manage the processing thread pool yourself. This can be done by configuring a separate thread pool within your application, for example using [Spring's `TaskExecutor` abstraction](http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/scheduling.html).
In your `pu.xml` file you can define a `ThreadPoolTaskExecutor` as follows:


```xml
<os-core:embedded-space id="space" name="mySpace"/>

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

You AsyncListener class would then look as follows:


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

# Batch Events

OpenSpaces, through the [Session Based Messaging API](./session-based-messaging-api.html), allows batching of notifications. Batching causes the space to accumulate the notifications at the space side for the specified operation(s) with objects that match the template/SQL criteria. Once a certain amount of time has been elapsed or a certain amount of objects have been accumulated, the events are sent to the client.

Batching is very useful when working with a remote space, since it reduces the network roundtrip operations.  The downside of this approach is a potential delay when events are delivered to the client.

Below is an example how batch notification should be configured. With this example the `eventListener` method will be called having one event passed as an argument each time (since the `pass-array-as-is` is not enabled) once there were 10 matching objects accumulated within the space or every 5000 millisecond since the last time a batch was sent to the client. In this case (notification delivery based on elapsed time), there might be less than 10 objects that will be sent to the client.

{{% tip %}}
Make sure you set a reasonable batch size to avoid overloading the listener with a large burst of events to process. A value under 100 will be acceptable for most cases.
{{% /tip %}}

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

## Pass Array as is

When batching is enabled an array of Entries is received from the notification. By default, the notify container serializes the execution of the array into invocation of the event listener for each element in the array. If you want the event to operate on the whole array (receive the array as a parameter), the `pass-array-as-is` configuration attribute should be set to `true`.

{{% note %}}
Listener argument has to be defined as Object[] when using passArrayAsIs and converted to appropriate class as part of the listener code.
{{% /note %}}

For the above example when using passArrayAsIs equivalent listener code will be,


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

By default the notify event container running on non-FIFO mode. This means the event listener will be called simultaneously by multiple threads in case there are concurrent notifications sent from the space. To have a sequential event listener calls, you should run the notify  event container in a FIFO mode.

{{% info%}}
For full FIFO support, the actual template also has to be marked as FIFO. For more details, refer to the [Space FIFO support](./fifo-support.html) section.
{{%/info%}}

Here is an example of how FIFO events can be configured with the notify container:

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

# Re-Register after complete space shutdown and restart

The Notify Container can recover from partial disconnections (e.g failover) automatically, but if the entire cluster is shut down and  restarted, the event registrations are lost and the notify container needs to be restarted. 

To get a notification about such disconnections, the *Auto Renew* feature needs to be enabled, and the container needs to implement a `LeaseListener` and register if to be triggered when the container becomes disconnected. For example:

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
            space = new GigaSpaceConfigurer(new UrlSpaceConfigurer("jini://*/*/space")).create();
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

{{% tip %}}
To make sure the client application will manage to reconnect automatically to the space cluster once it was restarted you may want to change the default configuration. See the [Proxy Connectivity]({{%currentadmurl%}}/tuning-proxy-connectivity.html) for details.
{{% /tip %}}

For more information see [Auto Renew](./session-based-messaging-api.html#disconnections).

# Resending Notifications after a Space-Client Disconnection

When a network failure occurs and the space can't communicate with the client, the space attempts to trigger the remote client listener several times. The `space-config.notifier-retries` property controls the re-try attempts. The default is 3 attempts.


```java
<os-core:embedded-space id="space" name="mySpace">
    <os-core:properties>
        <props>
            <prop key="space-config.notifier-retries">10</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

{{% include "/COM/notify-recovery.markdown" %}}


# Triggering Notifications on Backup Instances

By default notifications are replicated to backup instances but are not triggered. To enable notifications triggered also on backup instances the `cluster-config.groups.group.repl-policy.trigger-notify-templates` should be enabled. See below:


```xml
<os-core:space id="space" url="/./space" >	
	<os-core:properties>
		<props>
			<prop key="cluster-config.groups.group.repl-policy.trigger-notify-templates">true</prop>
		</props>
	</os-core:properties>
</os-core:space>
```

{{% note %}}
When this option is enabled - When running collocated notify container the listener implementation should not access its collocated instance as this is blocked with backup instances.  
{{%/note%}}

# Durable Notifications

Due-to the asynchronous nature of notification delivery, when a primary space fails right after replicating an operation to the backup space and before sending the notification to the registered client, the backup space might not be able to send the missing notifications, since it is in the process of moving to active mode.
This means that during this very short period of time, the registered client might not receive events that occurred in the primary space and were replicated to the backup space.

Durable notifications allows configuring the notify container to withstand failover and short network disconnections with no notifications lost.

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

Durable notifications are based on the replication mechanism and as such have some different semantics regarding other notify container configuration parameters.
For further details see [Durable Notifications](./durable-notifications.html).

{{%warning%}}
Durable Notification does not support an embedded Space, it can only be used with a remote proxy.  The alternative is to use a `Polling Container` using [SingleReadReceiveOperationHandler](./polling-container.html#trigger-receive-operation).
{{%/warning%}}


# Take on Notify

The notify event container can be configured to automatically perform a take on the notification data event. It can also be further configured to filter out events if the take operation returned `null`. (This usually happens when several clients receive this event, and only one succeeds with the take.)

{{%note%}}
Pojo properties should implement `hashcode` and `equals` methods for matching to succeed.(avoid using take on notify when one of the object fields is a  primitive array)
{{%/note%}}

Here is how the notify container can be configured:

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

# Event meta data

When registering for notifications, using the unified event session API (or using plain notify registration), the following interface needs to be implemented:


```java
public interface RemoteEventListener extends java.rmi.Remote, java.util.EventListener {

    void notify(net.jini.core.event.RemoteEvent event)
                throws net.jini.core.event.UnknownEventException, java.rmi.RemoteException;
}
```

GigaSpaces extends this interface by providing the `EntryArrivedRemoteEvent`, which holds additional information regarding the event that occurred. The notify container, by default, uses the `EntryArrivedRemoteEvent` in order to extract the actual data event represented by the event, and passes it as the first parameter. If access to the `EntryArrivedRemoteEvent` is still needed, it is passed as the last parameter to the space data event listener. Here is an example of how it can be used:


```java
public class SimpleListener implements SpaceDataEventListener {

    public void onEvent(Object data, GigaSpace gigaSpace, TransactionStatus txStatus, Object source) {
        EntryArrivedRemoteEvent entryArrivedRemoteEvent = (EntryArrivedRemoteEvent) source;
        // ...
    }
}
```

When using the different listener adapters, such as the annotation adapter, it can be accessed in the following manner (since adapters use reflection, there is no need to cast to `EntryArrivedRemoteEvent`):


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

# Default Values of Notify Container Configuration Parameters

The default values for all of the notify container configuration parameters, such as `perform-take-on-notify, ignore-event-on-null-take` and others can be found in the JavaDoc (and sources) of the class [SimpleNotifyEventListenerContainer]({{% api-javadoc %}}/org/openspaces/events/notify/SimpleNotifyEventListenerContainer.html) and its super class, namely [AbstractNotifyEventListenerContainer]({{% api-javadoc %}}/org/openspaces/events/notify/AbstractNotifyEventListenerContainer.html).
For example, `perform-take-on-notify` default value is documented in the method `SimpleNotifyEventListenerContainer.setPerformTakeOnNotify(boolean)`.


{{% include "/COM/notify-verses-polling.markdown" %}}
