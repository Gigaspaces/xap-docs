---
type: post110
title:  Formatting Log Messages
categories: XAP110ADM
parent: logging-overview.html
weight: 300
---



The logged output of a logger can be customized to any format, such as an XML or a human readable format.

Typically each logging `Handler` will have a `Formatter` associated with it. The `Formatter` takes a `LogRecord` and converts it to a string. The default `Formatter` assigned to the `Handlers` declared in the logging configuration file is `com.gigaspaces.logger.` `GSSimpleFormatter`. This formatter is based on the `java.util.logging.``SimpleFormatter` class, which prints a brief summary of the `LogRecord` in a human readable format.

# The `format` Property

The `LogRecord` passed to the `Formatter` is converted to a string, as specified by the `format` property in the logging configuration file.

#### Placeholders

The format is simply a string, which can contain any character including line-break characters. GigaSpaces provides several placeholders that can be added to the message.

0 - Date object
1 - Class name
2 - Method name
3 - Log Level
4 - Logger name
5 - Message
6 - Context (processing unit, GSC, etc)
7 - Thread name

#### The default format

By default, the logging configuration file comes with the following as the default format:


```bash
com.gigaspaces.logger.GSSimpleFormatter.format = {0,date,yyyy-MM-dd HH:mm:ss,SSS} {6} {3} [{4}] - {5}
```

Which translates to: **Date and time* printed down to the millisecond, **context** (if available), the **log level** of the message, the **logger name** and the **message**.

#### Customized format

If you wish to customize the format, change the default setting the in the logging configuration file (`<GigaSpaces>/config/gs_logging.properties`).
For example,


```bash
com.gigaspaces.logger.GSSimpleFormatter.format={0,date} {1}.{2}(..) {3} - {5}
```

Which translates to: **Date** in it's simplest form, **Class** name and **method**, the **log level** of the message and the **message**


```bash
Jul 14, 2009  com.gigaspaces.Class.method(..) INFO - Sample message
```

{{%refer%}}
For more details on how to customize a message see [java.text.MessageFormat](http://java.sun.com/javase/{{%version "java-version"%}}/docs/api/java/text/MessageFormat.html) and [java.text.SimpleDateFormat](http://java.sun.com/javase/{{%version "java-version"%}}/docs/api/java/text/SimpleDateFormat.html)
{{%/refer%}}
