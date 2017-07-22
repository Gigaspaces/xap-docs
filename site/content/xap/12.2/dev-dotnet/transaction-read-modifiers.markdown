---
type: post122
title:  Read Modifiers
categories: XAP122NET, PRM
parent: transaction-overview.html
weight: 300
---

{{%ssummary%}}{{%/ssummary%}}

XAP `ReadModifiers` class see [DotNetDoc]({{%api-dotnetdoc%}}/T_GigaSpaces_Core_ReadModifiers.htm) provides static methods and constants to decode read-type modifiers. The sets of modifiers are represented as integers with distinct bit positions representing different modifiers.

Four main types of modifiers can be used:

- `RepeatableRead` - default modifier
- `DirtyRead`
- `ReadCommitted`
- `ExclusiveReadLock`


You can use **bitwise** or the `|` operator to unite different modifiers.

{{% note %}}
`RepeatableRead`, `DirtyRead`, and `ReadCommitted` are mutually exclusive (i.e. can't be used together). `ExclusiveReadLock` can be joined with any of them.
{{%/note%}}

These modifiers can be set either at the proxy level - `proxy.ReadModifiers= int`, or at the operation level (e.g. using one of  read/`ReadIfExists`/`ReadMultiple`/count methods with a modifiers parameter).


# Repeatable Read

`RepeatableRead` is the default modifier, defined by the JavaSpaces specification.

The `RepeatableRead` isolation level allows a transaction to acquire read locks on an object it returns to an application, and write locks an object it write, updates, or deletes. By using the `RepeatableRead` isolation level, space operations issued multiple times within the same transaction always yield the same result. A transaction using the `RepeatableRead` isolation level can retrieve and manipulate the same object as many times as needed until it completes its task. However, no other transaction can write, update, or delete an object that can affect the result being accessed, until the isolating transaction releases its locks. That is, when the isolating transaction is either committed or rolled back.

Transactions using the `RepeatableRead` isolation level wait until the object that is write-locked by other transactions are unlocked before they acquire their own locks. This prevents them from reading "dirty" data. In addition, because other transactions cannot update or delete an object that is locked by a transaction using the `RepeatableRead` isolation level, non-repeatable read situations are avoided.

# Dirty Read

The JavaSpaces specification defines the visibility of object for read operations as follows:
A read operation performed under a `null` transaction can only access space objects that are not write-locked by non-null transactions. In other words, space objects that were written or taken by active transactions (transactions that have not been committed or rolled back) are not visible to the user performing a read operation.

Sometimes it is desirable for non-transactional read operations to have full visibility of the objects in the space. The `DirtyRead` modifier, once set, enables read/`readIfExists`/`readMultiple`/count operations under a `null` transaction to have this complete visibility.

## Code Example


```csharp
// write something under txn X and commit, making it publicly visible
proxy.Write( something, txnX, long.MaxValue);
txnX.commit();

// update this something with a new one under a different txn Y
proxy.Write( newSomething, txnY, long.MaxValue, 0);

// all read operations (Read, ReadIfExists, ReadMultiple, Count) return the
// version of the object before txnY was committed (newSomething).
// operations can be performed with a new txn Z or a null txn
proxy.Read( tmpl, null, ReadModifiers.DirtyRead);

// Note: using the same txn (txn Y) will return matches that are visible under the transaction
```

# Read Committed

The `ReadCommitted` modifier enables a read-committed isolation level in read operations.

Read-committed is the isolation-level in which a read operation (under a transaction or a `null` transaction) can not see changes made by other transactions, until those transactions are committed. At this level of isolation, dirty-reads are not possible, but unrepeatable-reads and phantoms might occur.

Read-committed is the default isolation level in database systems. This isolation level means that the read operations return the space objects that are currently committed, regardless of the fact that these space objects might be updated (with a newer version) or taken under an uncommitted transaction. This is opposed to the default space isolation-level (derived from the JavaSpaces specification), which is repeatable-read.

The read-committed isolation level is useful for the local cache, local view, and `ISpaceIterator`, which performs ReadMultiple and keep their current status by registering notify templates.

The `ReadCommitted` modifier is provided at the proxy level and the read API level. It is relevant for read, `ReadIfExists`, `ReadMultiple`, and count.

`ReadCommitted` and `DirtyRead` are mutually-exclusive. A space object under an (uncommitted) updating transaction or under a taking (unrolled) transaction returns the original (committed) value unless the operation is under the same transaction as the locking one.

If the read operation is under a transaction, there is no need to "enlist" the space object in the transaction (unless its already enlisted).

`ReadIfExists` has less space objects to wait for, unless these are new objects under an uncommitted transaction.

## Locking and Blocking Rules


| Operation A/Operation B | Update <br>under<br> transaction<br> Y | Take<br> under<br> transaction <br>Y | Read<br> under<br> transaction<br> Y | Update<br> null<br> transaction | Take,<br> null<br> transaction | Read,<br> null<br> transaction | Exclusive<br> Read<br> Lock | Dirty<br> Read<br> Transaction<br> Y<br> or null | Read <br>Committed <br>Transaction <br>Y <br>or null |
|:------------------------|:---------------------------|:-------------------------|:-------------------------|:-------------------------|:-----------------------|:-----------------------|:--------------------|:---------------------------------|:-------------------------------------|
| Update under transaction X | Blocked | Blocked | Blocked | Blocked | Blocked | Blocked | Blocked | Allowed | Allowed|
| Take under transaction X | Blocked | Blocked | Blocked | Blocked | Blocked | Blocked | Blocked | Allowed | Allowed |
| Read under transaction X | Blocked | Blocked | Allowed | Blocked | Blocked | Allowed | Blocked | Allowed | Allowed |
| Update, null transaction | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed |
| Take, null transaction | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed |
| Read, null transaction | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed   | Allowed | Allowed | Allowed|
| Exclusive Read Lock | Blocked | Blocked | Blocked | Blocked | Blocked | Blocked | Blocked | Allowed | Allowed |
| Read Committed transaction X or null | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed |
| Dirty Read Transaction X or null| Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed | Allowed |

{{% refer %}}
Refer to the [Space Locking and Blocking](./transaction-locking-and-blocking.html) section for XAP general locking and blocking rules.
{{% /refer %}}

{{% note %}}
- To read the original state of a space object that is locked under a transaction (take or update) you should use ReadCommitted mode.
- To read the current state of a space object that is locked under transaction (take or update) should use Dirty Read mode.
- Dirty read (without transaction) does not blocks transactional take operation.
{{% /note %}}

## Code Example

The examples below assumes you are using `ISpaceProxy` interface.


```csharp
ISpaceProxy proxy;
// write an object under txn X and commit, making it publicly visible
proxy.Write( user, txnX, long.MaxValue);
txnX.commit();

// update this object with a new one under a different txn Y
proxy.Write( user, txnY, 0, long.MaxValue);

// all read operations (read, ReadIfExists, ReadMultiple, Count) return the last publicly visible match.
// operations can be performed with a new txn Z or a null txn
proxy.Read( user, txnZ, ReadModifiers.ReadCommitted);

// Note: using the same txn (txn Y) will return matches that are visible under the transaction
```

# Exclusive Read Lock

The Exclusive Read Lock is similar to select for update SQL for RDBMS, or update lock with ODBMS.

In the JavaSpaces specification, a read under a transaction does not allow other users to modify the Entry, but it does allow two readers or more to read the same Entry under different transactions. To allow a user to block other users from reading an object, a read using exclusive read lock mode with a transaction should be performed.

The following methods support exclusive read lock when used with a transaction:

- `Read()`
- `ReadIfExists()`
- `ReadByID()`
- `ReadMultiple()`

The exclusive read lock is supported in a clustered environment when using the Jini Transaction Manager.


## Code Example


```csharp
public void exclusiveReadLock()
{
    // this will allow all read operations with this proxy to use Exclusive Read Lock mode
    proxy.ReadModifiers = ReadModifiers.ExclusiveReadLock;

    Lock lok = new Lock();
    lok.key = 1;
    lok.data = "my data";
    proxy.Write(lok, null, long.MaxValue);

    ITransactionManager mgr = GigaSpacesFactory.CreateDistributedTransactionManager ();
    ITransaction txn1 = mgr.Create();

    Lock lock_template1 = new Lock();
    lock_template1.key = 1;

     Lock lock1 = proxy.Read<Lock>(lock_template1, txn1, 10000);

     if (lock1 != null)
     {
         Console.WriteLine("Transaction " + txn1 + " Got exclusive Read Lock on Entry:"
                    + lock1.key);
     }
}
```

