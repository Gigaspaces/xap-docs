---
type: post
title:  Service Grid
weight: 100
parent: admin-tools-overview.html
---



# GS Agent 

|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| manager |Starts a XAP Manager, which bundles gsm, lus, zookeeper and a REST service (Starting more than one on the same host is not supported) Requires configuring a list of 1 or 3 manager servers using the XAP_MANAGER_SERVERS environment variable (see setenv-overrides script located in \<XAP-HOME\>/bin) |No|[REST Manager](/xap/12.3/admin/xap-manager-rest.html)|
| <nobr>manager-local<nobr> | Starts a standalone XAP Manager on the local machine | No | [REST Manager](/xap/12.3/admin/xap-manager-rest.html)|
| gsc=N  | Starts N Grid Service Containers (if =N is not specified, defaults to 1) |No||
| gsm=N   |Starts N Grid Service Managers (if =N is not specified, defaults to 1) |No||
| lus=N  | Starts N LookUp Services (if =N is not specified, defaults to 1) |No||
| esm      |Starts Elastic Service Manager (starting more than 1 is not supported)|No||
| <nobr>z, --zero-defaults <nobr> || No ||
| webui   |Launch the Web Management Console | No | [Web Management Console](/xap/12.3/admin/web-management-console.html)|


 
 
Shutting down the GS-Agent (advanced)<br>
Viewing Host Information (includes containers on the host)<br>
Starting a Container<br>
Terminating a Container<br>
Restarting a Container<br>
Viewing Container Information<br>
Viewing Logs<br>
Viewing Alerts<br>
Viewing Events<br>
Generating Dump Files