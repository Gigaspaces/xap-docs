---
type: post123
title:  Supported Platforms
categories: XAP123RN
parent: none
weight: 400
canonical: auto
---



# Requirements

GigaSpaces InsightEdge and XAP are implemented using Java, and require the following:

- Java version 8 or higher
- Any operating system that is supported by Java (for example Linux/Unix, Microsoft Windows, and Apple Mac OS/X)

XAP.NET requires [Microsoft .NET Framework 3.5](http://msdn.microsoft.com/en-us/vstudio/aa496123) or later. Additional information is provided in the [Installation](../dev-dotnet/installation-overview.html) section of the [XAP.NET Guide](../dev-dotnet). 

For information on VMWare support, refer to the [VMWare guidelines](vmware-guidelines.html).

# Limitations

The following issues have been encountered in specific use cases:

- SUSE-10 Linux Enterprise Service Pack 3 has bugs that make the OS network layer unreliable. Do not use this operating system with GigaSpaces products.
- Java 8: In these versions, the behavior of the multicast operations regarding IPv6 has changed, and this can cause long initial connection times. To mitigate this issue, add '-Djava.net.preferIPv4Stack=true' to the JVM arguments.

{{%note "Note"%}}
Java 9 is not currently supported with this version of InsightEdge and XAP due to changes in the Java platform. Java 9 is planned to be supported in a future release.
{{%/note%}}


# Lifecycle and End-of-Life Policy

## Java 

Refer to the [Oracle Java SE Support Roadmap](http://www.oracle.com/technetwork/java/eol-135779.html) for the latest information.
 
## XAP  

Refer to the [JVM & Third-Party End-Of-Life Policy](/release_notes/lifecycle.html) for the latest updates.
 


