---
type: post
title:  Distributed WAN Gateway 
categories: SBP
parent: wan-based-deployment.html
weight: 400
---

|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|:----------:|
| Shay  Hassidim| 12.0.1 | March 2017|    | {{%download "/download_files/sbp/wan-replication_multi_gw.zip"%}}   |


# Overview
 
By default, a single WAN / LAN gateway instance used to perform replication from a local space cluster into remote space cluster. 
For most systems, this will provide sufficient throughput to address the activity a single clustered space generating. 
There are cases with large space clusters or with systems that produce large volume of activity that require a distributed (multi-instance) gateway setup. 
The solution described here allows each partition (primary and backup instances) to replicate its activity via a dedicated gateway. 
This forms an architecture where each partition using a separate gateway instance that may run on a different machine, utilizing its full CPU and network bandwidth. 
This approach reducing the potential for redolog accumulation generating a lag between the source space and target space data as a result of a single gateway.

# Architecture

With the example below we will run a source space (New-York) with 2 partitions and a target space (London) with 3 partitions. A gateway running within New-York site will have 2 delegator instances , where the London site will run as well 2 gateway sink instances interacting with the London space cluster.

![image](/attachment_files/sbp/wan_gateway/distributed-wan-gateway-1.png)

# Example

**1.** Download {{%download "/download_files/sbp/wan-replication_multi_gw.zip"%}} the example and extract its content into an empty folder.<br> 
It includes two folders: **deploy** and **scripts**.<br>
- Deploy - This includes the PU folders for space and gateway<br>
- Scripts – This includes all the start agents and deploy scripts
 
**2.** Move the content of the deploy folder into `<GIGASPACES_HOME>\deploy` folder.<br>
**3.** Move into the scripts folder and edit the `setExampleEnv.bat/sh` script to include correct values for `XAP_NIC_ADDRESS` as the machine IP and `GS_HOME` as the XAP root folder location.
 
The scripts folder contains the necessary scripts to start the Grid Service Agent for each site, and the deployAll.bat/sh script  which will be used to automate the deployment of all gateways and space instances. This will allow you to run the entire setup on a single machine.
 
**4.** Run `startAgent-GB.bat/sh` script to start GB site agents.<br>
**5.** Run `startAgent-US.bat/sh` script to start US site agents.<br>
**6.** Run `deployAll.bat/sh` script to deploy the required processing units.



## Viewing the Space Clusters and Gateways

Once US and GB space clusters and gateways deployed you should see the following via the Web UI (make sure you use US,GB as the lookup groups when you login into the Web UI):
{{%align center%}}
![image2](/attachment_files/sbp/wan_gateway/distributed-wan-gateway-2.png)
{{%/align%}}

<br>

![image3](/attachment_files/sbp/wan_gateway/distributed-wan-gateway-3.png)

<br>

![image4](/attachment_files/sbp/wan_gateway/distributed-wan-gateway-4.png)


## US Space pu.xml configuration

The US space pu.xml should include the `${clusterInfo.instanceId}` as demonstrated below:

```xml
<os-gateway:targets id="gatewayTargets" local-gateway-name="US${clusterInfo.instanceId}">
    <os-gateway:target name="GB${clusterInfo.instanceId}" />
</os-gateway:targets>
```

## US Gateway pu.xml configuration

The US Gateway pu.xml should include the `${clusterInfo.instanceId}` as demonstrated below:
```xml
<os-gateway:delegator id="delegator" local-gateway-name="US${clusterInfo.instanceId}" gateway-lookups="gatewayLookups">
        <os-gateway:delegation target="GB${clusterInfo.instanceId}" />
    </os-gateway:delegator>
```

## GB Gateway pu.xml configuration

The GB Gateway pu.xml should include the `${clusterInfo.instanceId}` as demonstrated below:

```xml
<os-gateway:sink id="sink" local-gateway-name="GB${clusterInfo.instanceId}" gateway-lookups="gatewayLookups"
                     local-space-url="jini://*/*/spaceGB">
     <os-gateway:sources>
            <os-gateway:source name="US${clusterInfo.instanceId}"/>
     </os-gateway:sources>
</os-gateway:sink>
```

# Limitations
 
- Distributed transactions are not supported. Local transactions supported.
     

