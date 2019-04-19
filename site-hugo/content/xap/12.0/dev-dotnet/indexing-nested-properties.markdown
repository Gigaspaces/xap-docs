---
type: post120
title:  Nested Property Index
categories: XAP120NET, PRM
parent: indexing-overview.html
weight: 200
canonical: auto
---

{{% ssummary %}} {{% /ssummary %}}

# Overview
An index can be defined on a nested property to improve performance of nested queries - this is highly recommended.

Nested properties indexing uses an additional `[SpaceIndex]` attribute - `Path`.

# The SpaceIndex.Path Attribute

The `Path` attribute represents the path of the property within the nested object.

Below is an example of defining an index on a nested property:

{{%tabs%}}
{{%tab "  Single Index Annotation "%}}

```csharp
[SpaceClass]
public class Person
{
    //Properties
    ...
    // this defines and Extended index on the PersonalInfo.SocialSecurity property
    [SpaceIndex(Path = "SocialSecurity", Type = SpaceIndexType.Extended)]
    public Info PersonalInfo{ get; set; }
    // .....
}

public class Info
{
    public String Name {get; set;}
    public Address Address {get; set;}
    public DateTime Birthday {get; set;}
    public long SocialSecurity {get; set;}
    public int Id;
}

public class Address
{
    private int ZipCode {get; set;}
    private String Street {get; set;}
}
```
{{% /tab %}}

{{%tab "Multiple Indexes Annotation"%}}

```csharp
[SpaceClass]
public class Person
{
    //Properties
    ...

    // this defines several indexes on the same personalInfo property
    [SpaceIndex(Path = "SocialSecurity", Type = SpaceIndexType.Extended)]
    [SpaceIndex(Path = "Address.ZipCode", type = SpaceIndexType.Basic)})
    [SpaceProperty(StorageType = StorageType.Document)]
    public Info PersonalInfo{ get; set; }

    // this defines indexes on map keys
    [SpaceIndex(Path = "Key1", Type = SpaceIndexType.Basic)]
    [SpaceIndex(Path = "Key2", Type = SpaceIndexType.Basic)]
    public Dictionary<String, String> Table{ get; set; }
}

```

{{% /tab %}}

{{% /tabs %}}

The following is an example of query code that automatically triggers this index:


```csharp
SqlQuery<Person> query = new SqlQuery<Person>(
    "PersonalInfo.SocialSecurity<10000050L and PersonalInfo.SocialSecurity>=10000010");
```

{{%refer%}}
[SQL Query netsed Objects](./query-sql.html#nested-object-query)
{{%/refer%}}

# Nested Objects

By default, nested objects are kept in a binary form inside the space. In order to support nested matching, the relevant property should be stored as document, or as object if it is in an interoperability scenario and it has a corresponding Java class.

# Dictionary based Nested Properties

The same indexing techniques above are also applicable to Dictionary-based nested properties, which means that in the example above the `Info` and `Address` classes could be replaced with a `Dictionary<String,Object>`, with the dictionary keys representing the property names.

