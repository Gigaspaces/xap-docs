---
type: postsbp
title:  Even Data Distribution
categories: SBP
parent: data-access-patterns.html
weight: 400
---



{{% tip %}}
**Summary:**  This page covers some of the concepts to consider when partitioning data. It does not replace the load-balancing pages, where the concept is explored in greater detail, but provides a slightly higher-level view. <br/>
**Author**: Joe Ottinger, Technology Evangelist, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 7.1.2<br/>


{{% /tip %}}

# Overview

In order to leverage your entire space in the most efficient way, you need to be aware of and use an efficient partitioning strategy.

Partitioning refers to distribution across multiple nodes; if all of your data is held in one container, then your data isn't partitioned. It's far more efficient to spread a dataset across multiple nodes and multiple machines, if possible, which allows you to process a subset of data in parallel on each machine, collecting the results and collating them. (This is the "map/reduce" pattern in a nutshell.)

# Efficient Partitioning

In Gigaspaces XAP, there are six approaches to partitioning, as discussed on [Data-Partitioning]({{%latestadmurl%}}/data-partitioning.html). In order to understand the full scope of partitioning, see the [data-partitioning.html]({{%latestadmurl%}}/data-partitioning.html) section.

The most common approach to partitioning is, as stated in the [Data-Partitioning]({{%latestadmurl%}}/data-partitioning.html) documentation, hash-based partitioning, using an explicit value contained in a data object. This is specified via an annotation:


```java
public class MyData {
    private String id;
    private String groupId;

    @SpaceId
    public String getId() {return id;}
    public void setId(String s) { id=s; }

    @SpaceRouting
    public String getGroupId() { return groupId; }
    public void setGroupId(String s) { groupId=s; }
}
```

The approach for efficient partitioning depends very much on how the data is used. In the case of the `MyData` class above, it's likely that different groups' data will be routed to different partitions (but not **guaranteed** - because it's possible that various groupId values end up with the same partitioning values. As usual, see the [Data-Partitioning]({{%latestadmurl%}}/data-partitioning.html) documentation for more detail.)

When a task is started to handle all of a specific groupId's data, then the **single** partition holding that data will be involved, which can yield very efficient results; if multiple requests go out to handle different groups (i.e., count all values based on groupId), then the partitions can focus on handling only the groupIds held locally, sending the results back to the original caller. As stated above, this is a representation of the Map/Reduce algorithm, which is documented in [Task Execution over the Space]({{%latestjavaurl%}}/task-execution-over-the-space.html).

However, if one groupId consists of a much larger set than another, note that all of that groupId's data will be held on a single partition, which can be problematic. **Remember to examine what your data looks like and how it is partitioned!**

For perfectly distributed data that isn't naturally partitioned (i.e., something that doesn't have a groupId analog, from the example above), it's possible to use a routing field that is an ascending integer. For example:

{{%tabs%}}

{{%tab "  storeEvenlyDistributedData "%}}


```java
...
public void storeEvenlyDistributedData(GigaSpace space, int count) {
   if(count<1)
   {
      throw new IllegalArgumentException("count of objects must be > 0: passed value was "+count);
   }
   for(int i=0; i<count;i++)
   {
      MyData data=new MyData();
      data.setKey("key "+i);
      // JDK 1.4 will need to use "new Integer(i)" due to the lack of autoboxing.
      data.setRouting(i);
      space.write(data);
   }
}
...
```

{{% /tab %}}

{{%tab "  MyData "%}}


```java
...
public class MyData {
   String key;
   // note that space objects need to use Object references, not primitives
   Integer routing;
   public MyData(String key, Integer routing) {
      this.key=key;
      this.routing=routing;
   }

   public MyData() {}
   @SpaceId
   public String getKey() { return key; }
   @SpaceRouting
   public Integer getRouting() { return routing; }
   // various mutators follow, eliminated for brevity
}
```

{{% /tab %}}

{{% /tabs %}}

This is not especially efficient for remote task execution (because it doesn't naturally group related data) but does provide an even distribution across partitions, if that's what you need.
