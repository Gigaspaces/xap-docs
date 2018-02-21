---
type: post123
title: Platform Components
categories: XAP123OVW
parent: none
weight: 100
---

 

{{% align center%}}
![archi_overview.jpg](/attachment_files/Product_Architecture1.png)
{{% /align %}}

# XAP In-Memory Data Grid

GigaSpaces XAP is the core in-memory data grid for the InsightEdge Platform. XAP is designed to support ultra-low latency and high-throughput applications that follow [Space-Based Architecture](./space-based-architecture.html) design patterns. XAP is based on GigaSpaces' Spring-based [OpenSpaces Framework](./product-architecture.html#product-architecture-openSpaces-api-and-components) as the primary development environment, and uses the GigaSpaces space-based runtime to deliver core middleware facilities: messaging, data caching and parallelization.

The following is a brief overview of some XAP data grid capabilities.

**Data Modeling**

- [POJO]({{% latestjavaurl%}}/pojo-overview.html)
- [Documents]({{% latestjavaurl%}}/document-api.html)
- [Key/Value]({{% latestjavaurl%}}/map-api.html)
- [Geospatial]({{% latestjavaurl%}}/indexing-geospatial.html)
- [Full Text]({{% latestjavaurl%}}/indexing-text-search.html)

**API Patterns**

- [Grid SQL]({{% latestjavaurl%}}/query-sql.html)
- [Event Processing]({{% latestjavaurl%}}/event-processing.html)
- [RPC and Remoting]({{% latestjavaurl%}}/space-based-remoting-overview.html)
- [Map/Reduce]({{% latestjavaurl%}}/task-execution-overview.html)
- [Aggregators]({{% latestjavaurl%}}/aggregators.html)
- [Transaction Management]({{% latestjavaurl%}}/transaction-overview.html)


**Memory and Storage**

- [RAM](../dev-java/memory-management-overview.html)
- [SSD/Flash]({{%currentadmurl%}}/memoryxtend-rocksdb-ssd.html)


**Data Replication and Persistence**

- [Multi-Data Center Replication]({{% latestjavaurl%}}/multi-site-replication-overview.html)
- [Persistency]({{% latestjavaurl%}}/space-persistency-overview.html)


Applications running on XAP can be scaled out linearly, because XAP uses the [Space-Based Architecture](./space-based-architecture.html) (SBA) as a primary design pattern. With SBA, applications are built out of a set of self-sufficient units, known as Processing Units (PU). These units are completely independent of each other, so that the application can scale indefinitely without increasing complexity, just by adding more units. SBA is based on the {{%exurl "Tuple Space""http://en.wikipedia.org/wiki/Tuple_space"%}} paradigm; it follows many of the principles of {{%exurl "Service-Oriented Architecture""http://en.wikipedia.org/wiki/Service-Oriented_Architecture"%}} and {{%exurl "Event-Driven Architecture""http://en.wikipedia.org/wiki/Event_Driven_Architecture"%}}, as well as elements of {{%exurl "grid computing""http://en.wikipedia.org/wiki/Grid_computing"%}}.

# Applications and Microservices

Applications deployed on XAP are very much like JEE distributed server-side applications. Each application is composed of one or more scalable modules, and contains one or more business logic code components, domain model objects (entries), in-memory data storage, messaging, and event processing code.

A module in SBA is called a [Processing Unit]({{% latestjavaurl%}}/the-processing-unit-overview.html). This is the smallest unit for cross-cluster scaling. From a technical standpoint, a Processing Unit is a collection of classes and a configuration file, packaged as a single unit of scale.

**Processing Unit**:

- [Java Processing Unit]({{% latestjavaurl%}}/the-processing-unit-overview.html) - has code components in Java, and may contain a space component.
- [.NET Processing Unit]({{% latestneturl%}}/the-processing-unit-overview.html) -  has its code components in .NET, and may contain a space component.
- [Web Processing Unit]({{% latestjavaurl%}}/web-application-overview.html) - contains web applications. This Processing Unit can be packaged as a standard JEE WAR (Web Archive) file, and may contain a space component.

A Processing Unit package structure is defined, based on the technology:

