---
type: post122
title: XAP Architecture
categories: XAP122OVW
parent: none
weight: 400
---

{{%  ssummary %}}{{%  /ssummary %}}

GigaSpaces XAP is built from the following sub-systems:

{{% align center%}}
![archi_overview.jpg](/attachment_files/archi_overview.jpg)
{{% /align %}}

{{%  anchor Open-Interfacing-Layer%}}

# Open Interfacing Layer

Supports any language, any platform, any API - Achieve interoperability, easy migration, reduced learning curves, and faster time to market by leveraging existing assets - such as code and programming expertise - through:

- Standard API Support: Memcached, SQL, JPA, Spring, REST, a standard Map API and more.
- Multi-language Interoperability: Java, .NET, and C++
- Multi-platform Support: Any OS, physical or virtual
- API Mashup: Easily leverage modern APIs alongside existing standard APIs - enables you to use the right tool for the job at hand.



{{%  anchor OpenSpaces%}}

## OpenSpaces

OpenSpaces is the GigaSpaces native programming API. It is an open-source {{%exurl "Spring-based""https://spring.io/"%}} application interface designed to make space-based development easy, reliable, and scalable. In addition, the programming model is non-intrusive, based on a simple POJO programming model and a clean integration point with other development frameworks.

The OpenSpaces API is divided into four parts:

- Core API
- Messaging and Events
- Space Based Remoting
- Integrations

{{%  anchor Core-API%}}

## Core API

The core package of OpenSpaces provides APIs for direct access to a data grid, internally referred to as a "Space." The main interface is the GigaSpace, which enables the basic interaction with the data grid. The core components include basic infrastructure support such as Space {{% latestjavanet "the-gigaspace-interface-overview.html"%}} construction, simplified API using the GigaSpace  interface including Transaction Management {{% latestjavanet "transaction-overview.html"%}} and declarative transaction support. Core components also include support for [Map/Cache]({{<latestjavaurl>}}/map-api.html) construction and a simplified API using [GigaMap]({{<latestjavaurl>}}/map-api.html).

{{%  anchor Events%}}

## Events

The events package is built on top of the core package, and provides simple object-based event processing components through the event containers, making it roughly equivalent to Java EE's message-driven beans. (The primary differences outside of API semantics are in the power of OpenSpaces' selection criteria and routing.) The event package enables simple construction of event-driven applications.

Another alternative for events is the usage of JMS 1.1 on top of GigaSpaces, which is supported within the product and is recommended for client applications integrating with SBA applications.

The events module includes components for simplified EDA/Service Bus development. These components allow [unified event-handling]({{% latestjavaurl%}}/data-event-listener.html) and provide two mechanisms for event-generation: a Polling Container {{% latestjavanet "polling-container-overview.html"%}} uses polling received operations against the space, and a Notify Container {{% latestjavanet "notify-container-overview.html" %}} which uses the space's built-in notification support.

{{%  anchor Space-Based-Remoting%}}

## Space Based Remoting

The Remoting {{% latestjavanet "space-based-remoting-overview.html" %}} package provides capabilities for clients to access remote services. Remoting in GigaSpaces XAP is implemented on top of the data grid's clustering model, which provides location transparency, fail-over, and performance to remote service invocations. XAP implements remoting , using the space as the transport layer, similar to {{%exurl "the Spring remoting components""http://static.springframework.org/spring/docs/3.0.x/reference/remoting.html"%}}.

Remoting can be viewed as the alternative to Java EE Session Beans, or Java RMI, as it provides all of their capabilities as well as supporting synchronous and asynchronous invocations, and dynamic scripting languages - enabling you to use Groovy or Ruby in your space-based applications.

{{%  anchor Integrations%}}

## Integrations

This package contains integrations with non-XAP components. For more information please refer to the Programmers Guide.



{{%  anchor Elastic-Application-Container%}}

# Elastic Application Container

XAP is an end-to-end-scalable execution environment with "elastic deployment" to meet extreme throughput requirements.

- Linear scalability: Elastically deployed/provisioned to cope with extreme demand/throughput during application runtime with no human intervention
- Flexibility: Running a variety of application module types, from simple web modules to complex event processing modules
- Simpler to move into production:
    - Smooth, risk-free deployment through identical development and production environments
    - Faster deployment through eliminating silos
    - Continuous deployment with no downtime

Lightweight application containers provide a business logic execution environment at the node level. They also translate SBA semantics and services to the relevant container development framework implementation. For example, space transactions are translated to Spring transactions, when a Spring lightweight container is used.

The Grid Service Container (GSC) is responsible for providing Grid capabilities, whereas the lightweight container implementation is responsible at the single VM level. These architectures are very powerful, as it enables applications to take advantage of the familiar programming models and services at the single VM level, and in addition provides grid capabilities and services.

GigaSpaces XAP provides several default implementations as part of the product, and an additional plugin API, to enable other technology integrations.

**Current implementations supported by GigaSpaces XAP**



More information on the usage of the above integrations can be found in the Developer Guide {{% latestjavanet %}}.

{{%  anchor Spring-Container%}}

## Spring Container

The Spring framework container integration is included as part of XAP, and provides the ability to take advantage of {{%exurl "Spring""https://spring.io/"%}} components, programming model and capabilities.

The Spring framework provides very elegant abstractions, which makes it very easy to build layered and decoupled applications.

## Jetty Web Container

Jetty is a very popular web container, which provides support for JEE [web container]({{% latestjavaurl%}}/web-application-support-overview.html) specification services such as: Servlet, JavaServer Pages, JavaServer Faces, and others.

