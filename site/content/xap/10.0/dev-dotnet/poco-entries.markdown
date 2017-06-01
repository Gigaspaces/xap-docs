---
type: post100
title:  Overview
categories: XAP100NET
parent: poco-overview.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}



A data unit stored in the space is called an **Entry**. However, as you've seen in [Writing Your First Application](./dotnet-your-first-data-grid-application.html), there was no entry in the code - the program simply wrote a plain .NET object to the space and then took it. The API is .NET-friendly, and supports storing & retrieving .NET objects, which are transformed into space entries under the hood. For example, when the program wrote a `Message` object with a `String` property called `Text`, it was actually stored as an entry, whose type name was Message, which held one string property called Text.

Most of the time, the terms object and entry are used interchangeably, because the meaning is clear from the context, and the semantics are trivial. However, it is important to understand the difference between them. Moreover, some  features rely on applying semantics to an object to get a specific entry behavior. This page explains how objects are transformed to (and from) entries, and how that process can be controlled and customized.



# Including/Excluding Data from an Entry

By default, all public members (fields and properties) in a class are included in the entry, whereas non-public members are excluded. Since the common design practice is to expose data via public properties that encapsulate protected/private fields, usually there's no need to adjust this behavior. However, it can be adjusted for exceptional cases.

## Customizing a Specific Class

To customize a specific class, apply a `[SpaceClass]` attribute on the class, and use `IncludeProperties` and/or `IncludeFields` to specify which members should be included in the entry. Both `IncludeProperties` and `IncludeFields` are an `IncludeMembers` enumeration, which can be set to one of the following:

- `IncludeMembers.All` -- all members are included.
- `IncludeMembers.Public` -- public members are included, and non-public members are excluded.
- `IncludeMembers.None` -- all members are excluded.

#### Example 1.1 -- The default behavior


```csharp
public class Person {...}
```

This is actually equivalent to the following declaration:


```csharp
[SpaceClass(IncludeFields=IncludeMembers.Public, IncludeProperties=IncludeMembers.Public)]
public class Person {...}
```

#### Example 1.2 -- To exclude all properties and include all fields, even private ones:


```csharp
[SpaceClass(IncludeFields=IncludeMembers.All, IncludeProperties=IncludeMembers.None)]
public class Person {...}
```

## Customizing a Specific Member

To customize a specific field/property, apply a `[SpaceProperty]` to include it, or a `[SpaceExclude]` to exclude it. These settings override the class-level settings.

#### Example 1.3 -- Storing all the Person properties except the Password property


```csharp
public class Person
{
    [SpaceExclude]
    public String Password {...}
}
```

{{% info "Properties with Separate Accessors "%}}
Starting with .NET 2.0, properties can have separate accessors for getters and setters (e.g. public getter and private setter). In such cases, if either the getter or the setter is public, the property is considered public (i.e. setting `IncludeProperties=IncludeMembers.Public` includes the property in the entry).
{{% /info %}}

{{% info "Read-Only Properties "%}}
Read-only properties (getter, without setter), are included in the entry, but when the object is de serialized, the value is not restored, since there's no setter. This enables the space to be queried using such properties. There are two common scenarios for read-only properties:

- Calculated value -- the property returns a calculated value based on other fields/properties. This isn't a problem, since no data is lost due to the 'missing' setter.
- Access protection -- the class designer wishes to protect the property from outside changes. This is likely to be a problem, since the field value is lost. To prevent this problem, consider adding a private setter, or excluding the property, and including the field (as explained next).
{{% /info %}}

# Indexing

If a property is used frequently when querying the space, you can instruct the space to index it for faster retrieval, by using the `[SpaceProperty]` attribute, and specifying `Index=SpaceIndexType.Basic`. For example:


```csharp
public class Person
{
    [SpaceProperty(Index=SpaceIndexType.Basic)]
    public String UserID {...}
}
```

{{% info "Indexing Pros and Cons "%}}
Indexing a property speeds up queries which include the property, but slows down write operations for that object (since the space needs to index the property). For that reason, indexing is off by default, and it's up to the user to decide which fields should be indexed.
{{% /info %}}

# Object ID vs. Entry ID

## The Problem

Examine the following piece of code:

#### Example 2


