---
type: post
title:  Application Layer
categories: PRODUCT_OVERVIEW
weight: 500
parent: none
menu: product
---

{{%  ssummary %}}{{%  /ssummary %}}
Applications deployed on GigaSpaces XAP are very much like JEE distributed server-side applications.

Each application is composed of one or more scalable modules, and contains one or more business logic code components, domain model objects (entries), in-memory data storage, messaging, and event processing code.

A module in SBA is called a Processing Unit. This is the smallest unit for cross-cluster scaling. This unit is used for design, development, testing, packaging and deployment. It is very similar in nature to a JEE WAR file, and in fact shares many structural similarities with it.

# Processing Unit

A [Processing Unit]({{% latestjavaurl%}}/the-processing-unit-overview.html) is the unit of scale. It is an application component, provided by the application developer, which is deployed to be run on several GSC instances.

The Service Grid is responsible for taking a single copy of a Processing Unit, and creating several instances on multiple GSCs.

In technical details, a Processing Unit is a collection of classes and a configuration file, packaged as a single unit of scale. Classes are usually Services and Domain objects, which are part of the application. The configuration file is used to define the relationship between the Processing Unit components.

**There are several types of Processing Units**:

- [**Java** Processing Unit]({{% latestjavaurl%}}/the-processing-unit-overview.html) - has code components in Java, and may contain a space component.
- [**.NET** Processing Unit]({{% latestneturl%}}/the-processing-unit-overview.html) -  has its code components in .NET, and may contain a space component.
- [**Web** Processing Unit]({{% latestjavaurl%}}/web-application-support.html) - contains web applications. This Processing Unit can be packaged as a standard JEE WAR (Web Archive) file, and may contain a space component.
- [**EDG** Processing Unit]({{% latestjavaurl%}}/the-processing-unit-structure-and-configuration.html#data-only-pus) - contains space component(s) only.

A Processing Unit package structure is defined, based on the technology:

- A [**Java** Processing Unit]({{% latestjavaurl%}}/the-processing-unit-structure-and-configuration.html) is packaged usually as a JAR file, modeled after the Spring DI structure. The package can also be within a file structure based on the same structure.
- A **.NET** Processing Unit is packaged as a directory structure. For more information, please refer to the [XAP.NET Programmer's Guide]({{% latestneturl%}}/the-processing-unit-overview.html).
- A [**Web** Processing Unit]({{% latestjavaurl%}}/web-application-support.html#deployment) is packaged as a standard JEE WAR file.

# Data-Only Processing Unit (EDG)

In some cases there is a need to deploy an In Memory Data Grid (IMDG) only. For this reason there is a special Processing Unit called EDG - Enterprise Data Grid which can be deployed into the Service Grid. The EDG Processing Unit provides IMDG capabilities, and is accessed using the GigaSpace APIs.

An EDG Processing Unit is usually used in caching scenarios, when there is a need to provide the front-end of an information system or a database.

The value of an EDG Processing Unit is that its SLA is still maintained by the Service Grid.

# Collocation of Business Logic and Data

One of the key values of SBA, as explained above, is the ability to collocate data, business logic and messaging. A typical Processing Unit will contain, within the boundaries of the same Processing Unit, a space instance and business logic services.

The services will usually operate on data that is stored within the same space partition, providing memory access within the same process address space. This mode of interaction allows the minimal latency possible, as data is accessed by reference, as opposed to serialization required for out-of-process communication. Further more operation performance is maximal, as context switches are minimal.

Scalability is reached by deploying multiple instances of the same Processing Unit. Spaces of various Processing Units form a space cluster. However, each service within a particular Processing Unit accesses information on its own space partition.

# Spring Integration

All GigaSpaces components can be wired and configured with the application using relevant Spring Beans provided.
GigaSpaces Processing Unit is a pure Spring application context configuration file. Any Spring based application can be deployed as is into GigaSpaces container.

The GigaSpaces Spring Integration support:

- Spring Automatic Transaction Demarcation
- Spring Data
- Spring JMS
- Spring JPA
- Spring Hibernate
- Spring Remoting
- String Batch
- Spring Security
- Mule

# Migrating your Application to GigaSpaces XAP

It's important to realize that ease of migration was a core focus for GigaSpaces XAP. As such, there are many ways to migrate your application to GigaSpaces XAP, with gains in scalability and ease of maintenance all along the way.

1. Migrating a traditional Java EE application to GigaSpaces XAP is often started by replacing the asynchronous invocation layer with GigaSpaces XAP - i.e., removing your normal JMS container, with its points of failure based on database backends and TCP/IP connections, and using GigaSpaces XAP instead. This is a simple configuration change!
1. The next step in migrating a traditional Java EE application involves using the in-memory data grid to lower requirements on the standard relational database, which means your speed of execution of transactions goes up dramatically.
1. The third step in migration is converting the business logic to run inside GigaSpaces XAP itself in a processing unit, instead of using an enterprise application archive in a Java EE container. This conversion is usually very easy if the prior migration steps are followed - and they're easy as well.
