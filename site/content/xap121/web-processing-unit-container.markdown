---
type: post121
title:  Web Processing Unit Container
categories: XAP121
parent: web-application-overview.html
weight: 300
---


{{% ssummary  %}}{{% /ssummary %}}



OpenSpaces integration with the Service Grid allows you to deploy web applications (packaged as a WAR file) onto the Service Grid. The integration is built on top of the [Service Grid Processing Unit Container](./deploying-onto-the-service-grid.html).

The integration allows you to make use of the following Service Grid features:

- Dynamic allocation of several instances of a web application (probably fronted by a load balancer).
- Management of the instances running (if a GSC fails, the web application instances running on it will be instantiated on a different GSC).
- SLA monitor based dynamic allocation and de-allocation of web application instances.


The web application itself is a pure, JEE based, web application. The application can be the most generic web application, and automatically make use of the Service Grid features. The web application can define a Space (either embedded or remote) very easily (either using Spring or not).

The web container used behind the scenes is {{%exurl "Jetty" "http://www.eclipse.org/jetty/"%}} (with other containers coming in the near future). This page will list the common usage and configuration of web containers. Jetty specific configuration and usage can be found [here](./web-jetty-processing-unit-container.html).

# Deployment

The integration can either deploy a packaged WAR file or an exploded WAR file. In order to deploy packaged WAR file, it can be specified using one of the deployment mechanisms (UI/CLI/Programmatic, see more [here](./deploying-onto-the-service-grid.html#deployDirections)). When deploying a WAR file, it goes through the following steps until it gets to the GSC:

{{% info %}}
Note that the deploy client, the GSMs, and the GSCs can run on different machines.
{{%/info%}}

1. Point the deployment tool to the WAR file (UI/CLI/Programmatic).
1. The WAR file itself is uploaded to the chosen GSM (which will act as the primary GSM of the deployment).
1. The WAR is extracted under the GSM deploy directory with the provided processing unit name. The default directory location is `GSRoot/deploy/[processing unit name]`.
1. The GSM decides (based on the SLA) how many instances of the web application need to be deployed, and deploys them to the available GSCs.
1. Each GSC that is supposed to run an instance of the web application, downloads the web application into its own local file system. By default, it downloads it into `GSRoot/work/deployed-processing-units/[processing unit name]_[unique identifier]`.
1. The appropriate web container is configured to run the web application using the local file system location.

Deploying an exploded WAR is similar to deploying a packaged WAR. Here are the steps:

1. The exploded WAR file should be copied (manually) over to all the GSMs deploy directory. The default location is `GSRoot/deploy`.
1. A deploy command is issued with the processing unit name (the name of the directory under the deploy directory).
1. The GSM decides (based on the SLA) how many instances of the web application needs to be deployed, and deploys them to the available GSCs.
1. Each GSC that is supposed to run an instance of the web application, downloads the web application into its own local file system. By default, it downloads it into `GSRoot/work/deployed-processing-units/[processing unit name]_[unique identifier]`.
1. The appropriate web container is configured to run the web application using the local file system location.

{{% info %}}
The directory where the web applications are extracted (up to the `work` directory) on the GSC side can be controlled using the `com.gs.work` system property.

The deploy directory location (up to the `deploy` directory) used on the GSM side can be controlled using the `com.gs.deploy` system property.
{{%/info%}}

# Web Application Structure

A Web Application deployed into the Service Grid is, at the end of the day, just another type of a processing unit. This means that it inherits all the options that a processing unit has, among which is the ability to define an optional `META-INF/spring/pu.xml` configuration file as any other processing unit. Note however that class definitions and libraries on which the web application depends are placed in their standard JEE web application location (i.e. `WEB-INF/classes` and `WEB-INF/lib` respectively).

# Class Loaders

Here is the structure of the class loaders when several web applications are deployed on the Service Grid:


```java
              Bootstrap (Java)
                  |
               System (Java)
                  |
               Common (Service Grid)
                  |
            JEE Container
             /        \
        WebApp1     WebApp2
```

The following table shows which user controlled locations end up in which class loader, and the important JAR files that exist within each one:


|Class Loader|User Locations|Built in Jar Files|
|:-----------|:-------------|:-----------------|
|Common|\[GSRoot\]/lib/platform/ext/*.jar|xap-datagrid.jar|
|JEE Container|JEE container specific jars|\[GSRoot\]/lib/optional/jetty/*.jar|
|Webapp|\[PU\]/WEB-INF/classes, \[PU\]/WEB-INF/lib/*.jar|xap-openspaces.jar, spring/*.jar|

The idea behind the class loaders is to create a completely self sufficient web application. All relevant jar files or classes should exists within the web application (as if running it standalone) and then deploying it into the Service Grid will be a seamless experience.

A special case happen with `xap-datagrid.jar` which is automatically removed from `WEB-INF/lib` if it exists there since it has already been defined in the common class loader.

In terms of class loader delegation model, the web application class loader uses a parent last delegation mode. This means that the web application will first try and load classes from its own class loader, and only if they are not found, will delegate up to the parent class loader. This is the recommended way to work with this class loader model.

{{% note %}}
Where has the Service Class Loader gone? The Service Class Loader is still used in order to load the JEE container, but it is hidden from the user. The web application class loader is created with its parent class loader being the JEE container specific class loader and not the Service Class loader. Users should not worry in this case about the Service Class Loader and how it is used, as the above class loader model provides exactly the same semantics as a plain web container class loader model.
{{% /note %}}

{{% note %}}
The JEE Class Loader is created lazily when the first web application deployed into the GSC. This avoids the overhead involved when creating none-web processing units. There can also be several JEE class loaders per web container type, allowing to support both jetty and tomcat (for example) in the future quite easily.
{{% /note %}}


### Sharing Libraries Between Multiple Processing Units

{{%refer%}}
In some cases, multiple Processing Units use the same JAR files.In such cases it makes sense to place these JAR files in a central location accessible by all the Processing Units rather than packaging them individually with each of the Processing Units. [Read more](./the-processing-unit-structure-and-configuration.html#sharing-libraries-between-multiple-processing-units)
{{%/refer%}}

# Bootstrap Context Listener

When deploying a web application onto GigaSpaces Service Grid the `web.xml` of the web application will be automatically changed to include a `BootstrapWebApplicationContextListener`. The Bootstrap Context Listener provides the following services:

- It will automatically put the `ClusterInfo` and `BeanLevelProperties` that the system was deployed with into the web application `ServletContext`. The `ClusterInfo` class will be put under a context attribute `clusterInfo`, and the `BeanLevelProperties` will be put under a context attribute named `beanLevelProperties`.

- If there is a `META-INF/spring/pu.xml`, it will load it using Spring (that is why the spring and openspaces jars are automatically added to the web application class loader). The `ApplicationContext` created is put in the Servlet Context under an attribute named `applicationContext`. All the beans defined within the `META-INF/spring/pu.xml` which can be instantiated (i.e. are not abstract or require additional arguments to be created) will be bound to the `ServletContext` as well. Each bean name will be the `ServletContext` attribute name, and each bean will be the attribute value.

- If there is a `org.springframework.web.context.ContextLoaderListener` it will automatically be replaced with `ProcessingUnitContextLoaderListener`. The `ProcessingUnitContextLoaderListener` is exactly the same as the Spring one, except for the fact that it uses the (optional) `ApplicationContext` loaded from the `META-INF/spring/pu.xml` as the parent of the created Application Context. It will also add the `ClusterInfo` and `BeanLevelProperties` post processors to the created `ApplicationContext` so they can be injected to any bean requiring it (such as the embedded Space bean).

# Using a Space

There are several ways that a Space (and other components) can be used, and configured within a web application. Some common scenarios are listed below.

## Pure Remote Space

A typical usage pattern is connecting remotely to a Space. Here is an example (either using Spring within the web application Spring context file, or using pure Java code):

{{%tabs%}}
{{%tab "  Spring Namespace "%}}


```xml

<os-core:space-proxy id="space" space-name="mySpace"/>
<os-core:giga-space id="gigaSpace" space="space"/>
```

{{% /tab %}}
{{%tab "  Spring Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.SpaceProxyFactoryBean">
    <property name="name" value="space" />
</bean>
<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java

EmbeddedSpaceConfigurer configurer = new EmbeddedSpaceConfigurer("space");

GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).gigaSpace();

// ...

// shutting down / closing the Space
spaceConfigurer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

## Using pu.xml

A web application is still just a processing unit. This means that a **META-INF/spring/pu.xml** can be added, which can be used to define a [Space](./the-space-configuration.html). Accessing the beans is relatively simple as they are automatically added to the web application context and can be accessed from there. The key they are stored under is the bean id that each bean corresponds to.

Here is an example that starts an embedded Space as part of the web application within the `pu.xml`. The following is the content of the `pu.xml`

{{%tabs%}}
{{%tab "  Spring Namespace "%}}


```xml
<os-core:embedded-space id="space" space-name="mySpace"/>
<os-core:giga-space id="clusteredGigaSpace" space="space" clustered="true"/>
```

{{% /tab %}}
{{%tab "  Spring Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>
<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>
<bean id="clusteredGigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
	<property name="clustered" ref="true" />
</bean>
```

{{% /tab %}}
{{% /tabs %}}

Here is an example of a simple JSP that uses it:


```java
GigaSpace gigaSpace = (GigaSpace) getServletContext().getAttribute("clusteredGigaSpace");
```

# Embedded Space

The previous section described several options of how to start an embedded Space within the web application. The recommended way to work with embedded Space, is to work with its clustered proxy (the `clustered` flag in GigaSpace set to `true`) for interactions that originate from a web request. This is mainly since the load balancer does not know about routing specific classes to each cluster member.

Note, event driven operations should still work with non clustered embedded Space (usually). For example, a web request that results in writing an Order (using the clustered proxy) to the Space, and a polling container that picks it up and processes it asynchronously. The polling container should work with the non clustered, collocated, proxy of the cluster member space.

# Load Balancer

When deploying a highly available web site, usually a load balancer is used to load balance requests between at least two instances of a web container that actually runs the web application. When using GigaSpaces in order to deploy a web application, running more than one instance of a web application becomes a snap, as well as the manageability and virtualized nature of running web applications.

In order to create a single point of view, in terms of clients connecting to a server, a load balancer is usually used. While there are many different types of load balancers (both hardware and software), solving the load balancing problem is not new (i.e. it is not something that is introduced because the web application is deployed on GigaSpaces). Examples of how to configure load balancers can be found in specific web container sections.

GigaSpaces also comes with a built in integration with Apache httpd load balancer as described in the [Apache Load Balancer Agent](./apache-load-balancer-agent.html) section.
