---
type: post110
title:  Overview
categories: XAP110
parent: multi-space-replication-overview.html
weight: 100
---

{{% ssummary %}}  {{% /ssummary %}}



Multiple space replication is the ability to replicate state between different deployed spaces, i.e different cluster of space instances. Where each of the space instances of each of the spaces are reachable via network to the other. In some cases, this may even be across WAN using VPN or other mechanism to establish a VLAN. However, it is required to have a direct network connection between all space instances of all of the clusters (or at least a connection between all space instances to the targeted space gateway machine). Replicating between spaces in the same network, is done using exactly the same mechanisms and gateway in the [Multi-Site Replication over the WAN](./multi-site-replication-over-the-wan.html) case, however in a simplified way since many if the configuration and some of the components are not needed as the spaces reside in the same network.

![wan_use_cases.jpg](/attachment_files/wan_use_cases.jpg)

## Gateway Features

The GigaSpaces Gateway features the following:

- Total Data consistency, ordering with no data loss - Due-to its architecture, Gateway is stateless and data loss will never happen. Transactions at a source space are replicated in atomic manner and in the correct order to the remote spaces.
- Conflict resolution support - GigaSpaces will identify data conflicts and allow you to decide to override the local copy with the remote replicated copy, reject the remote replicated copy or merge the local and the remote copy.
- Monitoring and Management - The Gateway exposes statistics on replication activity to remote spaces.
- Multi-Master / Master-Slave support - The Gateway support for all popular replication topologies.
- Space bootstrapping - Once a space starts it can reload its entire data or specific data from a different space without introducing data consistency problems, this can also be used just for hot upgrading purposes where a newer version of the application is started and bootstrapping initial state from an existing cluster.
- Data filtering - The Gateway replication can have a custom plug-in that allows users to filter/modify data before it is replicated and after it has been replicated at each source/target spaces.
- Changing replication topology in run-time - adding or removing remote spaces without system shutdown.

{{% info %}}
In the case where each space resides on a different network and there is no network connectivity between all space instances of all the spaces, please refer to [Multi-Site Replication over the WAN](./multi-site-replication-over-the-wan.html) to understand how to establish such replication between different networks.
{{% /info %}}


# Supported Topologies

This page will demonstrate two sample multi-space replication topologies. These are not the only supported topologies. In fact, the permutations of topologies are quite extensive, and we've chosen to demonstrate two of the more common topologies which can also serve as a basis for other topologies as required by the user:

- Multi-master with two spaces, where each space is active and updates its subset of the data.
- Master-slave, where only one space actually updates the data while the rest either serve as a backup or use it in read only mode.

For both of the above topologies, replication is done in a similar way: Each space is replicating the relevant data to its target space(s) via the target space gateway which routes the data to the target space. The data is being replicated asynchronously in a reliable mode, which means that even if a primary space instance fails on the source space, the backup space instance which replaces it will immediately take control and replicate the missing data along with new data that has been generated on the newly elected primary space instance. This is very similar to the [Mirror Service](./asynchronous-persistency-with-the-mirror.html) replication scheme. The gateway is discussed in full below.

![gateway_lan_how_it_works.jpg](/attachment_files/gateway_lan_how_it_works.jpg)
Replication may use Hub & Spoke, Ring, Hierarchical or Pass-Through architecture:
![wan_topologies.jpg](/attachment_files/wan_topologies.jpg)

# Configuring a Space With Gateway Targets

A replication from one space to the another is basically a replication from a space to a target gateway. The source space is decoupled from the target space. Instead, it is configured to replicate to target space's gateway, the gateway is in charge of dispatching the incoming replication packets to the relevant target space. Each space needs to be made aware of the target gateways to which it replicates the data, by specifying the target gateways in the source space configuration.

From the space's perspective, a replication from one space to a target gateway is like any other replication channel (e.g mirror, backup...) and the backlog and confirmation state of the replication channel to the target gateway is kept in the source space. As a result, the gateway is stateless as far as holding the replication state is concerned, and only the source space is in charge of the replication progress and state. Each gateway has a unique logical name, in our cases will demonstrate with city names assuming there is a VPN or LAN between the spaces (e.g. "London" or "New York"). This name is used by remote spaces that are replicating to this space via its gateway.

