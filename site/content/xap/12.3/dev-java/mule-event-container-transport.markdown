---
type: post123
title:  Event Container Transport
categories: XAP123, OSS
parent: mule-esb.html
weight: 100
---

{{% ssummary%}}  {{% /ssummary %}}


XAP event container transport uses [event components](./messaging-support.html) (namely the [polling container](./polling-container-overview.html) and [notify container](./notify-container-overview.html)) to receive POJO messages using the Space as the transport layer. It also allows you to send POJO messages using the Space as the transport layer.

Since Mule configuration uses Spring, the event container transport can be easily integrated with OpenSpaces event components.

In order to use the event container transport (using XML namespaces), the following XML headers need to be defined:


```xml
<mule xmlns="http://www.mulesoft.org/schema/mule/core"
      xmlns:os-events="http://www.openspaces.org/schema/events"
      xmlns:os-core="http://www.openspaces.org/schema/core"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:spring="http://www.springframework.org/schema/beans"
      xmlns:os-eventcontainer="http://www.openspaces.org/schema/mule/os-eventcontainer"
      xmlns:stdio="http://www.mulesoft.org/schema/mule/stdio"
      xsi:schemaLocation="http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
      http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{%currentversion%}}/events/openspaces-events.xsd
      http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
      http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/{{%version mule%}}/mule.xsd
      http://www.mulesoft.org/schema/mule/stdio http://www.mulesoft.org/schema/mule/stdio/{{%version mule%}}/mule-stdio.xsd
      http://www.openspaces.org/schema/mule/os-eventcontainer http://www.openspaces.org/schema/{{%currentversion%}}/mule/{{%version mule%}}/mule-os-eventcontainer.xsd">

       <!-- Mule configuration comes here ... -->

</mule>
```

To configure OpenSpaces inbound or outbound components within Mule configuration file, we first have to configure the space. Declaring pure Spring beans within the Mule configuration file should be made between Spring bean tags.

The `space` and `giga-space` are declared just like any other typical OpenSpaces Spring configuration, for example:


```xml
    <spring:beans>
        <!--
            A bean representing a space (an IJSpace implementation).
        -->
        <os-core:embedded-space id="space" space-name="mySpace"  lookup-groups="${user.name}"/>

        <!--
            OpenSpaces simplified space API built on top of IJSpace/JavaSpace.
        -->
        <os-core:giga-space id="gigaSpace" space="space"/>

		....
		....

    </spring:beans>
```

When configuring inbound or outbound OpenSpaces event container endpoints, we must also configure an `OpenSpacesConnector` explicitly. This connector acts as a bridge between the inbound and outbound transports and the Spring application context. The application context is then used to get a reference to the `GigaSpace` bean and the specific polling or notify container beans.


```xml
<os-eventcontainer:connector name="gigaSpacesConnector"/>
```

# Inbound Component

OpenSpaces inbound component is based on either the polling event container or the notify event container. It registers itself as a listener to one of them. Here is an example of configuring a polling event container within the `spring:beans` Mule definition:


```xml
<spring:beans>
		......
		......

    <os-events:polling-container id="helloPollingEventContainer" giga-space="gigaSpace">
        <os-core:template>
            <spring:bean class="org.openspaces.itest.esb.mule.SimpleMessage">
                <spring:property name="read" value="false"/>
            </spring:bean>
        </os-core:template>
    </os-events:polling-container>
</spring:beans>
```

Unlike common polling/notify container configuration, this container lacks an event listener. This is due to the fact that the actual inbound transport acts as the event container listener.

We then configure the inbound transport as defined in the example below. The `os-eventcontainer://<eventContainerBeanId>` address means that this is an inbound endpoint that receives messages from the OpenSpaces polling/notify container registered under the bean name `<eventContainerBeanId>`. Here is an example that uses the `helloPollingEventContainer` defined above.


```xml
<model name="helloSample">

    <service name="MessageReaderUMO">
        <inbound-router>
            <os-eventcontainer:inbound-endpoint address="os-eventcontainer://helloPollingEventContainer"/>
        </inbound-router>

		<!-- outbound definition ... -->

    </service>

</model>
```

## Threading Model

By default, this inbound transport based on the event container uses the threading model defined within the event container definitions. There is an option to set `workManager=true`, which results in offloading the processing of a request to a managed Mule thread. This is currently not recommended.

# Outbound Component

The outbound endpoint is used for updating or writing POJO messages back to the Space.

We configure the outbound as defined in the example below. The `os-eventcontainer://< gigaSpacebeanId>` address means that this is an outbound endpoint that sends (writes) messages to the Space using the `GigaSpace` beans registered under the bean name `<gigaSpacebeanId>`. Here is an example:


```xml

<model name="helloSample">
    <service name="MessageReaderUMO">

        <!-- inbound definitions -->

        <outbound-router>
	        <outbound-pass-through-router>
	            <os-eventcontainer:outbound-endpoint address="os-eventcontainer://gigaSpace"/>
	        </outbound-pass-through-router>
	    </outbound-router>
    </service>
</model>
```

There is option to configure `writeLease` (defaults to `FOREVER`), `updateOrWrite` (defaults to `true`), and `updateTimeout` (defaults to `0`) parameters.

# Transaction Support

