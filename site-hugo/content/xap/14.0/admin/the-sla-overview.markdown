---
type: post140
title:  Service Level Agreement (SLA)
categories: XAP140ADM, PRM
weight: 350
parent: none
---


The GigaSpaces runtime environment (also referred to as the Service Grid) provides SLA-driven capabilities via the [Grid Service Manager (GSM)](../overview/the-runtime-environment.html#gsm) and the [Grid Service Container (GSC)](../overview/the-runtime-environment.html#gsc) runtime components. The GSC is responsible for running one or more Processing Units. The GSM is responsible for analyzing the deployment and provisioning the processing unit instances to the available GSCs.

{{% note "Enforcing SLA Definitions "%}}
The SLA definitions are only enforced when deploying the Processing Unit to the Service Grid, because this environment actively manages and controls the deployment using the GSM(s). When running within your IDE or in standalone mode, these definitions are ignored.
{{% /note %}}

The SLA definitions can be provided as part of the Processing Unit package, or during the Processing Unit's deployment process. These definitions determine the number of Processing Unit instances that should run, and deploy-time requirements such as the amount of free memory or CPU, or the clustering topology for Processing Units that contain a Space. The GSM reads the SLA definitions, and deploys the Processing Unit to the available GSCs accordingly.

# Defining the SLA for Your Processing Unit

The SLA contains the deployment criteria in terms of clustering topology (if it contains a Space) and deployment-time requirements. It can be defined in multiple ways:

- Include an `sla.xml` file that contains the definitions within the Processing Unit's JAR file. This file can be located at the root of the Processing Unit JAR or under the `META-INF/spring` directory, alongside the Processing Unit's XML file. This is the recommended method.

-  Embed the SLA definitions within the Processing Unit's `pu.xml` file.

-  Provide a separate XML file with the SLA definitions to the GSM at deployment time via one of the [deployment tools](../dev-java/deploying-onto-the-service-grid.html).

- Use the deployment tools themselves to provide/override the Processing Unit's SLA (see below). For example, the GUI deployment dialog enables you to type in various SLA definitions, such as the number of instances, number of backups, and Space topology.

The SLA definition, whether it comes in a separate file or embedded inside the `pu.xml` file, is composed of a single `<os-sla:sla>` XML element. A sample SLA definition is shown below (you can use plain Spring definitions, or GigaSpaces-specific namespace bindings):

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

The SLA definition shown above creates four instances of a Processing Unit using the `partitioned` [space topology](../overview/the-runtime-environment.html#topologies). It defines two partitions (`number-of-instances="2"`), each with one backup (`number-of-backups="1"`). In addition, it requires that a primary and a backup instance of the same partition not be provisioned to the same GSC (`max-instances-per-vm="1"`).

{{% note "Note"%}}
It is up to the developer to configure the SLA correctly. Trying to deploy a Processing Unit with a cluster schema that requires backups without specifying `numberOfBackups` will cause the deployment to fail.
{{%/note%}}

{{% note "Info"%}}
In older releases, the SLA definition also included dynamic runtime policies, such as creating additional Processing Unit instances based on CPU load, relocating a certain instance when the memory becomes saturated, etc. These capabilities are still supported, but have been deprecated in favor of the [Administration and Monitoring API](../dev-java/administration-and-monitoring-overview.html) which supports the above and and much more.
{{% /note %}}

# Defining the Space Cluster Topology

When a Processing Unit contains a Space, the SLA definition specifies the clustering topology for that Space. The `cluster-schema` XML element defines this, and is an attribute of the `os-sla:sla` XML element, as shown above.

The Space's clustering topology is defined within the SLA definition rather than the Space definition with the `pu.xml` file for the following reasons:

- The clustering topology directly affects the number of instances of the Processing Unit, and is therefore considered part of its SLA. For example, a partitioned Space can have two primaries and two backups, and a replicated Space can have five instances. This means that the Processing Unit containing them will have the same number of instances.

- Separating the clustering topology from the actual Space definition enables truly implementing the "write once, scale anywhere" principal. You can run the same Processing Unit within your IDE for unit tests using the default single-Space-instance clustering topology, and then deploy it to the runtime environment and run the same Processing Uunit with the real clustering topology (for example, with 4 partitions).

You can choose from numerous clustering topologies:

- `default`: Single Space instance, no replication or partitioning

- `sync-replicated`: Multiple Space instances. When written to one of the Space instances, objects are **synchronously** replicated to all Space instances. The maximum capacity of this topology is that of the smallest JVM in the cluster.

- `async-replicated` Multiple space instances. When written to one of the Space instances, objects are **asynchronously** replicated to all Space instances. The maximum capacity of this topology is that of the smallest JVM in the cluster.

- `partitioned` Multiple Space instances. Objects are distributed across all of the Space instances, so that each instance contains a separate subset of the data and forms a separate partition. The partitioning (distribution) of objects is based on their routing property. Optionally, when using this topology, you can designate one or more backup instances to each of the partitions, so that when an object is written to a certain partition, it is synchronously replicated to the backup copy(ies) of that partition. The maximum capacity of this topology is the overall capacity of all of the JVMs in the cluster, divided by the number of backups+1.

From the client application's perspective (the one that connects to the Space from another process), the clustering topology is transparent in most cases.

{{% refer %}}
Please refer to [this page](../overview/the-runtime-environment.html#topologies) for more details about space clustering topologies and guidelines regarding when to use each of the topologies.
The number-of-backups parameter should be used with the partitioned cluster schema. It is not supported with the sync-replicated or async-replicated cluster schema.
{{% /refer %}}

# SLA-Based Distribution and Provisioning

When deployed to the service grid, the Processing Unit instances are distributed based on the SLA definitions. These definitions form a set of constraints and requirements that should be met when a Processing Unit instance is provisioned to a specific container (GSC). The SLA definitions are considered during the initial deployment, relocation, and re-provisioning of an instance after failure.

# Default SLA Definitions

If no SLA definition is provided either within the Processing Unit XML configuration or during deploy time, the following default SLA is used:

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

# Maximum Instances per VM/Machine

The SLA definition allows you to define the maximum number of instances for a certain Processing Unit, either per JVM (GSC) or physical machine (regardless of the number of JVMs/GSCs that are running on it).

The max-instances parameter has different semantics when applied to Processing Units that contain a Space with primary-backup semantics (that uses the  partitioned cluster schema and defines at least one backup), and when applied to a Processing Unit that doesn"t contain an embedded Space, or that contains a Space with no primary-backup semantics.

{{% note "Note"%}}
When applied to a Processing Unit that doesn't contain an embedded Space, or that contains a Space with no primary-backup semantics, the `max-instances` parameter defines the **total** number of instances that can be deployed on a single JVM or on a single machine.
{{%/note%}}

When applied to a Processing Unit that contains a Space with primary-backup semantics, the `max-instances` parameter defines the total number of instances (which belong to the same primary-backup group or partition) that can be provisioned to a single JVM or a single machine.

The most common use of the `max-instances` parameter is for a Processing Unit that contains a Space with primary-backup semantics. Setting the value to `1` ensures that a primary and its backup(s) cannot be provisioned to the same JVM (GSC)/physical machine.

**max-instances-per-vm**<br>
The `max-instances-per-vm` defines the maximum number of instances per partition for a Processing Unit with an embedded Space. A partition may have a primary or backup instance. `max-instances-per-vm=1` means you won't have a primary and a backup of the same partition provisioned to the same GSC. (It is allowed to have multiple partitions with primary or backup instances provisioned to the same GSC.) You cannot limit the amount of instances from different partitions that a GSC may host.

If you have enough GSCs (the number of partitions x2), you will have a single instance per GSC. If you don't have enough GSCs, the primary and backup instances of the different partitions will be distributed across all the existing GSCs, and the system tries to distribute the primary instances in an even manner across all the GSCs on all the machines. If you increase the amount of GSCs after the initial deployment, you must "rebalance" the system, meaning distribute all the primaries across all the GSCs.

You can perform this activity via API, or automatically by using the ESM. Rebalancing the instances will increase the capacity of the data grid (because it will result in more GSCs hosting the data grid).
The rebalance concept is based on the assumption that there are initially more partitions than GSCs. The ratio between partitions to GSCs is called the scaling factor; meaning how much your data grid can expand itself without shutdown, and without increasing the amount of partitions.

Example: Start with 4 GSCs and 20 partitions with backups (40 instances). Each GSC will initially host 10 instances, and can end up with 40 GSCs (after one or more rebalancing operations) where each GSC hosts a single instance. In this example,  the capacity of the data grid is increased to 10 times larger without downtime, while the amount of partitions remains the same.
See the [capacity planning](/sbp/capacity-planning.html) section for more details.
 

The following is an example of setting the `max-instances-per-vm` parameter:

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

The following is an example of setting the maximum instances per machine parameter:

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



# Total Maximum Instances per VM

This refers to the total amount of Processing Unit instances that can be instantiated within a GSC. It should not be confused with the `max-instances-per-vm` parameter, which controls how many instances a partition may have within a GSC. The total maximum instances per VM does not control the total amount of instances from different Processing Units, or other partitions, that can be provisioned into a GSC. To control the total maximum instances per VM, use the  `com.gigaspaces.grid.gsc.serviceLimit` system property and set its value before running the GSC:


```bash
set XAP_GSC_OPTIONS=-Dcom.gigaspaces.grid.gsc.serviceLimit=2
```

The default value of the `com.gigaspaces.grid.gsc.serviceLimit` is **500**.


{{% anchor livenessDetection %}}

# Monitoring the Liveness of Processing Unit Instances

The [GSM](../overview/the-runtime-environment.html#gsm) monitors the liveness of all the Processing Unit instances it provisioned to the GSCs. The GSM pings each instance in the cluster to see whether it is available.

You can control how often a Processing Unit instance is monitored by the GSM, and in case of failure, how many times the GSM will try again to ping the instance and how long it will wait between retry attempts.

This is done using the `<os-sla:member-alive-indicator>` element, which contains the following attributes:


| Attribute | Description | Default |
|:----------|:------------|:--------|
| invocation-delay | How often (in milliseconds) an instance is monitored and verified to be alive by the GSM | 5000 (5 seconds) |
| retry-count | After an instance has been determined to not be alive, how many times to check it before giving up on it | 3 |
| retry-timeout | After an instance has been determined to not be alive, the timeout interval between retries (in milliseconds) | 500 (0.5 seconds) |

When a Pprocessing Unit instance is determined to be unavailable, the GSM that manages its deployment tries to re-provision the instance on another GSC (according to the SLA definitions).

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

## Troubleshooting the Liveness Detection Mechanism

For troubleshooting purposes, you can lower the logging threshold of the relevant log category by modifying the log configuration file located under `<XAP root>/config/log/xap_logging.properties` on the GSM. The default definition is as follows:

`org.openspaces.pu.container.servicegrid.PUFaultDetectionHandler.level` = INFO
You can change it to one of the below thresholds for more information:


| Level | Description |
|:------|:------------|
| CONFIG | Logs the configurations applied |
| FINE | Logs when a member is determined to be not alive |
| FINER | Logs when a member is indicated to be not alive (on each retry) |
| FINEST | every fault detection attempt |

{{% note "Note"%}} Level FINE is generally sufficient for service-failure troubleshooting.{{%/note%}}


