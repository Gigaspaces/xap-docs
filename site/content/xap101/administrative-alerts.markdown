---
type: post101
title:  Administrative Alerts
categories: XAP101
weight: 300
parent: administration-and-monitoring-overview.html
---




The alert mechanism provides the ability to receive alerts on various problematic conditions at runtime by using the [Administration and Monitoring API](./administration-and-monitoring-api.html). The alerts give indication for the "health state" of the system.

{{% info "Runtime Model "%}}
The alerts rules are running within the `Admin` instance which the user created. GigaSpaces does not run an alert service that is running behind the scenes. If you would like to register for alerts in your production system, the recommended way to do it is to deploy your alert listener to the GigaSpaces [The Runtime Environment]({{%currentadmurl%}}/the-runtime-environment.html) in the form of a [processing unit](./the-processing-unit-structure-and-configuration.html).
{{% /info %}}

The [Administration and Monitoring API](./administration-and-monitoring-api.html) provides events and statistics on top of which 'rules' can be applied to trigger an alert when required.

- **Events** such as `ReplicationStatusChangedEvent` indicate that the `ReplicationStatus` has changed (e.g. ACTIVE, DISABLED, DISCONNECTED). From this event, an alert trigger can be written to raise an alert if replication has disconnected, and to resolve an alert once replication has reconnected (ref. **Predefined Alerts** below).

- **Statistics** such as `OperatingSystemStatistics` arrive as an `OperatingSystemStatisticsChangedEvent`, from which you can extract statistics - in example CPU utilization statistics. An alert trigger can be written to raise an alert if the CPU utilization has crossed a threshold (e.g. 80%), and to resolve an alert once CPU utilization has crossed below a threshold (e.g. 60%).

These alerts provide for better supportability and easier troubleshooting.

The following diagram illustrates a simple flow of events/statistics sent to the 'Alert Trigger' which checks if the state answers the condition to raise an alert or to resolve an alert. Notice that an alert may be raised multiple times until it is finally resolved.

![alerts-flow-diagram.png](/attachment_files/alerts-flow-diagram.png)

# Predefined Alerts

XAP is packaged with a number of predefined alerts whose thresholds are configurable. XAP does not currently allow for custom alerts defined by the user. Alerts can be either raised or resolved. For example, a CPU alert can be raised when the CPU utilization on a certain host crosses a certain threshold (say 80%) for a predefined amount of time. This alert can be resolved when the CPU utilization goes below another threshold (say 30%) for a predefined amount of time.


| Alert Type| Default Thresholds|
|:----------|:------------------|
| CPU Utilization Alert | - Raise an alert if CPU utilization is above 80% for 1 minute{{<wbr>}}- Resolve alert if CPU utilization is below 60% for 1 minute |
| Physical Memory Utilization Alert | - Raise an alert if physical memory utilization is above 80% for 1 minute{{<wbr>}}- Resolve alert if physical memory utilization is below 60% for 1 minute |
| Heap Memory Utilization Alert | - Raise an alert if JVM heap memory utilization is above 80% for 1 minute{{<wbr>}}- Resolve alert if JVM heap memory utilization is below 60% for 1 minute |
| Garbage Collection Alert | - Raise an alert if a GC pause takes more than 10 seconds{{<wbr>}}- Resolve alert if GC pause takes less than 1 second |
| Replication Channel Disconnection Alert | - Raise an alert if replication channel has been disconnected between source and target Space{{<wbr>}}- Resolve alert if replication channel is reconnected between source and target Space |
| Replication Redo Log Overflow to Disk Alert | - Raise an alert if replication redo log has exceeded its memory capacity and has over flown to disk.{{<wbr>}}- Resolve alert if replication redo log no longer uses the disk |
| Replication Redo Log Size Alert | -  Raise an alert if replication redo log size goes 100,000 packets{{<wbr>}}- Resolve alert if replication redo log size goes below 1,000 packets |
| Mirror Persistence failure Alert | - Raise an alert if Mirror Space failed to persist due to an error (e.g. a DB error). The alert is reported via the replication channel{{<wbr>}}- Resolve alert if Mirror Space manages to persist to the DB for first time after the alert has been raised. |

# Static Configuration

