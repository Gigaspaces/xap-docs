---
type: post97
title:  Development
categories: XAP97NET
parent: none
weight: 200
---




This guide is written for application developers who wish to build scaled-out applications with GigaSpaces XAP. The guide provides all the information and tools needed to quickly develop either basic or more complex scaled-out applications. This includes reference documentation, foundations, APIs, configurations and more advanced topics.


<hr/>

- [PONO Support](./poco-overview.html){{<wbr>}}
Understanding the semantics of Space Entries and .NET Objects

- [Document API](./document-overview.html){{<wbr>}}
The XAP document API exposes the space as Document Store. A document, which is represented by the class SpaceDocument, is essentially collection of key-value pairs, where the keys are strings and the values are primitives, String, Date, other documents, or collections thereof.


- [Modeling your Data](./modeling-your-data.html){{<wbr>}}
Modeling your objects that are used to interact with the space.

- [The Space Interface](./the-gigaspace-interface-overview.html){{<wbr>}}
This section includes information about the different APIs for accessing and manipulating the Space data


- [Querying the Space](./querying-the-space.html){{<wbr>}}
This section explains the various mechanisms offered by GigaSpaces XAP to query the space for data, as well as related topics, such as how to use indexing to boost query performance and how the space can be iterated to fetch entries more efficiently.


- [Indexing](./indexing-overview.html){{<wbr>}}
This section explains the various mechanisms offered by GigaSpaces XAP to query the space for data, as well as related topics, such as how to use indexing to boost query performance and how the space can be iterated to fetch entries more efficiently.


- [FIFO Support](./fifo-overview.html){{<wbr>}}
Supporting FIFO (First In, First Out) behavior for Entries is a critical requirement when building messaging systems or implementing master-worker patterns. Users should be able to get Entries in the same order in which they were written. GigaSpaces supports both non-ordered Entries and FIFO ordered Entries when performing space operations.


- [Transactions](./transaction-overview.html){{<wbr>}}
XAP .Net provides an explicit transaction management programing model. It allows developers to easily write transactional code with the lowest overhead possible while minimizing the amount of hand-crafted code and separating it from the application hosting environment and instance management.

- [Space Persistence](./space-persistency-overview.html){{<wbr>}}
Using the GigaSpaces External Data Source interface to persist data stored in the space

- [Client Side Caching](./client-side-caching.html){{<wbr>}}
Using the GigaSpaces External Data Source interface to persist data stored in the space

- [Task Execution](./task-execution-over-the-space.html){{<wbr>}}
XAP support executing tasks in a collocated Space (processing unit that started an embedded Space). Space tasks can be executed either directly on a specific cluster member using typical routing value.

- [Event Processing](./event-processing.html){{<wbr>}}
This section will guide you through event processing APIs and configuration on top of the space.

- [Space Based Remoting](./space-based-remoting.html){{<wbr>}}
Remoting allows you to use remote invocations of PONO services, with the space as the transport layer.

- [Admininstration](./admin-modules.html){{<wbr>}}
The Admin API provides a way to administer and monitor all of XAP services and components using a simple API. The API provides information and the ability to operate on the currently running GigaSpaces Agent, GigaSpaces Manager, GigaSpaces Container, Lookup Service, Processing Unit and Spaces.

<hr/>













