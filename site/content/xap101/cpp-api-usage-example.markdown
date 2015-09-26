---
type: post101
title:  CPP API Usage
categories: XAP101
parent: cpp-api-examples.html
weight: 200
---






The C++ API Usage Example demonstrates the GigaSpaces C++ API performing the different space operations.

{{% refer %}}To learn about GigaSpaces C++ API, refer to the [GigaSpaces C++ API](./cpp-space-interface.html) section.{{% /refer %}}

The code for this example is located at `<XAP Root>\cpp\examples\APIUsageExample\`.

This example runs by default using a predefined **embedded space**. It can work with any given space (embedded, remote or clustered) simply by providing the space URL as an argument.

# Building and Running the Example

{{% note %}}
This example can be built and run on **Windows OS** only. If you use **Visual Studio** open the solution `examples.sln` located in `<XAP Root>\cpp\examples\`. It is recommended to set your solution configuration to `Release` and do a rebuild that will generate all related files.
{{%/note%}}

For more information on setting the environment refer to [Installing C++ Package](./installing-cpp-api-package.html#Setting the Environment).

After running the example the console will have the following output:


```bash

Retrieved a space proxy to /./APIUsageExampleEmbeddedSpace?groups=CPP-GROUP
Did snapshot for Person class
Running ...
Basic read, write and take
Batch operations
Number of persons that live in New York is: 50
Number of persons that live in New York aged 30+ is: 35
Total number of person entries removed from space: 100
Using Transactions
Using GSIterator
Number of values in iteration: 100
Number of values in blocking iteration: 100
Event Session API (Notifications)
Total number of notifications received: 100

DONE
Press Enter to end API Usage Example...

```

{{% info %}}
The API Usage Example can work with any given space simply by providing the space URL as an argument. To do so change the project Command Arguments and specify the space URL string. For example, `jini://localhost/*/mySpace`.
{{%/info%}}
