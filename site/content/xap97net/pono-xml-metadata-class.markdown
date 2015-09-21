---
type: post97
title:  Class Metadata
categories: XAP97NET
parent: pono-xml-metadata-overview.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}


# Name

|           |                         |
|-----------|-------------------------|
|Syntax     | name="" |
|Description| Contains the full qualified name of the specified class. Because this attribute is of the XML type `ID`, there can only be one `class-descriptor` per class. |

Example:

```xml
<gigaspaces-mapping>
	<class name="Model.Person">
	</class>
</gigaspaces-mapping>
```

# Alias name

|           |                         |
|-----------|-------------------------|
|Syntax     | alias-name="" |
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

|           |                         |
|-----------|-------------------------|
|Syntax     | persist="" |
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


{{%learn "./space-persistency.html"%}}

# Replication

|           |                         |
|-----------|-------------------------|
|Syntax     | replicate="" |
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


{{%learn "/xap97/replication.html"%}}


# FIFO Support

|           |                         |
|-----------|-------------------------|
|Syntax     | fifo=""  |
|Argument   | [FifoSupport](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_Metadata_FifoSupport.htm)|
|Default    | off|
|Description| Enabling  FIFO operations.     |

Example:


```xml
<gigaspaces-mapping>
    <class name="Model.Person" fifo="operation">
    </class>
</gigaspaces-mapping>
```


{{%learn "./fifo-support.html"%}}

# Storage Type

|           |                         |
|-----------|-------------------------|
|Syntax     | storage-type="" |
|Argument   | [StorageType](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_Metadata_StorageType.htm)|
|Default    | object |
|Description| To determine a default storage type for each non primitive property for which a (field level) storage type was not defined.|

Example:


```xml
<gigaspaces-mapping>
    <class name="Model.Person" storage-type="binary" />
</gigaspaces-mapping>

```


{{%learn "./poco-storage-type.html"%}}




# Inherit Index

|           |                         |
|-----------|-------------------------|
|Syntax     | inherit-indexes="" |
|Argument   | boolean          |
|Default    | true|
|Description| Whether to use the class indexes list only, or to also include the superclass' indexes. {{% wbr %}}If the class does not define indexes, superclass indexes are used. {{% wbr %}}Options:{{% wbr %}}- `false` -- class indexes only.{{% wbr %}}- `true` -- class indexes and superclass indexes.|

Example:


```xml
<gigaspaces-mapping>
    <class name="Model.Person" inherit-indexes="false" />
</gigaspaces-mapping>
```

{{%learn "./indexing.html"%}}


# Compound Index

|           |                         |
|-----------|-------------------------|
|Syntax     | compound-index paths="" |
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


{{%learn "./indexing-compound.html"%}}






