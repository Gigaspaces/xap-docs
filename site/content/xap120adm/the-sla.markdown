---
type: post120
title:  Overview
categories: XAP120ADM, PRM
parent: the-sla-overview.html
weight: 100
---


{{% ssummary   %}} {{% /ssummary %}}



The GigaSpaces runtime environment (A.K.A the Service Grid) provides SLA-driven capabilities via the [GSM](/product_overview/service-grid.html#gsm) and the [GSC](/product_overview/service-grid.html#gsc) runtime components. The GSC is responsible for running one or more Processing Units; while the GSM is responsible for analyzing the deployment and provisioning the processing unit instances to the available GSCs.

{{% note "Enforcing SLA definitions "%}}
The SLA definition are only enforced when deploying the processing unit on to the GigaSpaces service grid, since this environment actively manages and controls the deployment using the GSM(s). When running within your IDE or in standalone mode these definitions are ignored.
{{% /note %}}

The SLA definitions can be provided as part of the processing unit package or during the processing unit's deployment process. They define the number of processing unit instances that should be running and deploy-time requirements such as the amount of free memory or CPU or the clustering topology for processing units which contain a space. The GSM reads the SLA definition, and deploys the processing unit onto the available GSCs according to it.

# Defining Your Processing Unit's SLA

The SLA contains the deployment criteria in terms of clustering topology (if it contains a space) and deployment-time requirements.

It can be defined in multiple ways:

- Include an `sla.xml` file which contains the definitions within the processing unit's jar file. This file can be located at the root of the processing unit jar or under the `META-INF/spring` directory, alongside the processing unit's XML file. This is the most recommended way to follow.

-  Embed the SLA definitions within the processing unit's `pu.xml` file.

-  Provide a separate XML files with the SLA definitions to the GSM at deployment via one of the [deployment tools]({{%currentjavaurl%}}/deploying-onto-the-service-grid.html).

- Use the deployment tools themselves to provide/override the processing unit's SLA (see below). For example, the GUI deployment dialogue enables you to type in various SLA definitions, such as the number of instances, number of backups and space topology.

The SLA definition, whether it comes in a separate file or embedded inside the `pu.xml` file, is composed of a single `<os-sla:sla>` XML element. A sample SLA definition is shown below (note that you can use plain Spring definitions or GigaSpaces specific namespace bindings):

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-sla="http://www.openspaces.org/schema/sla"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{%currentversion%}}/sla/openspaces-sla.xsd">

    <os-sla:sla cluster-schema="partitioned" number-of-instances="2" number-of-backups="1" max-instances-per-vm="1">
    ...
    </os-sla:sla>
</beans>
```

{{% /tab %}}
{{%tab "  Plain "%}}


```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd">

	<bean id="SLA" class="org.openspaces.pu.sla.SLA">
		<property name="clusterSchema" value="partitioned" />
		<property name="numberOfInstances" value="2" />
		<property name="numberOfBackups" value="1" />
		<property name="maxInstancesPerVM" value="1" />
	</bean>
	...
