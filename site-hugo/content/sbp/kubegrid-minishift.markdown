


Deploying GigaSpaces Platforms on Minishift- 
Minishift is a tool that helps you run OpenShift locally by running a single-node OpenShift cluster inside a VM. This topic explains how to prepare the OpenShift environment so you can install KubeGrid (the InsightEdge and XAP Kubernetes deployment) on Minishift. The sample deployment described here uses adeploys InsightEdge using  machine running Windows 10 PowerShell andwith Oracle VirtualBox, but KubeGrid can be installed in on any Minishift cluster using your preferred tools.
The desktop preparation process for deploying KubeGrid in Minishift on Windows 10 involves the following steps, which are explained in detail below:
•	Set up your local machine or VM.
•	(For Windows environments only) Disable Hyper-V (as this deployment uses Oracle VirtualBox).
•	Configure Minishift to work with Oracle VirtualBox.
•	Set up the OpenShift container environment.
After the desktop has been prepared, you can follow the instructions in the Deploying a Data Grid in Kubernetes topic to deploy KubeGrid in on Minishift.
Prerequisites
Before beginning the preparation and deployment process to work with the data grid and InsightEdge, ensure that you have the following installed on your local machine or a VM:
•	Kubectl
•	Helm
•	Kubernetes cluster (cloud, on-premise, or local via Minishift)
•	Oracle VirtualBox
Note:
This sample deployment used the following software:
•	Oracle VirtualBox version 5.2.18 r124319
•	Docker version 18.06.1-ce, build e68fc7a
•	Helm version v2.10.0
•	Minishift v1.27.0+707887e
•	Kubectl: Client Version: v1.10.3, Server Version: v1.10.0
Preparing your Desktop Environment
In order to prepare the desktop environment to run OpenShift using Minishift, you need to perform the steps described below. PowerShell is the preferred tool for this process due to the amount of support information available online so that you can easily troubleshoot if you have any difficulty with the preparation and deployment process.
Step 1: Disabling Hyper-V
Note:
This step is only required if you are using a machine running Windows. If you are using a machine running Linux, skip this section and begin with Configuring Minishift to Use Oracle VirtualBox.
This sample deployment uses Oracle VirtualBox instead of Hyper-V. Therefore you must verify that Hyper-V is disabled. If it isn’t, do the following to disable it. 
1.	Open PowerShell as an administrator.
2.	In Windows Settings>Apps>Apps and Features, click Programs and Features on the right side of the window.
3.	In the Programs and Features window, from the list on the left click Turn Windows features on and off.
4.	In the Windows Features window, clear the Hyper-V checkbox and click OK. At this point you may have to restart the machine.
5.	If you restarted your machine, open PowerShell again as an administrator.
Note: This procedure disables Hyper-V temporarily. When the next batch of automatic updates is installed, Hyper-V will be enabled.
Step 2: Configuring Minishift to use VirtualBox
Minishift can work with a number of hypervisors, some of which require manual installation of the driver plug-in. The Oracle VirtualBox driver plug-in comes embedded in Minishift, so After you disable Hyper-V, you must only need to set the configuration in order to identify VirtualBox to Minishifte Minishift to use Oracle VirtualBox.
To configure Minishift to use Oracle VirtualBox:
•	Type the following command in PowerShell:
minishift config set vm-driver virtualbox
You should see the following output when the command is run:
No Minishift instance exists. New 'vm-driver' setting will be applied on next 'minishift start'
Step 3: Starting Minishift
Now thatAfter completing the Minishift has been configured to use Oracle VirtualBoxconfiguration, it needs to be initiated.
To start Minishift:
•	Type the following command in PowerShell:
minishift --memory 20000 --cpus 4 --v=5 start
This starts a one-node cluster on your machine with 2GB of memory, 4 CPUs, and log level 5.
Step 4: Setting up the Openshift Container Environment
The next step in the preparation (after the cluster is up and running) is to set up the OC (OpenShift Container) environment so that you can deploy KubeGrid.
To set up the OC environment:
1.	To initialize the OC CLI, type the following command in PowerShell: 
For Windows environments:
minishift oc-env|Invoke-Expression
For Linux environments:
eval $(minishift oc-env)
minishift oc-env|Invoke-Expression
1.2.	Log out of the OC environment by typing: oc logout
2.3.	Log into the OC environment as an administrator by typing oc login, using the following credentials: 
a.	username: admin
b.	password: admin
3.4.	Type the following command to enable the OpenShift addons: minishift addons apply admin-user
4.5.	Type the following commands to grant user permissions:
a.	oc adm policy add-cluster-role-to-user cluster-admin -z default --namespace default
b.	oc adm policy add-cluster-role-to-user cluster-admin -z default --namespace kube-system
5.6.	If you are installing the data grid only, skip this step. If you are installing InsightEdge, type the following commands:
a.	oc create serviceaccount spark
b.	oc create clusterrolebinding spark-role --clusterrole=edit --serviceaccount=default:spark --namespace=default
6.7.	Type the following to specify the Kubernetes default namespace: oc project default
7.8.	Type the following command to launch Helm: helm init
Deploying the InsightEdge DemoKubeGrid
Now that the desktop environment is prepared, you can install the InsightEdge demo on Minishift.
To deploy InsightEdgeKubeGrid and the InsightEdge demo:
Type the following command to access the insightedge GigaSpaces charts for the XAP data grid and InsightEdge:
helm repo add gigaspaces https://resources.gigaspaces.com/helm-charts
After adding the GigaSpaces Helm repo, install the required chart(s) by referencing the chart name and product package version. For example, to install InsightEdge, use the following command:
helm install gigaspaces/insightedge --version=14.0.1 --name demo
For more information about InsightEdge KubeGrid deployment options, see the Deploying a Data Grid in Kubernetes topic.
