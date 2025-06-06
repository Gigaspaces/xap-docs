---
type: post123
title:  Routing In Partitioned Spaces
categories: XAP123, OSS
parent: modeling-your-data.html
weight: 600
canonical: auto
---



A [partitioned space](../overview/terminology.html) provides the ability to perform space operations against multiple spaces from a single proxy transparently. The primary goal of the partitioned space is to provide unlimited In-Memory space storage size and group objects into the same partition to speed up performance. The initial intention is to write data into the partitioned space, and route query operations based on the template data.

Note that in such a configuration, the different spaces defined as partitions are not aware of each other ("Share Nothing") - the client proxy is the one that is aware of the partitioned space configuration.

{{%anchor Defining a Routing Property%}}

# Defining a Routing Property

**Partitioning** is used when the total data set is too big to be stored in a single space, and we want to divide the data into two or more groups (partitions). In order to do that, the proxy needs to know how to partition the data, i.e. which entry belongs in which partition.

In order to accomplish that a **routing property** can be defined on the entry type. When the proxy is asked to write an entry, it uses the entry's routing property's hash code to determine the relevant partition for it:


```java
Target partition space ID = safeABS(routing field hashcode) % (number of partitions)

int safeABS( int value)
{
     return value == Integer.MIN_VALUE ? Integer.MAX_VALUE : Math.abs(value);
}
```

The routing property can be explicitly set using the `@SpaceRouting` annotation for [POJO entries](./pojo-overview.html) or via the `SpaceTypeDescriptorBuilder` for [document entries](./document-api.html). If the routing property is not explicitly set, the space id property is used for routing. If the space id property is not defined, the first indexed property (alphabetically) is used for routing, otherwise the first property (alphabetically) is used for routing.

{{% note %}} It is highly recommended to explicitly define a routing property - when both space routing and space id are not defined, relying on implicit routing property selection can be confusing (being maintained mainly for backwards compatibility).{{%/note%}}

{{% note %}}
An auto-generated space ID can be used for routing as well. If the space ID is not defined in a document type, it will be implicitly defined to auto-generate the ID and routing information. (In earlier versions this was not supported, and was ignored in the implicit routing property selection mechanism.)
{{%/note%}}}

In some scenarios, the data model does not require sophisticated partitioning, and simple space-id-based partitioning is all that's needed (hence the default). In other scenarios this is not enough. For example, suppose we have a **Customer** class with a **customerId**, and an **Order** class with an **orderId** and **customerId**, and we want all the orders of each customer to be co-located with it in the same partition. In that case we'd explicitly set the routing property of **Order** to **customerId** to ensure they're co-located.

However, note that all objects with a given routing value will be stored on the _same partition_. This means that a given partition _must_ be able to hold all similarly-routed objects. If the routing value is weighted too far in a given direction (i.e., the distribution is very uneven, with one value having 90% of the data set, for example) the partitioning will be uneven. It's best to use a derived routing field that gives a flat distribution across all nodes, if possible.

{{%anchor Writing New Objects with a Collocated Business Logic%}}

# Writing New Objects with a Collocated Business Logic

When writing new objects from a collocated business logic with a partitioned space, the routing field must "match" the collocated space instance. When writing new objects from a collocated business logic with a partitioned space, the routing field must "match" the collocated space instance.

{{%refer%}}
See [Writing New Objects from a Collocated Business Logic](../admin/clustered-vs-non-clustered-proxies.html#Writing New Objects from a Collocated Business Logic) for details.
{{%/refer%}}

{{%anchor  Writing To a Partitioned Space %}}

# Writing To a Partitioned Space

As explained above, in [Defining a Routing Property](#defining-a-routing-property), when a proxy is asked to write an entry it extracts the routing property value to determine the relevant partition ID and forwards the entry to that partition. If the routing property value is null, an exception will be thrown indicating the proxy cannot write the entry because it does not know the target partition.

When an entry is being updated, the proxy uses the routing property to route the update request to the relevant partition.

On a batch write/update via `writeMultiple`, the proxy iterates over the batch of entries and divides them into groups according to the routing property, and each group is sent to the relevant partition in parallel.

An auto-generated ID property can be used for routing as well - the proxy detects that routing is auto-generated, selects a partition using a round-robin approach and forwards the entry to that partition. The space embeds the partition ID in the auto-generated ID value, so if the entry is later updated the proxy will extract the partition ID from the ID value and route the entry to the correct partition. Note that auto-generated routing is only valid in conjunction with auto-generated ID, and cannot be used to route other entries.

{{%anchor Querying a Partitioned Space %}}

# Querying a Partitioned Space

When a proxy is asked to execute a query (template, SQL or id) on a partitioned space, it first checks if the query contains a routing value. If it does, the query is executed only on the relevant partition, otherwise it is broadcast to the entire cluster.

This is relevant to all the `read` / `take` methods (if exists, by id, async, multiple, etc.), as well as the `count` and `clear` methods and space notifications.

{{% note%}}Blocking operations (`read` / `take` with `timeout` greater than 0) on a partitioned space do not support broadcast - if the routing value is `null` an exception will be thrown.

FIFO ordering is maintained per partition. If a FIFO query is broadcasted to the entire cluster the results are not guaranteed to be FIFO ordered.
{{%/note%}}


# Initializing a Partitioned Space

When initializing a partitioned space, it is possible to load only the data you need for a specific partition.

{{%refer%}}
For more details, see [Space Persistency Initial Load](./space-persistency-initial-load.html).
{{%/refer%}}

# Non-Partitioned Operations

The `clean` and `snapshot` operations are not partition-aware, i.e. they are executed on a default partition that the proxy selects implicitly, and not broadcast to the entire cluster.
