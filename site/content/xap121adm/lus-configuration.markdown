---
type: post121
title:  Global vs. Local LUS
categories: XAP121ADM, PRM
parent: runtime-configuration.html
weight: 700
---

{{%ssummary%}}{{%/ssummary%}}

{{%anchor global-lus%}}

# Global LUS

When you would like to have a fully dynamic elastic environment where the lookup service (LUS) may be running on **any machine** on the network to serve multiple LUS machine failure (for existing machines or to be started in the future) you should consider using the **global** LUS setup.

With the global LUS setup you should rely on multicast lookup discovery mechanism to locate running lookup services. This discovery process executed by the service grid components (GSC , GSM) - configured via the `XAP_LOOKUP_GROUPS` environment variable) or client applications when constructing their data grid proxy - configured via the `groups` URL String (i.e. `jini://*/*/DataGridName?groups=myGroup`) or `lookup-groups` spring property as part of the `pu.xml`.
Another alternative is to use unicast based lookup discovery and specify with the `XAP_LOOKUP_LOCATORS` environment variable all existing and future machines that are running/may be running the LUS. Same with the space Url on the client side (i.e. `jini://LUS_HOST1,LUS_HOST2/*/DataGridName`). In most cases its much simpler to use the multicast lookup discovery mechanism.

With the global LUS setup you should have two LUSs running across your service grid - these are running in **active-active** configuration. This means all agents are equal with their setup. They will be communicating with each other to preserve the exact amount of specified LUSs. Once a running LUS failed (as a result of machine termination, or LUS process failure) , a different agent that is not currently running a LUS will be starting a LUS to enforce the SLA specified at the agent startup. The `timeout` space URL property (i.e. `jini://*/*/DataGridName?timeout=10000`) or `lookup-timeout` Spring property with the `pu.xml` allows you to specify the LUS multicast discovery timeout.

# Global Setup Example

With the following example we have `Machine A`, `Machine B`, `Machine C` and `Machine D` running the service grid.

![global-localLUS2.jpg](/attachment_files/global-localLUS2.jpg)


All agents are started with the same command instructing them to maintain two global LUSs across the entire service grid:

```bash
gs-agent gsa.global.lus 2 gsa.lus 0
```

Upon startup the agents will decide which ones will run a LUS and which won't.
To enable this dynamic setup you should have multicast enabled between all service grid machines and also between client applications machines and service grid machines. To differentiate between different service grids running on the same network you should specify a unique `XAP_LOOKUP_GROUPS` environment variable value for the service grid machines before starting the agent. The client application should specify the right group via the URL (i.e. `jini://*/*/DataGridName?groups=myGroup`) or via the `lookup-groups` spring property as part of the pu.xml.

{{%anchor local-lus%}}

# Local LUS
With the local lookup service setup you should specify a list of known machines to run the LUS. This list (two recommended) should be configured on the service grid via the `XAP_LOOKUP_LOCATORS` environment variable (comma delimited) and also with the client application space URL (i.e. `jini://LUS_HOST1, LUS_HOST2/*/DataGridName`) or via the `lookup-locators` Spring property .

# Local Setup Example

With the following example we have `Machine A`, `Machine B`, `Machine C` and `Machine D` running the service grid.  We would like to start two LUSs across these machines. We have decided that `Machine A` and `Machine D` will be running a LUS.

![global-localLUS1.jpg](/attachment_files/global-localLUS1.jpg)

The agent on these machines will be started using the following:


```bash
gs-agent gsa.global.lus 0 gsa.lus 1
```

`Machine B` and `Machine C` will not run the lookup service. The agent on the machines will be started using the following:


```bash
gs-agent gsa.global.lus 0 gsa.lus 0
```

Upon startup the only `Machine A` and `Machine D` agent's that are configured to start a local LUS will have it running.  In case of `Machine A` or `Machine D` failure the system will have a single LUS. Service Grid components (GSC , GSM) and client applications will keep looking for this missing LUS internally (configured via the `com.gigaspaces.unicast.interval`  system property).

Once the missing LUS machine will be restarted they will reconnect. With a network running a DNS - you may start a new machine with the same Host name to support total machine failure while keeping number of running LUSs intact.

