---
type: post110
title:  Nested Properties
categories: XAP110NET
parent: querying-the-space.html
weight: 400
---

{{%ssummary%}}{{%/ssummary%}}

The [SQL Query](./query-sql.html) page shows how to express complex queries on flat space entries (entries which are composed of scalar types like integers and strings), but in reality space entries are often composed of more complex types.
For example, a **Person** class may have:

- An **Address** property of type **Address**, which is a user-defined class encapsulating address details (e.g. city, street).
- A **PhoneNumbers** property of type `Dictionary<String, String>`, encapsulating various phone numbers (e.g. home, work, mobile).
- A **Groups** property of type `List<String>`, encapsulating various groups the person belongs to.

An application would likely need to execute queries which involve such properties (e.g. a person which lives in city X, or is part of some group Y, or has a specific home phone number). This page explains how GigaSpaces [SQLQuery](./query-sql.html) can be used to express such queries.

# Matching Paths

Matching a nested property is done by specifying a **path** which describes how to obtain its value.

For example, suppose we have a **Person** class and an **Address** class, and **Person** has an **Address** property of type **Address**:


```csharp
[SpaceClass]
public class Person {
    public Address Address {set; get;}
    ......
}

[Serializable]
public class Address {
    public String City {set; get;}
    ......
}
```

The following example queries for a **Person** with an **Address** whose **City** equals "**New York**":


```csharp
... = new SqlQuery<Person>( "Address.City = 'New York'");
```

{{% info %}}
Note that other properties (if any) in **address** which are not part of the criteria are ignored in the matching process.
{{%/info%}}

The number of levels in the path is unlimited.
For example, suppose the **Address** class has a **Street** class which encapsulates a **name** (String) and a **houseNum** (int).
The following example queries for a **Person** who live on "**Main**" street:


```csharp
... = new SqlQuery<Person>("Address.Street.Name = 'Main'");
```

Naturally, any other feature of the SQL syntax can be used with paths:


```csharp
// Using parameters instead of fixed criteria expressions:
... = new SqlQuery<Person>("Address.City = ?", "New York");
// Using other SQL comparison operands:
... = new SqlQuery<Person>("Address.Street.HouseNum >= 10");
// Using SQL composition operands to express compound predicates:
... = new SqlQuery<Person>("Address.City = 'New York' AND address.street.houseNum >= 10");
```

Paths can also be specified in **ORDER BY** and **GROUP BY** clauses:


```csharp
// Query for Persons in 'Main' street, sort results by city:
... = new SqlQuery<Person>("Address.Street.Name = 'Main' ORDER BY Address.City");

// Query for Persons in 'Main' street, group results by city:
... = new SqlQuery<Person>("Address.Street.Name = 'Main' GROUP BY Address.City");
```

{{% anchor NestedObjectQueryIndexes %}}

## Indexing

Indexing plays an important role in a system's architecture - a query without indexes can slow down the system significantly. Paths can be indexed to boost performance, similar to properties.
For example, suppose we've analyzed our queries and decided that **address.city** is commonly used and should be indexed:


```csharp
[SpaceClass]
public class Person {
    [SpaceIndex(path = "City")]
    public Address Address {set; get;}

    ......
}
```

