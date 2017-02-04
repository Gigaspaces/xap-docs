---
type: post121
title:  Overview
categories: XAP121NET, PRM
parent: installation-overview.html
weight: 100
---



**Required Software:** {{%exurl "Microsoft .NET Framework 3.5" "https://msdn.microsoft.com/en-us/library/w0x726c2.aspx"%}} or later.

{{% note %}}
Recommended:  {{%exurl "Microsoft .NET Framework 4.0" "https://msdn.microsoft.com/en-us/library/w0x726c2.aspx"%}} or later.
{{%/note%}}

**Supported Operating Systems:** Any Windows operating system supported by the designated .NET Framework version.

**64 bit support:** XAP.NET is released in two separate packages for x86 and x64. The XAP.NET x86 package can be installed on a x64 environment and run using {{%exurl "WoW64" "http://msdn.microsoft.com/en-us/library/aa384249.aspx"%}}.

#  Installation

GigaSpaces XAP.NET is packaged as a standard Windows Installer package (`.msi` file). To start the installation simply double-click the msi file, and the installation wizard will pop up and guide you through the installation process. Once you accept the license agreement, you will see the standard installation details screen, allowing you to modify the installation path (default is `C:\GigaSpaces\XAP.NET {{%currentversion%}} x86` or `x64`) and which features to install.

## Other Installation Options

GigaSpaces XAP.NET offers more installation scenarios and customizations. For example:

- Command-line installation.
- Packaging XAP.NET in another installation package.
- Side-by-side installations.
- Using a different jvm.

{{%refer%}}
For more information see [Advanced Installation Scenarios](./advanced-installation-scenarios.html).
{{%/refer%}}
