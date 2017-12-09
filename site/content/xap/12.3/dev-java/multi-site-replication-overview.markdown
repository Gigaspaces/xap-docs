---
type: post123
title:  Multi-Site WAN Replication
categories: XAP123, ENT
parent: none
weight: 2400
---


Multiple site replication is the ability to replicate state between different deployed Spaces, where each Space can be physically located in a different geographical location (also termed a different deployment site).

Multiple site replication is a very common deployment topology in the following scenarios:

- **Disaster recovery planning** - In such cases, each deployment site is located far from the other sites (for example, on a different continent) so that if one site is completely destroyed or decommissioned, other sites are not affected and can continue to operate normally.
- **Failover** - When one site acts as a failover over target for another.
- **Maintaining data locality** - Per site for performance and latency reasons. For example, a global trading application that operates in multiple stock exchanges across the globe needs fast access to **Global Reference Data**, or an application that's deployed on multiple data centers in the cloud with a need to access the **User Profile Data** very quickly.

{{% align center%}}
![wan_use_cases.jpg](/attachment_files/wan_use_cases.jpg)
{{% /align%}}

## WAN Gateway Features

The XAP WAN Gateway features the following:

- Optimized bandwidth and WAN connection usage - With the XAP WAN Gateway, every site uses one connection when interacting with remote sites.
- Total data consistency, ordering with no data lost - Due to its unique architecture where the WAN Gateway is totally stateless, no data loss happens. Transactions at the local site are replicated atomically in the correct order to the remote sites.
- Conflict resolution support - XAP can identify data conflicts and allow you to decide to override the local copy with the remote replicated copy, reject the remote replicated copy, or merge the local and the remote copy.
- Monitoring and Management - The WAN Gateway exposes statistics on every activity going thought the replication to remote sites.
- Indirect replication - The WAN Gateway can replicate a packet from point A to point B via point C, to avoid connectivity problems between points A and B.
- Multi-Master / Master-Slave support - The WAN Gateway supports all popular replication typologies.
- Site bootstrapping - After a site starts, it can reload its entire data or specific data from a remote site without introducing data consistency problems.
- Data filtering - The WAN Gateway replication can have a custom plug-in that allows users to filter/modify data before and after it has been replicated, at each source/target node.
- Changing WAN replication topology during runtime - adding or removing remote sites without system shutdown.

{{% info "Info"%}}
This page describes how to establish replication between multiple Spaces in a typical WAN environment. Each Space is a separate network and there is a need for a designated outbound and inbound gateway machine or machines on each network, in order to interact with the other network(s). 
{{% /info %}}


# Supported Toplogies

This page describes two sample multi-site replication topologies. These are not the only supported topologies. In fact, the permutations of topologies are quite extensive, and discuss two of the more common topologies, which can also serve as a basis for other topologies as required by the user:

- Multi-master with two sites, where each site is active and updates its subset of the data.
- Master-slave, where only one site actually updates the data while the rest either serve as a backup or use it in read only mode.

For both of the above topologies, replication is done in in a similar way. Each Space replicates the relevant data to its target Space(s) via a local gateway, which routes the data to the gateway of the target Space(s) and from there to the target Space. The data is replicated asynchronously in a reliable mode, which means that even if a primary Space instance fails on the source site, the backup Space instance that replaces it will immediately take control and replicate the missing data along with new data that has been generated on the newly elected primary Space instance. This is very similar to the [Mirror Service](./asynchronous-persistency-with-the-mirror.html) replication scheme. The gateway is discussed in full below.

{{% align center%}}
![wan_how_it_works.jpg](/attachment_files/wan_how_it_works.jpg)
{{% /align%}}

Replication can use Hub & Spoke, Ring, Hierarchical or Pass-Through architecture:

{{% align center%}}
![wan_topologies.jpg](/attachment_files/wan_topologies.jpg)
{{% /align%}}

# Configuring a Space With Gateway Targets

