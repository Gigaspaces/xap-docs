---
type: post122
title:  JMS Failover
categories: XAP122, OSS
parent: jms-advanced.html
weight: 100
---

{{%ssummary%}}{{%/ssummary%}}

The JMS layer is responsible for handling space failover properly. The goal is to make failover transparent to the JMS client. In a primary-backup architecture, when the primary space fails, the backup space takes its place as the primary space. This switch should be transparent to the client application, so it won't know that failover ever occurred.

However, in some scenarios, the GigaSpaces JMS implementation uses local transactions, which are valid only on a single space. Messages consumed in a transaction are not acknowledged, and messaged produced in a transaction are not sent until the transaction is committed. If a transaction is aborted (rolled back), consumed messages are recovered, and produced messages are disposed.

In case of failover, any transaction related to the space that failed is aborted. Messages produced under the transaction are disposed, and consumed messages are automatically recovered. This cannot be transparent to the client application, since it has to know about it and respond accordingly.

Failover handling in GigaSpaces JMS relates to transactions. The GigaSpaces JMS implementation uses transactions to implement the `Session.CLIENT_ACKNOWLEDGE` mode and transacted sessions. Consumed messages in `Session.CLIENT_ACKNOWLEDGE` mode are acknowledged only when the client calls `Message.acknowledge()`. Space failure aborts the transaction used at that time, and all unacknowledged messages are redelivered.

{{% note %}}
When working in `Session.CLIENT_ACKNOWLEDGE` mode, produced messages are not included in the transaction.
{{%/note%}}

Transacted sessions also use transactions. Consumed messages are not acknowledged and produced messages are not sent until `Session.commit()` is called. Failover aborts the transaction used at that time, and as a result, produced messages are disposed and consumed messages are redelivered.

{{% note %}}
Failover might occur during the actual acknowledge action.
{{%/note%}}

# Notifying the Client about Failovers

When working in **synchronous delivery mode**, the JMS layer tries to receive a message from the space when the client calls `MessageConsumer.receive()`. The JMS layer notifies the client about space failover by throwing a `TransactionRolledBackException` to the user.

When working in **asynchronous delivery mode**, the JMS layer delivers messages to the client by using the `MessageListener.onMessage(Message)` method.

When using this interface, The JMS layer can't notify the client about the failure. In this case, when the JMS layer detects failover, it calls the connection's `ExceptionListener.onException()` method, passing it a `SpaceLostException` object that describes the problem. Clients should register an `ExceptionListener` with the connection, and look for the `SpaceLostException`. Without doing so, in case of a failover, asynchronous `MessageConsumers` might receive recovered messages without knowing about the failover.

# Handling Failovers with JMS

When using asynchronous `MessageConsumers`, the client should register an `ExceptionListener` with the connection. When space failure occurs, if a transaction was aborted, the JMS layer sends a `SpaceLostException` to notify the client.

When using synchronous consumers, a failover occurs, and a transaction is aborted, the JMS layer notifies the client about it by throwing a `TransactionRolledBackException` from the `receive()` method.

If failover occurs during message acknowledgment (when the client calls `Session.commit()` or `Message.acknowledge()`), the JMS layer throws a `TransactionRolledBackException`.

In `Session.CLIENT_ACKNOWLEDGE` mode, the client should react to this exception as it would react to **session recovery**.

In case of a single consumer, the next message to arrive is the first unacknowledged message. The `JMSRedelivered` header of recovered messages is set.

In transacted sessions, the client should react to this exception as it would react to **session rollback**.

The client has to resend the produced messages that have been disposed. In case of a single consumer, the next message to arrive is the first unacknowledged message. The `JMSRedelivered` header of recovered messages is set.

{{% refer %}}
The `JMSRedelivered` header is not set for messages redelivered from a Queue. For more details, see the [JMS Known Issues and Considerations](./jms-known-issues-and-considerations.html).
{{%/refer%}}

# Example Scenarios


## Synchronous Consumer

1. The client is using a single **synchronous** `MessageConsumer` that receives messages from a Queue, and wants to receive 5 messages and then acknowledge them all.
1. To do this, the client uses a session in `CLIENT_ACKNOWLEDGE` mode. It then receives and processes 3 messages with no error.
1. A space failure occurs, and the backup space takes its place as the primary space.
1. As a result, the transaction is aborted, and unacknowledged messages are automatically recovered.
1. In the fourth `receive()` call, the JMS layer detects the space failure.
1. To notify the client, the JMS layer throws a `TransactionRolledBackException`.
1. In `CLIENT_ACKNOWLEDGE` mode, the client should react to this as it would react to a session recovery.
1. In the next `receive()` call, the client receives the first unacknowledged message.
1. The `JMSRedelivered` header of recovered messages is set.

{{% refer %}}
In GigaSpaces 6.0, the `JMSRedelivered` header is not set for messages redelivered from a Queue. For more details, see the [Known Issues and Limitations](./jms-known-issues-and-considerations.html).
{{%/refer%}}

## Asynchronous Consumer

1. The client uses a single **asynchronous** `MessageConsumer` that receives messages from a Queue, and wants to receive 5 messages and then acknowledge them all.
1. To do this, the client uses a session in `CLIENT_ACKNOWLEDGE` mode.
1. The client receives and processes 3 messages with no error.
1. A space failure occurs, and the backup space takes its place as the primary space.
1. As a result, the transaction is aborted, and unacknowledged messages are automatically recovered.
1. To notify the client, the JMS layer calls the connection's `ExceptionListener` `onException()` method, passing it a `SpaceLostException`.
1. In `CLIENT_ACKNOWLEDGE` mode, the client should react to this as it would react to a session recovery.
1. The next message to arrive is the first unacknowledged message.
1. The `JMSRedelivered` header of recovered messages is set.


{{% refer %}}
In GigaSpaces 6.0, the `JMSRedelivered` header is not set for messages redelivered from a Queue. For more details, see the [Known Issues and Limitations](./jms-known-issues-and-considerations.html).
{{%/refer%}}
