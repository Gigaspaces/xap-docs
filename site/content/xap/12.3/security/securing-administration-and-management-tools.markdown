---
type: post123
title:  Administration and Management Tools
categories: XAP123SEC, PRM
parent: securing-xap-components.html
weight: 500
---

{{% ssummary %}}{{% /ssummary %}}

Monitoring and managing XAP can be done using the following tools:

* Command-line Interface (CLI), 
* Management Center (Desktop UI), 
* Web Management Console (Web-UI), 
* Administration API (Admin API),
* RESTful API

When XAP is secured, the interaction with each is first done by supplying credentials to allow proper authentication.

# Command-line Interface

The command-line interface can be used for script automation (e.g. deploy, undeploy, list, etc.)

See [Administration and Monitoring](command-line-interface-cli-security.html) section.


# Management Center
The desktop user-interface can be used to monitor and manage the grid services and data. 
It can also be used to manage users and roles, assign permissions, manage security using the directory manager extension.

See [Administration Guide - Security](../admin/gigaspaces-management-center-ui-security.html) section.


# Web Management Console

The Web-based user-interface can be used to monitor and manage the grid services and data.
The Web-UI server *must* be configured to allow the authentication using the login screen.

See [Securing components](securing-the-web-ui.html) section.


# Administration API

The Administration API has the full power to manage and monitor the grid components using a Java API.
To allow the discovery of secured components, it must authenticate with each. Un-authenticated components will not be listed in the API.

See [Administration and Monitoring](administration-and-monitoring-api-security.html) section.


# RESTful API

The REST server, an integrated component of the [XAP Manager](../admin/xap-manager.html), 
performs authentication and checks for sufficient privileges when executing RESTful operations.
Being an HTTP protocol, you must explicitly enable or disable SSL.

See [REST Manager API](../admin/xap-manager-rest-overview.html#security) section.