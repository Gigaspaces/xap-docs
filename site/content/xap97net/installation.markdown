---
type: post97net
title:  Overview
categories: XAP97NET
parent: installation-overview.html
weight: 50
---

{{%wbr%}}

**Required Software:** [Microsoft .NET Framework 2.0](http://msdn.microsoft.com/en-us/netframework/aa731542.aspx) or later (including  [Microsoft .NET Framework 4.0](http://msdn.microsoft.com/en-us/netframework/aa569263.aspx)).

{{% note %}}
Recommended: [Microsoft .NET Framework 2.0 Service Pack 2](http://www.microsoft.com/downloads/details.aspx?familyid=5B2C0358-915B-4EB5-9B1D-10E506DA9D0F&displaylang=en) or later.
{{%/note%}}

**Supported Operating Systems:** Any operating system supported by .NET Framework 2.0.

**64 bit support:** XAP.NET is released in two separate packages for x86 and x64. Itanium (ia64) is currently not supported. The XAP.NET x86 package can be installed on a x64 machine and run in WoW64 mode.

#  Installation

GigaSpaces XAP.NET is packaged as a standard Windows Installer package (.msi file). To start the installation simply double-click the msi file, and the installation wizard will pop up and guide you through the installation process.

Once you accept the license agreement, you will be asked to choose a setup type. Select 'Complete' to install all the features in the default path (C:\GigaSpaces\XAP.NET 9.5.0). Selecting 'Custom' will allow you to customize the installation path, which features will be installed, and more.

## Other Installation Options

GigaSpaces XAP.NET offers more installation scenarios and customizations. For example:

- Command-line installation.
- Packaging XAP.NET in another installation package.
- Side-by-side installations.
- Using a different jvm.

For more information see [Advanced Installation Scenarios](./advanced-installation-scenarios.html).