Since the inbound component is based on the event containers, transaction support can be enabled using the event container transaction support. Learn how to do this with the [polling container](./polling-container-overview.html#OpenSpacesEventsComponent-PollingContainer-TransactionSupport).

Any operation performed using the `GigaSpace` interface joins the ongoing transaction started by the event container using its support for [declarative transactions](./the-gigaspace-interface.html#OpenSpacesCoreComponent-GigaSpace-DeclarativeTransactions).

This means that any outbound component operating within a Spring managed transaction automatically joins the transaction since it uses `GigaSpace`. For outbound components that are used with a different inbound component (such as JMS), the Mule Spring transaction manager can be used. Here is an example (note the custom-transaction tag in the inbound and outbound transports):


```xml
<spring:beans>
   <os-core:embedded-space id="space" space-name="mySpace"/>

    <os-core:distributed-tx-manager id="transactionManager" />

    <os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

    <spring:bean id="transactionFactory"
                 class="org.mule.module.spring.transaction.SpringTransactionFactory">
        <spring:property name="manager">
            <spring:ref bean="transactionManager"/>
        </spring:property>
    </spring:bean>

</spring:beans>

<model name="helloSample">

    <service name="MessageReaderUMO">
        <inbound>
            <os-eventcontainer:inbound-endpoint
                    address="os-eventcontainer://helloPollingEventContainer">
                <custom-transaction factory-ref="transactionFactory" action="BEGIN_OR_JOIN"/>
            </os-eventcontainer:inbound-endpoint>
        </inbound>

        <component class="org.openspaces.itest.esb.mule.eventcontainer.tx.TxRollBackMessageReader"/>

        <outbound>
            <outbound-pass-through-router>
                <os-eventcontainer:outbound-endpoint giga-space="gigaSpace">
                    <custom-transaction factory-ref="transactionFactory" action="BEGIN_OR_JOIN"/>
                </os-eventcontainer:outbound-endpoint>
            </outbound-pass-through-router>
        </outbound>

    </service>

</model>
```

In the above example, the Mule transaction factory used is Spring-based, wrapping the Spring `PlatformTransactionManager`. For more information regarding OpenSpaces support for transactions (including XA), see the [OpenSpaces Core Component - Transaction Manager](./transaction-management.html) section.

# Full Example

In this example, POJO messages are received (`SimpleMessage`) from the Space, with their `read` flag set to `false`. The `MessageReader` service then processes the messages (and among other operations, sets the `read` flag to `true`). Finally, the `MessageReader` result is written back to the Space.


```xml

<?xml version="1.0" encoding="UTF-8"?>
<mule xmlns="http://www.mulesoft.org/schema/mule/core"
      xmlns:os-events="http://www.openspaces.org/schema/events"
      xmlns:os-core="http://www.openspaces.org/schema/core"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:spring="http://www.springframework.org/schema/beans"
      xmlns:os-eventcontainer="http://www.openspaces.org/schema/mule/os-eventcontainer"
      xmlns:stdio="http://www.mulesoft.org/schema/mule/stdio"
      xsi:schemaLocation="http://www.openspaces.org/schema/core http://www.openspaces.org/schema/core/openspaces-core.xsd
      http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
      http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
      http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/{{%version mule%}}/mule.xsd
      http://www.mulesoft.org/schema/mule/stdio http://www.mulesoft.org/schema/mule/stdio/{{%version mule%}}/mule-stdio.xsd
      http://www.openspaces.org/schema/mule/os-eventcontainer http://www.openspaces.org/schema/{{%currentversion%}}/mule/{{%version mule%}}/mule-os-eventcontainer.xsd">

    <description>Tests mule connector, receive and process single object at a time.</description>

    <spring:beans>
        <os-core:embedded-space id="space" space-name="space" lookup-groups="${user.name}"/>

        <os-core:giga-space id="gigaSpace" space="space"/>

        <os-events:polling-container id="helloPollingEventContainer" giga-space="gigaSpace">
            <os-core:template>
                <spring:bean class="org.openspaces.itest.esb.mule.SimpleMessage">
                    <spring:property name="read" value="false"/>
                </spring:bean>
            </os-core:template>
        </os-events:polling-container>
    </spring:beans>

    <!-- this connector must be declared ,in order to inject the  Spring appliction context -->
    <os-eventcontainer:connector name="gigaSpacesConnector"/>

    <!--The Mule model initialises and manages your UMO components-->

    <model name="helloSample">

        <service name="MessageReaderUMO">
            <inbound >
                <os-eventcontainer:inbound-endpoint address="os-eventcontainer://helloPollingEventContainer"/>
            </inbound>

            <component class="org.openspaces.itest.esb.mule.MessageReader"/>

            <outbound>
                <outbound-pass-through-router>
                    <os-eventcontainer:outbound-endpoint
                     address="os-eventcontainer://gigaSpace"/>
                </outbound-pass-through-router>
            </outbound>

        </service>

    </model>

</mule>
```

# Message Metadata

OpenSpaces mule event transport supports sending and receiving metadata using Mule. Since we are working with pure POJOs, these should implement the `MessageHeader` interface in order to include metadata. OpenSpaces Mule integration comes with a simple base class that already implements the `MessageHeader` interface called `AbstractMessageHeader`.

# Transformer

In order to transfer the metadata from the `UMOMessage` to the outgoing message via the OpenSpaces outbound endpoint, the transport is configured with a default outbound transformer called `org.openspaces.esb.mule.transformers.OpenSpacesTransformer`. The transformer copies the metadata from the `UMOMessage` to the transformed object, and assumes that the return object is the payload that is contained within the `UMOMessage`.

If the returned object is not the payload within the `UMOMessage`, the transformer allows overriding of the `getResultPayload(UMOMessage, String)` method and returns the new transformer object.

Here is an example:


```java
public class MessageTransformer extends OpenSpacesTransformer {

    protected Object getResultPayload(UMOMessage message, String outputEncoding) {
        Message msg = (Message) message.getPayload();
        ProcessedMessage pMsg = new ProcessedMessage(msg.getMessage());
        pMsg.setProperty("name", "processed message " + msg.getMessage());
        return pMsg;
    }
}
```


```java
public class ProcessedMessage extends AbstractMessageHeader implements Message {

    private String message;
		....
		....
}
```
