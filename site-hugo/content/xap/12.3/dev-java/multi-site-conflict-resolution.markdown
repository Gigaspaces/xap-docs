---
type: post123
title:  Conflict Resolution
categories: XAP123, ENT
parent: multi-site-replication-overview.html
weight: 500
canonical: auto
---


Multiple site conflict resolution is the ability to resolve conflicts caused when a Sink component attempts to perform operations on a local cluster and faces one of the following conflicts:

**Data Conflicts**<br>
- Entry is already in space conflict - occurs when the sink attempts to write an entry which already exists in the local cluster.<br>
- Entry is not in space conflict - occurs when the sink attempts to update an entry which doesn't exist in the local cluster.<br>
- Entry version conflict - occurs when the sink attempts to update an entry with a newer/older version than the one which is in the local cluster.<br>
- Entry is locked under transaction conflict - occurs when the sink attempts to update an entry in the local cluster which is locked under transaction.

**Register Type Descriptor Conflict**<br>
Occurs when an attempt to register a type descriptor over gateway replication in the local cluster configured in the sink component fails.

**Add Indexes Conflict**<br>
Occurs when an attempt to add indexes over gateway replication to the local cluster configured in the sink component fails.

**How Conflict Resolution Works**<br>
By default, operations that failed due-to transaction locks (EntryLockedUnderTransactionConflict) will be automatically retried a configurable amount of times. You may configure the time interval in-between retries. For other conflicts or when the number of retries exceeds, the conflict resolver is invoked. The conflict resolver gives the user the ability to abort or override conflicting operations.

**Default Behavior**<br>
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

## Single Operations

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

## Transactional Operations

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


# Example

In this example, the pu.xml adds the required space class with the `abortClasses` property. 
Whenever the target space has conflicting data in comparison to incoming data, it will call the `MyConflictResolver.onDataConflict` method
which will take the following actions:
        
- Process all DataConflict objects. Iterate over all `DataConflictOperation` for a DataConflict.
- It will fetch conflicting data objects from the target space using the space proxy.
- It will either abort or override data at the target space based on the existents of the class name in the abortClasses list or not.
- The objects(from source and target) can be added to a log-file for audit purpose or can be used for other purposes such as sending alerts.
        
{{%tabs%}}
{{%tab "pu.xml"%}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:os-events="http://www.openspaces.org/schema/events"
       xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
       xmlns:os-sla="http://www.openspaces.org/schema/sla"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{%currentversion%}}/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{%currentversion%}}/remoting/openspaces-remoting.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{%currentversion%}}/sla/openspaces-sla.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{%currentversion%}}/core/gateway/openspaces-gateway.xsd"> 
  
    <os-gateway:sink id="sink" local-gateway-name="GB" gateway-lookups="gatewayLookups"
                     local-space-url="jini://*/*/wanSpaceGB">
        <os-gateway:sources>
            <os-gateway:source name="US"/>
        </os-gateway:sources>
        
        <!-- Sink error handling configuration -->
		<os-gateway:error-handling conflict-resolver="conflictResolver" max-retries-on-tx-lock="5" tx-lock-retry-interval="100" />
    </os-gateway:sink>    
	
	<os-core:distributed-tx-manager id="transactionManager"/>

 	<os-core:space id="wanSpaceGB" url="jini://*/*/wanSpaceGB?locators=127.0.0.1:4366" />
	<os-core:giga-space id="gigaWanSpaceGB" space="wanSpaceGB" tx-manager="transactionManager"/>
	
    <!-- A conflict resolver implementation -->
	<bean id="conflictResolver" class="com.gigaspaces.wangateway.sink.MyConflictResolver">
		<property name="gigaWanSpaceGB" ref="gigaWanSpaceGB"/>
		<property name="abortClasses">
			<list>
				<value>com.j_spaces.examples.benchmark.messages.MessagePOJO</value>
			</list>
		</property>
	</bean>
	
    <os-gateway:lookups id="gatewayLookups">
        <os-gateway:lookup gateway-name="US" host="127.0.0.1" discovery-port="10768" communication-port="7100"/>
        <os-gateway:lookup gateway-name="GB" host="127.0.0.1" discovery-port="10769" communication-port="8100"/>
    </os-gateway:lookups>

</beans>
```
{{%/tab%}}

{{%tab "MyConflictResolver.java"%}}
```java
public class MyConflictResolver extends ConflictResolver {
	private GigaSpace gigaWanSpaceGB;
	private List<String> abortClasses;

