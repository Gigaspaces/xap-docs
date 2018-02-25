---
type: post123
title:  Administration Authorities
categories: XAP123SEC, PRM
parent: none
weight: 700
---



The Premium and Enterprise editions provide administration authorities for managing and monitoring the grid. 
For example, a user may have a `Grid Authority` of type `Provision PU`, which allows it to deploy and un-deploy in the Grid.

# Grid Authority

The **Grid Authority** consists of privileges for managing the Grid and its Services (GSMs, GSCs, Processing Units).

|       |     |
|-------|-----|
| Provision PU | Deploy, Un-deploy of processing units |
| Manage PU | Scale up/down, Relocate, Restart PU instance, Destroy PU instance |
| Manage Grid | Start, Terminate, Restart of GSC/GSM/LUS via GSA |

In distributed systems, it may be confusing as to which service is performing the authorization. The GSM is responsible for deploying, un-deploying, scaling, relocating, restarting and destroying of processing units. The GSA (if available) is responsible for starting, terminating restarting of GSC/GSM/LUS. The GSC on the other hand, mainly delegates the calls to the managing GSM (e.g. relocate).


{{% note "Note"%}}
When deploying an [elastic processing unit](../dev-java/elastic-processing-unit-overview.html), the *Provision PU* privilege is not enough - *Manage PU* and *Manage Grid* are required as well, since an elastic PU requires scaling and grid management.
{{%/note%}}

# Monitor Authority

The **Monitor Authority** consists of privileges for monitoring the Grid and its Processing Units.
Note that the monitoring is secured only by the 'tooling' (CLI/UI).

|       |     |
|-------|-----|
| Monitor JVM | Monitoring of JVM statistics |
| Monitor PU | Monitoring of Processing Units (classes, connections, statistics, etc.) |