A replication from one Space to another is basically a replication from a Space to a target gateway. The source Space is decoupled from the target Space. Instead, it is configured to replicate to the target Space's local gateway; the local gateway is in charge of dispatching the incoming replication packets to the relevant target Space. Each Space needs to be made aware of the target gateways to which it replicates the data, by specifying the target gateways in the source Space configuration.

From the Space's perspective, a replication from one Space to a target gateway is like any other replication channel (such as mirror or backup), and the backlog and confirmation state of the replication channel to the target gateway is kept in the source Space. As a result, the gateway is stateless as far as holding the replication state is concerned, and only the source Space is in charge of the replication progress and state. Each gateway has a unique logical name that usually corresponds to the physical site name (for example, "London" or "New York"). This name is used by remote Spaces that replicate to this Space via its local gateway.

The following snippet shows how to configure a Space that resides in New York to replicate to two other Spaces, one in London and one in Hong Kong:


```xml
<os-core:embedded-space id="space" space-name="myNYSpace" gateway-targets="gatewayTargets"/>

<os-gateway:targets id="gatewayTargets" local-gateway-name="NEWYORK">
  <os-gateway:target name="LONDON"/>
  <os-gateway:target name="HONGKONG"/>
</os-gateway:targets>
```

Each replication channel to the gateways can be configured with more parameters, and these parameters can applied to all gateways or specifically per gateway, for example:


```xml
<os-core:embedded-space id="space" space-name="myNYSpace" gateway-targets="gatewayTargets"/>

<os-gateway:targets id="gatewayTargets"
  local-gateway-name="NEWYORK" bulk-size="1000"
  max-redo-log-capacity="1000000">
  <os-gateway:target name="LONDON" />
  <os-gateway:target name="HONGKONG" bulk-size="100"/>
</os-gateway:targets>
```

Here, we specified a global bulk size of 1000 but have specifically overridden it in the replication channel to Hong Kong with 100, and have a global maximum redo log capacity for both targets of 1000000.

{{% refer %}}For more details about all the available configuration elements of the space gateway targets,  refer to [Configuring Space Gateway Targets](./configuring-space-gateway-targets.html).{{% /refer %}}

{{% vbar %}}
**Use the `partitioned` cluster schema**

You should have the `partitioned` cluster schema used with the Space to enable replication to the gateway. If you are not interested in having backups running but have the replication to the gateway running, you should have ZERO as the number of backups. See the following example of an sla.xml configuration you can use in such a case:


```java
<os-sla:sla cluster-schema="partitioned" number-of-instances="1" number-of-backups="0">
</os-sla:sla>
```

When there are no backups, running any failure of the primary might cause a loss of data.
{{% /vbar %}}

# Configuring and Deploying the Gateway

A gateway needs to be deployed locally as a Processing Unit in each site, and is composed of two different components; the delegator and the sink. The delegator is in charge of delegating outgoing replication from the local site to a remote gateway, and the sink is in charge of dispatching incoming replication from remote sites into the local Space.

{{% align center%}}
![wan_gatway_archi.jpg](/attachment_files/wan_gatway_archi.jpg)
{{% /align%}}

## Gateway Lookup

The different gateway components need to locate each other across the different sites. For example, a delegator needs to locate the sink of the target gateway to which it delegates the replication. This lookup is accomplished via dedicated lookup services that the gateways use to register and locate each other. This lookup process is usually done across the WAN, so the most reasonable way of locating the lookup services is using unicast (multicast is also supported). The following example demonstrates a unicast lookup discovery.

## Gateway Basic Configuration

Following the above example, here we demonstrate how to configure the local gateway processing unit in New York, which needs to send replication to London and Hong Kong and also receive replication from these two sites.


