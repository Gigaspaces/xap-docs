---
type: post110
title:  Client Side Caching
categories: XAP110
parent: none
weight: 1300
---




XAP supports client side caching of space data within the client application's JVM. When using client-side caching, the user essentially uses a two-layer cache architecture: The first layer is stored locally, within the client's JVM, and the second layer is stored within the remote master space. The remote master space may be used with any of the supported deployment topologies.


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

- For a detailed description of the different caching scenarios please consult the [Product Overview](/product_overview/caching-scenarios.html)

- For more advanced WAN based topologies see the [Multi-Site Replication over the WAN](./multi-site-replication-over-the-wan.html) section.



