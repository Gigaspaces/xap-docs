---
type: post123
title:  Event-Driven Programming
categories: XAP123NET, PRM
parent: xapnet-basics.html
weight: 500
---




The Space's Messaging and Events support provides messaging handlers that simplify event driven programming. Events are generated when objects are written, updated or taken from the space. With this framework you select events based on its content and designate a method that would be triggered as a result of that event, all through a simple and non-intrusive configuration. There are two main event handlers that are available:


# Notify Container
The Notify Container is the equivalent of a publish/subscribe semantics. An event listener is registered with the space that declares the type of events it is interested in. Upon receiving an event the listener is triggered. In this case all registered matching subscribers will be notified at the same time. 
 
 
# Polling Container
The Polling Container is the equivalent of a point to point paradigm. Unlike the notify container, the Polling Container blocks contentiously on space connection until a matching event arrives. Polling containers ensures that one and only one listener will be triggered per event even if there are more then one listener that matches that event. The entry will be removed from the space when the listener is invoked.


 

# Example
Lets take our online processing service; we want to create a notification container that receives events when a payment has been cancelled. 
First, we define the Notifier Container.


```csharp
using System;

using GigaSpaces.Core.Events;
using GigaSpaces.XAP.Events.Notify;
using GigaSpaces.XAP.Events;

using xaptutorial.model;

[NotifyEventDriven(NotifyType = DataEventType.Write | DataEventType.Update)]
[TransactionalEvent]
public class PaymentListener {
	[EventTemplate]
	Payment unprocessedData() {
		Payment template = new Payment();
		template.Status=ETransactionStatus.CANCELLED;
		return template;
	}

	[DataEventHandler]
	public Payment eventListener(Payment ev) {
		// process Payment
		Console.WriteLine("Notifier Received a payment");
		return null;
	}
}
```



In this example we define:


|Annotation | Description|
|:----------|:-----------|
|\[NotifyEventDriven\]| the listener as a notification listener |
|NotifyType | type of operation that causes a notification|
|\[EventTemplate\]| the event that will trigger the listener; a payment that was cancelled|
|\[SpaceDataEvent\]| the method that processes the event arrives. The return value is null, nothing will be written back into the space|
|\[TransactionalEvent\]| under transaction |

By default all events will trigger the notification. In our example we are restricting the events to be received by using the `NotifyType` annotation. In our example we are only interested in write and update events.

You can also define an event template using an SQLQuery.


```csharp
[EventTemplate]
SqlQuery<Payment> unprocessedData() {
    SqlQuery<Payment> template = new SqlQuery<Payment>("Status = ?");
    template.SetParameter(1, ETransactionStatus.CANCELLED);

    return template;
}
```


Now we are ready to register the container with the space:


```csharp
public void registerNotifierListener() {

	NotifyEventListenerContainer<PaymentListener> notifyEventListenerContainer =
		new NotifyEventListenerContainer<PaymentListener>(spaceProxy);

	notifyEventListenerContainer.Template = new PaymentListener();
	notifyEventListenerContainer.Start ();

	// when needed dispose of the container
	notifyEventListenerContainer.Dispose();
}
```

You can now execute a simple test that writes a Payment object into the space that will trigger the notification event:




```csharp
public void notifyTest() {
     Payment payment = new Payment();
     payment.Status=ETransactionStatus.CANCELLED;
     proxy.Write(payment);
}
```



Lets assume we want to implement a polling container that receives audit notifications.  Here is an example on how you would define a polling listener:


```csharp
using System;

using GigaSpaces.XAP.Events.Polling;
using GigaSpaces.XAP.Events;

using xaptutorial.model;

[PollingEventDriven]
public class AuditListener {

	[EventTemplate]
	Payment unprocessedData() {
		Payment template = new Payment();
		template.Status=ETransactionStatus.AUDITED;
		return template;
	}

	[DataEventHandler]
	public Payment eventListener(Payment e) {
		// process Payment
		Console.WriteLine("Polling Received a payment:");
		return null;
	}
}
```

And now we register the polling listener:


```csharp
public void registerPollingListener() {
	PollingEventListenerContainer<AuditListener> pollingEventListenerContainer =
		new PollingEventListenerContainer<AuditListener> (spaceProxy);

	pollingEventListenerContainer.Template = new AuditListener ();
	pollingEventListenerContainer.Start();

	// when needed dispose of the container
    pollingEventListenerContainer.Dispose();
}
```

{{%refer%}}
[Event Processing]({{%currentneturl%}}/event-processing.html)
{{%/refer%}}



# FIFO Support
Sometimes it is necessary to process events in the order the way they have been created. By default events are not ordered. XAP supports FIFO (First In, First Out) processing of events.  To enable FIFO operations you can turn on FIFO support for classes which will participate in such operations.    

Here is an example how you can enable FIFO support for a class using the `[SpaceClass]` annotation:


```csharp
using System;

using GigaSpaces.Core.Metadata;

namespace xaptutorial.model
{
	[SpaceClass(FifoSupport = FifoSupport.Operation)]
	public class Payment   {
		[SpaceID(AutoGenerate = true)]
		public String Id;
		[SpaceIndex(Type = SpaceIndexType.Basic)]
		public long? UserId;
		[SpaceRouting]
		[SpaceIndex(Type = SpaceIndexType.Basic)]
		public long?  MerchantId;

		// ....
	}
}
```

{{%refer%}}
[FIFO Support]({{%currentneturl%}}/fifo-support.html)
{{%/refer%}}





# Master Worker Pattern

{{%imagertext "/attachment_files/qsg/the_master_worker.jpg" "Master Worker Pattern"%}}
The Master-Worker Pattern (sometimes called the Master-Slave or the Map-Reduce pattern) is used for parallel processing. It follows a simple approach that allows applications to perform simultaneous processing across multiple machines or processes via a `Master` and multiple `Workers`.
In GigaSpaces XAP, you can implement the Master-Worker pattern using several methods:

- [Task Executors](/sbp/map-reduce-pattern-executors-example.html) - best for a scenario where the processing activity is collocated with the data (the data is stored within the same space as the tasks being executed).
- [Polling Containers]({{%currentneturl%}}/polling-container-overview.html) - in this case the processing activity runs in a separate machine/VM from the space. This approach should be used when the processing activity consumes a relatively large amount of CPU and takes a large amount of time.

It might also be relevant if the actual data required for the processing is not stored within the space, or the time it takes to retrieve the required data from the space is much shorter than the time it takes to complete the processing.

{{%/imagertext%}}

{{%refer%}}
[Master Worker Pattern](/sbp/master-worker-pattern.html)
{{%/refer%}}


