---
type: post123
title:  Event Exception Handler
categories: XAP123, OSS
parent: event-processing.html
weight: 500
---





XAP provides a mechanism allowing to hook into how exception raised by event listeners are handled, specifically when the event listeners are executed under the context of a transaction.


An event exception handler should implement the following interface:


```java
public interface EventExceptionHandler<T> {

    /**
     * A callback when a successful execution of a listener.
     *
     * @param data      The actual data object of the event
     * @param gigaSpace A GigaSpace instance that can be used to perform additional operations against the
     *                  space
     * @param txStatus  An optional transaction status allowing to rollback a transaction programmatically
     * @param source    Optional additional data or the actual source event data object (where relevant)
     */
    void onSuccess(T data, GigaSpace gigaSpace,
                   TransactionStatus txStatus, Object source) throws RuntimeException;

    /**
     * A callback to handle exception in an event container. The handler can either handle the exception
     * or propagate it.
     *
     * <p>If the event container is transactional, then propagating the exception will cause the transaction to
     * rollback, which handling it will cause the transaction to commit.
     *
     * <p>The TransactionStatus can also be used to control if the transaction
     * should be rolled back without throwing an exception.
     *
     * @param exception The listener thrown exception
     * @param data      The actual data object of the event
     * @param gigaSpace A GigaSpace instance that can be used to perform additional operations against the
     *                  space
     * @param txStatus  An optional transaction status allowing to rollback a transaction programmatically
     * @param source    Optional additional data or the actual source event data object (where relevant)
     */
    void onException(ListenerExecutionFailedException exception, T data,
                     GigaSpace gigaSpace, TransactionStatus txStatus, Object source) throws RuntimeException;
}
```

If we take the following simple implementation of the event listener interface:


```java
public class SimpleEventExceptionHandler implements EventExceptionHandler {
    public void onSuccess(T data, GigaSpace gigaSpace,
                          TransactionStatus txStatus, Object source) throws RuntimeException {
        // process success
    }

    public void onException(ListenerExecutionFailedException exception, Object data,
                            GigaSpace gigaSpace, TransactionStatus txStatus, Object source) throws RuntimeException {
        // process failure
    }
}
```

Here is how it can be configured:

{{%tabs%}}
{{%tab "  Annotation "%}}


```java
@EventDriven @Polling
public class SimpleListener {

    @ExceptionHandler
    public EventExceptionHandler exceptionHandler() {
        // can return this is SimpleListener implemented EventExceptionHandler
        return new SimpleEventExceptionHandler();
    }

    @EventTemplate
    Data unprocessedData() {
        Data template = new Data();
        template.setProcessed(false);
        return template;
    }

    @SpaceDataEvent
    public Data eventListener(Data event) {
        //process Data here
    }
}
```

{{% /tab %}}
{{%tab "  Namespace "%}}


```xml
<bean id="simpleExceptionHandler" class="SimpleEventExceptionHandler" />

<os-events:x-container ...>
    <!-- ... -->

    <os-events:exception-handler ref="simpleExceptionHandler" />
</os-events>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="simpleExceptionHandler" class="SimpleEventExceptionHandler" />

<bean id="eventContainer" class="...">
    <!-- ... -->

    <property name="exceptionHandler" ref="simpleExceptionHandler" />
</bean>
```

{{% /tab %}}
{{% /tabs %}}

# Using the Event Exception Handler

One of the main use cases that an Exception Handler can be used for is to filter out poison messages. For example, with a polling container, if the event data (the message) can't be processed, and the polling container is transactional, it will continue to retry the message indefinitely. It will start a transaction, perform a take, try and process the message, throwing an exception in the process, and rolling back the transaction causing the take operation to be rolled back.

If the type of the exception is know to be unrecoverable, an exception handler can be registered that will check the exception type (the cause of the ListenerExecutionFailedException), detect it, and not re-throw an exception, but instead write that entry wrapped in a "Poison Message" entry back into the space creating a dead letter queue that can be processed later on.

A retry counter can also be handled by creating a generic interface, for example called `RetryMessageEntry`, which certain messages will implement. That interface will allow to increment a counter and reset it. The counter field will be part of the entry (i.e. persisted in the space).

When an exception occurs, the retry counter will be incremented. If its under a specific threshold, the data object will be rewritten back to the space with the incremented counter (causing it to be taken again by the polling container). No exception will be raised in this case, as we want the transaction to be committed with the updated counter. Another option is to write a new entry with an updated counter, if there might have been side affects to the listener that the transaction should not commit.

If the threshold has been breached, the same poising message handling described above can be applied.

Its important to note that the exception handler `onException` and `onSuccess` operate under the existing on going transaction started by the polling container. Doing something outside of a transaction can be done by using a `GigaSpace` instance that is not associated with a transaction manager.


# Example

Here is an example where we create Purchase Orders that need to be processed. The `NewOrderProcessor` will try to process the Purchase Order. If the processing fails it will retry two times. If it can't process the order a `PoProcessException` is thrown that will be handled by the `PoEventExceptionHandler`.
The PoEventExceptionHandler will update the `state` of the PurchaseOrder to `UNPROCESSABLE` and write it back into the Space. The order will not be seen by the `NewOrderProcessor` again since its template specified the status to be `NEW`.

{{%tabs%}}
{{%tab " Program "%}}

