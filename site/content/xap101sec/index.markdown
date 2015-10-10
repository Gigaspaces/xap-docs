---
type: post101sec
title:  Security Guide
categories: XAP101SEC

---




{{% section %}}
{{% column  width="10%" %}}
![security.png](/attachment_files/subject/security.png)
{{% /column %}}

{{% column width="90%" %}}
This section provides an understanding of GigaSpaces XAP Security, where it fits in the GigaSpaces architecture, which components can be secured, and how to configure and customize the security depending on your application security requirements. XAP Security provides comprehensive support for securing your data, services, or both. XAP provides a set of authorities granting privileged access to data, and for performing operations on services.
{{% /column %}}
{{% /section %}}

<br>

{{%fpanel%}}

[Overview](./security.html)<br>
XAP Security provides comprehensive support for securing your data, services, or both. XAP provides a set of authorities granting privileged access to data, and for performing operations on services.

[Concepts](./security-concepts.html)<br>
Authentication, Encryption, Authenticity Validation, and Managing users and roles

[Authorities](./security-authorities.html)<br>
System, Grid, Space and Monitor authorities

[Configuration](./security-configurations.html)<br>
Configurations and defaults

[Directory Management](./programmatically-managing-the-security-directory.html)<br>
User and Role directory API

[File Based](./default-file-based-security-implementation.html)<br>
The default file-based users/roles directory; Overview, Getting Started, Configuration, and Custom Extensions

[Securing Components](./securing-xap-components.html)<br>
GigaSpaces XAP has security built over the major server component - GSA, GSM, GSC and also Processing Unit with Space data. This section explains how security relates to each component and the configurations needed to securing your application.

[Administration](./security-administration.html)<br>
This section explains the administration tools for managing XAP. The GUI provides a handy management tool for managing the users and roles, and comprehensive manageability of the secured components in the system.

[Hello World example](./securing-the-helloworld-example.html)<br>
securing-the-helloworld-example.html

[Custom Security](./custom-security.html)<br>
Customize security to meet your application requirements

[Spring Security](./spring-security-bridge.html)<br>
Declarative Spring-based custom security bridge

{{%/fpanel%}}