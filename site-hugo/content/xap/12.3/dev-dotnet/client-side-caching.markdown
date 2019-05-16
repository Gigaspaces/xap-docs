---
type: post123
title:  Client-Side Caching
categories: XAP123NET, PRM
parent: none
weight: 1000
canonical: auto
---


XAP supports client-side caching of Space data within the client application's JVM. When using client-side caching, the user essentially uses a two-layer cache architecture: The first layer is stored locally, within the client's JVM, and the second layer is stored within the remote master Space. The remote master Space may be used with any of the supported deployment topologies.


The following caching options are available:

- [Local Cache](./local-cache.html) - A local cache allows the client application to cache recently used data at the client memory address, and have it updated automatically by the Space when that data changes.
- [Local View](./local-view.html) - A Local View allows the client application to cache specific data based on client's criteria at the client memory address, and have it updated automatically by the Space when that data changes.


# Additional Resources

For a detailed description of the different caching scenarios, refer to [Caching Scenarios](../overview/caching-scenarios.html) in the Product Overview.





