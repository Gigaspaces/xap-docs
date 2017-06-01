---
type: post102
title:  Queue Provider
categories: XAP102
parent: mule-esb.html
weight: 300
---


{{% ssummary   %}}  {{% /ssummary %}}



XAP's queue provider is equivalent to the Mule [VM transport](http://www.mulesoft.org/documentation/display/MULE3USER/VM+Transport) and is used for internal communication between services managed by Mule based on the Space. By storing internal Mule messages in the Space using a virtualized queue, inter-service communication becomes highly available and fault-tolerant (in a primary-backup topology).

When working with Mule and the OpenSpaces Mule integration, each Mule instance can participate in a Space cluster by starting an embedded Space which is a cluster member within the cluster. In this scenario, the queues are actually stored only in the relevant cluster member. In a primary-backup topology, this means that the queue content (the relevant messages that are passed between services) are backed up to the relevant cluster backup member.

In this case, when the primary instance fails, the backup takes over and become primary, allowing services to continue and process messages without losing any data.

{{% note %}}
In such a scenario, the backup services probably shouldn't run at all and should start processing only in case of failover. In order to configure the SEDA services to be aware of the current space state of the cluster member they are working against, see the [Mule SEDA Model](./mule-seda-model.html) section.
{{%/note%}}

In order to use the OpenSpaces queue provider, the following namespaces should be defined:


```xml
<mule xmlns="
...
xmlns:os-queue=http://www.openspaces.org/schema/mule/os-queue

xsi:schemaLocation="...
http://www.openspaces.org/schema/mule/os-queue http://www.openspaces.org/schema/{{%currentversion%}}/mule/3.1/mule-os-queue.xsd">
```

# Connector

The OpenSpaces queue connector is used to define the Space this queue transport works against. It uses OpenSpaces `GigaSpace` bean in order to interact with the Space. Here is an example of how it can be defined:


```xml
<spring:beans>
    <os-core:embedded-space id="space" name="mySpace"/>

    <os-core:giga-space id="gigaSpace" space="space" />
</spring:beans>
<os-queue:connector name="queueConnector" giga-space="gigaSpace"  fifo="false"  persistent="false" />
```

The `giga-space` attribute is optional and is used if only one `GigaSpace` bean is defined in the Spring application context. The `fifo` flag defines whether the messages works in a FIFO mode or not. The `persistent` flag defines whether the messages are backed up to an external data source (if configured) using the Space.

{{% tip %}}
**All objects that end up being written to the space must be Serializable objects**
For any adapters that wraps a non-Serializable object such as JMS , EMail, Servlet and anything that uses Streams make sure you use inbound transformer before serialization.
{{% /tip %}}

# Inbound Component

The OpenSpaces queue inbound component is very simple once the queue connector is set. Here is an example:


```xml
<model>
    <service name="Appender1">

        <inbound-router>
            <os-queue:inbound-endpoint path="test1"/>
        </inbound-router>

        <!-- outbound transport ... -->
    </service>
</model>
```

# Outbound Component

The OpenSpaces queue outbound component is very simple to configure once the queue connector is set. Here is an example:


```xml
<model>
    <service name="Appender1">

        <!-- inbound transport ... -->

        <outbound-router>
        	<outbound-pass-through-router>
            	<os-queue:outbound-endpoint path="test2"/>
            </outbound-pass-through-router>
        </outbound-router>
    </service>
</model>
```

# Mule Messaging Styles Support

The queue supports the mule messaging styles - request-response and one-way.
The messaging style can be configured on the endpoints in the following way:


```xml
<model>
    <service name="Appender1">
        <!-- inbound transport ... -->
        <outbound-router>
        	<outbound-pass-through-router>
            	<os-queue:outbound-endpoint path="test2" exchange-pattern="request-response"/>
            </outbound-pass-through-router>
        </outbound-router>
    </service>
</model>
```

By default the exchange-pattern is set to request-response.

