---
type: post121
title:  JPA Relationships
categories: XAP121
parent: jpa-api-overview.html
weight: 300
---

{{% ssummary %}}{{% /ssummary %}}



The XAP JPA relationships model is different than Relational Databases model. In GigaSpaces relationships are owned, which means that an owner of a relationship holds the owned entities within itself in Space. For instance, if an Author has a One-to-many relationship with Book, in Space all the Book instances relevant for a specific Author will reside
within a Collection in Author.

{{% vbar %}}
When defining a One-to-one/One-to-many relationship the cascading type should be set to CascadeType.ALL using the relationship's annotation cascade attribute since no-cascading is unsupported.
Setting cascading globally can also be done in orm.xml:


```xml
<persistence-unit-metadata>
  <persistence-unit-defaults>
    <cascade-persist/>
  </persistence-unit-defaults>
</persistence-unit-metadata>
```
{{% /vbar %}}

{{%refer%}}
Further information can be found on the [Modeling your data](/sbp/modeling-your-data.html) page.
{{%/refer%}}

# Embedded

In the following example we have a Store entity which has an embedded Address property.
In this case, the Address property is saved as is within Store.


```java
// An Embeddable Address object
@Embeddable
public class Address implements Serializable {
  private String street;
  private String city;
  private String country;

  public Address() {
  }

  public String getStreet() {
    return this.street;
  }

  public String getCity() {
    return this.city;
  }

  public String getCountry() {
    return this.country;
  }

  /* Additional Getters & Setters */

}

// A Store entity with an embedded Address property
@Entity
public class Store {
  private Integer id;
  private Address address;

  public Store() {
  }

  @Id
  @SpaceId
  public Integer getId() {
    return this.id;
  }

  @Embedded
  @SpaceIndex(path = "city") // Address.city is indexed
  public Address getAddress() {
    return this.address;
  }

  /* Additional Getters & Setters */

}
```

We created an Embeddable Address object and used it as a property in our Store object.

{{% tip %}}
Please note that Embeddable classes must be Serializable since they're transferred over the network.
{{% /tip %}}

It's possible to query a Store entity by an Address property in the following way:


```java
EntityManager em = emf.createEntityManager();
Query query = em.createQuery("SELECT store FROM com.gigaspaces.objects.Store store WHERE s.address.city = 'London'");
List<Store> result = (List<Store>) query.getResultList();
```

# One-to-one

GigaSpaces JPA One-to-one relationship is very similar to an embedded relationship except for the fact that when querying
the owner entity it is possible to use an Inner Join.

As with Embeddable classes, owned entities in a relationship should always be Serializable since they are transferred over the network.

In the following example we show a One-to-one relationship between two entities, Order & Invoice:


```java
@Entity
public class Order {
  private Long id;
  private Date date;
  private Invoice invoice;

  public Order() {
  }

  @Id
  @SpaceId
  public Long getId() {
    return this.id;
  }

  public Date getDate() {
    return this.date;
  }

  @OneToOne(cascade = CascadeType.ALL)
  @SpaceIndex(path = "sum", type = SpaceIndexType.EXTENDED) // Invoice.sum is indexed
  public Invoice getInvoice() {
    return this.invoice;
  }

  // Additional Getters & Setters...

}

// An Invoice entity which is owned in the relationship and
// therefore should implement Serializable
@Entity
public class Invoice implements Serializable {
  private Long id;
  private Double sum;

  public Invoice() {
  }

  @Id
  @SpaceId
  public Long getId() {
    return this.id;
  }

  public Double getSum() {
    return this.sum;
  }

  // Additional Getters & Setters...

}
```

For One-to-one relationship we can use an Inner Join for querying:


```java
EntityManager em = emf.createEntityManager();
Query query = em.createQuery("SELECT order FROM com.gigaspaces.objects.Order order JOIN o.invoice invoice WHERE invoice.sum > 499.99");
List<Order> orders = (List<Order>) query.getResultList();
```

{{% tip %}}
We defined an extended index on Invoice.sum and therefore the above query takes advantage of the defined index.
{{% /tip %}}

# One-to-many

GigaSpaces JPA One-to-many relationship means that the owner of the relationship stores the owned entities in a collection within itself.

As with One-to-one, owned entities in a relationship should always be Serializable since they are transferred over the network.

Lets examine the following example:


```java
// An Author entity which will be the owner of a relationship.
@Entity
public class Author {
  private Integer id;
  private String name;
  private List<Book> books;

  public Author() {
  }

  @Id @SpaceId
  public Integer getId() {
    return this.id;
  }

  @SpaceRouting // Routing is determined by the author's name
  public String getName() {
    return this.name;
  }

  @OneToMany(cascade = CascadeType.ALL)
  @SpaceIndex(path = "[*].id") // Books are indexed by their id
  public List<Book> getBooks() {
    return this.books;
  }

  // Additional Getters & Setters..

}

// A Book entity which is owned in a relationship
// Book shouuld implement Serializable since its transferred over the network
@Entity
public class Book implements Serializable {
  private Integer id;
  private String name;

  public Book() {
  }

  @Id
  @SpaceId
  public Integer getId() {
  }

  public String getName() {
  }

  // Additional Getters & Setters..

}
```

We can use a JPQL Inner Join for querying an Author by a specific Book id:


```java
EntityManager em = emf.createEntityManager();
Query query = em.createQuery("SELECT author FROM com.gigaspaces.objects.Author author JOIN author.books book WHERE book.id = 100");
Author result = (Author) query.getSingleResult();
```

{{% tip %}}
We defined an index on Book.id and therefore the above query takes advantage of the defined index.
{{% /tip %}}

# Limitations

### Working with embedded entities limitations

When working with embedded entities its not possible to call JPA methods on owned entities.

### Owned many-to-many relationship

Owned many-to-many relationship is not supported since GigaSpaces data model doesn't permit it.
It is possible to implement such a relation by explicitly setting the Ids for each of the relationship participants.

### Unowned relationships

Unowned relationships where each part of the relation is represented as a Data Type in space is not supported.

