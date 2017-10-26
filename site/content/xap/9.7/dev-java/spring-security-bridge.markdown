---
type: post97
title:  Spring Security Bridge
categories: XAP97
parent: security-overview.html
weight: 800
---



Spring Security is one of the most mature and widely used Spring projects. In the following sections, we present a brief introduction to Spring Security and GigaSpaces Spring-based security bridge.

We will focus on a couple of the most commonly used authentication providers, such as database-oriented authentication and LDAP (Lightweight Data Access Protocol). For each, we provide a Spring XML security configuration, while demonstrating how GigaSpaces granted authorities should be stored.

The Spring-based security bridge leverages our [Custom Security](./custom-security.html) support. The implementation class `org.openspaces.security.spring.SpringSecurityManager` is open source as part of the **OpenSpaces** distribution.

{{% children %}}
