---
type: post110adm
title:  System Properties
categories: XAP110ADM
parent: runtime-configuration.html
weight: 400
---

{{%ssummary%}}{{%/ssummary%}}

# Administration

{{% include "/COM/xap101/config-admin.markdown" %}}

| Property name | Description | Default   |
|-----|----|------|
|gs.admin.jvm.probe.details |  Implementation of `JVMDetailsProbe`,probing the jvm, `JVMDetails` is used to return the details of the jvm such as jvm name, version vendor, start time, heap data etc.|   SigarJVMDetailsProbe|
|gs.admin.jvm.probe.statistics|Implementation of `JVMStatisticsProbe`, provides JVM statistics | SigarJVMStatisticsProbe|
|gs.admin.os.probe.statistics |Implementation of `OSStatistics`, provides OS statistics such as memory and network usage.| SigarOSStatisticsProbe|
|gs.admin.os.probe.details|    Implementation of `OSDetailsProbe`, provides OS details `OSDetails` like uid, name, architecture, version, processors number, host name and address etc.| SigarOSDetailsProbe|




# Deployment

{{% include "/COM/xap101/config-deploy.markdown" %}}


# Security

{{% include "/COM/xap101/config-security.markdown" %}}

# PU

{{% include "/COM/xap101/config-pu.markdown" %}}

# LRMI

#### Transport

{{% include "/COM/xap101/config-lrmi-transport.markdown" %}}

{{%refer%}}
Refer to [Tuning the communication protocol](./tuning-communication-protocol.html)
{{%/refer%}}


#### Filter

{{% include "/COM/xap101/config-lrmi-filter.markdown" %}}



#JMS

{{% include "/COM/xap101/config-jms.markdown" %}}

{{%refer%}}
Refer to [Messaging support]({{%currentjavaurl%}}/messaging-support.html)
{{%/refer%}}



# JMX

{{% include "/COM/xap101/config-jmx.markdown" %}}

{{%refer%}}
Refer to [JMX Management](./space-jmx-management.html)
{{%/refer%}}

# Multicast

{{% include "/COM/xap101/config-multicast.markdown" %}}

| Property name | Description | Default |
|-----|------|------|
|com.gs.multicast.enabled|Global property allowing you to completely enable or disable multicast in the system.| true|
|com.gs.multicast.announcement|the multicast address that controls the lookup service announcement. The lookup service uses this address to periodically announce its existence. |224.0.1.188|
|com.gs.multicast.request|the multicast address that controls the request of clients (when started) to available lookup services. | 224.0.1.187|
|com.gs.multicast.discoveryPort|the port used during discovery for multicast requests. Defaults to **4174**. Note that in case the property **com.sun.jini.reggie.initialUnicastDiscoveryPort** system property is not defined it is also used as the default post for unicast requests.|4174|
|com.gs.multicast.ttl|The multicast packet time to live. | 3|


