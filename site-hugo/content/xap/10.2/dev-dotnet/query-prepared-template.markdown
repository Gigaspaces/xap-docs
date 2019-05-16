---
type: post102
title:  Prepared Template
categories: XAP102NET
parent: querying-the-space.html
weight: 300
canonical: auto
---

{{% ssummary %}} {{% /ssummary %}}

When executing a query operation on the space, there's an overhead incurred by translating the query to an internal representation (in object templates the properties values are extracted using reflection, in [SQLQuery](./query-sql.html) the expression string is parsed to an expression tree). If the same query is executed over and over again without modification, that overhead can be removed by using **prepared templates**.

The `ISpaceProxy` interface provides a method called `Snapshot` which receives a template or query , translates it to an internal XAP query structure and returns a reference to that structure as `IPreparedTemplate<T>`. That reference can then be used with any of the proxy's query operations to execute queries on the space in a more efficient manner, since there's no need to translate or parse the query.

{{% info %}}
In previous versions the `Snapshot()` method was also used as a workaround for using SQLQuery with blocking operations. Starting 8.0 SQLQuery supports blocking operations out of the box so that workaround is no longer required.
{{%/info%}}

# Example

Use `ISpaceProxy.Snapshot` to create a prepared template from an object template or a [SqlQuery](./query-sql.html).

#### Creating a prepared template from an object


```csharp
    Person template= new Person();
    template.Age = 21;
    IPreparedTemplate<Person> preparedTemplate = proxy.Snapshot(template);
```

#### Creating a prepared template from SqlQuery


```csharp
    SqlQuery<Person> query = new SqlQuery<Person>(personTemplate, "Age >= ?");
    query.SetParameter(1, 21);
    IPreparedTemplate<Person> preparedTemplate = proxy.Snapshot(query);
```

{{% warning %}}
Using the `ISpaceProxy.Snapshot` method with complex SQL queries is not supported. For more information see [simple SQL queries](./query-sql.html#Simple SqlQuery).
{{%/warning%}}

After creating the prepared template, it can be passed as a template to the Read, Take, ReadMultiple, TakeMultiple, Count and Clear operations, as well as a template when registering for notification.

#### Taking an object from the space using the prepared template


```csharp
    Person person = proxy.Take(preparedTemplate);
```


