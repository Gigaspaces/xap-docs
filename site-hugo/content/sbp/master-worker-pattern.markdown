---
type: post
title:  Master-Worker Pattern
categories: SBP
parent: processing.html
weight: 500
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim| 7.0| August 2009|   |    |




# Overview
The [Master-Worker Pattern](http://books.google.com/books?id=9cV3TbahjW0C&pg=PA153&lpg=PA153&dq=JavaSpaces+Master-Worker+Pattern&source=bl&ots=1l_DQmEGNl&sig=IU2UTbG-xytamrby2r5yaJLnAkk&hl=en&ei=lm6RSo-dGJXjlAeYqOWjDA&sa=X&oi=book_result&ct=result&resnum=1#v=onepage&q=JavaSpaces%20Master-Worker%20Pattern&f=false) (sometimes called the Master-Slave or the Map-Reduce pattern) is used for parallel processing. It follows a simple approach that allows applications to perform simultaneous processing across multiple machines or processes via a `Master` and multiple `Workers`.

{{% align center %}}
![the_master_worker.jpg](/attachment_files/sbp/the_master_worker.jpg)
{{% /align %}}

In GigaSpaces XAP, you can implement the Master-Worker pattern using several methods:

- [Task Executors](./map-reduce-pattern-executors-example.html) - best for a scenario where the processing activity is collocated with the data (the data is stored within the same space as the tasks being executed).
- [Polling Containers](https://docs.gigaspaces.com/latest/dev-java/polling-container.html) - in this case the processing activity runs in a separate machine/VM from the space. This approach should be used when the processing activity consumes a relatively large amount of CPU and takes a large amount of time. It might also be relevant if the actual data required for the processing is not stored within the space, or the time it takes to retrieve the required data from the space is much shorter than the time it takes to complete the processing.

# Implementing Master-Worker in XAP using Polling Containers

The Polling Containers approach uses the classic JavaSpaces write/take operations to implement the parallel processing. This allows a `Master` client application to generate a `Job` that is a set of `Request` objects, write these into the space and immediately perform a Take operation on the `Result` objects.

The `Request` object has an `execute` method that includes the actual processing business logic. The `Workers`, implemented via a polling container, perform a continuous `Take` operation on the `Request` objects. Once a `Request` object has been consumed, its `execute` method is called and a `Result` object is written back into the space. The `Master` application consumes these incoming objects. Once the amount of `Result` objects consumed by the `Master` for the relevant `Job` matches the amount of `Request` objects, the `Job` execution has been completed.

When there is one space (with or without a backup) used by the `Master` and `Workers`, you can run the workers in blocking mode (take operation with timeout > 0). This means that once a matching `Request` is written into the space, one of the running workers immediately consumes it.

When running multiple `Workers`, processing is load-balanced across all the workers in an even manner. When there is a large amount of activity, you might need to run a partitioned space to allow the space layer to store a large number of `Request` objects (there will always be a small number of `Result` objects in the space), and to cope with a large number of `Workers`. This makes sure that your system can scale, and the space layer does not act as a bottleneck.

When running the space in clustered partitioned mode, you cannot run the workers in blocking mode without assigning a value to the `Request` object routing field. The [Designated Workers approach](#example-2-designated-workers) allows you to run the workers against a partitioned space, in blocking mode.

The following sections include code samples and configuration that illustrate the Master-Worker implementation via Polling Containers, using the Random Workers and Designated Workers approach.

{{% tip %}}
We invite you to [download](/attachment_files/sbp/MasterWorker.zip) the code examples and configuration files used with this article.
{{% /tip %}}

# Example 1 - Random Workers
With the Random Workers approach, each worker can consume `Request` objects from **every** space partition. In this case, the non-blocking mode is used. The workers scan the partitions in a round-robin fashion for a `Request` object to consume and execute. With this approach, there might be a small delay until the workers consume a `Request` object. This approach might generate some chatting over the network, since the workers connect to all existing partitions to look for `Request` objects to consume and in case none is found, wait for some time and then try again.

{{% section %}}

{{% column width="50%" %}}

Step 1:

{{% align center %}}
![master_worker_rr1.jpg](/attachment_files/sbp/master_worker_rr1.jpg)
{{% /align %}}

{{% /column %}}

{{% column width="50%" %}}

Step 2:

{{% align center %}}
![master_worker_rr2.jpg](/attachment_files/sbp/master_worker_rr2.jpg)
{{% /align %}}

{{% /column %}}

{{% /section %}}

{{%tabs%}}

{{%tab "  The Master "%}}


```java
public class Master {

	static GigaSpace space;

	public static void main(String[] args) {
		space= new GigaSpaceConfigurer(new UrlSpaceConfigurer("jini://**/**/mySpace")).gigaSpace();

		for (int i=0;i<10;i++)
		{
			submitJob(i, 100);
		}
		System.exit(0);
	}

	static public void submitJob(int jobId , int tasks)
	{
		System.out.println(new Date(System.currentTimeMillis())+
			" - Executing Job " +jobId);
		Request requests [] = new Request [tasks];
		for (int i=0;i<tasks; i++)
		{
			requests [i] = new  Request ();
			requests [i].setJobID(jobId);
			requests [i].setTaskID(jobId + "_"+i);
		}

		space.writeMultiple(requests);
		int count = 0;
		Result reponseTemplate = new Result();
		reponseTemplate.setJobID(jobId);
		Result reponses[] = new Result [tasks];
		while (true)
		{
			Result reponse = space.take(reponseTemplate,1000);
			if (reponse!=null)
			{
				reponses[count]= reponse ;
				count ++;
			}
			if (count == tasks)
			{
				System.out.println(new Date(System.currentTimeMillis())+ " - Done executing Job " +jobId);
				break;
			}
		}
	}
}
```

{{% /tab %}}

{{%tab "  The Worker "%}}


```java
@EventDriven @Polling (concurrentConsumers=2)
public class Worker {

	public Worker ()
	{
		System.out.println("Worker started!");
	}

    @EventTemplate
    Request template() {
    	Request template = new Request();
        return template;
    }

    @ReceiveHandler
    ReceiveOperationHandler receiveHandler() {
        SingleTakeReceiveOperationHandler receiveHandler = new SingleTakeReceiveOperationHandler();
        receiveHandler.setNonBlocking(true);
        receiveHandler.setNonBlockingFactor(10);
        return receiveHandler;
    }

    @SpaceDataEvent
    public Result execute(Request request) {
        //process Data here
    	try {Thread.sleep(1000);} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	Result reponse = new Result ();
    	reponse.setJobID(request.getJobID());
    	reponse.setTaskID(request.getTaskID());
    	return reponse;
    }
}
```

{{% /tab %}}

{{%tab "  The Worker PU config "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:os-events="http://www.openspaces.org/schema/events"
       xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd">

	<!-- Enable scan for OpenSpaces and Spring components -->
	<context:component-scan base-package="org.openspaces.example.masterworker.nonblocking"/>
	<!-- Enable support for @Polling annotation -->
	<os-events:annotation-support />
	<os-core:space-proxy id="space" name="mySpace" />
	<os-core:giga-space id="gigaSpace" space="space"/>
    <os-core:giga-space-context/>
    <os-remoting:annotation-support/>
</beans>
```

{{% /tab %}}

{{%tab "  The Base Space Class "%}}


```java
@SpaceClass
public class Base {

	public Base (){}

	Integer jobID;
	String taskID;
	Object payload;

	@SpaceProperty(index=IndexType.BASIC)
	@SpaceRouting
	public Integer getJobID() {
		return jobID;
	}
	public void setJobID(Integer jobID) {
		this.jobID = jobID;
	}

	@SpaceId(autoGenerate=false)
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}
}
```

{{% /tab %}}

{{%tab "  The Request Class "%}}


```java
@SpaceClass
public class Request extends Base{
	public Request (){}

}
```

{{% /tab %}}

{{%tab "   The Result Class "%}}


```java
@SpaceClass
public class Result extends Base {
	public Result (){}

}
```

{{% /tab %}}

{{%tab "  Deployment Commands "%}}
Deploying the clustered space PU:


```java
>gs deploy-space -cluster schema=partitioned total_members=2 mySpace
```

Deploying the Workers PU:


```java
>gs deploy -cluster total_members=4 MasterWorker.jar
```

{{% /tab %}}

{{% /tabs %}}

# Example 2 - Designated Workers
With this approach, each new worker is assigned a specific ID and consumes `Request` objects from a designated partition. In this case, the worker runs in blocking mode. The `Request` object routing field is populated with the partition ID, with the Polling Container template, and is also populated by the `Master` application before it is written into the partitioned clustered space.

{{% section %}}

{{% column width="50%" %}}

Step 1:

{{% align center %}}
![master_worker_de1.jpg](/attachment_files/sbp/master_worker_de1.jpg)
{{% /align %}}

{{% /column %}}

{{% column width="50%" %}}

Step 2:

{{% align center %}}
![master_worker_de2.jpg](/attachment_files/sbp/master_worker_de2.jpg)
{{% /align %}}

{{% /column %}}

{{% /section %}}

{{% tip %}}
With this approach the number of `Workers` should be greater than or equal to the number of partitions.
{{% /tip %}}

See below how the Designated Workers approach should be implemented:

{{%tabs%}}

{{%tab "  The Master "%}}


```java
public class Master {

	static GigaSpace space;
	static int partitions;

	public static void main(String[] args) {
		space = new GigaSpaceConfigurer(new UrlSpaceConfigurer("jini://*/*/mySpace")).gigaSpace();
		String total_members = space.getSpace().getURL().getProperty(SpaceURL.CLUSTER_TOTAL_MEMBERS);
		if (total_members != null)
			partitions =
				Integer.valueOf(total_members.substring(0,total_members.indexOf(","))).intValue();
		else
			partitions =1;

		for (int i=0;i<10;i++)
		{
			submitJob(i, 100);
		}
		System.exit(0);
	}

	static public void submitJob(int jobId , int tasks)
	{
		System.out.println(new Date(System.currentTimeMillis())+ " - Executing Job " +jobId);
		Request requests [] = new Request [tasks];
		AtomicInteger index = new AtomicInteger(0);
		for (int i=0;i<tasks; i++)
		{
			requests [i] = new  Request ();
			requests [i].setJobID(jobId);
			requests [i].setTaskID(jobId + "_"+i);
			requests [i].setRouting(index.getAndIncrement() %partitions );
		}

		space.writeMultiple(requests);
		int count = 0;
		Result reponseTemplate = new Result();
		reponseTemplate.setJobID(jobId);
		while (true)
		{
			Result _reponses[] = space.takeMultiple(reponseTemplate,Integer.MAX_VALUE);
			if (_reponses.length > 0)
			{
				count = count +_reponses.length;
			}
			if (count == tasks)
			{
				System.out.println(new Date(System.currentTimeMillis())+
					" - Done executing Job " +jobId);
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
```

{{% /tab %}}

{{%tab "  The Worker  "%}}


```java
@EventDriven @Polling (concurrentConsumers=1)
public class Worker implements ClusterInfoAware{

    public void setClusterInfo(ClusterInfo clusterInfo) {
    	System.out.println("--------------- > setClusterInfo called");
    	if (clusterInfo != null)
    	{
            this.clusterInfo = clusterInfo;
			System.out.println("--------------- > Worker " +
				clusterInfo.getInstanceId() + " started");

			String total_members = gigaspace.getSpace().getURL().getProperty(SpaceURL.CLUSTER_TOTAL_MEMBERS);

			int partitions ;
			if (total_members != null)
				partitions =
					Integer.valueOf(total_members .substring(0,total_members.indexOf(","))).intValue();
			else
				partitions =1;

			System.out.println("--------------- > "+ gigaspace.getSpace().getName() +
				" Space got " + partitions + " partitions ");
	        routingValue = (clusterInfo.getInstanceId() - 1) % partitions ;

			System.out.println("--------------- > Worker "+  clusterInfo.getInstanceId() +
				" attached to Partition:"+ routingValue );
    	}
		else
		{
			System.out.println("--------------- > Worker started in non clustered mode");
	        routingValue = 0;
		}
    }

    private ClusterInfo clusterInfo;

	public Worker (){}

    private Integer routingValue;

    @GigaSpaceContext
    GigaSpace gigaspace;

    public void afterPropertiesSet() throws Exception {

    }

    @EventTemplate
    Request template() {
    	Request template = new Request();
    	template.setRouting(routingValue);
        return template;
    }

    @ReceiveHandler
    ReceiveOperationHandler receiveHandler() {
        SingleTakeReceiveOperationHandler receiveHandler = new SingleTakeReceiveOperationHandler();
        return receiveHandler;
    }

    @SpaceDataEvent
    public Result execute(Request request) {
        //process Data here
    	try {Thread.sleep(1000);} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Result reponse = new Result ();
    	reponse.setJobID(request.getJobID());
    	reponse.setTaskID(request.getTaskID());
    	reponse.setRouting(request.getRouting());
    	return reponse;
    }
}
```

{{% /tab %}}

{{%tab "  The Worker PU config "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:os-events="http://www.openspaces.org/schema/events"
       xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd">

	<!-- Enable scan for OpenSpaces and Spring components -->
	<context:component-scan base-package="org.openspaces.example.masterworker.blocking"/>
	<!-- Enable support for @Polling annotation -->
	<os-events:annotation-support />
	<os-core:space-proxy id="space" name="mySpace" />
	<os-core:giga-space id="gigaSpace" space="space"/>
    <os-core:giga-space-context/>
    <os-remoting:annotation-support/>
</beans>
```

{{% /tab %}}

{{%tab "  The Base Space Class "%}}


```java
@SpaceClass
public class Base {

	public Base (){}

	Integer jobID;
	String taskID;
	Object payload;
	Integer routing;

	@SpaceProperty(index=IndexType.BASIC)
	public Integer getJobID() {
		return jobID;
	}
	public void setJobID(Integer jobID) {
		this.jobID = jobID;
	}

	@SpaceId(autoGenerate=false)
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
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

{{% /tab %}}

{{%tab "  The Request Class "%}}


```java
@SpaceClass
public class Request extends Base{
	public Request (){}

}
```

{{% /tab %}}

{{%tab "  The Result Class "%}}


```java
@SpaceClass
public class Result extends Base {
	public Result (){}

}
```

{{% /tab %}}

{{%tab "  Deployment Commands "%}}
Deploying the clustered space PU:


```java
>gs deploy-space -cluster schema=partitioned total_members=2 mySpace
```

Deploying the Workers PU:


```java
>gs deploy -cluster total_members=4 MasterWorker.jar
```

{{% /tab %}}

{{% /tabs %}}


# .NET Implementation

Here is an [example](/download_files/sbp/MasterWorkerForDotNet.zip) of the Master Worker Pattern for .NET.



# References

- [JavaSpaces Principles, Patterns, and Practice: Chapter 11](http://java.sun.com/developer/Books/JavaSpaces/chapter11.html)
- [Blog post:How to Implement my Processor? - The Polling Container Benchmark](https://blog.gigaspaces.com/how-to-implement-my-processor-the-polling-container-benchmark/)

