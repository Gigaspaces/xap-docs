---
type: post140
title:  JVM Tuning
categories: XAP140PROD
parent: none
weight: 900
---

In most cases, the applications that use InsightEdge and XAP are leveraging machines with very fast CPUs, where the amount of temporary objects created is relatively large for the JVM garbage collector to handle with its default settings. This means careful tuning of the JVM is very important to ensure stable and flawless behavior of the application.

{{%align "center"%}}
![image](/attachment_files/jvm-vm-memory.jpg)
{{%/align%}}

The following XAP processes may run on a virtual or a physical machine:

- GSA - Very lightweight process in terms of its memory and CPU usage. This process does not require any tuning. You should have one per machine, or in some cases one per Zone.
- GSC - The runtime environment. This is where the data grid and the deployed processing units run. This process requires the relevant tuning to address the memory capacity required. Number of GSCs should not exceed: `Total # of cores / 4`. With virtual machine setup you should have one GSC per VM. 
- GSM - Lightweight process. Does not require any tuning unless you have very large cluster (over 100 nodes). You should have two of these per XAP grid.
- LUS - Lightweight process. Does not require any tuning unless you have very large cluster (over 100 nodes). You should have two of these per XAP grid.
- ESM - Lightweight process. Does not require any tuning unless you have very large cluster (over 100 nodes). You should have one of this per XAP grid.



```bash
XAP VM Memory size = 
Guest OS Memory + JVM Memory for all GSCs + JVM Memory for GSM + JVM Memory for LUS + JVM Memory for ESM
```


```bash
JVM Memory for a GSC = 
JVM Max Heap (-Xmx value) + JVM Perm Size (-XX:MaxPermSize) + NumberOfConcurrentThreads * (-Xss) + "extra memory"
```

## Space Object Footprint

It may be necessary to calculate the Space Object Footprint. For instructions on how to do this, refer to [Capacity Planning](./production-capacity-planning.html).

### Using a Compound Index to Reduce the Index Footprint

A Compound Index can be used with **AND** queries to speed up the query execution time. This approach combines multiple fields into a single index. Using a Compound Index avoids having multiple indexes on multiple fields, which in turn can reduce the index footprint.

### UseCompressedOops JVM Option

The `-XX:+UseCompressedOops` allows a 64-bit JVM heap size of up to 32GB to use a 32-bit reference address. This can reduce the overall footprint by 20-40%.

### Compressed Storage Mode

Compressed Storage mode can be used to reduce the footprint of non-primitive fields when stored within the Space. This option compress the data on the client, where data stays compressed in the Space and is decompressed when it is read back on the client side. This approach may affect performance.

{{% note "Note" %}}
This option is not available for XAP.NET. 
{{% /note %}}

### Customized Initial Load

The default Space Data source Initial Load behavior loads all Space class data into each partition, and later filters out irrelevant objects. This activity may introduce large amount of garbage to be collected. You can use the `SQL MOD` query to fetch only the relevant data items to be loaded into each partition, which speeds up the initial load time and drastically reduce the amount of garbage generated during this process.

### Redo Log Sizing

The amount of redo log data depends on the following:

- Amount of in-flight activity
- Backup performance
- Primary backup connectivity (long disconnection means a lot of redo log data in memory).

The redo logs swap over to the hard disk at some point, therefore is it recommended to place its location on an SSD drive. Do not use a regular hard drive to store redo log data. The redo log data footprint is similar to the actual raw data footprint without indexes.

## JVM Basic Settings

This section provides examples of the JVM settings that are recommended for applications that generate A large number of temporary objects. In such situations, you afford long pauses due to garbage collection activity. These settings are appropriate for cases where you are running a IMDG, or when the business logic and the IMDG are co-located. For example, an IMDG with co-located Polling/Notify containers, Task executors, or Service remoting.

### JDK 1.6

The following JVM settings are for CMS mode, and are useful for low-latency scenarios:


```bash
-server -Xms8g -Xmx8g -Xmn2g -XX:+UseConcMarkSweepGC -XX:+UseParNewGC
-XX:CMSInitiatingOccupancyFraction=60 -XX:+UseCMSInitiatingOccupancyOnly
-XX:MaxPermSize=256m -XX:+ExplicitGCInvokesConcurrent -XX:+UseCompressedOops
-XX:+CMSClassUnloadingEnabled -XX:+CMSParallelRemarkEnabled
```

### JDK 1.7 - 1.8

The following JVM settings are for g1 mode, and are useful for low-latency scenarios:

```bash
-server -Xms8g -Xmx8g -XX:+UseG1GC -XX:MaxGCPauseMillis=500 -XX:InitiatingHeapOccupancyPercent=50 -XX:+UseCompressedOops
```

You can use the advanced options for JDK 1.7 with the following suggested values:

```bash
-XX:MaxTenuringThreshold=25 -XX:ParallelGCThreads=8 -XX:ConcGCThreads=8 -XX:G1ReservePercent=10 -XX:G1HeapRegionSize=32m
```

{{% tip "Tip"%}}
If your JVM is throwing an 'OutOfMemoryException', the JVM process should be restarted. You will have to to add this property to your JVM setting:
SUN -XX:+HeapDumpOnOutOfMemoryError -XX:OnOutOfMemoryError="kill -9 %p"
JROCKIT -XXexitOnOutOfMemory
{{% /tip %}}

## Young Generation Size (Xmn)

