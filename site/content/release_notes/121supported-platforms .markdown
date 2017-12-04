---
type: post
title:  Supported Platforms
categories: RELEASE_NOTES
parent: xap121.html
weight: 700
---

{{%ssummary%}}{{%/ssummary%}}

# Requirements

GigaSpaces InsightEdge and XAP are implemented using Java, and require the following:

- Java version 7 or 8
- Any operating system that is supported by Java (for example Linux/Unix, Microsoft Windows, and Apple Mac OS/X)

XAP.NET requires [Microsoft .NET Framework 3.5](http://msdn.microsoft.com/en-us/vstudio/aa496123) or later. Additional information is provided in the [Installation](/xap/12.1/dev-dotnet/installation.html) section of the [XAP.NET Guide](/xap/12.1/dev-dotnet). 

For information on VMWare support, refer to the [VMWare guidelines](./121vmware-guidelines.html).

# Limitations

The following issues have been encountered in specific use cases:

- SUSE-10 Linux Enterprise Service Pack 3 has bugs that make the OS network layer unreliable. Do not use this operating system with GigaSpaces products.
- Java 8 and Java 7 (> u79): In these versions, the behavior of the multicast operations regarding IPv6 has changed, and this can cause long initial connection times. To mitigate this issue, add '-Djava.net.preferIPv4Stack=true' to the JVM arguments.

# Integrations with 3rd Party Products

Please refer to [Integrations with 3rd Party Products](/release_notes/121third-party.html)

