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

In systems with MemoryXtend, the platform can store the values of indexed fields in off-heap memory, to avoid having to fetch data from disk (SSD or HD) for queries that only need the index. This feature can optimize performance by up to 50% for the following operations:

- Read with projection and only indexed fields in query and projection - primary instance optimization
- Take with only indexed fields in query - backup optimization
- Clear with only indexed fields in query - primary and backup instance optimization

# Administration

## New Command Line Interface

A new interactive Command Line Interface (CLI) based on the XAP Manager's REST API is planned for version 12.3. It is meant to replace the legacy GigaSpaces CLI, and supports both InsightEdge and XAP applications. This feature is still under construction, but is sufficiently mature for user evaluation. 

We encourage users to begin working with the new CLI and give us feedback. You can run the new CLI via the `xap` script in the **bin** folder (use `--help` to get started). Commands that have not yet been fully implemented will generate an "under construction" message. The following is a partial list of the available functionality:

- Deploy a Processing Unit or Space
- List, create, kill, and restart containers (GSCs)
- List hosts
- Start an agent on the current hosts
- List Spaces and Space instances

## GS-Agent Enhancement

Users can now configure the gs-agent to launch the Web Management Console when deploying an InsightEdge or XAP environment, using the new `--webui` option.

## XAP Manager

Version 2 of the REST API (on which the XAP Manager is based) is now available, and is supported by the new Command Line Interface and the Administration API.  

# GigaSpaces on Docker Hub

A Docker image for XAP has been published at [Docker Hub](https://hub.docker.com/r/gigaspaces/xap/). This feature is still under construction, but is sufficiently mature for user evaluation and feedback.

To access the Docker image, follow the instructions on the [GigaSpaces XAP](https://hub.docker.com/r/gigaspaces/xap/) page. Docker images for XAP open source and InsightEdge will be available soon.

# Tiered Storage Enhancement

The custom initial load mechanism in MemoryXtend has been extended to provide full life cycle management of the blobstore cache, so that users can prioritize data availability throughout the application lifetime. Users can define a set of queries that define what data should be cached as hot data in the in-memory data grid, while cold data continues to be stored on disk (SSD or HD).

# Third-Party Product Changes

* [Jetty](http://www.eclipse.org/jetty/) package has been upgraded to `9.2.24`
* [Spring Framework](https://projects.spring.io/spring-framework/) integration has been upgraded to `4.3.13`
* [Spring Security](http://projects.spring.io/spring-security/) integration has been upgraded to `4.2.3`