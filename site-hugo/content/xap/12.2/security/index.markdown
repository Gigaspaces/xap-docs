---
type: post122
title:  Security
categories: XAP122SEC, OSS
weight: 1250
---

This section provides an understanding of GigaSpaces Security features, where they fit in the GigaSpaces architecture, which components can be secured, and how to configure and customize the security depending on your application security requirements. XAP Security provides comprehensive support for securing your data, services, or both. GigaSpaces provides a set of authorities granting privileged access to data, and for performing operations on services.


{{%note "Note"%}}
Some security features are part of the open source edition, while others are only available with the commercial (licensed) editions.
{{%/note%}} 


# Dependencies

In order to use this feature, include the `${XAP_HOME}/lib/optional/security/xap-security.jar` file on your classpath or use maven dependencies:

```xml
<dependency>
    <groupId>com.gigaspaces</groupId>
    <artifactId>xap-security</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
```
{{%refer%}}
For more information on dependencies, refer to [Maven Artifacts](../started/maven-artifacts.html).
{{%/refer%}} 

# Main Security Features

The features listed below are available with the open-source editions of GigaSpaces products.

- Authority and Roles
    - Data access privileges
    - Operation privileges
    - Monitoring privileges
    - System administration privileges

- Data-Grid operations Auditing

- SSL Transport Layer security

# Extensions

The features listed below are available with the commercial licensed editions of GigaSpaces products.

- Spring Based Security support
    - LDAP Authenticating
    - Database Authenticating

- Administration tools
    - User intefaces: gs-ui, web-ui, gs CLI
    - Admin API

# Layers of Security

XAP security can be applied in three separate layers:

* [Data layer](securing-your-data.html) - Includes Spaces and Processing Units.
* [Grid layer](securing-the-grid-services.html)- Includes grid components (GSA, GSM, GSC, XAP Manager + REST Manager API).
* [Transport layer](securing-the-transport-layer-using-ssl.html) - Provides a generic network filter with SSL support.

You can secure any of these, or all of them for maximum security.

# Configuring Security

Security is configured via a property file that includes required and custom properties. For more information, refer to the following parts of the Security section in this Administration guide:

* [Space and Processing Unit](security-configurations.html) configuration options
* [Grid components](security-configurations-ext.html) configuration options

# Enabling Security

Security can be enabled separately for the data layer and the grid layer.

* The [data layer](securing-your-data.html) has options to declare a secured Space or Processing Unit.
* The [grid layer](securing-the-grid-services.html) has options to declare the Grid as secured.

# Security Implementations for Licensed GigaSpaces Products

{{%note "Note"%}}
The Open Source editions do not include a security implementation. A [reference implementation](security-ref-impl.html) is provided instead.
{{%/note%}}

When no property file is configured, there is a fallback implementation mainly used for testing or to get you started.
This implementation stores the user credentials in a local file and can be used to demonstrate that security has been applied.

Refer to the following sections for more information about XAP security implementation:

* To read more about the XAP file-based configuration options, refer to [Default File-Based Security](default-file-based-security-implementation-ext.html).
* To write your own implementation, refer to [Reference Implementation](security-ref-impl.html), which uses MongoDB.
* For other custom security implementations, refer to [Custom Security](custom-security.html). One such custom security implementation is the [Spring Security Bridge](spring-security-bridge.html).

## Hello World Example
The [Hello World](securing-the-helloworld-example.html) example provides a step-by-step guide to deploying a Processing Unit with a secured Space, accessing it from a remote proxy, and declaring principals using the administration tools.


## Spring Security

Spring Security is one of the most mature and widely used Spring projects. GigaSpaces provides a Spring-based security bridge to enable an extensible implementation to various authentication sources. One such example is with LDAP (Lightweight Directory Access Protocol).

For more information, refer to [Spring Security Bridge](spring-security-bridge.html).
