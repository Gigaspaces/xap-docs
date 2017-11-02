---
type: post123
title:  Admin API
categories: XAP123, PRM
parent: none
weight: 1900
---




The Admin API provides a way to administer and monitor all of XAP services and components using a simple API. The API provides information and the ability to operate on the currently running [XAP Agent](/product_overview/service-grid.html#gsa), [XAP Manager](/product_overview/service-grid.html#gsm), [XAP Container](/product_overview/service-grid.html#gsc), [Lookup Service](/product_overview/service-grid.html#lus), [Processing Unit](./the-processing-unit-overview.html) and Spaces.


**Dependencies**<br>
In order to use this feature, include the `${XAP_HOME}/lib/platform/service-grid/xap-admin.jar` file on your classpath or use maven dependencies:

```xml
<dependency>
    <groupId>com.gigaspaces</groupId>
    <artifactId>xap-admin</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
```
{{%refer%}}
For more information on dependencies see [Maven Artifacts](../started/maven-artifacts.html)
{{%/refer%}} 

<br>

{{%fpanel%}}

[Admin API Overview](./administration-and-monitoring-api.html)<br>
Simple API to monitor and administer XAP services and components.

[Dump API](./dump.html)<br>
The dump feature of the Admin API allows to easily generate dump information out of GigaSpaces runtime environment.

[Administrative Alerts](./administrative-alerts.html)<br>
The alert mechanism provides the ability to receive alerts on various problematic conditions at runtime by using the Administration and Monitoring API. The alerts give indication for the health state of the system.


[SNMP Alerts](./snmp-connectivity-via-alert-logging-gateway.html)<br>
The XAP Alert interface exposes the XAP environment and the application's health state. It allows users to register listeners on one or more alert types and receive notifications once an alert has been raised or has been resolved.


{{%/fpanel%}}