```java
public class Program {

	private static Logger logger = LoggerFactory
			.getLogger(Program.class);

	public static void main(String[] args) throws InterruptedException {

		GigaSpace space = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer(
				"sandboxSpace")).gigaSpace();

        // Register the Processor
		registerListener(space);

		Thread.sleep(1000);

        // Create a Purchase Order
		PurchaseOrder po = new PurchaseOrder();
		//po.setNumber("12345");
		po.setId(UUID.randomUUID());
		po.setState(EPurchaseOrderState.NEW);

        // Write it into the Space
		space.write(po);

		Thread.sleep(1000);

        // Read all not processable PO's
		SQLQuery<PurchaseOrder> query = new SQLQuery<PurchaseOrder>(
				PurchaseOrder.class, "state = ?");
		query.setParameter(1, EPurchaseOrderState.UNPROCESSABLE);

		for (PurchaseOrder pu : space.readMultiple(query)) {
			logger.debug("PurchaseOrder in Space "+ pu);
		}

		System.exit(1);
	}

	private static void registerListener(GigaSpace space) {
		SimplePollingEventListenerContainer pollingEventListenerContainer = new SimplePollingContainerConfigurer(
				space).eventListenerAnnotation(new NewOrderProcessor())
				.pollingContainer();

		pollingEventListenerContainer.start();
	}
}
```
{{%/tab%}}

{{%tab " PurchaseOrder "%}}

```java
@SpaceClass
public class PurchaseOrder {

	private int retryCounter = 0;

	private UUID id;

	private String number;

	private EPurchaseOrderState state;

	@SpaceId
	public UUID getId() {
		return id;
	}

	public int getRetryCounter() {
		return retryCounter;
	}

	public void setRetryCounter(int retryCounter) {
		this.retryCounter = retryCounter;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public EPurchaseOrderState getState() {
		return state;
	}

	public void setState(EPurchaseOrderState state) {
		this.state = state;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "PurchaseOrder [retryCounter=" + retryCounter + ", id=" + id
				+ ", number=" + number + ", state=" + state + "]";
	}
}
```
{{%/tab%}}

{{%tab " NewOrderProcessor "%}}

```java
@EventDriven
@Polling(gigaSpace = "sandboxSpace")
@TransactionalEvent
public class NewOrderProcessor {

	private static Logger logger = LoggerFactory
			.getLogger(NewOrderProcessor.class);

	@ExceptionHandler
	public EventExceptionHandler<PurchaseOrder> exceptionHandler() {
		return new PoEventExceptionHandler();
	}

	@EventTemplate
	SQLQuery<PurchaseOrder> unprocessedData() {
		SQLQuery<PurchaseOrder> template = new SQLQuery<PurchaseOrder>(
				PurchaseOrder.class, "state = ?");
		template.setParameter(1, EPurchaseOrderState.NEW);
		return template;
	}

	@SpaceDataEvent
	public PurchaseOrder eventListener(PurchaseOrder po)
			throws PoProcessingException {

		try {
			if (po.getNumber() == null) {
				throw new Exception("PO Number can't be null");
			}

			logger.debug("Processing PO ");
			// do some processing
			// change the state
			po.setState(EPurchaseOrderState.PROCESSED);
			// write back the PO to the space
			return po;

		} catch (Exception ex) {
			logger.debug("handling the exception for the: "
					+ (po.getRetryCounter() + 1) + " time");

			if (po.getRetryCounter() < 2) {
				logger.debug("Max retry count reached throwing exception");
				throw new PoProcessingException(
						"Unable to process PO after three tries");
			} else {
				po.setRetryCounter(po.getRetryCounter() + 1);
				return po;
			}
		}
	}
}
```
{{%/tab%}}

{{%tab " PoEventExceptionHandler "%}}

```java
public class PoEventExceptionHandler implements
		EventExceptionHandler<PurchaseOrder> {

	private static Logger logger = LoggerFactory
			.getLogger(PoEventExceptionHandler.class);

	public void onSuccess(PurchaseOrder po, GigaSpace space,
			TransactionStatus txStatus, Object obj) throws RuntimeException {
	}

	public void onException(ListenerExecutionFailedException exception,
			PurchaseOrder po, GigaSpace space, TransactionStatus txStatus,
			Object obj) throws RuntimeException {

		if (exception.getCause() instanceof PoProcessingException) {
			logger.debug("Dealing with the exception, change the status to UNPROCESSABLE and write it back into the space");
			po.setState(EPurchaseOrderState.UNPROCESSABLE);
			space.write(po);
		}
		else
		{
		    // Handle other exceptions
		}
	}
}
```
{{%/tab%}}

{{%tab " PoProcessingException "%}}

```java
public class PoProcessingException extends Exception{

	public PoProcessingException(String string) {
		super(string);
	}
}
```
{{%/tab%}}

{{%tab " EPurchaseOrderState "%}}

```java
public enum EPurchaseOrderState {
  NEW, PROCESSED, UNPROCESSABLE
}
```
{{%/tab%}}


{{%/tabs%}}


When you run the above example you will see the following output:


```bash
18:56:58.968 [GS-SimplePollingEventListenerContainer-1] DEBUG x.s.e.e.NewOrderProcessor - handling the exception for the  1 time
18:56:58.971 [GS-SimplePollingEventListenerContainer-1] DEBUG x.s.e.e.NewOrderProcessor - handling the exception for the 2 time
18:56:58.971 [GS-SimplePollingEventListenerContainer-1] DEBUG x.s.e.e.NewOrderProcessor - handling the exception for the 3 time
18:56:58.971 [GS-SimplePollingEventListenerContainer-1] DEBUG x.s.e.e.NewOrderProcessor - Max retry count reached throwing exception
18:56:58.972 [GS-SimplePollingEventListenerContainer-1] DEBUG x.s.e.e.PoEventExceptionHandler - Dealing with the exception, change the status to UPROCESSABLE and write it back into the space
18:56:59.968 [main] DEBUG x.s.e.e.Program - PurchaseOrder in Space PurchaseOrder [retryCounter=2, id=c47e2879-ca6a-4531-b073-3f5c09f658cd, number=null, state=UNPROCESSABLE]
```
