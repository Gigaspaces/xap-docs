---
type: post102sec
title:  Overview
categories: XAP102SEC
parent: none
weight : 100
---


{{% section %}}
{{% column width="80%" %}}
GigaSpaces Security provides comprehensive support for securing your data, services, or both. GigaSpaces provides a set of authorities granting privileged access to data, and for performing operations on services.
{{% /column %}}
{{% column width="20%" %}}
{{%align center%}}
{{%popup   "/attachment_files/security_ovreview.jpg"%}}
{{%/align%}}
{{% /column %}}
{{% /section %}}


Security comprises two major operations: **authentication** and **authorization**.

**Authentication** is the process of establishing and confirming the authenticity of a _principal_. A _principal_ in XAP terms, means a user (human or software) performing an action in your application.

**Authorization** refers to the process of deciding whether a principal is allowed to perform this action. The flow means that a principal is first established by the authentication process, and then authorized by the authorization decision process, when performing actions. These concepts are common and not specific to XAP Security.

At the authentication level, XAP Security is equipped with standard encryption algorithms (such as AES and MD5), which can be easily configured and replaced. The authentication layer is provided with a default implementation, which can be customized to integrate with other security standards (i.e. Spring Security). This layer is also known as the **authentication manager**.
The authentication layer is totally independent from the authorization decision layer. The **authorization decision manager** is internal to XAP components, and is used to intercept unauthorized access/operations to data and services.

XAP Security architecture has been designed to meet the needs of enterprise application security. XAP provides a complete experience throughout all the components, for a useful, configurable and extendable security system.

**XAP Main Security Features:**

- Authority roles based security
    - System , Monitor , Grid , Data-Grid Authorities

- Spring Based Security support
    - LDAP Authenticating
    - Database Authenticating

- Custom Security
    - Kerberos Authenticating

- Data-Grid operations Auditing

- SSL Transport Layer security

- UI, CLI and API Security Tools




