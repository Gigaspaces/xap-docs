---
type: post140
title:  MemoryXtend for PMEM
categories: XAP140ADM, ENT
parent: memoryxtend-overview.html
weight: 400
---


# Overview

Persistent Memory (PMEM) is a new class of memory that combines high capacity and affordability. By expanding affordable system memory capacities (greater than 3TB per CPU socket), end customers can use systems enabled with PMEM to better optimize their workloads by moving and maintaining larger amounts of data closer to the processor, and minimizing the higher latency of fetching data from system storage.

The Persistent Memory Development Kit, PMDK, is a collection of libraries that have been developed for various use cases, tuned, validated to production quality, and thoroughly documented. These libraries build on the Direct Access (DAX) feature available in both Linux and Windows, which allows applications direct load/store access to persistent memory by memory-mapping files on a persistent-memory-aware file system. The PMDK also includes a collection of tools, examples, and tutorials on persistent memory programming.

PMDK is vendor-neutral, started by Intel and motivated by the introduction of Optane DC persistent memory. PMDK is open source and will work with any persistent memory that provides the SNIA NVM Programming Model.

GigaSpaces has developed a PMEM driver that works with Intel's Optane DC PMEM array, which is currently in its beta stage. The MemoryXtend pmem storage driver stores Space objects in pmem, outside the Java heap.

{{%note%}}
Intel's Optane DC is not yet available in the market, so to use the GigaSpaces PMEM driver you need to emulate PMEM on your machine. For information on how to do this, see the section on [how to emulate Persistent Memory]( https://pmem.io/2016/02/22/pm-emulation.html) on Intel's Persistent Memory Programming project website.
{{%/note%}}


# Prerequisites

The GigaSpaces PMEM driver is based on the libpmem library, which provides low-level persistent memory support. In order to use this driver, you need the following shared libraries installed:

* libpmem
* libpmemobj
* libpmempool

Additionally, make sure that `LD_LIBRARY_PATH` includes the above libraries running in your environment.

For information about these libraries and how to install them, refer to the Intel Persistent Memory Programming project website at https://pmem.io/.

# Basic Configuration

{{%note%}}
The PMEM storage driver dose not currently support persistence mode.
{{%/note%}}

You can create a Space that utilizes the MemoryXtend PMEM storage driver via the `pu.xml` configuration file, or in the code. For example, to create a Space called 'mySpace' with a PMEM memory pool of 20GB to be allocated at /tmp/pu-local-storage-pool.txt:

{{%tabs%}}
{{%tab "pu.xml"%}}

```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/pmem-blob-store"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/14.0/core/openspaces-core.xsd
       http://www.openspaces.org/schema/pmem-blob-store http://www.openspaces.org/schema/14.0/pmem-blob-store/openspaces-pmem-blob-store.xsd">


    <blob-store:pmem-blob-store id="myBlobStore" memory-pool-size="20GB" file-name="/tmp/pu-local-storage-pool.txt"/>

    <os-core:embedded-space id="space" name="MySpace">
        <!-- cache-entries-percentage=0 in order to make sure that objects are written to the ssd-->
        <os-core:blob-store-data-policy  blob-store-handler="myBlobStore"
                                         cache-entries-percentage="20"
                                         avg-object-size-KB="10"
                                         persistent="false"/>
    </os-core:embedded-space>
</beans>
```
{{% /tab %}}
{{%tab "Java"%}}

```java
// Create off-heap storage driver:
BlobStoreStorageHandler blobStore = new PmemBlobStoreConfigurer()
        blobStore.setMemoryPoolSize("20GB");
        blobStore.setFileName("/tmp/pu-local-storage-pool.txt");
        blobStore.setVerbose(false);
        .create();
// Create space with that storage driver:
String spaceName = "mySpace";
EmbeddedSpaceConfigurer spaceConfigurer = new EmbeddedSpaceConfigurer(spaceName)
        .cachePolicy(new BlobStoreDataCachePolicy()
                .setBlobStoreHandler(blobStore)
                .setPersistent(false));
GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

The general [MemoryXtend configuration options](./memoryxtend-overview.html#configuration) also apply. For example, you can configure MemoryXtend to cache some data on-heap for faster access.

{{%note%}}
For an example of how to configure the on-heap cache properties, see the [MemoryXtend overview](./memoryxtend-overview.html#on-heap-cache) topic.
{{%/note%}}

## Defining the Memory Pool Size

In order to use PMEM storage, you must define the amount of memory to allocate, for example. `20g`. Use the following sizing units:

* `b` - Bytes
* `k`, `kb` - Kilobytes
* `m`, `mb` - Megabytes
* `g`, `gb` - Gigabytes

Before any operation that requires memory allocation (write, update, and initial load), the memory manager checks how much of the allocated memory has been used. If the threshold has been breached, an `OffHeapMemoryShortageException` is thrown. Read, take, and clear operations are always allowed.

# Monitoring

The amount of used memory can be tracked with the following monitoring and administration tools:

* Metrics - The `space_blobstore_off-heap_used-bytes_total` metric, as described on the [Metrics](./metrics-bundled.html#blobstore-operations) page.
* Admin API - Thru [SpaceInstanceStatistics.getBlobStoreStatistics()]({{% api-javadoc %}}/index.html?org/openspaces/admin/space/SpaceInstanceStatistics.html#getBlobStoreStatistics)
* Web Management Console - In the Space instances view, right-click any of the columns in the table and add the **Used Off-Heap** column.

{{%note%}}
The data grid views the PMEM storage as off-heap storage. Therefore, the monitoring tools are the same for both the PMEM and the off-heap drivers.
{{%/note%}}
