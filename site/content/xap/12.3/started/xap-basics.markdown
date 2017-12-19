---
type: post123
title:  XAP Basics
categories:  XAP122GS, OSS
weight: 400
---

This section provides some hands-on examples to demonstrate the core concepts and APIs of the XAP in-memory data grid. The tutorials are geared toward architects and developers who want to build scaled-out applications with XAP.

{{%fpanel%}}

[XAP Open-Source Functionality](xap-open-source.html){{<wbr>}}
A summary of the features and functionality that are available in the XAP open-source edition.

[Setting Up your IDE](xap-ide.html){{<wbr>}}
Set up your IDE to enable working with XAP.

[Maven](installation-maven-overview.html){{<wbr>}}
Install the XAP Maven plugin, so you can create and run a Processing Unit project.
  
[XAP in 5 minutes](xap-in-5-minutes.html)<br>
This tutorial explains how to deploy and use a XAP [Data Grid](../overview/the-in-memory-data-grid.html) from a Java client application.

[Interacting with the Space](xap-tutorial-part1.html)<br>
In this part of the tutorial we will demonstrate how to create a Space and how you can interact with it. We will also demonstrate how you can improve your space search performance by using indexes and returning partial results.
 
[Data Partitioning](data-partitioning.html){{<wbr>}}
An overview the the available data partition topologies available for the XAP in-memory data grid. 

[Deploying a Space](xap-tutorial-part2.html)<br>
In the previous section  you have learned about XAP's capabilities as a data store. In this part of the tutorial we will show you how you can deploy an In Memory Data Grid (IMDG) that provides scalability and failover.

[Distributed Processing](xap-tutorial-part3.html)<br>
In this part of the tutorial we will introduce you to the different processing services you can run on top of the Space.

[Event-Driven Programming](xap-tutorial-part4.html)<br>
The Spaces Messaging and Events support provides messaging handlers that simplify event driven programming.

[Creating a Processing Unit](xap-tutorial-part5.html)<br>
The PU is the fundamental unit of deployment in XAP. The PU itself runs within a Processing Unit Container and is deployed onto the Service Grid. Once a PU is deployed, a PU instance is the actual runtime entity.

[Transactions and Concurrency](xap-tutorial-part6.html)<br>
In this part of the tutorial we will introduce you to the transaction processing capabilities of XAP.

[Persist to Database](xap-tutorial-part7.html)<br>
There are many situations where space data needs to be persisted to permanent storage and retrieved from it.

[Deploy a Web App](xap-tutorial-part8.html)<br>
In this part of the tutorial we will show you how you can deploy a standard WAR file onto the Service Grid.

[Security](xap-tutorial-part10.html)<br>
In this part of the tutorial we will introduce you to XAP security, where it fits in the XAP architecture, which components can be secured, and how to configure and customize the security depending on your application security requirements. XAP Security provides comprehensive support for securing your data and services.

 
[Multi-Tiered Data Storage (SSD)](blobstore.html)<br>
This guide describes the general architecture and functionality of this storage model that is leveraging both on-heap, off-heap and SSD implementation, called MemoryXtend.

[JUnit testing](xap-junit.html)<br>
Creating unit tests for XAP and processing units with different topologies.

[Debugging](xap-debug.html)<br>
As part of your development process, you may want to run/debug your processing unit within your IDE.

{{%/fpanel%}}
