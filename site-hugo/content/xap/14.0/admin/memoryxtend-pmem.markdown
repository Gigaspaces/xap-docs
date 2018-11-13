---
type: post140
title:  MemoryXtend for PMEM
categories: XAP140ADM, ENT
parent: memoryxtend-overview.html
weight: 300
---


# Overview

The MemoryXtend pmem storage driver stores Space objects in pmem, outside the Java heap.
TODO : explain what is pmem
The pmem driver dose not support persistence mode.

# Prerequisites

In order to use this driver you should have the following shared libraries installed:
* libpmem
* libpmemobj
* libpmempool

Make sure that LD_LIBRARY_PATH includes the above libraries running in you envelopment.

For installation instruction please visit  https://pmem.io/

In order tolearn how to emulate pmem on your machine visit https://pmem.io/2016/02/22/pm-emulation.html

# Basic Configuration

You can create a Space with the MemoryXtend pmem storage driver via the `pu.xml` configuration file, or in the code.
For example, to create a Space called 'mySpace' with pmem of size 20GB to be allocated at /tmp/pu-local-storage-pool.txt:

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
{{%tab "Java Code"%}}

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

The  general [MemoryXtend configuration options](./memoryxtend-overview.html#configuration) also apply. For example, you can configure MemoryXtend to cache some data on-heap for faster access.

{{%note%}}
For an example of how to configure the on-heap cache properties, see the [MemoryXtend overview](./memoryxtend-overview.html#on-heap-cache) topic.
{{%/note%}}

## Defining the Memory Threshold

In order to pmem storage, you must define the amount of memory to allocate, for example. `20g`. Use the following sizing units:

* `b` - Bytes
* `k`, `kb` - Kilobytes
* `m`, `mb` - Megabytes
* `g`, `gb` - Gigabytes

Before any operation that requires memory allocation (write, update, and initial load), the memory manager checks how much of the allocated memory has been used.
If the threshold has been breached, an `OffHeapMemoryShortageException` is thrown. Read, take, and clear operations are always allowed.

# Monitoring

The amount of used off-heap memory can be tracked using the following:

* Metrics - The `space_blobstore_off-heap_used-bytes_total` metric, as described on the [Metrics](./metrics-bundled.html#blobstore-operations) page.
* Admin API - Thru [SpaceInstanceStatistics.getBlobStoreStatistics()]({{% api-javadoc %}}/index.html?org/openspaces/admin/space/SpaceInstanceStatistics.html#getBlobStoreStatistics)
* Web Management Console - In the Space instances view, right-click any of the columns in the table and add the **Used Off-Heap** column.

