---
type: post120
title:  JVM Configuration
categories: XAP120NET
parent: administrators-guide.html
weight: 500
---

{{% ssummary %}} {{% /ssummary %}}

This page explains how to configure runtime settings for XAP.NET applications. In general, the development phase of a XAP.NET based application does not require runtime tuning settings as the defaults are usually satisfactory. In later testing and production phases, however, it is sometimes required to customize the default configuration.

{{% anchor JvmLocation %}}

# Jvm Location

GigaSpaces XAP.NET is bundled with [Oracle JDK 7 update 17](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html), aiming to save .NET users the hassle of downloading and installing Java. However, it is quite simple to bind it to a different JVM (For a list of supported JVMs refer to Supported Platforms in the release notes):

1. Edit the `Settings.xml` file (located in `<XapNet>\config\Settings.xml`).
2. Locate the `<XapNet.Runtime.JavaHome>` node and change its value to the location of the JVM you wish to use.

{{% tip "Using JAVA_HOME "%}}
Many systems define an environment variable called `JAVA_HOME` which points to the JVM installation, since most java applications and libraries (including GigaSpaces XAP) use `JAVA_HOME` to locate java. Since the `Settings.xml` file supports environment variable expansion, it is possible to set <XapNet.Runtime.JavaHome> to `%JAVA_HOME%`, so you can later on change the jvm location without editing the xml file.
{{% /tip %}}

{{% anchor JvmSettings %}}

# Jvm Settings

Unlike .NET applications, which are compiled to executable files, java applications are compiled to `.class` or `.jar` files which are executed using the [Java Application Launcher](http://java.sun.com/javase/6/docs/technotes/tools/windows/java.html) (`java.exe`) tool. This tool supports various options which control the way the application is executed, such as memory allocation, garbage collection and more.

GigaSpaces XAP.NET tools and applications use [Java Native Interface (JNI)](http://java.sun.com/javase/6/docs/technotes/guides/jni/index.html) instead of `java.exe` to launch the JVM and execute java code, and use the Application Configuration File to load JVM settings.

To configure JVM settings, add a `GigaSpaces` section to the application configuration file:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
   <configSections>
      <section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
   </configSections>
   <GigaSpaces>
      <JvmSettings>
         <!-- Jvm Settings -->
      </JvmSettings>
   </GigaSpaces>
</configuration>
```

The rest of this section explains and demonstrates the various JVM settings.

## JvmCustomOptions

The `JvmCustomOptions` section accepts a collection of custom options, similar to `java.exe` options. For example, this


```java
java.exe -XX:+AggressiveOpts
```

is equivalent to


```xml
<JvmSettings>
   <JvmCustomOptions>
      <add Option="-XX:+AggressiveOpts"/>
   </JvmCustomOptions>
</JvmSettings>
```

This element also has an attribute called `IgnoreUnrecognized`, which determines the behavior when an unrecognized option is encountered: `true` means ignore that option, `false` means throw an exception and abort. The default is `false`.

In general, this section provides equivalent functionality to `java.exe` and thus is sufficient. However, since most .NET users are not familiar with common java options, The XAP.NET JVM settings schema provides additional elements which act as aliases and are more readable to non-java users.

## JvmDll

The `java.exe` tool supports two mutually exclusive modes, called `-client` and `-server`, which determine what VM will be loaded. In fact, client and server are different implementations of the JVM residing in two separate `jvm.dll` files.

This option cannot be configured in `JvmCustomOptions` because it is not supported by `JNI`. Instead, `JvmDll` comes to the rescue. For example, this


```java
java.exe -server
```

is equivalent to


```xml
<JvmSettings>
   <JvmDll Mode="Server"/>
</JvmSettings>
```

the `Mode` attribute can be either Client, Server or Custom.
If `Mode` is set to `Client` or `Server`, the `<XapNet.Runtime.JavaHome>` (explained in Jvm Lovation]) is used to locate the java home, and either the client or server VM is selected according to the mode.
If `Mode` is set to `Custom`, the `<XapNet.Runtime.JavaHome>` is ignored, and the location of the jvm is determined by a `Path` attribute. for example:


```xml
<JvmSettings>
   <JvmDll Mode="Custom" Path="C:\Foo\MyJvm.dll"/>
</JvmSettings>
```

## JvmMemory

`java.exe` provides two options to control memory allocation: -Xms determines the initial heap size, and -Xms determines the maximum heap size. The `JvmSettings` section offers an alias section called `JvmMemory`. For example:

```xml
<JvmSettings>
   <JvmCustomOptions>
      <add Option="-Xms512m"/>
      <add Option="-Xmx1024m"/>
   </JvmCustomOptions>
   <!-- is equivalent to -->
   <JvmMemory InitialHeapSizeInMB="512" MaximumHeapSizeInMB="1024"/>
</JvmSettings>
```

**Note:** It is not recommended to define memory settings both as custom options and in the `JvmMemory` section, since the expected behavior is not clear.

## JvmClassPath

The [class path](http://java.sun.com/javase/6/docs/technotes/tools/windows/classpath.html) is the path that Java searches for classes and other resource files. It is usually specified as a semicolon-separated list of paths. The `JvmSettings` offers a more readable solution in the form of `JvmClassPath`. The following examples are equivalent:


```java
java.exe -classpath C:\Foo;C:\Bar
```


```java
java.exe -Djava.class.path=C:\Foo;C:\Bar
```


```xml
<JvmSettings>
   <JvmCustomOptions>
      <add Option="-Djava.class.path=C:\Foo;C:\Bar"/>
   </JvmCustomOptions>
</JvmSettings>
```


```xml
<JvmSettings>
   <JvmClassPath>
      <add Path="C:\Foo"/>
      <add Path="C:\Bar"/>
   </JvmClassPath>
</JvmSettings>
```

In addition to being more readable, the `JvmClassPath` has another perk: it supports wildcard-expansion. So, for example, we could specify `C:\Foo\*.jar` and it will be expanded to include all jar files in `C:\Foo` before being passed to java.

## JvmBootClassPath

Similar to the class path, which determines the location of user classes, the boot class path determines the location of java bootstrap classes. The `JvmSettings` offers a more readable solution in the form of `JvmBootClassPath`, similar to `JvmClassPath`. The following examples are equivalent:


```java
java.exe -Xbootclasspath/p:C:\Foo;C:\Bar
```


```xml
<JvmSettings>
   <JvmCustomOptions>
      <add Option="-Xbootclasspath/p:C:\Foo;C:\Bar"/>
   </JvmCustomOptions>
</JvmSettings>
```


```xml
<JvmSettings>
   <JvmBootClassPath>
      <add Path="C:\Foo"/>
      <add Path="C:\Bar"/>
   </JvmBootClassPath>
</JvmSettings>
```

Note that this alias is used to **prepend** boot class path (/p). `java.exe` also supports -Xbootclasspath/a, which is used to **append** boot class path. There's no alias for that in `JvmSettings`, but of course it can be used in the `JvmCustomOptions`.

`JvmBootClassPath` supports wildcard expansion, similar to `JvmClassPath`.
