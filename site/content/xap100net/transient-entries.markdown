---
type: post100
title:  Transient Entries
categories: XAP100NET
parent: space-persistency-overview.html
weight: 700
---

When using a persistent space there are situations where not all the space objects need to be persistent (saved within the database). You can specify a space Object to be stored only in the space, without storing it also in the persistent store, by setting its `Persist` mode to have a **false** value. You should set this value at the space class level or at the object level. When setting it at the object level you should have it set to false before writing it into the space. After the space object has been written to the space you cannot change its persist mode.

Transient Space objects are treated the same as persistent objects, but when you call the write operation, only persistent objects are saved to the persistent store. All space operations, including batch operations, are valid for transient space object.

{{% tip "Non-Mirrored Entries "%}}
When using the [Space Persistency](./space-persistency.html) feature, you might not want all space objects to be persistent or to be delivered to some data source. The Space Persistency feature makes sure transient space objects are not persisted or delivered to the data source.
{{% /tip %}}


# POJO

The following example marks an entire class transient (i.e. non-persistent):

```csharp
[SpaceClass(Persist = false)]
public class MyData
{
	...
}
```

The following example marks the property which determines whether a class instance is transient or persistent:

```csharp
[SpaceClass]
public class MyData 
{
	[SpacePersist]
	public bool Persist {get; set;}
}
```

- Transient Space objects can be constructed using the `[SpacePersist]` on the relevant property. See [Modeling Your Data](./modeling-your-data.html) for details.
- Transient objects will be evicted from the space only by explicit take/clear operation.
- Having a space Class using the `persist=true` and memory (transient) based spaces, will not generate any errors or exceptions, but will not write these into any persistent store. The Entries will be transient.


# Space Document

```java
   SpaceDocument doc = new SpaceDocument("Entity");
   ......		
   doc.setTransient(true);
```