The integration with the [Jetty web container]({{% latestjavaurl%}}/web-jetty-processing-unit-container.html) allows you to run JEE web applications (.war files) in GigaSpaces XAP.

## Microsoft .NET container

The .NET SBA application takes advantage of the ability to run business services and .NET code, co located with the data stored within the space.

The [.NET container]({{% latestneturl%}}) bridges the technical gap and provides a native .NET experience for .NET applications.

 
## Mule Integration

Mule is a popular open source Enterprise Services Bus implementation in Java. The [Mule container integration]({{% latestjavaurl%}}/mule-esb.html) allows you to run a Mule application on top of the GigaSpaces XAP, and gain scalability, performance and high-availability, with almost no changes to the Mule application.



# Unified In-Memory Services

Data access, messaging, parallel processing services, speeding up your application performance.

- In-memory speed: Delivering unmatched performance by removing all physical I/O bottlenecks from the runtime flow
- Scalability: Intelligently distribute any data and messaging load across all available resources
- Capacity: Support terabytes of application data
- High Availability: Built-in hot backup and self-healing capabilities for zero downtime
- Consistency: Maintain data integrity with 100% transactional data handling

As an application platform, GigaSpaces XAP provides integrated, memory-based runtime capabilities. The core of these capabilities is backed by the space technology.  

The core middleware capabilities are:



## In-Memory Data Grid

An In-Memory Data Grid (IMDG) is a way of storing data across a grid of memory nodes. This service provides the application with:

1. Data storage capabilities.
1. Data query capabilities - single object, multiple object and aggregated complex queries.
1. Caching semantics - the ability to retrieve information from within-memory data structures.
1. Ability to execute business logic within the data - similar to database storage procedure capabilities.

It is important to note that the IMDG, although a memory-based service, is fully transactional, and follows the ACID (Atomicity, Concurrency, Isolation and Durability) transactional rules.

The IMDG uses the unified clustering layer, to provide a highly available and reliable service.

The main API to access the IMDG service, is the [GigaSpace interface]({{% latestjavaurl%}}/the-gigaspace-interface.html). In addition, one can use the Map API (using the [GigaMap interface]({{% latestjavaurl%}}/map-api.html)) to access the IMDG. Please refer to the [Developer Guide]({{% latestjavaurl%}}) for usage examples.

## Messaging Grid

The messaging grid aspect of the space provides messaging capabilities such as:

1. Event-Driven capabilities - the ability to build event-driven processing applications. This model enables fast (in-memory-based) asynchronous modular processing, resulting in a very efficient and scalable processing paradigm.
1. Asynchronous production and consumption of information.
1. One-to-one, Many-to-One, One-to-Many and Many-to-Many relationships.
1. FIFO  ordering. {{% latestjavanet "fifo-overview.html"%}}
1. Transaction Management {{% latestjavanet "transaction-overview.html"%}}.

The core APIs used for messaging are the OpenSpaces [Notify]({{% latestjavaurl%}}/notify-container.html) and [Polling]({{% latestjavaurl%}}/polling-container.html) Containers. In addition, a [JMS 1.1 implementation]({{% latestjavaurl%}}/messaging-support.html) API is available to be used with existing JMS based applications. More information can be found in the [Messaging and Events]({{% latestjavaurl%}}/messaging-support.html) section.

## Processing Services

Processing services include parallel processing capabilities.

### Parallel Processing

Sometimes the scalability bottleneck is within the processing capabilities. This means that there is a need to gain more processing power to be executed concurrently. In other words, there is a need for parallel processing. When there is no state involved, it is common to spawn many processes on multiple machines, and to assign a different task to each process.

However, the problem becomes much more complex when the tasks for execution require sharing of information. GigaSpaces XAP has built-in services for parallel processing. The master/worker pattern is used, where one process serves as the master and writes objects into the space, and many worker services each take work for execution and share the results. The workers then request a new piece of work, and so on. This pattern is important in practice, since it automatically balances the load.

### Compute Grid

The compute grid is a mechanism that allows you to run user code on all/some nodes of the grid, so that the code can run locally with the data.

Compute grids are an efficient solution when a computation requires a large data set to be processed, so that moving the code to where the data is, is much more efficient than moving the data to where the code is.

The efficiency derives from the fact that the processing task is sent to all the desired grid nodes concurrently. A partial result is calculated using the data on that  particular node, and then sent back to the client, where all the partial results are reduced to a final result.

The process is widely known as map/reduce, and is used extensively by companies like Google whenever a large data set needs to be processed in a short amount of time.



# Web Container

XAP can host your java web modules so your application is entirely managed and scaled on a single platform, providing load balancing and extreme throughput, and ensuring end to end scalability.

GigaSpaces allows you to deploy web applications (packaged as WAR files) onto the GSC. This support provides:

- Dynamic allocation of several instances of a web application (probably fronted by a load balancer).
- Management of the instances running (if a GSC fails, the web application instances running on it will be instantiated on a different GSC).
- SLA-monitor-based dynamic allocation and de-allocation of web application instances.

The deployed WAR is a pure Java EE-based web application. The application can be the most generic web application, and automatically make use of the Service Grid features. The web application can define a Space (either embedded or remote) very easily (either using Spring or not).

# Virtualized Deployment Infrastructure
Detailed information about the virtualized deployment infrastructure can found in the [Service Grid Layer](./the-runtime-environment.html) section.
