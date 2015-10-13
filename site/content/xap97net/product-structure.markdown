---
type: post97
title:  Product structure
categories: XAP97NET
parent: installation-overview.html
weight: 200
---





XAP .NET comes as a standalone product, that includes the following basic artifacts:



# Product Directory Structure

The default product location is **C:\GigaSpaces\XAP.NET \{Version\} \{Platform\}** (For example: C:\GigaSpaces\XAP.NET 8.0.6 x86). This convention supports side-by-side installation of different versions and platforms (x86, x64) of XAP.NET.

The product directory structure as follows (see Figure 1):

- **NET v2.0.50727** -- Contains XAP.NET for .NET 2.0
    - **Bin** -- Contains binaries (Executables and dll files) of the product.
        - **GigaSpaces.Core.dll** -- Main XAP.NET library. All XAP.NET applications reference this assembly.
        - **Gs-ui.exe** -- GigaSpaces Management Center.
        - **Gs-Agent.exe**, **Gsm.exe**, **Gsc.exe** - Service Grid executables.
        - **ServicesManager.exe** - GigaSpaces Services Manager.
    - **Config** -- Contains configuration files.
        - **Settings.xml** - Main product settings file.
    - **Deploy** -- Default location of Processing Units for Service Grid.
    - **Docs** -- Contains the documentation .chm files.
    - **Examples**  -- Contains examples demonstrating usage and features of XAP.NET.
    - **Practices** -- Contains the product practices.

- **NET v4.0.30319** -- Contains XAP.NET for .NET 4.0 (Same structure as NET v2.0.50727)
- **Runtime** -- Contains the Java part of the product as well as an embedded Java installation.

{{% indent %}}
![XapNetProductStructure_806.jpg](/attachment_files/dotnet/XapNetProductStructure_806.jpg)
{{% sub %}}**Figure 1. product directory structure**{{% /sub %}}
{{% /indent %}}

# Start Menu Links

The installer creates useful shortcuts in the start menu.

{{% indent %}}
![XapNetStartMenu_806.jpg](/attachment_files/dotnet/XapNetStartMenu_806.jpg)
{{% sub %}}**Figure 2. Shortcuts added to the Start Menu**{{% /sub %}}
{{% /indent %}}


