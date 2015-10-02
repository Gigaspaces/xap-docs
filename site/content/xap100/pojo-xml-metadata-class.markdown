---
type: post100
title:  Class Metadata
categories: XAP100
parent: pojo-xml-metadata-overview.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}


# Name

|     |   |
|----|----|
|Syntax     | name |
|Description| Contains the full qualified name of the specified class. Because this attribute is of the XML type `ID`, there can only be one `class-descriptor` per class. |

Example:

```xml
<gigaspaces-mapping>
	<class name="model.Person">
	</class>
</gigaspaces-mapping>
```



# Persistence

|     |   |
|----|----|
|Syntax     | persist |
|Argument   | boolean|
|Default    | false|
|Description|  This field indicates the persistency mode of the object. When a space is defined as persistent, a `true` value for this attribute will persist objects of this type.  |

Example:


```xml
<gigaspaces-mapping>
	<class name="model.Person" persist="true">
	</class>
</gigaspaces-mapping>
```


{{%learn "./space-persistency.html"%}}

# Replication

|     |   |
|----|----|
|Syntax     | replicate |
|Argument   | boolean|
|Default    | false|
|Description|  This field indicates the replication mode of the object. When a space is defined as replicated, a `true` value for this attribute will replicate objects of this type.|

Example:


```xml
<gigaspaces-mapping>
	<class name="model.Person" replicate="true">
	</class>
</gigaspaces-mapping>
```


{{%learn "/xap100adm/replication.html"%}}


# FIFO Support

|     |   |
|----|----|
|Syntax     | fifo-support  |
{{%javadoc "|Argument   | [FifoSupport](  com/gigaspaces/annotation/pojo/FifoSupport )|"%}}
|Default    | not_set|
|Description| Enabling  FIFO operations.     |

Example:


```xml
<gigaspaces-mapping>
    <class name="model.Person" fifo-support="operation">
    </class>
</gigaspaces-mapping>
```


{{%learn "./fifo-support.html"%}}

# Storage Type

|     |   |
|----|----|
|Syntax     | storage-type |
{{%javadoc "|Argument   | [StorageType](  com/gigaspaces/metadata/StorageType )          |"%}}
|Default    | object |
|Description| To determine a default storage type for each non primitive property for which a (field level) storage type was not defined.|

Example:


```xml
<gigaspaces-mapping>
    <class name="model.Person" storage-type="binary" />
</gigaspaces-mapping>

```


{{%learn "./storage-types-controlling-serialization.html"%}}


# Include Properties

|     |   |
|----|----|
|Syntax     | include-properties |
|Argument   | [IncludeProperties](http://www.gigaspaces.com/docs/JavaDoc{{%currentversion%}}/com/gigaspaces/annotation/pojo/SpaceClass.IncludeProperties.html)      |
|Default    | implicit|
|Description| `implicit` takes into account all POJO fields -- even if a `get` method is not declared   as a `SpaceProperty`, it is taken into account as a space field.`explicit` takes into account only the `get` methods which are declared in the mapping file. |

Example:


```xml
<gigaspaces-mapping>
    <class name="model.Person" include-properties="explicit" />
</gigaspaces-mapping>
```



# Inherit Index

|     |   |
|----|----|
|Syntax     | inherit-indexes |
|Argument   | boolean          |
|Default    | true|
|Description| Whether to use the class indexes list only, or to also include the superclass' indexes. {{<wbr>}}If the class does not define indexes, superclass indexes are used. {{<wbr>}}Options:{{<wbr>}}- `false` -- class indexes only.{{<wbr>}}- `true` -- class indexes and superclass indexes.|

Example:


```xml
<gigaspaces-mapping>
    <class name="model.Person" inherit-indexes="false" />
</gigaspaces-mapping>
```

{{%learn "./indexing.html"%}}


# Compound Index

|     |   |
|----|----|
|Syntax     | compound-index paths |
|Argument(s)| string          |
|Values     | attribute name(s)   |
|Description| Indexes can be defined for multiple attributes of a class  |

Example:


```xml
<gigaspaces-mapping>
    <class name="Data" >
        <compound-index paths="data1, data2"/>
        ...
    </class>
</gigaspaces-mapping>
```


{{%learn "./indexing-compound.html"%}}


# Blob Store

|     |   |
|----|----|
|Syntax     | blobstoreEnabled |
|Argument| boolean          |
|Default | true|
|Description| By default any Space Data Type is blobStore enabled. When decorating the space class with its meta data you may turn off the blobStore behavior using the @SpaceClass blobStore annotation or gs.xml blobStore tag.  |


Example:


```xml
<gigaspaces-mapping>
    <class name="com.test.Person" "blobstoreEnabled"="false" >
     .....
     </class>
</gigaspaces-mapping>

```


{{%learn "/xap100adm/blobstore-cache-policy.html"%}}






