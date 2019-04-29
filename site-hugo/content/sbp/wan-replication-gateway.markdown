---
type: post
title:  WAN Multi-Master Replication Gateway
parent: wan-based-deployment.html
categories: SBP
weight: 100
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim| 8.0.3| April 2013|    |    |


# Overview

This [WAN Gateway](https://docs.gigaspaces.com/latest/dev-java/multi-site-replication-over-the-wan.html) example includes PU folders with configuration files for a [Multi-Master](https://docs.gigaspaces.com/latest/dev-java/multi-site-replication-over-the-wan.html#Multi-Master Topology) topology that includes 3 sites: DE, RU, and US. Each site has an independent cluster and a gateway.

![wan_example1.jpg](/attachment_files/sbp/wan_example1.jpg)

You will find folders for the following PUs:

- wan-gateway-DE - Deployed to the **DE** zone, using the **DE** lookup group and a lookup service listening on port 4366.
- wan-gateway-RU - Deployed to the **RU** zone, using the **RU** lookup group and a lookup service listening on port 4166.
- wan-gateway-US - Deployed to the **US** zone, using the **US** lookup group and a lookup service listening on port 4266.
- wan-space-DE - Deployed to the **DE** zone, using the **DE** lookup group and a lookup service listening on port 4366.
- wan-space-RU - Deployed to the **RU** zone, using the **RU** lookup group and a lookup service listening on port 4166.
- wan-space-US - Deployed to the **US** zone, using the **US** lookup group and a lookup service listening on port 4266.

The internal architecture of the setup includes a clustered space and a gateway, where each gateway includes a delegator and a sink:

[<img src="/attachment_files/sbp/wan_example2.jpg" width="140" height="100">](/attachment_files/sbp/wan_example2.jpg)

# Installing the Example

Step 1. Download the [WAN_Gateway_example.zip](https://github.com/Gigaspaces/wan-gateway-examples/archive/master.zip). It includes two folders: **deploy** and **scripts**. View on [GitHub](https://github.com/Gigaspaces/wan-gateway-examples/tree/master/WAN_Replication_MultiMaster)

Step 2. Extract the file and and copy the contents of the **deploy folder** to the `\gigaspaces-xap-premium-<MadCap:variable name="Versions.product-version-short" />}-ga\deploy` folder. It should looks like this:


```java
Directory of D:\gigaspaces-xap-premium-<MadCap:variable name="Versions.product-version-short" />-ga\deploy

09/11/2011  04:41 AM    <DIR>          .
09/11/2011  04:41 AM    <DIR>          ..
07/05/2011  03:08 PM    <DIR>          templates
09/11/2011  04:44 AM    <DIR>          wan-gateway-DE
09/11/2011  04:44 AM    <DIR>          wan-gateway-RU
09/11/2011  04:43 AM    <DIR>          wan-gateway-US
09/11/2011  04:43 AM    <DIR>          wan-space-DE
09/11/2011  05:15 AM    <DIR>          wan-space-RU
09/11/2011  04:42 AM    <DIR>          wan-space-US
```

Step 3. Open the `scripts` folder and edit the `setExampleEnv.bat/sh` to include the machine IP address for the `NIC_ADDR`,  and the GigaSpaces root folder location for `GS_HOME`.

# Running the Example

The `scripts` folder contains scripts that are used to start the [Grid Service Agent](/product_overview/service-grid.html#gsa) for each site, along with a deploy script for all sites. This allows you to run the entire setup on one machine to test. Here are the steps to run the example:

1. Run `startAgent-DE.bat/sh` to start the DE site.
2. Run `startAgent-RU.bat/sh` to start the RU site.
3. Run `startAgent-US.bat/sh` to start the US site.
4. Run `deployAll.bat/sh` file to deploy all the PUs listed above.

# Viewing the Clusters

- Run `\gigaspaces-xap-premium-<MadCap:variable name="Versions.product-version-short" />-ga\bin\GS-UI.bat/sh`.
- After deployment, make sure you enable the relevant groups via the GigaSpaces Management Center:

![wan_example3.jpg](/attachment_files/sbp/wan_example3.jpg)

- Check all the groups:

![wan_example4.jpg](/attachment_files/sbp/wan_example4.jpg)

They should look like this:

[<img src="/attachment_files/sbp/wan_example5.jpg" width="140" height="100">](/attachment_files/sbp/wan_example5.jpg)

After being deployed successfully, you should see this:

[<img src="/attachment_files/sbp/wan_example6.jpg" width="140" height="100">](/attachment_files/sbp/wan_example6.jpg)

# Testing the WAN Gateway Replication

You can test the setup using the [benchmark utility](https://docs.gigaspaces.com/latest/admin/benchmark-browser.html) that comes with the GigaSpaces Management Center. Move one of the Clusters Benchmark icons and click the Start button:

[<img src="/attachment_files/sbp/wan_example7.jpg" width="140" height="100">](/attachment_files/sbp/wan_example7.jpg)

To view all of the Spaces' **Object Count** across all clusters, click the **Spaces** icon in the Space Browser tab. You should see the identical number of objects (5000) for all members:

[<img src="/attachment_files/sbp/wan_example8.jpg" width="140" height="100">](/attachment_files/sbp/wan_example8.jpg)

You can remove objects from each Space cluster by selecting **Take operation** and clicking Start:

[<img src="/attachment_files/sbp/wan_example9.jpg" width="140" height="100">](/attachment_files/sbp/wan_example9.jpg)

You will see the object count change to zero for each Space:

[<img src="/attachment_files/sbp/wan_example10.jpg" width="140" height="100">](/attachment_files/sbp/wan_example10.jpg)

# Replication Throughput Capacity

The total throughput a gateway can push to remote sites depends on the following criteria:

- Network speed
- Partition count
- Partition activity distribution
- Partition throughput
- Replication frequency
- Replication packet size
- Network bandwidth
- Replication metadata size

The total throughput is calculated as follows:


```java
Total TP = (Partition TP X Partitions count X Distribution X Network Speed)+ Replication Meta data size / Replication Frequency
```

If there are 10 data grid partitions, each sending 5000 objects/sec of size 1K to the gateway, with a replication frequency of 10 replication cycles/sec (100 ms delay between each replication cycle, i.e. 1000 operations per batch), with even distribution (1) and a network speed between sites of 10 requests/sec (i.e. 100 ms latency), the total is (10 X 5000 X 1 X 10) / 10 = 50,000 objects per second = 50 M per second.
 
The above assumes the network bandwidth is larger than 50M.

# WAN Gateway Replication Benchmark

The following benchmark inlcludes 2 sites; one located on the US East coast (EC2 region) and another one located in the EC2 EU Ireland region. The latency between the sites is 95 ms and the maximum bandwidth measured is 12 MByte/sec.

A client application located on the US East coast (EC2 region), running multiple threads, performs continuous write operations to a clustered Space in the same region. The Space cluster in the US East coast EC2 region has a WAN Gateway configured, replicating data to a clustered sSpace running in the EC2 EU Ireland region via a gateway running in the same region.

![wan_bench1.jpg](/attachment_files/sbp/wan_bench1.jpg)


{{% text-blue %}}Blue line{{% /text-blue %}} - The amount of data generated at the source site (EC2 EU East coast region) by the client application.

{{% text-green %}}Green line{{% /text-green %}}- The amount of consumed bandwidth is measured  at the target site (EC2 EU Ireland region).

{{% text-red %}}Red line{{% /text-red %}} - The network bandwidth.


![wan_bench2.jpg](/attachment_files/sbp/wan_bench2.jpg)


Up to 16 client threads in the client application, the utilized bandwidth at the target site is increasing. After the maximum bandwidth has been consumed, no matter how many client threads write data to the source Space, the target site bandwidth consumption will stay the same.

We see some difference between the amount of data generated and replicated at the source site, and the amount of bandwidth consumed at the target site. This difference is due to the overhead associated with the replicated data over the WAN and the network latency. For each replicated packet, some metadata is added that includes information about the order of the packet, its source location, etc.
