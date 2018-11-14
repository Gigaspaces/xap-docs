---
type: post140
title:  Installation
categories: XAP140NET, PRM
parent: none
weight: 100
---




This guide provides step by step instructions on how to download and install XAP.


{{%note "Note"%}}
The latest version of XAP can be downloaded {{%exurl "here" "http://www.gigaspaces.com/xap-download"%}}. For a list of supported platforms, refer to the [release notes](../rn/supported-platforms.html).
{{%/note%}}


**Required Software:** {{%exurl "Microsoft .NET Framework 3.5" "https://msdn.microsoft.com/en-us/library/w0x726c2.aspx"%}} or later.

**Recommended Software** {{%exurl "Microsoft .NET Framework 4.0" "https://msdn.microsoft.com/en-us/library/w0x726c2.aspx"%}} or later.


**Supported Operating Systems:** Any Windows operating system supported by the designated .NET Framework version.

#  Installation

GigaSpaces XAP.NET is packaged as a standard Windows Installer package (`.msi` file). To start the installation simply double-click the msi file, and the installation wizard will pop up and guide you through the installation process. After you accept the license agreement, you will see the standard installation details screen, allowing you to modify the installation path (default is `C:\GigaSpaces\XAP.NET {{%currentversion%}} x86` or `x64`) and which features to install.

## Other Installation Options

GigaSpaces XAP.NET offers more installation scenarios and customizations. For example:

- Command-line installation.
- Packaging XAP.NET in another installation package.
- Side-by-side installations.
- Using a different JVM.

{{%refer%}}
For more information about installing XAP.NET, refer to [Advanced Installation Scenarios](./advanced-installation-scenarios.html).
{{%/refer%}}
