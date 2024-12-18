---
type: post122
title: Service Grid Layer
categories: XAP122OVW
parent: product-architecture.html
weight: 600
canonical: auto
---

 

{{%  anchor GSRuntimeEnv %}}




The basic unit of deployment in the GigaSpaces XAP platform is the [Processing Unit]({{% latestjavaurl%}}/the-processing-unit-overview.html).

Once packaged, a processing unit is deployed onto the GigaSpaces runtime environment, which is called the *Service Grid*. It is responsible for materializing the processing unit's configuration, provisioning its instances to the runtime infrastructure and making sure they continue to run properly over time.


{{% note%}}
When developing your processing unit, you can [run and debug the processing unit within your IDE](../started/xap-debug.html). You will typically deploy it to the GigaSpaces runtime environment when it's ready for production or when you want to run it in the real-life runtime environment
{{%  /note %}}

# Architecture

The service grid is composed of a number of components:

{{% align center%}}
![gs_runtime.jpg](/attachment_files/gs_runtime.jpg)
{{% /align %}}


# Core Components

A processing unit can be deployed to the Service Grid using one of GigaSpaces deployment tools (UI, CLI, API), which uploads it to the *GSM* [Grid Service Manager](#gsm), the component which manages the deployment and life cycle of the processing unit). The GSM analyzes the deployment descriptor and determines how many instances of the processing unit should be created, and which containers should run them. 
It then ships the processing unit code to the running *GSC*'s [Grid Service Container](#gsc) and instructs them to instantiate the processing unit instances. The GSC provides an isolated runtime for the processing unit instance, and exposes its state to the GSM for monitoring. This phase in the deployment process is called *provisioning*.

Once provisioned, the GSM continuously monitors the processing unit instances to determine if they're functioning properly or not. When a certain instance fails, the GSM identifies that and re-provisions the failed instance on to another GSC, thus enforcing the processing unit's SLA.

In order to discover one another in the network, the GSCs and GSMs use a [Lookup Service](#lus), also called *LUS*. Each GSM and GSC registers itself in the LUS, and monitors the LUS to discover other GSM and GSC instances.

Finally, the *GSA* [Grid Service Agent](#gsa) component is used to start and manage the other components of the Service Grid (i.e. GSC, GSM, LUS). Typically, the GSA is started with the hosting machine's startup. Using the agent, you can bootstrap the entire cluster very easily, and start and stop additional GSCs, GSMs and lookup services at will.

All of the above components are fully manageable from the GigaSpaces management interfaces such as the [UI]({{% latestadmurl%}}/gigaspaces-management-center.html), CLI and [Admin API]({{% latestjavaurl%}}/administration-and-monitoring-api.html).

{{%  anchor gsm %}}

# Grid Service Manager (GSM)

The Grid Service Manager is the component which manages the deployment and life cycle of the processing unit.

When a processing unit is uploaded to the GSM (using one of GigaSpaces deployment tools: UI, CLI, API), the GSM analyzes the deployment descriptor and determines how many instances of the processing unit should be created, and which [containers](#gsc) should host them. It then ships the processing unit code to the relevant containers and instructs them to instantiate the processing unit instances. This phase in the deployment process is called *provisioning*.

Once provisioned, the GSM continuously monitors the processing unit instances to determine if they're functioning properly or not. When a certain instance fails, the GSM identifies that and re-provisions the failed instance on to another GSC, thus enforcing the processing unit's SLA.

{{% note%}}
It is common to start two GSM instances in each Service Grid for high-availability reasons: At any given point in time, each deployed processing unit is managed by a one GSM instance, and the other GSM(s) serve as its hot standby. If the active GSM fails for some reason, one of the standbys automatically takes over and start managing and monitoring the processing units that the failed GSM managed.
{{% /note%}}

{{%  anchor gsc %}}

# Grid Service Container (GSC)

The Grid Service Container provides an isolated runtime for one (or more) processing unit instance, and exposes its state to the [GSM](#gsm).

The GSC can be perceived as a node on the grid, which is controlled by [The Grid Service Manager](#gsm). The GSM provides commands of deployment and un-deployment of the Processing Unit instances into the GSC. The GSC reports its status to the GSM.

The GSC can host multiple processing unit instances simultaneously. The processing unit instances are isolated from each other using separate {{%exurl "Class loaders""http://en.wikipedia.org/wiki/Java_Classloader"%}} (in java) or {{%exurl "AppDomains""http://en.wikipedia.org/wiki/Appdomain"%}} (in .NET).

It is common to start several GSCs on the same physical machine, depending on the machine CPU and memory resources. The deployment of multiple GSCs on a single or multiple machines creates a virtual Service Grid. The fact is that GSCs are providing a layer of abstraction on top of the physical layer of machines. This concept enables deployment of clusters on various deployment typologies of enterprise data centers and public clouds.

{{%  anchor lus %}}

# The Lookup Service (LUS)

The Lookup Service provides a mechanism for services to discover each other. Each service can query the Lookup service for other services, and register itself in the Lookup Service so other services may find it. For example, the GSM queries the LUS to find active GSCs.

Note that the Lookup service is primarily used for establishing the initial connection - once service X discovers service Y via the Lookup Service, it usually creates a direct connection to it without further involvement of the Lookup Service.

Service registrations in the LUS are lease-based, and each service periodically renews its lease. That way, if a service hangs or disconnects from the LUS, its registration will be cancelled when the lease expires.

The Lookup Service can be configured for either a [multicast]({{% latestadmurl%}}/network-multicast.html) or [unicast]({{% latestadmurl%}}/network-unicast-discovery.html) environment (default is multicast).

Another important attribute in that context is the *lookup group*. The lookup group is a logical grouping of all the components that belong to the same runtime cluster. Using lookup groups, you can run multiple deployments on the same physical infrastructure, without them interfering with one another. For more details please refer to [Lookup Service Configuration]({{% latestadmurl%}}/network-lookup-service-configuration.html).

{{% note%}}
It is common to start at least two LUS instances in each Service Grid for high-availability reasons. Note that the lookup service can run in the same process with a GSM, or in standalone mode using its own process.
{{% /note%}}

The following services use the LUS:

* [GigaSpaces Manager](#gsm)

* [GigaSpaces Agent](#gsa)

* Processing Unit Instances (actual instances of a deployed Processing Unit)

* Space Instances (actual instances of a Space that form a topology)

{{%  refer %}}
For advanced information on the lookup service architecture, refer to [The Lookup Service](#lus).
{{% /refer%}}

{{%  anchor gsa %}}

# Grid Service Agent (GSA)

The Grid Service Agent (GSA) is a process manager that can spawn and manage Service Grid processes (Operating System level processes) such as [The Grid Service Manager](#gsm), [The Grid Service Container](#gsc), and [The Lookup Service](#lus). Typically, the GSA is started with the hosting machine's startup. Using the agent, you can bootstrap the entire cluster very easily, and start and stop additional GSCs, GSMs and lookup services at will.

Usually, a single GSA is run per machine. If you're setting up multiple Service Grids separated by [Lookup Groups or Locators](#lus), you'll probably start a GSA per machine per group.

The GSA exposes the ability to start, restart, and kill a process either using the [Administration and Monitoring API](../dev-java/administration-and-monitoring-api.html) or the GigaSpaces UI.

## Process Management

The GSA manages Operating System processes. There are two types of process management, local and global.

Local processes simply start the process type (for example, a [Grid Service Container](#gsc) without taking into account any other process types running by different GSAs.

Global processes take into account the number of process types [Grid Service Manager](#gsm) for example) that are currently running by other GSAs (within the same lookup groups or lookup locators). It will automatically try and run at least X number of processes *across* all the different GSAs (with a maximum of 1 process type per GSA). If a GSA running a process type that is managed globally fails, another GSA will identify the failure and start it in order to maintain at least X number of global process types.

# Optional Components

* The Elastic Service Manager (ESM) manages the [Elastic Processing Unit](../dev-java/elastic-processing-unit-overview.html) together with the GSM.

* The [Apache Load Balancer Agent](../dev-java/apache-load-balancer-agent.html) is used when deploying web applications.

* The Transaction Manager (TXM) is an optional component. When executing transactions that spans multiple space partitions you should use the Jini Transaction Manager or the Distributed Transaction Manager. See the [Transaction Management](../dev/java/transaction-management.html) section for details.
