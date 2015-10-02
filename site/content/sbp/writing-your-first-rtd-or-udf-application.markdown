---
type: postsbp
title:  Writing Your First RTD or UDF Application
categories: SBP
parent: excel-that-scales-solution.html
weight: 800
---

{{% ssummary%}}Writing your first RTD/UDF application using the GigaSpaces-Excel integration.{{% /ssummary %}}

# Overview

This section shows you how to write your first RTD or UDF application using the GigaSpaces-Excel integration.

Please read the [XAP-Excel integration prerequisites](./prerequisites---gigaspaces-excel-integration.html) before you proceed.

{{% refer %}}See some basic and advanced code examples for [working with UDF and RTD](./rtd-and-udf-examples-gigaspaces-excel-integration.html).{{% /refer %}}

# Writing Your First RTD/UDF Application

1. In Visual Studio, create 2 new **Class Library** projects, one for UDF and one for RTD.
2. Add the `<GigaSpaces Root>\Bin\GigaSpaces.Core.dll` reference.
3. In the **COM** tab, select **Microsoft Excel 11.0 Object Library**.
4. In the **.NET** tab, select **System.configuration**.
5. Office runtime:
    - For Excel 2003 - in the **COM** tab, add the **Microsoft Office 11.0 Object Library**.
    - For Excel 2007 - in the **COM** tab, add the **Microsoft Office 12.0 Object Library**.

6. Build the projects.
7. Make sure `GigaSpaces.Core.dll` and `GigaSpaces.NetToJava.dll` exist in your `Release` folder.
8. Run the following command from the `Release` folder:
    `%WinDir%\Microsoft.NET\Framework\v2.0.50727\RegAsm.exe \[MY_RTD_CLASSNAME\].dll /Codebase`

9. Save the [excel.exe.config](/download_files/sbp/excel.exe.config) file in your Excel runtime folder. For example: `C:\Program Files\Microsoft Office\OFFICE11`
10. Open Excel.
11. To add the UDF function:
    1. In Excel, go to **Tools** > **add ins** > **automation**.
    2. Scroll down to **\[MY_UDF_CLASSNAME\]**, select it and click **OK**. You might get a dialog at this point about mscoree.dll. Click No to this dialog (Yes will delete the add-in from the list).
12. Start a space: `<GigaSpaces Root>\Bin\Gs-ui.exe`.
13. To call a UDF, click the **Function** icon, and look for the **\[MY_UDF_CLASSNAME\]** functions.

- To call a RTD, write in any Excel cell:
    `=RTD("[MY PROG ID]",,[MY PARAMETERS])`

# What's Next?

{{% refer %}}
See the [GigaSpaces-Excel Market-Data Example](./gigaspaces-excel-market-data-example.html).
Back to The [Excel that Scales Solution](./excel-that-scales-solution.html) section.
{{% /refer %}}
