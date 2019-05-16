---
type: post122
title:  Transaction Support
categories: XAP122, OSS
parent: notify-container-overview.html
weight: 200
canonical: auto
---



The notify container can be configured with transaction support, so the event action can be performed under a transaction. Exceptions thrown by the event listener cause the operations performed within the listener to be rolled back automatically.

{{% note %}}
When using transactions, only the event listener operations are rolled back. The notifications are not sent again in case of a transaction rollback. In case this behavior is required, please consider using the [Polling Event Container](./polling-container.html).
{{%/note%}}

Transaction support can be configured as follows:

{{%tabs%}}
{{%tab "  Annotation "%}}


```xml

<!-- Enable scan for OpenSpaces and Spring components -->
<context:component-scan base-package="com.mycompany"/>

<!-- Enable support for @Polling annotation -->
<os-events:annotation-support />

<os-core:embedded-space  id="space" space-name="mySpace"/>

<os-core:distributed-tx-manager id="transactionManager" />

<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>
```


```java
@EventDriven @Notify @TransactionalEvent
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

<os-core:distributed-tx-manager id="transactionManager" />

<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

<bean id="simpleListener" class="SimpleListener" />

<os-events:notify-container id="eventContainer" giga-space="gigaSpace">

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
</os-events:notify-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="transactionManager" class="org.openspaces.core.transaction.manager.LocalJiniTransactionManager">
	<property name="space" ref="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
    <property name="space" ref="space" />
	<property name="transactionManager" ref="transactionManager" />
</bean>

<bean id="simpleListener" class="SimpleListener" />

<bean id="eventContainer" class="org.openspaces.events.notify.SimpleNotifyEventListenerContainer">
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
{{% /tabs %}}

