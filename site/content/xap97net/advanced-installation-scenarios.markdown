---
type: post97
title:  Advanced Scenarios
categories: XAP97NET
parent: installation-overview.html
weight: 100
---

{{% ssummary %}} {{% /ssummary %}}



GigaSpaces XAP.NET takes advantage of several runtime components from GigaSpaces XAP (and, as a consequence, Java). In most cases, this is transparent to the user, since the XAP.NET installation process includes the required Java and XAP files. However, in some cases, users may wish to use an existing Java or XAP installation (e.g. developing interoperability solutions, using both XAP and XAP.NET). This page explains how to install XAP.NET in such scenarios.

# Customizing Java

#### Q. Can I install XAP.NET using an existing java installation?

Yes. See [Jvm Configuration](./jvm-configuration.html#JvmLocation) for more information.

#### Q. I've already installed XAP.NET. Can I configure it to work with a different java version or location?

Yes. See [Jvm Configuration](./jvm-configuration.html#JvmLocation) for more information.

# Customizing XAP

#### Q. How do I install XAP.NET using an existing XAP installation?

GigaSpaces XAP.NET is bundled with GigaSpaces XAP components required at runtime. Developers engaged in interoperability solutions may prefer working with a full installation of the Java XAP, which contains additional documentation, examples and tutorials.
If you wish XAP.NET to use an existing XAP installation instead of creating an additional installation, follow these steps:

1. Start XAP.NET installation. Review and accept the license.
2. In the **Choose Setup Type** dialog, select **Custom** and click **Next** to continue.
3. In the **Custom Setup** dialog, expand the **Core Components** node in the feature tree, click **Built-in XAP Runtime**, and select **Entire feature will be unavailable**. Click **Next** to continue.
4. In the **XAP Runtime Components Installation Path** groupbox, type the location of your installed XAP, and click **Next** to continue.
5. Click **Install** to start the installation process.

#### Q. I've already installed XAP.NET. Can I configure it to work with a different XAP location?

Yes. Edit the `Settings.xml` file (located in `<ProductRoot>\Config`) and change the value of `<XapNet.Runtime.Path>` to the new location.

{{% note %}}
Mixing XAP.NET and XAP versions is not supported - always use the same version and build.
{{%/note%}}

# Automated Setup

#### Q. Can I run an automated, quiet install of XAP.NET from the command line?

Yes. From the command line, type the following:
`C:\>msiexec /i GigaSpaces-XAP.NET-9.0.0.5000-GA-x86.msi /quiet`

{{% info %}}
For more information about installing msi packages from command line, see [http://msdn.microsoft.com/en-us/library/aa372024(VS.85).aspx](http://msdn.microsoft.com/en-us/library/aa372024(VS.85).aspx).
{{%/info%}}

# Manual configuration without setup

#### Q. Can I package XAP.NET as a zip file instead of Windows Installer (msi)?

XAP.NET installation is released as an `msi` file because simply copying the file to the target machine is not enough - a few machine settings need to be configured, and the preferred method of doing this to date is Windows Installer.

Sometimes, however, you may want to use XAP.NET without running setup (e.g. on a production server).
To do that:

Step 1. Install XAP.NET on another machine (e.g. a developer's machine).
Step 2. Package the installed files into a zip file (or any other compression tool you prefer).
Step 3. Unzip the package on the target machine(s) wherever you prefer.

{{% note %}}
If you plan to use XAP.NET with .NET 4.0, make sure that the **Visual C++ 2010 Redistributable Package** [x86](http://www.microsoft.com/download/en/details.aspx?id=5555)  [x64](http://www.microsoft.com/download/en/details.aspx?id=14632)) is installed on the target machine (this is required only for manual installation - if the msi is installed the C++ redistribution package is installed automatically if needed).
{{%/note%}}

The final touch is to configure the location of XAP.NET. This can be achieved in one of the following ways:

Step 4. **Windows Registry:** Create a registry key named `HKLM\SOFTWARE\GigaSpaces\XAP.NET\<version>\<clrversion>`, with a String value named `SettingsPath` which points to the location of the `Settings.xml` file.

For example, the XAP.NET v9.5 x86 setup creates the following keys:
HKLM\SOFTWARE\GigaSpaces\XAP.NET\9.5.0.5000\CLR v2.0.50727\SettingsPath=C:\GigaSpaces\XAP.NET 9.5.0 x86\NET v2.0.50727\Config\Settings.xml

HKLM\SOFTWARE\GigaSpaces\XAP.NET\9.5.0.5000\CLR v4.0.30319\SettingsPath=C:\GigaSpaces\XAP.NET 9.5.0 x86\NET v4.0.30319\Config\Settings.xml

{{% info %}}
Starting 8.0.3 the HKCU is supported as well, and is searched before HKLM.
{{%/info%}}

Step 5. **Environment variable:** Create an environment variable named `XapNet_<version>_SettingsPath` which points to the settings file path. For example, for that same 9.5 version we would use:

XapNet_9.5.0.5000_SettingsPath=C:\GigaSpaces\XAP.NET 9.5.0 x86\NET v2.0.50727\Config\Settings.xml.

Step 6. **Application configuration file**: Use the XapNetSettingsFile element to configure the location of the settings file. For example:

```xml
<configuration>
    <configSections>
        <section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
    </configSections>
    <GigaSpaces>
        <XapNetSettingsFile Path="C:\GigaSpaces\XAP.NET 9.5.0 x86\NET v2.0.50727\Config\Settings.xml"/>
    </GigaSpaces>
</configuration>
```

Step 7. **Code:** Use the following code to set the location of the settings file at runtime:

```csharp
    GigaSpacesFactory.Configuration.XapNetSettingsFile.Path = @"C:\GigaSpaces\XAP.NET 9.5.0 x86\NET v2.0.50727\Config\Settings.xml"
```

#### Q. I need to minimize the installation size. Can I use a shared folder on a server to store part of the installation?

Yes. You can manually set the registry settings shown above to point both java and XAP to a shared folder on a remote server.

{{% note %}}
In such scenarios, the shared folder that contains XAP must be mapped as a network drive. XAP will fail to load if a network path is used (e.g. server\share).
{{%/note%}}

# Using GAC (Global Assembly Cache)

#### Q. My application uses the Global Assembly Cache to locate 3rd party assemblies. Which XAP.NET assemblies do I need to register?

Step 1. Register the following XAP.NET assemblies in the Global Assembly Cache:
    1. `Bin\GigaSpaces.Core.dll`
    2. `Bin\GigaSpaces.NetToJava.dll`

Step 2. Modify the GigaSpaces section definition in `Config\Default.config` and `Config\DefaultApp.config` to specify a strong-name for GigaSpaces.Core:


```csharp
<section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
```

Should be replaced it with:


```csharp
<section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core, Version=9.5.0.5000, Culture=neutral, PublicKeyToken=94297b57ee0e4ad5"/>
```

{{% info %}}
The `Version` should be set according to the version of XAP.NET in use (For example, if you're using XAP.NET 9.5 set the version to 9.5.0.5000).
{{%/info%}}
