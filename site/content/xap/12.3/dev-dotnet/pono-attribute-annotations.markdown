---
type: post123
title:  Property Annotations
categories: XAP123NET, PRM
parent: pono-annotation-overview.html
weight: 200
---

{{% ssummary %}}{{% /ssummary %}}



The [GigaSpaces API](./the-gigaspace-interface-overview.html) supports  field-level decorations with PONOs. These can be specified via annotations on the space class source itself. The annotations are defined on the getter methods.







# SpaceId

|           |                 |
|-----------|-----------------|
|Syntax     | SpaceId AutoGenerate|
|Argument   | boolean          |
|Default    | false |
|Description| Defines whether this field value is used when generating the Object ID. The field value should be unique -- i.e., no multiple objects with the same value should be written into the space (each object should have a different field value). When writing an object into the space with an existing `id` field value, an `EntryAlreadyInSpaceException` is thrown. The Object ID is created, based on the `id` field value.{{<wbr>}}Specifies if the object ID is generated automatically by the space when written into the space. If `false`, the field is indexed automatically, and if `true`, the field isn't indexed. If `autoGenerate` is declared as `false`, the field is indexed automatically. If `autoGenerate` is declared as `true`, the field isn't indexed. If `AutoGenerate` is `true`, the field must be of the type `String`. |

Example:


```csharp
[SpaceClass]
public class Person {

  [SpaceId(AutoGenerate=false)]
  public long? Id {set; get;}

}
```


{{%refer%}}
[Space Object ID](./poco-object-id.html)
{{%/refer%}}


# SpaceRouting

|           |                 |
|-----------|-----------------|
|Syntax     | SpaceRouting|
|Description| The `[SpaceRouting]` annotation specifies a get method for the field to be used to calculate the target space for the space operation (Read , Write...). The `[SpaceRouting]` field value hash code is used to calculate the target space when the space is running in **partitioned mode**.|

Example:


```csharp
[SpaceClass]
public class Employee {

  [SpaceId]
  [SpaceRouting]
  public long DepartmentId {set; get}

}
```

{{%refer%}}
[Data Partitioning]({{%currentadmurl%}}/data-partitioning.html)
{{%/refer%}}


# SpaceProperty

|           |                 |
|-----------|-----------------|
|Syntax     | SpaceProperty NullValue  |
|Argument   |  nullValue          |
|Default    |  null |
|Description| Specifies that a property value be treated as `null` when the object is written to the space and no value is assigned to the attribute. (where `-1` functions as a `null` value in case of an int)|


Example:


```csharp
[SpaceClass]
public class Employee {

  [SpaceProperty(NullValue="-1")]
  public int Age {set; get;}
}
```
 }

# SpaceIndex

|           |                 |
|-----------|-----------------|
|Syntax     |  SpaceIndex Type|
|Argument   |  [SpaceIndexType]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_Metadata_SpaceIndexType.htm)  |
|Description| Querying indexed fields speeds up read and take operations. The `[SpaceIndex]` annotation should be used to specify an indexed field.|

Example:


```csharp
[SpaceClass]
public class User {
	[SpaceIndex(Type = SpaceIndexType.Basic)]
	public String Name {set; get;}

	[SpaceIndex(Type = SpaceIndexType.Extended)]
	public double Balance{set; get;}
}
```


{{%refer%}}
[Indexing Objects](./indexing.html)
{{%/refer%}}

# Unique Index

|           |                 |
|-----------|-----------------|
|Syntax     |  SpaceIndex Type, Unique|
|Argument   | [SpaceIndexType]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_Metadata_SpaceIndexType.htm)  |
|Description| Unique constraints can be defined for an attribute or attributes of a space class. |
|Note |   The uniqueness is enforced per partition and not over the whole cluster. |

Example:


```csharp
[SpaceClass]
public class Person
{
    [SpaceIndex(Type=SpaceIndexType.Basic, Unique=true)]
    public String LastName{ get; set; }

}
```

{{%refer%}}
[Indexing Objects](./indexing.html)
{{%/refer%}}

# SpaceIndex Path

|           |                 |
|-----------|-----------------|
|Syntax     |  SpaceIndex Path ,Type|
|Argument   |  [SpaceIndexType]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_Metadata_SpaceIndexType.htm)|
|Description| The `path()` attribute represents the path of the indexed property within a nested object. |

Example:


```csharp
[SpaceClass]
public class Person {

   [SpaceIndex(Path = "SocialSecurity", Type = SpaceIndexType.Extended)]
   public Info PersonalInfo{ get; set; }
}

public class Info : Serializable {
	public String Name { get; set; }
	public Address Address{ get; set; }
	public Date Birthday { get; set; }
	public long SocialSecurity{ get; set; }
}

```

{{%refer%}}
[Indexing Nested Properties](./indexing-nested-properties.html)
{{%/refer%}}



# SpaceVersion



