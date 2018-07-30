---
type: post123
title:  Quiesce Mode 2
categories: XAP123ADM, PRM
parent: none
weight: 1201
---

# Overview

Quiesce Mode is a maintenance mode that enables stateful Processing Unit upgrades. 

State consistency is guaranteed for:
 
- cache data
- replication channel transactions

Interested components can implement a [listener](#implement-listener) and respond to quiesce state changes.

{{% anchor quiesce-the-space %}}

### REST API

The following are examples of entering and exiting Quiesce mode using the REST API.

#### Quiesce
```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/deployments/myApp/quiesce'
```

#### Unquiesce
```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/deployments/myApp/unquiesce'
```

### Command Line

{{%refer %}}
Quiesce mode can be also triggered using a CLI (see [Quiesce CLI](./quiesce-command-line-interface.html)).
{{%/refer%}}

### Java

The [ProcessingUnit API](https://resources.gigaspaces.com/api/xap/12.3.0/java/org/openspaces/admin/pu/ProcessingUnit.html) can be used from any application that has access to the Space.

```java
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

Once a reference to a `ProcessingUnit` is obtained, quiesce mode API calls are propogated to all Space instances and `QuiesceStateChangedListeners` in the Processing Unit. 

## Space Behavior During Quiesce Mode

In quiesce mode, the Space behaves as follows:

- Incoming operations throw a QuiesceException back to caller. (Replication of previous operations continue.)
- Single-phase transactions are rejected during prepare and commit.
- Two-phase commit transactions can be committed or aborted.
- The Lease Manager is paused, except for lease extension operations.

{{% refer %}}
Lease Manager details are documented [here](../dev-java/leases-automatic-expiration.html).
{{% /refer %}}

## Use Cases

### Safe Replication Channel Shutdown 

Spaces use replication channels both to store data in backing databases and also to support WAN replication to other Spaces. 

During shutdown, quiesce mode enables the system to safely drain off pending changes prior to termination. This ensures that all update operations completed on the grid are committed at destination before undeploy.

**Recipe**
  
1. [Quiesce the Space](#quiesce-the-space)
1. Wait for replication to complete  
1. Undeploy (mirror or gateway, then Space)

### Rolling System Upgrade

Spaces may require zero-downtime updgrades to stateful Processing Units. 

During upgrade, enabling quiesce mode guarantees data consistency throughout the upgrade process. 

1. [Quiesce the Space](#quiesce-the-space)
2. Hot Deploy<br>
2.1. Update Processing Unit (in the GSM deploy directory)<br>
2.2. Restart backup Space Instances<br>
2.3. Restart primary Space Instances<br>
3. Exit Quiesce mode

{{% refer %}}
Hot deploy is documented in more detail [here](../../../sbp/hot-deploy.html).
{{% /refer %}}


### Application Component Update

Components in a Processing Unit can implement `QuiesceStateChangedListener`. Implementing service Objects as Spring Beans is recommended. Any component that implements this listener interface will be registered to receive state change events, and respond appropriately.

Data Grid platform components that are sensitive to quiesce changes include [Polling](../dev-java/polling-container-overview.html) and [Notify](../dev-java/notify-container-overview.html) containers. Adding support for state change event detection is as simple as implementing the interface.

```java
public class CustomComponent implements QuiesceStateChangedListener /*, PollingContainer */ {
    // ...
    public void quiesceStateChanged(QuiesceStateChangedEvent event) {
        if (event.getQuiesceState() == QuiesceState.QUIESCED)
            // stop interacting with the space to prevent getting exceptions
        else
            // resume interacting with the space
    }
}
```

## Security

`Manage PU` privileges are required to quiesce or unquiesce a Processing Unit in systems implementing [security](../security/).

{{% anchor recipes %}}
## Recipes

### Configuration

The Quiesce feature can be disabled by setting `com.gs.engine.disableQuiesceMode` to true.


{{% anchor implement-listener %}}
**Quiesce State Changed Listener**

Components in a Processing Unit can implement `QuiesceStateChangedListener`. Implementing service Objects as Spring Beans is recommended. Any component that implements this listener interface will be registered to receive state change events, and respond appropriately.

Data Grid platform components that are sensitive to quiesce changes include [Polling](../dev-java/polling-container-overview.html) and [Notify](../dev-java/notify-container-overview.html) containers. Adding support for state change event detection is as simple as implementing the same interface.

```java
public class CustomComponent implements QuiesceStateChangedListener /*, PollingContainer */ {
    // ...
    public void quiesceStateChanged(QuiesceStateChangedEvent event) {
        if (event.getQuiesceState() == QuiesceState.QUIESCED)
            // stop interacting with the space to prevent getting exceptions
        else
            // resume interacting with the space
    }
}
```

## Limitations 

The following limitations and open issues apply to Quiesce mode:

- The Processing Unit should be **intact** before triggering a quiesce request.
- Although Quiesce handles the Processing Unit restart, it is not resilient regarding sudden network disconnections (in XAP grid component machines). Therefore, under rare conditions the quiesce request must be repeated manually by the user. 
- Quiesce state changed events are propagated only to the components (beans) that are located within the Processing Unit context file ([pu.xml](../dev-java/configuring-processing-unit-elements.html)), therefore custom components are not aware of quiesce state changed events (even if the component implements [QuiesceStateChangedListener](#implement-listener-recipe)).
- Replication of the Quiesce state between primary and backup GSM is not yet supported. GSM failover may result in losing the quiesce state of the system. If this happens, you must repeat the quiesce request  in order to re-inform the GSM about the current quiesce state.
