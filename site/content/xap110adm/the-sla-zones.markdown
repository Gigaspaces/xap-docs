---
type: post110
title:  Hosts, Zones & Machine Utilization
categories: XAP110ADM
parent: the-sla-overview.html
weight: 300
---



 The provisioning process of processing unit instances makes no guarantee regarding which GSCs each instance is provisioned on. It makes a best effort to evenly distribute the instances across available GSCs, taking into account the number of services on each host.



The SLA enables you to define requirements which control the provisioning process of processing unit instances on the available GSCs. The requirements are based on machine level statistics and grouping of the GSC processes to zones (see below).

The following example shows the variety of supported requirements:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<os-sla:sla>
    <os-sla:requirements>
        <os-sla:host ip="192.168.0.1" />
        <os-sla:host ip="192.168.0.2" />
        <os-sla:zone name="zone1" />
        <os-sla:zone name="zone2" />
        <os-sla:cpu high=".9" />
        <os-sla:memory high=".8" />
    </os-sla:requirements>
</os-sla:sla>
```

{{% /tab %}}
{{%tab "  Plain "%}}


```xml
<bean id="SLA" class="org.openspaces.pu.sla.SLA">
    <property name="requirements">
        <list>
            <bean class="org.openspaces.pu.sla.requirement.HostRequirement">
                <property name="id" value="192.168.0.1" />
            <bean class="org.openspaces.pu.sla.requirement.HostRequirement">
                <property name="id" value="192.168.0.2" />
            </bean>
            <bean class="org.openspaces.pu.sla.requirement.ZoneRequirement">
                <property name="zone" value="zone1" />
            </bean>
            <bean class="org.openspaces.pu.sla.requirement.ZoneRequirement">
                <property name="zone" value="zone2" />
            </bean>
            <bean class="org.openspaces.pu.sla.requirement.CpuRequirement">
                <property name="high" value=".9" />
            </bean>
            <bean class="org.openspaces.pu.sla.requirement.MemoryRequirement">
                <property name="high" value=".8" />
            </bean>
        </list>
    </property>
</bean>
```

{{% /tab %}}
{{% /tabs %}}

The above requirements contain the following provisioning constraints which are enforced by the GSM:

- Only provision the processing unit instances to hosts 192.168.0.1 and 192.168.0.2
- Only provision the processing unit instances to GSCs which belong to zone "zone1" or zone "zone2"
- Do not provision processing unit instances to GSCs whose hosting machine utilizes more than 90% of its CPU
- Do not provision processing unit instances to GSCs whose hosting JVM process utilizes more than 80% of its RAM

When using the host or zone requirements, note that more than one requirement can be defined (for example, to define a set of machines this Processing Unit can be deployed to; define the machine's CPU utilization limit; and define the GSC's memory usage limit ).

# Using Zones

{{%align center%}}
![zones.jpg](/attachment_files/zones.jpg)
{{%/align%}}

Defining zones allows you to configure logical tags for each GSC, according to which the processing unit instances will be provisioned to. You can use any alphanumeric combination for a zone name. The GSC can be started with a set of zones that are tagged to it, which match (or not) a given zone requirement in the processing unit's SLA (the zone name matching is case sensitive). The zones of a GSC are specified via the `com.gs.zones` system property. Typically this will be done by setting the GSC_JAVA_OPTIONS environment variable before running the GSC, as shown below (note that you can also edit the `setenv.sh/bat` script manually, but it is less recommended since it touches a core system script):

{{%tabs%}}
{{%tab "  Unix "%}}


```bash
export GSC_JAVA_OPTIONS=-Dcom.gs.zones=zone1,zone3 ${GSC_JAVA_OPTIONS}
```

{{% /tab %}}
{{%tab "  Windows "%}}


```java
set GSC_JAVA_OPTIONS=-Dcom.gs.zones=zone1,zone3 %GSC_JAVA_OPTIONS%
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
Admin admin = new AdminFactory().create();
admin.getGridServiceAgents().waitForAtLeastOne();
GridServiceAgents agents = admin.getGridServiceAgents();
for (GridServiceAgent gridServiceAgent : agents) {
	if (gridServiceAgent.getMachine().getHostName().equalsIgnoreCase("machineA"))
	{
		GridServiceAgent agent = admin.getGridServiceAgents().waitForAtLeastOne();
		GridServiceContainerOptions gscOptions = new GridServiceContainerOptions();
		gscOptions.vmInputArgument("-Dcom.gs.zones=zone1,zone3");
		gscOptions.vmInputArgument("-Xmx256m");
		agent.startGridService(gscOptions);
	}
}
```

{{% /tab %}}
{{% /tabs %}}

The above defines zones "zone1" and "zone3" to be tagged to the GSC which will be started. Note that multiple zones can be specified using a comma separated list.

The following table explains when a container with a specified zone can satisfy a Processing Unit with a required zone:


|Processing Unit|Container|Zone match?|
|:--------------|:--------|:----------|
|PU\[\]|GSC\[\]|YES|
|PU\[\]|GSC\[zone1\]|YES|
|PU\[zone1\]|GSC\[zone1\]|YES|
|PU\[zone1,zone2\]|GSC\[zone1\]|YES|
|PU\[zone1\]|GSC\[zone1,zone2\]|YES|
|PU\[zone1\]|GSC\[\]|NO|
|PU\[zone1\]|GSC\[zone2\]|NO|



# Max Instances per Zone

The SLA definition allows to define maximum instances of a processing unit per zone. The semantics of the setting is different when using a processing unit that has an embedded space with primary / backup, and all other cases (non backup processing unit with embedded space, and plain processing units).

When there are no backups, the setting basically controls the maximum instances per zone out of all the processing unit instances. The setting is usually used with the zone requirement setting to constrain where the processing unit instances will be created. Here is an example of a deployment where only 4 instances are allowed in zone1, and 3 instances are allowed in zone2, and the processing unit should only be instantiated on these two zones:


```xml
<os-sla:sla max-instances-per-zone="zone1/4,zone2/3">
    <os-sla:requirements>
        <os-sla:zone name="zone1" />
        <os-sla:zone name="zone2" />
    </os-sla:requirements>
