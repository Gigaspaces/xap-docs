---
type: post110net
title:  Template Matching
categories: XAP110NET
parent: querying-the-space.html
weight: 200
---

{{%ssummary%}}{{%/ssummary%}}

Template matching (a.k.a. Match by example) is a simple way to query the space - The template is a PONO of the desired entry type, and the properties which are set on the template (i.e. not null) are matched against the respective properties of entries of the same type in the space. Properties with null values are ignored (not matched).

Since by convention the default constructor usually initializes all the properties to `null` either implicitly or explicitly, in most cases it's enough to simply set the properties which should be matched, without bothering with explicitly setting null to the other properties. Note that setting two or more properties with non-null values provides an **AND** behavior.

{{% refer %}}
It is highly recommended to index one or more of the properties used in the template to speed up the matching process. For more information see [Indexing](./indexing.html).
If you require additional query options refer to [SQLQuery](./query-sql.html).
{{%/refer%}}

# Examples

The following examples assume the default constructor of **Person** initializes all its properties to `null`.

Read an entry of type **Person** whose **FirstName** property is **John**:


```csharp
Person template = new Person();
template.FirstName = "John";
Person person = spaceProxy.Read(template);
```

Read an entry of type **Person** whose **FirstName** is **John** and **LastName** is **Smith**:


```csharp
Person template = new Person();
template.FirstName = "John";
template.LastName = "Smith";
Person person = spaceProxy.Read(template);
```

If none of the properties are set, all the entries of the type are matched. For example, to count all entries of type **Person**:


```csharp
int numOfPersons = spaceProxy.Count(new Person());
```

If the template class is null, all the entries in the space are matched. For example, to clear all entries from the space:


```csharp
spaceProxy.Clear(null);
```

# Indexes

GigaSpaces XAP includes a sophisticated built-in real-time indexing engine (regardless whether the space is persistent or not) that maintains a hash and btree like indexes for each indexed Space Class attribute. If you store a large number of Space objects from the same class type in the space, consider defining one or more indexes for attributes used with template matching or [SQL Query](./query-sql.html). Defining indexes will improve the `Read/Take/ReadMultiple/TakeMultiple/Clear/Count` operations response time significantly. Remember, indexes impact `Write` and `Take` operations response time, so choose your indexed fields carefully - each index has an overhead. For more information see [Indexing](./indexing.html).

# Inheritance Support

Template Matching support inheritance relationships, so that entries of a sub-class are visible in the context of the super class, but not the other way around.
For example, suppose class **Citizen** extends class **Person**:


```csharp
spaceProxy.Write(new Person());
spaceProxy.Write(new Citizen());
// Count persons - should return 2:
int numberOfPersons = spaceProxy.Count(new Person());
// Count citizends - should return 1:
int numberOfCitizens = spaceProxy.Count(new Citizen());
```

{{% info%}}
Since all classes extends `Object`, a template of type `Object` will match all the entries in the space.
{{%/info%}}

# Partitioned cluster

When querying a partitioned cluster using a template, it is possible to use the routing property to control whether the query is broadcasted to the entire cluster or executed against a specific partition.
For more information see [Routing In Partitioned Spaces](./routing-in-partitioned-spaces.html).

# Primitive Types

Properties with primitive types pose a problem - a primitive type cannot be set to null. For example, suppose class **Person** has property **Age** of type **int**, and we wrote the following piece of code which writes and reads a person:


```csharp
// Create a person and write it to the space:
Person p1 = new Person();
p1.Age = 30;
spaceProxy.Write(p1);
// Read person from space:
Person p = spaceProxy.Read(new Person());
```

We expect **p** to hold the person we just wrote to the space, but in fact it will be null: since **age** is primitive it is implicitly initialized to 0 (zero) and cannot be set to null either implicitly or explicitly, which means we're actually matching for Persons whose age is 0 (zero).

To overcome this issue we can map a primitive value to null via the `[SpaceProperty(NullValue = ?)]` attribute. For example:


```csharp
public class Person
{
    private int age = -1;

    [SpaceProperty(NullValue = -1)]
    public int Age { get; set; }

    .......
}
```

We've indicated that `-1` should be treated as `null` when performing template matching, and initialized age to `-1` so users of Person class need not set it explicitly whenever they use it. For more information refer to [Object Metadata](./pono-attribute-annotations.html).

{{% note%}}
Properties of primitive types are implicitly boxed when stored in the space and unboxed when reconstructed to a PONO.
It is highly recommended to use the  primitive wrapper classes instead of primitives to simplify the code and avoid user errors.
{{%/note%}}

# Nested Template Matching

{{%refer%}}
Nested template matching is not supported - to match nested properties, collections and arrays use [SQLQuery](./query-sql.html).
{{%/refer%}}
