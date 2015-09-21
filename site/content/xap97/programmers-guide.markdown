---
type: post97
title:  Development
categories: XAP97
weight: 200
parent: none
---

{{%wbr%}}

{{%section%}}
{{%column width="10%" %}}
![data-access.jpg](/attachment_files/subject/data-access.png)
{{%/column%}}
{{%column width="90%" %}}
This guide is written for application developers who wish to build scaled-out applications with GigaSpaces XAP. The guide provides all the information and tools needed to quickly develop either basic or more complex scaled-out applications. This includes reference documentation, foundations, APIs, configurations and more advanced topics.
{{%/column%}}
{{%/section%}}


<hr/>


- [POJO as space object](./pojo-overview.html){{%wbr%}}
XAP's POJO support allows you to use JavaBean classes as space domain classes, and perform space operations using these objects. POJO domain Classes should follow rules similar to the ones defined by JPA, Hibernate and other domain class frameworks.


- [Space Document](./document-overview.html){{%wbr%}}
The XAP document API exposes the space as Document Store. A document, which is represented by the class SpaceDocument, is essentially collection of key-value pairs.

- [Modeling your Data](./modeling-your-data.html){{%wbr%}}
Modeling your objects that are used to interact with the space.

- [The Space Interface](./the-gigaspace-interface-overview.html){{%wbr%}}
In this section we will show you how to create and configure a Space and how to interact with it with the Space API.

- [Querying the Space](./querying-the-space.html){{%wbr%}}
This section explains the various mechanisms offered by GigaSpaces XAP to query the space for data.

- [Indexing](./indexing-overview.html){{%wbr%}}
To improve performance, it is possible to index one or more properties.

- [Transaction Management](./transaction-overview.html){{%wbr%}}
Transaction concept and API.

- [Space Persistency](./space-persistency-overview.html){{%wbr%}}
XAP provides advanced persistency capabilities for the space architecture.

- [Other Data Access API's](./other-data-access-apis.html){{%wbr%}}
XAP promotes openness and interoperability, supporting common interfaces for accessing data, such as JPA, Document, SQL, JDBC, Memcached and a native POJO based API.

- [Client Side Caching](./client-side-caching.html){{%wbr%}}
XAP supports client side caching of space data within the client application's JVM.

- [Task Execution](./task-execution-overview.html){{%wbr%}}
Task executors allow you to easily execute grid-wide tasks on the space using the XAP API.

- [Event Processing](./event-processing.html){{%wbr%}}
Event processing on top of the space.

- [Space Based Remoting](./space-based-remoting-overview.html){{%wbr%}}
Remoting allows you to use remote invocations of POJO services, with the space as the transport layer.

- [Messaging](./messaging-support.html){{%wbr%}}
XAP provide a JMS implementation, built on top of the core spaces architecture.

- [Interoperability](./interoperability-overview.html){{%wbr%}}
Interoperability between Java, .Net, cpp, REST.

- [Mule ESB Integration](./mule-esb.html){{%wbr%}}
XAP  support for Mule ESB.

- [Big Data Integration](./big-data.html){{%wbr%}}
XAP integration with Cassandra and MongoDB NoSql data bases.

- [Logging](./logging-api.html){{%wbr%}}
Configuring logging.

- [Administration API](./administration-and-monitoring-overview.html){{%wbr%}}
The Admin API provides a way to administer and monitor all of XAP services and components using a simple API.

<hr/>

