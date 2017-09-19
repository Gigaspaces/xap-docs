---
type: post122
title:  Lease Time
categories: XAP122, OSS
weight: 1000
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

Leases can be used for objects written to GigaSpaces cluster. All the write operations in [GigaSpace]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html) interface support Lease. Lease duration is an argument that is passed to the write operations and they return a Lease Context which can be used to manage the Leases.


```java
GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer("space")).gigaSpace();

MyMessage message1 = new MyMessage();

// Writes the message with 1000 millis/1 sec Lease
LeaseContext<MyMessage> lease1 = gigaSpace.write(message1, 1000);

MyMessage message2 = new MyMessage();

// Writes the message with Default Lease of Lease.FOREVER
LeaseContext<MyMessage> lease2 = gigaSpace.write(message2);

MyMessage message3 = new MyMessage();

// Writes the message with Lease of Lease.FOREVER
LeaseContext<MyMessage> lease3 = gigaSpace.write(message3, Lease.FOREVER);
```

# Getting Lease Expiration Date

You may use the `Lease.getExpiration` to retrieve the time where the space object will expire. See below simple example - It writes a space into the space with 10 seconds lease and later prints how much time is left for the space object to remain in space before it will expire:


```java
GigaSpace space = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer("space")).gigaSpace();

// Writing object into the space with 10 seconds lease time
LeaseContext<MyClass> o = space.write(new MyClass(),10000);
String UID = o.getUID();
System.out.println("Current Date:"+ new Date(System.currentTimeMillis()) + " Lease Expiration Date:" + new Date(o.getExpiration()));
while(true)
{
	long expiredDue = (o.getExpiration() - System.currentTimeMillis())/1000 ;
	System.out.println("Object "+UID +" Expired in :" + expiredDue+ " seconds");
	if (expiredDue <= 0) break;
	Thread.sleep(1000);
}
```

# Manually Managing Space Object Lease

GigaSpaces API returns the `LeaseContext` after every write operation/update operation. Space Object Leases can be renewed or cancelled based on the application needs.


```java
LeaseContext<Order> lease;

...
public void writeOrder() {
...
//Save lease from write operation
lease = gigaSpace.write(singleOrder);
...

public void cancelLease() {
...
lease.cancel();
```

Another alternative to using LeaseContext objects is to retrieve the objects and updating the Lease to desired duration.


```java
//Retrieve all processed low priority orders and expire the lease
Order template = new Order();

template.setType(OrderType.LOW);
template.setProcessed(true);

Order[] processedLowPriorityOrders = gigaSpace.readMultiple(template, 1000);

//Update the lease to expire in 1 second
gigaSpace.writeMultiple(processedLowPriorityOrders,
		1000,					// Update the Lease to 1 second
		UpdateModifiers.UPDATE_OR_WRITE); // Update existing object
```

{{% anchor LeaseRenewalManager %}}

# Managing Leases using LeaseRenewalManager

LeaseRenewalManager provides systematic renewal and management of a set of leases associated with one or more remote entities on behalf of a local entity.

