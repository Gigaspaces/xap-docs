---
type: post120
title:  Mule ESB Integration
categories: XAP120
parent: none
weight: 1700
---

XAP comes with comprehensive support for Mule v3.7. It allows you to use the Space as a Mule external transport, enabling receiving and dispatching of POJO messages over the Space.
An additional transport called os-queue allows you to replace the Mule VM transport with highly available inter VM transport over the Space.
A Mule application can be packaged and run as a Processing Unit within one of the SLA-driven Processing Unit containers.

<br>

{{%fpanel%}}

[Event Container](./mule-event-container-transport.html){{<wbr>}}
XAP's event container transport uses event components that allow you to send and receive POJO messages over the Space using Mule.

[Processing Unit](./mule-processing-unit.html){{<wbr>}}
The Mule Processing Unit allows you to run Mule within a Processing Unit, thus leveraging all of the Processing Unit and SLA-driven container capabilities.

[Queue Provider](./mule-queue-provider.html){{<wbr>}}
The XAP queue provider is used for internal space-based communication between services managed by Mule.

{{%/fpanel%}}

<br>

## Additional Resources

{{%exurl "Mule Site" "http://www.mulesoft.org"%}}

[Distributed Multi Mule service example](/sbp/mule-esb-example.html)

