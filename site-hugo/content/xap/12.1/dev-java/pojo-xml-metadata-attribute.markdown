---
type: post121
title:  Properties Metadata
categories: XAP121
parent: pojo-xml-metadata-overview.html
weight: 200
---

{{% ssummary %}}{{% /ssummary %}}


All the properties are written automatically into space. If the field is a reference to another object, it has to be Serializable and it will be written into space as well. Only the fields which need special space behavior need to be specified in the gs.xml file. Specify the fields which are id's, indexes or need exclusions, etc.



{{%refer%}}
XAP provides the ability to obtain and modify class meta data of objects stored in the Space [during runtime](./the-space-meta-data.html).
{{%/refer%}}

# Property

| | |
|----|----|
|Syntax     | property name |
|Argument   |  name of the attribute          |
|Description| contains mapping info for a property of a class |

Example:

```xml
<gigaspaces-mapping>
<class name="model.Person" persist="false" replicate="false">
    <property name="age" />
</class>
</gigaspaces-mapping>
```


### SpaceProperty

| | |
|----|----|
|Syntax     | null-value |
|Description| Specifies that an attribute value be treated as `null` when the object is written to the space and no value is assigned to the attribute. (where `-1` functions as a `null` value in case of an int)|

Example:


```xml
<gigaspaces-mapping>
<class name="model.Person" persist="false" replicate="false">
    <property name="age" null-value="-1" />
    <property name="status" null-value="-1" />
</class>
</gigaspaces-mapping>
```


### Reference

| | |
|----|----|
|Syntax     | reference |
|Argument   | class name         |
|Description| contains mapping information for a property of a class that is not primitive, but references another entity object. |

Example:


```xml
<gigaspaces-mapping>
<class name="Person">
    <property name="firstName" />
    <property name="address" reference="model.Address" />
</class>
</gigaspaces-mapping>
```






### SpaceStorageType

| | |
|----|----|
|Syntax     | storage-type|
|Argument   | [StorageType]({{% api-javadoc %}}/com/gigaspaces/metadata/StorageType.html) |
|Default    | object |
|Description| This tag is used to specify how the property is stored in the space. |

Example:


```xml
<gigaspaces-mapping>
<class name="model.Person" persist="false" replicate="false">
    <property name="age" null-value="-1" />
    <property name="address" storage-type="binary" />
</class>
</gigaspaces-mapping>
```


{{%refer%}}
[Storage Types and Serialization](./storage-types-controlling-serialization.html)
{{%/refer%}}


### SpaceIndex

| | |
|----|----|
|Syntax     |  index type|
|Argument   |  [SpaceIndexType]({{% api-javadoc %}}/com/gigaspaces/metadata/index/SpaceIndexType.html) |
|Description| Querying indexed fields speeds up read and take operations. The `index` tag should be used to specify an indexed field.|

Example:


```xml
<gigaspaces-mapping>
    <class name="model.Person" persist="false" replicate="false" >
        <property name="lastName">
            <index type="basic"/>
        </property>
        <property name="firstName">
            <index type="basic"/>
        </property>
        <property name="age">
             <index type="extended"/>
        </property>
    </class>
</gigaspaces-mapping>
```


{{%refer%}}
[Indexing Space Objects](./indexing.html)
{{%/refer%}}



### SpaceIndex Path

| | |
|----|----|
|Syntax     |  path type|
|Argument   |  [SpaceIndexType]({{% api-javadoc %}}/com/gigaspaces/metadata/index/SpaceIndexType.html)|
|Description| The `path` attribute represents the path of the indexed property within a nested object. |

Example:


```xml
<gigaspaces-mapping>
    <class name="model.Person"  >
        <property name="personalInfo">
		    <index path="socialSecurity" type = "extended"/>
		    <index path="address.zipCode" type = "basic"/>
		</property>
    </class>
</gigaspaces-mapping>
```


{{%refer%}}
[Indexing Nested Properties](./indexing-nested-properties.html)
{{%/refer%}}



### Unique Index

| | |
|----|----|
|Syntax     |  index type unique|
|Argument   |  [SpaceIndexType]({{% api-javadoc %}}/com/gigaspaces/metadata/index/SpaceIndexType.html) |
|Description| Unique constraints can be defined for an attribute or attributes of a space class. |
|Note |   The uniqueness is enforced per partition and not over the whole cluster. |

Example:


```xml
<gigaspaces-mapping>
    <class name="model.Person" persist="false" replicate="false" >
        <property name="lastName">
            <index type="BASIC" unique="true"/>
        </property>
        <property name="firstName">
            <index type="BASIC"/>
        </property>
        <property name="age">
             <index type="EXTENDED"/>
        </property>
    </class>
</gigaspaces-mapping>

```

