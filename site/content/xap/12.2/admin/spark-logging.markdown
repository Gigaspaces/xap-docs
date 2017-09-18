---
type: post122
title:  Spark Logging for InsightEdge
categories: XAP122ADM, IEE
weight: 75
---


Spark uses log4j to make logging calls.
For more information on the log4j logging framework, please refer to the following online documentation: {{%exurl "log4j Overview" "http://logging.apache.org/log4j/1.2/index.html"%}}.

# Configuration

The logging configuration is initialized using a logging configuration file that is read at startup. This logging configuration file is in the standard `java.util.Properties` format.

## InsightEdge log4j Default Configuration

The default Spark logger configuration file is located under:

```bash
<XAP_HOME>/insightedge/conf/spark_log4j.properties
```

### Configuration Overview

#### Console Appender
Writes log messages to the console

#### RollingFileAppender
Writes log messages to log files
1. Spark log files are written to <XAP_HOME>/logs
2. They are rolled based on file size (up to 2GB)

##### log file name format
The format of the log file name is:
```bash
{yyyy-MM-dd~HH.mm}-gigaspaces-{role}.log
```
The role is either spark-master or spark-worker. For example:
```bash
2017-09-17~17.44-gigaspaces-spark-master.log
```
#### Spark log message ConversionPattern
In log4j, log message format is configured in the *.layout.ConversionPattern property.

In InsightEdge, spark log messages format is configured to match the XAP format. The ConversionPattern is
```bash
    log4j.appender.console.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} %p [%c] - %m%n
    log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} %p [%c] - %m%n
```

## Overriding the Default Configuration

Spark_log4j.properties can either be modified to your preference or deleted if you want to use the spark default log4j configuration.
