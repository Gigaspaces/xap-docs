---
type: post123
title:  Distributed Processing
categories: XAP123GS, OSS
parent: xap-basics.html
weight: 700
---



In this part of the tutorial we will introduce you to the different processing services you can run on top of the space.





XAP includes a set of built-in service components such as Task Execution and Messaging services, each implementing commonly used Enterprise integration patterns. It's purpose is to make the implementation of distributed applications on-top of the space simpler and less intrusive and allow you to easily build highly scalable and performing applications. All services follow the POJO/Spring based abstraction approach which includes dependency injection and annotations.




# Task Execution
Task Execution provides a fine-grained API for performing ad-hoc parallel execution of user defined tasks. This framework should be used in the following scenarios:

* When the tasks are defined by clients and can be changed or added while the data-grid servers are running.
* As a dynamic "stored procedure" enabling to execute complex multi stage queries or data manipulation where the data resides, thus enabling to send back only the end result of the calculation and avoid excess network traffic.
* Scatter/Gather pattern - when you need to perform aggregated operations over a cluster of distributed partitions.

Task execution comes in two flavors:

- Java Tasks - In this mode you can pass Java code from the client to the cluster to be executed on the data grid nodes. The code is dynamically introduced to the server nodes classpath.
- Dynamic language tasks - Tasks can be defined using one of the dynamic languages supported by the JVM (JSR-223) and be compiled and executed on the fly. In this part of the tutorial we will not cover Dynamic language tasks.

{{%refer%}}[Dynamic Language Tasks]({{%currentjavaurl%}}/task-dynamic-language.html){{%/refer%}}


Java Tasks can be more efficient in terms of performance and tend to be more type-safe then dynamic tasks. Dynamic tasks on the other hand can be changed more frequently without causing class version conflicts and are more concise given the nature of dynamic languages..

{{%note "Tasks"%}}
- execute in a "broadcast" mode on all the primary cluster members concurrently and reduced to a single result on the client side. {{<wbr>}}
- can execute directly on a specific cluster member using typical routing declarations. {{<wbr>}}
- are completely dynamic both in terms of content and class definitions (the task class definition does not have to be defined within the space classpath).
{{%/note%}}


Here is an example of a Java task. We define a task that will collect all users that made a payment to a specific merchant:

{{%tabs%}}
{{%tab "  Task "%}}

```java
public class MerchantUserTask implements Task<HashSet<Integer>> {
     private Long merchantId;

     @TaskGigaSpace
     private transient GigaSpace space;

     public MerchantUserTask(Long merchantId) {
        this.merchantId = merchantId;
     }

     public HashSet<Integer> execute() throws Exception {
       SQLQuery<Payment> query = new SQLQuery<Payment>(Payment.class,"receivingMerchantId = ? ");
       query.setParameter(1, merchantId);

       Payment[] payments = space.readMultiple(query, Integer.MAX_VALUE);
       HashSet<Integer> userIds = new HashSet<Integer>();

       // Eliminate duplicate UserId
       if (payments != null) {
         for (int i = 0; i < payments.length; i++) {
            userIds.add(payments[i].getPayingAccountId());
         }
       }
       return userIds;
    }
}
```
{{% /tab %}}
{{%tab "  Execution "%}}

```java
public void executeTask() throws InterruptedException, ExecutionException {
     MerchantUserTask task = new MerchantUserTask(1L);

     AsyncFuture<HashSet<Integer>> result = space.execute(task);
     HashSet<Integer> hashSet = result.get();
}
```
{{% /tab %}}
{{% /tabs %}}

Task execution is asynchronous in nature, returning an AsyncFuture as the result of the execution allowing to get the result at a later stage in the code. AsyncFuture itself extends java.util.concurrent.Future that adds the ability to register a listener which will be called when the results are available.

Here is an example of a Future Listener:

{{%tabs%}}
{{%tab "  Async Listener "%}}

```java
public class AsyncListener implements AsyncFutureListener<HashSet<Integer>> {

     public void onResult(AsyncResult<HashSet<Integer>> result) {
          System.out.println("Listener received result");
     }
}
```
{{% /tab %}}
{{%tab "  Execution "%}}

```java
public void executeAsyncTask() throws InterruptedException {
	MerchantUserTask task = new MerchantUserTask(2L);
	AsyncListener l = new AsyncListener();

	space.execute(task, l);
}
```
{{% /tab %}}
{{% /tabs %}}

#### Task Routing
By nature, task execution is broadcasted to all partitions in the space. You can route a task directly to one of the partitions of the space. Here is an example demonstrating how to route a task to a partition:


```java
public void executeTaskWithRouting() throws InterruptedException, ExecutionException {
     // Route the task to partion 2
     AsyncFuture<HashSet<Integer>> result = space.execute(task, 2);
}
```

There are other options available for task routing
.
{{%refer%}}[Task Execution over the Space]({{%currentjavaurl%}}/task-execution-over-the-space.html){{%/refer%}}




# Distributed Task
A DistributedTask is a task that ends up executing more than once (concurrently) and returns a result that is a reduced operation of all the different executions.

In the example below we are creating a distributed task that finds all merchants with a specific category. Once all results are returned to the client, reduce is called and a list of all merchants is created.

