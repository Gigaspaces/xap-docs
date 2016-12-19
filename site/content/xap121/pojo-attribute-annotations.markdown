---
type: post121
title:  Property Annotations
categories: XAP121
parent: pojo-annotation-overview.html
weight: 200
---

{{% ssummary %}}{{% /ssummary %}}


The [XAP API](./the-gigaspace-interface-overview.html) supports  field-level decorations with POJOs. These can be specified via annotations on the Space class source itself. The annotations are defined on the getter methods.



{{%refer%}}
XAP provides the ability to obtain and modify class meta data of objects stored in the Space [during runtime](./the-space-meta-data.html).
{{%/refer%}}

# SpaceId

| | |
|----|----|
|Syntax     |  SpaceId autoGenerate |
|Argument   |  boolean          |
|Default    | false |
|Description| Defines whether this field value is used when generating the Object ID. The field value should be unique -- i.e., no multiple objects with the same value should be written into the space (each object should have a different field value). When writing an object into the space with an existing `id` field value, an `EntryAlreadyInSpaceException` is thrown. The Object ID is created, based on the `id` field value.{{<wbr>}}Specifies if the object ID is generated automatically by the space when written into the space. If `false`, the field is indexed automatically, and if `true`, the field isn't indexed. If `autoGenerate` is declared as `false`, the field is indexed automatically. If `autoGenerate` is declared as `true`, the field isn't indexed. If `autoGenerate` is `true`, the field must be of the type `java.lang.String`. |

<br>
Example:


```java
@SpaceClass
public class Person {

  private Long id;

  @SpaceId(autoGenerate=false)
  public Long getId()
  {
    return id;
  }
}
```


{{%refer%}}
[Space Object Id Operations](./space-object-id-operations.html)
{{%/refer%}}


# SpaceRouting

| | |
|----|----|
|Syntax     | SpaceRouting|
|Description| The `@SpaceRouting` annotation specifies a get method for the field to be used to calculate the target space for the space operation (read , write...). The `@SpaceRouting` field value hash code is used to calculate the target space when the space is running in **partitioned mode**.{{<wbr>}}The field value hash code is used to calculate the target space when the space is running in **partitioned mode**. |

<br>

Example:


```java
@SpaceClass
public class Employee {

  private Long departmentId;

  @SpaceRouting
  public Long getDepartmentId()
  {
    return departmentId;
  }
}
```


{{%refer%}}
[Data Partitioning]({{%currentadmurl%}}/data-partitioning.html)
{{%/refer%}}



<br>

# SpaceProperty

| | |
|----|----|
|Syntax     | SpaceProperty nullValue |
|Argument   |  nullValue          |
|Default    |  null |
|Description| Specifies that an attribute value be treated as `null` when the object is written to the space and no value is assigned to the attribute. (where `-1` functions as a `null` value in case of an int)|


Example:


```java
@SpaceClass
public class Employee {

  private int age;

  @SpaceProperty(nullValue="-1" )
  public int getAge()
  {
    return age;
  }
}
```


# SpaceIndex

| | |
|----|----|
|Syntax     |  SpaceIndex type |
|Argument   |  [SpaceIndexType]({{% api-javadoc %}}/com/gigaspaces/metadata/index/SpaceIndexType.html) |
|Description| Querying indexed fields speeds up read and take operations. The `@SpaceIndex` annotation should be used to specify an indexed field.|

<br>

Example:


```java
@SpaceClass
public class User {

	private Long id;
	private String name;
	private Double balance;
	private Double creditLimit;
	private EAccountStatus status;
	private Address address;
	private Map<String, String> contacts;

	public User() {
	}

	@SpaceId(autoGenerate = false)
	@SpaceRouting
	public Long getId() {
		return id;
	}

	@SpaceIndex(type = SpaceIndexType.BASIC)
	public String getName() {
		return name;
	}

	@SpaceIndex(type = SpaceIndexType.EXTENDED)
	public Double getCreditLimit() {
		return creditLimit;
	}
}
```



{{%refer%}}
[Indexing  ](./indexing.html)
{{%/refer%}}

<br>

# SpaceSpatialIndex

| | |
|----|----|
|Syntax     |  SpaceSpatialIndex|
|Description| Querying indexed fields speeds up read and take operations. The `@SpaceSpatialIndex` annotation should be used to specify an indexed field.|

<br>

Example:


```java
public class GasStation {
    private Point location;

    @SpaceSpatialIndex
    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}
```



