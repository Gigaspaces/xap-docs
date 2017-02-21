---
type: post121
title:  Example
categories: XAP121,PRM
parent: elastic-processing-unit-overview.html
weight: 600
---

This example deploys a partitioned data grid across several machines. It demonstrates the Elastic PU elasticity, continuous high-availability and even data distribution across all existing resources available for the grid. 
As long as there is enough memory resources across available machines, the PU primary and backup instances will be provisioned , ensuring no data loss while spreading the instances evenly.

# Setup

{{%section%}}
{{%column width="60%"%}}
XAP {{<currentversion>}}
- 4 VMs - XAP-1 , XAP-2 , XAP-3 , XAP-4<br>
- 8 GM RAM , 4 cores each

The Data-Grid cluster<br>
- 8 partitions with backup<br>
- Offheap storage<br>
- GSC size 500 MB heap
{{%/column%}} 

{{%column width="30%"%}}
![image](/attachment_files/epu/example1.png)
{{%/column%}}
{{%/section%}}

# Flow

{{%section%}}
{{%column width="60%"%}}
1. Start 4 VMs
2. Start agent on each machine
3. Deploy Elastic PU
4. Shutdown one VM (XAP-4)
5. IMDG Cluster rebalance automatically across 3 existing VMS
6. Shutdown another VM (XAP-3)
7. IMDG Cluster rebalance automatically across 2 existing VMS
8. Start VM (XAP-3)
9. IMDG Cluster rebalance automatically across 3 existing VMS
10. Start another VM (XAP-4)
11. IMDG Cluster rebalance automatically across 4 existing VMS
{{%/column%}}
{{%column width="30%"%}}
![image](/attachment_files/epu/example2.png)
{{%/column%}}
{{%/section%}}


# Starting the agent
Management node (XAP-1)
```bash
./gs-agent.sh gsa.esm 1 gsa.gsc 0 gsa.lus 1 gsa.global.lus 0 gsa.global.gms 0 gsa.gsm 1
```

Other nodes (XAP-2, XAP-3 , XAP-4)
```bash%
./gs-agent.sh gsa.esm 0 gsa.gsc 0 gsa.lus 0 gsa.global.lus 0 gsa.global.gms 0 gsa.gsm 0
```

#  Elastic PU 
## The Space pu.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:blob-store="http://www.openspaces.org/schema/mapdb-blob-store"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/mapdb-blob-store http://www.openspaces.org/schema/{{%currentversion%}}/mapdb-blob-store/openspaces-mapdb-blobstore.xsd">

    <blob-store:mapdb-blob-store id="offheapBlobstore"/>

    <os-core:embedded-space id="space" space-name="offheap-space">
       <os-core:blob-store-data-policy persistent="false" blob-store-handler="offheapBlobstore"/>
    </os-core:space>

    <os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```

## Deploying the elastic PU
The following command will start a stateful elastic PU with 8 partitions:

```bash
./gs.sh deploy-elastic-pu -type stateful -ha true -memory-capacity-per-container 500m -number-of-partitions 8 -puname offheap-space
```

You can see now in the [Web Management Console]({{%currentadmurl%}}/web-management-console.html) that all machines and partitions are up and running: 

![image](/attachment_files/epu/example3.png)

#  Shutting down XAP 4

Lets shut down instance 4. You will see in the web management console, that all instances have been distributed evenly across the rest of the available resources:

![image](/attachment_files/epu/example4.png)


## PU Instances Distribution

You can see that all primary and backup instances have been provisioned into the other remaining VM's:

{{%align center%}}
![image](/attachment_files/epu/example5.png)
{{%/align%}}

# Shutting down XAP 3

Lets shut down instance 3. Again look at the web management console:
![image](/attachment_files/epu/example6.png)

You can see that all primary and backup instances have been provisioned into the other remaining VM's:

##  PU Instances Distribution

{{%align center%}}
![image](/attachment_files/epu/example7.png)
{{%/align%}}


# Restart XAP 3

Once you restart this instance, you will see that the existing primaries and backup instances are rebalanced over all three servers:

![image](/attachment_files/epu/example8.png)

##  PU Instances Distribution

{{%align center%}}
![image](/attachment_files/epu/example9.png)
{{%/align%}}

# Restart XAP 4

Again, you will see that the existing primaries and backup instances are rebalanced over all for servers:

![image](/attachment_files/epu/example10.png)

##  PU Instances Distribution

{{%align center%}}
![image](/attachment_files/epu/example11.png)
{{%/align%}}




