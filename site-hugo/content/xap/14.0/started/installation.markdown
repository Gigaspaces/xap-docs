---
type: post140
title:  Downloading and Installing
categories: XAP140GS
parent: none
weight: 100
---



The GigaSpaces InsightEdge and XAP applications are 100% pure Java based, and therefore can run on any UNIX or Windows machine that supports Java.

# Prerequisites

Before installing InsightEdge or XAP, do the following:

- Review the [Supported Platforms](../rn/supported-platforms.html) section of the release notes.
- Make sure that {{%exurl "Java" "http://java.sun.com/javase/downloads/index.jsp"%}} is installed (Java 8 or higher).

InsightEdge/XAP locates Java via the standard `JAVA_HOME` or `PATH` environment variables.

# Installation
   
To install InsightEdge or XAP, download and unzip the application package to the location of your choice. Unzipping the file creates a `<XAP_HOME>` directory (e.g. `{{%version "gshome-directory-oss"%}}`).

{{% warning "Important"%}}
Do not use a path with spaces (such as `C:\Program Files\xap`). In some scenarios they may break.

Linux users: Make sure all `sh` files in the `/bin` and `/examples` folders are in executable mode, so you can run them from your machine. 
To check this, use the `ls -all` command for the relevant directory, and make sure that `x` is included in the file permissions.
{{% /warning %}}

## Network Discovery

By default, InsightEdge and XAP use multicast network discovery. If your environment does not support multicast, or you experience other network issues, refer to [Network Configuration](../admin/network.html) for more information.

## Important Tips

Before you begin working with GigaSpaces products, it is recommended to review the [Performance Tuning](../admin/tuning.html) section and apply the required changes in your system. For example, you may need to update the [max file descriptors limit](../admin/tuning-infrastructure.html#Max Processes and File Descriptors Limit) before you begin.

{{% refer %}}
It is strongly recommended to read the following sections in addition to the above, which explain how to further fine-tune your environment for better application performance, and how to assess system performance:

- [Infrastructure](../admin/tuning-infrastructure.html)
- [Applications](../admin/tuning-gigaspaces-performance.html)
- [Java Virtual Machines](../admin/tuning-java-virtual-machines.html)
- [Benchmarking](../admin/benchmarking.html)
{{% /refer %}}

# What's Next?

- To verify a local installation, a remote installation, and the cluster configuration, refer to the [Testing System Environment](../admin/troubleshooting-testing-system-environment.html) section.
- See [InsightEdge Basics](./insightedge-basics.html) or [XAP Basics](./xap-basics.html) for a step-by-step introduction to InsightEdge and XAP, including sample code.

{{% anchor 2 %}}




