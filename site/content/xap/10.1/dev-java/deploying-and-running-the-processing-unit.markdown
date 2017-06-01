---
type: post101
title:  Overview
categories: XAP101
parent: deploying-and-running-overview.html
weight: 100
---



In production, your processing units run on the GigaSpaces service grid, taking advantage of its SLA and monitoring capabilities. However, when developing the application, you typically want to run it from within your IDE or in a more lightweight mode that will not require you to start a complete runtime environment.

The abstraction which enables to run the same processing unit in different modes (service grid, within your IDE, etc.) is called _Processing Unit Containers_.

Naturally, the [GigaSpaces Container](/product_overview/service-grid.html#gsc) implements its own processing unit container, allowing for processing units which are provisioned to it to run properly.

In addition, to facilitate easier development and debugging, GigaSpaces provides two more processing unit containers: The _Integrated Processing Unit Container_ allows you to debug and run your processing units within your IDE in an isolated easy to use mode. The integrated processing unit container is simply a class with a main method, that instantiates a Spring application context for your processing unit and makes sure other services (such [cluster information](./obtaining-cluster-information.html) is available to your code as if it was running in a real cluster.

Similarly, the standalone processing unit container also runs your processing units in a simple isolated environment, allowing you to start and stop it quickly to avoid the overhead of starting a full-blown cluster.

Note that both the integrated processing unit container and the standalone one run the processing unit as a standalone Java process and therefore cannot enforce any of the [SLA requirements]({{%currentadmurl%}}/the-sla-overview.html)  of the processing unit. Their primary intention is to enable you to debug and unit test your processing units.

# Choosing the Correct Runtime Mode

The following table summarizes when to use each of the runtime modes


| You Would Like to... | Runtime Mode | Processing Unit Container Name |
|:---------------------|:-------------|:-------------------------------|
|  Unit test your processing unit within your IDE | [Embedded in IDE](./running-and-debugging-within-your-ide.html) | Integrated Processing Unit Container |
| Unit test your processing unit or run it in an unmanaged environment | [Standalone](./running-in-standalone-mode.html) | Standalone Processing Unit Container |
| Run your processing unit in production or to conduct full-blown integration tests | [Managed by the Service Grid](./deploying-onto-the-service-grid.html) | Service Grid Processing Unit Container |