The following snippet shows how to configure a space that resides in New York to replicate to two other spaces, one in London and one in Hong Kong:


```xml
<os-core:embedded-space id="space" name="myNYSpace" gateway-targets="gatewayTargets"/>

<os-gateway:targets id="gatewayTargets" local-gateway-name="NEWYORK">
  <os-gateway:target name="LONDON"/>
  <os-gateway:target name="HONGKONG"/>
</os-gateway:targets>
```

Each of replication channel to the gateways can be configured with more parameters, such parameters can applied to all gateways or specifically per gateway, for example:


```xml
<os-core:embedded-space id="space" name="myNYSpace" gateway-targets="gatewayTargets"/>

<os-gateway:targets id="gatewayTargets"
  local-gateway-name="NEWYORK" bulk-size="1000"
  max-redo-log-capacity="1000000">
  <os-gateway:target name="LONDON" />
  <os-gateway:target name="HONGKONG" bulk-size="100"/>
</os-gateway:targets>
```

Here we have specified a global bulk size of 1000 but have specifically overridden it in the replication channel to Hong Kong with 100, and have a global maximum redo log capacity for both targets of 1000000.

{{% refer %}}For more details about all the available configuration elements of the space gateway targets please refer to the [Configuring Space Gateway Targets](./configuring-space-gateway-targets.html) section.{{% /refer %}}

{{% vbar %}}
**Use the `partitioned-sync2backup` cluster schema**
You should have the `partitioned-sync2backup` cluster schema used with the space to enable the replication to the Gateway.
If you are not interested in having backups running but have the replication to the Gateway running, you should have ZERO as the number of backups. See below example of an sla.xml configuration you could use in such a case:


```xml
<os-sla:sla cluster-schema="partitioned-sync2backup" number-of-instances="1" number-of-backups="0">
</os-sla:sla>
```

Note that when there are no backups running any failure of the primary might cause a loss of data.
{{% /vbar %}}

# Configuring and Deploying the Gateway

A gateway needs to be deployed as a processing unit per space (though, one gateway processing unit can be used to replicate into more than one space), and it is composed by the sink component which is in charge of dispatching incoming replication from remote spaces into the targeted space.


## Gateway Basic Configuration

Following the above example, here we demonstrate how to configure the gateway processing unit in New York, which needs to send replication to London and Hong Kong and also receive replication from the other spaces.


```xml
<os-gateway:sink id="sink"
  local-gateway-name="NEWYORK"
  local-space-url="jini://*/*/myNYSpace">
  <os-gateway:sources>
    <os-gateway:source name="LONDON" />
    <os-gateway:source name="HONGKONG" />
  </os-gateway:sources>
</os-gateway:sink>
```


## Gateway and the Mirror Service

A gateway and a [Mirror Service](./asynchronous-persistency-with-the-mirror.html) are two different components which can co-exist together without any effect on each other. A gateway is just another reliable asynchronous target. Due to this fact, we will not discuss or demonstrate mirror service along side with a gateway because they do not contradict each other or require any special configuration when used in the same space cluster.

## Gateway and Distributed Transactions

By default the gateway will preserve distributed transactions atomicity (distributed transactions consolidation), this can be disabled by adding the following property to the space configuration:


```xml
<os-core:embedded-space id="space" name="localSpace" gateway-targets="gatewayTargets">
  <os-core:properties>
    <props>
      <prop key="cluster-config.groups.group.repl-policy.processing-type">
        global-order
      </prop>
    </props>
  </os-core:properties>
</os-core:embedded-space>
```

Distributed transaction consolidation is done by waiting for all the transaction participants data before processing is done by the Sink component.
In some cases, certain distributed transaction participants' data might be delayed due to network delay or disconnection and therefore may cause delays in replication.
In order to prevent this potential delay, it is possible to set a timeout parameter which indicates how much time to wait for distributed transactions participants data before processing the data individually for each participant.

