---
type: post123
title:  Client Side Caching
categories: XAP123, PRM
parent: none
weight: 1300
---




XAP supports client side caching of space data within the client application's JVM. When using client-side caching, the user essentially uses a two-layer cache architecture: The first layer is stored locally, within the client's JVM, and the second layer is stored within the remote master space. The remote master space may be used with any of the supported deployment topologies.


**Dependencies**<br>
In order to use this feature, include the `${XAP_HOME}/lib/optional/near-cache/xap-near-cache.jar` and `${XAP_HOME}/lib/optional/near-cache/xap-near-cache-spring.jar` files on your classpath or use maven dependencies:

```xml
<dependency>
    <groupId>com.gigaspaces</groupId>
    <artifactId>xap-near-cache</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
<dependency>
    <groupId>com.gigaspaces</groupId>
    <artifactId>xap-near-cache-spring</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
```
{{%refer%}}
For more information on dependencies see [Maven Artifacts](../started/maven-artifacts.html)
{{%/refer%}} 


<br>

{{%fpanel%}}

[Local Cache](./local-cache.html){{<wbr>}}
A local cache allows the client application to cache recently used data at the client memory address and have it updated automatically by the space when that data changes.

[Local View](./local-view.html){{<wbr>}}
A Local View allows the client application to cache specific data based on client's criteria at the client memory address and have it updated automatically by the space when that data changes.

[Client caching over the WAN](./client-side-caching-over-the-wan.html){{<wbr>}}
Client caching over the WAN.

[Monitoring client side cache](./monitoring-the-client-side-cache.html){{<wbr>}}
Monitoring the Local View/Cache.

{{%/fpanel%}}

<br>

#### Additional Resources

- For a detailed description of the different caching scenarios please consult the [Product Overview](../overview/caching-scenarios.html)

- For more advanced WAN based topologies see the [Multi-Site Replication over the WAN](./multi-site-replication-overview.html) section.



