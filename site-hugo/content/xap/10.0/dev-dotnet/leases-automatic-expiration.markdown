---
type: post100
title:  Lease Time
categories: XAP100NET
weight: 700
parent: the-gigaspace-interface-overview.html
---

{{% ssummary %}} {{% /ssummary %}}



In distributed applications on a network, where there may be partial failures of the network or of components, there needs to be a way for components to be timed out if they have failed, or have become unreachable. Lease is a basic mechanism GigaSpaces provides to address this problem. GigaSpaces provides this functionality using Jini technology.

# Lease Basics

The essential idea behind a lease is fairly simple.

- When creating a resource, the requestor creates the resource with a  limited life span.
- The grantor of the resource will then give access for some period of time that is no longer than that requested.
- The period of time that is actually granted is returned to the requestor as part of the Lease object.
- A holder of a lease can request that a Lease be renewed, or cancel the Lease at any time.
- Successfully renewing a lease extends the time period during which the lease is in effect.
- Cancelling the lease drops the lease immediately.

Few other ways Lease can be managed include,

- Specifying the Lease interface constant FOREVER, requests a lease that never expires. When granted such a lease, the grantor is responsible for ensuring that the leased resource is freed when no longer needed.
- Specifying the Lease interface constant ANY, indicates that no particular lease time is desired and that the grantor of the lease should supply a time that is most convenient for the grantor. (GigaSpaces converts leases with ANY constant to FOREVER leases).

# Space Object Lease

Leases can be used for objects written to an XAP cluster. All the write operations in [ISpaceProxy]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_ISpaceProxy.htm) interface support Lease. Lease duration is an argument that is passed to the write operations and they return a Lease Context which can be used to manage the Leases.


```csharp
MyMessage message1 = new MyMessage ();

// Writes the message with 1000 millis/1 sec Lease
ILeaseContext<MyMessage> lease1 = spaceProxy.Write (message1, 1000);

MyMessage message2 = new MyMessage ();

// Writes the message with Default Lease of FOREVER
ILeaseContext<MyMessage> lease2 = spaceProxy.Write (message2);

MyMessage message3 = new MyMessage ();

// Writes the message with Lease of FOREVER
ILeaseContext<MyMessage> lease3 = spaceProxy.Write (message3, long.MaxValue);
```

# Getting Lease Expiration Date

You may use the `Lease.getExpiration` to retrieve the time where the space object will expire. See below simple example - It writes a space into the space with 10 seconds lease and later prints how much time is left for the space object to remain in space before it will expire:


```csharp
// Writing object into the space with 10 seconds lease time
ILeaseContext<MyMessage> lease = spaceProxy.Write(new MyMessage(),10000);
String UID = lease.Uid;

Console.Write("Current Date:"+ new DateTime() + " Lease Expiration Date:" + new DateTime(lease.Expiration));

while(true)
{
    long expiredDue = (lease.Expiration - new DateTime().Millisecond) ;

	Console.Write("Object "+UID +" Expired in :" + expiredDue+ " seconds");

	if (expiredDue <= 0) break;
	    Thread.Sleep(1000);
}
```

# Manually Managing Space Object Lease

GigaSpaces API returns the `ILeaseContext` after every write operation/update operation. Space Object Leases can be renewed or cancelled based on the application needs.


```csharp
ILeaseContext<Order> lease;

...
public void writeOrder() {
...
//Save lease from write operation
lease = spaceProxy.Write(singleOrder);
...

public void cancelLease() {
...
lease.Cancel();
```

Another alternative to using ILeaseContext objects is to retrieve the objects and updating the Lease to desired duration.


```csharp
//Retrieve all processed low priority orders and expire the lease
Order template = new Order();

template.Type=OrderType.LOW;
template.Processed=true;

Order[] processedLowPriorityOrders = spaceProxy.ReadMultiple(template, 1000);

//Update the lease to expire in 1 second
spaceProxy.WriteMultiple(processedLowPriorityOrders,
		1000,					// Update the Lease to 1 second
		WriteModifiers.UpdateOrWrite); // Update existing object
```

{{% anchor LeaseRenewalManager %}}

