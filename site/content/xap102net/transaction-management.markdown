---
type: post102net
title:  Transaction Management
categories: XAP102NET
parent: transaction-overview.html
weight: 100
---



{{% ssummary %}}{{% /ssummary %}}
This section presents the transaction management programming model and its main features.

![tx_manager.jpg](/attachment_files/tx_manager.jpg)

With the XAP .Net transaction model the developer is responsible for explicitly starting and managing the transaction. You obtain an object representing the underlying space transaction by calling `GigaSpacesFactory.CreateDistributedTransactionManager`.  This call returns an implementation of the `ITransactionManager` interface used to create the transaction using the `ITransactionManager.Create()` call. This return `ITransaction` object that should be used with every space operations that participant with the transaction. Once you would like to commit the transaction call the `ITransaction.Commit()`.
If any error occurred, you need to abort the transaction by calling `ITransaction.Abort()`. To clean up the transaction resources call the `ITransaction.Dispose()`.

# Usage

Suppose we have an order processing system, and we need to validate incoming orders before they continue to be processed. Our code could look something like this:


```csharp
public void ProcessNewOrder(ISpaceProxy space)
{
    // Get an order which requires processing:
    Order order = space.Take<Order>(new SqlQuery<Order>("Status='New'"));
    if (order != null)
    {
        // Check if order is valid and update its status:
        order.Status = IsValid(order) ? "Valid" : "Invalid";
        // Write the updated order back to the space:
        space.Write(order);
    }
}
```

Naturally, this code is not safe because if something goes wrong between the Take and Write operations the order is lost. We can use transactions to make it safer:


```csharp
public void ProcessNewOrder(ISpaceProxy space, ITransactionManager txnManager)
{
    // Create a transaction using the transaction manager:
    using (ITransaction txn = txnManager.Create())
    {
        try
        {
            // Get an order which requires processing using the transaction:
            Order order = space.Take<Order>(new SqlQuery<Order>("Status='New'"), txn);
            if (order != null)
            {
                // Check if order is valid and update its status:
                order.Status = IsValid(order) ? "Valid" : "Invalid";
                // Write the updated order back to the space using the transaction:
                space.Write(order, txn);
            }
            // Commit the transaction - all operations are finalized.
            txn.Commit();
        }
        catch (Exception ex)
        {
            // Abort the transaction - all operations are rolled back.
            txn.Abort();
        }
    }
}
```

Let's look at the changes we've made:

1. The method now receives an additional argument which is a transaction manager, which is used to create a transaction.
2. The transaction is created in a **using** block, to ensure it is automatically disposed when done.
3. The business logic code is wrapped in a **try-catch** block, and we've added a **commit** upon successful execution and **abort** if an exception occurred.
4. The **Take** and **Write** operations now use the **txn** to tell the space to perform them under that transaction.

Last but not least, we now need to provide a transaction manager when invoking **ProcessNewOrder()**. This can be done by invoking `GigaSpacesFactory.CreateDistributedTransactionManager()`. Of course there's no need to create a transaction manager each time we create a transaction, which is why we left it out of the method. Usually the transaction manager is created once upon application initialization and used throughout the application.

{{% tip title="Distributed vs. Local transaction manager "%}}
In previous versions, users had to choose between using the distributed and local transaction manager, since the distributed manager was slower and the local did not support multiple partitions. The 8.0 release includes significant improvements in the distributed manager which makes this choice redundant. The local transaction manager has been deprecated and will be removed in future versions.
{{% /tip %}}

# Timeout

Another thing that can go wrong is that the application will hang before the transaction is committed or aborted, which means that entries that were affected by the transactions are locked away from other users. The solution to that is to create the transaction with a timeout - when the timeout expires the transaction is automatically rolled back and all entries are released.

You can specify a transaction timeout when the transaction is created:


```csharp
// Create a transaction with a 5 minute timeout:
txnManager.Create(5 ** 60 ** 1000);
```

Alternatively, you can set the default transaction timeout on the transaction manager:


```csharp
// Set the default transactions timeout to 5 minutes:
txnManager.DefaultLeaseTime = 5 ** 60 ** 1000;
```

# Isolation Levels

When performing read operations with transactions the transaction isolation level can be set by using the  Read Modifiers:


| Modifier | Description | Comment |
|------------|----------|-----------|
|RepeatableRead | Allows read operations to have visibility of entities that are not write-locked or exclusively-locked by active transactions. | This modifier cannot be used together with: DirtyRead, ReadCommitted|
|DirtyRead | Allows non-transactional read operations to have full visibility of the entities in the space, including entities that are exclusively-locked. | This modifier cannot be used together with: RepeatableRead, ReadCommitted|
|ExclusiveReadLock | Allows read operations to have exclusive visibility of entities that are not locked by active transactions. |  |
|ReadCommitted | Allows read operations to have visibility of already committed entities, regardless of the fact that these entities might be updated (with a newer version) or taken under an uncommitted transaction. |This modifier cannot be used together with: RepeatableRead, DirtyRead

Here is an example that shows the use of isolation levels;


```csharp

public void readWithTransaction()
{
	ITransactionManager mgr = GigaSpacesFactory.CreateDistributedTransactionManager ();

	// Create a transaction using the transaction manager:
	ITransaction trn = mgr.Create ();

	try {
		// ...
		SqlQuery<User> query = new SqlQuery<User> ("Contacts.Home = '770-123-5555'");
		User user = proxy.Read<User> (query, trn, 0, ReadModifiers.RepeatableRead);
		// ....
		trn.Commit();
	}catch(Exception e) {
		// rollback the transaction
		trn.Abort ();
	}
}
```