```xml
<os-gateway:delegator id="delegator"
  local-gateway-name="NEWYORK" gateway-lookups="gatewayLookups">
  <os-gateway:delegations>
    <os-gateway:delegation target="LONDON"/>
    <os-gateway:delegation target="HONGKONG"/>
  </os-gateway:delegations>
</os-gateway:delegator>

<os-gateway:sink id="sink"
  local-gateway-name="NEWYORK"
  gateway-lookups="gatewayLookups"
  local-space-url="jini://*/*/myNYSpace">
  <os-gateway:sources>
    <os-gateway:source name="LONDON" />
    <os-gateway:source name="HONGKONG" />
  </os-gateway:sources>
</os-gateway:sink>

<os-gateway:lookups id="gatewayLookups">
  <os-gateway:lookup gateway-name="NEWYORK"
    host="ny-gateway-host-machine" discovery-port="10001"
    communication-port="7000" />
  <os-gateway:lookup gateway-name="LONDON"
    host="london-gateway-host-machine" discovery-port="10002"
    communication-port="8000" />
  <os-gateway:lookup gateway-name="HONGKONG"
    host="hk-gateway-host-machine" discovery-port="10003"
    communication-port="9000" />
</os-gateway:lookups>
<!--The above ports and host names are randomly selected and
have no meaning, all sites could designate the same ports as well-->
```

In the above example, we see that both the sink and the delegator need a reference to the gateway lookup configuration, because both components are using this configuration to locate the relevant component or to register themselves. They use their local gateway name to identify themselves to the lookup configuration, where they should be registered and where they should look for their targets.

The delegator and sink components are actually isolated and can even be deployed in separate Processing Units, but the most simple deployment is to bundle them two together. However, in some cases you may want to separate them across two or more machines due to system load or other reasons.

{{% refer %}}For full details and available configurations,  refer to [Replication Gateway Components](./replication-gateway-components.html).{{% /refer %}}

## Gateway and the Mirror Service

A gateway and a [Mirror Service](./asynchronous-persistency-with-the-mirror.html) are two different components, which can co-exist together without affecting each other. A gateway is just another reliable asynchronous target. As such, there is no need to demonstrate the mirror service alongside a gateway because they don't conflict with each other or require any special configuration when used in the same Space cluster.

## Gateway and Distributed Transactions

By default, the gateway preserves the atomicity of distributed transactions (distributed transaction consolidation). This can be disabled by adding the following property to the Space configuration:


```xml
<os-core:embedded-space id="space" space-name="localSpace" gateway-targets="gatewayTargets">
  <os-core:properties>
    <props>
      <prop key="cluster-config.groups.group.repl-policy.processing-type">
        global-order
      </prop>
    </props>
  </os-core:properties>
</os-core:embedded-space>
```

Distributed transaction consolidation is done by waiting for data from all the transaction participants before processing is done by the sink component. In some cases, the data from certain distributed transaction participants may be delayed due to network delay or disconnection, and this can cause delays in replication.
In order to prevent this potential delay, you can set a timeout parameter that indicates how much time to wait for distributed transaction participant data before processing the data individually for each participant.

You can specify the behavior when processing is split into individual participants upon consolidation failure (timeout or other reasons); the unconsolidated transaction can be either aborted or committed.

While waiting for the pieces of a distributed transaction to arrive at the sink, replication isn't waiting but is keeping the data flow and preventing conflicts from happening.

The following example shows how to set the timeout for waiting for distributed transaction data to arrive. You can also set the amount of new operations to perform before processing data individually for each participant.


