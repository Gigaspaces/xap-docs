---
type: post122
title:  Spark Logging for InsightEdge
categories: XAP122ADM, IEE
weight: 75
---


Spark uses log4j to make logging calls.
For information about the log4j logging framework, refer to the {{%exurl "log4j Overview" "http://logging.apache.org/log4j/1.2/index.html"%}} online documentation.

# Configuration

Log4j configures the logging during initialization, using a logging configuration file that is read at startup. This logging configuration file is in the standard `java.util.Properties` format.

## InsightEdge log4j Default Configuration

The default Spark logger configuration file is located under the following directory:

```bash
<XAP_HOME>/insightedge/conf/spark_log4j.properties
```

## Configuration File Properties

### Console Appender

The Console Appender writes log messages to the console.

### RollingFileAppender

The RollingFileAppender writes log messages to log files. The Spark log files are: 

* Written to <XAP_HOME>/logs.
* Rolled based on file size (up to 2GB)

#### Log File Name Format

The format of the log file name is:

```bash
{yyyy-MM-dd~HH.mm}-gigaspaces-{role}.log
```
The role is either spark-master or spark-worker. For example:
```bash
2017-09-17~17.44-gigaspaces-spark-master.log
```
### Spark Log Message Format

In log4j, the log message format is configured in the `*.layout.ConversionPattern` property. In InsightEdge, the Spark log message format is configured to match the XAP format. Therefore, the ConversionPattern is:

```bash
    log4j.appender.console.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} %p [%c] - %m%n
    log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} %p [%c] - %m%n
```

# Overriding the Default Configuration

Spark_log4j.properties can either be modified to suit the requirements of your environment, or deleted if you prefer to use the Spark default log4j configuration.
