---
type: post
title:  Advanced Usage
categories: SBP
weight: 600
parent: spring-data.html
---


{{%ssummary%}}{{%/ssummary%}}

This section describes some of the more advanced features available through XAP Spring Data.

# Projection API

The Spring Data XAP supports [XAP Projection]({{%latestjavaurl%}}/query-partial-results.html) which allows to read only certain properties for the objects (delta read). This approach reduces network overhead,   memory consumption    and CPU overhead due to decreased serialization time.

The `XapRepository` interface provides you with basic `find` methods extended with the `Projection` argument. The example demonstrates how the `findOne` method can be used to select only `name` field from `Person`:


```java
@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository repository;

    public List<String> getAllNames() {
        Iterable<Person> personList = repository.findAll(Projection.projections("name"));
        // result processing ommited
    }

}
```

{{%note%}}
If you are using [Querydsl support](#querydsl), you can apply projection using `QueryDslProjection`. This approach will let you avoid run-time errors when the POJO field is renamed and projection fields are not since they are just strings.
{{%/note%}}

You can also supply your query methods with `Projection`, just add an additional argument to the method declaration:


```java
public interface PersonRepository extends XapRepository<Person, String> {

    List<Person> findByName(String name, Projection projection);

}
```

{{%refer%}}
To read more on projection refer to [Projection]({{%latestjavaurl%}}/query-partial-results.html) reference.
{{%/refer%}}

{{%anchor querydsl%}}

# Querydsl Support

The `Querydsl` framework let's you write type-safe queries in Java instead of using  query strings. It gives you several advantages: code completion in your IDE, domain types and properties can be accessed in a type-safe manner which reduces the probability of query syntax errors during run-time. If you want to read more about `Querydsl`, please, proceed to [Querydsl website](http://www.querydsl.com/).

Several steps are needed to start using XAP Repositories `Querydsl` support. First, use the repository as a `XapQueryDslPredicateExecutor` along with `XapRepository`:


```java
public interface PersonRepository extends XapRepository<Person, String>, XapQueryDslPredicateExecutor<Person> {
}
```

{{%note%}}
Note that you define the type of data to be accessed with Querydsl.
{{%/note%}}

Then, add the source processor to your maven build (`pom.xml`) using Maven Annotation Processing Tool plugin:


```xml
<project>
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>com.mysema.maven</groupId>
        <artifactId>apt-maven-plugin</artifactId>
        <version>1.1.3</version>
        <executions>
          <execution>
            <goals>
              <goal>process</goal>
            </goals>
            <configuration>
              <outputDirectory>target/generated-sources/java</outputDirectory>
              <processor>org.springframework.data.xap.querydsl.XapQueryDslAnnotationProcessor</processor>
            </configuration>
          </execution>
        </executions>
      </plugin>
      ...
    </plugins>
  </build>
</project>
```

This configuration will call `XapQueryDslAnnotationProcessor` before compiling your project sources. It will look for POJOs marked with `@SpaceClass` annotation and generate `Q...` classes for them that allow you to build up Querydsl `Predicate`s. Before using such classes, you have to call this processor with `process-sources` maven goal, or just call `install` if you are already using it:


```bash
mvn clean process-sources
mvn clean install
```

Now you can query your repository using Querydsl `Predicate`s:


```java
@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository repository;

    public Iterable<Person> getByAge(Integer minAge, Integer maxAge) {
        return repository.findAll(
                QPerson.person.name.isNotNull().and(QPerson.person.age.between(minAge, maxAge))
        );
    }

}
```

A full list of supported `Predicate` methods can be found in [Appendix B](./spring-data-appendix.html#appendix-b).


# Change API

The Spring Data XAP supports [XAP Change API]({{%latestjavaurl%}}/change-api.html) allowing to update existing objects in Space by specifying only the required change instead of passing the entire updated object. It reduces network traffic between the client and the Space. It also can prevent the need of reading the existing object prior to the change operation because the change operation can specify how to change the existing property without knowing its current value.

There are two possible ways you can use Change API within the Xap Repositories. The first option is to call the native Change API by accessing `space()` in `XapRepository`.
For that, the `GigaSpace.change` methods along with `ChangeSet` class can be used.

{{%refer%}}
Full explanation and code examples can be found at [Change API]({{%latestjavaurl%}}/change-api.html).
{{%/refer%}}

The second option would be to use `XapQueryDslPredicateExecutor.change` method built in `Querydsl` style. It accepts `QChangeSet` argument that is literally a `ChangeSet` with `Querydsl` syntax:


```java
@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository repository;

    public void increaseAgeByName(String name) {
        repository.change(
                QPerson.person.name.eq(name),
                QChangeSet.changeSet().increment(QPerson.person.age, 1)
        );
    }

}
```

{{%refer%}}
To start using Querydsl Change API syntax, refer to [Querydsl Support](#querydsl)
{{%/refer%}}

The full list of supported change methods can be found in [Appendix C](./spring-data-appendix.html#appendix-c).

# Take Operations

The Spring Data XAP supports take operations that are the same as querying the space, but returned objects are deleted from the storage. This approach removes the need to perform additional operations when you implement a pattern where consumers or workers are receiving tasks or messages.

Basic take operation can be performed by object ids with `take(...)` methods in `XapRepository` interface. More advanced querying is available in Querydsl style within `XapQueryDslPredicateExecutor` interface. Those accept `Predicate` to retrieve one or multiple objects that match the query:



```java
@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository repository;

    public Person takeByName(String name) {
        return repository.takeOne(QPerson.person.name.eq(name));
    }

}
```


#  Lease Time

Spring XAP Data comes with a support of defining lease time for new objects in the repository. The basic idea behind it is limiting the time an object lives in Space. To use this feature, you can specify the lease time (in any time units) when saving with `save(...)` methods. These overloaded methods will return a special `LeaseContext` object that allows you to track, renew and cancel the lease.

The essential idea behind a lease is fairly simple.
* When creating a resource, the requestor creates the resource with a limited life span.
* The grantor of the resource will then give access for some period of time that is no longer than that requested.
* The period of time that is actually granted is returned to the requestor as part of the Lease object.
* A holder of a lease can request that a Lease be renewed, or cancel the Lease at any time.
* Successfully renewing a lease extends the time period during which the lease is in effect.
* Cancelling the lease drops the lease immediately.

{{%refer%}}
To read more about this feature refer to [Lease Time]({{%latestjavaurl%}}/leases-automatic-expiration.html).
{{%/refer%}}


# Transactions

Spring Data XAP comes with a support of declarative Spring transactions based on `OpenSpaces` transaction managers. In order to apply transactional behaviour, the transaction manager must be provided as a reference when constructing the `GigaSpace` bean. For example (using the distributed transaction manager):


```xml
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:xap-data="http://www.springframework.org/schema/data/xap"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
        http://www.springframework.org/schema/data/xap http://www.springframework.org/schema/data/xap/spring-xap-1.0.xsd
        http://www.openspaces.org/schema/core http://www.openspaces.org/schema/10.1/core/openspaces-core.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">

  <xap-data:repositories base-package="com.yourcompany.foo.bar"/>

  <!-- Enables the detection on @Transactional annotations -->
  <tx:annotation-driven transaction-manager="transactionManager"/>

  <!-- Space declaration, nothing transaction-special here -->
  <os-core:space-proxy id="space" name="space"/>

  <!-- GigaSpace bean with provided transaction manager -->
  <os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

  <!-- OpenSpaces distributed transaction manager -->
  <os-core:distributed-tx-manager id="transactionManager"/>

</beans>
```

Now your service layer methods can be marked as `@Transactional`:


```java
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Transactional
    public void transactionalMethod(Person person) {
        ...
    }

}
```

{{%refer%}}
To read more about transaction support  refer to [Transactions Reference]({{%latestjavaurl%}}/transaction-overview.html).
{{%/refer%}}


#  Document Storage Support

The [XAP Document API]({{%latestjavaurl%}}/document-api.html) exposes the Space as Document Store. A document, which is represented by the class `SpaceDocument`, is a collection of key-value pairs, where the keys are strings and the values are primitives, `String`, `Date`, other documents, or collections thereof. Most importantly, the Space is aware of the internal structure of a document, and thus can index document properties at any nesting level and expose rich query semantics for retrieving documents.

While using Spring Data XAP you can declare one or more of your repositories to be a Document Repository. To do so, first, you have to add a schema definition of the document type into the Space configuration in context:


```xml
<os-core:embedded-space id="space" name="space">

  <os-core:space-type type-name="Person">
    <os-core:id property="id"/>
    <os-core:routing property="age"/>
    <os-core:basic-index path="name"/>
    <os-core:extended-index path="birthday"/>
  </os-core:space-type>

  <!-- other document types declarations -->

</os-core:embedded-space>
```

Then, extend `XapDocumentRepository` interface (instead of usual `XapRepository`) and annotate it with `@SpaceDocumentName` to wire it to document descriptor declared above:


```java
@SpaceDocumentName("Person")
public interface PersonDocumentRepository extends XapDocumentRepository<SpaceDocument, String> {
}
```

{{%note%}}
If you don't mark your Document Repository with `@SpaceDocumentName` annotation, context configuration will fail.
{{%/note%}}

Now `PersonDocumentRepository` will have basic CRUD operations available for `SpaceDocument` entities. To read more on available document storage features, refer to [Document API]({{%latestjavaurl%}}/document-api.html).

While documents allow using a dynamic schema, they force us to give up Java's type-safety for working with type less key-value pairs. Spring Data XAP supports extending the `SpaceDocument` class to provide a type-safe wrapper for documents which is much easier to code with, while maintaining the dynamic schema. As an example, let's declare a `PersonDocument` wrapper:


```java
public class PersonDocument extends SpaceDocument {
    public static final String TYPE_NAME = "Person";
    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_AGE = "age";
    // other properties omitted

    public PersonDocument() {
        super(TYPE_NAME);
    }

    public String getId() {
        return super.getProperty(PROPERTY_ID);
    }

    public PersonDocument setId(String id) {
        super.setProperty(PROPERTY_ID, id);
        return this;
    }

    public Integer getAge() {
        return super.getProperty(PROPERTY_AGE);
    }

    public PersonDocument setAge(Integer age) {
        super.setProperty(PROPERTY_AGE, age);
        return this;
    }

    // other properties accessors are omitted
}
```

{{%note%}}
Note that wrapper classes must have a parameter less constructor
{{%/note%}}

To work with objects of a `PersonDocument` class instead of `SpaceDocument`, Space configuration must contain the declaration of the wrapper class:



```xml
<os-core:embedded-space id="space" name="space">

  <os-core:space-type type-name="Person">
    <os-core:id property="id"/>
    <os-core:routing property="age"/>
    <os-core:basic-index path="name"/>
    <os-core:extended-index path="birthday"/>
    <os-core:document-class>com.yourcompany.foo.bar.PersonDocument</os-core:document-class>
  </os-core:space-type>

  <!-- other document types declarations -->

</os-core:embedded-space>
```

Now we can declare our Document Repository with the next syntax:


```java
@SpaceDocumentName(PersonDocument.TYPE_NAME)
public interface PersonDocumentRepository extends XapDocumentRepository<PersonDocument, String> {
}
```

{{%note%}}
Note that domain class of `PersonDocumentRepository` is now set to `PersonDocument` instead of `SpaceDocument`. Also, type name for `PersonDocument` is reused in `@SpaceDocumentName` annotation for the repository.
{{%/note%}}

{{%refer%}}
For more information about the  `SpaceDocument` refer to [Extended Document]({{%latestjavaurl%}}/document-extending.html).
{{%/refer%}}

You can supply your Document Repository with query methods. But be aware that due to dynamic nature of `SpaceDocument` there is no way for Spring Data to automatically derive query method names into queries. The only possibility to declare a method is to use `@Query` annotation or load queries from external resources.  Here is an example of Document Repository supplied with search and sorting methods:


```java
@SpaceDocumentName(PersonDocument.TYPE_NAME)
public interface PersonDocumentRepository extends XapDocumentRepository<PersonDocument, String> {

    // you can define simple queries
    @Query("name = ?")
    List<PersonDocument> findByName(String name);

    // you can build complex queries
    @Query("name = ? and age = ?")
    List<PersonDocument> findByNameAndAge(String name, Integer age);

    // you can query embedded properties
    @Query("spouse.name = ?")
    List<PersonDocument> findBySpouseName(String name);

    // you can query any properties, even if they are not present in you wrapper
    @Query("customField = ?")
    List<PersonDocument> findByCustomField(String value);

    // you can perform sorting using SQLQuery syntax
    @Query("age = ? order by id asc")
    List<PersonDocument> findByAgeSortedById(Integer age);

}
```

{{%note%}}
You don't have to declare document properties to use them in queries, which allows dynamically adding and removing the properties.
{{%/note%}}

Document Repositories do not support `Querydsl` syntax due to dynamic nature of `SpaceDocument` properties.

