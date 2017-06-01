---
type: post121
title:  Other Data Access APIs
categories: XAP121
parent: none
weight: 1200
---


XAP promotes openness and interoperability, supporting common interfaces for accessing data, such as JPA, Document, SQL, JDBC, Memcached, Spring Data and a native POJO based API. You can choose the best API for the case at hand, and be able to operate on the same data regardless of the APIs you have chosen. Look at the different API pages to learn about each and every use case for data accessing on XAP.



<hr>

{{%align center%}}
![anyapi.png](/attachment_files/anyapi.png)
{{%/align%}}

<br>

{{%fpanel%}}
[JDBC Driver](./jdbc-driver.html){{<wbr>}}
XAP allows applications to connect to the IMDG using a JDBC driver. AN XAP JDBC driver accepts SQL statements, translates them to space operations, and returns standard result sets.

[JPA](./jpa-api-overview.html){{<wbr>}}
XAP's JPA allows you to use JPA's functionality, annotations and execute JPQL queries on Space.

[MAP API](./map-api.html){{<wbr>}}
XAP allows applications to interact with the space and cache data using the Map API (JCache) or a HashTable API.

[memcached API](./memcached-api.html){{<wbr>}}
The memcached support is provided as a template deployment (similar to how basic data grid deployment works), allowing to easily issue commands to deploy a memcached cluster

[Spring Data](/sbp/spring-data.html){{<wbr>}}
XAP Spring Data implements the [Spring Data Framework](http://projects.spring.io/spring-data) that lets you use the concepts to the development of applications using XAP's In-Memory Data Grid (IMDG) as the data store.

{{%/fpanel%}}
