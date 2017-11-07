---
type: post123
title:  Main View
categories: XAP123ADM, PRM
parent: gigaspaces-management-center.html
weight: 50
---

The GigaSpaces Management Center is a Graphical User Interface that allows you to view spaces, containers, and clusters and configure them, using the Space Browser tab. You can also deploy and manage services using the Deployments tab.

**To start the GigaSpaces Management Center, run:**


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



The GigaSpaces Management Center is separated into 3 tabs (Space Browser, Deployment, Details, Utilization):

![GigaSpaces_Management_Center_6.5_M7.jpg](/attachment_files/GigaSpaces_Management_Center_6.5_M7.jpg)

Each GigaSpaces Management Center tab is described in a dedicated documentation tab below:




# Space Browser tab

The Space Browser tab allows you to view and configure two main components in XAP -- space and cluster.


The **Spaces** node view:

|      |     |
|----|-----|
| ![space_network_view_icon.gif](/attachment_files/space_network_view_icon.gif)|The main **Spaces** node displays the Space Network view -- a table listing all spaces in the network, and different details regarding those spaces.|
| ![container.gif](/attachment_files/container.gif)| Container nodes allow you to manage space containers -- shutting down the container, creating a space under it, and more.|
| ![spaceTreeIcon.gif](/attachment_files/spaceTreeIcon.gif) |Container nodes allow you to manage space containers -- shutting down the container, creating a space under it, and more.|


![GMC_space_6.0.jpg](/attachment_files/GMC_space_6.0.jpg)


The main **Clusters** node view:

|      |     |
|----|-----|
| ![cluster_node.gif](/attachment_files/cluster_node.gif) | The main **Clusters** |
| ![specific_cluster_icon.jpg](/attachment_files/specific_cluster_icon.jpg) | The main **Clusters** node that allow you to manage the cluster -- stop, start, restart, clean the cluster, and more.|
| ![cluster_topology_icon.jpg](/attachment_files/cluster_topology_icon.jpg) | Cluster group nodes |
| ![spaceTreeIcon.gif](/attachment_files/spaceTreeIcon.gif)| cluster members  Clicking a cluster member displays a graphic representation of the cluster, and tables showing details regarding replication, failover, load-balancing, and classes. |


![GMC_space_clusterNodeSelected_6.0.jpg](/attachment_files/GMC_space_clusterNodeSelected_6.0.jpg)

