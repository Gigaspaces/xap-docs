---
type: post
title:  Supported Platforms
categories: RELEASE_NOTES
parent: xap101.html
weight: 700
---

{{%ssummary%}}{{%/ssummary%}}

GigaSpaces XAP is 100% Java technology based, and as such is supported on every operating system that supports the Java Platform Standard Edition technology - i.e., Windows , Linux x86, Linux AMD64 (Opteron), Oracle Solaris, Hewlett Packard HP-UX, IBM P-Series PowerPC AIX, Apple Mac OS/X, etc.

# Tested & Certified Platforms

The list below represents the platforms that are regularly tested by GigaSpaces.

### Operating Systems

GigaSpaces is being tested with the following operating systems (32bit and 64bit):

- Windows 2008 Server SP2
- Linux RHEL 5.x/6.x
- Solaris 10

For information on VMWare support please refer to [VMWare guidelines](/release_notes/101vmware-guidelines.html).

{{%warning%}}
SUSE-10 sp3 has bugs which make the OS network layer unreliable. This OS should be avoided with GigaSpaces.
{{%/warning%}}

### Java 

GigaSpaces is being tested with the following JVMs (32bit and 64bit):

- Oracle 6 - XAP was tested using Oracle JVM version 6u43 and above.
- Oracle 7 - XAP was tested using Oracle JVM version 7u45 and above.
- Oracle 8 - XAP was tested using Oracle JVM version 8u25 and above.
- IBM 1.6.0 - XAP was tested using IBM JVM version 1.6.0 SR15. 
- IBM 1.7.0 - XAP was tested using IBM JVM version 1.7.0 SR6.

{{%note title="Java SE 1.5 EOL"%}}
Based on information made publicly available by The Oracle Corporation (formerly Sun Microsystems), as of October 30th 2009, Java SE 1.5 SDK has reached its End of Service Life (EOSL). Oracle has already ceased to support the 1.5 JVM. In addition, the other major JVM vendor, namely IBM, announced its limited ability to support these JVMs in light of Oracle's announcement. This in turn will limit GigaSpaces' ability to provide support for applications running on this JVM. Furthermore, from version 8.0 onwards, GigaSpaces XAP no longer supports the Java 1.5 SDK, and requires the use of Java 1.6 SDK or higher.

Please refer to the public website page for the latest updates about the [JVM & Third-Party End-Of-Life Policy](http://www.gigaspaces.com/EOL).
{{%/note%}}

# XAP.NET

Required Software: [Microsoft .NET Framework 3.5](http://msdn.microsoft.com/en-us/vstudio/aa496123) or later including Microsoft .NET Framework 4.0.
Recommended: [Microsoft .NET Framework 4](http://www.microsoft.com/en-us/download/details.aspx?id=17851).

Supported Operating Systems: Any operating system supported by .NET Framework 3.5.

64 bit support: XAP.NET is released in two separate packages for x86 and x64. Itanium (ia64) is currently not supported. The XAP.NET x86 package can be installed on a x64 machine and run in WoW64 mode.

For more information see [XAP.NET](../xap101net/)

# XAP C++

GigaSpaces C++ source code can be built on Linux and Windows 32bit or 64bit machines.
The current supported platforms and compilers are:

- Linux
   * 64bit - gcc.4.1.2
   * 32bit - gcc.4.1.2
- Windows
   * 32/64bit C++ for Visual Studio 2008/2010 (VS9.0/VS10.0)

# Integrations with 3rd Party Products

Please refer to [Integrations with 3rd Party Products](/release_notes/101third-party.html)

