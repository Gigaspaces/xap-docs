---
type: post101
title:  Example
categories: XAP101
parent: messaging-support.html
weight: 400
canonical: auto
---



{{%folderopen%}} Example Root `<XAP Root>\examples\data`

The [OpenSpaces Data Example]({{%currentjavatuturl%}}/the-openspaces-data-example.html) demonstrates how to use a JMS feeder to send POJOs to the space. In order to make it happen we use two main features: Spring [JmsTemplate](http://static.springframework.org/spring/docs/2.0.x/api/org/springframework/jms/core/JmsTemplate.html) and JMS [MessageConverter](./jms-space-interoperability.html). This section describes how those features are used in the example.

# Architecture

A processing unit runs the `JMSDataFeeder` that writes `Data` objects with raw data into the remote space. The `JMSDataFeeder` uses Spring's [JmsTemplate](http://static.springframework.org/spring/docs/2.0.x/api/org/springframework/jms/core/JmsTemplate.html) over GigaSpaces JMS, to write non-processed `Data` objects into the space every second. The `JMSDataFeeder` uses an `ObjectMessage2ObjectConverter` to convert the JMS ObjectMessages to `Data` objects before they are written to the space. Every `Data` object is written to the space with the processed value set to false, which is later set to true by the `DataProcessor`.

# JMSDataFeeder

The `JMSDataFeeder` is similar to the `DataFeeder`. The difference between the beans is that the `JMSDataFeeder` uses Spring's [JmsTemplate](http://static.springframework.org/spring/docs/2.0.x/api/org/springframework/jms/core/JmsTemplate.html) on top of the GigaSpaces JMS implementation to write the `Data` objects to the space; no space API is used directly. This is possible due to the usage of a `MessageConverter` that converts JMS messages into any required POJO type, in this case, `Data`. In this example, we configure the `ConnectionFactory` to use the `ObjectMessage2ObjectConverter` that comes with the GigaSpaces JMS implementation. The `ObjectMessage2ObjectConverter` receives a JMS `ObjectMessage` and returns the message's content (body) as the object to write to the space. The JMS `ObjectMessage` itself, including headers, properties etc., is not written. The `JMSDataFeeder` uses Spring's [JmsTemplate](http://static.springframework.org/spring/docs/2.0.x/api/org/springframework/jms/core/JmsTemplate.html) and `MessageCreator` to send `ObjectMessages` that contain the `Data` objects, and the converter makes sure that only the contained `Data` objects are written.

{{%tabs%}}
{{%tab "  Code "%}}


```java
public class JMSDataFeeder implements InitializingBean, DisposableBean {
    [..]
    Data data = new Data(Data.TYPES[counter++ % Data.TYPES.length], "FEEDER " + Long.toString(time));
    data.setProcessed(false);
    jmsTemplate.send(new MessageCreator() {
        public Message createMessage(Session session) throws JMSException {
            return session.createObjectMessage(data);
        }
    });
    [..]
}
```

{{% /tab %}}
{{%tab "  Configuration "%}}


```xml
<bean id="jmsDataFeeder" class="org.openspaces.example.data.feeder.JMSDataFeeder"/>
```

{{% /tab %}}
{{% /tabs %}}

The `JMSDataFeeder` is injected with a Spring [JmsTemplate](http://static.springframework.org/spring/docs/2.0.x/api/org/springframework/jms/core/JmsTemplate.html). The [JmsTemplate](http://static.springframework.org/spring/docs/2.0.x/api/org/springframework/jms/core/JmsTemplate.html) is injected with a JMS `ConnectionFactory` and a destination of type `Queue`. Unlike the `DataFeeder`, the `JMSDataFeeder` does not declare an instance of `GigaSpace`. `GigaSpace` is injected into the `ConnectionFactory` bean, and is used behind the scenes by the JMS layer. In addition, the `ConnectionFactory` is injected with a [MessageConverter](./jms-space-interoperability.html) of type `ObjectMessage2ObjectConverter`.

{{%tabs%}}
{{%tab "  Code "%}}


```java
public class JMSDataFeeder implements InitializingBean, DisposableBean {
    [..]
    /** Sets the JmsTemplate */
    public void setJmsTemplate(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate = jmsTemplate;
    }
    [..]
}
```

{{% /tab %}}
{{%tab "  Configuration "%}}


```xml
<bean id="jmsDataFeeder" class="org.openspaces.example.data.feeder.JMSDataFeeder">
    <property name="instanceId" value="${clusterInfo.instanceId}" />
    <property name="numberOfTypes" value="${numberOfTypes}" />
    <property name="jmsTemplate" ref="jmsTemplate" />
</bean>

<bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
    <property name="connectionFactory" ref="connectionFactory"/>
    <property name="defaultDestination" ref="destination" />
</bean>

<os-jms:queue id="destination" name="MyQueue" />

<os-jms:connection-factory id="connectionFactory" giga-space="gigaSpace" message-converter="messageConverter" />

<bean id="messageConverter" class="com.j_spaces.jms.utils.ObjectMessage2ObjectConverter" />
```

{{% /tab %}}
{{% /tabs %}}

Because the `JMSDataFeeder` implements Spring's `InitializingBean` and `DisposableBean` interfaces, its `afterPropertiesSet()` and `destroy()` methods are called when it is created or destroyed, respectively.
