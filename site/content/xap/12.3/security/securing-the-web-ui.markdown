---
type: post123
title: Web Management Console
categories: XAP123SEC, PRM
parent: security-administration.html
weight: 300
---



# Enabling Security

The Web Management Console (Web-UI) allows for a secured access when the security `enabled` property is set.
This property should be configured using `EXT_JAVA_OPTIONS` in `setenv` script and is applied to all XAP [Grid Components](securing-the-grid-services.html).

The property:
```java
-Dcom.gs.security.enabled=true
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

# Custom Credentials

An authentication mechanism might require a different set of actions to be taken on the provided credentials (username/password).
For that a custom extension can be provided - see [Credentials Provider](custom-authentication.html) for more information.

This credentials provider is configured as arguments to the command line of the Web-UI.
Use `-user-details-provider` for the provider class name.
Use `-user-details-properties` to provide additional properties. This argument is optional.

Run the `gs-webui` script with these parameters:

```bash
gs-webui(.sh/bat) -user-details-provider com.demo.CustomCredentialsProvider -user-details-properties custom-security.server-address=myServer
```

