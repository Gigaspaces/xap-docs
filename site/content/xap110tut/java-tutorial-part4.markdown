---
type: post110tut
title:  Events and Messaging
categories: XAP110TUT
weight: 800
parent: none
---

{{%section%}}
{{%column width="10%" %}}
{{<wbr>}}
![cassandra.png](/attachment_files/qsg/Events-Message.png)
{{%/column%}}
{{%column width="90%" %}}
{{% ssummary   %}} {{% /ssummary %}}
{{%/column%}}
{{%/section%}}

The Space's Messaging and Events support provides messaging handlers that simplify event driven programming.



Events are generated when objects are written, updated or taken from the space. With this framework you select events based on its content and designate a method that would be triggered as a result of that event, all through a simple and non-intrusive configuration. There are three main event handlers that are available:


# Notify Container
The Notify Container is the equivalent of a publish/subscribe semantics. An event listener is registered with the space that declares the type of events it is interested in. Upon receiving an event the listener is triggered. In this case all registered matching subscribers will be notified at the same time. 
 
 
# Polling Container
The Polling Container is the equivalent of a point to point paradigm. Unlike the notify container, the Polling Container blocks contentiously on space connection until a matching event arrives. Polling containers ensures that one and only one listener will be triggered per event even if there are more then one listener that matches that event. The entry will be removed from the space when the listener is invoked.

# Archive Container
The archive container is a mechanism built on top of a polling container to transfer historical data into Big-Data storage (for example Cassandra). The typical scenario is when streaming vast number of raw events through the Space, enriching them and then moving them to a Big-Data storage. Typically, there is no intention of keeping them in the space nor querying them in the space.

{{%refer%}}[The Archive Container]({{%currentjavaurl%}}/archive-container.html){{%/refer%}}

 

# Example
Lets take our online processing service; we want to create a notification container that receives events when a payment has been cancelled. 
First, we define the Notifier Container.


```java
@EventDriven
@Notify
public class PaymentListener {

     @EventTemplate
     Payment unprocessedData() {
        Payment template = new Payment();
        template.setStatus(ETransactionStatus.CANCELLED);
        return template;
     }

    @SpaceDataEvent
    public Payment eventListener(Payment event) {
        // process Payment
        return null;
    }
}
```



In this example we define:

|   |    |
|----|----|
|Annotation | Description|
|:----------|:-----------|
|@EventDriven @Notify| the listener as a notification listener |
|@EventTemplate|       the event that will trigger the listener; a payment that was cancelled|
|@SpaceDataEvent|      the method that processes the event arrives. The return value is null, nothing will be written back into the space|



By default all events will trigger the notification. In our example we are restricting the events to be received by using the `@NotifyType` annotation. In our example we are only interested in write and update events.


You can also define an event template using an SQLQuery.


```java
@EventTemplate
SQLQuery<Payment> unprocessedData() {
    SQLQuery<Payment> template = new SQLQuery<Payment>(Payment.class,"status = ?");
    template.setParameter(1, ETransactionStatus.CANCELLED);

    return template;
}
```


Now we are ready to register the container with the space:


```java
public void registerNotifierListener() {
     SimpleNotifyEventListenerContainer notifyEventListenerContainer = new SimpleNotifyContainerConfigurer(
	      space).eventListenerAnnotation(new PaymentListener())
	     .notifyContainer();
     notifyEventListenerContainer.start();
}
```

And with Java8 lambda syntax:


```java
public void registerNotifierListener() {
    Payment template = new Payment();
    template.setStatus(ETransactionStatus.CANCELLED);

	SimpleNotifyEventListenerContainer notifyEventListenerContainer = new SimpleNotifyContainerConfigurer(space)
		.template(template)
		.eventListener((data, gigaSpace, txStatus, source) -> {
			System.out.println("Got matching event! - " + (Payment)data);
			// process Payment
		})
		.notifyContainer();
     notifyEventListenerContainer.start();
}
```

You can now execute a simple test that writes a Payment object into the space that will trigger the notification event:




