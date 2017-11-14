---
type: post123
title:  Cassandra Integration
categories: XAP123, OSS
parent: space-persistency-overview.html
weight: 620
---

{{%warning "Important"%}}
This is a reference implementation of integrating Cassandra with XAP for persistency and data reload. 
GigaSpaces does not guarantee up-to-date upgrades or support of the target NoSQL store drivers. 
We encourage users to extend the {{%giturl "open source implementation" "https://github.com/Gigaspaces/xap-cassandra"%}}to their liking or contact GigaSpaces Professional Services for guidance at [Consulting Services](mailto:ps@gigaspaces.com).
{{%/warning%}}


The {{%exurl "Apache Cassandra Project" "http://cassandra.apache.org"%}} is a scalable multi-master database with no single points of failure. The Apache Cassandra Project develops a highly scalable second-generation distributed database, bringing together Dynamo's fully distributed design and Bigtable's ColumnFamily-based data model.



Cassandra is in use at Digg, Facebook, Twitter, Reddit, Rackspace, Cloudkick, Cisco, SimpleGeo, Ooyala, OpenX, and more companies that have large, active data sets. The largest production cluster has over 100 TB of data in over 150 machines. Data is automatically replicated to multiple nodes for fault-tolerance. Replication across multiple data centers is supported. Failed nodes can be replaced with no downtime. Every node in the cluster is identical. There are no network bottlenecks. There are no single points of failure.



