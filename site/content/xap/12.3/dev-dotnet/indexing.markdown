---
type: post123
title:  Space Index
categories: XAP123NET, PRM
parent: indexing-overview.html
weight: 100
---

# Index Types

The index type is determined by the `SpaceIndexType` enumeration. The index types are:

`NONE` - No indexing is used.

`BASIC` - Basic index is used - this speeds up equality matches (equal to/not equal to).

`EXTENDED` - Extended index - this speeds up comparison matches (bigger than/less than).


# Indexing at Design Time

Specifying which properties of a class are indexed is done using attributes or `gs.xml`.

{{%tabs%}}

{{%tab "  Annotations "%}}


```csharp
[SpaceClass]
public class Person
{
    ...
    [SpaceIndex(Type=SpaceIndexType.Basic)]
    public String FirstName{ get; set;}

    [SpaceIndex(Type=SpaceIndexType.Basic)]
    public String LastName{ get; set; }

    [SpaceIndex(Type=SpaceIndexType.Extended)]
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

By default, a property's index is inherited in sub-classes (i.e. if a property is indexed in a superclass, it is also indexed in a sub-class). If you need to change the index type of a property in a sub-class, you can override the property and annotate it with `[SpaceIndex]` using the requested index type (to disable indexing. use `None`).





