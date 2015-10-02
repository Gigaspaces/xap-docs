---
type: postsbp
title:  GigaSpaces-Excel Market-Data Example
categories: SBP
parent: excel-that-scales-solution.html
weight: 500
---

{{% tip %}}
**Summary:**  This example shows the usage of Microsoft Excel spreadsheets and GigaSpaces XAP for working with stock market data. It also demonstrates the building of Excel RTD and UDF components in C# .NET. <br/>
**Author**: Pini Cohen, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP.NET 6.6<br/>
**Contents:**<br/>


{{% /tip %}}

# Overview

This example shows the usage of Microsoft Excel spreadsheets and GigaSpaces XAP for working with stock market data. It also demonstrates the building of Excel RTD and UDF components in C# .NET. These components are integrated with Excel and work with the GigaSpaces XAP platform.

The example follows a common capital market use-case of managing market data flows. The market data flows are "wired" into the GigaSpaces XAP cluster, and Excel is notified of changes in the market data metrics.

The example demonstrates the following:

- Running GigaSpaces XAP with up-to-date stock market data.
- Running Excel spreadsheets that show a view of the stock market data, and are updated almost in realtime; to show changes and updates in the market data.
- Building and using Excel RTD and UDF components.


{{% refer %}}Getting Started with RTD and UDF? See the [Writing Your First RTD/UDF Application](./writing-your-first-rtd-or-udf-application.html) section and review the [prerequisites](./prerequisites---gigaspaces-excel-integration.html).{{% /refer %}}

# Example Structure

The example contains a few projects:

- The **`StockEntities`** project defines the `StockData` object, which stocks the market data with updated information.
- The **`StockQuote`** project is in charge of notifying the Excel workbook of changes in the stock market data. `StockQuote` is an Excel RTD server. It receives GigaSpaces events when the stock market data is updated, and in turn notifies Excel of changes.
- The **`StockOperations`** project is an Excel UDF (User-Defined Function). It returns the "Open" value of a Stock Symbol.
- The **`StockMarketFeeder`** project writes the stock market data into the space. It simulates changes and actions that take place in the stock market.

{{% anchor building %}}

# Building the Example

Compile the .NET applications using `<GigaSpaces Root>\Examples\ExcelStocks\bin\compile.bat`.

The .NET files are created in the `<Example Root>\Release` directory.

# Adding UDF Function to Excel

1. In Excel, go to **Tools** > **add ins** > **automation**.
2. Scroll down to **GigaSpaces.Examples.ExcelStocks.Operations.StockOperations**, select it and click **OK**.

{{% indent %}}
![automation.jpg](/attachment_files/sbp/automation.jpg)
{{% /indent %}}

3. You might get a dialog at this point about mscoree.dll. Click No to this dialog (Yes will delete the add-in from the list).

{{% indent %}}
![mscoree_dll.jpg](/attachment_files/sbp/mscoree_dll.jpg)
{{% /indent %}}

4. Click **OK** in the Add-ins dialog.

{{% indent %}}
![add-ins.jpg](/attachment_files/sbp/add-ins.jpg)
{{% /indent %}}

## Opening .NET Solution with Microsoft Visual Studio

1. Double-click the `ExcelStocks.sln` file, according to the version of Visual Studio you are using.
2. Choose **Build** > **Build Solution**. The files: `StockEntities.dll`, `StockOperations.dll(UDF)`, `StockQuote.dll(RTD)` and `StockMarketFeeder.exe(Market feeder)`  are created in the `Release` subdirectory.

# Running the Example

{{% exclamation %}} The example must be compiled before you run it (see [Building the Example](#building) above).

1. Start the GigaSpaces environment: `<Example Root>\bin\startAll.bat`.
2. Run the GigaSpaces Management Center `(<GigaSpaces Root>\Bin\Gs-ui.exe`) to see the **`GigaSpaces.Examples.ExcelStocks.StockEntities.StockData`** instances. To view only the relevant spaces:
    1. In the top menu bar, choose **Settings** > **Discovery** > **Group management**.
    2. Select only the **Excel** group, unselect all the rest.

3. Open the Excel file `<Example Root>\Chart.xls`.

# Example Scripts

The example includes the following scripts:

- **`compile.bat`** - compiles the .NET components.
- **`startAll.bat`** - runs the feeder process that starts an embedded space and fills the space with stock market data updates. These updates trigger RTD calls, and are presented in realtime in the open Excel workbook.

# Tested Configuration

- OS client -- Windows XP, SP2 and onwards
- .NET client -- MS .NET Framework 2
- Excel -- Office 2003 Professional
- GigaSpaces XAP.NET 6.6

# Tested Performance Metrics

The following metrics describe the scope of testing performed using the examples from above. The solution is known to be working with these numbers. It should be noted that these metrics are not an upper boundary. It is recommended that any deviation beyond these numbers first be verified with GigaSpaces PM and/or RND.

- Excel 2003 clients
- Up to 50 concurrent remote Excel clients (5 machines, 10 clients on each machine).
- Up to 200 space-related Excel cells (RTD) in a single spreadsheet.
- Up to 200 events per second pushed from the space to Excel.
- Object sizes of up to 10K.
- Maximum heap size of embedded Excel JVM - 64M.

{{% exclamation %}} A .NET local cache on the Excel side is not supported.

{{% refer %}}Back to The [Excel that Scales Solution](./excel-that-scales-solution.html) section.{{% /refer %}}
