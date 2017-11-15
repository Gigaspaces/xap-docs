---
type: post123
title:  Security
categories: XAP123SEC, OSS
weight: 1250
---



This section provides an understanding of GigaSpaces Security features, where they fit in the GigaSpaces architecture, which components can be secured, and how to configure and customize the security depending on your application security requirements. XAP Security provides comprehensive support for securing your data, services, or both. GigaSpaces provides a set of authorities granting privileged access to data, and for performing operations on services.


{{%note "Note"%}}
Some security features are part of the open source edition, while others are only available with the commercial (licensed) editions.
{{%/note%}} 

# Security Infrastructure for Open Source Editions

The GigaSpaces applictions come with a basic security infrastructure that can be configured to suit the needs of your organization. You can secure the [data layer](securing-your-data.html), which includes the Spaces and Processing Units, by defining the following elements for user authorities and roles:
 
- Data access privileges
- Operation privileges
- Monitoring privileges
- System administration privileges

You can also configure auditing for operations on the data grid, and secure the SSL transport layer.

The open source editions do not include a security implementation. A [reference implementation](security-ref-impl.html) is provided instead.


# Security Implementations for Commercial Editions

The commercial editions of XAP and InsightEdge come with an Admin API that can be used for administration and monitoring. Additionally, the following interfaces can also be used:

- GigaSpaces Management Center
- Web Management Console
- Command Line Interface

The administration and monitoring tools interact with the application layers as follows.

{{% align center%}}
![layers-of-security.png](/attachment_files/security/layers-of-security.png)
{{% /align%}}

{{%refer%}}
Refer to [Administration and Management Tools](security-administration.html) for details about the configuration options.
{{%/refer%}} 

## Securable Layers

XAP security can be applied in three separate layers:

* The [Data layer](securing-your-data.html) - Includes Spaces and Processing Units.
* The [Grid layer](securing-the-grid-services.html) - Includes  grid components (GSA, GSM, GSC, XAP Manager).
* The [Transport layer](securing-the-transport-layer-using-ssl.html) - Provides a generic network filter with SSL support.

You can secure any of these, or all of them for maximum security.

## Configuring Security

Security is configured via a property file that includes required and custom properties. For more information, refer to the following parts of the Security section in this Administration guide:

* [Space and Processing Unit](security-configurations.html) - Includes configuration options for the properties file.
* [Grid components](security-configurations-ext.html) - Includes configuration options for the grid components.
* [Web Management Console](securing-the-web-ui.html) - Includes configurations for authenticating with secure XAP components.
* [REST Manager API](securing-the-REST-manager.html) - Includes security and SSL configurations for secured RESTful API support.  

## Enabling Security

Security can be enabled separately for the data layer and the grid layer.

* The [Data layer](securing-your-data.html) has options to declare a secured Space or Processing Unit.
* The [Grid layer](securing-the-grid-services.html) has options to declare the Grid as secured.

## Out-of-the-Box Functionality

When no property file is configured, there is a fallback implementation mainly used for testing or to get you started.
This implementation stores the user credentials in a local file and can be used to demonstrate that security has been applied.

Refer to the following sections for more information about XAP security implementation:

* To read more about the XAP file-based configuration options, refer to [Default File-Based Security](default-file-based-security-implementation-ext.html).
* To write your own implementation, refer to [Reference Implementation](security-ref-impl.html), which uses MongoDB.
* For other custom security implementations, refer to [Custom Security](custom-security.html). One such custom security implementation is the [Spring Security Bridge](spring-security-bridge.html).

## Dependencies

In order to use the security implementation, include the `${XAP_HOME}/lib/optional/security/xap-security.jar` file on your classpath or use Maven dependencies:

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


## Hello World Example
The [Hello World](securing-the-helloworld-example.html) example provides a step-by-step guide to deploying a Processing Unit with a secured Space, accessing it from a remote proxy, and declaring principals using the administration tools.


## Spring Security

Spring Security is one of the most mature and widely used Spring projects. GigaSpaces provides a Spring-based security bridge to enable an extensible implementation to LDAP (Lightweight Directory Access Protocol) or database authentication.

{{% align center%}}
![spring-based-security.png](/attachment_files/security/spring-based-security.png)
{{% /align%}}

For more information, refer to [Spring Security Bridge](spring-security-bridge.html).