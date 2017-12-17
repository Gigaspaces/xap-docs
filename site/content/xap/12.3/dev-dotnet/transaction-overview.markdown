---
type: post123
title:  Transactions
categories: XAP123NET, PRM
parent: none
weight: 800
---


{{%imagertext "/attachment_files/tx_manager.jpg"%}}

With the XAP .NET transaction model, the developer is responsible for explicitly starting and managing the transaction. You can obtain an object representing the underlying Space transaction by calling `GigaSpacesFactory.CreateDistributedTransactionManager`.  This call returns an implementation of the `ITransactionManager` interface used to create the transaction using the `ITransactionManager.Create()` call. This returns an `ITransaction` object that should be used with every Space operation that participates with the transaction. When you want to commit the transaction, call the `ITransaction.Commit()`.

If an error occurs, you have to abort the transaction by calling `ITransaction.Abort()`. To clean up the transaction resources, call `ITransaction.Dispose()`.
{{%/imagertext%}}

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

This code isn't safe because if something goes wrong between the `Take` and `Write` operations, the order is lost. We can use transactions to make it safer:


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

Let's look at the changes that were made:

1. The method now receives an additional argument that is a transaction manager, which is used to create a transaction.
2. The transaction is created in a `using` block, to ensure it is automatically disposed of when done.
3. The business logic code is wrapped in a `try-catch` block, and we added a `commit` upon successful execution or `abort` if an exception occurs.
4. The `Take` and `Write` operations now use the `txn` to tell the Space to perform them under that transaction.

Finally, we now have to provide a transaction manager when invoking `ProcessNewOrder()`. This can be done by invoking `GigaSpacesFactory.CreateDistributedTransactionManager()`. It isn't necessary to create a transaction manager each time we create a transaction, so it was left out of the method. The transaction manager is usually created once upon application initialization, and used throughout the application.

# Timeout

In some circumstances, the application may hang before the transaction is committed or aborted, which means that entries that were affected by the transactions are locked away from other users. To prevent this, create the transaction with a timeout, so that when the timeout expires the transaction is automatically rolled back and all entries are released.

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

When performing read operations with transactions, the transaction isolation level can be set using the Read Modifiers:


| Modifier | Description | Comment |
|------------|----------|-----------|
|RepeatableRead | Allows read operations to have visibility of entities that are not write-locked or exclusively locked by active transactions. | This modifier cannot be used together with: DirtyRead, ReadCommitted|
|DirtyRead | Allows non-transactional read operations to have full visibility of the entities in the Space, including entities that are exclusively locked. | This modifier cannot be used together with: RepeatableRead, ReadCommitted|
|ExclusiveReadLock | Allows read operations to have exclusive visibility of entities that are not locked by active transactions. |  |
|ReadCommitted | Allows read operations to have visibility of already committed entities, regardless of whether these entities might be updated (with a newer version) or taken under an uncommitted transaction. |This modifier cannot be used together with: RepeatableRead, DirtyRead

The following example shows the use of isolation levels:


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


# Viewing Transactions

{{%note "Note"%}}
You can view and inspect ongoing Space transactions using the [GigaSpaces Management Center]({{%currentadmurl%}}/gigaspaces-browser-transaction-view.html).
{{%/note%}}
