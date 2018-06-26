---
type: post
title:  Early Access
parent:
categories: EARLY_ACCESS
weight:
---

This page contains early access information for XAP and InsightEdge 12.3.1, which is scheduled for release in Q2 2018. Early access builds are intended for those who want to get involved in the development process and try out new features and functionality early on, and even affect the final outcome. If you have any feedback on early access features, we'd love to hear it!

{{%infosign%}} If you're just getting started with version 12.3.1, we recommend reading the [What's New](/xap/12.3/rn/whats-new.html) page in the general release notes.

{{%tip "Disclaimer"%}}
Early access builds are provided as is, and should not be used in production. The latest stable release is version **12.3**. <br>[Download](http://www.gigaspaces.com/xap-download) | [Documentation](/xap/12.3/)
{{%/tip%}}
<hr/>

# 12.3.1 RC2 (Jun-24-2018)

## Download Links

* [InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-12.3.1-rc2-b19212.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-enterprise-12.3.1-rc2-b19212.zip) 

* [XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-12.3.1-rc2-b19212.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-enterprise-12.3.1-rc2-b19212.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap.net-12.3.1-rc2-b19212.msi)

## Changelog

### Features and Enhancements

- {{% jira id="GS-13572" %}}: XAP and InsightEdge demo command should fail if XAP_MANAGER_SERVERS is configured.
- {{% jira id="GS-13571" %}}: Enable building Python notebook in build process for InsightEdge Zeppelin tutorial.
- {{% jira id="GS-13581" %}}: Upgrade Apache Spark to version 2.3.1.
- {{% jira id="GS-13428" %}}: Enhance REST API and CLI to support primary zones.

### Resolved Issues

- {{% jira id="GS-13478" %}}: Change operation replicated to mirror can cause NPE in certain failover scenarios.

# 12.3.1 RC1 (Jun-17-2018)

## Download Links

* [InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-12.3.1-rc1-b19209.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-enterprise-12.3.1-rc1-b19209.zip) 

* [XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-12.3.1-rc1-b19209.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-enterprise-12.3.1-rc1-b19209.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap.net-12.3.1-rc1-b19209.msi)

## Changelog

### Features and Enhancements

- {{% jira id="GS-13564" %}}: Upgrade Spring to 4.3.17.
- {{% jira id="GS-13562" %}}: Upgrade Apache Zookeeper to 3.4.12 and Netty to 3.10.6.Final.
- {{% jira id="GS-13494" %}}: Upgrade Apache Spark to 2.3.0.
- {{% jira id="GS-13569" %}}: Log fills up with "unmarshalling failure" messages when a cluster partition member is unreachable.

### Resolved Issues

- {{% jira id="GS-13531" %}}: SpaceTypeDescriptorBuilder fails when explicitly indexing the routing key using deprecated values.
- {{% jira id="GS-13541" %}}: Manager class path does not include cert file located in 'com.gigaspaces.lib.opt.security' directory.
- {{% jira id="GS-13565" %}}: Space instance becomes backup although it is offered leadership by Apache Zookeeper.

# 12.3.1 M8 (Jun-10-2018)

## Download Links

* [InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-12.3.1-m8-b19208.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-enterprise-12.3.1-m8-b19208.zip) 

* [XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-12.3.1-m8-b19208.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-enterprise-12.3.1-m8-b19208.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap.net-12.3.1-m8-b19208.msi)

## Changelog

### Features and Enhancements

- {{% jira id="GS-13557" %}}: Configure Spark to include InsightEdge dependencies on startup.

### Resolved Issues

- {{% jira id="GS-13559" %}}: Display blobstore operation statistics in the GigaSpaces Management Center.

# 12.3.1 M7 (Jun-03-2018)

## Download Links

* \[[InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-open-12.3.1-m7-b19207.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-enterprise-12.3.1-m7-b19207.zip)\] 

* \[[XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-open-12.3.1-m7-b19207.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-enterprise-12.3.1-m7-b19207.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap.net-12.3.1-m7-b19207.msi)\]

## Changelog

Feature enhancements and bug fixes are currently in progress but were not completed within this milestone.

# 12.3.1 M6 (May-27-2018)

## Download Links

* \[[InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-open-12.3.1-m6-b19206.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-enterprise-12.3.1-m6-b19206.zip)\] 

* \[[XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-open-12.3.1-m6-b19206.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-enterprise-12.3.1-m6-b19206.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap.net-12.3.1-m6-b19206.msi)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13552" %}}: Share a single instance of Curator Framework client to support large-scale deployments.
- {{% jira id="GS-13551" %}}: Handle Apache Curator leader election events in a single threaded queue.

### Resolved Issues

- {{% jira id="GS-13547" %}}: ServerTypeDesc tree was not cloned correctly.

# 12.3.1 M5 (May-21-2018)

## Download Links

* \[[InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-open-12.3.1-m5-b19205.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-enterprise-12.3.1-m5-b19205.zip)\] 

* \[[XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-open-12.3.1-m5-b19205.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-enterprise-12.3.1-m5-b19205.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap.net-12.3.1-m5-b19205.msi)\]

## Changelog

### Features and Enhancements

N/A

### Resolved Issues

- {{% jira id="GS-13544" %}}: If a demotion event is sent shortly after an election event, the XAP Manager mishandles the second event and enters an inconsistent state.
- {{% jira id="GS-13548" %}}: Storing the blobStoreVersion not implemented in MemoryXtend off-heap driver.
- {{% jira id="GS-13539" %}}: The Java 8 LocalDateTime is not shown in the Web Management Console object inspector.
- {{% jira id="GS-13496" %}}: The Replication Channels metrics for partition clusters are not being reported.

# 12.3.1 M4 (May-13-2018)

## Download Links

* \[[InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-open-12.3.1-m4-b19204.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/12.3.1/gigaspaces-insightedge-12.3.1-m4-b19204.zip)\] 

* \[[XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-open-12.3.1-m4-b19204.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap-12.3.1-m4-b19204.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/12.3.1/gigaspaces-xap.net-12.3.1-m4-b19204.msi)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13485" %}}: Improve memory allocation by pre-allocating ArrayList instances in the SQL execution engine.
- {{% jira id="GS-13519" %}}: Upgrade RocksDB to 5.11.3.
- {{% jira id="GS-13524" %}}: Display blobstore operation statistics in the Web Management Console.
- {{% jira id="GS-13532" %}}: Enable configuring the MINIMAL_BUFFER_DIFF_TO_ALLOCATE property of the  OffHeapMemoryPool.

### Resolved Issues

- {{% jira id="GS-13537" %}}: The SQL query "SELECT COUNT (...) GROUP BY" doesn't work  in the Web Management Console when it includes enums.
- {{% jira id="GS-13469" %}}: Remoting service routes incorrectly when the routing key is long, and if the value is higher than the max integer.

# 12.3.1 M3 (May-06-2018)

## Download Links

* \[[InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.1/12.3.1-m3/gigaspaces-insightedge-open-12.3.1-m3-b19203.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.1/12.3.1-m3/gigaspaces-insightedge-12.3.1-m3-b19203.zip)\] 

* \[[XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.1/12.3.1-m3/gigaspaces-xap-open-12.3.1-m3-b19203.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.1/12.3.1-m3/gigaspaces-xap-12.3.1-m3-b19203.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.1/12.3.1-m3/gigaspaces-xap.net-12.3.1-m3-b19203.msi)\]

## Changelog

### Features and Enhancements

N/A

### Resolved Issues

- {{% jira id="GS-13521" %}}: Space instance recovery fails on "Failed while getting participants from zookeeper server".
- {{% jira id="GS-13486" %}}: IllegalArgumentException: A metric named process_cpu_time-total already exists.
- {{% jira id="GS-13451" %}}: The SQL query "SELECT COUNT (...) GROUP BY" doesn't work on  nested properties in the Web Management Console.

# 12.3.1 M2 (Apr-30-2018)

## Download Links

* \[[InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.1/12.3.1-m2/gigaspaces-insightedge-open-12.3.1-m2-b19202.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.1/12.3.1-m2/gigaspaces-insightedge-12.3.1-m2-b19202.zip)\] 

* \[[XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.1/12.3.1-m2/gigaspaces-xap-open-12.3.1-m2-b19202.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.1/12.3.1-m2/gigaspaces-xap-12.3.1-m2-b19202.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.1/12.3.1-m2/gigaspaces-xap.net-12.3.1-m2-b19202.msi)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13515" %}}: Upgrade Apache Curator to 4.0.1.

### Resolved Issues

- {{% jira id="GS-13521" %}}: Space instance recovery fails on "Failed while getting participants from zookeeper server".
- {{% jira id="GS-13486" %}}: IllegalArgumentException: A metric named process_cpu_time-total already exists.

# 12.3.1 M1 (Apr-16-2018)

## Download Links

* \[[InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.1/12.3.1-m1/gigaspaces-insightedge-open-12.3.1-m1-b19201.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.1/12.3.1-m1/gigaspaces-insightedge-12.3.1-m1-b19201.zip)\] 

* \[[XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.1/12.3.1-m1/gigaspaces-xap-open-12.3.1-m1-b19201.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.1/12.3.1-m1/gigaspaces-xap-12.3.1-m1-b19201.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.1/12.3.1-m1/gigaspaces-xap.net-12.3.1-m1-b19201.msi)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13518" %}}: Sort results for REST operations related to Spaces and Processing Units.
- {{% jira id="GS-13511" %}}: CLI Auto Complete.
- {{% jira id="GS-13510" %}}: Simplified the CLI command "pu decrement" to receive only one parameter: pui_id.
- {{% jira id="GS-13513" %}}: Enhance CLI output to display "N/A" instead of "-1" where relevant.

### Resolved Issues

- {{% jira id="GS-13520" %}}: Could not display the off-heap columns in the Web Management Console (in systems with MemoryXtend).
- {{% jira id="GS-13505" %}}: Running the demo command in the CLI using off-heap generates a NoClassDefFoundError.
- {{% jira id="GS-13502" %}}: Provisioning a Space instance fails on "Identified another participant with the same name for Space".
- {{% jira id="GS-13517" %}}: Index intersection optimization did not work as expected.
- {{% jira id="GS-13512" %}}: Insightedge CLI command fails to start Spark master and Spark worker when XAP_MANAGER_SERVERS is not configured.
- {{% jira id="GS-13509" %}}: Unhealthy Space remains in Stopped state indefinitely after a network failure.
- {{% jira id="GS-13508" %}}: Apache ZooKeeper sometimes fails to create a data directory on startup.