---
type: post140
title:  Deploying a Data Grid in Kubernetes
parent: kubernetes-overview.html
categories: XAP140ADM, PRM
weight: 200
---


{{%note%}}
The topics in this section assume basic knowledge of InsightEdge and the data grid. If you aren't familiar with the data grid (at minimum), review the contents of the [Getting Started](../xap/14.0/started/) section before performing the tasks described here.
{{%/note%}}

This topic describes how to deploy GigaSpaces products in a Kubernetes environment. The integration is packaged as a [Helm chart](https://docs.helm.sh/developing_charts/#charts). You can deploy the full InsightEdge platform, which includes the data grid, using the Helm chart available in the GigaSpaces Helm repository.

## Prerequisites

Before beginning to work with the data grid and InsightEdge, ensure that you have the following installed on your local machine or a VM:

* [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
* [Helm](https://docs.helm.sh/using_helm/#quickstart-guide)
* Kubernetes cluster (cloud, on-premise, or local via [minikube](https://kubernetes.io/docs/setup/minikube/))

This topic describes how to do basic and intermediate-level deployment tasks for the data grid and InsightEdge using simple Helm commands. You can perform these tasks using a Kubernetes minikube, because you only need a single node. 

{{%tip%}}
Configuring the data grid for InsightEdge involves the same tasks as configuring the data grid alone. The deployment and maintenance tasks described below use `insightedge` Helm charts commands. However, you can also perform these tasks using the `xap` Helm charts commands. 
{{%/tip%}}

# Getting Started

In this section, you will learn how to do the following:

- Get the required GigaSpaces Helm charts
- Start a data grid in Kubernetes
- Monitor your data gris using Kubernetes tools
- Delete your data grid

## Accessing the GigaSpaces Helm Charts

Helm supports installing charts in [a number of ways](https://docs.helm.sh/helm/#helm-install). A Helm chart can be used in a variety of formats and locations; packaged, unpackaged, accessed via a remote URL or even in a chart repository.  The `xap` and `insightedge` Helm charts are published in the GigaSpaces Helm chart repository at `https://resources.gigaspaces.com/helm-charts`. 

There are multiple ways to access these charts in order to install a GigaSpaces product in Kubernetes:

- Install a chart directly from the repo
- Add the GigaSpaces Helm chart repo to the Helm repo list
- Unpack the required Helm chart(s) in a local folder

### Adding a Helm Repo to the Repo List

You can point to the GigaSpaces Helm repo, so that Helm can locate the `xap` and `insightedge` charts for installation:

```bash
helm repo add gigaspaces https://resources.gigaspaces.com/helm-charts
```

After adding the GigaSpaces Helm repo, you can install the required chart(s) by referencing the chart name and product package version. For example, to install InsightEdge, use the following command:

```bash
helm install gigaspaces/insightedge --version={{%version helm-version" %}} --name demo
```

### Fetching the GigaSpaces Helm Charts from the GigaSpaces Repository

Another option is to fetch the GigaSpaces Helm charts that you need and unpack them locally, so you don’t have to repeat the repo name and package version in each command (which has the added benefit of making the commands shorter). For example, if you fetch and unpack the Helm chart using the following command:

{{%note%}}
You must fetch every chart that you will be using (for example: xap, xap-pu and xap-manager) in your GigaSpaces application environment.
{{%/note%}}

```bash
helm fetch gigaspaces/insightedge --version={{%version helm-version" %}} --untar
```

The chart is unpackaged in a local folder called insightedge, and then you can install the demo by typing:

```bash
helm install insightedge --name demo
```

All of the commands in the examples below assume that the Helm chart was fetched and stored in a local folder, but you can modify the commands to accomodate the other Helm install options (remote location, repo reference, etc.).

## Starting a Data Grid in Kubernetes

Run the following Helm command in the command window to start a data grid in Kubernetes. This deploys a Kubernetes cluster called `hello`, which contains a data grid comprised of one Space in a Data Pod, and one Platform Manager called `hello-xap-manager` in a Management Pod. The Platform Manager manages the Space, the Manager service, and the headless service. There are no backup instances specified. 

To start a data grid:

```bash
helm install xap --name hello
```

To start an InsightEdge data grid:

```bash
helm install insightedge --name hello
```

{{%note%}}
The rest of the data grid tasks described below use command examples from the `insightedge` Helm chart. However, you can also perform these tasks using the `xap` Helm chart. 
{{%/note%}}

## Monitoring the Data Grid in Kubernetes

You can monitor the `hello` cluster you deployed using any of the following administration tools.

* Helm chart: run the following command to print the status of the 'hello' release in the command window.

```bash
helm status hello
```

* Kubernetes dashboard: run the following command to open a dashboard in your browser, where you can see all the Pods and services by clicking the various tabs. For example, if you're using minikube:

```bash
minikube dashboard
```

* Kubectl: run the following command to print the name, description, and status of the Pods in the command window. A list of events is also printed, which can be used for troubleshooting purposes. For example, if you detected a problem in one of the Pods, you can see the Pod termination reason and exit code.

```bash
kubectl describe pod
```

## Deleting a Data Grid from the Kubernetes Environment 


To delete the `hello` Space cluster, use the following Helm command. It removes the release, but leaves the logs and data so you can inspect them at a future time. This command will remove both the Platform Manager and the Space.

```bash
helm del hello
```

If you want to remove the release and delete all the `hello` release data from the server, add the `--purge` option.

```bash
helm del --purge hello
```

# Deploying a Space Cluster

The demo above created a data grid that contained a single Space instance. Real-life environments generally have to store large volumes of data, and therefore need more than a single Space instance (a cluster).

Type the following Helm command to deploy a Space cluster with n Data Pods, with a partition count from 1 to n:

```
helm install insightedge --name test --set pu.partitions=n
```

# Defining High Availability (HA) 

There are several aspects to configuring a data grid for high availability. Each primary Data Pod needs a minimum of one backup Data Pod, and three Management Pods are deployed instead of one so that a quorum of Platform Managers is always available to manage the Spaces. Both the Data Pods and the Management Pods should have the Pod anti-affinity property set to true, so that the primary/backup sets and the managers are deployed on different nodes. This enables successful failover if a node gets disrupted.

{{%note%}}
The Kubernetes minikube runs on a single node and therefore doesn't provide anti-affinity, so you may want to evaluate XAP and InsightEdge high-availability behavior on a Kubernetes cluster that contains multiple nodes.
{{%/note%}}

## Configuring High Availability for the Platform Manager

When the manager high availability property (`ha`) is set to true, Kubernetes deploys three Management Pods. You should enable the manager high availability property so these Management Pods are deployed on different nodes.

The following Helm command deploys three Management Pods named `testmanager`, with high availability enabled:

```bash
helm install insightedge --name testmanager --set manager.ha=true,manager.antiAffinity.enabled=true
```

## Defining the Space Topology

When you set the Space high availability property to true, Kubernetes deploys a backup Data Pod for each primary Data Pod. You must also enable the Space anti-affinity (`antiAffinity`) property so that the backup Data Pods are deployed on different nodes than the primary Data Pods.

{{%note%}}
If you apply Pod anti-affinity on a minikube, not all of the Pods will be deployed, because the environment contains only a single node.
{{%/note%}}

The following Helm command deploys a Space cluster called `test` in a high availability topology, with anti-affinity enabled: 

```bash
helm install insightedge --name test --set pu.ha=true,pu.antiAffinity.enabled=true,pu.manager.name=testmanager
```

# Deploying Multiple Spaces on Kubernetes

If you want to deploy multiple data grids in the same Kubernetes environment, to better utilize resources it is best to deploy one Platform Manager cluster and then configure the Spaces to use this cluster, instead of deploying each data grid with its own Platform Manager.

To enable users to customize the installation, we provide several additional Helm charts that can be used to define specific constellations in more advanced scenarios:

- Manager (`insightedge-manager` or `xap-manager`)
- Processing Unit (`insightedge-pu` or `xap-pu`)
- Apache Zeppelin (`insightedge-zeppelin`)

Before using these charts for the first time, you must fetch the charts as described in Getting Started section.

## Deploying the Platform Manager

The helm command by default creates a Management Pod and a Data Pod together. When deploying a Platform Manager that will connect to multiple Spaces, you have to disable the part of the command that creates the Data Pod. Type the following Helm command to create a Management Pod called `testmanager` without the accompanying Space:

```bash
helm install insightedge-manager --name testmanager
```

## Deploying the Spaces

After the Management Pod has been deployed and the Platform Manager is available, you can deploy the Space instances and connect them to the Platform Manager. Use the following Helm command to deploy a cluster of Data Pods called `testspace`, and to specify that the cluster should connect to the `testmanager` Management Pod:

```bash
helm install insightedge-pu --name testspace --set manager.name=testmanager
```

# Deploying a Processing Unit in Kubernetes

A Processing Unit is a container that can hold any of the following:

- Data only (a Space)
- Function only (business logic)
- Both data and a function 

You can use the event-processing example available with the XAP and InsightEdge software packages to see how data is fed to the function and processed in Processing Units. The example creates the following modules:

- Processor - a Processing Unit with the main task of processing unprocessed data objects. The processing of data objects is accomplished  using both and event container and remoting.
- Feeder - a Processing Unit that contains two feeders, a standard Space feeder and a JMS feeder, to feed unprocessed data objects that are in turn processed by the processor module. The standard Space feeder feeds unprocessed data objects  by both directly writing them to the Space and using OpenSpaces Remoting. The JMS feeder uses the JMS API to feed unprocessed data objects using a MessageConverter, which converts JMS ObjectMessages into data objects.

{{%note%}}
As a prerequisite for running this example, you must install Maven on the machine where you unpacked the GigaSpaces software package.
{{%/note%}}

To build and deploy the event-processing example in Kubernetes, the following steps are required:

1. Build the sample Processing Units from the GigaSpaces software package.
2. Provide a URL for deployment to Kubernetes.
3. Deploy a Platform Manager (Management Pod).
4. Deploy the Processing Units that were created when you built the example to Data Pods in Kubernetes, connecting them to the Management Pod.
5. View the processor logs to see the data processing results.

## Building the Processing Unit Example

The first step in deploying the sample Processing Units to Kubernetes is to build them from the examples directory. The example uses Maven as its build tool, and comes with a build script that runs Maven automatically. 

Open a command window and navigate to the following folder in the XAP or InsightEdge package: 

```bash
cd <product home>/examples/data-app/event-processing/
```
	
Type the following command (for Unix environments) to build the processor and feeder Processing Units:

```bash
./build.sh package
```

This build script finalizes the Processing Unit structure of both the processor and the feeder, and copies the processor JAR file to /examples/data-app/event-processing/processor/target/data-processor/lib, making the /examples/data-app/event-processing/processor/target/data-processor/ a ready-to-use Processing Unit. The final result is two Processing Unit JAR files, one under processor/target and another under feeder/target. 

## Providing a URL for Kubernetes

In order to deploy the Processing Units on Kubernetes, a URL must be provided. You can use an existing HTTP server, or you can create a local HTTP server using Helm. Ensure that your Kubernetes environment has access to the URL that you provide. If you opt for a local server, we recommend creating it from the examples directory so that it can easily access the Processing Unit JARs that were created.

Type the following Helm command in the command window to create a local HTTP server:

```bash
helm serve --repo-path . --address <your machine IP>:<port>
```

Leave this command window open so the server remains available and Kubernetes can connect to it.

## Deploying the GigaSpaces Components

Similar to deploying a Space cluster, it is best practice to first deploy the Management Pod (with the Platform Manager), and then deploy the Data Pods (first the processor, then the feeder).

Open a new command window and navigate to the charts directory (where you fetched the charts from our repo).

As was done for the Space demo, type the following Helm command to deploy a Management Pod called `testmanager`:

```bash
helm install insightedge-manager --name testmanager 
```

Next, type the following Helm command to deploy a Data Pod with the processor Processing Unit from the location where it was built in the examples directory:

```bash
helm install insightedge-pu --name processor --set manager.name=testmanager,resourceUrl=http://192.168.33.16:8877/test/gigaspaces-xap-enterprise-14.0.0-m9-b19909/examples/data-app/event-processing/processor/target/data-processor.jar
```

Lastly, type the following Helm command to deploy a Data Pod with the feeder Processing Unit from the same directory:

```bash
helm install insightedge-pu --name feeder --set manager.name=testmanager,resourceUrl=http://192.168.33.16:8877/test/gigaspaces-xap-enterprise-14.0.0-m9-b19909/examples/data-app/event-processing/feeder/target/data-feeder.jar
```

## Monitoring the Processing Units

You can use one of the Kubernetes tools to view the logs for the processor Data Pod, where you can see that the sample data has been processed.


# Managing the Application Environment

## Configuring the Container Memory Allocation

You can configure the memory allocation for a Pod for both the Docker container and the Java on-heap memory. The on-heap memory allocation can be defined as an absolute value, or as a percentage of the Docker container.

The following Helm command allocates the amount of memory for both the Docker container and for the on-heap memory as an absolute value:

```bash
helm install insightedge --name test --set pu.resources.limits.memory=512Mi,pu.java.heap=256m
```

You can define the maximum size of the Docker container as an absolute value, and the maximum on-heap memory allocation for the Java running inside the Docker container as a percentage. If you use this approach, make sure you leave enough memory for the Java.

The following Helm command sets an absolute value for the Docker container, and defines the maximum Java on-heap memory as a percentage of the container memory:

```bash
helm install insightedge --name test --set pu.resources.limits.memory=256Mi,pu.java.heap=75%
```

## Configuring the Data Grid using the Helm Chart

### Default Helm Chart

The InsightEdge Helm chart has a list of supported values that can be configured. To view this list, use the following Helm command:

```bash
helm inspect insightedge
```

The values.yaml file is printed in the command window, and each configurable value has a short explanation above it. The indentation in this printout indicates a use of a '.' (dot) in the value name. For example, the high availability property for the Platform Manager is listed as follows in the file:

`manager:`<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`ha:false`
	
The value you will set will look like this in the command window: `manager.ha=true`

### Customizing a Helm Chart

You can create additional values.yaml files with customized values.

The following Helm command creates a replica of the original values.yaml file called hello.yaml:

```bash
helm install insightedge -f customValues.yaml --name hello
```
### Overriding the Processing Unit Properties

It is recommended to define the Processing Unit properties in the pu.xml as placeholders (as described in the Processing Unit [Deployment Properties](https://docs.gigaspaces.com/xap/14.0/dev-java/deployment-properties.html#defining-property-place-holders-in-your-processing-unit) topic), so you can override these properties using the Helm chart.  

After defining the properties as placeholders, use the `key1=value1;key2=value2` format to pass the override values to the Helm chart using either the `--set insightedge-pu.properties=<your key-value pairs>` command, or by editing the values.yaml.

## Monitoring the Data Grid

While Kubernetes provides a number of ways to monitor the Pods and services, you can use the GigaSpaces administration tools to monitor the data grid (Spaces and Processing Units).

### REST Manager API

You can open the GigaSpaces REST Manager API and verify that your data grid was set up properly. You can access it from the minikube on your local machine or VM.

To get the IP address of your minikube, type the `minikube ip` command in the command window. Then type the following URL (using the minikube IP address) in your browser to access the REST Manager API:

`http://<minikube ip>:8090`

For information on how to use the REST Manager API, see the [Administration Tools](../xap/14.0/admin/admin-tools.html) section of the documentation. 

### GigaSpaces Command Line Interface

You can use the GigaSpaces CLI to monitor and administer the data grid.

To access the CLI, click the **EXEC** button in the Kubernetes dashboard to open a shell into the Management Pod. Next, navigate to the `/opt/gigaspaces/bin` directory and start the GigaSpaces CLI (insightedge or xap).

At this point, you can use the CLI commands to monitor the data grid, making sure to set the --server with the Manager Headless Service name. 

To view a list of Spaces, type the following command:

```bash
./insightedge --server=test-space-xap-manager-hs space list
```

To view the Data Type statistics, typ the following command:

```bash
./insightedge --server=test-space-xap-manager-hs space info --type-stats test-space
```

For more information about the GigaSpaces CLI and available commands, see the [Administration Tools](../xap/14.0/admin/admin-tools.html) section of the documentation. 

## Advanced Monitoring Using Kubernetes Tools

You can monitor the status of the various Kubernetes components using the Kubernetes dashboard or kubectl, as described in the [Monitoring the Kubernetes Cluster](#monitoring-the-kubernetes-cluster) section.

The test-space-xap-manager-hs is one of the Kubernetes services. To list all of the Kubernetes services and exposed ports, type the following command:

```bash
kubectl get services
```

For more information on using the Kubernetes monitoring tools, refer to the [Kubernetes documentation](https://kubernetes.io/docs/home/?path=users&persona=app-developer&level=foundational).

## Troubleshooting

If the Kubernetes environment doesn't launch properly, you can investigate by checking the Init Container logs. An init container is always run before a GigaSpaces Pod is started. After the init container runs to completion, Kubernetes deploys the actual Pod (such as a Management Pod, Data Pod, etc.). So when you deploy a Space, for example, an init container runs first to verify that the Platform Manager is available, and then the Data Pod with the Space is created.

You can access this log in the Kubernetes dashboard, or run the following kubectl command to print the init container log in the command window:
 
```bash
kubectl logs test-xap-space-1-0 -c check-manager-ready
```
