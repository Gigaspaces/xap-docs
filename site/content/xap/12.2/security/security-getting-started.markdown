---
type: post122
title:  Security Getting Started
categories: XAP122SEC, OSS
parent: none
weight: 150
---

{{% ssummary %}}{{% /ssummary %}}

This section will get you started quickly with XAP security mechanism.

# Layers of Security

The security can be applied in three separate layers. 
- The [data layer](securing-your-data.html) that includes Space and Processing Units.
- The [grid layer](securing-the-grid-services.html) that includes grid components (GSA, GSM, GSC, XAP Manager + RESTful API).
- The [transport layer](securing-the-transport-layer-using-ssl.html) that provides a generic network filter with SSL support.

You may secure either or all for maximum security.

# Configuring Security

Security is configured using a property file that includes required and custom properties.
For more information see the following sections on:
- [Space and Processing Unit](security-configurations.html) configuration options
- [Grid components](security-configurations-ext.html) configuration options

# Enabling Security

Security can be enabled separately for the data layer and the grid layer.
- The [data layer](securing-your-data.html) has options to declare a secured Space or Processing Unit.
- The [grid layer](securing-the-grid-services.html) has options to declare the Grid as secured.

# Security implementations

When no property file is configured, there is a fallback implementation mainly used for testing or to get you started.
It stores the user credentials in a local file and can be used to demo that security is applied.

To read more about the file-based configuration options, see [default security implementation](default-file-based-security-implementation-ext.html).

To write your own implementation, see the [reference implementation](security-ref-impl.html) that uses MongoDB.

For other custom security implementations see the [Custom Security](custom-security.html) section. One such custom security implementation is the [Spring Security Bridge](spring-security-bridge.html).

{{%note%}}
The Open-Source version does not include a security implementation.
A [reference implementation](security-ref-impl.html) is provided instead.
{{%/note%}}


# Hello World Example
The [Hello World](securing-the-helloworld-example.html) example will guide you step by step into deploying Processing-Unit with a secured Space, accessing it from a remote proxy, and declaring principals using the administration tools.


# Spring Security

Spring Security is one of the most mature and widely used Spring projects. We provide a Spring-based security bridge to enable a pluggable implementation into various authentication sources. One such example is with LDAP (Lightweight Directory Access Protocol).

For more information, see [Spring Security Bridge](spring-security-bridge.html).
