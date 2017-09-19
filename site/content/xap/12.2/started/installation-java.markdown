---
type: post122
title:  Setup
categories: XAP122GS
parent: installation.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}

GigaSpaces InsightEdge and XAP is 100% pure Java based, and therefore can run on any UNIX or Windows machine that supports Java.

# Installation

Before installing:

- Review the release notes for [supported platforms](/xap/12.2/rn/supported-platforms.html)
- Make sure {{%exurl "Java" "http://java.sun.com/javase/downloads/index.jsp"%}} is installed (Minimum: Java 7, Recommended: Java 8)
   - InsightEdge/XAP locates Java via the standard `JAVA_HOME` or `PATH` environment variables

To install InsightEdge / XAP, simply unzip it using your favorite unzip tool to the location of your choice. Unzipping the file creates a `<XAP_HOME>` directory (e.g. `{{%version "gshome-directory-oss"%}}`).

- Do not use path with spaces (such as `C:\Program Files\xap`) - In some scenarios they break, it's best to avoid it.
- Linux users: Make sure all `sh` files in the `/bin` and `/examples` folders are in executable mode, so you can run them from your machine. To check this, use the `ls \-all` command for the relevant directory, and make sure that `x` is included in the file permissions.

{{% info "Network Discovery" %}}
By default, InsightEdge and XAP uses multicast network discovery. If your environment does not support multicast, or you experience other network issues, please refer to [Network Configuration](../admin/network.html) for more information.
{{% /info %}}

# What's Next?

- To verify a local installation, a remote installation, and the cluster configuration, refer to the [Testing System Environment](../admin/troubleshooting-testing-system-environment.html) section.
- See the [Quick Start Guide]({{%currentjavatuturl%}}) for your first steps with XAP.

{{% anchor 2 %}}

# Important Tips

Before you begin working with GigaSpaces, it is recommended to review the [Performance Tuning and Considerations](../admin/tuning.html) sections and apply the required changes. For example, you may need to update the [**max file descriptors limit**](../admin/tuning-infrastructure.html#Max Processes and File Descriptors Limit) before you begin.

{{% refer %}}
 The recommendation is to review at least the following sections:

- [Tuning Infrastructure](../admin/tuning-infrastructure.html)
- [Tuning GigaSpaces Performance - Basics](../admin/tuning-gigaspaces-performance.html)
- [Tuning Java Virtual Machines](../admin/tuning-java-virtual-machines.html)
- [Benchmarking the platform](../admin/moving-into-production-checklist.html)
{{% /refer %}}