{{%refer%}}
Refer to [Multicast Settings](./network-lookup-service-configuration.html#multicast-settings)
{{%/refer%}}

# Web

{{% include "/COM/xap101/config-web.markdown" %}}


| Property name | Description | Default   |
|-----|-----|----|
| com.gigaspaces.start.httpPort | Webster http port definition | default 0 - free port |
| com.gigaspaces.start.httpServerRetries | Webster http port retries - if the initial HTTP port is in use, tries ports between **httpPort ..** and **httpPort+(N-1)** | default is **10**, for example: **initial port=1900** tries **1900**, **1901**, **... 1909** |
| com.gigaspaces.start.hostAddress | Webster host address. | default is **localhost** |
| com.gigaspaces.start.httpRoots | Webster root library locations. | Default includes XAP libraries, Jini libraries, etc. |
| com.gigaspaces.start.addHttpRoots | Additional Webster root library locations (appended to httpRoots). | gslib;gslibrequuired;deployroot |
| com.gs.browser.httpd.enabled | Boolean value. Setting this property to **true** indicates to start a Webster HTTPD server inside the Space Browser. | true|
| com.gs.embedded-services.httpd.port | Indicates to start Webster HTTPD in the specified port. By default, it uses an **9813** port or generated one if it is used. | 9813 |



# Space Filter

{{% include "/COM/xap101/config-space-filter.markdown" %}}

| Property name | Description | Default   |
|-----|-----|----|
| com.gs.filters.statistics.enabled | Enabling / disable Space filter statistics. | true |


{{%refer%}}
Refer to [Space Filters]({{%currentjavaurl%}}/the-space-filters.html)
{{%/refer%}}




# Logging

{{% include "/COM/xap101/config-logging.markdown" %}}

| Property name | Description | Default   |
|-----|-----|------|
| com.gs.logging.disabled | If **true**, the default **gs_logging.properties** file will not be loaded and none of the GS log handlers will be set to the **LogManager**. | **false** |
| com.gs.logging.debug | To troubleshoot and detect which logging properties file was loaded and from which location, set the following system property to **true**. This property already exists in the scripts (for convenience) and by default is set to false.|**false**|
| line.separator | The GS logging formatter Line separator string.&nbsp; This is the value of the **line.separator** property at the moment that the **SimpleFormatter** was created. | |
|java.util.logging.config.file | It indicates file path to the Java logging file location. Use it to enable finest logging troubleshooting of various GigaSpaces Services. You may control this setting via the GS_LOGGING_CONFIG_FILE_PROP environment variable.| GSHOME\config\gs_logging.properties|
| Logging Categories | Refer to [Logging Categories](./logging.html#logging-categories) | |



{{%refer%}}
Refer to [Logging](./logging-overview.html)
{{%/refer%}}

# Debug

{{% include "/COM/xap101/config-debug.markdown" %}}



# Fault Detection

{{% include "/COM/xap101/config-fault-detection.markdown" %}}


# Space Proxy Router

{{% include "/COM/xap101/config-space-proxy-router.markdown" %}}

| Property name | Description | Default   |
|-----|-----|-------|
| space-config.proxy.router.active-server-lookup-timeout |  If an operation cannot be executed because the target member is not available, the maximum time (in milliseconds) the router is allowed to wait while searching for an active member. | 20000 |
| <nobr>space-config.proxy.router.active-server-lookup-sampling-interval</nobr> | The interval (in milliseconds) between active member lookup samples. | 100 |
| space-config.proxy.router.threadpool-size | Number of threads in the dedicated thread pool used by the space proxy router | 2 * number of cores |
| space-config.proxy.router.load-balancer-type | Load balancer type to be used by the router for active-active topologies (STICKY or ROUND_ROBIN) | STICKY |


# Slow Consumer

### Server side

{{% include "/COM/xap101/config-slow-consumer-server.markdown" %}}

### Client side

{{% include "/COM/xap101/config-slow-consumer-client.markdown" %}}

{{%refer%}}
Refer to [Slow consumer](./slow-consumer.html)
{{%/refer%}}


# Cluster

{{% include "/COM/xap101/config-cluster.markdown" %}}

| Property name | Description | Default   |
|-----|-----|------|
|  com.gs.xmlschema.validation  | Boolean value. If **false**, does not validate cluster XML config schema. | true |



{{%refer%}}
Refer to [Data Grid Clustering](./data-grid-clustering.html)
{{%/refer%}}


# Replication

{{% include "/COM/xap101/config-replication.markdown" %}}

{{%refer%}}
Refer to [Replication](./replication.html)
{{%/refer%}}


# Space Browser

{{% include "/COM/xap101/config-space-browser.markdown" %}}

| Property name | Description | Default   |
|-----|------|------|
|  com.gs.browser.containername  | Used in browser UI to set the default SpaceURL address field. **com.gs.browser.containername=** **mySpace_container** controls the default container name in the Space Browser Space URL field, when the browser is started. | |
|  com.gs.browser.laf.isCross  | SpaceBrowser UI Look and Feel. | |
|  com.gs.browser.unicast_discovery  | Sets the Space Browser unicast discovery using **hostname:port** URL. **com.gs.browser.unicast_discovery=\<ip-address\>** can be used if multicast is disabled on the local machine. In such a case, the unicast protocol is used for the lookup discovery (unicast discovery is disabled by default). | By default it is not set |



# XML

{{% include "/COM/xap101/config-xml.markdown" %}}


# Transaction

{{% include "/COM/xap101/config-transaction.markdown" %}}

| Property name | Description | Default   |
|-----|-----|----|
|  com.gs.xa.failOnInvalidRollback  | When set to **false**, the **XAResource** does not throw an error when attempting to roll back a non-existing transaction or a transaction the has already been rolled back. For more details, see [Javadoc](http://docs.oracle.com/javase/1.5.0/docs/api/javax/transaction/xa/XAResource.html) | true |


# Misc


| Property name | Description | Default value |
|-----|-----|------|
|  com.gs.jndi.url  | Used by the container schema. | localhost:10098 |
|  com.gs.protocol  | Used by the space schema. | NIO |
|  com.gs.engine.cache_policy  | Used by the space schema. | 1 - ALL IN CACHE |
|  com.gs.env.report  | Allows you to view all the runtime configuration settings. | false|
|  com.gs.licensekey  | License key string. | |
|  com.gs.localhost.name  | | |





{{% refer %}}Refer to the [SystemProperties](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/com/j_spaces/kernel/SystemProperties.html) class for more details.{{% /refer %}}