</os-sla:sla>
```

If, for example, we want to only have 4 instances running on zone1, 3 instances running on zone2, and the rest on zone3, then we can simply allow processing units to be created on zone3 as well in the requirements:


```xml
<os-sla:sla max-instances-per-zone="zone1/4,zone2/3">
    <os-sla:requirements>
        <os-sla:zone name="zone1" />
        <os-sla:zone name="zone2" />
        <os-sla:zone name="zone3" />
    </os-sla:requirements>
</os-sla:sla>
```

When a processing unit does start an embedded space with a backup topology, the semantics of the SLA applies on a per partition level. This means that the setting allows to control if the primary and backup will run on the same zone or not. For example, if we want to deploy a primary with backup topology with 10 partitions each with one backup, and have the primary and backup not run on the same zone we can do the following:


```xml
<os-sla:sla number-of-instances="10" number-of=backups="1" max-instances-per-zone="zone1/1,zone2/1">
    <os-sla:requirements>
        <os-sla:zone name="zone1" />
        <os-sla:zone name="zone2" />
    </os-sla:requirements>
</os-sla:sla>
```

In the above case, the primary and the backup will not run on the same zone. If the primary of partition 1 was started on zone1, then the backup of partition 1 will be started on zone2. This comes very handy when defining rack aware deployments.

{{% refer %}}
You may use the [Primary-Backup Zone Controller](/sbp/primary-backup-zone-controller.html) to deploy primary and backup instances on specific different zones.
{{% /refer %}}

# Requires Isolation

If `requires isolation` is true a, single processing unit instance takes exclusivity on a given GSC and only it can be deployed onto this GSC, (like GSC -Dcom.gigaspaces.grid.gsc.serviceLimit=1)

The following example shows the requires isolation configuration:

{{%tabs%}}

{{%tab "  Namespace "%}}

```xml
<os-core:embedded-space id="space" name="${dataGridName}"/>

<os-sla:sla cluster-schema="partitioned-sync2backup"
           number-of-instances="2" number-of-backups="0" requires-isolation="true">
</os-sla:sla>
```
{{% /tab %}}

{{%tab "  AdminApi "%}}

```java
gsm.deploy(new SpaceDeployment("mySpace").numberOfInstances(2).numberOfBackups(0).requiresIsolation(true));
gsm.deploy((new ProcessingUnitDeployment("myPU").numberOfInstances(2).numberOfBackups(0).requiresIsolation(true)));
```
{{% /tab %}}

{{%tab "  Command Line Interface "%}}

```xml
gs> deploy-space -requires-isolation true mySpace
gs> deploy -requires-isolation true myPU
```
{{% /tab %}}

{{% /tabs %}}

# Instance Level Requirements

You can also define SLA deployment requirements on per processing unit instance basis. Here is an example:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<os-sla:sla>
    <os-sla:requirements>
        <os-sla:cpu high=".9" />
        <os-sla:memory high=".8" />
    </os-sla:requirements>
    <os-sla:instance-SLAs>
        <os-sla:instance-SLA instance-id="1">
             <os-sla:requirements>
                <os-sla:host ip="100.0.0.1" />
             </os-sla:requirements>
        </os-sla:instance-SLA>
        <os-sla:instance-SLA instance-id="1" backup-id="1">
             <os-sla:requirements>
                <os-sla:host ip="100.0.0.2" />
             </os-sla:requirements>
        </os-sla:instance-SLA>
    </os-sla:instance-SLAs>
</os-sla:sla>
```

