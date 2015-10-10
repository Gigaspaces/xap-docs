---
type: post110net
title:  .Net Developers Guide
categories: XAP110NET
---



{{%section%}}
{{%column width="10%" %}}
<img src="/attachment_files/subject/imc.png" width="80" height="80">
{{%/column%}}
{{%column width="90%" %}}
This guide introduces the key concepts for building XAP applications. The topics in this guide span installation, tutorials, the XAP API, Processing Unit and platform interoperability.
{{%/column%}}
{{%/section%}}

<br>

{{%fpanel%}}

[Installation](./installation.html){{<wbr>}}
This guide provides step by step instructions on how to download and install XAP on different operating systems for the supported languages.

[PONO Support](./poco-overview.html){{<wbr>}}
Understanding the semantics of Space Entries and .NET Objects

[Document API](./document-overview.html){{<wbr>}}
The XAP document API exposes the space as Document Store. A document, which is represented by the class SpaceDocument, is essentially collection of key-value pairs, where the keys are strings and the values are primitives, String, Date, other documents, or collections thereof.

[Modeling your Data](./modeling-your-data.html){{<wbr>}}
Modeling your objects that are used to interact with the space.

[The Space Interface](./the-gigaspace-interface-overview.html){{<wbr>}}
This section includes information about the different APIs for accessing and manipulating the Space data

[Querying the Space](./querying-the-space.html){{<wbr>}}
This section explains the various mechanisms offered by GigaSpaces XAP to query the space for data, as well as related topics, such as how to use indexing to boost query performance and how the space can be iterated to fetch entries more efficiently.

[Indexing](./indexing-overview.html){{<wbr>}}
This section explains the various mechanisms offered by GigaSpaces XAP to query the space for data, as well as related topics, such as how to use indexing to boost query performance and how the space can be iterated to fetch entries more efficiently.

[FIFO Support](./fifo-overview.html){{<wbr>}}
Supporting FIFO (First In, First Out) behavior for Entries is a critical requirement when building messaging systems or implementing master-worker patterns. Users should be able to get Entries in the same order in which they were written. GigaSpaces supports both non-ordered Entries and FIFO ordered Entries when performing space operations.

[Transactions](./transaction-overview.html){{<wbr>}}
XAP .Net provides an explicit transaction management programing model. It allows developers to easily write transactional code with the lowest overhead possible while minimizing the amount of hand-crafted code and separating it from the application hosting environment and instance management.

[Space Persistence](./space-persistency-overview.html){{<wbr>}}
Using the GigaSpaces External Data Source interface to persist data stored in the space

[Client Side Caching](./client-side-caching.html){{<wbr>}}
Using the GigaSpaces External Data Source interface to persist data stored in the space

[Task Execution](./task-execution-over-the-space.html){{<wbr>}}
XAP support executing tasks in a collocated Space (processing unit that started an embedded Space). Space tasks can be executed either directly on a specific cluster member using typical routing value.

[Event Processing](./event-processing.html){{<wbr>}}
This section will guide you through event processing APIs and configuration on top of the space.

[Space Based Remoting](./space-based-remoting-overview.html){{<wbr>}}
Remoting allows you to use remote invocations of PONO services, with the space as the transport layer.

[Admininstration API](./admin-modules.html){{<wbr>}}
The Admin API provides a way to administer and monitor all of XAP services and components using a simple API. The API provides information and the ability to operate on the currently running GigaSpaces Agent, GigaSpaces Manager, GigaSpaces Container, Lookup Service, Processing Unit and Spaces.

[The Processing Unit](./the-processing-unit-overview.html){{<wbr>}}
The Processing Unit is the unit of packaging and deployment in the GigaSpaces XAP platform. This section describes its directory structure, deployment descriptor and SLA definition and configuration.

[Deployment and Administration](./administrators-guide.html){{<wbr>}}
The primary focus of the Administrator's Guide, is to provide both a basic, as well as more advanced overview of the GigaSpaces XAP administration and operation components.

[Platform Interoperability](./interoperability.html){{<wbr>}}
The possibility for organizations whose projects include a combination of Java, .NET and C++ platforms to communicate and access each other easily and efficiently while also maintaining the benefits of the XAP scale-out application server.

{{%/fpanel%}}

