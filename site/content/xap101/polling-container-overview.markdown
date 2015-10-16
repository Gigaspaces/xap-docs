---
type: post101
title:  Polling Container
categories: XAP101
parent: event-processing.html
weight: 300
---


The polling event container is an implementation of the polling consumer pattern which uses the Space to receive events.
A polling event operation is mainly used when simulating Queue semantics or when using the master-worker design pattern.


<br>


{{%fpanel%}}

[Overview](./polling-container.html){{<wbr>}}
The polling event container.

[Concurrent consumers](./polling-container-scaling.html){{<wbr>}}
By default, the polling event container starts a single thread that performs the receive operations, and invokes the event listener. It can be configured to start several concurrent consumer threads.

[Transaction support](./polling-container-transactions.html){{<wbr>}}
Both the receive operation and the actual event action can be configured to be performed under a transaction.

{{%/fpanel%}}

<br>

#### Additional Resources

{{%youtube "GwLfDYgl6f8"  "Event Processing"%}}


