---
type: post100
title:  Concurrent Consumers
categories: XAP100
parent: polling-container-overview.html
weight: 200
---

{{%wbr%}}



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

