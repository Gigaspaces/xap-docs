---
type: post120
title:  Class Metadata
categories: XAP120NET, PRM
parent: pono-xml-metadata-overview.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}


# Name

|           |                 |
|-----------|-----------------|
|Syntax     | name |
|Description| Contains the full qualified name of the specified class. Because this attribute is of the XML type `ID`, there can only be one `class-descriptor` per class. |

Example:

```xml
<gigaspaces-mapping>
	<class name="Model.Person">
	</class>
</gigaspaces-mapping>
```

# Alias name

|           |                 |
|-----------|-----------------|
|Syntax     | alias-name |
|Argument   | boolean|
|Description|  Gives the ability to map a C# class name (including namespace) to a space class name  |

Example:


```xml
<gigaspaces-mapping>
	<class name="Model.Person" alias-name="CommonPerson">
	</class>
</gigaspaces-mapping>
```


# Persistence

|           |                 |
|-----------|-----------------|
|Syntax     | persist |
|Argument   | boolean|
|Default    | false|
|Description|  This field indicates the persistency mode of the object. When a space is defined as persistent, a `true` value for this attribute will persist objects of this type.  |

Example:


```xml
<gigaspaces-mapping>
	<class name="Model.Person" persist="true">
	</class>
</gigaspaces-mapping>
```

{{%refer%}}
[Space Persistence](./space-persistency.html)
{{%/refer%}}

# Replication

|           |                 |
|-----------|-----------------|
|Syntax     | replicate |
|Argument   | boolean|
|Default    | false|
|Description|  This field indicates the replication mode of the object. When a space is defined as replicated, a `true` value for this attribute will replicate objects of this type.|

Example:


```xml
<gigaspaces-mapping>
	<class name="Model.Person" replicate="true">
	</class>
</gigaspaces-mapping>
```

{{%refer%}}
[Replication]({{%currentadmurl%}}/replication.html)
{{%/refer%}}




# FIFO Support

|           |                 |
|-----------|-----------------|
|Syntax     | fifo  |
|Argument   | [FifoSupport]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_Metadata_FifoSupport.htm)|
|Default    | off|
|Description| Enabling  FIFO operations.     |

Example:


```xml
<gigaspaces-mapping>
    <class name="Model.Person" fifo="operation">
    </class>
</gigaspaces-mapping>
```

{{%refer%}}
[Space Persistence](./fifo-support.html)
{{%/refer%}}

# Storage Type

|           |                 |
|-----------|-----------------|
|Syntax     | storage-type |
|Argument   | [StorageType]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_Metadata_StorageType.htm)|
|Default    | object |
|Description| To determine a default storage type for each non primitive property for which a (field level) storage type was not defined.|

Example:


```xml
<gigaspaces-mapping>
    <class name="Model.Person" storage-type="binary" />
</gigaspaces-mapping>

```


{{%refer%}}
[Space Object storage types](./poco-storage-type.html)
{{%/refer%}}





# Inherit Index

|           |                 |
|-----------|-----------------|
|Syntax     | inherit-indexes |
|Argument   | boolean          |
|Default    | true|
|Description| Whether to use the class indexes list only, or to also include the superclass' indexes. {{<wbr>}}If the class does not define indexes, superclass indexes are used. {{<wbr>}}Options:{{<wbr>}}- `false` -- class indexes only.{{<wbr>}}- `true` -- class indexes and superclass indexes.|

Example:


```xml
<gigaspaces-mapping>
    <class name="Model.Person" inherit-indexes="false" />
</gigaspaces-mapping>
```
{{%refer%}}
[Indexing Objects](./indexing.html)
{{%/refer%}}

# Compound Index

|           |                 |
|-----------|-----------------|
|Syntax     | compound-index paths |
|Argument(s)| string          |
|Values     | attribute name(s)   |
|Description| Indexes can be defined for multiple properties of a class  |

Example:


```xml
<gigaspaces-mapping>
    <class name="Data" >
        <compound-index paths="Data1, Data2"/>
        ...
    </class>
</gigaspaces-mapping>
```


{{%refer%}}
[Compound Indexing](././indexing-compound.html)
{{%/refer%}}






