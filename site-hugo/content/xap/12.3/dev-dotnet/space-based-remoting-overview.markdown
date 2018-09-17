---
type: post123
title:  Space-Based Remoting
categories: XAP123NET, PRM
parent: none
weight: 1300
---


Remoting allows you to use remote invocations of POJO services, with the Space as the transport layer. For each option, you have to define the contract between the client and server, provide an implementation of the contract, and host the service on the server side via a Processing Unit.

The following options are available for remote invocation of services in the Space:

- [Domain Service Host](./domain-service-host.html) - Used to host services within the hosting processing unit domain which are exposed for remote invocation.
- [Executor-Based Remoting](./executor-based-remoting.html) - Allows you to use remote invocations of services with the Space as the transport layer, using Executors.

