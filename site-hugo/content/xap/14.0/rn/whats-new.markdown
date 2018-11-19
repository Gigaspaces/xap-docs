---
type: post140
title:  What's New
categories: XAP140RN
parent: none
weight: 100
---

This section describes product changes, along with new features and functionality for InsightEdge Platform and XAP release 14.0.

# Kubernetes Integration

The InsightEdge Platform and the XAP data grid both support the Kubernetes environment. Benefits include:
 
- The ability to deploy GigaSpaces-based applications in whatever environment best suits the business needs of the enterprise.
- Kubernetes synergizes with InsightEdge and XAP, simplifying the operationalizing of machine learning and transactional processing at scale.
- InsightEdge and XAP utilize key features of Kubernetes, such as cloud-native orchestration automation with self-healing, cooperative multi-tenancy, and RBAC authorization.
- Auto-deployment of data services and deep learning and machine learning frameworks, such as Apache Spark, Stateful Sets (analogous to Processing Units), as well as the Apache Zeppelin web notebook.

{{<infosign>}} For more information, see [Orchestration](../admin/orchestration.html).

# Tiered Storage (MemoryXtend)

This version of InsightEdge and XAP introduces our newest Persistent Memory (PMEM) driver for the MemoryXtend module. The combined strength of [Optane DC Persistent Memory](https://newsroom.intel.com/editorials/re-architecting-data-center-memory-storage-hierarchy/) and the InsightEdge Platform provides customers with the required performance and TCO optimization for uncompromised business results: 

- In-memory extreme performance at at a significantly lower cost.
- Large reduction in the number of servers required, which reduces footprint, power, maintenance, network and other overhead costs.

{{<infosign>}} For more information, see [MemoryXtend for PMEM](../admin/memoryxtend-pmem.html).

# Hot Swap - Rebalancing with Zero Downtime

Our new demote capability in the REST Manager API makes it easier, faster, and simpler to rebalance a system after significant environment change scenarios, such as  failover or scaling. In the past, the only way to demote a primary instance in order to rebalance a system was to force a restart. Now customers can write their own rebalancing policies that take advantage of the ability to perform a hot swap during runtime, without having to reload the data.

{{<infosign>}} For more information, see [Hot Primary Swap](../admin/resource-load-balancing.html).

# Enhanced Web Notebook Functionality 

InsightEdge now offers a JDBC-based interpreter for Apache Zeppelin. Customers can use this new interpreter to access data directly from the data grid, instead of via a Spark interpreter. This provides incredible value for analysts and developers, who can now visualize the Space data within the Zeppelin notebook.

{{<infosign>}} For more information, see [Using the InsightEdge Web Notebook](../started/insightedge-zeppelin.html).

# Third-Party Product Changes

* [Apache Spark](https://spark.apache.org/) has been upgraded to `2.3.2`
* [Apache Zeppelin](https://zeppelin.apache.org/) has been upgraded to `0.8`

The following third-party dependency updates mitigate known security vulnerabilities:

* [Jetty](http://www.eclipse.org/jetty/) package has been upgraded to `9.3.25`
* [Spring Framework](https://projects.spring.io/spring-framework/) integration has been upgraded to `4.3.19`
* [Spring Security](http://projects.spring.io/spring-security/) integration has been upgraded to `4.2.8`

# End-Of-Life and Deprecated Features and Functionality

The following functionality is deprecated as of this release:

- Legacy command line interface (use the new command line interface that was introduced in release 12.3 instead)
- The gs-agent script (use the `insightedge host run-agent` or `xap host run-agent` command instead)

The following functionality is end of life as of this release:

- XAP.NET for Microsoft .NET v3.5

In addition, we recommend that you review GigaSapaces' updated Java and Spring Framework policies on the [Product Lifecycle and End-of-Life Policy](/release_notes/lifecycle.html) page.








