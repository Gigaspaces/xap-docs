---
type: post97
title:  Java System Properties
categories: XAP97ADM
parent: runtime-configuration.html
weight: 400
---

{{% refer %}}Refer to the [SystemProperties]({{% api-javadoc %}}/com/j_spaces/kernel/SystemProperties.html) class for more details.{{% /refer %}}


| Property name | Description | Default value |
|:--------------|:------------|:--------------|
| `com.gs.home` | GigaSpaces home directory. {{<wbr>}}Not required, if not set explicitly, it is resolved | `JSHOMEDIR` |
| `com.gs.grid.secured` | Security property indicating whether the GSM and GSC should be loaded in a secured mode.  | false (non-secured) |
| `com.gs.deploy` | The location of the deploy directory of the GSM. | `XAPHOME/deploy` |
| `com.gs.work` | The location of the work directory of the GSM and GSC. | `XAPHOME/work` |
| `com.gs.active_election.timeout` | Defines the sleep timeout between iterations in the Active election algorithm | 1000 msec |
| `com.gs.replication.disable-duplicate-filtering` | Disables the duplication filtering mechanism used to avoid double processing of packets after recovery. | false |
| `com.gs.cluster.cache-loader.external-data-source` | Boolean property. Must be set to `true` when working with external data source | `false` |
| `com.gs.cluster.cache-loader.central-data-source` | Boolean property. Must be set to `true` when the cluster uses external data source and{{<wbr>}}a central database to keep its data | `false` |
| `com.gs.lrmi.filter.factory` | Factory class that is used to load communication filter.| empty |
| `com.gs.lrmi.filter.security.keystore` | Name of the keystore file that should be used by SSL communication filter. | empty |
| `com.gs.lrmi.filter.security.password` | Keystore password that should be used by SSL communication filter. | empty |
| `com.gs.browser.containername` | Used in browser UI to set the default SpaceURL address field. `com.gs.browser.containername=` `mySpace_container` controls the default container name in the Space Browser Space URL field, when the browser is started. | |
| `com.gs.browser.httpd.enabled` | Boolean value. Setting this property to `true` indicates to start a Webster HTTPD server inside the Space Browser. | |
| `com.gs.embedded-services.httpd.port` | Indicates to start Webster HTTPD in the specified port. By default, it uses an `9813` port or generated one if it is used. | `9813` |
| `com.gs.browser.laf.isCross` | SpaceBrowser UI Look and Feel. | |
| `com.gs.browser.unicast_discovery` | Sets the Space Browser unicast discovery using `hostname:port` URL. `com.gs.browser.unicast_discovery=` `<ip-address>` can be used if multicast is disabled on the local machine. In such a case, the unicast protocol is used for the lookup discovery (unicast discovery is disabled by default). | By default it is not set |
| `com.gs.cluster.url-protocol-prefix` | Used in clustered configuration to provide same prefix for all cluster members URL 0 i.e. `rmi:RMIRegistryMachineHostName`\. | Not set by default |
| `com.gs.clusterXML.debug` | Boolean value. If `true`, display cluster configuration when space started. | `False` |
| `com.gs.dbcache.debug` | Used in hibernate cache plugin. | |
| `com.gs.debug` | Used in examples - benchmark , query, p2p , p2p fifo , p2p JMS, `SimpleQueueReceiver, SimpleQueueSender, SimpleTopicPublisher, SimpleTopicSubscriber`. | `False` |
| `com.gs.embeddedQP.enabled` | Boolean value. Used at JDBC driver. If `true`, running query processor embedded within the application. | `False` |
| `com.gs.embeddedQP.properties` | Used at JDBC driver. Properties file location. | |
| `com.gs.env.report` | Allows you to view all the runtime configuration settings. | |
| `com.gs.EvictionStrategy` | Local Cache property. | {{% anchor standby %}} |
as the parent directory of the `JSpaces.jar` file. | |
| `com.gs.jms.compressionMinSize` | Compresses a JMS text message's data if its size is larger than a configured value (threshold). The message data is compressed when sent, and decompressed when received. The value assigned should be in bytes. | `500000` (0.5 MB) |
| `com.gs.jmx.enabled` | System property that determines JMX supporting. | `true` |
| `com.gs.licensekey` | License key string. | |
| `com.gs.localhost.name` | | |
| `com.gs.logging.disabled` | If `true`, the default `gs_logging.properties` file will not be loaded and none of the GS log handlers will be set to the `LogManager`. | `false` |
| `com.gs.multicastnotify` | Local Cache property. | |
| `com.gs.onewaynotify`| Boolean value. If `false`, performs notify operations in two way mode (ack on notify).| `true` |
| `com.gs.pushMode` | Local Cache property. | `false` |
| `com.gs.putFirst` | Local Cache property. | `false`&nbsp; |
| `com.gs.resourcepool.timeout` | Sets the resource release timeout in ms. | `5000` |
| `com.gs.security.ssl.password` | User password for SSL. | |
| `com.gs.sizeLimit` | Local Cache property. | |
| `com.gs.jms.compressionMinSize` | JMS - The minimum size (in bytes) which from where we start to compress the message body. e.g. if a 1 MB Text `JMSMessage` body is sent, and the `compressionMinSize` value is 500000 (0.5MB) then we will compress that message body (only), otherwise we will send (write) it as is. | `500000` |
| `com.gs.url` | Cache factory. | |
| `com.gs.xmlschema.validation` | Boolean value. If `false`, does not validate cluster XML config schema. | `true` |
| `com.gs.resourcepool.debug` | Boolean value `com.gs.resourcepool.debug=true` to set debug mode. | |
| `line.separator` | The GS logging formatter Line separator string.&nbsp; This is the value of the `line.separator` property at the moment that the `SimpleFormatter` was created. | |
| `com.gigaspaces.start.jmx` | JMX property. | |
| `com.gigaspaces.start.container` | JMX property. | |
| `com.gigaspaces.start.httpPort` | Webster http port definition | default 0 - free port |
| `com.gigaspaces.start.httpServerRetries` | Webster http port retries - if the initial HTTP port is in use, tries ports between `httpPort ..` and `httpPort+(N-1)` | default is `10`, for example: `initial port=1900` tries `1900`, `1901`, `... 1909` |
| `com.gigaspaces.start.hostAddress` | Webster host address. | default is `localhost` |
| `com.gigaspaces.start.httpRoots` | Webster root library locations. | Default includes GigaSpaces libraries, Jini libraries, etc. |
| `com.gigaspaces.start.addHttpRoots` | Additional Webster root library locations (appended to `httpRoots`). | |
| `com.gs.jndi.url` | Used by the container schema. | `localhost:10098` |
| `com.gs.cluster.cluster_enabled` | Used by the space schema. | `false` |
| `com.gs.cluster.config-url` | Used by the space schema. | |
| `com.gs.protocol` | Used by the space schema. | `NIO` |
| `com.gs.engine.cache_policy` | Used by the space schema. | `1 - ALL IN CACHE` |
| `com.gs.memory_usage_enabled` | Used by the space schema. | `false` |
| `com.gs.filters.statistics.enabled` | Used by the space schema. | |
| `com.gs.cluster.cache-loader.external-data-source` | Used by the cluster schemas for the `CacheLoader`. | |
| `com.gs.cluster.cache-loader.shared-data-source` | Used by the cluster schemas for the `CacheLoader`. | |
| `com.gs.cluster.livenessMonitorFrequency` | Defines the frequency in which liveness of 'live' members in a cluster is monitored. See [Viewing Clustered Space Status](./troubleshooting-viewing-clustered-space-status.html) for more details. | Default 10000 ms&nbsp; |
| `com.gs.cluster.livenessDetectorFrequency` | Defines the frequency in which liveness of members in a cluster is detected. See [Viewing Clustered Space Status](./troubleshooting-viewing-clustered-space-status.html) | Default 5000 ms |
| `com.gs.callGC` | Boolean value.{{<wbr>}}Call garbage collection when performing eviction. This used when running in LRU cache policy and also at client side when using local cache. | `false` |
| `com.gs.xa.failOnInvalidRollback` | Boolean value.{{<wbr>}}When set to `false`, the `XAResource` does not throw an error when attempting to roll back a non-existing transaction or a transaction the has already been rolled back. For more details, see {{<wbr>}}[Javadoc](http://docs.oracle.com/javase/1.5.0/docs/api/javax/transaction/xa/XAResource.html) | `true` {{% anchor maxbuffer %}} |
