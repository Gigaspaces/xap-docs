---
type: post100
title:  Routing In Partitioned Spaces
categories: XAP100NET
parent: modeling-your-data.html
weight: 600
---




A [partitioned space](/product_overview/terminology.html) provides the ability to perform space operations against multiple spaces from a single proxy transparently. The primary goal of the partitioned space is to provide unlimited In-Memory space storage size, and to group related objects into the same partition to speed up performance. The initial intention is to write data into the partitioned space, and route query operations based on the template data.

Note that in such a configuration, the different spaces defined as partitions are not aware of each other ("Share Nothing") - The client proxy is the one that is aware of the partitioned spaces configuration.

{{%anchor Defining a Routing Property%}}

# Defining a Routing Property

**Partitioning** is used when the total data set is too big to be stored in a single space, and we want to divide the data into two or more groups (partitions). In order to do that, the proxy needs to know how to partition the data, i.e. which entry belongs in which partition.

In order to accomplish that a **routing property** can be defined on the entry type. When the proxy is asked to write an entry it uses the entry's routing property value hash code to determine the relevant partition for it:


```csharp
Target partition space ID =
    getRoutingPropertyValue(entry).hashCode() % (number of partitions)
```

The routing property can be explicitly set using the \[`SpaceRouting`\] attribute for [Object entries](./pono-attribute-annotations.html) or via the `SpaceTypeDescriptorBuilder` for [document entries](./document-api.html). If the routing property is not explicitly set, the space id property is used for routing. If the space id property is not defined, the first indexed property (alphabetically) is used for routing, otherwise the first property (alphabetically) is used for routing.

{{% note %}}
It is highly recommended to explicitly define a routing property - when both space routing and space id are not defined, relying on implicit routing property selection can be confusing, being maintained mainly for backwards compatibility.
{{%/note%}}

{{% info %}}
Starting with 8.0.1, an auto generated space id can be used for routing as well. In previous versions this was not supported, and was ignored in the implicit routing property selection mechanism.

Starting with 8.0.1, if the space id is not defined in a document type, it will be implicitly defined to auto generate id and routing information.
{{%/info%}}

In some scenarios, the data model does not require sophisticated partitioning, and simple space-id-based partitioning is all that's needed (hence the default). In other scenarios, this is not enough. For example, suppose we have a **Customer** class with a **customerId**, and an **Order** class with an **orderId** and **customerId**, and we want all the orders of each customer to be co-located with it in the same partition. In that case, we'd explicitly set the routing property of **Order** to **customerId** to ensure they're co-located.

# Writing To A Partitioned Space

As explained above in [Defining a Routing Property](#Defining a Routing Property), when a proxy is asked to write an entry it extracts the routing property value to determine the relevant partition id and forwards the entry to that partition. If the routing property value is null an exception will be thrown indicating the proxy cannot write the entry because it does not know the target partition.

When an entry is being updated, the proxy uses the routing property to route the update request to the relevant partition.

On a batch write/update via `writeMultiple`, the proxy iterates over the batch of entries and divides them into groups according to the routing property, and each group is sent to the relevant partition in parallel.

Starting with 8.0.1, an auto generated id property can be used for routing as well - the proxy detects that routing is auto generated, selects a partition using a round robin mechanism and forwards the entry to that partition. The space embeds the partition id in the auto generated id value, so if the entry is later updated the proxy will extract the partition id from the id value and route the entry to the correct partition. Note that auto generated routing is only valid in conjunction with auto generated id, and cannot be used to route other entries.

# Querying A Partitioned Space

When a proxy is asked to execute a query (template, SQL or id) on a partitioned space, it first checks if the query contains a routing value. If it does, the query is executed only on the relevant partition, otherwise it is broadcasted to the entire cluster.

This is relevant to all the `read` / `take` methods (if exists, by id, async, multiple, etc.), as well as the `count` and `clear` methods and space notifications.

{{% note %}}
Blocking operations (`read` / `take` with `timeout` greater than 0) on a partitioned space do not support broadcast - if the routing value is null and exception will be thrown.
{{%/note%}}

{{% note %}}
FIFO ordering is maintained per partition. If a FIFO query is broadcasted to the entire cluster the results are not guaranteed to be FIFO ordered.
{{%/note%}}

# Initializing A Partitioned Space

{{%refer%}}
When initializing a partitioned space, it is possible to load only the data you need for a specific partition. For more details, see [Persistency](./space-persistency-overview.html).
{{%/refer%}}

# Non-Partitioned Operations

The `clean` and `snapshot` operations are not partition-aware, i.e. they are executed on a default partition that the proxy selects implicitly, and not broadcast to the entire cluster.
