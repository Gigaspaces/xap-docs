---
type: post97
title:  Known Issues and Considerations
categories: XAP97
parent: jms-advanced.html
weight: 500
canonical: auto
---

JMS API open issues, unsupported features, and considerations



- GS-2167 -- **Durable subscribers** are currently not supported. Trying to invoke the `Session.createDurableSubscriber` method or the `Session.unsubscribe` method throws a `JMSException`.
- GS-2168 -- When consuming messages from a Queue, and a session recovery takes place, the `JMSRedelivered` header of the recovered messages is not set. This problem does not occur with Topic recovered messages.
Message recovery takes place in the following scenarios:
    - Transaction rollback.
    - The `Session.recover` method is invoked.
    - In case of space failover (for more details, see the [JMS failover](./jms-failover.html) section).

- GS-2169 -- **Message Selectors** are currently not supported. Creating a `MessageConsumer` with a selector does not effect the message consumption. Note: Since the JMS message is also a space entry, you can use the space template matching and querying features.
- GS-2170 -- **Message priority** is currently not supported. Currently, all message are have the same priority: `Message.DEFAULT_PRIORITY`. When setting the priority of a message, the priority value is set correctly in the message, but the JMS layer does not take it in consideration.
As a result, when messages are redelivered, even if a message with higher priority arrives, it is not delivered before other messages.

- GS-2171 -- JMS sessions support only a single `MessageConsumer`. Trying to create a consumer for a session that already has a consumer throws a `JMSException`.
- GS-2173 -- JMS does not support `StreamMessage`. Using this message type will probably cause errors, and therefore is not recommended. A working implementation of `StreamMessage` is planned for future releases.
- GS-2213 -- JMS does not support message delivery threshold. A JMS provider might implement a message delivery threshold that prevents a message from being redelivered (due to session recovery) more times than that threshold number. When that number is passed, the message is sent to a dead letter queue. Currently, this feature is not supported.
- GS-3314 -- In cluster environment, custom JMS destinations that are specified in the properties file are not bound to the RMI registry.
For example, you may bind a JMS Queue by setting the property _`space-config.jms.administrated-destinations.queues.queue-names=`_**_`gs.Queue1`_**. You should expect to find it in the RMI registry as _`GigaSpaces;ContainerName;spaceName;jms;destinations;`_**_`gs.Queue1`_**, but it will be missing.
Workaround:

1. Extract the file: DefaultConfig_ClusteredJMS.xml from JSpaces.jar\config to the JS_HOME\config.
1. Edit DefaultConfig_ClusteredJMS.xml to contain the required Queue:


```java
<queues>
    <queue-names>MyQueue,TempQueue,gs.Queue1</queue-names>
</queues>
```

1. Restart the cluster.
This should make the required queue available in the RMI registry of all spaces in the cluster.

- GS-3478 -- JMS does not support destination partitioning. Currently, all messages of a JMS destination are routed to the same partition. The partition where the messages go is resolved according to the index field of the message which, currently, is the destination name.
