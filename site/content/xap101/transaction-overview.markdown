---
type: post101
title:  Transactions
categories: XAP101
parent: none
weight: 1000
---




{{%section%}}
{{%column width="10%" %}}
![transaction.png](/attachment_files/subject/transaction.png)
{{%/column%}}
{{%column width="90%" %}}
The Spring Framework provides a transaction manager abstraction using the `PlatformTransactionManager` interface with several different built-in implementations, such as JDBC Data Source and JTA. GigaSpaces provides several implementations for Spring's `PlatformTransactionManager`, allowing you to use the GigaSpaces local and Jini Distributed Transaction Managers.
{{%/column%}}
{{%/section%}}

<br>

{{%fpanel%}}
[Transaction interface](./transaction-management.html){{<wbr>}}
Transaction concept and API

[Locking and Blocking](./transaction-locking-and-blocking.html){{<wbr>}}
Using optimistic and pessimistic locking to preserve the integrity of changes in a multi-user scenarios.

[Read Modifiers](./transaction-read-modifiers.html){{<wbr>}}
XAP ReadModifiers class provides static methods and constants to decode read-type modifiers.

[Optimistic Locking](./transaction-optimistic-locking.html){{<wbr>}}
The optimistic locking protocol provides better performance and scalability when having concurrent access to the same data. Optimistic locking offers higher concurrency and better performance than pessimistic locking. It also avoids deadlocks.

[Pessimistic Locking](./transaction-pessimistic-locking.html){{<wbr>}}
In the pessimistic locking approach, your program must explicitly obtain a lock using a transaction on one or more objects before making any changes.
{{%/fpanel%}}
