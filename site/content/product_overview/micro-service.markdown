---
type: post
title:  Space Based Micro Services
categories: PRODUCT_OVERVIEW
parent: none
weight: 1220
---

{{%  ssummary %}}{{%  /ssummary %}}

# Introduction
The Micro services paper by [Martin Fowler & James Lewis](http://martinfowler.com/articles/microservices.html)  has been influencing many development team and projects around the world. This software architecture style has gained prominence among web-scale startups and enterprises.

This paper describes how micro services related to SBA and XAP. We will review the different principles micro services lays out and describe how these fit into the SBA model.

# What is SBA?
The Space based architecture is a software architecture pattern for achieving linear scalability of stateful, high-performance applications using the tuple space paradigm. It follows many of the principles of representational state transfer (REST), service-oriented architecture (SOA) and event-driven architecture (EDA), as well as elements of grid computing. With a space-based architecture, applications are built out of a set of self-sufficient units, known as processing-units (PU) or elastic processing-units (EPU). These units are independent of each other, so that the application can scale by adding more units (called also instances).

The XAP PU
The XAP PU is the unit of scalability and failover. The PU support Java , .Net and C++. It includes the definition of artifacts the user would like to have common lifecycle. These may have some dependency. For Java based PU such dependency would be descried via Spring IoC. XAP allows you to deploy multiple independent PU's into the same grid or deploy a composition of PUs that have some inter-dependency. In such a case , XAP will orchestrate the PUs provisioning to have the right deploy , healing and scaling order.

# Separation of Concerns
XAP PU promotes developing and deploying applications composed of autonomous, self-contained units - very similar to micro service architecture. The PU concept provides functional separation of concern , where the deployed PUs are mostly independent , each having its own lifecycle , scaling behavior while exposing well known public interface via remote methods or a web service.

As micro services , the PUs are fine-grained units of execution. They are designed to do one thing very well. A PU may encapsulate a web service (Web PU) that expose a well defined functionality with a well-known entry point - packaged as one unit of execution. It may also have a space instance (in- memory data grid node) , this will make it a stateful PU, where it can preserve its state. A stateful PU usually deployed with a backup instance. This makes it highly-available to survive failures without any data loss.

Logical separation of layers within a PU is fine, however, is not highly advised keeping all interaction of the business logic locally. A PU as a micro service should adhere being atomic business entity, which must implement everything to achieve the desired business functionality. For optimized cross micro services communication map-reduce approach should be used.

# Cloud Deployment
An application deployed on a cloud platform, when handling a cloud instance failure, produces higher operational costs and system design complexities. This leads to a general lack of confidence in cloud platforms, and eventual disagreement among the technical team when considering the application for cloud deployment. To deal with these symptoms, we need a more modular system that is loosely coupled, and can be changed or maintained in parts, to prevent downtime during failures. 

Microservices deployment on a Cloud environment is very popular. As the Cloud is a very dynamic, sometimes unpredicted environment, deploying self-sufficient components as micro services becomes an important ingredient for a healthy system. XAP inherently delivering this functionality as each PU includes all the 3rd party components and resources it need to be self-sufficient. XAP, together with its [Cloudify](http://getcloudify.org) blueprint , can be deployed on any Cloud environment, leveraging the Cloud ability to provision compute resources on-demand. This allow XAP to deliver continuous-availability with zero-downtime for the deployed PU where Cloudify handles the full automation of the PU lifecycle. This provides the system true consistency, durability and scalability.

# Communication
Micro services communicate with each other through language and platform-agnostic application programming interfaces (APIs). With XAP , micro services may use the following options:
- Proactive communication - In this case micro services sharing their state by writing it into the space. This data is available for other micro services by proactively reading it. For faster data sharing , micro services can run a [client side cache]({{%latestjavaurl%}}/client-side-caching.html) that will store frequently used data.
- Reactive communication - In this case micro services [registering]({{%latestjavaurl%}}/notify-container.html) for specific data they are interested with. When the desired data is written into the space by micro services, the registered micro services receiving notification with the interested data.

In both cases the micro services are is loosely coupled with each other avoiding synchronous and blocking-calls whenever possible.

# Configuration
Micro services configuration with XAP been inspired from popular SW concepts and libraries such as Jini , Rio, Spring , Hibernate , Tomcat , etc. For Java users a micro service will usually describe itself via spring based configuration file called [pu.xml]({{%latestjavaurl%}}/the-processing-unit-overview.html). It will compose Beans definition that will include the micro service internal artefacts and their dependency. The Beans will follow the regular life-cycle scheme Spring provides with few additional events provided with XAP such micro service (PU) creation and moving into [backup or primary mode]({{%latestjavaurl%}}/the-space-notifications.html).

# Web Services
XAP provides users the freedom to deploy different types of PUs. These are usually divided into stateless and stateful PUs. Stateless PU would be any PU that does not hold any state. It is using some other service (database , space, file) to hold its state. This could be a web PU , or a [remote Space service]({{%latestjavaurl%}}/space-based-remoting-overview.html) that are acting as a front-end to the users. Sateless PU can hold a local cache or local view that hold a copy of a state where its golden copy preserved within another stateful PU. A stateless PU usually deployed using a cluster topology without backups - as there is no state to maintain.

Stateful PU will be any PU that embeds a Space component , usually deployed with primary-backup cluster topology. It may hold user defined business logic such as [event handlers]({{%latestjavaurl%}}/event-processing.html) or other collocated logic called explicitly or implicitly.

Since most micro services implementations might have a front-end web service end-point , the Stateless PU running web container within XAP grid such as Jetty or Tomcat can be very useful.

# Processing
Data Processing with XAP comes in multiple flavors. All these support micro services very well. You may have reactive based processing or proactive one. Each may be deployed as a micro service. Reactive based processing implemented using the different event handlers - polling , notify and archive container. These are usually collocated with each micro service instance (stateful PU) for maximum performance and minimal network utilization that avoid expensive serialization.

The proactive processing usually performs map-reduce across the entire data grid nodes. This would invoke business logic across primary data grid nodes , where each return an intermediate result that is reduced and aggregated and passed to the caller.

# Partitioning
XAP Partitioning (routing) fits very well micro services. It is using implicitly the space class ID (key) by default to distribute the space objects across the different data grid partitions. Users can specify any other space class field as the routing field. This allows users to collocate different objects to reside within the same partition. This avoid extra remote calls when accessing multiple related space objects and the usage of distributed transactions.

Partitioning is fundamental to any stateful system and its the primary reason why XAP provides scalability when it comes to data access and data processing. You can always spread the load across additional/fewer machines. You may add more machine to run the grid manually or dynamically (EPU).

# Evolving Service Contracts
Micro services promotes evolving service contracts. This concept allows the micro service interface to be modified without any downtime. The PU support this via [hot deploy](/sbp/xap-hot-deploy.html) process. Compile and build your micro services with the newly added interfaces and run the hot deploy tool. This will use waterfall approach where backup instances will be updated , switched to primary mode and later the new backups that were previously primary will be upgraded as well. When this process completed all the instances of a deployed PU will be refreshed running latest business logic.

# Global HTTP Session
Micro services in its base is about breaking the application into manageable self-sufficient services. With large scale web applications many times users getting into a point that their monolithic application has been broken down into multiple logical components as the effort to test the entire web application with every change is prohibitive.

For Java application (J2E) this means multiple different ear or war files that need to be deployed where all need to share the same HTTP session. This means the HTTP session should be replicated across all these different deployed components. Usually these are running on different JVMs and different machines - sometimes deployed across different web containers from different vendors . Many times the HTTP session has not been designed to support replication as it is not serializable or it is bound to some external resource such as a database. Making it distributed shared across multiple parties without modifying the application is a major burden. XAP Global HTTP Session addressing all these issues - it can handle non-serializable content preserved within the HTTP session.

# Containerization
Operating system-level virtualization called also Container technology such as Docker, Rocket and LXD offer portability of code across multiple environments. Developers are able to move code written on their development machines seamlessly across virtual machines, private cloud and public cloud. Each running container provides everything from an operating system to the code responsible for executing a task. XAP , as a JVM based technology offer its own container - called GSC to act as the runtime environment of the deployed PU. XAP GSC does include the operating system, but the PU lifecycle management infrastructure. XAP does support multi-tenancy allowing multiple PUs to be provisioned within the same GSC sharing the same JVM resources or complete isolation having a dedicated GSC per PU instance.

# Unit Testing
Micro services application , like any other application should have for every service that you deliver a test suite. It should cover all the service functionality, security, performance, error handling, and consumption driven testing for every current and future consumer. This must be included as part of the build pipeline for automated regression testing. XAP PU can have [unit tests](/xap110/installation-eclipse-junit.html) developed that will mimic any data grid cluster topology leveraging the same pu.xml used when deploying the PU into production environment.

The [IPUC]({{%latestjavaurl%}}/running-and-debugging-within-your-ide.html) for Java users has been designed allow users running their PU within their IDE and also to be used with Unit Tests. This allow users to simulate a complete PU (specific PU instance or a complete cluster) to be provisioned within the single JVM. When testing micro services based application this makes the Unit testing very simple.

The [XAP Maven Plugin]({{%latestjavaurl%}}/maven-plugin.html) provides similar functionality as it allow users to build , deploy and run a PU.

# Gateway
With micro services , [API gateway](http://microservices.io/patterns/apigateway.html) used to orchestrate the cross-functional micro services that may reduce round trips for web applications.

API gateway is primarily used to Insulates the clients from how the application is partitioned into micro services and to Insulates the clients from the problem of determining the locations of service instances. It can also Reduces the number of requests/roundtrips. For example, the API gateway enables clients to retrieve data from multiple services with a single round-trip. Fewer requests also means less overhead and improves the user experience.

WIth XAP an API gateway will be implemented as a [web PU]({{%latestjavatuturl%}}/java-tutorial-part8.html) exposing Rest API deployed into XAP. You may use Jetty or Tomcat as the web container that will be running within the XAP Grid. As any PU , the web PU can scale dynamically to increase/decrease its capacity in runtime without any downtime.

# Best-of-Breed
Micro service promotes developers to choose best-of-breed languages, frameworks and tools to write parts of applications. XAP provides developers ability to deploy .Net , Java , Groovy, Scala and C++ PUs into the same grid and share data between them without any special conversion. This allow each micro service to be written in a language that is best suited for the task.

Design for Failure
Due-to the nature of micro services application deployment running in a distributed configuration usually on virtualized private/public clout - they should be designed to address failure. XAP includes multiple components that are will provide deployed stateless or stateful micro services ability to tolerate failure: These are Event handlers exception handlers , transactions , active-election and split brain solution

# Reentrancy
Micro services should support Reentrancy. This means it can be interrupted in the middle of its execution and then safely called again ("re-entered") before its previous invocations complete execution. With XAP you should use [transaction semantic]({{%latestjavaurl%}}/the-space-transactions.html)  with the business logic that interacts with the data grid. Since XAP support spring transaction framework , the effort executing transactional logic is very minimal. Simply annotate the relevant method or class with the standard @Transactional , inject the XAP transaction manager and you are done.

# Runtime Governance
Services need to be architected assuming that change is inevitable.  Have a strategy to manage the forward compatible service changes and allow your consumers to upgrade gracefully. Still, you should have the ability to deploy newer versions with no downtime. The [hot deploy](http://docs.gigaspaces.com/sbp/xap-hot-deploy.html) utility will allow you to refresh existing running PU that includes your business logic with a new version without undeploying   the PU. For a statefull PU this is critical as it allows you to update any collocated business logic (i.e. polling / notify container) without any downtime to the data grid.

# Building a gateway in every service
To avoid tangling and over-complicated multi-hierarchy inter-communication between front-end web service layer and the different micro services, the space should be used as a common shared memory and communication fabric the different micro services should be utilizing.

You may collocate the space with the micro service that perform the most intensive interaction with the space and let the other micro services use it via remote communication. This is very common approach. Another approach would be to have a dedicated micro service that will run the space only , and satellite micro services that may have local cache / local view that maintain a copy of the shared data.

# Persistence Layer
Micro services promotes polyglot strategy for code and databases. With XAP you can have dedicated space for each micro service - usually having the heavy lifting business logic performed by the micro service collocated with the space itself. These different micro services can leverage a different database for persistence or share the same database. This makes the database administration effort simpler.

With XAP , you will have persistence configured usually using write-behind approach (aka Mirror Service). This write-behind activity would allow the application to operate even if the underlying database is slow or even if the database failed or going under maintenance activity. Once the database will be on-line , all the operation that were logged will be propagated to the database.

# Resource Control
Thread pool and connection pool management are required with any distributed web scale architecture. XAP provides (client and server side configuration)[LRMI protocol]({{%latestadmurl%}}/tuning-communication-protocol.html) that allows you to set boundary for amount of resources a client or server side components should use. Since micro services can be both a client or a server side component such resource control settings are very useful.

Synchronous calls considered harmful
Micro services promote the usage of asynchronous calls. With SBA these should be implemented via the various asynchronous data access API provided: async Notifications , async Task execution , async remote method invocation , Async Write , Async read , Async take and Async clear. These allow your application to leverage non-blocking architecture when interacting with the space.

# Elasticity
Micro services by definitions should support elasticity. This means the capacity of the micro service to handle incoming requests or data to store should grow or shrink without any downtime. This can happen automatically based on pre-defined SLA or manually. XAP support this capability both for stateful and stateless PU.

# High-Availability
With micro services , the culture and design principles should embrace failure and faults, similar to anti-fragile systems. Developers and operators can develop and deploy self-healing applications.  XAP delivers high-availability by keeping every item stored within the data grid within at least two different machines. This redundancy will be kept as long as there are available resources on the network. Upon failure of a data grid node a new PU instance running the missing data grid node will be provisioned, keeping the capacity of the deployed stateful PU intact.

# Semantic Monitoring
Semantic monitoring can provide an early warning system of something going wrong that triggers development teams to follow up and investigate. XAP includes advanced monitoring and alerting tools that may introspect any component within the system. This include the grid and its hosting machines , the PU and its stateful (space) or stateless artefacts. You may configure custom dashboards that will allow you to correlate different metrics.  These may be both operational and business relevant metrics. This provides simple resolution path when problems are identified. You may also push metrics available into external monitoring tools such as CA APM to enterprise level monitoring.

# Consistency
Micro service architectures emphasize transaction-less coordination between services, with explicit recognition that consistency may only be eventual consistency and problems are dealt with by compensating operations. With XAP the recommendation is actually to leverage transactions as XAP has been built to support strict consistency. You can use eventual consistency with XAP (asynchronous replication , optimistic locking protocol) but this may impact accuracy and may lead to data loss.

# Conclusion
Developing and deploying micro services with XAP should adhere to several straightforward concepts. Many of these are classic concepts that does not require special changes to the regular development and deployment cycle you may have. Sticking to these concepts will deliver top quality , great end-user experience , an agile , cost effective , robust , scalable and simple to maintain application.

