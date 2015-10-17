---
type: post100
title:  Command Line Interface
categories: XAP100ADM
parent: administration-tools.html
weight: 100
---

{{%ssummary%}} {{%/ssummary%}}



GigaSpaces provides an interactive command line tool as part of the product. This can be started using gs.sh/bat command (referred to as **GigaSpaces CLI**).

This tool provides many commands that can be used to manage and gather information about the various GigaSpaces runtime components. This section describes the commands supported by GigaSpaces CLI.


# help

#### Syntax

    gs> help [command-name]

#### Description

The `help` command displays the syntax of a specified CLI command, or a list of all available commands.

#### Options


| Option | Description |
|:-------|:------------|
| `command-name` | The name of any one of the GigaSpaces CLI commands. Default: List all commands, without their arguments. |


# cd

#### Syntax

    gs> cd directory

#### Description

The `cd` command moves to a different working directory, like the Unix `cd` command.

`directory` accepts an absolute or relative file-system path.

#### Options


| Option | Description |
|:-------|:------------|
|None||


# gsa

#### Syntax

    gs> gsa <command> [-host<host name/ip address>]

#### Description

The following gsa CLI commands are available:


| Command | Description |
|:--------|:------------|
| shutdown | shutdown gsa |
| start-gsc | starts gsc agent within specific gsa service |
| start-gsm | starts gsm agent within specific gsa service |
| start-lookup | starts lus agent within specific gsa service |

#### Options


| Option | Description | Value Format |
|:-------|:------------|:-------------|
| `help` / `h` | Prints help -- the command's usage and options. | |
| `host` / `h` | Host name, optional parameter, allows to locate gsa that is running on specific machine. | |


{{%accordion%}}
{{%accord title="Example"%}}
The following prints a numbered list of GSA services, and you can choose a GSA service to create gsc within it:

    gs> gsa start-gsc
    total 5
    [1]  10278  Grid Service Agent  gigaspaces-8.0.0-XAPPremium...  pc-lab43@192.xxx.x.xx
    [2]  20157  Grid Service Agent  gigaspaces-8.0.0-XAPPremium...  pc-lab37@192.xxx.x.xx
    [3]  26850  Grid Service Agent  gigaspaces-8.0.0-XAPPremium...  pc-lab38@192.xxx.x.xx
    [4]  11752  Grid Service Agent  gigaspaces-8.0.0-XAPPremium...  Evgeny@192.xxx.xx.xxx
    [5]  1608   Grid Service Agent  gigaspaces-8.0.0-XAPPremium...  pc-lab42@192.xxx.x.xx

    Enter a number of service to gsa start-gsc or "c" to cancel :

Example for gsa shutdown when specific host name provided:

    gs> gsa shutdown -host pc-lab43
    total 1
    [1]  10278  Grid Service Agent  gigaspaces-8.0.0-XAPPremium...  pc-lab43@192.xxx.x.xx
    [2]  all

    Enter a comma-separated list to gsa shutdown or "c" to cancel :

{{%/accord%}}
{{%/accordion%}}


# jconsole

#### Syntax

    gs> jconsole [jmx-connection-string]

#### Description

The `jconsole` command launches the Service Grid Admin UI, allowing you to browse the JMX instance associated with a specific Grid Service Container (GSC).

{{% tip %}}
JMX provides remote management capabilities for monitoring memory utilization, VM system configuration, performance and garbage collection characteristics etc. By default, each GSC is set to run a JMX enabled VM.
For more details on monitoring and management using JMX, refer to the [JMX Management](./space-jmx-management.html) section.
{{% /tip %}}

{{% include "/COM/jconsolejmapwarning.markdown" %}}

#### Options


| Option | Description |
|:-------|:------------|
| `jmx-connection-string` | The JMX connection string to be used for starting the Java Management Console. If not provided, a selection list of all GSCs is displayed. You can select the appropriate instance from the list. |
| `h` / `help`  | Prints help |

