---
type: post121
title:  Pessimistic Locking
categories: XAP121
parent: transaction-overview.html
weight: 500
canonical: auto
---

{{%ssummary%}}{{%/ssummary%}}

The pessimistic locking protocol provides data consistency in a multi user transactional environment. It should be used when there might be a large number of clients trying to read and update the same object(s) at the same time. This protocol utilize the system resources (CPU, network) in a very efficient manner both at the client and space server side.

This scenario is different from the optimistic locking protocol since we assume with the pessimistic locking protocol, that every object that is read and retrieved from the space will eventually be updated where the transaction duration is relatively very short.

To enforce a sole reader and sole updater for the object we explicitly lock the object via a transaction using the [ReadModifiers.EXCLUSIVE_READ_LOCK]({{% api-javadoc %}}/index.html?com/j_spaces/core/client/ReadModifiers.html).

When performing read operations without locking the object via a transaction, users can immediately perform an update operation, without having to wait for other users to complete their transaction (since there is none). However, there is no guarantee that the update operation will be performed on the latest version of the object.



The optimistic locking protocol assumes that a client that retrieved an object from the space, might or might not update the object, so it never locks the object when it is reading it. This makes the object accessible for large amount of users avoiding the need to wait for the lock to be released. Using the optimistic locking protocol when every object that is read is also updated, will consume unnecessary resources at the client and space side since all the clients will try to get the latest version of the object when updating it.
 

{{%note "To implement the pessimistic locking protocol you should have the following:"%}}

- Start a transaction. You may use spring automatic transaction demarcation by annotating the method as `@Transactional`.
- Read the object using the `Exclusive read Lock` modifier.  This will block other transactions from reading the same object. If there is another transaction locking the object, the read operation will wait for the transaction (according to the specified timeout) to be completed (commit or abort) and return the latest version of the object. You may read multiple objects during this phase.
- Modify the object data. Execute your business logic. This might involve calculations, etc.
- Update the object within the space. You may update multiple objects.
- Commit the transaction. This will happen automatically when the transactional method will be completed.

{{%/note%}}

{{% note %}} 
Having the above executed at the space side via [Executor Based Remoting](./executor-based-remoting.html) or [Task Executors](./task-execution-over-the-space.html) is highly recommended. This will shorten the transaction time period since all space operations will be executed against a colocated space. 
{{%/note%}}

# Example

See below example illustrating the usage of the exclusive locking mode implementing pessimistic locking:


```java
@Transactional (propagation=Propagation.REQUIRES_NEW)
public void executeOrders(Integer orderIDs[]) throws Exception
{
	Order orders[] = new Order [orderIDs[].length];
	for (int i=0;i<orderIDs.length;i++)
	{
		orders[i]= space.readById(Order.class, orderIDs[i],orderIDs[i],5000,ReadModifiers.EXCLUSIVE_READ_LOCK);
                if (orders[i] != null)
		     orders[i].setStatus(DONE);
	}
	Object rets[] = space.updateMultiple(orders, new long[orderIDs.size()], UpdateModifiers.UPDATE_ONLY);

	for (int i=0;i<rets.length;i++)
	{
		if (rets[i] == null)
		{
			throw (new ReadTimeOutException("can't update object " + orders[i]));
		}

		if (rets[i] instanceof Exception)
		{
			if (rets[i] instanceof EntryNotInSpaceException)
			{
				throw (EntryNotInSpaceException)rets[i];
			}
			else if (rets[i] instanceof OperationTimeoutException )
			{
				throw (OperationTimeoutException)rets[i];
			}
			else
			{
				throw (Exception)rets[i];
			}
		}
	}
}
```

# Important Considerations

- Starting with XAP 7.1.2 GigaSpaces throws `java.lang.IllegalArgumentException: Using EXCLUSIVE_READ_LOCK modifier without a transaction is illegal` exception as a protection mechanism when performing exclusive read **without** using a transaction. You must use a transaction when using exclusive read lock.
- Pessimistic locking decreases concurrency because objects might be locked for long periods of time.
- Locking objects also imposes more work on the space server since read operations are transactional.
- When there is a conflict, users have to "get in line" to wait for an object to become unlocked. These users may, themselves, already have other objects locked while they're waiting. If any of these objects needed by a user are further ahead in line, then you will have a "deadlock," which can be difficult to manage.