Clients of the renewal manager simply give their leases to the manager and the manager renews each lease as necessary to achieve a _desired expiration time_ (which may be later than the lease's current _actual expiration time_). Failures encountered while renewing a lease can optionally be reflected to the client via `LeaseListener` and `LeaseRenewalEvent` events.

{{% note %}}
 Clients wishing to use this class must create an instance of this class in their own virtual machine to locally manage the leases granted to them. If the virtual machine that the manager was created in exits or crashes, the renewal manager will be destroyed.
{{% /note %}}

{{% note %}}
 `LeaseListener` is invoked only when LeaseRenewalManager gets an exception in renewing the Lease
{{% /note %}}

The LeaseRenewalManager distinguishes between two time values associated with lease expiration: the _desired expiration time_ for the lease, and the _actual expiration time_ granted when the lease is created or last renewed. The desired expiration represents when the client would like the lease to expire. The actual expiration represents when the lease is going to expire if it is not renewed. Both time values are absolute times, not relative time durations. The _desired expiration time_ can be retrieved using the renewal manager's `getExpiration` method. The _actual expiration time_ of a lease object can be retrieved by invoking the lease's getExpiration method.

{{% note %}}
 _desired expiration time_ is absolute time and not relative time. When adding Leases to a LeaseRenewalManager, remember to pass appropriate value for `desiredExpiration` parameter.
{{% /note %}}

Each lease in the managed set also has two other associated attributes: a desired renewal duration, and a remaining desired duration. The desired renewal duration is specified (directly or indirectly) when the lease is added to the set. This duration must normally be a positive number; however, it may be Lease.FOREVER if the lease's desired expiration is to be unlimited. The remaining desired duration is always the desired expiration less the current time.

Each time a lease is renewed, the renewal manager will ask for an extension equal to the lease's renewal duration if the renewal duration is:

- Lease.FOREVER, or
- less than the remaining desired duration,

otherwise it will ask for an extension equal to the lease's remaining desired duration.

{{% note %}}
 Starting from XAP 9.5, Lease.ANY is not supported, equivalent is to use Lease.FOREVER when using LeaseRenewalManager.
{{% /note %}}

Once a lease is given to a lease renewal manager, the manager will continue to renew the lease until one of the following occurs:

- The lease's desired or actual expiration time is reached.
- An explicit removal of the lease from the set is requested via a cancel, clear, or remove call on the renewal manager.
- The renewal manager tries to renew the lease and gets a bad object exception, bad invocation exception, or LeaseException.

### Configuring LeaseRenewalManager

LeaseRenewalManager supports [Configuration](http://www.gigaspaces.com/docs/JiniApi/net/jini/config/Configuration.html) entries, with component "net.jini.lease.LeaseRenewalManager":


| Property | Type | Default | Description |
|:---------|:-----|:--------|:------------|
| roundTripTime | long| 10 * 1000 // 10 seconds | The worst-case latency, expressed in milliseconds, between client and GigaSpace cluster (call to renew a lease). The value must be greater than zero. Unrealistically low values for this entry may result in failure to renew a lease. Leases managed by this manager should have durations exceeding the roundTripTime. This entry is obtained in the constructor.|
| renewBatchTimeWindow |long | 5 \* 60 \* 1000 // 5 minutes | The maximum number of milliseconds earlier than a lease would typically be renewed to allow it to be renewed in order to permit batching its renewal with that of other leases. The value must not be negative. This entry is obtained in the constructor.|
| taskManager | TaskManager | new TaskManager(11, 15000, 1.0f) | The object used to manage queuing tasks involved with renewing leases and sending notifications. The value must not be null. The default value creates a maximum of 11 threads for performing operations, waits 15 seconds before removing idle threads, and uses a load factor of 1.0 when determining whether to create a new thread. Note that the implementation of the renewal algorithm includes an assumption that the TaskManager uses a load factor of 1.0.|

### Lease Renewal Algorithm

The time at which a lease is scheduled for renewal is based on the expiration time of the lease, possibly adjusted to account for the latency of the renewal call. The configuration entry roundTripTime, which defaults to ten seconds, represents the total time to make the remote call.

The following pseudo code was derived from the code which computes the renewal time. In this code, rtt represents the value of the roundTripTime:


```java
  endTime = lease.getExpiration();
  delta = endTime - now;
  if (delta <= rtt * 2) {
	delta = rtt;
  } else if (delta <= rtt * 8) {
	delta /= 2;
  } else if (delta <= 1000 * 60 * 60 * 24 * 7) {
	delta /= 8;
  } else if (delta <= 1000 * 60 * 60 * 24 * 14) {
	delta = 1000 * 60 * 60 * 24;
  } else {
	delta = 1000 * 60 * 60 * 24 * 3;
  }
  renew = endTime - delta;
```

It is important to note that delta is never less than rtt when the renewal time is computed. A lease which would expire within this time range will be scheduled for immediate renewal.

{{% note %}}
 The use of very short lease durations (at or below rtt) can cause the renewal manager to effectively ignore the lease duration and repeatedly schedule the lease for immediate renewal.
{{% /note %}}

If an attempt to renew a lease fails with an indefinite exception, a renewal is rescheduled with an updated renewal time as computed by the following pseudo code:


```java
  delta = endTime - renew;
  if (delta > rtt) {
	  if (delta <= rtt * 3) {
		delta = rtt;
	  } else if (delta <= 1000 * 60 * 60) {
		delta /= 3;
	  } else if (delta <= 1000 * 60 * 60 * 24) {
		delta = 1000 * 60 * 30;
	  } else if (delta <= 1000 * 60 * 60 * 24 * 7) {
		delta = 1000 * 60 * 60 * 3;
	  } else {
		delta = 1000 * 60 * 60 * 8;
	  }
	  renew += delta;
  }
```

Client leases are maintained in a collection sorted by descending renewal time. A renewal thread is spawned whenever the renewal time of the last lease in the collection is reached. This renewal thread examines all of the leases in the collection whose renewal time falls within renewBatchTimeWindow milliseconds of the renewal time of the last lease. If any of these leases can be batch renewed with the last lease (as determined by calling the canBatch method of the last lease) then a LeaseMap is created, all eligible leases are added to it and the LeaseMap.renewAll() method is called.

Otherwise, the last lease is renewed directly.
The TaskManager that manages the renewal threads has a bound on the number of simultaneous threads it will support. The renewal time of leases may be adjusted earlier in time to reduce the likelihood that the renewal of a lease will be delayed due to exhaustion of the thread pool. Actual renewal times are determined by starting with the lease with the latest (farthest off) desired renewal time and working backwards. When computing the actual renewal time for a lease, the renewals of all leases with later renewal times, which will be initiated during the round trip time of the current lease's renewal, are considered. If using the desired renewal time for the current lease would result in more in-progress renewals than the number of threads allowed, the renewal time of the current lease is shifted earlier in time, such that the maximum number of threads is not exceeded.

### Example

Following example shows a client writing `Order`'s to the space with a limited lease. It uses a LeaseRenewalManager to renew the Lease for the `Order`. It also uses a LeaseListener which is triggered in case LeaseRenewalManager runs into errors renewing a lease. Example source can be downloaded from [here](/download_files/LeaseRenewalManager.zip).

{{%tabs%}}
{{%tab "  LeaseManagerClient "%}}


```java
...

public class LeaseManagerClient {

...

    public LeaseManagerClient(String url) {

	// use gigaspace wrapper to for simpler API
	this.gigaSpace = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer("space")).gigaSpace();

	createOrder();

    }

    private void createOrder() {
	// Create new order
	Order order = new Order();
	order.setData("NewOrder");
	order.setProcessed(false);

	DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS");
	Calendar calendar = Calendar.getInstance();

	// Write the order with limited lease
	LeaseContext<Order> lease = gigaSpace.write(order, 10000);

	calendar.setTimeInMillis(System.currentTimeMillis());
	logger.info("Wrote Event at: " + formatter.format(calendar.getTime()));

	calendar.setTimeInMillis(lease.getExpiration());
	logger.info("Object Leased upto: "
			+ formatter.format(calendar.getTime()));

	MyLeaseListener myListener = new MyLeaseListener();
	LeaseRenewalManager renewer;
	try {

		//Create a LeaseRenewalManager
		renewer = new LeaseRenewalManager();

		//Renew the order lease for 1 minute and keep renewing every 10 seconds
		renewer.renewUntil(lease, System.currentTimeMillis() + 60000,
				10000, myListener);

		// Get the expiration of the lease
                // (this should now point to 1 minute instead of 10 seconds
		long leasedUntil = renewer.getExpiration(lease);
		calendar.setTimeInMillis(leasedUntil);
		logger.info("LeaseRenewalManager manages lease upto: "
				+ formatter.format(calendar.getTime()));
	} catch (ConfigurationException e1) {
		e1.printStackTrace();
		logger.info("Error configuring LeaseRenewalManager");
	} catch (UnknownLeaseException e) {
		e.printStackTrace();
		logger.info("Error retrieving Expiration");
	}
    }

}
```

{{% /tab %}}
{{%tab "  Using Custom LeaseRenewalManager Configuration "%}}


```java
public LeaseManagerClient(String url) {

... // Same as previous one

}

private void createOrder() {

        ... // Same as previous one

	// Create a custom configuration
        // (use non-default roundTripTime, default is 10 secs)
	LeaseRenewalConfiguration myConfig = new LeaseRenewalConfiguration(1000);

        MyLeaseListener myListener = new MyLeaseListener();
	LeaseRenewalManager renewer;
	try {

		//Create a LeaseRenewalManager using custom config
		renewer = new LeaseRenewalManager(myConfig);

		//Renew the order lease for 1 minute and keep renewing every 10 seconds
		renewer.renewUntil(lease, System.currentTimeMillis() + 60000,
				10000, myListener);

        ... // Same as previous one

}

private static final class LeaseRenewalConfiguration implements Configuration {

	final private Long configRTT;

	public LeaseRenewalConfiguration(long renewRTT) {
		configRTT = renewRTT;
	}

	public Object getEntry(String component, String name, Class type)
			throws ConfigurationException {
		return getEntry(component, name, type, -1l, null);
	}

	public Object getEntry(String component, String name, Class type,
			Object defaultValue) throws ConfigurationException {
		return getEntry(component, name, type, defaultValue, null);
	}

	public Object getEntry(String component, String name, Class type,
			Object defaultValue, Object data) throws ConfigurationException {

		if (!component.equals("net.jini.lease.LeaseRenewalManager"))
			return defaultValue;
		if (name.equals("roundTripTime"))
			return configRTT; // renewalRTT
		if (name.equals("renewBatchTimeWindow"))
			return 2l; // renewBatchTimeWindow
		return defaultValue;
	}
}
```

{{% /tab %}}
{{%tab "  LeaseListener "%}}


```java
public class MyLeaseListener implements LeaseListener{

	Logger logger = Logger.getLogger(this.getClass().getName());

	public MyLeaseListener ()
	{
	}

	public void notify(LeaseRenewalEvent event) {
		logger.info("LeaseRenewalEvent failed. Received the Event " + event);
	}
}
```

{{% /tab %}}
{{%tab "  Order "%}}


```java
public class Order implements Serializable {

	private String id;
	private Boolean processed;
	private String data;

	@SpaceId(autoGenerate=true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String d) {
		this.data = d;
	}

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}
}
```

{{% /tab %}}
{{% /tabs %}}

API page for [LeaseRenewalManager](http://www.gigaspaces.com/docs/JiniApi/net/jini/lease/LeaseRenewalManager.html) has additional information.

# Lease Expiration Notifications

Getting events once the space object lease expired can be done using the [Notify Container](./notify-container.html). See example below:


```java
SimpleListener myListenr = new SimpleListener();
SimpleNotifyEventListenerContainer notifyEventListenerContainer = new SimpleNotifyContainerConfigurer(
	gigaSpace).notifyLeaseExpire(true).
	template(new MyData()).eventListener(myListenr)
	.notifyContainer();
```

The `Listener`:


```java
public class SimpleListener implements SpaceDataEventListener<MyData> {
	public void onEvent(MyData data, GigaSpace gigaSpace,
			TransactionStatus txStatus, Object source) {
		// process event
		EntryArrivedRemoteEvent event = (EntryArrivedRemoteEvent) source;

		System.out.println(new Date() + " Got " +
 NotifyActionType.fromModifier(event.getNotifyType())+" notification for Object "
				+ data.getId() );
	}
}
```

And with Java8 lambda syntax:


```java
SimpleNotifyEventListenerContainer notifyEventListenerContainer = new SimpleNotifyContainerConfigurer(space)
	.template(new MyData())
    .eventListener((data, gigaSpace, txStatus, source) -> {
        System.out.println("Got matching event! - " + (MyData)data);
    })
    .notifyContainer();

or

SimpleNotifyEventListenerContainer notifyEventListenerContainer = new SimpleNotifyContainerConfigurer(space)
	.template(new MyData())
    .eventListener((SpaceDataEventListener<TradePojo>)(TradePojo data, GigaSpace gigaSpace1, TransactionStatus txStatus, Object source) -> {
        System.out.println("Got matching event! - " + data);
    })
    .notifyContainer();
```

Notifications for expired space objects are delivered both from the primary and backup space instances. In some cases you may want to handle notifications sent only from the primary instances. The [lease expiration notification example](/download_files/LeaseExpirationNotificationExample.zip) show how you can identify from which instance (primary or a backup) the lease expiration notifications has been sent.

# Space Object Lease with a Persistent Space

When Objects are written to a Persistent Space (backed by a permanent store using ExternalDataSource), objects are written to the permanent store and removed from the space once lease expires. To avoid reloading the expired data into space objects should use @SpaceLeaseExpiration annotation.

Once an object's lease expires the underlying persistence store:

- will not be updated when running in `LRU` <br>
- will not be updated when running in `All in cache`.
- when running with `blobstore` (SSD or local file implementation) expired space objects are removed from the `blobstore`.

# Lease Manager

You can control how often this thread invokes the invalidation process. This involves iterating through all the expired space objects since the last invalidation cycle, and allowing the JVM garbage collector to release the memory consumed for the object. To configure the Lease Manager interval use the following:


```java
space-config.lease_manager.expiration_time_interval=10000
```

- When writing objects into the space using a short lease time, it is recommended to configure the lease manager interval to be short.
- The `NOTIFY_LEASE_EXPIRATION` notification is called when the Lease Manager invalidates the object.
- Notifications for expired objects sent **both from the primary and the backup space** (in case you have a backup running).
- When a transaction is timed out, its locked objects are released when the lease manager thread is triggered. This means that if a client fails while a transaction is opened with locked objects (take, write, read, and update using transactions), the locked objects are released once the lease manager thread identifies the expired transaction.
