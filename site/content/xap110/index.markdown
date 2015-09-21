---
type: post110
title:  Java Developers Guide
categories: XAP110
---

{{%wbr%}}

{{%bannerleft "/attachment_files/subject/imc.png" %}}
This guide introduces the key concepts for building XAP applications. The topics in this guide span installation, tutorials, the XAP API, Processing Unit, web application support, Space replication, platform interoperability and security.
{{%/bannerleft%}}


{{%wbr%}}

{{% fpanel %}}
[Installation](./installation.html){{%wbr%}}
This guide provides step by step instructions on how to download and install XAP on different operating systems for the supported languages.

[POJO as space object](./pojo-overview.html){{%wbr%}}
XAP POJO support allows you to use JavaBean classes as space domain classes, and perform space operations using these objects. POJO domain Classes should follow rules similar to the ones defined by JPA, Hibernate and other domain class frameworks.


[Space Document](./document-overview.html){{%wbr%}}
The XAP document API exposes the space as Document Store. A document, which is represented by the class SpaceDocument, is essentially collection of key-value pairs.

[Modeling your Data](./modeling-your-data.html){{%wbr%}}
Modeling your objects that are used to interact with the space.

[The Space Interface](./the-gigaspace-interface-overview.html){{%wbr%}}
In this section we will show you how to create and configure a Space and how to interact with it with the Space API.

[Querying the Space](./querying-the-space.html){{%wbr%}}
This section explains the various mechanisms offered by GigaSpaces XAP to query the space for data.

[Indexing](./indexing-overview.html){{%wbr%}}
To improve performance, it is possible to index one or more properties.

[Transaction Management](./transaction-overview.html){{%wbr%}}
Transaction concept and API.

[Space Persistency](./space-persistency-overview.html){{%wbr%}}
XAP provides advanced persistency capabilities for the space architecture.

[Other Data Access API's](./other-data-access-apis.html){{%wbr%}}
XAP promotes openness and interoperability, supporting common interfaces for accessing data, such as JPA, Document, SQL, JDBC, Memcached and a native POJO based API.

[Client Side Caching](./client-side-caching.html){{%wbr%}}
XAP supports client side caching of space data within the client application's JVM.

[Task Execution](./task-execution-overview.html){{%wbr%}}
Task executors allow you to easily execute grid-wide tasks on the space using the XAP API.

[Event Processing](./event-processing.html){{%wbr%}}
Event processing on top of the space.

[Space Based Remoting](./space-based-remoting-overview.html){{%wbr%}}
Remoting allows you to use remote invocations of POJO services, with the space as the transport layer.


[Mule ESB Integration](./mule-esb.html){{%wbr%}}
XAP  support for Mule ESB.

[Big Data Integration](./big-data.html){{%wbr%}}
XAP integration with Cassandra and MongoDB NoSql data bases.

[Administration API](./administration-and-monitoring-overview.html){{%wbr%}}
The Admin API provides a way to administer and monitor all of XAP services and components using a simple API.

[The Processing Unit](./the-processing-unit-overview.html){{%wbr%}}
The Processing Unit is the unit of packaging and deployment in the GigaSpaces XAP platform. This section describes its directory structure, deployment descriptor and SLA definition and configuration.

[Elastic Processing Unit](./elastic-processing-unit-overview.html){{%wbr%}}
How to deploy and manage an Elastic Processing Unit (EPU).

[Web Application Support](./web-application-overview.html){{%wbr%}}
XAP's integration with the Service Grid allows you to deploy web applications (packaged as a WAR file) onto the Service Grid.

[Global HTTP Session Sharing](./global-http-session-sharing-overview.html){{%wbr%}}
Global HTTP Session Sharing allows transparent session replication between remote sites and session sharing between different application servers in real-time. The solution uses the Shiro Session Manager library


[Space Replication](./multi-space-replication-overview.html){{%wbr%}}
Multiple space replication is the ability to replicate state between different deployed spaces, i.e different cluster of space instances.

[Multi Site Replication](./multi-site-replication-overview.html){{%wbr%}}
Multiple site replication is the ability to replicate state between different deployed spaces, where each space can be also physically located in a different geographical location (also termed a different deployment site).

[Platform Interoperability](./interoperability-overview.html){{%wbr%}}
The possibility for organizations whose projects include a combination of Java, .NET and C++ platforms to communicate and access each other easily and efficiently while also maintaining the benefits of the XAP scale-out application server.

{{%/fpanel%}}

