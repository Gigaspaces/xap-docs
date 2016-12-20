---
type: post
title:  Universal Deployer
categories: SBP
parent: production.html
weight: 1500
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim| 8.0.1 | April 2011|    |    |


# Overview
The GigaSpaces Universal Deployer (GUD) allows deploying the different supported processing units ([Space PU]({{%latestjavaurl%}}/the-processing-unit-overview.html), [Regular PU]({{%latestjavaurl%}}/the-processing-unit-structure-and-configuration.html), [Elastic PU]({{%latestjavaurl%}}/elastic-processing-unit.html), [memCache PU]({{%latestjavaurl%}}/memcached-api.html) or [Web PU]({{%latestjavaurl%}}/web-processing-unit-container.html) via a simple configuration file. The GUD support dependency based deployment allowing multiple processing units to be deployed as one atomic process (e.g. composite application).

{{% tip %}}
Starting with XAP 9 you may use the **Deployment Dependencies API** to specify the deployment order. See the [Application Deployment and Processing Unit Dependencies]({{%latestjavaurl%}}/deploying-onto-the-service-grid.html#Application+Deployment+and+Processing+Unit+Dependencies) for details.
{{% /tip %}}

## Dependency Based Deployment
The GUD allows you to specify for each deployed PU its dependency PU list. The dependent PU will be deployed after all its PUs dependencies will be fully deployed. The mechanism is recursive; if any of these PUs depends on other PU(s), these will be deployed before the top level dependent PU is deployed. This ensures the deploy order will have the entire PUs deployed in the correct order.

{{% align center %}}
![universantDeploy_depen.jpg](/attachment_files/sbp/universantDeploy_depen.jpg)
{{% /align %}}

To describe the above deploy plan the GUD should have the following config file:


```java
A [B,C] -file myA_PU.jar
B [D,E] -file myB_PU.jar
C [F,G] -file myC_PU.jar
D [] -file myD_PU.jar
E [] -file myE_PU.jar
F [] -file myF_PU.jar
G [] -file myG_PU.jar
```

With the above config the PUs will be deployed in the following order: F, D, E, G, C, B, A.

## Abort Strategy
In case of a deployment failure, you may specify an abort strategy. In case of unsuccessful deployment you may instruct the GUD to undeploy the specific PU that could not be deployed or undeploy the entire PUs that already been deployed.

# GUD Command Line Options
The GUD support the following command line arguments:


|Switch|Description|Options|Default|
|------|-----------|-------|-------|
|-command|deploy or undeploy PU listed at the config file location| deploy , undeploy| deploy|
|-config|GUD config file location| | |
|-locators|lookup service locators| | |
|-groups|lookup service groups| | |
|-abortDeployOnFailure|In case of a failure with the deploy of any of the PUs, undeploy all the PUs listed at the GUD config file| true,false| false|

# Running the GUD

## Install the GUD

- Download the [GigaSpaces Universal Deployer](/attachment_files/sbp/GSUniversalDeployer.zip) and extract it into an empty folder.
- Edit the `runGSUniversalDeployer.bat` or `runGSUniversalDeployer.sh` to include the correct parameters.

Here an example how you should run the GUD:


```java
java com.gigaspaces.admin.GSUniversalDeployer -config c:\\puList.txt -locators 127.0.0.1 -abortDeployOnFailure true
```

## GUD Configuration File
The universal Deployer configuration file may include multiple lines with the following format:


```java
PU Name [Dependent PU List] deploy options
```

Example:


```java
mySpace [] -type space -cluster schema=partitioned-sync2backup total_members=2,1
myPU [] -type regular -file c:\myPU.zip
A [B,C] -elastic manualCapacity  -user xxx -password yyyy -undeployOnFailure true -file mywar.war
B [D,E] -cluster schema=partitioned total_members=2,1 -user xxx -password yyyy  -file myjar.jar
C [F,E] -elastic eagerCapacity -user xxx -password yyyy -properties embed://prop2=value2;prop3=value3 -file myzip.zip
D [] -cluster total_members=2 -user xxx -password yyyy -properties file://d:/temp/context.properties
E [] -cluster total_members=2 -user xxx -password yyyy -zones zone1,zone2
```

## GUD Configuration File Options
Each GUD configuration file line supports the following options:


|Switch|Description|Options|Default|
|:-----|:----------|:------|:------|
|-file| PU file. You may have .zip , .jar , .war file deployed.| | |
|-type|PU Type|Regular,Space,memcache|Regular|
|-timeout| Timeout to abort the deploy process. If the deploy process is not completed within this timeframe, undeploy process will be initiated. | | 120 seconds|
|-undeployOnFailure | undeploy the PU in case of a failure or when the deploy could not be completed within the timeout specified|false , true|  false|
|-spaceurl|Used with memcache PU type.|The url of the space, can be embedded, e.g.: /./myMemcached, or remote e.g.: `jini://*/*/myMemcached`|`/./memcached`|
|-sla |Location of an optional xml file holding the SLA element|sla file location| |
|-cluster |Set cluster parameters|schema=partitioned-sync2backup  : The cluster schema{{<wbr>}}total_members=1,1               : The number of instances and number of backups | |
|-user xxx -password yyyy               |Deploys a secured processing unit propagated with the supplied user and password| | |
|-secured |Deploys a secured processing unit (implicit when using -user/-password)| false,true|false|
|-properties | Location of context level properties file or list of properties. Examples:{{<wbr>}}`-properties embed://prop2=value2;prop3=value3` or `-properties file://d:/context.properties`| | |
|-max-instances-per-vm number           | Set the SLA number of instances per VM|Numeric value | |
|-max-instances-per-machine number      | Set the SLA number of instances per machine|Numeric value | |
|-max-instances-per-zone |Set the SLA number of instances per zone |zone/number| |
|-zones | Set the SLA zone requirements | | |
|-elastic | Deploy the PU as an [Elastic PU]({{%latestjavaurl%}}/elastic-processing-unit.html). You should have the ESM started when using this option.| manualCapacity,eagerCapacity | |
|-highlyAvailable | Used with a Stale-full Elastic PU. Start backup instances. Specifies if the space should duplicate each information on two different machines.|true,false|true|
|-memoryCapacityPerContainer |Used with an Elastic PU. Specifies the the heap size per container (operating system process). |Numeric value with a Memory Unit. Memory units supported: m for MB,g for GB|32m|
|-maxMemoryCapacity |Used with an Elastic PU. Specifies the estimated **maximum** total memory capacity used with this processing unit. |Numeric value with a Memory Unit. Memory units supported: m for MB,g for GB|256m|
|-memoryCapacity |Used with an Elastic PU. Specifies the estimated **initial** total memory capacity used with this processing unit. | Numeric value with a Memory Unit. Memory units supported: m for MB,g for GB|128m|
|-singleMachineDeployment |Used with an Elastic PU. Allows deployment of the processing unit on a single machine, by lifting the limitation for primary and backup processing unit instances from the same partition to be deployed on different machines. | false,true|false|

## Commenting lines
You may use `/` or `#` to comment out a line within the configuration file.

## Logging
When deploying the GUD will display information about started/terminated containers, started/terminated PU instances.

