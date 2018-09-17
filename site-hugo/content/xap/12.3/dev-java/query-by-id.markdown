---
type: post123
title:  Id Queries
categories: XAP123, OSS
parent: querying-the-space.html
weight: 100
---


 


The space can be queried for entries using [Template Matching](./query-template-matching.html) or [SQLQuery](./query-sql.html), but sometimes we know the exact id of the entry we need and prefer a faster solution. This is where id-based queries come handy.

# Reading an Entry By ID

When you would like to access an object using its ID for read and take operations you should first specify the ID field. You can specify it via `@SpaceId (autogenerate=false)` annotation:


```java
@SpaceId (autoGenerate=false)
public String getEmployeeID() {
    return employeeID;
}
```

or via the gs.xml configuration:


```java
<id name="employeeID" auto-generate="false" />
```

Here is how you can read the object back from the space using its ID and the `readById` operation:


```java
GigaSpace gigaSpace;
Employee myEmployee = gigaSpace.readById(Employee.class , myEmployeeID , routingValue);
```

# Reading Multiple Entries by IDs

The following shows how to read multiple objects using their IDs:


```java
GigaSpace gigaSpace;

// Initialize an ids array
Integer[] ids = new Integer[] { ... };

// Set a routing key value (not mandatory but more efficient)
Integer routingKey = ...;

// Read objects from space
ReadByIdsResult<Employee> result = gigaSpace.readByIds(Employee.class, ids, routingKey);

// Loop through results
for (Employee employee : result) {
  // ...
}
```

# Getting Partial Results

You can specify which properties should be populated when the result is created the [Projection API](./query-partial-results.html).

{{% refer %}}
See [Parent Child Relationship](/sbp/parent-child-relationship.html) for a full usage example of the `readByIds` operation.
ReadById is intended to objects with meaningful ids,if used with auto-generate="true" ids,the given object type will be ignored.
{{% /refer %}}


