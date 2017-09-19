---
type: post122
title:  Overview
categories: XAP122ADM, PRM
parent: runtime-configuration.html
weight: 100
---

{{% ssummary %}}{{%  /ssummary %}}

{{% anchor GSRuntimeEnv %}}

The basic unit of deployment in the GigaSpaces XAP platform is the [Processing Unit]({{% currentjavaurl%}}/the-processing-unit-overview.html).

Once packaged, a processing unit is deployed onto the XAP runtime environment, which is called the *Service Grid*. It is responsible for materializing the processing unit's configuration, provisioning its instances to the runtime infrastructure and making sure they continue to run properly over time.

{{%  info %}}
While developing your processing unit, it's recommended to [run and debug the processing unit within your IDE](../started/xap-debug.html). Deploy it to the Service Grid when you want to test it in the real-life runtime environment, and of course for production.
{{%  /info %}}

# Architecture

The service grid is composed of the following components:

* **XAP Manager**, or `manager` - Encapsulates the following management components:
  * [Lookup Service](#lus), or `lus` - Provides service discovery for service grid components
  * [Grid Service Manager](#gsm), or `gsm` - Manages the deployment and life cycle of the processing unit
  * [Embedded Zookeeper](zookeeper.html) - Provides consistent state management and facilitates leader election in data grids
  * [RESTful API](xap-manager-rest.html) - Provides cross-platform endpoint to manage the XAP runtime environment from anywhere
* [Grid Service Container](#gsc), or `gsc` - Provides an isolated runtime for one (or more) processing unit instance
* [Grid Service Agent](#gsa), or `gs-agent` - Starts and monitors service grid components on its machine.

{{%  info %}}
In previous versions the Grid Service Manager and Lookup Service were a standalone components. Starting 12.1, it's highly recommended to start the manager instead - in addition to simplicity, it also offers better consistency and protection against network segmentation (via Zookeeper), as well as a RESTful management API. For backward compatibility, you can still start the LUS and GSM independently, to simplify the upgrade process, but keep in mind upcoming releases will include additional improvements which will require usage of the manager.
{{%  /info %}}

Those components are fully manageable from XAP management interfaces such as the [UI](gigaspaces-management-center.html), CLI and [Admin API]({{% currentjavaurl%}}/administration-and-monitoring-api.html).

{{% align center%}}
[comment]: <> (![gs_runtime.jpg](/attachment_files/gs_runtime.jpg))
{{% /align %}}

{{% anchor gsm %}}

## Grid Service Manager

When a processing unit is uploaded to the GSM, it analyzes the deployment descriptor and determines how many instances of the processing unit should be created, and which [containers](#gsc) should host them. It then ships the processing unit code to the relevant containers and instructs them to instantiate the processing unit instances. This phase in the deployment process is called *provisioning*.

Once provisioned, the GSM continuously monitors the processing unit instances to determine if they're functioning properly or not. When a certain instance fails, the GSM identifies that and re-provisions the failed instance on to another GSC, thus enforcing the processing unit's SLA.

{{% anchor gsc %}}

## Grid Service Container

The GSC can be perceived as a node on the grid, which is controlled by the [GSM](#gsm). The GSM provides commands of deployment and un-deployment of the Processing Unit instances into the GSC. The GSC reports its status to the GSM.

The GSC can host multiple processing unit instances simultaneously. The processing unit instances are isolated from each other using separate [Class loaders](http://en.wikipedia.org/wiki/Java_Classloader) (in java) or [AppDomains](http://en.wikipedia.org/wiki/Appdomain) (in .NET).

It is common to start several GSCs on the same physical machine, depending on the machine CPU and memory resources. The deployment of multiple GSCs on a single or multiple machines creates a virtual Service Grid. The fact is that GSCs are providing a layer of abstraction on top of the physical layer of machines. This concept enables deployment of clusters on various deployment typologies of enterprise data centers and public clouds.

{{% anchor lus %}}

## The Lookup Service

The Lookup Service provides a means for services to discover each other. Each service can query the Lookup service for other services, and register itself in the Lookup Service so other services may find it. For example, the GSM queries the LUS to find active GSCs.

Note that the Lookup service is primarily used for establishing the initial connection - once service X discovers service Y via the Lookup Service, it usually creates a direct connection to it without further involvement of the Lookup Service.

Service registrations in the LUS are lease-based, and each service periodically renews its lease. That way, if a service hangs or disconnects from the LUS, its registration will be cancelled when the lease expires.

For more information on the lookup service, refer to [The Lookup Service](./the-lookup-service.html).

{{% anchor gsa %}}

## Grid Service Agent

The Grid Service Agent (gs-agent) is a process manager that can spawn and manage Service Grid processes (Operating System level processes) such as The [GSM](#gsm), [GSC](#gsc), and [The Lookup Service](#lus). Typically, the gs-agent is started with the hosting machine's startup. Using the agent, you can bootstrap the entire cluster very easily, and start and stop additional GSCs, GSMs and lookup services at will.

Usually, each machine runs a single gs-agent. If you're setting up multiple Service Grids separated by [Lookup Groups or Locators](#lus), you'll probably start a gs-agent per machine per group.

The gs-agent exposes the ability to start, restart, and kill a process either using the [Administration and Monitoring API]({{% currentjavaurl%}}/administration-and-monitoring-api.html) or the GigaSpaces UI.

# High Availability

High availability is acheived by redundancy. The recommended way is to setup and start 3 XAP Managers (See [XAP Manager High Availability](xap-manager.html#high-availability). 

Alternatively, it's possible to manually configure and start multiple LUS and GSM instances. For more information on this see:

* [Global vs. Local LUS](lus-configuration.html)
* [Global vs. Local GSM](gsm-configuration.html)

# Advanced

## Process Management

The gs-agent manages Operating System processes. There are two types of process management, local and global.

Local processes simply start the process type (for example, a [Grid Service Container](#gsc) without taking into account any other process types running by different GSAs.

Global processes take into account the number of process types [Grid Service Manager](#gsm) for example) that are currently running by other GSAs (within the same lookup groups or lookup locators). It will automatically try and run at least X number of processes *across* all the different GSAs (with a maximum of 1 process type per GSA). If a GSA running a process type that is managed globally fails, another GSA will identify the failure and start it in order to maintain at least X number of global process types.
