---
type: post123
title:  Collection Index
categories: XAP123, OSS
parent: indexing-overview.html
weight: 300
---

An index can be defined on a Collection property (java.util.Collection implementation) or Array. Setting such an index means that each of the Collection's or Array's items is indexed. Setting an index on a Collection / Array done using the SpaceIndex.path() attribute where a Collection / Array property should be followed by "\[\*\]".

The following example shows how to define an index on a List of Integers:


```java
@SpaceClass
public class CollectionIndexingExample {
  private Integer id;
  private List<Integer> numbers;

  // Getter and setter methods...

  @SpaceIndex(path="[*]")   // This means that each Integer in the List is indexed.
  public List<Integer> getNumbers() {
    return this.numbers;
  }

}
```

The following query shows how to use the index:


```java
SQLQuery<CollectionIndexingExample> sqlQuery = new SQLQuery<CollectionIndexingExample>(
    CollectionIndexingExample.class, "numbers[*] = 30");
CollectionIndexingExample[] result = gigaspace.readMultiple(sqlQuery);
```

{{% refer %}}
See the [Free Text Search](./query-sql.html#Free Text Search) section for more details.
{{%/refer%}}

# Nested property within a Collection

Its also possible to index a nested property within a collection.

The following example shows how to define an index on Book.id property which resides in a Collection property in Author:


```java
@SpaceClass
public class Author {
  private String name;
  private Collection<Book> books;

  // Getter and setter methods...

  @SpaceIndex(path="[*].id")   // This means that each Book.id in the Collection is indexed.
  public Collection<Book> getBooks() {
    return this.books;
  }
}

public class Book {
  private Integer id;
  private String name;

  // Getter and setter methods...

  public Integer getId() {
    return this.id;
  }
}
```

The following query shows how to take advantage of the defined index:


```java
SQLQuery<Author> sqlQuery = new SQLQuery<Author>(Author.class, "books[*].id = 57");
Author result = gigaspace.read(sqlQuery);
```

Setting an index on a Collection within a nested property is also accepted:


```java
@SpaceClass
public class Employee {
  private Long id;
  private Information information;

  // Getter and setter methods...

  @SpaceIndex(path="phoneNumbers[*]")
  public Information getInformation() {
    return this.information;
  }

}

public class Information {
  private Address address;
  private List<String> phoneNumbers;

  // Getter and setter methods...

  public List<String> getPhoneNumbers() {
    return this.phoneNumbers;
  }

}
```

{{% note "Note"%}}
@SpaceIndex(type=SpaceIndexType.EQUAL), @SpaceIndex(type=SpaceIndexType.ORDERED), and @SpaceIndex(type=SpaceIndexType.EQUAL_AND_ORDERED) are supported.
{{% /note %}}

