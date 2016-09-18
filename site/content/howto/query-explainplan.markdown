---

type: post
title: Explain Plan

weight: 1400
---

{{%warning%}}
This a preview feature .......
{{%/warning%}}


# Intro 

 

# Usage 

Use .withExplainPlan() to indicate a query should be executed with an explain plan
Execute the query as usual
Use .getExplainPlan() to get the result, and print it



```java
SQLQuery query = new SQLQuery(MyPojo.class, "price > 1000").withExplainPlan();
Object result = gigaSpace.read(query);
System.out.println(query.getExplainPlan());
```

#  Explain Plan - Index Information
 
- Describes the indexes that the space considered to use, and the selected index in each stage.
- “And” choice node behavior is to scan through the relevant indexes and choose the minimal one.
- “Or” choice node behavior is to unify all the indexes that each “And” chose.
- Each cluster node may produce different result
- The information breakdown is by the Pojo  type.

#  Explain Plan - Scanning Information

- Describes the number of entries the space scanned in order to find the matching entries, and how many entries were a match.
- Each cluster node may produce different result
- The information breakdown is by the Pojo  type.


 
# Limitations
 
 - Only Java API is supported
 - JDBC not supported (hence also Web-UI/GS-UI)
 - .NET not supported
 - Only Basic and Extended index are supported
 - No support for collection, compound, unique.
 - Off-Heap is not supported
 - FIFO grouping is not supported
 - Geospatial is not supported
 - Only Read, ReadMultiple, TakeMultiple & count  operations are supported
 - Not supported: Take/Clear and variations, blocking operations, space iterator, aggregate , change, notifications
 - Not thread Safe
 - Only Basic query type are Supported
 - Not supported: Regex, Is null, Sql function
 
# Some Interesting Space Behaviors 

- Prefers id property index 

- Prefers basic index over extended index
