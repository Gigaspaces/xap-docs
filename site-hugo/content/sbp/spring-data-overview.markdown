---
type: post
title:  XAP Support
categories: SBP
weight: 200
parent: spring-data.html
---


When developing an XAP application using Spring we need to configure a connection to the active Space inside the Spring IoC container. We will show you how a basic connection can be created using XML or Java based Spring configurations.

You can start an `Embedded Space` or set up a `Service Grid`. When using the [Embedded Space]({{%lateststartedurl%}}/xap-tutorial-part1.html) you don't need to start any additional processes in your environment.

To start the Data Grid use the following command:


{{%tabs%}}
{{%tab " Windows"%}}

```bash
gs.bat host run-agent --auto --gsc=2
gs.bat space deploy --partitions=1 --ha space
```
{{%/tab%}}
{{%tab " Linux"%}}

```bash
gs.sh host run-agent --auto --gsc=2
gs.sh space deploy --partitions=1 --ha space
```
{{%/tab%}}
{{%/tabs%}}

{{%refer%}}
Refer to [XAP Quick Start guide]({{%lateststartedurl%}}/xap-in-5-minutes.html) for more information.
{{%/refer%}}

In your project (assuming you build it with [Maven](http://maven.apache.org/)) add the following to the `pom.xml` dependencies section:


```xml
<dependencies>
    <!-- other dependency elements omitted -->
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-xap</artifactId>
        <version>1.0.0.RELEASE</version>
    </dependency>
</dependencies>
```

# Space Connection with XML metadata

To use the XAP Repository you need to provide a connection to the Space with an instance of `GigaSpace`. This can be configured with the Spring XML configuration:


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="
         http://www.openspaces.org/schema/core
         http://www.openspaces.org/schema/10.1/core/openspaces-core.xsd
         http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans-4.0.xsd">

  <!-- A bean representing space proxy (requires active data grid with the same name) -->
  <os-core:space-proxy id="space" name="space"/>

  <!-- GigaSpace interface implementation used for SpaceClient injection -->
  <os-core:giga-space id="gigaSpace" space="space"/>

</beans>
```

If you want to use an `Embedded Space`, use the following configuration:


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="
         http://www.openspaces.org/schema/core
         http://www.openspaces.org/schema/10.1/core/openspaces-core.xsd
         http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans-4.0.xsd">

  <!-- A bean representing an embedded space -->
  <os-core:embedded-space id="space" name="space"/>

  <!-- GigaSpace interface implementation used for SpaceClient injection -->
  <os-core:giga-space id="gigaSpace" space="space"/>

</beans>
```

# Space Connection with Java metadata

The same configuration can be achieved with Java-based bean metadata:


```java
@Configuration
public class ContextConfiguration {
    /**
     * Builds a space instance with settings that allow it to connect to the 'space'.
     */
    @Bean
    public GigaSpace space() {
        UrlSpaceConfigurer urlSpaceConfigurer = new UrlSpaceConfigurer("jini://*/*/space");
        return new GigaSpaceConfigurer(urlSpaceConfigurer).gigaSpace();
    }
}
```

Using an Embedded Space:


```java
@Configuration
public class ContextConfiguration {
    /**
     * Builds a space instance with settings that allow it start the embedded space with name 'space'.
     */
    @Bean
    public GigaSpace space() {
        UrlSpaceConfigurer urlSpaceConfigurer = new UrlSpaceConfigurer("/./space");
        return new GigaSpaceConfigurer(urlSpaceConfigurer).gigaSpace();
    }
}
```

# Other Space configurations

In some projects you might want to apply other configurations to the Space. There are several options available:

## Local cache

A `Local Cache` is a Client Side Cache that maintains a subset of the master Space's data based on the client application's recent activity. The local cache is created empty, and whenever the client application executes a query the local cache first tries to fulfill it from the cache, otherwise it executes it on the master Space and caches the result locally for future queries.

{{%refer%}}
To find out more about Local Cache and it's configuration, refer to [Local Cache Reference]({{%latestjavaurl%}}/local-cache.html).
{{%/refer%}}

## Local view

A `Local View` is a Client Side read only Cache that maintains a subset of the master space's data, allowing the client to read distributed data without performing any remote calls or data serialization. Data is streamed into the client local view based on predefined criteria (a collection of SQLQuery objects) specified by the client when the local view is created.

{{%refer%}}
To read more on Local View, refer to [Local View Reference]({{%latestjavaurl%}}/local-view.html).
{{%/refer%}}

## Space Persistence

Space Persistence is a configuration where Space data is persisted into permanent storage and retrieved from it.

{{%refer%}}
Read more on persisting Space data [here]({{%lateststartedurl%}}/xap-tutorial-part7.html).
{{%/refer%}}



## Space Security

XAP Security provides comprehensive support for securing your data and Space operations  or both. XAP provides a set of authorities granting privileged   to access data and for performing operations on the Space.

{{%refer%}}
To find out more about security configuration,  refer to [Security Guide]({{%latestsecurl%}}).
{{%/refer%}}

# Using native write and read operation

`GigaSpace` configured above can be used directly to perform interaction with the Space. To do so,  simply inject the `GigaSpace` bean into your Repository classes. Here is an example, we have a `Person Repository` that defines the basic operations:

{{%tabs%}}
{{%tab " Person"%}}

```java
@SpaceClass
public class Person {
    private Integer id;
    private String name;
    private Integer age;

    public Person() {
    }

    @SpaceId(autoGenerate = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // getters and setters for other fields are omitted

}
```
{{%/tab%}}
{{%tab " Repository"%}}


```java
@Repository
public class XapPersonRepository implements PersonRepository {

    // GigaSpace can be injected and used directly in Repository layer

    @Autowired
    private GigaSpace space;

    public void create(Person person) {
        space.write(person);
    }

    public List<Person> findById(String personId) {
        return space.readById(Person.class, personId);
    }
    ...
}
```
{{%/tab%}}
{{%/tabs%}}

{{%note%}}
The class is marked with `@SpaceClass` annotation - it allows Spring XAP to look for entities in your data model and automatically handle their structure. Also, the `getId()` method is marked with `@SpaceId(autogenerate = true)` annotation - it will tell the space to handle ids automatically.
{{%/note%}}




# Modeling your data

Spring Data XAP comes with the transparent support of XAP native features. Additional configurations can be applied to your POJOs to boost the performance and reduce memory usage. When building the data model using Spring Data XAP you might want to consider some of the following features:

##  Indexing

The most well-known data store function that allows to boost common queries performance is index support. XAP provides several options: basic, compound and unique indexes. All of these features can be applied by simply annotating POJO classes or their fields, e.g. with `@SpaceIndex` or `@CompoundSpaceIndex` annotations.

{{%refer%}}
Please, refer to [Indexing]({{%latestjavaurl%}}/indexing-overview.html) for more details and examples of POJO classes.
{{%/refer%}}

## Storage Types

You can define the form in which objects will be stored in Space either with annotations on each POJO in your model or with defining default Storage Type for the  Space. This is done to save up time on serialization/de-serialization, reduce memory usage or to define schema that will change in time. Three Storage Types are available for POJOs: `OBJECT`, `BINARY` and `COMPRESSED`.

{{%refer%}}
To read more on this feature, please, refer to [Storage Types]({{%latestjavaurl%}}/storage-types-controlling-serialization.html).
{{%/refer%}}

## Exclusion

You can mark some POJO properties with `@SpaceExclude` to disable writing their values to the Space. It will also affect Querydsl `Q...` classes generation from POJOs - marked fields won't be available for querying in `Querydsl` style.

##  Other Annotation-based Features

{{%refer%}}
For the full list of available annotations, please, refer to [Annotation based Metadata]({{%latestjavaurl%}}/pojo-annotation-overview.html).
{{%/refer%}}
