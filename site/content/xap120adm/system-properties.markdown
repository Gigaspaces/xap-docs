---
type: post120
title:  System Properties
categories: XAP120ADM
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

| Property name | Description | Default   |
|-----|------|------|
| com.gs.home | XAP home directory. Not required, if not set explicitly, it is resolved | GSHOME |
| com.gs.deploy | The location of the deploy directory of the GSM. | GSHOME/deploy |
| com.gs.work | The location of the work directory of the GSM and GSC. | GSHOME/work |
|com.gs.pu-common|The location of common classes used across multiple processing units. The libraries located within this folder loaded into each PU instance classloader (and not into the system classloader as with the `com.gigaspaces.lib.platform.ext`.|GSHOME\lib\optional\pu-common  |
|com.gigaspaces.lib.platform.ext|PUs shared classloader libraries folder. PU jars located within this folder loaded once into the JVM system classloader and shared between all the PU instances classloaders within the GSC. In most cases this is a better option than the com.gs.pu-common for JDBC drivers and other 3rd party libraries. This is useful option when you want multiple processing units to share the same 3rd party jar files and do not want to repackage the processing unit jar whenever one of these 3rd party jars changes.|GSHOME\lib\platform\ext|



# Security

{{% include "/COM/xap101/config-security.markdown" %}}

| Property name | Description | Default   |
|-----|------|---------|
|com.gs.security.enabled  | Security property indicating whether the GSM and GSC should be loaded in a secured mode.  | false   |
|com.gs.security.properties-file |Identifies the security properties file location  | \<path/filename\>|
|com.gs.security.disable-commit-abort-authentication| Disable transaction authentication| false |


# PU

{{% include "/COM/xap101/config-pu.markdown" %}}

| Property name | Description | Default   |
|-----|-----|------|
| com.gs.pu.classloader.scala-lib-path |   |   scala/lib|
| com.gs.pu.classloader.os-in-common-classloader |   | false  |


# LRMI

## Transport

{{% include "/COM/xap100/config-lrmi-transport.markdown" %}}

| Property name | Description | Default   | Server Client  | Unit | Can be <br>over<br>ridden <br>at the client side|
|----------------|------------|--------------|----------------------|-----|------------------------------------|
| com.gs.transport_protocol<br>.lrmi.max-conn-pool | The maximum amount of connections to the space server remote services that can work simultaneously in a client connection pool. Starts with 1 connection. Defined per each remote service (by default, each remote service has 1024 maximum connections). | 1024 | Server | Conne<br>ction| No|
| com.gs.transport_protocol<br>.lrmi.max-threads     | XAP maintains a thread pool on the server side (at the GSC process level), that manages incoming remote requests. This parameter specifies the maximum size of this thread pool.| 128 | Server | Threads | No |
| com.gs.transport_protocol<br>.lrmi.min-threads     | XAP maintains a thread pool on the server side that manages incoming remote requests. The thread pool size is increased each time with one additional thread and shrinks when existing threads are not used for 5 minutes. This parameter specifies the minimum size of this thread pool. | 1 | Server | Threads | No|
| com.gs.transport_protocol.<br>lrmi.custom.threadpool.max-threads| XAP maintains a thread pool on the server side that manages incoming remote requests for Task and Notification invocation. The thread pool size is increased each time with one additional thread and shrinks when existing threads are not used for 5 minutes. This parameter specifies the maximum size of this thread pool. | 128 | Server | Threads | No|
| com.gs.transport_protocol.<br>lrmi.custom.threadpool.min-threads| XAP maintains a thread pool on the server side that manages incoming remote requests for Task and Notification invocation. The thread pool size is increased each time with one additional thread and shrinks when existing threads are not used for 5 minutes. This parameter specifies the minimum size of this thread pool. | 1 | Server | Threads | No|
| com.gs.transport_protocol.<br>lrmi.system-priority.<br>threadpool.max-threads| XAP maintains a thread pool on the server side that manages incoming remote requests for admin invocation. The thread pool size is increased each time with one additional thread and shrinks when existing threads are not used for 5 minutes. This parameter specifies the maximum size of this thread pool. | 8 | Server |
| com.gs.transport_protocol.<br>lrmi.system-priority.<br>threadpool.min-threads| XAP maintains a thread pool on the server side that manages incoming remote requests for admin invocation. The thread pool size is increased each time with one additional thread and shrinks when existing threads are not used for 5 minutes. This parameter specifies the maximum size of this thread pool. | 1 | Server |
| com.gs.transport_protocol<br>.lrmi.bind-port | Server port used for incoming client requests, or notifications from server to client. The server port is set by default to 0, which means next free port. This means that whenever XAP is launched, it allocates one of the available ports. Define a specific port value to enforce a specific port on the space server or client side. You can define a range of ports  | 0 | Server| | No|
| java.rmi.server.hostname                  | Binds the XAP Server on a specified network interface. If java.rmi.server.hostname is null the system sets the localhost IP address. | host<br>name | Client & Server | | No|
| com.gs.transport_protocol<br>.lrmi.idle_conn<br>ection_timeout | Watchdog idle connection timeout. | 900 sec| Client| Seconds |Yes|
| com.gs.transport_protocol<br>.lrmi.request_timeout | Watchdog request timeout. | 30 sec| Client | Seconds |Yes|
| com.gs.transport_protocol<br>.lrmi.inspect_timeout| Watchdog dummy packet connection timeout used when the watchdog suspects a request connection is blocked (com.gs.transport_protocol.lrmi.request_timeout elapsed). | 1000 millisec| Client | millisec|Yes|
| com.gs.transport_protocol<br>.lrmi.threadpool.idle_timeout | LRMI thread pool idle timeout. Usually should be tuned for server side| 300000 milisec| Server | millisec| No|
| com.gs.transport_protocol<br>.lrmi.connect_timeout | LRMI timeout to establish a socket connection | 30 sec| Server | Seconds| No|
| com.gs.transport_protocol<br>.lrmi.maxBufferSize | The NIO internal cache (a DirectByteBuffer) might cause an OutOfMemoryError due-to direct memory exhaustion. To avoid such a scenario, the LRMI layer breaks the outgoing buffer into a several chunks. By doing so, the NIO internal cache is kept small, and may not cause any error. The size of these chunks can be determined by this property| 65536 (64k)| Client & Server | Bytes | Yes |
| com.gs.transport_protocol<br>.lrmi.selector.threads | LRMI selector threads. This should be configured with multi core machines. Usualy should be tuned for server side| 4 | Client & Server | Threads| No|
| com.gs.transport_protocol<br>.lrmi.use_async_connect | Use asynchronous IO to connect. The default of true should work for most systems. | true | Client & Server | boolean  | No|
| com.gs.transport_protocol<br>.lrmi.use_async_connect | Use asynchronous IO to connect. The default of true should work for most systems. | true | Client & Server | boolean  | No|
| com.gs.transport_protocol<br>.lrmi.classloading | Enables LRMI dynamic class loading.| true | Server | boolean  | No|
| com.gs.transport_protocol<br>.lrmi.classloading.import | Enables importing of classes using LRMI dynamic class loading.| true | Server | boolean  | No|
| com.gs.transport_protocol<br>.lrmi.classloading.export | Enables exporting of classes using lrmi dynamic class loading.| true | Server | boolean  | No|
| com.gs.transport_protocol<br>.lrmi.tcp-send-buffer-size | Set the TCP Send Buffer size (SO_SNDBUF).| OS default | Client & Server| bytes |Yes|
| com.gs.transport_protocol<br>.lrmi.tcp-receive-buffer-size | Set the TCP receive Buffer size (SO_RCVBUF).| OS default | Client & Server| bytes |Yes|
| com.gs.transport_protocol<br>.lrmi.tcp-keep-alive | Set the TCP keep alive mode (SO_KEEPALIVE).| true | Client & Server| Seconds|Yes|
| com.gs.transport_protocol<br>.lrmi.timeout_resolution | Resolution in percents. Timeout resolution indicates the accuracy of the request timeout. | 10 | Client | Percent|Yes|
| com.gs.transport_protocol<br>.lrmi.system-priority.<br>threadpool.min-threads|This parameter specifies the minimum size of a thread pool used to control admin API calls| 1 |  Server| Threads|No|
| com.gs.transport_protocol<br>.lrmi.system-priority.threadpool.max-threads | This parameter specifies the maximum size of a thread pool used to control admin API calls | 8 | Server | Threads|No|
| com.gs.transport_protocol<br>.lrmi.custom.threadpool.<br>idle_timeout | Idle timeout | 300000 millisec     |   | Invo<br>cation | No |
| com.gs.transport_protocol<br>.lrmi.threadpool.<br>queue-size | Regualr operations thread pool queue size |  | {{% fontsize 10 %}}2147483647 {{%/fontsize%}}  |   | Invo<br>cation | No | 
| com.gs.transport_protocol<br>.lrmi.custom.threadpool.<br>queue-size | Custom operations thread pool queue size  |  | {{% fontsize 10 %}}2147483647{{% /fontsize%}}    |   | Invo<br>cation | No |
| com.gs.transport_protocol<br>.lrmi.system-priority.<br>threadpool.queue-size | System operations thread pool queue size |  |  | {{% fontsize 10 %}}2147483647 {{%/fontsize %}}     |   | Invo<br>cation | No |
 

{{%refer%}}
Refer to [Tuning the communication protocol](./tuning-communication-protocol.html)
{{%/refer%}}


## Filter

{{% include "/COM/xap101/config-lrmi-filter.markdown" %}}

| Property name | Description | Default   |
|-----|-----|------|
| com.gs.lrmi.filter.factory | Factory class that is used to load communication filter.| empty |
| com.gs.lrmi.filter.security.keystore | Name of the keystore file that should be used by SSL communication filter. | empty |
| com.gs.lrmi.filter.security.password | Keystore password that should be used by SSL communication filter. | empty |


# Threads Usage


| Thread Name | Description | Configuration Parameters | Client{{<wbr>}}Server|
|:------------|:------------|:-------------------------|:-------------|
|LRMI Selector Accept |Responsible for accepting incoming socket connections. |Single Thread|Server|
|LRMI async Selector  |Client side, handles async invocation, i.e executors, asyncRead/take. |4 Threads|Server|
|Pending Answers-pool |Responsible for sending a callback to template based waiting  operations (read/take). |space-config.engine.min_threads<br>space-config.engine.max_threads|Server|
|Notifier-pool        |Responsible to dispatch notification to client side. Used with the `Notify Container`{{%currentjavanet "notify-container.html"%}} and [Session Based Messaging API]({{%currentjavaurl%}}/session-based-messaging-api.html). See the [Scaling Notification Delivery]({{%currentjavaurl%}}/notify-container.html#Scaling Notification Delivery) for details.| space-config.engine.notify_min_threads<br>space-config.engine.notify_max_threads| Server|
|Processor-pool       |Pool for space operations post processing that can be done asynchronously to user operation. Transaction cleanup, notifications etc.  |space-config.engine.min_threads<br>space-config.engine.max_threads |Server|
|Connection-pool      |Pool for regualr space operations execution.  |com.gs.transport_protocol.lrmi.max-threads <br> com.gs.transport_protocol.lrmi.min-threads |Server|
|Custom pool          |Pool for notify and task execution  |com.gs.transport_protocol.lrmi.custom.<br>threadpool.min-threads<br>com.gs.transport_protocol.lrmi.custom.<br>threadpool.max-threads |Server|
|System pool          |Pool for admin operations           |com.gs.transport_protocol.lrmi.system-priority.threadpool.min-threads<br>com.gs.transport_protocol.lrmi.system-priority.threadpool.max-threads |Server|
|SG LeaseReaper       | Used by the Service Grid | Single Thread |Server|
|Template Expiration Manager Timer | The main thread of expiration manager. Wakes up on each expiration period to find the expired templates. | Single Thread per space | Server|
|TemplateExpirationManager-pool#|Responsible for sending response to waiting client when their template expires.| 16 threads max per space |Server|
|SyncReplicationChannel SPACE_NAME|Runs per sync replication channel |Dynamically adjusted|Server|
|CapabilityChannel  |  |  |Server|
|ClassLoaderCache-{{<wbr>}}SelfCleaningTable |Used to clean up resources of class loaders once terminated (undeploy of processing unit) |Single Thread|Server|
|TransactionTableHolder-SelfCleaningTable | Responsible for cleaning zombie local transactions that were abandoned by the user application without committing. Single thread per GSC |Single Thread per space |Server|
|SLA Monitor Disk|Used by the Service Grid. SLA free disk space monitor|Single Thread|Server|
|Memory:writer|Used by the Service Grid. SLA Memory monitor|Single Thread|Server| 
|CPU:writer|Used by the Service Grid. SLA CPU monitor |Single Thread|Server|
|pollingEventContainer#|Used with the [Polling Container]({{%currentjavaurl%}}/polling-container.html) | See the [Concurrent Consumers]({{%currentjavaurl%}}/polling-container.html#Concurrent Consumers)|Client|
|LeaseRenewalManager Task| Respisible to review resource lease.|One per resource|Client|
|JoinManager Task| Responsible to communicate with the lookup service|Single thread per client proxy| Client|
|Liveness-detector| See the [Proxy Connectivity](./tuning-proxy-connectivity.html) for details.|Single thread per client proxy|Client|
|Liveness-monitor| See the [Proxy Connectivity](./tuning-proxy-connectivity.html) for details.| Single thread per client proxy|Client|
|LocalTransactionManagerImpl{{<wbr>}}$Reaper SPACE_NAME | A thread that reaps expired transactions entries and other objects| Single thread per space | Server|
|GSPingManager| Used by the Service Grid| |Server|
|LeaseManager$Reaper SPACE_NAME |See the [Lease Manager]({{%currentjavaurl%}}/leases-automatic-expiration.html#Lease Manager) for details |Single Thread per space| Server|
|Cache PersistentGC|Responsible for backup activities (cleanup indexes)|Single Thread per space|Server|
|SPACE_NAME BackgroundFifoThread3_Notify=false#|Threads that use segmentation in order to handle background events like handling waiting templates in a fifo fashion | Thread pool per space|Server|
|SPACE_NAME BackgroundFifoThread3_Notify=true#|Threads that use segmentation in order to handle background notify events  in a fifo fashion|Thread pool per space|Server|
|Statistics-Task| | | Server|
|SPACE_NAME Batch Notifier|Used when using batch notifications. See the [Batch Events]({{%currentjavaurl%}}/notify-container.html#Batch Events) for details.|Single Thread per space|Server|
|ActiveFailureDetector| Responsible for the active election process. See the [Failure Detection](./troubleshooting-failure-detection.html) for details.|Single Thread|Server|
|ProvisionLeaseManager|Used by the Service Grid|Single Thread|Server|
|Watchdog|See the [Communication Protocol](./tuning-communication-protocol.html) for details. |Single Thread per proxy|Client|
|Scheduled System Boot Thread|Used when the system starts|Single Thread| Server|
|SLAThresholdTaskPool#|Used by the Service Grid|Single Thread| Server|
|CapabilityChannel|Used by the Service Grid|Single Thread| Server|
|DynamicSmartStub-InvHandlerCache-SelfCleaningTable$Cleaner|Used by LRMI for background cleanup|Single Thread|Server|
|ClassLoaderCache-SelfCleaningTable$Cleaner|Used by LRMI to cleanup unused classes|Single Thread|Server|
|ProvisionFailurePool#|Used by the Service Grid. Monitors the running services| |Server - GSM|
|ProvisionPool#|Used by the Service Grid. Monitors the running services| |Server - GSM|
|ProvisionMonitorEventPool#|Used by the Service Grid. Monitors the running services| |Server - GSM|
|Reggie Comm Task-pool#|Used by the Service Grid. Monitors the running services| |Server - GSM|
|SDM Cache Task|Used by the Service Grid.| |Server - GSM|
|unicast request|Used by the Service Grid.| |Server - GSM|
|service expire|Used by the Service Grid.| |Server - GSM|
|Webster Runner|Used by the Service Grid.| |Server - GSM|
|Event Lease Manager|Used by the Service Grid.| |Server - GSM|
|Fault Detection Handler|Used by the Service Grid.| |Server - GSM|

{{%refer%}}
Refer to [Threads Usage](./tuning-threads-usage.html)
{{%/refer%}}


# JMS

{{% include "/COM/xap101/config-jms.markdown" %}}

| Property name | Description | Default   |
|-----|-----|----|
|com.gs.jms.enabled|If true it will register the jms administrated objects in the rmi registry| false|
|com.gs.jms.compressionMinSize | JMS - The minimum size (in bytes) which from where we start to compress the message body. e.g. if a 1 MB Text **JMSMessage** body is sent, and the **compressionMinSize** value is 500000 (0.5MB) then we will compress that message body (only), otherwise we will send (write) it as is. | 500000 |
|com.gs.jms.iterator.buffersize|The iterator buffer size used for the QueueBrowser.|1000 objects|
|com.gs.jms.use_mahalo| If true, when JMS clients use transacted sessions the JMS transactions will use the Mahalo Jini transaction manager, which expects the manager to be started.|false|



{{%refer%}}
Refer to [Messaging support]({{%currentjavaurl%}}/messaging-support.html)
{{%/refer%}}



# JMX

{{% include "/COM/xap101/config-jmx.markdown" %}}

| Property name | Description | Default   |
|-----|------|------|
|com.gs.jmx.enabled | Enable / disable JMX support. | true |
|com.gs.jmx.createJmxConnetor|Enable / disable the JMXConnector to connect remotely to the JMXServer with the supported protocol  (such as RMI, HTTP and so). |  false|
|com.j_spaces.core.container.directory{{%wbr%}}_services.jndi.url |The network url and port for the JMXConnector| service:jmx:rmi:///jndi/{{%wbr%}}rmi://127.0.1.1:10098/jmxrmi|

{{%refer%}}
Refer to [JMX Management](./space-jmx-management.html)
{{%/refer%}}

# Dump

| Property name | Description | Default   |
|-----|------|------|
| com.gs.memory.create-heap-dump-on-memory-shortage   | Turn on and off the heap dump | false |
| com.gs.memory.max-heaps-on-memory-shortage          | Turn off heap dump after n times | 1     |
| com.gs.memory.heaps-on-memory-shortage-quiet-period | How much time to wait between heap dumps |24h |

{{%refer%}}
These values can be modified using the JConsole using the `HeapDumpMBean` with ObjectName `org.xap:type=HeapDumpMBean`
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
| com.gs.logging.disabled | If **true**, the default **xap_logging.properties** file will not be loaded and none of the GS log handlers will be set to the **LogManager**. | **false** |
| com.gs.logging.debug | To troubleshoot and detect which logging properties file was loaded and from which location, set the following system property to **true**. This property already exists in the scripts (for convenience) and by default is set to false.|**false**|
| line.separator | The GS logging formatter Line separator string.&nbsp; This is the value of the **line.separator** property at the moment that the **SimpleFormatter** was created. | |
|java.util.logging.config.file | It indicates file path to the Java logging file location. Use it to enable finest logging troubleshooting of various GigaSpaces Services. You may control this setting via the GS_LOGGING_CONFIG_FILE_PROP environment variable.| GSHOME\config\log\xap_logging.properties|
| Logging Categories | Refer to [Logging Categories](./logging.html#logging-categories) | |



{{%refer%}}
Refer to [Logging](./logging-overview.html)
{{%/refer%}}

# Debug

{{% include "/COM/xap101/config-debug.markdown" %}}

| Property name | Description | Default   |
|-----|------|-----|
| com.gs.debug | Used in examples - benchmark , query, p2p , p2p fifo , p2p JMS, **SimpleQueueReceiver**,**SimpleQueueSender**, **SimpleTopicPublisher**, **SimpleTopicSubscriber**. | False |




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

| Property name | Description | Default   | Unit|
|-----|-------|------|-----|
|com.gs.transport_protocol.lrmi.slow-consumer.enabled| Specify whether slow consumer protection is enabled | false | |
|<nobr>com.gs.transport_protocol.lrmi.slow-consumer.throughput</nobr>| Specify what is the lower bound of notification network traffic consumption (in bytes) by a client which below it, is suspected as a slow consumer. | 5000 | bytes/second  |
|com.gs.transport_protocol.lrmi.slow-consumer.latency| Specify a time period the space will evaluate a client suspected as slow consumer until it will be identified as a slow consumer. At the end of this time period, a client identified as a slow consumer will have its notification lease canceled.| 500 | milliseconds|
|com.gs.transport_protocol.lrmi.slow-consumer.retries| Specify the number of times within the specified latency limitation a space will retry to send notification into a client suspected as a slow consumer. | 3 | retries|


### Client side

{{% include "/COM/xap101/config-slow-consumer-client.markdown" %}}

| Property name | Description | Default| Unit|
|-----|-----|-----|------|
|<nobr>com.gs.transport_protocol.lrmi.threadpool.queue-size<nobr>| specify the lrmi thread pool maximum queue size|Integer.MAX_VALUE |Notification Packets (object/batch)|
|com.gs.fifo_notify.queue| specify the fifo notifications queue size|Integer.MAX_VALUE|Notification Packets (object/batch)|


{{%refer%}}
Refer to [Slow consumer](./slow-consumer.html)
{{%/refer%}}


# Cluster

{{% include "/COM/xap101/config-cluster.markdown" %}}

| Property name | Description | Default   |
|-------|------|------|
|  com.gs.cluster.cluster_enabled  | Used by the space schema. | **false** |
|  com.gs.cluster.config-url  | Used by the space schema. | |
|  com.gs.cluster.cache-loader.shared-data-source  | Used by the cluster schemas for the **CacheLoader**. | |
|  com.gs.cluster.livenessMonitorFrequency  | Defines the frequency in which liveness of 'live' members in a cluster is monitored. See [Viewing Clustered Space Status](./troubleshooting-viewing-clustered-space-status.html) for more details. |  10000 ms  |
|  com.gs.cluster.livenessDetectorFrequency  | Defines the frequency in which liveness of members in a cluster is detected. See [Viewing Clustered Space Status](./troubleshooting-viewing-clustered-space-status.html) |  5000 ms |
| <nobr> com.gs.cluster.cache-loader.external-data-source</nobr>  | Boolean property. Must be set to **true** when working with external data source | **false** |
|  com.gs.cluster.cache-loader.central-data-source  | Boolean property. Must be set to **true** when the cluster uses external data source and{{% wbr %}}a central database to keep its data | **false** |
|  com.gs.cluster.url-protocol-prefix  | Used in clustered configuration to provide same prefix for all cluster members URL 0 i.e. **rmi:RMIRegistryMachineHostName**. | Not set by default |
|  com.gs.clusterXML.debug  | Boolean value. If **true**, display cluster configuration when space started. | **False** |



{{%refer%}}
Refer to [Data Grid Clustering](./data-grid-clustering.html)
{{%/refer%}}


# Replication

{{% include "/COM/xap101/config-replication.markdown" %}}

| Property name | Description | Default   |
|-------|------|-------|
| <nobr>com.gs.replication.disable-duplicate-filtering</nobr>  | Disables the duplication filtering mechanism used to avoid double processing of packets after recovery. | **false** |


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

| Property name | Description | Default   |
|-----|-----|----|
|  com.gs.xmlschema.validation  | Boolean value. If **false**, does not validate cluster XML config schema. | true |




# Transaction

{{% include "/COM/xap101/config-transaction.markdown" %}}

| Property name | Description | Default   |
|-----|-----|----|
|  com.gs.xa.failOnInvalidRollback  | When set to **false**, the **XAResource** does not throw an error when attempting to roll back a non-existing transaction or a transaction the has already been rolled back. For more details, see [Javadoc](http://docs.oracle.com/javase/1.5.0/docs/api/javax/transaction/xa/XAResource.html) | true |

# JDBC

| Property name | Description | Default   |
|---|------|-----|
|  com.gs.embeddedQP.enabled  | Boolean value. Used at JDBC driver. If **true**, running query processor embedded within the application. | **False** |
|  com.gs.embeddedQP.properties  | Used at JDBC driver. Properties file location. | |


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

