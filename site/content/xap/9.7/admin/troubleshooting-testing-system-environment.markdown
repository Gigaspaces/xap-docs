---
type: post97
title:  Testing System Environment
categories: XAP97ADM
parent: troubleshooting.html
weight: 200
---

{{% ssummary%}}{{% /ssummary %}}


To perform a simple system testing you should use the `gsInstance.bat` script on windows or `gsInstance.sh` script on linux.

{{% tip %}}
To setup a production environment see the [Moving into Production Checklist](./moving-into-production-checklist.html).
{{% /tip %}}

# Verifying Local Installation

1. Run a single GigaSpaces space instance by moving to the `<GigaSpaces Root>\bin` directory and running the `gsInstance.bat/sh` command. You should see such an output:


```bash
D:\gigaspaces-xap-premium-8.0.1-ga\bin>gsInstance.bat
Starting a Space Instance
Setting space url to "/./mySpace?schema=default&properties=gs&groups="gigaspaces-8.0.1-XAPPremium-ga""
java version "1.6.0_24"
Java(TM) SE Runtime Environment (build 1.6.0_24-b07)
Java HotSpot(TM) 64-Bit Server VM (build 19.5-b02, mixed mode)

Log file: D:\gigaspaces-xap-premium-8.0.1-ga\bin\..\logs\2011-06-19~01.21-gigaspaces-service-192.168.1.101-12840.log
2011-06-19 01:21:03,458  INFO [com.gigaspaces.common.resourceloader] - Loading properties file from:
file:/D:/gigaspaces-xap-premium-8.0.1-ga/config/gs.properties
2011-06-19 01:21:03,896  INFO [com.gigaspaces.container] -

 System Environment:
         System:
                 OS Name: Windows 7
                 OS Version: 6.1
                 Architecture: amd64
                 Number Of Processors: 4
         JVM Details:
                 Java Version: 1.6.0_24, Sun Microsystems Inc.
                 Java Runtime: Java(TM) SE Runtime Environment (build 1.6.0_24-b07)
                 Java VM: Java HotSpot(TM) 64-Bit Server VM 19.5-b02, Sun Microsystems Inc.
                 Java Home: d:\java\jdk1.6.0_24\jre
         JVM Memory:
                 Max Heap Size (KB): 466048
                 Current Allocated Heap Size (KB): 116451
         Network Interfaces Information:
                 Host Name: [my-PC]
                 Network Interface Name: lo / Software Loopback Interface 1
                 IP Address: 0:0:0:0:0:0:0:1
                 IP Address: 192.168.1.101
	 Zones: N/A
         Process Id: 12840
         GigaSpaces Platform:
                 Edition: XAP Premium 8.0.1 GA
                 Build: 5200
                 Home: D:\gigaspaces-xap-premium-8.0.1-ga\bin
  ..\

2011-06-19 01:21:04,231  INFO [com.gigaspaces.container] - Created RMIRegistry on: < 192.168.1.101:10098 >
2011-06-19 01:21:04,244  INFO [com.gigaspaces.container] - Webster HTTP server started successfully serving the
following roots: D:\gigaspaces-xap-premium-8.0.1-ga\bin
  ..\/lib;D:\gigaspaces-xap-premium-8.0.1-ga\bin
  ..\/lib/jini Webster
serving on: 192.168.1.101:9813
2011-06-19 01:21:04,741  INFO [com.sun.jini.reggie] - started Reggie: ab0f28be-5829-4566-861e-4ccad1da1c50,
[gigaspaces-8.0.1-XAPPremium-ga], jini://192.168.1.101:4166/
2011-06-19 01:21:05,046  INFO [com.gigaspaces.core.common] - Starting Space [mySpace_container:mySpace] with url
[/./mySpace?schema=default&properties=gs&groups=gigaspaces-8.0.1-XAPPremium-ga&state=started] ...
2011-06-19 01:21:05,515  INFO [com.gigaspaces.cache] - Cache manager created with policy [ALL IN CACHE],
persistency mode [memory]
2011-06-19 01:21:05,703  INFO [com.sun.jini.mahalo.startup] - Mahalo started: com.sun.jini.mahalo.TransientMahaloImpl@31bd669d
2011-06-19 01:21:05,712  INFO [com.gigaspaces.core.common] - Space [mySpace_container:mySpace] with url
[/./mySpace?schema=default&properties=gs&groups=gigaspaces-8.0.1-XAPPremium-ga&state=started] started successfully
```

