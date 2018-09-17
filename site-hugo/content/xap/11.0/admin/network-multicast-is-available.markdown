---
type: post110
title:  Is Multicast Available
categories: XAP110ADM
parent: network-multicast.html
weight: 300
---

{{% ssummary %}}{{% /ssummary %}}

Used to determine whether multicast is available.

# Syntax

    gs> admin multicastTest [variable[variable]]

# Description

The admin `multicastTest` utility is used to determine whether multicast is available.

It determines whether a multicast socket can be created and a request announcement can be sent.

{{% tip %}}
It is still possible that multicast does not work outside the tested host.

Make sure your network and machines running GigaSpaces are configured to have multicast enabled.
See the [How to Configure Multicast](./network-multicast.html) section for details on how to enable multicast.
{{% /tip %}}

This multicast utility serves to check multicast communication between sender and receiver running on different machines. You should run this utility from two different shells, where one should run from the sender machine and the other from the receiver machine. The sender machine should display the round trip reply time from all receiver machines. If multicast is configured properly on your machine and network, you should see the following message in your sender machine:

    Reply from 192.138.120.56:5558 bytes=100 time=1.539ms

{{% tip %}}
For multi-homed network card machine use `multicastTest -sender -ba [network card IP]`.
{{% /tip %}}

{{% refer %}}
To configure Jini in a multi network environment see the [Multi Network Card Configuration Section](./network-multi-nic.html).
{{% /refer %}}

# Options


| Option | Description | Value Format |
|:-------|:------------|:-------------|
| <nobr>sender / receiver<nobr> | Specifies whether you are the sender or the receiver -- `-sender` is used by the source machine, and `-receiver` is used by each target machine. | |
| ba | Binding address. Useful for multi homed hosts. | `localhost` |
| ma | Multicast address. | |
| t | Time to live for multicast packets. | |
| verbose | Print out the received messages. | |

# Example

Run the following at the sender machine:

    admin multicastTest -sender -ba localhost -verbose

Run the following at the receiver machine:

    admin multicastTest -receiver -ba localhost -verbose

The sender should display the following:


```bash
<XAP Root>\bin\gs>admin multicastTest -sender -ba localhost  -verbose
JAVA_HOME environment variable is set to D:\JDK\jdk1.5.0_04 in "<XAP Root>\bin\setenv.bat"
Environment set successfully from c:\GigaSpacesXAP6.0\bin
  ..
Starting Multicast-Sender...
Started MulticastSocket=/224.0.0.150:5558, ack-reply port: 5559, ttl=1, bind interface=/127.0.0.1, eventSize=100

---------- [SENDER MACHINE HOST] NETWORK INTERFACE INFO -----------
Names: lo / MS TCP Loopback interface
        Address: 127.0.0.1
Names: eth0 / Broadcom NetXtreme Gigabit Ethernet - Packet Scheduler Miniport
        Address: 192.168.10.178
Names: eth1 / Bluetooth LAN Access Server Driver - Packet Scheduler Miniport
        Address: 169.254.111.108
---------- [SENDER MACHINE HOST] NETWORK INTERFACE INFO -----------
Reply from 127.0.0.1:5558 bytes=100 time=2.869079ms
Reply from 127.0.0.1:5558 bytes=100 time=0.807924ms
Reply from 127.0.0.1:5558 bytes=100 time=0.744508ms
Reply from 127.0.0.1:5558 bytes=100 time=0.876089ms
```

The receiver should display the following:


```bash
<XAP Root>\bin\gs>admin multicastTest -receiver -ba localhost  -verbose
JAVA_HOME environment variable is set to D:\JDK\jdk1.5.0_04 in "<XAP Root>\bin\setenv.bat"
Environment set successfully from c:\GigaSpacesXAP6.0\bin
  ..
Starting Multicast-Receiver...

---------- [RECEIVER MACHINE HOST] NETWORK INTERFACE INFO -----------
Names: lo / MS TCP Loopback interface
        Address: 127.0.0.1
Names: eth0 / Broadcom NetXtreme Gigabit Ethernet - Packet Scheduler Miniport
        Address: 192.168.10.178
Names: eth1 / Bluetooth LAN Access Server Driver - Packet Scheduler Miniport
        Address: 169.254.111.108
---------- [SENDER MACHINE HOST] NETWORK INTERFACE INFO -----------
Received from [sender=127.0.0.1:5559] packet size: 100 bytes
Received from [sender=127.0.0.1:5559] packet size: 100 bytes
Received from [sender=127.0.0.1:5559] packet size: 100 bytes
Received from [sender=127.0.0.1:5559] packet size: 100 bytes
```
