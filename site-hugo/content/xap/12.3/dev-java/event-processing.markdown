---
type: post123
title:  Event Processing
categories: XAP123, OSS
parent: none
weight: 1500
canonical: auto
---


This section describes the event processing APIs and how to configure them on top of the Space. The relevant APIs include the [Notify Container](./notify-container-overview.html), which wraps the Space data event session API with event container abstraction, and the [Polling Container](./polling-container-overview.html), which allows you to perform polling receive operations against the Space.

Events that are received by the polling and notify containers are handled by the [Event Listener](./data-event-listener.html), which is a Space Data Event Listener and the [Event Exception Listener](./event-exception-handler.html).

Regarding [FIFO Ordering](./fifo-overview.html), XAP supports both non-ordered Entries and FIFO ordered Entries when performing Space operations. XAP also includes [JMS message support](./messaging-support.html) that is built on top of the core Space architecture.

# Additional Resources

{{%youtube "GwLfDYgl6f8"  "Event Processing"%}}


