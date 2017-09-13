---
type: post122
title: Product Architecture
categories: XAP122OVW
parent: none
weight: 400
---

{{%  ssummary %}}{{%  /ssummary %}}

### GigaSpaces XAP is built from the following sub-systems:

{{% align center%}}
![archi_overview.jpg](/attachment_files/archi_overview.jpg)
{{% /align %}}

{{%  anchor Open-Interfacing-Layer%}}

# Open Interfacing Layer

Supports any language, any platform, any API - Achieve interoperability, easy migration, reduced learning curves, and faster time to market by leveraging existing assets - such as code and programming expertise - through:

- Standard API Support: Memcached, SQL, JPA, Spring, REST, a standard Map API and more.
- Multi-language Interoperability: Java, .Net, and C++
- Multi-platform Support: Any OS, physical or virtual
- API Mashup: Easily leverage modern APIs alongside existing standard APIs - enables you to use the right tool for the job at hand.



{{%  anchor OpenSpaces%}}

## OpenSpaces

OpenSpaces is the GigaSpaces native programming API. It is an open-source [Spring-based](http://www.springframework.org/about) application interface designed to make space-based development easy, reliable, and scalable. In addition, the programming model is non-intrusive, based on a simple POJO programming model and a clean integration point with other development frameworks.

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

The Remoting {{% latestjavanet "space-based-remoting-overview.html" %}} package provides capabilities for clients to access remote services. Remoting in GigaSpaces XAP is implemented on top of the data grid's clustering model, which provides location transparency, fail-over, and performance to remote service invocations. XAP implements remoting , using the space as the transport layer, similar to [other Spring remoting components](http://static.springframework.org/spring/docs/3.0.x/reference/remoting.html).

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

**Current implementations supported by GigaSpaces XAP**:



More information on the usage of the above integrations can be found in the Programmer's Guide {{% latestjavanet %}}.

{{%  anchor Spring-Container%}}

## Spring Container

The Spring framework container integration is included as part of XAP, and provides the ability to take advantage of [Spring](http://www.springframework.org/about) components, programming model and capabilities.

The Spring framework provides very elegant abstractions, which makes it very easy to build layered and decoupled applications.

## Jetty Web Container

Jetty is a very popular web container, which provides support for JEE [web container]({{% latestjavaurl%}}/web-application-support.html) specification services such as: Servlet, JavaServer Pages, JavaServer Faces, and others.

The [integration with the Jetty web container]({{% latestjavaurl%}}/web-jetty-processing-unit-container.html), allows you to run JEE web applications (.war files) in GigaSpaces XAP.

## Microsoft .NET container

The .NET SBA application takes advantage of the ability to run business services and .NET code, co located with the data stored within the space.

The [.NET container]({{% latestneturl%}}) bridges the technical gap and provides a native .NET experience for .NET applications.

## C++ Container

Much like the .NET container, the [CPP Processing Unit]({{% latestjavaurl%}}/cpp-processing-unit.html) provides a native C++ runtime environment for C++ SBA applications.

## Mule Integration

Mule is a popular open source Enterprise Services Bus implementation in Java. The [Mule container integration]({{% latestjavaurl%}}/mule-esb.html) allows you to run a Mule application on top of the GigaSpaces XAP, and gain scalability, performance and high-availability, with almost no changes to the Mule application.



# Unified In-Memory Services

Data access, messaging, parallel processing services, speeding up your application performance.

- In-memory speed: Delivering unmatched performance by removing all physical I/O bottlenecks from the runtime flow
- Scalability: Intelligently distribute any data and messaging load across all available resources
- Capacity: Support terabytes of application data
- High Availability: Built-in hot backup and self-healing capabilities for zero downtime
- Consistency: Maintain data integrity with 100% transactional data handling

As an application platform, GigaSpaces XAP provides integrated, memory-based runtime capabilities. The core of these capabilities is backed by the space technology - for more information, please refer to [Space - Concepts and Capabilities](./concepts.html#space-concepts-and-capabilities).

The core middleware capabilities are:



## In-Memory Data Grid

An In-Memory Data Grid (IMDG) is the way of storing data across a grid of memory nodes. This service provides the application with:

1. Data storage capabilities.
1. Data query capabilities - single object, multiple object and aggregated complex queries.
1. Caching semantics - the ability to retrieve information from within-memory data structures.
1. Ability to execute business logic within the data - similar to database storage procedure capabilities.

It is important to note that the IMDG, although a memory-based service, is fully transactional, and follows the ACID (Atomicity, Concurrency, Isolation and Durability) transactional rules.

The IMDG uses the unified clustering layer, to provide a highly available and reliable service.

The main API to access the IMDG service, is the [GigaSpace interface]({{% latestjavaurl%}}/the-gigaspace-interface.html). In addition, one can use the Map API (using the [GigaMap interface]({{% latestjavaurl%}}/map-api.html)) to access the IMDG. Please refer to the [Programmer's Guide]({{% latestjavaurl%}}) for usage examples.

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

XAP supports any environment, anytime, anywhere - traditional data center, public/private cloud, or hybrid deployments.

Isolate the runtime environment, physical address, and platform type from your application. The system takes care of provisioning your application onto the best available resources, and self adjusts to maintain utilization levels as machine availability changes over time.

## Real-Time SLA Assurance Engine

- Optimize IT resource utilization
- Dynamic resource allocation: Allocate resources automatically or manually - to avoid over-provisioning and/or under-utilization
- Automatic failover and self healing: Automatically recover from failures to maintain consistent SLA throughout the application life cycle
- Automation of manual processes such as provisioning, deployment, scaling and failover.

## Management & Monitoring Engine

Production-grade control and visibility

- Out-of-the-box multi-dimensional monitoring:
    - Operational: Services availability and topology
    - Application: Deep level business logic and data monitoring
    - Scalability: Load balancing and throughput monitoring
    - Utilization: Resource utilization tracking

- Fully managed platform:
    - Remote services control
    - Provision interface for integration with external tools and processes
    - Security - easily integrates with existing security mechanisms via JAAS

- Intuitive, comprehensive, user-friendly graphical interface: Short learning curve, fast time to service.

# Unified In-Memory Clustering



The role of clustering in GigaSpaces XAP is to provide scaling, load-balancing and high-availability. The main difference between GigaSpaces XAP and other clustering alternatives, is the use of a single clustering model for all middleware core capabilities. This means that the data and the services colocated with it are equally available. An example of how useful this is is that when a primary node fails, and another node acts as its backup, both application components (i.e. data and messaging) become active at the same time.

The ability to support a unified clustering model is a direct result of the underlying space-based clustering model. For more information on the concept of space, please refer to [Space - Concepts and Capabilities](./concepts.html#space-concepts-and-capabilities).

## Scaling

In space-based architecture, adding additional cluster nodes results in a linear addition of compute power and memory capacity. This results in the application's ability to support a higher workload, without adding to latency or application complexity.

## Data Load Balancing and Partitioning

GigaSpaces XAP's ability to distribute the processing load and/or storage across the cluster nodes results in the ability to support high and fluctuating throughput in addition to large volumes of data.

GigaSpaces XAP also supports content-based balancing, in addition to other technical balancing procedures, such as round-robin, random and connection affinity. The full power of content-based routing results in the ability to predict the physical location of the data, and to make sure that processing is routed into the same partition. This results in in-process computation, which leads to lower latencies and higher throughput.

## High Availability

High Availability (HA) is key to business critical applications, and it is a common requirement from the application server, to support it. The basic requirement of high availability is that failing services and application components will continue on different backup servers, without service disruption.

The key challenge with HA is state management. The typical solution for servers without state recovery capabilities is to remain 'stateless', and to store the state in a single shared storage - database or some kind of shared file system. However these solutions are very costly, since they result in synchronization and remote access to physical disks. In addition, the session state can be large, which means it introduces network latency into every update of data - even when that data is limited to the current transaction.

As GigaSpaces XAP has distributed shared memory capabilities, it is very simple and efficient to preserve high availability of stateful applications. The application state is replicated into backup nodes, resulting in immediate recovery in cases of fail-over and high-performance high-availability.

The GigaSpaces XAP solution does not require a compromise between stateless application complexity, performance and resiliency.



# Virtualized Deployment Infrastructure



A Deployment Infrastructure is responsible for abstracting the physical characteristics of the host machines from the application deployment. The Deployment Infrastructure is a set of runtime container processes deployed on multiple physical machines, which together form a virtual runtime cloud.  Once the cloud is formed, applications can be deployed for execution across the cloud, without a need to define specific host machine characteristics.

In addition, a Service Level agreement (SLA) is used at deployment time. It guarantees the system will be available and scalable. When it comes to provisioning and monitoring large-scale systems, the ability to specifically define the location of each node in the cluster becomes very laborious. The Deployment Infrastructure takes a pre-defined application SLA, and makes sure that it is met during deployment and runtime, throughout the application's life-cycle.

To clarify, here is an example of an application SLA:

1. Deploy 50 instances of each Processing Unit.
1. Make sure each processing unit has one backup.
1. Make sure primary and backups of the same Processing Unit are not deployed to the same VM.
1. Make sure primary and backup of the same Processing Unit are not deployed on the same physical server.

In this type of example, the Deployment Infrastructure is responsible for making sure that 100 Processing Units instances are deployed. Once the SLA is breached (for example, if a machine that is hosting contains a Processing Unit instance fails), the deployment infrastructure is responsible for re-provisioning all the processing units previously deployed on this machine into other containers running on another available machine(s).

{{%  info %}}
Note: The logical separation between multiple Deployment Infrastructures is defined by a Lookup Group. A lookup group is a logical name associated with each Deployment Infrastructure components. This is the prime way of separating between multiple Deployment Infrastructure environments running on the same network.
{{% /info%}}

## Grid Service Agent (GSA)

The [Grid Service Agent (GSA)](./service-grid.html#gsa) acts as a process manager that can spawn and manage Deployment Infrastructure processes (Operating System level processes) such as [Grid Service Manager](./service-grid.html#gsm) and [Grid Service Container](./service-grid.html#gsc).

Usually, a single GSA is deployed on each individual machine. The GSA spawns [Grid Service Managers](./service-grid.html#gsm), [Grid Service Containers](./service-grid.html#gsc), and other processes. Once a process is spawned, the GSA assigns a unique id for it and manages its life cycle. The GSA will restart the process if it exits abnormally (exit code different than 0), or if a specific console output has been encountered (for example, OutOfMemoryError).

{{%  note %}}
Though [Grid Service Manager](./service-grid.html#gsm), [Grid Service Container](./service-grid.html#gsc), and other processes can be started independently, it is preferable that they be started using the GSA, thus allowing to easily monitor and manage them.
{{% /note%}}

{{%  anchor gsm %}}

## Grid Service Manager (GSM)

The Grid Service Manager (GSM) is a special Deployment Infrastructure service responsible for managing the GSCs. The GSM accepts user deployment and undeployment requests, and provisions the Deployment Infrastructure cloud accordingly.

The GSM monitors SLA breach events throughout the life-cycle of the application, and is responsible for taking corrective actions once SLAs are breached.

{{%  info %}}
It is common to start two instances of GSMs across a Deployment Infrastructure cloud, for high-availability reasons.
{{% /info%}}

{{%  anchor gsc %}}

## Grid Service Container (GSC)

A Grid Service Container (GSC) is a container which hosts Processing Units instances. The GSC can be perceived as a node on the grid, which is controlled by the GSM. The GSM instruct the GSC to deploy or undeploy Processing Unit instances. The GSC reports its status to the GSM.

It is common to start several GSCs on the same physical machine, depending on the machine CPU and memory resources. The GSCs provide a layer of abstraction on top of the physical layer (Machines). This concept enables deployment of clusters on various deployment topologies on enterprise data centers and public clouds.


