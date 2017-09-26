---
type: post122
title: Getting Started
categories: XAP122SEC, OSS
parent: none
weight: 150
---

{{% ssummary %}}{{% /ssummary %}}

This topic provides a high-level explanation of the XAP security mechanisms, so you can start working with them quickly.

# Layers of Security

XAP security can be applied in three separate layers:

* The [data layer](securing-your-data.html), which includes Space and Processing Units.
* The [grid layer](securing-the-grid-services.html), which includes grid components (GSA, GSM, GSC, XAP Manager + RESTful API).
* The [transport layer](securing-the-transport-layer-using-ssl.html), which provides a generic network filter with SSL support.

You may secure any of these, or all of them for maximum security.

# Configuring Security

Security is configured using a property file that includes required and custom properties. For more information, refer to the following parts of the Security section in this Administration guide:

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
The [Hello World](securing-the-helloworld-example.html) example will guide you step by step into deploying Processing-Unit with a secured Space, accessing it from a remote proxy, and declaring principals using the administration tools.


## Spring Security

Spring Security is one of the most mature and widely used Spring projects. We provide a Spring-based security bridge to enable a pluggable implementation into various authentication sources. One such example is with LDAP (Lightweight Directory Access Protocol).

For more information, refer to [Spring Security Bridge](spring-security-bridge.html).
