---
type: post123
title:  Distributed Processing
categories: XAP123NET, PRM
parent: xapnet-basics.html
weight: 400
---



In this part of the tutorial we will introduce you to the different processing services you can run on top of the space.
 XAP includes a set of built-in service components such as Task Execution and Messaging services, each implementing commonly used Enterprise integration patterns.
 It's purpose is to make the implementation of distributed applications on-top of the space simpler and less intrusive and allow you to easily build highly scalable and performing applications.

# Task Execution
Task Execution provides a fine-grained API for performing ad-hoc parallel execution of user defined tasks. This framework should be used in the following scenarios:

* When the tasks are defined by clients and can be changed or added while the data-grid servers are running.
* As a dynamic "stored procedure" enabling to execute complex multi stage queries or data manipulation where the data resides, thus enabling to send back only the end result of the calculation and avoid excess network traffic.
* Scatter/Gather pattern - when you need to perform aggregated operations over a cluster of distributed partitions.



Here is an example of a task. We define a task that will collect all users that made a payment to a specific merchant:

{{%tabs%}}
{{%tab "  Task "%}}

```csharp
using System;
using System.Collections.Generic;

using GigaSpaces.Core;
using GigaSpaces.Core.Metadata;
using GigaSpaces.Core.Executors;

using xaptutorial.model;

[Serializable]
public class MerchantUserTask : ISpaceTask<HashSet<long?>> {
	public long? MerchantId;

	public MerchantUserTask(long? merchantId) {
		this.MerchantId = merchantId;
	}

	public HashSet<long?> Execute(ISpaceProxy spaceProxy, ITransaction tx)  {
		SqlQuery<Payment> query = new SqlQuery<Payment>( "MerchantId = ? ");
		query.SetParameter(1, MerchantId);

		Payment[] payments = spaceProxy.ReadMultiple<Payment>(query, int.MaxValue);
		HashSet<long?> userIds = new HashSet<long?>();

		// Eliminate duplicate UserId
		if (payments != null) {
			for (int i = 0; i < payments.Length; i++) {
				userIds.Add(payments[i].getUserId());
			}
		}
		return userIds;
	}
}
```
{{% /tab %}}
{{%tab "  Execution "%}}

```csharp
public void executeTask()  {
	MerchantUserTask task = new MerchantUserTask(2);

    //Execute the task on a specific node using a specified routing value (2)
	HashSet<long?> result = proxy.Execute(task,2);
}
```
{{% /tab %}}
{{% /tabs %}}

{{%note%}}
 A space task needs to be serializable because it is being serialized and reconstructed at the node.
{{%/note%}}




# Distributed Task
A DistributedTask is a task that ends up executing more than once (concurrently) and returns a result that is a reduced operation of all the different executions.

In the example below we are creating a distributed task that finds all merchants with a specific category. Once all results are returned to the client, reduce is called and a list of all merchants is created.

{{%tabs%}}
{{%tab "  DistributedTask "%}}

```csharp
using System;
using System.Collections.Generic;

using GigaSpaces.Core;
using GigaSpaces.Core.Metadata;
using GigaSpaces.Core.Executors;

using xaptutorial.model;

[Serializable]
public class MerchantByCategoryTask : IDistributedSpaceTask<List<Merchant>, Merchant[]> {

	public ECategoryType CategoryType;

	public MerchantByCategoryTask(ECategoryType categoryType) {
		this.CategoryType = categoryType;
	}

	public Merchant[] Execute(ISpaceProxy spaceProxy, ITransaction tx)   {
		SqlQuery<Merchant> query = new SqlQuery<Merchant>( "Category = ?");
		query.SetParameter(1, CategoryType);
		return spaceProxy.ReadMultiple<Merchant>(query, int.MaxValue);
	}

	public List<Merchant> Reduce(SpaceTaskResultsCollection<Merchant[]> results){

		List<Merchant> list = new List<Merchant>();

		foreach (SpaceTaskResult<Merchant[]>  result in results) {
			if (result.Exception != null) {
				throw result.Exception;
			}

			foreach (Merchant res in result.Result) {
				list.Add (res);
			}
	    }
		return list;
	}
}
```
{{% /tab %}}
{{%tab "  Execution "%}}

```csharp
public void executeDistributedTask(){
	MerchantByCategoryTask task = new MerchantByCategoryTask(
			ECategoryType.AUTOMOTIVE);

	//Execute the task on all the primary nodes with in the cluster
	List<Merchant> result = proxy.Execute(task);
}
```
{{% /tab %}}
{{% /tabs %}}



# Asynchronous Task

A space task can also be executed asynchronously with the corresponding `BeginExecute` `EndExecute` method. This follows the standard .NET asynchronous API, once the execution is complete the execute invoker is notified by the async result which is received from the `BeginExecute` method or to a supplied callback. This will be similar to executing a task in a separate thread, allowing to continue local process while waiting for the result to be calculated at the space nodes.

Here is an example that executes asynchronous using async result:


```csharp
public void executeDistributedTaskAsync()
{
	MerchantByCategoryTask task = new MerchantByCategoryTask(ECategoryType.AUTOMOTIVE);

	IAsyncResult<List<Merchant>> asyncResult = proxy.BeginExecute(task, null /*callback*/, null /*state object*/);
	//	...
	//This will block until the result execution has arrived
	asyncResult.AsyncWaitHandle.WaitOne();
	//Gets the actual result of the async execution
	List<Merchant> result = proxy.EndExecute(asyncResult);
}
```

{{%refer%}}
[Task Execution over the Space]({{%currentneturl%}}/task-execution-over-the-space.html)
{{%/refer%}}






 
