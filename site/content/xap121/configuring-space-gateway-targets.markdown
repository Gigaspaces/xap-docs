---
type: post121
title:  Configuring Targets
categories: XAP121, ENT
parent: multi-site-replication-overview.html
weight: 300
---





Each space that is replicating to another space (or spaces) is actually replicating to the local gateway of the target space, and that gateway is in charge of dispatching the replication to the relevant partitions. Such replicating space needs be configured with the name of each of the target gateways and replication related parameters per gateway or for all gateways.

Here is an example of how this configuration should look:


```xml
 <os-core:embedded-space id="space" space-name="myNYSpace" gateway-targets="gatewayTargets"/>

<os-gateway:targets id="gatewayTargets" local-gateway-name="NEWYORK"
  idle-time-threshold="3000" bulk-size="1000" max-redo-log-capacity="1000000">
  <os-gateway:target name="LONDON" />
  <os-gateway:target name="HONGKONG" bulk-size="100"/>
</os-gateway:targets>
```

Each configuration can be configured for all gateways or specifically per each gateway as seen in the above example, max-redo-log-capacity is configured for all gateways while bulk-size is specifically overridden in the configuration of HONGKONG gateway target. A recommended reading regarding the replication redo-log is [Controlling the Replication Redo Log]({{%currentadmurl%}}/controlling-the-replication-redo-log.html).

# Configurable Parameters


|Property|Description|Default|
|:-------|:----------|:------|
|bulk-size|Specifies the size of each replication bulk in terms of replication packets| 100 packets |
|pending-operation-threshold|Specifies the threshold of number of packets that are pending replication that once breached, a replication bulk will be transmitted | 100 packets |
|idle-time-threshold|Specifies the maximum time to wait since the last time a replication bulk was transmitted, once elapsed, a replication bulk will be transmitted even if the `pending-operation-threshold` is not reached| 1000 milliseconds |
|max-redo-log-capacity|Specifies the maximum number of packets that should be held in the redo-log for a replication gateway (-1 means unlimited) | 100,000,000 |
|<nobr>on-redo-log-capacity-exceeded</nobr>| `drop-oldest` will result in dropping the oldest packet in the redo-log once the capacity is exceeded, `block-operations` will result in blocking all new replicated operations by denying such new operation by throwing an exception to the operation invoker. | `drop-oldest` |

If one of the gateway targets name matches the local-gateway-name, it will be filtered and removed from the list at deploy time. This may be helpful for creating symmetric configuration which is demonstrated at [Multi-Site Replication (WAN)](./multi-site-replication-over-the-wan.html) page.
