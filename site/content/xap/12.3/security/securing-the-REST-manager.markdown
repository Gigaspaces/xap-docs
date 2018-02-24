---
type: post123
title: REST Manager API
categories: XAP123SEC, PRM
parent: security-administration.html
weight: 350
---



# Enabling Security

The REST Manager API allows for secured access and operations when the security `enabled` property is set.
This property should be configured using `EXT_JAVA_OPTIONS` in `setenv` script and is applied to all XAP [Grid Components](securing-the-grid-services.html).

The property:
```java
-Dcom.gs.security.enabled=true
```

By default, to get you up and running, if nothing was configured the fallback security implementation 
uses a local file to save credentials (see [File-Based Security](../security/default-file-based-security-implementation-ext.html).

When security is enabled, the REST Manager API performs basic authentication and checks if the user has sufficient privileges 
to execute the operation.

# SSL Configuration

{{%note "Note"%}}
Basic authentication does not encrypt user credentials, so running a XAP Manager in a secure environment without SSL is a security hazard; 
The system detects this and aborts. You must **explicitly** enable or disable the SSL by setting the system property `com.gs.manager.rest.ssl.enabled`.
{{%/note%}}

You can either:

- Disable SSL explicitly (not recommended).
- Enable SSL, and the system will generate a certificate for you.
- Enable SSL and provide a trusted certificate that you own.

An auto-generated certificate provides reasonable security, but if your enterprise security guidelines are more 
strict you can provide your own certificate.

Finally, if you need to configure something that we don’t expose (we use Jetty under the hood to host the web app), 
you can provide your own jetty.xml file via a system property.


|Port |System Property |Default |
|:----|:---------------|:-------|
|Enable/disable |com.gs.manager.rest.ssl.enabled| must be explicitly set |
|Keystore path  |com.gs.manager.rest.ssl.keystore-path | |
|Keystore password|com.gs.manager.rest.ssl.keystore-password| |
|Custom config |com.gs.manager.rest.jetty.config|  |


# Security Properties File

The security properties file is used to configure the `SecurityManager`, that is responsible for the authentication and authorization process.
The `security.properties` file is common to all components and is usually located under `<XAP root>/config/security/security.properties`.

The REST component is part of the XAP Grid components.
To only affect the Grid components, use the `grid-security.properties` instead.

The configuration file can be located anywhere in the classpath or in the classpath under `config/security`.

Alternatively, a system property can be set to indicate the location of the properties file: 

```java
-Dcom.gs.security.properties-file = my-security.properties
```

By setting `-Dcom.gs.security.properties-file` the property file will be located as a direct path (e.g. `~/home/user/my-security.properties`), 
a resource (e.g. "my-security.properties") in the classpath or in the classpath under `config/security`.
