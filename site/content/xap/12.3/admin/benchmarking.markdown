---
type: post123
title:  Benchmarking
categories: XAP123ADM, PRM
parent: none
weight: 1100
---


Performance benchmarking with the XAP In-Memory Data Grid mainly involve testing latency and throughput. XAP offers data access routines for remote data access as well as co-located data access, so you should test latency and throughput in both cases (as appropriate for your architecture and deployment topology).

In addition, you should also test transactional and non-transactional operations, single and batch operations, synchronous and asynchronous options, as all of these are basic building blocks for any application.

Benchmarks should test simple and complex queries, because GigaSpaces applications support both simple and complex data models and provide indexes to speed up query execution time.

Another dimension for these tests is concurrency, which measures how the system scales as a function of the amount of concurrent remote users or co-located threads accessing the system.

Grid size (the topology) is also a factor. This measures how the system scales as a function of the number of data-grid nodes leveraging the [In-Memory-Map-Reduce API]({{%currentjavaurl%}}/task-execution-over-the-space.html). The [client side caching]({{%currentjavaurl%}}/client-side-caching.html) component, which is viable in many scenarios, should also be tested (read-mostly scenarios). The performance benchmark matrix is extensive, and requires a benchmark tool to support all of the options/scenarios.

# Results

Benchmarks results are available on the {{%exurl "benchmarks page" "http://www.gigaspaces.com/benchmarks"%}}. We update these from time to time, and post new benchmarks results on {{%exurl "our benchmark blog" "http://blog.gigaspaces.com/category/benchmarks"%}}.

# Tools

XAP has the following benchmark tools that can be used to run benchmark tests on your XAP-based environment:

- [GS-UI Benchmark View](./benchmark-browser.html) - This is a Java Swing-based tool that allowing you to run various tests on the Space. It is not currently geared toward exporting the results or automating the benchmark scenarios, but can provide a good sense of how your environment is performing.
- [Benchmark Utility - GigaSpaces CLI](./benchmark-utility-cli.html) - This is the Java command line version of the GS-UI Benchmark View. It includes similar options, and supports automation of the benchmarks via scripts. The source code is provided for this tool.

# Large Scale

If you want to run large-scale benchmark tests with a large number of concurrent accesses, or on a large number of data grid partitions, you should consider contacting GigaSpaces Support to help tune the configuration of the environment and JVM. In some cases GigaSpaces can also help with the code, as some of the default behaviors for the basic API have special modifiers that may boost performance.

Developers sometime miss or misunderstand these advanced options, due to the vast amount of features the product offers. We urge you to {{%exurl "approach our support team" "http://www.gigaspaces.com/content/customer-support-services"%}} for advanced benchmarking and testing options. Information about tuning the product for such environments is not fully publicly available, because many larger systems have specialized requirements for which configurations are available, and for which generalized solutions are not entirely appropriate.

