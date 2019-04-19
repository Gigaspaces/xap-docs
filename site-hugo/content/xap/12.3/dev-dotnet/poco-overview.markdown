---
type: post123
title:  PONO Support
categories: XAP123NET, PRM
parent: none
weight: 200
canonical: auto
---


XAP allows you to store and retrieve PONOs to and from the Space. This section introduces the basic concepts and Dynamic Properties.

A data unit stored in the space is called an **Entry**.  Plain .NET objects are written to the Space and then read back. The API is .NET-friendly, and supports storing and retrieving .NET objects, which are transformed into Space entries under the hood. For example, when the program writes a `Message` object with a `String` property called `Text`, it is actually stored as an entry whose type name is Message, which holds one string property called Text.

Most of the time, the terms object and entry are used interchangeably, because the meaning is clear from the context, and the semantics are trivial. However, it is important to understand the difference between them. Moreover, some  features rely on applying semantics to an object to get a specific entry behavior. This page explains how objects are transformed to (and from) entries, and how that process can be controlled and customized.

# Including/Excluding Data from an Entry

By default, all public members (fields and properties) in a class are included in the entry, whereas non-public members are excluded. The common design practice is to expose data via public properties that encapsulate protected/private fields, so usually there's no need to adjust this behavior. However, it can be modified for exceptional cases.

## Customizing a Specific Class

To customize a specific class, apply a `[SpaceClass]` attribute on the class, and use `IncludeProperties` and/or `IncludeFields` to specify which members should be included in the entry. Both `IncludeProperties` and `IncludeFields` are an `IncludeMembers` enumeration, which can be set to one of the following:

- `IncludeMembers.All` -- all members are included.
- `IncludeMembers.Public` -- public members are included, and non-public members are excluded.
- `IncludeMembers.None` -- all members are excluded.

### Example 1.1 -- Default Behavior


```csharp
public class Person {...}
```

This is actually equivalent to the following declaration:


```csharp
[SpaceClass(IncludeFields=IncludeMembers.Public, IncludeProperties=IncludeMembers.Public)]
public class Person {...}
```

### Example 1.2 -- Exclude All Properties and Include All Fields (even Private)


```csharp
[SpaceClass(IncludeFields=IncludeMembers.All, IncludeProperties=IncludeMembers.None)]
public class Person {...}
```

## Customizing a Specific Member

To customize a specific field/property, apply a `[SpaceProperty]` to include it, or a `[SpaceExclude]` to exclude it. These settings override the class-level settings.

### Example 1.3 -- Storing All Person Properties except the Password Property


```csharp
public class Person
{
    [SpaceExclude]
    public String Password {...}
}
```

{{% note "Properties with Separate Accessors "%}}
Starting with .NET 2.0, properties can have separate accessors for getters and setters (for example, a public getter and private setter). In these cases, if either the getter or the setter is public, the property is considered public (setting `IncludeProperties=IncludeMembers.Public` includes the property in the entry).
{{% /note %}}

{{% note "Read-Only Properties "%}}
Read-only properties (getter, without setter), are included in the entry, but when the object is deserialized, the value is not restored because there's no setter. This enables the Space to be queried using such properties. There are two common scenarios for read-only properties:

- Calculated value -- the property returns a calculated value based on other fields/properties. This isn't a problem because no data is lost due to the 'missing' setter.
- Access protection -- the class designer is meant to protect the property from outside changes. This can be problematic because the field value is lost. To prevent this, consider adding a private setter or excluding the property, and including the field (as explained next).
{{% /note %}}

# Indexing

If a property is used frequently when querying the Space, you can instruct the Space to index it for faster retrieval using the `[SpaceProperty]` attribute, and specifying `Index=SpaceIndexType.Basic`. For example:


```csharp
public class Person
{
    [SpaceProperty(Index=SpaceIndexType.Basic)]
    public String UserID {...}
}
```

{{% note "Indexing Pros and Cons "%}}
Indexing a property speeds up queries that include the property, but slows down write operations for that object (because the Space has to index the property). For this reason indexing is off by default, and the user decides which fields should be indexed.
{{% /note %}}

