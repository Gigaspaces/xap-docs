---
type: post101
title:  Elastic Deployment with Command Line
categories: XAP101ADM
parent: administration-tools.html
weight: 250
---

{{%ssummary%}} {{%/ssummary%}}

# application

### Syntax


```bash
gs> deploy-application [-user xxx -password yyy] [-secured true/false] application_directory_or_zipfile
```

### Description

Deploys an [application]({{%currentjavaurl%}}/deploying-onto-the-service-grid.html#Application Deployment and Processing Unit Dependencies), which deploys one or more processing units in dependency order onto the service grid.

{{% note %}}
Deploying an application that is a mixture of elastic and non-elastic spaces/processing units may end with the non-elastic spaces/processing units deployed on GSCs which are shared with elastic spaces/processing units that the ESM has started.

To avoid such behavior, you should start your GSCs with zones and specifies these zones in the processing unit properties.
{{% /note %}}

{{%accordion%}}
{{%accord title="**<u>Example</u>**"%}}


The following deploys the data-app example application (which includes a feeder and a processor).

    gs> deploy-application examples/data/dist.zip

The dist.zip file includes:

    application.xml
    feeder.jar
    processor.jar

application.xml file describes the application dependencies:


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:os-admin="http://www.openspaces.org/schema/admin"
spring
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
spring
	http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
	http://www.openspaces.org/schema/admin http://www.openspaces.org/schema/{{% currentversion %}}/admin/openspaces-admin.xsd">

	<context:annotation-config />

	<os-admin:application name="data-app">

        <os-admin:elastic-stateful-pu
                secured="false"
                memory-capacity-per-container-in-mb="32"
                number-of-partitions="1"
                highly-available="false"
                file="processor.jar">
            <os-admin:depends-on name="elasticSpace"/>
        </os-admin:elastic-stateful-pu>

        <os-admin:elastic-space name="elasticSpace" max-memory-capacity-in-mb="32" memory-capacity-per-container-in-mb="32" highly-available="false">
            <os-admin:depends-on name="nonElasticSpace"/>
                </os-admin:elastic-space>

        <os-admin:space name="nonElasticSpace" cluster-schema="partitioned-sync2backup" number-of-instances="1" number-of-backups="0" zones="nonElasticSpaceZone">
        </os-admin:space>

        <os-admin:pu processing-unit="feeder.jar" zones="nonElasticPUZone">
            <os-admin:depends-on name="processor" min-instances-per-partition="1"/>
        </os-admin:pu>

	</os-admin:application>
</beans>
```

A [dedicated machine provisioning]({{%currentjavaurl%}}/elastic-processing-unit-provisioning.html) config can be provided to elastic space/pu element:


```xml
<os-admin:discovered-machine-provisioning-config id="myConfig" grid-service-agents-zones="zone1,zone2" reserved-memory-capacity-per-machine-in-mb="1024"/>

<os-admin:elastic-space name="elasticSpace" max-memory-capacity-in-mb="32" memory-capacity-per-container-in-mb="32" highly-available="false">
    <os-admin:dedicated-machine-provisioning elastic-machine-provisioning-config="myConfig"/>
</os-admin:elastic-space>
```

You can also use the [shared machine provisioning]({{%currentjavaurl%}}/elastic-processing-unit-provisioning.html#shared-machine-provisioning) config that allows two processing units to share the same machine:


```xml
<os-admin:discovered-machine-provisioning-config id="myConfig" grid-service-agents-zones="zone1,zone2" reserved-memory-capacity-per-machine-in-mb="1024"/>

<os-admin:elastic-space name="firstElasticSpace" max-memory-capacity-in-mb="32" memory-capacity-per-container-in-mb="32" highly-available="false">
    <os-admin:shared-machine-provisioning sharing-id="sId" elastic-machine-provisioning-config="myConfig"/>
</os-admin:elastic-space>

<os-admin:elastic-space name="secondElasticSpace" max-memory-capacity-in-mb="32" memory-capacity-per-container-in-mb="32" highly-available="false">
    <os-admin:shared-machine-provisioning sharing-id="sId" elastic-machine-provisioning-config="myConfig"/>
</os-admin:elastic-space>
```

{{%/accord%}}
{{%/accordion%}}

{{%accordion%}}
{{%accord title="**<u>Options</u>**"%}}


|Option|Description|Value Format|
|:-----|:----------|:-----------|
| `-timeout` | Allows you to specify a timeout value (in milliseconds) when looking up the GSM to deploy to.{{% wbr %}}Defaults to `5000` milliseconds (5 seconds).| `-timeout [timeoutValue]`|
| `-deploy-timeout` | Timeout for deploy operation (in milliseconds),{{% wbr %}}otherwise blocks until all successful/failed deployment events arrive (default)" |`-deploy-timeout [timeoutValue]`|
| `-h` / `-help`  | Prints help | |
| `-secured` | Deploys a secured processing unit (implicit when using -user/-password) - [(CLI) Security]({{%currentsecurl%}}/command-line-interface-(cli)-security.html)| `-secured [true/false]`|
| `-user` `-password` | Deploys a secured processing unit propagated with the supplied user and password - [(CLI) Security]({{%currentsecurl%}}/command-line-interface-(cli)-security.html)| `-user xxx -password yyyy`|
{{%/accord%}}
{{%/accordion%}}



# undeploy application

### Syntax


```bash
gs> undeploy-application application_name
```

### Description

Undeploys an [application]({{%currentjavaurl%}}/deploying-onto-the-service-grid.html#Application Deployment and Processing Unit Dependencies) from the service grid, while respecting pu dependency order.


{{%accordion%}}
{{%accord title="**<u>Example</u>**"%}}

The following undeploys the data-app example application (which includes a feeder and a processor).

    gs> undeploy-application data-app
{{%/accord%}}
{{%/accordion%}}


{{%accordion%}}
{{%accord title="**<u>Options</u>**"%}}


|Option|Description|Value Format|
|:-----|:----------|:-----------|
| `-timeout` | Allows you to specify a timeout value (in milliseconds) when looking up the GSM to deploy to.{{% wbr %}}Defaults to `5000` milliseconds (5 seconds).| `-timeout [timeoutValue]`|
| `-undeploy-timeout` | Timeout for deploy operation (in milliseconds), otherwise blocks until all successful/failed deployment events arrive (default)" |`-undeploy-timeout [timeoutValue]`|
| `-h` / `-help`  | Prints help | |
| `-secured` | Deploys a secured processing unit (implicit when using -user/-password) - [(CLI) Security]({{%currentsecurl%}}/command-line-interface-(cli)-security.html)| `-secured [true/false]`|
| `-user` `-password` | Deploys a secured processing unit propagated with the supplied user and password - [(CLI) Security]({{%currentsecurl%}}/command-line-interface-(cli)-security.html)| `-user xxx -password yyyy`|
{{%/accord%}}
{{%/accordion%}}



# deploy elastic space

### Syntax

```bash
gs> deploy-elastic-space [options] [space name]
```

### Description

An Elastic Space only Processing Unit can be easily deployed onto the Service Grid.

{{% note %}}
Deploying an elastic space requires at least one ESM to be running.
{{% /note %}}

{{% note %}}
The options' order is important as some overrides others
{{% /note %}}

{{%accordion%}}
{{%accord title="**<u>Example</u>**"%}}

The following deploys an elastic space named mySpace with memory-capacity-per-container=32m and number-of-partitions=8.

    gs> deploy-elastic-space -memory-capacity-per-container 32m -number-of-partitions 8 mySpace

The following deploys an elastic space named mySpace with manual scale strategy and memory-capacity=128m.

    gs> deploy-elastic-space -memory-capacity-per-container 32m -max-memory-capacity 256m -scale strategy=manual memory-capacity=128m mySpace

The following deploys a secured elastic space called mySpace with max-memory-capacity equals to 256m.

    gs> deploy-elastic-space -memory-capacity-per-container 32m -max-memory-capacity 256m -secured true -user myusername -password mypassword mySpace

The following specifies command line arguments.

    gs> deploy-elastic-space -cmdargs "-Xms2g,-Xmx10g" -max-memory-capacity 20g mySpace

{{%/accord%}}
{{%/accordion%}}

{{%accordion%}}
{{%accord title="**<u>Options</u>**"%}}


|Option|Description|Value Format|
|:-----|:----------|:-----------|
| Space Name {{% wbr %}} **mandatory** | The name of the space to be deployed.| |
| `-mcpc`, `-memory-capacity-per-container` {{% wbr %}}**mandatory** | Specifies the the heap size per GSC. | `-mcpc [number[m/g]]` |
| `-mmc`, `-max-memory-capacity` {{% wbr %}}**mandatory**(*) | Specifies an estimate of the maximum memory capacity for this processing unit.{{% wbr %}}(*)Either `-max-memory-capacity` or `-number-of-partitions` option must be provided. | `-mcc [number[m/g]]` |
| `-nop`, `-number-of-partitions` {{% wbr %}}**mandatory**(*) | Defines the number of processing unit partitions.{{% wbr %}}(*)Either `-max-memory-capacity` or `-number-of-partitions` option must be provided. | `-nop [number]` |
| `-nobpp`, `-number-of-backups-per-partition` | Specifies the number of backup processing unit instances per partition.{{% wbr %}}This is an advanced property. | `-nobpp [number]` |
| `-mnocc`, `-max-number-of-cpu-cores` | Specifies an estimate for the maximum total number of cpu cores used by this processing unit. | `-mnooc [number]` |
| `-smd`, `-single-machine-deployment` | Allows deployment of the processing unit on a single machine.{{% wbr %}}Defaults to `false`. | `-smd [true/false]` |
| `-ha`, `-highly-available` | Specifies if each space partition has a backup instance.{{%wbr%}}True by default. | `-ha [true/false]` |
| `-secured` | Deploys a secured processing unit (implicit when using -user/-password).{{% wbr %}}Defaults to `false`. | `-secured [true/false]` |
| `-user` `-password` | Deploys a secured processing unit propagated with the supplied user and password - [(CLI) Security]({{%currentsecurl%}}/command-line-interface-(cli)-security.html)| `-user xxx -password yyyy`|
| `-dmp`, `-dedicated-machine-provisioning` | Configure the server side bean that starts and stops machines automatically. | `-dmp [dedicated machine provisioning properties]` {{% wbr %}} [dedicated machine provisioning properties](#dedicated-machine-provisioning-properties) |
| `-smp`, `-shared-machine-provisioning` | Configure the server side bean that starts and stops machines automatically.{{% wbr %}}The machines returned by the machine provisioner will be shared by other processing unit instances with the same sharingId. | `-smd [shared machine provisioning properties]` {{% wbr %}} [shared machine provisioning properties](#shared-machine-provisioning-properties) |
| `-scale` | Enables the specified scale strategy, and disables all other scale strategies.{{% wbr %}}Defaults to `eager` scale strategy. | `-scale [scale properties]` {{% wbr %}} [scale properties](#scale-properties) |
| `-timeout` | Timeout for deploy operation.{{% wbr %}}Defaults to `120` seconds. | `-timeout [timeout in seconds]` |
| `-uof`, `-undeploy-on-failure` | Undeploy the processing unit if the deploy process is not completed within the timeout frame.{{% wbr %}}Defaults to `false`. | `-uof [true/false]` |
| `-cmdargs`, `-command-line-args` | Adds the arguments as JVM level arguments when the process is executed using pure JVM. {{%wbr%}}Note the quotes in the value. | `-cmdargs ["comma separated list of args"]` |
| `-ctxp`, `-context-properties` | Defines a context deploy time property overriding any ${...} | `-ctxp key1=value1 key2=value2` |
{{%/accord%}}
{{%/accordion%}}


# deploy elastic pu

### Syntax

```bash
gs> deploy-elastic-pu [options] [-puname ...] [-file ...] [space name]
```

### Description

An Elastic PU only Processing Unit can be easily deployed onto the Service Grid.

{{% note %}}
Deploying an elastic pu requires at least one ESM to be running.
{{% /note %}}

{{% note %}}
The options' order is important as some overrides others
{{% /note %}}

{{%accordion%}}
{{%accord title="**<u>Example</u>**"%}}

The following deploys an elastic stateless pu from file.

    gs> deploy-elastic-pu -type stateless -memory-capacity-per-container 32m -file /home/user/feeder.jar

The following deploys an elastic stateful pu from file with manual scale strategy and memory-capacity=128m.

    gs> deploy-elastic-pu -type stateful -memory-capacity-per-container 32m -max-memory-capacity 256m -scale strategy=manual memory-capacity=128m -file /home/user/processor.jar

The following deploys a secured stateful pu with -puname option.

    gs> deploy-elastic-pu -type stateful -memory-capacity-per-container 32m -number-of-partitions 8 -puname feeder

{{%/accord%}}
{{%/accordion%}}


{{%accordion%}}
{{%accord title="**<u>Options</u>**"%}}


|Option|Description|Value Format|
|:-----|:----------|:-----------|
| `-type` {{% wbr %}}**mandatory** | Specifies the processing unit type.{{% wbr %}}Options are: `stateful`, `stateless`. | `-type [stateful/stateless]` |
| `-file` {{% wbr %}}**mandatory**(*)  | Processing unit file path (processing unit jar/zip file or a directory).{{% wbr %}}(*)Either `-file` or `-puname` option must be provided. | `-file /home/user/myprocessingunit.jar` |
| `-puname` {{% wbr %}}**mandatory**(*) | Processing unit name (should exists under the [GS ROOT]/deploy directory).{{% wbr %}}(*)Either`-file` or `-puname` option must be provided. | `-puname processor` |
| `-name` | Overrides the Processing Unit's name | `-name myProcessingUnitName` |
| `-mcpc`, `-memory-capacity-per-container` {{% wbr %}}**mandatory** | Specifies the the heap size per GSC. | `-mcpc [number[m/g]]` |
| `-secured` | Deploys a secured processing unit (implicit when using -user/-password).{{% wbr %}}Defaults to `false`. | `-secured [true/false]` |
| `-user` `-password` | Deploys a secured processing unit propagated with the supplied user and password - [(CLI) Security]({{%currentsecurl%}}/command-line-interface-(cli)-security.html)| `-user xxx -password yyyy`|
| `-dmp`, `-dedicated-machine-provisioning` | Configure the server side bean that starts and stops machines automatically. | `-dmp [dedicated machine provisioning properties]` {{% wbr %}} [dedicated machine provisioning properties](#dedicated-machine-provisioning-properties) |
| `-smp`, `-shared-machine-provisioning` | Configure the server side bean that starts and stops machines automatically.{{% wbr %}}The machines returned by the machine provisioner will be shared by other processing unit instances with the same sharingId. | `-smd [shared machine provisioning properties]` {{% wbr %}} [shared machine provisioning properties](#shared-machine-provisioning-properties) |
| `-scale` | Enables the specified scale strategy, and disables all other scale strategies.{{% wbr %}}Defaults to `eager` scale strategy. | `-scale [scale properties]` {{% wbr %}} [scale properties](#scale-properties) |
| `-timeout` | Timeout for deploy operation.{{% wbr %}}Defaults to `120` seconds. | `-timeout [timeout in seconds]` |
| `-uof`, `-undeploy-on-failure` | Undeploy the processing unit if the deploy process is not completed within the timeout frame.{{% wbr %}}Defaults to `false`. | `-uof [true/false]` |
| `-cmdargs`, `-command-line-args` | Adds the arguments as JVM level arguments when the process is executed using pure JVM. {{%wbr%}}Note the quotes in the value. | `-cmdargs ["comma separated list of args"]` |
| `-ctxp`, `-context-properties` | Defines a context deploy time property overriding any ${...} | `-ctxp key1=value1 key2=value2` |
|:-----|:----------|:-----------|

The following options are supported with a `stateful` elastic PU only


|Option|Description|Value Format|
|:-----|:----------|:-----------|
| `-mmc`, `-max-memory-capacity` {{% wbr %}}**mandatory**(*) | Specifies an estimate of the maximum memory capacity for this processing unit.{{% wbr %}}(*)Either `-max-memory-capacity` or `-number-of-partitions` option must be provided. | `-mcc [number[m/g]]` |
| `-nop`, `-number-of-partitions` {{% wbr %}}**mandatory**(*) | Defines the number of processing unit partitions.{{% wbr %}}(*)Either `-max-memory-capacity` or `-number-of-partitions` option must be provided. | `-nop [number]` |
| `-nobpp`, `-number-of-backups-per-partition` | Specifies the number of backup processing unit instances per partition.{{% wbr %}}This is an advanced property, default to 1 | `-nobpp [number]` |
| `-mnocc`, `-max-number-of-cpu-cores` | Specifies an estimate for the maximum total number of cpu cores used by this processing unit. | `-mnooc [number]` |
| `-smd`, `-single-machine-deployment` | Allows deployment of the processing unit on a single machine.{{% wbr %}}Defaults to `false`. | `-smd [true/false]` |
| `-ha`, `-highly-available` | Specifies if each space partition has a backup instance.{{%wbr%}}True by default. | `-ha [true/false]` |
{{%/accord%}}
{{%/accordion%}}


# dedicated machine provisioning properties

### Description

The following provisioning properties may be used with the `-dedicated-machine-provisioning [provisioning properties]` option in `deploy-elastic-space` and `deploy-elastic-pu` commands.


{{%accordion%}}
{{%accord title="**<u>Example</u>**"%}}

The following deploys an elastic space named mySpace with zones [zone1,zone2] while taking into consideration a reserved 1536m memory per machine.

    gs> deploy-elastic-space -dedicated-machine-provisioning grid-service-agents-zones=zone1,zone2 reserved-memory-capacity-per-machine=1536m mySpace

{{%/accord%}}
{{%/accordion%}}

{{%accordion%}}
{{%accord title="**<u>Options</u>**"%}}


|Syntax|Description|
|:-----|:----------|
| `gsaz, grid-service-agents-zones=zone1,zone2` | Specifies the processing unit name. |
| `rmcpm, reserved-memory-capacity-per-machine=1g` | Sets the expected amount of memory per machine that is reserved for processes other than grid containers. |
| `rmcpmm, reserved-memory-capacity-per-management-machine=1g` | Sets the expected amount of memory per management machine that is reserved for processes other than grid containers. |
{{%/accord%}}
{{%/accordion%}}

# shared machine provisioning properties

### Description

The following provisioning properties may be used with the `-shared-machine-provisioning [provisioning properties]` option in `deploy-elastic-space` and `deploy-elastic-pu` commands.


{{%accordion%}}
{{%accord title="**<u>Example</u>**"%}}

The following deploys an elastic space named mySpace with zones [zone1,zone2] while taking into consideration a reserved 1536m memory per machine.

    gs> deploy-elastic-space -shared-machine-provisioning sharing-id=myid grid-service-agents-zones=zone1,zone2 reserved-memory-capacity-per-machine=1536m mySpace

{{%/accord%}}
{{%/accordion%}}

{{%accordion%}}
{{%accord title="**<u>Options</u>**"%}}


|Syntax|Description|
|:-----|:----------|
| `sid, sharing-id=value` | Specifies the processing unit name. |
| `gsaz, grid-service-agents-zones=zone1,zone2` | Specifies the processing unit name. |
| `rmcpm, reserved-memory-capacity-per-machine=1g` | Sets the expected amount of memory per machine that is reserved for processes other than grid containers. |
| `rmcpmm, reserved-memory-capacity-per-management-machine=1g` | Sets the expected amount of memory per management machine that is reserved for processes other than grid containers. |
{{%/accord%}}
{{%/accordion%}}


# scale properties

### Description

The following scale properties may be used with the `-scale [scale properties]` option in `deploy-elastic-space` and `deploy-elastic-pu` commands.

{{%accordion%}}
{{%accord title="**<u>Example</u>**"%}}

The following deploys an elastic stateful pu from file with `manual` scale strategy and `memory-capacity=128m`.

    gs> deploy-elastic-pu -type stateless -scale strategy=manual memory-capacity=128m -file /home/user/processor.jar

The following deploys an elastic space named `mySpace` with `manual` scale strategy and `memory-capacity=128m`.

    gs> deploy-elastic-space -scale strategy=manual memory-capacity=128m mySpace

{{%/accord%}}
{{%/accordion%}}


{{%accordion%}}
{{%accord title="**<u>Options</u>**"%}}


|Syntax|Description|
|:-----|:----------|
| `strategy=[eager/manual]`{{% wbr %}}**mandatory** | Specifies the processing unit name. |
| `max-concurrent-relocations-per-machine=[number]` | Specifies the number of processing unit instance relocations each machine can handle concurrently. {{% wbr %}}CLI's default is 1. |
| `at-most-one-concurrent-relocation=[true/false]` | Limits the number of concurrent relocations for the entire cluster to 1. {{% wbr%}}* Notice this is an aggregated property across all machines. {{%wbr%}}CLI's default is true. |


The following options are supported with `manual` strategy only

|         |                  |
|----------------------------------|-----------------------|
| `number-of-cpu-cores=[number]` | Specifies the number of CPU cores (as reported by the operating system). {{% wbr %}}This includes both real cores and hyper-threaded cores. |
| `memory-capacity=[number[m/g]]` | Specifies the memory capacity (RAM). |
{{%/accord%}}
{{%/accordion%}}



# scale elastic processing unit

### Syntax

```bash
gs> scale [options] -name [processing unit name]
```

### Description

Easily scale an already deployed elastic processing unit.

{{%accordion%}}
{{%accord title="**<u>Example</u>**"%}}

    gs> scale -name myspace -number-of-cpu-cores 2

    gs> scale -name myspace -memory-capacity 256m

{{%/accord%}}
{{%/accordion%}}

{{%accordion%}}
{{%accord title="**<u>Options</u>**"%}}


|Option|Description|Value Format|
|:-----|:----------|:-----------|
| `-name` {{% wbr %}}**mandatory** | Specifies the processing unit name. | `-name [processing unit name]` |
| `-nocc`, `-number-of-cpu-cores` | Specifies the number of CPU cores (as reported by the operating system). | `-nocc [number]` |
| `-mc`, `-memory-capacity` | Specifies the memory capacity (RAM). | `-mc [number[m/g]]` |
| `-mcrpm`, `-max-concurrent-relocations-per-machine` | Specifies the number of processing unit instance relocations each machine can handle concurrently | `-mcrpm [number]` |
{{%/accord%}}
{{%/accordion%}}


# undeploy PU

### Syntax


```bash
gs> undeploy-pu pu_name
```

### Description

Undeploys a processing unit from the service grid, while respecting pu dependency order.

{{%accordion%}}
{{%accord title="**<u>Example</u>**"%}}

The following undeploys the mySpace processing unit.

    gs> undeploy mySpace

{{%/accord%}}
{{%/accordion%}}

{{%accordion%}}
{{%accord title="**<u>Options</u>**"%}}


|Option|Description|Value Format|
|:-----|:----------|:-----------|
| `-h` / `-help`  | Prints help | |

{{%/accord%}}
{{%/accordion%}}
