---
type: post123
title:  What's New
categories: XAP123RN
parent: none
weight: 100
---

This section describes product changes, along with new features and functionality for the early access version of InsightEdge Platform release 12.3.

Draft documentation is available, and may not completely describe the product updates as it is still in progress. For additional details about the available features and enhancements in this version, refer to the  [Early Access](/early_access/index.html) description.

# Performance Improvement

In systems with MemoryXtend, XAP can store the values of indexed fields in off-heap memory, to avoid having to fetch data from disk for queries that only need the index. This feature can optimize performance by up to 50% for the following operations:

- Read with projection and only indexed fields in query and projection - primary instance optimization
- Take with only indexed fields in query - backup optimization
- Clear with only indexed fields in query - primary and backup instance optimization

# Administration

## New Command Line Interface

A new interactive Command Line Interface (CLI) based on the REST Manager API is now available. It is meant to replace the legacy GigaSpaces CLI, and supports both InsightEdge and XAP applications. The new CLI uses a fluent syntax command set and offers a wide range of administrative and maintenance capabilities.

{{%info "Incremental Release"%}}
The new CLI currently offers a subset of the planned commands, with more introduced in each milestone. We encourage users to begin working with this administration tool and give us feedback during the early access stage.
{{%/info%}}


## GS-Agent Enhancement

Users can now configure the gs-agent to launch the Web Management Console when deploying an InsightEdge or XAP environment, using the new `--webui` option.

# Tiered Storage Enhancement

The custom initial load mechanism in MemoryXtend has been extended to provide full life cycle management of the blobstore cache, so that users can prioritize data availability throughout the application lifetime. Users can define a set of queries that define what data should be cached as hot data in the in-memory data grid, while cold data continues to be stored on disk.

# Third-Party Product Changes

* [Spring Framework](https://projects.spring.io/spring-framework/) integration has been upgraded to `4.3.13`
* [Spring Security](http://projects.spring.io/spring-security/) integration has been upgraded to `4.2.3`