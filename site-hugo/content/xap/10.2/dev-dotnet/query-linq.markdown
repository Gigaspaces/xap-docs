---
type: post102
title:  LINQ
categories: XAP102NET
parent: querying-the-space.html
weight: 800
canonical: auto
---




XAP.NET includes a custom LINQ provider, which enables developers to take advantage of their existing .NET skills to query the space without learning a new language.
To enable the provider add the following `using` statement:


```csharp
using GigaSpaces.Core.Linq; 
```

This brings the `Query<T>` extension method into scope, which is the entry point for writing LINQ queries. For example: 

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.Name == "Smith" 
            select p; 

foreach (var person in query) 
{ 
    // ...
} 
```

Another option is to convert the LINQ query to a space query, which can be used with any of the space proxy query operations. For example: 

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.Name == "Smith" 
            select p; 
var result = spaceProxy.Take<Person>(query.ToSpaceQuery()); 
```
And finally, you can create an `ExpressionQuery` with a predicate expression: 

```csharp
var result = spaceProxy.Take<Person>(new ExpressionQuery<Person>(p => p.Name == "Smith")); 
```
{{%tip%}}While LINQ is a great syntax for querying a data source, it cannot leverage XAP-specific features (removing results, batching, fifo, transactions, notifications and more). This gap is bridged with `ExpressionQuery`, which allows you to use LINQ with any space query operation.{{%/tip%}}

{{%note%}}
Only LINQ queries that can be translated to an equivalent [SQLQuery](./query-sql.html) are supported. A LINQ query that cannot be translated will throw an exception at runtime with a message which indicates which part of the query is not supported.
{{%/note%}}

# Indexing 

It is highly recommended to use indexes on relevant properties to increase performance. For more information see [Indexing](./indexing.html). 

# Supported LINQ operators 

{{%panel "The following LINQ operators are supported:"%}}

