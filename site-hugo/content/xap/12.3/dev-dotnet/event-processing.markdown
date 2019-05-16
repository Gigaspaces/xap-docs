---
type: post123
title:  Event Processing
categories: XAP123NET, PRM
parent: none
weight: 1200
canonical: auto
---





This section the event processing APIs and how to configure them on top of the space. The relevant APIs include the [Notify Container](./notify-container-overview.html), which wraps the Space data event session API with event container abstraction, and the [Polling Container](./polling-container-overview.html), which allows you to perform polling receive operations against the Space.

The [Event Listener Container](./event-listener-container.html) is an interface that represents an abstraction for subscribing to, and receiving events over a Space proxy.

Regarding [FIFO Ordering](./fifo-overview.html), XAP supports both non-ordered Entries and FIFO ordered Entries when performing space operations.

