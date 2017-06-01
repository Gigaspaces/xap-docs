---
type: post110
title:  Indexing
categories: XAP110
parent: none
weight: 900
---





For read and take operations, XAP iterates non-null values that match template or SQLQuery criteria, returning matches from the Space. This process can be time consuming, especially when there are many potential matches. To improve performance, it is possible to index one or more Space class properties. The Space maintains additional data for indexed properties, which shortens the time required to determine a match, thus improving performance.


<br>


{{%fpanel%}}

[Basic concept](./indexing.html){{<wbr>}}
Basic index types

[Indexing Nested properties](./indexing-nested-properties.html){{<wbr>}}
An index can be defined on a nested property to improve performance of nested queries.

[Indexing collections](./indexing-collections.html){{<wbr>}}
An index can be defined on a Collection property (java.util.Collection implementation).

[Compound index](./indexing-compound.html){{<wbr>}}
Compound indexes can be defined using multiple class properties.

[Geospatial index](./indexing-geospatial.html){{<wbr>}}
Indexes can be created for geometry data types.
 

[Unique index](./indexing-unique.html){{<wbr>}}
Unique constraints can be defined for a property or properties of a space class.

 

{{%/fpanel%}}
