---
type: post123
title:  Unique Index
categories: XAP123, OSS
parent: indexing-overview.html
weight: 700
canonical: auto
---


Unique constraints can be defined for an attribute or attributes of a space class. This will ensure that only one instance of the space class exists in the space with the specific attribute value. These indexes are applicable to all types of index; Equal, Ordered, Equal-and-Ordered, Compound, and Collection indexes.

{{%note%}}
The uniqueness is enforced per partition and not over the whole cluster.
{{%/note%}}

# Operation

When the system encounters a unique constraint violation in one of the index-changing api calls (write/update/change) a  UniqueConstraintViolationException is thrown.

The operation which caused the violation is rolled back with the following effects:



| Operation | Action |
|:--------------|:------------|
|write|the entry is removed|
|update|the original value is restored|
|change|the original value is restored|

{{%note%}}If the operation(write or update) is performed under a transaction, the unique value check is done when the operation is performed (eager mode) and not when the transaction is committed. {{%/note%}}


# API

A unique attribute is added to the `@SpaceIndex` annotation. Unique = true will designate a unique constraint.

Example:

{{%tabs%}}
{{%tab "  Java "%}}

```java

@SpaceClass
public class Person
{
  @SpaceIndex(type=SpaceIndexType.EQUAL, unique = true)
  private String lastName;

  @SpaceIndex(type=SpaceIndexType.EQUAL)
  private String firstName;

  @SpaceIndex(type=SpaceIndexType.ORDERED)
  private Integer age;
 .
 .
```
{{% /tab%}}

{{%tab "  XML "%}}

```xml

<gigaspaces-mapping>
    <class name="com.gigaspaces.examples.Person" persist="false" replicate="false" fifo="false" >
        <property name="lastName">
            <index type="EQUAL" unique="true"/>
        </property>
        <property name="firstName">
            <index type="EQUAL"/>
        </property>
        <property name="age">
             <index type="ORDERED"/>
        </property>
    </class>
</gigaspaces-mapping>
```
{{% /tab%}}
{{% /tabs%}}

# Limitations

*	Supported only for ALL_IN_CACHE cache policy, not supported for LRU and other evictable cache policies
*	Not supported for local-cache/local-view since its only per-partition enforcement
*	Currently not supported for dynamic (on-the-fly) indexes.

