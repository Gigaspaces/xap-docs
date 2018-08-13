---
type: post
title:  Early Access
parent:
categories: EARLY_ACCESS
weight:
---

This page contains early access information for XAP and InsightEdge 14.0.0, which is scheduled for release in Q4 2018. Early access builds are intended for those who want to get involved in the development process and try out new features and functionality early on, and even affect the final outcome. If you have any feedback on early access features, we'd love to hear it!

<!--
{{%infosign%}} If you're just getting started with version 14.0.0, we recommend reading the [What's New](/xap/12.3/rn/whats-new.html) page in the general release notes.
-->

{{%tip "Disclaimer"%}}
Early access builds are provided as is, and should not be used in production. The latest stable release is version **12.3.1**. The early access documentation is not available yet, so please use the documentation for the lastest stable release.<br>[Download](http://www.gigaspaces.com/xap-download) | [Documentation](/xap/12.3/)
{{%/tip%}}
<hr/>

# 14.0.0 M7 (Aug-12-2018)

## Download Links

* \[[InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/14.0.0/gigaspaces-insightedge-14.0.0-m7-b19907.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/14.0.0/gigaspaces-insightedge-enterprise-14.0.0-m7-b19907.zip)\] 

* \[[XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.0.0/gigaspaces-xap-14.0.0-m7-b19907.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.0.0/gigaspaces-xap-enterprise-14.0.0-m7-b19907.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.0.0/gigaspaces-xap.net-14.0.0-m7-b19907.msi)\]

## Changelog

Feature enhancements and bug fixes are currently in progress, but were not completed within this milestone.

# 14.0.0 M6 (Aug-05-2018)

## Download Links

* \[[InsightEdge (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/14.0.0/gigaspaces-insightedge-14.0.0-m6-b19906.zip) | [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/14.0.0/gigaspaces-insightedge-enterprise-14.0.0-m6-b19906.zip)\] 

* \[[XAP (Open Source)](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.0.0/gigaspaces-xap-14.0.0-m6-b19906.zip) | [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.0.0/gigaspaces-xap-enterprise-14.0.0-m6-b19906.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.0.0/gigaspaces-xap.net-14.0.0-m6-b19906.msi)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13600" %}}: Add system property to show standalone Spaces in REST Manager API and CLI.
- {{% jira id="GS-13448" %}}: Enhance logging to reflect replication connection state changes.
- {{% jira id="GS-13596" %}}: Enhance command line `--server` option to support `host:port` format.
- {{% jira id="GS-13591" %}}: Add a summary to CLI output.
- {{% jira id="GS-13573" %}}: Improve  XAP and InsightEdge demo command behavior when XAP_MANAGER_SERVERS is configured.

### Resolved Issues

- {{% jira id="GS-13404" %}}: Can't generate dump files from user interface after first attempt.
- {{% jira id="GS-13386" %}}: "Only Live Services Log Dump" doesn't work as expected.
