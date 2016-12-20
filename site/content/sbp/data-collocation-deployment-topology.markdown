---
type: post
title:  Data-Collocation Deployment Topology
categories: SBP
parent: production.html
weight: 300
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim| 7.0 | September 2009|  |     |



# The ABCs of the Processing Unit
GigaSpaces XAP includes a universal grid-based runtime environment for real-time extreme transaction processing (XTP) for Java, C++ and .Net applications. The runtime environment includes a Service Level Agreement-based container called a [GSC](/product_overview/service-grid.html#gsc), in which you are able to deploy business logic and stateful data.

When your business logic and its state are managed by the GigaSpaces runtime components, they are able to self-heal in the case of a failure via the continuous high-availability mechanism of the system. They can dynamically scale themselves by configuration or by a programmable API - all out-of-the-box.

The actual deployment is done via a dedicated service called the [GSM](/product_overview/service-grid.html#gsm), which is responsible for the deployment process and for enforcing the pre-defined SLA (if any) throughout the lifecycle of the application.

The application code running within the GSC should be compiled, packaged into a library, and deployed via the relevant deploy command/API/UI. The GSM is responsible for pushing the application libraries into all the machines running the GSC at deployment, when scaling the system, or when there is a failure that requires the system to use a new available GSC. Once the application libraries have been deployed to the GSC, the container loads the application, initializes it, and then starts the application. You can use external parameters in the deployment and get the status of the business logic and its state via the relevant [administration API]({{%latestjavaurl%}}/administration-and-monitoring-api.html), or the provided administration tools.

Since the GSC and its GSM manager include "sensors" that allow the behavior of the hosted processing units to be monitored, additional business logic instances may be instantiated on other GSCs running on other machines. This is done to cope with the additional increasing incoming requests, or to enlarge the capacity of the running spaces to store more data in-memory.

The Processing Unit configuration should follow the basic standards that comprise any processing unit. These include space settings, transaction settings, cluster settings, notify and polling container settings, and your own specific business logic settings. The Processing Unit should also include the actual business logic libraries and any relevant third party libraries used.

##  The Data Colocation Deployment Topologies
When deploying the processing unit, one of the following data colocation deployment topologies can be used:

- Pure business logic - the state is stored within another processing unit deployed separately. Multiple instances of the business logic can be deployed and scaled dynamically.

{{% align center %}}
![pu_with_bean.gif](/attachment_files/sbp/pu_with_bean.gif)
{{% /align %}}

- Space instance(s) - these are deployed using one of the built-in cluster topologies (replicated, partitioned or partitioned-replicated) or a custom topology.

{{% align center %}}
![pu_with_space.gif](/attachment_files/sbp/pu_with_space.gif)
{{% /align %}}

- Business logic and colocated space instance(s) - the business logic accesses only the colocated spaces or remote space instances running within another processing instance (or running as part of different processing unit). You can deploy these using any of the built-in cluster topologies.

{{% align center %}}
![pu_with_space_and_bean.gif](/attachment_files/sbp/pu_with_space_and_bean.gif)
{{% /align %}}

- Business logic with a local cache - this scenario is relevant for read-mostly cases, where some of the data is cached at the client side.

There are no remote calls involved when the business logic interacts only with its colocated space. Remote calls would be involved in the case where the business logic accesses remote spaces explicitly (with a space proxy configured in a clustered mode), and when the colocated space is configured to have a replica (backup) space. In this case (the replica space), every destructive operation (write, update, take) triggers a replication event that transports the colocated space changes to the replica space(s). Data replication can be done synchronously (which impacts performance), or asynchronously (which does not affect performance).

When the business logic is deployed with a colocated space, it can inherit the space's active mode (i.e., primary or backup). This means that the business logic can be running in stand-by mode, uninitialized, as long as its colocated space is running in backup mode. A space running in backup mode is updated via the existing primary space, and is not accessible by a regular client for direct interaction. If the primary space and its colocated business logic fails (for any reason), the colocated business logic running with the backup space is initialized and started.

# How can I get more CPU Power for my Application?
Historically, software systems scaled by adding more hardware. These ran more software instances that eventually scaled the overall system allowing it to support additional increasing numbers of requests. More CPUs and more memory can always be added to the same machine to be able to process more data/requests per unit of time, but at some point the machine is bound to reach its physical limits in terms of CPU and memory.

## How can I Scale my Application?
The concept of the OpenSpaces Processing Unit was designed around scalability, and the Processing Unit is the unit of scale. Adding scalability, then, is a matter of running more processing units on the any machine participating in the XAP cluster. The application can be scaled by:

- Running multiple consumers concurrently within the same process (when using the polling container).
- Running multiple processing unit instances concurrently within the same machine.
- Running multiple processing unit instances across multiple machines that are running concurrently, and utilizing your compute resources over the network (a.k.a. the grid).

## The Starvation Scenario
Nevertheless, in some cases it is not possible to fully take advantage of the available horsepower across the network.

### Data-access Starvation
Often this occurs because of the data access layer, which cannot feed data quickly enough to the business logic.

This is known as a starvation scenario.

To solve this bottleneck, the processing unit allows you to **colocate** the business logic and data; both data and business logic are hosted within the same process, sharing the same memory space. Colocating business logic and data can be done statically (user service associated with a colocated space), or dynamically (via [Service Executors](./map-reduce-pattern-executors-example.html#Service Executors Example) or [Task Executors](./map-reduce-pattern-executors-example.html#Executors Task Example)).

{{% section %}}

{{% column width="50%" %}}

Collocated Mode - The Task Executor-Step 1:

{{% align center %}}
![DistributedTaskExecution_phase1.jpg](/attachment_files/sbp/DistributedTaskExecution_phase1.jpg)
{{% /align %}}

{{% /column %}}

{{% column width="50%" %}}

Collocated Mode - The Task Executor-Step 2:

{{% align center %}}
![DistributedTaskExecution_phase2.jpg](/attachment_files/sbp/DistributedTaskExecution_phase2.jpg)
{{% /align %}}

{{% /column %}}

{{% /section %}}

### CPU Starvation
In other cases, the bottleneck is not data access, but in the amount of time it takes to process the incoming data (a.k.a. the CPU bound scenario). In this scenario, there is no problem with accessing the data and handing it to the business logic that needs to process it. However, there is a need to run the business logic across as many CPUs and machines as possible, to fully take advantage of the resources over the network.

In this case, it makes better sense to have the data and business logic running in separate processing units, each with its own SLA and scaling requirements. In this case, the business logic may use the [Master-Worker Pattern](./master-worker-pattern.html), having the actual business logic running separately and independently from the space processing unit.

{{% section %}}

{{% column width="50%" %}}

Non-Collocated Mode - The Master Worker Pattern-Step 1:

{{% align center %}}
![master_worker_rr1.jpg](/attachment_files/sbp/master_worker_rr1.jpg)
{{% /align %}}

{{% /column %}}

{{% column width="50%" %}}

Non-Collocated Mode - The Master Worker Pattern-Step 2:

{{% align center %}}
![master_worker_rr2.jpg](/attachment_files/sbp/master_worker_rr2.jpg)
{{% /align %}}

{{% /column %}}

{{% /section %}}

GigaSpaces allows you to build your business logic without having to take the final deployment topology into consideration. The code can be designed, implemented and unit-tested using a single, embedded space colocated with the business logic on your development or testing environment. The same code can then be deployed across a production system involving hundreds of machines having hundreds of spaces with the business logic and data in separated spaces, ultimately processing millions of data items per second with a sub-millisecond latency.

So the question is: to colocate or not to colocate?

# To Colocate or not to Colocate?
When designing your system deployment strategy, you should take a decision about colocating or not colocating your business logic with its state (your application data). This means you should take the following into consideration:

1. Is your business logic designed to process incoming data events without accessing data located in remote other space partitions?
2. Is your data model able to support stickiness, meaning that it can be routed to the same logical partition, based on its content?
3. How big is the data used by a single business logic transaction?
4. What is the time it takes to process the incoming events?
5. What is the amount of data you would like to store within the space?

To help you make the right decision when deploying your application, here are a few guidelines that correlate to the above considerations:

#### Is your business logic designed to process incoming data events without accessing data located in remote other space partitions?
In other words: the business logic can access only its collocated space, or the entire cluster members.

If the collocated space can store both the data required for the processing (cached data), and the consumed data (messages), there is a good chance you can use the collocated mode.

If the business logic needs data stored within other partitions, you might have two space proxies used by the business logic - one that accesses only the collocated space and consumes the incoming "tasks" that need to be processed, and one that accesses all space cluster members and fetches data using space SQL queries needed for the processing. Advanced implementations would use the Map Reduce technique (a.k.a. executors). This popular technique invokes business logic at the relevant partitions that produce intermediate results. These results are then delivered back to the client that aggregates these and returns the final result to the original caller.

#### Is your data model designed to support stickiness?
To allow the application data to be routed to the correct logical partition, associated different objects should have the same routing field value used. A client accessing a remote clustered space has, by default, a space proxy running a simple algorithm before each space operation that calculates the target partition for each space operation.

By default, the calculation uses the hash code value of a field declared as the routing field.

Each space class should have a single routing field declared where the actual field value can be assigned by getting data from possibly several other fields. The routing field value hash code is used to rout write/read operations to the correct partition. For fail-safe operations, a partition may have one or more dedicated backup spaces running in standby mode and holding identical data.

#### How big is the data used by a single business logic transaction?
When a processing unit hosts your business logic, but accesses a remote space running as a separate processing unit within the same machine as the business logic, or in a different remote machine, there is some cost involved that varies depending on the topology. The remote call overhead depends on the network speed, network bandwidth, data complexity (serialization involved), and its size. The larger the size of the serialized and transported data, the longer it takes for the remote operation to be completed. This applies both to write and read operations.

If the amount of data used for each business logic transaction involves a small amount of objects with a relatively small size, colocating the business logic with the data would not impact the performance significantly. This means you should consider running the business logic and the space as separate processing units.

If the amount of data used for each business logic transaction involves a large amount of objects with a relatively large size, colocating the data and the business logic would boost the application performance dramatically.

#### 4. What is the time it takes to process the incoming events?
When a processing unit hosting your business logic has the space colocated as well, no remote calls or serialization calls are involved when the business logic accesses its colocated data. If the time spent executing the business logic ("task" calculation time) is much longer than the time it takes for the business logic to: retrieve the task from the space, write back the result, or read the required data from the space, it might be logical to run the business logic as a standalone processing unit, separately from the space (i.e. use the [Master-Worker Pattern](./master-worker-pattern.html)).

As a rule of thumb, a good ratio for running the business logic separately from the space would be 1:10 or more - i.e. if the average time for performing the three basic space remote calls required to retrieve the object from the space (take, read, write) is ~1 ms, and the time it takes to perform the relevant business logic (unrelated to the space) is ~10 ms, it would be wise to run the business logic as a standalone processing unit. If the ratio is less than 1:10, you should consider colocating the business logic with the data.

#### 5. What is the amount of data you would like to store within the space?
Another important consideration is the total amount of data that your application stores within the space. If you have a relatively small amount of data (that fits into one JVM process), there is no point in partitioning it across many partitions. This means that if you would like to distribute the business logic to many machines having a different scaling ratio than the space, you should not colocate the business logic and the data, but run it as an independent processing unit.

If the amount of data you store within the space is relatively large (more than what can be stored within one JVM process or one machine), and is partitioned across multiple partitions, you should use one of the colocation techniques to allow the business logic and the data to be colocated. You might use only the executors or colocate the business logic service statically, or mix these two options.

## Summary
Here is a simple matrix you can use to determine if you should collocate your space with your business logic or not:


|Scenario|Collocate|Not Collocate|
|:-------|:-------:|:-----------:|
|Data model supports stickiness| {{<oksign>}}| |
|Transaction takes a short time (<10ms)|{{<oksign>}} | |
|Transaction takes a long time (>100ms) | |{{<oksign>}}|
|Transaction uses a small data set (>1K)| | {{<oksign>}}|
|Transaction uses a large data set (>100K)| {{<oksign>}}| |
|Amount of data within the space <2GB| | {{<oksign>}}|
|Amount of data within the space >2GB| {{<oksign>}}| |

# Conclusion
Choosing the right data colocation deployment topology involves several different considerations. There is no one golden rule you should follow. You should review the different considerations described above, to determine the right data colocation deployment topology that fits your application the best.
