---
type: post110
title:  Configuration
categories: XAP110
weight: 200
parent: the-processing-unit-overview.html
---


{{% ssummary  %}} {{% /ssummary %}}



The processing unit's configuration is expressed using a number of configuration files, namely `pu.xml, sla.xml` and `pu.properties`, which are typically located under the `META-INF/spring` directory within the processing unit jar file.

Each file is used to configure separate aspects of the processing unit. The `pu.xml` file is used to configure the actual elements that the processing unit contains, e.g. space and space proxies, event handles and remote service. Generally speaking, it contains of the application specific components of the processing unit.

# The pu.xml file

This file is a [Spring framework](http://www.springframework.org) XML configuration file. It leverages the Spring framework [IoC container](http://static.springframework.org/spring/docs/2.5.x/reference/beans.html) and extends it by using the Spring [custom namespace mechanism](http://static.springframework.org/spring/docs/2.5.x/reference/extensible-xml.html).

{{% refer %}}
It is recommended (although not mandatory) that you familiarize yourself with the Spring XML configuration basics. A good place to start is [here](http://static.springframework.org/spring/docs/2.5.x/reference/beans.html)
{{% /refer %}}

# pu.xml Contents

The definitions in the `pu.xml` file are divided into 2 major categories:

- GigaSpaces specific components, such as [space](./the-space-configuration.html#proxy), [GigaSpace](./the-gigaspace-interface.html), [event containers](./messaging-support.html) or [remote service exporters](./space-based-remoting.html).

- User defined beans, which define instances of user classes to be used by the processing unit. For example, user defined event handlers to which the event containers delegate events as those are received.

{{% info "SLA (Service Level Agreement) definitions "%}}
the `pu.xml` may also contain [SLA definitions]({{%currentadmurl%}}/the-sla-overview.html). In previous releases, this was the recommended way to define your processing unit's SLA. As of version 7.0, the recommended way to do it is to use a separate sla.xml file, which separates this deployment and runtime aspect from the the processing unit's components. This enables better modularization and reuse of the processing unit.
{{% /info %}}

This section focuses on the overall structure of the `pu.xml` file. The various GigaSpaces components that can be included in the `pu.xml` (such as [event containers](./messaging-support.html), [remote services](./space-based-remoting.html) and others) are covered in detail in their respective sections in the programmer's guide.

# Multiple Configuration Options

In recent versions (2.0 and 2.5), the Spring framework has introduced two additional ways to configure your Spring application.

- Extensive support for [configuration through Java 5 annotations](http://static.springframework.org/spring/docs/2.5.x/reference/beans.html#beans-annotation-config) to simplify many of the configuration tasks a bring the configuration closer to the code

- [Extensible XML schema](http://static.springframework.org/spring/docs/2.5.x/reference/extensible-xml.html) through the use of custom namespaces and namespace handlers. This allows for products like GigaSpaces (or even Spring itself) to create their own XML elements for simplifying the configuration of their specific components instead of using the plain Spring XML syntax
Naturally, GigaSpaces XAP also leverages the extensibility of the above two mechanisms and enables users to configure the processing unit through GigaSpaces-specific XML elements and Java 5 annotations.

Throughout this guide, every configuration example will be provided in all of the supported forms (plain Spring, custom namespace element and annotations). See below for an example pu.xml configuration file.

# Sample pu.xml Configuration

Here's a sample `pu.xml` configuration file. Note the comments in the file which describe the various elements that appear in it.


```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--
    top level element of the Spring configuration. Note the multiple namespace definition for both
    GigaSpaces and Spring. You can simply copy and paste this portion of the pu.xml file
-->
<beans xmlns="http://www.springframework.org/schema/beans"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:context="http://www.springframework.org/schema/context"
   xmlns:os-core="http://www.openspaces.org/schema/core"
   xmlns:os-events="http://www.openspaces.org/schema/events"
   xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
   xmlns:os-sla="http://www.openspaces.org/schema/sla"
   xsi:schemaLocation="
   http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
   http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
   http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
   http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{%currentversion%}}/events/openspaces-events.xsd
   http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{%currentversion%}}/remoting/openspaces-remoting.xsd
   http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{%currentversion%}}/sla/openspaces-sla.xsd">

    <!-- Enables to configure Spring beans through annotations   -->
    <context:annotation-config />

    <!-- Enable @PostPrimary and others annotation support. -->
    <os-core:annotation-support />

    <!-- Enables using @Polling and @Notify annotations to creating polling and notify containers  -->
    <os-events:annotation-support />

    <!-- Enables using @RemotingService and other remoting related annotations   -->
    <os-remoting:annotation-support />

    <!--
        A bean representing a space. Here we configure an embedded space. Note
        that we do not specify here the cluster topology of the space. It is
        declared by the os-sla:sla element of this pu.xml file.
    -->
     <os-core:embedded-space id="space" name="mySpace"/>

    <!-- Defines a distributed transaction manager.-->
    <os-core:distributed-tx-manager id="transactionManager" />

    <!-- Define the GigaSpace instance that the application will use to access the space  -->
    <os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

</beans>
```

# Importing xml files into the pu.xml

To import one more more xml files into your pu.xml you can use the following:


```xml
<import resource="classpath*:/applicationContext-component.xml" />
<import resource="classpath*:/applicationContext-matching.xml"/>
<import resource="classpath*:/applicationContext-services.xml"/>
<import resource="classpath*:/applicationContext-jmx.xml" />
<import resource="classpath*:/applicationContext-containers.xml"/>
```
