---
type: post
title:  OpenSpaces
categories: FAQ
weight: 100
parent: none

---

{{%fpanel%}}

- [What is OpenSpaces?](#1)

- [Who should use OpenSpaces?](#2)

- [Is OpenSpaces an open source project?](#3)

- [What is the open source license for OpenSpaces?](#4)

- [How can I contribute code to OpenSpaces?](#5)

- [What is a Processing Unit?](#6)

- [How does OpenSpaces relate to Spring?](#7)

- [How does OpenSpaces relate to the GigaSpaces Service Grid?](#8)

- [How does OpenSpaces relate to EDA and SOA?](#9)

- [How do I package my application with OpenSpaces?](#10)

- [How can I change a service within a Processing Unit without shutting down the Processing Unit?](#11)

{{%/fpanel%}}

{{%anchor 1%}}

### What is OpenSpaces?

OpenSpaces is designed to enable scaling-out of stateful applications in a simple way using the [Spring Framework](http://www.springframework.org/). It is shipped as an open source initiative from GigaSpaces, and supports the GigaSpaces Space-Based Architecture model out-of-the-box.

OpenSpaces is useful for Spring users, Service-Oriented Architecture (SOA) and Event-Driven Architecture (EDA) developers, transactional applications, real-time analytics, and Web 2.0 applications.

{{%anchor 2%}}

### Who should use OpenSpaces?

- **Spring Users** -- Spring users can use this framework for high-availability and scalability of their application  -- without being dependent on a J2EE container -- by using a lightweight SLA-driven container. They can thus benefit from the simplicity of Spring throughout the entire application and deployment environment.
- **SOA/EDA Developers** -- those looking to develop SOA/EDA applications will find OpenSpaces to be a simple and highly efficient approach for building such architectures in high-performance, stateful environments.
- **Low-Latency Transactional Applications** -- typical transaction-processing applications, such as those for billing, trading and order management, involve handling incoming data-feeds, data enrichment (logic which turns those feeds into something meaningful), and a set of 'workers' that perform specific business logic on this data, such as matching. OpenSpaces provides the specific ingredients required for this process, such as POJO-driven event handlers, a Data Grid and Messaging Grid, which provide the mechanisms for processing the events, as well as the data and the workflow. It enables encapsulation of all those elements into a Processing Unit to ensure low latency. It can then be partitioned for scalability.
- **Real-Time Analytics** -- real-time analytical applications, such as P&L calculation, reconciliation and fraud detection, are typically required to process high volumes of data at high speeds. Writing such applications requires logic for loading data; optimization for reducing the memory footprint; in-memory query capabilities and parallel querying for efficient data retrieval and data aggregation; integration with external data sources (primarily relational databases); and scalability. These requirements specifically apply to the data capacity level, which will enable storing of data across a cluster of hundreds of units possibly holding several terabytes of data. OpenSpaces includes all of the above through GigaSpaces' Space-Based Architecture, and significantly simplifies the implementation of such applications using the OpenSpaces abstraction. This is done with event-handlers that act as the equivalent of stored-procedures, built-in support for a 'mirror-service' to enable synchronization with external databases, and Spring DAO support.
- **Web 2.0** -- Web 2.0 applications often have to support a very high number of concurrent users/connections. The read/write nature of Web 2.0 applications makes them more stateful than typical Web 1.0 applications. This makes them harder to scale through a simple load-balancer approach commonly used in existing stateless web-based applications. Spring already provides a rich environment, that simplifies the development of web-based applications through its MVC framework, and integrates with many other web frameworks and popular AJAX toolkits. With OpenSpaces, you can take these applications and simply scale them out by putting the data into in-memory partitions. Session information can also be stored into the In-Memory Data Grid (IMDG), thus eliminating a potential memory bottleneck, while having numerous concurrent users. In addition, the Processing Grid enables parallel processing of user requests asynchronously to avoid high latency. AJAX applications can also benefit from this framework by spreading the AJAX load between the different partitions. This allows handling a large number of consumers who are consuming real-time information through the system.

{{%anchor 3%}}

### Is OpenSpaces an open source project?

Not exactly. OpenSpaces is the top layer of XAP (eXtreme Application Platform) - a powerful, scalable In-Memory Data Grid. OpenSpaces is a package which wraps the core XAP functionality with a user-friendly Spring-based API, as well as additional patterns which have become common across many users and are now part of the library.

We've decided to mirror the OpenSpaces source code to a public GitHub repositoy -- [Gigaspaces/xap-openspaces](https://github.com/Gigaspaces/xap-openspaces) -- so users can fork and/or clone it in order to

- Review the code to better understand how certain features work under the hood.
- Debug problems independently (in addition to posting a question on the [forum](http://ask.gigaspaces.org) or submitting a [support ticket](http://www.gigaspaces.com/support-center)).
- Develop your own framework around it, without being locked into the existing abstraction. For example, users can implement their own event handler, which can take data from other sources than the space. They can also extend the Processing Unit to meet their specific needs.

{{%anchor 4%}}

### What is the open source license for OpenSpaces?

OpenSpaces is provided under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). Note that the underlying GigaSpaces XAP implementation is provided only in its binary distribution format. A Development Source license for XAP will be provided to GigaSpaces customers.

{{%anchor 5%}}

### How can I contribute code to OpenSpaces?

The OpenSpaces source code is mirrored to the [Gigaspaces/xap-openspaces](https://github.com/Gigaspaces/xap-openspaces) GitHub repository, so you can fork and/or clone it to review the code, debug it, or even extend it. However, since this is a mirror repository, we cannot accept pull requests. If you have a suggestion, you're welcome to post it on the [forum](http://ask.gigaspaces.org) or email us at [contribute@openspaces.org](mailto:contribute@openspaces.org).

{{%anchor 6%}}

### What is a Processing Unit?

A Processing Unit represents the unit of scalability and failover in Space-Based Architecture (SBA). The Processing Unit is designed to be a self-sufficient unit that contains the messaging, data and business logic within the same process. It is written just like any Spring application with the addition of the built-in [OpenSpaces Components](/product_overview/product-architecture.html#OpenSpaces-API-and-Components) for handling events, state and workflow.

{{%anchor 7%}}

### How does OpenSpaces relate to Spring?

OpenSpaces utilizes Spring as the POJO development infrastructure and adds runtime and development components for developing POJO-driven EDA/SOA applications, and scales them out across a pool of machines in a simple way -- without a JEE container.

To achieve the above goal, it adds the following components to the Spring development environment:

- **Processing Unit** -- the core unit of work, which encapsulates the middleware as well as the business logic as a single unit of scale and failover.
- **SLA-Driven Container** -- a lightweight container that enables dynamic deployment of Processing Units over a pool of machines based on machine availability, CPU utilization, capabilities and other criteria.
- **In-Memory Data Grid** -- provides in-memory distributed data storage.
- **Declarative Event Containers** -- for triggering events from the space into POJOs in either pull or push mode.
- **Remoting** -- utilizes the space as the underlying transport for invoking remote methods on POJO services that "live" within a Processing Unit. With this approach, a client can invoke a method on a service even if it changes its physical location. It can re-route requests to available services in case of failover.
- **Declarative transaction support** for the GigaSpaces In-Memory Data Grid.
- **Spring Security** -- Spring Security is one of the most mature and widely used Spring projects. Spring Security can be used to secure The Data-Grid and application deployed into the GigaSpaces container.

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


{{%anchor 8%}}

### How does OpenSpaces relate to the GigaSpaces Service Grid?

The GigaSpaces Service Grid is used as the SLA-driven container for the deployment of Spring-based Processing Units. OpenSpaces uses a Spring-based deployment descriptor. The Spring-based deployment-descriptor is a simplified version of the Service Grid deployment-descriptor, specialized for EDA and SOA use cases. It provides a generic form of defining SLAs for an entire Processing Unit. For example, it adds primary-backup semantics and a partitioning topology that maintains the partitioning of the spaces, as well as the services.

{{%anchor 9%}}

### How does OpenSpaces relate to EDA and SOA?

Service-Oriented Architectures (SOA) and Event-Driven Architectures (EDA) introduce the notion of composing an application with a set of loosely-coupled services that together can fulfill a certain business requirement. In this way, we can reduce the dependency among components, simplifying the introduction of new functionality, integration with other components, performing partial upgrades, as well as handling failover and scaling events.

While the concept behind SOA/EDA is straightforward, most existing implementations are based on Web Services as the core runtime environment. Space-Based Architecture (SBA) -- the design pattern behind OpenSpaces -- is a design pattern for building SOA/EDA driven applications in high-performance, stateful environments. It is based on a POJO-driven approach (as opposes to Web Services), which provides a framework for loosely-coupled POJO services that are collocated on the same Java Virtual Machine (JVM) -- the same services can be accessed remotely without any code change. It leverages an In-Memory Data Grid to enable sharing of context and information between the collocated services, and other distributed services can therefore handle state transition and coordination in a very efficient way.

OpenSpaces was designed to support the SBA model out of the box in a simple and generic way, and therefore, make the development of high-performance SOA/EDA applications simple and scalable.

{{%anchor 10%}}

### How do I package my application with OpenSpaces?

One of the main goals of OpenSpaces is simplifying lifecycle management of an application. A typical application starts within the developer IDE and then progresses to a test environment, pre-production and finally production. OpenSpaces allows to run and debug a Processing Unit within the IDE in a simple manner, package it and then simply provide different deployment scenarios.

A Processing Unit is a simple directory structure. It includes a Spring XML configuration file (under `META-INF/spring/pu.xml`), the business logic class files, and third-party module `jar` files. A Processing Unit, under the mentioned structure, can then run within the IDE, locally, and deployed on the SLA-driven container without any changes.

{{%anchor 11%}}

### Can I change a service within a Processing Unit without shutting down the Processing Unit?

Yes. OpenSpaces supports dynamic reloading of **selected** service beans (business logic) without bringing down the processing unit. Read more [here]({{%latestjavaurl%}}/reloading-business-logic.html).

This mainly applies when wanting to change business logic of a Processing Unit that also starts a space, without shutting down the space. Any Processing Unit or other services that connect to the space remotely can be replaced easily with the current version.

{{%info%}}

Didn't find the answers you were looking for? Feel free to **[write to us](mailto:feedback60@gigaspaces.com?subject=OpenSpaces%20FAQ)** or visit our **[forums](http://ask.gigaspaces.org)**.
{{%/info%}}