```xml
<os-gateway:sink id="sink" local-gateway-name="NEWYORK"
  gateway-lookups="gatewayLookups"
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

Distributed transaction participant data will be processed individually if 10 seconds have passed and not all of the participant data has arrived. or if 20 new operations were executed after the distributed transaction.


|Attribute|Default Value|
|:--------|:------------|
|dist-tx-wait-timeout-millis|60000 milliseconds|
|dist-tx-wait-for-opers|unlimited (-1 = unlimited)|
|dist-tx-consolidation-failure-action|commit|

{{% info "Info"%}}
If you set the `cluster-config.groups.group.repl-policy.processing-type` property to `global-source`, all reliable asynchronous replication targets for that Space will work in non-distributed transaction consolidation mode. For example, a Mirror will also work in non-distributed transaction consolidation mode.
{{% /info %}}

{{% note "Notes"%}}
Consolidation failure can occur under normal circumstances, if the target gateway is restarted or crashes during the consolidation process. For example, you may have a scenario where the transaction was successfully consolidated and executed on the target cluster, but the gateway was stopped while sending confirmation to the transaction participants in the source site and some of them received the confirmation while others did not. In this case, the transaction is actually successfully executed in the target site, and by default when the consolidation failure event occurs, the unconfirmed part reaches the conflict resolution handler will abort it (by default), and the state will remain consistent.

Setting both `dist-tx-wait-timeout-millis` and `dist-tx-wait-for-opers` to unlimited (or to very high value) is risky. The replication backlog may accumulate due to a packet that is unconsolidated, causing the replication process to wait for consolidation that may never occur.
{{%/note%}}

# Master-Slave Topology

With this architecture, we have a master-slave topology where all data is manipulated in one site, and two other sites read the data but don't update it. In other words, the "other sites" - the slaves - should not replicate data to the other gateways.

{{% align center%}}
![wan_master_slave.jpg](/attachment_files/wan_master_slave.jpg)
{{% /align%}}

In this case, New York's site will be the active site while London and Hong Kong will be the passive sites. As explained before, being passive does not necessarily mean no work is done in these sites. However, in terms of replication over the WAN, these sites should not replicate to the other sites and usually should not alter data replicated from other sites, because it may cause conflicts.

Like all XAP Processing Units, the configuration details of each of the above components is placed in a `pu.xml` file. These are the contents of the file for each component:

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

  <os-core:embedded-space id="space" space-name="myNYSpace" gateway-targets="gatewayTargets"/>

  <os-gateway:targets id="gatewayTargets" local-gateway-name="NEWYORK">
    <os-gateway:target name="LONDON"/>
    <os-gateway:target name="HONGKONG"/>
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
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
    http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
    http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
    http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
    http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
    http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

  <os-gateway:delegator id="delegator"
    local-gateway-name="NEWYORK" gateway-lookups="gatewayLookups">
    <os-gateway:delegations>
      <os-gateway:delegation target="LONDON"/>
      <os-gateway:delegation target="HONGKONG"/>
    </os-gateway:delegations>
  </os-gateway:delegator>

  <!--No sink needed as this site is not receiving replication from any gateway-->
  <os-gateway:lookups id="gatewayLookups">
    <os-gateway:lookup gateway-name="NEWYORK" host="ny-gateway-host-machine"
      discovery-port="10001" communication-port="7000" />
    <os-gateway:lookup gateway-name="LONDON" host="london-gateway-host-machine"
      discovery-port="10002" communication-port="8000" />
    <os-gateway:lookup gateway-name="HONGKONG" host="hk-gateway-host-machine"
      discovery-port="10003" communication-port="9000" />
  </os-gateway:lookups>
  <!-- The above ports and host names are randomly selected and has no meaning,
       all sites could designate the same ports as well-->
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

  <os-core:embedded-space id="space" space-name="myLondonSpace/">
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
  <!--No delegator needed as this site is not replicating to any gateway-->

  <os-gateway:sink id="sink" local-gateway-name="LONDON"
    gateway-lookups="gatewayLookups" local-space-url="jini://*/*/myLondonSpace">
    <os-gateway:sources>
       <os-gateway:source name="NEWYORK" />
    </os-gateway:sources>
  </os-gateway:sink>

  <os-gateway:lookups id="gatewayLookups">
     <os-gateway:lookup gateway-name="LONDON"
       host="london-gateway-host-machine" discovery-port="10002"
       communication-port="8000" />
  </os-gateway:lookups>
  <!-- Only the lookup parameters of this site is needed since
       the sink will only register itself in the lookup service and no delegator
       exists so there is no need to find a remote gateway -->
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


  <os-core:embedded-space id="space" space-name="myHKSpace"/>
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

  <!-- No delegator needed as this site is not
       replicating to any gateway-->

  <os-gateway:sink id="sink" local-gateway-name="HONGKONG"
    gateway-lookups="gatewayLookups"
    local-space-url="jini://*/*/myHKSpace">
    <os-gateway:sources>
      <os-gateway:source name="NEWYORK" />
    </os-gateway:sources>
  </os-gateway:sink>

  <os-gateway:lookups id="gatewayLookups">
    <os-gateway:lookup gateway-name="HONGKONG"
      host="HK-gateway-host-machine" discovery-port="10003"
      communication-port="9000" />
    </os-gateway:lookups>
    <!-- Only the lookup parameters of this site is
         needed since the sink will only register itself in
         the lookup service and no delegator
         exists so there is no need to find a remote gateway -->

</beans>
```

