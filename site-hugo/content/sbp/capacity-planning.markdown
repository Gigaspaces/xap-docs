---
type: post
title:  Capacity Planning
categories: SBP
parent: production.html
weight: 101
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim|  | Feb 2014|    |    |


# Capacity Planning - Voodoo or Simple Math?
Planning the number of machines your application requires, is one of the most important tasks you need to do before moving into production and making your application available to your customers/users. Typically, this is a task you need to do in the early stages of the project in order to budget for hardware and relevant software products your system will use. At a minimum, this involves estimating the number of CPUs and cores plus the memory each machine must have.

Another important deployment decision is to calculate the maximum number of In-Memory-Data-Grid (IMDG) partitions the application requires. This deployment parameter determines how scalable your application IMDG is. Once the application is deployed, the number of IMDG partitions remains constant, but their physical location, their activation mode (primary or backup), and hosting containers are dynamic.

The IMDG instances could fail and relocate themselves to another machine automatically. They can relocate as a result of an SLA (Service-Level-Agreement) event (for example - CPU and memory utilization breach), or they can relocate, based on manual intervention by an administrator. This IMDG "mobility" is a critical capability that, beyond providing the application with the ability to scale dynamically and self-heal itself, can avoid over provisioning and unnecessary over budgeting of your hardware. You can start small, and grow as needed with your hardware utilization. The capacity planning should take this fact into consideration.

This article deals with the following capacity planning issues:

1. How to calculate the footprint of your objects when stored within the IMDG?
2. What is the balance between the amount of active clients, machine cores, and the IMDG process heap size?
3. How should the number of IMDG Partitions be calculated? We demonstrate how this should be done using 2 examples.

Please note that there is no need to specify the maximum number of partitions for services that are not collocated with IMDG instances. These can be scaled dynamically without the need to specify maximum instances.

# The Object Footprint within the IMDG
The object footprint within the IMDG is determined, based on:

- The original object size - the number of object fields and their content size.
- The JVM type (32 or 64 bit) - a 64 bit JVM might consume more memory due to the pointer address size.
- The number of indexed fields - every indexed value means another copy of the value within the index list.
- The number of different indexed values - more different values means a different index list.
- The object UID size - the UID is a string-based field, and consumes about 35 characters. You might have the object UID based on your own unique ID.

The best way to determine the exact footprint is via a simple test that allows you to perform some extrapolation when running the application in production. Here is how you should calculate the footprint:

1. Start a single IMDG instance.
2. Take a measurement of the free memory.
3. Write a sample number of objects into the IMDG (have a decent number of objects written - 100,000 is a good number).
4. Measure the free memory again.

This test gives you a very good understanding of the object footprint within the IMDG. Don't forget that if you have a backup running, the amount of memory you need to accommodate your objects, is double.

See below an example of an object footprint using a 32 and 64 Bit JVM using different amount of indexes. The numbers below may vary based on the actual Object data and indexes values.


|Indexes	|Footprint 32 Bit JVM - Windows|Footprint 64 Bit JVM - Linux|Footprint 64 Bit JVM - UseCompressedOops - Linux|
|:--|:----|:-----------------------------|:----------------------------------------------------------------------------|
|0	|331	|306	|308|
|1 - String	|456	|705	|493|
|2 - String+Integer	|493	|989	|671|
|3 - String+Integer+Long	|533	|1002	|680|
|4 - String+Integer+Long+Double	|571	|1026	|691|

{{% align center %}}
![footprint_bench7.1.2.jpg](/attachment_files/sbp/footprint_bench7.1.2.jpg)
{{% /align %}}

- Test conducted using GigaSpaces XAP 7.1.2.
- All objects values are different.
- The Object has one String field, one Integer field, one Long field and one Double field.
- Footprint measured in Bytes.
- Basic Index type is used. An Extended Index type will have an additional footprint (20%) compared to a Basic Index type.

{{% tip %}}
You may decrease the raw object footprint (not the indexes footprint) using the [GigaSpaces Serialization API](./lowering-the-space-object-footprint.html)
{{% /tip %}}

