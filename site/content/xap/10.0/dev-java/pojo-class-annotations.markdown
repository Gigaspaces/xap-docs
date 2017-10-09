---
type: post100
title:  Class Annotations
categories: XAP100
parent: pojo-annotation-overview.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}



The [GigaSpaces API](./the-gigaspace-interface-overview.html) supports class level decorations with POJOs. These can be specified via annotations on the space class source itself  for all class instances.


{{<wbr>}}

# Persistence

|     |   |
|----|----|
|Syntax     | persist |
|Argument   | boolean          |
|Default    | false|
|Description| When a space is defined as persistent, a 'true' value for this annotation persists objects of this type. |

Example:


```java
@SpaceClass(persist=true)
public class Person {
//
}
```

{{%learn "./space-persistency.html"%}}


# Include Properties

|     |   |
|----|----|
|Syntax     | includeProperties|
|Argument   | [IncludeProperties]({{% api-javadoc %}}/com/gigaspaces/annotation/pojo/SpaceClass.IncludeProperties.html)      |
|Default    | IncludeProperties.IMPLICIT|
|Description| `IncludeProperties.IMPLICIT` takes into account all POJO fields -- even if a `get` method is not declared with a `@SpaceProperty` annotation, it is taken into account as a space field.`IncludeProperties.EXPLICIT` takes into account only the `get` methods which are declared with a `@SpaceProperty` annotation. |

Example:

```java
@SpaceClass(includeProperties=IncludeProperties.EXPLICIT)
public class Person {
  //
}
```


# FIFO Support

|     |   |
|----|----|
|Syntax     | fifoSupport |
|Argument   | [FifoSupport]({{% api-javadoc %}}/com/gigaspaces/annotation/pojo/FifoSupport.html) |
|Default    | FifoSupport.NOT_SET|
|Description| To enable FIFO operations, set this attribute to `FifoSupport.OPERATION`|


Example:

```java
@SpaceClass(fifoSupport=FifoSupport.OPERATION)
public class Person {
  //
}
```

{{%learn "./fifo-support.html"%}}


# Inherit Index

|     |   |
|----|----|
|Syntax     | inheritIndexes |
|Argument   | boolean          |
|Default    | true|
|Description| Whether to use the class indexes list only, or to also include the superclass' indexes. {{<wbr>}}If the class does not define indexes, superclass indexes are used. {{<wbr>}}Options:{{<wbr>}}- `false` -- class indexes only.{{<wbr>}}- `true` -- class indexes and superclass indexes.|

Example:


```java
@SpaceClass(inheritIndexes=false)
public class Person {
  //
}
```

{{%learn "./indexing.html"%}}

# Storage Type

|     |   |
|----|----|
|Syntax     | storageType |
|Argument   | [StorageType]({{% api-javadoc %}}/com/gigaspaces/metadata/StorageType.html )   |
|Default    | StorageType.OBJECT |
|Description| To determine a default storage type for each non primitive property for which a (field level) storage type was not defined.|


Example:


```java
@SpaceClass(storageType=StorageType.BINARY)
public class Person {
  //
}
```

{{%learn "./storage-types-controlling-serialization.html"%}}

# Replication

|     |   |
|----|----|
|Syntax     | replicate |
|Argument   | boolean          |
|Default    | true|
|Description| When running in a partial replication mode, a `false` value for this property will not replicates all objects from this class type to the replica space or backup space.} |

Example:


```java
@SpaceClass(replicate=false)
public class Person {
  //
}
```



{{%learn "/xap/10.0/admin/replication.html"%}}


# Compound Index

|     |   |
|----|----|
|Syntax     | CompoundSpaceIndexes CompoundSpaceIndex paths  |
|Argument(s)| string          |
|Values     | attribute name(s)   |
|Description| Indexes can be defined for multiple attributes of a class  |


Example:

```java
@CompoundSpaceIndexes({ @CompoundSpaceIndex(paths = { "firstName", "lastName" }) })
@SpaceClass
public class User {
     private Long id;
     private String firstName;
     private String lastName;
     private Double balance;
     private Double creditLimit;
     private EAccountStatus status;
     private Address address;
     private String[] comment;
}

```

{{%learn "./indexing-compound.html"%}}

# Blob Store

|     |   |
|----|----|
|Syntax     | blobstoreEnabled  |
|Argument | boolean          |
|Default | true|
|Description| By default any Space Data Type is blobStore enabled. When decorating the space class with its meta data you may turn off the blobStore behavior using the @SpaceClass blobStore annotation or gs.xml blobStore tag.  |


Example:

```java
@SpaceClass(blobstoreEnabled = false)
public class Person {
    .......
}

```

{{%learn "/xap/10.0/admin/blobstore-cache-policy.html"%}}

