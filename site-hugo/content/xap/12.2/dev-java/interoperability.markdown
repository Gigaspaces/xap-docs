---
type: post122
title:  Overview
categories: XAP122, OSS
parent: interoperability-overview.html
weight: 100
---



XAP introduces interoperability -- the possibility for organizations whose projects include a combination of Java, .NET and other platforms to communicate and access each other easily and efficiently, while also maintaining the benefits of the GigaSpaces scale-out application server:

- **Transparency**: using your native API in the space transparently with minimum changes to existing assets. Objects are stored in the space in the same manner regardless of the platform they are written from. For example, an object can be written to the space from a Java service, and a .NET service can receive the notification.
- **Performance**: communication between platforms is performed through the space directly, without the need for adapters or XML translation.
- **SOA**: all applications communicate using the space, allowing each of them to exist as a loosely-coupled service.

![interop.png](/attachment_files/interop.jpg)

To work with GigaSpaces interoperability, classes from the different platforms should meet the following criteria:

- The classes should have the **same fully qualified name** (i.e. including package/namespace).
- The classes should have the **same hierarchy** (i.e. same base/superclass structure).
- The classes should have the **same properties with the same names and matching types**.

{{%refer%}}
Please refer to the [interoperability section in our XAP.NET documentation]({{%currentneturl%}}/dotnet-java-interoperability.html).
{{%/refer%}}
