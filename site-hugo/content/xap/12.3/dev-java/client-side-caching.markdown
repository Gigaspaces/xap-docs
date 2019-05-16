---
type: post123
title:  Client-Side Caching
categories: XAP123, PRM
parent: none
weight: 1300
canonical: auto
---




XAP supports client-side caching of Space data within the client application's JVM. When using client-side caching, the user essentially uses a two-layer cache architecture: The first layer is stored locally, within the client's JVM, and the second layer is stored within the remote master Space. The remote master Space may be used with any of the supported deployment topologies.


# Dependencies

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
For more information on dependencies, refer to [Maven Artifacts](../started/maven-artifacts.html).
{{%/refer%}} 


# Additional Resources

- For a detailed description of the different caching scenarios, refer to [Caching Scenarios](../overview/caching-scenarios.html) in the Product Overview.
- For more advanced WAN- based topologies see the [Multi-Site Replication over the WAN](./multi-site-replication-overview.html) section.