{{% tip %}}
You can reduce the JVM memory footprint using the `-XX:+UseCompressedOops` JVM option. It is part of the JDK6u14 and JDK7. See more details here: [https://wikis.oracle.com/display/HotSpotInternals/CompressedOops](https://wikis.oracle.com/display/HotSpotInternals/CompressedOops). It is highly recommended to use the latest JDK release when using this option.
{{% /tip %}}

# Active Clients vs. Cores vs. Heap Size
Since the IMDG kernel is a highly multi-threaded process, it has a relatively large number of active threads handling incoming requests. These requests could come from remote clients or collocated clients. Here are a few examples:

- any remote call involves a thread at the IMDG side that handles the request.
- a notification delivery might involve multiple threads sending events to registered clients.
- any destructive operation (write, update, take) also triggers a replication event that is handled via a dedicated replication channel, which is using a dedicated thread to handle the replication request to the backup IMDG instance.
- there is some periodic background activity, used to monitor the relevant components that are using its own dedicated threads within the IMDG kernel.

A collocated client does not go through the network layer, and interacts with the IMDG kernel very fast. This means that the machine CPU core that has been assigned to deal with this thread activity is very busy, and it does not wait for the operating system to handle IO operations. Taking this fact into consideration means we can have less concurrent clients served by the same core when compared to remote clients activity.

The number of active threads and machine cores is important also for the maximum heap size allocated for the JVM running the GSC. You should have some "headroom" for the JVM garbage collection activity to deal with cleaning allocated resources and reclaiming unused memory. A large heap size means a potentially large number of temporary objects that might generate work for the garbage collection activity. So you should have some reasonable balance between the number of cores the machine is running, the number of GSCs/IMDG running, and the number of active clients/threads accessing the IMDG.

A machine running 4 quad-core cores with fast CPUs (3GHz clock) is able to handle 20-30 concurrent collocated clients without any special delay, and 100-150 concurrent remote clients without any special delay. Such a JVM should have at least a 2-3GB heap size to handle the IMDG data and additional resources utilizing memory. With the above we assume the application business logic is very simple and does not have any IO operations, and the IMDG persistency mode is asynchronous.

# How should I Calculate the Number of IMDG Partitions?
The question is how to calculate the number of IMDG partitions required by an application.
The calculation is essentially based on the maximum number of machines available. Theoretically, in an ideal world where you have an unlimited budget and unlimited resources, you might want to have a dedicated machine per IMDG instance hosted within a dedicated Grid Container (GSC). In the real world, in order to avoid a large budget for hardware, you can initially have multiple IMDG instances running on the same machine within a single GSC. This deployment topology can be modified later to move closer to the ideal of a dedicated GSC hosting a single IMDG instance (one IMDG instance per GSC).

The number of GSCs per machine you need initially, is calculated based on the machine's physical RAM and the amount of heap memory you would like to have for the JVM running the GSC. In many cases the heap size is determined based on the operating system: for a 32 bit OS, you would go for a 2G maximum heap size, and for a 64 bit OS, you would go for 6-10G maximum heap size (the JVM -Xmx argument). For performance optimization you should have the initial heap size the same as the maximum size. The next sections demonstrate capacity planning using a simple real life example.

Here are a few basic formulas you can use:


```java
Amount of GSCs per Machine = Amount of Total Machine Cores/2
```


```java
Total Amount of GSC = Amount of GSCs per Machine X Initial amount of Machines
```


```java
GSC max heap Size = min (6, (Machine RAM Size * 0.8) / Amount of GSCs per Machine))
```


```java
Amount of Data-Grid Partitions = Total Amount of GSC X Scaling Growth Rate / 2
```

Where:

- Number of Total Machine Cores - the total number of cores the machine is running. For a quad-core with 2 CPUs (Duo) machine this value is 8.
- Number of Data-Grid Partitions - this is the number of IMDG Partitions you need to set when deploying.
- GSC max heap Size - JVM Xmx value
- Number of GSCs per machine - the number of GSCs you run per machine. This is a GSA parameter.
- Total amount of data in-memory - this is a number you should estimate, based on the object footprint you are storing within the space.
- Scaling Growth Rate - this is the expansion ratio. Usually it might be between 2 to 10. This value determines how much you would like to expand the Data-Grid capacity without downtime.
- Initial number of machines - the initial available machines you have when deploying the Data-Grid.
- Machine RAM Size - amount of physical RAM a machine has.
- Total number of GSCs - total number of GSCs that are initially running when deploying the Data-Grid.

## Example 1 - Basic Capacity Planning
With our example, we initially have 2 machines used to run our IMDG application. 10 machines might be allocated for the project within the next 12 months. Each machine has 32GB of RAM with 4 quad-core CPUs. This gives us a total of 64GB of RAM. Later, when all 10 machines are available we will have potentially 320GB of total memory. The memory is used both by the primary IMDG and the backup IMDG instances (exact replica of the primary machines).

The machines run Linux 64 bit OS. Allocating 6GB per JVM as the max heap size for the GSC, results in 5 GSCs per machine - i.e. 10 GSCs initially across 2 machines. Once we use all the machines our full budget allows us, we will have 50 GSCs.

{{% align center %}}
![capacity_planning1.jpg](/attachment_files/sbp/capacity_planning1.jpg)
{{% /align %}}

Figure 1: 2 Machines Topology - five IMDG Instances per GSC, total 64GB RAM

Having a maximum of 40 GSCs hosting the IMDG, means you might want to have half of them (20 GSCs) running primary IMDG instances and the other half running backup instances. This number is used as the number of partitions the IMDG is deployed with - this means that with the 2 machines we have initially, 10 GSCs host 40 IMDG instances. Later, as needed, these IMDG instances will be relocated to new GSCs that are started on new machines. Each machine will start 4 GSCs that will join the GigaSpaces grid and will allow the administrator to manually or automatically expand the capacity of the IMDG while the application is running.

{{% align center %}}
![capacity_planning2.jpg](/attachment_files/sbp/capacity_planning2.jpg)
{{% /align %}}

Figure 2: 10 Machines Topology - one IMDG Instance per GSC, total 320GB RAM

This rebalancing of the IMDG instances can be done via the UI, or via a simple program using the new Admin API released with XAP 7.0. Future versions of XAP will have built-in re-balancing models you may use.

Note that being able to determine the number of IMDG partitions prior to the application deployment allows you to have exact knowledge of how your routing field values should be distributed, and how your data will be partitioned across the different JVMs that will be hosting your data in-memory.

## Example 2 - Capacity Planning when Running on the Cloud
In a cloud environment, you have access to essentially unlimited resources. You can spin up new virtual machines and have practically unlimited amounts of memory and CPU power. With such an environment, the calculation of the maximum number of IMDG partitions cannot be based on the maximum number of machines you might have allocated for your project. Theoretically you could have an unlimited number of machines started on the cloud to host your IMDG.

Still, you must have some value for the maximum number of IMDG partitions when deploying your application. In such cases, you should calculate the number of IMDG partitions, based on the amount of memory your application might generate and store within the IMDG.

Here is a simple example you might use:
An application using 3 types of classes to store its data within the IMDG.

- Class A - object average size is 1KB
- Class B - object average size is 10KB
- Class C - object average size is 100KB

The application needs to generate 1 million objects for each type of class during its life cycle:

- Class A - total memory needed = 1KB X 1M = 1GB
- Class B - total memory needed = 10KB X 1M = 10GB
- Class C - total memory needed = 100KB X 1M = 100GB

Total memory required to store application data in memory = 111GB

When using a machine with 32GB of RAM, we need 4 machines to run primary IMDG instances to store 111GB of data in memory and another 4 machines to run backup IMDG instances. For a 64 bit OS we have 5 GSCs, each having a 6GB maximum heap size - for a total of 40 GSCs (5 X 4 X 2). With 20 GSCs used to run primary instances, we could pick 80 as the number of partitions we could have (each GSC will initially host 4 IMDG instances).

This means that we have 160 IMDG instances (half primary and half backups) hosted within 40 GSCs. Theoretically, this allows us to expand the IMDG to run across 160 machines (one GSC per machine). This means 160 X 10GB as the heap size = 1.6TB of IMDG memory capacity to host the IMDG objects!  This is a huge capacity for the IMDG, which is in fact, ten times larger than the estimated size - we have lots of room in case our initial memory utilization was wrong.


# Used Memory Utility

Checking the used memory on all primary instances can be done using the following (we assume we have one GSC per primary instance):


```java
GigaSpace gigaSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer(spaceURL)).gigaSpace();
Future<Long> taskresult = gigaSpace.execute(new FreeMemoryTask());
long usedMem = taskresult.get();
System.out.println("Used Mem[MB] " + (double)usedMem/(1024*1024));
```

The `FreeMemoryTask` implementation:


```java
import java.util.Iterator;
import java.util.List;

import org.openspaces.core.executor.DistributedTask;

import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.async.AsyncResult;

public class FreeMemoryTask implements DistributedTask<Long, Long>{

	Integer routing;
	public Long execute() throws Exception {
		
		Runtime rt = Runtime.getRuntime();
		System.out.println("Calling GC...");
		rt.gc();
		Thread.sleep(5000);
		System.out.println("Done GC..." + 
				" Used memory " + (rt.totalMemory() - rt.freeMemory() )+
				" Free Memory " + rt.freeMemory() + 
				" MaxMemory " + rt.maxMemory() + 
				" Committed memory "+ rt.totalMemory());
		
		return (rt.totalMemory() - rt.freeMemory() );
	}

	@Override
	public Long reduce(List<AsyncResult<Long>> _usedMemList) throws Exception {
		
		long totalUsed =0;
		Iterator<AsyncResult<Long>> usedMemList = _usedMemList.iterator();
		while (usedMemList.hasNext())
		{
			totalUsed  = totalUsed + usedMemList.next().getResult();
		}
		return totalUsed ;
	}

	@SpaceRouting
	public Integer getRouting() {
		return routing;
	}

	public void setRouting(Integer routing) {
		this.routing = routing;
	}
}

```


# Conclusion
Capacity planning is not voodoo. You can estimate the number of machines your application IMDG might need via a simple capacity planning process. To avoid over provisioning, you should start small, and expand your IMDG capacity when needed. The maximum number of IMDG partitions can be calculated, based on a simple estimation of the number of machines you have available, or based on the size and quantity of objects your application generates. This allows your application to scale while remaining resilient and robust.

