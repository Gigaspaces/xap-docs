---
type: post122
title:  Securing Components
categories: XAP122SEC, PRM
parent: none
weight: 800
canonical: auto
---



Each major component in the XAP data grid (Grid Service Agent, Grid Service Manager, Grid Service Container, and Processing Unit) has its own [security configuration](./security-configurations-ext.html) that can be adapted individually to suit the needs of your application environment. Security can also be enabled for other components and processes in the XAP environment. This section explains how security relates to each component, and how to configure the properties to properly secure your application.

You can apply security settings at the following levels:

- [Data](./securing-your-data.html) - You can secure the Space, Processing Unit, local cache/view, Space filters, task execution/executors, event-driven remoting, and the JDBC driver.
- [Service Grid](./securing-the-grid-services.html) - You can enable security for the Grid Service Agent (GSA), Grid Service Manager (GSM), and Grid Service Container (GSC).
- [Transport layer](./securing-the-transport-layer-using-ssl.html) - XAP contains a generic network filter that also provides SSL support via an SSL communication filter.