# Object ID vs. Entry ID

## The Problem

Examine the following piece of code:

### Example 2


```csharp
Message message = new Message();
message.Text = "Same Same, But Different";
proxy.Write(message);
proxy.Write(message);
```

If you execute it, and examine the Space in the GigaSpaces Management Center, you'll see two different entries with the same text, even though from the .NET perspective there's only one object. You should also see an additional column called **UID**, which is not part of our .NET object and which contains a unique identifier that distinguishes the entries from each other. This unique identifier is commonly referred to as a **Space ID**.

What happened behind the scenes? When a write operation executes, a new entry is created and the properties are copied from the object to the entry. Each entry contains an additional special hidden UID property, which we ignored and left as null. When the Space receives an entry to store, it verifies that there's no stored entry with the same UID, and if the UID is null it generates a unique one. In our scenario, because the UID was ignored, the second write operation resulted in a separate entry containing the same data with a different UID.

So how do we utilize the Space ID?

## Exposing the Generated Space ID

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

If you run the code from **example 2** again, you'll see that the second write fails with an `EntryAlreayInSpaceException`. If you examine the newly added `MessageID` property in the debugger, you'll see that even though we didn't set it, it contains a unique identifier string.

When a property is marked as `[SpaceID(AutoGenerate = true)]`, it is mapped to the entry's UID. On the first write operation the `MessageID` was null, so the entry UID was null, and the Space generated a UID for it. Before the operation was completed, the generated UID was copied back to the `MessageID` property, as the debugger shows. On the second write operation, the Space again creates an entry and maps the object data to the entry, but this time the `MessageID` is no longer empty, so the entry UID is not empty. The Space checked if the UID is unique, discovered that there's another entry with the same UID, and aborted the operation.

Next, we'll see how to make the Space use the object when generating the UID.

## Controlling the Generated Space ID

Modify the `SpaceID` declaration from `true` to `false`:


```csharp
[SpaceID(AutoGenerate = false)]
public String MessageID {...}
```


Queries performed with the UID are much faster, because the query mechanism can reduce the result set efficiently.

Two modes of SpaceID are supported:

- Specify `[SpaceID(AutoGenerate=true)]` on the property that should hold the generated ID if you want the Space to automatically generate the UID. A SpaceID field that has AutoGenerate=true must be of type `string`.
- Specify `[SpaceID(AutoGenerate=false)]` on a property if you want the Space to generate the UID using a specific value of that property.

The default is `AutoGenerate=false`. Only one property in a class can be marked as a SpaceID property.

{{% note "Note"%}}
There is no need to explicitly index a field that is marked as SpaceID, because it is already indexed.
{{%/note%}}

# Under the Hood

Each public property/field in the object is mapped to a property of the same name and type in the entry. Non-public properties and fields are ignored. This means that any object can be stored in the Space; it does not have to inherit from a specific base class, implement an interface, or have any attributes. The only requirement is to have a parameter-less constructor (it doesn't have to be public), so the object can be created when it is retrieved from the Space.

While this generic approach solves simple scenarios easily, in some cases it is not enough. For example, you may want to exclude a specific property from being stored in the Space, or specify that a certain property should be indexed for faster performance. To that end, you can use a set of .NET attributes to control how an object is mapped to an entry. If you don't want to (or can't) use XAP.NET attributes in your code, you can create an XML file that defines those behaviors, commonly called `gs.xml`.

{{% note "Info" %}}
Working with .NET attributes is usually simpler and easier than working with external XML files, so this page demonstrates all the features using attributes. However, every feature shown here can also be implemented using `gs.xml`.

Mapping a .NET object to a Space entry does not involve .NET serialization, which means that the `[Serializable]` indication is not required, and in fact ignored. Even so, it is a good design practice to mark all objects stored in the space as `[Serializable]`, to maintain .NET standards.
{{%/note%}}

# Routing

