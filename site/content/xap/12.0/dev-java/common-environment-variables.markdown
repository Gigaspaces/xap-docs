---
type: post120
title:  Environment Variables
categories: XAP120
parent: installation.html
weight: 120
---

The XAP environment configuration is maintained by a configuration script file called `setenv`, located in the `XAP_HOME/bin` directory. It is recommended to use the `setenv` utility to derive the commonly used XAP libraries and setup environment. 
To use this utility, you simply need to call it from within your script file.

The following list describes commonly used variables which are defined in this script:

|Name|Description|Default Value|
|:---|:----------|:------------|
|  JAVA_HOME            |The directory in which Java is installed              ||
|  XAP_HOME             |The GigaSpaces XAP home directory                     | Automatically set via the folder structure |
|  XAP_LOOKUP_GROUPS    |Lookup Service groups used for multicast discovery    | {{%version "default-lookup-group"%}} |
|  XAP_LOOKUP_LOCATORS  | Lookup Service Locators used for unicast discovery   ||
|  XAP_NIC_ADDRESS      | The network interface card which will be used by XAP | Automatically set to the host name |
|  XAP_SECURITY_POLICY  | The default policy file.|XAP_HOME/policy/policy.all  |
|  XAP_LOGS_CONFIG_FILE | The location of XAP logging configuration            | XAP_HOME/config/log/xap_logging.properties |
|  XAP_GSC_OPTIONS      | Java options for the Grid Service Container (GSC)    ||
|  XAP_GSM_OPTIONS      | Java options for the Grid Service Manager (GSM)      ||
|  XAP_GSA_OPTIONS      | Java options for the Grid Service Agent (GSA)        ||
|  XAP_LUS_OPTIONS      | Java options for the Lookup Service (LUS)            ||
|  XAP_ESM_OPTIONS      | Java options for the Elastic Service Manager (ESM)   ||

# Overriding Default Values

During initial development and usage of XAP there's usually no need to change any of the default values, but at some point you'll probably want to change some of them (e.g. the Grid Service Container heap size). 


{{%note%}}
It is highly recommended not to make those changes in the original `setenv` script, as it complicates upgrading XAP later on. Instead, XAP provides an additional empty script called `setenv-overrides`, which is automatically called by `setenv`, and is intended for users to specify their overrides in a safe manner.
{{%/note%}}

# Upgrading From Previous Versions

In previous versions, environment variable names were inconsistent, which occasionally led to confusion. Starting with XAP 11.0, all XAP related environment variables have been renamed to have a `XAP_` prefix, so they're easier to identify. The following table maps the pre-11.0 names to the new names:

|Name before 11.0|Name in 11.0|
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

{{%tip%}}
 If you'd rather postpone or avoid changing your scripts to the new names, you can use the new `setenv-overrides` script to map the corresponding values. For example, suppose in the past you've needed to override the default lookup groups and the GSC options. If you've followed the best practices, you probably created a custom script to set those environment variables before calling the original script, something like:
{{%/tip%}}


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

The `gs-agent` script calls the `setenv` script to setup the environment, which in turn calls the `setenv-overrides` script mentioned above to allow users to override the default values. You can modify `setenv-overrides` to propagate the old variables to the new ones, for example:

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
