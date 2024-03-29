---
type: post123
title:  Configuration
categories: XAP123GS
parent: none
weight: 300
canonical: auto
---


This topic explains how the XAP and InsightEdge environments are configured, and how to customize them to suit your specific application needs.

## XAP

The XAP environment configuration is maintained by a configuration script file called `setenv`, located in the `XAP_HOME/bin` directory. Each XAP script invokes this script to load the XAP configuration. If you are developing a  standalone XAP client, it is recommended to use the `setenv` utility to derive the commonly used XAP libraries and setup environment. 

To use this utility, you can simply call it from within your script file.

## InsightEdge

The InsightEdge environment configuration is maintained by a configuration script file called `insightedge-env`, located in the `XAP_HOME/insightedge/conf` directory. Each InsightEdge script invokes this script to load the InsightEdge configuration. Additionally, `insightedge-env` starts by invoking the XAP `setenv` configuration script, so all of the XAP environment configuration is also applied.

# Customizing the Environment Variables

During the initial development stages, it is usually unnecessary to change any of the default XAP environment values. However, at some point the environment will need customization to suit your specific requirements (such as the Grid Service Container heap size). 

{{%note "Note"%}}
Do not make changes to the original `setenv` script, as it complicates upgrading XAP later on. Instead, use the `setenv-overrides` script, which is automatically called by `setenv` and is intended for specifying custom overrides in a safe manner.
{{%/note%}}

# XAP Environment Variables

The following list describes XAP-related environment variables:

|Name                   |Description                                           |Default Value|
|:----------------------|:-----------------------------------------------------|:------------|
|  JAVA_HOME            | The directory in which Java is installed             | |
|  XAP_HOME             | The GigaSpaces XAP home directory                    | Automatically set via the folder structure |
|  XAP_LICENSE          | License key (for Premium and Enterprise editions)    | |
|  XAP_LOOKUP_GROUPS    | Lookup Service groups used for multicast discovery   | {{%version "default-lookup-group"%}} |
|  XAP_LOOKUP_LOCATORS  | Lookup Service Locators used for unicast discovery   | |
|  XAP_NIC_ADDRESS      | The network interface card which will be used by XAP | Automatically set to the host name |
|  XAP_SECURITY_POLICY  | The default policy file.|XAP_HOME/policy/policy.all  | |
|  XAP_LOGS_CONFIG_FILE | The location of the XAP logging configuration        | XAP_HOME/config/log/xap_logging.properties |
|  XAP_MANAGER_SERVERS  | Set the list of Manager servers that other machines can connect to   | |
|  XAP_PUBLIC_HOST	    | Define the public IP address for the Docker host machine | |
|  XAP_MANAGER_OPTIONS  | Java options for the XAP Manager                     | |
|  XAP_GSC_OPTIONS      | Java options for the Grid Service Container (GSC)    | |
|  XAP_GSM_OPTIONS      | Java options for the Grid Service Manager (GSM) 	{{%exclamation%}}      | |
|  XAP_GSA_OPTIONS      | Java options for the Grid Service Agent (GSA)        | |
|  XAP_LUS_OPTIONS      | Java options for the Lookup Service (LUS) 	{{%exclamation%}}            | |
|  XAP_ESM_OPTIONS      | Java options for the Elastic Service Manager (ESM)   | |
|  XAP_GUI_OPTIONS      | Java options for the GigaSpaces Management Center    | |
|  XAP_WEBUI_OPTIONS    | Java options for the Web Management Console          | |

{{%exclamation%}} When running in `./gs-agent --manager` configuration, this environment variable is ignored. 


# InsightEdge Environment Variables

The following list describes InsightEdge-related environment variables:

|Name                       |Description                                            |Default Value|
|:--------------------------|:------------------------------------------------------|:------------|
| SPARK_HOME                | The directory where Spark is installed                | `XAP_HOME/insightedge` |
| INSIGHTEDGE_CLASSPATH_EXT | Extra classpath to append to InsightEdge components   | |
| INSIGHTEDGE_SPACE_NAME    | Space name to use in InsightEdge scripts and examples | `insightedge-space`    |

In addition, you can also use standard Spark environment variables. The InsightEdge platform loads Spark components in a manner that preserves their usage. For example, set `SPARK_MASTER_PORT` to override the default `7077` port.

# Upgrading from Previous Versions

In previous versions, environment variable names were inconsistent, which occasionally led to confusion. Starting with XAP 11.0, all XAP-related environment variables have been renamed with a `XAP_` prefix, so they're easier to identify. The following table maps the pre-11.0 names to the current names:

|Name before 11.0|Name in 12.0 and higher|
|:---|:----------|
|  JSHOMEDIR  |  XAP_HOME  |
|  LOOKUPGROUPS  |  XAP_LOOKUP_GROUPS  |
|  LOOKUPLOCATORS  |  XAP_LOOKUP_LOCATORS  |
|  NIC_ADDR  |  XAP_NIC_ADDRESS  |
|  POLICY  |  XAP_SECURITY_POLICY  |
|  GS_LOGGING_CONFIG_FILE  |  XAP_LOGS_CONFIG_FILE  |
|  GSC_JAVA_OPTIONS  |  XAP_GSC_OPTIONS  |
|  GSM_JAVA_OPTIONS  |  XAP_GSM_OPTIONS  |
|  GSA_JAVA_OPTIONS  |  XAP_GSA_OPTIONS  |
|  LUS_JAVA_OPTIONS  |  XAP_LUS_OPTIONS  |
|  ESM_JAVA_OPTIONS  |  XAP_ESM_OPTIONS  |

{{%anchor extension %}}

{{%note "Info"%}}
 If you'd rather postpone or avoid changing your scripts to the new names, you can use the new `setenv-overrides` script to map the corresponding values. For example, suppose you customized the lookup groups and the GSC options. If you followed GigaSpaces best practices, you created a custom script to set those environment variables before calling the original script, for example:


{{%tabs%}}
{{%tab " my-gs-agent.sh (Unix)"%}}
```bash
export LOOKUPGROUPS=foo
export GSC_JAVA_OPTIONS=-Xms1024m -Xmx1024m
./gs-agent.sh
```
{{%/tab%}}
{{%tab " my-gs-agent.bat (Windows)"%}}
```bash
set LOOKUPGROUPS=foo
set GSC_JAVA_OPTIONS=-Xms1024m -Xmx1024m
call gs-agent.bat
```
{{%/tab%}}
{{%/tabs%}}

<br>
The `gs-agent` script calls the `setenv` script to set up the environment, which in turn calls the `setenv-overrides` script mentioned above to implement any customization that overrides the default values. You can modify `setenv-overrides` to propagate the old variables to the new ones, for example:

{{%tabs%}}
{{%tab " setenv-overrides.sh (Unix)"%}}
```bash
export XAP_LOOKUP_GROUPS=$LOOKUPGROUPS
export XAP_GSC_OPTIONS=$GSC_JAVA_OPTIONS
```
{{%/tab%}}
{{%tab " setenv-overrides.bat (Windows)"%}}
```bash
set XAP_LOOKUP_GROUPS=%LOOKUPGROUPS%
set XAP_GSC_OPTIONS=%GSC_JAVA_OPTIONS%
```
{{%/tab%}}
{{%/tabs%}}

{{%/note%}}
