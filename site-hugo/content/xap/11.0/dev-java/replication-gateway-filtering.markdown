---
type: post110
title:  Filtering  Data
categories: XAP110
parent: multi-site-replication-overview.html
weight: 700
canonical: auto
---



In some cases, there can be data that should not be replicated between the sites but should still be replicated locally between the primary and the backup/mirror service. In this case using the replicate class level or object level decoration is irrelevant as there is a need to control the replication behavior only to the remote site. Since a replication channel to a gateway is like any other replication channel, therefore a custom [Replication Filter]({{%currentadmurl%}}/cluster-replication-filters.html) at the source space can be used to filter the relevant data from being sent to the target gateway.

{{% align center%}}
![WAN-replicationfilter.jpg](/attachment_files/WAN-replicationfilter.jpg)
{{% /align%}}

This filtering should be based on the replication target name in order to identify the correct replication filter is called for the outgoing replication to the gateway.

{{% tip %}}
The output-filter can be used also to modify the replicated data before it is arriving the target site. When using a `SpaceDocument` the modified field must be a predefined field described with the document schema (fixed field).
{{% /tip %}}

# Using the Filter

With the example below a replication filter is used with the source space (output-filter). The New-York space is configured not to replicate the Stock object type to London site. This Stock object type still being replicated to all other location replication targets (backup/mirror) and also to the remote Hong Kong gateway. The filtering can be determined also based on the content of the replicated object or any other custom business logic.

{{%tabs%}}
{{%tab "  New York Space "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:os-core="http://www.openspaces.org/schema/core"
	xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-sla="http://www.openspaces.org/schema/sla"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

        <bean id="londonFilter" class="com.gigaspaces.examples.gateway.LondonReplicationFilter"/>

        <os-core:embedded-space id="space" name="myNYSpace" gateway-targets="gatewayTargets">
          <os-core:space-replication-filter>
            <os-core:output-filter ref="londonFilter"/>
          </os-core:space-replication-filter>
        </os-core:embedded-space>

        <os-gateway:targets id="gatewayTargets" local-gateway-name="NEWYORK">
          <os-gateway:target name="LONDON"/>
          <os-gateway:target name="HONGKONG"/>
        </os-gateway:targets>

</beans>
```

{{% /tab %}}
{{%tab "  Replication Filter Implementation "%}}


```java
public class LondonReplicationFilter implements IReplicationFilter {

  public void init(IJSpace space, String paramUrl, ReplicationPolicy replicationPolicy) {
  }

  public void process(int direction, IReplicationFilterEntry replicationFilterEntry, String replicationTargetName) {
      if (replicationTargetName.equals("gateway:LONDON")) {
          if (replicationFilterEntry.getClassName().equals(Stock.class.getName())) {
              replicationFilterEntry.discard();
          }
      }
  }

  public void close() {
  }
}
```

{{% /tab %}}
{{% /tabs %}}

