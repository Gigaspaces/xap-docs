---
type: post123
title:  Administration
categories: XAP123SEC, PRM
parent: none
weight: 1000
---

{{% ssummary %}}{{% /ssummary %}}

Administration and monitoring XAP can be done using the following tools:

* Command-line Interface (CLI), 
* GigaSpaces Management Center (Desktop UI), 
* Administration API (Admin API),
* Web Management Console (Web-UI),
* REST Manager API

When XAP is secured, the interaction with each is first done by supplying credentials to allow proper authentication.

# Command-line Interface

The command-line interface can be used for script automation (e.g. deploy, undeploy, list, etc.)
It has both an interactive and non-interactive modes that allow secure login. 
The non-interactive mode can be used for script automation.
[Read more...](command-line-interface-cli-security.html)


# GigaSpaces Management Center
The desktop user-interface can be used to monitor and manage the grid services and data. 
It can also be used to manage users and roles, assign permissions, manage security using the directory manager extension.
[Read more...](../admin/gigaspaces-management-center-ui-security.html)

# Administration API

The Administration API has the full power to manage and monitor the grid components using a Java API.
To allow the discovery of secured components, it must authenticate with each. 
Un-authenticated components will not be listed in the API.
[Read more...](administration-and-monitoring-api-security.html)


# Web Management Console

The Web-based user-interface can be used to monitor and manage the grid services and data.
The Web-UI server *must* be configured to allow the authentication using the login screen.
[Read more...](securing-the-web-ui.html)


# REST Manager API

The REST server, an integrated component of the [XAP Manager](../admin/xap-manager.html), 
performs authentication and checks for sufficient privileges when executing RESTful operations.
Being an HTTP protocol, you must explicitly enable or disable SSL.
[Read more...](securing-the-REST-manager.html)

# Auditing

Auditing can be done for authentication requests to secured services, and also for Space operations.
[Read more...](./auditing.html) 

# Java Security Policy

You can use the default settings of the `java.security.policy` property, 
or customize them to suit your application and environment needs.
[Read more...](./java-security-policy-file.html)

