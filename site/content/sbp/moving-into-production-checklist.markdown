---
type: postsbp
title:  Moving into Production Checklist
categories: SBP
parent: production.html
weight: 800
---


{{% ssummary %}}{{% /ssummary %}}


{{% tip %}}
**Summary:** The following list should provide you with the main activities to be done prior moving your system into production. Reviewing this list and executing the relevant recommendations should result in a stable environment with a low probability of unexpected behavior or failures that are result of a GigaSpaces XAP environment misconfiguration.
<br/>
**Author**: Shay Hassidim, Deputy CTO, GigaSpaces<br/>
**Recently tested with GigaSpaces XAP version**: XAP 10.0<br/>
**Last Update:** April 2014<br/>

{{% /tip %}}

In general, XAP runs on every OS supporting the JVM technology (Windows, Linux, Solaris, AIX, HP, etc). See below tuning and configuration recommendations that most of the applications running on GigaSpaces XAP might need.

# Monitoring

Having reliable monitoring functionality tracking XAP and its surroundings is an important task to be completed **before moving into production**. Correctly monitoring the XAP environment will allow you to proactively take actions (manually/ automatically) before any system failure avoiding bad user experience, data loss or abnormal sudden system shutdown. You may identify an increase with the system usage to allocate additional CPU or memory resources, or identify broken components to be addressed before these actually impact system health or correct system behavior.

The monitoring functionality should track the following:

- Service Grid statistics 
- Data Grid statistics
- Event Containers (Polling / Notify / Archiver) statistics
- Remote Service statistics
- Remote communication statistics
- Client Local cache / view statistics
- Web application statistics
- Mirror Service statistics
- Replication statistics
- Admin Alerts (CPU Utilization, Garbage Collection Alert, Replication Channel Disconnection Alert , etc)
- Log files

For all service grid components (`GSA` , `LUS` , `GSM` , `GSC`) you should monitor Thread count , CPU utilization, file descriptors count , memory utilization and network utilization.

You may publish this information in real-time to any enterprise monitoring system you may have (CA Wily introscope, HP Operations Manager , IBM Tivoli , etc) to be correlated with your existing application monitoring. You may also generate daily / hourly reports of this information to be shared with relevant entities within your organization to be processed offline to estimate required system capacity and size upon system growth.

{{% note %}}
Please approach [GigaSpaces support](mailto:support@gigaspaces.com?subject=XAP Monitoring tools) team for Monitoring tools provided as part of GigaSpaces professional services. These can be adapted to fit your exact requirements. 
{{%/note%}}

# File Descriptors

The XAP LRMI communication layer opens network connections dynamically. With large scale applications or with clients that are running a large number of threads accessing the Data-Grid, you might end up having a large number of file descriptors used both on the client and server side. You might have multiple JVMs running on the machine. This might need to increase the default max user processes value.

On Linux OS - without the right file descriptors settings you will have **Too many open files** error messages at the XAP processes log files displayed or on the client side. Low file descriptors values will impact application stability and ability for clients to connect to the XAP grid.  You will have to set both **System** level File Descriptor Limit and also **Process** level File Descriptor Limit to high values to avoid these problems. See below how.

## System File Descriptor Limit

To change the number of file descriptors in Linux, do the following as the `root` user:

Edit the following line in the `/etc/sysctl.conf` file:


```bash
fs.file-max = 300000
```
300000 will be the new file descriptor limit that you want to set.

Apply the change by running the following command:


```bash
/sbin/sysctl -p
```

Verify your settings using:

```bash
cat /proc/sys/fs/file-max 
```

OR 


```bash
sysctl fs.file-max
```


## Process File Descriptor Limit
The Linux OS by default has a relatively small number of file descriptors available and max user processes (1024). You should make sure that your standalone clients, or GSA/GSM/GSC/LUS using a user account which have its **maximum open file descriptors (open files) and max user processes** configured to a high value. A good value is 32768.

Setting the **max open file descriptors** and **max user processes** is done via the following call:


```bash
ulimit -n 32768 -u 32768
```

Alternatively, you should have the following files updated:


```bash
/etc/security/limits.conf

- soft    nofile          32768
- hard    nofile          32768

/etc/security/limits.d/90-nproc.conf

- soft nproc 32768
```