This setting controls the size of the heap allocated for the young generation objects (all the objects that have a short lifetime). Young generation objects are in a specific location in the heap, where the garbage collector passes frequently. All new objects are created in the young generation region (called "eden"). When an object survives (is still "alive") after more than 2-3 gc cleaning cycles, it will be moved to the "old generation" region; these objects are called "survivors". A recommended value for the `Xmn` should be 33% of the `Xmx` value.

## Thread Stack Tuning (Xss)

In many cases, the thread stack size needs to be tuned because the default size is too high. In Java SE 6 OS, the default thread stack size on Sparc is 512k for 32-bit VMs, and 1024k for 64-bit VMs. On x86 Solaris/Linux OS, the thread stack size is 320k for 32-bit VMs and 1024k for 64-bit VMs.

On Microsoft Windows OS, the default thread stack size is read from the binary (java.exe). As of Java SE 6, this value is 320k for 32-bit VMs and 1024k for 64-bit VMs.
You can reduce your thread stack size by running with the -Xss option. For example:

```bash
java -server -Xss384k
```

In some versions of Microsoft Windows, the OS may round up thread stack sizes using very coarse granularity. If the requested size is less than the default size by 1K or more, the stack size is rounded up to the default; otherwise, the stack size is rounded up to a multiple of 1MB. 64k is the least amount of stack space allowed per thread.

## Extra Memory

Extra memory is the memory required for NIO direct memory buffers, JIT code cache, classloaders, Socket Buffers (receive/send), JNI, and GC internal info.
Direct memory buffer usage for Socket Buffer utilization on the GSC side:

```java
com.gs.transport_protocol.lrmi.maxBufferSize X com.gs.transport_protocol.lrmi.max-threads
```

For example - with the default `maxBufferSize` size and 100 threads:

```bash
64k X 100 = 6400KB = 6.4MB
```

With large objects and batch operations (readMultiple, writeMultiple, Space Iterator) increasing the maxBufferSize may improve system performance.

### MaxDirectMemorySize

This JVM option specifies the maximum total size of java.nio (New I/O package) direct buffer allocations. It is used with network data transfer and serialization activity.

The default value for direct memory buffers depends on your version of your JVM. Oracle HotSpot has a default equal to the maximum heap size (`-Xmx` value), although some early versions may default to a particular value. To control this specific memory area, use the `-XX:MaxDirectMemorySize` property. See the following example:


```bash
java -XX:MaxDirectMemorySize=2g myApp
```

Format:

```bash
-XX:MaxDirectMemorySize=size[g|G|m|M|k|K]`
```


Some useful references:

- [Getting Started with the G1 Garbage Collector](http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/G1GettingStarted/index.html)
- [jdk7 garbage collection and documentation](http://stackoverflow.com/questions/8111310/java-7-jdk-7-garbage-collection-and-documentation)
- [g1 cms java garbage collector](http://blog.sematext.com/2013/06/24/g1-cms-java-garbage-collector)
- [java7 g1 options](http://stackoverflow.com/questions/8262674/java7-g1-options)
- [large java heap with g1 collector part 1](http://mpouttuclarke.wordpress.com/2013/03/13/large-java-heap-with-g1-collector-part-1)

{{% tip "Tip"%}}
It is highly recommended to use the latest JDK release when using these options.
{{% /tip %}}

## Capturing Detailed Garbage Collection Statistics

To capture detailed information about garbage collection and how it is performing, add the following parameters to the JVM settings:


```bash
-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:/path/to/log/directory/gc-log-file.log
```

Modify the path and file names appropriately. You must use a different file name for each invocation in order to not overwrite the files from multiple processes.

 

## Soft References LRU Policy

In order to provide the highest level of performance, XAP takes advantage of features in the Java language that allow for effective caching in the face of memory demands. In particular, the [SoftReference](http://docs.oracle.com/javase/6/docs/api/java/lang/ref/SoftReference.html) class is used to store data until there is a need for explicit garbage collection, at which point the data stored in soft references will be collected if possible. The system default is 1000, which represents the amount of time (in milliseconds) that objects will survive past their last reference. `-XX:SoftRefLRUPolicyMSPerMB` is the parameter that allows you to determine how much data is cached by allowing the JVM to control how long it endures; it is recommended to set this value to **500** in active, dynamic systems:


```bash
-XX:SoftRefLRUPolicyMSPerMB=500
```

The above means that softly reachable objects will remain alive for 500 milliseconds after the last time they were referenced.

## Permanent Generation Space

For applications that use relatively large amounts of third-party libraries (for example, the PU uses a large amount of JARs), the default permanent generation space size may not be adequate. If this is so, increase the permanent generation space size. Additionally, refer to the suggested parameters above that should be used together with the other CMS parameters (-XX:+CMSClassUnloadingEnabled). The following are suggested values:


```bash
-XX:PermSize=512m -XX:MaxPermSize=512m
```

{{% note "Note"%}}
XAP is a Java-based product. .NET applications that use XAP should also be aware of using the JVM libraries as part of the .NET client libraries.
{{%/note%}}

{{%refer%}}
Refer to [Tuning Java Virtual Machines](../admin/tuning-java-virtual-machines.html) and [Java SE 6 HotSpot Virtual Machine Garbage Collection Tuning](http://java.sun.com/javase/technologies/hotspot/gc/gc_tuning_6.html) for more detailed JVM tuning recommendations.
{{%/refer%}}

