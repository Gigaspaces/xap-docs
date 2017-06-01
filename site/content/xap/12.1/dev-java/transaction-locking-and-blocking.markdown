---
type: post121
title:  Locking and Blocking
categories: XAP121
parent: transaction-overview.html
weight: 200
---


Locking of objects occurs in multi-user systems to preserve the integrity of changes, so that changes made by one process do not accidentally overwrite changes made by another process.
GigaSpaces provides two strategies for locking objects: Optimistic and pessimistic. Both of these are discussed below. The focus is on optimistic locking, the preferred strategy in the GigaSpaces context.



# Blocking Rules Overview

Space operations have rules that specify whether they should be blocked if another operation takes place in parallel. Space operations can accept a transaction object, and this object can effect locking behavior.

These rules should be considered very carefully when building space-based applications, especially in scenarios with many concurrent users.
The following table describes space operation blocking rules. The operations listed on the top ("Operation B") are blocked, or allowed, when performed in conjunction with operations listed on the left ("Operation A").

{{% note "Blocking behavior"%}}
TX denotes transaction, and it is assumed that operations A are called under a different transaction to operations B. If they are called under the same transaction, blocking behavior is different.
{{% /note %}}


|Oper. A / Oper. B|Update <br>under <br>TX Y|Take <br> under<br> TX Y|Read <br> under <br>TX Y|Update<br> null <br>TX|Take<br> null <br>TX|Read<br> null<br> TX|
|-----------------|-----------------|----------------|----------------|-----------------|---------------|---------------|
|Update under TX X| Blocked|Blocked|Blocked|Blocked|Blocked|Blocked <br>(unless in dirty_read) |
|Take under TX X|Blocked|Blocked|Blocked|Blocked|Blocked|Blocked|
|Read under TX X|Blocked|Blocked|Allowed|Blocked|Blocked|Allowed|
|Update, null TX|Allowed|Allowed|Allowed|Allowed|Allowed|Allowed|
|Take, null TX|Allowed|Allowed|Allowed|Allowed|Allowed|Allowed|
|Read, null TX|Allowed|Allowed|Allowed|Allowed|Allowed|Allowed|

# Read Operation

A read lock guarantees that an object is not changed while you are looking at it. It is useful when you want to look at an object but not change it.

A read lock provides its guarantee by blocking all other requests for an update and take operation; it does not block other read requests. A request for a read operation is only blocked if the object already has an update operation.

# Update Operation

An update lock guarantees that you are the sole user of an object and that you are looking at the most up-to-date version of an object. It is useful when you want to update an existing object.

An update lock provides its guarantee by blocking all other requests for a read, take or update lock on a particular object. Conversely, a request for an update lock is blocked if the object already has a read, take or update lock.

# Take Operation

The take operation behaves much like the read operation, except that a matching entry is also removed from the space.

A take lock provides its guarantee by blocking all other requests for read, take and update operations. A request for a take operation is blocked if the object already has an update lock, read lock or take lock.

# Clear Operation

The clear operation behaves much like the take operation, except that a matching entry is not sent back to the client.

A `clear` lock provides its guarantee by blocking all other requests for read, take and update operations. A request for a `clear` operation is blocked if the object already has an update lock, read lock or take lock.

# Operations with a Null Transaction

In general, when a space operation is called with a `null` transaction object, its scope is limited to the entry or entries passed to the operation. Any other space operations, with or without a transaction, can be executed without any blocking.


