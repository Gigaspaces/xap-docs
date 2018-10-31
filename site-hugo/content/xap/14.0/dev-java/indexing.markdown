---
type: post140
title:  Space Index
categories: XAP140, OSS
parent: indexing-overview.html
weight: 100
---





# Index Types

The index type is determined by the `SpaceIndexType` enumeration. The index types are:

**NONE** - No indexing is used.

**EQUAL** - performs equality matching (equal to/not equal to).

**ORDERED** - performs ordered matching (bigger than/less than).

**EQUAL_AND_ORDERED** - performs both equality and ordered matching, and uses a larger memory footprint than the other indexing types.

{{%note "Note"%}}
The **BASIC** and **EXTENDED** index types have been deprecated as of version 12.3. 
{{%/note%}}

# Indexing at Design Time

Specifying which properties of a class are indexed is done using annotations or gs.xml.

{{%tabs%}}
{{%tab "  Annotations "%}}


```java
@SpaceClass
public class Person
{
    private String lastName;
    private String firstName;
    private Integer age;

    ...
    @SpaceIndex(type=SpaceIndexType.EQUAL)
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    @SpaceIndex(type=SpaceIndexType.EQUAL)
    public String getLastName() {return lastName;}
    public void setLastName(String name) {this.lastName = name;}

    @SpaceIndex(type=SpaceIndexType.ORDERED)
    public Integer getAge() {return age;}
    public void setAge(Integer age) {this.age = age;}
}
```

{{% /tab %}}
{{%tab "  XML "%}}


```java
<gigaspaces-mapping>
    <class name="com.gigaspaces.examples.Person" persist="false" replicate="false" fifo="false" >
        <property name="lastName">
            <index type="EQUAL"/>
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

{{% /tab %}}
{{% /tabs %}}

## Inheritance

By default, a property's index is inherited in sub-classes (i.e. if a property is indexed in a superclass, it is also indexed in a sub-class). If you need to change the index type of a property in a sub-class, you can override the property and annotate it with `@SpaceIndex` using the requested index type (to disable indexing, use `NONE`).


