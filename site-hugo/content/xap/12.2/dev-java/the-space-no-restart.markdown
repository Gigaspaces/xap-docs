---
type: post122
title:  Change code without restarts
categories: XAP122, OSS
weight: 650
canonical: auto
parent: the-gigaspace-interface-overview.html
---

 

When executing user code on the space (e.g. space tasks), the space automatically loads the code from the remote client and caches it for future executions.
Since the code is cached, modifications are ignored, and users are forced to restart the space whenever they modify the code.

Starting with 12.1, you can use the `@SupportCodeChange` annotation to tell the space your code has changed.
The space can store multiple versions of the same task. This is ideal for supporting clients using different versions of a task.


This annotation can be used with:

- [Task Execution](./task-execution-overview.html)<br>
- [Custom Change](./change-api-custom-operation.html)<br>
- [Custom Aggregator](./aggregators.html#custom-aggregation)


# Task execution

For example, start with annotating your task with @SupportCodeChange(id="1"), and when the code changes, set the annotation to @SupportCodeChange(id="2"), and the space will load the new task.


{{%tabs%}}
{{%tab "Task version 1"%}}
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


{{%refer%}}
For detailed information on task execution see [Task Execution over the space](./task-execution-overview.html)
{{%/refer%}}





# Custom Change

The annotation can be used for custom change operations.

{{%tabs%}}
{{%tab "Change Version 1"%}}
```java
import com.gigaspaces.annotation.SupportCodeChange;
import com.gigaspaces.client.CustomChangeOperation;
import com.gigaspaces.server.MutableServerEntry;

@SupportCodeChange(id="1")
public class MultiplyIntegerChangeOperation extends CustomChangeOperation {
	private static final long serialVersionUID = 1L;
	private final String path;
	private final int multiplier;

	public MultiplyIntegerChangeOperation(String path) {
		this.path = path;
	}

	@Override
	public String getName() {
		return "multiplyInt";
	}

	public String getPath() {
		return path;
	}

	@Override
	public Object change(MutableServerEntry entry) {
		int oldValue = (Integer) entry.getPathValue(path);
		int newValue = oldValue * 10;
		entry.setPathValue(path, newValue);
		return newValue;
	}
}
```
{{%/tab%}}

{{%tab "Change version 2"%}}
```java
import com.gigaspaces.annotation.SupportCodeChange;
import com.gigaspaces.client.CustomChangeOperation;
import com.gigaspaces.server.MutableServerEntry;

@SupportCodeChange(id="2")
public class MultiplyIntegerChangeOperation extends CustomChangeOperation {
	private static final long serialVersionUID = 1L;
	private final String path;

	public MultiplyIntegerChangeOperation(String path) {
		this.path = path;
	}

	@Override
	public String getName() {
		return "multiplyInt";
	}

	public String getPath() {
		return path;
	}

	@Override
	public Object change(MutableServerEntry entry) {
		int oldValue = (Integer) entry.getPathValue(path);
		int newValue = oldValue * 20;
		entry.setPathValue(path, newValue);
		return newValue;
	}
}
```
{{%/tab%}}

{{%tab "Program"%}}
```java

	GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer("xapSpace")).gigaSpace();

	SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class, "salary > 50");
	ChangeResult<Employee> result = gigaSpace.change(query,
			new ChangeSet().custom(new MultiplyIntegerChangeOperation("salary")),
			ChangeModifiers.RETURN_DETAILED_RESULTS);
```
{{%/tab%}}
{{%/tabs%}}


{{%refer%}}
For detailed information on Custom Change see [Custom Change](./change-api-custom-operation.html)
{{%/refer%}}

# Custom Aggregation

The annotation can be used for custom aggregation operations.

{{%tabs%}}
{{%tab "Aggregation Version 1"%}}
```java
@SupportCodeChange(id ="1")
public class ConcatAggregator extends SpaceEntriesAggregator<String> {

    private final String path;
    private transient StringBuilder sb;

    public ConcatAggregator(String path) {
        this.path = path;
    }

    @Override
    public String getDefaultAlias() {
        return "concat(" + path + ")";
    }

    @Override
    public void aggregate(SpaceEntriesAggregatorContext context) {
        String value = (String) context.getPathValue(path);
        if (value != null)
            concat(value);
    }

    @Override
    public String getIntermediateResult() {
        return sb == null ? null : sb.toString();
    }

    @Override
    public void aggregateIntermediateResult(String partitionResult) {
        concat(partitionResult);
    }

    private void concat(String s) {
        if (sb == null) {
            sb = new StringBuilder(s);
        } else {
            sb.append(',').append(s);
        }
    }
}
```
{{%/tab%}}

{{%tab "Aggregation version 2"%}}
```java
@SupportCodeChange(id ="2")
public class ConcatAggregator extends SpaceEntriesAggregator<String> {

    private final String path;
    private transient StringBuilder sb;

    public ConcatAggregator(String path) {
        this.path = path;
    }

    @Override
    public String getDefaultAlias() {
        return "concat(" + path + ")";
    }

    @Override
    public void aggregate(SpaceEntriesAggregatorContext context) {
        String value = (String) context.getPathValue(path);
        if (value != null)
            concat(value);
    }

    @Override
    public String getIntermediateResult() {
        return sb == null ? null : sb.toString();
    }

    @Override
    public void aggregateIntermediateResult(String partitionResult) {
        concat(partitionResult);
    }

    private void concat(String s) {
        if (sb == null) {
            sb = new StringBuilder(s);
        } else {
            sb.append(':').append(s);
        }
    }
}
```
{{%/tab%}}

{{%tab "Program"%}}
```java
	GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer("xapSpace")).gigaSpace();
 	SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class, "salary > 50");
	AggregationResult result = gigaSpace.aggregate(query, new AggregationSet().add(new ConcatAggregator("name")));
	String concatResult = result.getString("concat(name)");
```
{{%/tab%}}
{{%/tabs%}}


{{%refer%}}
For detailed information on Custom Aggregator see [Aggregators](./aggregators.html)
{{%/refer%}}


# Number of caches

The default limit of class loaders (caches) is set to 3, when breached, the oldest cache is evicted in favor of the new one.
This can be modified via space properties:


| Property name | Description | Default   |
|-----|----|------|
|space-config.remote-code.max-class-loaders | Limit number of class loaders (caches)  |   3 |
|space-config.remote-code.support.code.change | Enable / Disable code change   |  true |
 

 
{{%note%}}
`@SupportCodeChange` without id or with id="" are not cached.
{{%/note%}}


# Limitations

- When using `@SupportCodeChange` no new types, can be introduced to the space.
- Anonymous classes and lambdas do not support the annotation.
 
