---
type: post97
title:  Limitations
categories: XAP97
parent: multi-site-replication-overview.html
weight: 900
---



The following limitations and open issues apply to the multi-site gateway replication module:

1. False conflict warnings my arise during bootstrap process which can occur if there are more than two sites replicating to the bootstrapped sites, this can happen if some data is being copied from the bootstrap source site which was replicated from the third site.
1. Override conflict resolution for version conflict when the source space has an older version, may cause a local cache at the target space which contains the conflicted entry to have stale representation of that entry.
1. Take/Clear operations raises no conflicts if the Sink component fails to remove the desired entry either if its locked under transaction or if it doesn't exist in the local cluster.
