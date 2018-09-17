---
type: post122
title:  Configuration
categories: XAP122ADM, PRM
parent: metrics-overview.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}

XAP provides a framework for collecting and reporting metrics from the distributed runtime environment into a metric repository of your choice, which can then be analysed and used to identity trends in the system behaviour. Before you can start using the metrics framework you will need to first setup and configure InfluxDB & Grafana to work with XAP. You can follow this guide [here](./web-management-monitoring.html) on how to do this.

# Overview

The Metrics framework is composed of **Metrics**, **Metric Samplers** and **Metric Reporters**.

A **Metric** is a piece of code which provides a value of something at the current time (e.g. CPU percentage, free memory, active LRMI threads, etc.). XAP is bundled with an abundance of metrics which can be used to monitor its behaviour, and additional metrics can be [defined by the user](./metrics-user-defined.html).

Each metric is registered in a **Metric Sampler**, which periodically samples all its registered metrics and publishes them via one or more **Metric Reporter**. XAP can be configured to modify the sample rate of the default sampler or configure additional samplers to provide different granularity for different metrics groups.

A **Metric Reporter** receives a snapshot of metrics values from the sampler and reports them to wherever it wishes. XAP is bundled with an [InfluxDB Reporter](./metrics-influxdb-reporter.html), and users can implement additional [custom reporters](./metrics-custom-reporter.html) as they wish.

# Configuration

By default, Metrics configuration is loaded from `XAP_HOME/config/metrics/metrics.xml`. This location can be overridden using the `com.gigaspaces.metrics.config` system property.

## Samplers

A **Metric Sampler** is denoted by a name, and has two properties which determine its behaviour:

* `sample-rate` - Determines the rate at which the sampler samples its metrics. can be specified in minutes (`m`), seconds (`s`) or even milliseconds (`ms`).
* `report-rate` - Determines the rate at which the sampler reports the samples via the reporters. If not configured, same as sample rate (i.e. each sample is reported immediately). Useful for reducing network burden when sample-rate is small (e.g. sample each second but report the last 10 samples every 10 seconds). Must be greater than sample-rate, and must be a multiple of sample-rate.

The default configuration contains the following samplers. Users may modify their settings, or add/remove additional samplers as they please.


```xml
<metrics-configuration>
    <!-- define which sampling rates can be assigned to a metric -->
    <samplers>
        <!-- 'default' is configured to sample (and report) its metrics every 5 seconds -->
        <sampler name="default" sample-rate="5s" />
        <!-- 'high' is configured to sample its metrics every second, and report in batch every 5 seconds -->
        <sampler name="high" sample-rate="1s" report-rate="5s" />
        <!-- 'low' is configured to sample (and report) its metrics every minute -->
        <sampler name="low" sample-rate="1m" />
        <!-- 'off' is configured to never sample (and report) its metrics -->
        <sampler name="off" sample-rate="0" />
    </samplers>
</metrics-configuration>
```

## Assigning Metrics to Samplers

By default, metrics are assigned to the `default` sampler. A metric can be configured to use a different sampler. For example, to assign the 'free memory' metric to the `high` sampler:


```xml
<metrics-configuration>
    <metrics>
        <metric prefix="os_memory_free-bytes" sampler="high"/>
    </metrics>
</metrics-configuration>
```

In fact, a more common user story is to assign a group of metrics to a sampler rather than a specific one. For example, to assign all memory metrics to the `high` sampler:


```xml
<metrics-configuration>
    <metrics>
        <metric prefix="os_memory" sampler="high"/>
    </metrics>
</metrics-configuration>
```

Now, suppose that you also want to turn off CPU metrics because you're not interested in them:


```xml
<metrics-configuration>
    <metrics>
        <metric prefix="os_memory" sampler="high"/>
        <metric prefix="os_cpu"    sampler="off"/>
    </metrics>
</metrics-configuration>
```

And finally, suppose you want all other `os` metrics to use the `low` sampler:


```xml
<metrics-configuration>
    <metrics>
        <metric prefix="os"        sampler="low"/>
        <metric prefix="os_memory" sampler="high"/>
        <metric prefix="os_cpu"    sampler="off"/>
    </metrics>
</metrics-configuration>
```

As you can see, detailed prefix takes precedence over less detailed ones.