```java
public void notifyTest() {
     Payment payment = new Payment();
     payment.setStatus(ETransactionStatus.CANCELLED);
     space.write(payment);
}
```


Lets assume we want to implement a polling container that receives audit notifications.  Here is an example on how you would define a polling listener:


```java
@EventDriven
@Polling
@NotifyType(write = true, update = true)
public class AuditListener {
	@EventTemplate
	Payment unprocessedData() {
		Payment template = new Payment();
		template.setStatus(ETransactionStatus.AUDITED);
		return template;
	}

	@SpaceDataEvent
	public Payment eventListener(Payment event) {
		// process Payment
		System.out.println("Polling Received a payment:"+i);
		return null;
	}
}
```

And now we register the polling listener:


```java
	public void registerPollingListener() {
		SimplePollingEventListenerContainer pollingEventListenerContainer = new SimplePollingContainerConfigurer(space)
			.eventListenerAnnotation(new AuditListener())
			.pollingContainer();
		pollingEventListenerContainer.start();
	}
```

The same can be accomplished with Java8 lambda syntax:


```java
	public void registerPollingListener() {
		Payment template = new Payment();
   		template.setStatus(ETransactionStatus.AUDITED);

		SimplePollingEventListenerContainer pollingEventListenerContainer = new SimplePollingContainerConfigurer(space)
			.template(template)
			.eventListener((data, gigaSpace, txStatus, source) -> {
				System.out.println("Got matching event! - " + (Payment)data);
				// process Payment
			}
			.pollingContainer();
		pollingEventListenerContainer.start();
	}
```


{{%refer%}}[Event Processing]({{%currentjavaurl%}}/event-processing.html){{%/refer%}}

# FIFO Support
Sometimes it is necessary to process events in the order the way they have been created. By default events are not ordered. XAP supports FIFO (First In, First Out) processing of events.  To enable FIFO operations you can turn on FIFO support for classes which will participate in such operations.    

Here is an example how you can enable FIFO support for a class using the `@SpaceClass` annotation:


```java
@SpaceClass(fifoSupport=FifoSupport.OPERATION)
public class Payment implements Serializable {

	private static final long serialVersionUID = 1L;
	private String paymentId;
	private Integer payingAccountId;
}
```

{{%refer%}}[FIFO Support]({{%currentjavaurl%}}/fifo-support.html){{%/refer%}}




 

# JMS
In addition to the polling containers you can also use a JMS facade on top of the space to deliver events. The JMS facade is designed to enable integration with external feeders that cannot or were not designed to work with the space based API. 

{{%refer%}}[Messaging Support]({{%currentjavaurl%}}/messaging-support.html){{%/refer%}}

 


# Master Worker Pattern
{{%section%}}
{{%column width="85%" %}}
The Master-Worker Pattern (sometimes called the Master-Slave or the Map-Reduce pattern) is used for parallel processing. It follows a simple approach that allows applications to perform simultaneous processing across multiple machines or processes via a `Master` and multiple `Workers`.
In GigaSpaces XAP, you can implement the Master-Worker pattern using several methods:

- [Task Executors](/sbp/map-reduce-pattern-executors-example.html) - best for a scenario where the processing activity is collocated with the data (the data is stored within the same space as the tasks being executed).
- [Polling Containers]({{%currentjavaurl%}}/polling-container.html) - in this case the processing activity runs in a separate machine/VM from the space. This approach should be used when the processing activity consumes a relatively large amount of CPU and takes a large amount of time. It might also be relevant if the actual data required for the processing is not stored within the space, or the time it takes to retrieve the required data from the space is much shorter than the time it takes to complete the processing.

{{%/column%}}
{{%column width="15%" %}}

{{%popup   "/attachment_files/qsg/the_master_worker.jpg"%}}

{{%/column%}}
{{%/section%}}


{{%refer%}}
[Services & Best Practices, Master Worker Pattern](/sbp/master-worker-pattern.html)
{{%/refer%}}



