---
type: post123
title:  FIFO Grouping
categories: XAP123, OSS
parent: fifo-overview.html
weight: 200
---




The FIFO Grouping designed to allow efficient processing of events with partial ordering constraints. To better understand FIFO groups, let's first examine the constraints of total ordering, i.e. What it takes to process events in the exact order in which they arrive. There are two elements that effectively limit the scalability of processing events with total ordering:

- Maintaining the original order of arrival (the "natural FIFO order"), which requires the Space to maintain a central data structure (i.e. event queue) that is accessed every time an event enters the system. This can become a central bottleneck quite quickly and limit the throughput of the system.
- Ensuring in-order processing of events. This seems quite straight forward once you maintain the natural FIFO ordering, but in many cases, you want to ensure that a certain event will not be processed until its preceding event has been fully processed. This requirement effectively means that you can only process one event at a time, constraining the event processing to a single thread.

In most cases, your application does not require total ordering, but rather ordering within groups of events. Let's take a flight booking system as an example. Such an system should process the bookings for each flight one by one, to avoid conflicting bookings for the same seats and ensure fairness. But it can process many flights simultaneously, since there's no relevance to the order of processing across different flights. The FIFO groups feature allows you to implement this kind of scenario easily by designating a "FIFO group" field. The value of this field indicates the unique group identifier. In the above example, this would be the flight number, so effectively your application can process many bookings simultaneously for different flights, but it will never process two bookings for the same flight simultaneously. In more generic terms, the FIFO groups capability ensures the following:

- Within the same group, events will be processed in the order they arrived, and exclusively, meaning that only one event will be processed at a time, regardless of the number of event processors.
- Across groups, any number of events can be processed simultaneously.

{{% align center %}}
![fifo-group.jpg](/attachment_files/fifo-group.jpg)
{{% /align %}}

The FIFO-Grouping can be used with financial systems to process **trade orders**, in healthcare systems to processes **patient medical data**, with transportation systems to process **reservations**, with airlines systems to process **flight schedule** , with billing system to processes **payments**, etc. With the flight reservation system scenario several reservations can be processed simultaneously but the reservations of a particular fight must be processed exclusively and in FIFO order.

# How it works

FIFO-Grouping ('FG') enables reading/taking certain space entries in FIFO order (by order of insertion), and is mainly used as an enhancement of the openspaces polling-containers.  When a property is declared with the `@SpaceFifoGroupingProperty` annotation ('the FG designated property'), a read/take operation with the `FIFO_GROUPING_POLL` modifier will return all space entries that match the selection template in FIFO order. Different values of the FG property define groups of space entries that match that value - FIFO ordering exists within each group and not between different groups.

