---
type: post102
title:  XAP JNDI
categories: XAP102
parent: messaging-support.html
weight: 500
canonical: auto
---



XAP runs a JNDI service where JMS objects can be bound by name. The administrator configures JMS destinations in the space configuration file. When the space is started, it creates and binds the configured destinations to JNDI, making them available for use.

The application uses a JNDI context to obtain these objects, and to start working with JMS. The context is initialized with the values specified in the `jndi.properties` file in the GigaSpaces classpath. After obtaining the context, the client calls the `context.lookup()` method, passing the name of the required resource. If the resource is found in JNDI, it is returned to the client.

# Obtaining ConnectionFactory Instances from JNDI

It is not possible to configure connection factories.

When JMS services in JNDI are enabled, the space creates and binds the following connection factories:


```java
Connectionfactory           GigaSpaces;ContainerName;SpaceName;GSConnectionFactoryImpl
TopicConnectionfactory      GigaSpaces;ContainerName;SpaceName;GSTopicConnectionFactoryImpl
QueueConnectionfactory      GigaSpaces;ContainerName;SpaceName;GSQueueConnectionFactoryImpl
```

To obtain a connection factory, the application uses JNDI lookup:


```java
// get a connection factory (unified model)
InitialContext context = new InitialContext();
ConnectionFactory factory = (ConnectionFactory)context.lookup("GigaSpaces;ContainerName;SpaceName;GSConnectionFactoryImpl");
```

or:


```java
// get a topic connection factory (publish/subscribe)
InitialContext context = new InitialContext();
TopicConnectionFactory factory = (TopicConnectionFactory)context.lookup("GigaSpaces;ContainerName;SpaceName;GSTopicConnectionFactoryImpl");
```

or:


```java
// get a queue connection factory (point to point)
InitialContext context = new InitialContext();
QueueConnectionFactory factory = (QueueConnectionFactory)context.lookup("GigaSpaces;ContainerName;SpaceName;GSQueueConnectionFactoryImpl");
```

# Configuring JMS Destinations

To enable JMS services in GigaSpaces JNDI, change the space schema file as follows:

1. Set the `com.j_spaces.core.container.directory_services.jms_services.enabled` property to `true`.
1. Add a comma-separated list of topic names to the `space-config.jms.administrated-destinations.topics.topic-names` property.
1. Add a comma-separated list of queue names to the `space-config.jms.administrated-destinations.queues.queue-names` property.

For example:


```java
com.j_spaces.core.container.directory_services.jms_services.enabled=true

space-config.jms.administrated-destinations.topics.topic-names=MyTopic,TempTopic
space-config.jms.administrated-destinations.queues.queue-names=MyQueue,TempQueue
```

The example above:

- Enables JMS services in JNDI
- Configures `MyTopic` and `TempTopic`.
- Configures `MyQueue` and `TempQueue`

{{% note %}}
When configuring destination as described above, the space binds the destinations to the following names:
{{%/note%}}


```java
GigaSpaces;ContainerName;spaceName;jms;destinations;MyTopic
GigaSpaces;ContainerName;spaceName;jms;destinations;TempTopic
GigaSpaces;ContainerName;spaceName;jms;destinations;MyQueue
GigaSpaces;ContainerName;spaceName;jms;destinations;TempQueue
```

# Obtaining JMS Destinations from JNDI

The application uses a JNDI context to get the destinations from JNDI.

For example, to get the `MyTopic` Topic:


```java
InitialContext context = new InitialContext();
Topic myTopic = (Topic)context.lookup("GigaSpaces;containername;spaceName;jms;destinations;MyTopic");
```

{{% info %}}
To get the resource, use the full binding name. If you configure `MyTopic` in the space configuration, the space binds it to the name:
{{%/info%}}


```java
GigaSpaces;ContainerName;spaceName;jms;destinations;MyTopic.
```
