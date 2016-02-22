---
type: post110
title:  Environment Variables
categories: XAP110
parent: installation-java-overview.html
weight: 200
---

The XAP environment configuration is maintained by a configuration script file called `setenv`, located in the `XAP_HOME/bin` directory. It is recommended to use the `setenv` utility to derive the commonly used GigaSpaces libraries and setup environment. To use this utility, you simply need to call it from your script file.

The following list describes commonly used variables which are defined in this script:

|Name|Description|
|:---|:----------|
|  JAVA_HOME  |The directory in which Java is installed|
|  XAP_HOME  |The GigaSpaces XAP home directory|
|  XAP_LOOKUP_GROUPS  |Lookup Service groups used for multicast discovery|
|  XAP_LOOKUP_LOCATORS  | Lookup Service Locators used for unicast discovery|
|  XAP_NIC_ADDRESS  | The NIC\_ADDR represents the specific network interface card IP address or the default host name. When using Multi Network-Interface cards, explicitly set it with the NIC address (e.g. 192.168.0.2) as it is the value of the the java.rmi.server.hostname system property. |
|  XAP_SECURITY_POLICY  |The default policy file.|

{{% info %}} The default value of the `XAP_LOOKUP_GROUPS` variable is the GigaSpaces version number, preceded by `xap`. For example, in GigaSpaces XAP 11.0.0, the default lookup group is `xap-11.0.0`. This is the lookup group which the space register with, and which clients use by default to connect to the space.{{%/info%}}