For further information see [http://www.mulesoft.org/documentation/display/MULECDEV/MEPs](http://www.mulesoft.org/documentation/display/MULECDEV/MEPs).

# Transaction Support

Operations performed on the Space when working with a virtualized queue are all performed using the `GigaSpace` bean. The `GigaSpace` bean supports [declarative transactions](./the-gigaspace-interface.html#OpenSpacesCoreComponent-GigaSpace-DeclarativeTransactions) when working with Spring transaction managers. Mule can be configured to work with Spring transaction managers as due to its built-in transaction management strategy, which allows you to use OpenSpaces with [different transaction managers](./transaction-management.html), including XA.

Here is an example of how to configure working with queues using the Distributed Transaction Manager:


```xml
<spring:beans>
    <os-core:embedded-space id="space" name="mySpace"/>

    <os-core:distributed-tx-manager id="transactionManager" />

    <os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

    <spring:bean id="transactionFactory"
                 class="org.mule.extras.spring.transaction.SpringTransactionFactory">
        <spring:property name="manager">
            <spring:ref bean="transactionManager"/>
        </spring:property>
    </spring:bean>

</spring:beans>

<model>
    <service name="Appender1">

        <!-- inbound transport ... -->

        <outbound-router>
        	<outbound-pass-through-router>
            	<os-queue:outbound-endpoint path="test2"/>
            </outbound-pass-through-router>
        </outbound-router>
    </service>
</model>
```

The above example defines a Mule `transactionFactory`, which is used to handle transactions within Mule. Since the `giga-space` bean is aware of it, any operations performed on it using the `os-queue` result in joining an existing transaction if one is in progress.

# Full Example

The example below will uses two components: `Appender1` and `Appender2`. The 2 services that wrap the components use the OpenSpaces queue transport as both the inbound and outbound transports.


```xml
<?xml version="1.0" encoding="UTF-8"?>
<mule xmlns="http://www.mulesoft.org/schema/mule/core"
      xmlns:os-events="http://www.openspaces.org/schema/events"
      xmlns:os-core="http://www.openspaces.org/schema/core"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:spring="http://www.springframework.org/schema/beans"
      xmlns:os-queue="http://www.openspaces.org/schema/mule/os-queue"
      xmlns:stdio="http://www.mulesoft.org/schema/mule/stdio"
      xsi:schemaLocation="http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{%currentversion%}}/events/openspaces-events.xsd
       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/3.1/mule.xsd
       http://www.mulesoft.org/schema/mule/stdio http://www.mulesoft.org/schema/mule/stdio/3.1/mule-stdio.xsd
       http://www.openspaces.org/schema/mule/os-queue http://www.openspaces.org/schema/{{%currentversion%}}/mule/3.1/mule-os-queue.xsd">

    <spring:beans>
        <os-core:embedded-space id="space" name="mySpace"  lookup-groups="${user.name}"/>

        <os-core:giga-space id="gigaSpace" space="space" />
    </spring:beans>

    <!-- this connector must be declared ,in order to inject the  Spring appliction context -->
    <os-queue:connector name="queueConnector" giga-space="gigaSpace"   />

    <model name="helloSample">

        <service name="Appender1">

            <inbound-router>
                <os-queue:inbound-endpoint path="test1"/>
            </inbound-router>

            <component class="test.EchoeComponentAppender"/>

            <outbound-router>
                <outbound-pass-through-router>
                    <os-queue:outbound-endpoint path="test2"/>
                </outbound-pass-through-router>
            </outbound-router>

        </service>

        <service name="Appender2">
            <inbound-router>
                <os-queue:inbound-endpoint path="test2"/>
            </inbound-router>

            <component class="test.EchoeComponentAppender"/>

            <outbound-router>
                <outbound-pass-through-router>
                    <os-queue:outbound-endpoint path="test3"/>
                </outbound-pass-through-router>
            </outbound-router>
        </service>
    </model>
</mule>
```

In order to complete the example, the `MuleClient` can be used to interact with the services.


```java
muleClient.dispatch("os-queue://test1", "testme", null);

MuleMessage message = muleClient.request("os-queue://test3", 5000);
```

The above code dispatches a message to a virtualized queue named `test1`. As a result, the `Appender1` service receives the message, processes it, and passes it to a virtualized queue named `test2`. In turn, the `Appender2` service receives the message, processes it, and sends it to a virtualized queue named `test3`. `muleClient` is used to receive a message from `test3` with a timeout of 5 seconds. This results in any receiving processed messages `Appender2` produces.
