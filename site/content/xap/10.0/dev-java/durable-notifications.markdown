---
type: post100
title:  Durable Notifications
categories: XAP100
parent: session-based-messaging-api.html
weight: 100
---

{{% ssummary %}} {{% /ssummary %}}



Due to the asynchronous nature of notification delivery, when a primary space fails right after replicating an operation to the backup space and before sending the notification to the registered client, the backup space might not be able to send the missing notifications, since it is in the process of moving to active mode.
This means that during this very short period of time, the registered client might not receive events that occurred in the primary space and were replicated to the backup space.
Durable notifications use the replication mechanism to handle the above case. In addition, in case there are short network disconnections, the events that occurred during this period will not be lost and will be sent to the client.

Because durable notifications are based on the replication mechanism, there exists some different semantics regarding other EventSessionConfig parameters.
These differences are described in the following sections.

# Configuration

Using the `com.gigaspaces.events.EventSessionConfig` set the durable notifications flag to true.


```java
EventSessionFactory factory = EventSessionFactory.getFactory(space);
EventSessionConfig config = new EventSessionConfig();
config.setDurableNotifications(true);
DataEventSession session = factory.newDataEventSession(config);
```

# Batch Parameters

Durable notifications are always sent as batches over the network (even if the client will receive them one by one), so tuning these parameters might be beneficial in certain cases.


```java
EventSessionConfig#setBatch(int size, long delay, int pendingThreshold)
```


|Property| Description | Default | Unit |
|:-------|:------------|:--------|:-----|
|size|The upper bound for the amount of notifications sent in each batch (Note: events that occurred under transactions will be sent on commit and the entire transaction is counted as one operation for the upper bound).|5000| |
|delay|The maximum time to delay notifications in case there are not enough notifications to pass the pendingThreshold.|10|ms|
|pendingThreshold|The maximum amount of notifications the server holds before he wakes up and starts sending the notifications.|100| |


```java
EventSessionConfig config = new EventSessionConfig();
config.setDurableNotifications(true);
config.setBatch(5000, 10, 100);
```

# Disconnection Notifications

In order to receive a notification upon disconnection, use the following:


```java
setAutoRenew(boolean renew, net.jini.lease.LeaseListener listener)
```

{{%warning "Deprecated since XAP 9.7"%}}
```java
setAutoRenew(boolean renew, net.jini.lease.LeaseListener listener, long renewExpiration, long renewDuration, long renewRTT)
```
Setting `renewExpiration`, `renewDuration` and `renewRTT` is not recommended. Please use the method described above for auto renewal.

|Property|Description|Default|Unit|
|:-------|:----------|:------|:---|
|renew|Must be set to `true` in order to receive disconnection notifications.|false| |
|renewExpiration|Could be used to set a lease that will be applied to registering listeners. (`renew` must be set to `true`)|Lease.FOREVER|ms|
|renewDuration|The period of time a disconnected space should be considered as down and a disconnection notification should be sent. (`renew` must be set to `true`)|20000|ms|
|renewRTT|Ignored| | |
{{%/warning%}}

An example of a LeaseListener implementation:


```java
public class MyLeaseListener implements LeaseListener {

	DataEventSession session;

	public MyLeaseListener (DataEventSession session) {
		this.session = session;
	}

	// Called when renewDuration elapsed after initial disconnection and no re-connection occurred
	public void notify(LeaseRenewalEvent event) {
		System.out.println("Disconnected - try to re-register");
		session.addListener(new MyData(), this, Lease.FOREVER, null, null, NotifyActionType.NOTIFY_ALL);
		System.out.println("Notify Re-registration Done!");
	}
}
```

Note that when adding a listener to the session with a specified lease, the maximum value between the session lease and the listener lease will be used.

# Limitations and Requirements

{{%vbar%}}
- Durable Notification does not support an embedded Space, it can only be used with a remote proxy. Use a [Polling container](./polling-container.html) as an alternative.
- The durable notifications feature is available only for primary backup topologies.
- Notifications are always sent as batches over the network. (even if the client will receive them one by one)
- Only Unicast communication type is supported.
- IDelegationFilter is not supported.
- Listener template must be replicable.
- Notify template is always replicated to backup space. (this is the default)
- Notify template cannot be triggered at backup space. (this is the default)
{{%/vbar%}}

# Notes

- Durable Notifications are always in FIFO ordering.
