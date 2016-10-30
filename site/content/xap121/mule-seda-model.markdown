---
type: post121
title:  SEDA Model
categories: XAP121
parent: mule-esb.html
weight: 400
---


{{% ssummary  %}} {{% /ssummary %}}



A [Mule model](http://www.mulesoft.org/documentation-3.2/display/MULE3USER/Models#Models) manages the runtime behavior of the service components that a Mule instance hosts. The manner in which these components are invoked and treated is all encapsulated inside the Mule model.

XAP's Mule model integration comes with two main features. The first is the ability to define services that are aware of the "direct" Space mode they are working with (the direct cluster member) and only start when working against a primary cluster member (in an SBA topology). The second feature is the ability to store SEDA queues in a virtualized space queue instead of Mule's default VM queues.

To use this model, we have to add `os-seda` to the namespaces.


```xml
< ...
xmlns:os-seda="http://www.openspaces.org/schema/mule/os-seda"
...
xsi:schemaLocation="
http://www.openspaces.org/schema/mule/os-seda http://www.openspaces.org/schema/8.0/mule/3.1/mule-os-seda.xsd>
```

# Space Mode Aware Service

When working in an SBA architecture, where each Mule Processing Unit works with (and starts) an embedded Space cluster member, some cluster members are actually the backup members (in a primary-backup topology). In such a case, operations performed directly against the Space cluster member are not allowed (clustered operations are, of course, allowed). Many times, we want to start services that work directly against the cluster member only when they are working directly with a primary cluster member. For more details, see the [OpenSpaces Core Component - Space](./the-space-configuration.html#proxy) section.

This is easily achieved using OpenSpaces extensions to the Mule SEDA model. Here is an example:


```xml
<spring:beans>
	<os-core:embedded-space id="space" name="mySpace"/>
	<os-core:giga-space id="gigaSpace" space="space" />
	<os-core:giga-space id="clusteredGigaSpace" space="space" clustered="true" />
</spring:beans>

<os-seda:model name="sampleModel" giga-space="gigaSpace">

    <os-seda:space-aware-service name="spaceAwareService">

        <!-- service definition -->

    </os-seda:space-aware-service>

    <service name="plainService">

        <!-- service definition -->

    </service>

</os-seda:model>
```

In the above example we have two services: `spaceAwareService` and `plainService`. `spaceAwareService` only starts if the Space it is working against is a primary cluster member. `plainService` works in any case (which is fine if it uses the `clusteredGigaSpace` bean).

# Enhanced SEDA Model

OpenSpaces enhanced SEDA model allows storing of SEDA queues on top of the Space. This allows you to have a highly available and fault-tolerant SEDA implementation with the benefits of fast, in-memory, virtualized Space queue implementations.

The enhanced SEDA queue model is mainly used when working in SBA mode. This means that each Mule Processing Unit instance starts a Space cluster member, and SEDA queues are stored directly in the cluster member this instance started. When working with a primary-backup topology, `os-seda` aware services of a Mule Processing Unit instance that started a backup cluster member are not started until the cluster member moves to primary mode.

{{% info %}}
In such a topology, SEDA queue content is replicated from the primary cluster member to the backup, allowing you to continue the processing seamlessly in case of the primary failure.
{{%/info%}}

Here is an example of how it can be configured:


```xml
<spring:beans>
	<os-core:embedded-space id="space" name="mySpace" lookup-groups="${user.name}"/>
	<os-core:giga-space id="gigaSpace" space="space" />
</spring:beans>

<os-seda:model name="helloSample">

    <os-seda:service name="Appender1">

        <inbound-router>
            <vm:inbound-endpoint path="test1"/>
        </inbound-router>

        <component class="org.mule.components.simple.EchoComponent"/>

        <outbound-router>
            <outbound-pass-through-router>
                <vm:outbound-endpoint path="test2"/>
            </outbound-pass-through-router>
        </outbound-router>

    </os-seda:service>

    <os-seda:service name="Appender2">

        <inbound-router>
            <vm:inbound-endpoint path="test2"/>
        </inbound-router>

        <component class="org.mule.components.simple.EchoComponent"/>

        <outbound-router>
            <outbound-pass-through-router>
                <vm:outbound-endpoint path="test3"/>
            </outbound-pass-through-router>
        </outbound-router>

    </os-seda:service>

</model>
```

In the above example, the fact that we are using the `os-seda:model`, and in it `os-seda:service` means that the `os-seda:service` instance queue is a virtualized queue stored in the Space.

# Transaction Support

The operations performed on the Space when working with the SEDA virtualized queue are all performed using the `GigaSpace` bean. `GigaSpace` supports [declarative transactions](./the-gigaspace-interface.html#OpenSpacesCoreComponent-GigaSpace-DeclarativeTransactions) when working with Spring transaction managers. Mule can be configured to work with Spring transaction managers as its built-in transaction management strategy allows you to use OpenSpaces with [different transaction managers](./transaction-management.html), including XA.

Here is an example of how working with `os-seda` using the Space Local Transaction Manager can be configured:


```xml
<spring:beans>
   <os-core:embedded-space id="space" name="mySpace"/>

    <os-core:distributed-tx-manager id="transactionManager" />

    <os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

    <spring:bean id="transactionFactory"
                 class="org.mule.module.spring.transaction.SpringTransactionFactory">
        <spring:property name="manager">
            <spring:ref bean="transactionManager"/>
        </spring:property>
    </spring:bean>

</spring:beans>

<os-seda:model name="helloSample">

    <os-seda:service name="Appender1">

        <inbound-router>
            <vm:inbound-endpoint path="test1"/>
        </inbound-router>

        <component class="org.mule.components.simple.EchoComponent"/>

        <outbound-router>
            <outbound-pass-through-router>
                <vm:outbound-endpoint path="test2"/>
            </outbound-pass-through-router>
        </outbound-router>

    </os-seda:service>
</os-seda:model>
```

The above example defines a Mule `transactionFactory`, which is used to handle transactions within Mule. Since the `giga-space` bean is aware of it, any operations performed on it using `os-seda` result in joining an existing transaction if one is in progress.
