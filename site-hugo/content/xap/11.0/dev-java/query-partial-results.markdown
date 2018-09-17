---
type: post110
title:  Projection
categories: XAP110
parent: querying-the-space.html
weight: 800
---



{{% section %}}
{{% column width="60%" %}}
In some cases, when querying the Space for `Objects`, only certain properties of those `Objects` are required (a.k.a. delta read). The same scenario is also relevant when subscribing for notifications on Space data changes, where only specific properties are of interest to the subscriber. For that purpose, the *Projection API* can be used, where one can declare which properties are of interest. The space will only populate these properties on the returned data.
{{% /column %}}
{{% column width="40%" %}}
![space-projections.jpg](/attachment_files/space-projections.jpg)
{{% /column %}}
{{% /section %}}

This approach reduces network overhead, garbage memory generation and CPU overhead due to serialization.


# Examples

Projection supports using a [SQLQuery](./query-sql.html) or [Id Queries](./query-by-id.html). Below is a simple example reading a `Person` Object where only the 'firstName' and 'lastName' properties are returned in the array or results. All other `Person` properties will not be populated:


```java
public class Person
{
  ...
  @SpaceId(autoGenerate = false)
  public Long getId() { ... }
  public String getFirstName() { ... }
  public String getLastName() { ... }
  public String getAddress() { ... }
  ...
}

GigaSpace gigaSpace = //... obtain a gigaspace reference.
Long id = //... obtain the space object ID.
Person result = gigaSpace.read<Person>(new IdQuery<Person>(Person.class, id).setProjections("firstName", "lastName"));
```

With the above example a specific Person is being read but only its 'firstName' and 'lastName' will contains values and all the other properties will contain a `null` value.

You may use the same approach with the `SQLQuery` or `IdsQuery`:


```java
SQLQuery<Person> query = new SQLQuery<Person>(Person.class,"").
		setProjections("firstName", "lastName");
Person result[] = gigaSpace.readMultiple(query);

IdsQuery<Person> idsQuery = new IdsQuery<Person>(Person.class, new Long[]{id1,id2}).
		setProjections("firstName", "lastName");
Person result[] = space.readByIds(idsQuery).getResultsArray();
```




The [SpaceDocument API](./document-api.html) supports projection as well:


```java
SQLQuery<SpaceDocument> docQuery = new SQLQuery<SpaceDocument>(Person.class.getName() ,"",
	QueryResultType.DOCUMENT).setProjections("firstName", "lastName");
SpaceDocument docresult[] = gigaSpace.readMultiple(docQuery);
```

You can also use projection for nested properties:


```java
SQLQuery<SpaceDocument> docQuery = new SQLQuery<SpaceDocument>(Person.class.getName() ,"",
	QueryResultType.DOCUMENT).setProjections("user.address.street", "user.address.zipCode");
SpaceDocument docresult[] = gigaSpace.readMultiple(docQuery);
```


# Supported Operations

A projection is defined for any operation that returns data from the Space. Therefore id-based or query-based operations support projections. You can use the Projection API with `read`,`take`,`readById`,`takeById`,`readMultiple` and `takeMultiple` operations. When performing a `take` operation with projection, the entire Object will be removed from the space, but the result returned to the user will contain only the projected properties.


You can use projections with a [Notify Container](./notify-container.html), when subscribing to notifications. You can use it with a [Polling Container](./polling-container.html), when consuming Space Objects. You can also create a [Local View](./local-view.html) with templates or a `View` using projections. The local view will maintain the relevant objects, but the view of the data will contain only the projected properties.
Both dynamic and fixed properties can be specified - the syntax is the same. As a result, when providing a property name which is not part of the property set, it will be treated as a dynamic property: That is, if there is no like-named dynamic property present on a query result Object, then the property will be ignored entirely (and no Exception will be thrown). Please note that a result may contain multiple objects, each with a different combination of properties (fixed and/or dynamic) - each object will be treated individually when applying projections to it.

# Considerations

1. You can't use a projection on [Local Cache](./local-cache.html), as the Local Cache needs to contain fully constructed objects. Reconstructing an Object locally with projection would only negatively impact performance.
1. You can't use a projection to query a Local View for the same reason as for Local Cache. However, you can create a Local View with a projection template in which case the Local View will contain the Objects in their projected form.

# Working Examples

{{%refer%}}
[This repository](https://github.com/GigaSpaces/gs-executor-remoting/) contains an integration test that performs projection on a query in the context of [Executor Based Remoting](./executor-based-remoting.html). Relevant lines of code (Scala) are [here](https://github.com/GigaSpaces/gs-executor-remoting/blob/master/src/test/scala/com/gigaspaces/sbp/WatchRepairSuite.scala#L124).
{{%/refer%}}

