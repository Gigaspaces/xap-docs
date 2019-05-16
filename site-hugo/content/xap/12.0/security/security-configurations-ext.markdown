---
type: post120
title:  Component Configuration
categories: XAP120SEC, PRM
parent: securing-xap-components.html
weight: 100
canonical: auto
---

{{% ssummary %}}{{% /ssummary %}}


The security components (GSA, GSM, GSC and Processing Unit) can each have their own security configuration. As such, you can have different security behaviors even between Processing Units. But, all have one common concept: all configurations are declared using properties (either located in a properties file or part of the components properties).

The security implementation dictates the properties needed for configuration. For example, if you consider the out-of-the-box file-based security implementation, you can either rely on the defaults or configure the file location, password encoders, etc.

When a secured component is started, it looks for the security configuration properties in order to instantiate the security implementation.

## Logging

Any configurations that are applied can be seen by setting the logging level to CONFIG (see `xap_logging.properties`):


```java
com.gigaspaces.security.level = CONFIG
```

## Security Properties File

The security properties file is used to load the and configure the `SecurityManager`. Each component can be configured with it's own file or all can use a common file located at `<XAP root>/config/security/security.properties`.

A security properties file is first located by using a direct path. If it doesn't exist, it is looked for in the classpath or in the classpath under `config/security/`. As last resort, If the **component's** file isn't located, the default `security.properties` file is looked for in the classpath or in the classpath under `config/security`.

#### Grid - Security Configuration file

The default Grid components security configuration file is `grid-security.properties`. It can be located anywhere in the classpath or in the classpath under `config/security`. Here you can declare different configurations that will affect all the Grid components (such as GSA, GSM, GSC) together.

#### Processing Unit - Security Configuration file

The default Processing Unit (Space) security configuration file is `/META-INF/spring/pu.properties`. This is equivalent to custom properties being passed to the Space. This allows you to configure different configurations for different Processing Units. Thus, having a Processing Unit Cluster point to a specific security directory.

It is possible to separate the security configurations from the Processing Unit configurations, by placing the configurations in `/META-INF/spring/<Space name>-security.properties` file.

#### Space - Security Configuration file

For a standalone space, the default Space security configuration file is `<space-name>-security.properties`. Just like the `pu.properties`, you can include the security properties as part of the custom properties being passed to the space `/./space?properties=myCustomProps`.

#### Configure using a System property

An alternative to the component's security property file, is the System Property override.


```java
-Dcom.gs.security.properties-file = my-security.properties
```

By setting `-Dcom.gs.security.properties-file` the property file will be located as a direct path, a resource in the classpath or in the classpath under `config/security`.

Since System Properties are JVM level, any Processing Unit deployed within a GSC will also benefit from this configuration. But, if security configurations were provided as part of the `pu.properties` they will be used instead.