{{% /tab %}}
{{%tab "  Plain "%}}


```xml
<bean id="SLA" class="org.openspaces.pu.sla.SLA">
    <property name="requirements">
        <list>
            <bean class="org.openspaces.pu.sla.requirement.CpuRequirement">
                <property name="high" value=".9" />
            </bean>
            <bean class="org.openspaces.pu.sla.requirement.MemoryRequirement">
                <property name="high" value=".8" />
            </bean>
        </list>
    </property>
    <property name="instanceSLAs">
        <list>
            <bean class="org.openspaces.pu.sla.InstanceSLA">
                <property name="instanceId" value="1" />
                <property name="requirements">
                    <list>
                        <bean class="org.openspaces.pu.sla.requirement.HostRequirement">
                            <property name="id" value="100.0.0.1" />
                        </bean>
                    </list>
                </property>
            </bean>
            <bean class="org.openspaces.pu.sla.InstanceSLA">
                <property name="instanceId" value="1" />
                <property name="backupId" value="1" />
                <property name="requirements">
                    <list>
                        <bean class="org.openspaces.pu.sla.requirement.HostRequirement">
                            <property name="id" value="100.0.0.2" />
                        </bean>
                    </list>
                </property>
            </bean>
        </list>
    </property>
</bean>
```

{{% /tab %}}
{{% /tabs %}}

The above example verifies that the **first instance** is deployed to a specific machine (specified by its IP address), and its **second instance** for the same partition is deployed to a different machine. All instances share the "general" requirements of CPU and memory. The first instance **might be** the primary and the second might be the backup, but there is no guarantee these will remain primary/backup as these are runtime properties and might change during the life-cycle of the clustered space. The activation property of the space (primary or backup) is determined once the instance is deployed and is not controlled by the GSM but as part of the primary election process.


To control the location of the primary and backup instances during the life-cycle of the clustered space you should use the [Primary-Backup Zone Controller best practice](/sbp/primary-backup-zone-controller.html).

# Zone Based Partitioning Provisioning
To accommodate partitions with different size we can use the zones configuration to provision each partition into a different zone. Each zone will be associated with GSCs having a different heap size (Xmx). The assumption here is a GSCs hosting a single partition instance (primary or a backup instance). With the following example we deploy a cluster with 3 partitions where each partition deployed into a different zone: Small (1GB GSC), Medium (2GB GSC ) and Large (3GB GSC).

To start the small zone:

```bash
set GSC_JAVA_OPTIONS=-Dcom.gs.zones=zoneSmall -Xmx1g
gs-agent.bat
```

To start the medium zone:

```bash
set GSC_JAVA_OPTIONS=-Dcom.gs.zones=zoneMedium -Xmx2g
gs-agent.bat
```

To start the large zone:

```bash
set GSC_JAVA_OPTIONS=-Dcom.gs.zones=zoneLarge -Xmx3g
gs-agent.bat
```

Deploy a 3 partitions with a backup space cluster using the following sla.xml:


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:os-sla="http://www.openspaces.org/schema/sla"
xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/sla/openspaces-sla.xsd">

<os-sla:sla>
	<os-sla:instance-SLAs>
		<os-sla:instance-SLA instance-id="1">
			<os-sla:requirements>
				<os-sla:zone name="zoneSmall" />
			</os-sla:requirements>
		</os-sla:instance-SLA>
		<os-sla:instance-SLA instance-id="1" backup-id="1">
			<os-sla:requirements>
				<os-sla:zone name="zoneSmall" />
			</os-sla:requirements>
		</os-sla:instance-SLA>
		<os-sla:instance-SLA instance-id="2">
			<os-sla:requirements>
				<os-sla:zone name="zoneMedium" />
			</os-sla:requirements>
		</os-sla:instance-SLA>
		<os-sla:instance-SLA instance-id="2" backup-id="1">
			<os-sla:requirements>
				<os-sla:zone name="zoneMedium" />
			</os-sla:requirements>
		</os-sla:instance-SLA>
		<os-sla:instance-SLA instance-id="3">
			<os-sla:requirements>
				<os-sla:zone name="zoneLarge" />
			</os-sla:requirements>
		</os-sla:instance-SLA>
		<os-sla:instance-SLA instance-id="3" backup-id="1">
			<os-sla:requirements>
				<os-sla:zone name="zoneLarge" />
			</os-sla:requirements>
		</os-sla:instance-SLA>
	</os-sla:instance-SLAs>
</os-sla:sla>
</beans>
```


{{% note %}}
When using instance level SLA, max-instances settings do not apply (or any cluster level setting).
{{% /note %}}



