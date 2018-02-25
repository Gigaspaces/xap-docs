---
type: post123
title:  Deploying and Running
categories: XAP123, OSS
parent: the-processing-unit-overview.html
weight: 600
---


This section describes the various options for debugging and running your Processing Units.

{{% note "Note" %}}
Some of the features discussed here are not part of the open-source edition, and are only available in the licensed editions (starting with XAP Premium).
{{% /note %}}

In production, your Processing Units run on the XAP service grid, taking advantage of its SLA and monitoring capabilities. However, when developing your application, you typically want to run it from within your IDE or in a more lightweight mode that will not require you to start a complete runtime environment.

The abstraction that enables running the same Processing Unit in different modes (service grid, within your IDE, etc.) is called _Processing Unit Containers_.

Naturally, the [Grid Service Container](../overview/the-runtime-environment.html#gsc) implements its own Processing Unit container, allowing for Processing Units that are provisioned to it to run properly.

In addition, to facilitate easier development and debugging, GigaSpaces provides two more Processing Unit containers. The _Integrated Processing Unit Container_ allows you to debug and run your Processing Units within your IDE in an isolated, easy-to-use mode. The Integrated Processing Unit Container is simply a class with a main method, which instantiates a Spring application context for your Processing Unit and makes sure other services (such as [cluster information](./obtaining-cluster-information.html)) is available to your code as if it was running in a real cluster.

Similarly, the standalone Processing Unit container also runs your Processing Units in a simple isolated environment, allowing you to start and stop them quickly to avoid the overhead of starting a full-blown cluster.

Both the Integrated Processing Unit Container and the standalone Processing Unit run as a standalone Java process, and therefore cannot enforce any of the [SLA requirements](../admin/the-sla-overview.html) of the Processing Unit. Their primary purpose is to enable you to debug and unit test your Processing Units.

# Choosing the Correct Runtime Mode

The following table summarizes when to use each of the runtime modes.


| You Would Like to... | Runtime Mode | Processing Unit Container Name |
|:---------------------|:-------------|:-------------------------------|
|  Unit test your Processing Unit within your IDE | [Embedded in IDE](../started/xap-debug.html) | Integrated Processing Unit Container |
| Unit test your Processing Unit or run it in an unmanaged environment | [Standalone](./running-in-standalone-mode.html) | Standalone Processing Unit Container |
| Run your Processing Unit in production or conduct full-blown integration tests | [Managed by the Service Grid](./deploying-onto-the-service-grid.html) | Service Grid Processing Unit Container |

