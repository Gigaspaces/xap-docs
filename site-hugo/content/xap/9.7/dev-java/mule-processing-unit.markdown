---
type: post97
title:  Processing Unit
categories: XAP97
parent: mule-esb.html
weight: 200
canonical: auto
---


{{% ssummary  %}} {{% /ssummary %}}



XAP allows to configure, package and deploy a [Processing Unit](./packaging-and-deployment.html). The OpenSpaces Mule integration allows you to run a pure Mule application (with or without OpenSpaces special extension points and transports) as a Processing Unit.

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
When deploying a Processing Unit into the [SLA-driven container](./deploying-onto-the-service-grid.html), Mule JAR files should be "installed" into the GigaSpaces installation (on each node). The following minimum set of JARs need to be copied into `<GigaSpaces Root>\lib\platform\mule` (if the Mule directory does not exists, create it).
{{%/note%}}

- Copy the following JAR files from `<Mule Root>\lib\mule` to `<GigaSpaces Root>\lib\platform\mule`:
`mule-core`, `mule-module-spring-config`, `mule-module-spring-extras`, `mule-transport-quartz`, `mule-transport-stdio`, `mule-transport-vm` . Other transports (if used) should be copied as well.

- Copy the following JAR files from `<Mule Root>\lib\opt` to `<GigaSpaces Root>\lib\platform\mule`:
`commons-beanutils`, `commons-io`, `commons-lang`, `jug-2.0.0-asl`, `quartz-all-1.6.6`. Other required JAR files can be copied as well (or they can be put in the Processing Unit `lib` directory).

Note, since version 7.0, the required mule jar files can also be placed within the processing unit `lib` directory, without "installing" mule into each GSC node installation.

#### Packaging for Mule example

In the [Mule ESB example](/sbp/mule-esb-example.html), the following jars are required and should be placed under <GigaSpaces Root>\lib\platform\mule


```bash
commons-beanutils-1.8.0.jar
commons-cli-1.2.jar
commons-io-1.4.jar
commons-lang-2.4.jar
jgrapht-jdk1.5-0.7.3.jar
jug-2.0.0-asl.jar
mule-core-3.3.0.jar
mule-core-3.3.0-tests.jar
mule-module-annotations-3.3.0.jar
mule-module-client-3.3.0.jar
mule-module-jbossts-3.3.0.jar
mule-module-spring-config-3.3.0.jar
mule-module-spring-extras-3.3.0.jar
mule-tests-functional-3.3.0.jar
mule-transport-http-3.3.0.jar
mule-transport-quartz-3.3.0.jar
mule-transport-stdio-3.3.0.jar
mule-transport-vm-3.3.0.jar
mvel2-2.0.19.jar
quartz-all-1.6.6.jar
slf4j-api-1.6.1.jar
```

# Running/Deploying

Once the Processing Unit is packaged, it can be run using the different options that come with OpenSpaces:

- [Standalone](./running-in-standalone-mode.html)
- [SLA driven container](./deploying-onto-the-service-grid.html)
