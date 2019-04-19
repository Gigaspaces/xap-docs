---
type: post122
title:  Transactions and Concurrency
categories: XAP122NET, PRM
parent: xapnet-basics.html
weight: 700
canonical: auto
---



In this part of the tutorial we will introduce you to the transaction processing capabilities of XAP.

With the XAP .NET transaction model the developer is responsible for explicitly starting and managing the transaction. You obtain an object representing the underlying space transaction by calling `GigaSpacesFactory.CreateDistributedTransactionManager`. This call returns an implementation of the `ITransactionManager` interface used to create the transaction using the `ITransactionManager.Create()`.

Here is an example how you use transactions:


```csharp
public void readWithTransaction()
{
	ITransactionManager mgr = GigaSpacesFactory.CreateDistributedTransactionManager ();

	// Create a transaction using the transaction manager:
	using (ITransaction txn = mgr.Create ()) {
		try {
			// ...
			SqlQuery<User> query = new SqlQuery<User> ("Contacts.HOME = '770-123-5555'");
			User user = proxy.Read<User> (query, txn, 0, ReadModifiers.RepeatableRead);
			// ....
			txn.Commit ();
		} catch (Exception e) {
			// rollback the transaction
			txn.Abort ();
		}
	}
}
```


# Event Processing
All event containers support transactions.

### Polling container
Both the receive operation and the actual event action can be configured to be performed under a transaction. Transaction support is required when, for example, an exception occurs in the event listener, and the receive operation needs to be rolled back (and the actual data event is returned to the space). Adding transaction support is very simple in the polling container, and can be done by injecting a transaction manager into it. Let's take our payment polling container and make it transactional.

You make a polling container transactional by adding the `[TransactionalEvent]` annotation:

```csharp
using System;

using GigaSpaces.Core.Events;
using GigaSpaces.XAP.Events.Polling;
using GigaSpaces.XAP.Events;

using xaptutorial.model;

[PollingEventDriven]
[TransactionalEvent]
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

{{%refer%}}
[Polling Container]({{%currentneturl%}}/polling-container.html)
{{%/refer%}}




### Notify Container
Just like the Polling container, both the receive operation and the actual event action can be configured to be performed under a transaction. However, in case an error occurs (rollback), the notification is lost and not sent again.

{{%refer%}}
[Notify Container]({{%currentneturl%}}/notify-container.html)
{{%/refer%}}




# Task Execution
Executors fully support transactions similar to other XAP operations. Once an execute operation is executed within a declarative transaction, it will automatically join it. The transaction itself is then passed to the node the task executed on and added declaratively to it. This means that any XAP operation performed within the task execute operation will automatically join the transaction started on the client side.

{{%refer%}}
[Task Execution over the Space]({{%currentneturl%}}/task-execution-over-the-space.html)
{{%/refer%}}





# Remoting Service
Executor remoting supports transactional execution of services. On the client side, if there is an ongoing declarative transaction during the service invocation (a Space based transaction), the service will be executed under the same transaction. The transaction itself is passed to the server and any Space related operations (performed using XAP) will be executed under the same transaction.

{{%refer%}}
[Space based Remoting]({{%currentneturl%}}/space-based-remoting-overview.html)
{{%/refer%}}





# Concurrency
Locking of objects occurs in multi-user systems to preserve the integrity of changes, so that changes made by one process do not accidentally overwrite changes made by another process. XAP provides two strategies for locking objects: Optimistic and pessimistic. The focus is on optimistic locking, the preferred strategy in the XAP context.

### Optimistic Locking
With optimistic locking, you write your business logic allowing multiple users to read the same object at the same time, but allow only one user to update the object successfully. The assumption is that there will be a relatively large number of users trying to read the same object, but a low probability of having a small number of users trying to update the same object at the same time. In the case of multiple users trying to update the same object at the same time, the one(s) that try to update a non-recent object version fail.

XAP uses a version number for an object to accomplish optimistic locking. When a client makes updates to an object an additional where clause is added to the update operation where the client version number of the object is compared against the version number of the object in space. If the the version number is not the same, the operation is rejected, indicating that the object has been modified by some other client or process.

Here is an example of how optimistic locking is enabled in XAP. First we need to indicate to the space that it will hold versioned objects.


```csharp
public void creatVersionedSpace()
{
	// Embedded Space
	// Create the SpaceProxy
	ISpaceProxy spaceProxy = new EmbeddedSpaceFactory("xapTutorialSpace").Create();
	spaceProxy.OptimisticLocking = true;
}
```

You should enable the Space class to support the optimistic locking, by including the `[SpaceVersion]` decoration on an int getter field. This field stores the current object version and is maintained by XAP. See below for an example:

```csharp
namespace xaptutorial.model
{
	[SpaceClass]
	public class Account {
		[SpaceID]
		[SpaceRouting]
		public long? Id { set; get; }
		public String Number{ set; get; }
		public double? Receipts{ set; get; }
		public double? FeeAmount{ set; get; }
		public Nullable<EAccountStatus> Status{ set; get; }
		[SpaceVersion]
		public int Version{ set; get; }

		// ...
	}
}
```




### Pessimistic Locking
The pessimistic locking protocol provides data consistency in a multi user transactional environment. It should be used when there might be a large number of clients trying to read and update the same object(s) at the same time. This protocol utilize the system resources (CPU, network) in a very efficient manner both at the client and space server side.
This scenario is different from the optimistic locking protocol since we assume with the pessimistic locking protocol, that every object that is read and retrieved from the space will eventually be updated where the transaction duration is relatively very short.

Here is an example of pessimistic locking that uses the exclusive read lock ReadModifier:

```csharp
ITransactionManager mgr = GigaSpacesFactory.CreateDistributedTransactionManager ();

// Create a transaction using the transaction manager:
using (ITransaction txn = mgr.Create ()) {
    try {
	    // Read and lock the payment object
	    Payment payment = proxy.ReadById(1,txn,ReadModifiers.ExclusiveReadLock);

	    payment.Status=ETransactionStatus.CANCELLED;
	    space.Write(payment,txn);
	    txn.Commit ();
	} catch (Exception e) {
       	// rollback the transaction
        txn.Abort ();
    }
}
```


XAP provides additional read modifiers to denote the isolation level:

- RepeatableRead  - default modifier
- DirtyRead
- ReadCommitted
- ExclusiveReadLock

{{%refer%}}
[Transaction Read Modifiers]({{%currentneturl%}}/transaction-read-modifiers.html)
{{%/refer%}}




