---
type: post140
title:  GigaSpaces Management Center
categories: XAP140ADM,PRM
weight: 400
parent: admin-tools.html
---

# Overview

The GigaSpaces Management Center is a user interface that allows you to do the following:

- View, configure, and maintain Spaces, containers, and clusters in the Space Browser tab.
- Deploy and manage services in the Deployments tab.
- Manage users and roles, and perform security administration
- Generate a dump file that holds information about the XAP runtime environment, for a specific container or across the full environment.

# Using the GigaSpaces Management Center in Production and Large-Scale Environments

Follow these network guidelines:

- We recommend running the GigaSpaces Management Center on the **same network subnet** where the XAP data grid and other GigaSpaces runtime components are running.
- The GigaSpaces Management Center communicates with the data grid, GSCs, GSM, GSA, and LUS continuously, so it should have fast connectivity with these components. High-latency connectivity will affect the responsiveness of the  interface and its initial bootstrap time.
- In production environments, use a remote desktop product such as [VNC](http://www.realvnc.com/products/free/4.1/index.html) or [No Machine](http://www.nomachine.com).
- Run the GigaSpaces Management Center on the **same** network subnet as the data grid and the other XAP runtime components, and run the remote access application on the client side to access the remote machines desktop from the administrator machine desktop.
- Make sure that the host machine where the GigaSpaces Management Center is running has Internet access.

{{%note%}}
If the host machine doesn't have Internet access, the GigaSpaces Management Center won't start. To prevent this from happening, set the system property `com.gs.get-build` to `false` in the EXT_JAVA_OPTIONS environment variable in the setenv-overrides file.
{{%/note%}}

With relatively large amount of GSCs, Services or data grid partitions (over 20 units), we also recommend increasing the heap size of the GigaSpaces Management Center to 1G (-Xmx1g).

# Starting The GigaSpaces Management Center

To start the GigaSpaces Management Center, run the following:


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

# Main View

![GigaSpaces_Management_Center_6.5_M7.jpg](/attachment_files/GigaSpaces_Management_Center_6.5_M7.jpg)

The GigaSpaces Management Center contains 3 tabs:

- Hosts tab - view information about the host machines.
- Deployed Processing Units - view information about the Processing Units that have been deployed (including events and exceptions) and perform deployment-related tasks.
- Space Browser - view information about the Spaces and clusters in the data grid and perform configuration and maintenance tasks.


The Space Browser tab has the following components.

## Spaces Node

|      |     |
|----|-----|
| ![space_network_view_icon.gif](/attachment_files/space_network_view_icon.gif)|The main **Spaces** node displays the Space Network view -- a table listing all spaces in the network, and different details regarding those spaces.|
| ![container.gif](/attachment_files/container.gif)| Container nodes allow you to manage space containers -- shutting down the container, creating a space under it, and more.|
| ![spaceTreeIcon.gif](/attachment_files/spaceTreeIcon.gif) |Container nodes allow you to manage space containers -- shutting down the container, creating a space under it, and more.|


![GMC_space_6.0.jpg](/attachment_files/GMC_space_6.0.jpg)


## Clusters Node

|      |     |
|----|-----|
| ![cluster_node.gif](/attachment_files/cluster_node.gif) | The main **Clusters** |
| ![specific_cluster_icon.jpg](/attachment_files/specific_cluster_icon.jpg) | The main **Clusters** node that allow you to manage the cluster -- stop, start, restart, clean the cluster, and more.|
| ![cluster_topology_icon.jpg](/attachment_files/cluster_topology_icon.jpg) | Cluster group nodes |
| ![spaceTreeIcon.gif](/attachment_files/spaceTreeIcon.gif)| cluster members  Clicking a cluster member displays a graphic representation of the cluster, and tables showing details regarding replication, failover, load-balancing, and classes. |


![GMC_space_clusterNodeSelected_6.0.jpg](/attachment_files/GMC_space_clusterNodeSelected_6.0.jpg)
