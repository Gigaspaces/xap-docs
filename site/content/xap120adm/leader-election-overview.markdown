---
type: post120
title:  Overview
categories: XAP120ADM
parent: leader-election.html
weight: 100
---

{{% ssummary %}}  {{% /ssummary %}}



# CAP Theorem
The CAP theorem, also known as Brewer's theorem, states that it is impossible for a distributed computer system to simultaneously provide all three of the following guarantees:

* Consistency (all nodes see the same data at the same time)

* Availability (node failures do not prevent survivors from continuing to operate)

* Partition Tolerance (the system continues to operate despite arbitrary message loss)

{{%align center%}}
![image](/attachment_files/cap.png)
{{%/align%}}

