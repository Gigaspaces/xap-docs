---
type: post101
title: Overview
categories: XAP101
parent: space-based-remoting-overview.html
weight: 100
---




Spring provides support for [various remoting technologies](http://static.springframework.org/spring/docs/2.0.x/reference/remoting.html). GigaSpaces uses the same concepts to provide remoting, using the space as the underlying protocol.

Some benefits of using the space as the transport layer include:

- **High availability** -- since the space by its nature (based on the cluster topology) is highly available, remote invocations get this feature automatically when using the space as the transport layer.
- **Load-balancing** -- when using a space with a partitioned cluster topology, each remote invocation is automatically directed to the appropriate partition (based on its routing handler), providing automatic load-balancing.
- **Performance** -- remote invocations are represented in fast internal OpenSpaces objects, providing fast serialization and transport over the net.
- **Asynchronous execution** -- by its nature, remoting support is asynchronous, allowing for much higher throughput of remote invocations. OpenSpaces allows you to use asynchronous execution using Futures, and also provides synchronous support (built on top of it).



The OpenSpaces API supports two types of remoting, distinguished by the underlying implementation used to send the remote call. The first is called [Executor Based Remoting](./executor-based-remoting.html), and the second is called [Event Driven Remoting](./event-driven-remoting.html).

# Choosing the Correct Remoting Mechanism

This section explains when you should choose to use each of the remoting implementations. Note that as far as the calling code is concerned, the choice between the implementations is transparent and requires only configuration changes.

In most cases, you should choose [Executor Based Remoting](./executor-based-remoting.html). It is based on the GigaSpaces [Task Executors](./task-execution-over-the-space.html) feature, which means that it executes the method invocation by submitting a special kind of task which executes on the space side by calling the invoked service. It allows for synchronous and asynchronous invocation, map/reduce style invocations and transparent invocation failover.

[Event Driven Remoting](./event-driven-remoting.html) supports most of the above capabilities, but does not support map/reduce style invocations. In terms of implementation, it's based on the [Polling Container](./polling-container.html) feature, which means that it writes an invocation entry to the space which is later consumed by a polling container. Once taking the invocation entry from the space, the polling container's event handler delegates the call to the space-side service.

The [Event Driven Remoting](./event-driven-remoting.html) implementation is slower than the [Executor Based Remoting](./executor-based-remoting.html) since it requires 4 space operations to complete a single remote call: write invocation entry by client --> take invocation entry by polling container --> write invocation result by polling container --> take invocation result by client. In contrast, [Executor Based Remoting](./executor-based-remoting.html) only requires a single `execute()` call.

However, there are two main scenarios where you should prefer [Event Driven Remoting](./event-driven-remoting.html) on top of [Executor Based Remoting](./executor-based-remoting.html):

- When you would like the actual service to not to be co-located with the space. With [Executor Based Remoting](./executor-based-remoting.html), the remote service implementation can only be located within the space's JVM(s). With [Event Driven Remoting](./event-driven-remoting.html), you can locate the client on a remote machine and use the classic **Master/Worker pattern** for processing the invocation. This offloads the processing from the space (at the expense of moving your service away from the data it might need to do the processing).
- When unexpected bursts of invocations are a probable scenario, using [Event Driven Remoting](./event-driven-remoting.html) may prove worthwhile, since invocations are not processed as they occur; they are "queued" in the space and are processed by the polling container when resources are available. By limiting the number of threads of the polling container you can make sure the invocations do not maximize the CPU of the space. (The [Alerts](./administrative-alerts.html) API can help monitor this situation.)


