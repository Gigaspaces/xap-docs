---
type: post
title:  Calculation Offload - GigaSpaces-Excel Integration
categories: SBP
parent: excel-that-scales-solution.html
weight: 100
---


# Overview

In this pattern, all Excel functions and calculations are performed **by the space** (on the server side) asynchronously. This allows you to use Excel regularly, where calculations are performed on the space side simultaneously, until the calculation is finished; then, Excel is notified and the data is displayed in the spreadsheet.

This pattern is based on the [GigaSpaces master-worker pattern](./master-worker-pattern.html), and allows you to offload complex logic from Excel spreadsheets to run in parallel on the GigaSpaces cluster collocated with the data.

A typical use-case for this pattern is a Value at Risk (VAR) calculation. The Excel user initiates a command to start a VAR calculation with specific parameters. This command is updated in the space using UDF. Once the command arrives, multiple Processing Units (PU) or spaces workers "pick up" the VAR task, execute it in parallel, and return the completed calculation result to the space (for example, with the status `done`). The completed results are then pushed to the Excel spreadsheet using RTD, which "listens" for a specific notification (for example, objects with status `done`); thus completing the workflow.

Using this pattern is divided into 4 main steps:

1. [Loading your data to the space](#1-loading-data).
2. [Implementing the algorithm](#2-implementing-algorithm) that performs the desired calculation **inside the space**.
3. [Implementing a start trigger](#3-implementing-start-trigger-and-passing-parameters) by which Excel tells the space to begin calculating, and passing the relevant parameters from Excel to the space.
4. [Implementing a finish trigger](#4-implementing-finish-trigger) by which the space tells Excel that it has finished calculating, and displays the results into the Excel spreadsheet.

## 1 -- Loading Data

As a first step, you need to load your data from its current source to the space.

GigaSpaces provides [OpenSpaces](/product_overview/index.html) as its main API. However, it is also possible to load data from different types of applications transparently, using different connectors implemented by GigaSpaces:

- For messaging-based applications, refer to the [JMS]({{%latestjavaurl%}}/messaging-support.html) section.
- If your application is an external data source (like a database), refer to the [Persistency]({{%latestjavaurl%}}/space-persistency.html) section.

## 2 -- Implementing Algorithm

After you've loaded your data to the space, you need to implement the algorithm which performs the calculation in the space, thus removing the load from Excel.

To do this, you need to write a set of [Processing Units]({{%latestjavaurl%}}/the-processing-unit-structure-and-configuration.html) into the space, which perform the calculation.

## 3 -- Implementing Start Trigger and Passing Parameters

After you've implemented Processing Units to perform the calculation, you need to tell them to begin calculating. You also need to pass the Excel function parameters to the space.

Using Microsoft Excel UDF (User-Defined Functions), Excel writes an Entry to the space instructing the Processing Units to begin working.

{{% refer %}}**Learn how to do this**:

- [HelloUDF example](./rtd-and-udf-examples-gigaspaces-excel-integration.html#HelloUDF -- Performing Excel Functions in Space) (basic)
- [UDFSample example](./rtd-and-udf-examples-gigaspaces-excel-integration.html#UDFSample -- Performing Excel Functions in Space) (advanced)
- [Writing Your First UDF Application](./writing-your-first-rtd-or-udf-application.html){{% /refer %}}

## 4 -- Implementing Finish Trigger

In the last step, you need to implement a finish trigger in the space, which tells Excel that the space has finished calculating; and finally, displaying your results in the spreadsheet.

The space writes a notification to the Microsoft Excel RTD (Real-Time Data) server, which in turn notifies Excel that the space has finished calculating.

{{% refer %}}

**Learn how to do this**:

- [HelloRTD example](./rtd-and-udf-examples-gigaspaces-excel-integration.html#HelloRTD -- Loading Data from Space to Excel) (basic)
- [RTDSample example](./rtd-and-udf-examples-gigaspaces-excel-integration.html#RTDSample -- Loading Data from Space to Excel) (advanced)
- [Writing Your First UDF Application](./writing-your-first-rtd-or-udf-application.html)

For details on building Excel Real-Time Data components in Visual Basic .NET, see the [Microsoft website](http://msdn2.microsoft.com/en-us/library/aa140061.aspx).
{{% /refer %}}

# What's Next?

{{% refer %}}
[See the full example](./gigaspaces-excel-market-data-example.html)
[Try another pattern](./excel-that-scales-solution.html)
{{% /refer %}}
