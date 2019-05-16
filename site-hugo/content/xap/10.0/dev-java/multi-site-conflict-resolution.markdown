---
type: post100
title:  Conflict Resolution
categories: XAP100
parent: multi-site-replication-overview.html
weight: 500
canonical: auto
---

{{% ssummary %}}{{% /ssummary %}}




Multiple site conflict resolution is the ability to resolve conflicts caused when a Sink component attempts to perform operations on a local cluster and faces one of the following conflicts:

#### Data Conflicts

- Entry is already in space conflict - occurs when the sink attempts to write an entry which already exists in the local cluster.
- Entry is not in space conflict - occurs when the sink attempts to update an entry which doesn't exist in the local cluster.
- Entry version conflict - occurs when the sink attempts to update an entry with a newer/older version than the one which is in the local cluster.
- Entry is locked under transaction conflict - occurs when the sink attempts to update an entry in the local cluster which is locked under transaction.

#### Register Type Descriptor Conflict

Occurs when an attempt to register a type descriptor over gateway replication in the local cluster configured in the sink component fails.

#### Add Indexes Conflict

Occurs when an attempt to add indexes over gateway replication to the local cluster configured in the sink component fails.

# How Conflict Resolution Works

By default, operations that failed due-to transaction locks (EntryLockedUnderTransactionConflict) will be automatically retried a configurable amount of times. You may configure the time interval in-between retries. For other conflicts or when the number of retries exceeds, the conflict resolver is invoked. The conflict resolver gives the user the ability to abort or override conflicting operations.

# Default Behavior

The default behavior states that an operation which failed due-to a transaction lock will be retried 5 times with a time interval of 100ms in-between each retry. If no conflict resolver was specified, the default action for all conflicting operations is 'abort' so all conflicting operations will automatically be aborted (if an operation which is a part of a transaction fails, the entire transaction scope will be aborted).

# Configuration

Configuration is made in the Sink component's element using the 'error-handling' sub element. In the following example we'll see how to configure the number of retries to 20 with a time interval of 5 seconds between each retry and set a conflict resolver for the Sink gateway component.

The conflict resolver implementation should be an extension of "com.gigaspaces.cluster.replication.gateway.conflict.ConflictResolver".


```xml
<!-- gateway Sink component -->
<os-gateway:sink id="sink" local-gateway-name="NEW-YORK" gateway-lookups="gatewayLookups"
          local-space-url="jini://*/*/localSpace">
  <os-gateway:sources>
    <os-gateway:source name="LONDON" />
  </os-gateway:sources>

  <!-- Sink error handling configuration -->
  <os-gateway:error-handling conflict-resolver="conflictResolver" max-retries-on-tx-lock="5"
     tx-lock-retry-interval="100" />

</os-gateway:sink>

<!-- A conflict resolver implementation -->
<bean id="conflictResolver" class="com.gigaspaces.gateway.sink.MyConflictResolver" />
```

# Conflict Resolver Implementation

Lets examine the following conflict resolver implementation which aborts all conflicts executed from a source named "LONDON" and for other sources aborts on entry version conflict:


```java
public class MyConflictResolver extends com.gigaspaces.cluster.replication.gateway.conflict.ConflictResolver {

  @Override
  public void onDataConflict(String sourceGatewayName, DataConflict conflict) {
    // If source is "LONDON" -> abort all operations
    if (sourceGatewayName.equals("LONDON")) {
      conflict.abortAll();

    // Otherwise, abort if conflict is entry version conflict
    } else {
      for (DataConflictOperation operation : conflict.getOperations()) {
        if (operation.hasConflict() && operation.getConflictCause() instanceof EntryVersionConflict)
          operation.abort();
        else if (operation.supportsOverride())
          operation.override();
      }
    }
  }

}
```

We distinguish between two cases when processing DataConflict operations.

### Single Operations

When a single operation faces a conflict, the DataConflict will only hold this particular so conflict.getOperations() will have one operation in it.

For example, if our local site LONDON replicates data to a site named NEW-YORK:


```java
// The following is executed on a cluster at LONDON
londonGigaSpace.write(new Person("John Doe"));
londonGigaSpace.write(new Person("Jane Doe"));

// The two write operations will be replicated to the NEW-YORK site and if both
// of the operations will face a conflict, the conflict resolver will be invoked twice
// and on each of the invocations the conflict.getOperations() will hold the single
// conflicting operations.
```

In case the conflict resolver didn't resolve an operation (abort or override was not called for this operation) the operation will be aborted.

### Transactional Operations

When a transaction is committed, its enough that one of the transaction operation had a conflict and all of the transaction operations will be a part of the DataConflict and therefore when calling `conflict.getOperations()` all of the transaction operations are returned and its possible to identify the conflicting operations using the DataConflictOperation.hasConflict() method.

For example, We have a local site named LONDON which replicates data to a site named NEW-YORK. We write three Person objects to LONDON which will be replicated to NEW-YORK. If at least one of these operations will fail, all three operations will appear in DataConflict.getOperations()
when the conflict resolver onDataConflict method will be invoked.


