---
type: post
title:  Using Grafana for Monitoring and Analytics in Kubernetes
categories: SBP
parent: insightedge.html
weight: 150
---

|Author|Product Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|:----------:|
| Dharma Prakash and Dixson Huie| 14.0 | January 2019| | [InsightEdge metrics demo](https://github.com/Gigaspaces-sbp/kubernetes/tree/master/ie-metrics-demo) |

# Overview

Monitoring a production environment of any system requires inspecting many statistics; InsightEdge and XAP are no different. The Web Management Console collects and displays various data grid metrics by polling the system's components, but these statistics have some limitations:

- **History** - Statistics are polled and aggregated in memory at runtime, so they're limited to a few minutes of history, whereas in production users often need at least several days' worth of statistics.
- **Persistency** - If the web server goes down, all statistics are lost.
- **Scalability** - The web server gathers statistics by polling each component. This approach is not scalable, and does not behave well on large clusters.

To overcome these limitations, GigaSpaces products include a powerful and versatile [framework for reporting metrics](https://docs.gigaspaces.com/latest/admin/metrics-overview.html) - each component can report its own metrics (or even user-defined metrics) to any database or tool the user favours by implementing a [metrics reporter](https://docs.gigaspaces.com/latest/admin/metrics-custom-reporter.html). In addition, GigaSpaces provides a [built-in reporter for InfluxDB](https://docs.gigaspaces.com/latest/admin/metrics-influxdb-reporter.html) and integration with Grafana to provide metrics storage and visualization: 

- {{%exurl "InfluxDB""http://influxdb.com/"%}} is an open-source distributed time-series database with a powerful query language.
- {{%exurl "Grafana""http://grafana.org"%}} is a graph and dashboard builder for visualizing time-series metrics, which supports InfluxDB (and other data sources).

These third-party applications can be installed in Kubernetes to enable monitoring, analytics and data visualization for InsightEdge and XAP installed with KubeGrid. This topic describes a sample configuration of InfluxDB and Grafana in Kubernetes, using sample dashboards to display data from the data grid.

{{% note%}}The data in the InfluxDB database is lost if the application is undeployed from your system.{{% /note%}}

# Prerequisites

Before running this demo, create an account and a repository on Docker Hub if you don't already have them. You will also need the following applications installed locally:

- Oracle Virtualbox - tool that runs the guest operating system that hosts the minikube.
- Docker - the containers that hold the InsightEdge and XAP pods are Docker based.
- Helm - the package manager that GigaSpaces uses to install products on Kubernetes.
- Minikube - tool that runs a single-node Kubernetes cluster for developing and testing applications in Kubernetes.
- Kubectl - the Kubernetes CLI used to run commands against a Kubernetes cluster.

{{% note%}}
This demo was set up using the following configuration:

- Oracle Virtualbox version 5.2.22
- Docker version 18.09
- Helm version 2.11
- Minikube version 0.31
- Kubectl version 1.13
{{% /note%}}

# Integration Steps

Integrating InfluxDB and Grafana for monitoring GigaSpaces products in Kubernetes requires the following steps:

1. Clone the [InsightEdge metrics demo](https://github.com/Gigaspaces-sbp/kubernetes/tree/master/ie-metrics-demo) from the Github repo to your local machine.
1. Install and configure InfluxDB on your local machine and in Kubernetes.
1. Install and configure Grafana in Kubernetes.
1. Create a custom Docker image and upload it to your Docker Hub account.
1. Install InsightEdge in Kubernetes using the custom Docker image.
1. In the Grafana client, configure the data source and apply the URL for the GigaSpaces metrics reporter.
1. Import the two sample dashboards from the InsightEdge metrics demo software package.

# Installing and Configuring InfluxDB

InfluxDB needs to be installed in two places:

- On your local machine in order to run the client.
- In Kubernetes (minkube) to collect the metrics from the data grid. 

After installing InfluxDB in Kubernetes, you need to apply port forwarding so that InfluxDB can communicate with Grafana, and set up the InfluxDB demo database so it can hold the metrics data.

To install InfluxDB on your local machine:

1. Download the software from {{%exurl "here""https://portal.influxdata.com/downloads/"%}}.
1. Follow the {{%exurl "installation instructions""https://influxdb.com/docs/v0.9/introduction/installation.html"%}}.
1. Make sure that the InfluxDB database service is not running locally (the actual database will be located in the Kubernetes installation).

To install and configure InfluxDB in Kubernetes:

1. In the command window, type the following:

	```
	helm install --name ie-metrics stable/influxdb
	```
	
	{{% note%}}This installation is only for the purposes of running the demo, so the credentials aren't set.{{% /note%}}

1. Set port forwarding by typing the the following command:

	```
	kubectl port-forward --namespace default $(kubectl get pods --namespace default -l app=ie-metrics-influxdb -o jsonpath='{ .items[0].metadata.name }') 8086:8086
	```
	
1. Run the InfluxDB CLI and do the following to create an InfluxDB database for the demo:

	- To start a database session, type `influx`.
	- To create the demo database, type `create database demodb`.
	- To verify that the database was created, type `show databases`. 
	
# Installing and Configuring Grafana 

The next step in the integration is setting up Grafana. This includes installing Grafana in Kubernetes, setting port forwarding and configuring the Grafana client to use InfluxDB and the sample dashboards in the demo.

{{% note%}}Make sure you have already cloned the InsightEdge metrics demo software from Github; you will need the files when you configure the Grafana client.{{% /note%}}

## Installing Grafana in Kubernetes

To install Grafana in Kubernetes:

1. In the command window, type the following:

	```
	helm install --name ie-grafana stable/grafana
	```

1. Set the port forwarding for Grafana in one of the following ways:

	- Short form:
	
	```
	kubectl port-forward --namespace default $(kubectl get pods --namespace default -l "app=grafana,release=ie-grafana" -o jsonpath='{ .items[0].metadata.name }') 3000
	```
		
	- Long form:
	
	```
	export POD_NAME=$(kubectl get pods --namespace default --selector "app=grafana,release=ie-grafana" --output="jsonpath='{ .items[0].metadata.name }')"
	kubectl port-forward --namespace default $POD_NAME 3000
	```

# Preparing the Custom Docker Image

The native GigaSpaces InfluxDB metrics reporter is disabled by default in the InsightEdge software package. Therefore, you need to prepare a custom Docker image that will use the modified (enabled) [metrics.xml](https://github.com/Gigaspaces-sbp/kubernetes/blob/master/ie-metrics-demo/docker/metrics.xml) file in the InsightEdge metrics demo. This custom Docker image should then be uploaded to the Docker Hub account that you created, so it can be accessed by the InsightEdge Helm chart.

To prepare the custom Docker image:

1. Log in to your Docker Hub repository:

	```
	sudo docker login --username=<your username>
	```

1. Build the custom Docker image that will include the modified metrics file:

	```
	sudo docker build -t <username>/ie-metrics-demo:14.0.1 
	sudo docker image ls
	```
	
1. Push the Docker image to your Docker Hub account:

	```
	sudo docker push <username>/ie-metrics-demo:14.0.1
	```

# Installing InsightEdge with the Custom Docker Image

At this point, you are finally ready to install InsightEdge in Kubernetes. You have both of the third-party applications with their connection information up and running in Kubernetes, and you have the custom Docker image that supports the metrics configuratgion file with the InfluxDB reporter enabled.

To install InsightEdge in Kubernetes:

1. Download the InsightEdge Helm charts as follows:

	- Add the GigaSpaces chart repository:
		
	```
	helm repo add gigaspaces https://resources.gigaspaces.com/helm-charts
	```
	
	- Download the Helm charts to your local machine. The following command fetches the InsightEdge charts and extracts them:
	
	```
	helm fetch gigaspaces/insightedge --version=14.0.1 --untar
	```

1. 	To instruct Helm to install InsightEdge using the custom Docker image, modify the following property in the `insightedge/charts/insightedge-pu/values.yaml` file, where &lt;username&gt; is your Docker Hub account:

	```
	repository:
	<username>/ie-metrics-demo
	```
	
1. Deploy InsightEdge in Kubernetes:

	```
	helm install insightedge --name demo
	```

{{% note%}}For more information about the GigaSpaces Helm charts and how to install and configure InsightEdge and XAP in Kubernetes, see the [KubeGrid](https://docs.gigaspaces.com/latest/admin/kubernetes-overview.html) section of the product documentation.{{% /note%}}	
	
# Using Grafana to View InsightEdge Metrics
	
## Configuring the Grafana Client

You need to do the following in the Grafana client so that it will work with the GigaSpaces metrics reporter and the sample dashboards from the InsightEdge metrics demo:

- Configure the data source.
- Import the sample dashboards from the demo package.

**To configure the data source:**

1. Open a browser to `localhost:3000` and provide the username and password.

	{{% note%}}The username is `admin`. To get the password, type the following command in the command window:
	
		```
		kubectl get secret --namespace default ie-grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo
		```
	
	{{% /note%}}
	
1. Click the **Settings** <img src="/attachment_files/sbp/grafana/settings-icon.png" width="24" height="22" /> icon, then select **Data Sources**.
1. Click **Add Data Source**. In the Settings tab of the Data Sources/New screen, do the following:
	
	- In the **Name** field, type the name of the data source: `ie-metrics-demo`
	- From the **Type** dropdown list, select `InfluxDB`.
	- In the **HTTP** area, type the following URL: `http://ie-metrics-influxdb.default:8086`
	- In the **InfluxDB Details** area, type the database name: `demodb`

{{% align center%}}
<img src="/attachment_files/sbp/grafana/ie-metrics-demo-datasource.png" width="589" height="590" />
{{% /align%}}
	
**To import the the sample dashboards:**

1. Click the **Create** <img src="/attachment_files/sbp/grafana/create-icon.png" width="24" height="22" /> icon, then select  **Import**.
1. In the Import window, click **Upload .json file**.
1. Navigate to the Space-demo.json file in the cloned InsightEdge metrics demo and select it.
1. Click **Load**.
1. Configure the data grid as the data source in the **Options** area by selecting **ie-metrics-demo** from the xap dropdown list.
1. Repeat the process for the ProcessingUnit-demo.json file.
	
{{% align center%}}
<img src="/attachment_files/sbp/grafana/grafana-import-dashboard.png" width="730" height="337" />
{{% /align%}}
	
## Viewing the Dashboards

After you finish configuring the Grafana client, you can open the dashboards and view the metrics.

{{% align center%}}
![space-demo-dashboard.png](/attachment_files/sbp/grafana/pu-demo-dashboard.png)
{{% /align%}}


