---
type: post120
title:  Hola Mundo
categories:  XAP120TUT, OS
weight: 300
parent: none
---


This example builds on the basic [Hello-World example](./java-tutorial-hello-world.html) by presenting Processing Unit and event-processing concepts.

The Hola Mundo example demonstrates how co-located business logic is configured by use of a Processing Unit.
Here we demonstrate a translation function which is triggered upon write operations to each partition. 
The "Hello" is translated to "Hola" and "World" is translated to "Mundo".

This example also covers running from IDE with different deployment modes: embedded, partitioned

Please refer to the open source edition
`<XAP root>/examples/hola-mundo` or github repository: {{%git "https://github.com/xap/xap/tree/master/xap-examples/hola-mundo"%}}
 
