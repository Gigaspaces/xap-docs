---
type: post122
title:  Overview
categories: XAP122SEC, OS
parent: none
weight : 100
---



XAP Security provides comprehensive support for securing your data, services, or both. GigaSpaces provides a set of authorities granting privileged access to data, and for performing operations on services.


**Dependencies**<br>
In order to use this feature, include the `${XAP_HOME}/lib/optional/security/xap-security.jar` file on your classpath or use maven dependencies:

```xml
<dependency>
    <groupId>com.gigaspaces</groupId>
    <artifactId>xap-security</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
```
{{%refer%}}
For more information on dependencies see [Maven Artifacts]({{%currentjavaurl%}}/maven-artifacts.html)
{{%/refer%}} 

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
