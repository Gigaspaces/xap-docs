---
type: post123
title:  POJO Support
categories: XAP123, OSS
parent: none
weight: 400
---

The XAP interface fully supports POJOs. In terms of preconditions, your POJO classes must follow the JavaBeans conventions (have a no-argument constructor) and declare a getter and setter to every field you want saved on the Space. The POJO class doesn't have to implement `java.io.Serializable`, but its properties must. This is necessary because the POJOs fields are extracted when written to the Space, and stored in a special tuple format that enables the Space to index and analyze them more easily. Therefore the actual POJO is not sent over the network, but rather its properties.

# Metadata

When writing POJOs to the Space, you can provide some metadata about the POJO's class to the Space, using Java 5 annotations or an XML configuration. This overview uses annotation to provide metadata. For a complete reference to POJO annotations and XML configuration, refer to the [POJO Metadata](./modeling-your-data.html) section.

Here is an overview of the most commonly used POJO annotations:

- `@SpaceClass`: Class level annotation - Optional. Used to mark a class that is written to the Space (by default if you write a class to the Space, it is marked as such automatically). Use this to provide additional metadata at the class level, such as whether or not this class is persisted to a database if [Persistency](./space-persistency-overview.html) is configured on the Space.

- `@SpaceId`: The identifier property of the POJO. This property uniquely identifies the POJO within the Space, and is similar to a primary key in a database. You can choose between an application-generated ID (`autoGenerate=false`), and an automatically generated ID (`autoGenerate=true`).

- `@SpaceProperty`: Defines various attributes related to a POJO property - the null value if you are using a primitive property (e.g. `nullValue=-1`), and whether they should be indexed for faster querying (`index=EQUAL`).

- `@SpaceVersion`: Optional. The version property of the POJO. Defines a property to be used to indicate the version of the instance (used to implement optimistic locking).

- `@SpaceRouting`: The routing property for the POJO. In a partitioned Space, this controls how instances of a certain class are distributed across the partitions. When two instances (even of different classes), have the same value for their routing property, they end up in the same partition.

 
 You can define Space class metadata by class and field level decorations. These can be defined via annotations or XML configurations files (**gs.xml file**).
 
{{%refer%}}
For more information about POJO metadata, refer to [Modeling your Data](./modeling-your-data.html).
{{%/refer%}}

{{%warning "Important"%}}
When using annotations and the gs.xml file to define metadata, the metadata in the gs.xml will be used and the annotations will be ignored. **It is not recommended to use the two metadata definitions for a Space class at the same time**.
{{%/warning%}}

##  Primitives vs. Wrapper Classes for POJO Properties 

XAP supports both primitives (`int`, `long`, `double`, `float`, etc.), and primitive wrappers (`java.lang.Integer`, `java.lang.Double`, etc.). In general, it is recommended that you use the primitive wrapper. This enables you to use the `null` values as a wildcard when using template matching.

If you use primitives, make sure you define the following for your POJO class:

- The `null` value for the property - primitive types are not nullable, therefore you must indicate to the Space a value that is treated as `null`. This is important for template matching (see below), where null values are considered as wildcards, and do not restrict the search.
- It is recommended that the initial value (assigned in the constructor) for this field matches the null value. This enables you to quickly create new instances, and use them as templates for template matching without changing any property except the ones you want to use for matching.
 
The following is a sample POJO class:


```java
@SpaceClass
public class Person {
    private Integer id;
    private String name;
    private String lastName;
    private int age;

    ...
    public Person() {}

    @SpaceId(autoGenerate=false)
    @SpaceRouting
    public Integer getId() { return id;}

    public void setId(Integer id) {  this.id = id; }

    @SpaceIndex(type=SpaceIndexType.EQUAL)
    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    @SpaceProperty(nullValue="-1")
    public int getAge(){ return age; }

    .......
}
```

# POJO Rules

When using a POJO as a Space domain class, follow these guidelines:

- A POJO class must implement a default (zero argument) constructor.
- Getter/setter methods for fields that you want to be persisted in the Space.
- Non-primitive fields must implement `Serializable`. For example, if you are using a POJO class that contains a nested class.
- Primitive `boolean` should not be used as a POJO field, as this could lead to problems when using template-based matching. `Boolean` should be used instead.
- A POJO class should have Space class metadata decorations using annotations or a `gs.xml` file with relevant metadata (indexes, version field, FIFO mode, persistency mode, primary key (i.e. `id`)). If neither are provided, the **defaults** are presumed. (The default settings may not always match your needs.)
- The ID field can be determined using the `@SpaceId` annotation or the `id` tag.
- The `SpaceId` field can be `java.lang.String` type, or any other type that implements the `toString()` that provides a unique value.
- The `@Spaceid` annotation or `id` tag must be declared when performing update operations.
- A POJO class must implement the `Serializable`  interface if used as a parameter for a remote call ([Space Based Remoting](./space-based-remoting-overview.html)).

## Controlling Space Class Fields Introduction

To force XAP to ignore a POJO field when the Space class is introduced to the Space, use one of the following:

- `@SpaceExclude` annotation on the getter method for the fields you don't want included as part of the Space class.
- `@SpaceClass(includeProperties=IncludeProperties.EXPLICIT)` class-level annotation and use the `@SpaceProperty()` with each getter method for fields that should be considered part of the Space class.

## Space POJO Class Metadata Data File

POJO Space mapping files `gs.xml` files can be loaded from:

- `<CLASSPATH>\config\mapping` folder, or
- The same package where the class file is located using the format `<<Class Name>>.gs.xml`.

## User-Defined Space Class Fields

You may have user-defined data types (non-primitive data types) with your Space class. These should implement the `Serializable` or `Externalizable` interface. The user-defined class nested fields can be used with queries, and can be indexed. See the [Nested Properties](./query-sql.html#Nested Properties) and the [Nested Properties Indexing](./indexing.html#Nested Properties Indexing) section for details.

## Reference Handling

When running in embedded mode, Space object fields **are passed by reference** to the Space. Extra caution should be taken with non-primitive, non-mutable fields such as collections (`HashTable, Vector`). Changes made to those fields outside the context of the Space affect the value of those fields in the Space, and may result in unexpected behavior.

For example, index lists aren't maintained because the Space is unaware of the modified field values. For those fields, it is recommended to pass a cloned value rather then the reference itself. Passing a cloned value is important when several threads access the Object fields, for example application threads and replication threads.

## Non-Indexed Fields

Non-indexed fields that are not used for queries should be placed within a user-defined class (payload object), and have their getter and setter placed within the payload class. This improves the read/write performance because these fields aren't introduced to the Space class model.

{{% note "Indexing"%}}
[Indexing](./indexing-overview.html) is **critical** for good performance over large Spaces. Don't forget to index properly with the @SpaceIndex(type=SpaceIndexType.EQUAL) or @SpaceIndex(type=SpaceIndexType.ORDERED) annotation, or use the gs.xml equivalent.
{{% /note %}}

# Jini Entry

{{%warning "Important"%}}
The XAP POJO **should not** implement the `net.jini.core.entry.Entry` interface. When doing so a **different handling is performed** for the object, ignoring all XAP POJO annotations/XML decorations.
{{%/warning%}}