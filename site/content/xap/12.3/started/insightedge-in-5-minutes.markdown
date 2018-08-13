---
type: post123
title:  InsightEdge in 5 Minutes
categories: XAP123GS, OSS
parent: insightedge-basics.html
weight: 70
---

This topic describes how to start an InsightEdge using the embedded demo, in order to get you up and running as quickly as possible. The demo is launched via the GigaSpaces command line interface (CLI) tool. The InsightEdge demo runs in standalone mode and creates the following environment on your local machine:

* Spark Master
* Spark Worker
* Apache Zeppelin
* Data grid comprised of a Space in high availability mode (two primary partitions, each with a single backup partition)

# Initializing the InsightEdge Application

It takes just a few simple steps to start InsightEdge using the CLI.

**To begin working with InsightEdge:**

1. Download the InsightEdge package from the GigaSpaces website and install it, as described in [Downloading and Installing](./installation.html).
1. From the &lt;INSIGHTEDGE_HOME&gt; directory, open the `xap-license.txt` file and apply the "tryme" license key, which will give you access to the InsightEdge features and functionality for 24 hours.

```
# License can also be set via the XAP_LICENSE environment variable or com.gs.licensekey system property
tryme
```

{{%note%}}
For more information about InsightEdge license options, see the [Product License](./license-key.html) page.
{{%/note%}}

3. Navigate to the following directory and start the CLI:
{{%tabs%}}
{{%tab "Linux/Mac"%}}

```
<INSIGHTEDGE_HOME>/bin
```

{{% /tab %}}
{{%tab "Windows"%}}

```
<INSIGHTEDGE_HOME>\bin
```

{{% /tab %}}
{{% /tabs %}}

4. Run the following command in the CLI:
{{%tabs%}}
{{%tab "Linux/Mac"%}}

```
./insightedge demo
 ```

{{% /tab %}}
{{%tab "Windows"%}}

```
insightedge demo
 ```

{{% /tab %}}
{{% /tabs %}}


The InsightEdge environment includes a number of components, and may take up to a minute to finish initializing.

The Web Management Console is automatically launched in the local machine's default web browser at the following address: `localhost:8099`. It opens to the host screen, which shows the four containers that were created by the demo (two partitions with one backup each). 

{{%note%}}
For information on how to use the Web Management Console and other administration tools to view the XAP core components (the data fabric within InsightEdge), see the [Administering Spaces and Processing Units](../admin/admin-spaces-pu.html) topics in the Administration section.
{{%/note%}}

# Apache Zeppelin Notebook Examples

After you get InsightEdge up and running, you can use the Apache Zeppelin web notebook to explore the examples that are provided with the product.

**To access the examples in the web notebook:**

1. Navigate to `localhost:9090` in your web browser.
2. The InsightEdge package comes with three Getting Started notebooks; InsightEdge Basics, InsightEdge Geospatial API, and InsightEdge Python Example. Open any of these examples and click **Play** to run the paragraph it contains.
{{%note%}}
If you see an **Interpreter Binding** setting in the notebook main window, click **Save** (don't deselect any interpreters or the examples won't work).
{{%/note%}}


# What's Next?

* For more infomation about the Apache Zeppelin Web Notebook, see [Using the InsightEdge Web Notebook](insightedge-zeppelin.html).
* To run the examples on your local InsightEdge environment, see [Running InsightEdge IDE Examples](insightedge-examples.html).
* To create your first InsightEdge application, see [Developing Your First InsightEdge Application](insightedge-first-app.html).
* To learn more about the InsightEdge CLI script, use the `--help` option.

