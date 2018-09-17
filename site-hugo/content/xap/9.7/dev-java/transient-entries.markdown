---
type: post97
title:  Transient Entries
categories: XAP97
parent: space-persistency-overview.html
weight: 700
---


When using a persistent space there are situations where not all the space objects need to be persistent (saved within the database). You can specify a space Object to be stored only in the space, without storing it also in the persistent store, by setting its `persist` mode to have a **false** value. You should set this value at the space class level or at the object level. When setting it at the object level you should have it set to false before writing it into the space. After the space object has been written to the space you cannot change its persist mode.

Transient Space objects are treated the same as persistent objects, but when you call the write operation, only persistent objects are saved to the persistent store. All space operations, including batch operations, are valid for transient space object.

{{% tip  "Non-Mirrored Entries "%}}
When using the [Space Persistency](./space-persistency.html) feature, you might not want all space objects to be persistent or to be delivered to some data source. The Space Persistency feature makes sure transient space objects are not persisted or delivered to the data source.
{{% /tip %}}


# POJO

```java
@SpaceClass (persist=false)
public class MyData { 
	...
}
```


```java
@SpaceClass
public class MyData {
	...
	@SpacePersist
	boolean getPersistMode()
	{
		return false;
	}
}
```

- Transient Space objects can be constructed using the `@SpacePersist` on the relevant getter method. See the [POJO Metadata](./modeling-your-data.html) for details.
- Transient objects will be evicted from the space only by explicit take/clear operation.
- Having a space Class using the `persist=true` and memory (transient) based spaces, will not generate any errors or exceptions, but will not write these into any persistent store. The Entries will be transient.


# Space Document
 
```java
   SpaceDocument doc = new SpaceDocument("Entity");
   ......		
   doc.setTransient(true);
```