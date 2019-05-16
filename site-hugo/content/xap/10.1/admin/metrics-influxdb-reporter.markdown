---
type: post101
title:  InfluxDB Reporter
categories: XAP101ADM
parent: metrics-overview.html
weight: 200
canonical: auto
---

GigaSpaces XAP is shipped with built-in support for [InfluxDB](http://influxdb.com/). This page explains how to configure XAP to report metrics to InfluxDB. 

Configuration takes place in `metrics.xml`, which resides in `XAP_HOME/config/metrics`. For more information see [Configuration](./metrics-configuration.html).

{{%info%}}InfluxDB StatusThe GigaSpaces InfluxDB reporter implementation is based on [InfluxDB v0.8.x](http://influxdb.com/docs/v0.8/). InfluxDB v0.9 (which is expected to be production ready in April 2015) includes some powerful enhancements (e.g. tags support), which the XAP metric framework can leverage as well. We're already experimenting with InfluxDB 0.9 and plan to release a compatible reporter soon after InfluxDB 0.9 is GA.{{%/info%}}


{{%note%}}
InfluxDB currently runs only on Linux. If you have a repository that runs on Windows, you can implement your own [custom reporter](./metrics-custom-reporter.html) to integrate with the repository.
{{%/note%}}


# HTTP Reporter

InfluxDB provides an [HTTP API](http://influxdb.com/docs/v0.8/api/reading_and_writing_data.html#writing-data-through-http) for writing data, which is implemented by XAP. For example, if InfluxDB is installed on `myhost` and you want to report metrics to the `mydb` database, use the following configuration:


```xml
<metrics-configuration>
    <reporters>
        <reporter name="influxdb-http">
            <property name="host" value="myhost"/>
            <property name="database" value="mydb"/>
            <property name="username" value="root"/>
            <property name="password" value="root"/>
        </reporter>
    </reporters>
</metrics-configuration>
```

### Security

InfluxDB security is enabled by default, and requires a username and password to access the database. The default username and password are `root`, as in the example above. If your database has different username/password, change the configuration accordingly.

### Port

The InfluxDB HTTP API is bounded to port `8086` by default. If you've configured your InfluxDB instance to use a different port you should modify the reporter configuration accordingly and add a `<property name="port" value="nnnn"/>` with the new port.


### Report Length

The InfluxDB reporter batches multiple metrics within each report to maximize performance, up to a maximum value determined by the `max-report-length` property. The default value is `65507` (based on [UDP max length](http://en.wikipedia.org/wiki/User_Datagram_Protocol)), and usually should not be changed. 

# UDP Reporter

InfluxDB allows you to [write data through UDP](http://influxdb.com/docs/v0.8/api/reading_and_writing_data.html#writing-data-through-json-+-udp), assuming you will be writing data to a single database which is configured in InfluxDB's configuration file. The following snippet shows how to configure xap to use it:


```xml
<metrics-configuration>
    <reporters>
        <reporter name="influxdb-udp">
            <property name="host" value="myhost"/>
            <property name="port" value="4444"/>
        </reporter>
    </reporters>
</metrics-configuration>
```

Note we're setting the `host` to the location of the InfluxDB installation, and the database is not configured here at all. The default port is `4444`.

