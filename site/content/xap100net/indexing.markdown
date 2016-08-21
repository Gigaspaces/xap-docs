---
type: post100
title:  Basic Index
categories: XAP100NET
parent: indexing-overview.html
weight: 100
---

{{% ssummary %}} {{% /ssummary %}}



When a space is looking for a match for a read or take operation, it iterates over non-null values in the template, looking for matches in the space. This process can be time consuming, especially when there are many potential matches. To improve performance, it is possible to index one or more properties. The space maintains additional data for indexed properties, which shortens the time required to determine a match, thus improving performance.

# Choosing which Properties to Index

One might wonder why properties are not always indexed, or why all the properties in all the classes are not always indexed. The reason is that indexing has its downsides as well:

- An indexed property can speed up read/take operations, but might also slow down write/update operations.
- An indexed property consumes more resources, specifically memory footprint per entry.

# When to Use Indexing

Naturally the question arises of when to use indexing. Usually it is recommended to index properties that are used in common queries. However, in some scenarios one might favor less footprint, or faster performance for a specific query, and adding/removing an index should be considered.

{{% warning %}}  Remember that "Premature optimization is the root of all evil". It is always recommended to benchmark your code to get better results. {{%/warning%}}

# Index Types

The index type is determined by the `SpaceIndexType` enumeration. The index types are:

`NONE` - No indexing is used.

`BASIC` - Basic index is used - this speeds up equality matches (equal to/not equal to).

`EXTENDED` - Extended index - this speeds up comparison matches (bigger than/less than).


# Indexing at Design-time

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

By default, a property's index is inherited in sub classes (i.e. if a property is indexed in a super class, it is also indexed in a sub class). If you need to change the index type of a property in a subclass you can override the property and annotate it with `[SpaceIndex]` using the requested index type (to disable indexing use `None`).

# Indexing at Run-time

Indexes can be added dynamically at run-time using the GigaSpaces Management Center GUI.

{{% note %}}
Removing an index or changing an index type is currently not supported.
{{%/note%}}




