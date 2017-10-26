---
type: post97
title:  API Code Generator
categories: XAP97
parent: xap-cpp.html
weight: 70
---



The `*gs.xml` file and the `gsxml2cpp` utility allow you to use your C++ classes in the space without a need for mapping or special knowledge regarding other APIs. This file is in charge of generating the C++ class serialization code, allowing you to use your C++ classes in the space. It can also be used to generate the C++ class header file and POJO Java file.

Here is the general flow:

{{% indent %}}
![cpp-dev-process.jpg](/attachment_files/cpp-dev-process.jpg)
{{% /indent %}}

- To define your classes in the space using the `*.gs.xml` file, perform the following steps:

1. Write a `*gs.xml` file, defining its [XML elements](./cpp-api-mapping-file.html) as desired.
1. Run the [gsxml2cpp](./cpp-gsxml-utility.html) command -- this command generates the serializer code, which includes the classes defined in your `gs.xml` file.
1. Compile the serializer code -- this is done using the `gsxml2cpp` command `cpp output` parameter. For an example of how to do this, refer to the [C++ Hello World Example](./cpp-api-hello-world-example.html#Code Walkthrough).
1. Compiling the serializer code generates a DLL/OS file in the format of: `serialize.dll` or  `serialize.so`. Copy the DLL/SO file to the `<GigaSpaces Root>\lib\platform\native` directory.

You can now use your C++ classes inside the space.

{{% refer %}}For an explanation of the different `gs.xml` elements, refer to the [CPP API Mapping File](./cpp-api-mapping-file.html) section.{{% /refer %}}

{{% refer %}}To find out about the **supported C++ types**, refer to the [Supported Types](./cpp-api-mapping-file.html#type -- Supported Types) section.{{% /refer %}}

{{% refer %}}For an explanation of the `gsxml2cpp` command and how to use it, refer to the [gsxml2cpp Utility](./cpp-gsxml-utility.html) section.{{% /refer %}}
