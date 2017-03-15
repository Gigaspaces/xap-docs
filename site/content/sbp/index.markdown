---
type: post
title:  Solutions, Patterns & Best Practices
categories: SBP
---

This section contains GigaSpaces XAP known patterns, solutions and best practices that we've accumulated over time based on numerous real life use cases. Each pattern is listed with its author/origin, and the XAP product version that it was tested with.

{{% note "Important Note"%}}
The patterns below are presented as is. While most of them are used in real life use cases and productions environments, and GigaSpaces is making its best effort to keep them up to date, they should not be considered as fully productized artifacts, and you should test them in your own environment before using them.
{{% /note %}}


# Examples


| Example | Description |
|:--------------|:------|
|[Your first JPA application](./first-jpa-app.html) | This tutorial explains how the sample Spring PetClinic application can be fine tuned to use GigaSpaces XAP JPA API and deployed into the GigaSpaces XAP platform. |
|[Deploying and Interacting with the Space](./deploying-and-interacting-with-the-space.html) | This section covers the main APIs of GigaSpaces XAP |
|[Order Management Tutorial](./xap-order-management-tutorial.html) | This section describes SBA from an application development perspective. It focuses on developing a high-throughput, event-driven, service-oriented application using GigaSpaces XAP and Space-Based Architecture. |
|[A Typical SBA Application](./a-typical-sba-application.html) |  In this tutorial, you will see three core space-based concepts in action: architecture, application deployment, and scaling.  |
|[Your First Real Time Big Data Analytics Application](./first-real-time-big-data-analytics-app.html) | This tutorial shows  a Real-time (RT) Analytics system using Twitter as an example, and show in details how these challenges can be met by using   XAP.  |
|[Your First .NET Application](./dotnet-your-first-xap-application.html) |  This example demonstrates a simple processing unit architecture project - a complete SBA application that can easily scale.  |

 


# Data Access Patterns


| Pattern | Level | Description |
|:--------------|:------|:------------|
|[Distributed Collections](./distributed-collections.html) | Advanced |   Distributed Collections|
|[Custom Aggregators](./aggregators-custom.html) | Advanced | Aggregators provided by the core XAP platform are extensible, allowing developers to modify existing functionality as well as add new features.|
|[XAP Spring Data](./spring-data.html) | Advanced | XAP Spring Data implements the Spring Data Framework |
|[Spark Integration](./spark-integration.html) | Advanced | This pattern explains how to integrate XAP with Spark.|
|[Storm Integration](./storm-integration.html) | Advanced | This pattern explains how to integrate XAP with Storm.|
|[Kafka Integration](./kafka-integration.html) | Advanced | This pattern explains how to integrate XAP with Kafka.|
|[DynaCache Integration](./ibm-websphere-cache.html) | Advanced | This pattern explains how to integrate IBM's DynaCache with XAP|
|[Cache Interface](./cache-interface.html) | Beginner | This pattern explains how to implement a cache interface using the Map API.|
|[Spring Cache Abstraction with XAP](./spring-cache.html) | Beginner | This article shows how to use the Spring Cache Abstraction provider with   XAP.|
|[Moving from Hibernate to Space](./moving-from-hibernate-to-space.html) | Beginner | Moving from Database Centric into In-Memory Model. Can be used when moving from **J2EE Session Bean** into XAP.|
|[Storing Partition Summary Information](./storing-partition-information.html)| Beginner | Using a Service Executor to store information of each partition onto the partition. |
|[Even Data Distribution](./even-data-distribution.html)| Beginner | Technique to ensure even data load-balancing with partitioned IMDG.|
|[Space Object Modeling](./space-object-modeling.html)| Beginner | Migrating Relational Model into Object model.|
|[Oracle Delta Server](./oracle-delta-server.html)| Beginner |This pattern presents the Oracle Delta Server allowing the data grid to receive updates from the database conducted by applications that are not using the data grid as their system of record (Non-Aware Data-Grid Clients) |
|[Sql Server Delta Server](./sqlserver-delta-server.html)| Beginner |This pattern presents the Sql Server Delta Server allowing the data grid to receive updates from the database conducted by applications that are not using the data grid as their system of record (Non-Aware Data-Grid Clients) |
|[Sql Server Data Grid](./sqlserver-datagrid.html)| Beginner |This pattern presents a Data Grid using SQL Server as its persistence layer. Initial Load and Write behind are demonstrated with this best practice. |
|[XAP.NET MongoDB Data Grid](./mongodb-datagrid.html)| Beginner |This pattern presents a Data Grid using MongoDB as its persistence layer. Initial Load and Write behind are demonstrated with this best practice.|
|[Excel that Scales Solution](./excel-that-scales-solution.html)| Beginner | Leveraging GigaSpaces .Net API to scale Excel applications.|
|[Global ID Generator](./global-id-generator.html)| Beginner | Replacing Database sequencing with lightweight In-Memory ID Generator.|
|[Web Service PU](./web-service-pu.html)|Beginner | Web Service Processing Unit. Using the CXF Framework.|
|[ODBC Driver](./odbc-driver.html)| Beginner | Accessing GigaSpaces Data Grid via 3rd party ODBC Driver.|
|[IMDG with Large Backend Database Support](./imdg-with-large-backend-database-support.html)| Advanced | Leveraging IMDG running in LRU mode with large backend Database.|
|[DB2 Delta Server](./db2-delta-server.html)| Advanced |This pattern presents the DB2 Delta Server allowing the data grid to receive updates from the database conducted by applications that are not using the data grid as their system of record (Non-Aware Data-Grid Clients) |
|[Dynamic Polling Container Templates using TriggerOperationHandler](./dynamic-polling-container-templates-using-triggeroperationhandler.html)| Advanced | Polling Containers using TriggerOperationHandler |
|[Custom Matching](./custom-matching.html)| Advanced | Implementing complex customized queries for pattern matching.|
|[Custom Eviction](./custom-eviction.html)| Advanced | Implementing used defined data eviction when using large backend Database.|
|[Lowering the Space Object Footprint](./lowering-the-space-object-footprint.html)| Advanced | Implementing compaction API to reduce the object footprint when storing objects with large numeric values.|
|[Rapid Data Load](./rapid-data-load.html)| Advanced | Loading large amount of data into each partition using Map-Reduce technique.|
|[Schema Evolution](./schema-evolution.html)| Advanced | Leveraging different techniques to enhance the application data model without IMDG downtime.|
|[Distributed Bitmap](./distributed-bitmap.html) | Advanced| Use XAP to distribute a bitmap across a partitioned cluster. |
|[Modeling your data](./modeling-your-data.html) | Beginner| Moving from a centralized to a distributed data store. |
|[XAP as Hibernate Second Level Cache](./gigaspaces-for-hibernate-orm-users.html) | Advanced| Using XAP as a 2nd-level distributed cache for your Hibernate-based database integration|



