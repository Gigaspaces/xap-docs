---
type: post102
title:  Transactions
categories: XAP102NET
parent: none
weight: 800
---


XAP .Net provides an explicit transaction management programing model. It allows developers to easily write transactional code with the lowest overhead possible while minimizing the amount of hand-crafted code and separating it from the application hosting environment and instance management.


<br>

{{%fpanel%}}

[Transaction interface](./transaction-management.html){{<wbr>}}
Transaction concept and API

[Locking and Blocking](./transaction-locking-and-blocking.html){{<wbr>}}
Using optimistic and pessimistic locking to preserve the integrity of changes in a multi-user scenarios.

[Read Modifiers](./transaction-read-modifiers.html){{<wbr>}}
XAP's ExclusiveReadLock, ReadCommitted, DirtyRead, and RepeatableRead modifiers.

[Optimistic Locking](./transaction-optimistic-locking.html){{<wbr>}}
The optimistic locking protocol provides better performance and scalability when having concurrent access to the same data. Optimistic locking offers higher concurrency and better performance than pessimistic locking. It also avoids deadlocks.

[Pessimistic Locking](./transaction-pessimistic-locking.html){{<wbr>}}
In the pessimistic locking approach, your program must explicitly obtain a lock using a transaction on one or more objects before making any changes.


[ManagementCenter]({{%currentadmurl%}}/gigaspaces-browser-transaction-view.html)<br>
You can view and inspect ongoing Space transactions with the Management Center.

{{%/fpanel%}}
