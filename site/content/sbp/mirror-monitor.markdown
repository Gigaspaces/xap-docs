---
type: postsbp
title:  Mirror Monitor
categories: SBP
parent: production.html
weight: 700
---

{{% tip %}}
**Summary:**  Mirror Service Monitoring Utility <br/>
**Author**: Shay Hassidim, Deputy CTO, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 7.1<br/>
**Last Update:** April 2010<br/>
**Contents:**<br/>


{{% /tip %}}

# Overview
The Mirror Service is an essential component when persisting your data into the enterprise database. The [Mirror Service]({{%latestjavaurl%}}/asynchronous-persistency-with-the-mirror.html) delegates the IMDG activities into the database in a reliable asynchronous manner, allowing the application to access the data stored in-memory without having the database as part of the critical path of the transaction. When using the Mirror Service, the database performance and availability would not impact the application response time.

The Mirror Service behavior is important for the stability of the application and the consistency of the data within the Database. With large scale application, you might want to monitor the Mirror Service behavior in real time. This means reacting in a timely fashion in case there is a need to intervene with its activity or related components that interacts with the Mirror Service (IMDG, Database).

# How the Mirror Monitor works?
The Mirror Monitor utility gathers statistics about the Mirror Service behavior and exposes these via standard JMX Mbean. You may access this collected data using standard JMX viewers such as the JConsole utility comes with the JVM.

The Mirror Monitor extends the built-in openspaces `DefaultHibernateExternalDataSource`, and collects statistics about the activities the mirror performs. You may log these statistics into a log file or/and access these by using the JMX API.

The Mirror Monitor using the GigaSpaces [Administration and Monitoring API]({{%latestjavaurl%}}/administration-and-monitoring-api.html) to receive information about the current status of the replication redo log size of all the IMDG primary instances. When having multiple partitions for the IMDG, the redo log value exposed is a sum of all the IMDG primary instances current replication redo log size.

This utility collecting stats about:

- Write, Update, Take operations count executed by the Mirror.
- Write, Update, Take operations current throughput executed by the Mirror.
- Redo log size accumulated at the primary spaces.

The Mirror Monitor includes the following:

- `IMirrorStats` Interface - required for the JMX MBean.
- `MirrorStats` - JMX MBean exposing the statistics collected.
- `Mirror` - extends the `DefaultHibernateExternalDataSource` , and calculates the current Mirror Write/Update/Remove speed.
- `SpaceModeListener` - Identify a failure of the primary space and switch monitoring the new primary.

# How to use the Mirror Monitor?
In order to use the Mirror Monitor with your application you should:

1. Download the [Mirror Monitor project](/attachment_files/sbp/MirrorMonitor.zip).
2. Add the Mirror Monitor classes into your Mirror PU.
3. Change the Mirror pu.xml configuration to have this:


```java
<bean id="hibernateDataSource" class="com.gigaspaces.util.mirror.monitor.Mirror">
	        <property name="sessionFactory" ref="sessionFactory"/>
		<property name="spaceName" value="mySpace"  />
		<property name="ide" value="false"/>
		<property name="debug" value="false"/>
		<property name="displayStats" value="false"/>
 	</bean>

<os-core:space id="mirror" url="/./mirror-service" schema="mirror" external-data-source="hibernateDataSource" />
```

Properties List:


|Property|Description|Default Value|
|--------|------------|------------|
|spaceName|Space Name to monitor|mySpace|
|sessionFactory|Hibernate Session|sessionFactory|
|ide|Should be true of Mirror running within the IDE. Should be used in Development mode.|false|
|debug|Will bypass the Database calls. Should be true only when debugging. |false|
|displayStats|When true will display the Mirror stats to output. Should be used in Development mode.|false|

{{% info %}}
The example assumes that space is deployed before the mirror,if you require otherwise,then you should move the code from Mirror.init method to first call of collectStats() (when space is available.)
{{% /info %}}

# Viewing the Mirror Statistics
To view the Statistics gathered by the Mirror monitor:

1. Start the JConsole via the Gigaspaces Mangament Console for the Mirror Service: <br/>
![mirror_monitor2.jpg](/attachment_files/sbp/mirror_monitor2.jpg)

2. Move into the MBeans Tab and select the com.gigaspaces.util.mirror.monitor: <br/>
![mirror_monitor3.jpg](/attachment_files/sbp/mirror_monitor3.jpg)

3. Select the Mirror Stats->Attributes: <br/>
![mirror_monitor.jpg](/attachment_files/sbp/mirror_monitor.jpg)

Double clicking any of the values, will display a graph that will be refreshed automatically. Without having the graph displayed, you will have to click the Refresh button manually to see the most updated values for each statistic.
