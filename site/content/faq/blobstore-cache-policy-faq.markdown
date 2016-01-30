---
type: faq
title:  Flash Drive Storage
categories: FAQ
parent: none
weight: 1000

---




{{%panel%}}

- [Does XAP MemoryXtend Support all XAP APIs?](#1)

- [Can XAP MemoryXtend be used with a Data Grid collocating application code?](#2)

- [Does XAP MemoryXtend supported with all JVMs?](#3)

- [Does XAP MemoryXtend support transactions?](#4)

- [Does XAP MemoryXtend support SQL based data access?](#5)

- [What's the performance difference of MemoryXtend for SSD vs pure RAM data grid configuration?](#6)

- [Can I deploy any data grid topology with XAP MemoryXtend?](#7)

- [Can I implement an hybrid configuration to use both RAM and SSD for the data grid memory space?](#8)

- [Does XAP local (near) client side cache leverage SSD?](#9)

- [Can I use XAP MemoryXtend with any operating system?](#10)

- [Can I use MemoryXtend on the Cloud?](#11)

- [Can I use XAP MemoryXtend with Cloudify?](#12)


{{%/panel%}}

{{%anchor 1%}}

### Does XAP MemoryXtend Support all XAP APIs?
Yes. All XAP Data Grid API are supported with XAP SSD. This includes single `write` operations , batch `write` operations , update , batch update , delta update (change) , batch delta update , single object read, batch read (readMultiple), SQL based data query. `readById` (single key lookup) , `readByIds` (multi keys lookup) , notification and paging API. XAP SSD fully support data grid distributed transactions. XAP SSD implemented using the new Storage API. The `SpaceDataSource` and `SyncendPoint` persistence API does not support SSD in this point.

{{%anchor 2%}}

### Can XAP MemoryXtend be used with a Data Grid collocating application code?
Absolutely. XAP is application platform with a universal IMDG. It is highly recommended deployment model collocating the business logic with the data grid. This allows the application to enjoy extreme low latency accessing and manipulating the data. Data processing scenarios such as real-time analytics would benefit this deployment model as these need to access large amount of data very quickly or perform comprehensive data manipulations on numerical data. Performing such activities from collocated code would avoid serialization and network overhead. 

{{%anchor 3%}}

### Does XAP MemoryXtend supported with all JVMs?
XAP MemoryXtend support Oracle JDK 1.6 and JDK 1.7. 

{{%anchor 4%}}

### Does XAP MemoryXtend support transactions?
Yes. Distributed transactions and all common transaction isolation supported including XA transaction.

{{%anchor 5%}}

### Does XAP MemoryXtend support SQL based data access?
Yes. You may execute any SQL Query with XAP MemoryXtend. The indexes maintain in RAM allowing the Data grid query engine to evaluate the query without accessing the raw data stored on the SSD. This allows XAP MemoryXtend to execute SQL based queries extremely efficiently even across large number of nodes.

{{%anchor 6%}}

### What's the performance difference of MemoryXtend for SSD vs pure RAM data grid configuration?
Pure RAM data grid is about 2.5 faster with write operations and 4.5 faster on read operations. Still, when looking on the price-performance scale SSD based data grid is about 3.6 better than pure RAM data grid on write operations and 2.1 better with read operations. The numbers provided based on tests with Fusion-IO.

{{%anchor 7%}}

### Can I deploy any data grid topology with XAP MemoryXtend?
Yes. Clustered Synchronous-replicated data grid , Asynchronous-replicated data grid, partitioned data grid and partitioned with Synchronous-replicated backup data grid are supported. Multi-data grid cluster setup across different geographies is also supported.

{{%anchor 8%}}

### Can I implement an hybrid configuration to use both RAM and SSD for the data grid memory space?
Yes. That's actually how XAP MemoryXtend for SSD works. Indexes are maintained in RAM where raw data is maintained on SSD with some first level RAM based cache used for frequently used data. You may configure the amount of RAM used to store cached data. Transactional operations keeping objects under transaction in RAM for optimized performance. 

{{%anchor 9%}}

### Does XAP local (near) client side cache leverage SSD?
No. Client side cache such as local cache and local view are purely RAM based cache instances and cannot store their data on SSD.

{{%anchor 10%}}

### Can I use XAP MemoryXtend with any operating system?
XAP MemoryXtend for SSD support Linux CentOS 5.8. Windows OS will be supported in the future.

{{%anchor 11%}}

### Can I use MemoryXtend on the Cloud?
Yes. Any Cloud that support Images with SSD storage is supported. This includes all AWS EC2, RackSpace, and IBM Softlayer images with SSD storage configuration. Due-to the elastic nature of XAP and Cloudify orchestration capabilities, running XAP MemoryXtend on the Cloud is highly recommended. 

{{%anchor 12%}}

### Can I use XAP MemoryXtend with Cloudify?
Yes. XAP is fully supported with Cloudify allowing you to scale XAP over SSD dynamically with private cloud, public cloud, physical or virtualized environments.



{{%refer%}}
[MemoryXtend]({{%latestadmurl%}}/memoryxtend.html)
{{%/refer%}}