When working with a clustered Space, one of the properties in a class is used to determine the routing behavior of that class within the cluster (how instances of that class are partitioned across the cluster's nodes). The routing property is determined according to the following rules:

1. The property marked with `[SpaceRouting]` attribute is used.
2. Otherwise, the property marked with `[SpaceID]` is used.
3. Otherwise, the first indexed property in alphabetical order is used.
4. Otherwise, the first property in alphabetical order is used.

Note that only one property in a class can be marked as a routing property.

{{% tip "Declare the routing property explicitly "%}}
We recommend explicitly declaring which property is the routing property, and not to rely on rules 2 and onward. Relying on those rules can lead to confusing problems (for example, if the SpaceID is changed, or an index is added to a property). Explicitly declaring the routing property makes your code clearer and less error-prone.
{{% /tip %}}

# Versioning

The Space can keep track of an object's version (how many times it was written/updated in the Space), and provide optimistic concurrency using that version information. For this reason, the Space needs to store the object's version in some property in the object. To specify that a property should be used for versioning, mark it with a `[SpaceVersion]` attribute. If no property is marked as a Space version, the Space does not store version information for that class.

Only one property in a class can be marked as a version property, and it must be of type `int`.

# NullValue

When a class contains a field or a property that is not a nullable type, (for example, a primitive such as `int` or a struct such as `DateTime`), it is recommended to specify a null value for it that will be used when querying the Space for that class. The `NullValue` attribute instructs the Space to ignore this field when performing matching or partial update, when the content of the field in the template equals the defined `NullValue`.

{{% note "Nullables "%}}
We recommend avoiding the use of such fields and properties, and the need to define null values, by wrapping them with their corresponding Nullable, for instance Nullable&lt;int&gt; or Nullable&lt;DateTime&gt;.
{{% /note %}}

To specify a null value, the field or property should be marked with the `[SpaceProperty(NullValue = ?)]` attribute:

### Example 3.1 - Null Value on a Primitive `int`


```csharp
public class Person
{
    [SpaceProperty(NullValue = -1)]
    public int Age {...}
}
```

### Example 3.2 - Null Value on `DateTime`


```csharp
public class Person
{
    [SpaceProperty(NullValue = "1900-01-01T12:00:00")]
    public DateTime BornDate {...}
}
```

# Mapping

By default, the name of the class in the Space is the fully-qualified class name (including namespace), and the property/field names in the Space are equal to the .NET name. In some cases, usually in interoperability scenarios, you may have to map your .NET class name and properties to different names in the Space using the `AliasName` property on `[SpaceClass]` and `[SpaceProperty]`. For example, the following .NET Person class contains mapping to an equivalent Java Person class:


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

For more information, refer to [Platform Interoperability](./interoperability.html).

{{% note "AliasName and SqlQuery "%}}
When using Space SqlQuery on an object with properties that are aliased, the query text must use the aliased property names. For more information about SqlQuery, refer to [Sql Query](./query-sql.html).
{{% /note %}}

# Persistency

The Space can be attached to an external data source, and persist its classes through it. It can be specified whether a certain class should be persisted or not. To do this, use the `[SpaceClass(Persist=true)]` or `[SpaceClass(Persist=false)]` class level attribute. The default is `[SpaceClass(Persist=true)]`.


```csharp
[SpaceClass(Persist=false)]
public class Person {...}
```

# Replication

Some cluster topologies have replication defined, which means that some or all of the data is replicated between the Spaces. In this case, it can be specified whether each class should be replicated or not using the `[SpaceClass(Replicate=true)]` or `[SpaceClass(Replicate=false)]` class level attribute. The default is `[SpaceClass(Replicate=true)]`.


```csharp
[SpaceClass(Replicate=false)]
public class Person {...}
```

# FIFO

A class can be marked to operate in FIFO mode, which means that all the inserts, removals and notifications of this class should be done in First-in-First-out mode. You can specify whether each class should operate in FIFO mode or not using the `[SpaceClass(Fifo=true)]` or `[SpaceClass(Fifo=false)]` class level attribute. The default is `[SpaceClass(Fifo=false)]`.


```csharp
[SpaceClass(Fifo=true)]
public class Person {...}
```