{{% /tab %}}
{{% /tabs %}}

# Multi-Master Topology

With this architecture, we have a multi-master topology where data is generated and manipulated in all sites.

{{% align center%}}
![wan_multi_master.jpg](/attachment_files/wan_multi_master.jpg)
{{% /align%}}

The example below describes two sites, but any number of sites is supported in the same manner. In a master-slave topology, each site should try to modify different subsets of the data as much as possible, because many conflicts can occur if multiple sites change the same Space entries at the same time. These conflicts can be resolved using a conflict resolver, which is described in detail in the [Multi-Site Conflict Resolution](./multi-site-conflict-resolution.html) topic.

In the example below, New York and London are the two active sites.

Here are the contents of the file for each component:

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

        <os-core:embedded-space id="space" space-name="myNYSpace" gateway-targets="gatewayTargets"/>

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

        <os-gateway:delegator id="delegator"
          local-gateway-name="NEWYORK" gateway-lookups="gatewayLookups">
          <os-gateway:delegations>
            <os-gateway:delegation target="LONDON"/>
          </os-gateway:delegations>
        </os-gateway:delegator>

        <os-gateway:sink id="sink" local-gateway-name="NEWYORK"
           gateway-lookups="gatewayLookups" local-space-url="jini://*/*/myNYSpace">
          <os-gateway:sources>
            <os-gateway:source name="LONDON" />
          </os-gateway:sources>
        </os-gateway:sink>

        <os-gateway:lookups id="gatewayLookups">
          <os-gateway:lookup gateway-name="NEWYORK"
          host="ny-gateway-host-machine" discovery-port="10001"
          communication-port="7000" />
          <os-gateway:lookup gateway-name="LONDON"
            host="london-gateway-host-machine" discovery-port="10002"
            communication-port="8000" />
        </os-gateway:lookups>
        <!--The above ports and host names are randomly selected
            and have no meaning,
            all sites could designate the same ports as well-->

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

        <os-core:embedded-space id="space" space-name="myLondonSpace"
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

        <os-gateway:delegator id="delegator" local-gateway-name="LONDON"
          gateway-lookups="gatewayLookups">
          <os-gateway:delegations>
            <os-gateway:delegation target="NEWYORK"/>
          </os-gateway:delegations>
        </os-gateway:delegator>

        <os-gateway:sink id="sink" local-gateway-name="LONDON"
          gateway-lookups="gatewayLookups" local-space-url="jini://*/*/myLondonSpace">
          <os-gateway:sources>
            <os-gateway:source name="NEWYORK" />
          </os-gateway:sources>
        </os-gateway:sink>

        <os-gateway:lookups id="gatewayLookups">
          <os-gateway:lookup gateway-name="NEWYORK"
            host="ny-gateway-host-machine" discovery-port="10001"
            communication-port="7000" />
          <os-gateway:lookup gateway-name="LONDON"
            host="london-gateway-host-machine" discovery-port="10002"
            communication-port="8000" />
        </os-gateway:lookups>
        <!--The above ports and host names are randomly selected and have no meaning,
            all sites could designate the same ports as well-->

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

        <os-gateway:delegator id="delegator"
          local-gateway-name="${localGatewayName}" gateway-lookups="gatewayLookups">
          <os-gateway:delegations>
            <os-gateway:delegation target="NEWYORK"/>
            <os-gateway:delegation target="LONDON"/>
          </os-gateway:delegations>
        </os-gateway:delegator>

        <os-gateway:sink id="sink"
          local-gateway-name="${localGatewayName}"
          gateway-lookups="gatewayLookups" local-space-url="${localSpaceUrl}">
          <os-gateway:sources>
            <os-gateway:source name="NEWYORK" />
            <os-gateway:source name="LONDON" />
          </os-gateway:sources>
        </os-gateway:sink>

        <os-gateway:lookups id="gatewayLookups">
          <os-gateway:lookup gateway-name="NEWYORK"
            host="ny-gateway-host-machine" discovery-port="10001"
            communication-port="7000" />
          <os-gateway:lookup gateway-name="LONDON"
            host="london-gateway-host-machine" discovery-port="10002"
            communication-port="8000" />
        </os-gateway:lookups>
        <!--The above ports and host names are randomly selected and have no meaning,
            all sites could designate the same ports as well-->