{{% info%}}
Note that since the index is specified on top of the **address** property, the `path` is "**City**" rather than "**Address.City**".
For more information see the [Nested Properties Indexing](./indexing.html#Nested Properties Indexing) section under [Indexing](./indexing.html).
{{%/info%}}

{{% warning%}}
The type of the nested object must be a class - querying interfaces is not supported.
Nested properties' classes should be `Serializable`, otherwise the entry will not be accessible from remote clients.
{{%/warning%}}

# Nested Dictionary

The path syntax is tailored to detect `Dictionary` objects and provide access to their content: when a property's key is appended to the path, it is implicitly resolved to the property's value (at runtime).

For example, suppose the **Person** class contains a **PhoneNumbers** property which is a `Dictionary`, encapsulating various phone numbers (e.g. home, work, mobile):


```csharp
[SpaceClass]
public class Person {
    public Dictionary<String, String> PhoneNumbers {set; get;}
   ........
}
```

The following example queries for a **Person** whose **PhoneNumbers** property contains the key-value pair **"Home" - "555-1234"**:


```csharp
... = new SqlQuery<Person>("PhoneNumbers.Home = '555-1234'");
```

{{% info%}}
A path can continue traversing from 'regular' properties to maps and back to 'regular' properties as needed.
Map properties are useful for creating a flexible schema - since the keys in the map are not part of the schema, the map can be used to add or remove data from a space object without changing its structure.
{{%/info%}}

## Indexing

Paths containing map keys can be indexed to boost performance, similar to 'regular' paths.
For example, suppose we've analyzed our queries and decided that **PhoneNumbers.Home** is commonly used and should be indexed:


```csharp
[SpaceClass]
public class Person {
    public Address Address {set; get;}

    [SpaceIndex(path = "Home")]
    public Dictionary<String, String> PhoneNumbers {set; get;}

    .......
}
```

{{% info %}}
Note that since the index is specified on top of the **PhoneNumbers** property, the `path` is "**Home**" rather than "**PhoneNumbers.Home**".
For more information see the [Nested Properties Indexing](./indexing.html#Nested properties indexing) section under [Indexing](./indexing.html).
{{%/info%}}

{{% anchor collection-support %}}

# Nested Collections

The GigaSpaces SQL syntax supports a special operand `[*]`, which is sometimes referred to as the 'contains' operand. This operand is used in conjunction with collection properties to indicate that each collection item should be evaluated, and if at least one such item matches, the owner entry is considered as matched.

{{% info%}}
Arrays are supported as well, except for arrays of primitive types (int, bool, etc.) which are are not supported - use the equivalent nullable type (int?, bool?, etc.) instead.
{{%/info%}}

Suppose we have a type called **Dealer** with a property called **Cars** (which is a list of strings).
The following example queries for a **Dealer** whose *cars* collection property contains the **"Honda"** String:


```csharp
... = new SqlQuery<Dealer>("Cars[*] = 'Honda'");
```

Now suppose that **Cars** is not a collection of Strings but of a user-defined class called **Car**, which has a string property called **company**.
The following example queries for a **Dealer** whose **cars** collection property contains a **Car** with **company** which equals **"Honda"**:


```csharp
... = new SqlQuery<Dealer>("Cars[*].Company = 'Honda'");
```

Matching collections within collections recursively is supported as well.
Suppose the **Car** class has a collection of strings called **Tags**, to store additional information.
The following example queries for a **Dealer** which contains a **Car** which contains a **Tag** which equals "**Convertible**":


```csharp
... = new SqlQuery<Dealer>("Cars[*].Tags[*] = 'Convertible'");
```


{{<wbr>}}

## Multiple Conditions On Collection Items

The scope of the `[*]` operand is bounded to the predicate it participates in. When using it more than once, each occurrence is evaluated separately.
This behavior is useful when looking for multiple distinct items, for example: a dealer with several cars of different companies.
The following example queries for a **Dealer** which has both a **Honda** and a **Subaru**:


```csharp
... = new SqlQuery<Dealer>("Cars[*].Company = 'Honda' AND Cars[*].Company = 'Subaru'");
```

You can use parentheses to specify multiple conditions on the same collection item. See examples:
The following example queries for a **Dealer** which has a **Car** whose *company* equals "**Honda**" and **color** equals "**Red**":


```csharp
... = new SqlQuery<Dealer>("Cars[*](Company = 'Honda' AND Color = 'Red')");
```


{{% note "Caution "%}}
Writing that last query without parentheses will yield results which are somewhat confusing:


```csharp
... = new SqlQuery<Dealer>("Cars[*].Company = 'Honda' AND Cars[*].Color = 'Red'");
```


This query will match any **Dealer** with a **Honda** car and a **red** car, but not necessarily the same car (e.g. a blue **Honda** and a **red** Subaru).
{{% /note %}}

The following example queries for a **Dealer** which has a **Car** whose *company* equals "**Honda**" and **color** equals "**Red**" and one of the Car nested **Tags** List equals to "Convertible":

```csharp
... = new SqlQuery<Dealer>("Cars[*](Company = 'Honda' AND Color = 'Red' AND Tags[*] = 'Convertible')");
```

#### Here is a graphical representation of this query:

![/attachment_files/nestedquery.jpg](/attachment_files/nestedquery.jpg)


{{%warning%}}
The following is not supported

```java
... = new SqlQuery<Dealer>("Cars[*](company = 'Honda' OR color = 'Red' OR tags[*] = 'Convertible')");
```

As it does not make sense to perform an OR in this case.
{{%/warning%}}


## Indexing

Collection properties can be indexed to boost performance, similar to 'regular' paths, except that each item in the collection is indexed.
For example, suppose we've analyzed our queries and decided that **Cars[*].Company** is commonly used and should be indexed:


```csharp
[SpaceIndex(path = "[*].Company")]
public List<Car> Cars{set; get;}

}
```

{{% note %}}
Note that since the index is specified on top of the **cars** property, the `path` is `[*].Company rather than cars[*].Company`.
The bigger the collection - the more memory is required to store the index at the server (since each item is indexed). Use with caution!
For more information see the [Collection Indexing](./indexing.html#Collection Indexing) section under [Indexing](./indexing.html).
{{%/note%}}


# Limitations

{{%vbar%}}
- The SQLQuery syntax for Nested Properties does not support the `IN` operation.
- The type of the nested object must be a class - querying interfaces is not supported.
- Nested properties' classes should be `Serializable`, otherwise the entry will not be accessible from remote clients.
- Arrays are supported as well, except for arrays of primitive types (int, bool, etc.) which are are not supported - use the equivalent nullable type (int?, bool?, etc.) instead.

{{%/vbar%}}
