---
type: post123
title:  JPA API
categories: XAP123, OSS
parent: other-data-access-apis.html
weight: 200
---



The Java Persistency API (JPA) is a Java-programming language framework that manages relational data in applications using the Java platform. XAP's JPA API allows you to use the JPA's functionality and annotations, and to execute JPQL queries on a Space. XAP's JPA implementation is based on {{%exurl "OpenJPA" "http://openjpa.apache.org/"%}}.

{{% note "Note"%}}
We recommend that you [familiarize yourself with JPA](http://download.oracle.com/javaee/6/tutorial/doc/bnbpz.html) before reading this section. We also suggest that you review the [XAP PetClinic JPA Tutorial](/sbp/first-jpa-app.html), which describes how a standard JPA application (the Spring PetClinic) can be adapted to XAP JPA and deployed to the XAP runtime environment.
{{% /note %}}

# XAP JPA Configuration

## OpenJPA

OpenJPA's JAR file is included with the XAP distribution (provided under `<XAP root>/lib/optional/jpa`), and the XAP-specific JPA implementation classes are part of the OpenSpaces JAR (located under `<XAP root>/lib/required/xap-openspaces.jar`).
Maven users should define the following dependency in their `pom.xml` file:


```xml
<dependencies>
  <dependency>
    <groupId>org.apache.openjpa</groupId>
    <artifactId>openjpa</artifactId>
    <version>{{%version openjpa%}}</version>
  </dependency>
</dependencies>
```




## The persistence.xml File

To enable the XAP JPA implementation, you should specify the following three mandatory properties in your `persistence.xml` file:

- `BrokerFactory` should be set to `abstractstore`, which tells OpenJPA that an alternate `StoreManager` (the layer responsible for interacting with the underlying database) is going to be used.
- `abstractstore.AbstractStoreManager` should be set to `org.openspaces.jpa.StoreManager`, which tells OpenJPA to use the OpenSpaces `StoreManager`.
- `LockManager` should be set to `none` because OpenJPA's default lock manager is set to `version` (Optimistic locking), which is currently unsupported.

Your `persistence.xml` file should be placed in any **/META-INF folder in your classpath.

### BrokerFactory Property

There is no need to set the `abstractstore.AbstractStoreManager` property. Instead, make sure to set the `BrokerFactory` property to `org.openspaces.jpa.BrokerFactory`, as shown in the example below.

The following is an example of a XAP JPA `persistence.xml` configuration file:


```xml
<persistence-unit name="gigaspaces" transaction-type="RESOURCE_LOCAL">
	<provider>org.apache.openjpa.persistence.PersistenceProviderImpl</provider>
	<properties>
            <property name="BrokerFactory" value="abstractstore"/>
            <property name="abstractstore.AbstractStoreManager" value="org.openspaces.jpa.StoreManager"/>
            <property name="LockManager" value="none"/>
	</properties>
</persistence-unit>
```



```xml
<persistence-unit name="gigaspaces" transaction-type="RESOURCE_LOCAL">
	<provider>org.apache.openjpa.persistence.PersistenceProviderImpl</provider>
	<properties>
            <property name="BrokerFactory" value="org.openspaces.jpa.BrokerFactory"/>
            <property name="LockManager" value="none"/>
	</properties>
</persistence-unit>
```

### Transaction Read Lock Level

The default XAP JPA read lock level is set to "read", which is equivalent to XAP's `ReadModifiers.REPEATABLE_READ`. In order to use `ReadModifiers.EXCLUSIVE_READLOCK`, the `ReadLockLevel` property should be set to "write":


```xml
  <property name="ReadLockLevel" value="write"/>
```

## Space Connection Injection

Specifying a Space connection URL or a Space instance can be done in one of the ways described below.

### Referencing an Existing Space Instance through Factory Properties

You can specify a Space instance when creating an `EntityManagerFactory` as follows:


```java
GigaSpace gigaspace = ...
Properties properties = new Properties();
properties.put("ConnectionFactory", gigaspace.getSpace());
EntityManagerFactory emf = Persistence.createEntityManagerFactory("gigaspaces", properties);
```

### Injection using Spring

You can inject either an `EntityManager` or `EntityManagerFactory` using Spring. Before reading this, we recommend that you familiarize yourself with [Spring's JPA support](http://static.springsource.org/spring/docs/3.0.x/reference/orm.html#orm-jpa).

The following example shows how to inject a Space-based `EntityManagerFactory`. The Spring XML configuration file declares a Space, an `EntityManagerFactory`, a transaction manager, and a JPA service bean (this is our DAO):


```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-{{%version "spring"%}}.xsd">

	<!-- space definition -->
    <os-core:embedded-space  id="space" space-name="jpaSpace" lookup-groups="test"/>

    <!-- gigaspace definition -->
    <os-core:giga-space id="gigaSpace" space="space"/>

    <!-- JPA entity manager factory definition -->
	<bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
                <!-- this relies on the fact that our persistence.xml file defines a persistence unit named "gigaspaces" -->
		<property name="persistenceUnitName" value="gigaspaces"/>
		<property name="jpaVendorAdapter">
			<bean class="org.openspaces.jpa.OpenSpacesJpaVendorAdapter">
				<property name="space" value="#{gigaSpace.space}"/>
			</bean>
		</property>
	</bean>

    <!-- JPA transaction manager definition -->
	<bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
		<property name="entityManagerFactory" ref="entityManagerFactory" />
	</bean>

    <!-- support annotations -->
	<bean class="org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor" />
	<context:annotation-config/>
	<tx:annotation-driven transaction-manager="transactionManager"/>

    <!-- JPA example service definition -->
	<bean id="jpaService" class="org.openspaces.jpa.JpaService" />
</beans>
```

In our DAO, Spring injects the `EntityManager` using the `@PersistentContext` annotation. Spring creates an `EntityManager` using the `EntityManagerFactory` defined at the beginning of every transaction.


```java
@Repository
@Transactional
public class JpaService {
    @PersistenceContext
    private EntityManager em;

    public JpaService() {
    }

    @Transactional
    public void persistObject() {
        em.persist(...);
    }
}
```
{{% refer %}}
For detailed information regarding the `persistence.xml` file, refer to the [OpenJPA Manual](http://openjpa.apache.org/builds/2.0.0/apache-openjpa-2.0.0/docs/manual/jpa_overview_persistence.html#jpa_overview_persistence_xml).
{{% /refer %}}

## Listing Your Persistent Classes

When working with persistent classes, there are several ways to make the JPA layer aware of them.

When using Spring, this is done automatically unless otherwise specified (see below). Spring will scan your classpath, looking for classes that have the `@Entity` annotation.

When not using Spring, there are two options:

- Point to an `orm.xml` file your `persistence.xml` file:


```xml
  <persistence-unit name="gigaspaces" transaction-type="RESOURCE_LOCAL">
    <mapping-file>META-INF/orm.xml</mapping-file>
    <exclude-unlisted-classes/>
  </persistence-unit>
```

- Use the `<class>` tag in your `pesistence.xml` file. For example, if you're going to use the classes `Trade`, `Book` & `Author`, list them in your `persistence.xml` file as follows:


```xml
<persistence-unit name="gigaspaces" transaction-type="RESOURCE_LOCAL">
	<provider>org.apache.openjpa.persistence.PersistenceProviderImpl</provider>
        <class>org.openspaces.objects.Trade</class>
        <class>org.openspaces.objects.Book</class>
        <class>org.openspaces.objects.Author</class>
	<properties>
        <property name="BrokerFactory" value="abstractstore"/>
        <property name="abstractstore.AbstractStoreManager" value="org.openspaces.jpa.StoreManager"/>
        <property name="LockManager" value="none"/>
	</properties>
</persistence-unit>
```

## Enhancing Your Classes

JPA classes are monitored at runtime for automatic dirty detection. There must be bytecode enhancement in order for this to be transparent to the user. OpenJPA offers 3 options for this:

1. Enhance at build time using an Ant or a Maven script.
1. Enhance at runtime  using OpenJPA's Java agent enhancer.
1. When using Spring, you can [specify a `LoadTimeWeaver`](http://static.springsource.org/spring/docs/3.0.x/reference/orm.html#orm-jpa-setup-lcemfb) that will enhance the classes at load time.

{{% info "Info"%}}
The first option (enhance at build time) is the best in terms of performance and suitability for the XAP runtime environment.For detailed information about how to enhance your entities, refer to the [OpenJPA Entity Enhancement page](http://openjpa.apache.org/entity-enhancement.html).
{{% /info %}}

# XAP JPA Entities

An entity class must meet the following requirements:

1. The class must be annotated with the `javax.persistence.Entity` annotation.
1. The class must have a public no-argument constructor (the class may have other constructors).
1. XAP and JPA annotations can only be declared on Getters, NOT on fields.


### Annotations

XAP JPA Entities must have both JPA and XAP annotations for the following annotations:


|XAP|JPA|
|:---------|:--|
| @SpaceId| @Id/@EmbeddedId|
| @SpaceExclude| `@Transient|

As with XAP POJOs, you can use the `@SpaceIndex` & `@SpaceRouting` annotations with GigaSpaces JPA entities.

{{% note "Note"%}}
Indexes should only be declared in the owning entity of a relationship. Examples are available on the [JPA Relationships](./jpa-relationships.html) page.
{{% /note %}}

THe following is an example of a basic JPA Entity:


```java
@Entity
public class Trade {
  private Long id;
  private Double quantity;
  private List<Double> rates;
  private boolean state;

  // Public no-argument constructor
  public Trade() {
  }

  // Both SpaceId and Id should be declared on the id property
  @Id
  @SpaceId
  public Long getId() {
    return this.id;
  }

  // Persistent property, no additional GigaSpaces annotations needs to be used.
  public Double getQuantity() {
    return this.quantity;
  }

  // A persistent collection property. In this case we'll use a GigaSpaces annotation
  // for indexing its values.
  @ElementCollection
  @SpaceIndex(path = "[*]")
  public List<Double> getRates() {
    return this.rates;
  }

  // A transient property. In this case we'll use both GigaSpaces and JPA annotations
  @Transient
  @SpaceExclude
  public boolean getState() {
    return this.state;
  }

  /* Additional Getters & Setters... */

}
```

{{%refer%}}
For auto-generated ID declaration and complex object ID declaration, refer to [JPA Entity ID](./jpa-entity-id.html).
{{%/refer%}}

The following is an example of a JPA Owner entity with a one-to-many relationship:


```java
@Entity
public class Owner {
    //
    private Integer id;
    private String name;
    private List<Pet> pets;
    //
    public Owner() {
    }
    public Owner(Integer id, String name, List<Pet> pets) {
        super();
        this.id = id;
        this.name = name;
        this.pets = pets;
    }
    //
    @Id
    @SpaceId
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @SpaceRouting
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @OneToMany(cascade = CascadeType.ALL)
    public List<Pet> getPets() {
        return pets;
    }
    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}
```

## Non-Indexed Fields

Non-indexed fields that are not used for queries should be placed within a user-defined class (payload object) and have their getter and setter placed within the payload class. This improves read/write performance because these fields are not introduced to the Space class model.

# JPA Query Language (JPQL)

XAP JPA supports a subset of JPQL. Here are a few examples of the supported queries:

**Querying on Properties of Nested Objects**


```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("gigaspaces");
EntityManager em = emf.createEntityManager();
Query query = em.createQuery("SELECT c from org.openspaces.objects.Customer c WHERE c.address.country = 'United States'");
List<Customer> customers = (List<Customer>) query.getResultList();
em.close();
emf.close();
```

**JOIN support for one to many relationship (Owner --> List<Pet>)**


```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("gigaspaces");
EntityManager em = emf.createEntityManager();
Query query = em.createQuery("SELECT o FROM org.openspaces.objects.Owner o JOIN o.pets p WHERE p.name = :name");
query.setParameter("name", "Whiskey");
Owner owner = (Owner) query.getSingleResult();
em.close();
emf.close();
```

{{% info "Info"%}}
When specifying entity names in the XAP JPQL, the full class qualified name should be used as shown in the above examples.
{{% /info %}}

# Persisting Collection Properties

You can make a collection property persistent by using the `@ElementCollection` annotation. The following example shows an entity with a collection of Integers:


```java
@Entity
public class Card {
  // ...

  private List<Integer> numbers;

  @ElementCollection
  @SpaceIndex(path = "[*]") // the list values will be indexed.
  public List<Integer> getNumbers() {
    return this.numbers;
  }

  public void setNumbers(List<Integer> numbers) {
    this.numbers = numbers;
  }

  // ...
}
```

In order to query the Card entity using a specific Integer in the numbers collection we use JPQL's `MEMBER OF`:


```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("gigaspaces");
EntityManager em = emf.createEntityManager();
Query query = em.createQuery("SELECT c FROM org.openspaces.objects.Card c WHERE :number MEMBER OF c.numbers");
query.setParameter("number", "10");
Card card = (Card) query.getSingleResult();
em.close();
emf.close();
```

# Persisting Enum Properties

JPA allows persisting Enum properties using the `@Enumerated` annotation, as shown below:


```java
// A Vehicle entity which has an Enum property
@Entity
public class Vehicle {
  // Enum Declaration
  public enum VehicleType { CAR, TRUCK, BIKE };

  private Integer id;
  private String name;
  private VehicleType type;

  public Vehicle() {
  }

  @Id
  @SpaceId
  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  @Enumerated
  public VehicleType getType() {
    return this.type;
  }

  /* Additional Getters & Setters */

}
```

We used the `@Enumerated` annotation for persisting an Enum property.
Specifying a value for the `@Enumerated.value()` attribute has no effect because Enums are saved in XAP as is.

## Enums In JPQL

You can query according to an Enum property by setting an Enum parameter, or by using the Enum's value in the query string:


```java
EntityManager em = emf.createEntityManager();

// Query using an Enum parameter
Query query1 = em.createQuery("SELECT vehicle FROM com.gigaspaces.objects.Vehicle vehicle WHERE vehicle.type = :type");
query1.setParameter("type", VehicleType.CAR);
Vehicle result1 = (Vehicle) query1.getSingleResult();

// Query using an Enum in query's string
Query query2 = em.createQuery("SELECT vehicle FROM com.gigaspaces.objects.Vehicle vehicle WHERE vehicle.type = 'BIKE'");
Vehicle result2 = (Vehicle) query2.getSingleResult();
```

# Interoperability

The XAP JPA implementation is fully interoperable with the XAP native [POJO API](./pojo-overview.html).
For instance, you can persist a JPA entity and read it using the native POJO-driven Space API:


```java
@Entity
public class Author {
  private Integer id;
  private String name;
  private List<Book> books;

  public Author() {
  }

  @Id
  @SpaceId
  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  @OneToMany(cascade = CascadeType.ALL)
  @SpaceIndex(path = "[*].id")
  public List<Book> getBooks() {
    return this.books;
  }

  // Additional Getters & Setters...

}

@Entity
public class Book implements Serializable {
  private Integer id;
  private String name;

  public Book() {
  }

  @Id
  @SpaceId
  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  // Additional Getters & Setters...

}

GigaSpace gigaspace = ...
Book book1 = new Book(10, "Book Title 1");
Book book2 = new Book(20, "Book Title 2");
List<Book> books = new ArrayList<Book>();
books.add(book1);
books.add(book2);
Author author = new Author();
author.setId(1234);
author.setBooks(books);

// Persist using GigaSpaces JPA..
Properties properties = new Properties();
properties.put("ConnectionFactory", gigaspace.getSpace());
EntityManagerFactory emf = Persistence.createEntityManagerFactory("gigaspaces", properties);
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();
em.persist(author);
em.getTransaction().commit();
em.close();

// Read using Space API..
Author result = gigaspace.readById(Author.class, 1234);

// Or even SQLQuery..
SQLQuery<Author> query = new SQLQuery<Author>(Author.class, "id = 1234");
result = gigaspace.read(query);

// Or by a certain book..
query = new SQLQuery<Author>(Author.class, "books[*].id = 10");
result = gigaspace.read(query);
```

# Native Query Execution


The XAP JPA native query execution is a powerful feature used to execute the items discussed in the following subsections.

{{%refer%}}
- SQLQuery syntax-like queries ([SQLQuery](./query-sql.html)).
- XAP Tasks ([Task Execution over the Space](./task-execution-over-the-space.html)).
- XAP Dynamic Scripts ([Dynamic Language Tasks](./task-dynamic-language.html)).
{{%/refer%}}

## SQLQuery Execution

SQLQuery execution using JPA native query API is relatively simple, and done as follows:


```java
// SQLQuery execution
EntityManager em = emf.createEntityManagerFactory();
Query query = em.createNativeQuery("name = 'John Doe'", Author.class);
Author author = (Author) query.getSingleResult();

// SQLQuery execution with parameters
query = em.createNativeQuery("name = ?", Author.class);
query.setParameter(1, "John Doe");
author = (Author) query.getSingleResult();
```

{{%refer%}}
For more details about the SQLQuery syntax, refer to the [SQLQuery](./query-sql.html) page.
{{%/refer%}}

## Task Execution

Using XAP's JPA native query API, you can execute tasks over the Space as follows:


```java
// Task definition
public class MyTask implements Task<Integer> {

  @TaskGigaSpace
  private transient GigaSpace gigaSpace;
  private Object routing;

  public MyTask(Object routing) {
    this.routing = routing;
  }

  public Integer execute() throws Exception {
    return gigaSpace.count(new Author());
  }

  @SpaceRouting
  public Object getRouting() {
    return this.routing;
  }
}

// Task execution
Query query = em.createNativeQuery("execute ?");    // Special syntax for task execution
query.setParameter(1, new MyTask(1));               // We pass our task instance as a parameter to the query
Integer result = (Integer) query.getSingleResult(); // Task execution always returns a single result
```

{{% info "Info"%}}
Task execution using JPA's native query API is always synchronous.
{{% /info %}}

### Getting an EntityManagerFactory Instance in a Task

You can get an EntityManagerFactory instance (according to the bean definition in pu.xml) by implementing the ApplicationContextAware interface. For example:


```java
public class MyTask implements Task<Integer>, ApplicationContextAware {

  private transient EntityManagerFactory emf;

  public void setApplicationContext(ApplicationContext context) throws BeansException {
    // Get the entityManagerFactory bean
    emf = (EntityManagerFactory) context.getBean("entityManagerFactory");
  }

  public Integer execute() throws Exception {

    // Create an EntityManager..
    EntityManager em = emf.createEntityManager();

    // ...

    em.close();

    return 0;
  }

}
```

Another option (instead of using the ApplicationContextAware interface) is to annotate your task with the `@AutowireTask` annotation, and annotate the `EntityManagerFactory` property with a `@Resource` annotation.

{{%refer%}}
For more information about XAP tasks, refer to [Task Execution over the Space](./task-execution-over-the-space.html).
{{%/refer%}}

## Dynamic Script Execution

In addition to task execution, the XAP JPA native query execution also offers the ability to execute dynamic scripts such as Groovy, JavaScript & JRuby over the Space.
Dynamic script execution over the Space is based on task execution and remoting, so your Processing Unit must have a remoting scripting executor service:


```xml
<!-- The service exporter exposing the scripting service -->
<os-remoting:service-exporter id="serviceExporter">
     <os-remoting:service ref="scriptingExecutor"/>
</os-remoting:service-exporter>
```

The next step is using the exposed scripting service on the client side, using JPA's native query API:


```java
// Dynamic Script execution
Script script = new StaticScript("GroovyScript", "groovy", "println 'Dynamic Script Execution using JPA'; return 0");

Query query = em.createNativeQuery("execute ?");     // Special syntax for script execution (similar to task execution)
query.setParameter(1, script);                       // We pass our script as a parameter to the query
Integer result = (Integer) query.getSingleResult();  // Script execution always returns a single result
```

{{%refer%}}
For more information about dynamic script execution, refer to [Dynamic Language Tasks](./task-dynamic-language.html).
{{%/refer%}}

# XAP JPA Limitations

{{%refer%}}
For a list of unsupported JPA features and limitations,  refer to [XAP JPA Limitations](./jpa-limitations.html).
{{%/refer%}}
