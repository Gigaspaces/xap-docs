---
type: post140
title:  GigaSpaces Management Center
categories: XAP140ADM, PRM
parent: admin-legacy-tools.html
weight: 300
---


# Overview


The GigaSpaces Management Center is a user interface that allows you to do the following:

- View, configure, and maintain Spaces, containers, and clusters in the Space Browser tab.
- Deploy and manage services in the Deployments tab.
- Manage users and roles, and perform security administration
- Generate a dump file that holds information about the XAP runtime environment, for a specific container or across the full environment.

# Using the GigaSpaces Management Center in Production and Large-Scale Environments

We recommend running the GigaSpaces Management Center on the **same network subnet** where the XAP data grid and other GigaSpaces runtime components are running. The GigaSpaces Management Center communicates with the data grid, GSCs, GSM, GSA, and LUS continuously, so it should have fast connectivity with these components. High-latency connectivity will affect the responsiveness of the  interface and its initial bootstrap time. In production environments, use a remote desktop products such as [VNC](http://www.realvnc.com/products/free/4.1/index.html) or [No Machine](http://www.nomachine.com). Run the GigaSpaces Management Center on the **same** network subnet as the data grid and the other XAP runtime components, and run the remote access application on the client side to access the remote machines desktop from the administrator machine desktop.

With relatively large amount of GSCs, Services or data grid partitions (over 20 units), we also recommend increasing the heap size of the GigaSpaces Management Center to 1G (-Xmx1g).


