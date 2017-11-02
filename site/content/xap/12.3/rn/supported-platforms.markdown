---
type: post123
title:  Supported Platforms
categories: XAP123RN
parent: none
weight: 400
---

{{%ssummary%}}{{%/ssummary%}}

GigaSpaces InsightEdge and XAP are implemented using Java, and as such are supported on every operating system that supports the Java Platform Standard Edition technology - Linux/Unix, Microsoft Windows, Apple Mac OS/X, Oracle Solaris, Hewlett Packard HP-UX, IBM P-Series PowerPC AIX, etc.

# Tested & Certified Platforms

This section lists the platforms that are regularly tested by GigaSpaces.

## Operating Systems

GigaSpaces products are tested with the following operating systems (32-bit and 64-bit):

- Microsoft Windows Server 2008 SP2
- Linux RHEL 5.x/6.x
- CentOS 7-7.1
- Solaris 10

For information on VMWare support, refer to the [VMWare guidelines](vmware-guidelines.html).

{{%warning "Important"%}}
SUSE-10 Linux Enterprise Service Pack 3 has bugs that make the OS network layer unreliable. Do not use this operating system with GigaSpaces products.
{{%/warning%}}

## Java 

GigaSpaces products are tested with the following JVMs (32-bit and 64-bit):

- Oracle 7: XAP was tested using Oracle JVM version 7u45 and above.
- Oracle 8: XAP was tested using Oracle JVM version 8u25 and above.
- IBM 1.7.0: XAP was tested using IBM JVM version 1.7.0 SR6.

{{%warning "Important"%}}
* Java 8 and Java 7 (> u79): In these versions, the behavior of the multicast operations regarding IPv6 has changed, and this can cause long initial connection times. To mitigate this issue, add '-Djava.net.preferIPv4Stack=true' to the JVM arguments.
* Java 9: Not currently supported with this version of InsightEdge and XAP due to changes in the Java platform. Java 9 is planned to be supported in a future release.
{{%/warning%}}

## XAP.NET Requirements

{{%note "Note"%}}
For more information about running XAP in a .NET environment with third-party products, refer to the [XAP.NET Guide](../dev-dotnet).
{{%/note%}}

To install and run XAP using .NET, observe the following third-party guidelines and requirements:

* Required Software: [Microsoft .NET Framework 3.5](http://msdn.microsoft.com/en-us/vstudio/aa496123) or later, including Microsoft .NET Framework 4.0.
* Recommended: [Microsoft .NET Framework 4](http://www.microsoft.com/en-us/download/details.aspx?id=17851).

### Supported Operating Systems

XAP.NET is certified with any operating system that is supported by Microsoft .NET Framework 3.5.

# Lifecycle and End-of-Life Policy

## Java 

Refer to the [Oracle Java SE Support Roadmap](http://www.oracle.com/technetwork/java/eol-135779.html) for the latest information.
 
## XAP  

Refer to the [JVM & Third-Party End-Of-Life Policy](/release_notes/lifecycle.html) for the latest updates.
 


