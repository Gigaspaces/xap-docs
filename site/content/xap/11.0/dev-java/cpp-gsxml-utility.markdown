---
type: post110
title:  gsxml2cpp Utility
categories: XAP110
parent: xap-cpp.html
weight: 700
---


This section describes the `gsxml2cpp` utility usage and available options. The `gsxml2cpp` utility generates serializer code, created by the `gs.xml` file, allowing you to perform space operations in the space. To learn how to do this, see the [CPP API Code Generator](./cpp-api-code-generator.html) section.

# How to invoke the gsxml2cpp Utility

The gsxml2cpp utility is usually called by a custom build tool. See below example how you can configure visual studio to call the gsxml2cpp:

![gsxml2cpp.jpg](/attachment_files/gsxml2cpp.jpg)

## Syntax

```bash
gs> gsxml2cpp <XML input file> <Package name> <output serializer file (.cpp)> <output classes file (.h)> <POJO output directory [optional]> <PONO output directory [optional]>
```

# Options

To run this command from the command line, the environment must be set using the `<XAP Root>\cpp\env.cmd` script.

The `gsxml2cpp` command has the following parameters:


| Parameter Description | Required Optional | Description |
|:----------------------|:------------------|:------------|
| XML input file | required | The `gs.xml` file name, or full path if it does not exist in the working directory. Holds definitions of object classes. |
| Package name | required | Not in use, used for backwards compatibility only. Use a valid string.  |
| <nobr>C++ serializer file (.cpp)</nobr> | required | The generated C++ serializer file name. Holds the serializers for each object class defined in the input `gs.xml` file. |
| C++ classes file (.h) | required | The generated C++ classes header file name. Holds the class definitions for each object class defined in the input `gs.xml` file. If this value is defined as `DummyHeaderFile`, a dummy file named `DummyHeaderFile.txt` is created and the C++ class header file is not included automatically in the C++ serializer file. |
| POJO output directory | optional | The target directory to hold the generated POJO (Java) files. |
| PONO output directory | optional | The target directory to hold the generated PONO (.NET) files. |

## Example


```bash
$gsxml2cpp ../serializer/helloWorld.gs.xml HelloWorld ../serializer/HelloWorldMessage.cpp ../serializer/HelloWorldMessage.h
```
