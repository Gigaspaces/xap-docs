---
type: post123
title:  Scripts
categories: XAP123ADM, OSS
parent: administration-tools.html
weight: 50
---

{{% note "Note"%}}
Some of the features described here are part of the open source edition, while others are only available with the commercial (licensed) editions.
{{% /note %}}

The `<XAP root>/bin` folder includes the following scripts that can be used to manage and monitor [GigaSpaces Runtime](./the-runtime-environment.html) components and applications. 

**Open-Source Scripts**

- **setenv** - Used by all scripts to configure and load [Common Environment Variables]({{%currentjavaurl%}}/common-environment-variables.html).

**Additional Scripts**

The scripts listed below are available in the licensed editions of the GigaSpaces applications.

- **gs** - starts the GigaSpaces [interactive shell](./command-line-interface.html).
- **gs-ui** - starts the [GigaSpaces Management Center](./gigaspaces-management-center.html).
- **gs-webui** - starts the [Web Management Console](./web-management-console.html).
- **gs-agent** - Starts the [GigaSpaces runtime environment](./the-runtime-environment.html) via the [Grid Service Agent](../overview/the-runtime-environment.html#gsa) (GSA), which starts and manages the [Service Grid](../overview/the-runtime-environment.html) components.


In addition to the scripts described [here](./scripts.html), the `bin` folder contains **advanced_scripts.zip** for additional tasks, usually for development and troubleshooting.

# Service Grid Scripts

If you need to start the [Service Grid](../overview/the-runtime-environment.html) components manually instead of via the [gs-agent](../overview/the-runtime-environment.html#gsa), use the following scripts.

**Open-Source Scripts**

- **lookup-service** - starts an instance of the [LUS](../overview/the-runtime-environment.html#lus).

**Additional Scripts**

The scripts listed below are available in the licensed editions of the GigaSpaces applications.

- **gsc** - starts an instance of the [GSC](../overview/the-runtime-environment.html#gsc).
- **gsm** - starts an instance of the [GSM](../overview/the-runtime-environment.html#gsm) and [LUS](../overview/the-runtime-environment.html#lus).
- **gsm_nolus** - starts an instance of the [GSM](../overview/the-runtime-environment.html#gsm).
- **esm** - starts an instance of the [ESM]({{%currentjavaurl%}}/elastic-processing-unit-overview.html).
- **startJiniTX_Mahalo** - starts an instance of the Distributed transaction manager.

# Processing Units

**Open-Source Scripts**

- **pu-instance** - starts a standalone, un managed instance of a processing unit.
- **space-instance** - starts a standalone, un managed instance of a space. Used usually in development.

**Additional Scripts**

The script listed below is available in the licensed editions of the GigaSpaces applications.

- **gs-memcached** - starts standalone, un managed instance of [Memcached API]({{%currentjavaurl%}}/memcached-api.html) listener.

# Misc

The scripts listed below are available in the licensed editions of the GigaSpaces applications.

- **lookupbrowser** - Used with for special debug scenarios to inspect the lookup service.
- **platform-info** - prints GigaSpaces version info (Use the command line [version](./command-line-interface.html) instead).

This section explains how to start a light version of the GigaSpaces server, which loads a container and one space, using the `space-instance` script. The `space-instance` (which calls [SpaceFinder]({{% api-javadoc %}}/index.html?com/j_spaces/core/client/SpaceFinder.html)) starts by default embedded Reggie and Webster services.

# Starting Embedded Mahalo

By default, `space-instance` does not start an embedded Mahalo (Jini Transaction Manager).

You can enable this option in one of the following ways:

- Setting the following option to `true` in your container schema:


```xml
<embedded-services>
...
<mahalo>
	<!-- If true, will start an embedded Mahalo Jini Transaction Manager. Default value: false -->
    <start-embedded-mahalo>${com.gs.start-embedded-mahalo}</start-embedded-mahalo>
</mahalo>
```

- Setting the following option in the `space-instance` command line:

    -Dcom.gs.start-embedded-mahalo=true

- Setting XPath in the `<XAP Root>\config\gs.properties` file:

    com.j_spaces.core.container.embedded-services.mahalo.start-embedded-mahalo=true

{{% note "Note"%}}
XAP supports space monitoring and management using JMX - The Java Management Extensions. For more details, refer to the [JMX Management](./space-jmx-management.html) section.
{{% /note %}}

{{% note "Note"%}}
When running `space-instance`, the Jini Lookup Service runs implicitly. When having many Jini Lookup Services running across the network, the spaces and clients might be overloaded since they publish themselves into the Lookup Service, or are trying to get updates about newly registered services.
A good practice is to have two Lookup Services running using the `lookup-service` command located in the `<XAP Root>\bin` directory, or the GSM command located in the `<XAP Root>\bin` folder. This ensures no single point of failure for the Lookup Service.
{{% /note %}}

### Syntax & Arguments

The full `space-instance` syntax (the arguments passed below are optional):


```java
space-instance "/./newSpace?schema=persistent" "../../classes" "-DmyOwnSysProp=value -DmyOwnSysProp2=value"
```

The `space-instance` arguments are passed through the command line. These arguments are optional - if you do not want to pass any arguments, you don't have to specify anything in the command line, as seen below:


```java
space-instance
```

You can use three arguments. All arguments must be enclosed by quotes (`" "`). If used, the arguments must be entered in the following order (descending):


| Argument | Description |
|:---------|:------------|
| <nobr>Argument 1 <nobr>| Defines a space URL. The value is set into the `SPACE_URL` variable. If no value is passed for this argument, the space URL defined in the `space-instance` script is used. |
| Argument 2 | Defines a path which will be appended to the beginning of the used classpath. The value you define is set into the `APPEND_TO_CLASSPATH_ARG` variable. If no value is passed, the classpath defined in the `space-instance` script is used. |
| Argument 3 | Defines additional command line arguments such as system properties. The value is set into the `APPEND_ADDITIONAL_ARG` variable. |

If you are using the third and/or second argument only, **you must use empty quote signs for the argument or arguments that come before the one you are using**. For example:

    space-instance "" "" "-DmyOwnSysProp=value -DmyOwnSysProp2=value"

In the example above, only the third argument is used, so two pairs of empty quote signs are written before it. In this case, the default URL and classpath (defined in the `space-instance` script) are used, and only the system properties are appended.




