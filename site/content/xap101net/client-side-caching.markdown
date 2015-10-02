---
type: post101net
title:  Client Side Caching
categories: XAP101NET
parent: none
weight: 1000
---

<br>

XAP supports client side caching of space data within the client application's JVM. When using client-side caching, the user essentially uses a two-layer cache architecture: The first layer is stored locally, within the client's JVM, and the second layer is stored within the remote master space. The remote master space may be used with any of the supported deployment topologies.


<br>

{{%fpanel%}}

[Local Cache](./local-cache.html){{%wbr%}}
A local cache allows the client application to cache recently used data at the client memory address and have it updated automatically by the space when that data changes.

[Local View](./local-view.html){{%wbr%}}
A Local View allows the client application to cache specific data based on client's criteria at the client memory address and have it updated automatically by the space when that data changes.

{{%/fpanel%}}

<br>

#### Additional Resources

- For a detailed description of the different caching scenarios please consult the [Product Overview](/product_overview/caching-scenarios.html)





