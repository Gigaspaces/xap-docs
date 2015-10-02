---
type: post110adm
title:  Failover
categories: XAP110ADM
parent: data-grid-clustering.html
weight: 300
---


{{% ssummary %}} {{% /ssummary %}}



Failover is the mechanism used to route user operations to alternate spaces in case the target space (of the original operation) fails.

The component responsible for failover in GigaSpaces is the clustered proxy. When an operation on a space fails, the clustered proxy tries to locate an available and active space member. If it finds such a space, it re-invokes the operation on that space member, otherwise it throws the original exception.

# Failover and Recovery Process

Failover and Recovery process includes the following steps (we assume spaces deployed using the SLA driven container (GSC and GSM):

1. Backup identifies that the primary space is not available and moving itself to a primary mode. For information on speeding up this step refer to [Failure Detection]({{%currentadmurl%}}/troubleshooting-failure-detection.html).
2. Space proxy router discovers the new primary space and routs the request to it. For information on speeding up this step refer to [Proxy Connectivity]({{%currentadmurl%}}/tuning-proxy-connectivity.html).
3. GSM identifies the backup space is missing and provision new space into existing GSC - preferably empty GSC or based on predefined SLA. You can tune this step by modifying the GSM settings.
4. Backup started. It is looking for the look service and register itself.
5. Backup identifies that a primary space exists (goes through primary election process) and moves itself into backup mode. If there are multiple GSMs and bad network or wrong locators configuration -- you might end up here with split-brain scenario where you have multiple primaries running.
6. Backup read existing space objects from the primary space (aka memory recovery). This might take some time with few million space objects. GigaSpaces using multiple threads with this activity. You can tune this by modifying the recovery batch size and also the amount of recovery threads.
7. Primary clears its redo log (During the above step) and starts to accumulate incoming destructive events (write , update , take) within its redo log. This is one of the reasons why the redo log size can have limited size in many cases. You can use the redo-log-capacity to configure the redo-log size. For example: If the recovery takes 30 sec and the client performs 1000 destructive operations/sec -- your redo log size can be 30,000.
8. Backup completes reading space objects from primary.
9. Primary replicates redo log content to the backup (via the async replication channel) -- In this point backup getting also sync replication events from primary. You can control the redo log replication speed using the async replication batch size.
10. Once the redo replication completed -- Recovery done. Backup is ready to act as a primary.

To speed up the recovery time you can override the following properties:

1. `cluster-config.groups.group.repl-policy.recovery-chunk-size` - Objects per chunk (default is 200).
1. `cluster-config.groups.group.repl-policy.recovery-thread-pool-size` - Number of threads the backup is running to consume data from the primary (default is 4).

# Failover with Blocking Operations and Transactions

Even if a client invokes a blocking operation, like `take with timeout` and the server fails after receiving the take arguments but before returning a result, the client gets an exception, and the failover process aborts. This is because in a clustered engine, unlike a non-clustered one, the server thread servicing the request waits for a final answer to be given (unless the request is timed out).
If an operation is performed under a transaction and the target space that serviced the transaction has failed, the clustered proxy automatically aborts the transaction and throws a transaction exception back to the caller. (There is no point in re-invoking the operation to a different space, because the failed space member is a transaction participant. The transaction will ultimately be aborted by the transaction manager.) The caller catching the exception can start a new transaction and continue execution; later calls on the proxy are directed to an available space in the group.

# Backup Space reconciliation after network disconnection

Network disconnections and disruptions impact the availability of the distributed cluster. In such cases, the failure-detection mechanisms will attempt to heal the cluster by creating new instances to preserve availability. The failure-detection determines when an instance is unreachable. There is no real distinction between an instance which is abruptly terminated and an instance which is unreachable due to network problems. In the latter case the healing process may lead to extra Space instances when network connection is restored. When the extra Space instance is a primary, the [Split-Brain mechanism]({{%currentadmurl%}}/split-brain-and-primary-resolution.html) kicks in. When the extra Space instance is a backup, reconciliation is done by determining if this backup Space is connected to a discovered primary Space. The discovered primary Space is either already connected to a backup Space or not. If the backup Space is not a "replication target" of the discovered primary Space, it will be shutdown. 

When both Split-Brain and an extra Backup Space occur, both mechanisms work side-by-side. Extra backup Space instances will be shutdown and then a primary Space instance will be chosen out of the existing primaries. A backup Space will be provisioned if necessary.


