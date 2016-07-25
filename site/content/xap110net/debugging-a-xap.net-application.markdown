---
type: post110
title:  Application Debugging
categories: XAP110NET
parent: installation-overview.html
weight: 600
---


In some cases, you might want to monitor the activity of the JVM running as part of your .NET application. The [jconsole](http://java.sun.com/j2se/1.5.0/docs/guide/management/jconsole.html) is a great tool that allows you to troubleshoot the JVM internals.

# Opening the JMX port

The following is used to open the JMX port to view and monitor the JVM loaded into the .NET process memory address.

Have the following settings as part of your `app.config` file:

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

See [.NET JVM Configuration]({{%currentneturl}}/jvm-configuration.html) for more details.

# Viewing in JConsole
1. Start `jconsole` -- jconsole is located under the bin directory of the Java home, by default it is under `<Installation dir>\Runtime\java\bin`
2. Once the `jconsole` is started, select the **Local** tab:

{{% align center %}}
![jcon1.jpg](/attachment_files/dotnet/jcon11.jpg)
{{% /align %}}

3. This shows the status of the JVM running in your .NET application:

{{%align center%}}
![jcon2.jpg](/attachment_files/dotnet/jcon21.jpg)
{{%/align%}}

{{% refer %}}For more details on JMX and `jconsole`, refer to:
- [Sun - Monitoring and Management Using JMX](http://java.sun.com/j2se/1.5.0/docs/guide/management/agent.html)
- [Sun - Using jconsole](http://java.sun.com/j2se/1.5.0/docs/guide/management/jconsole.html)
{{% /refer %}}

# Viewing in JVisualVM

As an alternative to viewing in JConsole, you can also use JVisualVM.

1. Start JVisualVM. The default location is the `C:\GigaSpaces\XAP.NET-{{%version "xap-release"%}}-x64\Runtime\Java` directory.
2. Connect via JMX. Go to File | Add JMX Connection...
{{%align center%}}
![jvisualvm-connect.png](/attachment_files/dotnet/jvisualvm-connect.png)
{{%/align%}}
3. Enter the Connection: `localhost:5144`
4. For thread dumps, go to the "Threads" tab, and click on the "Thread Dump" button.
{{%align center%}}
![jvisualvm-thread1.png](/attachment_files/dotnet/jvisualvm-thread1.png)
{{%/align%}}
Or:
Right click on the application in the left pane, and select "Thread Dump".
{{%align center%}}
![jvisualvm-thread2.png](/attachment_files/dotnet/jvisualvm-thread2.png)
{{%/align%}}
5. Similarly for heap dumps, go to the "Monitor" tab, click on the "Heap Dump" button. Or right click on the application in the left pane and select "Heap Dump".
