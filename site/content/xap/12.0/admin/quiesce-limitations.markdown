---
type: post120
title:  Limitations and Considerations
categories: XAP120ADM, PRM
parent: quiescemode.html
weight: 300
---



The following limitations and open issues apply to Quiesce Mode:

1. The processing unit should be **intact** before triggering quiesce request.
2. Though Quiesce handles processing unit restart, it is not resilient to sudden network disconnections (in XAP grid components machines) therefore it is possible that in rare conditions the quiesce request will have to be repeated manually by the user. <br> 
3. Quiesce state changed events are propagated only to the components (beans) that are located within the processing unit context file ([pu.xml]({{%currentjavaurl%}}/configuring-processing-unit-elements.html)), therefore custom components will not be aware of quiesce state changed events (even if the component implements [QuiesceStateChangedListener](./quiesce-pu-api.html#quiesce-state-changed-listener)).
4. Replication of Quiesce state between primary and backup GSM is not yet supported - GSM failover may cause loosing the quiesce state of the system, in this case repeating quiesce request will also be required in order to re inform the GSM about quiesce state.