- [Any](http://msdn.microsoft.com/en-us/library/system.linq.queryable.any) - Returns true if there are any entries matching the query in the space, false otherwise.
- [Count](http://msdn.microsoft.com/en-us/library/system.linq.queryable.count) - Returns `int` `Count` of entries.
- [LongCount](http://msdn.microsoft.com/en-us/library/system.linq.queryable.longcount) - Same as `Count`, but returns `long` instead of `int`.
- [Single](http://msdn.microsoft.com/en-us/library/system.linq.queryable.single) - Returns the only matching entry from the space. Throws an exception if there are no matching entries or more than one match.
- [SingleOrDefault](http://msdn.microsoft.com/en-us/library/system.linq.queryable.singleordefault) - Returns the only matching entry from the space, or null if there are no matching entries. Throws an exception if there is more than one match.
- [OrderBy](http://msdn.microsoft.com/en-us/library/system.linq.queryable.orderby)/[OrderByDescending](http://msdn.microsoft.com/en-us/library/system.linq.queryable.orderbydescending)/[ThenBy](http://msdn.microsoft.com/en-us/library/system.linq.queryable.thenby)/[ThenByDescending](http://msdn.microsoft.com/en-us/library/system.linq.queryable.thenbydescending) - Specifies the order of the results.
- [Select](http://msdn.microsoft.com/en-us/library/system.linq.queryable.select) - Specifies if the entire entry is returned or a subset of its properties (see [Projection](#projection)).
- [Where](http://msdn.microsoft.com/en-us/library/system.linq.queryable.where) - Specifies the criteria used for querying the space (see [predicates](#predicates))
{{%/panel%}}

# Predicates 

The XAP LINQ provider supports various operators, as explained below. 
For the following code samples, assume the following classes are defined: 

```csharp
public class Person 
{ 
    public String Name {get; set;} 
    public int NumOfChildren {get; set;} 
    public ICollection<String> Aliases {get; set;} 
    [SpaceProperty(StorageType = StorageType.Document)] 
    public Address HomeAddress {get; set;} 
    [SpaceProperty(StorageType = StorageType.Document)] 
    public IDictionary<String, Address> Addresses {get; set;} 
    [SpaceProperty(StorageType = StorageType.Document)] 
    public Car[] Cars {get; set;} 
} 

public class Address 
{ 
    public String City {get; set;} 
    public String Street {get; set;} 
} 

public class Car 
{ 
    public String Color {get; set;} 
    public String Manufacturer {get; set;} 
} 

```

## Equality Operators 
Use the standard `==` and `!=` operators for equals/not equals conditions, respectively. For example, to query for entries whose *Name* is *"Smith"*: 

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.Name == "Smith" 
            select p; 
```

## Comparison Operators 
Use the standard `>`, `>=`, `<`, and `<=` for comparisons, respectively. For example, to query for entries whose *NumOfChildren* is greater than *2*: 

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.NumOfChildren > 2 
            select p; 
```

## Conditional Operators 
Use the standard `&&` and `||` for conditional and/or expressions, respectively. For example, to query for entries whose *Name* is *"Smith"* and *NumOfChildren* is greater than *2*: 

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.Name == "Smith" && p.NumOfChildren > 2 
            select p; 
```

## Nested Paths 

Nested paths can be traversed and queried using the dot (`.`) notation. For example, to query for entries whose *HomeAddress*'s *Street* equals *Main*: 

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.HomeAddress.Street == "Main" 
            select p; 
```

Dictionary entries can be traversed as well. For example, to query for entries whose *Addresses* contains a *Home* key which maps to an `Address` whose *Street* equals *Main*: 

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.Addresses["Home"].Street == "Main"
            select p; 
```

{{%refer%}}
By default user-defined types are stored in the space in a binary format, which cannot be queried. If the path includes a user-defined type, the relevant property's storage type should be set to *Document*. For more information refer to [Property Storage Type](./poco-storage-type.html).
{{%/refer%}}

## Sub-strings 

The [System.String](http://msdn.microsoft.com/en-us/library/System.String) methods [Contains(String)](http://msdn.microsoft.com/en-us/library/dy85x1sa), [StartsWith(String)](http://msdn.microsoft.com/en-us/library/baketfxw) and [EndsWith(String)](http://msdn.microsoft.com/en-us/library/2333wewz) can be used to match sub-strings of a member. For example, to query for entries whose *Name* ends with *"Smith"*: 

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.Name.EndsWith("Smith") 
            select p; 
```

{{%info%}}
The `StartsWith` and `EndsWith` methods have multiple overloads, but only the single-parameter overload is supported in this LINQ provider.
{{%/info%}}

## Collection Membership 

The [Enumerable.Contains(T value)](http://msdn.microsoft.com/en-us/library/bb352880) extension method can be used to check if any of the collection match a specific value. For example, to query for entries whose *Aliases* contains *"Smith"*: 

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.Aliases.Contains("Smith") 
            select p; 
```

In addition, the [Enumerable.Any(Func(T, bool))](http://msdn.microsoft.com/en-us/library/bb534972) extension method can be used to check if any of the collection items match a specific predicate. For example, to query for entries whose *Cars* contains a red honda: 

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.Cars.Any(c => c.Color == "Red" && c.Manufacturer == "Honda") 
            select p; 
```

Another option is to test if the member is part of a collection, (aka IN clause in traditional SQL). For example, to query for entries whose *Name* is one of the items of a given array:

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where new String[] {"Smith", "Doe"}.Contains(p.Name) 
            select p; 
```

{{%info%}}
By default user-defined types are stored in the space in a binary format, which cannot be queried. If the path includes a user-defined type, the relevant property's storage type should be set to *Document*. For more information refer to [Property Storage Type](./poco-storage-type.html).
{{%/info%}}

# Projection 

Projection is useful when only a subset of an entry's properties are required - instead of returning the entire entry, the query can declare which properties should be returned. This information is passed down all the way to the server which executes the query and yields the results, so that only the relevant properties are transmitted back, which reduces network traffic and improves performance.

For example, to query for entries whose *Name* ends with *"Smith"* and return only their *Name*: 

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.Name.EndsWith("Smith") 
            select p.Name; 
```

To return both the *Name* and *HomeAddress*:

```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.Name.EndsWith("Smith") 
            select new {p.Name, p.HomeAddress};
```

In this case the result will be an anonymous class with 2 properties. Since anonymous types are only useful within the scope of the method in which they're defined, you may prefer using `ExpressionQuery` with projections instead:


```csharp
var query = new ExpressionQuery<Person>(p => p.Name.EndsWith("Smith"));
query.Projections = new List<String> {"Id", "HomeAddress"};
```

In this case the result is the original type (`Person`), but only the projected properties are set and the rest of the properties are nulls (or default values). 

# Batch results 

Consider the following query: 

```csharp
var query = from p in spaceProxy.Query<Person>() select p; 
foreach (var person in query) 
{ 
    // ... 
} 
```

The default implementation is to execute a `ReadMultiple` under the hood, which retrieves all the matching entries at once, then enumerates them one by one. However, if the size of the result is large, two potential problems can occur:
 
- All the entries must be fetched before processing starts, i.e. the last entry must be loaded before the first entry is iterated. 
- The size of the result might be too large for the client's memory, in which case the application will fail with an out of memory exception. 

The solution to both problem is the same - batching. For example, the previous example can be modified as such: 

```csharp
var query = (from p in spaceProxy.Query<Person>() select p).Batch(100); 
foreach (var person in query) 
{ 
    // ... 
} 
```
The `Batch()` extension method instructs the provider to retrieve the results in batches not exceeding 100 entries each. This both protects the memory usage and allows processing to start before all entries are retrieved. 

{{%info%}}
Batching is suitable for large result sets, but on small ones it actually slows performance down, as it require multiple remote calls to the space to retrieve the data instead of fetching it all at once.
{{%/info%}}

# Aggregation

XAP lets you perform aggregations across the Space. There is no need to retrieve the entire data set from the Space to the client side , iterate the result set and perform the aggregation. The Aggregators allow you to perform the entire aggregation activity at the Space side avoiding any data retrieval back to the client side. Only the result of each aggregation activity performed with each partition is returned back to the client side where all the results are reduced and returned to the client application.


{{%learn "./aggregators.html"%}}
