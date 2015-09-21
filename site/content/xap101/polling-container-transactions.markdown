---
type: post101
title:  Transaction Support
categories: XAP101
parent: polling-container-overview.html
weight: 200
---

{{% ssummary  %}}{{%/ssummary%}}

{{%wbr%}}

Both the receive operation and the actual event action can be configured to be performed under a transaction. Transaction support is required when, for example, an exception occurs in the event listener, and the receive operation needs to be to rolled back (and the actual data event is returned to the space). Adding transaction support is very simple in the polling container, and can be done by injecting a transaction manager into it.

For example:

{{%tabs%}}
{{%tab "  Annotation "%}}


```xml

<!-- Enable scan for OpenSpaces and Spring components -->
<context:component-scan base-package="com.mycompany"/>

<!-- Enable support for @Polling annotation -->
<os-events:annotation-support />

<os-core:embedded-space id="space" name="mySpace"/>

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

<os-core:embedded-space id="space" name="mySpace"/>

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

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
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
```

{{% /tab %}}
{{% /tabs %}}

# Isolation & Timeout

When using transactions with polling container a special care should be taken with timeout values. Transactions started by the polling container can have a timeout value associated with them (if not set will default to the default timeout value of the transaction manager, which is 60 Sec). If setting a specific timeout value, make sure the timeout value is higher than the timeout value for blocking operations and includes the expected execution time of the associated listener.

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

<os-core:embedded-space id="space" name="mySpace"/>

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

<os-core:embedded-space id="space" name="mySpace"/>

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

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
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