- A [Java Processing Unit]({{% latestjavaurl%}}/the-processing-unit-structure-and-configuration.html) is packaged usually as a JAR file, modeled after the Spring DI structure. The package can also be within a file structure based on the same structure.
- A .NET Processing Unit is packaged as a directory structure. For more information, refer to the [XAP.NET Guide]({{% latestneturl%}}/the-processing-unit-overview.html).
- A [Web Processing Unit]({{% latestjavaurl%}}/web-application-overview.html#deployment) is packaged as a standard JEE WAR file.

The services will usually operate on data that is stored within the same space partition, providing memory access within the same process address space. This mode of interaction allows the minimal latency possible, as data is accessed by reference, as opposed to serialization required for out-of-process communication.

Process units can be deployed through the GigaSpaces [Management Center]({{%currentadmurl%}}/gigaspaces-management-center.html), [Web UI]({{%currentadmurl%}}/web-management-console.html), [Command Line Interface]({{%currentadmurl%}}/deploy-command-line-interface.html) and [REST Management API]({{%currentadmurl%}}/xap-manager-rest.html).


# Analytics and Big Data
The InsightEdge Platform provides a first-class integration between Apache Spark and any data grid capability. This allows hybrid/transactional analytics processing by co-locating Spark jobs in-place with low-latency data grid applications. The platform includes a full Spark distribution that is managed by a highly-available clustering tier that provides auto-healing, as well as local and geographical redundancy. The following is a sample of available InsightEdge analytics capabilities:

- [Interactive Data Analytics Web UI](../started/insightedge-zeppelin.html)
- [Spark ETL and Transformations]({{% latestjavaurl%}}/insightedge-rdd.html)
- [Spark SQL]({{% latestjavaurl%}}/insightedge-dataframes.html)
- [Streaming]({{% latestjavaurl%}}/insightedge-streaming.html)
- [Machine Learning]({{% latestjavaurl%}}/insightedge-mllib.html)
- [Geospatial Analytics]({{% latestjavaurl%}}/insightedge-geospatial.html)
- [Python API]({{% latestjavaurl%}}/insightedge-python.html)


# Distributed SQL and Visualizations (BI)

The InsightEdge Enterprise edition provides a [SQL-99 query engine]({{% latestjavaurl%}}/sql-client.html) for `read-only` operations. The purpose of this query engine is to provide business intelligence developers and grid administrators with a mechanism to perform interactive data analysis through vizualization tools (such as: Tableau, QlikView and MicroStrategy).

# Cluster Management

All components and applications in the platform are managed, deployed, and monitored through a highly available, resilient service discovery that comprises the [XAP service grid](./the-runtime-environment.html) and {{%exurl "Apache ZooKeeper""https://zookeeper.apache.org/"%}}. The combination is responsible for system health monitoring, service discovery, and resource orchestration, as well as other low-level services for scaling your workloads across your data center or cloud.

# REST Orchestration

In addition to the management center, and web and command line interfaces, GigaSpaces provides a [grid management API]({{%currentadmurl%}}/xap-manager-rest.html) interface exposed through the standard HTTP REST protocol. This REST interface can be called from your custom management and administration scripts to support different deployment and provisioning lifecycle operations. In addition, the REST management API provides a [plug-in provider]({{%currentadmurl%}}/xap-manager-rest-pluggable.html) to implement your own orchestration/integration endpoints.

# Management and Monitoring

GigaSpaces provides several components for monitoring and troubleshooting your XAP or InsightEdge Enterprise deployments. These include:

- [Management Center]({{%currentadmurl%}}/gigaspaces-management-center.html)
- [Web Management Console]({{%currentadmurl%}}/web-management-console.html)
- [Command Line Interface]({{%currentadmurl%}}/deploy-command-line-interface.html)
- [Metrics Reporting and Integration]({{%currentadmurl%}}/metrics-overview.html)
- [Administrative Alerts]({{% latestjavaurl%}}/administrative-alerts.html)


# Security and Auditing

GigaSpaces provides security features that allow the administrators to implement security controls across any component. Security management in GigaSpaces consists of security controls to access the entire platform stack, from [SSL encryption of data traffic]({{%currentsecurl%}}/securing-the-transport-layer-using-ssl.html) to [role-based authentication/authorization]({{%currentsecurl%}}/securing-xap-components.html) for deployment, data access and provisioning.