{{% note "Exclusivity"%}}
The selected group is locked until the operation is terminated- the operation transaction is committed/ aborted.  See the [Exclusivity](./fifo-grouping.html#Exclusivity) section for more elaborations.
{{% /note %}}

# Method Of Operation

- FG operations must be performed within transactions.
- Any class that supports FG is logically divided to groups according to the FG designated property. This property must be indexed and will be automatically indexed by the system if an index definition does not exist for it.  An additional data structure is kept for this property in order to assist in traversing the different groups.
- In the selecting template a null value will generally be rendered for this property which stands for bring any available group. An available group is any FG that matches the selection template and is not currently locked by another FG thread (see [Exclusivity](./fifo-grouping.html#Exclusivity) section).

- If the selecting template (Pojo) has a value for a property other than the FG designated property - this property can be indexed (like for any regular read/take operation) and in addition a `@SpaceFifoGroupingIndex` annotation can be added  to it  in order to assist in efficient traversal. In this case the system will create a compound index that contains this property and the FG designated property. For example, if a polling container is responsible for all the new reservations (reservations with processingState = NEW), then it is recommended to declare a SpaceFifoGroupingIndex on the processingState property. This would help to achieve better performance when searching for reservations of certain flight that have processingState = NEW.

# Exclusivity

Once a FG operation returns with a result, the relevant group(s) is locked to other FG threads until the transaction is terminated. The locked group is the group with the value returned in the FG designated property and matching the selecting template.
**The exclusivity rule is as follows:**
A group can be locked by a requesting template if no other template is locking the same FG designated property value, or - if another template is locking the same FG designated property value, the intersection between the 2 groups is null.

**For example:**
Lets assume we have an Order POJO template with property named label annotated as  `@SpaceFifoGroupingProperty` and property named state annotated as `@SpaceFifoGroupingIndex`.
If polling container A got a FG with Label = "LABEL1" using an Order POJO with State property = null, no other FG thread/container will be able to access the FG designated by "LABEL1".
If on the other hand polling container B got a FG with Label = "LABEL2" using an Order pojo with State property = 0, polling container C will be able to get FG with same label "LABEL2" using an Order pojo with State = 1, since the intersection between the groups is null. Exclusivity is released upon transaction termination.

# Interaction with non-FIFO Grouping templates

When a FG template locks a group, its first entry is locked under a transaction so it cannot be accessed by any destructive space operation. The other entries in the group are not physically locked and may be operated-upon by non FG templates. If a FG template which is intending to lock a group encounters its first entry locked under a transaction by a non FG template - this group is abandoned in order not to create a gap (by skipping the first entry)

# Setting the FIFO Grouping property

Specifying which property of a class is the FG property is done using annotations or gs.xml.

{{%tabs%}}
{{%tab "  Annotations "%}}


```java
@SpaceClass
public class FlightReservation
{
    	private FlightInfo flightInfo;
    	private Person customer;
	private State processingState;
    	...
	@SpaceFifoGroupingProperty(path = "flightNumber")
    	public FlightInfo getFlightInfo() {return flightInfo;}
   	public void setFlightInfo(FlightInfo flightInfo) {this.flightInfo = flightInfo;}
}
```

{{% /tab %}}
{{%tab "  XML "%}}


```xml
<gigaspaces-mapping>
    <class name="com.gigaspaces.examples.FlightReservation">
        	<fifo-grouping-property name="flightInfo" path=" flightNumber" />
    </class>
</gigaspaces-mapping>
```

{{% /tab %}}
{{% /tabs %}}

# Setting FIFO Grouping index

Specifying which properties of a class are a FG index is done using annotations or gs.xml.

{{%tabs%}}
{{%tab "  Annotations "%}}


```java
@SpaceFifoGroupingIndex
public State getProcessingState() {return processingState;}
public void setProcessed (State processingState) {this.processingState = processingState;}
@SpaceFifoGroupingIndex(path = "id")
public Person getCustomer() {return customer;}
public void setCustomer (Person customer) {this.customer = customer;}
```

{{% /tab %}}
{{%tab "  XML "%}}


```xml
<gigaspaces-mapping>
	<class name="com.gigaspaces.examples.FlightReservation />
		<property name="processingState">
			<fifo-grouping-index />
		</property>
		<property name="customer">
			<fifo-grouping-index  path="id"/>
		</property>
</gigaspaces-mapping>
```

{{% /tab %}}
{{% /tabs %}}

# Working Patterns

## Take

Take the first entry from an available FG (openspaces `SingleTakeReceiveOperationHandler`).
**Take multiple:** take entries from available FG. No ordering between different groups. Entries of same groups may not be in adjacent positions. (openspaces `MultiTakeReceiveOperationHandler`)

## Read

read the first entry from an available FG. In this pattern entry property may be changed and the operation ending with update. For example- a ProcessingState property which is initially  0, some FG polling container querying for 0 and changing it to 1, other container querying for 1 and changing to 2 etc until a container querying for the final state with take. Note that in such implementation the ProcessingState property should better be defined as  `@SpaceFifoGroupingIndex`.
(openspaces `ExclusiveReadReceiveOperationHandler`)

## Read multiple

read entries from available FG. No ordering between different groups. Entries of same groups may not be in adjacent positions. (openspaces `MultiExclusiveReadReceiveOperationHandler`)

## Notify

FIFO Grouping designed to be used with **Polling container only**.  If you are looking getting notifications when consuming objects in FIFO Grouping mode, you should use a polling container running in FIFO Grouping mode that will write "a command object" with a small lease that will trigger a Notify container Running in FIFO mode using the relevant template for the command object.

# Query operations with FIFO Grouping

## Using READ/TAKE modifiers

To execute read/take operations with FG, use the `TakeModifiers.FIFO_GROUPING_POLL` modifier. For example:


```java
space.take(new FlightReservation(), timeout, TakeModifiers.FIFO_GROUPING_POLL);
```

If class FlightReservation isn't declared with a FG property, an exception will be thrown.

## Using Polling container

When registering for polling events use the `AbstractFifoGroupingReceiveOperationHandler.setUseFifoGrouping(true)`
to instruct the space that events should be sent to the client in FIFO order (grouping by the fifoGroupingProperty that should have been declared for the space class).

-  `ExclusiveReadReceiveOperationHandler` example:

{{%tabs%}}
{{%tab "Annotation"%}}

```java
@EventDriven @Polling(concurrentConsumers = 3, maxConcurrentConsumers = 5)
@TransactionalEvent
public class SimpleListener
{
	@EventTemplate
	FlightReservation unprocessedData() {
		 return new FlightReservation(null, State.NEW_RESERVATION);
	}
	@SpaceDataEvent
	public FlightReservation eventListener(FlightReservation event) {
	        //process reservation here
	}
	@ReceiveHandler
	ReceiveOperationHandler receiveHandler() {
		handler = new ExclusiveReadReceiveOperationHandler();
		handler.setUseFifoGrouping(true);
		return handler;
	}
}
```
{{% /tab %}}

{{%tab "Namespace" %}}

```xml
    <bean id="simpleListener" class="SimpleListener" />

    <os-events:polling-container id="eventContainer" giga-space="gigaSpace" concurrent-consumers="3" max-concurrent-consumers="5" >
        <os-events:tx-support tx-manager="transactionManager"/>
        <os-events:receive-operation-handler>
            <bean class="org.openspaces.events.polling.receive.ExclusiveReadReceiveOperationHandler" >
                <property name="useFifoGrouping" value="true" />
            </bean>
        </os-events:receive-operation-handler>
        <os-core:template>
            <bean class="FlightReservation">
                <property name="processed" value="false"/>
                <constructor-arg name="..."><null/></constructor-arg>
                <constructor-arg name="state">
                    <bean class="State" factory-method="valueOf">
                        <constructor-arg>
                            <value>NEW_RESERVATION</value>
                        </constructor-arg>
                    </bean>
                </constructor-arg>
            </bean>
        </os-core:template>
        <os-events:listener>
            <os-events:method-adapter method-name="eventListener">
                <os-events:delegate ref="simpleListener"/>
            </os-events:method-adapter>
        </os-events:listener>    
    </os-events:polling-container>
```

{{% /tab %}}

{{%tab "Code"%}}

```java
AbstractFifoGroupingReceiveOperationHandler receiveOperationHandler = new ExclusiveReadReceiveOperationHandler();
FlightReservationEventListener eventListener = new FlightReservationEventListener();
receiveOperationHandler.setUseFifoGrouping(true);
SimplePollingEventListenerContainer pollingEventListenerContainer =
         new SimplePollingContainerConfigurer(transactionGigaSpace)
.transactionManager(platformTransactionManager)
.template(new FlightReservation(null, State.NEW_RESERVATION))
.receiveOperationHandler(receiveOperationHandler)
.eventListener(eventListener)
.create();
pollingEventListenerContainer.start();
```
{{% /tab %}}

{{% /tabs %}}


# SpaceIndex

Declaring both `spaceFifoGroupingProperty` or `SpaceFifoGroupingIndex` and `spaceIndex` (type `EQUAL` or `ORDERED`) with the same path will yield one index with the `spaceIndex` type. Declaring only `spaceFifoGroupingProperty` or `spaceFifoGroupingIndex` will yield one index, type `EQUAL`.

# Inheritance

All property's FG declarations (both `SpaceFifoGroupingProeprty` and `SpaceFifoGroupingIndex`) are inherited in sub classes.

- Overriding of `SpaceFifoGroupingProperty` is not allowed.
- Overriding of `SpaceFifoGroupingIndex` is allowed in order to add more FG indexes.
For example, declaring `SpaceFifoGroupingIndex(path="a")`, overriding in subclass and declaring `SpaceFifoGroupingIndex(path="b")` will yield two FG indexes: property a index and property b index (both of type `EQUAL` if no `SpaceIndex` with `ORDERED` type was declared).

# Considerations

- FG is not supported with a Space using [LRU Cache policy](./lru-cache-policy.html) and [Space Persistency](./space-persistency-overview.html) enabled.
- Cross partitioning of groups is not supported (same limitation as in regular FIFO operations).
- `@SpaceFifoGroupingProperty` and `@SpaceFifoGroupingIndex` cannot be used as dynamic indexes.
- `@SpaceFifoGroupingProperty` and `@SpaceFifoGroupingIndex` cannot be used  as collection indexes.
(e.g. declaring `@SpaceFifoGroupingProperty( path="\[*\]")` is not allowed).

- FIFO operation is not supported for FG template - it is ignored.
- If the template is a SQL query template, only queries that can be performed in a single call to the space are supported (an exception is thrown).
- FG operations must be performed under a transaction.
- There can be only one FG property per type.
- A FG index cannot be declared on some type's property without existence of a FG property for that type.
- The following annotations cannot be used with the FG property/index annotations for the same property:
    - `spaceVersion`
    - `spacePersist`
    - `spaceLeaseExpiration`
    - `spaceDynamicProperties`
    - `spaceExclude`.
    - `SpaceStorageType` with type Different than OBJECT

# Performance Hints

- If an indexed-property is used in the FG template - define it as `@SpaceFifoGroupingIndex` if footprint is not a crucial issue.
- Since each successful operation locks a group (until its completion) there is no use in using # of threads which is greater than the # of potential groups.
- For indexed properties (other than the `@SpaceFifoGroupingProperty`) which are used with >, >=, < , <= conditions - the index cannot be used in the scan (because no FIFO order is maintained  in an ordered index).
