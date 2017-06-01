---
type: post120
title:  Multi Site WAN Replication
categories: XAP120, ENT
parent: none
weight: 2400
---




Multiple site replication is the ability to replicate state between different deployed spaces, where each space can be also physically located in a different geographical location (also termed a different deployment site).

**Dependencies**<br>
In order to use this feature, include the `${XAP_HOME}/lib/optional/wan-gateway/xap-wan-gateway.jar` and `${XAP_HOME}/lib/optional/wan-gateway/xap-wan-gateway-spring.jar` files on your classpath or use maven dependencies:

```xml
<dependency>
    <groupId>com.gigaspaces</groupId>
    <artifactId>xap-wan-gateway</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
<dependency>
    <groupId>com.gigaspaces</groupId>
    <artifactId>xap-wan-gateway-spring</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
```
{{%refer%}}
For more information on dependencies see [Maven Artifacts](./maven-artifacts.html)
{{%/refer%}} 
 

<br>

{{%fpanel%}}

[Overview](./multi-site-replication-over-the-wan.html){{<wbr>}}
Establish data synchronization between multiple sites (spaces), usually used over the WAN.

[Communication Filter](./communication-filter-over-the-wan.html){{<wbr>}}
Creating filters over the network communication which goes across the WAN.

[Gateway Components](./replication-gateway-components.html){{<wbr>}}
Replication gateway components.

[Configuration](./configuring-space-gateway-targets.html){{<wbr>}}
Configure replication gateway targets of a space.

[Bootstrapping](./replication-gateway-bootstrapping-process.html){{<wbr>}}
Bootstrapping a site from another site across gateways.

[Conflict Resolution](./multi-site-conflict-resolution.html){{<wbr>}}
Multi-site replication conflict resolution and how to customize its functionality.

[Changing deployment during runtime](./changing-multi-site-deployment-during-runtime.html){{<wbr>}}
Configuring replication gateway targets of a space.

[Filtering](./replication-gateway-filtering.html){{<wbr>}}
Filtering specific data from being replicated through a gateway to another site.

[Intercepting Events](./intercepting-replication-events-at-the-gateway.html){{<wbr>}}
Custom interceptors at the gateway which can be used to implement custom logic upon incoming replication events in a target gateway

[Known Limitations](./multi-site-replication-limitations.html){{<wbr>}}
Known issues and limitations

[FAQ](/faq/multi-site-replication-over-the-wan-faq.html){{<wbr>}}
Frequently asked questions.
{{%/fpanel%}}

<br>

#### Additional Resources
{{%youtube "V7rbbmWo3JU"  "Multi Site Deployment"%}}








