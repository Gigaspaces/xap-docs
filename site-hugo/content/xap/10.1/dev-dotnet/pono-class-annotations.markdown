---
type: post101
title:  Class Annotations
categories: XAP101NET
parent: pono-annotation-overview.html
weight: 100
canonical: auto
---

{{% ssummary %}}{{% /ssummary %}}



The [GigaSpaces API](./the-gigaspace-interface-overview.html) supports class level decorations with PONOs. These can be specified via annotations on the space class source itself  for all class instances.


{{<wbr>}}

# Alias name

|                |             |
|----------------|-------------|
|Syntax     | AliasName |
|Argument   | String          |
|Description| By default, the name of the class in the Space is the fully-qualified class name (i.e. including namespace). In some cases, usually in interoperability scenarios, you may need to map your C# Class name and properties to different names in the Space.  |

Example:


```csharp
[SpaceClass(AliasName="com.mycompany.myproject.Person")]
public class Person {
//
}
```

{{%learn "./interoperability.html"%}}

# Persistence

|                |             |
|----------------|-------------|
|Syntax     | Persist|
|Argument   | boolean          |
|Default    | false|
|Description| When a Space is defined as persistent, a 'true' value for this annotation persists objects of this type. |

Example:


```csharp
[SpaceClass(Persist=true)]
public class Person {
//
}
```

{{%learn "./space-persistency.html"%}}


# Include Properties

|                |             |
|----------------|-------------|
|Syntax     | IncludeFields, IncludeProperties |
|Argument   | [IncludeMembers]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_Metadata_IncludeMembers.htm)      |
|Default    | IncludeFields=IncludeMembers.All, IncludeProperties=IncludeMembers.All)|
|Description|  By default, all public members (fields and properties) in a class are stored in the space, whereas non-public members are ignored. Since classes are usually designed with private/protected fields and public properties wrapping them, in most cases the default behavior is also the desired one.|

Example:

```csharp
[SpaceClass(IncludeFields=IncludeMembers.Public, IncludeProperties=IncludeMembers.Public)]
public class Person {
  //
}
```

{{%note "Different Accessor for Properties"%}}
Starting with .NET v2.0, properties can have different accessors for getters and setters (e.g. public getter and private setter). In such cases, if either the getter or the setter is public, the space treats the property as public (i.e. IncludeProperties=IncludeMembers.Public means that this property is stored).
{{%/note%}}

{{%note "Read-Only Properties "%}}
Read-only properties (getter without setter) are stored in the space, but when the object is de-serialized, the value is not restored, since there is no setter. This enables the space to be queried using such properties. There are two common scenarios for read-only properties:

- Calculated value - the property returns a calculated value based on other properties. This isn't a problem since no data is lost due to the 'missing' setter.
- Access protection - the class designer wishes to protect the property from outside changes. This is probably a problem since the field value is lost. To prevent this problem, consider adding a private setter, or excluding the property and including the field (as explained next).

{{%/note%}}

# FIFO Support

|                |             |
|----------------|-------------|
|Syntax     | FifoSupport |
|Argument   | [FifoSupport]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_Metadata_FifoSupport.htm)|
|Default    | FifoSupport.Off|
|Description| To enable FIFO operations, set this attribute to `FifoSupport.Operation`|


Example:

```csharp
[SpaceClass(FifoSupport=FifoSupport.Operation)]
public class Person {
  //
}
```

{{%learn "./fifo-support.html"%}}


# Inherit Index

|                |             |
|----------------|-------------|
|Syntax     | InheritIndexes |
|Argument   | boolean          |
|Default    | true|
|Description| Whether to use the class indexes list only, or to also include the superclass' indexes. {{<wbr>}}If the class does not define indexes, superclass indexes are used. {{<wbr>}}Options:{{<wbr>}}- `false` -- class indexes only.{{<wbr>}}- `true` -- class indexes and superclass indexes.|

Example:


```csharp
[SpaceClass(InheritIndexes=false)]
public class Person {
  //
}
```

{{%learn "./indexing.html"%}}


# Replication

|                |             |
|----------------|-------------|
|Syntax     | Replicate |
|Argument   | boolean          |
|Default    | true|
|Description| When running in a partial replication mode, a `false` value for this property will not replicates all objects from this class type to the replica space or backup space.} |

Example:


```csharp
[SpaceClass(Replicate=false)]
public class Person {
  //
}
```



{{%learn "/xap/10.1/admin/replication.html"%}}


# Compound Index

|                |             |
|----------------|-------------|
|Syntax     | CompoundSpaceIndex Paths  |
|Argument(s)| string          |
|Values     | attribute name(s)   |
|Description| Indexes can be defined for multiple properties of a class  |


Example:

```csharp
[CompoundSpaceIndex(Paths = new[] {"FirstName", "LastName"})]
[SpaceClass]
public class User {
     ....
     public String FirstName;
     public String LastName;

}

```

{{%learn "./indexing-compound.html"%}}