{{%accordion%}}
{{%accord title="Example"%}}

    gs> jconsole
    total services 2
    [1]   Grid Service Container   gs-grid   69.203.99.5
    [2]   Grid Service Manager     gs-grid   69.203.99.5

    Choose a service for jconsole support or "c" to cancel : 1
    Launching jconsole, command successful

    gs> jconsole service:jmx:rmi:///jndi/rmi://192.10.10.10:10098/jmxrmi
    Launching jconsole, command successful
{{%/accord%}}
{{%/accordion%}}


# list

#### Syntax

    gs> list [type] [options]

#### Description

The `list` command lists information about active Service Grid services.

The following values are allowed for **`type`**. Only one type can be specified.


| Type | Description |
|:-----|:------------|
| `gsm` | Grid Service Monitor |
| `gsc` | Grid Service Container |
| `lus` | Lists all active Jini Lookup Service instances and their attributes. |
| Default (none) | List all types of services, but in less detail than when the types are specified individually: only service name, group, and machine. |

#### Options

Each option adds to (or subtracts from) the default information listed. You can specify one option, more than one, or none.

**The following options are available for types `gsm`, `gsc`, and default:**


| Option | Description |
|:-------|:------------|
| `cpu` | Lists the CPU utilization of each machine. |
| `jmx` | Lists the jmx connection entry, if any. |
| `codeserver` | Lists the IP addresses to which the service has been exported. |
| `timeout` | Available for type `lus` only (**new in GigaSpaces 6.0.2** and onwards) -- the discovery timeout (in milliseconds). Usage example: `timeout=20000`. Default value is 30000 msec. |


{{%accordion%}}
{{%accord title="Example"%}}


List All Services in the Network

    gs> list
    total services 4
    [1] Lookup gs-grid, gigaspaces-13 11.0.0.3
    [2] Grid Service Container gs-grid pc-nati@11.0.0.3
    [3] Lookup gigaspaces-1365 11.0.0.5
    [4] Grid Service Manager gs-grid pc-nati@11.0.0.3

List all Service Containers

    gs> list gsc
    total 1
    [1] Grid Service Container gs-grid pc-nati@11.0.0.3
    No contained services

List Grid Service Managers

    gs> list gsm
    total 1
    [1] Grid Service Manager gs-grid pc-nati@11.0.0.3
    GigaSpace Service Deployment role=primary
    GigaSpace planned=1 actual=1 pending=0
    id=1 11.0.0.3

