---
type: post
title:  Deploying GigaSpaces Platforms on Minishift
categories: SBP
parent: insightedge.html
weight: 90
---

|Author|Product Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|:----------:|
| Dharma Prakash and Dixson Huie| 14.0 | February 2019| | |

# Overview
 
Minishift is a tool that helps you run OpenShift locally by running a single-node OpenShift cluster inside a VM. This topic explains how to prepare the OpenShift environment so you can install KubeGrid (the InsightEdge and XAP Kubernetes deployment) on Minishift. The sample deployment described here deploys InsightEdge using PowerShell and Oracle VirtualBox, but KubeGrid can be installed on any Minishift cluster using your preferred tools.

The desktop preparation process for deploying KubeGrid in Minishift involves the following steps, which are explained in detail below:

1. Set up your local machine or VM.
1. (For Windows environments only) Disable Hyper-V (as this deployment uses Oracle VirtualBox).
1. Configure Minishift to work with Oracle VirtualBox.
1. Set up the OpenShift container environment.

After the desktop has been prepared, you can follow the instructions in the [Deploying a Data Grid in Kubernetes](https://docs.gigaspaces.com/xap/14.0/admin/kubernetes-data-grid.html) topic to deploy KubeGrid on Minishift.

# Prerequisites

Before beginning the preparation and deployment process, ensure that you have the following installed on your local machine or a VM:

- Kubectl
- Helm
- Kubernetes cluster (cloud, on-premise, or local via Minishift)
- Oracle VirtualBox


{{%note%}}
This sample deployment used the following software:

- Oracle VirtualBox version 5.2.18 r124319
- Docker version 18.06.1-ce, build e68fc7a
- Helm version v2.10.0
- Minishift v1.27.0+707887e
- Kubectl: Client Version: v1.10.3, Server Version: v1.10.0
{{%/note%}}

# Preparing your Desktop Environment

In order to prepare the desktop environment to run OpenShift using Minishift, you need to perform the steps described below. PowerShell is the preferred tool for this process due to the amount of support information available online so that you can easily troubleshoot if you have any difficulty with the preparation and deployment process.

## Disabling Hyper-V

{{%note%}}
This step is only required if you are using a machine running Windows. If you are using a machine running Linux, skip this section and begin with Configuring Minishift to Use Oracle VirtualBox.
{{%/note%}}

This sample deployment uses Oracle VirtualBox instead of Hyper-V. Therefore you must verify that Hyper-V is disabled. If it isn’t, do the following to disable it.
 
1. Open PowerShell as an administrator.
1. In Windows Settings>Apps>Apps and Features, click **Programs and Features** on the right side of the window.
1. In the Programs and Features window, from the list on the left click **Turn Windows features on and off**.
1. In the Windows Features window, clear the **Hyper-V checkbox** and click **OK**. At this point you may have to restart the machine.
1. If you restarted your machine, open PowerShell again as an administrator.

{{%note%}}
This procedure disables Hyper-V temporarily. When the next batch of automatic updates is installed, Hyper-V will be enabled.
{{%/note%}}

## Configuring Minishift to use VirtualBox

Minishift can work with a number of hypervisors, some of which require manual installation of the driver plug-in. The Oracle VirtualBox driver plug-in comes embedded in Minishift, so you only need to set the configuration in order to identify VirtualBox to Minishift.

To configure Minishift to use Oracle VirtualBox:

- Type the following command in PowerShell:
	```
	minishift config set vm-driver virtualbox
	```
	
	You should see the following output when the command is run:
	```
	No Minishift instance exists. New 'vm-driver' setting will be applied on next 'minishift start'
	```
	
## Starting Minishift

After completing the Minishift configuration, it needs to be initiated.

To start Minishift:

- Type the following command in PowerShell:

	```
	minishift --memory 20000 --cpus 4 --v=5 start
	```

	This starts a one-node cluster on your machine with 2GB of memory, 4 CPUs, and log level 5.

## Setting up the Openshift Container Environment

The next step in the preparation (after the cluster is up and running) is to set up the OC (OpenShift Container) environment so that you can deploy KubeGrid.

To set up the OC environment:

1.	To initialize the OC CLI, type the following command in PowerShell: 
	For Windows environments:
	
	```
	minishift oc-env|Invoke-Expression
	```
	
	For Linux environments:
	
	```
	eval $(minishift oc-env)
	```
	
1. Log out of the OC environment by typing: oc logout
1. Log into the OC environment as an administrator by typing oc login, using the following credentials: 
	- username: `admin`
	- password: `admin`
	
1. Type the following command to enable the OpenShift addons:

	```
	minishift addons apply admin-user
	```
	
1. Type the following commands to grant user permissions:

	```
	oc adm policy add-cluster-role-to-user cluster-admin -z default --namespace default
	oc adm policy add-cluster-role-to-user cluster-admin -z default --namespace kube-system
	```
 
1. If you are installing the data grid only, skip this step. If you are installing InsightEdge, type the following commands:

	```
	oc create serviceaccount spark
	oc create clusterrolebinding spark-role --clusterrole=edit --serviceaccount=default:spark --namespace=default
	```
	
1. Type the following to specify the Kubernetes default namespace:

	```
	oc project default
	```
	
1. Type the following command to launch Helm:

	```
	helm init
	```

# Deploying KubeGrid

Now that the desktop environment is prepared, you can install the InsightEdge demo on Minishift.

To deploy KubeGrid and the InsightEdge demo:

1. Type the following command to access the insightedge GigaSpaces charts for the XAP data grid and InsightEdge:

	```
	helm repo add gigaspaces https://resources.gigaspaces.com/helm-charts
	```
	
1. After adding the GigaSpaces Helm repo, install the required chart(s) by referencing the chart name and product package version. For example, to install InsightEdge, use the following command:

	```
	helm install gigaspaces/insightedge --version=14.0.1 --name demo
	```

For more information about InsightEdge KubeGrid deployment options, see the [Deploying a Data Grid in Kubernetes](https://docs.gigaspaces.com/xap/14.0/admin/kubernetes-data-grid.html) topic.
