---
type: post123
title:  Custom Security
categories: XAP123SEC, PRM
parent: none
weight: 850
canonical: auto
---

{{% note "Note"%}}
This section assumes that you are familiar with [basic security concepts](./security-concepts.html), and with XAP-specific [security configurations](./security-configurations.html). Before implementing custom security from scratch, consider the following alternatives:

- Extending the [default file-based security implementation](./default-file-based-security-implementation-ext.html) that is already provided with XAP, which supports replacing the encoding, referencing a security file on an HTTP server, and more.
- Using or extending the [Spring Security Bridge](./spring-security-bridge.html).
{{%/note%}}

GigaSpaces security was designed with customization in mind. There are numerous security standards and practices, so users can implement the built-in security features out of the box, or customize them to suit the needs of the industry and environment.

You can customize the security protocols for the following:

- [Authentication](./custom-authentication.html) - How servers authenticate the clients that access them.
- [User/Role Management](./custom-user-role-management.html) - Creation and management of users and roles.



# Packaging and Classpath

The most common scenario is for all services to share the **same** custom security. This is easily accomplished by placing the custom implementation classes in the `lib/optional/security` directory.

{{% note "Note"%}}
You can use a different directory by configuring the `com.gigaspaces.lib.opt.security` system property.
{{%/note%}}


```java
<XAP root>/lib/optional/security/my-custom-security.jar
```

Processing Units can share a custom security implementation that may differ from that of the GSM and GSCs. In this case, the custom security JAR can be placed under `pu-common`.


```java
<XAP root>/lib/optional/pu-common/my-pu-custom-security.jar
```

If each Processing Unit has its **own** custom security implementation, the custom security JAR can be part of the Processing Unit distribution.


```java
<XAP root>/deploy/hello-processor/lib/my-processor-custom-security.jar
```

{{% note "Note"%}} 
We recommend that the custom security JAR contain only security-related classes. 
{{%/note%}}
