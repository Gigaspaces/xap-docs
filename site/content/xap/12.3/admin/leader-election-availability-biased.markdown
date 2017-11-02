---
type: post123
title:  Availability Biased
categories: XAP123ADM, PRM
parent: leader-election.html
weight: 300
---



{{% imagertext "/attachment_files/lus4.jpg" %}}
In this leader election implementation, Space instances register themselves with the Lookup Service (LUS). The Space instances then undergo an active-election process, to discover current Space instances and elect a primary instance.

The active-election process is a 3-phase procedure:

- Pending
- Prepare
- Active
{{% /imagertext%}}
