---
type: post140
title:  Gateway Bootstrapping
categories: XAP140, ENT
parent: multi-space-replication-overview.html
weight: 200
---

 



Bootstrapping a space from another space is a process in which one space is starting fresh and it is being populated with the data of another space. This can be useful after a very long disconnection where the replication redo-log in the source spaces that replicates to this space was dropped due to breaching capacity limitations, and the disconnected space should start fresh. Or some initiated downtime due to some maintenance of one space which should bootstrap once it is restarted. Another scenario would be an application upgrade where a newer version of the application is started in a new space and it needs to bootstrap its initial state from the existing application space.

# How It Works

A bootstrap of a space is being done via a gateway sink of that space and the gateway sink of the space that the bootstrap should be done from. Therefore, both space must have a deployed gateway sink.

The bootstrap target space gateway sink will locate the gateway sink of the bootstrap source space and it will use the bootstrap source space gateway sink in order to initiate a recovery process from each partition primary space instance. All the data that will be received during the bootstrap process into the sink will be inserted to the local space.

All the changes that are done in the bootstrap source space during this copy process are accumulated at the replication redo-log and will be sent to the bootstrap target gateway sink once the copy stage is complete. During the bootstrap copy stage, the gateway sink which is being bootstrapped will not be available for replication, and all the remote spaces will have a disconnected replication channel to that gateway sink.

{{% align center%}}
![gateway-bootstrap-copy.jpg](/attachment_files/gateway-bootstrap-copy.jpg)
{{% /align %}}

1. NY Sink initiates bootstrap process from the London Sink
1. The London Sink start reading data from the local London cluster
1. The London sink sends the data to the NY Sink
1. The NY sink pushes it to the local NY cluster

{{% align center%}}
![gateway-bootstrap-sync.jpg](/attachment_files/gateway-bootstrap-sync.jpg)
{{% /align%}}

# Initiating a Bootstrap

Unlike regular space recovery, where a backup or replicated space performs a recovery, a bootstrap has much more implications and therefore it is an administrative command executed by the user on demand.

The bootstrap is initiated on the gateway sink of the space that needs to be bootstrapped for another space. The following conditions should be held before initiating a bootstrap:

1. The space that is being bootstrapped should be first cleared of all data, a recommended way to do so is by redeploying that space (any entry which is not cleared will not be updated during the copy stage of the bootstrap process, it will be skipped).
1. The space that is being bootstrapped should not be active during the bootstrap copy stage, which means it should not update the incoming data during the bootstrap stage. It is up to the user to make sure this condition is held.
1. At the bootstrap target there should be a gateway sink configured with the bootstrap source gateway as one of its sources.
1. At the bootstrap source there should be a gateway sink (no need to configure the bootstrap target as a source if it is not supposed to replicate into the source space).
1. The gateway sink at the bootstrapping gateway needs to be configured with `requires-bootstrap=true`, it should look as follows:


```xml
<os-gateway:sink id="sink" local-gateway-name="NEWYORK"
  local-space-url="jini://*/*/myNYSpace" requires-bootstrap="true">
  <os-gateway:sources>
    <os-gateway:source name="LONDON" />
    <os-gateway:source name="HONGKONG" />
  </os-gateway:sources>
</os-gateway:sink>
```

After meting the condition specified in the previous, the bootstrap should be initiated using the [Admin API](./administration-and-monitoring-overview.html).
Following is an example of how to bootstrap New-York from London:


```java
//Create an admin to the local environment
Admin admin = new AdminFactory().create();
Gateway newyorkGateway = admin.getGateways().waitFor("NEWYORK");
GatewaySinkSource londonSinkSource = newyorkGateway.waitForSinkSource("LONDON");
BootstrapResult bootstrapResult = londonSinkSource.bootstrapFromGatewayAndWait, 3600, TimeUnit.SECONDS);
```

The bootstrap method will block until the bootstrap is completed and the result will specify whether the bootstrap completed successfully or some error occurred, such as timeout.

When a gateway sink is started with `requires-bootstrap` state, it will not be open for incoming replication until a bootstrap was initiated, which means remote spaces incoming replication channels will be disconnected. It is possible to enable incoming replication to a gateway sink in that state without initiating a bootstrap by calling the `enableIncomingReplication`. For example:


```java
//Create an admin to the local environment
Admin admin = new AdminFactory().create();
Gateway newyorkGateway = admin.getGateways().waitFor("NEWYORK");
GatewaySink sink = newyorkGateway.waitForSink("LONDON");
sink.enableIncomingReplication();
```

Once a gateway sink has executed a bootstrap process or the `enableIncomingReplication` was called, it cannot execute a bootstrap process again because it is already open for incoming replication.

The bootstrap process requires the sink to be at a disabled state to prevent from ongoing replication interfering with the bootstrap first copy stage. Therefor a sink must be restarted for bootstrap if the above occurred.
