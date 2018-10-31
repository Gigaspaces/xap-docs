---
type: post140
title:  Securing Components
categories: XAP140SEC, PRM
parent: none
weight: 800
---



Each major component in XAP (Data-grid, Grid Service Agent, Grid Service Manager, Grid Service Container, Processing Unit, and Web Management Console) 
has its own security configuration that can be adapted individually to suit the needs of your application environment. 
Security can also be enabled for other components and processes in the XAP environment. 
This section explains how security relates to each component, and how to configure the properties to properly secure your application.

You can apply security settings at the following levels:

- [Data](./securing-your-data.html) - You can secure the Space, Processing Unit, local cache/view, Space filters, task execution/executors, event-driven remoting, and the JDBC driver.
- [Service Grid](./securing-the-grid-services.html) - You can enable security for the Grid Service Agent (GSA), Grid Service Manager (GSM), and Grid Service Container (GSC).
- [Transport layer](./securing-the-transport-layer-using-ssl.html) - XAP contains a generic network filter that also provides SSL support via an SSL communication filter.

To administer and manage these components, set security configurations for:

- [Web Management Console](../admin/tools-web-ui.html#security) - using a secured Web-UI server.
- [REST Manager API](../admin/admin-rest-manager-api.html#security) - using a secured RESTful API.