	public void setGigaWanSpaceGB(GigaSpace gigaWanSpaceGB) {
		this.gigaWanSpaceGB = gigaWanSpaceGB;
	}

	public void setAbortClasses(List<String> abortClasses) {
		this.abortClasses = abortClasses;
	}

	public MyConflictResolver() {
		super();
	}

	@Override
	public void onDataConflict(String sourceGatewayName, DataConflict conflict) {
		System.out.println("---Start from MyConflictResolver.onDataConflict --");

		System.out.println("sourceGatewayName=" + sourceGatewayName);
		System.out.println("conflict.toString() " + conflict.toString());
		System.out.println("conflict.getClass().getCanonicalName()=" + conflict.getClass().getCanonicalName());
		System.out.println("conflict.getOperations()=" + conflict.getOperations().toString());
		System.out.println("---End from MyConflictResolver.onDataConflict --");

		try {
			for (DataConflictOperation operation : conflict.getOperations()) {
				String canonicalName = operation.getDataAsDocument().getTypeName();
				SpaceDocument remoteDocument = operation.getDataAsDocument();
				System.out.println("---operation start---");
				System.out.println("operation.getUid()=" + operation.getUid());
				System.out.println("remoteDocument=" + remoteDocument);
				Object spaceIdValue = operation.getSpaceId();
				System.out.println("conflict class is " + canonicalName);

				GigaSpaceTypeManager gigaSpaceTypeManager = gigaWanSpaceGB.getTypeManager();
				if (gigaSpaceTypeManager == null) {
					System.out.println("gigaSpaceTypeManager is null");
				} else {
					System.out.println("gigaSpaceTypeManager is not null");
				}
				SpaceTypeDescriptor spaceTypeDescriptor = gigaSpaceTypeManager.getTypeDescriptor(canonicalName);
				if (spaceTypeDescriptor == null) {
					System.out.println("spaceTypeDescriptor is null");
				} else {
					System.out.println("spaceTypeDescriptor is not null");
				}
				Object routingValue = getRoutingValue(remoteDocument, spaceTypeDescriptor);
				IdQuery<SpaceDocument> idQuery = null;
				Object localObject = null;
				if (routingValue == null) {
					idQuery = new IdQuery<SpaceDocument>(canonicalName, remoteDocument, spaceIdValue);
					localObject = gigaWanSpaceGB.readById(idQuery);
				} else {
					System.out.println("routingValue:" + routingValue + ", spaceIdValue:" + spaceIdValue);
					idQuery = new IdQuery<SpaceDocument>(canonicalName, spaceIdValue, routingValue);
					localObject = gigaWanSpaceGB.readById(idQuery);
				}
				if (localObject == null) {
					System.out.println("localObject is null for operation.getUid()=" + operation.getUid()
							+ ", spaceIdValue=" + spaceIdValue);
				} else {
					System.out.println("localObject found of spaceIdValue=" + localObject);
				}

				System.out.println("-- remoteDocument=" + remoteDocument);
				System.out.println("operation.getConflictCause()=" + operation.getConflictCause());
				System.out.println("operation.getConflictCause()=" + operation.getConflictCause());
				System.out.println("operation.getResolveAttempt()=" + operation.getResolveAttempt());
				System.out.println("operation.hasConflict()=" + operation.hasConflict());
				System.out.println("operation.supportsOverride()=" + operation.supportsOverride());
				System.out.println("operation.supportsAbort()=" + operation.supportsAbort());
				if (abortClasses.contains(canonicalName)) {
					System.out.println("canonicalName is in abort list so abort operation.");
					operation.abort();
				} else {
					System.out.println("canonicalName is overriden.");
					operation.override();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getRoutingValue(SpaceDocument instance, SpaceTypeDescriptor spaceTypeDescriptor) {
		try {
			String idPropertyName = spaceTypeDescriptor.getIdPropertyName();
			String routingPropertyName = spaceTypeDescriptor.getRoutingPropertyName();
			System.out.println("Id Property Name:" + idPropertyName);
			System.out.println("Routing Property Name:" + routingPropertyName);
			return instance.getProperty(routingPropertyName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
```
{{%/tab%}}
{{%/tabs%}}

{{%note%}}
The space classes don't have to be part of the deployed WAN GW PU jar. You can leverage the [Space Document API](./document-api.html) 
as a generic resolver to access incoming replicated objects and also the target copy (sink local space) allowing value comparison.

This will work also with any Java / .NET / Rest API / JDBC API implementations. 
{{%/note%}}
