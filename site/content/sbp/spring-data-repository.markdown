---
type: post
title:  XAP Repository
categories: SBP
weight: 400
parent: spring-data.html
---


This part of the documentation explains how to configure and use Repositories with Spring Data. You can operate directly with the `GigaSpace` bean created in the previous section to perform read and write operations. It is easier to use Spring Data Repositories for the same purposes. This approach significantly reduces the amount of boilerplate code from your data-access layer as well as gives you more flexibility and cleaner code which is easy to read and support. The `GigaSpace` bean is still available with the `space()` method at the `XapRepository` interface.

{{%note%}}
Spring Data XAP supports all Spring Data Commons configuration features like exclude filters, standalone configuration, manual wiring, etc. For more details on how to apply them, please, refer to [Creating Repository Instances](http://docs.spring.io/spring-data/commons/docs/current/reference/html/#repositories.create-instances).
{{%/note%}}

Before using Spring Data XAP features you will need to create your repository interface extending `XapRepository` and tell the Spring Container to look for such classes.

{{%note%}}
Spring Data XAP does not support `ignoreCase` and `nullHandling` in query expressions and `Sort`.
{{%/note%}}

An example of such user-defined repository with no additional functionality is given below:


```java
public interface PersonRepository extends XapRepository<Person, String> {
}
```

{{%note%}}
Note that you define the type of data to be stored and the type of it's id.
{{%/note%}}

# Registering XAP repositories using XML-based metadata

While you can use Spring's traditional `<beans/>` XML namespace to register an instance of your repository implementing `XapRepository` with the container, the XML configuration can be quite verbose as it is general purpose. To simplify configuration, XAP Spring Data   provides a dedicated XML namespace.

To enable Spring searching for repositories, add the configuration in the XML-based metadata:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:xap-data="http://www.springframework.org/schema/data/xap"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="
         http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
         http://www.springframework.org/schema/data/xap http://www.springframework.org/schema/data/xap/spring-xap-1.0.xsd">

  <xap-data:repositories base-package="com.yourcompany.foo.bar"/>

  <!-- other configuration omitted -->

</beans>
```

{{%note%}}
The Spring Container will search for interfaces extending `XapRepository` in packages and it's subpackages defined by the `base-package` attribute.
{{%/note%}}

Repositories will look for the `GigaSpace` bean by type in the context, if this behaviour is not overridden. If you have several Space declarations, repositories will not be able to wire the Space automatically. To define the Space to be used by XAP Repositories, just add a `gigaspace` attribute with a proper bean id. An example can be found [below](#repositories-multi).

# Registering XAP repositories using Java-based metadata

To achieve the same configuration with Java-based bean metadata, simply add the `@EnableXapRepositories` annotation to the configuration class:

```java
@Configuration
@EnableXapRepositories("com.yourcompany.foo.bar")
public class ContextConfiguration {
    // bean definitions omitted
}
```

{{%note%}}
The `base-package` can be defined as a value of the `@EnableXapRepositories` annotation. Also, the `GigaSpace` bean will be automatically found in the context by type or can be explicitly wired with the `gigaspace` attribute. An example can be found [below](#repositories-multi).
{{%/note%}}

# Excluding custom interfaces from the search

If you need to have an interface that won't be treated as a Repository by Spring Container, you can mark it with the `@NoRepositoryBean` annotation:


```java
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends XapRepository<T, ID> {

    // you can define methods that apply to all other repositories

    T findByName(String name);

    ...
}
```

# Multi-space configuration

Sometimes it is required to have different groups of repositories to store and exchange data in different Spaces. The Configuration for such a case will have several Space declarations and several repository groups. For each group, the Space to use will be assigned using `gigaspace` attribute referring to the `GigaSpace` bean id. Usually, groups of repositories will be placed in subpackages - it makes the system structure clearer and eases the configuration as well.

Here is an example of a multi-space configuration in XML-based metadata:


```xml
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:xap-data="http://www.springframework.org/schema/data/xap"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
           http://www.springframework.org/schema/data/xap http://www.springframework.org/schema/data/xap/spring-xap-1.0.xsd
           http://www.openspaces.org/schema/core http://www.openspaces.org/schema/10.1/core/openspaces-core.xsd">

  <!-- Initializes repositories in .operational with operationalGSpace -->
  <xap-data:repositories base-package="com.yourcompany.foo.bar.repository.operational" gigaspace="operationalGSpace"/>
  <!-- Initializes repositories in .billing with billingGSpace -->
  <xap-data:repositories base-package="com.yourcompany.foo.bar.repository.billing" gigaspace="billingGSpace"/>

  <os-core:space-proxy id="billingSpace" name="billing"/>
  <os-core:giga-space id="billingGSpace" space="billingSpace"/>

  <os-core:embedded-space id="operationalSpace" name="operational"/>
  <os-core:giga-space id="operationalGSpace" space="operationalSpace"/>

  <!-- other configuration omitted -->

</beans>
```

With the Java-based configuration you will have to separate groups of repositories into different sub-contexts:


```java
@Configuration
@Import({OperationalRepositories.class, BillingRepositories.class})
public class ContextConfiguration {
    /* other beans declaration omitted */
}

@Configuration
@EnableXapRepositories(basePackages = "com.yourcompany.foo.bar.repository.operational", gigaspace = "operationalGSpace")
class OperationalRepositories {
    /**
     * @return embedded operational space configuration
     */
    @Bean
    public GigaSpace operationalGSpace() {
        return new GigaSpaceConfigurer(new UrlSpaceConfigurer("/./operational")).gigaSpace();
    }
}

@Configuration
@EnableXapRepositories(basePackages = "com.yourcompany.foo.bar.repository.billing", gigaspace = "billingGSpace")
class BillingRepositories {
    /**
     * @return proxy billing space configuration
     */
    @Bean
    public GigaSpace billingGSpace() {
        return new GigaSpaceConfigurer(new UrlSpaceConfigurer("jini://*/*/billing")).gigaSpace();
    }
}
```
