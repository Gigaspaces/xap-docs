---
type: post100
title:  Spring Security Bridge
categories: XAP100SEC
parent: none
weight: 900
---

<br>

Spring Security is one of the most mature and widely used Spring projects. In the following sections, we present a brief introduction to Spring Security and GigaSpaces Spring-based security bridge.

We will focus on a couple of the most commonly used authentication providers, such as database-oriented authentication and LDAP (Lightweight Data Access Protocol). For each, we provide a Spring XML security configuration, while demonstrating how GigaSpaces granted authorities should be stored.

The Spring-based security bridge leverages our [Custom Security](./custom-security.html) support. The implementation class `org.openspaces.security.spring.SpringSecurityManager` is open source as part of the **OpenSpaces** distribution.

<br>

{{%fpanel%}}

[Overview](./introducing-spring-security.html){{%wbr%}}
Introduction Spring Security Framework.

[Spring based bridge](./gigaspaces-spring-based-security-bridge.html){{%wbr%}}
Introducing GigaSpaces Spring-based security bridge.

[Data base authentication](./authenticating-against-a-database.html){{%wbr%}}
Storing user details in a relational database using DaoAuthenticationProvider.

[Ldap authentication](./authenticating-against-an-ldap-repository.html){{%wbr%}}
Using LDAP (Lightweight Directory Access Protocol) with Spring Security.


{{%/fpanel%}}



