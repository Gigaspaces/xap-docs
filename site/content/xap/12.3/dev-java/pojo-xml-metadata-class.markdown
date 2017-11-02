---
type: post123
title:  Class Metadata
categories: XAP123, OSS
parent: pojo-xml-metadata-overview.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}



{{%refer%}}
XAP provides the ability to obtain and modify class meta data of objects stored in the Space [during runtime](./the-space-meta-data.html).
{{%/refer%}}


# Name

| | |
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

| | |
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


{{%refer%}}
[Space Persistence](./space-persistency.html)
{{%/refer%}}


# Replication

| | |
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



{{%refer%}}
[Replication]({{%currentadmurl%}}/replication.html")
{{%/refer%}}



# FIFO Support

| | |
|----|----|
|Syntax     | fifo-support  |
|Argument   | [FifoSupport]({{% api-javadoc %}}/com/gigaspaces/annotation/pojo/FifoSupport.html) |
|Default    | not_set|
|Description| Enabling  FIFO operations.     |

Example:


```xml
<gigaspaces-mapping>
    <class name="model.Person" fifo-support="operation">
    </class>
</gigaspaces-mapping>
```




{{%refer%}}
[FIFO Support](./fifo-support.html")
{{%/refer%}}


# Storage Type

| | |
|----|----|
|Syntax     | storage-type |
|Argument   | [StorageType]({{% api-javadoc %}}/com/gigaspaces/metadata/StorageType.html) |
|Default    | object |
|Description| To determine a default storage type for each non primitive property for which a (field level) storage type was not defined.|

Example:


```xml
<gigaspaces-mapping>
    <class name="model.Person" storage-type="binary" />
</gigaspaces-mapping>

```



{{%refer%}}
[Storage Tyes and Serialization](./storage-types-controlling-serialization.html")
{{%/refer%}}




# Include Properties

| | |
|----|----|
|Syntax     | include-properties |
|Argument   | [IncludeProperties]({{% api-javadoc %}}/com/gigaspaces/annotation/pojo/SpaceClass.IncludeProperties.html)      |
|Default    | implicit|
|Description| `implicit` takes into account all POJO fields -- even if a `get` method is not declared   as a `SpaceProperty`, it is taken into account as a space field.`explicit` takes into account only the `get` methods which are declared in the mapping file. |

Example:


```xml
<gigaspaces-mapping>
    <class name="model.Person" include-properties="explicit" />
</gigaspaces-mapping>
```



# Inherit Index

| | |
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


{{%refer%}}
[Indexing Space Objects](./indexing.html)
{{%/refer%}}





# Compound Index

| | |
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


{{%refer%}}
[Compound Indexing ](./indexing-compound.html)
{{%/refer%}}




# Blob Store

| | |
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



{{%refer%}}
[MemoryXtend]({{%currentadmurl%}}/memoryxtend-overview.html")
{{%/refer%}}







