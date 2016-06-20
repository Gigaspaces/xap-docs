---
type: post120
title:  Advanced Scenarios
categories: XAP120NET
parent: installation-overview.html
weight: 400
---

# Customizing Java

#### Q. Can I configure XAP.NET to work with a different java version or location?

Yes. See [Jvm Configuration](./jvm-configuration.html#JvmLocation) for more information.

# Customizing XAP

#### Q. Can I configure XAP.NET to work with an existing XAP installation?

GigaSpaces XAP.NET is bundled with GigaSpaces XAP components required at runtime. Developers engaged in interoperability solutions may prefer working with a full installation of the Java XAP, which contains additional documentation, examples and tutorials.

If you wish to modify your XAP.NET installation to use an existing XAP installation, edit the `Settings.xml` file (located in `<ProductRoot>\Config`) and change the value of `<XapNet.Runtime.Path>` to the new location.

{{% note %}}
Mixing XAP.NET and XAP versions is not supported - always use the same version and build.
{{%/note%}}

# Automated Setup

#### Q. Can I run an automated, quiet install of XAP.NET from the command line?

Yes. From the command line, type the following:

```xml
C:\>msiexec /i {{%version "msi-filename" %}} /quiet
```

{{% refer %}}
For more information about installing `msi` packages from command line, see [http://msdn.microsoft.com/en-us/library/aa372024.aspx](http://msdn.microsoft.com/en-us/library/aa372024.aspx).
{{%/refer%}}

# Manual configuration without setup

#### Q. Can I package XAP.NET as a zip file instead of Windows Installer (msi)?

XAP.NET installation is released as an `msi` file because simply copying the file to the target machine is not enough - a few machine settings need to be configured, and the preferred method of doing this to date is Windows Installer.

Sometimes, however, you may want to use XAP.NET without running setup (e.g. on a production server).
To do that:

**Step 1.** Install XAP.NET on another machine (e.g. a developer's machine).

**Step 2.** Package the installed files into a zip file (or any other compression tool you prefer).

**Step 3.** Unzip the package on the target machine(s) wherever you prefer.

{{% note %}}
If you plan to use XAP.NET with .NET 4.0, make sure that the **Visual C++ 2010 Redistributable Package** [x86](http://www.microsoft.com/download/en/details.aspx?id=5555)  [x64](http://www.microsoft.com/download/en/details.aspx?id=14632)) is installed on the target machine (this is required only for manual installation - if the msi is installed the C++ redistribution package is installed automatically if needed).
{{%/note%}}

**Step 4.** The final touch is to configure the location of XAP.NET. 

This can be achieved in one of the following ways:

####  Environment variable
 
Create an environment variable named `XapNet_<version>_SettingsPath` which points to the settings file path. For example, for that same {{%version "xap-version"%}} version we would use:


```xml
XapNet_{{%version "xap-release"%}}.11600_SettingsPath=C:\GigaSpaces\XAP.NET {{%version "xap-release"%}} x64\NET v4.0\Config\Settings.xml
```

####  Windows Registry
 
Create a registry key named `HKLM\SOFTWARE\GigaSpaces\XAP.NET\<version>\<clrversion>`, with a String value named `SettingsPath` which points to the location of the `Settings.xml` file.

For example, the XAP.NET v{{%version "xap-version"%}} x64 setup creates the following keys:

```xml
HKLM\SOFTWARE\GigaSpaces\XAP.NET\{{%version "xap-release"%}}.11600\CLR v2.0.50727\SettingsPath=C:\GigaSpaces\XAP.NET {{%version "xap-release"%}} x64\NET v3.5\Config\Settings.xml
HKLM\SOFTWARE\GigaSpaces\XAP.NET\{{%version "xap-release"%}}.11600\CLR v4.0.30319\SettingsPath=C:\GigaSpaces\XAP.NET {{%version "xap-release"%}} x64\NET v4.0\Config\Settings.xml
```

{{% info %}}
HKCU is supported as well, and is searched before HKLM.
{{%/info%}}



####  Application configuration file: 

Use the `XapNetSettingsFile` element to configure the location of the settings file. For example:

```xml
<configuration>
    <configSections>
        <section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
    </configSections>
    <GigaSpaces>
        <XapNetSettingsFile Path="C:\GigaSpaces\XAP.NET {{%version "xap-release"%}} x64\NET v4.0\Config\Settings.xml"/>
    </GigaSpaces>
</configuration>
```

#### Code:
 
Use the following code to set the location of the settings file at runtime:

```csharp
    GigaSpacesFactory.Configuration.XapNetSettingsFile.Path = @"C:\GigaSpaces\XAP.NET {{%version "xap-release"%}} x64\NET v4.0\Config\Settings.xml"
```

#### Q. I need to minimize the installation size. Can I use a shared folder on a server to store part of the installation?

Yes. You can manually set the registry settings shown above to point both java and XAP to a shared folder on a remote server.

{{% note %}}
In such scenarios, the shared folder that contains XAP must be mapped as a network drive. XAP will fail to load if a network path is used (e.g. `\\server\share`).
{{%/note%}}

# Using GAC (Global Assembly Cache)

#### Q. My application uses the Global Assembly Cache to locate 3rd party assemblies. Which XAP.NET assemblies do I need to register?

The following XAP.NET assemblies should be registered when working with the Global Assembly Cache:

  * `Bin\GigaSpaces.Core.dll`

  * `Bin\GigaSpaces.NetToJava.dll`
