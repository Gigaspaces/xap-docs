---
type: post
title:  Refreshable Business Logic Example
categories: SBP
weight: 2200
parent: production.html
---
 

|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
|Shravan (Sean) Kumar,| 7.1.3|   |           | {{%download "/attachment_files/sbp/refreshable-prototype.zip" %}}        |


# Overview

Service Reloading [page]({{%latestjavaurl%}}/reloading-business-logic.html) explains how Refreshable context can be used to reload business logic. Find a modified version of Helloworld example which is included in the GigaSpaces installation configured for refreshable business logic.

# Source Code

Processor code from the Helloworld example is modified to implement Spring InitializingBean and DisplosableBean interfaces.

```java
public class Processor implements InitializingBean, DisposableBean {
    Logger logger=Logger.getLogger(this.getClass().getName());

    /**
     * Process the given Message and return it to the caller.
     * This method is invoked using OpenSpaces Events when a matching event
     * occurs.
     */
    @SpaceDataEvent
    public Message processMessage(Message msg) {
        logger.info("Processor PROCESSING: " + msg);
        msg.setInfo(msg.getInfo() + "World !!");
        return msg;
    }

    public Processor() {
        logger.info("Processor instantiated, waiting for messages feed...");
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("BEAN LOADED to the SPACE.");

	}

	@Override
	public void destroy() throws Exception {
		logger.info("BEAN DESTROYED.");

	}
}
```

The `pu.xml` for the processor is now split into two parts. The Space is defined in the pu.xml and refreshable business logic is defined as part of the refreshable-beans.xml file. You will also need a pu.properties file to disable the pu download.

{{%tabs%}}
 

