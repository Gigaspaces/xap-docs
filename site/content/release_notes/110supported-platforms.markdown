---
type: post
title:  Supported Platforms
categories: RELEASE_NOTES
parent: xap110.html
weight: 700
---

{{%ssummary%}}{{%/ssummary%}}
# Requirements

GigaSpaces InsightEdge and XAP are implemented using Java, and require the following:

- Java version 6, 7 or 8
- Any operating system that is supported by Java (for example Linux/Unix, Microsoft Windows, and Apple Mac OS/X)

XAP.NET requires [Microsoft .NET Framework 3.5](http://msdn.microsoft.com/en-us/vstudio/aa496123) or later. Additional information is provided in the [Installation](/xap/11.0/dev-dotnet/installation.html) section of the [XAP.NET Guide](/xap/11.0/dev-dotnet). 

For information on VMWare support, refer to the [VMWare guidelines](./110vmware-guidelines.html).

# Limitations

The following issues have been encountered in specific use cases:

- SUSE-10 Linux Enterprise Service Pack 3 has bugs that make the OS network layer unreliable. Do not use this operating system with GigaSpaces products.
- Java 8 and Java 7 (> u79): In these versions, the behavior of the multicast operations regarding IPv6 has changed, and this can cause long initial connection times. To mitigate this issue, add '-Djava.net.preferIPv4Stack=true' to the JVM arguments.

# XAP C++

GigaSpaces C++ source code can be built on Linux and Windows 32bit or 64bit machines.
The current supported platforms and compilers are:

- Linux
   * 64bit - gcc.4.1.2
   * 32bit - gcc.4.1.2
- Windows
   * 32/64bit C++ for Visual Studio 2008 and 2010 (VS9.0/10.0)

# Integrations with 3rd Party Products

Please refer to [Integrations with 3rd Party Products](/release_notes/101third-party.html)

