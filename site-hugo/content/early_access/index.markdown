---
type: post
title:  Early Access
parent: none
categories: EARLY_ACCESS
weight: 1
---

This page contains early access information for XAP and InsightEdge 14.2.0, which is scheduled for release at the end of Q1 2019. Early access builds are intended for those who want to get involved in the development process and try out new features and functionality early on, and even affect the final outcome. If you have any feedback on early access features, we'd love to hear it!

{{%tip "Disclaimer"%}} Early access builds are provided as is, and should not be used in production. The latest stable release is version 14.0.1.<br>[Download](https://www.gigaspaces.com/download-center) | [Documentation](/xap/14.0/)</br>{{%/tip%}}

# 14.2.0 M9 (Feb-24-2019)

## Download Links

* \[[InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/14.2.0/gigaspaces-insightedge-14.2.0-m9-b20409.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/14.2.0/gigaspaces-insightedge-enterprise-14.2.0-m9-b20409.zip)\] 

* \[[XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.2.0/gigaspaces-xap-14.2.0-m9-b20409.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.2.0/gigaspaces-xap-enterprise-14.2.0-m9-b20409.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.2.0/gigaspaces-xap.net-14.2.0-m9-b20409.msi)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13776" %}}: Added ability to disable caching of the change path for the Change API.
- {{% jira id="GS-13784" %}}: The Apache Zeppelin Shell interpreter is now packaged with InsightEdge.
- {{% jira id="GS-13774" %}}: New CLI now supports using a properties file to configure the Processing Unit properties.
- {{% jira id="GS-13772" %}}: Upgrade Apache Zeppelin to 0.8.1.

### Resolved Issues

- {{% jira id="GS-13781" %}}: Java vendor parsing failed on StringIndexOutOfBoundsException.
- {{% jira id="GS-13714" %}}: Executing a SQL query with a function on an indexed field returned incorrect results.
- {{% jira id="GS-13777" %}}: Processing Unit remains in UNDEPLOYED state although all instances have terminated.
- {{% jira id="GS-13741" %}}: Aggregation operation ignores transactions.