</beans>
```

{{% /tab %}}
{{% /tabs %}}

The SLA definition above creates 4 instances of a Processing Unit using the `partitioned` [space topology](/product_overview/space-topologies.html). It defines 2 partitions (`number-of-instances="2"`), each with one backup (`number-of-backups="1"`). In addition, it requires that a primary and a backup instance of the same partition will not be provisioned to the same GSC (`max-instances-per-vm="1"`).

{{% tip %}}
It is up to the deployer to configure the SLA correctly. Trying to deploy a Processing Unit with a cluster schema that requires backups without specifying `numberOfBackups` causes the deployment to fail.
{{%/tip%}}

{{% info %}}
In previous releases, the SLA definition also included dynamic runtime policies, e.g. creating additional processing unit instances based on CPU load, relocating a certain instance when the memory becomes saturated, etc. These capabilities are still supported, but are considered deprecated.  Version 7.0 introduces the [Administration and Monitoring API]({{%currentjavaurl%}}/administration-and-monitoring-api.html) which supports the above and and much more.
{{% /info %}}

# Defining the Space Cluster Topology

When a processing unit contains a space, the SLA specifies the clustering topology for that space. The XML element that defines this is the `cluster-schema`, and is an attribute of the `os-sla:sla` XML element, as shown above.

The reason for defining the space's clustering topology within the SLA definition rather than the space definition with the `pu.xml` file is twofold:

- The clustering topology directly affects the number of instances of the processing unit, and is therefore considered part of its SLA. For example, a partitioned space can have 2 primaries and 2 backups, and a replicated space can have 5 instances. This means that the containing processing unit will have the same number of instances.

- More importantly, separating the clustering topology from the actual space definition enables to truly implement the "write once, scale anywhere" principal. It means you can run the same processing unit within your IDE for unit tests using the default, single space instance clustering topology, and then deploy it to the runtime environment and run the same processing unit with the real clustering topology (e.g. partitioned with 4 partitions).

There are numerous clustering topologies you can choose from:

- `default`: Single space instance, no replication or partitioning

- `sync-replicated`: Multiple space instances. When written to one of the space instances, objects are **synchronously** replicated to all space instances. The maximum capacity of this topology is the one of the smallest JVM in the cluster.

- `async-replicated` Multiple space instances. When written to one of the space instances, objects are **asynchronously** replicated to all space instances. The maximum capacity of this topology is the one of the smallest JVM in the cluster.

- `partitioned` Multiple space instances. Objects are distributed across all of the space instances, such that each instance contains a separate subset of the data and forms a separate partition. The partitioning (distribution) of objects is based on their routing property. Optionally, when using this topology, you can designate one or more backup instances to each of the partitions, such that when an object is written to  a certain partition, it is synchronously replicated to the backup copy(ies) of that partition. The maximum capacity of this topology is the overall capacity of all of the JVMs in the cluster, divided by the number of backups+1.

From the client application's perspective (the one that connects to the space from another process), the clustering topology is transparent in most cases.

{{% refer %}}
Please refer to [this page](/product_overview/space-topologies.html) for more details about space clustering topologies and guidelines regarding when to use each of the topologies.
The number-of-backups parameter should be used with the partitioned cluster schema. It is not supported with the sync-replicated or async-replicated cluster schema.
{{% /refer %}}

# SLA Based Distribution and Provisioning

When deployed to the service grid, the processing unit instances are distributed based on the SLA definitions. These definitions form a set of constraints and requirements that should be met when a processing unit instance is provisioned on to a specific container (GSC). The SLA definitions considered during the initial deployment, relocation, and re-provisioning of an instance after failure.

# Default SLA Definitions

If no SLA definition is provided either within the Processing Unit XML configuration or during deploy time, a default SLA is used. The following is the default SLA definition:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<os-sla:sla number-of-instances="1" />
```

{{% /tab %}}
{{%tab "  Plain "%}}


```xml
<bean id="SLA" class="org.openspaces.pu.sla.SLA">
    <property name="numberOfInstances" value="1" />
</bean>
```

{{% /tab %}}
{{% /tabs %}}

# Max Instances per VM/Machine

The SLA definition allows you to define the maximum number of instances for a certain processing unit, either per JVM (GSC) or physical machine (regardless of the number of JVMs/GSCs that are running on it).

The max-instances parameter has different semantics when applied to processing units that contain a space with primary-backup semantics (i.e. that uses the  partitioned cluster schema and defines at least one backup) and when applied to a processing unit which contains no embedded space, or which contains a space with no primary-backup semantics.

{{% note %}}
When applied to a processing unit which contains no embedded space, or which contains a space with no primary-backup semantics, the max-instances parameter defines the **total** number of instances that can be deployed on a single JVM or on a single machine.
{{%/note%}}

When applied to a processing unit which contains a space with primary-backup semantics, the max-instances parameter defines the total number of instances which belong to the same primary-backup group (or partition) that can be provisioned to a single JVM or a single machine.

The most common usage of the max-instances feature is when using a processing unit which contains a space with primary-backup semantics. By setting its value to `1`, you ensure that a primary and its backup(s) cannot be provisioned to the same JVM (GSC) / physical machine.

{{% vbar "max-instances-per-vm"%}}
The `max-instances-per-vm` means for a PU with an embedded space the max amount of instances per partition. A partition may have primary or backup instance. The `max-instances-per-vm=1` means you won't have primary and a backup of the same partition provisioned into the same GSC. You may have multiple partitions with primary or backup instances provisioned into the same GSC. You can't limit the amount of instances from different partitions a GSC may host.

If you have enough GSCs (as the amount of partitions X 2) you will end up having a single instance per GSC.
If you won't have enough GSCs we will distribute the primary or backup instances of the different partitions across all the existing GSCs, and will do our best to distribute primary instances in even manner across all the GSCs on all the machines.
If you will increase the amount of GSCs after the initial deploy you will have to "re-balance" the system - meaning; distribute all the primaries across all the GSCs.

