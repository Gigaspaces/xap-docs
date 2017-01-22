---
type: post121
title:  InfluxDB Reporter
categories: XAP121ADM
parent: metrics-overview.html
weight: 200
---

GigaSpaces XAP is shipped with built-in support for {{%exurl "InfluxDB" "http://influxdb.com/"%}} v0.9. This page explains how to configure XAP to report metrics to InfluxDB. 

Configuration takes place in `metrics.xml`, which resides in `XAP_HOME/config/metrics`. For more information see [Configuration](./metrics-configuration.html).

# Usage

InfluxDB provides an {{%exurl "HTTP API" "https://influxdb.com/docs/v0.9/guides/writing_data.html"%}} for writing data, which is implemented by XAP. For example, if InfluxDB is installed on `localhost` and you want to report metrics to the `mydb` database, use the following configuration:

```xml
<metrics-configuration>
    <reporters>
        <reporter name="influxdb">
            <property name="host" value="localhost"/>
            <property name="database" value="mydb"/>
        </reporter>
    </reporters>
</metrics-configuration>
```

### Port

The InfluxDB HTTP API is bounded to port `8086` by default. If you've configured your InfluxDB instance to use a different port you should modify the reporter configuration accordingly and add a `<property name="port" value="nnnn"/>` with the new port.

### Security

InfluxDB security is disabled by default. Should you choose to enable it, add a `username` and `password` properties to the configuration to specify the credentials which will be used by the reporter.

### Retention Policy

InfluxDB uses {{%exurl "retention policies" "https://influxdb.com/docs/v0.9/concepts/glossary.html#retention-policy"%}} to define how long the data is stored and how it's replicated accross the cluster. You can configure a `retention-policy` property to instruct the reporter which retention policy to write to. If not set, the default retention policy of the database will be used.

### Report Length

The InfluxDB reporter batches multiple metrics within each report to maximize performance, up to a maximum value determined by the `max-report-length` property. The default value is `65507` (based on {{%exurl "UDP max length" "http://en.wikipedia.org/wiki/User_Datagram_Protocol"%}}), and usually should not be changed. 

### UDP

InfluxDB allows you to {{%exurl "write data through UDP" "https://influxdb.com/docs/v0.9/write_protocols/udp.html"%}}. Note that by default, no InfluxDB ports are open to UDP. To configure InfluxDB to support writes over UDP you must adjust its config file. Once you do, you need to configure XAP InfluxDB reporter to use `udp` by adding a `<property name="protocol" value="udp"/>` property to the configuration. You should also specify the port you've set in InfluxDB config file.

Note that when using `udp` the database is configured for the UDP endpoint at the InfluxDB config file, so there's no need to configure it in XAP.

