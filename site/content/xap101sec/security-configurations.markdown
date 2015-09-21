---
type: post101
title:  Configuration
categories: XAP101SEC
parent: none
weight: 400
---

{{% ssummary %}}{{% /ssummary %}}


The security components (GSA, GSM, GSC and Processing Unit) can each have their own security configuration. As such, you can have different security behaviors even between Processing Units. But, all have one common concept: all configurations are declared using properties (either located in a properties file or part of the components properties).

The security implementation dictates the properties needed for configuration. For example, if you consider the out-of-the-box file-based security implementation, you can either rely on the defaults or configure the file location, password encoders, etc.

When a secured component is started, it looks for the security configuration properties in order to instantiate the security implementation.

# Logging

Any configurations that are applied can be seen by setting the logging level to CONFIG (see `gs_logging.properties`):


```java
com.gigaspaces.security.level = CONFIG
```

# The Security Properties File

The default `security.properties` file is looked for in the classpath or in the classpath under `config/security`.
If all of the components will be requiring the same security configuration, then this is all you need.

Commonly placed under:


```java
<XAP root>/config/security/security.properties
```

# Component Security Properties File

If specific configurations are to be applied to a component, then the +component's+ `xxx-security.properties` file is first located by using a direct path. If it doesn't exist, it is looked for in the classpath or in the classpath under `config/security/`. If the +component's+ file isn't located, the default `security.properties` file is looked for in the classpath or in the classpath under `config/security`.

#### Grid - Security Configuration file

The default Grid components security configuration file is `grid-security.properties`. It can be located anywhere in the classpath or in the classpath under `config/security`. Here you can declare different configurations that will affect all the Grid components (such as GSA, GSM, GSC) together.

#### Processing Unit - Security Configuration file

The default Processing Unit (Space) security configuration file is `/META-INF/spring/pu.properties`. This is equivalent to custom properties being passed to the Space. This allows you to configure different configurations for different Processing Units. Thus, having a Processing Unit Cluster point to a specific security directory.

It is possible to separate the security configurations from the Processing Unit configurations, by placing the configurations in `/META-INF/spring/<Space name>-security.properties` file.

#### Space - Security Configuration file

For a standalone space, the default Space security configuration file is `<Space-name>-security.properties`. Just like the `pu.properties`, you can include the security properties as part of the custom properties being passed to the space `"/./space?properties=myCustomProps`.

#### Configure using a System property

An alternative to the component's security property file, is the System Property override.


```java
-Dcom.gs.security.properties-file = my-security.properties
```

By setting `-Dcom.gs.security.properties-file` the property file will be located as a direct path, a resource in the classpath or in the classpath under `config/security`.

Since System Properties are JVM level, any Processing Unit deployed within a GSC will also benefit from this configuration. But, if security configurations were provided as part of the `pu.properties` they will be used instead.

