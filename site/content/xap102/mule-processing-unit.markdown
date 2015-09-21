---
type: post102
title:  Processing Unit
categories: XAP102
parent: mule-esb.html
weight: 200
---


{{% ssummary  %}} {{% /ssummary %}}



XAP allows to configure, package and deploy a [Processing Unit](./the-processing-unit-overview.html). The OpenSpaces Mule integration allows you to run a pure Mule application (with or without OpenSpaces special extension points and transports) as a Processing Unit.

# Configuration

In order to run Mule as a Processing Unit within the `pu.xml` file (which is a Spring application context), the following needs to be defined:


```xml
<bean class="org.openspaces.esb.mule.pu.OpenSpacesMuleContextLoader">
    <property name="location" value="/META-INF/spring/mule.xml"/>
</bean>
```

In the example above, the `OpenSpacesMuleContextLoader` loads a Mule application as defined in the `mule.xml` configuration file. The `location` parameter is optional and defaults to the value specified in the example above.

# Packaging

Packaging of the Processing Unit should follow the [Processing Unit structure](./the-processing-unit-structure-and-configuration.html).

{{% note %}}
When deploying a Processing Unit into the [SLA-driven container](./deploying-onto-the-service-grid.html), Mule JAR files should be "installed" into the XAP installation (on each node). The following minimum set of JARs need to be copied into **`<XAP Root>\lib\platform\mule`** (if the Mule directory does not exists, create it).
{{%/note%}}

- Download Mule 3.5 distribution from [mule's website](https://www.mulesoft.org/download-mule-esb-community-edition) and extract the file.
 
- Copy the following JAR files from `<Mule Root>\lib\mule` to `<XAP Root>\lib\platform\mule`:
`mule-commons`, `mule-core`, `mule-core-tests`, `mule-module-annotations`, `mule-module-client`, `mule-module-jbossts`, `mule-module-spring-config`, `mule-module-spring-extras`,  `mule-mvel2-2.1.9-MULE-003`, `mule-transport-http`, `mule-transport-quartz`, `mule-transport-stdio`, `mule-transport-vm` . Other transports (if used) should be copied as well.

- Copy the following JAR files from `<Mule Root>\lib\opt` to `<XAP Root>\lib\platform\mule`:
`commons-beanutils`, `commons-io`, `commons-lang`, `dom4j`, `guava`, `jaxen`, `quartz`, `uuid`, `jgrapht-jdk1.5`. Other required JAR files can be copied as well (or they can be put in the Processing Unit `lib` directory).

- Copy the following JAR files from `<Mule Root>\lib\boot` to `<XAP Root>\lib\platform\mule`:
`commons-cli`, `log4j`, `mule-module-logging`, `wrapper`. Other required JAR files can be copied as well (or they can be put in the Processing Unit `lib` directory).

{{% note %}}
Since version 7.0, the required mule jar files can also be placed within the processing unit `lib` directory, without "installing" mule into each GSC node installation.
{{%/note%}}

{{% note %}}
Since version 10.1 XAP is using spring 4.1.1 jars and Mule does not support spring 4 it is required to replace **`<XAP Root>\lib\required\spring-*4.1.1*.jar`** and **`<XAP Root>\lib\optional\spring-*4.1.1*.jar`** files to spring 3.2.4 version jars.
{{%/note%}}

#### Packaging for Mule example

In the [Mule ESB example](/sbp/mule-esb-example.html), the following jars are required and should be placed under <XAP Root>\lib\platform\mule


```console
commons-beanutils-1.8.0.jar
commons-cli-1.2.jar
commons-io-1.4.jar
commons-lang-2.4.jar
dom4j-1.6.1.jar
guava-16.0.1.jar
jaxen-1.1.1.jar
jgrapht-jdk1.5-0.7.3.jar
log4j-1.2.16.jar
mule-common-3.5.0.jar
mule-core-3.5.0.jar
mule-core-3.5.0-tests.jar
mule-module-annotations-3.5.0.jar
mule-module-client-3.5.0.jar
mule-module-jbossts-3.5.0.jar
mule-module-logging-3.5.0.jar
mule-module-spring-config-3.5.0.jar
mule-module-spring-extras-3.5.0.jar
mule-mvel2-2.1.9-MULE-003.jar
mule-transport-http-3.5.0.jar
mule-transport-quartz-3.5.0.jar
mule-transport-stdio-3.5.0.jar
mule-transport-vm-3.5.0.jar
quartz-1.8.5.jar
uuid-3.4.0.jar
wrapper-3.2.3.jar
```

# Running/Deploying

Once the Processing Unit is packaged, it can be run using the different options that come with OpenSpaces:

- [Standalone](./running-in-standalone-mode.html)
- [SLA driven container](./deploying-onto-the-service-grid.html)
