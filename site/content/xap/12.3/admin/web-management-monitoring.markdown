---
type: post123
title:  Using Grafana for Monitoring and Analytics
categories: XAP123ADM,PRM
parent: web-management-console.html
weight: 540
---

{{% ssummary %}}{{% /ssummary %}}

# Overview

Monitoring a production environment of any system requires inspecting many statistics, and XAP is no different. The Web Management Console provides various metrics by polling the system's components, but these statistics have a few limitations:

- **History** - Since statistics are polled and aggregated at runtime in memory, they're limited to a few minutes of history, whereas in production users often need to store metrics for a few days at least.
- **Persistency** - If the web server goes down, all statistics are lost.
- **Scalability** - The web server gathers statistics by polling each component. This approach is not scalable, and does not behave well on large clusters.

To overcome these limitations, XAP provides a powerful and versatile [framework for reporting metrics](./metrics-overview.html) - each component can report its own metrics (or even user-defined metrics) to any database or tool the user favours by implementing a [metrics reporter](./metrics-custom-reporter.html). In addition, XAP provides a [built-in reporter for InfluxDB](./metrics-influxdb-reporter.html) and integration with Grafana to provide metrics storage and visualization: 

- {{%exurl "InfluxDB""http://influxdb.com/"%}} is an open-source distributed time-series database with a powerful query language.
- {{%exurl "Grafana""http://grafana.org"%}} is a graph and dashboard builder for visualizing time-series metrics, which supports InfluxDB (and other data sources).

Once enabled, XAP reports metrics to InfluxDB, and the Web Management Console provides an embedded Grafana view in the **Monitoring** tab, as well as automatic creation of dashboards in Grafana whenever a new Processing Unit is deployed.

InfluxDB and Grafana are both open-source and free, but are not bundled in XAP distribution. Installation is straightforward, as described below. Note that XAP's default metrics configuration is set to match the default settings of InfluxDB and Grafana, so if this is your first time we recommend sticking with the defaults to simplify the process.

# Installing and Configuring InfluxDB

To install InfluxDB (0.9 or later), download from {{%exurl "here""https://influxdb.com/download/index.html"%}} and follow the {{%exurl "installation instructions""https://influxdb.com/docs/v0.9/introduction/installation.html"%}}.

To configure InfluxDB,  edit the **metrics.xml** file (found under `[XAP_HOME]/config/metrics`). Change the following part according to your influxDB host (myhost) and database name (mydb) that stores metrics:


```xml

    <grafana>
        <datasources>
            <datasource name="influxdb">
                <property name="type" value="influxdb"/>
                <property name="url" value="http://myhost:8086/db/mydb"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </datasource>
            <datasource name="grafana">
                <property name="type" value="influxdb"/>
                <property name="url" value="http://myhost:8086/db/grafana"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
                <property name="grafanaDB" value="true"/>
            </datasource>
        </datasources>
    </grafana>

```

# Installing and Configuring Grafana

To install Grafana (2.5 or later), download from {{%exurl "here""http://grafana.org/download/"%}}, and follow the {{%exurl "installation instructions""http://docs.grafana.org/installation/"%}}.

After installation, you need to configure Grafana to work with XAP's metrics configuration, which is located at `[XAP_HOME]/config/metrics/metrics.xml`. Assuming you've installed InfluxDB and Grafana without changing the defaults, all you need to do is uncomment the InfluxDB `reporter` and `grafana` elements, as shown below:

```xml
<metrics-configuration>
    <reporters>
        <reporter name="influxdb">
            <property name="host" value="localhost"/>
            <property name="database" value="mydb"/>
        </reporter>
    </reporters>
    <grafana url="http://localhost:3000" api-key="" user="admin" password="admin">
        <datasources>
            <datasource name="xap">
                <property name="type" value="influxdb"/>
                <property name="isDefault" value="true"/>
                <property name="url" value="http://localhost:8086"/>
                <property name="database" value="mydb"/>
                <property name="access" value="proxy"/>
            </datasource>
        </datasources>
    </grafana>
</metrics-configuration>
```

- `reporter` - InfluxDB installation automatically creates a database called `mydb`, which matches the default. See [InfluxDB Reporter](./metrics-influxdb-reporter.html) for information about other optional settings.
- `grafana` - Grafana installation defaults to port `3000` with username/password `admin`/`admin`. In production you'll probably change those in Grafana, so make sure to change them in `metrics.xml` as well. Even better, you can generate an API key in Grafana and place it in the `api-key` attribute.
- `datasource` - This tells Grafana how to connect to your InfluxDB database. Note that InfluxDB uses port `8086` by default for an API endpoint, and that we're connecting to the default `mydb` database.

{{%note "Verifying the default database exists"%}}
Some InfluxDB packages do not automatically create the default `mydb` database. we recommend you use the InfluxDB [Web Admin](https://influxdb.com/docs/v0.9/tools/web_admin.html) or [shell](https://influxdb.com/docs/v0.9/tools/shell.html) to check if it exists, and create it if needed. For more info see [Getting Started with InfluxDB](https://influxdb.com/docs/v0.9/introduction/getting_started.html#logging-in-and-creating-your-first-database).
{{%/note%}}

# Getting Started

AFter you install InfluxDB and Grafana and configure the `metrics.xml` file, start the Web Management Console and navigate to the **Monitoring** tab - you'll see Grafana's home page (you'll probably get a login page on the first time - just type in the default `admin`/`admin`, and you'll get the home page):

![image](/attachment_files/web-console/monitor.jpg)

Since we haven't deployed anything yet, the **Dashboards** list is empty. Go ahead and deploy a space or any other processing unit - you'll notice a default dashboard is created for each processing unit and space, with graphs showing commonly used metrics.

# Dashboards

![image](/attachment_files/web-console/monitor1.jpg)

 

## Default Space dashboard
 
![image](/attachment_files/web-console/monitor2.jpg)

 

## Default Processing Unit dashboard 
 
![image](/attachment_files/web-console/monitor3.jpg)
