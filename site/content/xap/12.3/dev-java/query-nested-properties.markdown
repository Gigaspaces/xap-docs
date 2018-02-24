---
type: post123
title:  Nested Properties
categories: XAP123, OSS
parent: querying-the-space.html
weight: 400
---




The [SQL Query](./query-sql.html) page shows how to express complex queries on flat space entries (e.g. entries which are composed of scalar types like integers and strings), but in reality space entries are often composed of more complex types.
For example, a **Person** class may have:

- An **address** property of type **Address**, which is a user-defined class encapsulating address details (e.g. city, street).
- A **phoneNumbers** property of type `Map<String, String>`, encapsulating various phone numbers (e.g. home, work, mobile).
- A **groups** property of type `List<String>`, encapsulating various groups the person belongs to.

An application would likely need to execute queries which involve such properties (e.g. a person which lives in city X, or is part of some group Y, or has a specific home phone number). This page explains how GigaSpaces [SQLQuery](./query-sql.html) can be used to express such queries.

# Matching Paths

Matching a nested property is done by specifying a **Path** which describes how to obtain its value.

For example, suppose we have a **Person** class and an **Address** class, and **Person** has an **address** property of type **Address**:


```java
@SpaceClass
public class Person {
    private Address address;
    // Getter and setter methods are omitted for brevity.
}

public class Address implements Serializable {
    private String city;
    // Getter and setter methods are omitted for brevity.
}
```

The following example queries for a **Person** with an **address** whose **city** equals "**New York**":


```java
... = new SQLQuery<Person>(Person.class, "address.city = 'New York'");
```

{{% info %}}
Note that other properties (if any) in **address** which are not part of the criteria are ignored in the matching process.
{{%/info%}}

The number of levels in the path is unlimited.
For example, suppose the **Address** class has a **Street** class which encapsulates a **name** (String) and a **houseNum** (int).
The following example queries for a **Person** who live on "*Main**" street:


```java
... = new SQLQuery<Person>(Person.class, "address.street.name = 'Main'");
```

Naturally, any other feature of the SQL syntax can be used with paths:


```java
// Using parameters instead of fixed criteria expressions:
... = new SQLQuery<Person>(Person.class, "address.city = ?", "New York");
// Using other SQL comparison operands:
... = new SQLQuery<Person>(Person.class, "address.street.houseNum >= 10");
// Using SQL composition operands to express compound predicates:
... = new SQLQuery<Person>(Person.class, "address.city = 'New York' AND address.street.houseNum >= 10");
```

Paths can also be specified in **ORDER BY** and **GROUP BY** clauses:


```java
// Query for Persons in 'Main' street, sort results by city:
... = new SQLQuery<Person>(Person.class, "address.street.name = 'Main' ORDER BY address.city");

// Query for Persons in 'Main' street, group results by city:
... = new SQLQuery<Person>(Person.class, "address.street.name = 'Main' GROUP BY address.city");
```

{{% anchor NestedObjectQueryIndexes %}}

## Indexing

Indexing plays an important role in a system's architecture - a query without indexes can slow down the system significantly. Paths can be indexed to boost performance, similar to properties.
For example, suppose we've analyzed our queries and decided that **address.city** is commonly used and should be indexed:


```java
@SpaceClass
public class Person {
    private Address address;

    @SpaceIndex(path = "city")
    public Address getAddress() {
        return address;
    }
    // Setter method is omitted for brevity.
}
```

