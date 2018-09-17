---
type: post110
title:  The Processing Unit
categories: XAP110
parent: none
weight: 2000
---


The Processing Unit is the unit of packaging and deployment in the GigaSpaces XAP platform. This section describes its directory structure, deployment descriptor and SLA definition and configuration.



<br>

{{%fpanel%}}

[Deployment Unit directory structure](./the-processing-unit-structure-and-configuration.html){{<wbr>}}
This section describes the Processing Unit directory structure.

[Processing Unit configuration](./configuring-processing-unit-elements.html){{<wbr>}}
This section describes the Processing Unit deployment configuration file (pu.xml).

[Obtaining Cluster information](./obtaining-cluster-information.html){{<wbr>}}
Obtaining information about the clustering topology, member id and other cluster related information can be useful in many cases. Cluster information can be provided to the processing unit instances at deployment time.


[Reloading business logic](./reloading-business-logic.html){{<wbr>}}
The service reloading feature allows you to reload business logic (Spring beans) without shutting down the application or undeploying a Processing Unit.


[Custom Processing Unit](./custom-processing-unit-details-and-monitors.html){{<wbr>}}
A Processing Unit can implement the ServiceDetailsProvider and ServiceMonitorsProvider interfaces to expose information to calling APIs or to be viewable in the web-UI.

[Deploying and Running](./deploying-and-running-overview.html){{<wbr>}}
This section describes the various options to debug and run your processing units.
{{%/fpanel%}}



