---
type: post
title:  Mule ESB Example
categories: SBP
parent: processing.html
weight: 600
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim <br>Rafi Pinto| 8.0.1| March 2011| Mule: 3.1.0  |    |





# Overview
GigaSpaces [Mule ESB]({{%latestjavaurl%}}/mule-esb.html) OpenSpaces comes with comprehensive support for Mule 3.1.0. It allows you to use the [Space as a Mule external transport]({{%latestjavaurl%}}/mule-event-container-transport.html), enabling receiving and dispatching of POJO messages over the Space.

This [example](/attachment_files/sbp/Mule_Multi_service_Example.zip) illustrates distributed multi services mule application. The different mule services construct a simple workflow, where each service passes its outbound data into the next service in line via the Space. Each Service can run in a different machine(s) and [scale dynamically](#scale-dynamically) or [manually](#scale-manually) in a different manner.

{{% tip %}}
Make sure you have the [libraries required](#libraries-required) located at the correct location before running the example.
Alternatively if you are using mule 3.5 you can create you project with the maven template mule-3.5, it will be created with all the mule jars inside the pu jar so no need to add any required libraries.
{{% /tip %}}

Here are the example components:

- A Data-Grid with 2 partitions and one backup for each partition.
- A Feeder pushing Data objects with state=0 into the space.
- A Verifier Service consuming state=0 objects and moving these into state=1
- An Approver Service consuming state=1 objects and moving these into state=2
- A Monitor Service used to Scale Up and Down the Verifier Service in a Dynamic Manner.

{{% align center %}}
![mule_example_flow1.jpg](/attachment_files/sbp/mule_example_flow1.jpg)
{{% /align %}}

The example using the polling container as the inbound-endpoint with each Service and a space connector as the outbound-endpoint where a state field within the [Data](#the-space-data-class) object acts as the workflow "Queue".

Each service with this example is packaged within its own [Processing Unit]({{%latestjavaurl%}}/mule-processing-unit.html) and can scale independently in [dynamic manner](#scale-dynamically).

{{% exclamation %}} The [Mule Queue Provider]({{%latestjavaurl%}}/mule-queue-provider.html) is not illustrated with this example. It is relevant for different use case where the Mule Services scales together with the Data-Grid and running within the same Processing Unit.

# The Space Data Class
The Space Data Class is the POJO used as the "Queue" to coordinate the activities between the different services. Data Objects are consumed from the Data-Grid and written back into the Data-Grid by each Service. Here is the Data Space class definition:


```java
@SpaceClass
public class Data implements Serializable {
    private String id;
    private Long type;
    private Integer state;
    @SpaceId(autoGenerate=true)
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    @SpaceRouting
    public Long getType() {return type;}
	@SpaceProperty(index=IndexType.BASIC)
	public Integer getState() {return state;}
    public void setState(Integer state) {this.state= state;}
}
```

- The `type` field is the routing field used to load-balance state object across the different IMDG partitions.
- The `state` field is the "Queue" used to determine the state of the Data object. This field is modified when the Data object is passing between the different Services. This field is indexed to allow fast search of matching objects when the different services consume these from the IMDG.

# The Feeder Service
This service pushing data objects into the Data-Grid in periodic manner. These will be consumed by the Verifier Service. Here is the Feeder Service:


```java
public class Feeder  {
    private long numberOfTypes = 10;
    private static  long counter = 1;
    public void setNumberOfTypes(long num) {
        numberOfTypes = num;
    }
    /****
     * Triggered by mule quartz squeduling service to write new Data object to the space.
     */
    public Data feed() {
		long time = System.currentTimeMillis();
		Data data = new Data((counter++ % numberOfTypes), "FEEDER " + Long.toString(time));
		data.setState(0);
		System.out.println("--- FEEDER WROTE " + data);
		return data;
    }
}
```

## The Feeder Service Configuration
{{%accordion%}}
{{%accord title="**Click to view the Feeder Service Configuration..**"%}}
Here is the Feeder Service Configuration:


```java
<mule ...
    <spring:beans>
        <os-core:space id="space" url="jini://**/**/space"/>
        <os-core:giga-space id="gigaSpace" space="space"/>
    </spring:beans>
    <os-eventcontainer:connector name="gigaSpacesConnector" />
    <model name="feeder">
        <service name="DataFeederUMO">
            <inbound>
                <quartz:inbound-endpoint jobName="feedJob" repeatInterval="1000">
                    <quartz:event-generator-job/>
                </quartz:inbound-endpoint>
            </inbound>
            <component class="com.mycompany.app.feeder.Feeder"/>
			<outbound>
				<pass-through-router>
					<os-eventcontainer:outbound-endpoint giga-space="gigaSpace"/>
				</pass-through-router>
			</outbound>
        </service>
    </model>
</mule>
```

{{%/accord%}}
{{%/accordion%}}

# The Verifier Service
This service consumes Data objects with state=0 and transfer these into state=1.


```java
public class Verifier {

    public Data verify(Data data) throws Exception {
        System.out.println("----- Verifier got Data: " + data);
        data.setRawData(data.getRawData() + "-Verified");
		data.setState(1);
        return data;
    }
}
```

## The Verifier Service Configuration
The Verifier Service Configuration using a `polling-container` running in a [None-Blocking mode]({{%latestjavaurl%}}/polling-container.html#Non-Blocking+Receive+Handler). This allows the Service to consume Data objects from any partition.

{{%accordion%}}
{{%accord title="**Click to view the Verifier Service Configuration..**"%}}


```java
<mule
...
    <spring:beans>
        <os-core:space id="space" url="jini://**/**/space"/>
        <os-core:giga-space id="gigaSpace" space="space"/>
        <os-events:polling-container id="dataProcessorPollingEventContainer" giga-space="gigaSpace" receive-timeout="10000">
			<os-events:receive-operation-handler>
				<spring:bean class="org.openspaces.events.polling.receive.SingleTakeReceiveOperationHandler">
					<spring:property name="nonBlocking" value="true" />
					<spring:property name="nonBlockingFactor" value="10" />
				</spring:bean>
			</os-events:receive-operation-handler>

            <os-core:template>
                <spring:bean class="com.mycompany.app.common.Data">
                    <spring:property name="state" value="0"/>
                </spring:bean>
            </os-core:template>
        </os-events:polling-container>
    </spring:beans>
    <os-eventcontainer:connector name="gigaSpacesConnector"/>
    <model name="ProcessingModel">
	    <service name="Verifier">
            <inbound>
                <os-eventcontainer:inbound-endpoint address="os-eventcontainer://dataProcessorPollingEventContainer"/>
            </inbound>
            <component class="com.mycompany.app.processor.Verifier"/>

			<outbound>
                <pass-through-router>
                    <os-eventcontainer:outbound-endpoint giga-space="gigaSpace"/>
                </pass-through-router>
            </outbound>
        </service>
    </model>
</mule>
```

{{%/accord%}}
{{%/accordion%}}

# The Approver Service
This service consumes Data objects with state=1 and transfer these into state=2.


```java
public class Approver {
    public Data approve(Data data) throws Exception {
        System.out.println("----- Approver got Data: " + data);
        data.setRawData(data.getRawData() + "-Approved");
		data.setState(2);
        return data;
    }
}
```

## The Approver Service Configuration
The Approver Service Configuration using a `polling-container` running in a [None-Blocking mode]({{%latestjavaurl%}}/polling-container.html#Non-Blocking+Receive+Handler). This allows the Service to consume Data objects from any partition.

{{%accordion%}}
{{%accord title="**Click to view the Approver Service Configuration..**"%}}


```java
<mule
...
    <spring:beans>
        <os-core:space id="space" url="jini://**/**/space"/>
        <os-core:giga-space id="gigaSpace" space="space"/>
        <os-events:polling-container id="dataProcessorPollingEventContainer" giga-space="gigaSpace" receive-timeout="10000">
			<os-events:receive-operation-handler>
				<spring:bean class="org.openspaces.events.polling.receive.SingleTakeReceiveOperationHandler">
					<spring:property name="nonBlocking" value="true" />
					<spring:property name="nonBlockingFactor" value="10" />
				</spring:bean>
			</os-events:receive-operation-handler>

            <os-core:template>
                <spring:bean class="com.mycompany.app.common.Data">
                    <spring:property name="state" value="1"/>
                </spring:bean>
            </os-core:template>
        </os-events:polling-container>
    </spring:beans>
    <os-eventcontainer:connector name="gigaSpacesConnector"/>
    <model name="ProcessingModel2">
		<service name="Approver">
            <inbound>
                <os-eventcontainer:inbound-endpoint address="os-eventcontainer://dataProcessorPollingEventContainer"/>
            </inbound>
            <component class="com.mycompany.app.processor.Approver"/>
            <outbound>
                <pass-through-router>
                    <os-eventcontainer:outbound-endpoint giga-space="gigaSpace"/>
                </pass-through-router>
            </outbound>
		</service>
    </model>
</mule>
```

{{%/accord%}}
{{%/accordion%}}

# Building the Example

Step 1. Have the correct maven bin folder (located at `\gigaspaces-xap-premium-10.0.0\tools\maven\apache-maven\bin`) as part of the `PATH`.
Step 2. Have the GigaSpaces maven plug-in installed. See the [Maven Plugin]({{%lateststartedurl%}}/installation-maven-overview.html) for instructions how to install it.
In general to install the GigaSpaces maven plug-in you should run the following:


```java
Windows:
\gigaspaces-xap-premium-10.0.0\tools\maven>installmavenrep.bat
Unix:
\gigaspaces-xap-premium-10.0.0\tools\maven>installmavenrep.sh
```

Step 3.
If you are using mule 3.3:
Download the [example package](/attachment_files/sbp/Mule_Multi_service_Example.zip) and extract it into `Gigaspaces Root\tools\maven`. Once extracted you will have the following folders under `Gigaspaces Root\tools\maven\my-app`:

- common
- feeder
- approver
- verifier
- monitor

for mule 3.5:
 1. create your project with the cmd: 

```java
\mvn os:create -DartifactId=<your project name> -Dtemplate=mule-3.5
```

Step 4. To build the example run the following command:


```java
\gigaspaces-xap-premium-10.0.0\tools\maven\my-app>mvn
```

Once the example libraries will be successfully created, you will be able to deploy the example.

# Deploying the Example
In order to deploy the different Processing unit comprising this example:

- Make sure you have the [libraries required](#libraries-required) located at the correct location.
- Start at least one GSM and several GSCs in the same machine on multiple different machines.
- Use the following commands to deploy the Processing unit libraries:


```bash
\gigaspaces-xap-premium-10.0.0\bin\gs pudeploy -cluster schema=partitioned-sync2backup 
	total_members=2,1 -properties embed://dataGridName=space -max-instances-per-vm 1
	-override-name space /templates/datagrid

\gigaspaces-xap-premium-10.0.0\bin\gs pudeploy ..\tools\maven\my-app\verifier\target\my-app-verifier.jar
\gigaspaces-xap-premium-10.0.0\bin\gs pudeploy ..\tools\maven\my-app\approver\target\my-app-approver.jar
\gigaspaces-xap-premium-10.0.0\bin\gs pudeploy ..\tools\maven\my-app\feeder\target\my-app-feeder.jar
```

with mule 3.5 you can deploy from you project with the cmd:

```bash
\mvn os:deploy -Dgroups=<your jini group>
```
{{% exclamation %}} You may find the different Processing Unit libraries under the `target` folder of each Processing Unit.

Once the different processing will be deployed you should have the following displayed as part of the GS-UI:

{{% align center %}}
![mule_deploy.jpg](/attachment_files/sbp/mule_deploy.jpg)
{{% /align %}}

{{% align center %}}
![mule_grid.jpg](/attachment_files/sbp/mule_grid.jpg)
{{% /align %}}


You may check the statistics you review the Write and Take operations called by the different Services when interacting with the Data-Grid.

# Scale Manually
You may have multiple instances of each service running. Running multiple instances of the Feeder service will push more Data objects into the Space. Running multiple services of the Approver or Verifier will consume relevant objects faster.

{{% align center %}}
![mule_example_flow2.jpg](/attachment_files/sbp/mule_example_flow2.jpg)
{{% /align %}}

In order to increase the amount of the deployed services (Feeder, Approver or Verifier) , select the relevant processing unit and click the Increase button. See example below:


{{% align center %}}
![mule_scale_manual.jpg](/attachment_files/sbp/mule_scale_manual.jpg)
{{% /align %}}

A new instance of the relevant service will be created at of the existing running GSCs.

# Scale Dynamically
In many cases you might want to scale the mule services in a dynamic manner. Dynamic Scaling means increasing or decreasing the amount of active Mule services while the system is running to be able to consume/process incoming data faster/slower.

The example includes a special processing Unit - The `Monitor` PU. This service monitors Data objects within the space with a specific state field value and increase/decrease the amount of the relevant Service instances while the system is running. You may run for example several `Feeder` instances to push more Data objects into the IMDG. In order there would not be generated a backlog of too many Data objects with `state=0` the `Monitor` Service will increase the amount of `Verifier` services. Once there is small amount of Data objects with `state=0` the `Monitor` service will decrease the amount of `Verifier` services instances running to maintain only one `Verifier` service instance running.

The Monitor Service includes the following properties:


|Property Name|Description|Default Value|
|:------------|:----------|:------------|
|monitoredPU| Service Name to Monitor and Scale dynamically |my-app-verifier|
|highLimit| When to Scale Up. Once the Average amount of Data Objects will be above this value, the amount of monitored services will be increased by one. |50|
|lowLimit| When to Scale Down. Once the Average amount of Data Objects will be below this value, the amount of monitored services will be decreased by one.|10|
|state| Data object state value to monitor. The Monitor counting periodically the amount of Data objects matching the specified state value.|0|
|maxInstances| Maximum Amount of service instances. The Monitor service will scale up the monitored service up to this amount of instances.|5|
|minInstances| Minimum Amount of service instances. The Monitor service will scale down the monitored service up to this amount of instances.|1|
|sleeptimeBeforeNextCheckAfterScaleup| Time in ms to wait after scaling up before a check is done to evaluate the need to scale the monitored service again.|5000|
|sleeptimeBeforeNextCheckAfterScaledown| Time in ms to wait after scaling down before a check is done to evaluate the need to scale the monitored service again.|5000|
|period| Duration in ms before the next check will be triggered|2000|

The Monitor Service using the following Classes to implement the dynamic scaling behavior:

- [org.openspaces.admin.Admin](http://www.gigaspaces.com/docs/JavaDoc{{%latestxaprelease%}}/org/openspaces/admin/Admin.html) -  The main interface for accessing Admin API. Created using the AdminFactory class. It provides access to the main elements in the Admin API and in GigaSpaces such as the GridServiceAgents, LookupServices, GridServiceManagers, GridServiceContainers, ProcessingUnits, and Spaces.
- [org.openspaces.admin.AdminFactory](http://www.gigaspaces.com/docs/JavaDoc{{%latestxaprelease%}}/org/openspaces/admin/AdminFactory.html) - This class should be used to access all GigaSpaces Administration classes.
- [org.openspaces.admin.pu.ProcessingUnit](http://www.gigaspaces.com/docs/JavaDoc{{%latestxaprelease%}}/org/openspaces/admin/pu/ProcessingUnit.html) - This class provides methods to manipulate the life cycle and mange a running Proecssing Unit. You may Increment , Decrement the amount of processing unit instances associated with this PU.

{{% exclamation %}} See the [Administration and Monitoring API]({{%latestjavaurl%}}/administration-and-monitoring-api.html) for more details about the GigaSpaces Administration API.

## Deploying the Monitor Service
In order to deploy the Monitor Service run the following:


```java
\gigaspaces-xap-premium-10.0.0\bin\gs pudeploy ..\tools\maven\my-app\monitor\target\my-app-monitor.jar
```

## Testing Dynamic Scalability
To see how the Verifier Service scale up, deploy the Feeder with 2 instances:


```java
\gigaspaces-xap-premium-10.0.0\bin\gs pudeploy -cluster total_members=2 ..\tools\maven\my-app\feeder\target\my-app-feeder.jar
```

The Monitor Service will increment the amount of verifier instances when there will be 50 ,100, 150 and 200 Data objects with `state=0` within the IMDG.

{{% align center %}}
![mule_scale_u.jpg](/attachment_files/sbp/mule_scale_u.jpg)
{{% /align %}}

You can Query the IMDG via the Query view for Data objects with `state=0` using the following Query:


```java
select count(*) from com.mycompany.app.common.Data WHERE state='0'
```

To scale Down the verifier Service undeploy the Feeder PU.


```java
\gigaspaces-xap-premium-10.0.0\bin\gs undeploy my-app-feeder
```

The Monitor Service will decrement the amount of verifier instances when there will be 50 ,40 30 and 20 Data objects with `state=0` within the IMDG.

{{% align center %}}
![mule_scale_d.jpg](/attachment_files/sbp/mule_scale_d.jpg)
{{% /align %}}

# Transaction Support
You may add [transaction support]({{%latestjavaurl%}}/mule-event-container-transport.html#Transaction+Support) to the Mule Service by adding the `distributed-tx-manager` and the `tx-support` tags. Since we are using a clustered space we will be using the [Distributed Jini Transaction Manager|XAP8:Transaction Management#Distributed Jini Transaction Manager].

{{% exclamation %}} See also the [Polling Container Transaction Support]({{%latestjavaurl%}}/polling-container.html#Transaction+Support) for additional details.

See below example:


```java
<mule ....
    <spring:beans>
        <os-core:space id="space" url="jini://**/**/space"/>
		<os-core:distributed-tx-manager id="transactionManager" default-timeout="30"/>
		<os-core:giga-space id="gigaSpace" tx-manager="transactionManager" space="space"/>
		<context:component-scan base-package="com.mycompany.app.processor"/>

	    <spring:bean id="transactionFactory"
	                 class="org.mule.module.spring.transaction.SpringTransactionFactory">
	        <spring:property name="manager">
	            <spring:ref bean="transactionManager"/>
	        </spring:property>
	    </spring:bean>

        <os-events:polling-container id="dataProcessorPollingEventContainer" giga-space="gigaSpace" receive-timeout="10000">
			<os-events:tx-support tx-manager="transactionManager"/>
			<os-events:receive-operation-handler>
				<spring:bean class="org.openspaces.events.polling.receive.SingleTakeReceiveOperationHandler">
					<spring:property name="nonBlocking" value="true" />
					<spring:property name="nonBlockingFactor" value="10" />
				</spring:bean>
			</os-events:receive-operation-handler>

            <os-core:template>
                <spring:bean class="com.mycompany.app.common.Data">
                    <spring:property name="state" value="0"/>
                </spring:bean>
            </os-core:template>
        </os-events:polling-container>

    </spring:beans>

    <os-eventcontainer:connector name="gigaSpacesConnector"/>

    <model name="ProcessingModel">
	    <service name="Verifier">
            <inbound>
                <os-eventcontainer:inbound-endpoint address="os-eventcontainer://dataProcessorPollingEventContainer">
                   <custom-transaction factory-ref="transactionFactory" action="BEGIN_OR_JOIN" timeout="30" />
				</os-eventcontainer:inbound-endpoint>
            </inbound>
            <component class="com.mycompany.app.processor.Verifier"/>

		   <outbound>
				<pass-through-router>
					<os-eventcontainer:outbound-endpoint giga-space="gigaSpace">
						<custom-transaction factory-ref="transactionFactory" action="BEGIN_OR_JOIN" timeout="30" />
					</os-eventcontainer:outbound-endpoint>
				</pass-through-router>
			</outbound>
        </service>
    </model>
</mule>
```

{{%warning%}}
Time based Parameters Units:

- The **default-timeout** parameter and the **custom-transaction timeout** parameter are in second units.
- The receive-timeout , commit and abort timeout , lookup-timeout , duration and round-trip-time parameters are in millisecond units.
{{%/warning%}}

# Libraries Required
The [attached libraries](/attachment_files/sbp/mule-jars.zip) should be located at your `GigaSpaces Root\lib\platform\mule` folder before deploying the example. See below fill list of the libraries required to run this example:


```java
backport-util-concurrent-3.1-osgi.jar
commons-beanutils-1.8.0.jar
commons-collections-3.2.1.jar
commons-io-1.4.jar
commons-lang-2.4.jar
commons-pool-1.5.3.jar
jug-2.0.0-osgi-asl.jar
mule-core-3.1.0.jar
mule-module-annotations-3.1.0.jar
mule-module-client-3.1.0.jar
mule-module-spring-config-3.1.0.jar
mule-module-spring-extras-3.1.0.jar
mule-transport-quartz-3.1.0.jar
mule-transport-stdio-3.1.0.jar
mule-transport-vm-3.1.0.jar
quartz-all-1.6.0-osgi.jar
```

