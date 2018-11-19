---
type: post140
title:  MemoryXtend for Off-Heap RAM
categories: XAP140ADM, ENT
parent: memoryxtend-overview.html
weight: 300
---


# Overview

The MemoryXtend off-heap storage driver stores Space objects in RAM, outside the Java heap. This has two benefits:

* Better RAM utilization - Objects are stored off-heap in serialized form, which consumes less memory than the same object would use in a Java heap.
* Reduced garbage collection activity - Less data is stored on the Java heap, so the garbage collector has less work. This yields more deterministic behavior with a lower chance of experiencing stop-the-world pauses.


{{%align center%}}
![image](/attachment_files/blobstore/ohr3.png)
{{%/align%}}

# Basic Configuration

You can create a Space with the MemoryXtend off-heap storage driver via the `pu.xml` configuration file, or in the code. For example, to create a Space called 'mySpace' that can use up to 20GB of off-heap RAM:

{{%tabs%}}
{{%tab "pu.xml"%}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/off-heap-blob-store"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/off-heap-blob-store http://www.openspaces.org/schema/{{% currentversion %}}/off-heap-blob-store/openspaces-off-heap-blob-store.xsd">

    <blob-store:off-heap-blob-store id="offheapBlobstore" memory-threshold="20g"/>

    <os-core:embedded-space id="space" name="mySpace">
       <os-core:blob-store-data-policy persistent="false" blob-store-handler="offheapBlobstore"/>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}
{{%tab "Java Code"%}}

```java
// Create off-heap storage driver:
String memoryThreshold = "5g";
BlobStoreStorageHandler blobStore = new OffHeapBlobStoreConfigurer()
        .setMemoryThreshold(memoryThreshold)
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

In order to use off-heap storage, you must define the amount of memory to allocate, for example. `20g`. Use the following sizing units:

* `b` - Bytes
* `k`, `kb` - Kilobytes
* `m`, `mb` - Megabytes
* `g`, `gb` - Gigabytes

Before any operation that requires memory allocation (write, update, and initial load), the memory manager checks how much of the allocated memory has been used. If the threshold has been breached, an `OffHeapMemoryShortageException` is thrown. Read, take, and clear operations are always allowed.

{{%warning "Important"%}}
If the used memory is below the configured threshold, then a large write operation may exceed the threshold without being blocked or throwing an exception. Users should take this into account when setting the maximum memory size. This behavior is similar to that of the regular [memory manager](../dev-java/memory-management-overview.html).
{{%/warning%}}

# Advanced Configuration

As part of fine-tuning the MemoryXtend functionality, you can control the balance between memory utilization and system performance. This is useful because there are two ways to update the value of an object in the off-heap memory:

* Free up the old memory allocation, and reallocate new memory.
* Overwrite the old value using the same allocated memory.

Deleting and reallocating memory takes longer then overwriting the same memory area, but if the size of the new value is smaller then the old value, then the overwrite option leaves part of the original allocated memory underutilized. You can use the `BLOBSTORE_OFF_HEAP_MIN_DIFF_TO_ALLOCATE_PROP` Space property to control when to trade off system performance for maximizing the memory usage. 

This Space property works as follows: if the oldValueSize - newValueSize > blobstore.off-heap.update_threshold, then delete and reallocate memory for the object. Otherwise overwrite the old object value with the new object value.

The default threshold is 50B.


## Example

{{%tabs%}}
{{%tab "pu.xml"%}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/off-heap-blob-store"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/off-heap-blob-store http://www.openspaces.org/schema/{{% currentversion %}}/off-heap-blob-store/openspaces-off-heap-blob-store.xsd">

    <blob-store:off-heap-blob-store id="offheapBlobstore" memory-threshold="20g"/>

    <os-core:embedded-space id="space" name="mySpace">
       <os-core:blob-store-data-policy persistent="false" blob-store-handler="offheapBlobstore"/>
       <os-core:properties>
          <props>
             <prop key="blobstore.off-heap.update_threshold">1M</prop>
          </props>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}
{{%tab "Java Code"%}}

```java
// Create off-heap storage driver:
String memoryThreshold = "5g";
BlobStoreStorageHandler blobStore = new OffHeapBlobStoreConfigurer()
        .setMemoryThreshold(memoryThreshold)
        .create();
// Create space with that storage driver:
String spaceName = "mySpace";
EmbeddedSpaceConfigurer spaceConfigurer = new EmbeddedSpaceConfigurer(spaceName)
        .cachePolicy(new BlobStoreDataCachePolicy()
                .setBlobStoreHandler(blobStore)
                .setPersistent(false));
spaceConfigurer.addProperty("blobstore.off-heap.update_threshold", "1M");
GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
```

{{%/tab%}}
{{% /tabs %}}

# Monitoring

The amount of used off-heap memory can be tracked using the following:

* Metrics - The `space_blobstore_off-heap_used-bytes_total` metric, as described on the [Metrics](./metrics-bundled.html#blobstore-operations) page.
* Admin API - Thru [SpaceInstanceStatistics.getBlobStoreStatistics()]({{% api-javadoc %}}/index.html?org/openspaces/admin/space/SpaceInstanceStatistics.html#getBlobStoreStatistics)
* Web Management Console - In the Space instances view, right-click any of the columns in the table and add the **Used Off-Heap** column.

