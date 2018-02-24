---
type: post123
title:  Developer Guide
categories: XAP123
weight: 400
---




This guide introduces the key concepts for building InsightEdge and XAP applications. The topics in this guide span installation, tutorials, the XAP API, Processing Unit, web application support, Space replication, platform interoperability and security.



<br>

{{% fpanel %}}

[POJO Support](./pojo-overview.html)<br>
XAP POJO support allows you to use JavaBean classes as space domain classes, and perform space operations using these objects. POJO domain Classes should follow rules similar to the ones defined by JPA, Hibernate and other domain class frameworks.


[Space Document](./document-overview.html)<br>
The XAP document API exposes the space as Document Store. A document, which is represented by the class SpaceDocument, is essentially collection of key-value pairs.

[XAP Data Modeling](./modeling-your-data.html)<br>
Modeling your objects that are used to interact with the space.

[The Space Interface](./the-gigaspace-interface-overview.html)<br>
In this section we will show you how to create and configure a Space and how to interact with it with the Space API.

[Querying the Space](./querying-the-space.html)<br>
This section explains the various mechanisms offered by GigaSpaces XAP to query the space for data.

[Indexing](./indexing-overview.html)<br>
To improve performance, it is possible to index one or more properties.

[Transactions](./transaction-overview.html)<br>
Transaction concept and API.

[Space Persistency](./space-persistency-overview.html)<br>
XAP provides advanced persistency capabilities for the space architecture.

[Memory Management](./memory-management-overview.html)<br>
The Memory Management facility is used to prevent situations where a Space server gets into an out-of-memory failure scenario.


[Other Data Access APIs](./other-data-access-apis.html)<br>
XAP promotes openness and interoperability, supporting common interfaces for accessing data, such as JPA, Document, SQL, JDBC, Memcached and a native POJO based API.

[Client-Side Caching](./client-side-caching.html)<br>
XAP supports client side caching of space data within the client application's JVM.

[Task Execution](./task-execution-overview.html)<br>
Task executors allow you to easily execute grid-wide tasks on the space using the XAP API.

[Event Processing](./event-processing.html)<br>
Event processing on top of the space.

[Space-Based Remoting](./space-based-remoting-overview.html)<br>
Remoting allows you to use remote invocations of POJO services, with the space as the transport layer.


[Mule ESB Integration](./mule-esb.html)<br>
XAP  support for Mule ESB.
 
[Admin API](./administration-and-monitoring-overview.html)<br>
The Admin API provides a way to administer and monitor all of XAP services and components using a simple API.

[The Processing Unit](./the-processing-unit-overview.html)<br>
The Processing Unit is the unit of packaging and deployment in the GigaSpaces XAP platform. This section describes its directory structure, deployment descriptor and SLA definition and configuration.

[Elastic Processing Unit](./elastic-processing-unit-overview.html)<br>
How to deploy and manage an Elastic Processing Unit (EPU).

[Web Application Support](./web-application-overview.html)<br>
XAP's integration with the Service Grid allows you to deploy web applications (packaged as a WAR file) onto the Service Grid.
 

[Space Replication](./multi-space-replication-overview.html)<br>
Multiple space replication is the ability to replicate state between different deployed spaces, i.e different cluster of space instances.

[Multi-Site LAN Replication](./multi-site-replication-overview.html)<br>
Describes how to replicate state and establish data synchronization between multiple spaces that have direct network communication between them.

[Multi-Site WAN Replication](./multi-site-replication-overview.html)<br>
Describes how to replicate state between different deployed spaces and synchronize data, where each space may be also physically located in a different geographical location.

[Platform Interoperability](./interoperability-overview.html)<br>
The possibility for organizations whose projects include a combination of Java, .NET and other platforms to communicate and access each other easily and efficiently while also maintaining the benefits of the XAP scale-out application server.


[InsightEdge APIs](./insightedge-apis.html)<br>
Description of APIs that are part of the InsightEdge engine.

{{%/fpanel%}}

