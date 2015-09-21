---
type: post102
title:  Application Debugging
categories: XAP102NET
parent: installation-overview.html
weight: 600
---


In some cases, you might want to monitor the activity of the JVM running as part of your .NET application. The [jconsole](http://java.sun.com/j2se/1.5.0/docs/guide/management/jconsole.html) is a great tool that allows you to troubleshoot the JVM internals.

**To view and monitor the JVM loaded into the .NET process memory address using `jconsole`:**

1. Have the following settings as part of your `app.config` file:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces>
    <JvmSettings>
      <JvmCustomOptions IgnoreUnrecognized="false">
        <add Option="-Dcom.sun.management.jmxremote.port=5144"/>
        <add Option="-Dcom.sun.management.jmxremote.ssl=false"/>
        <add Option="-Dcom.sun.management.jmxremote.authenticate=false"/>
      </JvmCustomOptions>
    </JvmSettings>
  </GigaSpaces>
</configuration>
```

2. Start `jconsole` -- jconsole is located under the bin directory of the Java home, by default it is under `<Installation dir>\Runtime\java\bin`
3. Once the `jconsole` is started, select the **Local** tab:

{{% indent %}}
![jcon1.jpg](/attachment_files/dotnet/jcon11.jpg)
{{% /indent %}}

4. This shows the status of the JVM running in your .NET application:

![jcon2.jpg](/attachment_files/dotnet/jcon21.jpg)

{{% refer %}}For more details on JMX and `jconsole`, refer to:
- [Sun - Monitoring and Management Using JMX](http://java.sun.com/j2se/1.5.0/docs/guide/management/agent.html)
- [Sun - Using jconsole](http://java.sun.com/j2se/1.5.0/docs/guide/management/jconsole.html)
{{% /refer %}}