```csharp
Message message = new Message();
message.Text = "Same Same, But Different";
proxy.Write(message);
proxy.Write(message);
```

If you execute it, and examine the space in the GigaSpaces Management Center, you will see two different entries with the same text, even though from the .NET perspective, there's only one object. You should also see an additional column called **UID**, which is not part of our .NET object, and which contains a unique identifier that distinguishes the entries from each other. This unique identifier is commonly referred to as a **Space ID**.

What happened behind the scenes? When a write operation executes, a new entry is created, and the properties are copied from the object to the entry. Each entry contains an additional special hidden UID property, which we ignored and left as null. When the space receives an entry to store, it verifies that there's no stored entry with the same UID, and if the UID is null, it generates a unique one. In our scenario, because the UID was ignored, the second write operation resulted in a separate entry containing the same data, with a different UID.

So how do we utilize the Space ID?

## Exposing Generated Space ID

Let's add the following piece of code -- the `Message` class:


```csharp
private String _messageID;
[SpaceID(AutoGenerate = true)]
public String MessageID
{
    get { return _messageID; }
    set { _messageID = value; }
}
```

If you run the code from **example 2** again, you will see that the second write fails, with an `EntryAlreayInSpaceException`. If you examine the newly added `MessageID` property in the debugger, you will see that even though we didn't set it, it contains a unique identifier string.

When a property is marked as `[SpaceID(AutoGenerate = true)]`, it is mapped to the entry's UID. On the first write operation the `MessageID` was null, so the entry UID was null, and the space generated a UID for it. Before the operation was completed, the generated UID was copied back to the `MessageID` property, as the debugger shows. On the second write operation, the space again creates an entry, and maps the object data to the entry, but this time the `MessageID` is no longer empty, so the entry UID is not empty. The space checked if the UID is unique, discovered there's another entry with the same UID and aborted the operation.

Next, we'll see how to make the space use the object when generating the UID.

## Controlling the Generated Space ID

Modify the `SpaceID` declaration from `true` to `false`:


```csharp
[SpaceID(AutoGenerate = false)]
public String MessageID {...}
```


- Queries performed with the UID are much faster, since the query mechanism can reduce the result set efficiently.

There are two modes of SpaceID that are supported:

- If you want the space to automatically generate the UID for you, specify `[SpaceID(AutoGenerate=true)]` on the property which should hold the generated ID. A SpaceID field that has AutoGenerate=true specified, must be of type `string`.
- If you want the space to generate the UID using a specific property's value, specify `[SpaceID(AutoGenerate=false)]` on that property.

The default is `AutoGenerate=false`. Note that only one property in a class can be marked as a SpaceID property.

{{% note %}}
There is no need to explicitly index a field which is marked as SpaceID, because it is already indexed.
{{%/note%}}

# Under the Hood

Each public property/field in the object is mapped to a property of the same name and type in the entry. Non-public properties and fields are ignored. This means that any object can be stored in the space - it does not have to inherit from a specific base class, implement an interface, or have any attributes. The only requirement is to have a parameter less constructor (it doesn't have to be public), so the object can be created when being retrieved from the space.

While this generic approach solves simple scenarios easily, in some cases it is not enough. For example, you may want to exclude a specific property from being stored in the space, or specify that a certain property should be indexed for faster performance. For that end, you can use a set of .NET attributes to control how an object is mapped to an entry. If you don't want to (or can't) use XAP.NET attributes in your code, you can create an xml file that defines those behaviors, commonly called `gs.xml`.

{{% info %}}
Since working with .NET attributes is usually simpler and easier than working with external xml files, this page demonstrates all the features using attributes. However, every feature shown here can also be implemented using `gs.xml`.

Mapping a .NET object to a space entry does not involve .NET serialization, which means that the `[Serializable]` indication is not required, and in fact ignored. Even so, it is a good design practice to mark all objects stored in the space as `[Serializable]`, to keep in-line with .NET standards.
{{%/info%}}

# Routing