|           |                 |
|-----------|-----------------|
|Syntax     | SpaceVersion|
|Description| This annotation is used for object versioning used for optimistic locking. |
|Note       | The attribute must be an `int` data type. |

Example:


```csharp
[SpaceClass]
public class Employee {

  [SpaceVersion]
  public int Version { get; set; }

}
```

{{%refer%}}
[Transactions Optimistic Locking](./transaction-optimistic-locking.html)
{{%/refer%}}


# SpacePersist

|           |                 |
|-----------|-----------------|
|Syntax     | SpacePersist|
|Description| This specifies a getter method for holding the persistency mode of the object overriding the class level persist declaration. This field should be of the boolean data type.{{<wbr>}}If the persist class level annotation is true, all objects of this class type will be persisted into the underlying data store (Mirror, ExternalDataSource, Storage Adapter).|
|Note       | When using this option, you must have the space class level `persist` decoration specified.|

Example:


```csharp
[SpaceClass(Persist=true)
public class Employee {

  [SpacePersist]
  public Bool Persist{ get; set; }
}
```



# SpaceExclude

|           |                 |
|-----------|-----------------|
|Syntax     |  SpaceExclude|
|Description| When this annotation is specified the attribute is not written into the space.|

Example:


```csharp
[SpaceClass]
public class Employee {

  [SpaceExclude]
  public String MothersName{ get; set; }
}
```



# SpaceStorageType

|           |                 |
|-----------|-----------------|
|Syntax     | SpaceStorageType StorageType |
|Argument   | [StorageType]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_Metadata_StorageType.htm)          |
|Default    | StorageType.Object |
|Description| This annotation is used to specify how the attribute is stored in the space. |

Example:


```csharp
[SpaceClass]
public class Message {

  [SpaceStorageType(storageType=StorageType.BINARY)]
  public String PayLoad{ get; set; }

}
```


{{%refer%}}
[Space Object Storage Type](./poco-storage-type.html)
{{%/refer%}}


# SpaceFifoGroupingProperty

|           |                 |
|-----------|-----------------|
|Syntax     | SpaceFifoGroupingProperty Path|
|Argument   | path          |
|Description| This annotation is used to define a space FIFO grouping property. |
|Note | If defined, the `TakeModifiers.FIFO_GROUPING_POLL` or `ReadModifiers.FIFO_GROUPING_POLL` modifiers can be used to return all space entries that match the selection template in FIFO order. Different values of the FG property define groups of space entries that match each value. FIFO ordering exists within each group and not between different groups. |

Example:


```csharp
[SpaceClass]
public class FlightReservation
{
    [SpaceFifoGroupingProperty(Path = "FlightNumber")]
    public FlightInfo Info { get; set; }

}
```

{{%refer%}}
[FIFO Grouping](./fifo-grouping.html)
{{%/refer%}}


# SpaceFifoGroupingIndex

|           |                 |
|-----------|-----------------|
|Syntax     | SpaceFifoGroupingIndex Path|
|Description| This annotation is used to define a space FIFO grouping Index. |
|Note |This annotation can be declared on several properties in a class in order to assist in efficient traversal.{{<wbr>}}If defined, there must be a property in the class, marked with the `[SpaceFifoGroupingProperty]` annotation.{{<wbr>}}A compound index that contains this FIFO grouping index and the FIFO grouping property will be created.   |

Example:


```csharp
[SpaceFifoGroupingIndex]
public State ProcessingState { get; set; }
[SpaceFifoGroupingIndex(Path = "Id")]
public Person Customer { get; set; }

```

{{%refer%}}
[FIFO Grouping](./fifo-grouping.html)
{{%/refer%}}




# SpaceDynamicProperties

|           |                 |
|-----------|-----------------|
|Syntax     | SpaceDynamicProperties|
|Description| Allows adding properties freely to a class without worrying about the schema.|
|Note|**Only one property per class can be annotated with `[SpaceDynamicProperties]`.**|

Example:


```csharp
[SpaceClass]
public class Person {

    public String Name { get; set; }

    [SpaceDynamicProperties]
    public DocumentProperties ExtraInfo { get; set; }
}
```


{{%refer%}}
[Dynamic Properties](./poco-dynamic-properties.html)
{{%/refer%}}



# Alias Name

|           |                 |
|-----------|-----------------|
|Syntax     | AliasName|
|Description| In some cases, usually in interoperability scenarios, you may need to map your C# properties to different names in the Space. You can do that using the AliasName property on [SpaceProperty].  |
|Note| When using space SqlQuery on an object with properties which are aliased, the query text needs to use the aliased property names. For more information about SqlQuery, see [GigaSpaces.NET - Sql Query](./query-sql.html).|

Example:


```csharp
[SpaceClass]
public class Person {

 [SpaceProperty(AliasName="firstName")]
 public String FirstName {set; get;}

}
```


{{%refer%}}
[Interoperability](./interoperability.html)
{{%/refer%}}



