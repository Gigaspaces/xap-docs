---
type: post102
title:  JMS Messaging Support
categories: XAP102
parent: event-processing.html
weight: 700
---

<br>

XAP provide a JMS implementation, built on top of the core JavaSpaces layer. JMS messages are implemented as POJO, indexed, and routed to the relevant space partition according to the destination name. GigaSpaces XAP JMS implementation supports the unified messaging model, introduced in version 1.1 of the JMS specification.


{{%note%}}
Starting with 10.1, the JMS support has been moved out of the core into its own jar files. When using JMS, please include the jar files located under `[XAP-HOME]/lib/optional/jms`
in your runtime configuration.
{{%/note%}}

<br>


{{%fpanel%}}

[JMS Basics](./jms-api-basic-usage.html){{<wbr>}}
JMS basics and concepts.

[JMS Messages with XAP](./jms-messages-in-gigaspaces.html){{<wbr>}}
JMS messages implementation; supported and unsupported message types; message compression; accessing JMS messages via space API.

[JMS-Space Interoperability](./jms-space-interoperability.html){{<wbr>}}
Creating JMS messages with the space API; reading/taking JMS messages with the space API; using JMS API with the MessageConverter to send custom POJOs to the space.

[JMS Example](./jms-with-openspaces-example.html){{<wbr>}}
Including a JMS feeder in a processing unit using Spring JmsTemplate, and using the JMS message converter to send POJOs to the space, using the JMS API.

[XAP JNDI](./jms-with-gigaspaces-jndi.html){{<wbr>}}
JNDI Overview, Binding/Obtaining ConnectionFactory and Destination instances.

[JMS resources without JNDI](./jms-with-gsjmsadmin.html){{<wbr>}}
Obtaining JMS resources without JNDI, using JNDI with GSJMSAdmin.

[Andvanced Options](./jms-advanced.html){{<wbr>}}
Advanced options including transaction support and security.
{{%/fpanel%}}

<br>


#### Additional Resources


| Best Practice | Description|
|:--------------|:-----------|
|[<nobr>Master-Worker Pattern</nobr>](/sbp/master-worker-pattern.html)| Grid computing pattern. Implementing distributed processing across multiple workers deployed into the Grid. |
|[Priority Based Queue](/sbp/priority-based-queue.html)|Messaging based pattern. Can be used when moving from **J2EE JMS** Quality of Service into XAP.|
|[Parallel Queue Pattern](/sbp/parallel-queue-pattern.html)|Messaging based pattern. Can be used when moving from **J2EE JMS Service Activator Aggregator Strategy/MDB** into XAP.|
|[Unit Of Work](/sbp/unit-of-work.html)|Messaging based pattern. Can be used when moving from **J2EE JMS** Unit of Order into XAP.|

