---
type: post120
title:  Overview
categories: XAP120SEC, OS
parent: none
weight : 100
---



GigaSpaces Security provides comprehensive support for securing your data, services, or both. GigaSpaces provides a set of authorities granting privileged access to data, and for performing operations on services.

Security comprises of **authentication** and **authorization**. These concepts are not specific to XAP Security. 

**Authentication** is the process of establishing and confirming the authenticity of a _principal_. A _principal_ in XAP terms, means a user (human or software) performing an action in your application. XAP's authentication layer is equipped with standard encryption algorithms (such as AES and MD5), and can be customized to integrate with other security standards (i.e. Spring Security). 

**Authorization** refers to the process of deciding whether a principal is allowed to perform this action. A principal is first established by the authentication process, and then authorized by the authorization decision process, when performing actions. The authorization layer is used to intercept unauthorized access to data and restrict operations. It also maps users to specific authorities and allows grouping of authorities into roles.

XAP Security architecture has been designed to meet the needs of enterprise application security. XAP provides a complete experience throughout all the components, for a useful, configurable and extend-able security system.


**XAP Main Security Features:**

- Authority and Roles
    - Data access privileges
    - Operation privileges
    - Monitoring privileges
    - System administration privileges

- Data-Grid operations Auditing

- SSL Transport Layer security

**Extensions:** _(available in XAP Premium/Enterprise)_

- Spring Based Security support
    - LDAP Authenticating
    - Database Authenticating

- Administration tools
    - User intefaces: gs-ui, web-ui, gs CLI
    - Admin API