1. Make sure it loads without errors/exceptions. If you have error/exceptions check the following:
    - `JAVA_HOME` environment variable - Make sure it points to a valid **JDK** folder.
    - Network setup - Make sure the machine has a valid network interface installed with a valid IP.
    - hosts file - Make sure it includes entry for `localhost` or other machines you are accessing.
    - Multiple NICs - If your machine running multiple network interfaces, make sure you have the `NIC_ADDR` environment variable set to a valid IP of the machine. This should be done on every machine running GigaSpaces.
    - User permissions - Make sure you run the `gsInstnce.sh` script with a linux user that has permissions to write into the `<Gigaspaces root>/logs` folder.
    - `CLASSPATH` environment variable - Make sure the `CLASSPATH` environment variable is not specified. You might have some libraries specified as part of the `CLASSPATH` that cause GigaSpaces to fail.
    - `JSHOMEDIR` - environment variable - Make sure the `JSHOMEDIR` environment variable is not specified or pointing to a different GigaSpaces release folder. It might be you have some other GigaSpaces release installed on the same machine with `JSHOMEDIR` variable pointing to this release folder.
1. Ping the space by running the `<GigaSpaces Root>\bin\gs.bat/sh` utility:{{<wbr>}}
    `gs space ping -url jini://*/*/mySpace`

{{% tip %}}
The following using multicast lookup discovery protocol. The first `*` of the URL means search for the lookup service across the network using multicast.
{{% /tip %}}

The following result output should appear on the console:


```bash
D:\gigaspaces-xap-premium-8.0.1-ga\bin>gs space ping -url jini://*/*/mySpace
total 1
ping from <mySpace> space
 with:
Finder URL: jini://*/*/mySpace?timeout=5000
Lease Timeout:  10 seconds
LookupFinder timeout: 5000 milliseconds
Buffer Size: 100
Iterations: 5

Average Time = 135 milliseconds
```

{{% tip %}}
Subsequent space ping calls will have much faster response time. The fist space ping call introduce some meta data to the space. This happens only once.
{{% /tip %}}

If the space ping call fails with the following:


```bash
total 0
Service is not found using the URL: jini://*/*/mySpace?timeout=5000
```

You have the following options:
    - Make sure your network and machines running GigaSpaces are configured to have multicast enabled. See the [How to Configure Multicast](./network-multicast.html) section for details on how to enable multicast.
    - Perform a space ping using **unicast** lookup discovery protocol:
    gs space ping -url jini://localhost/*/mySpace
The following result should appear on the console:


```bash
D:\gigaspaces-xap-premium-8.0.1-ga\bin>gs space ping -url jini://localhost/*/mySpace
total 1
ping from <mySpace> space
 with:
Finder URL: jini://localhost/*/mySpace?timeout=5000
Lease Timeout:  10 seconds
LookupFinder timeout: 5000 milliseconds
Buffer Size: 100
Iterations: 5

Average Time = 135 milliseconds
```

{{% tip %}}
When the ping client running on a remote machine (other than the machine running the space), `localhost` should be replaced with the machine **hostname** or **IP** running the space instance.
{{% /tip %}}

# Verifying Remote Installation

Repeat the above steps #3, where the ping command is called from a machine different than the one running the space. You will have to install GigaSpaces both on the client and server machines.

