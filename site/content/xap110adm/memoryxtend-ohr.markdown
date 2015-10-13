---
type: post110
title:  Off Heap RAM
categories: XAP110ADM
parent: memoryxtend.html
weight: 300
---


{{% ssummary %}}  {{% /ssummary %}}




When running with offheap blobstore enabled, you eliminate the JVM garbage collection influence since only part of your application data is stored into JVM Heap and most of the data is stored into off heap memory.


XAP is using [MapDB](http://www.mapdb.org/), an embedded database engine which provides concurrent Maps, Sets and Queues backed by disk storage or off-heap memory.
When writing an object to the space, its indexed data maintained on-heap and it's raw data is stored on off-heap.

The indexes maintain in RAM (on-heap) allowing the XAP query engine to evaluate the query without accessing the raw data stored on off-heap. This allows XAP to execute SQL based queries extremely efficiently even across large number of nodes. All XAP Data Grid APIs are supported including distributed transactions, leasing (Time To live) , FIFO , batch operations , etc. All clustering topologies supported. All client side cache options are supported.

# BlobStore Configuration

The off-heap BlobStore settings includes the following options:


| Property               | Description                                               | Default | Use |
|:-----------------------|:----------------------------------------------------------|:--------|:--------|
| <nobr>write-only-block-percentage</nobr> | Specifies a maximum threshold for off-heap memory use. If the space containers off-heap memory usage exceeds this threshold, a `BlobStoreMemoryShortageException` is thrown. | 80 | optional |

The IMDG BlobStore settings includes the following options:{{<wbr>}}


| Property | Description | Default | Use |
|:---------|:------------|:--------|:--------|
| blob-store-handler | BlobStore implementation |  | required |
| <nobr>cache-entries-percentage</nobr> | On-Heap cache stores objects in their native format. This cache size determined based on the percentage of the GSC JVM max memory(-Xmx). If `-Xmx` is not specified the cache size default to `10000` objects. This is an LRU based data cache.| 20% | optional |
| avg-object-size-KB |  Average object size. | 5KB | optional |


# JVM Configuration
Configure the maximum off-heap(direct) memory that the JVM will allocate: -XX:MaxDirectMemorySize=100G, off-heap(direct) memory is separate from the JVM heap allocated by -Xmx. 
The value allocated by `-XX:MaxDirectMemorySize` must not exceed physical RAM, and should likely to be less than total available RAM.
Default value of `-XX:MaxDirectMemorySize` is depends on your JVM version, Oracle Java default is 64mb.

Configuring an IMDG (Space) with BlobStore should be done via the `MapDBBlobStoreDataPolicyFactoryBean`, or the `MapDBBlobStoreConfigurer`. For example:


```java
GSC_JAVA_OPTIONS="-server -Xms20g -Xmx20g -XX:MaxDirectMemorySize=100g -Xmn6g -XX:+UseG1GC"; export GSC_JAVA_OPTIONS
```

# PU Configuration
{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/mapdb-blob-store"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/mapdb-blob-store http://www.openspaces.org/schema/{{% currentversion %}}/mapdb-blob-store/openspaces-mapdb-blobstore.xsd">


    <blob-store:mapdb-blob-store id="offheapBlobstore" write-only-block-percentage="80"/>

    <os-core:space id="space" url="/./myDataGrid" lookup-groups="${user.name}">
        <os-core:blob-store-data-policy blob-store-handler="offheapBlobstore" cache-entries-percentage="0"
                                        avg-object-size-KB="5"/>
    </os-core:space>


    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
{{% /tab %}}

{{%tab "  Code "%}}

Programmatic approach to start a BlobStore space:


```java
String spaceURL = "/./mySpace";
MapDBBlobStoreConfigurer configurer = new MapDBBlobStoreConfigurer();
configurer.setBlobStoreWriteOnlyBlockPercentage(80);
MapDBBlobStoreHandler  mapDBBlobStoreHandler = configurer.create();

BlobStoreDataCachePolicy cachePolicy = new BlobStoreDataCachePolicy();
cachePolicy.setAvgObjectSizeKB(5l);
cachePolicy.setCacheEntriesPercentage(20);
cachePolicy.setBlobStoreHandler(mapDBBlobStoreHandler);

UrlSpaceConfigurer urlConfig = new UrlSpaceConfigurer(spaceURL);
urlConfig.cachePolicy(cachePolicy);
GigaSpace gigaSpace = new GigaSpaceConfigurer(urlConfig.space()).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

The above example:

- Configures the off-heap BlobStore bean.
- Configures the Space bean (Data Grid) to use the blobStore implementation. 


# Space Class Configuration
By default any Space Data Type is `blobStore` enabled. When decorating the space class with its meta data you may turn off the `blobStore` behavior using the `@SpaceClass blobstoreEnabled` annotation or gs.xml `blobstoreEnabled` tag.

Here is a sample annotation disabling `blobStore` mode:


```java
@SpaceClass(blobstoreEnabled = false)
public class Person {
    .......
}
```

Here is a sample xml decoration for a POJO class disabling `blobStore` mode:

```xml
<gigaspaces-mapping>
    <class name="com.test.Person" "blobstoreEnabled"="false" >
     .....
     </class>
</gigaspaces-mapping>
```




