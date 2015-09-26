---
type: post110
title:  Overview
categories: XAP110ADM
parent: benchmarking.html
weight: 100
---

{{%ssummary%}}{{%/ssummary%}}


Performance benchmarks with XAP's In-Memory Data Grid mainly involve testing latency and throughput. Since XAP offers data access routines for remote data access as well as colocated data access, you should test latency and throughput in both cases (as appropriate for your architecture and deployment topology).

In addition, you should also test transactional and non-transactional operations, single and batch operations, synchronous and asynchronous options, as all of these are basic building blocks for any application.

Benchmarks should test simple and complex queries as GigaSpaces supports both simple and complex data models and provides indexes to speed up query execution time.

Another dimension for these tests is concurrency - which measures how the system scales as a function of the amount of concurrent remote users or colocated threads accessing the system.

One more dimension is the grid size (the topology). This measures how the system scales as a function of the number of data-grid nodes leveraging GigaSpaces [In-Memory-Map-Reduce API]({{%currentjavaurl%}}/task-execution-over-the-space.html). There is also a [client side caching]({{%currentjavaurl%}}/client-side-caching.html) component that is viable in many scenarios that is important to test (read-mostly scenarios).  As you see the performance benchmark matrix is extensive and requires the benchmark tool to support all these options/scenarios.

# Results

The different benchmarks results can be found on the [benchmarks page](http://www.gigaspaces.com/benchmarks). We  update these from time to time and post new benchmarks results on [our benchmark blog](http://blog.gigaspaces.com/category/benchmarks).

# Tools

- [GS-UI Benchmark View](./benchmark-browser.html)This is a Java Swing-based tool allowing you to run various tests. It does not currently have a good way to export the results and automate the benchmark scenarios. Still, with a matter of few clicks, you can get a good sense how your environment performs.
- [Benchmark Utility - GigaSpaces CLI](./benchmark-utility-cli.html) This is the Java command line version of the GS-UI Benchmark View. It include similar options, but being a command-line tool allows automation of the benchmarks via scripts. The source code is provided for this tool.
- [C++ Benchmark Framework](./benchmark-c++.html) XAP C++ API Benchmark framework. Source code provided.


# Large Scale

Note that once you are running large scale benchmarks with a large number of concurrent accesses, or a large number of data grid partitions, you should consider our involvement to help tune the configuration of the environment and JVM, and in some cases we should help with the code, too, as some of the default behaviors for the basic API have special modifiers that may boost the performance.

Many times developers sometime miss or misunderstand these advanced options, due to the vast amount of features the products offers.  Please do not hesitate to [approach our support team](http://www.gigaspaces.com/content/customer-support-services) for advanced benchmarking and testing options. The information about tuning the product for such environments is not fully publicly available, since many larger systems have specialized requirements for which configurations are available but for which generalized solutions are not entirely appropriate.

