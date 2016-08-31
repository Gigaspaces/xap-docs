---
type: post
title:  Overview
categories: PRODUCT_OVERVIEW
parent: none
weight: 100
menu: product
---

{{% ssummary%}}{{% /ssummary%}}

GigaSpaces XAP is a platform that scales out stateful applications in high-performance low-latency environments. It is designed to support eXtreme transactional applications such as OMS (Order Management Systems), pre-paid systems, trading, and market data; and real-time analytics applications such as profit-and-loss analysis, reconciliation, and Value at Risk.

XAP is based on GigaSpaces' Spring-based [Open Spaces Framework](./product-architecture.html#product-architecture-openSpaces-api-and-components) as the primary development environment, and uses the GigaSpaces space-based runtime to deliver core middleware facilities: messaging, data caching and parallelization.

Applications running on XAP can be scaled out linearly, because XAP uses the [Space Based Architecture](./concepts.html#concepts-space-based-architecture) (SBA) as a primary design pattern. With SBA, applications are built out of a set of self-sufficient units, known as Processing Units (PU). These units are completely independent of each other, so that the application can scale indefinitely without increasing complexity, just by adding more units. SBA is based on the [Tuple Space](http://en.wikipedia.org/wiki/Tuple_space) paradigm; it follows many of the principles of [Service-Oriented Architecture](http://en.wikipedia.org/wiki/Service-Oriented_Architecture) and [Event-Driven Architecture](http://en.wikipedia.org/wiki/Event_Driven_Architecture), as well as elements of [grid computing](http://en.wikipedia.org/wiki/Grid_computing).

**This overview introduces XAP from several different perspectives:**

- **[A components perspective](#components)** - explains key capabilities of GigaSpaces XAP -- the Open Spaces framework; the space-based core middleware and the middleware facilities it provides; and the SLA-Driven Container.
- **[A runtime perspective](#runtime)** - shows how GigaSpaces components execute and interact in runtime on multiple physical machines.
- **[An SOA/EDA perspective](#soa)** - shows how GigaSpaces XAP and the Space-Based Architecture are actually a special case of SOA/EDA, and can be used to implement a Service Oriented Architecture which supports high-performance, stateful services.
- **[A remote client perspective](#client)** - illustrates how GigaSpaces XAP is viewed and accessed by remote clients, whether they are running inside XAP Processing Units or as independent POJO services.

{{%  refer %}} For a GigaSpaces XAP product architecture overview, see the [Product Architecture section](./product-architecture.html).{{%  /refer %}}

{{%  anchor components %}}

# XAP Components Perspective

The following diagram shows a component view of GigaSpaces XAP. The main components are described in more detail below.

{{% align center%}}
![XAP Architecture Overview.jpg](/attachment_files/XAP Architecture Overview.jpg)
{{% /align %}}

## OpenSpaces

Open Spaces is the primary framework for developing applications in GigaSpaces. Open Spaces uses Spring as a POJO-driven development infrastructure, and adds runtime and development components for developing POJO-driven EDA/SOA-based applications, and scaling them out simply across a pool of machines, without dependency on a J2EE container.

To achieve these goals, Open Spaces adds the following components to the Spring development environment:

- **[Processing Unit]({{%latestjavaurl%}}/the-processing-unit-overview.html)** -- the core unit of work. Encapsulates the middleware together with the business logic in a single unit of scaling and failover.
- **[SLA-Driven Container](./service-grid.html#gsc)** -- a lightweight container that enables dynamic deployment of Processing Units over a pool of machines, based on machine availability, CPU utilization, and other hardware and software criteria.
- [In-Memory Data Grid](./the-in-memory-data-grid.html) -- provides in-memory distributed data storage.
- **[Declarative Event Containers]({{% latestjavaurl%}}/messaging-support.html)** -- for triggering events from the space into POJOs in pull or push mode.
- **[Remoting]({{% latestjavaurl%}}/space-based-remoting.html)** -- utilizes the space as the underlying transport for invoking remote methods on the POJO services inside the Processing Unit. This approach allows the client to invoke methods on a service even if it changes physical location, and enables re-routing of requests to available services in case of failover.
- **[Declarative transaction support]({{% latestjavaurl%}}/transaction-management.html)** for GigaSpaces In-Memory Data Grid.

{{%  refer %}} For a list of frequently-asked questions, including licensing model and positioning questions, see the [OpenSpaces FAQ](/faq/openspaces-faq.html).{{%  /refer %}}

## Core Middleware

XAP relies on the JavaSpaces (space-based) model as its core middleware, and provides specialized components, implemented as wrapper facades on top of the space implementations, to deliver specific data or messaging semantics. XAP exposes both the JavaSpaces API, with different flavors suited to the usage scenario (SQLQuery for data, FIFO for messaging, etc.), and other standard APIs such JCache/JDBC and JMS.

**XAP middleware virtualization facilities:**

- **Space-Based Clustering** -- provides all clustering services necessary to stateful applications. Based on a clustered JavaSpaces implementation.
- **In-Memory Data Grid** -- provides data caching semantics on top of the GigaSpaces core middleware; addresses the key issues of distributed state sharing. Supports a wide set of APIs including JDBC for SQL/IMDB, hash table through Map/JCache interface, and JavaSpaces. All common caching topologies are supported, including replication and partitioning of data. The table below summarizes the key features of this component.


| Feature | Benefit |
|:--------|:--------|
| [Extended and Standard Query]({{% latestjavaurl%}}/query-sql.html) based on SQL, and ability to connect to IMDG using standard JDBC connector. | Makes the IMDG accessible to standard reporting tools, and makes accessing the IMDG just like accessing a JDBC-compatible database, reducing the learning curve. |
| SQL-based continuous query support. | Brings relevant data close to the local memory of the relevant application instance. |
| [GigaSpaces Management Center]({{% latestadmurl%}}/gigaspaces-management-center.html) -- central management, monitoring and control of all IMDG instances on the network. | Allows the entire IMDG to be controlled and viewed from an administrator's console. |
| [Mirror Service]({{% latestjavaurl%}}/asynchronous-persistency-with-the-mirror.html) -- transparent persistence of data from the entire IMDG to a legacy database or other data source. | Allows seamless integration with existing reporting and back-office systems. |
| Real-time event notification -- application instances can selectively subscribe to specific events. | Provides capabilities usually offered by messaging systems, including slow-consumer support, FIFO, batching, pub/sub, content-based routing. |

- **Messaging Grid** -- enables services to communicate and share information across the distributed In-Memory Data Grid. Supports a variety of messaging scenarios using the JavaSpaces or JMS API.
- **[*Parallel Processing*](/sbp/xap-order-management-tutorial.html)** -- enables parallel execution of low latency, high-throughput business transactions, using the Master-Worker pattern.

## SLA-Driven Container

SLA-Driven Containers (known also as Grid Service Containers or GSCs), enable deployment of Processing Units over a dynamic pool of machines, based on SLA definitions. Each container is a Java process which provides a hosting environment for application services bundled in a Processing Unit. The container virtualizes the underlying compute resources, and performs mapping between the application runtime and the underlying resources, based on SLA criteria such as CPU and memory usage, hardware configuration and software resource availability (JVM, DB, etc). It also provides self-healing capabilities for handling failure or scaling events.

### SLA-Driven In-Memory Data Grid

With XAP, IMDG instances are constructed out of space instances. The IMDG can be deployed just like any other service, within a Processing Unit. This provides the option to associate SLA definitions with the IMDG. A common use is to relocate IMDG instances based on memory utilization; another use is to use SLA definitions to handle deployment of different IMDG topologies over the same containers.

The SLA-Driven Containers can also add self-healing characteristics. When one container crashes, the failed IMDG instances are automatically relocated to the available containers, providing the application with continuous high-availability.

{{% align center%}}
![XAP SLA Container failover.jpg](/attachment_files/XAP SLA Container failover.jpg)
{{% /align %}}

{{%  anchor runtime %}}

# Runtime Perspective

From a runtime perspective, a XAP cluster is a cluster of machines, each running one or more instances of SLA-Driven Containers. The containers are responsible for exposing the hardware resources to the XAP applications.

An application running with XAP is built of multiple Processing Units. The Processing Units are packaged as part of a bundle; bundle structure is compliant with Spring/OSGI. Each bundle contains a deployment descriptor named `pu.xml`, a Spring application context file with Open Spaces component extensions. This file contains the Processing Unit's SLA definition, as well as associations between the application components, namely the POJO services, the space middleware components, and most commonly, a Data Grid.

{{% align center%}}
![XAP Runtime Perspective2.jpg](/attachment_files/XAP Runtime Perspective2.jpg)
{{% /align %}}

The application is deployed through a GSM (Grid Service Manager) which performs match-making between the SLA definitions of the application's Processing Unit and the available SLA-Driven Containers. The SLA definitions include the number of instances that need to be deployed, the number of instances that should run per container and across the entire network, and system requirements such as the required JVM version or database version.

Different applications may have one or more instances of their Processing Units running in the same container at the same time. Even though the applications share the same JVM instance, they are kept isolated through application-specific classloaders.

{{%  anchor soa %}}

# SOA/EDA Perspective

The Space-Based Architecture (SBA) can be viewed as a special case of SOA/EDA, designed specifically for high-performance stateful applications.

{{% align center%}}
![xap_soa1.gif](/attachment_files/xap_soa1.gif)
{{% /align %}}

A classic SOA is based on the Enterprise Service Bus (ESB) model, as shown in the diagram above. In this model, services become loosely-coupled through the use of a messaging bus. Scaling is done by adding more services into the bus and load-balancing the requests between them.

Most implementations of this model rely on web services to handle message flow between the services. These implementations cannot handle state-fullness of the services. So while the loosely-coupled concept of SOA can be promising for simplifying and scaling of services over the network, most existing implementations of this model are not suited for handling high-performance applications, especially not in the context of stateful services.

With SBA, a similar model can be implemented using the space. The space functions as an in-memory messaging bus -- an ESB for delivering and routing transactions -- but also as an In-Memory Data Grid which can support stateful services.

{{% align center%}}
![xap_soa2.gif](/attachment_files/xap_soa2.gif)
{{% /align %}}

To avoid the I/O overhead associated with the interaction of these services with either the messaging layer or the data layer, SBA introduces the concept of a Processing Unit, which is essentially a deployment/runtime optimization. Instead of having each component of the architecture separate and remote, we bundle together the relevant message queue, its associated services, and the data into a single unit: the Processing Unit, which always runs in a single VM. In this case, the interaction between the messaging, the services, and the data layer is done in-process as well as in-memory, ensuring the lowest possible latency.

{{% align center%}}
![xap_soa3.gif](/attachment_files/xap_soa3.gif)
{{% /align %}}

The services that reside within a Processing Unit are just like any other services in the web services world. Their lifecycle can be managed individually, and they can be deployed and upgraded dynamically without bringing down the entire Processing Unit (assuming they are implemented as OSGI services).

The services can also be accessed by any other services, whether they reside in the same processing unit or are remote clients. In the case of collocated services, interaction is very efficient, since it is done entirely in-memory. In the case of remote services, Processing Unit services can be accessed in various forms, including the classic remoting topology. The following section describes the different interaction and runtime options for clients interacting with SBA Processing Unit services.
{{%  anchor client %}}

# Remote Client Perspective

Applications deployed in XAP are distributed across multiple machines. In the classic tier-based approach, remote client interactions were mainly RPC-based, or in some cases message-driven. RPC-based communication assumes direct reference to a remote server. This approach doesn't work in XAP-based applications, because they span multiple physical machines and change their location during runtime based on the SLA. This lead to a requirement that client interaction with XAP applications can be done through a virtualized remote reference, which can keep track of different application instances during runtime, and route client requests to the appropriate instance based on the request type, its content, and so on.

## Modes of Interaction

XAP-based applications enable several modes of communication between the client application and the actual server instances, all relying on the space to enable virtualization of the interaction.

### Data-Driven Interaction

Data-driven interaction is common in analytics scenarios. It means that the client application interacts primarily with the application data, by performing queries and updates. The business logic is triggered as a result of this interaction, by means of notifications (the equivalent of database applications) or extended queries (the equivalent of stored procedure).

In XAP, this mode of interaction can be achieved in two ways:

- By interacting directly using the space interface. In this case, a _write_ operation on the space is the equivalent of an _update/put_ or _insert_; a _read_ operation is the equivalent of a _select_; and a _notify_ operation is the equivalent of a _trigger_.
- Using the wrapper facades provided by GigaSpaces, such as the JCache/Map interface or JDBC driver, which perform this mapping implicitly.

### Message-Driven Interaction

Message-driven interaction is common in transaction processing scenarios and is based on the Command Pattern (also known as the Master-Worker Pattern). In this pattern, applications interact by sending command messages; services on the server side await these messages and are triggered by their arrival. Ordinarily, the business logic of the services is to interact with the IMDG to retrieve current state information, to reference the data, and finally to synchronize state to enable workflow with other services.

In XAP, this mode of interaction can be achieved in two ways:

- By interacting directly with the space interface. In this case, a _write_ operation is the equivalent of a _send_ and a _notify_ or _take_ operation is the equivalent of a _receive_ or _subscribe_, respectively.
- Using the JMS interface, which is provided as a wrapper on top of the space API, and maps between the JMS operation and the required space operations.

### RPC-Based Interaction

RPC (Remote Procedure Call) is used to invoke business logic method on a remote server. It is different from the message driven interaction in two respects:

- **Synchronous by nature** -- based on a request-response interaction.
- **Type-safe** -- ensures type safety of the operation and arguments at compile time, because it is directly bounded with the remote service interface.

In XAP, this mode of interaction is achieved by space-based remoting. This method leverages the fact that the space is already exposed as a remote entity, and has an existing virtualization mechanism, to enable remote invocation on services that are spread across multiple Processing Units, possibly running on multiple physical machines.

With space-based remoting, a remote stub in generated for the remote service, using dynamic proxies. When a method is invoked on this proxy, the stub implicitly maps it to a command that is written to the space and is routed to the appropriate server instance. On the server-side, a generic delegator takes these commands and execute the method on the specific bean instance, based on the method name and arguments provided in the command. The result is also returned through the space, is received by the dynamic proxy, and is returned transparently to the client as the return value of the method.

{{%  refer %}}For more details, see [Executor Based Remoting]({{% latestjavaurl%}}/executor-based-remoting.html).{{%  /refer %}}

## Remote Client Interaction Options

From a runtime perspective, there are several ways remote clients can interact with XAP-based applications:

- **Remote client running in a Processing Unit on an SLA-Driven Container** -- a client can be deployed in its own Processing Unit, like the server instances, except that the client references services residing in remote Processing Units. In this mode the client is deployed and runs in an SLA-Driven Container, just like the server instances.
- **Remote client running in a standalone processing unit** -- the client still runs in a processing unit, but outside the container. This allows it to leverage Processing Unit facilities to simplify its logic -- facilities such as the space abstraction and transactions -- without being dependent on the container. This mode can be useful for rich clients running as Swing applications, as web containers, and so on.
- **Plain Java clients, J2EE** -- this can be either a regular POJO client that interacts with the space, or a Session Bean that obtains a reference to the space through the GigaSpaces SpaceFinder method, and uses that reference to interact with the space directly.
- **[.NET]({{% latestjavaurl%}}/index.html)**, **[C++]({{% latestjavaurl%}}/xap-cpp.html)** -- GigaSpaces provides .NET and C++ libraries that enable direct interaction with services via the space, just like a POJO client.

{{% align center%}}
![Remote Clients.jpg](/attachment_files/Remote Clients.jpg)
{{% /align %}}