The static configuration for the predefined alerts mentioned above is defined in the `<XAP Root>/config/alerts/alerts.xml` file. It includes the default settings for each alert.

For example, the following is a snippet that represents the configuration of the **CPU Utilization Alert**.
The alert is configured with a high threshold of 80% and a low threshold of 60% and a   period of 1 minute. An alert will be raised if CPU utilization in a certain host in the GigaSpaces cluster crosses 80% for a period of 1 minute. A raised alert will be resolved if the CPU utilization goes below 60% for 1 minute.


```xml
...
    <alert class="org.openspaces.admin.alert.config.CpuUtilizationAlertConfiguration"
           enabled="true">
        <property key="high-threshold-perc" value="80" />
        <property key="low-threshold-perc" value="60" />
        <property key="measurement-period-milliseconds" value="60000" />
    </alert>
...
```

The `class` attribute above is the implementation class used to configure the settings of this alert. When configuring `enabled="false"` alerts of this type will not be triggered, until enabled again (at runtime).

Note that some alert triggers define that an alert is raised each time a certain high threshold is crossed for example. This means that can be are multiple raised alerts at the same time, each indicating its own utilization reading at the time it was triggered. On the other hand, there can only be one resolving alert, which 'resolves' the already raised alert/s.

Since GigaSpaces XAP is working in a distributed environment, an alert is identified with a specific component (machine, JVM, Space, etc.).

# Viewing Alerts (Web-UI)

Alerts are visible in the Alerts View of the Web-UI Since 8.0.1. The Web-UI server utilizes the `<XAP Root>/config/alerts/alerts.xml` configuration file. These configurations apply to any client connecting to the Web-UI at the specified host and port.

The alerts are grouped together by their 'type' (e.g. CPU, Memory, GC, etc.). When an alert is raised, it is aggregated with other consecutive alerts of the same 'type'. Previous alerts form the aggregation get "pushed" down (circled in red). A resolved alert "closes" the aggregation (circled in green). A new alert of the same 'type' will "open" a new aggregation.

Sort the 'status' column in ascending order to show latest unresolved alerts.

![alerts-view-web-ui.png](/attachment_files/alerts-view-web-ui.png)

# Listening for Alerts

