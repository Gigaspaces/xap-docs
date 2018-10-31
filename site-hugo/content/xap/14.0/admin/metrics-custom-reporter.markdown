---
type: post140
title:  Custom Reporter
categories: XAP140ADM, PRM
parent: metrics-overview.html
weight: 300
---



This page demonstrates how to implement and use a custom metric reporter. For demonstration purposes we'll show a reporter which writes metrics to a text file.

# Implementation

A custom metric reporter implementation requires overriding two classes: `MetricReporter` and `MetricReporterFactory`. The custom `MetricReporter` accepts metric snapshots and reports them as it wishes. The `MetricReporterFactory` is used to encapsulate configuration and instantiate the `MetricReporter`.

### FileReporterFactory

The `FileReporterFactory` extends `MetricReporterFactory` in order to create a reporter (in this case, a `FileReporter`) instance with the relevant settings. This being a *file* reporter, we'll obviously want to let the user choose the target file. For example:


```java
public class FileReporterFactory extends MetricReporterFactory<FileReporter> {
    private String path;

    @Override
    public FileReporter create() throws Exception {
        return new FileReporter(this);
    }

    @Override
    public void load(Properties properties) {
        super.load(properties);
        path = properties.getProperty("path");
        if (path == null)
            throw new RuntimeException("Property `path` must be provided");
    }

    public String getPath() {
        return path;
    }
}
```

Note that we're overriding the `load(Properties properties)` method to load the `path` property from the configuration file.

### FileReporter

Implementing a reporter requires creating a constructor to configure the reporter using the reporter factory, and overriding the `report(List<MetricRegistrySnapshot> snapshots)` method to traverse the metrics data and actually report it (in this case, writing it to a file). For example:


```java
public class FileReporter extends MetricReporter {
    private final File file;

    public FileReporter(FileReporterFactory factory) {
        super(factory);
        this.file = new File(factory.getPath()).getAbsoluteFile();
    }

    @Override
    public void report(List<MetricRegistrySnapshot> snapshots) {
        StringBuilder sb = new StringBuilder();
        sb.append("Reported at " + formatDateTime(System.currentTimeMillis())).append(EOL);
        for (MetricRegistrySnapshot snapshot : snapshots) {
            sb.append("Sample taken at " + formatDateTime(snapshot.getTimestamp())).append(EOL);
            for (Map.Entry<MetricTagsSnapshot, MetricGroupSnapshot> groupEntry : snapshot.getGroups().entrySet()) {
                sb.append("\tTags: " + groupEntry.getKey()).append(EOL);
                for (Map.Entry<String, Object> metricEntry : groupEntry.getValue().getMetricsValues().entrySet()) {
                    sb.append("\t\t"+metricEntry.getKey() + " => " + metricEntry.getValue()).append(EOL);
                }
            }
        }
        writeToFile(sb.toString());
    }
}
```

The `report` method receives a list of snapshots. By default each sample is immediately reported (i.e. the list will contain a single snapshot), but users may configure the sampler to batch multiple samples into each report. The `MetricRegistrySnapshot` contains mapping of metrics tags to metrics groups - since each metric is registered with tags, the samples are grouped by those tags to simplify the report. Finally, each `MetricGroupSnapshot` contains mappings from metrics names to metrics values.

Note that some methods and fields were omitted from this snippet for brevity. The full implementation is available here:

{{%accordion%}}
{{%accord title="FileReporterFactory"%}}

```java
package com.gigaspaces.demo;

import com.gigaspaces.metrics.MetricReporterFactory;
import java.util.Properties;

public class FileReporterFactory extends MetricReporterFactory<FileReporter> {
    private String path;

    @Override
    public FileReporter create() throws Exception {
        return new FileReporter(this);
    }

    @Override
    public void load(Properties properties) {
        super.load(properties);
        path = properties.getProperty("path");
        if (path == null) {
            throw new RuntimeException("Property [path] must be provided when using file reporter");
        }
    }

    public String getPath() {
        return path;
    }
}
```
{{%/accord%}}
{{%accord title="FileReporter"%}}

```java
package com.gigaspaces.demo;

import com.gigaspaces.metrics.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class FileReporter extends MetricReporter {
    private static final Logger logger = Logger.getLogger(FileReporter.class.getName());
    private static final String EOL = System.getProperty("line.separator");

    private final Date date = new Date();
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    private final File file;

    protected FileReporter(FileReporterFactory factory) {
        super(factory);
        this.file = new File(factory.getPath()).getAbsoluteFile();
    }

    @Override
    public void report(List<MetricRegistrySnapshot> snapshots) {
        StringBuilder sb = new StringBuilder();
        sb.append("Reported at " + formatDateTime(System.currentTimeMillis())).append(EOL);
        for (MetricRegistrySnapshot snapshot : snapshots) {
            sb.append("Sample taken at " + formatDateTime(snapshot.getTimestamp())).append(EOL);

            for (Map.Entry<MetricTagsSnapshot, MetricGroupSnapshot> groupEntry : snapshot.getGroups().entrySet()) {
                sb.append("\tTags: " + groupEntry.getKey()).append(EOL);
                for (Map.Entry<String, Object> metricEntry : groupEntry.getValue().getMetricsValues().entrySet()) {
                    sb.append("\t\t"+metricEntry.getKey() + " => " + metricEntry.getValue()).append(EOL);
                }
            }
        }
        writeToFile(sb.toString());
    }

    private String formatDateTime(long timestamp) {
        date.setTime(timestamp);
        return dateFormatter.format(date);
    }

    private void writeToFile(String text) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file, true);
            fw.write(text);
        } catch (IOException e) {
            logger.warning("Failed to write report to file");
        }
        if (fw != null) {
            try {
                fw.close();
            } catch (IOException e) {
                logger.warning("Failed to close FileWriter");
            }
        }
    }
}
```
{{%/accord%}}
{{%/accordion%}}

# Usage

Now that we have a custom reporter implementation we need to configure the system to use it. This is done via the `metrics.xml` configuration file (located under `XAP_HOME/config/metrics` by default), at the `reporters` element. For example, to create a file reporter named `myReporter` which writes results to `c:\gigaspaces\metrics-output.txt`:


```xml
<metrics-configuration>
    <reporters>
        <reporter name="myReporter" factory-class="com.gigaspaces.metrics.reporters.FileReporterFactory">
            <property name="path" value="c:\gigaspaces\metrics-output.txt"/>
        </reporter>
    </reporters>
</metrics-configuration>
```

Basically we're telling the metrics manager which class should be used to instantiate the reporter and which parameters to provide along.

In addition, we'll need to include the compiled `FileReporter` and `FileReporterFactory` classes in the product's class path. The easiest way to accomplish this is under `XAP_HOME/lib/optional/metrics`, which is automatically included in the class path.

