---
type: post
title:  Early Access
parent: none
categories: EARLY_ACCESS
weight: 1
---

This page contains early access information for the InsightEdge Platform and XAP data grid version 14.5.0, which is scheduled for release at the end of Q2 2019. Early access builds are intended for those who want to get involved in the development process and try out new features and functionality early on, and even affect the final outcome. If you have any feedback on early access features, we'd love to hear it!

 {{%tip "Disclaimer"%}} Early access builds are provided as is, and should not be used in production. The latest stable release is version 14.0.1.<br>[Download](https://www.gigaspaces.com/download-center) | [Documentation](/latest/)</br>{{%/tip%}}


# 15.0.0 M8 (Aug-12-2019)

## Download Links

* [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/15.0.0/gigaspaces-insightedge-enterprise-15.0.0-m8.zip) 

* [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/15.0.0/gigaspaces-xap-enterprise-15.0.0-m8.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/15.0.0/gigaspaces-xap.net-15.0.0-m8.msi)

## Changelog

### Features and Enhancements

- {{% jira id="GS-13887" %}}: Kubernetes network policy enhancement to enable data operations from remote clients via a public IP/port.
- {{% jira id="GS-13881" %}}: Added Lucene support for collections in full text search.
- {{% jira id="GS-13883" %}}: Enhance InsightEdge JDBC driver to optionally get schema from an alternative URL instead of from a Space.
- {{% jira id="GS-13683" %}}: Upgrade Jetty to 9.4.19.
- {{% jira id="GS-13816" %}}: Improve SQL query performance when distinct keyword is used.
- {{% jira id="GS-13696" %}}: User-defined SQL functions now support setting parameters.
- {{% jira id="GS-13893" %}}: Added the CONTAINS_KEY built-in SQL function that checks if a map contains a key.
- {{% jira id="GS-12362" %}}: The GS Agent only monitors (and restarts) components that were started successfully.
- {{% jira id="GS-13891" %}}: Upgrade RocksDB to 6.0.1.
- {{% jira id="GS-13879" %}}: Upgrade Hibernate to 5.2.
- {{% jira id="GS-13868" %}}: Added support in AnalyticsXtreme for Hive partitioned tables.

### Resolved Issues

- {{% jira id="GS-13894" %}}: GS Agent components don't terminate when the parent process is no longer alive.
- {{% jira id="GS-13863" %}}: Full recovery failed when using a FIFO group.
- {{% jira id="GS-13207" %}}: Polling container didn't have a graceful shutdown when using a DynamicEventTemplate in combination with a SQL query.
- {{% jira id="GS-13812" %}}: Full recovery does not preserve the FIFO grouping order.
