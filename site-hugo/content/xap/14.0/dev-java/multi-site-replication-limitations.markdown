---
type: post140
title:  Limitations
categories: XAP140, ENT
parent: multi-site-replication-overview.html
weight: 900
---



The following limitations and open issues apply to the multi-site gateway replication module:

1. False conflict warnings may arise during bootstrap process which can occur if there are more than two sites replicating to the bootstrapped sites, this can happen if some data is being copied from the bootstrap source site which was replicated from the third site.
1. Override conflict resolution for version conflict when the source space has an older version, may cause a local cache at the target space which contains the conflicted entry to have stale representation of that entry.
1. Take/Clear operations raises no conflicts if the Sink component fails to remove the desired entry either if its locked under transaction or if it doesn't exist in the local cluster.
1. When using LRMI port range system property (com.gs.transport_protocol.lrmi.bind-port) in order to specify the grid's components ports for LRMI socket, an exception may occur when WAN gateway's communication port is in that range and the gateway and the grid component are planned to be deployed on the same machine.
To prevent this port collision, one should not pick a port inside the LRMI port range as the gateway's communication port.
