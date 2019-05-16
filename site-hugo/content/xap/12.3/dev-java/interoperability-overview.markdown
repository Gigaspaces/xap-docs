---
type: post123
title:  Platform Interoperability
categories: XAP123, OSS
parent: none
weight: 2500
canonical: auto
---


{{% note "Note" %}}
Some of the features listed on this page are not part of the open-source edition, and are only available in the licensed editions (starting with XAP Premium).
{{% /note %}}

XAP supports interoperability, which is the ability for organizations whose projects include a combination of Java, .NET and other platforms to communicate and access each other easily and efficiently, while also maintaining the benefits of the XAP scale-out application server:

- **Transparency**: Use your native API in the Space transparently with minimum changes to existing assets. Objects are stored in the Space in the same manner regardless of the platform they are written from. For example, an object can be written to the space from a Java service, and a .NET service can receive the notification.
- **Performance**: Communicate between platforms directly through the Space, without the need for adapters or XML translation.
- **SOA**: All applications communicate using the Space, allowing each of them to exist as a loosely-coupled service.

![interop.png](/attachment_files/interop.jpg)

To work with XAP interoperability, classes from the different platforms should meet the following criteria:

- The **same fully qualified name** (i.e. including package/namespace).
- The **same hierarchy** (i.e. same base/superclass structure).
- The **same properties with the same names and matching types**.

{{%refer%}}
For information about XAP.NET, refer to the [interoperability](../dev-dotnet/interoperability.html) section in the XAP.NET guide.
{{%/refer%}}
