---
type: post97
title:  Deployment Properties
categories: XAP97
parent: deploying-and-running-overview.html
weight: 400
canonical: auto
---

 {{% ssummary   %}} {{% /ssummary %}}



When a processing unit is deployed and provisioned, you can inject property values to it only known at deployment time, or in order to further configure the processing unit elements. Injected properties can be either properties that have been explicitly externalized from the [processing unit configuration file](./configuring-processing-unit-elements.html) or properties related to one the platform components (e.g. a space) that can be configured at deployment time. This mechanism is built on top of Spring's support for an externalized properties configuration called [PropertyPlaceholderConfigurer](http://static.springframework.org/spring/docs/2.5.x/reference/beans.html#beans-factory-placeholderconfigurer). This mechanism has been enhanced to provide a powerful yet simple property injection.

{{% info %}}
One of the core values of GigaSpaces XAP processing unit model is the fact that a processing unit need not be changed at all in the transition from the development environment (namely your IDE) to the production environment. This feature, along with others, is one of the enablers of this behavior.
{{%/info%}}

# Defining Property Place Holders in Your Processing Unit

Property injection to the processing unit's configuration is supported at the Spring application context scope, which means that it can be applied to all of the components configured in the [processing unit's configuration file](./configuring-processing-unit-elements.html) (whether GigaSpaces out of the box components like the space, space proxy or event containers, or user-defined beans).

Below you can find an example of an XML configuration which defines to property place holders, `spaceSchema` and `connectionTimeout`. In this example we also specify default values for them, which is always a good practice and does not force the deployer to specify values for these place holders.

{{% tip %}}
Note that for property place holders we use the `${placeholder name}` notation.
{{%/tip%}}

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<!-- The PropertyPlaceholderConfigurer must be present in order to define default value
     for properties.
-->
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="properties"><props>
        <prop key="spaceSchema">default</prop>
        <prop key="connectionTimeout">1000</prop>
    </props></property>
</bean>

<os-core:space id="space" url="/./space" schema="${spaceSchema}" />

<bean id="connBean" class="MyConnection">
    <property name="timeout" value="${connectionTimeout}" />
</bean>
```

{{% /tab %}}
{{%tab "  Plain "%}}


```xml

<!-- Define sensible defaults -->
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="properties"><props>
        <prop key="spaceSchema">default</prop>
        <prop key="connectionTimeout">1000</prop>
    </props></property>
</bean>

<bean id="space" class="org.openspaces.core.space.UrlSpaceFactoryBean">
    <property name="url" value="/./space" />
    <property name="schema" value="${spaceSchema}" />
</bean>

<bean id="connBean" class="MyConnection">
    <property name="timeout" value="${connectionTimeout}" />
</bean>
```

{{% /tab %}}
{{% /tabs %}}

The various [processing unit runtime modes](./deploying-and-running-the-processing-unit.html) all provide support for injecting property values instead of place holders. There are several ways to define the values for the property place holders, depending on how you choose to run/deploy your processing unit.

### Using a `.properties` File

If you would like your properties to be configured in a dedicated file this can be done in various ways.

- Including a `pu.properties` file in the processing unit. This file can be placed under the root of the processing unit or under the `META-INF/spring` directory (This is also described [here](./the-processing-unit-structure-and-configuration.html)). The values of this file will be loaded and injected automatically to the processing unit instances at deployment time.

- External `.properties` file. The file can have any name, as long as it's accessible to the deployment tool (UI, CLI, etc.) or specified when [running the processing unit within your IDE](./running-and-debugging-within-your-ide.html) or in [standalone mode](./running-in-standalone-mode.html).
When deploying through the [CLI]({{%currentadmurl%}}/command-line-interface.html) or [running within the IDE](./running-and-debugging-within-your-ide.html), you specify the location of the file using `-properties <location>` as a command line argument with the CLI or a program argument within the IDE.


```java
gs deploy -properties file://myConfigFolder/pu.properties data-processor.jar
```

By default, the location is a file-system-based location of a properties file (follows Spring [Resource Loader](http://static.springframework.org/spring/docs/2.5.x/reference/resources.html#resources-resourceloader) syntax).

The following is an example of a `.properties` file that can be used with the sample `pu.xml` configuration shown above. In this case, the values within the `.properties` file are injected to the processing unit instances (overriding values in `pu.properties` if it exists).

When deploying via the UI, the file can be loaded into the deployment wizard by clicking "Next" in the first screen of the wizard and then "Load Properties", locating the properties file on your local disk. This will load all the property place holders that are in the file.

- Direct property injection. This can also be done via one of the deployment tools (UI, CLI) or specified when [running the processing unit within your IDE](./running-and-debugging-within-your-ide.html).
When deploying through the [CLI]({{%currentadmurl%}}/command-line-interface.html) or [running within the IDE](./running-and-debugging-within-your-ide.html), you specify property values by using the following syntax:


```java
-properties embed://spaceSchema=persistent;connectionTimeout=15000
```

This can be specified as part of the command line arguments or as a program argument when running within your IDE.
When deploying via the UI, click "Next" in the first screen of the deployment wizard and then "+" to add properties. Any property you specify here will be injected to the appropriate property place holder (if such exists) and will override the `pu.properties` within the processing unit.

{{% info "Property Injection for SLA Definitions "%}}
From version 7.0 onwards, the processing unit's [SLA definitions](./configuring-the-processing-unit-sla.html) can be defined in a separate `sla.xml` file (unlike previous release in which they could only have been defined in the `pu.xml` file). As you may recall, the SLA definition are expressed via the `<os-sla:sla>` XML element in either the `pu.xml` of the `sla.xml` files.

You should note however that property injection, as described in this page, and any external jars imports, is only available for SLA definitions expressed in a separate `sla.xml` file, and will not be applied to the `<os-sla:sla>` element if it is part of the `pu.xml` file.

Also note that the parsing of the SLA element happens on the deploy tool side, so the properties should be available on the deploy tool side.
{{% /info %}}

# Using Deployment Properties to Override Space Schema and Cluster Schema

When a [Space](./the-space-configuration.html) is created, two major groups of configuration elements determine its runtime configuration: the space schema and the cluster schema. The cluster schema controls the space clustering topology (partitioned or replicated), whether the replication to its replicas is synchronous or asynchronous, and various other aspects that control its clustering behavior. The space schema on the other hand, controls other elements which are not related to the space clustering topology, such as the eviction strategy (LRU, ALL_IN_CACHE), whether or not its persistent, etc.

The basis for these two configuration groups are XML files located inside the GigaSpaces libraries. In order to override the values in these XML files, one can simply specify the [XPath](http://en.wikipedia.org/wiki/XPath) expression that corresponds to the element to be overridden.

These expression can also be included in all of the above mentioned property injection mechanisms (with the exception that you do not have to explicitly specify property placeholders for them).

Here's an example for a space configured within the processing unit, and a property injection overriding its schema name:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:space id="space" url="/./space" />
```

{{% /tab %}}
{{%tab "  Plain "%}}


```xml

<bean id="space" class="org.openspaces.core.space.UrlSpaceFactoryBean">
    <property name="url" value="/./space" />
</bean>
```

{{% /tab %}}
{{% /tabs %}}

When deploying the space you should use `-properties space embed://gs.space.url.arg.schema=persistent`. This instructs the runtime to override the configuration of the bean named "space" in your pu.xml file with the specified value. You may also configure the space directly inside your processing unit using direct property injection on the space bean.
