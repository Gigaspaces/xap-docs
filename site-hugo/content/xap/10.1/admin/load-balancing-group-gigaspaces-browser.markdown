---
type: post101
title:  Load-Balancing Group
categories: XAP101ADM
parent: working-with-clusters-gigaspaces-browser.html
weight: 400
canonical: auto
---

{{% ssummary %}}A load-balancing group defines load-balancing between spaces in the cluster.{{% /ssummary %}}


A group can define a load-balancing policy. If it does, then any other group that contains at least one space belonging to this group cannot define a load-balancing policy. Thus, the load-balancing policy of the group determines the load-balancing behavior for the clustered proxy of any space member in the group.
If a group defines a load-balancing policy, it is called a load-balancing group (a load-balancing group can also be a replication group and a failover group).
If a space belongs to a failover group and a load-balancing group, it must be the same group. If there is a space failure in the group, load-balancing will continue among the space members in the group (as long as there is at least one live space in the group) according to the load-balancing policy.

{{% refer %}}
For details about scaling a running space cluster **in runtime** see the [Elastic Processing Unit]({{%currentjavaurl%}}/elastic-processing-unit.html) section.
{{% /refer %}}

