---
type: post100adm
title:  Multicast Configuration
categories: XAP100ADM
parent: network.html
weight: 200
---

{{% ssummary %}} {{% /ssummary %}}



Multicast is the delivery of information to a group of destinations simultaneously, using the most efficient strategy to deliver messages over each link of the network only once, and create copies only when the links to the destinations split.

The word "multicast" is typically used to refer to IP Multicast, the implementation of the multicast concept on the IP routing level, where routers create optimal spanning tree distribution paths for data grams sent to a multicast destination address in realtime. However, there are also other implementations of the multicast distribution strategy listed below.
(Source - wikipedia: [http://en.wikipedia.org/wiki/Multicast](http://en.wikipedia.org/wiki/Multicast)).

GigaSpaces uses multicast in the following cases:

- [When deploying to the service grid]({{%currentjavaurl%}}/deploying-onto-the-service-grid.html) GigaSpaces XAP uses multicast to discover the [Lookup Service](./network-lookup-service-configuration.html ), and register their proxies.
- Clients use multicast to discover the [Lookup Service](./network-lookup-service-configuration.html ) and look up a matching service proxy (such as the space).

{{% tip title="What should I do in order to determine if multicast is enabled on my environment? "%}}
Refer to the [How to Determine Whether Multicast is Available](./network-multicast-is-available.html) section for more details.
{{% /tip %}}

To enable the important capabilities above, you should enable multicast on machines running clients, spaces or services.

{{% tip title="What should I do if I can't enable multicast? "%}}

- If you cannot enable multicast in your environment, you can use unicast discovery to allow services and clients to locate the Lookup Service.
- Space cluster replication uses unicast by default. You should use multicast replication when having more than 10 clients acting as replica spaces per target space.
{{% /tip %}}

{{% note %}}
In case you want to **disable the Jini Lookup Service Multicast announcements** please refer to [this](./network-lookup-service-configuration.html#Multicast Settings) section in the Wiki.
{{%/note%}}

# To Multicast or to not Multicast?

Multicast is not a mandatory with XAP. It is used as a secondary mechanism for lookup service discovery. Unicast is the other mechanism. Both are turned on by default on the client side and on the service grid side (GSM,GSC,LUS).

Multicast is not used with replication , notification , monitoring or any client activity against the space. Disabling multicast discovery means you are loosing dynamicy for the lookup service location. This means if the lookup service fails (very low probably to happen) you will be able to start it only on the machines listed on the `LOOKUPLOCATORS` list. Client `locators` should have the same list used.

When multicast is disabled global lookup service should not be use. You should use only local lookup service configuration with the agent with the machines running the lookup service:

```bash
gs-agent gsa.global.lus 0 gsa.lus 1 gsa.gsm.global 0 gsa.gsm 1
```

Machines not running the lookup service should have their agent started using:

```bash
gs-agent gsa.global.lus 0 gsa.lus 0 gsa.gsm.global 0 gsa.gsm 0
```

The GSC count should be added to the commands above above as usual.

If you can't have multicast enabled within your network you should disable it on the client side and on the service grid side. It will save some CPU activity performed continuously.

# Configuring Multicast on Linux

In some cases, Linux distributions do not have multicast enabled by default and the `/etc/hosts` file does not include the IP address associated with the server's hostname. An error that is frequently met is: `hostname associated with the localhost` in `/etc/hosts`. The machine's hostname in `/etc/hosts` should be associated with the IP address set to the server's network interface, or to the external static NAT IP address of the server (the address clients should connect to).
Make sure the `/etc/hosts` has the machine's IP, together with the IP: `127.0.0.1`.


```bash
127.0.0.1        localhost
192.168.10.127   Mylinux
```

{{% tip %}}
Make sure all network machines can ping each other. You might need to list all the network machines' IPs as part of each machine's `hosts` file, or have a DHCP server configured.
{{% /tip %}}

Before running GigaSpaces, make sure your network interface supports multicast, and the appropriate routes are properly configured.

To check if your network interface supports multicast, run the following `ifconfig`:


```bash
$ /sbin/ifconfig -a
eth0      Link encap:Ethernet  HWaddr 00:30:48:2E:67:BA
          inet addr:192.168.10.127  Bcast:192.168.10.255  Mask:255.255.255.0
          inet6 addr: fe80::230:48ff:fe2e:67ba/64 Scope:Link
          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
          RX packets:89049707 errors:34 dropped:0 overruns:0 frame:34
          TX packets:79402911 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1000
          RX bytes:24029278032 (22.3 GiB)  TX bytes:52438225932 (48.8 GiB)
          Base address:0x3000 Memory:dc300000-dc320000

eth1      Link encap:Ethernet  HWaddr 00:30:48:2E:67:BB
          inet addr:192.168.0.1  Bcast:192.168.0.255  Mask:255.255.255.0
          inet6 addr: fe80::230:48ff:fe2e:67bb/64 Scope:Link
          UP BROADCAST MULTICAST  MTU:1500  Metric:1
          RX packets:0 errors:0 dropped:0 overruns:0 frame:0
          TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1000
          RX bytes:0 (0.0 b)  TX bytes:0 (0.0 b)
          Base address:0x3040 Memory:dc320000-dc340000

lo        Link encap:Local Loopback
          inet addr:127.0.0.1  Mask:255.0.0.0
          inet6 addr: ::1/128 Scope:Host
          UP LOOPBACK RUNNING  MTU:16436  Metric:1
          RX packets:1366770822 errors:0 dropped:0 overruns:0 frame:0
          TX packets:1366770822 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:0
          RX bytes:319301580062 (297.3 GiB)  TX bytes:319301580062 (297.3 GiB)

sit0      Link encap:IPv6-in-IPv4
          NOARP  MTU:1480  Metric:1
          RX packets:0 errors:0 dropped:0 overruns:0 frame:0
          TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:0
          RX bytes:0 (0.0 b)  TX bytes:0 (0.0 b)
```

If the `MULTICAST` attribute in the fourth line of the `eth0` properties is not present, it's possible that your kernel has not been compiled with multicast support. Before re-compiling the kernel, try enabling multicast on your network interface (`eth0` in this case) via `ifconfig`:


```bash
$ /sbin/ifconfig etho multicast
```

To check that multicast routing is configured, run the following `route` command:


```bash
$ /sbin/route -n
Kernel IP routing table
Destination     Gateway         Genmask         Flags Metric Ref    Use Iface
192.168.0.0     0.0.0.0         255.255.255.0   U     0      0        0 eth1
192.168.10.0    0.0.0.0         255.255.255.0   U     0      0        0 eth0
169.254.0.0     0.0.0.0         255.255.0.0     U     0      0        0 eth1
224.0.0.0       0.0.0.0         240.0.0.0       U     0      0        0 eth0
0.0.0.0         192.168.10.1    0.0.0.0         UG    0      0        0 eth0
```

If the destination `224.0.0.0` entry is not present, you need to enable multicast routing.

To enable multicast routing, run the following `route` command as **root**:


```bash
$ /sbin/route -n add -net 224.0.0.0 netmask 240.0.0.0 dev eth0
```

{{% tip %}}
On Redhat systems you can configure this route statically via the network setup configuration tools -- see:
[redhat - Chapter 6. Network Configuration](http://www.redhat.com/docs/manuals/linux/RHL-7.2-Manual/custom-guide/ch-network-config.html).
{{% /tip %}}

Alternatively, try executing the command above as part of a startup script in `/etc/rc.d/\*`.

{{% tip %}}
For more details on Linux `ifconfig` and `route` commands, refer to:

- [http://linux.about.com/od/commands/l/blcmdl8_ifconfi.htm](http://linux.about.com/od/commands/l/blcmdl8_ifconfi.htm)
- [http://linux.about.com/od/commands/l/blcmdl8_route.htm](http://linux.about.com/od/commands/l/blcmdl8_route.htm)
{{% /tip %}}

{{% tip %}}
[tcpdump](http://www.tcpdump.org/) is another useful command which dumps traffic on a network:


```bash
tcpdump -i eth0 ip multicast
```

{{% /tip %}}

{{% tip %}}
 By default multicast is not allowed between the virtual machines, so the unicast should be configured instead
{{% /tip %}}

# Configuring Multicast on Windows

To enable multicasting from a token ring on a Windows® 2000 workstation to any Windows 98/NT machine, set the `TrFunctionalMcastAddress` parameter to `0` in the Windows 2000 registry:

1. Click **Start* > *Run** on the Windows 2000 taskbar.
1. In the **Open** field, select or type **REGEDIT**.
1. Click **OK**. The **Registry Editor** window opens.
1. Click **HKEY_LOCAL_Machine** > **SYSTEM** > **CurrentControlSet** > **Services** > **Tcpip** > **Parameters**.
1. Right-click **TrFunctionalMcastAddress**, and click **Modify**. The **Edit DWORD Value** window opens.
1. In the **Value** data field, type `0`.
1. Click **OK** to save changes and exit.
1. Close the **Registry Editor**.

# Configuring Multicast Scope Time-To-Live (TTL) Value

The **[multicast Time-To-Live (TTL)](http://en.wikipedia.org/wiki/Time_to_live)** value specifies the number of routers (hops) that multicast traffic is permitted to pass through before expiring on the network. For each router (hop), the original specified TTL is decremented by one (1). When its TTL reaches a value of zero (0), each multicast datagram expires and is no longer forwarded through the network to other subnets.

The problem of multicasts/broadcasts not passing the router/switch is a well known issue - most routers (Cisco, 3Com, etc) have multicast forwarding disabled by default - otherwise the networks will be flooded with packets coming from very distant locations. To get it delivered all over the globe takes below 30 hops, so TTL 20 means delivery to more than half of it. It is very common that network experts in large networks hate the flooding problem caused by multicasts/broadcasts sent with the large TTL, and block it.

# Packet Sniffer/Network Analyzer Tool

[Wireshark (formerly Ethereal)](http://www.wireshark.org/) - accumulates years of network analyzing experience and is far more mature and known than other tools. It is a cross-platform packet sniffer/network analyzer tool (used both in Windows and Unix/Linux). It allows you to examine data from a live network, or from a capture file on disk. You can interactively browse the capture data, viewing summary and detail information for each packet. It has several powerful features, including a rich display filter language and the ability to view the reconstructed stream of a TCP session.

{{% info %}}
**To find TTL**, you should monitor some traffic (start-stop on the proper interface), in the monitoring log. Choose the packet you are interested in, and look at its IP layer - TTL (and other parameters) are shown.

The **default TTL value is 3** (was 15). See [Multicast Settings](./network-lookup-service-configuration.html#LookupServiceConfiguration-MulticastSettings) section for details of how to modify that value.
{{%/info%}}
