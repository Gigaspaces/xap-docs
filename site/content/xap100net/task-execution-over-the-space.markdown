---
type: post100net
title:  Task Execution
categories: XAP100NET
parent: programmers-guide.html
weight: 1000
---



{{% ssummary %}}{{% /ssummary %}}



{{%section%}}
{{%column width="60%" %}}
XAP support executing tasks in a collocated Space (processing unit that started an embedded Space). Space tasks can be executed either directly on a specific cluster member using typical routing value. Space tasks can also be distributed, which means it is executed in a "broadcast" mode on all the primary cluster members concurrently and reduced to a single result on the client side, which is also known as the map reduce pattern which is used in many applications that does parallel processing. Space tasks are dynamic in terms of content, it contains user code that will be executed at the Space as is.
{{%/column%}}
{{%column width="40%" %}}
![Executors_task_flow_basic.jpg](/attachment_files/Executors_task_flow_basic.jpg)
{{%/column%}}
{{%/section%}}

# Space Task API

The `ISpaceTask` interface is defined as follows:


```csharp
public interface ISpaceTask<T>
{
  T Execute(ISpaceProxy spaceProxy, ITransaction tx);
}
```

Here is a simple implementation of a space task that calculates the number of objects on a single node and prints a message at the single node output.


```csharp
[Serializable]
public class CountTask : ISpaceTask<int>
{
  private String _message;

  public CountTask(String message)
  {
    _message = message;
  }

  public int Execute(ISpaceProxy spaceProxy, ITransaction tx)
  {
    System.Console.WriteLine(message);
    return spaceProxy.Count();
  }
}
```

{{% info %}}
A space task needs to be serializable because it is being serialized and reconstructed at the node.
The assembly that contains the task needs to be in the domain of the processing unit that contains the embedded space (present at its deployment directory).
{{%/info%}}

**Executing the space task**


```csharp
ISpaceProxy spaceProxy = // obtain a proxy to a space
//Execute the task on a specific node using a specified routing value (2)
//And inserting the calculation result to count variable
int count = spaceProxy.Execute(new CountTask("hello world"), 2);
```

# Distributed Task

A `IDistributedSpaceTask` is a space task that ends up executing more than once (concurrently) and returns a result that is a reduced operation of all the different execution.

{{%section%}}
{{%column width="45%" %}}
Phase 1 - Sending the Space tasks to be executed:
![DistributedTaskExecution_phase1.jpg](/attachment_files/DistributedTaskExecution_phase1.jpg)
{{%/column%}}
{{%column width="45%" %}}
Phase 2 - Getting the results back to be reduced:
![DistributedTaskExecution_phase2.jpg](/attachment_files/DistributedTaskExecution_phase2.jpg)
{{%/column%}}
{{%/section%}}

The `IDistributedSpaceTask` interface is a composition both `ISpaceTask` and `ISpaceTaskResultsReducer` interfaces.

The `ISpaceTaskResultsReducer` is defined as follows:


```csharp
public interface ISpaceTaskResultsReducer<R, T>
{
  R Reduce(SpaceTaskResultsCollection<T> results);
}
```

Here is a simple example of a distributed space task that extends our previous example:


```csharp
[Serializable]
public class DistributedCountTask : IDistributedSpaceTask<long, int>
{
  private String _message;

  public CountTask(String message)
  {
    _message = message;
  }

  public int Execute(ISpaceProxy spaceProxy, ITransaction tx)
  {
    System.Console.WriteLine(message);
    return spaceProxy.Count(new Object());
  }

  public long Reduce(SpaceTaskResultsCollection<int> results)
  {
    int sum = 0;
    foreach(SpaceTaskResult<int> result in results)
    {
      if (result.Exception != null)
        throw result.Exception;
      sum += result.Result;
    }
    return sum;
  }
}
```

This task will execute on each one of the primary nodes in the cluster,
it will print the message in each node and it will return the summary of the count result on each node (Equivalent to ISpaceProxy.Count(new Object()) method).

**Executing the distributed task**


```csharp
ISpaceProxy spaceProxy = // obtain a proxy to a space
//Execute the task on all the primary nodes with in the cluster
//and inserting the calculation result to count variable
long count = spaceProxy.Execute(new DistributedCountTask("hello world"));
```

# Results Filter

