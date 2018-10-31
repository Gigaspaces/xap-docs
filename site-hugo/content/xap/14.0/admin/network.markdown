---
type: post140
title:  Network Configuration
categories: XAP140ADM, OSS
parent: none
weight: 800
---


{{% note "Note" %}}
Some of the features described on this page are not part of the open-source edition, and are only available in the licensed editions (starting with XAP Premium).
{{% /note %}}


This section explains how to configure the network to support your XAP implementation. XAP supports [unicast service discovery](./network-unicast-discovery.html) and [multicast](./network-multicast.html) where available (for both Linux and Microsoft Windows operating systems).

You can [configure the Lookup Service (LUS)](./network-lookup-service-configuration.html) to work with both unicast and multicast. XAP is supported [over NAT](./network-over-nat.html) and [over firewall](./network-over-firewall.html), and you can either [use the default ports or specify custom ports](./network-ports.html) for the different XAP components and services. XAP can also be configured to work with a specific subset of network cards in environments with [multiple network cards](./network-multi-nic.html). 


