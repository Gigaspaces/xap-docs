---
type: post110
title:  Overview
categories: XAP110
parent: interoperability-overview.html
weight: 100
---



XAP introduces interoperability -- the possibility for organizations whose projects include a combination of Java, .NET and C++ platforms to communicate and access each other easily and efficiently, while also maintaining the benefits of the GigaSpaces scale-out application server:

- **Transparency**: using your native API in the space transparently with minimum changes to existing assets. Objects are stored in the space in the same manner regardless of the platform they are written from. For example, an object can be written to the space from a Java feeder, processed using a c++ Processing Unit, and a .NET client can receive the notification.
- **Performance**: communication between platforms is performed through the space directly, without the need for adapters or XML translation.
- **SOA**: all applications communicate using the space, allowing each of them to exist as a loosely-coupled service.

![interop.png](/attachment_files/interop.jpg)

To work with GigaSpaces interoperability, classes from the different platforms should meet the following criteria:

- The classes should have the **same fully qualified name** (i.e. including package/namespace).
- The classes should have the **same hierarchy** (i.e. same base/superclass structure).
- The classes should have the **same properties with the same names and matching types**.

There are two methodologies for developing interoperable solutions:

- Writing the class code in each relevant platform by yourself (See the **Writing Interoperable Class** tab below)
- Writing an XML file that describes the Entry, and use the code generation tool in the c++ component to generate classes for all platforms (see the **Code Generator** tab below)

There are pros and cons for each alternative. Here are some points to help you decide which methodology is best for you:

- If you need a c++ version of the class, you have to write the `gs.xml` file to generate the c++ code.
- If you're getting started with interoperability, you can use the code generator as a learning tool to review interoperable classes in all platforms.
- The code generation tool does not support some of the advanced features in .NET. If you have a need for these features, it is recommended to use the generator to get started, and manually edit the generated code. In this case you need to be careful when changing the class, since regenerating the code might override your manual changes.


### Writing Interoperable Classes

{{%refer%}}
Please refer to the [interoperability section in our XAP.NET documentation]({{%currentneturl%}}/dotnet-java-interoperability.html).
{{%/refer%}}


### Code Generator

{{%refer%}}
Please refer to the [C++ Code Generation Guidelines](./cpp-api-code-generator.html).
{{%/refer%}}
