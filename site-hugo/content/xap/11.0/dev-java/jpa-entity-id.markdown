---
type: post110
title:  JPA Entity Id
categories: XAP110
parent: jpa-api-overview.html
weight: 200
canonical: auto
---

{{% ssummary %}}{{% /ssummary %}}

Various options for mapping the identifier property of your JPA entities are provided.

# User-Defined Id

In this case, the Entity's Id must be set manually in the user code before attempting to persist the entity.

{{% info %}}
Reminder: GigaSpaces `@SpaceId` annotation should also be defined on the property defined with JPA's `@Id` annotation
{{% /info %}}
Example of user defined Id:


```java
@Entity
public class Car {
  private Long id;

  public Car() {
  }

  //both @Id and @SpaceId should be defined
  @Id
  @SpaceId
  public Long getId() {
    return this.id;
  }

  /* Additional Getters & Setters */

}
```

Before persisting a Car entity, we have to set its Id property:


```java
// Create a new Car instance
Car car = new Car();
// Set its Id property
car.setId(100);

// Persist
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

em.persist(car);

em.getTransaction().commit();
em.close();
```

# Auto-Generated Id

An Auto-Generated Id is an Id generated by GigaSpaces.
In order to specify that an Id is auto-generated, we'll use JPA's @GeneratedValue annotation (Please note that only GenerationType.IDENTITY is supported) and set GigaSpaces @SpaceId's autoGenerate attribute to true:


```java
@Entity
public class Form {
  // The auto-generated property can only be a String
  private String id;

  public Form() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @SpaceId(autoGenerate = true)
  public String getId() {
    return this.id;
  }

  /* Additional Getters & Setters */

}
```

When persisting a Form entity, we don't need set its Id property:


```java
// Create a new Car instance
Form form = new Form();

// Persist
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

em.persist(form);

em.getTransaction().commit();
em.close();

// Print the auto-generated Id
System.out.println(form.getId());
```

{{% warning %}}
Auto Generated Id Limitations:

- When using an auto-generated Id, the Id property type must be of type: java.lang.String.
- An auto-generated Id can only be set on an owner entity if its a part of a relation.
{{% /warning %}}

# Compound Object Id - `@EmbeddedId`

A compound object Id is an Entity Id which is not a primitive type or a primitive type wrapper.
It's defined using JPA's `@EmbeddedId` annotation.
Note that the class that uses the compound the Id should adhere to the following requirements:

1. There should be no `@Id` annotation when the `@EmbeddedId` annotation is used.
1. The `@EmbeddedId` annotated property's type should have an `@Embeddable` annotation & implement `Serializable`.

{{% note %}}
The Compound ID class must implement a `toString` method that return a unique String for each ID.
{{%/note%}}

Lets examine the following example:


```java
// This class will be used as an EmbeddedId
@Embeddable
public class ComplexObjectId implements Serilizable {
  private static final long serialVersionUID = 1L;
  private Integer number;
  private String text;

  public ComplexObjectId() {
  }

  public ComplexObjectId(Integer number, String text) {
    this.number = number;
    this.text = text;
  }

  // An Embeddable class which is used as an EmbeddedId should implement
  // toString() having a unique returned value.
  @Override
  public String toString() {
    return number.toString() + text;
  }

}

// An entity with an EmbeddedId
@Entity
public class MyObject {
  private ComplexObjectId id;

  public MyObject() {
  }

  @EmbeddedId
  @SpaceId
  public ComplexObjectId getId() {
    return this.id;
  }

  public void setId(ComplexObjectId id) {
    this.id = id;
  }

  /* Additional Getters & Setters */

}
```

# Usage example:


```java
ComplexObjectId id = new ComplexObjectId(100, "1234");
MyObject myObject = new MyObject();
myObject.setId(id);

// Persist
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();
em.persist(myObject);
em.getTransaction().commit();
em.close();

// Find
em = emf.createEntityManager();
myObject = (MyObject) em.find(MyObject.class, id);
em.close();
```

# Limitations

## `@GeneratedValue`

Only `GenerationType.IDENTITY` is supported for the `GeneratedValue.strategy()` attribute.

## Multi Value Ids

Setting an `@Id/@EmbeddedId` annotation for more than one property is not supported hence the `@IdClass` annotation is not supported.

