---
type: post100
title:  Changing Deployment during Runtime
categories: XAP100
parent: multi-site-replication-overview.html
weight: 600
canonical: auto
---




The topology might change during runtime, for instance a new site can be added and the existing sites should be familiar with it and start replicating to it and receive replication from it. On the other hand a site can be removed and the existing should stop holding replication backlog for it and drop it from their list of gateway targets.

# The Process of Adding or Removing Sites

Adding or removing a site without down time is done in three stages.

1. If adding a new site, it is better to deploy it first (Both Gateway PU and Space) fully configured to interact with the other sites as required by the topology, if removing an existing site then it is better to undeploy it as the last stage to avoid backlog accumulation at the remaining sites until the site is properly removed from them. That being said, this is just a recommendation and the order of this stages is not mandatory and the system will manage any order gracefully.
1. The relevant Gateway PUs of the existing sites needs to be updated with the new setting, this is done by updating the corresponding `pu.xml` to reflect the new deployment sites state, either adding or removing a site and including all the required lookup attributes. Since a Gateway pu is stateless, they can be undeployed and redeployed with the new updated `pu.xml` configuration during runtime without affecting the spaces state, you will observe a disconnection between the sites which communicate through this gateway but it will be restored once the gateway PU is redeployed with the updated configuration.
1. Updating the relevant existing space (or spaces) with the new gateway targets state. Once again this is done by updating the corresponding `pu.xml` (in GSMs deploy folder) space with the added or removed site. However, a space is stateful, hence simply redeploying it with the updated configuration will cause downtime. Therefore the admin provides an API for adding or removing a Gateway target to and from a Space, it should be used as follows:


```java

//Adding a gateway target
Admin admin = // obtain an admin.
...
GatewayTarget gatewayTarget = new GatewayTarget("new site name");
//Apply specific configuration if needed, for each property
//which is not set the default gateway target configuration
//that were specified in the space pu.xml will be inherited.
gatewayTarget.setBulkSize(500);

Space space = admin.getSpaces().waitFor("mySpace");
space.getReplicationManager().addGatewayTarget(gatewayTarget);
...
//Removing a gateway target
space.getReplicationManager().removeGatewayTarget("removed site name");
```

Both methods will block until the configuration change is propagated to the entire space instances of the relevant space.
