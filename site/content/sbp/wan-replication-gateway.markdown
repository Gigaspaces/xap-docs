---
type: postsbp
title:  WAN Multi-Master Replication Gateway
parent: wan-based-deployment.html
categories: SBP
weight: 100
---

{{% ssummary page %}}WAN Multi-Master replication example{{% /ssummary %}}


{{% tip %}}
**Author**: Shay Hassidim, Deputy CTO GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 8.0.3<br/>


{{% /tip %}}

# Overview

This [WAN Gateway]({{%latestjavaurl%}}/multi-site-replication-over-the-wan.html) example includes PU folders with config files for a [Multi-Master]({{%latestjavaurl%}}/multi-site-replication-over-the-wan.html#Multi-Master Topology) topology that includes 3 sites: DE , RU , US. Each site have an independent cluster and a Gateway.
![wan_example1.jpg](/attachment_files/sbp/wan_example1.jpg)

You will find folders for the following PUs:

- wan-gateway-DE - Deployed into **DE** zone, using the **DE** lookup group and a lookup service listening on port 4366.
- wan-gateway-RU - Deployed into **RU** zone, using the **RU** lookup group and a lookup service listening on port 4166.
- wan-gateway-US - Deployed into **US** zone, using the **US** lookup group and a lookup service listening on port 4266.
- wan-space-DE - Deployed into **DE** zone, using the **DE** lookup group and a lookup service listening on port 4366.
- wan-space-RU - Deployed into **RU** zone, using the **RU** lookup group and a lookup service listening on port 4166.
- wan-space-US - Deployed into **US** zone, using the **US** lookup group and a lookup service listening on port 4266.

The internal architecture of the setup includes a clustered space and a Gateway, where each Gateway includes a Delegator and a Sink:

[<img src="/attachment_files/sbp/wan_example2.jpg" width="140" height="100">](/attachment_files/sbp/wan_example2.jpg)

# Installing the Example

Step 1. Download the [WAN_Gateway_example.zip](https://github.com/Gigaspaces/wan-gateway-examples/archive/master.zip). It includes two folders: **deploy** and **scripts**. View on [GitHub](https://github.com/Gigaspaces/wan-gateway-examples/tree/master/WAN_Replication_MultiMaster)

Step 2. Please extract the file and and copy the content of the **deploy folder** into `\gigaspaces-xap-premium-{{%latestxaprelease%}}-ga\deploy` folder. It should looks like this:


```java
Directory of D:\gigaspaces-xap-premium-{{%latestxaprelease%}}-ga\deploy

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

Step 3. Please move into the `scripts` folder and edit the `setExampleEnv.bat/sh` to include correct values for `NIC_ADDR` as the machine IP and `GS_HOME` to have GigaSpaces root folder location.

# Running the Example
You will find within the `scripts` folder running scripts to start [Grid Service Agent](/product_overview/service-grid.html#gsa) for each site and a deploy script for all sites. This will allow you to run the entire setup on one machine to test. Here are the steps to run the example:

1. Run `startAgent-DE.bat/sh` or to start DE site.
2. Run `startAgent-RU.bat/sh` to start RU site.
3. Run `startAgent-US.bat/sh` to start US site.
4. Run `deployAll.bat/sh` file to deploy all the PUs listed above.

# Viewing the Clusters

- Start the `\gigaspaces-xap-premium-{{%latestxaprelease%}}-ga\bin\GS-UI.bat/sh`.
- Once you deployed make sure you enable the relevant groups within the GS-UI:
![wan_example3.jpg](/attachment_files/sbp/wan_example3.jpg)

You should check all Groups:
![wan_example4.jpg](/attachment_files/sbp/wan_example4.jpg)

You should see this:
[<img src="/attachment_files/sbp/wan_example5.jpg" width="140" height="100">](/attachment_files/sbp/wan_example5.jpg)

Once deployed successfully you should see this:
[<img src="/attachment_files/sbp/wan_example6.jpg" width="140" height="100">](/attachment_files/sbp/wan_example6.jpg)

# Testing the WAN Gateway Replication
You can test the setup by using the [benchmark utility]({{%latestadmurl%}}/benchmark-browser.html) comes with the GS-UI. Move the one of the Clusters Benchmark icon and click the Start Button:
[<img src="/attachment_files/sbp/wan_example7.jpg" width="140" height="100">](/attachment_files/sbp/wan_example7.jpg)

You will see all spaces **Object Count** across all clusters by clicking the **Spaces icon** on the Space Browser Tab. You should see identical number of objects (5000) for all members:
[<img src="/attachment_files/sbp/wan_example8.jpg" width="140" height="100">](/attachment_files/sbp/wan_example8.jpg)

You can remove objects from each space cluster by selecting the **Take operation** and click Start:
[<img src="/attachment_files/sbp/wan_example9.jpg" width="140" height="100">](/attachment_files/sbp/wan_example9.jpg)

You will see the Object Count changing having zero object count for each space:
[<img src="/attachment_files/sbp/wan_example10.jpg" width="140" height="100">](/attachment_files/sbp/wan_example10.jpg)

# Replication Throughput Capacity

The total TP a gateway can push out into remote sites depends on:

- Network speed
- Partition count
- Partition activity Distribution
- Partition TP
- Replication Frequency
- Replication packet size
- Network bandwidth
- Replication Meta data size

The total TP will be:


```java
Total TP = (Partition TP X Partitions count X Distribution X Network Speed)+ Replication Meta data size / Replication Frequency
```

If we have 10 IMDG partitions, each sending 5000 objects/sec 1K size to the GW with a replication frequency of 10 replication cycles per/sec (100 ms delay between each replication cycle , i.e. 1000 operations per batch) with even distribution (1) and network speed between the sites is 10 requests/sec (i.e. 100 ms latency) theÂ Total TPÂ we will have is: (10 X 5000 X 1 X 10) / 10 = 50,000 objects per second. = 50M per second
Â 
The above assumes the network bandwidth is larger than 50M.

# WAN Gateway Replication Benchmark

With the following benchmark we have 2 sites; one located in the US East coast EC2 region and another one located within EC2 EU Ireland region. The latency between the sites is 95 ms and the maximum bandwidth measured is 12MByte/sec.

A client application located within the US East coast EC2 region running multiple threads perform continuous write operation to a clustered space in the same region. The space cluster within the US East coast EC2 region has a WAN Gateway configured , replicating data to a clustered space running within the EC2 EU Ireland region via a Gateway running within the same region.

![wan_bench1.jpg](/attachment_files/sbp/wan_bench1.jpg)

{{% color blue %}}Blue line{{% /color %}} - The amount of data generated at the source site (EC2 EU East coast region) by the client application.

{{% color green %}}Green line{{% /color %}}- The amount of consumed bandwidth is measured  at the target site (EC2 EU Ireland region).

{{% color red %}}Red line{{% /color %}} - The network bandwidth.

![wan_bench2.jpg](/attachment_files/sbp/wan_bench2.jpg)

Up to 16 client threads at the client application, the utilized bandwidth at the target site is increasing. Once the maximum bandwidth has been consumed, no matter how many client threads will be writing data to the source space, the target site bandwidth consumption will stay the same.

We do see some difference between the amount of data generated and replicated at the source site and the amount of bandwidth consumed at the target site. This difference caused due-to the overhead associated with the replicated data over the WAN and the network latency. For each replicated packet some meta data is added. It includes info about the order of the packet, its source location, etc.
