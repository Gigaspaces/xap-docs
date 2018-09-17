---
type: post123
title:  Consistency Biased
categories: XAP123ADM, PRM
parent: leader-election.html
weight: 200
---

In distributed computing, consistency-biased leader election is established using an odd number of coordinators (usually 3). Having an odd number ensures that if network segmentation occurs, there will be no draw between the segments - the majority of coordinators (e.g. 2) will be in one segment, and the minority (e.g. 1) in the other. This allows each segment to play its role and ensure consistency:

* If the minority segment holds leadership, it will relinquish it, and will suspend itself until it rejoins the majority.
* The majority segment will select a new leader if needed, knowing that the minority will relinquish the previous leader.

In XAP, consistency-biased leader election is used when the space is deployed on an environmenent managed by a [XAP Manager](xap-manager.html) cluster. Each manager contains an embedded Apache Zookeeper node (znode), and together they provide the necessary environment to ensure consistency.

# What is Apache Zookeeper?

[Apache ZooKeeper](https://zookeeper.apache.org/) is a centralized service providing distributed synchronization, which can be used for various use cases in distributed systems such as leader election, configuration, distributed locks and more. It is highly reliable and widely used across the industry, both in open source projects such as Apache HBase and Apache Kafka), and by large companies such as Yahoo and Rackspace.

{{% tip "Tip"%}} You don't have to download or setup Apache Zookeeper. It comes packaged with XAP, and is automatically started and monitored by the XAP Manager. {{% /tip %}}

# Usage

When a space is deployed on an environment managed by a [XAP Manager](xap-manager.html), consistency-biased leader election is automatically enabled.

# Configuration

The default configuration is valid for most environments and applications. You can change it if you need to decrease/increase failover time (i.e. the duration between a primary fails and a backup accepts leadership in its place), using the following space properties:

| Property             | Description                                               | Default |
|:---------------------|:----------------------------------------------------------|:--------|
| `space-config.leader-election.zookeeper.session-timeout`                 | Session timeout (in milliseconds)    | `8000` |
| <nobr>`space-config.leader-election.zookeeper.connection-timeout`</nobr> | Connection timeout (in milliseconds) | `5000` |
| `space-config.leader-election.zookeeper.retry-timeout`                   | Retry timeout, in case operation fails | `Integer.MAX_VALUE` |
| `space-config.leader-election.zookeeper.retry-interval`                  | Interval between retries (in milliseconds) | `1000` |

{{% note "Note" %}} A shorter failover time is not always advantageous. It may cause short network disconnections to trigger unnecessary failovers, which can affect system stability. Change the defaults only after careful consideration, and adjust the values to suit your network capabilities and applicative requirements. {{% /note %}}

# Implementation

GigaSpaces XAP uses the Apache Curator [leader selector](http://curator.apache.org/curator-recipes/leader-election.html) recipe, which implements a distributed lock with a notification mechanism using Apache Zookeeper.

{{%align center%}}
![image](/attachment_files/zookeeper-based-leader-selector.png)
{{%/align%}}

The following occurs during leader election:

- There is a znode, say “/participants/partitionX".
- All participants of the election process create an ephemeral-sequential node on the same election path.
- The node with the smallest sequence number is the leader.
- Each “follower” node listens to the node with the next lower sequence number.
- Upon leader removal, go to election path and find a new leader, or become the leader if it has the lowest sequence number.
- Upon session expiration (disconnection), check the election state and go to election if needed. If there is a disconnection, the primary Space instance is moved to Quiese mode and will be restarted. 

## Partition Split Brain Instances

The Apache Zookeeper leader selector avoids split-brain instances through quorum. If the primary Space is not in the majority, that Space is frozen (or quiesced) until the network is connected and the frozen primary Spac eis terminated automatically.
