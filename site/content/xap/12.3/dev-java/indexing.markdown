---
type: post123
title:  Space Index
categories: XAP123, OSS
parent: indexing-overview.html
weight: 100
---





# Index Types

The index type is determined by the `SpaceIndexType` enumeration. The index types are:

**NONE** - No indexing is used.

**BASIC** - Basic index is used - this speeds up equality matches (equal to/not equal to).

**EXTENDED** - Extended index - this speeds up comparison matches (bigger than/less than).

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
    @SpaceIndex(type=SpaceIndexType.BASIC)
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    @SpaceIndex(type=SpaceIndexType.BASIC)
    public String getLastName() {return lastName;}
    public void setLastName(String name) {this.lastName = name;}

    @SpaceIndex(type=SpaceIndexType.EXTENDED)
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
            <index type="BASIC"/>
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

{{% /tab %}}
{{% /tabs %}}

## Inheritance

By default, a property's index is inherited in sub-classes (i.e. if a property is indexed in a superclass, it is also indexed in a sub-class). If you need to change the index type of a property in a sub-class, you can override the property and annotate it with `@SpaceIndex` using the requested index type (to disable indexing, use `NONE`).