It is possible to specify the behavior when processing is split to individual participants upon consolidation failure (timeout or other reasons), the unconsolidated transaction can be either aborted or committed.

Please note that while waiting for the pieces of a distributed transaction to arrive at the sink, replication isn't waiting but keeping the data flow and preventing from conflicts to happen.

The following example demonstrates how to set the timeout for waiting for distributed transaction data to arrive. It is also possible to set the amount of new operations to perform before processing data individually for each participant


```xml
<os-gateway:sink id="sink" local-gateway-name="NEWYORK"
  local-space-url="jini://*/*/myNYSpace">
  <os-gateway:sources>
    <os-gateway:source name="LONDON" />
    <os-gateway:source name="HONGKONG" />
  </os-gateway:sources>
  <os-gateway:tx-support
     dist-tx-wait-timeout-millis="10000"
     dist-tx-wait-for-opers="20"
     dist-tx-consolidation-failure-action="commit"/> <!--or "abort"-->
</os-gateway:sink>
```

Distributed transaction participants data will be processed individually if 10 seconds have passed and not all of the participants data  has arrived or if 20 new operations were executed after the distributed transaction.


|Attribute|Default Value|
|:--------|:------------|
|dist-tx-wait-timeout-millis|60000 milliseconds|
|dist-tx-wait-for-opers|unlimited (-1 = unlimited)|
|dist-tx-consolidation-failure-action|commit|

{{% info %}}
Note that by setting the `cluster-config.groups.group.repl-policy.processing-type` property to `global-source` all reliable asynchronous replication targets for that space will work in non-distributed transaction consolidation mode (For example, a Mirror would be in-non distributed transaction consolidation mode as well.)
{{% /info %}}

{{% note %}}
Consolidation failure can occur under normal circumstances, if the target gateway is restarted or crashed during the consolidation process. In a case where the transaction was successfully consolidated and executed on the target cluster but the gateway was stopped while sending confirmation to the transaction participants in the source space and some of them have received the confirmation while others have not. In such case, the transaction is actually successfully executed in the target space and by default when the consolidation failure event will occur the unconfirmed part will reach the conflict resolution handler which by default will abort it and the state will remain consistent.
{{%/note%}}

{{% note %}}
Due to the above, setting both `dist-tx-wait-timeout-millis` and `dist-tx-wait-for-opers` to unlimited (or very high value) is risky and may cause replication backlog accumulation due to a
packet which is unconsolidated and waits for consolidation which may never occur.
{{%/note%}}

# Master-Slave Topology

With this architecture, we have a master-slave topology where all data is being manipulated in one space, and two other spaces are reading the data but not updating it. In other words, the "other spaces" - the slaves - should not replicate data to the other gateways.

![wan_master_slave.jpg](/attachment_files/wan_master_slave.jpg)

In this case, New York's space will be the active space while London and Hong Kong will be the passive spaces. As explained before, being passive does not necessarily means no work is done in these spaces. However, in terms of replication, these spaces should not replicate to the other spaces and usually should not alter data replicated from other spaces because it may cause conflicts.

Like all GigaSpaces Processing Units, the configuration details of each of the above components is placed in a `pu.xml` file. Here are the contents of the files for each of the components:

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
  xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
    http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
    http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
    http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
    http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
    http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

  <os-core:embedded-space id="space" name="myNYSpace" gateway-targets="gatewayTargets"/>

  <os-gateway:targets id="gatewayTargets" local-gateway-name="NEWYORK">
    <os-gateway:target name="LONDON"/>
    <os-gateway:target name="HONGKONG"/>
  </os-gateway:targets>
</beans>
```

{{% /tab %}}

{{%tab "  London Space "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:os-core="http://www.openspaces.org/schema/core"
  xmlns:os-events="http://www.openspaces.org/schema/events"
  xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
  xmlns:os-sla="http://www.openspaces.org/schema/sla"
  xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
    http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
    http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
    http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
    http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
    http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

  <os-core:embedded-space  id="space" name="myLondonSpace"/>
    <!-- No gateway targets needed as this space
         is not replicating to any gateway-->
</beans>
```