When working with a clustered space, one of the properties in a class is used to determine the routing behavior of that class within the cluster (i.e. how instances of that class are partitioned across the cluster's nodes). The routing property is determined according to the following rules:

1. The property marked with `[SpaceRouting]` attribute is used.
2. Otherwise, the property marked with `[SpaceID]` is used.
3. Otherwise, the first indexed property in alphabetical order is used.
4. Otherwise, the first property in alphabetical order is used.

Note that only one property in a class can be marked as a routing property.

{{% tip "Declare the routing property explicitly "%}}
It's highly recommended to explicitly declare which property is the routing property, and not rely on rules 2 and onward. Relying on those rules can lead to confusing problems (e.g. the SpaceID is changed, or an index is added to a property, etc.). Explicitly declaring the routing property makes your code clearer and less error-prone.
{{% /tip %}}

# Versioning

The space can keep track of an object's version (i.e. how many times it was written/updated in the space), and provide optimistic concurrency using that version information. For that reason, the space needs to store the object's version in some property in the object. To specify that a property should be used for versioning, mark it with a `[SpaceVersion]` attribute. If no property is marked as a space version, the space does not store version information for that class.

Note that only one property in a class can be marked as a version property, and it must be of type `int`.

# NullValue

When a class contains a field or a property of not a nullable type, (for instance a primitive such as `int` or a struct such as `DateTime`), it is recommended to specify a null value for it that will be used when querying the space for that class. The `NullValue` attribute instructs the space to ignore this field, when performing matching or partial update, when the content of the field in the template equals the defined `NullValue`.

{{% info "Nullables "%}}
It is recommended that you avoid the usage of such fields and properties, and the need to define null values, by wrapping them with their corresponding Nullable, for instance Nullable<int> or Nullable<DateTime>.
{{% /info %}}

To specify a null value, the field or property should be marked with the `[SpaceProperty(NullValue = ?)]` attribute:

#### Example 3.1 - Null value on a primitive int


```csharp
public class Person
{
    [SpaceProperty(NullValue = -1)]
    public int Age {...}
}
```

#### Example 3.2 - Null value on DateTime


```csharp
public class Person
{
    [SpaceProperty(NullValue = "1900-01-01T12:00:00")]
    public DateTime BornDate {...}
}
```

# Mapping

By default, the name of the class in the space is the fully-qualified class name (i.e. including namespace), and the properties/fields names in the space equal to the .NET name. In some cases, usually in interoperability scenarios, you may need to map your .NET class name and properties to different names in the space. You can do that using the `AliasName` property on `[SpaceClass]` and `[SpaceProperty]`. For example, the following .NET Person class contains mapping to an equivalent Java Person class:


```csharp
namespace MyCompany.MyProject
{
    [SpaceClass(AliasName="com.mycompany.myproject.Person")]
    public class Person
    {
        [SpaceProperty(AliasName="firstName")]
        public String FirstName {...}
    }
}
```

For more information, see [GigaSpaces.NET - Interoperability With Non .NET Applications](./interoperability.html).

{{% note "AliasName and SqlQuery "%}}
When using space SqlQuery on an object with properties which are aliased, the query text needs to use the aliased property names. For more information about SqlQuery, see [GigaSpaces.NET - Sql Query](./query-sql.html).
{{% /note %}}

# Persistency

The space can be attached to an external data source, and persist its classes through it. It can be specified whether a certain class should be persisted or not. To do this, use the `[SpaceClass(Persist=true)]` or `[SpaceClass(Persist=false)]` class level attribute. The default is `[SpaceClass(Persist=true)]`.


```csharp
[SpaceClass(Persist=false)]
public class Person {...}
```

# Replication

Some cluster topologies have replication defined, which means that some or all of the data is replicated between the spaces. In this case, it can be specified whether each class should be replicated or not, by using the `[SpaceClass(Replicate=true)]` or `[SpaceClass(Replicate=false)]` class level attribute. The default is `[SpaceClass(Replicate=true)]`.


```csharp
[SpaceClass(Replicate=false)]
public class Person {...}
```

# FIFO

A class can be marked to operate in FIFO mode, which means that all the inserts, removals and notifications of this class should be done in First-in-First-out mode. It can be specified whether each class should operate in FIFO mode or not, by using the `[SpaceClass(Fifo=true)]` or `[SpaceClass(Fifo=false)]` class level attribute. The default is `[SpaceClass(Fifo=false)]`.


```csharp
[SpaceClass(Fifo=true)]
public class Person {...}
```

