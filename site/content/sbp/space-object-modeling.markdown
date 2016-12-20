---
type: post
title:  Space Object Modeling
categories: SBP
parent: data-access-patterns.html
weight: 1700
---



|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim| 7.1 | March 2009|    |    |




When constructing the space classes and their relationship, and having persistency turned on, you should design the space classes to follow a few basic rules. The rules are mostly relevant when having associations between different objects, and when the database tables include foreign and primary key relations that need to be mapped to objects in memory.

There are two options you may use: a **Hibernate-based model** and a **Space-based model**. The latter is the recommended one, since it provides better performance, scalability and consumes less memory when an object is stored in memory.

# The Hibernate-Based Object Model
When you have an object graph object model with collections or references association (using Hibernate OneToMany, ManyToOne), you can use the Hibernate model to load these objects into the space. There are some important considerations when using this approach:

- You should use the DefaultExternalDataSource.
- The footprint utilization is high since you might end up loading the same object more than once into the space JVM. This can happen in the following cases:

1. When the loaded objects are regular space objects (in case you wrote these also as space objects) and also an embedded object within another space object.
2. When you have the same object referenced from more than one space object.

- The initial-load phase is relatively slow (mostly due to the duplicated objects).
- Data should be fully loaded from the database - only **Eager mode** is supported. Lazy fetching is not supported.
- You might end up having data inconsistency problems due to the duplicated objects in memory.

# The Space Object Model
If you want to optimize the memory footprint, and avoid duplicated objects in memory, and the inconsistency involved with the usage of the Hibernate model when loading collection/reference objects, you need to implement the collection/reference object load and store, using the GigaSpaces API (at the DAO layer or some helper class). Important considerations when using this approach:

- You can use the StatelessExternalDataSource that is usually faster than the DefaultExternalDataSource - but it does not support collection/reference load and store.

- To load a collection/reference you should:

1. Store the ID (and the routing data) of the parent object within the child object (in case we have a oneToMany relationship), to be able to fetch the collection. This ID field within the child object (of its parent object ID) should be indexed to allow fast data retrieval. The GigaSpace.readMultiple should be used to implement the relevant data access routine. In some cases it makes sense to implement such data access routines using the Task executors that allow you to implement multi-level object graph data retrieval without having multiple remote calls.

2. Store the ID (and routing data) of the child object within the parent (in case of a OneToOne relationship). The GigaSpace.readById method should be used to implement the relevant data access routine.

- The initial load phase runs faster than the Hibernate-based model.

- Since each object loaded is a true space object, you have better control of how data is partitioned.

- With this approach, the actual referenced object/collection within the space object is not stored within the space object. It should be excluded using the @SpaceExclude decoration. This allows the field data to be transient within the space object.

## Example

The Parent Class:


```java
@Entity
@Table(name = "parent")
public class Parent
{
	@Transient
	private Child child;

	@SpaceExclude
	public Child getChild() {
		return this.child;
	}

	@Column(name = "child_id")
	private Integer childId;

	protected void setChild(Child child) {
		this.child= child;
		if (child!= null) {
			this.childId = child.getId();
		}  else {
			this.childId = null;
		}
	}

	protected Integer getChildId()
	{
		return childId;
	}

	protected void setChildId(Integer childId)
	{
		this.childId= childId;
	}

}
```

The Child Class:


```java
public class Child implements Serializable {

    @Id
    private Integer id;

    public void setId(Integer id) {
		this.id = id;
	}

    @SpaceId(autoGenerate = false)
    @SpaceRouting
    public Integer getId() {
		return id;
	}
}
```

The Data access layer:


```java
public Child loadChild(int childId) {
	Child child = gigaSpace.readById(Child.class, childId);
	return child;
}
```


```java
@Transactional
public void storeChild(Child entity) {
if (entity.getId() == null) {
    entity.setId(idGenerator.generateId());
}
gigaSpace.write(entity);
}
```

In both cases, you can implement object navigation (myObject.getA().getB()). You just need to make sure that the collections/references object load is supported.

