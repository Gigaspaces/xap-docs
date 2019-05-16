---
type: post100
title:  Custom Security
categories: XAP100SEC
parent: none
weight: 800
canonical: auto
---


GigaSpaces XAP security was designed with customization in mind - since there are numerous security standards and practices, users can either use the built in security features in the product or customize them to their own needs.

The following security aspects can be customized:

- [Authentication](./custom-authentication.html) - Cuztomize how servers authenticate the clients which access them.
- [User/Role Management](./custom-user-role-management.html) - Customize creation and management of users and roles.

{{% info %}}
This section assumes that you are familiar with the [Security Basics](./security-concepts.html), and more specifically with [Security Configurations](./security-configurations.html).
Before implementing custom security from scratch, consider the following alternatives:
{{%/info%}}

- Extending the [Default File-Based Security Implementation](./default-file-based-security-implementation.html) provided with the product (Supports replacing the encoding, referencing a security file on an HTTP server, and more).
- Using or extending the [Spring Security Bridge](./spring-security-bridge.html).

# Packaging and Classpath

The most common scenario is for all services to share the **same** custom security. This is easily accomplished by placing the custom implementation classes in the `lib/optional/security` directory.

{{% info %}}
You can use a different directory by configuring the `com.gigaspaces.lib.opt.security` system property.
{{%/info%}}


```java
<XAP root>/lib/optional/security/my-custom-security.jar
```

Processing units may share a custom security implementation that may +differ+ from that of the GSM and GSCs. In this case, the custom security jar can be placed under `pu-common`.


```java
<XAP root>/lib/optional/pu-common/my-pu-custom-security.jar
```

If each processing unit has its **own** custom security implementation, the custom security jar can be part of the processing unit distribution.


```java
<XAP root>/deploy/hello-processor/lib/my-processor-custom-security.jar
```

{{% info %}} It is recommended that the custom security jar should only contain security-related classes. {{%/info%}}