You may perform this activity via API or automatically by using the ESM. Rebalancing the instances will increase the capacity of the data-grid (more GSCs hosting the data-grid).
The rebalance concept is based on the assumption that you had initially more partitions that GSCs. We call the ratio between partitions to GSCs the scaling factor; meaning how much your data-grid can expand itself without any shutdown and without increasing the amount of partitions.

Example: you start with 4 GSCs and 20 partitions with backups (40 instances) which means each GSC will host initially 10 instances and may end up with 40 GSCs (after one or more re-balance operations) where each will host a single instance. With this example we have increased the capacity of the data grid to be 10 times larger without downtime while the amount of partitions remain the same.
See the [capacity planning](/sbp/capacity-planning.html) for more details.
{{% /vbar %}}

Here is an example of setting the `max-instances-per-vm` parameter:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<os-sla:sla max-instances-per-vm="1" />
```

{{% /tab %}}
{{%tab "  Plain "%}}


```xml
<bean id="SLA" class="org.openspaces.pu.sla.SLA">
    <property name="maxInstancesOfVM" value="1" />
</bean>
```

{{% /tab %}}
{{% /tabs %}}

Here is an example of setting the max instances per machine parameter:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<os-sla:sla max-instances-per-machine="1" />
```

{{% /tab %}}
{{%tab "  Plain "%}}


```xml
<bean id="SLA" class="org.openspaces.pu.sla.SLA">
    <property name="maxInstancesOfMachine" value="1" />
</bean>
```

{{% /tab %}}
{{% /tabs %}}



# Total Max Instances Per VM

This parameter controls the total amount of PU instances that can be instantiated within a GSC. This is very different than the `max-instances-per-vm` that controls how many instances a partition may have within a GSC. It does not control the total amount of instances from different PUs or other partitions that can be provisioned into a GSC. To control the Total Max Instances Per VM you should use the system property `com.gigaspaces.grid.gsc.serviceLimit` and set its value before running the GSC:


```java
set GSC_JAVA_OPTIONS=-Dcom.gigaspaces.grid.gsc.serviceLimit=2
```

The default value of the `com.gigaspaces.grid.gsc.serviceLimit` is **500**.




{{% anchor livenessDetection %}}

# Monitoring the Liveness of Processing Unit Instances

The [GSM](/product_overview/service-grid.html#gsm) monitors the liveness of all the processing unit instances it provisioned to the GSCs. The GSM pings each instance in the cluster to see whether it's available.

You can also control how often a processing unit instance will be monitored by the GSM, and in case of failure, how many times the GSM will retry to ping the instance and for how long it will wait between retry attempts.

This is done using the `<os-sla:member-alive-indicator>` element. It contains the following attributes:


| Attribute | Description | Default |
|:----------|:------------|:--------|
| invocation-delay | How often (in milliseconds) an instance will be monitored and verified to be alive by the GSM | 5000 (5 seconds) |
| retry-count | Once an instance has been determined to not be alive, how many times to check it before giving up on it | 3 |
| retry-timeout | Once an instance has been determined to not be alive, what is the timeout interval between retries (in milliseconds) | 500 (0.5 seconds) |

When a processing unit instance is determined as unavailable, the GSM which manages its deployment tries to re-provision the instance on another GSC (according to the SLA definitions)

{{%tabs%}}
{{%tab "  NameSpace "%}}


```xml
<os-sla:sla>
    <os-sla:member-alive-indicator invocation-delay="5000" retry-count="3" retry-timeout="500" />
</os-sla:sla>
```

{{% /tab %}}
{{%tab "  Plain "%}}


```xml
<bean id="SLA" class="org.openspaces.pu.sla.SLA">
    <property name="member-alive-indicator">
        <bean class="org.openspaces.pu.sla.MemberAliveIndicator">
            <property name="invocationDelay" value="5000" />
            <property name="retryCount" value="3" />
            <property name="retryTimeout" value="500" />
        </bean>
    </property>
</bean>
```

{{% /tab %}}
{{% /tabs %}}

#### Troubleshooting the Liveness Detection Mechanism

For troubleshooting purposes, you can lower the logging threshold of the relevant log category by modifying the log configuration file located under `<XAP root>/config/gs_logging.properties` on the GSM. The default definition is as follows:

`org.openspaces.pu.container.servicegrid.PUFaultDetectionHandler.level` = INFO
You can change it to one of the below thresholds for more information:


| Level | Description |
|:------|:------------|
| CONFIG | Logs the configurations applied |
| FINE | Logs once a member is determined as not alive |
| FINER | Logs once a member is indicated as not alive (on each retry) |
| FINEST | every fault detection attempt |

{{% note %}} For service-failure troubleshooting, Level.FINE should suffice.{{%/note%}}


