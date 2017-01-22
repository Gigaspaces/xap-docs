---
type: post121
title:  Read Modifiers
categories: XAP121
parent: transaction-overview.html
weight: 300
---

{{% ssummary %}}{{% /ssummary %}}



XAP `ReadModifiers` class (see [Javadoc]({{% api-javadoc %}}/index.html?com/j_spaces/core/client/ReadModifiers.html)) provides static methods and constants to decode read-type modifiers. The sets of modifiers are represented as integers with distinct bit positions representing different modifiers.

Four main types of modifiers can be used:

- `REPEATABLE_READ` - default modifier
- `DIRTY_READ`
- `READ_COMMITTED`
- `EXCLUSIVE_READ_LOCK`

These should be used for backward compatibility with older versions of GigaSpaces:

- `MATCH_BY_ID`
- `THROW_PARTIAL_FAILURE`

You can use **bitwise** or the `|` operator to unite different modifiers.

{{% note %}}
`REPEATABLE_READ`, `DIRTY_READ`, and `READ_COMMITTED` are mutually exclusive (i.e. can't be used together). `EXCLUSIVE_READ_LOCK` can be joined with any of them.
{{%/note%}}

These modifiers can be set either at the proxy level - `IJSpace.setReadModifiers(int)`, or at the operation level (e.g. using one of `IJSpace` read `readIfExists` `readMultiple` count methods with a modifiers parameter).

# Spring - XAP ReadModifiers

The following table describes the mapping between the {{%exurl "Spring TransactionDefinition" "http://static.springsource.org/spring/docs/2.0.x/api/org/springframework/transaction/TransactionDefinition.html"%}} Mapping to XAP ReadModifiers:


|Spring TransactionDefinition| XAP ReadModifiers |
|:---------------------------|:-------------------------|
|ISOLATION_READ_UNCOMMITTED| DIRTY_READ|
|ISOLATION_READ_COMMITTED|READ_COMMITTED|
|ISOLATION_REPEATABLE_READ|REPEATABLE_READ|

{{% warning "SERIALIZABLE"%}}
`SERIALIZABLE`isolation is not supported.No exception is currently thrown when used.
{{% /warning %}}

# Repeatable Read

`REPEATABLE_READ` is the default modifier, defined by the JavaSpaces specification.

The `REPEATABLE_READ` isolation level allows a transaction to acquire read locks on an object it returns to an application, and write locks an object it write, updates, or deletes. By using the `REPEATABLE_READ` isolation level, space operations issued multiple times within the same transaction always yield the same result. A transaction using the `REPEATABLE_READ` isolation level can retrieve and manipulate the same object as many times as needed until it completes its task. However, no other transaction can write, update, or delete an object that can affect the result being accessed, until the isolating transaction releases its locks. That is, when the isolating transaction is either committed or rolled back.

Transactions using the `REPEATABLE_READ` isolation level wait until the object that is write-locked by other transactions are unlocked before they acquire their own locks. This prevents them from reading "dirty" data. In addition, because other transactions cannot update or delete an object that is locked by a transaction using the `REPEATABLE_READ` isolation level, non-repeatable read situations are avoided.

# Dirty Read

The JavaSpaces specification defines the visibility of object for read operations as follows:
A read operation performed under a `null` transaction can only access space objects that are not write-locked by non-null transactions. In other words, space objects that were written or taken by active transactions (transactions that have not been committed or rolled back) are not visible to the user performing a read operation.

Sometimes it is desirable for non-transactional read operations to have full visibility of the objects in the space. The `DIRTY_READ` modifier, once set, enables read/`readIfExists`/`readMultiple`/count operations under a `null` transaction to have this complete visibility.

## Code Example


```java
// write something under txn X and commit, making it publicly visible
space.write( something, txnX, Lease.FOREVER);
txnX.commit();

// update this something with a new one under a different txn Y
space.update( newSomething, txnY, Lease.FOREVER, IJSpace.NO_WAIT);

// all read operations (read, readIfExists, readMultiple, count) return the
// version of the object before txnY was committed (newSomething).
// operations can be performed with a new txn Z or a null txn
space.read( tmpl, null, ReadModifiers.DIRTY_READ);

// Note: using the same txn (txn Y) will return matches that are visible under the transaction
```

# Read Committed

The `READ_COMMITTED` modifier enables a read-committed isolation level in read operations.

Read-committed is the isolation-level in which a read operation (under a transaction or a `null` transaction) can not see changes made by other transactions, until those transactions are committed. At this level of isolation, dirty-reads are not possible, but unrepeatable-reads and phantoms might occur.

Read-committed is the default isolation level in database systems. This isolation level means that the read operations return the space objects that are currently committed, regardless of the fact that these space objects might be updated (with a newer version) or taken under an uncommitted transaction. This is opposed to the default space isolation-level (derived from the JavaSpaces specification), which is repeatable-read.

The read-committed isolation level is useful for the local cache, local view, and `GSIterators`, which performs readMultiple and keep their current status by registering notify templates.

The `READ_COMMITTED` modifier is provided at the proxy level and the read API level. It is relevant for read, `readIfExists`, `readMultiple`, and count.

`READ_COMMITTED` and `DIRTY_READ` are mutually-exclusive. A space object under an (uncommitted) updating transaction or under a taking (unrolled) transaction returns the original (committed) value unless the operation is under the same transaction as the locking one.

If the read operation is under a transaction, there is no need to "enlist" the space object in the transaction (unless its already enlisted).

`readIfExists` has less space objects to wait for, unless these are new objects under an uncommitted transaction.

## Locking and Blocking Rules


| Operation A/Operation B | Update<br> under <br>transaction <br>Y | Take <br>under<br>transaction<br> Y | Read<br> under<br> transaction<br> Y | Update,<br> null <br>transaction | Take,<br> null<br> transaction | Read,<br> null <br>transaction | Exclusive <br>Read <br>Lock | Dirty<br> Read<br> Transaction<br> Y <br>or null | Read <br>Committed<br> Transaction<br> Y <br>or null |
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

{{% refer %}}Refer to the [Space Locking and Blocking](./transaction-locking-and-blocking.html) section for XAP general locking and blocking rules.{{% /refer %}}

{{% note %}}
- To read the original state of a space object that is locked under a transaction (take or update) you should use READ_COMMITTED mode.
- To read the current state of a space object that is locked under transaction (take or update) should use Dirty Read mode.
- Dirty read (without transaction) does not blocks transactional take operation.
{{% /note %}}

## Code Example

The examples below assumes you are using `IJSpace` interface that is available via the `GigaSpaces.getSpace()`. If you are using the `GigaSpaces` interface and Spring automatic transaction demarcation, you will not need to specify the transaction object explicitly. Still, the blocking rules will be enforced.


```java
GigaSpace space= ...
// write an object under txn X and commit, making it publicly visible
space.write( something, txnX, Lease.FOREVER);
txnX.commit();

// update this object with a new one under a different txn Y
space.update( newSomething, txnY, Lease.FOREVER, Space.NO_WAIT);

// all read operations (read, readIfExists, readMultiple, count) return the last publicly visible match.
// operations can be performed with a new txn Z or a null txn
space.read( tmpl, txnZ, ReadModifiers.READ_COMMITTED);

// Note: using the same txn (txn Y) will return matches that are visible under the transaction
```

# Exclusive Read Lock

The Exclusive Read Lock is similar to select for update SQL for RDBMS, or update lock with ODBMS.

In the JavaSpaces specification, a read under a transaction does not allow other users to modify the Entry, but it does allow two readers or more to read the same Entry under different transactions. To allow a user to block other users from reading an object, a read using exclusive read lock mode with a transaction should be performed.

The following methods support exclusive read lock when used with a transaction:

- `read()`
- `readIfExists()`
- `readByID()`
- `readMultiple()`

The exclusive read lock is supported in a clustered environment when using the Jini Transaction Manager.

{{% tip "EXCLUSIVE_READ_LOCK modifier"%}}
Starting with XAP 7.1.2 GigaSpaces throws `java.lang.IllegalArgumentException`: Using EXCLUSIVE_READ_LOCK modifier without a transaction is illegal exception as a protection mechanism when performing exclusive read **without** using a transaction. You must use a transaction when using exclusive read lock.
{{% /tip %}}

## Code Example


```java
IJSpace space = ...
space.setReadModifiers(ReadModifiers.EXCLUSIVE_READ_LOCK);
// this will allow all read operations with this proxy to use Exclusive Read Lock mode
Lock lock = new Lock();
lock.key = new Integer(1);
lock.data = "my data";
space.write(lock, null, Lease.FOREVER);
Transaction txn1 = getTX();
Lock lock_template1 = new Lock();
lock_template1.key = new Integer(1);
Lock lock1 = (Lock) space.read(lock_template1, txn1, 1000);
If (lock1!= null)
	System.out.println("Transaction " + txn1.id + " Got exclusive Read Lock on Entry:"
		+ lock1.getId());
```

### MATCH_BY_ID & THROW_PARTIAL_FAILURE

The matching behavior can be changed by adding one of these modifiers.

Setting `MATCH_BY_ID` changes the matching algorithm, such that once a SpaceID is set the other fields' values are ignored. The matching is only done according to the SpaceID value.

Setting `THROW_PARTIAL_FAILURE` is only effective when `readMultiple()` is called. When set and a matching can be performed **only on part of the partitions** instead of just returning the partial result a `QueryMultiplePartialFailureException` is thrown including the partial result and the source of the partial failure.
