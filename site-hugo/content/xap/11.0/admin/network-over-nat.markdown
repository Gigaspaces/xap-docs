---
type: post110
title:  XAP Over NAT
categories: XAP110ADM
parent: network.html
weight: 450
canonical: auto
---

Some scenarios may include usage of XAP components where each component is  in a different private network, and the connection between the components needs to be on  a different public IP (i.e., via [network address translation](http://en.wikipedia.org/wiki/Network_address_translation).)

XAP components expose themselves via the built in remoting mechanism ([LRMI](./tuning-communication-protocol.html)) that contains the IP of the source component as part of its connection url. In the above case, the private IP has no meaning to a remote component/client that needs to connect to this component.

For instance, imagine a remote client which is trying to connect to a space that exists in a private network but has a public IP provided by the NAT mapping: the LRMI url would normally contain the private IP but has to use the public IP to actually connect. This page addresses how to solve this issue.

# Default NAT mapping using network_mapping.config

There is a built in mechanism for mapping private ips to public ones, the `/config/network_mapping.config`.

This file contains a list of translations, using the format:

    <private ip>:<private port>,<public ip>:<public port>

This file should be edited on the machine that is accessing the cluster via the public IP. For example, suppose you have a machine in a cluster behind a router with the private IP/port of 10.0.0.1:4000, with a public IP of 212.232.12.1:5000. This mapping should be placed at the remote client's `/config/network_mapping.config` file as a separate line in the following format:

    10.0.0.1:4000,212.232.12.1:5000

The mapping can be configured differently on each machine. If, for example, this space also needs to connect to the client and the client is also has a private IP, then the mapping file at the space machine should contain the mapping of the private to public ip of the client in the same manner.

In other words, each machine in the network needs to have a translation file for each _other_ machine it needs to connect to - if those machines use network-translated IPs.

The location of the mapping file (file should be located within the class path) can be overridden by using the following system property:

    com.gs.transport_protocol.lrmi.network-mapping-file=<file location>
.

# Custom mapping logic

You can implement your own custom mapping logic by specifying a class name that will be used to map addresses.

This is done by implementing the `com.gigaspaces.lrmi.INetworkMapper` interface:


```java
public interface INetworkMapper
{
    ServerAddress map(ServerAddress serverAddress);
}
```

This interface will be called each time a new connection will be created between the proxy to the service and the `map` method will be called with the given server address and the actual connection will be made using the return value of the method.
In order to use your own implementation you should have your own class placed in a jar in `/lib/platform/ext` and specify the class name via the following system property:

    com.gs.transport_protocol.lrmi.network-mapper=<class name>

Note that the system will construct the object with no arguments, so a null-argument constructor must be available.
