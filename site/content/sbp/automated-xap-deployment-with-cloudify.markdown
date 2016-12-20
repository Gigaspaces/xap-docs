---
type: post
title:  Automated XAP Deployment with Cloudify
categories: SBP
parent: production.html
weight: 1700
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| DeWayne Filppi| 9.6.0 | November 2013|    |    |




# Overview

Running XAP in a cloud platform can provide the scalability and performance of XAP combined with nearly limitless resources and flexible compute resource consumption.  This always been possible with manual VM creation and deployment, but the advent of GigaSpaces Cloudify has made the process highly automated and quick , not to mention opening the door for the creation of application recipes that can automatically deploy and manage all components of a XAP centric system.

# The XAP Recipes

XAP on Cloudify consists of two service recipes: one that represents management processes (web UI, GSM, LUS) and one that represents nodes hosting containers (GSCs).The management recipe is called "xap-management" and the container recipe is called "xap-container".  There is also an application recipe [xap9x-tiny](https://github.com/CloudifySource/cloudify-recipes/tree/master/apps/xap9x-tiny) that combines them (xap-management should start prior to xap-container instances) using small instance sizes.
 The service recipes are in a folder called "xap9x" in the [github cloudify-recipes project](https://github.com/CloudifySource/cloudify-recipes/tree/master/services/xap9x).

## XAP-management

The XAP-management recipe launches the XAP management processes: the GSM and LUS, as well as the Web UI. The LUS port is configurable in the recipe properties, and effectively identifies the cluster. All clusters (including Cloudify itself) must have unique LUS ports.

XAP-management can support up to 2 instances. Containers (GSCs) are deployed via the xap-container recipe. xap-management, on starting, updates the hosts table on the containers (if any) via a custom command. Containers likewise update their hosts tables on startup to avoid static IP configuration in the recipes.

The recipe provides a link to the XAP Web UI in the details section of the Cloudify UI.

### Custom Commands

The recipe provides several custom commands:

 * *deploy-pu*

Deploys a stateful/grid processing unit. Usage: deploy-pu puurl schema partitions backups max-per-vm max-per-machine name. Arguments (all args are required):
name: The deployed name for the processing unit. Defaults to the pu file name unless overridden.
puurl: A URL where the processing unit jar can be found
schema: The cluster schema (e.g. partitioned-sync2backup)
partitions: The number of partitions. Ignored if not partitioned.
backups: The number of backups per partition. Ignored if not partitioned.
max-per-vm: Maximum instances per JVM/container. See here for details
max-per-machine: Maximum instances per physical machine/cloud vm.

* *deploy-pu-basic*

A convenience command that provides defaults to deploy-pu for a basic installation. Arguments (all args are required):
puurl: A URL where the processing unit jar can be found
Notes: deploys a non-partitioned, single instance cluster. Useful for simple applications or testing.

* *deploy-grid*

Deploys a space. Usage: deploy-space name schema partitions backups max-per-vm max-per-machine.
Notes: see deploy-pu for argument meanings.

* *undeploy-grid*

Undeploys a grid by name. Usage: undeploy-grid name.. Args:
name: The name of the grid as assigned in the deploy-grid command.

## xap-container

The xap-container recipe starts a single GSC. When it starts it locates the management nodes and updates the /etc/hosts file (no Windows support yet). The recipe has effectively no upper limit on instances and is elastic. It has no custom commands intended for public use.  The recipe is configurable in the recipe properties.  Key among these is the "licence" property, which should be set to your XAP license string.  Also, the managementService recipe can be important if you inherit xap-management in a recipe of your own (effectively renaming it).  xap-container auto configures by discovering the management service at deploy time by its name.  Another important property is gsc_jvm_options, which is passed to the GSC startup in the `GSC_JAVA_OPTIONS` environment variable, determining the GSC size.  Some care must be take here not to request a JVM larger the Cloud template you configure the service with minus room for the Cloudify agent and the operating system.