{{%tabs%}}
{{%tab "  DistributedTask "%}}

```java
public class MerchantByCategoryTask implements DistributedTask<Merchant[], List<Merchant>> {

    private ECategoryType categoryType;
    @TaskGigaSpace
    private transient GigaSpace gigaSpace;

    public MerchantByCategoryTask(ECategoryType categoryType) {
      this.categoryType = categoryType;
    }

    public Merchant[] execute() throws Exception {
       SQLQuery<Merchant> query = new SQLQuery<Merchant>(Merchant.class,"category = ?");
       query.setParameter(1, categoryType);
       return gigaSpace.readMultiple(query, Integer.MAX_VALUE);
    }
    public List<Merchant> reduce(List<AsyncResult<Merchant[]>> results) throws Exception {
       List<Merchant> list = new ArrayList<Merchant>();

       for (AsyncResult<Merchant[]> result : results) {
         if (result.getException() != null) {
           throw result.getException();
         }
         Collections.addAll(list, result.getResult());
       }
       return list;
   }
}
```
{{% /tab %}}
{{%tab "  Execution "%}}

```java
public void executeDistributedTask() throws InterruptedException,ExecutionException {
    MerchantByCategoryTask task = new MerchantByCategoryTask(ECategoryType.AUTOMOTIVE);

    AsyncFuture<List<Merchant>> result = space.execute(task);
    List<Merchant> list = result.get();
}
```
{{% /tab %}}
{{% /tabs %}}

By default, the task is broadcasted to all primary nodes. You can also execute a distributed task on selected nodes based on different routing values:

```java
AsyncFuture<List<Merchant>> result = space.execute(task,1,2,3);
```

XAP provides out of the box Aggregator Tasks.

{{%refer%}}
[Aggregators]({{%currentjavaurl%}}/aggregators.html)
{{%/refer%}}


#### ExecutorBuilder
The executor builder allows to combine several task executions (both distributed ones and non distributed ones) into a seemingly single execution (with a reduce phase).

{{%refer%}}
[Task Execution over the Space]({{%currentjavaurl%}}/task-execution-over-the-space.html)
{{%/refer%}}





# Space Based Remoting
Spring provides support for various remoting technologies. XAP uses the same concepts to provide remoting, using the space as the underlying protocol.

Why would you use the space as the transport layer include:

- High availability - since the space by its nature (based on the cluster topology) is highly available, remote invocations get this feature automatically when using the space as the transport layer.
- Load-balancing - when using a space with a partitioned cluster topology, each remote invocation is automatically directed to the appropriate partition (based on its routing handler), providing automatic load-balancing.
- Performance - remote invocations are represented in fast internal XAP objects, providing fast serialization and transport over the net.
- Asynchronous execution - by its nature, remoting support is asynchronous, allowing for much higher throughput of remote invocations. XAP allows you to use asynchronous execution using Futures, and also provides synchronous support (built on top of it).


- Executor Based Remoting
The Executor Based Remoting executes synchronous or asynchronous calls between the client and the server. In this mode the client invocation executes a task that invokes the corresponding server method immediately when the call arrives at the server. The server must therefore be collocated with the space.

- Event Driven Remoting
Event Driven Remoting supports most of the above capabilities, but does not support map/reduce style invocations. In terms of implementation, it's based on the Polling Container feature, which means that it writes an invocation entry to the space which is later consumed by a polling container. Once taking the invocation entry from the space, the polling container's event handler delegates the call to the space-side service.

In this tutorial will look at an Executor based Remoting service.

#### Defining the contract
In order to support remoting, the first step is to define the contract between the client and the server. Here is an example of a payment service:

```java
public interface IPaymentProcessor {
    Payment processPayment(Payment data);
}
```

#### Implement the Service
Next, an implementation of this contract needs to be provided. This implementation will "live" on the server side. Here is a sample implementation:

```java
@RemotingService
public class PaymentProcessor implements IPaymentProcessor {
     public Payment processPayment(Payment payment) {
       System.out.println("Processing payment ");
       return payment;
    }
}
```

#### Exporting the service
The service is exported to the server with the Spring configuration. Here is an example:

```xml
<!-- Scan the packages for annotations / -->
<context:component-scan base-package="xap.qsg" />

<!-- Enables using @RemotingService and other remoting related annotations -->
<os-remoting:annotation-support />

<!-- A bean representing a space (an IJSpace implementation) -->
<os-core:embedded-space id="space" space-name="xapTutorialSpace" />

<!-- Define the GigaSpace instance that the application will use to access the space -->
<os-core:giga-space id="xapTutorialSpace" space="space"/>

<os-remoting:service-exporter id="serviceExporter" />
```

#### Client side invocation
On the client side the remoting proxy is injected with the @ExecutorProxy annotation:

```java
public class RemoteService {
     @Autowired
     @Qualifier(IRemotingService.SPACE)
     private GigaSpace space;

     @ExecutorProxy
     private IPaymentProcessor paymentProcessor;

     public void executePaymentService() {
        Payment payment = paymentProcessor.processPayment(new Payment());
     }
}
```

{{%refer%}}[Space Based Remoting]({{%currentjavaurl%}}/space-based-remoting.html){{%/refer%}}

