---
type: post123
title:  System Configuration
categories: XAP123NET, PRM
parent: administrators-guide.html
weight: 200
canonical: auto
---

 



GigaSpaces XAP.NET offers a collection of system properties which can be used to tweak the system's behavior. For a list of system properties refer to [System Properties List](../admin/system-properties.html). This page discusses various options of defining and overriding system properties configuration.

# Using Application Configuration File

Like most .NET libraries, GigaSpaces XAP.NET configuration is based on {{%exurl "Application Configuration Files""http://msdn.microsoft.com/en-us/library/ms229689.aspx"%}}. To set system properties via the configuration file:

Step 1. Add an Application Configuration File to your project (if your project already contains such a file skip this step):


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
</configuration>
```

Step 2. Add a definition for GigaSpaces configuration section:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
   <configSections>
      <section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
   </configSections>
   <GigaSpaces>
      <!-- GigaSpaces Configuration settings are placed here -->
   </GigaSpaces>
</configuration>
```

Step 3. To add a system property, define a `SystemProperties` section in `GigaSpaces` section and add the desired properties names and values. The following example sets `com.gs.zones` to "foo":


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
   <configSections>
      <section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
   </configSections>
   <GigaSpaces>
      <SystemProperties>
         <add Name="com.gs.zones" Value="foo"/>
      </SystemProperties>
   </GigaSpaces>
</configuration>
```

# Reading and changing configuration at runtime

The Xml configuration is mapped to a .NET Object model, which can be viewed and changed at runtime. This can be useful for logging the configuration settings, setting a specific configuration based on some business logic, etc.

The configuration is loaded into `GigaSpacesFactory.Configuration`. If there's no application configuration file the default configuration is loaded. The following example reads and modifies the configuration:


```java
String zones = GigaSpacesFactory.Configuration.SystemProperties.GetPropertyValue("com.gs.zones");
if (zones == "foo")
    GigaSpacesFactory.Configuration.SystemProperties.SetPropertyValue("com.gs.zones", "bar");
```

{{% note %}}
Using `GigaSpacesFactory.Configuration` requires a reference to the `System.Configuration` assembly, which is not referenced by default.
{{%/note%}}

Configuration changes must be made **before** XAP.NET is initialized. Changes made afterwards will not affect the system, and may cause an exception. The `GigaSpacesFactory.IsInitialized` property can be used to determine if XAP.NET is initialized or not. For example:


```java
String zones = GigaSpacesFactory.Configuration.SystemProperties.GetPropertyValue("com.gs.zones");
if (zones == "foo")
{
    if (GigaSpacesFactory.IsInitialized)
        Console.WriteLine("Cannot change configuration because GigaSpaces is already initialized.");
    else
        GigaSpacesFactory.Configuration.SystemProperties.SetPropertyValue("com.gs.zones", "bar");
}
```

In addition, the `GigaSpacesFactory.Initialize()` method can be used to initialize XAP.NET explicitly and finalize changes (this is optional - if not called explicitly, this method will be called implicitly by GigaSpaces automatically when needed).

# Macros

Continuing the previous example, say we have not one but five applications which we want to configure with the same zones. We want to be able to change the zones in a single place which affects all those applications. This is where  macros are useful.

Macros are similar to system environment variables: a macro is a key-value pair which functions as a dynamic value, i.e. the key can be expanded/replaced by the value. This allows us to use the key in several places while the value is defined in a single place.

Macros are defined in the main settings file - `<XapNetFolder>\Config\Settings.xml`.

Let's edit the `Settings.xml` file, define a new macro for our app called MyApp.Zones, and set its value to foo:


```xml
<Settings>
    <!-- Out-of-the-box macros were omitted for brevity.  -->
    <MyApp.Zones>foo</MyApp.Zones>
</Settings>
```

Next we'll edit the application configuration file to use the macro instead of the static value:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
   <configSections>
      <section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
   </configSections>
   <GigaSpaces>
      <SystemProperties>
         <add Name="com.gs.zones" Value="$(MyApp.Zones)"/>
      </SystemProperties>
   </GigaSpaces>
</configuration>
```

GigaSpaces automatically scans property values for macros and replaces them with macro values.

# Base Configuration Files

Continuing the previous example, say we want to add more system properties to be shared by all applications. Instead of having to edit each application configuration file, we prefer a single configuration file which contains shared settings. This is where Base Configuration Files come handy.

The `GigaSpaces` configuration section contains a `BaseConfigFile` settings, which can be used to specify the location of a configuration file using the same schema which contains additional configuration that should be loaded. For example, We could put a configuration file called MyApp.config in a shared folder on a server which looks like this:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
   <configSections>
      <section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
   </configSections>
   <GigaSpaces>
      <SystemProperties>
         <add Name="com.gs.zones" Value="$(MyApp.Zones)"/>
         <add Name="someProperty1" Value="SomeValue1"/>
         <add Name="someProperty2" Value="SomeValue2"/>
      </SystemProperties>
   </GigaSpaces>
</configuration>
```

And then change the application configuration files to look like this:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
    <configSections>
        <section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
    </configSections>
    <GigaSpaces>
        <BaseConfigFile Path="MyServer\MyAppSharedFolder\MyApp.config" />
    </GigaSpaces>
</configuration>
```

### Overriding settings from a base configuration

Suppose that we have five applications using this base configuration file, and now we want to add a sixth application with one small difference - the property 'SomeProperty' should be 'OtherValue' and not 'SomeValue1'. What do we do?

The mechanism that merges the base configuration with the current configuration is smart enough to detect such overrides - if a property with the same name is defined both in the configuration file and in the base configuration file, the value from the base configuration is ignored, or overridden. This means we can use it to define common values and override them in specific cases.

