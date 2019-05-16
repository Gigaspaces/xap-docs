---
type: post123
title:  Space Index
categories: XAP123NET, PRM
parent: indexing-overview.html
weight: 100
canonical: auto
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

Specifying which properties of a class are indexed is done using attributes or `gs.xml`.

{{%tabs%}}

{{%tab "  Annotations "%}}


```csharp
[SpaceClass]
public class Person
{
    ...
    [SpaceIndex(Type=SpaceIndexType.Equal)]
    public String FirstName{ get; set;}

    [SpaceIndex(Type=SpaceIndexType.Equal)]
    public String LastName{ get; set; }

    [SpaceIndex(Type=SpaceIndexType.Ordered)]
    public int? Age{ get; set; }
}
```

{{% /tab %}}

{{%tab "  XML "%}}


```xml
<gigaspaces-mapping>
    <class name="Gigaspaces.Examples.Person"
        persist="false" replicate="false" fifo="false" >
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

By default, a property's index is inherited in sub-classes (i.e. if a property is indexed in a superclass, it is also indexed in a sub-class). If you need to change the index type of a property in a sub-class, you can override the property and annotate it with `[SpaceIndex]` using the requested index type (to disable indexing. use `None`).





