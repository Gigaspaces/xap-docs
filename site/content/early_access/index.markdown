---
type: post
title:  Early Access
parent:
categories: EARLY_ACCESS
weight:
---

This page contains early access information for XAP and InsightEdge 12.3, which is scheduled for release in Q1 2018. Early access builds are intended for those who want to get involved in the development process and try out new stuff early on, and even affect the final outcome. If you have any feedback on early access features, we'd love to hear it!

{{%plus%}} If you're just getting started with version 12.3, we recommend reading the [What's New](/xap/12.3/rn/whats-new.html) page in the general release notes.

{{%info "Disclaimer"%}}
Early access builds are provided as is, and should not be used in production. The latest stable release is version **12.2.0** - [Download](http://www.gigaspaces.com/xap-download) | [Documentation](/xap/12.2/)
{{%/info%}}
<hr/>

# 12.3 M12 (Dec-31-2017)

## Download Links

* \[[InsightEdge](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.0/12.3.0-m12/gigaspaces-insightedge-12.3.0-m12-b18912.zip)\] 

* \[[XAP](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m12/gigaspaces-xap-12.3.0-m12-b18912.zip) | [XAP.NET x64](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m12/gigaspaces-xap.net-12.3.0-m12-b18912.msi) | [XAP Open Source](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.0/12.3.0-m12/gigaspaces-xap-open-12.3.0-m12-b18912.zip)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-12800" %}}: Improved AbstractEventListenerContainer monitoring via public getter methods.
- {{% jira id="GS-13224" %}}: Improve log message to include table name when initial load fails due to wrong column name.

### Resolved Issues

N/A

# 12.3 M11 (Dec-24-2017)

## Download Links

* \[[InsightEdge](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.0/12.3.0-m11/gigaspaces-insightedge-12.3.0-m11-b18911.zip)\] 

* \[[XAP](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m11/gigaspaces-xap-12.3.0-m11-b18911.zip) | [XAP.NET x64](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m11/gigaspaces-xap.net-12.3.0-m11-b18911.msi) | [XAP Open Source](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.0/12.3.0-m11/gigaspaces-xap-open-12.3.0-m11-b18911.zip)\]

## Changelog

### Features and Enhancements

N/A

### Resolved Issues

- {{% jira id="GS-13453" %}}: Inefficient use of underlying data structure slows down iterator performance.
- {{% jira id="GS-13284" %}}: In rare scenarios, a transactional Polling container may drop events.

# 12.3 M10 (Dec-17-2017)

## Download Links

* \[[InsightEdge](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.0/12.3.0-m10/gigaspaces-insightedge-12.3.0-m10-b18910.zip)\] 

* \[[XAP](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m10/gigaspaces-xap-12.3.0-m10-b18910.zip) | [XAP.NET x64](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m10/gigaspaces-xap.net-12.3.0-m10-b18910.msi) | [XAP Open Source](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.0/12.3.0-m10/gigaspaces-xap-open-12.3.0-m10-b18910.zip)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13452" %}}: Improve responsiveness of remote statistics gathering in Admin API.

### Resolved Issues

- {{% jira id="GS-13236" %}}: The Admin API blocks itself, becoming unresponsive and exploding memory usage.

# 12.3 M9 (Dec-10-2017)

## Download Links

* \[[InsightEdge](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.0/12.3.0-m9/gigaspaces-insightedge-12.3.0-m9-b18909.zip)\] 

* \[[XAP](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m9/gigaspaces-xap-12.3.0-m9-b18909.zip) | [XAP.NET x64](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m9/gigaspaces-xap.net-12.3.0-m9-b18909.msi) | [XAP Open Source](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.0/12.3.0-m9/gigaspaces-xap-open-12.3.0-m9-b18909.zip)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13136" %}}: Make instance variables in DefaultHibernateSpaceDataSourceConfigurer protected.

### Resolved Issues

N/A

# 12.3 M8 (Dec-03-2017)

## Download Links

* \[[InsightEdge](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.0/12.3.0-m8/gigaspaces-insightedge-12.3.0-m8-b18908.zip)\] 

* \[[XAP](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m8/gigaspaces-xap-12.3.0-m8-b18908.zip) | [XAP.NET x64](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m8/gigaspaces-xap.net-12.3.0-m8-b18908.msi) | [XAP Open Source](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.0/12.3.0-m8/gigaspaces-xap-open-12.3.0-m8-b18908.zip)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13411" %}}: User-defined cache criteria for hot data in MemoryXtend.
- {{% jira id="GS-13436" %}}: Added more GSM state information to application dump.
- {{% jira id="GS-13442" %}}: Reduce the footprint of the Lookup Service template cache.

### Resolved Issues

- {{% jira id="GS-13440" %}}: Incomplete recovery of processing units after healing of GSM (if there are 3 or more).

# 12.3 M7 (Nov-26-2017)

## Download Links

* \[[InsightEdge](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.0/12.3.0-m7/gigaspaces-insightedge-12.3.0-m7-b18907.zip)\] 

* \[[XAP](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m7/gigaspaces-xap-12.3.0-m7-b18907.zip) | [XAP.NET x64](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m7/gigaspaces-xap.net-12.3.0-m7-b18907.msi) | [XAP Open Source](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.0/12.3.0-m7/gigaspaces-xap-open-12.3.0-m7-b18907.zip)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13302" %}}: When the query result set includes only indexes, fetch results from off-heap memory instead of disk.
- {{% jira id="GS-13433" %}}: Enhance LRMI network filter buffer allocation strategy by adding support for large objects (>10MB).

### Resolved Issues

- {{% jira id="GS-13430" %}}: Inconsistent Request ID type in Manager REST API (string vs. numeric).

# 12.3 M6 (Nov-19-2017)

## Download Links

* \[[InsightEdge](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.0/12.3.0-m6/gigaspaces-insightedge-12.3.0-m6-b18906.zip)\] 

* \[[XAP](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m6/gigaspaces-xap-12.3.0-m6-b18906.zip) | [XAP.NET x64](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m6/gigaspaces-xap.net-12.3.0-m6-b18906.msi) | [XAP Open Source](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.0/12.3.0-m6/gigaspaces-xap-open-12.3.0-m6-b18906.zip)\]

## Changelog

### Features and Enhancements

N/A

### Resolved Issues

- {{% jira id="GS-13427" %}}: The REST manager specification contained the wrong request status values.
- {{% jira id="GS-13204" %}}: ProcessingUnitInstance.relocationAndWait() might block forever.

# 12.3 M5 (Nov-12-2017)

## Download Links

* \[[InsightEdge](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.0/12.3.0-m5/gigaspaces-insightedge-12.3.0-m5-b18905.zip)\] 

* \[[XAP](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m5/gigaspaces-xap-12.3.0-m5-b18905.zip) | [XAP.NET x64](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m5/gigaspaces-xap.net-12.3.0-m5-b18905.msi) | [XAP Open Source](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.0/12.3.0-m5/gigaspaces-xap-open-12.3.0-m5-b18905.zip)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13418" %}}: Skip login page if Web UI is in non-secured mode.

### Resolved Issues

N/A 

# 12.3 M4 (Nov-5-2017)

## Download Links

* \[[InsightEdge](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.3.0/12.3.0-m4/gigaspaces-insightedge-12.3.0-m4-b18904.zip)\] 

* \[[XAP](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m4/gigaspaces-xap-12.3.0-m4-b18904.zip) | [XAP.NET x64](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap/12.3.0/12.3.0-m4/gigaspaces-xap.net-12.3.0-m4-b18904.msi) | [XAP Open Source](https://gigaspaces-releases-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.3.0/12.3.0-m4/gigaspaces-xap-open-12.3.0-m4-b18904.zip)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-12365" %}}: Asynchronous execution should always return immediately.
- {{% jira id="GS-13417" %}}: Add metrics to Lookup Service to facilitate troubleshooting of performance issues.
- {{% jira id="GS-13389" %}}: Lookup Service (LUS) enhancements to improve stability in environments with large clusters.

### Resolved Issues

- {{% jira id="GS-13383" %}}: Insightedge shutdown command on Mac systems doesn't terminate the gs-agent.
- {{% jira id="GS-13371" %}}: REST calls to Spark failed on machine failover.
- {{% jira id="GS-13317" %}}: No proactive verification of user credentials when logging into secured web-ui.
- {{% jira id="GS-13416" %}}: Lookup service improvements resulted in loss of client notifications in rare scenarios.
- {{% jira id="GS-13281" %}}: Querying a type with enum on a re-deployed space returns incorrect results.