{{%refer%}}
[Indexing Objects](./indexing.html)
{{%/refer%}}

### SpaceFifoGroupingIndex

| | |
|----|----|
|Syntax     | fifo-grouping-index path|
|Description| This tag is used to define a space FIFO grouping Index. |
|Note |This tag can be declared on several properties in a class in order to assist in efficient traversal.{{<wbr>}}If defined, there must be a property in the class, marked with the `@SpaceFifoGroupingProperty` annotation.{{<wbr>}}A compound index that contains this FIFO grouping index and the FIFO grouping property will be created.   |

Example:


```xml
<gigaspaces-mapping>
	<class name="com.gigaspaces.examples.FlightReservation>
		<property name="processingState">
			<fifo-grouping-index />
		</property>
		<property name="customer">
			<fifo-grouping-index  path="id"/>
		</property>
	</class>
</gigaspaces-mapping>

```


{{%refer%}}
[FIFO](./fifo-grouping.html)
{{%/refer%}}


# SpaceId

| | |
|----|----|
|Syntax     | id  |
|Argument   | auto-generate |
|Default    | false |
|Description| Defines whether this field value is used when generating the Object ID. The field value should be unique -- i.e., no multiple objects with the same value should be written into the space (each object should have a different field value). When writing an object into the space with an existing `id` field value, an `EntryAlreadyInSpaceException` is thrown. The Object ID is created, based on the `id` field value.{{<wbr>}}Specifies if the object ID is generated automatically by the space when written into the space. If `false`, the field is indexed automatically, and if `true`, the field isn't indexed. If `autoGenerate` is declared as `false`, the field is indexed automatically. If `autoGenerate` is declared as `true`, the field isn't indexed. If `autoGenerate` is `true`, the field must be of the type `java.lang.String`. |

Example:


```xml
<gigaspaces-mapping>
	<class name="model.Person">
	    <property name="personId" />
        <property name="firstName" />
        <property name="lastName" />
        <property name="type" />
		<id name="personId" auto-generate="true" />
	</class>
</gigaspaces-mapping>
```

