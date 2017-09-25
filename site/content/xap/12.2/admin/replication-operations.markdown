---
type: post122
title:  Replication Operations
categories: XAP122ADM, OSS
parent: replication.html
weight: 500
---

{{% ssummary %}}  {{% /ssummary %}}



The replication module used to synchronize the data and state between two space instances, but each space operations is handled slightly different to achieve maximum reliability and performance.

# Which Operations are Replicated?

- Write/Update/Take of space objects.
- Registration to notify events.
- Object/Notifications lease cancellation or renewal.
- Committed transactions.

{{% refer %}}
All the operations that are replicated, are also recovered during the space recovery process. See [Space Instance Recovery](./space-instance-recovery.html)
{{%/refer%}}

# Which Operations are Not Replicated?

- Notifications on space operations.
- Object/Notifications lease expiration.
- Data loading from the database.
- Uncommitted transactions.
- Space tasks
- Read only operations - read/count etc.

# Granularity

By default, all data and operations are replicated to all target spaces that belong to the same replication group.
You can control the replication granularity and limit what is replicated and what not. Below are the different replication granularity options:

- **Class level replication** - replicate only specific classes. To configure that you need to set:


```java
cluster-config.groups.group.repl-policy.policy-type=partial-replication
```

and add the following annotation to the space class:


```java
@SpaceClass(replicate=false)
public class MyPojo
{
 ...
}
```

- **Operation level replication** - space can be configured so that only specific operations are replicated from a source space to the target space(s).

{{% info %}}
In replicated topology, **the take and clear operations are identical**. Therefore, referrals to the take operation in this section are also relevant for the clear operation.
{{%/info%}}

This can be done by setting the following property:

For example:


```xml
<os-core:embedded-space id="space" space-name="mySpace">
    <os-core:properties>
        <props>
             <prop key="cluster-config.groups.group.repl-policy.permitted-operations">write, extend_lease, lease_expiration, notify</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

In the example above, the `take` and `update` operations are not replicated , while the `write`, `extend_lease`, `lease_expiration`, and `notify_registration` operations are replicated to all spaces.

{{% note %}}
All possible operations  are - write, take, extend_lease, update, discard, lease_expiration, notify
An empty value means no operations to be replicated.
{{%/note%}}

- **Instance level granularity** - [replication filters](#replication-filters) can be used to control which objects are replicate and which are not, per object instance.

## Transactions

Space transaction is replicated as part of the commit process. This means that uncommitted transactions are saved only on the active space instance and in case of a space instance failure must be restarted by the application.
Once transaction is committed on the active space instance, it is replicated to all the target space instances as one atomic operation.

## Notification registration

By default notify registrations are replicated to the target space instance but only the source notifies the listener registered to those events.
The replication in this case is done to ensure that if the source space fails the target will continue to send the events to the registered listeners.
This behavior can be controlled with the following configuration:

Here are the system behaviors when using these options:


| Property | Description | Default Value |
|:---------|:------------|:--------------|
|  cluster-config.groups.group.repl-policy.replicate-notify-templates  | Boolean value. If set to **true**, the notify templates are replicated to the target space. | true|
|  cluster-config.groups.group.repl-policy.trigger-notify-templates  | Boolean value. If set to **true**, the replicated operations will trigger the notify templates and send events to the registered listeners. | false |

A table describing the behavior of combining the different properties:


| Replicate Notify Template Setting | Trigger Notify Template Setting | Explanation |
|:----------------------------------|:--------------------------------|:------------|
| True | False | Client gets notification from the active space after registration.{{<wbr>}}If the active source space fails the target  space instance will continue to send the events to the registered listeners. |
| False | False | Client gets notification only from those spaces to which it registered.{{<wbr>}}Client does not get notifications from spaces which received their data by replication. Failover is not supported|
| False | True | Client gets notification only from those spaces that it registered for notification.{{<wbr>}}Notification occurs when data has been delivered to the space, either by a client application or from the replication. |
| True | True | Client gets notification from all cluster spaces after registration.{{<wbr>}}Client gets multiple notifications for every space event. |

{{% info %}}
Enabling both the Replicate notify templates and trigger notify templates triggers an event for each space, so it may result in more events than you initially intended.
 You can use the source of the event to check which space triggered it.
{{%/info%}}

# Conflict Resolution

Certain scenarios may cause the replication target space instance to receive duplicate or conflicting data updates. It usually happens in an active-active topologies (sync replication / async-replication) where the replication channel is bi-directional. In this case both space replicas sending and receiving data updates via replication. In some cases (split brain , LRU cache policy) it might be relevant also for primary-backup data grid topology. Most common scenarios:

1. The same space object is added/updated or removed at the same time on two or more space instances (replicas).
2. Data recovery in a topology that has more than two space instances in the same replication group.
3. Frequent lease expiration/renewal of the same object
4. The replicated space object is locked under a transaction at the destination space

The **default** behavior in these cases is to treat the conflicting operations as duplicates and ignore them. This can be controlled using the `cluster-config.groups.group.repl-policy.on-conflicting-packets` property which has two options - **ignore** or **override**:


| Property | Description | Default Value |
|:---------|:------------|:--------------|
| cluster-config.groups.group.repl-policy.on-conflicting-packets | If set to **ignore**, the conflicting operations are ignored. If set to **override** the newest operation will override the data in the target.| ignore|

When a conflict happens you may observe the following messages in the logs:

- Replication detected conflicting Update operation on entry - <com.mycomp.myclass> uid=<-5434534533283^26^5434538^0^0>. Symptom: Entry is locked by another transaction.
- Replication detected conflicting Write operation on entry - <com.mycomp.myclass> uid=<-5434534533283^26^5434538^0^0>. Symptom: Entry already in space.
- Replication detected conflicting Update operation on entry - <com.mycomp.myclass> uid=<-5434534533283^26^5434538^0^0>. Symptom: Entry not in space.
- Replication detected illegal take operation on entry uid=<-5434534533283^26^5434538^0^0>. Symptom: Entry class name wasn't replicated. Ignoring the illegal operation. 

{{% refer %}}
This conflict resolution does not cover replication over WAN when using the WAN Gateway. This has a different [Conflict resolution mechanism]({{%currentjavaurl%}}/multi-site-conflict-resolution.html).
{{%/refer%}}

#   Optimizations

By default some operations are replicated in an optimized way - only the minimum required data for the operation is replicated.
For example the take operation only replicates the object ID to minimize the network overhead.

Additional optimizations that can affect the replication performance is the update operation. Regular object updates replicate the whole object state - all the properties even those that were not changed. This can be optimized by using `WriteModifier.PARTIAL_UPDATE` modifier when performing the object update. When this modifier is used, the replication will replicate only the changed properties and not the whole object.

{{% refer %}}
When mirror is used additional settings are required to support the partial update. See [Optimizing the Mirror Activity]({{%currentjavaurl%}}/asynchronous-persistency-with-the-mirror.html#Optimizing the Mirror Activity).
{{%/refer%}}

#   Filters

You can call your own business logic whenever the data is replicated. For example, you can modify the space objects data, compress/decompress, or block specific operations and space objects from being replicated to other spaces. Your business logic is called whenever the replication packet leaves the source space (output event), and arrives at the target space (input event).
This is described under the [Cluster Replication Filters](./cluster-replication-filters.html) section.
