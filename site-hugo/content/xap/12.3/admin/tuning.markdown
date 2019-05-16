---
type: post123
title:  Performance Tuning
categories: XAP123ADM, OSS
parent: none
weight: 1000
canonical: auto
---


After you have developed your application, you may want to adjust parameters in various areas, in order to improve performance. The following issues may affect system performance, and can be fine-tuned to better suit the needs of your XAP-based application:

- [Infrastructure](./tuning-infrastructure.html) - The hardware and operating system settings can be optimized to suit the requirements of the XAP functionality that has been implemented.
- [XAP application features and design](./tuning-gigaspaces-performance.html) - Select the appropriate data model and design features for your application needs.
- [Slow consumers](./slow-consumer.html) - XAP includes a feature that can detect slow consumers and adjust the system behavior, so that performance is only minimally affected.
- [Large-scale deployment](./tuning-large-scale-deployment.html) - When designing a large cluster, some fine-tuning is required to ensure that the cluster can handle heavy loads, while providing rapid and stable performance.
- [Proxy connectivity](./tuning-proxy-connectivity.html) - The default parameter values of the Space proxy router can be modified to better suit they way that the client application connects to the XAP data grid.
- [Handling large objects](./tuning-handling-large-objects.html) - If your application has to store large files in memory, you can adjust the memory allocation behavior accordingly.
- [Communication protocol](./tuning-communication-protocol.html) - XAP includes a proprietary LRMI (Light Remote Method Invocation) communication adaptor, built on top of the NIO communication protocol. This adaptor has parameters that can be tweaked to improve application performance.
- [Java Virtual Machines (JVMs)](./tuning-java-virtual-machines.html) - XAP is a Java process and runs on a JVM that can be adjusted to enhance system use.
- [Threads Usage](./tuning-threads-usage.html) - XAP uses thread resources extensively to scale activities depending on system load. You can adjust the configuration parameters.

## Additional Resources

For more information about fine-tuning the performance of your XAP-based application, refer to the section that describes [Moving Into Production](../production/index.html).
