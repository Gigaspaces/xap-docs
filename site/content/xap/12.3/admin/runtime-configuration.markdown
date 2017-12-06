---
type: post123
title:  The Service Grid
categories: XAP123ADM, PRM
weight: 700
parent: none
---

This section describes the available XAP runtime configuration parameters.

{{% anchor GSRuntimeEnv %}}

The basic unit of deployment in the GigaSpaces XAP platform is the [Processing Unit]({{% currentjavaurl%}}/the-processing-unit-overview.html).

After is it packaged, a Processing Unit is deployed to the XAP runtime environment, which is called the *Service Grid*. The Service Grid is responsible for materializing the Processing Unit's configuration, provisioning its instances to the runtime infrastructure, and making sure the instances continue to run properly over time.

{{%  info "Info"%}}
When developing your Processing Unit, we recommend [running and debugging the Processing Unit within your IDE](../started/xap-debug.html). Deploy it to the Service Grid when you want to test it in the real-life runtime environment, and for production.
{{%  /info %}}

# Architecture

The Service 
Grid is composed of the following components:

* **XAP Manager**, or `manager` - Contains the following management components:
  * [Lookup Service](#lus), or `lus` - Provides service discovery for Service 
  Grid components
  * [Grid Service Manager](#gsm), or `gsm` - Manages the deployment and life cycle of the Processing Unit
  * Embedded Zookeeper - Provides consistent state management and facilitates leader election in data grids
  * [REST Manager API](xap-manager-rest.html) - Provides a cross-platform endpoint to manage the XAP runtime environment from anywhere
* [Grid Service Container](#gsc), or `gsc` - Provides an isolated runtime for one (or more) Processing Unit instance(s)
* [Grid Service Agent](#gsa), or `gs-agent` - Starts and monitors Service Grid components on its machine.

{{%  info "Info"%}}
In previous XAP versions, the Grid Service Manager and Lookup Service were standalone components. Starting in XAP version 12.1, we recommend starting the XAP Manager instead. In addition to this being simpler, it also offers better consistency and protection against network segmentation (via Apache Zookeeper), as well as the REST Manager API.

For backward compatibility and to simplify the upgrade process, you can still start the LUS and GSM independently. However, future releases may include additional new features and functionality that will require using the XAP Manager.
{{%  /info %}}

All of the above components are fully manageable from management interfaces such as the [XAP Management Center](gigaspaces-management-center.html), the command line interface, and the [Admin API]({{% currentjavaurl%}}/administration-and-monitoring-overview.html).

{{% align center%}}
[comment]: <> (![gs_runtime.jpg](/attachment_files/gs_runtime.jpg))
{{% /align %}}

{{% anchor gsm %}}

## Grid Service Manager

When a Processing Unit is uploaded to the Grid Service Manager (GSM), it analyzes the deployment descriptor and determines how many instances of the Processing Unit should be created, and which [containers](#gsc) should host them. The GSM then ships the Processing Unit code to the relevant containers, and instructs them to instantiate the Processing Unit instances. This phase in the deployment process is called *provisioning*.

After it is provisioned, the GSM continuously monitors the Processing Unit instances to verify that they are functioning properly. When a certain instance fails, the GSM identifies it and re-provisions the failed instance to another GSC, in order to enforce the Processing Unit's SLA.

{{% anchor gsc %}}

## Grid Service Container

The Grid Service Container (GSC) can be thought of as a node on the Service Grid, which is controlled by the [GSM](#gsm). The GSM sends deployment and un-deployment commands of the Processing Unit instances to the GSC. The GSC reports its status to the GSM.

The GSC can host multiple Processing Unit instances simultaneously. The Processing Unit instances are isolated from each other using separate [Class loaders](http://en.wikipedia.org/wiki/Java_Classloader) (in java) or [AppDomains](http://en.wikipedia.org/wiki/Appdomain) (in .NET).

It is common to start several GSCs on the same physical machine, depending on the machine CPU and memory resources. The deployment of multiple GSCs on a single or multiple machines creates a virtual Service Grid. The GSCs provide a layer of abstraction on top of the physical layer of machines. This enables deploying clusters in various deployment typologies for enterprise data centers and public clouds.

{{% anchor lus %}}

## The Lookup Service

The Lookup Service (LUS) provides a means for services to discover each other. Each service can query the LUS for other services, and register itself in the LUS so other services can find it. For example, the GSM queries the LUS to find active GSCs.

The LUS is used primarily to establish the initial connection. After service X discovers service Y via the LUS, it usually creates a direct connection without further involving the LUS.

Service registrations in the LUS are lease-based, and each service periodically renews its lease. That way, if a service hangs or disconnects from the LUS, its registration gets cancelled when the lease expires.

For more information on the LUS, refer to [The Lookup Service](./the-lookup-service.html).

{{% anchor gsa %}}

## Grid Service Agent

The Grid Service Agent (gs-agent) is a process manager that can spawn and manage Service Grid processes (operating-system-level processes) such as the [GSM](#gsm), [GSC](#gsc), and [LUS](#lus). Typically, the gs-agent is started with the host machine's startup. Using the agent, you can bootstrap the entire cluster very easily, and start and stop additional GSCs, GSMs and LUSs as necessary.

Usually, each machine runs a single gs-agent. If you are setting up multiple Service Grids separated by [Lookup Groups or Locators](#lus), you will probably start a gs-agent per machine, per group.

The gs-agent exposes the ability to start, restart, and kill a process either using the [Administration and Monitoring API]({{% currentjavaurl%}}/administration-and-monitoring-overview.html) or the XAP Management Center.

# High Availability

High availability is acheived via redundancy. We recommend setting up and starting three XAP Managers (refer to [XAP Manager High Availability](xap-manager.html#high-availability)). 

Alternatively, it is possible to manually configure and start multiple LUS and GSM instances. For more information, refer to the following topics:

* [Global vs. Local LUS](lus-configuration.html)
* [Global vs. Local GSM](gsm-configuration.html)

# Advanced

## Process Management

The gs-agent manages operating system processes. There are two types of process management, local and global.

Local processes simply start the process type (for example, a [Grid Service Container](#gsc)) without taking into account any other process types run by different GSAs.

Global processes take into account the number of process types (a [Grid Service Manager](#gsm) for example) that are currently run by other GSAs within the same lookup groups or lookup locators. It will automatically try and run at least X number of processes *across* all the different GSAs (with a maximum of 1 process type per GSA). If a GSA running a globally managed process type fails, another GSA will identify the failure and start it in order to maintain at least X number of global process types.


