---
type: post97
title:  Environment Variables
categories: XAP97
parent: installation-java.html
weight: 100
---


The XAP environment configuration is maintained by a configuration script file. This script is located in the `<GigaSpaces Root>\bin` directory:

- Windows: `setenv.bat`
- UNIX: `Setenv.sh`

Below is a list of some of a selected list of commonly used variables that are defined in this script:


|Name|Description|
|:---|:----------|
| `JAVA_HOME` |The directory in which Java is installed.|
| `JAVACCMD` |The Java compile command variable.|
| `JAVAWCMD` |The JavaW process.|
| `JSHOMEDIR` |The GigaSpaces home directory.|
| `LOOKUPGROUPS` |Jini Lookup Service group.|
| `LOOKUPLOCATORS` | Jini Lookup Service Locators used for unicast discovery.|
| `NIC_ADDR` | The NIC\_ADDR represents the specific network interface card IP address or the default host name. When using Multi Network-Interface cards, explicitly set it with the NIC address (e.g. 192.168.0.2) as it is the value of the the java.rmi.server.hostname system property. (see RMI\_OPTIONS).|
| `GS_JARS` | The GS_JARS contains the same list as defined in the Class-Path entry of the JSpaces.jar manifest file. These jars are required for client application and starting a Space from within your application.|
| `POLICY` |The default policy file.|
| `PRODUCTION_MODE` |Indicates if GigaSpaces Server will be started in Production mode (default to the production mode).|
| `RMI_OPTIONS` |Additional RMI optional properties such as GC. In a setup for Multi Network-Interface cards please append -Djava.rmi.server.hostname="%NIC\_ADDR%" with proper network-interface IP address to the RMI\_OPTIONS|
| `JAVA_VM_NAME` |Vendor of the JVM (i.e. All, BEA, HP, IBM, Sun, etc.). Default is `ALL`, meaning general settings.|
| `ECLIPSE_REMOTE_DEBUG` |For remote Eclipse debugging add the `ECLIPSE_REMOTE_DEBUG` variable to the application command line.|
| `REMOTE_JMX` |Enable monitoring and management from remote systems using JMX jconsole.|

{{% info %}} The default value of the `LOOKUPGROUPS` variable is the GigaSpaces version number, preceded by `XAP`. For example, in GigaSpaces XAP 6.0, the default lookup group is `gigaspaces-6.0XAP`. This is the lookup group which the space and Jini Transaction Manager register with, and which clients use by default to connect to the space.{{%/info%}}

# Using the setenv Utility

It is recommended to use the `setenv` utility to derive the commonly used GigaSpaces libraries and setup environment. To use this utility, you simply need to call it from your script file.
