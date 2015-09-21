---
type: post
title:  Ensuring Accurate Time Across the Cluster
categories: SBP
weight: 2000
parent: production.html
---



{{% ssummary page %}} {{% /ssummary %}}

{{% tip %}}
**Summary:** This article illustrates how to ensure accurate time across the cluster <br/>
**Author:** John Burke<br/>
**Last Update:** October 2014<br/>
{{% /tip %}}



# Introduction

It is important that all the machines in the cluster have the same time with very little variation. The way computers are kept synchronized across networks is usually with the Network Time Protocol (NTP). This procotol stipulates the data that is transmitted to client machines connected to a time server, and defines a protocol for reducing the possible variation resulting from small differences in the amount of time it takes packets to travel from point A to point B.

Most modern operating systems have utilities for configuring computers to use timeservers to synchronize their clocks with very small margins of difference, usually a millisecond or less. Nevertheless large clusters may be better off using a single local timeserver which synchronizes itself to a small number of reliable external servers and then provides this time to the local cluster.



{{%refer%}}
The following link from [NIST](http://www.nist.gov/pml/div688/grp40/its.cfm) (US National Institute for Standards and Technology) indicates how to find the time utility on the three most popular desktop operating systems (if these aren't sufficient check the Time Server section for more thorough instructions):
{{%/refer%}}

In this instance the machine with which the client machines will synchronize will be a local timeserver. The system administrator should provide this hostname after determining which local machine will fill the role of the timeserver. In the event that a particular machine is a server and not a client, it will nevertheless obtain its time from the local timeserver, but the configuration will be located in a configuration file, described below in the 'Time Server' section.

# Time Server


### Linux

The simplest way is use a pool. Most linux distributions will install the NTP utility with a pre-defined list of pool servers. The following is an example, taken from the 'server' section of ntp.conf on a Ubuntu machine.


```bash
# Use servers from the NTP Pool Project. Approved by Ubuntu Technical Board
# on 2011-02-08 (LP: #104525). See http://www.pool.ntp.org/join.html for
# more information.
server 0.ubuntu.pool.ntp.org
server 1.ubuntu.pool.ntp.org
server 2.ubuntu.pool.ntp.org
server 3.ubuntu.pool.ntp.org

# Use Ubuntu's ntp server as a fallback.
server ntp.ubuntu.com
```


### Microsoft Windows

Those using Microsoft Windows operating systems should follow the instructions [here](http://support2.microsoft.com/kb/816042).

### Apple Macintosh

Comprehensive instructions for Apple Macintosh users can be found here [{{%pdf%}}](http://tf.nist.gov/service/pdf/macintosh.pdf).

Users of all operating systems can decide to use time server pools, even if their operating system doesn't configure them by default. To do so simply follow [these instructions](http://www.pool.ntp.org/en/use.html) (Note: it contains more explicit instructions for Windows users)




# Advanced Configuration 

The more advanced method is to find a small number of time servers which are very close to the cluster in terms of network hops and latency, and ideally have a very low deviation of latency. The following [site](http://support.ntp.org/bin/view/Servers/WebHome) contains a list of global timeservers, both those belonging to pools and non-associated servers, with an indication as to whether the server allows public access in the case of non-pool servers.



The list itself is sorted by ISO country code. Find a half dozen servers that are close to your location, and ping them to find the three or four best. Comment out any existing server declarations in the ntp.conf file (usually found in /etc), and add these timeservers in the same manner, ie. 'server <timeserver>'. Save the file and restart the ntp daemon ('ntpd' - usually '/etc/init.d/ntpd restart'). Wait 15 minutes or so and execute 'ntpdc -p', which will list all the upstream peers of your cluster's timeserver. If there are any whose strata is '16', remove them, as they have not synchronized.

For all other cluster machines comment out the server entries and add only one, with the name of the local timeserver.

{{%refer%}}
For the curious, the [NIST](http://www.nist.gov/pml/div688/what-time.cfm) maintains a page explaining timekeeping in general, and on the Internet in particular.
{{%/refer%}}
