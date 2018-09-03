---
type: post
title:  Deploying a Data Grid in Kubernetes
parent: kubernetes-overview.html
categories: EARLY_ACCESS
weight: 100
---

In order to familiarize yourself with the data grid and InsightEdge in a Kubernetes deployment, you can launch a demo. This topic describes how to perform basic deployment tasks with the demo using simple Helm commands. You can perform these deployment tasks on the Kubernetes minikube, because you only need a single node. 

# Starting a Data Grid in Kubernetes

Run the following Helm command in the command window to start a data grid in Kubernetes. This deploys a Kubernetes cluster called `hello`, which contains a data grid comprised of one Space in a Data Pod, and one Platform Manager called `hello-xap-manager` in a Management Pod. The Platform Manager manages the Space, the Manager service, and the headless service. There are no backup instances specified. 

```
helm install xap --name hello
```

## Working with Data Objects

The [Deploying a Processing Unit in Kubernetes](#deploying-a-processing-unit-in-kubernetes) section provides an example of how the data grid stores and processes data objects.

## Monitoring the Data Grid in Kubernetes

You can monitor the `hello` cluster you deployed using any of the following administration tools.

* Kubernetes dashboard: run the following command to open a dashboard in your browser, where you can see all the Pods and services by clicking the various tabs.

```
minikube dashboard
```

* Kubectl: run the following command to print the name, description, and status of the Pods in the command window. A list of events is also printed, which can be used for troubleshooting purposes. For example, if you detected a problem in one of the Pods, you can see the Pod termination reason and exit code.

```
kubectl describe pod
```

* Helm chart: run the following command to print the status of the 'hello' release in the command window.

```
helm status hello
```

## Deleting a Data Grid from the Kubernetes Environment 


To delete the `hello` Space cluster, use the following Helm command. It removes the release, but leaves the logs and data so you can inspect them at a future time. This command will remove both the Platform Manager and the Space.

```
helm del hello
```

If you want to remove the release and delete all the `hello` release data from the server, add the `--purge` option.

```
helm del --purge hello
```

# Beyond the Basics

This section describes how to create a more complex data grid deployment in Kubernetes, such as deploying multiple or clustered Spaces, configuring high availability, and implementing Pod anti-affinity.

While you can use the Kubernetes minikube to deploy a clustered Space, performance may be affected based on the available memory and CPU power of the local machine or VM running the minikube. Additionally, there are some features that can't be applied if you are deploying on a single node, such as Pod anti-affinity.

## Deploying a Clustered Space on Kubernetes

When you deploy a data grid that is comprised of a clustered Space in a standard environment, you need one Management Pod that contains the Platform Manager, and multiple Data Pods for the Space cluster.

### Deploying the Platform Manager

If you want to connect multiple Spaces to a single Platform Manager, you should deploy the Management Pod first, using the following Helm command to create a Pod called `testmanager`:
```
helm install xap/ --name testmanager --set space.enabled=false
```

### Creating a Space Cluster

After the Management Pod has been deployed and the Platform Manager is available, you can deploy the Space instances and connect them to the Platform Manager. Use the following Helm command to deploy a cluster of Data Pods called `testspace` with `n` partitions, and to specify that the cluster should connect to the `testmanager` Management Pod:

```
helm install xap/ --name testspace --set space.manager=testmanager,space.partitions=n
```

## Deploying a Processing Unit in Kubernetes

A Processing Unit is a container that can hold any of the following:

- Data only (a Space)
- Function only (business logic)
- Both data and a function 

You can use the event-processing example available with the XAP and InsightEdge software packages to see how data is fed to the function and processed in Processing Units. The example creates the following modules:

- Processor - a Processing Unit with the main task of processing unprocessed data objects. The processing of data objects is accomplished  using both and event container and remoting.
- Feeder - a Processing Unit that contains two feeders, a standard Space feeder and a JMS feeder, to feed unprocessed data objects that are in turn processed by the processor module. The standard Space feeder feeds unprocessed data objects  by both directly writing them to the Space and using OpenSpaces Remoting. The JMS feeder uses the JMS API to feed unprocessed data objects using a MessageConverter, which converts JMS ObjectMessages into data objects.

**Note:** As a prerequisite for running this example, you must install Maven on the machine where you unpacked the GigaSpaces software package.

To build and deploy the event-processing example in Kubernetes, the following steps are required:

1. Build the sample Processing Units from the GigaSpaces software package.
2. Provide a URL for deployment to Kubernetes.
3. Deploy a Platform Manager (Management Pod).
4. Deploy the Processing Units that were created when you built the example to Data Pods in Kubernetes, connecting them to the Management Pod.
5. View the processor logs to see the data processing results.

### Building the Processing Unit Example

The first step in deploying the sample Processing Units to Kubernetes is to build them from the examples directory. The example uses Maven as its build tool, and comes with a build script that runs Maven automatically. 

Open a command window and navigate to the following folder in the XAP or InsightEdge package: 

```
cd <xap home>/examples/data-app/event-processing/
```
	
Type the following command (for Unix environments) to build the processor and feeder Processing Units:

```
./build.sh package
```

This build script finalizes the Processing Unit structure of both the processor and the feeder, and copies the processor JAR file to /examples/data-app/event-processing/processor/target/data-processor/lib, making the /examples/data-app/event-processing/processor/target/data-processor/ a ready-to-use Processing Unit. The final result is two Processing Unit JAR files, one under processor/target and another under feeder/target. 

### Providing a URL for Kubernetes

In order to deploy the Processing Units on Kubernetes, a URL must be provided. You can use an existing HTTP server, or you can create a local HTTP server using Helm. Ensure that your Kubernetes environment has access to the URL that you provide. If you opt for a local server, we recommend creating it from the examples directory so that it can easily access the Processing Unit JARs that were created.

Type the following Helm command in the command window to create a local HTTP server:

```
helm serve --repo-path . --address <your machine IP>:<port>
```

Leave this command window open so the server remains available and Kubernetes can connect to it.

### Deploying the GigaSpaces Components

Similar to deploying a Space cluster, it is best practice to first deploy the Management Pod (with the Platform Manager), and then deploy the Data Pods (first the processor, then the feeder).

Open a new command window and navigate to the charts directory in `<xap home>/tools/kubernetes/charts/`.

As was done for the Space demo, type the following Helm command to deploy a Management Pod called `testmanager`:

```
helm install xap/ --name testmanager --set space.enabled=false
```

Next, type the following Helm command to deploy a Data Pod with the processor Processing Unit from the location where it was built in the examples directory:

```
helm install xap/ --name processor --set space.manager=testmanager,space.pu.resource=http://192.168.33.16:8877/test/gigaspaces-xap-enterprise-14.0.0-m9-b19909/examples/data-app/event-processing/processor/target/data-processor.jar
```

Lastly, type the following Helm command to deploy a Data Pod with the feeder Processing Unit from the same directory:

```
helm install xap/ --name feeder --set space.manager=testmanager,space.pu.resource=http://192.168.33.16:8877/test/gigaspaces-xap-enterprise-14.0.0-m9-b19909/examples/data-app/event-processing/feeder/target/data-feeder.jar
```

### Monitoring the Processing Units

You can use one of the Kubernetes tools to view the logs for the processor Data Pod, where you can see that the sample data has been processed.

## Defining a High Availability (HA) Environment

There are several aspects to configuring a data grid for high availability. Each primary Data Pod needs a minimum of one backup Data Pod, and three Management Pods are deployed instead of one so that a quorum of Platform Managers is always available to manage the Spaces. Both the Data Pods and the Management Pods should have the Pod anti-affinity property set to true, so that the primary/backup sets and the managers are deployed on different nodes. This enables successful failover if a node gets disrupted.

**Note:** The Kubernetes minikube runs on a single node and therefore doesn't provide anti-affinity, so it is best to evaluate XAP and InsightEdge high-availability behavior on a Kubernetes cluster that contains multiple nodes. 

### Defining High Availability for the Platform Manager

When the manager high availability property (`ha`) is set to true, Kubernetes deploys three Management Pods. You should enable the manager high availability property so these Management Pods are deployed on different nodes.

The following Helm command deploys three Management Pods named `testmanager`, with high availability enabled:

```
helm install xap/ --name testmanager --set space.enabled=false,manager.ha=true,manager.antiAffinity.enabled=true
```

### Defining the Space Topology

When you set the Space high availability property to true, Kubernetes deploys a 2.1 topology, meaning two primary Data Pods (with a Pod ID of `0` for the first primary, and `1` for the second primary) and one backup Data Pod per primary. You must also enable the Space anti-affinity (`antiAffinity`) property so that the backup Data Pods are deployed on different nodes than the primary Data Pods.

The following Helm command deploys a Space cluster called `test` in a high availability topology, with anti-affinity enabled: 

```
helm install xap --name test --set space.ha=true,space.antiAffinity.enabled=true,space.manager=testmanager
```

# Managing the Application Environment

## Configuring the Container Memory Allocation

You can configure the memory allocation for a Pod for both the Docker container and the Java on-heap memory. The on-heap memory allocation can be defined as an absolute value, or as a percentage of the Docker container.

The following Helm command allocates the amount of memory for both the Docker container and for the on-heap memory as an absolute value:

```
helm install xap --name test --set space.resources.limits.memory=512Mi,space.java.heap=256m
```

You can define the maximum size of the Docker container as an absolute value, and the maximum on-heap memory allocation for the Java running inside the Docker container as a percentage. If you use this approach, make sure you leave enough memory for the Java.

The following Helm command sets an absolute value for the Docker container, and defines the maximum Java on-heap memory as a percentage of the container memory:

```
helm install xap --name test --set space.resources.limits.memory=256Mi,space.java.heap=75%
```

## Configuring the Helm Chart Values

### Default Helm Charts

The Helm charts have a list of supported values that can be configured. To view this list, use the following Helm command:

```
helm inspect xap/
```

The values.yaml file is printed in the command window, and each configurable value has a short explanation above it. The indentation in this printout indicates a use of a '.' (dot) in the value name. For example, the high availability property for the Platform Manager is listed as follows in the file:

manager:
    ha:false
	
The value you will set will look like this in the command window: `manager.ha=true`

### Customizing a Helm Chart

You can create additional values.yaml files with customized values.

The following Helm command creates a replica of the original values.yaml file called hello.yaml:

```
helm install xap -f customValues.yaml --name hello
```

## Monitoring the Data Grid

While Kubernetes provides a number of ways to monitor the Pods and services, you can use the GigaSpaces administration tools to monitor the data grid (Spaces and Processing Units).

### REST Manager API

You can open the GigaSpaces REST Manager API and verify that your data grid was set up properly. You can access it from the minikube on your local machine or VM.

To get the IP address of your minikube, type the `minikube ip` command in the command window. Then type the following URL (using the minikube IP address) in your browser to access the REST Manager API:

`http://<minikube ip>:8090`

For information on how to use the REST Manager API, see the [Administration Tools](../xap/12.3/admin/admin-tools.html) section of the documentation. 

### GigaSpaces Command Line Interface

You can use the GigaSpaces CLI to monitor and administer the data grid.

To access the CLI, click the **EXEC** button in the Kubernetes dashboard to open a shell into the Management Pod. Next, navigate to the `/opt/gigaspaces/bin` directory and start the GigaSpaces CLI (xap or insightedge).

At this point, you can use the CLI commands to monitor the data grid, making sure to set the --server with the Manager Headless Service name. 

To view a list of Spaces, type the following command:

```
./xap --server=test-space-xap-manager-hs space list
```

To view the Data Type statistics, typ the following command:

```
./xap --server=test-space-xap-manager-hs space info --type-stats test-space
```

For more information about the GigaSpaces CLI and available commands, see the [Administration Tools](../xap/12.3/admin/admin-tools.html) section of the documentation. 

## Advanced Monitoring Using Kubernetes Tools

You can monitor the status of the various Kubernetes components using the Kubernetes dashboard or kubectl, as described in the [Monitoring the Kubernetes Cluster](#monitoring-the-kubernetes-cluster) section.

The test-space-xap-manager-hs is one of the Kubernetes services. To list all of the Kubernetes services and exposed ports, type the following command:

```
kubectl get services
```

For more information on using the Kubernetes monitoring tools, refer to the [Kubernetes documentation](https://kubernetes.io/docs/home/?path=users&persona=app-developer&level=foundational).

## Troubleshooting

If the Kubernetes environment doesn't launch properly, you can investigate by checking the Init Container logs. An init container is always run before a GigaSpaces Pod is started. After the init container runs to completion, Kubernetes deploys the actual Pod (such as a Management Pod, Data Pod, WAN Gateway Pod, etc.). So when you deploy a Space, for example, an init container runs first to verify that the Platform Manager is available, and then the Data Pod with the Space is created.

You can access this log in the Kubernetes dashboard, or run the following kubectl command to print the init container log in the command window:
 
```
kubectl logs test-xap-space-1-0 -c check-manager-ready
```
