---
type: post123
title:  Configuration
categories: XAP123SEC, PRM
parent: securing-xap-components.html
weight: 100
---




The security components (GSA, GSM, GSC and Processing Unit) can each have their own security configuration. As such, you can have different security behaviors even between Processing Units. But, all have one common concept: all configurations are declared using properties (either located in a properties file or part of the components properties).

The security implementation dictates the properties needed for configuration. For example, if you consider the out-of-the-box file-based security implementation, you can either rely on the defaults or configure the file location, password encoders, etc.

When a secured component is started, it looks for the security configuration properties in order to instantiate the security implementation.

# Enabling Security

Security can be enabled separately for the [Data layer](securing-your-data.html) and the [Grid layer](securing-the-grid-services.html).

# Logging

Any configurations that are applied can be seen by setting the logging level to CONFIG (see `xap_logging.properties`):


```java
com.gigaspaces.security.level = CONFIG
```

# Security Properties File

The security properties file is used to configure the `SecurityManager`, that is responsible for the authentication and authorization process.
The `security.properties` file is common to all components and is usually located under `<XAP root>/config/security/security.properties`.
 
To only affect the Web-UI, use the `webui-security.properties` instead.

The configuration file can be located anywhere in the classpath or in the classpath under `config/security`.

Alternatively, a system property can be set to indicate the location of the properties file: 

```java
-Dcom.gs.security.properties-file = my-security.properties
```

By setting `-Dcom.gs.security.properties-file` the property file will be located as a direct path (e.g. `~/home/user/my-security.properties`), 
a resource (e.g. "my-security.properties") in the classpath or in the classpath under `config/security`.

{{% note "Note"%}}
Since System Properties are JVM level, any Processing Unit deployed within a GSC will also benefit from this configuration. 
But, if security configurations were provided as part of the `pu.properties` they will be used instead.
{{%/note%}}

# Configuration Options

## Space

For a standalone Space, the default Space security configuration file is `<space-name>-security.properties`. 
Just like the `pu.properties`, you can include the security properties as part of the custom properties being passed to the space `/./space?properties=myCustomProps`.

## Processing Unit

The default Processing Unit (Space) security configuration file is `/META-INF/spring/pu.properties`. 
This is equivalent to custom properties being passed to the Space. 
This allows you to configure different configurations for different Processing Units. 
Thus, having a Processing Unit Cluster point to a specific security directory.

It is possible to separate the security configurations from the Processing Unit configurations, 
by placing the configurations in `/META-INF/spring/<Space name>-security.properties` file.

## Service Grid Components

The default Grid components security configuration file is `grid-security.properties`.  
Here you can declare different configurations that will affect all the Grid components (such as GSA, GSM, GSC) together.

## Web Management Console

The default Web Management Console component security configuration file is `webui-security.properties`.  The configuration file affects only the [Web Management Console](../admin/tools-web-ui.html#security).


