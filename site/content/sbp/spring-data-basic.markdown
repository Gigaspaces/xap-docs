---
type: postsbp
title: Basic Usage
categories: SBP
weight: 500
parent: spring-data.html
---


{{%ssummary%}}{{%/ssummary%}}



This section describes how to define query methods. The basic concept: user defines methods using a specific query syntax as method name, the XAP repository proxy derives these methods into XAP queries. A full explanation of this mechanism can be found at [Spring Data Reference](http://docs.spring.io/spring-data/data-commons/docs/1.9.1.RELEASE/reference/html/#repositories.query-methods). In this document only basic usage will be explained.

# Query Methods

Here is an example of a Repository declaration with different  methods:


```java
public interface PersonRepository extends XapRepository<Person, String> {

    // you can query objects with exact field match
    List<Person> findByName(String name);

    List<Person> findByAge(Integer age);

    // you can use ranged of search for number fields
    List<Person> findByAgeBetween(Integer minAge, Integer maxAge);

    // you can use boolean expressions to define complex conditions
    List<Person> findByNameAndAge(String name, Integer age);

    List<Person> findByNameOrAge(String name, Integer age);

}
```

As you can see, different keywords can be used and combined to create desired conditions. A full list of supported keywords can be found in [Appendix A](./spring-data-appendix.html#appendix-a).

The process of deriving query methods into XAP Queries depends a lot on the query lookup strategy chosen for the repository. XAP Spring Data provides the support for all [common strategies](http://docs.spring.io/spring-data/data-commons/docs/1.9.1.RELEASE/reference/html/#repositories.query-methods.query-lookup-strategies).

The default strategy enables both deriving queries from method names and overriding them with custom defined queries. There are several ways to specify custom queries for a method.

#### The `@Query` annotation on the method:


```java
public interface PersonRepository extends XapRepository<Person, String> {

    @Query("name = ? order by name asc")
    List<Person> findByNameOrdered(String name);

}
```

The syntax used for `@Query` is similar to SQL queries.

{{%refer%}}
Refer to [SQLQuery]({{%latestjavaurl%}}/query-sql.html) for the full list of features.
{{%/refer%}}

####  Import named queries from an external resource.

Let's say we have `named-queries.properties` file in the classpath with next content:


```java
Person.findByNameOrdered=name = ? order by name asc
```

The query strings defined in the file will be applied to methods with same names in `PersonRepository` if we target `named-queries.properties` in the configuration. In XML-based configuration the `named-queries-location` attribute can be used:


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:xap-data="http://www.springframework.org/schema/data/xap"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="
         http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
         http://www.springframework.org/schema/data/xap http://www.springframework.org/schema/data/xap/spring-xap-1.0.xsd">

  <xap-data:repositories base-package="com.yourcompany.foo.bar"
                         named-queries-location="classpath:named-queries.properties"/>

  <!-- other configuration omitted -->

</beans>
```

Similarly, annotation field `namedQueriesLocation` can be used in Java-based configuration:


```java
@Configuration
@EnableXapRepositories(value = "com.yourcompany.foo.bar", namedQueriesLocation = "classpath:named-queries.properties")
public class ContextConfiguration {
    // bean definitions omitted
}
```


# Custom Methods

Custom methods can be added to repository interfaces. Spring Data allows you to provide custom repository code and still utilize basic CRUD features and query method functionality. To extend your repository, you first define a separate interface with custom methods:


```java
public interface PersonRepositoryCustom {

    String customMethod();
}
```

 Then you add an implementation for the defined interface:

```java
 public class PersonRepositoryCustomImpl implements PersonRepositoryCustom {

     public String customMethod() {
         // your custom implementation
     }

 }
```

{{%note%}}
Spring Data recognizes an `Impl` suffix by default to look for custom methods implementations.
{{%/note%}}

 The implementation itself does not depend on Spring Data, so you can inject other beans or property values into it using standard dependency injection. E.g. you could inject `GigaSpaces` and use it directly in your custom methods.

 The third step would be to apply interface with custom methods to your repository declaration:


```java
 public interface PersonRepository extends XapRepository<Person, String>, PersonRepositoryCustom {

     // query methods declarations are ommited
 }
```

 This will combine basic CRUD methods and your custom functionality and make it available to clients.

How does it really work? Spring Data looks for implementations of custom methods among all classes located under the `base-package` attribute in XML or `basePackages` in Java configuration. It searches for `<custom interface name><suffix>` classes, where `suffix` is `Impl` by default. If your project conventions tell you to use another suffix for the implementations, you can specify it with `repository-impl-postfix` attribute in XML configuration:


```xml
 <xap-data:repositories
         base-package="com.yourcompany.foo.bar"
         repository-impl-postfix="FooBar"/>
```

 Or with `repositoryImplementationPostfix` in Java configuration:


```java
 @Configuration
 @EnableXapRepositories(value = "com.yourcompany.foo.bar", repositoryImplementationPostfix = "FooBar")
 public class ContextConfiguration {
     // bean definitions omitted
 }
```

Another option would be to manually put the implementation into the context and use a proper name for it. In XML configuration it would look like this:


```xml
<xap-data:repositories base-package="com.yourcompany.foo.bar"/>

<bean id="personRepositoryImpl" class="...">
<!-- further configuration -->
</bean>
```

 And similarly in Java-based configuration:


```java
@Configuration
@EnableXapRepositories("com.yourcompany.foo.bar")
public class ContextConfiguration {

    @Bean
    public AnyClassHere personRepositoryImpl() {
        // further configuration
    }

    // other bean definitions omitted
}
```
