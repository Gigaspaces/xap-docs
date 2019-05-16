---
type: post123
title:  Hardware Selection
categories: XAP123PROD
parent: none
weight: 100
canonical: auto
---

The general rule when selecting the hardware to run GigaSpaces applications is **the faster the better**. Multi-core machines with large amounts of memory are the most cost effective, because they provide the best performance by leveraging large JVM heap size that can handle simultaneous requests with minimal thread context switch overhead.

You can run production systems with 30G-50GB heap size with some JVM tuning when leveraging multi-core machines. The recommended hardware is [Intel Xeon Processor 5600 Series](https://ark.intel.com/products/series/59213/Legacy-Intel-Xeon-Processors?series=47915). Here is an example for [recommended server configuration](http://www.cisco.com/en/US/products/ps10280/prod_models_comparison.html):


|Model|Cisco UCS B200 M2 Blade Server|Cisco UCS B250 M2 Extended Memory Blade Server|
|:----|:----------------------------:|:--------------------------------------------:|
|Processor Sockets|2|2|
|Processors Supported|Intel Xeon processor 5600 Series|Intel Xeon processor 5600 Series|
|Memory Capacity|12 DIMMs; up to 192 GB|48 DIMMs; up to 384 GB|
|Memory Size and Speed|4, 8, and 16 GB DDR3; 1066 MHz and 1333 MHz|4 and 8 GB DDR3; 1066 MHz and 1333 MHz|
|Internal Disk Drive|2x 2.5-in. SFF SAS or 15mm SATA SSD|2x 2.5-in. SFF SAS or 15mm SATA SSD|
|Integrated Raid|0,1|0,1|
|Mezzanine I/O Adapter Slots|1|2|
|I/O Throughput|Up to 20 Gbps|Up to 40 Gbps|
|Form Factor|Half width|Full width|
|Max. Servers per Chassis|8|4|

# CPU Requirements

Since most of the application activities are conducted in-memory, the CPU speed affects your application performance fairly drastically. You may have a machine with plenty of CPU cores but a slow CPU clock speed, which eventually slows down the application or the data grid response time. As a basic rule, pick the fastest CPU you can find. The data grid itself and its container are highly multi-threaded components, so it is important to use machines with more than a single core to host the GSC to run your data grid or application. A good number for the amount of GSCs per machine is half of the total number of cores.

# Disk Space

With any XAP-based system, log files will be generated. A good best practice is to allocate at least 100MB of free disk size per machine running GigaSpaces applications. Ensure that you delete old log files or move them to a backup location. The XAP data grid may overflow data into the `work directory` when there is a long replication disconnection or replication delay. The location of the `work directory` should be on local storage for each XAP node in order to make this replication backlog data always available to the node. The storage should have enough disk space to store the replication backlog as explained in [Controlling the Replication Redo Log](../admin/controlling-the-replication-redo-log.html).

