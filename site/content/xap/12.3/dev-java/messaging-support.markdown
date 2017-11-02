---
type: post123
title:  JMS Messaging Support
categories: XAP123, OSS
parent: event-processing.html
weight: 700
---


XAP provide a JMS implementation, built on top of the core JavaSpaces layer. JMS messages are implemented as POJO, indexed, and routed to the relevant space partition according to the destination name. GigaSpaces XAP JMS implementation supports the unified messaging model, introduced in version 1.1 of the JMS specification.

**Dependencies**<br>
In order to use this feature, include the `${XAP_HOME}/lib/optional/jms/xap-jms.jar` file on your classpath or use maven dependencies:

```xml
<dependency>
    <groupId>org.gigaspaces</groupId>
    <artifactId>xap-jms</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
```
{{%refer%}}
For more information on dependencies see [Maven Artifacts](../started/maven-artifacts.html)
{{%/refer%}} 

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

