---
type: post123
title:  MongoDB Integration
categories: XAP123, OSS
parent: space-persistency-overview.html
weight: 630
---

{{%warning "Important"%}}
This is a reference implementation of integrating MongoDB with XAP for persistency and data reload. GigaSpaces does not guarantee up-to-date upgrades or support of the target NoSQL store drivers. We encourage users to extend the open source implementation to their liking or contact GigaSpaces Professional Services for guidance at [Consulting Services](mailto:ps@gigaspaces.com). 
{{%/warning%}} 

[MongoDB](http://www.mongodb.com/) is an open-source database used by companies of all sizes, across all industries and for a wide variety of applications. It is an agile database that allows schemas to change quickly as applications evolve, while still providing the functionality developers expect from traditional databases, such as secondary indexes, a full query language and strict consistency.

MongoDB is built for scalability, performance and high availability, scaling from single server deployments to large, complex multi-site architectures. By leveraging in-memory computing, MongoDB provides high performance for both reads and writes. MongoDB's native replication and automated failover enable enterprise-grade reliability and operational flexibility.


 

**Dependencies**<br>
In order to use this feature, include the `${XAP_HOME}/lib/optional/mongodb/xap-mongodb.jar` file on your classpath or use maven dependencies:

```xml
<dependency>
    <groupId>org.gigaspaces</groupId>
    <artifactId>xap-mongodb</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
```
{{%refer%}}
For more information on dependencies, refer to [Maven Artifacts](../started/maven-artifacts.html).
{{%/refer%}} 



