---
type: post102
title:  Transactions
categories: XAP102
parent: jms-advanced.html
weight: 200
---


XAP's JMS implementation uses transactions to implement the `Session.CLIENT_ACKNOWLEDGE` mode and transacted sessions.

When using `CLIENT_ACKNOWLEDGE` mode, messages are consumed in a transaction, and sent without a transaction.

The transaction is committed only when the client calls `Message.acknowledge()`, acknowledging the consumed messages.

Transacted sessions use transactions to consume and to send messages. When the client calls `Session.commit()`, unacknowledged messages are acknowledged and produced messages are written to the space.

# Local vs. Distributed Transactions

In GigaSpaces JMS, it is possible to use local transactions and distributed transactions (Mahalo).

In general, using local transactions is more efficient than using distributed transactions.

The only limitation when using local transactions relates to the fact that a transaction is valid only for a single space. This is problematic, because GigaSpaces routs JMS messages to the spaces according to the destination name. A message to a Queue named `myQueue1` might arrive at `space1`, while another message to a Queue named `myQueue2` might arrive at `space2`. If a client uses a local transaction to send the two messages, it might get a `TransactionException`.

To overcome this problem, the client can use distributed transactions, that can span over more than one space.

## When to Use Distributed Transactions

The JMS client uses a cluster that contains more than one space. The session is transacted, and transactions involve more than one destination (for message consumption and/or production):

- Consuming a message from a queue, and sending a message to another destination.
- Sending more than one message, to more than one destination.

{{% info %}}
You do not need to use distributed transactions when consuming from a Topic, and sending a single message to another destination.
{{%/info%}}

### Preparing JMS to Work with Distributed Transactions

To use Mahalo, you must first enable the Mahalo service in GigaSpaces.

To enable Mahalo, set the following XPath property in the `<XAP Root>\config\gs.properties` file:


```java
com.j_spaces.core.container.embedded-services.mahalo.start-embedded-mahalo=true
```

To use Mahalo transactions in JMS transacted sessions, add the following system property:


```java
com.gs.jms.use_mahalo=true
```

{{% info %}}
You can avoid using distributed transactions by:
{{%/info%}}

- Not involving more than one destination in a transaction.
- Using only a single space (not a cluster).
- Consuming messages from Topic, and sending messages to a **single** destination.