{{%refer%}}
[SpaceSpatialIndex](./query-geospatial.html#indexing)
{{%/refer%}}

<br>

# Unique Index

| | |
|----|----|
|Syntax     |  SpaceIndex type , unique  |
|Argument   |  [SpaceIndexType]({{% api-javadoc %}}/com/gigaspaces/metadata/index/SpaceIndexType.html) |
|Description| Unique constraints can be defined for an attribute or attributes of a space class. |
|Note |   The uniqueness is enforced per partition and not over the whole cluster. |

<br>

Example:


```java
@SpaceClass
public class Person
{
  @SpaceIndex(type=SpaceIndexType.BASIC, unique = true)
  private String lastName;

  @SpaceIndex(type=SpaceIndexType.BASIC)
  private String firstName;

  @SpaceIndex(type=SpaceIndexType.EXTENDED)
  private Integer age;
 .
 .
}
```



{{%refer%}}
[Indexing  ](./indexing.html)
{{%/refer%}}


<br>

# SpaceIndex Path

| | |
|----|----|
|Syntax     |  SpaceIndex path  ,type  |
|Argument   |  [SpaceIndexType]({{% api-javadoc %}}/com/gigaspaces/metadata/index/SpaceIndexType.html)|
|Description| The `path()` attribute represents the path of the indexed property within a nested object. |

<br>

Example:


```java
@SpaceClass
public class Person {
    private int id;
    private Info personalInfo;
    private String description;
    //getter and setter methods
    ...

    // this defines and EXTENDED index on the personalInfo.socialSecurity property
    @SpaceIndex(path = "socialSecurity", type = SpaceIndexType.EXTENDED)
    public Info getPersonalInfo() {
         return personalInfo;
    }
}

public static class Info implements Serializable {
	private String name;
	private Address address;
	private Date birthday;
	private long socialSecurity;
	private int _id;
	//getter and setter methods
}

public static class Address implements Serializable {
	private int zipCode;
	private String street;
	//getter and setter methods
}
```


{{%refer%}}
[Indexing Nested Properties](./indexing-nested-properties.html)
{{%/refer%}}


<br>
# SpaceVersion



| | |
|----|----|
|Syntax     |  SpaceVersion|
|Description| This annotation is used for object versioning used for optimistic locking. |
|Note       | The attribute must be an `int` data type. |

<br>

Example:


```java
@SpaceClass
public class Employee {

  private int version;

  @SpaceVersion
  public int getVersion()
  {
    return version;
  }
}
```


{{%refer%}}
[Transaction Optimistic Locking](./transaction-optimistic-locking.html)
{{%/refer%}}


<br>

# SpacePersist

| | |
|----|----|
|Syntax     | SpacePersist|
|Description| This specifies a getter method for holding the persistency mode of the object overriding the class level persist declaration. This field should be of the boolean data type.{{<wbr>}}If the persist class level annotation is true, all objects of this class type will be persisted into the underlying data store (Mirror, ExternalDataSource, Storage Adapter).|
|Note       | When using this option, you must have the space class level `persist` decoration specified.|

<br>

Example:


```java
@SpaceClass(persist=true)
public class Employee {

  private boolean persist;

  @SpacePersist
  public boolean isPersist()
  {
    return persist;
  }
}
```

<br>

# SpaceExclude

| | |
|----|----|
|Syntax     |  SpaceExclude|
|Description| When this annotation is specified the attribute is not written into the space.|
|Note | - When `IncludeProperties` is defined as `IMPLICIT`, `@SpaceExclude` should usually be used. This is because `IMPLICIT` instructs the system to take all POJO fields into account.{{<wbr>}}- When `IncludeProperties` is defined as `EXPLICIT`, there is no need to use `@SpaceExclude`.{{<wbr>}}- `@SpaceExclude` can still be used, even if `IncludeProperties` is not defined.  |

<br>

Example:


```java
@SpaceClass
public class Employee {

  private String mothersName;

  @SpaceExclude
  public String getMothersName()
  {
    return mothersName;
  }
}
```

<br>

# SpaceLeaseExpiration

| | |
|----|----|
|Syntax     |  @SpaceLeaseExpiration|
|Description|This annotation specifies the attribute for holding the timestamp of when the instance's lease expires (this is a standard Java timestamp based on the 1/1/1970 epoch). This property should not be populated by the user code. The space will populate this property automatically based on the lease time given by the user when writing the object. When using an external data source, you can choose to persist this value to the database. Subsequently, when data is reloaded from the external data source (at startup time for example), the space will filter out instances whose lease expiration timestamp has already passed. This field should be a `long` data type.|

<br>

Example:


```java
@SpaceClass (persist=true)
public class MyData {
    private long lease;
	.............

    @SpaceLeaseExpiration
    public long getLease()
    {
        return lease;
    }
    public void setLease(long lease) {
        this.lease = lease;
    }
}
```


{{%refer%}}
[Automatic Lease Expiration](./leases-automatic-expiration.html)
{{%/refer%}}


<br>

# SpaceStorageType

| | |
|----|----|
|Syntax     | SpaceStorageType storageType|
|Argument   | [StorageType]({{% api-javadoc %}}/com/gigaspaces/metadata/StorageType.html) |
|Default    | StorageType.OBJECT |
|Description| This annotation is used to specify how the attribute is stored in the space. |

<br>

Example:


```java
@SpaceClass
public class Message {

  private String payLoad;

  @SpaceStorageType(storageType=StorageType.BINARY)
  public String getpayLoad()
  {
    return payLoad;
  }
}
```

{{%refer%}}
[Storage Types and Serialisation](./storage-types-controlling-serialization.html)
{{%/refer%}}

<br>

# SpaceFifoGroupingProperty

| | |
|----|----|
|Syntax     | SpaceFifoGroupingProperty path |
|Argument   | path          |
|Description| This annotation is used to define a space FIFO grouping property. |
|Note | If defined, the `TakeModifiers.FIFO_GROUPING_POLL` or `ReadModifiers.FIFO_GROUPING_POLL` modifiers can be used to return all space entries that match the selection template in FIFO order. Different values of the FG property define groups of space entries that match each value. FIFO ordering exists within each group and not between different groups. |

<br>

Example:


```java
@SpaceClass
public class FlightReservation
{
    private FlightInfo flightInfo;
    private Person customer;
	private State processingState;
    ...
	@SpaceFifoGroupingProperty(path = "flightNumber")
    public FlightInfo getFlightInfo() {return flightInfo;}
   	public void setFlightInfo(FlightInfo flightInfo) {this.flightInfo = flightInfo;}
}
```

{{%refer%}}
[FIFO Grouping](./fifo-grouping.html)
{{%/refer%}}


<br>

# SpaceFifoGroupingIndex

| | |
|----|----|
|Syntax     | SpaceFifoGroupingIndex|
|Description| This annotation is used to define a space FIFO grouping Index. |
|Note |This annotation can be declared on several properties in a class in order to assist in efficient traversal.{{<wbr>}}If defined, there must be a property in the class, marked with the `@SpaceFifoGroupingProperty` annotation.{{<wbr>}}A compound index that contains this FIFO grouping index and the FIFO grouping property will be created.   |

<br>
Example:


```java

    @SpaceFifoGroupingIndex
    public State getProcessingState() {return processingState;}
    public void setProcessed (State processingState) {this.processingState = processingState;}

    @SpaceFifoGroupingIndex(path = "id")
    public Person getCustomer() {return customer;}
    public void setCustomer (Person customer) {this.customer = customer;}

```

{{%refer%}}
[FIFO Grouping](./fifo-grouping.html)
{{%/refer%}}




<br>

# SpaceDynamicProperties

| | |
|----|----|
|Syntax     | SpaceDynamicProperties|
|Description| Allows adding properties freely to a class without worrying about the schema.|
|Note|**Only one property per class can be annotated with `@SpaceDynamicProperties`.**|

<br>
Example:


```java
@SpaceClass
public class Person {
    public Person (){}
    private String name;
    private Integer id;
    private DocumentProperties extraInfo;

    public String getName() {return name}
    public void setName(String name) {this.name=name}

    @SpaceId
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id=id;}

    @SpaceDynamicProperties
    public DocumentProperties getExtraInfo() {return extraInfo;}
    public void setExtraInfo(DocumentProperties extraInfo) {this.extraInfo=extraInfo;}
}
```

{{%refer%}}
[Dynamic Properties](./dynamic-properties.html)
{{%/refer%}}


<br>

# SpaceDocumentSupport

| | |
|----|----|
|Syntax     | SpaceDocumentSupport operationType|
|Argument   | SpaceDocumentSupport          |
|Default    | SpaceDocumentSupport.DEFAULT|
|Description| If the POJO contains properties which are POJO themselves, the space will implicitly convert these properties to space documents as needed.This works the other way around as well - if a Space document is created with a nested space document property, it will be converted to a POJO with a nested POJO property when read as a POJO. You can disable this implicit conversion and preserve the nested POJO instance within document entries by setting it to `COPY`|

<br>
Example:


```java
@SpaceClass
public class Person {
    ...
    @SpaceProperty(documentSupport = SpaceDocumentSupport.COPY)
    public Address getAddress() {...}
    public Person setAddress(Address address) {...}
    ...
}
```



{{%refer%}}
[Document POJO Interoperability](./document-pojo-interoperability.html#deep-interoperability)
{{%/refer%}}

<br>

# SpaceClassConstructor

| | |
|----|----|
|Syntax     | SpaceClassConstructor|
|Description| This annotation can be placed on a POJO constructor to denote that this constructor should be used during object instantiation.{{<wbr>}}Using this annotations, it is possible for the POJO to have immutable properties (i.e. `final` fields).{{<wbr>}}As opposed to a standard POJO, a POJO annotated with this annotation may omit setters for its properties.{{<wbr>}}Except for the case where the id property is auto generated, only properties defined in this constructor will be considered space properties.The annotations can be placed on at most one constructor.|

<br>

Example:


```java
@SpaceClass
public class Person {

    public Person ()
    {
    }
    
    @SpaceClassConstructor
    public Person (Long id, String firstName, String LastName)
    {
    }
}
```

<br>

# Space sequence number

| | |
|----|----|
|Syntax     | SpaceSequenceNumber|
|Description| A sequence number (like a data-base sequence-number/autoincrement column) is a property that is given a unique incrementing value when the entry is written to the Space. It's a means for assigning a unique monotony -incremented value that can be used as a per-space (unique) key.The sequence-number is unique per-partition.  The annotated field is of type Long.    |

<br>
Example:


```java
@SpaceClass
public class Person {

    private Long sequenceNumber;

    public Person ()
    {
    }

   @SpaceSequenceNumber
   public Long getSequenceNumber()
   {
     return this.sequenceNumber;
   }
}
```
