---
type: post120
title:  Off Heap - MapDB Add-On
categories: XAP120ADM, ENT
parent: memoryxtend-overview.html
weight: 300
---

{{% ssummary %}}  {{% /ssummary %}}

# What is MapDB?

[MapDB](http://www.mapdb.org/) is an embedded database engine which provides concurrent Maps, Sets and Queues backed by disk storage or off-heap memory. GigaSpaces XAP provides a [MemoryXtend](./memoryxtend.html) add-on based on MapDB, which can be used for reducing the JVM heap size and garbage collection, since the entries are stored off-heap.


{{%refer%}}
If you're not familiar with MemoryXtend, make sure you read its [documentation](./memoryxtend.html) before proceeding.
{{%/refer%}}

 

# Prerequisites

- Java 7 (or later)


{{%align center%}}
![image](/attachment_files/blobstore/ohr3.png)
{{%/align%}}






# Configuration

{{%align center%}}
![image](/attachment_files/blobstore/ohr1.png)
{{%/align%}}


Creating a space with the MapDB add-on can be done via `pu.xml` or code. For example:

{{%tabs%}}
{{%tab "pu.xml"%}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/mapdb-blob-store"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/mapdb-blob-store http://www.openspaces.org/schema/{{% currentversion %}}/mapdb-blob-store/openspaces-mapdb-blobstore.xsd">

    <blob-store:mapdb-blob-store id="offheapBlobstore"/>

    <os-core:space id="space" url="/./myDataGrid">
       <os-core:blob-store-data-policy persistent="false" blob-store-handler="offheapBlobstore"/>
    </os-core:space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}
{{%tab "Code"%}}

```java
String spaceURL = "/./mySpace";
MapDBBlobStoreConfigurer configurer = new MapDBBlobStoreConfigurer();
MapDBBlobStoreHandler mapDBBlobStoreHandler = configurer.create();

BlobStoreDataCachePolicy cachePolicy = new BlobStoreDataCachePolicy();
cachePolicy.setBlobStoreHandler(mapDBBlobStoreHandler);

UrlSpaceConfigurer urlConfig = new UrlSpaceConfigurer(spaceURL);
urlConfig.cachePolicy(cachePolicy);
GigaSpace gigaSpace = new GigaSpaceConfigurer(urlConfig.space()).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

In addition to the general [MemoryXtend configuration options](./memoryxtend.html#configuration), the MapDB MemoryXtend add-on supports the following configuration options:

| Property               | Description                                               | Default | Use |
|:-----------------------|:----------------------------------------------------------|:--------|:--------|
| <nobr>write-only-block-percentage</nobr> | Specifies a maximum threshold for off-heap memory use. If the space containers off-heap memory usage exceeds this threshold, a `BlobStoreMemoryShortageException` is thrown. | 80 | optional |
| <nobr>compaction-interval</nobr> | Specifies the off-heap compaction interval in millis. | 30000 | optional |
| <nobr>compaction-minimal-interval</nobr> | pecifies the off-heap compaction minimal interval in millis. | 10000 | optional |

# JVM Configuration

Configure the maximum off-heap(direct) memory that the JVM will allocate: -XX:MaxDirectMemorySize=100G, off-heap(direct) memory is separate from the JVM heap allocated by -Xmx. 
The value allocated by `-XX:MaxDirectMemorySize` must not exceed physical RAM, and should likely to be less than total available RAM.
Default value of `-XX:MaxDirectMemorySize` is depends on your JVM version, Oracle Java default is 64mb.

Configuring an IMDG (Space) with BlobStore should be done via the `MapDBBlobStoreDataPolicyFactoryBean`, or the `MapDBBlobStoreConfigurer`. For example:

```java
GSC_JAVA_OPTIONS="-server -Xms20g -Xmx20g -XX:MaxDirectMemorySize=100g -Xmn6g -XX:+UseG1GC"; export GSC_JAVA_OPTIONS
```
