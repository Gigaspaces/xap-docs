---
type: post140
title:  The Processing Unit
categories: XAP140, OSS
parent: none
weight: 2000
---


The Processing Unit is the unit of packaging and deployment in the GigaSpaces XAP platform. This section describes its [directory structure](./the-processing-unit-structure-and-configuration.html) and how to set the values in the [configuration file](./configuring-processing-unit-elements.html).

You can [obtain cluster information](./obtaining-cluster-information.html) about the topology, member ID and other related parameters. Cluster information can be provided to the processing unit instances at deployment time. You can also [reload business logic](./reloading-business-logic.html) (Spring beans) without shutting down the application or undeploying a Processing Unit.

If you want to build a [Custom Processing Unit](./custom-processing-unit-details-and-monitors.html), you can can implement the ServiceDetailsProvider and ServiceMonitorsProvider interfaces to expose information to calling APIs, or to be viewable in the Web Managment Console.

In order to [deploy and run](./deploying-and-running-overview.html) the Processing Unit, you have to choose the appropriate runtime mode and possibly do some debugging. There are also a number of deployment properties that can be set.




