---
type: post121
title:  Change code without restarts
categories: XAP121
weight: 650
parent: the-gigaspace-interface-overview.html
---


{{%warning%}}
This page is under construction !
{{%/warning%}}


When executing user code on the space (e.g. space tasks), the space automatically loads the code from the remote client and caches it for future executions.
Since the code is cached, modifications are ignored, and users are forced to restart the space whenever they modify the code.

Starting with 12.1, you can use the `@SupportCodeChange` annotation to tell the space your code has changed.
The space can store multiple versions of the same task. This is ideal for supporting clients using different versions of a task.


This feature can be used for:

- [Task Execution](./task-execution-overview.html)<br>
- [Custom Change](./change-api-custom-operation.html)<br>
- [Custom Aggregator](./aggregators.html#custom-aggregation)


# Task execution

For example, start with annotating your task with @SupportCodeChange(id="1"), and when the code changes, set the annotation to @SupportCodeChange(id="2"), and the space will load the new task.


{{%tabs%}}
{{%tab "Task version 1"%}}
Define a Task with version 1

```java
import org.openspaces.core.executor.Task;

import com.gigaspaces.annotation.SupportCodeChange;

@SupportCodeChange(id="1")
public class DynamicTask implements Task<Integer> {

	@Override
	public Integer execute() throws Exception {
		return new Integer(1);
	}
}
```
{{%/tab%}}

{{%tab "Task version 2"%}}

```java
import org.openspaces.core.executor.Task;

import com.gigaspaces.annotation.SupportCodeChange;

@SupportCodeChange(id="2")
public class DynamicTask implements Task<Integer> {

	@Override
	public Integer execute() throws Exception {
		return new Integer(2);
	}
}
```
{{%/tab%}}

{{%tab "Program"%}}
```java
 GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer("xapSpace")).gigaSpace();

 DynamicTask task = new DynamicTask();

 AsyncFuture<Integer> execute = gigaSpace.execute(task);
 System.out.println(execute.get());
```
{{%/tab%}}

{{%/tabs%}}

<br>
# Custom Change


# Custom Aggregation


# Limitations