---
type: post
title:  Supported Platforms
categories: RELEASE_NOTES
parent: xap97.html
weight: 700
---

{{%ssummary%}}{{%/ssummary%}}

# Overview

GigaSpaces XAP Data-Grid , Messaging-Grid , Compute-Grid , local cache , remoting , Persistency, Service Grid and OpenSpaces API are 100% Java technology based.

GigaSpaces XAP is supported on every operating system that supports the Java Platform Standard Edition technology - i.e., Windows , Linux x86, Linux AMD64 (Opteron), Oracle Solaris, Hewlett Packard HP-UX, IBM P-Series PowerPC AIX, Apple Mac OS/X, etc.

The list below represents the platforms that are regularly tested by GigaSpaces.

{{%info%}}
GigaSpaces components (space, cluster of spaces, Processing Unit, GSM, GSC, LUS, Mahalo, GUI, CLI) can run only with the same GigaSpaces JARs (i.e., the same version and build number).{{%/info%}}

# Mixing XAP Versions
The following is supported:

- Runtime backward compatibility: Starting from GigaSpaces 7.1, GigaSpaces Space instances are backward compatible with Space clients across major versions. That means that clients running on version 8.0, 9.0 or 9.1 are compatible with Space instances running on version 9.7.
- Binary compatibility: applications built using 8.0.x or higher run without any code changes on a clean 9.7.x installation.
- Servers (GSMs, GSCs and Space instances) running on any future service pack of version 9.7 (e.g. 9.7.1) are guaranteed to work with older service packs of that version (e.g. 9.7.0).


# Tested & Certified Platforms

Recommended and Certified Environment

GigaSpaces is being tested with (32bit and 64bit JVMs):

- Windows 2008 Server SP2
- Linux RHEL 5.x/6.x
- Solaris 10
- [VMWare guidelines](/release_notes/97vmware-guidelines.html)

{{%warning%}}
SUSE-10 sp3 has bugs which make the OS network layer unreliable. This OS should be avoided with GigaSpaces.
{{%/warning%}}

Tested JVMs:

- Oracle 6 - XAP was tested using Sun JVM version 6u43 and above.
- Oracle 7 - XAP was tested using Sun JVM version 7u45 and above.
- IBM 1.6.0 - XAP was tested using IBM JVM version 1.6.0 IBM J9VM. 
- IBM 1.7.0 - XAP was tested using IBM JVM version 1.7.0 IBM J9VM SR6.


{{%note%}}
Using IBM jdk there might be a problem in xml parsing package bundled with jdk,in previous GS version the xml parsing package was bundled with the product. Starting with 10.1 this is no longer the case, so in case of a problem 'working' xml parsing package should be added to GS boot class path.
{{%/note%}}

{{%note%}}
Since November 2013 we discovered a bug in version 1.6 and forward:
String.equals() may return inconsistent values in concurrent applications
There is an open ticked for this issue: PMR 65006,999,706
We therefore do not recommend using IBM JDK 1.6\1.7 
{{%/note%}}


GigaSpaces recommends that customers upgrade to a fully-supported environment, such as the latest GigaSpaces XAP 9.7.x and the latest *Java 1.6/1.7 SDK.

# End-of-Life Java Versions

- Java SE 1.5 EOL - based on information made publicly available by The Oracle Corporation (formerly Sun Microsystems), as of October 30th 2009, Java SE 1.5 SDK has reached its End of Service Life (EOSL).
- Oracle has already ceased to support the 1.5 JVM. In addition, the other major JVM vendor, namely IBM, announced its limited ability to support these JVMs in light of Oracle's announcement. This in turn will limit GigaSpaces' ability to provide support for applications running on this JVM.
- From version 8.0 onwards, GigaSpaces XAP no longer supports the Java 1.5 SDK, and will require the use of Java 1.6 SDK or higher.

