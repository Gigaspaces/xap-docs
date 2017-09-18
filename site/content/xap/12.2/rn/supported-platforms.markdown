---
type: post122
title:  Supported Platforms
categories: XAP122RN
parent: none
weight: 400
---

{{%ssummary%}}{{%/ssummary%}}

GigaSpaces InsightEdge and XAP are implemented using Java, and as such are supported on every operating system that supports the Java Platform Standard Edition technology - Linux/Unix, Windows, Apple Mac OS/X, Oracle Solaris, Hewlett Packard HP-UX, IBM P-Series PowerPC AIX, etc.

# Tested & Certified Platforms

The list below represents the platforms that are regularly tested by GigaSpaces.

### Operating Systems

GigaSpaces is being tested with the following operating systems (32bit and 64bit):

- Windows 2008 Server SP2
- Linux RHEL 5.x/6.x
- CentOS 7-7.1
- Solaris 10

For information on VMWare support please refer to [VMWare guidelines](vmware-guidelines.html).

{{%warning%}}
SUSE-10 sp3 has bugs which make the OS network layer unreliable. This OS should be avoided with GigaSpaces.
{{%/warning%}}

### Java 

GigaSpaces is being tested with the following JVMs (32bit and 64bit):

- Oracle 7 - XAP was tested using Oracle JVM version 7u45 and above.
- Oracle 8 - XAP was tested using Oracle JVM version 8u25 and above.
- IBM 1.7.0 - XAP was tested using IBM JVM version 1.7.0 SR6.

{{%warning "Java8"%}}
Java8 and Java7 (> u79) have change multicast operations with regards to IPv6 and this can cause long initial connection times. Please add '-Djava.net.preferIPv4Stack=true' to the JVM arguments.
{{%/warning%}}

# Lifecycle and End-of-Life Policy

## Java 

Refer to [Oracle Java SE Support Roadmap](http://www.oracle.com/technetwork/java/eol-135779.html) for the latest information.
 
## XAP  

Refer to [JVM & Third-Party End-Of-Life Policy](./lifecycle.html) for the latest updates.
 
# XAP.NET

* Required Software: [Microsoft .NET Framework 3.5](http://msdn.microsoft.com/en-us/vstudio/aa496123) or later including Microsoft .NET Framework 4.0.
* Recommended: [Microsoft .NET Framework 4](http://www.microsoft.com/en-us/download/details.aspx?id=17851).

Supported Operating Systems: Any operating system supported by .NET Framework 3.5.

For more information see [XAP.NET Guide](../dev-dotnet)

# Integrations with 3rd Party Products

Please refer to [Integrations with 3rd Party Products](third-party.html)