Alerts can be consumed using a registered event listener by registering with the `AlertManager` component (which is part of the [Administration and Monitoring API](./administration-and-monitoring-api.html). The listener will be notified of alerts that have been triggered.

Javadoc ref: [Alert](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/alert/Alert.html)&nbsp;[AlertManager](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/alert/AlertManager.html)&nbsp;[XmlAlertConfigurationParser](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/alert/config/parser/XmlAlertConfigurationParser.html)&nbsp;[AlertTriggeredEventListener](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/alert/events/AlertTriggeredEventListener.html)


```java
 Admin admin = new AdminFactory().createAdmin();

 AlertManager alertManager = admin.getAlertManager();

 alertManager.configure(new XmlAlertConfigurationParser("alerts.xml").parse());

 alertManager.getAlertTriggered().add(new AlertTriggeredEventListener() {

            public void alertTriggered(Alert alert) {
                System.out.println(alert);
            }
 });
```

{{% info "Alert XML Configuration "%}}
The `"alerts.xml"` file argument specifies the file which holds the configuration settings.
The file argument can be:

- a direct path (e.g. /export/user/my-alerts.xml) or,
- a file in the classpath or,
- a file under config/alerts/ in the classpath.
{{% /info %}}

The default parser parses the XML file. If needed, you can implement a different parser (see [AlertConfigurationParser](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/alert/config/parser/AlertConfigurationParser.html) interface).
The `alertTriggered` method is called upon each alert triggered.

# The Alert Event

The `org.openspaces.admin.alert.Alert` instance includes the following set of properties:


| Name | Description | Value |
|:-----|:------------|:------|
| Description | The readable description of the alert | e.g. CPU Utilization crossed a threshold of 80% |
| Timestamp | The date and time of the alert occurred | `System.currentTimeMillis()` |
| Severity | The alert severity | `SEVERE, WARNING, INFO` |
| Status | The alert status | `ESCALATED,` `RAISED` `, SUPPRESSED,` `RESOLVED` `, NA` |
| UID | A unique identifier for this alert | component hash code with an incrementing number |
| Component UID | The grid component UID that the alert has been triggered for | e.g. "service-id: 15f4b0b4-3a78-413f-bd3a-429a50dcdf05" |
| Group UID | A unique identifier for a set of alerts triggered for the same component,{{<wbr>}}e.g. two different machines will have a different group UID if their CPU utilization {{<wbr>}}raised an alert.But for each machine, the raised alerts and resolved{{<wbr>}}alert for CPU utilization will have the same group UID | e.g. "aafb1222-826c3cbb-73c6-4903-bdc1-d858f1324e12" |
| Config | The configuration properties set for this alert | |
| Properties | Any runtime properties that could be extracted and are relevant information for this alert | e.g. host name, host address, memory utilization,etc. |

Javadoc ref: [AlertSeverity](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/alert/AlertSeverity.html)&nbsp;[AlertStatus](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/alert/AlertStatus.html)

# Runtime Configuration

The static alert configurations are parsed using the `XmlAlertConfigurationParser`. This pre-configures the alert manager with all the alerts found in the `alerts.xml`.


```java
 Admin admin = new AdminFactory().createAdmin();
 AlertManager alertManager = admin.getAlertManager();

 alertManager.configure(new XmlAlertConfigurationParser("alerts.xml").parse());
 ...
```

Alert configuration settings can be changed (at runtime) for a pre-configured/pre-defined alert type.

#### Enabling and Disabling of an alert

A pre-configured but **disabled** alert can be easily enabled, but an already **enabled** alert type will need to be **disabled** prior to setting a new configuration.

#### Enable a Disabled Predefined Alert

For a predefined but disabled alert, enable it by specifying the alert Class type. The configuration settings that were predefined will be used.


```java
alertManager.enableAlert(CpuUtilizationAlertConfiguration.class);
```

#### Disable a Predefined Enabled Alert

Too disable an existing alert (yet keep its configuration), use the following code:


```java
alertManager.disableAlert(CpuUtilizationAlertConfiguration.class);
```

#### Re-configure a predefined alert

For a predefined alert, obtain the current configuration, change the settings and re-configure the `AlertManager`.
In the background, the enabled alert will be disabled, set and re-enabled with the new configuration.


```java
CpuUtilizationAlertConfiguration config = alertManager.getConfig(CpuUtilizationAlertConfiguration.class);
config.setHighThresholdPerc(85);
alertManager.configure(config);
```

#### Configure and enable a predefined disabled alert

If predefined settings need to be changed, get the configuration, change the settings, enable and re-configure.


```java
CpuUtilizationAlertConfiguration config = alertManager.getConfig(CpuUtilizationAlertConfiguration.class);
config.setHighThresholdPerc(85);
config.setEnabled(true); //don't forget
alertManager.configure(config);
```

#### Configure an Undefined Alert

For an alert which wasn't defined in the original set of alerts, create a new configuration with required settings and call configure.


```java
CpuUtilizationAlertConfiguration config = new CpuUtilizationAlertConfiguration();
config.setHighThresholdPerc(85);
config.setLowThresholdPerc(70);
config.setMeasurementPeriod(60, TimeUnit.SECONDS);
config.setEnabled(true);
alertManager.configure(config);
```

#### Using the `AlertConfigurer`

For a more fluent API, `AlertConfigurer` implementations provide chaining methods.


```java
final AlertManager alertManager = admin.getAlertManager();
alertManager.setConfig(
      new CpuUtilizationAlertConfigurer()
          .raiseAlertIfCpuAbove(80)
          .resolveAlertIfCpuBelow(60)
          .create()
);
alertManager.enableAlert(CpuUtilizationAlertConfiguration.class);
```

Since 8.0.2  the `enabled` indication has been added to the `AlertConfigurer` API.
This lets you use the `AlertManager.configure(AlertConfiguration ...)` method to configure one or more alert configurations following the same fluent API usage as above.

This code sample does exactly the same as the code above.


```java
final AlertManager alertManager = admin.getAlertManager();
alertManager.setConfig(
      new CpuUtilizationAlertConfigurer()
          .raiseAlertIfCpuAbove(80)
          .resolveAlertIfCpuBelow(60)
          .enabled(true) //since 8.0.2
          .create()
);
```