{{%tab  "pu.xml" %}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:os-events="http://www.openspaces.org/schema/events"
       xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
       xmlns:os-sla="http://www.openspaces.org/schema/sla"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/sla/openspaces-sla.xsd">

	<!-- For injecting GigaSpaces automatically -->
	<os-core:giga-space-context/>

    <!--
        A bean representing a space (an IJSpace implementation).
    -->
    <os-core:space id="space" url="/./processorSpace">
		<os-core:filter-provider ref="remotingServiceExporter"/>
	</os-core:space>

    <!-- 
        Defines a local Jini transaction manager (will be used for transactional operations with the space).
    -->
    <os-core:local-tx-manager id="transactionManager" space="space"/>

    <!--
        A wrapper bean to the space to provide OpenSpaces simplified space API (built on top of IJSpace/JavaSpace).
    -->
    <os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

	<!-- reloadable beans go into this Spring configuration file -->
	<os-remoting:service-exporter id="remotingServiceExporter">
	    <os-remoting:service ref="refreshableBeans"/>
	</os-remoting:service-exporter>

	<os-core:refreshable-context-loader id="refreshableBeans"
              location="classpath:/META-INF/spring/refreshable-beans.xml"/>

    <os-sla:sla cluster-schema="partitioned-sync2backup"
    			number-of-instances="2"
    			number-of-backups="1"
                max-instances-per-vm="1">
    </os-sla:sla>
</beans>
```

{{% /tab %}}

{{% tab "refreshable-beans.xml" %}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:os-core="http://www.openspaces.org/schema/core"
	xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-sla="http://www.openspaces.org/schema/sla"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/sla/openspaces-sla.xsd">

	<os-core:giga-space-context />

	    <!--
        The processor bean
    -->
    <bean id="helloProcessor" class="org.openspaces.example.helloworld.processor.Processor"/>

    <!--
        A Polling Container bean that performs repeated take operations
        from the space of objects matching a defined template.
        (The take operations are by default blocking, which means a single
        take operation is waiting until a match is found)
        The template here instructs the polling container to take objects
        of type Message with their "info" attribute set to "Hello ".
        When a match is found, the object is taken and passed to a listener bean
        - here the listener is the previously defined Processor bean.
        This bean has the method processMessage(), which is invoked on the
        taken object, retuning a processed object.
        After the object is processed, it is written back to the space by the Polling Container.
    -->
    <os-events:polling-container id="helloProcessorPollingEventContainer" giga-space="gigaSpace">
    <os-events:tx-support tx-manager="transactionManager"/>
    <os-core:template>
        <bean class="org.openspaces.example.helloworld.common.Message">
            <property name="info" value="Hello "/>
        </bean>
    </os-core:template>
    <os-events:listener>
        <os-events:annotation-adapter>
            <os-events:delegate ref="helloProcessor"/>
        </os-events:annotation-adapter>
    </os-events:listener>
	</os-events:polling-container>

</beans>
```

{{% /tab %}}

{{% tab "pu.properties" %}}

```bash
pu.download=false
```

{{% /tab %}}

{{% /tabs %}}

# Running the Example

**Step 1.** Extract the [example](/attachment_files/sbp/refreshable-prototype.zip) archive into a folder. Navigate to the folder (calling it <refreshable-prototype>), modify the setDevEnv.bat file to have proper paths for GigaSpaces home, Java home and Ant home. Also modify the NIC_ADDR variable to have proper ip address. Run the startShell.bat script. This will open a command window.

**Step 2.** Build the example to use your GigaSpaces and Java versions using following,

```bash
 build dist
```

**Step 3.** Start a gs-agent using the provided script.

```bash
 gs-agent.bat
```

**Step 4.* Run the ant task "deploy-processor" to deploy the processor space.

```bash
 build deploy-processor
```

 Confirm that the processor space was deployed successfully using a gs-ui session.

**Step 5.** Run the ant task "run-feeder" to load data into the space. This will put 1000 `Data` objects into the space.

```bash
 build run-feeder
```

 You will see that the processor code that was used to process these messages by inspecting the log messages.

```bash
[gsc][2/6872]   2011-01-21 13:25:31,470 processor.1 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - Processor PROCESSING: id [144] info[Hello ]
[gsc][2/6872]   2011-01-21 13:25:31,473 processor.1 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - Processor PROCESSING: id [292] info[Hello ]
[gsc][2/6872]   2011-01-21 13:25:31,476 processor.1 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - Processor PROCESSING: id [258] info[Hello ]
```

**Step 6.** Make code changes to processor to simulate business logic changes. (You can use the newer version of Processor code provided in this file "<refreshable-prototype>\processor\src\org\openspaces\example\helloworld\processor\NewProcessorCode.txt".
 This version modifies the processor to log new messages to simulate business logic change).

**Step 7.** Run the ant task "build copy-processor-classes" to copy the new version of Processor bean to appropriate GigaSpaces folders.
**Step 8.** Run the refresh client using `refresh.bat` to reload the new classes. If everything worked fine you should see messages similar to below on the gs-agent window (or space logs),

```bash
[gsc][1/12464]  2011-01-21 13:30:29,971 processor.2 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - BEAN DESTROYED.
[gsc][2/6872]   2011-01-21 13:30:29,978 processor.1 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - BEAN DESTROYED.
[gsc][2/6872]   2011-01-21 13:30:30,180 processor.1 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - New Processor instantiated, waiting for messages feed...
[gsc][2/6872]   2011-01-21 13:30:30,246 processor.1 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - New BEAN LOADED to the SPACE.
[gsc][1/12464]  2011-01-21 13:30:30,340 processor.2 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - New Processor instantiated, waiting for messages feed...
[gsc][1/12464]  2011-01-21 13:30:30,389 processor.2 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - New BEAN LOADED to the SPACE.
```

**Step 9.** Run the feeder again and this time the `Data` should be processed using newer version of Processor Logic and log messages in the gs-agent window (or space logs) will look like below,

```bash
 [gsc][2/6872]   2011-01-21 13:31:21,906 processor.1 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - New Processor PROCESSING: id[132] info[Hello ]
[gsc][2/6872]   2011-01-21 13:31:21,908 processor.1 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - New Processor PROCESSING: id[104] info[Hello ]
[gsc][2/6872]   2011-01-21 13:31:21,915 processor.1 [1]/refreshableBeans INFO
[org.openspaces.example.helloworld.processor.Processor] - New Processor PROCESSING: id[102] info[Hello ]
```