{{% /tab %}}

{{%tab "  London Gateway "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:context="http://www.springframework.org/schema/context"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:os-core="http://www.openspaces.org/schema/core"
  xmlns:os-events="http://www.openspaces.org/schema/events"
  xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
  xmlns:os-sla="http://www.openspaces.org/schema/sla"
  xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
    http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
    http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
    http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
    http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
    http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

  <os-gateway:sink id="sink" local-gateway-name="LONDON"
    local-space-url="jini://*/*/myLondonSpace">
    <os-gateway:sources>
       <os-gateway:source name="NEWYORK" />
    </os-gateway:sources>
  </os-gateway:sink>

</beans>
```

{{% /tab %}}

{{%tab "  Hong Kong Space "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:os-core="http://www.openspaces.org/schema/core"
  xmlns:os-events="http://www.openspaces.org/schema/events"
  xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
  xmlns:os-sla="http://www.openspaces.org/schema/sla"
  xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
    http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
    http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
    http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
    http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
    http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

  <os-core:embedded-space  id="space" name="myHKSpace"/>
    <!-- No gateway targets needed as this space
         is not replicating to any gateway-->
</beans>
```

{{% /tab %}}

{{%tab "  Hong Kong Gateway "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:context="http://www.springframework.org/schema/context"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:os-core="http://www.openspaces.org/schema/core"
  xmlns:os-events="http://www.openspaces.org/schema/events"
  xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
  xmlns:os-sla="http://www.openspaces.org/schema/sla"
  xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
    http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
    http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
    http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
    http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
    http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

  <os-gateway:sink id="sink" local-gateway-name="Hong Kong"
    local-space-url="jini://*/*/myHKSpace">
    <os-gateway:sources>
      <os-gateway:source name="NEWYORK" />
    </os-gateway:sources>
  </os-gateway:sink>
</beans>
```

{{% /tab %}}

{{% /tabs %}}

# Multi-Master Topology

With this architecture, we will have a multi-master topology where data is being generated and manipulated in all spaces.

![wan_multi_master.jpg](/attachment_files/wan_multi_master.jpg)

We will demonstrate this using two spaces but any number of spaces is supported in the same manner. In a master-slave topology, each space should try to modify different subsets of the data as much as possible because many conflicts can occur if multiple spaces are changing the same space entries at the same time. Such conflict can be resolved using a conflict resolver which will be discussed fully at [Multi-Site Conflict Resolution](./multi-site-conflict-resolution.html).

{{% note %}}
This link refers to multi-site replication, where replication is done indirectly via local gateway delegator to the target gateway sink. However the subject in this context is the same for both cases.
{{%/note%}}

With the example below we will have only New York and London as the two active spaces.

Here are the contents of the files for each of the components:

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
        xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

        <os-core:embedded-space  id="space" name="myNYSpace" gateway-targets="gatewayTargets"/>

        <os-gateway:targets id="gatewayTargets" local-gateway-name="NEWYORK">
          <os-gateway:target name="LONDON"/>
        </os-gateway:targets>

</beans>
```

{{% /tab %}}
{{%tab "  New York Gateway "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:os-core="http://www.openspaces.org/schema/core"
	xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-sla="http://www.openspaces.org/schema/sla"
        xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

        <os-gateway:sink id="sink" local-gateway-name="NEWYORK"
           local-space-url="jini://*/*/myNYSpace">
          <os-gateway:sources>
            <os-gateway:source name="LONDON" />
          </os-gateway:sources>
        </os-gateway:sink>

</beans>
```

{{% /tab %}}
{{%tab "  London Space "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:os-core="http://www.openspaces.org/schema/core"
	xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-sla="http://www.openspaces.org/schema/sla"
        xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

        <os-core:embedded-space  id="space" name="myLondonSpace"
          gateway-targets="gatewayTargets"/>

        <os-gateway:targets id="gatewayTargets" local-gateway-name="LONDON">
          <os-gateway:target name="NEWYORK"/>
        </os-gateway:targets>

</beans>
```

{{% /tab %}}
{{%tab "  London Gateway "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:os-core="http://www.openspaces.org/schema/core"
	xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-sla="http://www.openspaces.org/schema/sla"
        xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

        <os-gateway:sink id="sink" local-gateway-name="LONDON"
          local-space-url="jini://*/*/myLondonSpace">
          <os-gateway:sources>
            <os-gateway:source name="NEWYORK" />
          </os-gateway:sources>
        </os-gateway:sink>

</beans>
```

{{% /tab %}}
{{%tab "  Symmetric Gateway Config "%}}
In this example, the gateway `pu.xml` is quite symmetric, the only difference is the local gateway name and the target gateway name. In such cases, it may be more convenient to create a single gateway `pu.xml` and use place holders to override the relevant properties at deploy time by injecting values for these properties:


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:os-core="http://www.openspaces.org/schema/core"
	xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-sla="http://www.openspaces.org/schema/sla"
        xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

        <bean id="propertiesConfigurer"
          class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"/>

        <os-gateway:sink id="sink"
          local-gateway-name="${localGatewayName}"
          local-space-url="${localSpaceUrl}">
          <os-gateway:sources>
            <os-gateway:source name="NEWYORK" />
            <os-gateway:source name="LONDON" />
          </os-gateway:sources>
        </os-gateway:sink>

</beans>
```

In the above we have configured both LONDON and NEWYORK at the sources of the sink, the sink will filter a gateway target and source if they match their local name. Using the above technique may simplify scenarios which are symmetric but it is not recommended when the scenarios are **not** symmetric as it can be unnecessarily confusing.
{{% /tab %}}
{{% /tabs %}}

# Filtering Replication Between Gateways

In some cases, there can be data that should not be replicated between the spaces but should still be replicated locally to the backup or a mirror service. Hence, specifying the object is not replicated does not fit. Since a replication channel to a gateway is like any other replication channel, a custom [Replication Filter]({{%currentadmurl%}}/cluster-replication-filters.html) at the source space can be used to filter the relevant data from being sent to the target gateway. This filtering should be based on the replication target name in order to identify that the replication filter is called for the correct outgoing replication to the gateway.


{{% refer %}}For full details and example please refer to [Replication Gateway Filtering](./replication-gateway-filtering.html){{% /refer %}}

{{% note %}}
This link refers to multi-site replication, where replication is done indirectly via local gateway delegator to the target gateway sink. However the subject in this context is the same for both cases.
{{%/note%}}

# Bootstrap One Space From Another Space

Bootstrapping a space from another space is a process in which one space is starting fresh and it is being populated with the data of another space. This can be useful after a very long disconnection where the replication redo-log in the source spaces that replicates to this space was dropped due to breaching capacity limitations, and the disconnected space should start fresh. Other reasons may be an explicit planned downtime due-to some maintenance of one space which lead to a complete system bootstrap once restarted.
Or bootstrapping a new version of an application where a gateway target is added dynamically to an existing space, and the new space will bootstrap from the existing one and once finished traffic is diverted to the new space.

{{% refer %}}For full details of how to enable the bootstrap mechanism refer to [Replication Gateway Bootstrapping Process](./replication-gateway-lan-bootstrapping-process.html){{% /refer %}}

# Changing Replication Topology During Runtime

The topology might change during runtime, for instance a new space can be added and the existing spaces should be familiar with it and start replicating to it and receive replication from it. On the other hand a space can be removed and the existing should stop holding replication backlog for it and drop it from their list of gateway targets.

{{% refer %}}For full details of how to add and remove gateway targets during runtime refer to [Changing Multi-Site Deployment during Runtime](./changing-multi-site-deployment-during-runtime.html){{% /refer %}}


{{% note %}}
This link refers to multi-site replication, where replication is done indirectly via local gateway delegator to the target gateway sink. However the subject in this context is the same for both cases.
{{%/note%}}
