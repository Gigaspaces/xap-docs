---
type: post
title:  Data Visualization with Grafana
categories: SBP
parent: insightedge.html
weight: 100
---

|Author|Product Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|:----------:|
| Ester Atzmon| 14.0 | January 2019| | [InsightEdge-Grafana Connector](https://github.com/Gigaspaces/insightedge-grafana-connector)|

# Overview

Data visualization helps people understand the significance of data by placing it in a visual context. Patterns, trends and correlations can be exposed and recognized using data visualization software.

Data visualization software is also important for big data and advanced analytics projects. Businesses need a fast, easy way to get an overview of their data. Visualization is also key for advanced analytics; when implementing advanced predictive analytics or machine learning algorithms, the output needs to be visualized to monitor results and make sure that the data models are performing correctly. 

The InsightEdge Platform can be easily integrated with {{%exurl "Grafana""http://grafana.org"%}}, a graph and dashboard builder for visualizing time-series metrics so users can create, explore, and share their dashboards no matter where the data is stored.  

To use Grafana for data visualization with InsightEdge, you will have to do the following:

1. Download and configure the InsightEdge-Grafana connector.
1. Download and install the Grafana client.
1. Download and install the Grafana SimpleJson data source plugin (available from the Grafana website).
1. Change the default port of the Grafana client (the default port is usually blocked).
1. In the Grafana client, configure the data source and apply the URL for the InsightEdge-Grafana connector.

After making the above changes, you can visualize data from the Space using Grafana dashboards. The following sections provide detailed instructions on integrating InsightEdge and Grafana.

{{%note%}}
This sample integration was run in the following environment:

- InsightEdge Platform release 14.0.1
- Grafana version 5.2.4
- SimpleJson version 1.4.0
{{%/note%}}

# Downloading and Configuring the InsightEdge-Grafana Connector

The first step in integrating InsightEdge with Grafana is to download and configure the connector so that Grafana can access the data from the InsightEdge-based application.

{{%note%}}The configuration files are located in the *<host directory>/timeseries-rest/tree/master/src/main/resources* folder.{{%/note%}}

To install the InsightEdge-Grafana connector:

1. Clone the contents of the github repo.
1. In the [grafana-insightedge.properties file](https://github.com/Gigaspaces/insightedge-grafana-connector/blob/master/src/main/resources/grafana-insightedge.properties), modify the properties to suit your local evironment (host, port, space name, lookup group, and lookup locator (optional)).

	Sample configuration:	
	```
	CONNECTOR_HOST=HostName
	CONNECTOR_PORT=8082
	XAP_SPACE_NAME=demo
	XAP_LOOKUP_GROUPS=xap-14.0.0
	#XAP_LOOKUP_LOCATORS=
	```
	{{%note%}}The syntax of the URL that is used to configure the simple JSON data source to work with the Space is `http://<yourhost>:<port>/insightedge/metrics`. The variables are based on values you added to the properties file.{{%/note%}}

1. Configure the [tablesData.txt file](https://github.com/Gigaspaces/insightedge-grafana-connector/blob/master/src/main/resources/tablesData.txt) that the connector will use to categorize the data. 

	Sample data values:
	```
	TelemetryVolt,com.gigaspaces.Telemetry,longDate,volt,machineID
	TelemetryRotate,com.gigaspaces.Telemetry,longDate,rotate,machineID
	TelemetryPressure,com.gigaspaces.Telemetry,longDate,pressure,machineID
	TelemetryVibration,com.gigaspaces.Telemetry,longDate,vibration,machineID
	```
	Each row represents the following fields in Grafana (the values from the first line of the above sample are provided for reference):
	- &lt;display name of table as it appears in the dashboard&gt; - `TelemetryPressure`
	- &lt;class name in the Space&gt; - `com.gigaspaces.Telemetry`
	- &lt;timestamp field in the class (of type long)&gt; - `longDate`
	- &lt;field name that contains the requested value (of type double)&gt; - `pressure`
	- &lt;name of data series&gt; - `machineID`


# Installing and Configuring Grafana Products

## Installing the Grafana Client (Dashboard Application)

Grafana is not bundled with GigaSpaces products, but the product installation is easy and straightforward.
To install Grafana:

1. Download the client from the Grafanalabs {{%exurl "download page""http://grafana.org/download/"%}}.
1. Follow the {{%exurl "installation instructions""http://docs.grafana.org/installation/"%}}.

## Installing the SimpleJson Data Source Plugin

After you download and install the dashboard application, download and install the SimpleJson data source plugin.

**To install the plugin:**

1. Download the plugin package from the {{%exurl "Grafana plugin page""https://grafana.com/plugins/grafana-simple-json-datasource/installation"%}}.
1. Open a command window from the `\bin` folder of the Grafana client software.
1. Type `grafana-cli plugins install grafana-simple-json-datasource`. The plugin is installed into your Grafana plugins directory; the default is */var/lib/grafana/plugins*.

## Configuring the Grafana Client

After installing the Grafana client and the SimpleJson data source plugin, you must configure the following so that Grafana will work with the InsightEdge connector and visualize the data in the Space:

- Change the default port of the Grafana client to an open port (the default port is usually blocked).
- Configure the data source.

**To configure the data source:**

1. Run the Grafana server as described in the Grafana installation instructions.
1. In the Settings tab of the Data Sources screen, configure the following:
	- Assign a **Name** for the data source. For example, InsightedgeDS.
	- Verify that the data source **Type** is SimpleJson.
	- In the HTTP area, type the URL of the connector in the **URL** field using the syntax described in the section [above](#downloading-and-configuring-the-insightedge-grafana-connector).

{{% align center%}}
![grafana-client.png](/attachment_files/sbp/grafana/grafana-client.png)
{{% /align%}}
	
# Visualizing the Data

After you finish configuring the Grafana client, you can create new dashboards to view the data from the Space. For example, using the sample data in the connector package, you can create a Telemetry dashboard, as shown below.

**To view the Space data in the dashboard:**

1. In the upper-right area of the dashboard, define the time period for which you want to see data.
1. In the Metrics tab below the graph, select the InsightedgeDS data source.
1. Select TelemetryPressure from the dropdown list.

{{% align center%}}
![telemetry-dashboard.png](/attachment_files/sbp/grafana/telemetry-dashboard.png)
{{% /align%}}


