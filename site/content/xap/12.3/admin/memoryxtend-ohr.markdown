---
type: post123
title:  MemoryXtend for Off-Heap RAM
categories: XAP123ADM, ENT
parent: memoryxtend-overview.html
weight: 300
---


# Introduction

The MemoryXtend off-heap storage driver stores Space objects in RAM, outside the Java heap. This has two benefits:

* Better RAM utilization - Objects are stored off-heap in serialized form, which consumes less memory than the same object would use in a Java heap.
* Reduced GC activity - As less data is stored on the Java heap, the Garbage Collector's work becomes easier, yielding more deterministic behavior with lower chance of experiencing stop-the world pauses.

<br>

{{%align center%}}
![image](/attachment_files/blobstore/ohr3.png)
{{%/align%}}

# Configuration

Creating a Space with the off-heap storage driver can be done via `pu.xml` or code. For example, to create a Space called 'mySpace' that can use up to 20GB of off-heap RAM:

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

<br/>
Note that the  general [MemoryXtend configuration options](./memoryxtend-overview.html#configuration) also apply. For example, you can configure MemoryXtend to cache some data on-heap for faster access.

## Memory Threshold

In order to use off-heap storage, you must define the amount of memory to allocate, for example. `20g`. Use the following sizing units:

* `b` - Bytes
* `k`, `kb` - KiloBytes
* `m`, `mb` - MegaBytes
* `g`, `gb` - GigaBytes

Before any operation that requires memory allocation (write, update, and initial load), the memory manager checks how much of the allocated memory has been used. If the threshold has been breached, an `OffHeapMemoryShortageException` is thrown. Read, take, and clear operations are always allowed.

{{%warning "Important"%}}
If the used memory is below the configured threshold, then a large write operation may exceed the threshold without being blocked or throwing an exception. Users should take this into account when setting the maximum memory size. This behavior is similar to that of the regular [memory manager](../dev-java/memory-management-overview.html).
{{%/warning%}}

# Advanced Configuration

when updating a value in the off-heap memory and the size of the new value is smaller then the old one we have two choices,
to free the old memory and reallocate new memory or to overwrite the old value on the same memory space.

deleting and reallocating is heavier then rewriting but rewriting leave allocated memory that is not utilized, that is why we provide a space property to control the decision.

if the oldValueSize - newValueSize > blobstore.off-heap.update_threshold then we delete and reallocate and otherwise we overwrite the old value with the new.

default threshold is 50B.


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


# Monitoring

The amount of used off-heap memory can be tracked using the following:

* Metrics - The `space_blobstore_off-heap_used-bytes_total` metric, as described on the [Metrics](./metrics-bundled.html#blobstore-operations) page.
* Admin API - Thru [SpaceInstanceStatistics.getBlobStoreStatistics()]({{% api-javadoc %}}/index.html?org/openspaces/admin/space/SpaceInstanceStatistics.html#getBlobStoreStatistics)
* Web Management Console - In the Space instances view, right-click any of the columns in the table and add the **Used Off-Heap** column.