See [increase-open-files-limit](https://rtcamp.com/tutorials/linux/increase-open-files-limit/) for more details.

## Monitoring Utilized File Descriptors
You can monitor the MaxFileDescriptorCount and OpenFileDescriptorCount with the JConsole:

![jmx-file-descriptors.png](/sbp/attachment_files/jmx-file-descriptors.png)


# Sharing Grid Management Services Infrastructure
There are numerous ways allowing different systems/applications/groups to share the same pool of servers (in development or production environment) on the network.  A non-exhaustive list of some of the options is delineated below:


1. Outside of XAP:  Dedicated hardware for each group, each set of servers runs an independent XAP runtime environment (aka Service Grid) without sharing the same server between different groups.  This naive approach is good for simple or temporary scenarios. In this case each XAP runtime environment is isolated from each other using [different LOOKUPLOCATORS](#RunningMultipleLocators) or [different LOOKUPGROUPS](#RunningMultipleGroups) value.

2. [Using Multiple Zones](#RunningMultipleZones):  A single XAP runtime environment spans all servers, where each group of XAP containers (across several machines) are labeled with a specific Zone.  You may have multiple Zones used by different containers on the same server. For example, have on server A two containers labeled with zoneX and four containers labeled with zoneY and on server B two containers labeled with zoneX and four containers labeled with zoneY.
At deployment time, application services (aka processing Unit) are deployed using a specific Zone. This instructs the system to provision the services into the corresponding containers.  Use of multiple Zones breaks logically the runtime environment into different segments.

3. [Using Multiple Lookup Groups (multicast lookup discovery)](#RunningMultipleGroups):  All servers running multiple XAP runtime environments, where each XAP container using a specific Lookup Group when registering with the Lookup Service.  At deployment time, application services (aka processing Unit) are deployed using a specific lookup group. Use of multiple lookup group breaks logically the Infrastructure into different segments. The Lookup Group value controlled via the `LOOKUPGROUPS` environment variable. When using this option you should make sure multicast is enabled on all machines.

4. [Using Multiple Lookup Locators (unicast lookup discovery)](#RunningMultipleLocators): All servers running multiple XAP runtime environments, where each XAP container using a specific Lookup locator when registering with the Lookup Service.  At deployment time, application services (aka processing Unit) are deployed using a specific lookup locator. Use of multiple lookup locators breaks logically the Infrastructure into different segments. If you have multiple lookup services running on the same server, each will use a different listening port. You may control this port using the `com.sun.jini.reggie.initialUnicastDiscoveryPort` system property. The Lookup Locators value controlled via the `LOOKUPLOCATORS` environment variable.

5. Using a shared XAP runtime environment: A single XAP runtime environment spans all servers, with no use of Zones or Lookup Groups/Locators.   Application services share the servers and allocation done in a random manner without using any pre-defined segmentation.

For any of the above options, XAP exposes the ability to control a deployed application service in run-time, such that new application service instances can be created or existing instances can be relocated.   This tight operational control enables even more creative resource sharing possibilities.

Devising the appropriate resource sharing strategy for your system should consider the breadth of operational requirements and application services' characteristics. For example, two applications with variable load may run into trouble running on a fixed-size shared environment if peak loads coincide.
XAP provides consultancy services for the environment planning stage that addresses the above as well as other considerations impacting your environment. For more information see [GigaPro Services](http://www.gigaspaces.com/content/gigapro-full-services-offering-xap-customers)

# Binding the Process into a Machine IP Address
In many cases, the machines that are running GigaSpaces XAP (i.e., a GSA, GSM, or GSC), or running GigaSpaces XAP client applications (e.g., web servers or standalone JVM/.Net/CPP processes) have multiple network cards with multiple IP addresses. To make sure that the XAP processes or the XAP client application processes bind themselves to the correct IP addresses - accessible from another machines - you should use the `NIC_ADDR` environment variable, or the [java.rmi.server.hostname](http://docs.oracle.com/javase/7/docs/api/java/rmi/server/package-summary.html) system property. Both should be set to the IP of the machine (one of them in case of a machine with multiple IP addresses). Without having this environment/property specified, in some cases, a client process is not able to be notified of events generated by the XAP runtime environment or the space.

Examples:


```bash
export NIC_ADDR=10.10.10.100
./gs-agent.sh &
```


```bash
 java -Djava.rmi.server.hostname=10.10.10.100 MyApplication
```

{{% tip %}}
With the above approach, you can leverage multiple network cards within the same machine to provide a higher level of hardware resiliency, and utilize the network bandwidth in an optimal manner, by binding different JVM processes running on the same physical machine to different IP addresses. One example of this would be four GSCs running on the same machine, where two of the them are using IP_1 and the other two are using IP_2.
{{% /tip %}}

{{% exclamation %}} For more information, see [How to Configure an Environment With Multiple Network-Cards (Multi-NIC)]({{%latestadmurl%}}/network-multi-nic.html)

# Ports
GigaSpaces XAP uses TCP/IP for most of its remote operations. The following components within XAP require open ports:


| Service | Description | Configuration Property | Default value |
|:--------|:------------|:-----------------------|:--------------|
|RMI registry listening port |Used as an alternative directory service.| `com.gigaspaces.system.registryPort` System property|10098 and above.|
|Webster listening port|Internal web service used as part of the application deployment process. |`com.gigaspaces.start.httpPort` System property|9813|
|[Web UI Agent]({{%latestadmurl%}}/web-management-console.html)|GigaSpaces XAP Dashboard Web Application. | `com.gs.webui.port` System property|8099|

Here are examples of how to set different LRMI listening ports for the GS-UI, and another set of ports for the GSA/GSC/GSM/Lookup Service:


```bash
 export EXT_JAVA_OPTIONS=-Dcom.gs.transport_protocol.lrmi.bind-port=7000-7500
```


```bash
 export EXT_JAVA_OPTIONS=-Dcom.gs.transport_protocol.lrmi.bind-port=8000-8100
```

A running GSC tries to use the first free port that is not used out of the port range specified. The same port might be used for several connections (via a multiplexed protocol). If all of the port range is exhausted, an error is displayed.

{{% tip %}}
When there are several GSCs running on the same machine, or several servers running on the same machine, it is recommended that you set a different LRMI port range for each JVM.  Having 100 as a port range for the GSCs supports a large number of clients (a few thousand).
{{% /tip %}}

# Client LRMI Connection Pool and Server LRMI Connection Thread Pool

{{%section%}}
{{%column width="70%" %}}
{{%/column%}}
{{%column width="30%" %}}
{{%popup   "/attachment_files/sbp/lrmi_archi2.jpg"%}}
{{% /column%}}
{{%/section%}}

The default LRMI behavior will open a different connection at the client side and start a connection thread at the server side, once a multithreaded client accesses a server component. All client connections may be shared between all the client threads when communicating with the server. All server side connection threads may be shared between all client connections.


## Client LRMI Connection Pool
The client LRMI connection pool is maintained per server component - i.e. by each space partition. For each space partition a client maintains a dedicated connection pool shared between all client threads accessing a specific partition. When having multiple partitions (N) hosted within the same GSC, a client may open maximum of `N * com.gs.transport_protocol.lrmi.max-conn-pool` connections against the GSC JVM process.

{{% tip %}}
You may need to change the `com.gs.transport_protocol.lrmi.max-conn-pool` value (1024) to have a smaller number. The default value might be high for application with multiple partitions.


```bash
Client total # of open connections = com.gs.transport_protocol.lrmi.max-conn-pool * # of partitions
```

This may result very large amount of connections started at the client side resulting **Too many open files** error. You should increase the OS' max file descriptors amount by calling the following before running the client application (on UNIX):


```bash
ulimit -n 65536
```

or by lowering the `com.gs.transport_protocol.lrmi.max-conn-pool` value.
{{% /tip %}}

## Server LRMI Connection Thread Pool
The LRMI connection thread pool is a server side component. It is in charge of executing the incoming LRMI invocations. It is a single thread pool within the JVM that executes all the invocations, from all the clients and all the replication targets.

{{% tip %}}
{{% /tip %}}

# Lookup Locators and Groups
A space (or any other service, such as a GSC or GSM) publishes (or registers/exports) itself within the [Lookup Service](/product_overview/the-lookup-service.html). The lookup service acts as the system directory service. The lookup service (aka service proxy) keeps information about each service, such as its location and its exposed remote methods. Every client or service needs to discover a lookup service as part of its bootstrap process.

There are 2 main options for how to discover a lookup service:

- **Via locator(s)** - Unicast Discovery mode. With this option a specific IP (or hostname) used indicating the machine running the lookup service. This option can be used when multicast communication is disabled on the network, or when you want to avoid the overhead involved with the multicast discovery.
- **Via group(s)** - Multicast Discovery mode. relevant **only when the network supports multicast**. This is a "tag" you assign to the lookup.  Clients that want to register with this lookup service, or search for a service proxy, need to use this specific group when discovering the lookup service.

To configure the GigaSpaces XAP runtime components (GSA,GSC,GSM,LUS) to use unicast discovery you should set the `LOOKUPLOCATORS` variable:


```bash
export LOOKUPLOCATORS=MachineA,MachineB
./gs-agent.sh &
```

To configure the GigaSpaces XP runtime components (GSA,GSC,GSM,LUS) to use multicast discovery you should set the `LOOKUPGROUPS` variable:


```bash
export LOOKUPGROUPS=Group1,Group2
./gs-agent.sh &
```

When running multiple systems on the same network infrastructure, you should isolate these by having a dedicated set of lookup services (and  GSC/GSM) for each system. Each should have different locators/groups settings.


## Space URL Examples
See below for examples of [Space URL]({{%latestjavaurl%}}/the-space-configuration.html) you should be familiar with:

- `jini://localhost/*/space` - this space URL means that the client is trying to discover the lookup service on the localhost, together with discovering it on the network via multicast (enabled by default).

- `jini://localhost/*/space?locators=host,host2` - this space URL means that together with searching for the lookup service on the localhost or the network, we are looking for it on host1 and host2. We call this unicast lookup discovery.

- `jini://localhost/*/space?groups=A,B` - this space URL means that together with searching for the lookup service on the localhost, we are using multicast discovery to search for all the lookup services associated with group A or B.

- `jini://*/*/space` - this space URL means that searching for the lookup service is done only via multicast discovery.

- `/./space?groups=A,B` - this space URL means that the started space registers itself with group A and B. To access such a space via a remote client, it needs to use the following space URL: `jini://*/*/space?groups=A` or `jini://*/*/space?groups=B`.


## Space Configuration with Unit Tests

When running unit tests, you might want these set up so that no remote client can access the space they are running. This includes regular clients or the GS-UI.

{{% tip %}}
When running a space running in embedded mode and not deployed into a GSC (standalone space), it starts a lookup service automatically. This allows you to access it from the GS-UI.
If it is running within the GSC, it finds the lookup via the `LOOKUPLOCATORS` or `LOOKUPGROUPS` settings.
{{% /tip %}}
.
Here is a simple configuration you should place within your pu.xml to disable the lookup service startup, disable the space registration with the lookup service, and disable the space registration with the Rmi registry, when the space starts as a PU or running as a standalone:


```xml
<os-core:embedded-space id="space" name="myspace">
    <os-core:properties>
        <props>
            <prop key="com.j_spaces.core.container.directory_services.jini_lus.start-embedded-lus">false</prop>
            <prop key="com.j_spaces.core.container.directory_services.jini_lus.enabled">false</prop>
            <prop key="com.j_spaces.core.container.directory_services.jndi.enabled">false</prop>
            <prop key="com.j_spaces.core.container.embedded-services.httpd.enabled">false</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

# The Runtime Environment - GSA, LUS, GSM and GSCs
In a dynamic environment where you want to start [GSCs](/product_overview/service-grid.html#gsc) and [GSM](/product_overview/service-grid.html#gsm) remotely, manually or dynamically, the [GSA](/product_overview/service-grid.html#gsa) is the only component you should have running on the machine that is hosting the [XAP runtime environment]({{%latestadmurl%}}/the-runtime-environment.html). This lightweight service acts as an agent and starts a GSC/GSM/LUS when needed.

You should plan the initial number of GSCs and GSMs based on the application memory footprint, and the amount of processing you might need. The most basic deployment should include 2 GSMs (running on different machines), 2 Lookup services (running on different machines), and 2 GSCs (running on each machine). These host your Data-Grid or any other application components (services, web servers, Mirror) that you deploy.

In general, the total amount of GSCs you are running across the machines that host the system depends on:

- The amount of data you want to store in memory.
- The JVM maximum heap size.
- The processing requirements.
- The number of users the system needs to serve.
- The total number of CPU cores the machine is running.

{{% tip %}}
A good number for the amount of GSCs a machine should host would be **half of the amount** of total CPU cores, each having no more than a 10G maximum heap size.
{{% /tip %}}

## Configuring the Runtime Environment

JVM parameters (system properties, heap settings etc.) that are shared between all components are best set using the `EXT_JAVA_OPTIONS` environment variable. However, starting from 7.1.1, specific GSA JVM parameters can be easily passed using `GSA_JAVA_OPTIONS` that will be appended to `EXT_JAVA_OPTIONS`. If `GSA_JAVA_OPTIONS` is not defined, the system will behave as in 7.1.0. As a good practice, one can add all components' environment variables ( `GSA_JAVA_OPTIONS`, `GSM_JAVA_OPTIONS`, `GSC_JAVA_OPTIONS`, `LUS_JAVA_OPTIONS`) within the GSA script, or in a wrapper script and the values will be passed to corresponding components.

{{%tabs%}}

{{%tab "  Linux "%}}


```bash
#Wrapper Script
export GSA_JAVA_OPTIONS='-Xmx256m'
export GSC_JAVA_OPTIONS='-Xmx2048m'
export GSM_JAVA_OPTIONS='-Xmx1024m'
export LUS_JAVA_OPTIONS='-Xmx1024m'

#call gs-agent.sh
. ./gs-agent.sh
```

{{% /tab %}}

{{%tab "  Windows "%}}


```bash
@rem Wrapper Script
@set GSA_JAVA_OPTIONS=-Xmx256m
@set GSC_JAVA_OPTIONS=-Xmx2048m
@set GSM_JAVA_OPTIONS=-Xmx1024m
@set LUS_JAVA_OPTIONS=-Xmx1024m

@rem call gs-agent.bat
call gs-agent.bat
```

{{% /tab %}}

{{% /tabs %}}

{{%anchor RunningMultipleGroups %}}

## Running Multiple Groups
You may have a set of LUS/GSM managing GSCs associated to a specific group. Let's assume you would like to "break" your network into 2 groups. Here is how you should start the GigaSpaces XAP runtime environment:

{{%accordion%}}
{{%accord parent=acc3 | title="Step 1. Run gs-agent starting LUS/GSM with GroupX: "%}}

```bash
export LOOKUPGROUPS=GroupX
gs-agent.sh gsa.global.lus 0 gsa.lus 1 gsa.global.gsm 0 gsa.gsm 1 gsa.gsc 0
```
{{%/accord%}}
{{%accord parent=acc3 | title="Step 2. Run gs-agent that will start GSCs with GroupX (4 GGCs with this example): "%}}

```bash
export LOOKUPGROUPS=GroupX
gs-agent.sh gsa.global.lus 0 gsa.lus 0 gsa.global.gsm 0 gsa.gsm 0 gsa.gsc 4
```
{{%/accord%}}
{{%accord parent=acc3 | title="Step 3. Run gs-agent starting LUS/GSM with GroupY: "%}}

```bash
export LOOKUPGROUPS=GroupX
gs-agent.sh gsa.global.lus 0 gsa.lus 1 gsa.global.gsm 0 gsa.gsm 1 gsa.gsc 0
```
{{%/accord%}}
{{%accord parent=acc3 | title="Step 4. Run gs-agent that will start GSCs with GroupY (2 GGCs with this example): "%}}

```bash
export LOOKUPGROUPS=GroupY
gs-agent.sh gsa.global.lus 0 gsa.lus 0 gsa.global.gsm 0 gsa.gsm 0 gsa.gsc 2
```
{{%/accord%}}
{{%accord parent=acc3 | title="Step 5. Deploy a space into GroupX GSCs "%}}

```bash
export LOOKUPGROUPS=GroupX
gs deploy-space -cluster schema=partitioned total_members=4 spaceX
```
{{%/accord%}}
{{%accord parent=acc3 | title="Step 6. Deploy a space into GroupY GSCs"%}}

```bash
export LOOKUPGROUPS=GroupY
gs deploy-space -cluster schema=partitioned total_members=2 spaceY
```
{{%/accord%}}
{{%/accordion%}}

<br>

{{% anchor RunningMultipleLocators%}}

## Running Multiple Locators
You may have a set of LUS/GSM managing GSCs associated to a specific locator. Let's assume you would like to "break" your network into 2 groups using different lookup locators. Here is how you should start the GigaSpaces XAP runtime environment:

{{%accordion%}}
{{%accord parent=acc1 | title="Step 1. Run gs-agent starting LUS/GSM with a lookup service listening on port 8888:"%}}

```bash
export LUS_JAVA_OPTIONS=-Dcom.sun.jini.reggie.initialUnicastDiscoveryPort=8888
export LOOKUPLOCATORS=127.0.0.1:8888
export EXT_JAVA_OPTIONS=-Dcom.gs.multicast.enabled=false
gs-agent.sh gsa.global.lus 0 gsa.lus 1 gsa.global.gsm 0 gsa.gsm 1 gsa.gsc 0
```
{{%/accord%}}
{{%accord parent=acc1 | title="Step 2. Run gs-agent that will start GSCs using the lookup listening on port 8888 (4 GGCs with this example):"%}}

```bash
export LOOKUPLOCATORS=127.0.0.1:8888
export EXT_JAVA_OPTIONS=-Dcom.gs.multicast.enabled=false
gs-agent.sh gsa.global.lus 0 gsa.lus 0 gsa.global.gsm 0 gsa.gsm 0 gsa.gsc 4
```
{{%/accord%}}
{{%accord parent=acc1 | title="Step 3. Run gs-agent starting LUS/GSM with a lookup service listening on port 9999:"%}}

```bash
export LUS_JAVA_OPTIONS=-Dcom.sun.jini.reggie.initialUnicastDiscoveryPort=9999
export LOOKUPLOCATORS=127.0.0.1:8888
export EXT_JAVA_OPTIONS=-Dcom.gs.multicast.enabled=false
gs-agent.sh gsa.global.lus 0 gsa.lus 1 gsa.global.gsm 0 gsa.gsm 1 gsa.gsc 0
```
{{%/accord%}}
{{%accord parent=acc1 | title="Step 4. Run gs-agent that will start GSCs using the lookup listening on port 9999 (2 GGCs with this example):"%}}


```bash
export LOOKUPLOCATORS=127.0.0.1:9999
export EXT_JAVA_OPTIONS=-Dcom.gs.multicast.enabled=false
gs-agent.sh gsa.global.lus 0 gsa.lus 0 gsa.global.gsm 0 gsa.gsm 0 gsa.gsc 2
```
{{%/accord%}}
{{%accord parent=acc1 | title="Step 5. Deploy a space using lookup listening on port 8888:"%}}

```bash
export LOOKUPLOCATORS=127.0.0.1:8888
gs deploy-space -cluster schema=partitioned total_members=4 spaceX
```
{{%/accord%}}
{{%accord parent=acc1 | title="Step 6. Deploy a space using lookup listening on port 9999"%}}

```bash
export LOOKUPLOCATORS=127.0.0.1:9999
gs deploy-space -cluster schema=partitioned total_members=2 spaceY
```
{{%/accord%}}
{{%/accordion%}}

{{% tip %}}
On top of the Lookup service, there is also an alternative way to export the space proxy - it is done via the RMI registry (JNDI). It is started by default within any JVM running a GSC/GSM. By default, the port used is 10098 and above. This option should be used only in special cases where somehow there is no way to use the default lookup service. Since this is the usual RMI registry, it suffers from known problems, such as being non-distributed, non-highly-available, etc.
{{% /tip %}}

The lookup service runs by default as a standalone JVM process started by the GSA. You can also embed it to run together with the GSM. In general, you should run 2 lookup services per system. Running more than 2 lookup services may cause an overhead, due to the chatting and heartbeat mechanism performed between the services and the lookup service, to signal the existence of the service.

# Zones

{{%section%}}
{{%column width="70%" %}}
The [XAP Zone]({{%latestadmurl%}}/the-sla-overview.html) allows you to "label" a running GSC(s) before starting it. The XAP **Zone** should be used to isolate applications and a Data-Grid running on the same network. It has been designed to allow users to deploy a processing unit into specific set of GSCs where all these **sharing the same set of LUSs and GSMs**.
{{%/column%}}
{{%column width="30%" %}}
{{%popup   "/sbp/attachment_files/zones.jpg"%}}
{{%/column%}}
{{%/section%}}

The **Zone** property can be used for example to deploy your Data-Grid into a specific GSC(s) labeled with specific zone(s). The zone is specified prior to the GSC startup, and cannot be changed once the GSC has been started.


{{% tip %}}
You should make sure you have an adequate number of GSCs running, prior to deploying an application whose SLA specified a specific zone.
{{% /tip %}}

To use Zones when deploying your PU you should:

{{%accordion%}}
{{%accord parent=acc4 | title="Step 1. Start the GSC using the `com.gs.zones` system property. Example: "%}}


```bash
export EXT_JAVA_OPTIONS=-Dcom.gs.zones=webZone ${EXT_JAVA_OPTIONS}
gs-agent gsa.gsc 2
```
{{%/accord%}}
{{%accord parent=acc4 | title="Step 2. Deploy the PU using the `-zones` option. Example: "%}}

```bash
gs deploy -zones webZone myWar.war
```
{{%/accord%}}
{{%/accordion%}}
<br>

{{%anchor RunningMultipleZones%}}

## Running Multiple Zones
You may have a set of LUS/GSM managing multiple zones (recommended) or have a separate LUS/GSM set per zone. In such a case (set of LUS/GSM managing multiple zones) you should run these in the following manner:

{{%accordion%}}
{{%accord parent=acc0| title="Step 1. Run gs-agent on the machines you want to have the LUS/GSM:"%}}

```bash
gs-agent.sh gsa.global.lus 0 gsa.lus 1 gsa.global.gsm 0 gsa.gsm 1 gsa.gsc 0
```
{{%/accord%}}
{{%accord parent=acc0| title="Step 2. Run gs-agent that will start GSCs with zoneX (4 GGCs with this example):"%}}

```bash
export EXT_JAVA_OPTIONS=-Dcom.gs.zones=zoneX ${EXT_JAVA_OPTIONS}
gs-agent.sh gsa.global.lus 0 gsa.lus 0 gsa.global.gsm 0 gsa.gsm 0 gsa.gsc 4
```
{{%/accord%}}
{{%accord parent=acc0| title="Step 3. Run gs-agent that will start GSCs with zoneY (2 GGCs with this example):"%}}

```bash
export EXT_JAVA_OPTIONS=-Dcom.gs.zones=zoneY ${EXT_JAVA_OPTIONS}
gs-agent.sh gsa.global.lus 0 gsa.lus 0 gsa.global.gsm 0 gsa.gsm 0 gsa.gsc 2
```
{{%/accord%}}
{{%/accordion%}}

Note that with XAP 7.1.1 new variables provided that allows you to set different JVM arguments for GSC,GSM,LUS,GSA separately (GSA_JAVA_OPTIONS , GSC_JAVA_OPTIONS , GSM_JAVA_OPTIONS , LUS_JAVA_OPTIONS).


# Failover Considerations

GigaSpaces XAP provides continuous high-availability even when the infrastructure processes or entire (physical/virtual) machines fail.  This capability is provided **out of the box** but does require some attention to configuration to meet the needs of your specific application.

## N + 1 and N + 2 Configurations

Determining the optimal high availability configuration for your particular application requires balancing the cost of additional hardware (or virtual machines) against the risk of downtime.  In most cases it pays off having additional resources available to avoid downtime, compromising system health, instability, poor reliability , no durability and incompleteness.

The two most common GigaSpaces XAP deployment configurations are referred to as **N + 1** and **N + 2**.  This refers to the number of machines running XAP that can fail without compromising the data grid and its applications to deliver reasonable performance and to stay in good health.  In an **N + 1** configuration, the core N machines have sufficient RAM and CPU power to run the application if one of the **N + 1** machines fail.  In an **N + 2** configuration, the same is true if two of the machines fail or become unavailable.

In either configuration, the data grid (or any deployed business logic) is distributed across all available machines.  Each machine hosts a set of GSCs and there are at least two GSMs and two LUSs running.  When deploying regular (static) PU, you may have spare GSCs available on each machine to accommodate a failure.  If a machine becomes unavailable, the backup PU instance corresponding to the primary nodes on that machine become primaries and the GSM provision a new backup in one of the spare GSCs.  In this case you may need to call the rebalance utility to distribute primary and backups evenly across all GSCs. This failover is transparent to clients of the application and business logic running within it.
When deploying elastic PU, GSCs will be created on the fly , where missing PU instances will be provisioned into these newly started GSCs. In this case primary and backup instances will be automatically distributed evenly across all machines. 

## Grid Failure Handling Strategy

When deploying your XAP based system in production you should consider the following failure scenarios. These should address, GSC , Machine/VM and a complete Data-Center failure:

1. **Single GSC Failure** - This is the simplest case - good for small deployment that are not mission critical and does not require continuous high-availability to survive multiple failures. The GSA will manage the GSC life-cycle locally or globally and accomadate GSC failure. This assumes you are using static PU deployment.
2. **Multiple GSCs Failures** - In this case you should deploy using the Elastic PU. This will start GSCs as needed on available VM/Machines to survive multiple failures and support dynamic scaling.
3. **Complete VM/ Machine Failure** - Elastic PU deployment should be used together with XAP init.d setup. This will start the GigaSpaces XAP agent once the VM/Machine is restarted. Cloudify can be used to complete orchestration of the XAP installation and configuration.
5. **Complete Data-Center Failure** - Elastic PU together with XAP init.d setup should be used; Data replication over the WAN using the WAN Gateway should be used. Cloudify can be used to complete orchestration of the XAP installation and configuration.

## Guaranteed Notifications

When using notifications (notify container, session messaging API) you should enable Guaranteed Notifications to address a primary space failure while sending notifications. This allows the backup once promoted into a primary, to continue the notification delivery. The Guaranteed Notifications managed on the client side. 

When considering a notify container that is embedded with the space, please note the guaranteed notifications are not supported in this scenario - This means that upon failure notifications that has been send might not be fully processed. In this case blocking read/asyc read should be considered as an alternative to notifications.

## Balanced Primary-Backups Provisioning

When having a failure of the data grid nodes or a machine running XAP you should consider having even distribution of the primary and backups instances across all existing machines running XAP. This ensure balanced CPU utilization across all XAP machines. The Elastic processing unit should be used to deploy the data grid - it support even primary/backup distribution automatically when XAP machine fails and when a new one added to the grid. In this case spare GSCs on each XAP machine are not required.

## CPU Utilization

CPU utilization should not exceed the 40 % in average to support complete machine/VM failure. This headroom will enable the machines running XAP to cope with the additional capacity required when one or more machines running the grid will fail or go through maintenance.

## LUS Failure

To support LUS (lookup service) failure at least two LUS should be started. You may run in Global or local [LUS configuration]({{%latestadmurl%}}/lus-configuration.html) that ensures two LUS will be running. Running Global LUS configuration (recommended when using multicast lookup discovery configuration) ensure you will have two LUS running on two different machines even if a machine running a LUS fails. When using unicast lookup discovery configuration and a LUS failed the clients and service grid components may throw exceptions as internally they will be trying to perform lookup discovery frequently for the missing lookup. You may configure the lookup discovery intervals using the `com.gigaspaces.unicast.interval` property.


## GSA Failure

The GSA acting as a process manager, watching the GSC , LUS , ESM and GSM processes.  Having the GSA failed is a very rare scenario. If it happens you should look for unusual HW , OS or JDK failure that may cause it. To address GSA failure you should run it as a service. This how the OS will restart it automatically once failed. See how you can run it as a Windows Service or a Linux Service.

## GSM Failure

The GSM responsible for deployment and provisioning of deployed PUs(stateless , statefull). It is utilized at the deploy time, PU failure and PU migration from one GSC to another GSC. To support HA you should have two GSMs started per grid. You may Global or local [GSM configuration]({{%latestadmurl%}}/gsm-configuration.html) that ensures two GSM will be running. In most cases Global GSM configuration is recommended unless you require hot deploy functionality.


## ESM Failure

The ESM responsible for elastic PU provisioning when deployed and addressing space instances rebalance once PU should scale up/down/in/out or when GSA failed or started. When ESM fails it will be restarted automatically using one of the existing agents. 



## XAP Distributed Transactions

XAP Distributed Transactions involves XAP remote or local Distributed Transaction Manager and one or more data grid. With a remote Distributed Transaction Manager you should consider running at least two remote transaction managers. It is recommended to use a local Distributed Transaction Manager as it makes the overall architecture simpler.

## XA Transactions

XA Transactions involves the XA transaction manager, XAP data grid node(s) and some additional participant(s). The transaction manager usually deployed independently. The transaction manager might fail, so it should be deployed in some HA configuration. Client code should support transaction manager failure by caching relevant transaction exception, and retry the activity by aborting the old transaction, starting a new transaction , executing relevant operations and committing. The recommended transaction manager XAP certified with is Atomikos and JBoss.

## Mirror Service Failure

The Mirror Service like the WAN Gateway acting as a broker, responsible to persist activities conducted at the data grid into external data source such as a database.

The Mirror Service does not hold state so its failure would not result any data lose, but its failure means data will not be stored into the external data source. You do not need to deploy the mirror in a clustered configuration (aka primary-backup). By default XAP will try to start the mirror service in case it failed. Since in many cases the Mirror service accessing a database, you might have the database accepting connection only from specific machine with specific ports. To address this, you should configure the database to allow connections from all machines that may run the mirror service - by default all machines running XAP.

## WAN Gateway Failure

The WAN gateway like the mirror service acting as a broker, responsible to replicate activities conducted at the local data grid into another (remote) data grid. The WAN Gateway does not hold state so its failure would not result any data lose, but its failure means data will not be replicated between source and destination data grid. You do not need to deploy the WAN Gateway in a clustered configuration (aka primary-backup). By default XAP will try to start a WAN Gateway in case it failed. Since WAN Gateway usually configured to use specific port on specific machine(s), you should configure the WAN Gateway PU to be provisioned into specific machine(s).

# Capacity Planning

In order to estimate the amount of total RAM and CPU required for your application, you should take the following into consideration:

- The Object Footprint within the space.
- Active Clients vs. Cores vs. Heap Size.
- The number of space partitions and backups.

The [Capacity Planning](./capacity-planning.html) section provides a detailed explanation how to estimate the resources required.

# PU Packaging and CLASSPATH

## User PU Application Libraries
A [Processing Unit]({{%latestjavaurl%}}/the-processing-unit-structure-and-configuration.html) JAR file, or a [Web Application]({{%latestjavaurl%}}/web-jetty-processing-unit-container.html) WAR file should include within its lib folder, all the necessary JARs required for the application. Resource files should be placed within one of the JAR files within the PU JAR, located under the lib folder. In addition, the PU JAR should include the pu.xml within the `META-INF\spring` folder.
In order to close LRMI threads when closing application,please use:LRMIManager.shutdown().

## Data-Grid PU Libraries
When deploying a Data-Grid PU, it is recommended that you include all space classes and their dependency classes as part a PU JAR file. This PU JAR file should include a pu.xml within the META-INF\spring, to include the space declarations and relevant tuning parameters.

## GS-UI Libraries
It is recommended that you include all space classes and their dependency classes as part of the GS-UI CLASSAPTH . This makes sure that you can query the data via the GS-UI. To set the GS-UI classpath, set the `POST_CLASSPATH` variable prior to calling the GS-UI script to have the application JARs locations.

{{% tip %}}
To avoid the need to load the same library into each PU instance classloader running within the GSC, you should place common libraries (such as JDBC driver, logging libraries, Hibernate libraries and their dependencies) at the `<gigaspaces-xap root>\lib\optional\pu-common` folder. You may specify the location of this folder using the `com.gs.pu-common` system property.
{{% /tip %}}

# JVM Tuning

{{%section%}}
{{%column width="70%" %}}
In most cases, the applications using XAP are leveraging machines with very fast CPUs, where the amount of temporary objects created is relatively large for the JVM garbage collector to handle with its default settings. This means careful tuning of the JVM is very important to ensure stable and flawless behavior of the application.

Below represents the different XAP processes a virtual or a physical machine may run:
{{%/column%}}
{{%column width="30%" %}}
{{%popup   "/sbp/attachment_files/jvm-vm-memory.jpg"%}}
{{%/column%}}
{{%/section%}}

- GSA - Very lightweight process in terms of its memory and CPU usage. This process does not require any tuning. You should have one per machine or in some cases one per Zone.
- GSC - The runtime environment. This is where the data grid and the deployed processing units are running. This process requires the relevant tuning to address the memory capacity required. Number of GSCs should not exceed: `Total # of cores / 4`. With virtual machine setup you should have one GSC per VM. 
- GSM - Lightweight process. Does not require any tuning unless you have very large cluster (over 100 nodes). You should have two of these per XAP grid.
- LUS - Lightweight process. Does not require any tuning unless you have very large cluster (over 100 nodes). You should have two of these per XAP grid.
- ESM - Lightweight process. Does not require any tuning unless you have very large cluster (over 100 nodes). You should have one of this per XAP grid.



```bash
XAP VM Memory size = 
Guest OS Memory + JVM Memory for all GSCs + JVM Memory for GSM + JVM Memory for LUS + JVM Memory for ESM
```


```bash
JVM Memory for a GSC = 
JVM Max Heap (-Xmx value) + JVM Perm Size 
