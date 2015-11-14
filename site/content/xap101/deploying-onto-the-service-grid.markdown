---
type: post101
title:  Deploying onto the Grid
categories: XAP101
parent: deploying-and-running-overview.html
weight: 300
---

{{% ssummary%}}{{% /ssummary %}}



Deploying your processing unit to the [service grid]({{%currentadmurl%}}/the-runtime-environment.html) is the preferred way to run in your production environment. The service grid provides the following main benefits to every processing unit deployed onto it:

- Automatic distribution and provisioning of the processing unit instances: When deploying to the [service grid]({{%currentadmurl%}}/the-runtime-environment.html) the [GigaSpaces Manager](/product_overview/service-grid.html#gsm) identifies the relevant [GigaSpaces Containers](/product_overview/service-grid.html#gsc) and takes care of distributing the processing unit binaries to them. You do not need to manually install the processing unit anywhere on the cluster - only into the service grid.

- SLA enforcement: The [GigaSpaces Manager](/product_overview/service-grid.html#gsm) is also responsible for enforcing your processing unit's [Service Level Agreement]({{%currentadmurl%}}/the-sla-overview.html), or SLA. At deployment time, it will create a specified number of processing unit instances (based on the SLA) and provision them to the running containers while enforcing all the [deployment requirements]({{%currentadmurl%}}/the-sla-overview.html), such as memory and CPU utilization, or specific deployment zones. At runtime, it will monitor the processing unit instances, and if any of them fail to fulfill the SLA or become unavailable it will re-instantiate the processing unit automatically on another container.

- Automatic machine provisioning and dynamic SLA enforcement: Elastic Processing Units are a new type of [Processing Unit](./the-processing-unit-overview.html). Elastic PUs provision machines and start [GigaSpaces Containers](/product_overview/service-grid.html#gsc) automatically based on the processing units' Memory and CPU requirements. When the available machines do not meet the requirements, the manager provisions new machines and balances the Processing Unit deployment across machines. This also occurs when a machine fails, or when the requirements changes while the application is running.

{{% refer %}}
You can use the [GigaSpaces Universal Deployer](/sbp/universal-deployer.html) to deploy complex multi processing unit applications.
{{% /refer%}}

# The Deployment Process

Once built according to the processing unit [directory structure](./the-processing-unit-structure-and-configuration.html), the processing unit can be deployed via the various deployment tools available in GigaSpaces XAP ([UI]({{%currentadmurl%}}/gigaspaces-management-center.html), [CLI]({{%currentadmurl%}}/deploy-command-line-interface.html), Ant, [Maven](./maven-plugin.html) or the [Admin API](./administration-and-monitoring-api.html)).

After you [package](./the-processing-unit-overview.html) the processing unit and deploy it via one of the deployment tools, the deployment tool uploads it to all the running [GSMs](/product_overview/service-grid.html#gsm), where it is extracted and provisioned to the [GSCs](/product_overview/service-grid.html#gsc).

{{% info "To Jar or Not to Jar "%}}
The recommended way to deploy the processing unit is by packaging it into a .jar or a .zip archive and specifying the location of the packaged file to the deployment tool in use.

However, GigaSpaces XAP also supports the deployment of exploded processing units. (The deployment tool will package the processing unit directories into a jar file automatically). Another option to deploy a processing unit is by placing the exploded processing unit under the deploy directory of each of the GSMs and issuing  a deploy command with the processing unit name (the name of the directory under the deploy directory).
{{% /info %}}

# Distribution of Processing Unit Binaries to the Running GSCs

By default, when a processing unit instance is provisioned to run on a certain GSC, the GSC downloads the processing unit archive from the GSM into the `<XAP Root>/work/deployed-processing-units` directory (The location of this directory can be overridden via the `com.gs.work` system property).

Downloading the processing unit archive to the GSC is the recommended option, but it can be disabled. In order to disable it, the `pu.download` [deployment property](./deployment-properties.html)  should be set to `false`. This will not download the entire archive to the GSC, but will force the GSC to load the processing unit classes one at a time from the GSM via a URLClassLoader.

{{% anchor deployDirections %}}

# Processing Unit Deployment using various Deployment Tools

GigaSpaces provides several options to deploy a processing unit onto the Service Grid. Below you can find a simple deployment example with the various deployment tools for deploying a processing unit archive called `myPU.jar` located in the `/opt/gigaspaces` directory:

{{%tabs%}}
{{%tab "  Admin API "%}}
Deploying via code is done using the GigaSpaces [Admin API](./administration-and-monitoring-api.html). The following example shows how to deploy the `myPU.jar` processing unit using one of the available GSMs. For more details please consult the [Admin API](./administration-and-monitoring-api.html)


```java
Admin admin = new AdminFactory().addGroup("myGroup").create();
File puArchive = new File("/opt/gigaspaces/myPU.jar");
ProcessingUnit pu = admin.getGridServiceManagers().waitForAtLeastOne().deploy(
    new ProcessingUnitDeployment(puArchive));
```

{{% /tab %}}
{{%tab "  Ant "%}}
Deploying with Ant is based on the `org.openspaces.pu.container.servicegrid.deploy.Deploy` class (in fact, all of the deployment tools use this class although it is not exposed directly to the end user).

In the below example we create an Ant macro using this class and use it to deploy our processing unit. The deploy class is executable via its `main()` method, and can accept various parameters to control the deployment process. These parameters are identical to these of the `deploy` CLI command, for a complete list of the available parameters please consult the [`deploy` CLI reference documentation.]({{%currentadmurl%}}/deploy-command-line-interface.html).


```xml
<deploy file="/opt/gigaspaces/myPU.jar" />

<macrodef name="deploy">
    <attribute name="file"/>
    <sequential>
        <java
            classname="org.openspaces.pu.container.servicegrid.deploy.Deploy"
            fork="false">
            <classpath refid="all-libs"/>
            <arg value="-groups" />
            <arg value="mygroup" />
            <arg value="@{file}"/>
        </java>
    </sequential>
</macrodef>
```

{{% /tab %}}
{{%tab "  GigaSpaces CLI "%}}
Deploying via the CLI is based on the `deploy` command. This command accepts various parameters to control the deployment process. These parameters are documented in full in the [`deploy` CLI reference documentation.]({{%currentadmurl%}}/deploy-command-line-interface.html).


```java
> <XAP root>/bin/gs.sh(bat) deploy myPU.jar
```

{{% /tab %}}
{{%tab "  GigaSpaces UI "%}}

- Open the GigaSpaces UI by launching `<XAP root>/bin/gs-ui.sh(bat)
- Click the "Deploy Application" button ![deploy_processing_unit_button.jpg](/attachment_files/deploy_processing_unit_button.jpg) at the top left of the window
- In the deployment wizard, click **...** to select your processing unit archive, and then click **Deploy**
{{% /tab %}}
{{% /tabs %}}

# Elastic Processing Unit Deployment using the Admin API

When deploying a partitioned Processing Unit or a partitioned Space it is recommended to use the new [Elastic Processing Unit](./elastic-processing-unit.html). This can be done via the [Admin API](./administration-and-monitoring-api.html). The following example shows how to deploy a processing unit as an Elastic PU.

## Step 1 - Start a GigaSpaces agent on each machine:

You should have **one GigaSpaces agent** running the ESM. No GSCs should be started.

{{%tabs%}}
{{%tab "  Windows "%}}


```java
rem Agent deployment that potentially can start management processes
set LOOKUPGROUPS=myGroup
set JSHOMEDIR=d:\gigaspaces
start cmd /c "%JSHOMEDIR%\bin\gs-agent.bat gsa.global.esm 1 gsa.gsc 0 gsa.global.gsm 2 gsa.global.lus 2"
```

{{% /tab %}}

{{%tab "  Linux "%}}


```java
# Agent deployment that potentially can start management processes

export LOOKUPGROUPS=myGroup
export JSHOMEDIR=~/gigaspaces
nohup ${JSHOMEDIR}/bin/gs-agent.sh gsa.global.esm 1 gsa.gsc 0 gsa.global.gsm 2 gsa.global.lus 2 > /dev/null 2>&1 &
```

{{% /tab %}}
{{% /tabs %}}

## Step 2 - Run the deployment code:


```java
// Wait for the discovery of the managers and at least one agent
Admin admin = new AdminFactory().addGroup("myGroup").create();
admin.getGridServiceAgents().waitForAtLeastOne();
admin.getElasticServiceManagers().waitForAtLeastOne();
GridServiceManager gsm = admin.getGridServiceManagers().waitForAtLeastOne();

// Deploy the Elastic Processing Unit.
// Set the maximum memory and CPU capacity and initial capacity
File puArchive = new File("/opt/gigaspaces/myPU.jar");
ProcessingUnit pu = gsm.deploy(
        new ElasticStatefulProcessingUnitDeployment(puArchive)
           .memoryCapacityPerContainer(16,MemoryUnit.GIGABYTES)
           .maxMemoryCapacity(512,MemoryUnit.GIGABYTES)
           .maxNumberOfCpuCores(32)
           // uncomment when working with a single machine agent
           //.singleMachineDeployment()
           // set the initial memory and CPU capacity
           .scale(new ManualCapacityScaleConfigurer()
                  .memoryCapacity(128,MemoryUnit.GIGABYTES)
                  .numberOfCpuCores(8)
                  .create())
);

// Wait until the deployment is complete.
pu.waitForSpace().waitFor(pu.getTotalNumberOfInstances());
```

## Step 3 - Scale the PU by increasing the memory and CPU capacity:


```java
// Scale in runtime to the desired memory and CPU capacity
pu.scale(
    new ManualCapacityScaleConfigurer()
       .memoryCapacity(256,MemoryUnit.GIGABYTES)
       .numberOfCpuCores(16)
       .create()
);
```

## Step 4 - To undeploy the Processing Unit run the following:


```java
// Wait up to 10 seconds for the processing unit to be discovered
ProcessingUnit pu = admin.getProcessingUnits()
   .waitFor("myPU",10,TimeUnit.SECONDS);
if (pu != null) {
   // Wait up to 3 minutes for the processing unit
   // (including all instances) to be gracefully undeployed
   // For versions prior to 8.0.5 (7.x - 8.0.4) use pu.undeploy()
   pu.undeployAndWait(3, TimeUnit.MINUTES);
}
```

Since we are un deploying an Elastic Processing Unit, this will also terminate all the GSCs hosting the PU.

Running the deployment code from the command line is very convenient. Rename the java file extension to groovy, and run it using the following command:

{{%tabs%}}
{{%tab "  Windows "%}}


```java
%JSHOMEDIR%\tools\groovy\bin\groovy deploy.groovy
```

{{% /tab %}}

{{%tab "  Linux "%}}


```java
${JSHOMEDIR}tools/groovy/bin/groovy deploy.groovy
```

{{% /tab %}}
{{% /tabs %}}

# Hot Deploy

To enable business continuity in a better manner, having system upgrade without any downtime, here is a simple procedure you should follow when you would like to perform a hot deploy, upgrading a PU that includes both a business logic and a collocated embedded space:

1. Upload the PU new/modified classes (i.e. polling container `SpaceDataEvent` implementation or relevant listener class and any other dependency classes) to the PU deploy folder on all the GSM machines.
2. Restart the PU instance running the backup space. This will force the backup PU instance to reload a new version of the business logic classes from the GSM.
3. Wait for the backup PU to fully recover its data from the primary.
4. Restart the Primary PU instance. This will turn the existing backup instance to become a primary instance. The previous primary will turn into a backup, load the new business logic classes and recover its data from the existing primary.
5. Optional - You can restart the existing primary to force it to switch into a backup instance again. The new primary will also use the new version of the business logic classes.

You can script the above procedure via the [Administration and Monitoring API](./administration-and-monitoring-api.html), allowing you to perform system upgrade without downtime.

## Restart a running PU via the GS-UI

To restart a running PU (all instances) via the GS-UI you should:

1. Start the GS-UI - move into the Deployed Processing Unit tab
2. Right click the mouse on the PU instance you want to restart
3. Select the restart menu option
![pu_restart.jpg](/attachment_files/pu_restart.jpg)

4. Confirm the operation
![pu_restart2.jpg](/attachment_files/pu_restart2.jpg)

5. Within few seconds the restart operation will be completed. If the amount of data to recover is large (few millions of objects), this might take few minutes.
6. Repeat steps 2-4 for all backup instances.
7. Repeat steps 2-4 for all primary instances. This will switch the relevant backup to be a primary mode where the existing primary will switch into a backup mode.

## Restart a running PU via the Admin API

The [ProcessingUnitInstance](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/pu/ProcessingUnitInstance.html) includes few `restart` methods you may use to restart a PU instance:


```java
restart()
restartAndWait()
restartAndWait(long timeout, TimeUnit timeUnit)
```

Here is an example code that is using the `ProcessingUnitInstance.restart` to restart the entire PU instances in an automatic manner:


```java
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.openspaces.admin.pu.ProcessingUnit;
import org.openspaces.admin.pu.ProcessingUnitInstance;
import com.gigaspaces.cluster.activeelection.SpaceMode;

public class PUReatartMain {
	static Logger logger = Logger.getLogger("PUReatart");

	public static void main(String[] args) {
		String puToRestart = "myPU";
		Admin admin = new AdminFactory().createAdmin();

		ProcessingUnit processingUnit = admin.getProcessingUnits().waitFor(
				puToRestart, 10, TimeUnit.SECONDS);

                if (processingUnit == null)
                {
		   logger.info("can't get PU instances for "+puToRestart );
		   admin.close();
		   System.exit(0);
                }

                // Wait for all the members to be discovered
                processingUnit.waitFor(processingUnit.getTotalNumberOfInstances());

		ProcessingUnitInstance[] puInstances = processingUnit.getInstances();
		// restart all backups
		for (int i = 0; i < puInstances.length; i++) {

			if (puInstances[i].getSpaceInstance().getMode() == SpaceMode.BACKUP) {
				restartPUInstance(puInstances[i]);
			}
		}

		// restart all primaries
		for (int i = 0; i < puInstances.length; i++) {
			if (puInstances[i].getSpaceInstance().getMode() == SpaceMode.PRIMARY) {
				restartPUInstance(puInstances[i]);
			}
		}
		admin.close();
		System.exit(0);
	}
	private static void restartPUInstance(
			ProcessingUnitInstance pi) {
		final String instStr = pi.getSpaceInstance().getMode() != SpaceMode.PRIMARY?"backup" : "primary";
		logger.info("restarting instance " + pi.getInstanceId()
				+ " on " + pi.getMachine().getHostName() + "["
				+ pi.getMachine().getHostAddress() + "] GSC PID:"
				+ pi.getVirtualMachine().getDetails().getPid() + " mode:"
				+ instStr + "...");

		pi = pi.restartAndWait();
		logger.info("done");
	}
}
```


{{%refer%}}
The [XAP Hot Deploy](/sbp/xap-hot-deploy.html) tool allows business logic running as a PU to be refreshed (rolling PU upgrade) without any system downtime and data loss. The tool uses the hot deploy approach , placing new PU code on the GSM PU deploy folder and later restart each PU instance.
{{%/refer%}}


# Application Deployment and Processing Unit Dependencies

An application is a logical abstraction that groups one or more Processing Units. Application allows:

1. Dedicated page in the Web User Interface showing the application Processing Units and dependencies
1. Parallel deployment of application processing units, while respecting dependency order.
1. Undeployment of application processing units in reverse dependency order

## Web User Interface

Each application gets its own page in the web user interface which includes a symbol for each processing unit, and a directed arrow showing the dependencies between the deployed processing unites. See the example below where the feeder PU depends on the Space PU (feeder->space).
![feeder_space.png](/attachment_files/feeder_space.png)

The feeder instance is deployed only after the space is deployed
![feeder_space2.png](/attachment_files/feeder_space2.png)

## Deployment Dependencies

The sample code below deploys an application named "data-app" which consists of a space and a feeder processing unit. The feeder processing unit instances are deployed only after the space deployment is complete (each partition has both a primary and a backup space instance).

{{%tabs%}}
{{%tab "  Admin API "%}}


```java
Admin admin = new AdminFactory().addGroup("myGroup").create();
File feederArchive = new File("/opt/gigaspaces/myfeeder.jar");

Application dataApp = admin.getGridServiceManagers().deploy(
  new ApplicationDeployment("data-app")

  .addProcessingUnitDeployment(
    new ProcessingUnitDeployment(feederArchive)
    .name("feeder")
    .addDependency("space"))

  .addProcessingUnitDeployment(
    new SpaceDeployment("space"))
);

for (ProcessingUnit pu : dataApp.getProcessingUnits()) {
  pu.waitFor(pu.getTotalNumberOfInstances());
}
```

{{% /tab %}}
{{%tab "  Admin API and XML "%}}

Since XAP v9.0.1 the processing unit dependencies can be described using an XML file.



```java
Admin admin = new AdminFactory().addGroup("myGroup").create();
//The dist zip file includes feeder.jar and application.xml file
File application = new File("/opt/gigaspaces/examples/data/dist.zip");
//Application folders are supported as well
//File application = new File("/opt/gigaspaces/examples/data/dist");
Application dataApp = admin.getGridServiceManagers().deploy(
  new ApplicationFileDeployment(application)
  .create()
);

for (ProcessingUnit pu : dataApp.getProcessingUnits()) {
  pu.waitFor(pu.getTotalNumberOfInstances());
}
```

Here is the content of the application.xml file (that resides alongside feeder.jar in dist.zip):


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

		<os-admin:space name="space" />

		<os-admin:pu processing-unit="feeder.jar">
			<os-admin:depends-on name="space"/>
		</os-admin:pu>

	</os-admin:application>
</beans>
```

{{% /tab %}}
{{%tab "  GigaSpaces CLI and XML "%}}
Since XAP v9.0.1 the processing unit dependencies can be described using an XML file.


```java
> <XAP root>/bin/gs.sh(bat) deploy-application examples/data/dist.zip
```

Here is the content of the application.xml file (that resides alongside feeder.jar in dist.zip):


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

		<os-admin:space name="space" />

		<os-admin:pu processing-unit="feeder.jar">
			<os-admin:depends-on name="space"/>
		</os-admin:pu>

	</os-admin:application>
</beans>
```

{{% /tab %}}

{{% /tabs %}}

The reason for imposing this dependency is that the space proxy bean in the feeder processing unit would fail to initialize if the space is not available. However, this restriction could be too severe since the feeder is a singleton processing unit. For example, if a container with the feeder and a space instance fails, the space is still available (the backup is elected to primary). However the feeder is not re-deployed until the space has all instances running, which will not happen unless a container is (re)started.

## Adaptive SLA

The feeder can relax this restriction, by specifying a dependency of at least one instance per partition. Now the feeder is redeployed as long as the space has a minimum of one instance per partition. The downside of this approach is that during initial deployment there is a small time gap in which the feeder writes data to the space while there is only one copy of the data (one instance per partition).

{{%tabs%}}
{{%tab "  Admin API "%}}


```java
Admin admin = new AdminFactory().addGroup("myGroup").create();
File feederArchive = new File("/opt/gigaspaces/myfeeder.jar");

// The ProcessingUnitDependenciesConfigurer supports dependencies on a minimum number of instances,
// on a minimum number of instances per partition, or waiting for a deployment of another processing unit to complete.
Application dataApp = admin.getGridServiceManagers().deploy(
  new ApplicationDeployment("data-app")
    .addProcessingUnitDeployment(
      new ProcessingUnitDeployment(feederArchive)
        .name("feeder")
        .addDependencies(new ProcessingUnitDeploymentDependenciesConfigurer()
                     .dependsOnMinimumNumberOfDeployedInstancesPerPartition("space",1)
                     .create())
    .addProcessingUnitDeployment(
      new SpaceDeployment("space"))

);

for (ProcessingUnit pu : dataApp.getProcessingUnits()) {
  pu.waitFor(pu.getTotalNumberOfInstances());
}
```

{{% /tab %}}
{{%tab "  Admin API with XML "%}}
Since XAP v9.0.1 the processing unit dependencies can be described using an XML file.



```java
Admin admin = new AdminFactory().addGroup("myGroup").create();
//The dist zip file includes feeder.jar and application.xml file
File application = new File("/opt/gigaspaces/examples/data/dist.zip");
//Application folders are supported as well
//File application = new File("/opt/gigaspaces/examples/data/dist");
Application dataApp = admin.getGridServiceManagers().deploy(
  new ApplicationFileDeployment(application)
  .create()
);

for (ProcessingUnit pu : dataApp.getProcessingUnits()) {
  pu.waitFor(pu.getTotalNumberOfInstances());
}
```

Here is the content of the application.xml file (that resides alongside feeder.jar in dist.zip):


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

		<os-admin:space name="space" />

		<os-admin:pu processing-unit="feeder.jar">
			<os-admin:depends-on name="space" min-instances-per-partition="1"/>
		</os-admin:pu>

	</os-admin:application>
</beans>
```

{{% /tab %}}
{{%tab "  GigaSpaces CLI with XML "%}}
Since XAP v9.0.1 the processing unit dependencies can be described using an XML file.


```java
gigaspaces/bin/gs.sh deploy-application gigaspaces/examples/data/dist.zip
```

Here is the content of the application.xml file (that resides alongside feeder.jar in dist.zip):


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

		<os-admin:space name="space" />

		<os-admin:pu processing-unit="feeder.jar">
			<os-admin:depends-on name="space" min-instances-per-partition="1"/>
		</os-admin:pu>

	</os-admin:application>
</beans>
```

{{% /tab %}}

{{% /tabs %}}

## Undeployment Order

When it is time to undeploy dataApp, we would like the feeder to undeploy first, and only then the space. This would reduce the number of false warnings in the feeder complaining that the space is not available. The command `dataApp.undeployAndWait(3, TimeUnit.MINUTES)` un deploys the application in reverse dependency order and waits for at most 3 minutes for all processing unit instances to gracefully shutdown.

In the example above, the feeder instance will complete the teardown of all the spring beans, and only then the space would undeploy.

# Monitoring Deployment Progress


The deployment progress can be monitored using the events provided by the Admin API. There are 4 provision events on a processing unit instance:

1. `ProvisionStatus#ATTEMPT` - an attempt to provision an instance on an available `GridServiceContainer`
1. `ProvisionStatus#SUCCESS` - a successful provisioning attempt on a `GridServiceContainer`
1. `ProvisionStatus#FAILURE` - a failed attempt to provision an instance on an available `GridServiceContainer`
1. `ProvisionStatus#PENDING` - a pending to provision an instance until a matching `GridServiceContainer` is discovered


```java
Admin admin = new AdminFactory().create();
admin.getProcessingUnits().getProcessingUnitInstanceProvisionStatusChanged().add( listener )
//or
admin.getProcessingUnits().getProcessingUnit("xyz").getProcessingUnitInstanceProvisionStatusChanged( listener )
//or
admin.addEventListener(new ProcessingUnitInstanceProvisionStatusChangedEventListener() {
   @Override
   public void processingUnitInstanceProvisionStatusChanged(
		ProcessingUnitInstanceProvisionStatusChangedEvent event) {
       ProvisionStatus newStatus = event.getNewStatus();
       ...
   }
});
```

A compound listener (implements several interfaces) can be registered using the `Admin.addEventListener(...)`.

# Monitoring Processing Unit instance Fault-detection

Using the member-alive-indicator (see [Monitoring the Liveness of Processing Unit Instances]({{%currentadmurl%}}/the-sla-overview.html)  ) the Grid Service Manager (GSM) actively monitors each processing unit instance. When an "is alive" check fails, it **suspects** that the processing unit instance is no longer alive, and retries to contact it (using the configured retries and timeouts in pu.xml under `os-sla:member-alive-indicator`). When all retries fail, the GSM reports that it **detected** a failure and tries to re-deploy it on an available Grid Service Container (GSC).

These member-alive-indicator transitions are reflected using the Admin API `MemberAliveIndicatorStatus`. Use the API to register for status changed events, and better visibility of GSM decisions based on the fault-detection mechanism. An alert is fired upon a fault- detection trigger, also visible in Web User Interface.

The `MemberAliveIndicatorStatus`  has three states: ALIVE, SUSPECTING and FAILURE. The transition from SUSPECTING to FAILURE is final. From this state the processing unit instance is considered not alive. The transition from SUSPECTING back to ALIVE can occur if one of the retries succeeded in contacting the processing unit instance.


```java
Admin admin = new AdminFactory().create();
admin.getProcessingUnits().getProcessingUnitInstanceMemberAliveIndicatorStatusChanged().add( listener )
//or
admin.getProcessingUnits().getProcessingUnit("xyz").getProcessingUnitInstanceMemberAliveIndicatorStatusChanged( listener )
//or
admin.addEventListener(new ProcessingUnitInstanceMemberAliveIndicatorStatusChangedEventListener() {
   @Override
   public void processingUnitInstanceMemberAliveIndicatorStatusChanged (
	ProcessingUnitInstanceMemberAliveIndicatorStatusChangedEvent event) {
       MemberAliveIndicatorStatus newStatus = event.getNewStatus();
       ...
   }
});
```

A compound listener (implements several interfaces) can be registered using the `Admin.addEventListener(...)`.
