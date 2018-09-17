---
type: post120
title:  Performance Tuning
categories: XAP120ADM
parent: none
weight: 1000
---



In this guide you will find information on how to tune your XAP application. Amongst the topics covered are JVM tuning and thread usage.

<br>

{{%fpanel%}}

[Infrastructure](./tuning-infrastructure.html){{<wbr>}}
Recommendations for tuning the infrastructure on which XAP is running.

[Application](./tuning-gigaspaces-performance.html){{<wbr>}}
This section lists helpful recommendations for tuning your application when using XAP to boost its performance, and improving its scalability.

[Slow Consumer](./slow-consumer.html){{<wbr>}}
The Space includes a special mechanism that detects clients that cannot consume the notifications sent fast enough - i.e. slow consumers.

[Large scale deployment](./tuning-large-scale-deployment.html){{<wbr>}}
When designing a large cluster, there are several things that need to be taken into account to assure that the cluster will be able to handle heavy loads, and perform quickly and stably.

[Proxy connectivity](./tuning-proxy-connectivity.html){{<wbr>}}
Configuring and optimizing the proxy connectivity.

[Handling large objects](./tuning-handling-large-objects.html){{<wbr>}}
How to handle large files or objects written to the Space.


[LRMI Configuration](./tuning-communication-protocol.html)<br>
XAP provides a pluggable communication adaptor, LRMI (Light Remote Method Invocation), built on top of NIO communication protocol.

[JVM Tuning](./tuning-java-virtual-machines.html){{<wbr>}}
As part of configuring XAP, you can fine-tune settings that enhance system use of the JVM.

[Tuning Threads](./tuning-threads-usage.html){{<wbr>}}
This section describes Thread pool tuning.
{{%/fpanel%}}

<br>

## Additional Resources

- [Moving your application into production](./moving-into-production-checklist.html)
