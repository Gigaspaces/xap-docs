---
type: post123
title:  What's New
categories: XAP123RN
parent: none
weight: 100
---

This section describes product changes, along with new features and functionality for InsightEdge platform release 12.3.

# Tiered Storage (MemoryXtend)

## User controlled cache

The LRU on-heap cache has been enhanced to accept user-defined criteria to control which data should be cached, so that users can prioritize data availability. Users can define a set of queries that define what data should be cached as hot data in the in-memory data grid, while cold data continues to be stored on disk (SSD or HD). This is an evolution of an existing API from previous versions, which controlled what data should be cached during initial load - that policy now affects the entire application lifetime of the grid.

{{<infosign>}} For more information see [User-Defined Cache](../admin/memoryxtend-overview.html#user-defined-cache)

## Off-Heap Storage Driver

In addition to the existing SSD/Disk storage driver, we're introducting a new storage driver for **Off-Heap RAM**. This allows users to store data on RAM outside the JVM heap, which is faster than SSD access, and consumes less memory than On-Heap objects.

{{<infosign>}} For more information see [MemoryXtend for Off-Heap RAM](../admin/memoryxtend-ohr.html)


## Off-Heap Caching for SSD Storage Driver

In systems with MemoryXtend, the platform can store the values of indexed fields in off-heap memory, to avoid having to fetch data from disk (SSD or HD) for queries that only need the index. This feature can optimize performance by up to 50% for the following operations:

- Read with projection and only indexed fields in query and projection - primary instance optimization
- Take with only indexed fields in query - backup optimization
- Clear with only indexed fields in query - primary and backup instance optimization

{{<infosign>}} For more information see [Off-Heap Cache](../admin/memoryxtend-rocksdb-ssd.html#off-heap-cache)

# New Command Line Interface

A new Command Line Interface (CLI) is now available, which supports both InsightEdge and XAP applications. The new command line interface unifies the user experience across InsightEdge and XAP, and is based on the recent addition of Manager REST API, enabling you to manage remote or cloud-based environment as easily as managing your local machine. In addition, it provides modern syntax and user-friendly command structure and help screens to guide you thru learning it.

To get started with the new CLI simply navigate to the **bin** folder and run `insightedge --help` or `xap --help` (according to the product your're using).

The following is a partial list of the available functionality:

- Start and stop an agent (gs-agent) on the current host.
- Deploy and manage a Processing Unit or Space
- List, create, kill, and restart containers (GSCs)
- List hosts
- List Spaces and Space instances

The previous GigaSpaces CLI is still provided and supported, but will be removed in future versions. We encourage you to give the new CLI a try, and tell us if you're missing anything or have an idea on how to improve it.

{{<infosign>}} For more information see [Command Line Interface](../admin/admin-interactive-cli.html)

# Docker Support

Starting 12.3 we're providing official [Docker](https://www.docker.com) images for InsightEdge and XAP (both open source and enterprise).

The docker images are available at [Docker Hub](https://hub.docker.com/r/gigaspaces/), and their corresponding docker files are available at [GitHub](https://github.com/gigaspaces/docker).

The docker images takes advantage of the new Command Line Interface and REST API, which makes it seamless to work with docker on your local machine, within your enterprise network or on the cloud.

{{<infosign>}} For more information see [Deploying From Docker Hub](../started/docker-deployment-xap.html)

# Memory Footprint

New index types have been introduced, to offer users additional indexing options that requires a smaller memory footprint. The new index types are:

- `EQUAL` - Performs matching by equality, and should be used instead of the BASIC indexing option.
- `ORDERED` - Performs ordered matching.
- `EQUAL_AND_ORDERED` - Performs both matching by equality and ordered matching, and should be used instead of the `EXTENDED` indexing option.

The new `ORDERED` index consumes less memory than `EQUAL_AND_ORDERED`, at the cost of matching equality less efficiently. When optimizing for ordered queries alone, this index should be preferred. When optimizing for both ordered and equality queries, there's a performance-vs-footprint tradeoff between `ORDERED` and `EQUAL_AND_ORDERED`, which you should consider according to your use-case.

{{<infosign>}} For more information see [Index Types](../dev-java/indexing.html#index-types)

# Third-Party Product Changes

{{%info "Spark 2.3 Support"%}}
The InsightEdge platform supports Apache Spark version 2.3. Due to lack of support for Apache Spark version 2.3 in the current Apache Zeppelin version, InsightEdge is packaged with Apache Spark 2.2. Customers that do not use Apache Zeppelin and have their own Apache Spark implementation can use version 2.3 with GigaSpaces products.
{{%/info%}}

* [Apache Zookeeper](https://zookeeper.apache.org/) has been upgraded to `3.4.10`
* [Jetty](http://www.eclipse.org/jetty/) package has been upgraded to `9.2.24`
* [Spring Framework](https://projects.spring.io/spring-framework/) integration has been upgraded to `4.3.13`
* [Spring Security](http://projects.spring.io/spring-security/) integration has been upgraded to `4.2.3`

# End-Of-Life and Deprecated Features and Functionality

The following functionality is deprecated as of this release:

- V1 of the XAP Manager REST API (use the new V2 instead)
- `BASIC` and `EXTENDED` index types (use `EQUAL` | `ORDERED` | `EQUAL_AND_ORDERED` instead)

In addition, Java 7 is no longer supported - the minimum supported Java version is now Java 8.