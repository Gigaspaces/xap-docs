---
type: post120
title:  XAP Performance
categories: XAP120ADM
parent:  tuning.html
weight: 200
---





In this guide you will find information on how to tune your XAP application.

<br>

{{%fpanel%}}

[Overview](./tuning-gigaspaces-performance.html){{<wbr>}}
This section lists helpful recommendations for tuning your application when using XAP to boost its performance, and improving its scalability.

[Slow Consumer](./slow-consumer.html){{<wbr>}}
The Space includes a special mechanism that detects clients that cannot consume the notifications sent fast enough - i.e. slow consumers.

[Large scale deployment](./tuning-large-scale-deployment.html){{<wbr>}}
When designing a large cluster, there are several things that need to be taken into account to assure that the cluster will be able to handle heavy loads, and perform quickly and stably.

[Proxy connectivity](./tuning-proxy-connectivity.html){{<wbr>}}
Configuring and optimizing the proxy connectivity.

[Handling large objects](./tuning-handling-large-objects.html){{<wbr>}}
How to handle large files or objects written to the Space.

XAP provides a pluggable communication adaptor, LRMI (Light Remote Method Invocation), built on top of NIO communication protocol.

{{%/fpanel%}}