{{%note%}}Please refer to the public website page for the latest updates about the [JVM & Third-Party End-Of-Life Policy](http://www.gigaspaces.com/EOL).{{%/note%}}

# Product Integrations


| Product | Product Page | Version | Component | Documentation
|-------|--------|--------|-------|----------|
| Cassandra                | [Cassandra](http://cassandra.apache.org/)                                             | 1.1.6 | Cassandra archiver and Cassandra EDS | [Cassandra Integration](/xap/9.7/dev-java/cassandra.html) |
| Hibernate                | [Hivernate](http://www.hibernate.org/)                                                   | 3.6.1 | Persistency                          | [Hibernate Space Persistency](/xap/9.7/dev-java/hibernate-space-persistency.html) |
| log4j                    | [log4j](http://logging.apache.org/log4j/1.2/)                             | 1.2.17 | Hibernate                           |      | 
| log4j-snmp-trap-appender | [log4j-snmp-trap-appender](http://code.google.com/p/log4j-snmp-trap-appender/) | 1.2.9 | Alert integration example            | [SNMP Connectivity via Alert Logging Gateway](/xap/9.7/dev-java/snmp-connectivity-via-alert-logging-gateway.html)  |
| MongoDB                  | [MongoDB](http://www.mongodb.org/)                                                       | 2.11.2 | MongoDB archiver and MongoDB EDS    | [MongoDB Integration](/xap/9.7/dev-java/mongodb.html) |
| Mule                     | [Mule](http://www.mulesoft.org/)                                                     | 3.3.0 | XAP Mule PU                          | [Mule ESB](/xap/9.7/dev-java/mule-esb.html) |
| Scala                    | [Scala](http://oss.sonatype.org/content/groups/scala-tools/) | 2.10.1 | Express common programming patterns in a concise | [Scala](/xap/9.7/dev-java/scala.html) | 
| snmp4j                   | [snmp4j](http://www.snmp4j.org/)                                                          | 1.11.2 | Alert integration example   | [SNMP Connectivity via Alert Logging Gateway](/xap/9.7/dev-java/snmp-connectivity-via-alert-logging-gateway.html) | 


# .NET Interface

### System Requirements
Required Software: [Microsoft .NET Framework 2.0](http://msdn.microsoft.com/en-us/vstudio/aa496123) or later including Microsoft .NET Framework 4.0.
Recommended: [Microsoft .NET Framework 2.0 Service Pack 2](http://www.microsoft.com/en-us/download/details.aspx?id=1639) or later.

Supported Operating Systems: Any operating system supported by .NET Framework 2.0.

64 bit support: XAP.NET is released in two separate packages for x86 and x64. Itanium (ia64) is currently not supported. The XAP.NET x86 package can be installed on a x64 machine and run in WoW64 mode.

### Installation
GigaSpaces XAP.NET is packaged as a standard Windows Installer package (.msi file). To start the installation simply double-click the msi file, and the installation wizard will pop up and guide you through the installation process.

Once you accept the licence agreement, you will be asked to choose a setup type. Select 'Complete' to install all the features in the default path (C:\GigaSpaces\XAP.NET 9.7.0). Selecting 'Custom' will allow you to customize the installation path, which features will be installed, and more.

### Other Installation Options
GigaSpaces XAP.NET offers more installation scenarios and customizations. For example:

- Command-line installation.
- Packaging XAP.NET in another installation package.
- Side-by-side installations.
- Using a different jvm.

For more information see Advanced Installation Scenarios.

# C++ Interface
GigaSpaces C++ source code can be built on Linux and Windows 32bit or 64bit machines.
The current supported platforms and compilers are:

- Linux
   * 64bit - gcc.4.1.2
   * 32bit - gcc.4.1.2
- Windows
   * 32/64bit C++ for VisualStudio 2008/2010 (VS9.0/VS10.0)


# Integrations with 3rd Party Products

[Integrations with 3rd Party Products](/release_notes/97third-party.html)
