---
type: post110
title:  The Runtime Environment
categories: XAP110ADM
parent: runtime-configuration.html
weight: 100
canonical: auto
---

{{% ssummary %}}{{% /ssummary %}}

{{% anchor GSRuntimeEnv %}}

A processing unit is deployed onto the XAP runtime environment, which is called the [Service Grid](/product_overview/the-runtime-environment.html). It is responsible for materializing the processing unit's configuration, provisioning its instances to the runtime infrastructure and making sure they continue to run properly over time.

# Starting a Service Grid

To start a Service Grid on a machine, launch the `gs-agent` utility located in the `<XAPHOME>/bin` folder. This will start the [Grid Service Agent](/product_overview/service-grid.html#gsa), which is responsible of starting and managing the other Service Grid components (GSC, GSM, etc.). Command-line arguments are used to specify which Service Grid components should be started and managed. In general, `gsa.[process type] n` will start `n` instances of the specified `process type`. Use the `global` keyword (e.g. `gsa.global.[process type] n`) to specify that the agent should coordinate with other running agents the hosting and management of that service. For example, to start two GSCs, two global GSMs and two global LUSs, use the following command:


```xml
gs-agent gsa.gsc 2 gsa.global.gsm 2 gsa.global.lus 2
```

In fact, since this configuration is widely used, it is also default configuration - running `gs-agent` without any arguments would produce the same effect. If you simply want more GSCs (say, 3 instead of 2) and keep the other defaults, simply run:


```xml
gs-agent gsa.gsc 3
```

### Lookup Service Considerations

When starting a [Lookup Service](/product_overview/service-grid.html#lus) and other services in unicast mode (not multicast), it means that specific machines will be the ones that will run the [Lookup Service](/product_overview/service-grid.html#lus). This means that on the machines running the LUS, the following command will be used (assuming other defaults are used for GSM and GSC):


```xml
gs-agent gsa.global.lus 0 gsa.lus 1
```

And on machines that will not run the LUS, the following command should be used:


```xml
gs-agent gsa.global.lus 0
```

{{%refer%}}
You can use the [GigaSpaces Universal Deployer](/sbp/universal-deployer.html) to deploy complex multi processing unit applications.
{{%/refer%}}

# Configuration

Service Grid configuration is often composed of two layers: system-wide configuration and component-specific configuration.

The system-wide configuration specifies settings which all components share, e.g. discovery (unicast/multicast), security, zones, etc. These are set using the `EXT_JAVA_OPTIONS` environment variable.

The component-specific configuration specifies settings per component type, e.g. the GSC memory limit is greater than the GSM and LUS. These are set using one or more of the environment variables: `XAP_GSA_OPTIONS`, `XAP_GSC_OPTIONS`, `XAP_GSM_OPTIONS`, `XAP_LUS_OPTIONS`.

{{%info%}}The component-specific configuration override the system-wide configuration. {{%/info%}}

For example:

{{% section %}}
{{% column width="50%" %}}

```xml
Linux
export XAP_GSA_OPTIONS=-Xmx256m
export XAP_GSC_OPTIONS=-Xmx2048m
export XAP_GSM_OPTIONS=-Xmx1024m
export XAP_LUS_OPTIONS=-Xmx1024m

./gs-agent.sh
```
{{% /column %}}

{{% column width="45%" %}}

```xml
Windows
set XAP_GSA_OPTIONS=-Xmx256m
set XAP_GSC_OPTIONS=-Xmx2048m
set XAP_GSM_OPTIONS=-Xmx1024m
set XAP_LUS_OPTIONS=-Xmx1024m

call gs-agent.bat
```
{{% /column %}}
{{% /section %}}

# Customizing GSA Components

GSA manages different process types. Each process type is defined within the `<XAPHOME>\config\gsa` directory in an xml file that identifies the process type by its name.

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

Here is an example of the `gsc.xml` configuration file:


```xml
<process initial-instances="script" shutdown-class="com.gigaspaces.grid.gsa.GigaSpacesShutdownProcessHandler" restart-on-exit="always">
    <script enable="true" work-dir="${com.gs.home}/bin"
            windows="${com.gs.home}/bin/gs.bat" unix="${com.gs.home}/bin/gs.sh">
        <argument>start</argument>
        <argument>"GSC"</argument>
    </script>
    <vm enable="true" work-dir="${com.gs.home}/bin" main-class="com.gigaspaces.start.SystemBoot">
        <input-argument></input-argument>
        <argument>com.gigaspaces.start.services="GSC"</argument>
    </vm>
    <restart-regex>.*java\.lang\.OutOfMemoryError.*</restart-regex>
</process>
```

The GSA can either spawn a script based process, or a pure JVM (with its arguments) process. The GSC for example, allows for both types of process spawning.

* The `initial-instances` parameter controls what type of spawning will be performed when the GSA spawns processes by itself (and not on demand by the Admin API).
* The `shutdown-class` followed by the `restart-on-exit` flag, controls if the process will be restarted upon termination. There are three types of values:
   * `always` - Always restarts the process if it exits.
   * `never` - Never restarts the process if it exists.
   * `!0` - Restarts the process only if the exit code is different than 0.
* The `shutdown-class` is an implementation of `com.gigaspaces.grid.gsa.ShutdownProcessHandler` interface and provides the default shutdown hooks before and after shutdown of a process, to make sure it is shutdown properly. The `shutdown-class` can be omitted.
* The `restart-regex` (there can be more than one element) is applied to each console output line of the managed process, and if there is a match, the GSA will automatically restart the process. By default, the GSC will be restarted if there is an `OutOfMemoryError`.

In addition, within the `script` tag, you can add the following tags:

* `argument` - adds a command ling argument which will be passed to the script. In the `gsc.xml` example above, there are two command line arguments.
* `environment` -adds an environment variable. For example, `<environment name="XAP_GSC_OPTIONS">-Xmx1024m</environment>` can be used to override the memory for the GSC.

# Advanced Configuration

In some scenarios you'll need to have several 'flavours' of components (e.g. multiple zones, or different sizes of GSCs, etc.). You can create a custom gs-agent script to manage each of those, or you can do this all within a single agent.

For example, suppose we want our agent to load 2 'small' GSCs (512MB each) in a zone called *Small*, and 1 'large' GSC (1024MB) in a zone called *Large*. To achieve this, we'll duplicate the default `gsc.xml` (which resides in `<XAPHOME>/config/gsa`) into `gsc_small.xml` and `gsc_large.xml`, and modify them to include an `environment` tag which sets `XAP_GSC_OPTIONS` to the required settings:

{{%tabs%}}
{{%tab "  gsc_small.xml "%}}

```xml
<process initial-instances="script" shutdown-class="com.gigaspaces.grid.gsa.GigaSpacesShutdownProcessHandler" restart-on-exit="always">
    <script enable="true" work-dir="${com.gs.home}/bin"
            windows="${com.gs.home}/bin/gs.bat" unix="${com.gs.home}/bin/gs.sh">
        <argument>start</argument>
        <argument>"GSC"</argument>
		<environment name="XAP_GSC_OPTIONS"> -Xms512m -Xmx512m -Dcom.gs.zones=Small</environment>
    </script>
    <vm enable="true" work-dir="${com.gs.home}/bin" main-class="com.gigaspaces.start.SystemBoot">
        <input-argument></input-argument>
        <argument>com.gigaspaces.start.services="GSC"</argument>
    </vm>
    <restart-regex>.*java\.lang\.OutOfMemoryError.*</restart-regex>
</process>
```
{{% /tab %}}
{{%tab "  gsc_large.xml "%}}

```xml
<process initial-instances="script" shutdown-class="com.gigaspaces.grid.gsa.GigaSpacesShutdownProcessHandler" restart-on-exit="always">
    <script enable="true" work-dir="${com.gs.home}/bin"
            windows="${com.gs.home}/bin/gs.bat" unix="${com.gs.home}/bin/gs.sh">
        <argument>start</argument>
        <argument>"GSC"</argument>
		<environment name="XAP_GSC_OPTIONS">-Xms1024m -Xmx1024m -Dcom.gs.zones=Large</environment>
    </script>
    <vm enable="true" work-dir="${com.gs.home}/bin" main-class="com.gigaspaces.start.SystemBoot">
        <input-argument></input-argument>
        <argument>com.gigaspaces.start.services="GSC"</argument>
    </vm>
    <restart-regex>.*java\.lang\.OutOfMemoryError.*</restart-regex>
</process>
```
{{% /tab %}}
{{% /tabs %}}

Now, to start the agent, we'll use the following command:


```xml
gs-agent gsa.gsc 0 gsa.gsc_small 2 gsa.gsc_large 1
```

{{%info%}}
Note that we're setting `gsa.gsc 0` to avoid loading the default `gsc` component.
{{%/info%}}


