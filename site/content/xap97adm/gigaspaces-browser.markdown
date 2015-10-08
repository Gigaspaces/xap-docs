---
type: post97adm
title:  Main View
categories: XAP97ADM
parent: gigaspaces-management-center.html
weight: 50
---


{{% note title="Using the XAP Management Center in production and large-scale environments "%}}

- It is highly recommended to run the XAP Management Center on the **same network subnet** as the Data-Grid and other GigaSpaces runtime components are running. Since the Management Center communicates with the Data-Grid,GSCs, GSM, GSA and Lookup-Service continuously, it should have fast connectivity with these components. High latency connectivity will impact the responsiveness of the  Management Center and its initial bootstrap time. In production environments you should use remote desktop products such as [VNC](http://www.realvnc.com/products/free/4.1/index.html) or [No Machine](http://www.nomachine.com), run the  Management Center at the **same** network subnet as the Data-Grid and the other XAP runtime components and run the VNC or NX client side to access the remote machines desktop from the administrator machine desktop.
- With relatively large amount of GSCs , Services or Data-Grid partitions (over 20 units) it is recommended to increase the heap size of the XAP Management Center to 1G (-Xmx1g).
{{% /note %}}


XAP Management Center is a Graphical User Interface that allows you to view spaces, containers, and clusters and configure them, using the Space Browser tab. You can also deploy and manage services using the Deployments tab.

**To start the XAP Management Center, run:**


{{%tabs%}}
{{%tab " Windows"%}}

```bash
<XAP Root>\bin\gs-ui.bat
```
{{%/tab%}}
{{%tab " Unix"%}}

```bash
<XAP Root>/bin/gs-ui.sh
```
{{%/tab%}}
{{%/tabs%}}



The XAP Management Center is separated into 3 tabs (Space Browser, Deployment, Details, Utilization):

![GigaSpaces_Management_Center_6.5_M7.jpg](/attachment_files/GigaSpaces_Management_Center_6.5_M7.jpg)

Each XAP Management Center tab is described in a dedicated documentation tab below:




# Space Browser tab

The Space Browser tab allows you to view and configure two main components in XAP -- space and cluster.


The **Spaces** node view:

|                   |      |
|-------------------|------|
| ![space_network_view_icon.gif](/attachment_files/space_network_view_icon.gif)|The main **Spaces** node displays the Space Network view -- a table listing all spaces in the network, and different details regarding those spaces.|
| ![container.gif](/attachment_files/container.gif)| Container nodes allow you to manage space containers -- shutting down the container, creating a space under it, and more.|
| ![spaceTreeIcon.gif](/attachment_files/spaceTreeIcon.gif) |Container nodes allow you to manage space containers -- shutting down the container, creating a space under it, and more.|


![GMC_space_6.0.jpg](/attachment_files/GMC_space_6.0.jpg)


The main **Clusters** node view:

|                   |      |
|-------------------|------|
| ![cluster_node.gif](/attachment_files/cluster_node.gif) | The main **Clusters** |
| ![specific_cluster_icon.jpg](/attachment_files/specific_cluster_icon.jpg) | The main **Clusters** node that allow you to manage the cluster -- stop, start, restart, clean the cluster, and more.|
| ![cluster_topology_icon.jpg](/attachment_files/cluster_topology_icon.jpg) | Cluster group nodes |
| ![spaceTreeIcon.gif](/attachment_files/spaceTreeIcon.gif)| cluster members  Clicking a cluster member displays a graphic representation of the cluster, and tables showing details regarding replication, failover, load-balancing, and classes. |


![GMC_space_clusterNodeSelected_6.0.jpg](/attachment_files/GMC_space_clusterNodeSelected_6.0.jpg)

