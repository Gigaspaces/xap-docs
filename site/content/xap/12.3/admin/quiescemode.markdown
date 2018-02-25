---
type: post123
title:  Quiesce Mode
categories: XAP123ADM, PRM
parent: none
weight: 1200
---

# Overview

This feature adds support for "Quiesce state" (or maintenance mode) for a Processing Unit. If a space is **quiesced**, it rejects all data-related operations with an exception, allowing the user to perform maintenance operations.

The XAP [Polling Container](../dev-java/polling-container-overview.html) and [Notify Container](../dev-java/notify-container-overview.html) are also aware of the Space's quiesce state, and handle quiesce state changed events by implementing [QuiesceStateChangedListener](#listener). 

In addition to the XAP event containers mentioned above, a user-defined Bean can also handle quiesce state changed events by implementing [QuiesceStateChangedListener](#listener). 

Quiesce is usually triggered using the ProcessingUnit API, and is propagated to all Space instances and quiesce state changed listeners in the Processing Unit. 

{{%refer %}}
Quiesce mode can be also triggered using a CLI (see [Quiesce CLI](./quiesce-command-line-interface.html)).
{{%/refer%}}



## Uses for Quiesce State

There are several scenarios that can benefit from quiesce state. Some of these are described below.

### Safe Shutdown

Use Quiesce state to shut down an application that contains a data grid and a mirror in a safe manner. This ensures that all operations completed on the grid are flushed to the mirror before undeploy.

**To perform a safe shutdown:**
  
1. Enter quiesce mode.
1. Wait for replication between the space and the mirror to complete (by monitoring the redolog).
1. Undeploy the space and mirror.

### Rolling System Upgrade

Use Quiesce mode to perform a rolling system upgrade on a live system (typically a Processing Unit code upgrade) and avoid inconsistencies related to processing of requests during the upgrade process.

**To perform a rolling system upgrade with Quiesce mode:**

1. Enter Quiesce mode.  <br>
2. Perform the hot-upgrade pattern: <br>
2.1.   Update the Processing Unit in the GSM.<br>
2.2.  Restart the backup instances.<br>
2.3. Restart the primary instances.<br>
3. Exit Quiesce mode and verify that the upgraded system resumes and works as expected,

{{% refer %}}
Review additional use case examples [here](#usecase).
{{%/refer%}}

## Space Behavior During Quiesce Mode

The Space behaves as follows when it is in Quiesce mode:

- Rejects all user data-related new operations by throwing a QuiesceException back to the user (replication of previous ongoing operations continues).
- Commit and abort are allowed for two-phase commit transactions.
- Commit and prepare of single-phase transactions are rejected.
- The Lease manager is paused, except for extend lease operations, in order to prevent losing notify listener templates.

## Security

In a secure system, `Manage PU` privileges are required to quiesce/unquiesce a Processing Unit.

## Configuration

The Quiesce feature can be disabled by setting `com.gs.engine.disableQuiesceMode` to true.


## Limitations 

The following limitations and open issues apply to Quiesce mode:

- The Processing Unit should be **intact** before triggering a quiesce request.
- Although Quiesce handles the Processing Unit restart, it is not resilient regarding sudden network disconnections (in XAP grid component machines). Therefore, under rare conditions the quiesce request must be repeated manually by the user. 
- Quiesce state changed events are propagated only to the components (beans) that are located within the Processing Unit context file ([pu.xml](../dev-java/configuring-processing-unit-elements.html)), therefore custom components are not aware of quiesce state changed events (even if the component implements [QuiesceStateChangedListener](#listener)).
- Replication of the Quiesce state between primary and backup GSM is not yet supported. GSM failover may result in losing the quiesce state of the system. If this happens, you must repeat the quiesce request  in order to re-inform the GSM about the current quiesce state.

 

# Processing Unit API

The following ProcessingUnit interface API is used to trigger and manage Quiesce Mode.

 
```java
// Requests quiesce request from the GSM.
// All space instances and listeners will switch to quiesced mode.
// If the GSM rejects the request an exception with the rejection failure will be thrown.
QuiesceResult quiesce(QuiesceRequest request);
// Requests unquiesce request from the GSM.
// All space instances and listeners will switch to unquiesced mode.
// If the GSM rejects the request an exception with the rejection failure will be thrown.
void unquiesce(QuiesceRequest request);
// Return true if the processing unit reached to desired state as well as all instances in the requested timeout, false otherwise.
boolean waitFor(QuiesceState desiredState, long timeout, TimeUnit timeUnit);
boolean waitFor(QuiesceState desiredState);
// Return the quiesce details of the processing unit
QuiesceDetails getQuiesceDetails();
```

{{% anchor listener %}}

## Quiesce State Changed Listener

A user-defined component (Spring Bean) in a Processing Unit can implement `QuiesceStateChangedListener` and be aware of quiesce state changed events.

```java
public class CustomComponent implements ... ,QuiesceStateChangedListener {
    ...
    public void quiesceStateChanged(QuiesceStateChangedEvent event) {
        if (event.getQuiesceState() == QuiesceState.QUIESCED)
            // stop interacting with the space to prevent getting exceptions
        else
            // resume interacting with the space
    }
}
```

{{%anchor usecase%}}

## Sample Use Cases 

### Safely Undeploy a Stateful Processing Unit

```java
QuiesceRequest request = new QuiesceRequest("Jacob: performing safe shutdown in 11:33 AM");
QuiesceResult result = pu.quiesce(request);
boolean quiesced = pu.waitFor(QuiesceState.QUIESCED, OPERATION_TIMEOUT, TimeUnit.MINUTES);
if (quiesced) {
    System.out.println("All instances are QUIESCED, shutting down...");
    // wait for redo log to drop to zero
    ...
    pu.undeployAndWait(OPERATION_TIMEOUT, TimeUnit.MILLISECONDS)
}
else {
    System.out.println("All instances were not QUIESCED within the given timeout");
    // Print QuiesceDetails to figure out which instances were not QUIESCED 
    System.out.println("Details: " + pu.getQuiesceDetails());
    // retry or do some logic 
    ...
}

```

### Rolling Upgrade on a Live System


```java
QuiesceRequest request = new QuiesceRequest("Jacob: performing hot deploy in 11:33 AM");
QuiesceResult result = pu.quiesce(request);
pu.waitFor(QuiesceState.QUIESCED, OPERATION_TIMEOUT, TimeUnit.MINUTES);
// wait for redo log to drop to zero
...
// upgrade the system, restart backups and then primaries
...
QuiesceRequest resumeRequest = new QuiesceRequest("Jacob: resumming the system in 12:14 AM");
pu.unquiesce(resumeRequest);
pu.waitFor(QuiesceState.UNQUIESCED, OPERATION_TIMEOUT, TimeUnit.MINUTES);
System.out.println("the system was successfully upgraded");

```


# REST API

The following are examples of entering and exiting Quiesce mode using the REST API.

## Quiesce

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/deployments/myApp/quiesce'
```

## Unquiesce

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/deployments/myApp/unquiesce'
```

