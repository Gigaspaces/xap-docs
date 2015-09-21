---
type: post97
title:  Browsing JMS Queues
categories: XAP97
parent: jms-advanced.html
weight: 400
---




A client application can create a `QueueBrowser` to examine messages in a queue without actually deleting them. The `QueueBrowser` contains the `getEnumeration()` method, which returns an enumeration of the queue's messages:


```java
QueueBrowser browser = session.createBrowser(queue);
Enumeration enum = browser.getEnumeration();
while (enum.hasMoreElements()) {
    System.out.println("Message on queue is: " + iter.nextElement());
}
```

{{% note %}}
This version of GigaSpaces JMS does not support message selectors. For more details, see the [JMS Known Issues and Considerations](./jms-known-issues-and-considerations.html).
{{%/note%}}

The JMS specifications do not define whether the `QueueBrowser` represents a snapshot of the queue, or whether the `QueueBrowser` dynamically updates it. However, with the Enterprise Messaging Grid, a snapshot is taken when the call is made to `getEnumeration()`.
