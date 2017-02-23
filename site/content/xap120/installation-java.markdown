---
type: post120
title:  Setup
categories: XAP120
parent: installation.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}

GigaSpaces XAP Java edition is 100% pure Java based, and therefore can run on any UNIX or Windows machine that supports Java.

{{%anchor Prior-to-Installation%}}

# Prior to Installation

**Prior to the XAP installation, make sure**:

- Review the release notes for the [supported platforms](/release_notes).
- [JDK](http://java.sun.com/javase/downloads/index.jsp) (6 or later) is installed
- Set the `JAVA_HOME` environment variable to the JDK (not JRE) directory (for example, `D:\java\jdk1.7.0_70`).
- Set the `PATH` environment variable to include `JAVA_HOME\bin` (for example, `%JAVA_HOME%\bin;%SystemRoot%\system32;%SystemRoot%`
- Optional: The network and machines running GigaSpaces are configured to enable multicast (see the [Multicast Configuration]({{%currentadmurl%}}/network-multicast.html) for more information).
- Optional: Set the `XAP_NIC_ADDRESS` environment variable to the machine's IP address on each machine running XAP.

{{%note%}}
Use a JDK and not JRE - Have a JDK (Java Development Kit) installed and not a JRE (Java Runtime Environment) on each machine running XAP.
{{%/note%}}

# Installation

Unzip the ZIP file using your favorite unzip tool to the location of your choice:

```bash
unzip {{%version "build-filename-oss" %}}
```

Unzipping the file creates a `<XAP_HOME>` directory (e.g. `{{%version "gshome-directory-oss"%}}`).

- We recommend avoiding paths with spaces (such as `C:\Program Files`) to avoid complications later on.
- Linux users - Make sure all `sh` files in the `/bin` and the `/examples` directory are in executable mode, so you can run them from your machine. To check this, use the `ls \-all` command for the relevant directory, and make sure that `x` is included in the file permissions.

# What's Next?

- To verify a local installation, a remote installation, and the cluster configuration, refer to the [Testing System Environment]({{%currentadmurl%}}/troubleshooting-testing-system-environment.html) section.
- See the [Quick Start Guide]({{%currentjavatuturl%}}) for your first steps with XAP.

{{% anchor 2 %}}

# Important Tips

Before you begin working with GigaSpaces, it is recommended to review the [Performance Tuning and Considerations]({{%currentadmurl%}}/tuning.html) sections and apply the required changes. For example, you may need to update the [**max file descriptors limit**]({{%currentadmurl%}}/tuning-infrastructure.html#Max Processes and File Descriptors Limit) before you begin.

{{% refer %}}
 The recommendation is to review at least the following sections:

- [Tuning Infrastructure]({{%currentadmurl%}}/tuning-infrastructure.html)
- [Tuning GigaSpaces Performance - Basics]({{%currentadmurl%}}/tuning-gigaspaces-performance.html)
- [Tuning Java Virtual Machines]({{%currentadmurl%}}/tuning-java-virtual-machines.html)
- [Benchmarking the platform]({{%currentadmurl%}}/moving-into-production-checklist.html)
{{% /refer %}}


