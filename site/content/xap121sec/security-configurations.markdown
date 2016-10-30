---
type: post121
title:  Configuration
categories: XAP121SEC, OS
parent: none
weight: 500
---

{{% ssummary %}}{{% /ssummary %}}


Each Space/Processing Unit can have it's own security configuration. As such, you can have different security behaviors, but there is one common concept: all configurations are declared using properties (either located in a properties file or part of the components properties).

When a secured component is started, it looks for the security configuration properties in order to instantiate the security implementation.

```java
public interface SecurityManager {
    ...
    void init(Properties properties) throws SecurityException;
}
```

## Security Properties File

The `security.properties` file is looked for in the classpath or in the classpath under `config/security`.
If all of the components will be requiring the same security configuration, then this is all you need.

Commonly placed under:

```java
<XAP root>/config/security/security.properties
```

This file must include at least one defined property - `com.gs.security.security-manager.class` specifying the `SecurityManager` implementation class.
For example:
```java
com.gs.security.security-manager.class=my.company.MySecurityManagerImpl
```

#### Space - Security Configuration file

For a standalone space, the default Space security configuration file is `<space-name>-security.properties`. You can include the security properties as part of the custom properties being passed to the space `"/./space?properties=myCustomProps`.


#### Space Processing Unit - Security Configuration file

The default Space Processing Unit security configuration file is `/META-INF/spring/pu.properties`. This is equivalent to custom properties being passed to the Space. This allows you to configure different configurations for different Processing Units. Thus, having a Processing Unit Cluster point to a specific security directory.

It is possible to separate the security configurations from the Processing Unit configurations, by placing the configurations in `/META-INF/spring/<Space name>-security.properties` file.


#### Configure using a System property

An alternative is the System Property override.

```java
-Dcom.gs.security.properties-file=my-security.properties
```

By setting `-Dcom.gs.security.properties-file` the property file will be located as a direct path, a resource in the classpath or in the classpath under `config/security`.
