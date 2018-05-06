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

- {{% jira id="GS-13516" %}}: Sort results for REST operations related to Spaces and Processing Units.
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