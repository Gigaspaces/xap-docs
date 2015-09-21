---
type: post97adm
title:  Port usage Control
categories: XAP97ADM
parent: network.html
weight: 700
---



XAP space and client components open different ports in various situations. The following list describes the different ports used by XAP and how these can be modified:

{{% refer %}}Learn how to **[set XAP over a firewall](./network-over-firewall.html)**.{{% /refer %}}


| Service | Description | Configuration Property| Default value |
|:--------|:------------|:----------------------|:--------------|
|LRMI listening port|Used with client-space and space-space communication. |`com.gs.transport_protocol.lrmi.bind-port` System property. |variable , random|
|RMI registry listening port |Used as an alternative directory service.| `com.gigaspaces.system.registryPort` System property|10098 and above.|
|RMI registry Retries |Used as an alternative directory service.| `com.gigaspaces.system.registryRetries` System property|Default is 20.|
|Webster listening port|Internal web service used as part of the application deployment process. |`com.gigaspaces.start.httpPort` System property|9813|

- When starting a space and providing the port as part of the URL - i.e. `java://localhost:PORT/container/space` - the specified port will be used both for the RMI registry listener and also for the container to register into the RMI registry.
- The Jini Lookup Service uses unicast and multicast announcements and requests.
- The **multicast** discovery protocol uses ports 4170.
- You can **completely disable multicast announcement traffic**. Refer to the [Lookup Service Configuration](./network-lookup-service-configuration.html) or [Setting XAP Over Firewall](./network-over-firewall.html) sections for more details.
- When running a clustered space using replication via multicast, additional ports are used.