List all Active Jini Lookup Service Instances and their Attributes

    gs> list lus
    Searching for available Jini Lookup Services...

    -----------------------------------------------------------------------
    -- Discovered Lookup Service at host [ 192.168.10.133:4160 ].
    -- Lookup Service registered to the following jini groups:
                     Group [ gigaspaces-6.0XAP-DOTNET-PC ]
    -- Lookup Service has [3] services, lookup took [1828] millis, [1] seconds:
                     Service Class: com.j_spaces.core.client.JSpaceProxy | 0ab4e4e1-ca5e-4be3-bc00-ac4648108012
                     Service Attributes Set: [net.jini.lookup.entry.Name(name=myExcelDemoSpace), com.j_spaces.lookup.entry.C
    lusterName(name=NONE), com.j_spaces.lookup.entry.ClusterGroup(electionGroup=null,replicationGroup=NONE,loadBalancingGrou
    p=null), com.j_spaces.lookup.entry.Persistent(type=MEMORY), com.j_spaces.lookup.entry.ContainerName(name=myExcelDemoSpac
    e_container), net.jini.lookup.entry.ServiceInfo(name=JavaSpace,manufacturer=GigaSpaces Technologies Ltd.,vendor=GigaSpac
    es,version=GigaSpaces Platform(TM) 6.0 XAP edition (build 2001-6),model=,serialNumber=), com.j_spaces.lookup.entry.HostN
    ame(name=dotnet-pc), com.j_spaces.lookup.entry.State(state=started,electable=null,replicable=null)]

                     Service Class: com.j_spaces.core.JSpaceContainerProxy | c8bd6d9d-0e79-4541-b7e4-1f3427279e5a
                     Service Attributes Set: [net.jini.lookup.entry.Name(name=myExcelDemoSpace_container), com.j_spaces.look
    up.entry.ContainerName(name=myExcelDemoSpace_container), net.jini.lookup.entry.ServiceInfo(name=JSpaceContainer,manufact
    urer=GigaSpaces Technologies Ltd.,vendor=GigaSpaces,version=GigaSpaces Platform(TM) 6.0 XAP edition (build 2001-6),model
    =,serialNumber=), com.j_spaces.lookup.entry.HostName(name=dotnet-pc)]

                     Service Class: com.sun.jini.reggie.RegistrarProxy | e375ba99-4b1f-4c13-9cf0-4b5b76eb2327
                     Service Attributes Set: [net.jini.lookup.entry.ServiceInfo(name=Lookup,manufacturer=Sun Microsystems, I
    nc.,vendor=Sun Microsystems, Inc.,version=2.1,model=,serialNumber=), com.sun.jini.lookup.entry.BasicServiceType(type=Loo
    kup), net.jini.lookup.entry.Name(name=Lookup), org.jini.rio.entry.OperationalStringEntry(name=Service Grid Infrastructur
    e)]

    -----------------------------------------------------------------------
    -- Discovered Lookup Service at host [ 192.168.10.36:4160 ].
    -- Lookup Service registered to the following jini groups:
                     Group [ gigaspaces-6.0XAP-mishak ]
    -- Lookup Service has [9] services, lookup took [1203] millis, [1] seconds:
                     Service Class: $Proxy18 | 2edbae56-79d6-43bb-abb0-182c5b11a6d0
                     Service Attributes Set: [org.jini.rio.entry.OperationalStringEntry(name=Compute Grid), org.jini.rio.ent

{{%/accord%}}
{{%/accordion%}}

#### Troubleshooting: terminated services are still listed

The `list` command uses the lookup service to extract the information regarding the services.

A service's entry in the lookup is leased and kept until the next renewal attempt fails. If the service is not properly shutdown, i.e. abruptly terminated, it doesn't un register itself from the lookup service. Thus, attempts to call `list` will still display the service until its lease expires.

For defaults and configuration options, refer to [Jini Lookup Service configuration]({{%currentadmurl%}}/network-lookup-service-configuration.html) settings for `minMaxServiceLease` property.


# login

#### Syntax

    gs> login
    gs> login -user xxx -password yyy

#### Description

{{% refer %}}[Command Line Interface (CLI) Security]({{%currentsecurl%}}/command-line-interface-cli-security.html){{% /refer %}}

This CLI command allows you to login to secured services: GSM, GSC and spaces.
Each time you invoke this command, you are required afterwards to type in the user name and password (if not supplied in the command). The user name and password are used in order to attempt to authenticate secured services before invoking any operation for them ( e.g. pudeploy, undeploy, space clear, space connections ... ).

{{%accordion%}}
{{%accord title="Example"%}}

    gs> login
    Please enter user name:
    my_name
    Please enter user password:

    gs>
{{%/accord%}}
{{%/accordion%}}


# ls

#### Syntax

    gs> ls [-l]

#### Description

The `ls` command lists the contents of the current working directory, like the Unix `ls` command.

#### Options


| Option | Description |
|:-------|:------------|
| `-l` | List the contents in more detail. |


# pwd

#### Syntax

    gs> pwd

#### Description

The `pwd` command displays the full path of the current working directory, like the Unix `pwd` command.

#### Options

None.


# quit

#### Syntax

    gs> quit

#### Description

The `quit` command ends the CLI session.

{{% tip %}}
A shortcut for this command is `q`.
{{% /tip %}}

#### Options

None.

# set

#### Syntax

    gs> set [variable [value]]

#### Description

The `set` command sets the Service Grid system environment variables.

#### Options


| Option | Description | Value Format |
|:-------|:------------|:-------------|
| `groups` | The Jini lookup groups value. The group filters the specific group of services that should be managed by the current shell context. | * List of comma-delimited names: `group1,group2,...`{{<wbr>}}    - `all_groups` assigns the current context to all available GridService elements in the network. |
| `locators` | A list of Jini locators hosts. The locators provide an alternate discovery mechanism, if multicast is not working. The locator points to the location of Jini lookup hosts. Normally a lookup service is running as an embedded instance with the Grid Service Monitor (GSM). | `jini://<hostname1>`{{<wbr>}}    To set several locators you have to execute:{{<wbr>}}    `set locators jini://<hostname1>`{{<wbr>}}    `set locators jini://<hostname2>`{{<wbr>}}    ...{{<wbr>}}    To empty the locators variable you have to execute:{{<wbr>}}
`set locators null` |
| `system-props` | Should be one of the properties used by the system. Omit this parameter to view a list of the available properties. | `<system property name>=<property value>` |
| `disco-timeout` | Jini discovery timeout \[in ms\]. Defines the time for waiting for service discovery announcements. Relevant for multicast discovery only. | |
| `wait-on-deploy` | Timeout \[in ms\] for finding deployable servers. | |
| `deploy-timeout` | Timeout \[in ms\] for blocking for deployment status. |

{{% refer %}}
Make sure your network and machines running GigaSpaces are configured to have multicast enabled.
See the [How to Configure Multicast]({{%currentadmurl%}}/network-multicast.html) section for details on how to enable multicast.
{{% /refer %}}

{{%accordion%}}
{{%accord title="Example"%}}


Displaying Current System Variables

    gs> set
    groups ALL_GROUPS
    locators null
    system-props com.gigaspaces.grid.lib=C:demosGS-Cache1362GigaSpacesXAP6.0ServiceGridlib
    com.gigaspaces.grid.home=C:demosGS-Cache1362GigaSpacesXAP6.0ServiceGrid
    com.gs.jini_lus.groups=gigaspaces-1346
    com.gigaspaces.jini.lib=C:demosGS-Cache1362GigaSpacesXAP6.0ServiceGrid..libjini
    com.gigaspaces.lib=C:demosGS-Cache1362GigaSpacesXAP6.0ServiceGrid..lib
    com.gs.home= C:demosGS
    disco-timeout 5000
    wait-on-deploy true
    deploy-timeout 5000

Setting New Location for Two Properties with Timeout

This example sets new locations for the properties `com.gigaspaces.lib` and `com.gs.home`, with a timeout of five and a half seconds for discovery.

    gs> set system-props com.gigaspaces.lib=c:gslib,com.gs.home=c:gs
    gs> set discovery-timeout 5500
    gs> set groups all_groups
    gs> set groups gs-grid,test
{{%/accord%}}
{{%/accordion%}}


# stats

#### Syntax

    gs> stats

#### Description

The `stats` command provides statistics from the Grid Service Container (GSC) and Grid Service Monitor (GSM) of the local machine. The following statistics are reported:


| Statistic | Decription |
|:----------|:-----------|
| User name | Current user name in local operating system. |
| Home directory | Service Grid installation directory. |
| Time of login | Time the Service Grid CLI was started. |
| Elapsed time since login | Elapsed time since the Service Grid CLI was started. |
| Pathname of log file | Where advertised, the group advertised, and the time when the service was found to be discarded. |
| HTTP | Address (plus roots) if any HTTP server has been started under this session . |
| Lookup service discovery statistics | Statistics on discovered lookup instances, including IP address and port, group, and time elapsed. At the end, statistics are shown for discarded lookup services, if any. |

#### Options

None.

{{%accordion%}}
{{%accord title="Example"%}}

    gs> stats

    User : nuser
    Home directory :  GigaSpacesXAP6.0ServiceGridbin
    Login time : Mon Jan 30 12:02:59 GMT+02:00 2006
    Time logged in : 14 minutes, 14 seconds
    Log file :  GigaSpacesXAP6.0ServiceGridbings.log
    http : No HTTP server started
    Lookup Service Discovery Statistics
            No lookup services discovered
    Lookup Service Discarded Statistics
            127.0.0.1:4160     gs-grid, gigaspaces-13 Mon Jan 30 12:04:16 GMT+02:00 2006

{{%/accord%}}
{{%/accordion%}}

# version

#### Syntax


```java
gs> version [-verbose]
```

#### Description

The  `version ` command displays the product's version information.

#### Options


|Option | Description |
|----------|------------|
|  `-verbose ` | Displays additional environment information (OS, JVM, network, system properties, etc). |

