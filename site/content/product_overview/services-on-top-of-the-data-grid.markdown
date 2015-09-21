---

title:  Services on top of the Data Grid
categories: PRODUCT_OVERVIEW
weight: 400
parent: the-in-memory-data-grid.html
menu: product
---



{{%  ssummary   %}}  {{%  /ssummary %}}



GigaSpaces includes a set of built-in service components such as Task execution service components, Messaging services components, each implementing commonly used pattern and framework. It was designed to make the programming of distributed applications on-top of the space based API simpler and less intrusive.  All services follow the POJO/Spring based abstraction approach which include dependency injection and annotations. The GigaSpaces DataGrid is used as a shared clustering framework for all those components. The shared clustering provides general purpose solution that ensures the high availability, scalability, and transaction integrity of all those API's.

### Choosing the Correct Service Component

This section explains when you should use each of the service components:

# Task Execution


Task Execution {{% latestjavanet "task-execution-over-the-space.html"%}} provides a fine-grained API for performing ad-hoc parallel execution of user defined tasks. You should use this framework in the following scenarios:

- When the tasks are defined by clients and can be changed or added while the data-grid servers are running.

- As a dynamic "stored procedure" enabling to execute complex multi stage queries or data manipulation where the data resides, thus enabling to send back only the end result of the calculation and avoid excess network traffic.

- Map/Reduce pattern - when you need to perform aggregated operations over a cluster of distributed partitions.


Task execution comes in two flavors:

- Java Tasks{{% latestjavanet "task-execution-over-the-space.html"%}} - In this mode you can pass Java code from the client to the cluster to be executed on the data grid nodes. The code is dynamically introduced to the server nodes classpath.

- [Dynamic language tasks]({{% latestjavaurl%}}/dynamic-language-tasks.html) - Tasks can be defined using one of the dynamic languages supported by the JVM (JSR-223) and be compiled and executed on the fly.

Java Tasks can be more efficient in terms of performance and tend to be more type-safe then dynamic tasks. Dynamic tasks on the other hand can be changed more frequently without causing class version conflicts.



# Messaging and Events

The Space's Messaging and Events support provides messaging handlers that simplify event driven programming. With this framework you select an event based on its content and designate a method that would be triggered as a result of that event, all through a simple and non-intrusive configuration. There are two main event handlers that are available:

### Notify Container


The Notify Container {{% latestjavanet "notify-container.html" %}}  is the equivalent of a publish/subscribe messaging. Uses the space.notify() API as the underlying event delivery mechanism. With this method the listener doesn't hold a connection to the space. The event handler is triggered by the space as soon as matching event arrived. Notify will call ALL matched subscribers at the same time.



### Polling Container

The Polling Container {{% latestjavanet "polling-container.html" %}}  is the equivalent of a point to point communication. It uses the space.take() API as the underlying event delivery mechanism. Unlike the notify container the Polling container blocks contentiously on space connection until a matching event arrives. Polling containers ensures that one and only one listener will be triggered per event even if there are more then one listener that matches that event.


You should use the notify container for one to many or many to many relationship and polling container for one to one or many to one relationship.

### JMS

In addition to the polling containers you can also use a [JMS facade]({{% latestjavaurl%}}/messaging-support.html) on top of the space to deliver events. The JMS facade is designed primarily to enable integration with external feeders that cannot or were not designed to work with the space based API. JMS in general provides less fine grained event matching semantics in comparison to the one provided by the space, and is therefore not the recommended way to deliver inter-application events.

See [Messaging Support]({{% latestjavaurl%}}/messaging-support.html) for details.

# Space Based Remoting

Space Based Remoting is the equivalent to JEE Session Beans or to Java RMI. It leverages the space as a reliable and scalable transport mechanism that is used as a communication bus between POJO based services and their clients. Unlike Tasks and Polling Containers the Remoting mechanism exposes a more type-safe and static interface. If you have already built your business logic using a remoting approach you would find it easier to use this model to scale your services without rewriting your client side access code. It is also the least intrusive mechanism in terms of Space API usage.

There are basically two flavors to this framework:

### Executor Based Remoting


The Executor Based Remoting {{% latestjavanet "executor-based-remoting.html"%}} used to deliver synchronous or asynchronous calls between the client and the server. In this mode the client invocation executes a task that invokes the relevant server method immediately when the call arrives to the server. The server must therefore be collocated with the space.
The client thread can wait for the execution to complete synchronously or use `Future` to receive the result asynchronously.


### Event Driven Remoting

With the Event Driven Remoting ([Jave version]({{% latestjavaurl%}}/event-driven-remoting.html) \| [.NET version]({{% latestneturl%}}/domain-service-host.html)) remote calls are queued in the space before they are executed. The implementation uses a polling container to handle the event and trigger the appropriate service implementation instance. Unlike the `Executor Based Remoting` the service implementation can be remote as well as collocated with the space.
