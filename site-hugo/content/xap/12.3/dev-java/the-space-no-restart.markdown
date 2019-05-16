---
type: post123
title:  Changing Code without a Restart
categories: XAP123, OSS
weight: 250
canonical: auto
parent: change-api-overview.html
---

The `@SupportCodeChange` annotation is used to tell the Space that your code has changed during runtime, affecting the Space task that needs to be executed. The Space can store multiple versions of the same task. This is useful in supporting client applications that may need to implement multiple different versions of a task.

This annotation can be used with:

- [Task Execution](./task-execution-overview.html)<br>
- [Custom Change](./change-api-custom-operation.html)<br>
- [Custom Aggregator](./aggregators.html#custom-aggregation)


# Task Execution

The annotation can be used when defining task execution. For example, annotate your task with `@SupportCodeChange(id="1")`. When the code changes, set the annotation to `@SupportCodeChange(id="2")`, and the Space will load the new task.


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
For detailed information about task execution, see the [Task Execution](./task-execution-overview.html) topic.
{{%/refer%}}

# Custom Change

The annotation can be used for custom change operations, as demonstrated in the example below.

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

{{%tab "Change Version 2"%}}
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
For detailed information about the Custom Change operation, see the [Custom Change](./change-api-custom-operation.html) topic.
{{%/refer%}}

# Custom Aggregation

The annotation can also be used for custom aggregation operations, as demonstrated by the following example.

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

{{%tab "Aggregation Version 2"%}}
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
For detailed information about how to use custom aggregators, see the [Aggregators](./aggregators.html) topic.
{{%/refer%}}


# Number of Caches

The default limit of class loaders (caches) is 3. when this limit is breached, the oldest cache is evicted in favor of the new one.
This value can be modified via the following Space properties:


| Property Name | Description | Default   |
|-----|----|------|
|space-config.remote-code.max-class-loaders | Limit number of class loaders (caches)  |   3 |
|space-config.remote-code.support.code.change | Enable/Disable code change   |  true |
 

 
{{%note%}}
`@SupportCodeChange` annotations without an ID or with `id=""` are not cached.
{{%/note%}}


# Limitations

- When using `@SupportCodeChange`, no new types can be introduced to the Space.
- Anonymous classes and lambdas do not support the annotation.
 
