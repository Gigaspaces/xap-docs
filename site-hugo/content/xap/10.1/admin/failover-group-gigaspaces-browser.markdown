---
type: post101
title:  Failover Group
categories: XAP101ADM
parent: working-with-clusters-gigaspaces-browser.html
weight: 300
canonical: auto
---

{{% ssummary %}}A failover group defines failover between spaces in the cluster.{{% /ssummary %}}


A group can define a failover policy, in which case clients of spaces belonging to the group receive and use a clustered proxy instead of a regular proxy. The failover policy of the group determines the failover behavior for the clustered proxy of any space in the group.
A group that defines a failover policy is called a failover group. A failover group can also be a replication group and a load-balancing group.

{{% tip %}}
A space cannot reside in different load-balancing and failover groups. In other words, the only way to apply both load balancing and failover to a space is to define these policies for one group, which the space belongs to.
{{% /tip %}}

If an operation performed on a space fails because the space server fails, the clustered proxy tries to locate another space in the failover group and redirect the operation to it, subject to the failover policy defined. An exception is operations that are performed under a transaction; in this case, the clustered proxy aborts the transaction and throws an exception to the application. In this case, the application should start a new transaction, perform the operations again and re-commit.

See the [Data-Partitioning](./data-partitioning.html) for details.