When executing a distributed space task, results arrive in an asynchronous manner and once all the results have arrived, the `ISpaceTaskResultsReducer` is used to reduce them. The `ISpaceTasukResultsFilter` can be used as a callback and filter mechanism to be invoked for each result that arrives.


```csharp
public interface ISpaceTaskResultsFilter<T>
{
  SpaceTaskFilterDecision GetFilterDecision(SpaceTaskFilterInfo<T> info);
}


// Controls what should be done with the results of an execution.
public enum SpaceTaskFilterDecision
{
  // Continue processing the distributed task.
  Continue = 0,
  // Break out of the processing of the distributed task and move
  // to the reduce phase including the current result.
  Break = 1,
  // Skip this result and continue processing the rest of the results.
  Skip = 2,
  // Skip this result and breaks out of the processing of the distributed task
  // and move to the reduce phase.
  SkipAndBreak = 3,
}
```

The filter can be used to control if a result should be used or not (the `Skip` decision). If we have enough results and we can move to the reduce phase (the `Break` decision). If we should continue accumulating results (the `Continue` decision). Or if we don't want to use the current result and move to the reduce phase (the `SkipAndBreak` decision).

The filter can also be used as a way to be identify that results have arrived and we can do something within our application as a result of that. Note, in this case, make sure that heavy processing should be performed on a separate (probably pooled) thread.

# Transactions

Space tasks fully support transactions, an execute request can receive a
transaction from the client and it will be delegated into the task execution it self once it is being executed at the space node.

When executing a single space task, usually a local transaction will suffice, while when executing a distributed space task, a distributed transaction will be required.

The transaction creation, commit and abort normally should be done at the client according to the result.

Here's a simple example


```csharp
ISpaceProxy spaceProxy = // obtain a proxy to a space

ITransaction tx = spaceProxy.LocalTransactionManager.Create();
try
{
  //Execute the task on a specific node using a specified routing value (2)
  //And inserting the calculation result to count variable
  int count = spaceProxy.Execute(new ClearMyObjectTask(), 2, tx);
  tx.Commit();
}
catch(Exception ex)
{
  tx.Abort();
}

[Serializable]
public class ClearMyObjectTask : ISpaceTask<int>
{
  public int Execute(ISpaceProxy spaceProxy, ITransaction tx)
  {
    MyObject template = new MyObject();
    int result spaceProxy.Count(template, tx);
    spaceProxy.Clear(template, tx);
    return result;
  }
}
```

# Asynchronous Execution

A space task can also be executed asynchronously with the corresponding `BeginExecute` `EndExecute` method. This follows the standard .NET asynchronous API, once the execution is complete the execute invoker is notified by the async result which is received from the `BeginExecute` method or to a supplied callback. This will be similar to executing a task in a separate thread, allowing to continue local process while waiting for the result to be calculated at the space nodes.

#### Executing asynchronous space using async result


```csharp
ISpaceProxy spaceProxy = // obtain a proxy to a space
//Execute the task on all the primary nodes with in the cluster
IAsyncResult<long> asyncResult = spaceProxy.BeginExecute(new DistributedCountTask("hello world"), null /*callback*/, null /*state object*/);

...
//This will block until the result execution has arrived
long count = spaceProxy.EndExecute(asyncResult);
```

#### Executing asynchronous space using async result wait handle


```csharp
ISpaceProxy spaceProxy = // obtain a proxy to a space
//Execute the task on all the primary nodes with in the cluster
IAsyncResult<long> asyncResult = spaceProxy.BeginExecute(new DistributedCountTask("hello world"), null /*callback*/, null /*state object*/);

...
//This will block until the result execution has arrived
asyncResult.AsyncWaitHandle.WaitOne();
//Gets the actual result of the async execution
long count = spaceProxy.EndExecute(asyncResult);
```

#### Executing asynchronous space using async call back and a state object


```csharp
ISpaceProxy spaceProxy = // obtain a proxy to a space
//Execute the task on all the primary nodes with in the cluster
spaceProxy.BeginExecute(new DistributedCountTask("hello world"),ResultCallBack, new MyStateObject());

...

public void ResultCallBack<long>(IAsyncResult<long> asyncResult)
{
	//Gets the state object attached to this execution
	MyStateObject stateObject = (MyStateObject)asyncResult.AsyncState;
	...
	//Gets the actual result of the async execution
	long count = spaceProxy.EndExecute(asyncResult);
	...
}
```