</beans>
```

In the above example, we both LONDON and NEWYORK are configured at the sources of the sink and as delegation targets. The delegator and the sink filter a gateway target and source if they match their local name. Using the above technique may simplify scenarios that are symmetric, but it is not recommended when the scenarios are **not** symmetric as it can be unnecessarily confusing.
{{% /tab %}}
{{% /tabs %}}

{{% refer %}}
The [Multi-Master running example](/sbp/wan-replication-gateway.html) topic includes a three-way setup that replicates data between three sites.
{{% /refer %}}

# Filtering Replication Between Gateways

On occasion, there may be data that should not be replicated between sites but should still be replicated locally to the backup or a mirror service. If this is the case, it isn't suitable to specify that the object should not be replicated. A replication channel to a gateway is like any other replication channel, so a custom [Replication Filter]({{%currentadmurl%}}/cluster-replication-filters.html) at the source Space can be used to filter the relevant data from being sent to the target gateway. This filtering should be based on the replication target name, in order to identify that the replication filter is called for the correct outgoing replication to the gateway.

{{% refer %}}For full details and an example, refer to [Replication Gateway Filtering](./replication-gateway-filtering.html).{{% /refer %}}

# Bootstrapping a Site From Another Site

To bootstrap a site from another site is to restart a site Space from scratch, and to populate it with data from another site Space. This can be useful after a very long disconnection where the replication redo-log in the source Spaces that replicate to this site was dropped due to breaching capacity limitations, and the disconnected site should therefore start fresh. Other reasons include an explicit planned downtime due to site maintenance that leads to a complete system bootstrap upon restart.

{{% refer %}}For more informaion on how to enable the bootstrap mechanism, refer to [Replication Gateway Bootstrapping Process](./replication-gateway-bootstrapping-process.html).{{% /refer %}}

# Changing the Site Topology During Runtime

You may have to modify your site topology during runtime. For example, a new site may be added and the existing sites may have to start replicating to it and receive replication from it. Alternatively, a site may be removed and the existing sites should stop holding replication backlogs for the removed site, and delete it from their list of gateway targets.

{{% refer %}}For more information on how to add and remove sites during runtime, refer to [Changing Multi-Site Deployment during Runtime](./changing-multi-site-deployment-during-runtime.html).{{% /refer %}}

# Additional Resources

The following pages in this section provide more details on the Multi-Site Replication module:

- [Replication Throughput Capacity](/sbp/wan-replication-gateway.html#Replication Throughput Capacity)
- [WAN Gateway Replication Benchmark](/sbp/wan-replication-gateway.html#WAN Gateway Replication Benchmark)

You can also view the following video:

{{%youtube "V7rbbmWo3JU"  "Multi-Site Deployment"%}}








