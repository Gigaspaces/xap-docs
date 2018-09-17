---
type: post120
title:  Projection
categories: XAP120NET, PRM
parent: querying-the-space.html
weight: 700
---



In some cases when querying the space for objects, only specific properties of objects are required and not the entire object (delta read). The same scenario is also relevant when subscribing for notifications on space data changes, where only specific properties are of interest to the subscriber. For that purpose the Projection API can be used where one can specify which properties are of interest and the space will only populate these properties with the actual data when the result is returned back to the user. This approach reduces network overhead , garbage memory generation and serialization CPU overhead.


# Example

Projections are supported using a [SqlQuery](./query-sql.html) or [ID Queries](./query-by-id.html). Below is a simple example that demonstrates reading a `Person` object where only the 'FirstName' and 'LastName' properties are returned with the query result array. All other `Person` properties will not be returned:


```csharp
public class Person
{
  ...
  [SpaceId(AutoGenerate = false)]
  public Long Id {get; set;}
  public String FirstName {get; set;}
  public String LastName {get; set;}
  public String Address {get; set;}
  ...
}

ISpaceProxy space = //... obtain a Space reference.
Long id = //... obtain the space object ID.
Person result = space.Read<Person>(new IdQuery<Person>(id) {Projections = new []{"FirstName", "LastName"});
```

With the above example a specific Person is being read but only its 'FirstName' and 'LastName' will contains values and all the other properties will contain a `null` value.

You may use the same approach with the `SqlQuery` or `IdsQuery`:


```csharp
SqlQuery<Person> query = new SqlQuery<Person>("") {Projections = new []{"FirstName", "LastName"}};
Person result[] = space.ReadMultiple(query);

IdsQuery<Person> idsQuery = new IdsQuery<Person>(new Long[]{id1,id2}) {Projections = new []{"FirstName", "LastName"};
Person result[] = space.ReadByIds(idsQuery).ResultsArray;
```

The [SpaceDocument](./document-api.html) support projections as well:


```csharp
SqlQuery<SpaceDocument> docQuery = new SqlQuery<SpaceDocument>(typeof(Person).Name ,"")
  {Projections = new []{"FirstName", "LastName"}};
SpaceDocument docresult[] = space.ReadMultiple(docQuery);
```


You can also use projection for nested properties:


```csharp
SqlQuery<SpaceDocument> docQuery = new SqlQuery<SpaceDocument>(typeof(Person).Name ,"")
  {Projections = new []{"Address.Street", "Address.ZipCode"}};
SpaceDocument docresult[] = space.ReadMultiple(docQuery);
```



# Supported Operations

A projection is defined for any operation that returns data from the Space. Therefore id-based or query-based operations support projections. You can use the Projection API with `Read`,`Take`,`ReadById`,`TakeById`,`ReadMultiple` and `TakeMultiple` operations. When performing a `Take` operation with projection, the entire Object will be removed from the space, but the result returned to the user will contain only the projected properties.

You can use projections with a [Notify Container](./notify-container.html), when subscribing to notifications. You can use it with a [Polling Container](./polling-container.html), when consuming Space Objects. You can also create a [Local View](./local-view.html) with templates or a `View` using projections. The local view will maintain the relevant objects, but the view of the data will contain only the projected properties.
Both dynamic and fixed properties can be specified - the syntax is the same. As a result, when providing a property name which is not part of the property set, it will be treated as a dynamic property: That is, if there is no like-named dynamic property present on a query result Object, then the property will be ignored entirely (and no Exception will be thrown). Please note that a result may contain multiple objects, each with a different combination of properties (fixed and/or dynamic) - each object will be treated individually when applying projections to it.

# Considerations

1. You can't use a projection on [Local Cache](./local-cache.html), as the Local Cache needs to contain fully constructed objects. Reconstructing an Object locally with projection would only negatively impact performance.
1. You can't use a projection to query a Local View for the same reason as for Local Cache. However, you can create a Local View with a projection template in which case the Local View will contain the Objects in their projected form.

# Working Examples

{{%refer%}}
[This repository](https://github.com/GigaSpaces/gs-executor-remoting/) (Scala) contains an integration test that performs projection on a query in the context of [Executor Based Remoting](./executor-based-remoting.html). Relevant lines of code (Scala) are [here](https://github.com/GigaSpaces/gs-executor-remoting/blob/master/src/test/scala/com/gigaspaces/sbp/WatchRepairSuite.scala#L124).
{{%/refer%}}
