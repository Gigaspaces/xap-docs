---
type: post
title:  Dynamic Polling Container Templates using TriggerOperationHandler
categories: SBP
parent: data-access-patterns.html
weight: 300
---



{{% tip %}}
**Summary:**  This article illustrates how to use TriggerOperationHandler <br/>
**Author**: Shravan (Sean) Kumar, Solutions Architect, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 9.0.0<br/>


{{% /tip %}}

# Overview

Polling Container is one of the most powerful and commonly used feature of GigaSpaces when processing data. To recap, Polling Containers perform a polling receive operation against the space. If a receive operation succeeds (a value is returned from the receive operation), the Data Event Listener is invoked with the event. The receive operation is performed using a static template. For most use cases a static template or a static SQL query (parameters are constant) is sufficient. Examples include: Receive any Order that is marked as "UN_PROCESSED", receive any Order where customer name is like "VIPCustomer", etc.

There are always use cases where you need dynamic templates. 

Some examples: 

1. Processing messages that are older than an hour
1. Processing an order only after all the items in an order are in the space
1. Processing messages in a certain order

Each of these examples need a query that lacks all parameters at configuration time. `TriggerOperationsHandler` helps achieve this behavior.

The [Polling Container]({{%latestjavaurl%}}/polling-container.html) shows where `TriggerOperationsHandler` fits into the Polling Container Life Cycle. Polling Container invokes the `TriggerOperationsHandler.triggerReceive()` method before invoking the `ReceiveHandler` which does the actual take and this is the perfect extension point where you can customize or modify the template.

# Example

Below is an example that shows how you can use `TriggerOperationsHandler` to process the Message with highest priority (assuming id is the priority) across the cluster and process them in same partition where the `Message` object resides. It is based on helloworld example which is included with GigaSpaces XAP. Using a default Polling container template this will not be possible, but using a custom `TriggerOperationHandler` you can achieve this.

`MyTrigger` implementation is shown below,


```java
public class MyTrigger implements TriggerOperationHandler {

	private GigaSpace clusteredGigaSpace;

	@Override
	public Object triggerReceive(Object t, GigaSpace gigaSpace,
			long receiveTimeout) throws DataAccessException {

		// Make the thread wait for new data with a blocking read with timeout.
		// Otherwise trigger operation handler will keep getting invoked in a
		// tight loop
		Message template = new Message();
		template.setInfo("Hello ");
		Message newMsg = gigaSpace.read((Message) template, 60000);

		if (newMsg != null) {
			SQLQuery<Message> query = new SQLQuery<Message>(Message.class,
					"processed = false ORDER BY id DESC");

			Message localObject = (Message) gigaSpace.read(query);

			// If there is an object matching the template, validate if this is
			// right priority
			if (localObject != null) {

				Message clusteredObject = clusteredGigaSpace.read(query);

				if (clusteredObject != null
						&& localObject.getId().equals(clusteredObject.getId())) {
					return localObject;
				}
			}
		}
		return null;
	}

	@Override
	public boolean isUseTriggerAsTemplate() {
		return true;
	}

	public GigaSpace getClusteredGigaSpace() {
		return clusteredGigaSpace;
	}

	public void setClusteredGigaSpace(GigaSpace clusteredGigaSpace) {
		this.clusteredGigaSpace = clusteredGigaSpace;
	}

}
```

`MyTrigger` runs a cluster wide query and will need clustered proxy which is injected from the pu.xml. Another useful feature of `TriggerOperationHandler` is ability to pass the template that the receive operation handler uses for performing the take. As you can see above the `isUseTriggerAsTemplate` returns a boolean flag to indicate that the receive operation handler should use the template returned by `MyTrigger` to perform the take.

pu.xml snippet below shows how MyTrigger is configured on the polling container,


```java
    <os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

    <os-core:giga-space id="clusteredGigaSpace" clustered="true" space="space" tx-manager="transactionManager"/>

    <!--
        The processor bean
    -->
    <bean id="helloProcessor" class="org.openspaces.example.helloworld.processor.Processor"/>

    <os-events:polling-container id="helloProcessorPollingEventContainer"
	    giga-space="gigaSpace">
		<os-events:tx-support tx-manager="transactionManager"/>
		<os-events:trigger-operation-handler>
			<bean class="org.openspaces.example.helloworld.common.MyTrigger">
				<property name="clusteredGigaSpace" ref="clusteredGigaSpace"/>
			</bean>
		</os-events:trigger-operation-handler>
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
```

Notice the clustered proxy being passed to MyTrigger as a property.

# Getting the project
Example project is held on github in the [best practices](https://github.com/Gigaspaces/bestpractices) project. This is an umbrella repository; the specific project is in the [helloTriggerHandler](https://github.com/Gigaspaces/bestpractices/tree/master/helloTriggerHandler) directory under the root.

You can run this example just as how you would run helloworld example using the included ant build scripts. Be sure to use a cluster with at least 2 partitions when testing this.