# Parallel Processing & Messaging Patterns


| Pattern | Level | Description |
|:--------------|:------|:------------|
|[Map-Reduce Pattern - Executors Example](./map-reduce-pattern-executors-example.html)| Beginner | Implementing Task Executors and Service Executors to perform parallel queries or parallel processing across multiple IMDG partitions. Can be used when moving from **J2EE EJB**/RMI/IIOP into XAP.|
|[Master-Worker Pattern](./master-worker-pattern.html)| Beginner | Grid computing pattern. Implementing distributed processing across multiple workers deployed into the Grid. |
|[Event Driven Remoting Example](./event-driven-remoting-example.html)| Beginner | Request-Response pattern. Implementing asynchronous remove service invocation. Can be used when moving from **J2EE MDB** into XAP.|
|[Mule ESB Example](./mule-esb-example.html)| Advanced | Scaling ESB based application. Using the IMDG as the ESB state repository. Deploying the ESB into the GigaSpaces grid allowing it to scale in an elastic manner.|
|[Priority Based Queue](./priority-based-queue.html)| Advanced | Messaging based pattern. Can be used when moving from **J2EE JMS** Quality of Service into XAP.|
|[Parallel Queue Pattern](./parallel-queue-pattern.html)| Advanced |Messaging based pattern. Can be used when moving from **J2EE JMS Service Activator Aggregator Strategy/MDB** into XAP.|
|[Unit Of Work](./unit-of-work.html)| Advanced |Messaging based pattern. Can be used when moving from **J2EE JMS** Unit of Order into XAP.|
|[Drools Rule Engine Integration](./xap-drools-integration.html)| Advanced | Scaling business rule management system based application.|
|[Spring Batch PU](./spring-batch-pu.html)| Advanced | Complex Batch Processing using [Spring Batch](http://static.springsource.org/spring-batch).|
|[JTA-XA Example](./jta-xa-example.html) | Advanced | Integrating GigaSpaces with an external JMS Server using XA. Atomikos is used as the JTA Transaction provider. Apache ActiveMQ is used as a the JMS provider.|
|[XAP.NET Custom Aggregators](./aggregators.html) | Advanced | Implemeting custom Aggregators with XAP.Net|


# Setup Production Environment


| Pattern | Level | Description |
|:--------------|:------|:------------|
|[Distributed Mirror](./distributed-mirror.html)| Advanced | A mirror service for each partition|
|[Embedding Tomcat as a Web Container](./tomcat-pu.html)| Beginner | This article presents embedding Tomcat 7 within the XAP data grid|
|[Embedding XAP for OEMs](./embedding-xap-for-oems.html)| Beginner | A quick and simple example of how an OEM might embed GigaSpaces XAP for customer use.|
|[Universal Deployer](./universal-deployer.html) |Beginner | Allows deploying composite applications. Support multiple processing unit dependency based deployment.|
|[Data-Collocation Deployment Topology](./data-collocation-deployment-topology.html)| Beginner | Considerations which topology to choose when deploying both business logic and data into the Grid.|
|[Mirror Monitor](./mirror-monitor.html)| Beginner | Monitoring asynchronous persistency behavior in real time using JMX.|
|[Safe Grid Shutdown](./safe-grid-shutdown.html)| Beginner | Cleanly shutting down the grid after all async replication operations are committed.|
|[Hyperic integration](./hyperic-integration.html)| Beginner | Hyperic monitoring integration example.|
|[Primary-Backup Zone Controller](./primary-backup-zone-controller.html)|Beginner | Using Zones to control Data-Grid primary/backup instances location.|
|[RESTful Admin API](http://www.openspaces.org/display/RES/Project+Documentation)| Beginner | Using web services to monitor and administrate GigaSpaces.|
|[Scaling Agent](./scaling-agent.html)| Beginner | Using the administration API to scale web applications. Can be used when moving **J2EE Web applications** into XAP elastic Web Container.|
|[Web Load Balancer Agent PU](./web-load-balancer-agent-pu.html)| Advanced | Using the administration API to build customized HTTP load-balancer agent. Can be used when moving **J2EE Web applications** into XAP elastic Web Container.|
|[Capacity Planning](./capacity-planning.html)| Advanced | Considerations for sizing your system before moving into production.|
|[Recipes For XAP Cloud Deployment](./automated-xap-deployment-with-cloudify.html)| Advanced | Recipes For XAP Cloud Deployment.|
|[Puppet to install and configure XAP](./puppet-xap-module.html)| Advanced | This pattern explains how to use Puppet to install and configure XAP.|
|[Managing XAP with init.d](./initd.html)| Beginner | This pattern explains how to manage XAP with init.d.|
|[Ensuring Accurate Time Across the Cluster](./cluster-time-sync.html)| Beginner | This pattern explains how to setup cluster wide time synchronization.|
|[XAP Hot Deploy](./xap-hot-deploy.html)| Advanced | This tool allows business logic to be refreshed without any system downtime and data loss.|
|[Refresh Business Logic](./refreshable-business-logic-example.html)| Advanced | Example how to refresh  business logic|



# Security


| Pattern | Level | Description |
|:--------------|:------|:------------|
|[Securing the Transport Layer Using SSL and Self-Signed Certificates Example](./ssl-self-signed-cert-example.html)| Beginner | This article presents how to secure communications using SSL and self-signed certificates.|



# WAN Based Deployment


| Pattern | Level | Description |
|:--------------|:------|:------------|
|[Distributed WAN Gateway](./wan-gateway-distributed.html)| Advanced | Distributed (multi-instance) gateway example.|
|[WAN Gateway Multi-Master Replication](./wan-replication-gateway.html)| Beginner | Multi-Master WAN replication example.|
|[WAN Gateway Master-Slave Replication](./wan-gateway-master-slave-replication.html)| Beginner | Single-Master/Multi-slave replication topology example.|
|[WAN Gateway Pass-Through Replication](./wan-gateway-pass-through-replication.html)| Beginner | Pass-Through WAN replication topology example.|
|[WAN Gateway Command Line Interface Tool](./wan-gateway-command-line-tool.html)| Advanced | Configure and deploy WAN Gateway via command line.|
|[Observable WAN](./observable-wan.html)|Advanced|Monitor and measure the replication performance of a multi-site deployment.|


# Solutions


| Pattern | Level | Description |
|:--------------|:------|:------------|
|[Exporting/Import data](./export-import-tool.html) | Beginner | Export and import data from / into a space.|
|[WAN Gateway CLI Tool](./wan-gateway-command-line-tool.html) | Advanced | Create and deploy a WAN gateway PU.|
|[Elastic Distributed Calculation Engine](./elastic-distributed-calculation-engine.html)|Advanced| Elastic Distributed Calculation Engine implementation using Map-Reduce approach.|
|[Trading Settlement](./trading-settlement.html) |Advanced| A trading settlement system where the entire tier-based architecture is built on GigaSpaces.|
|[Mainframe Integration](./mainframe-integration.html) |Advanced| GigaSpaces XAP can simplify the migration effort from mainframe based systems and reduce the cost of the legacy applications. GigaSpaces XAP act as a front-end layer for mainframe based systems may boost the system performance and improve the overall system response time on peak load.|
|[XAP.NET ASP.NET Session State Store](./dotnet-session-store.html)|Advanced| This pattern presents the HTTP Session sharing best practice for IIS servers and .NET apps.|
|[Time Series with Real Time Analytics](./time-series.html) | Advanced | This pattern explains how to create a time series with XAP .|
|[Integration scalatest](./i10n-scalatest.html)|Advanced|This blueprint shows how to i10n test with a scalatest FunSuite.|
