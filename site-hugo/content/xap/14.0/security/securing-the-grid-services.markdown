---
type: post140
title: Securing the Grid Services
categories: XAP140SEC, PRM
parent: securing-xap-components.html
weight: 300
---



# Enabling Security

XAP security is enabled by setting a system property.
Append the following system property to `EXT_JAVA_OPTIONS` in `setenv` script.


```java
-Dcom.gs.security.enabled=true
```

This property affects the GSA, GSM, GSC.
Actually it affects also a standalone PU instance (with a space).

# Grid Service Agent



The [Grid Service Agent (GSA)](../overview/the-runtime-environment.html#gsa) acts as a process manager that can spawn and manage Service Grid processes (GSM, GSC, etc.). When GSA is secured, a spawned GSM/GSC will also be secured. To _start_, _kill_, _restart_ a process you need **Manage Grid** privileges.

# Grid Service Manager



The [Grid Service Manager (GSM)](../overview/the-runtime-environment.html#gsm) is responsible for managing Processing Units and Grid Service Containers. The GSM accepts deployment and un-deployment requests of Processing Units, and provisions them to GSCs. To _deploy_, _un-deploy_ you need **Provision PU** privileges.

A secured GSM can **only** connect to another secured GSM. This means that a Processing Unit managed by a secured GSM can be recoverable upon failover only by another secured GSM.

# XAP Manager

The [XAP Manager](../admin/xap-manager.html) is a component which stacks together the LUS and GSM along with ZooKeeper and a [RESTful Management API](../admin/xap-manager-rest.html). 
The privileges of the GSM are the same as if it were standalone. For each REST operation, we list the required privilege in the yaml file, also viewable in the Swagger tool (see [RESTful API](../admin/xap-manager-rest.html)). 
For example, starting a container would require **Manage Grid** privileges. Deploying a Space or a Processing Unit would require **Provision PU** privileges. 
For more information about using the RESTful API with security, refer to [REST Manager API - Security](../admin/admin-rest-manager-api.html#security).

## Elastic Processing Units

When an [Elastic Processing Unit](../dev-java/elastic-processing-unit-overview.html) is deployed, the GSM performs the initial provisioning and the *ESM* monitors it to ensure the elastic requirements are met. This means that when the service grid is secured, the ESM requires credentials with *Manage PU* and *Manage Grid* permissions to monitor and manage the elastic processing units. This is done using the `com.gs.esm.username` and `com.gs.esm.password` system properties.


# Grid Service Container


A [Grid Service Container (GSC)](../overview/the-runtime-environment.html#gsc) is a container which hosts deployed Processing Unit Instances, and reports its status to the GSM. In general, operations on the GSC are routed to the managing GSM of the processing unit. To _scale up/down_, _relocate_, _restart PU instance_, _destroy PU instance_ you need **Manage PU** privileges.

A secured GSC can **only** connect to a secured GSM. This means that a Processing Unit managed by a secured GSM can only be provisioned to a secured GSC.

{{%note%}}
Security of a GSC does not enforce any security on the deployed Processing Unit. i.e., a secured GSC can contain a Processing unit with a non-secured space, and a non-secured GSC can contain a Processing Unit with a secured space.
{{%/note%}}