{{% info%}}
Note that since the index is specified on top of the **address** property, the `path` is "**city**" rather than "**address.city**".
For more information see the [Nested Properties Indexing](./indexing.html#Nested Properties Indexing) section under [Indexing](./indexing.html).
{{%/info%}}

{{% warning%}}
The type of the nested object must be a class - querying interfaces is not supported.
Nested properties' classes should be `Serializable`, otherwise the entry will not be accessible from remote clients.
{{%/warning%}}

# Nested Maps

The path syntax is tailored to detect `Map` objects and provide access to their content: when a property's key is appended to the path, it is implicitly resolved to the property's value (at runtime).

For example, suppose the **Person** class contains a **phoneNumbers** property which is a `Map`, encapsulating various phone numbers (e.g. home, work, mobile):


```java
@SpaceClass
public class Person {
    private Map<String, String> phoneNumbers;
    // Getter and setter methods are omitted for brevity.
}
```

The following example queries for a **Person** whose **phoneNumbers** property contains the key-value pair **"home" - "555-1234"**:


```java
... = new SQLQuery<Person>(Person.class, "phoneNumbers.home = '555-1234'");
```

{{% info%}}
A path can continue traversing from 'regular' properties to maps and back to 'regular' properties as needed.
Map properties are useful for creating a flexible schema - since the keys in the map are not part of the schema, the map can be used to add or remove data from a space object without changing its structure.
{{%/info%}}

## Indexing

Paths containing map keys can be indexed to boost performance, similar to 'regular' paths.
For example, suppose we've analyzed our queries and decided that **phoneNumbers.home** is commonly used and should be indexed:


```java
@SpaceClass
public class Person {
    private Address address;

    @SpaceIndex(path = "home")
    public Map<String, String> getPhoneNumbers() {
        return phoneNumbers;
    }
    // Setter method is omitted for brevity.
}
```

{{% info %}}
Note that since the index is specified on top of the **phoneNumbers** property, the `path` is "**home**" rather than "**phoneNumbers.home**".
For more information see the [Nested Properties Indexing](./indexing.html#Nested properties indexing) section under [Indexing](./indexing.html).
{{%/info%}}

{{% anchor collection-support %}}

# Nested Collections

The GigaSpaces SQL syntax supports a special operand `[*]`, which is sometimes referred to as the 'contains' operand. This operand is used in conjunction with collection properties to indicate that each collection item should be evaluated, and if at least one such item matches, the owner entry is considered as matched.

{{% info%}}
Arrays are supported as well, except for arrays of primitive types (int, boolean, etc.) which are are **not** supported - use the equivalent wrapper type (java.lang.Integer, java.lang.Boolean, etc.) instead.
{{%/info%}}

Suppose we have a type called **Dealer** with a property called **cars** (which is a list of strings).
The following example queries for a **Dealer** whose *cars* collection property contains the **"Honda"** String:


```java
... = new SQLQuery<Dealer>(Dealer.class, "cars[*] = 'Honda'");
```

Now suppose that **cars** is not a collection of Strings but of a user-defined class called **Car**, which has a string property called **company**.
The following example queries for a **Dealer** whose **cars** collection property contains a **Car** with **company** which equals **"Honda"**:


```java
... = new SQLQuery<Dealer>(Dealer.class, "cars[*].company = 'Honda'");
```

Matching collections within collections recursively is supported as well.
Suppose the **Car** class has a collection of strings called **tags**, to store additional information.
The following example queries for a **Dealer** which contains a **car** which contains a **tag** which equals "**Convertible**":


```java
... = new SQLQuery<Dealer>(Dealer.class, "cars[*].tags[*] = 'Convertible'");
```





{{<wbr>}}

## Multiple Conditions On Collection Items

The scope of the `[*]` operand is bounded to the predicate it participates in. When using it more than once, each occurrence is evaluated separately.
This behavior is useful when looking for multiple distinct items, for example: a dealer with several cars of different companies.
The following example queries for a **Dealer** which has both a **Honda** and a **Subaru**:


```java
... = new SQLQuery<Dealer>(Dealer.class, "cars[*].company = 'Honda' AND cars[*].company = 'Subaru'");
```

You can use parentheses to specify multiple conditions on the same collection item. See examples below:
The following example queries for a **Dealer** which has a **Car** whose *company* equals "**Honda**" and **color** equals "**Red**":


```java
... = new SQLQuery<Dealer>(Dealer.class, "cars[*](company = 'Honda' AND color = 'Red')");
```

{{% note "Caution "%}}
Writing that last query without parentheses will yield results which are somewhat confusing:


```java
... = new SQLQuery<Dealer>(Dealer.class, "cars[*].company = 'Honda' AND cars[*].color = 'Red'");
```


This query will match any **Dealer** with a **Honda** car and a **Red** car, but not necessarily the same car (e.g. a blue **Honda** and a **Red** Subaru).
{{% /note %}}

The following example queries for a **Dealer** which has a **Car** whose *company* equals "**Honda**" and **color** equals "**Red**" and one of the Car nested **Tags** List equals to "Convertible":

```java
... = new SqlQuery<Dealer>("Cars[*](company = 'Honda' AND color = 'Red' AND tags[*] = 'Convertible')");
```

Here is a graphical representation of this query:

![/attachment_files/nestedquery.jpg](/attachment_files/nestedquery.jpg)


{{%warning "The following is not supported"%}}


```java
... = new SqlQuery<Dealer>("Cars[*](company = 'Honda' OR color = 'Red' OR tags[*] = 'Convertible')");
```

As it does not make sense to perform an OR in this case.
{{%/warning%}}

#### In operation

Beginning 10.1, SQLQuery now supports `IN` when used with nested properties. For example, to search for a dealer with cars where company is either "Honda" or "Ford"

```java
... = new SQLQuery<Dealer>(Dealer.class, "cars[*].company in ('Honda', 'Ford') ");
```

## Indexing

Collection properties can be indexed to boost performance, similar to 'regular' paths, except that each item in the collection is indexed.
For example, suppose we've analyzed our queries and decided that **cars[*].company** is commonly used and should be indexed:


```java
@SpaceIndex(path = "[*].company")
public List<Car> getCars() {
  return this.cars;
}
```

{{% note %}}
Note that since the index is specified on top of the **cars** property, the `path` is \[*\].company rather than cars\[*\].company**.
The bigger the collection - the more memory is required to store the index at the server (since each item is indexed). Use with caution!
For more information see the [Collection Indexing](./indexing.html#Collection Indexing) section under [Indexing](./indexing.html).
{{%/note%}}


# Limitations

 
- The type of the nested object must be a class - querying interfaces is not supported.
- Nested properties' classes should be `Serializable`, otherwise the entry will not be accessible from remote clients.
- Arrays of primitive types (int, boolean, etc.) are not supported - use the equivalent wrapper type (java.lang.Integer, java.lang.Boolean, etc.) instead.

 

