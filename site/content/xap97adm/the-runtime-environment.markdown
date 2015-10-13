---
type: post97
title:  The Runtime Environment
categories: XAP97ADM
parent: runtime-configuration.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}

{{% anchor GSRuntimeEnv %}}

A processing unit is deployed onto the XAP runtime environment, which is called the [Service Grid](/product_overview/the-runtime-environment.html). It is responsible for materializing the processing unit's configuration, provisioning its instances to the runtime infrastructure and making sure they continue to run properly over time.


# Starting a Service Grid

In order to start the GSA, the `<GSHOME>/bin/gs-agent.(sh/bat)` can be used.

The preferable (and easiest) way to start a Service Grid is the [Grid Service Agent](/product_overview/service-grid.html#gsa). However, each of the components can be started manually.

The following table summarized how to start each component:


|Component | Linux (XAP) | Windows (XAP) | Windows (XAP.NET) |
|:---------|:------------|:--------------|:------------------|
| GSA | `gs-agent.sh` | `gs-agent.bat` | gs-agent.exe |
| GSC | `gsc.sh` | `gsc.bat` | `gsc.exe` |
| GSM | `gsm_nolus.sh` | `gsm_nolus.bat` | N\A |
| LUS | `startJiniLUS.sh` | `startJiniLUS.bat` | `lus.exe` |
| GSM + LUS | `gsm.sh` | `gsm.bat` | `gsm.exe` |

## GSA Parameters

The GSA parameters control how many local process the GSA will spawn on startup (per process type), and the number of globally managed process the GSA will maintain (in cooperation with other GSAs) (per process type). By default, the GSA is started with 2 local [Grid Service Containers](#gsc), and manage 2 global [Grid Service Manager](#gsm) and 2 global [Lookup Service](/product_overview/service-grid.html#lus). This is the equivalent of starting the GSA with the following parameters:


```java
gs-agent gsa.gsc 2 gsa.global.gsm 2 gsa.global.lus 2
```

In order to, for example, start 3 local GSCs, 2 global GSMs, and no global LUS, the following command can be used:


```java
gs-agent gsa.gsc 3 gsa.global.gsm 2 gsa.global.lus 0
```

In general, the `gsa.[process type]` followed by a number controls the number of local processes of the specific process type that will be spawned by the GSA. The `gsa.global.[process type]` following by a number controls the number of globally managed processes of the specific process type.

### Lookup Service Considerations

When starting a [Lookup Service](/product_overview/service-grid.html#lus) and other services in unicast mode (not multicast), it means that specific machines will be the ones that will run the [Lookup Service](/product_overview/service-grid.html#lus). This means that on the machines running the LUS, the following command will be used (assuming other defaults are used for GSM and GSC):


```java
gs-agent gsa.global.lus 0 gsa.lus 1
```

And on machines that will not run the LUS, the following command should be used:


```java
gs-agent gsa.global.lus 0
```


{{%tip%}}
You can use the [GigaSpaces Universal Deployer](/sbp/universal-deployer.html) to deploy complex multi processing unit applications.
{{%/tip%}}

# Customizing GSA Components

GSA manages different process types. Each process type is defined within the `<GSHOME>\config\gsa` directory in an xml file that identifies the process type by its name.

{{% tip %}}You can change the default location of the GSA configuration files using the `com.gigaspaces.grid.gsa.config-directory` system property.
{{% /tip %}}

The following are the process types that come out of the box:


|Processes Type|Description|XML config file name|Properties file name|
|:-------------|:----------|:-------------------|:-------------------|
|gsc|Defines a [Grid Service Container](/product_overview/service-grid.html#gsc)|gsc.xml|gsc.properties|
|gsm|Defines a [Grid Service Manager](/product_overview/service-grid.html#gsm)|gsm.xml|gsm.properties|
|lus|Defines a [Lookup Service](/product_overview/service-grid.html#lus)| lus.xml|lus.properties|
|gsm_lus|Defines a [Grid Service Manager](/product_overview/service-grid.html#gs) and [Lookup Service](/product_overview/service-grid.html#lus) within the same JVM|gsm_lus.xml|gsm_lus.properties|
|esm|Defines an Elastic Service Manager which is required for deploying [Elastic Processing Unit]({{%currentjavaurl%}}/elastic-processing-unit.html)|esm.xml|esm.properties |

Here is an example of the gsc xml configuration file:


```xml
<process initial-instances="script" shutdown-class="com.gigaspaces.grid.gsa.GigaSpacesShutdownProcessHandler" restart-on-exit="always">
 <script enable="true" work-dir="${com.gs.home}/bin"
  windows="${com.gs.home}/bin/gsc.bat"
  unix="${com.gs.home}/bin/gsc.sh">
  <argument></argument>
 </script>
 <vm enable="true" work-dir="${com.gs.home}/bin"
   main-class="com.gigaspaces.start.SystemBoot">
   <input-argument></input-argument>
   <argument>com.gigaspaces.start.services="GSC"</argument>
 </vm>
 <restart-regex>.*OutOfMemoryError.*</restart-regex>
</process>
```

The GSA can either spawn a script based process, or a pure JVM (with its arguments) process. The GSC for example, allows for both types of process spawning.

* The `initial-instances` parameter controls what type of spawning will be performed when the GSA spawns processes by itself (and not on demand by the Admin API). <br>
* The `shutdown-class` followed by the `restart-on-exit` flag, controls if the process will be restarted upon termination. There are three types of values: <br>
   * restart-on-exit="always": Always restarts the process if it exits<br>
   * restart-on-exit="never": Never restarts the process if it exists<br>
   * restart-on-exit="!0": Restarts the process only if the exit code is different than 0<br>
* The `shutdown-class` is an implementation of `com.gigaspaces.grid.gsa.ShutdownProcessHandler` interface and provides the default shutdown hooks before and after shutdown of a process, to make sure it is shutdown properly. The `shutdown-class` can be omitted.<br>
* The `restart-regex` (there can be more than one element) is applied to each console output line of the managed process, and if there is a match, the GSA will automatically restart the process. By default, the GSC will be restarted if there is an `OutOfMemoryError`.

# Grid Configuration

Service Grid configuration is often composed of two layers: system-wide configuration and component-specific configuration.
The system-wide configuration specifies settings which all components share, e.g. discovery (unicast/multicast), security, zones, etc. These are set in the `EXT_JAVA_OPTIONS` environment variable.
The component-specific configuration specifies settings per component type, e.g. the GSC memory limit is greater than the GSM and LUS. These are set in one or more of the following environment variables:

- `GSA_JAVA_OPTIONS`
- `GSC_JAVA_OPTIONS`
- `GSM_JAVA_OPTIONS`
- `LUS_JAVA_OPTIONS`

For example:

{{% section %}}
{{% column width="50%" %}}

```java
Linux
export GSA_JAVA_OPTIONS=-Xmx256m
export GSC_JAVA_OPTIONS=-Xmx2048m
export GSM_JAVA_OPTIONS=-Xmx1024m
export LUS_JAVA_OPTIONS=-Xmx1024m

. ./gs-agent.sh
```
{{% /column %}}

{{% column width="45%" %}}

```java
Windows
set GSA_JAVA_OPTIONS=-Xmx256m
set GSC_JAVA_OPTIONS=-Xmx2048m
set GSM_JAVA_OPTIONS=-Xmx1024m
set LUS_JAVA_OPTIONS=-Xmx1024m

call gs-agent.bat

```
{{% /column %}}
{{% /section %}}

#### Configuring independent components

It is highly recommended that the Service Grid is started (and configured) using the gs-agent as shown above. If you do need to start a specific component separately, you can use the same environment variables shown above.

For example:

{{% section %}}
{{% column width="50%" %}}

```java
Linux
export GSC_JAVA_OPTIONS=-Xmx1024m

. ./gsc.sh
```
{{% /column %}}
{{% column width="45%" %}}

```java
Windows
set GSC_JAVA_OPTIONS=-Xmx1024m

call gsc.bat
```
{{% /column %}}
{{% /section %}}

{{% note %}}
Component specific configuration can be set using system properties (follows the [component name].[property name] notation).
{{%/note%}}