{{%refer%}}
[Space Object ID Operations{./space-object-id-operations.html)
{{%/refer%}}



# SpaceRouting

| | |
|----|----|
|Syntax     | routing  |
|Description| The `routing` tag specifies a get method for the field to be used to calculate the target space for the space operation (read , write...). The `routing` field value hash code is used to calculate the target space when the Space is running in **partitioned mode**.{{<wbr>}}The field value hash code is used to calculate the target space when the space is running in **partitioned mode**. |

Example:


```xml
<gigaspaces-mapping>
	<class name="model.Person">
		<property name="id" />
        <property name="firstName" />
        <property name="lastName" />
        <property name="type" />
		<id name="id" auto-generate="true" />
        <routing name="type" />
	</class>
</gigaspaces-mapping>
```


{{%refer%}}
[FIFO]({{%currentadmurl%}}/data-partitioning.html)
{{%/refer%}}


# Class Reference

| | |
|----|----|
|Syntax     | class-ref  |
|Argument   | class name   |
|Description| Contains the full qualified name of the specified class. |

Example:


```xml
<gigaspaces-mapping>
<class name="Person">
    <property name="firstName" />
    <property class-ref="model.Person" />
</class>
</gigaspaces-mapping>
```

# SpacePersist

| | |
|----|----|
|Syntax     | persist name=""|
|Description| This specifies a getter method for holding the persistency mode of the object overriding the class level persist declaration. This field should be of the boolean data type.{{<wbr>}}If the persist class level annotation is true, all objects of this class type will be persisted into the underlying data store (Mirror, ExternalDataSource, Storage Adapter).|
|Note       | When using this option, you must have the space class level `persist` decoration specified.|

Example:


```xml
<gigaspaces-mapping>
<class name="model.Person" persist="false" replicate="false">
    <property name="age" null-value="-1" />
    <persist name="persistence" />
</class>
</gigaspaces-mapping>
```

# SpaceVersion

| | |
|----|----|
|Syntax     |  version  |
|Description| This tag is used for object versioning used for optimistic locking. |
|Note       | The attribute must be an `int` data type. |

Example:


```xml
<gigaspaces-mapping>
	<class name="model.Person">
		<property name="id" />
		<property name="versionId" />
        <property name="firstName" />
        <property name="lastName" />
        <property name="type" />
		<id name="id" auto-generate="true" />
        <routing name="type" />
        <version name="versionId" />
	</class>
</gigaspaces-mapping>
```


{{%refer%}}
[Transactions Optimistic locking]{./transaction-optimistic-locking.html)
{{%/refer%}}



# SpaceExclude

| | |
|----|----|
|Syntax     |  exclude  |
|Description| When this tag is specified the property is not written into the space.|
|Note | - When `include-properties` is defined as `implicit`, `exclude` should   be used. This is because `implicit` instructs the system to take all POJO fields into account.{{<wbr>}}- When `include-properties` is defined as `explicit`, there is no need to use `exclude`.{{<wbr>}}- `exclude` can still be used, even if `include-properties` is not defined.  |

Example:


```xml
<gigaspaces-mapping>
	<class name="model.Person">
		<property name="id" />
        <property name="firstName" />
        <property name="lastName" />
        <property name="type" />
		<id name="id" auto-generate="true" />
        <routing name="type" />
        <exclude name="streetAddress" />
	</class>
</gigaspaces-mapping>
```


# SpaceLeaseExpiration

| | |
|----|----|
|Syntax     |  lease-expiration   |
|Description|This tag specifies the property for holding the timestamp of when the instance's lease expires (this is a standard Java timestamp based on the 1/1/1970 epoch). This property should not be populated by the user code. The space will populate this property automatically based on the lease time given by the user when writing the object. When using an external data source, you can choose to persist this value to the database. Subsequently, when data is reloaded from the external data source (at startup time for example), the space will filter out instances whose lease expiration timestamp has already passed. This field should be a `long` data type.|

Example:


```xml
<gigaspaces-mapping>
	<class name="model.Person">
		<property name="id" />
        <property name="firstName" />
        <property name="leaseExp" />
        <property name="type" />
		<id name="id" auto-generate="true" />
        <routing name="type" />
        <lease-expiration name="leaseExp" />
	</class>
</gigaspaces-mapping>
```


{{%refer%}}
[Lease automatic expiration]{./leases-automatic-expiration.html)
{{%/refer%}}



# SpaceFifoGroupingProperty

| | |
|----|----|
|Syntax     | fifo-grouping-property   path|
|Argument   | path          |
|Description| This tag is used to define a space FIFO grouping property. |
|Note | If defined, the `TakeModifiers.FIFO_GROUPING_POLL` or `ReadModifiers.FIFO_GROUPING_POLL` modifiers can be used to return all space entries that match the selection template in FIFO order. Different values of the FG property define groups of space entries that match each value. FIFO ordering exists within each group and not between different groups. |

Example:


```xml
<gigaspaces-mapping>
    <class name="com.gigaspaces.examples.FlightReservation">
        	<fifo-grouping-property name="flightInfo" path=" flightNumber" />
    </class>
</gigaspaces-mapping>

```


{{%refer%}}
[FIFO Grouping]{./fifo-grouping.html)
{{%/refer%}}



{{%refer%}}
[FIFO Grouping]{./fifo-grouping.html)
{{%/refer%}}



# SpaceDynamicProperties

| | |
|----|----|
|Syntax     | dynamic-properties |
|Description| Allows adding properties freely to a class without worrying about the schema.|


Example:


```xml
<gigaspaces-mapping>
	<class name="model.Person">
		<property name="id" />
        <property name="firstName" />
        <property name="personInfo" />
        <property name="type" />
		<id name="id" auto-generate="true" />
        <routing name="type" />
        <dynamic-properties name="personInfo" />
	</class>
</gigaspaces-mapping>
```


{{%refer%}}
[Dynamic Properties]{./dynamic-properties.html)
{{%/refer%}}




# SpaceDocumentSupport

| | |
|----|----|
|Syntax     | document-support |
|Description|  If the POJO contains properties which are POJO themselves, the space will implicitly convert these properties to space documents as needed.This works the other way around as well - if a Space document is created with a nested space document property, it will be converted to a POJO with a nested POJO property when read as a POJO. You can disable this implicit conversion and preserve the nested POJO instance within document entries by setting it to `copy`|


Example:


```xml
<gigaspaces-mapping>
	<class name="model.Person">
		<property name="id" />
        <property name="firstName" />
        <property name="address" document-support="copy"/>
	</class>
</gigaspaces-mapping>
```



{{%refer%}}
[Document POJO Interoperability]{./document-pojo-interoperability.html#deep-interoperability)
{{%/refer%}}




# Space sequence number

| | |
|----|----|
|Syntax     | sequence-number|
|Description| A sequence number (like a data-base sequence-number/autoincrement column) is a property that is given a unique incrementing value when the entry is written to the Space. The sequence-number is unique per-partition.  The property is of type Long.   |

Example:


```xml
<gigaspaces-mapping>
	<class name="model.Person">
		<property name="id" />
		<property name="sequenceNumber">
        <property name="firstName" />
        <sequence-number name="sequenceNumber" />
	</class>
</gigaspaces-mapping>
```




