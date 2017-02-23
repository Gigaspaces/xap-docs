---
type: post110
title:  Overview
categories: XAP110ADM
parent: logging-overview.html
weight: 100
---


{{% ssummary %}}{{% /ssummary %}}



XAP makes logging calls by use of the Java <sup>TM</sup> platform's core logging facilities.
For more information on the JDK logging framework, please refer to the following online documentation: [Java Logging Overview](http://java.sun.com/j2se/1.5.0/docs/guide/logging/overview.html).

# Configuration File

The logging configuration is initialized using a logging configuration file that is read at startup. This logging configuration file is in the standard `java.util.Properties` format. It configures custom versions of both `java.util.logging.Handler` and `java.util.logging.Formatter`, and default levels for frequently used loggers (categories).

The default configuration file is located under:


```bash
<XAP>/config/gs_logging.properties
```

# Logging Level

The [logging level](http://docs.oracle.com/javase/{{%version "java-version"%}}/docs/api/java/util/logging/Level.html) class defines a set of standard logging levels that can be used to control logging output. The logging level are ordered and are specified by ordered integers/constants.

{{% tip %}}
Enabling logging at a given level also enables logging at all higher levels.
{{% /tip %}}

The supported logging levels in descending order are:

- SEVERE (highest value)
- WARNING
- INFO
- CONFIG
- FINE
- FINER
- FINEST (lowest value)

In addition, there is a level **OFF** that can be used to turn off logging, and a level **ALL** that can be used to enable logging of all messages.

# Logging Categories

The following categories are available:

- Client
- Communication Protocol
- Class Loader
- Space (Caching, Query, Replication, etc)
- Runtime (GCC, GSM, etc)
- Security
- Web Container
- Mule Integration
- Management

For each category there are various logger name you should use when configuring the logging level. See `gs_logging.properties` file for exact logger name supported for each category. You can find all logger names also at the `com.gigaspaces.logger.Constants` class.

Here are the different modules , their logging names and their default logging level:

{{%tabs%}}
{{%tab "Client"%}}

{{%panel%}}
**Client General**


```bash
com.gigaspaces.client.level = INFO
com.gigaspaces.client.cluster.liveness.level = INFO
```

GigaSpaces Client can be another component or application that connects to a GigaSpaces cluster.
Liveness check is a functionality that runs inside a GigaSpaces proxy (usually held by a client connecting to a space) to keep track of the cluster members.
Additional info about GigaSpaces proxy can be found [here]({{%currentadmurl%}}/tuning-proxy-connectivity.html)

**.Net API**

The logging configuration file includes declarations of the loggers available at the bridge between .NET and Java.



```bash
com.gigaspaces.externaldatasource.dotnet.level = INFO
com.gigaspaces.bridge.dispatcher.level = INFO
com.gigaspaces.bridge.pbsexecuter.level = INFO
com.gigaspaces.dotnet.pu.level = INFO
```

**C++ API**

The logging configuration file includes declarations of the C++ Java Proxy logger, which logs info such as exceptions and JVM creation.


```bash
com.gigaspaces.cpp.proxy.level = INFO
```

**OpenSpaces**


```bash
org.openspaces.level = INFO
org.openspaces.pu.container.support.level = WARNING
org.openspaces.pu.container.jee.context.ProcessingUnitWebApplicationContext.level = WARNING
org.springframework.level = WARNING
```

OpenSpaces wraps the   core product with Spring which enables Spring configuration and Spring life cycle to XAP applications.  

**Spring**


```bash
com.gigaspaces.spring.level = INFO
```

GigaSpaces Spring application logging

**JMS API**


```bash
com.gigaspaces.jms.level = INFO
```
{{%/panel%}}

{{% /tab %}}

{{% tab "Comunication Protocol" %}}
{{%panel%}}

```bash
com.gigaspaces.lrmi.nio.filters.SSLFilterFactory.level = INFO
com.gigaspaces.lrmi.level = INFO
com.gigaspaces.lrmi.stubcache.level = INFO
com.gigaspaces.lrmi.context.level = INFO
com.gigaspaces.lrmi.marshal.level = INFO
com.gigaspaces.lrmi.watchdog.level = INFO
com.gigaspaces.lrmi.classloading.level = INFO
com.gigaspaces.lrmi.slow_consumer.level = INFO
com.gigaspaces.lrmi.exporter.level = INFO
com.gigaspaces.lrmi.communication.transport.level = INFO
com.gigaspaces.lrmi.communication.manager.level = INFO
com.gigaspaces.lrmi.channel.transport.level = INFO
com.gigaspaces.lrmi.channel.manager.level = INFO
com.gigaspaces.lrmi.channel.protocol.level = INFO
```

{{%/panel%}}
{{% /tab %}}
{{%tab "  Class Loader "%}}
{{%panel%}}


```bash
com.gigaspaces.core.classloadercleaner.level = INFO
com.gigaspaces.core.classloadercache.level = INFO
```

XAP applications are running as part of a XAP runtime container and packaged using the structure described [here]({{%currentjavaurl%}}/the-processing-unit-structure-and-configuration.html).
Application jars/classes are packaged in different folders and some of the classes could be loaded as part of GigaSpaces container (GSC's). There are multiple class loaders involved when an application is running. More information about the class loaders and their hierarchy is [here]({{%currentjavaurl%}}/the-processing-unit-structure-and-configuration.html).
{{%/panel%}}
{{% /tab %}}
{{%tab "  Space "%}}
{{%panel%}}
**Space Core & Kernel**


```bash
com.gigaspaces.core.engine.level = INFO
com.gigaspaces.core.lease.level = INFO
com.gigaspaces.core.types.level = INFO
com.gigaspaces.memory-manager.level = INFO
com.gigaspaces.kernel.level = CONFIG
com.gigaspaces.core.common.level = INFO
com.gigaspaces.core.config.level = CONFIG
com.gigaspaces.container.level = INFO
```

Core runtime for the space component of GigaSpaces, above loggers relate to this component and some aspects of this engine including, lease, object types and Memory Manager.

**Space Filters**


```bash
com.gigaspaces.filters.level = INFO
```

Space filters are described here {{%currentjavanet "the-space-filters.html" %}}

**Space Persistency**


```bash
com.gigaspaces.persistent.level = INFO
com.gigaspaces.persistent.shared_iterator.level = INFO
org.hibernate.level = WARNING
```

GigaSpaces persistence options are explained here {{%currentjavanet "space-persistency.html"%}}. One of the packaged External Data Source implementations uses Hibernate and it is called Hibernate External Data Source which is described [here]({{%currentjavaurl%}}/hibernate-space-persistency.html).

**Space Query**


```bash
com.gigaspaces.query.level = INFO
```

GigaSpaces supports SQL queries on the data in space and logger corresponds to this functionality {{%currentjavanet "query-sql.html"%}}.

**Space LRU and Eviction**


```bash
com.gigaspaces.cache.level = INFO
```

More information about LRU policy and Eviction behavior is [here](./lru-cache-policy.html)

**Space Notifications**


```bash
com.gigaspaces.core.notify.level = INFO
```

Notifications are a mechanism that can be used to identify events related to space data (write, update, take, etc). Notifications are typically used with a [Notify Container]({{%currentjavaurl%}}/notify-container.html).
Another way notifications can be used is thru Session based messaging which is discussed [here]({{%currentjavaurl%}}/session-based-messaging-api.html).

**Space FIFO**


```bash
com.gigaspaces.core.fifo.level = INFO
```

FIFO functionality is applicable for writes, reads and events (notifications) and discussed here {{%currentjavanet "fifo-support.html" %}}.

**Space Replication**


```bash
com.gigaspaces.core.cluster.replication.level = INFO
com.gigaspaces.core.cluster.replication.redolog.level = INFO
com.gigaspaces.core.cluster.sync_replication.level = INFO
```

When a cluster topology is replicated, replication functionality is enabled. More information about topologies is [here](/product_overview/space-topologies.html).
Replication between spaces is one of the core features of GigaSpaces and is explained in detail [here](./replication.html).

**Space Partitioning**


```bash
com.gigaspaces.core.cluster.partition.level = INFO
```

When cluster uses partitioned topology, data is partitioned across multiple instances of spaces. More information about topologies is [here](/product_overview/space-topologies.html).

**Space Active-Election**


```bash
com.gigaspaces.cluster.active_election.level = INFO
```

When multiple instances (primary/backup(s)), Active Election process is used by cluster members to determine which member acts as a primary. Additional information regarding active election process is [here](./split-brain-and-primary-resolution.html).

**POJO**


```bash
com.gigaspaces.pojo.level = INFO
```

Logger corresponding to GigaSpaces POJO support, more info [here]({{%currentjavaurl%}}/pojo-support.html).

**Space XA manager**


```bash
com.gigaspaces.core.xa.level = INFO
```

Logger corresponding to XA Transaction manager running in the space, more information here {{%currentjavanet "transaction-management.html" %}}.

**Space Jini Dist. TX manager**


```bash
com.sun.jini.mahalo.startup.level = INFO
com.sun.jini.mahalo.destroy.level = INFO
```

Logger for Jini Distributed Transaction manager, more information here {{%currentjavanet "transaction-management.html" %}}.

**SpaceURL, SpaceValidator, SpaceURLParser**


```bash
com.gigaspaces.common.spaceurl.level = INFO
com.gigaspaces.common.spacefinder.level = INFO
com.gigaspaces.common.lookupfinder.level = INFO
com.gigaspaces.common.resourceloader.level = INFO
```

SpaceURL and its constraints are explained here {{%currentjavanet "the-space-configuration.html" %}}.
Other loggers are related to this and applicable when a client trying to create a space proxy using a URL.

**Space Multicast Notifications**


```bash
com.gigaspaces.worker.multicast.level = INFO
```

Space notifications support multicast mode and this logger corresponds to this [functionality]({{%currentjavaurl%}}/session-based-messaging-api.html#AdvancedOptions)
{{%/panel%}}
{{% /tab %}}
{{%tab "  Runtime "%}}
{{%panel%}}
**Service Container - General**


```bash
com.gigaspaces.grid.space.SpaceHandler.level = FINE
org.jini.rio.level = INFO
com.gigaspaces.start.level = CONFIG
com.gigaspaces.grid.space.GigaSpacesFaultDetectionHandler.level=INFO
com.gigaspaces.grid.lookup.level = INFO
com.gigaspaces.management.level = INFO
```

**Lookup Service**


```bash
com.gigaspaces.core.lookupmanager.level = INFO
com.sun.jini.reggie.level = INFO
net.jini.discovery.LookupLocatorDiscovery.level = INFO
net.jini.lookup.ServiceDiscoveryManager.level = INFO
net.jini.discovery.LookupDiscovery.level = INFO
net.jini.lookup.JoinManager.level = INFO
net.jini.config.level = WARNING
com.sun.jini.start.service.starter.level = INFO
com.sun.jini.thread.TaskManager.level = INFO
```

Lookup Service is a runtime registry of GigaSpaces components. Each component registers itself to a LUS thereby providing visibility to other components. For e.g., a GSM discovers a GSC by looking at an entry in LUS and GSC discovers a GSM similarly. More information about LUS is [here](/product_overview/the-lookup-service.html).

**GSM**


```bash
com.gigaspaces.grid.gsm.level = INFO
com.gigaspaces.grid.gsm.peer.level = INFO
com.gigaspaces.grid.gsm.feedback.level = INFO
com.gigaspaces.grid.gsm.provision.level = INFO
com.gigaspaces.grid.gsm.services.level = INFO
com.gigaspaces.grid.gsm.service-instances.level = INFO
com.gigaspaces.grid.gsm.selector.level = INFO
org.jini.rio.tools.webster.level = INFO
```

GSM manages the applications and maintains the SLA's of deployments. More information about GSM is [here](/product_overview/service-grid.html#gsm).

**GSC**


```bash
com.gigaspaces.grid.gsc.level = INFO
com.gigaspaces.grid.gsc.GSCFaultDetectionHandler.level = INFO
com.gigaspaces.grid.gsm.GSMFaultDetectionHandler.level = INFO
org.openspaces.pu.container.servicegrid.PUFaultDetectionHandler.level = INFO
```

GSC is the runtime environment for GigaSpaces applications. More information about GSC's is [here](/product_overview/service-grid.html#gsc).

**ESM**


```bash
org.openspaces.grid.esm.level = INFO
```

Elastic Service Manager (ESM) is an implementation of the Elastic Middleware Services. It is built on-top of the existing administrative API exposed by the GigaSpaces components. See [The Elastic Service Manager]({{%currentjavaurl%}}/elastic-processing-unit.html) page for more details.


**GSA**


```bash
com.gigaspaces.grid.gsa.level = INFO
```

The GigaSpaces Agent (GSA) acts as a process manager that can spawn and manage Service Grid processes (Operating System level processes) such as The GigaSpaces Manager (GSM), The GigaSpaces Container (GSC), and Lookup Service (LUS). More information regarding GSA can be found [here](/product_overview/service-grid.html#gsa).
{{%/panel%}}
{{% /tab %}}
{{%tab "  Security "%}}
{{%panel%}}

```bash
com.gigaspaces.security.level = INFO
```

Logger corresponding to security of GigaSpaces components. This includes configuration and runtime execution of security functionality. More information regarding GigaSpaces security is [here]({{%currentsecurl%}}/security.html).
{{%/panel%}}
{{% /tab %}}
{{%tab "  Web Container "%}}
{{%panel%}}
Any web application default logging level (logger name for them is web.`[processing unit name].[instance id]`)


```bash
web.level = INFO
org.mortbay.level = WARNING
```

Web application support in XAP is provided using a Jetty container. These loggers correspond to Web Container. More information about GigaSpaces Web Application support is [here]({{%currentjavaurl%}}/web-jetty-processing-unit-container.html).
{{%/panel%}}
{{% /tab %}}
{{%tab "  Mule Integration "%}}
{{%panel%}}

```bash
org.mule.level = WARNING
org.mule.MuleServer.level = INFO
org.mule.RegistryContext.level = INFO
org.openspaces.esb.mule.level = WARNING
```

These loggers correspond to Mule integration. More information about Mule integration is here, [Mule ESB]({{%currentjavaurl%}}/mule-esb.html) and [Mule Processing Unit]({{%currentjavaurl%}}/mule-processing-unit.html)
{{%/panel%}}
{{% /tab %}}

{{%tab "  Management "%}}
{{%panel%}}
For GUI, browser, cluster view, JMX logging:


```bash
com.gigaspaces.admin.level = INFO
com.gigaspaces.admin.ui.level = INFO
com.gigaspaces.admin.ui.cluster.view.level = INFO
com.gigaspaces.admin.ui.spacebrowser.level = INFO
com.gigaspaces.admin.cli.level = INFO
com.gigaspaces.jmx.level = INFO
```

Loggers corresponding to XAPs Management Console/UI. Additional information regarding UI can be found [here]({{%currentadmurl%}}/gigaspaces-management-center.html).
{{%/panel%}}
{{% /tab %}}

{{%tab " Persistence"%}}

{{%panel%}}
For Persistence:


```bash
com.gigaspaces.persistent.level = INFO
com.gigaspaces.persistent.shared_iterator.level = INFO
```
{{%/panel%}}
{{% /tab %}}


{{% /tabs %}}

# Overriding the Default Configuration

The configuration defined in the `gs_logging.properties` file may be overridden by either using system properties or by providing an external configuration file with overrides. This external configuration file should be located in the classpath under:


```bash
/config/gs_ext_logging.properties
```

Any configuration that you wish to override in `gs_logging.properties` file, should appear in `gs_ext_logging.properties` with its new value. The same applies for system properties, e.g.


```bash
-Dcom.gigaspaces.exceptions.level=WARNING
```

{{% info "Defining System Properties when Starting GSCs, GSMs and other runtime components "%}}
The recommended way to define system properties when starting service grid components is to wrap the original script, e.g. `gsc.sh(bat)` with a wrapper script which include the EXT_JAVA_OPTIONS variable. The `setenv.sh(bat)` script which is used by these components will pick these options automatically and use them as JVM arguments.
{{% /info %}}

# Overriding the Configuration File

Your own configuration file may also be used instead of the platform's default. This is done by setting the configuration file location using a system property:


```bash
-Djava.util.logging.config.file=myfile.properties
```

GigaSpaces scripts rely on the exported environment variable `GS_LOGGING_CONFIG_FILE` (declared in `<GigaSpaces>/bin/setenv script`). The preferred way to apply your override file is to use a wrapper script: export the new setting of this variable and call the original script. This ensures that when `setenv.sh(bat)` is called from within the platform's scripts, it will pick up the override.


```bash
# unix

export XAP_LOGS_CONFIG_FILE=myfile.properties
./gsc.sh
```

Provided that your application initializes the logging facility via the Logging API (e.g. `LogManager.readConfiguration(InputStream ins)`), you may wish to disable the GigaSpaces configuration altogether. Once disabled, your Java logging settings will take place. This is done with the following system property:


```bash
-Dcom.gs.logging.disabled=true
```

# Troubleshooting

To troubleshoot and detect which logging properties file was loaded and from which location, set the following system property to **true**. This property already exists in the scripts (for convenience) and by default is set to **false**.


```bash
-Dcom.gs.logging.debug=true
```

# Handlers

GigaSpaces out of the box configures logging with two log Handlers,

- `java.util.logging.ConsoleHandler`: A simple handler for writing formatted output to System.err (level is set to ALL)
- `com.gigaspaces.logger.RollingFileHandler`: A handler that writes formatted output to a file that rolls over if a certain policy is triggered. see [Managing Log Files](./logging-managing-files.html)

Java util logging supports other handlers. MemoryHandler, SocketHandler or any other handler can be used instead of the above. More information about handlers is [here](http://docs.oracle.com/javase/{{%version "java-version"%}}/docs/technotes/guides/logging/). You can also use one of the [open source logging frameworks](http://java-source.net/open-source/logging) that support java.util.logging.

# Formatters

Formatters are in charge of formatting the log messages and adding meta data to them (date, time, level, etc).
GigaSpaces configures the logging `Handler`'s `formatter` property with a single `Formatter` implementation class:
`com.gigaspaces.logger.GSSimpleFormatter. This formatter class is based on the `java.util.logging.SimpleFormatter` class. see [Formatting Log Messages](./logging-formatting-messages.html) for more details.

# Exception visibility

GigaSpaces prints exception stack traces for messages with level `SEVERE` or higher.


```bash
com.gigaspaces.exceptions.level = SEVERE
```

Messages with lower levels with only be logged with the exception's `toString()` value. To force the logger to print the stack trace of exception with lower levels, such as Level `WARNING` for example, set the `com.gigaspaces.exceptions.level` property to `WARNING`.

Note that if the exception is a `java.lang.RuntimeException` its stack trace will always be logged, regardless of the level definition.

# Logging Management at Runtime

It is possible to change various logger level settings while the system is up and running without the need to restart it.
This can be very useful in production environments when the system needs to be troubleshooted, but at the same time cannot be restarted.

You can do so by connecting to the JMX Bean of the Java logging facility via JConsole for example.
You can start JConsole for a specific running GSC or GSM using the GigaSpaces Managment Center (`<GigaSpaces>\bin\gs-ui.sh(bat)`).
To change the logging level in JConsole do the following:

1. Traverse to the MBeans tab
1. Expand the `java.util.logging` tree node and locate the Logging tree node
1. Select the Operations tab
1. Type the logger's name and level for the arguments of the `setLoggerLevel()` method. For example, If you want to change `com.gigaspaces.exceptions.level` level to `WARNING`, use `setLoggerLevel(com.gigaspaces.exceptions, WARNING)`.

{{% include "/COM/jconsolejmapwarning.markdown" %}}

{{% info %}}
Note, you will need to use the logging level without the .level string e.g.: "com.gigaspaces.core.cluster.replication" and set value "FINE"
{{%/info%}}

The LoggingMXBean enables you to:

- Get the name of the log level associated with the specified logger
- Get the list of currently registered loggers
- Get the name of the parent for the specified logger
- Sets the specified logger to the specified new level