```java
// The following is executed on a cluster at LONDON
PlatformTransactionManager ptm = new DistributedJiniTxManagerConfigurer().transactionManager();
GigaSpace londonGigaSpace = new GigaSpaceConfigurer(...).transactionManager(ptm).gigaSpace();
DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
TransactionStatus status = ptm.getTransaction(definition);

Person[] persons = new Person[3];
persons[0].setId(1);
persons[0].setName("John Doe");
persons[1].setId(2);
persons[1].setName("Jane Doe");
persons[2].setId(3);
persons[2].setName("George Woo");
londonGigaSpace.writeMultiple(persons);

ptm.commit(status);
```

If no decision was taken regarding any of the conflicts presented in the DataConflict all of the operations will be aborted.
Otherwise, if a decision has been made for at least one conflicting operation, only conflicting operations which had no decision made for them
will be aborted.

Since all of the transaction operations appear in the DataConflict, it's also possible to take action for operations which didn't have a conflict.
For example:


```java
// The following is executed on a cluster at LONDON
PlatformTransactionManager ptm = new DistributedJiniTxManagerConfigurer().transactionManager();
GigaSpace londonGigaSpace = new GigaSpaceConfigurer(...).transactionManager(ptm).gigaSpace();
DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
TransactionStatus status = ptm.getTransaction(definition);

Person person = new Person();
person.setId(10);
person.setName("Johny");

Person personToUpdate = new Person();
person.setId(20);
person.setName("Updated!");

londonGigaSpace.write(person);               // This operation will cause a conflict..

londonGigaSpace.takeById(Person.class, 5);   // No conflict..

// This operation will cause a conflict..
londonGigaSpace.write(personToUpdate, Lease.FOREVER, 0, UpdateModifiers.UPDATE_ONLY);

ptm.commit(status);
```

Now, lets examine a conflict resolver implementation which will abort the two first operations (write and take) and override the last (write) operation:


```java
public class MyConflictResolver extends com.gigaspaces.cluster.replication.gateway.conflict.ConflictResolver {

  @Override
  public void onDataConflict(String sourceGatewayName, DataConflict conflict) {

    // conflict.getOperation() contains three operations.. write, take and update.
    for (DataConflictOperation operation : conflict.getOperations()) {

      // Abort write operations or operations which didn't cause a conflict..
      // Note that the take operation didn't cause a conflict and yet we can abort it.
      if (operation.getOperationType() == OperationType.WRITE || !operation.hasConflict()) {
        operation.abort();

      // Override conflicting update operations..
      } else if (operation.getOperationType() == OperationType.UPDATE || operation.getOperationType() ==
              OperationType.PARTIAL_UPDATE) {
        if (operation.supportsOverride())
          operation.override();
      }

    }
  }

}
```

# Overriding Operations

Overriding conflicting operations has a different behavior for each operation.
The table below describes override behavior for each case:


|Operation|Conflict|Behavior|
|:--------|:-------|:-------|
|Write|Entry is already in space conflict|Update the existing entry in the local cluster with the provided one|
|Update|Entry is not in space conflict|Write the provided entry to the local cluster|
|Update|Entry version conflict|Update the existing entry in the local cluster with the provided one|
|Update|Entry is locked under transaction conflict|The original operation will be retried|

### Changing Entry Data On Conflict

It is possible to change the entry data before overriding the conflicting operation. In the following example, we have gateway replication from NEW-YORK to LONDON. We'll attempt to write a Person object to NEW-YORK which already exists in LONDON and we'll change the conflicting Person
object properties before overriding the operation:


```java
// Write a Person object to NEW-YORK
Person person = new Person();
person.setId(1000);
person.setName("John Doe");
person.setText("Success!");
newyorkGigaSpace.write(person);

// Our conflict resolver at LONDON
public class MyConflictResolver extends com.gigaspaces.cluster.replication.gateway.conflict.ConflictResolver {

  @Override
  public void onDataConflict(String sourceGatewayName, DataConflict conflict) {
    if (conflict.getOperations().length == 1) {
      DataConflictOperation operation = conflict.getOperations()[0];
      if (operation.hasConflict() && operation.getConflictCause() instanceof EntryAlreadyInSpaceConflict) {
        if (operation.supportsOverride()) {
          Person person = (Person) operation..getDataAsObject();
          // The entry in the local space will be updated with the changes
          // made to the operation's entry
          person.setText("Conflict!");
          operation.override();
        }
      }
    }
  }

}
```

# Metadata Conflicts

For both register type descriptor and add indexes conflicts it is not possible to take an action for the conflict
though you can identify such conflicts using the conflict resolver's onRegisterTypeDescriptorConflict and onAddIndexConflict methods.
For example:


```java
public class MyConflictResolver extends com.gigaspaces.cluster.replication.gateway.conflict.ConflictResolver {

  @Override
  public void onRegisterTypeDescriptorConflict(String sourceGatewayName, RegisterTypeDescriptorConflict conflict) {
    // Do something useful with the conflict..
  }

  @Override
  public void onAddIndexConflict(String sourceGatewayName, AddIndexConflict conflict) {
    // Do something useful with the conflict..
  }

}
```

# Limitations

{{%refer%}}
Please refer to the [Multi-Site Replication Limitations](./multi-site-replication-limitations.html) page.
{{%/refer%}}
