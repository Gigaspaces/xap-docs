---
type: post110
title:  Security
categories: XAP110
parent: jms-advanced.html
weight: 300
---


To use the Enterprise Messaging Grid security facilities, you must specify a user name and password when you create the connection:

- Using the unified model:


```java
Connection connection = connectionFactory.createConnection(username,password);
```

- Using the point to point domain:


```java
QueueConnection queueConnection = queueConnectionFactory.createQueueConnection(username,password);
```

- Using the publish/subscribe domain:


```java
TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(username,password);
```

{{% info %}}
To use authentication, you must define a secured space with the same user/password.
{{%/info%}}
