---
type: post121
title:  Custom Change
categories: XAP121
parent: change-api-overview.html
weight: 200
---

{{% ssummary %}}{{% /ssummary %}}


A custom change operation lets the user implement his own change operation in case the built-in operations (increment, add, remove, set, etc.) do not suffice. This is a very powerful capability but it must be used with extreme caution.

# Implementing and Using a Custom Change Operation

The implementation should extend the abstract `CustomChangeOperation` class and implement both the `getName` and `change` methods.
See below an example of a change operation which multiplies an integer property value:


```java
public class MultiplyIntegerChangeOperation extends CustomChangeOperation {
  private static final long serialVersionUID = 1L;
  private final String path;
  private final int multiplier;

  public MultiplyIntegerChangeOperation(String path, int multiplier) {
    this.path = path;
    this.multiplier = multiplier;
  }

  @Override
  public String getName() {
    return "multiplyInt";
  }
  
  public String getPath() {
	return path;
  }
  
  public int getMultiplier() {
	return multiplier;
  }

  @Override
  public Object change(MutableServerEntry entry) {
    //Assume this is an integer property, if this is not true an exception will be thrown 
    //and the change operation will fail
    int oldValue = (Integer)entry.getPathValue(path);
    int newValue = oldValue * multiplier;
    entry.setPathValue(path, newValue);
    return newValue;
  }  
}
```

Using it will be like any other change operation, while providing this custom implementation:


```java
gigaSpace.change(query, new ChangeSet().custom(new MultiplyIntegerChangeOperation("votes", 2)));
```

With Java 8 lambda syntax the above can be done in a simpler way and without extending the `CustomChangeOperation` interface:

```java
gigaSpace.change(query, new ChangeSet().custom("multiplyInt", (entry) -> {
        //Assume this is an integer property, if this is not true an exception will be thrown 
        //and the change operation will fail
        int oldValue = (Integer)entry.getPathValue("votes");
        int newValue = oldValue * 2;
        entry.setPathValue("votes", newValue);
        return newValue;
    }));
```


# The Name of a Custom Change Operation 

The custom operation is treated like the built-in change operations (in fact the build in implementations are using the same mechanism), therefore the operation should have a unique name which is used in all the relevant places as described in the [Change API Advanced Page](./change-api-advanced.html), such as configuring which operations are supported by a `SpaceSynchronizationEndpoint` implementation, using it inside space and replication filters to identify which custom change operation is executed, etc.

# Mandatory Implementation Requirements 

When implementing a custom change operation, the following guidelines must be followed:

The provided `MutableServerEntry` is wrapping the actual object which is kept in space, therefore it is crucial to understand when a value is retrieved from the entry
it points to the actual reference in the data structure of the Space. The content of this reference should not be changed as it will directly affect the object in Space and will break data integrity, transaction management and visibility scoping (e.g. transaction abort will not restore the previous value). Changing a value should always be done via the [MutableServerEntry]({{% api-javadoc %}}/com/gigaspaces/server/MutableServerEntry.html#setPathValue(String, Object)).
Moreover, if you want to change a property within that value by invoking a method on that object (e.g. if the value is a list, adding an item to the list), you must first clone the fetched value, and then invoke the method on the cloned copy. Otherwise, you will change the existing data structure in the space without going through the proper data update mechanism and will potentially break data integrity.

Below you can find an example that adds the element 2 into an ArrayList that exists in the entry under a property named "listProperty". The result sent to client (if requested) is the size of the collection after the change. Note that we clone the ArrayList before modifying it as explained above.
	 

```java
public Object change(MutableServerEntry entry) {
  ArrayList oldValue = (ArrayList)entry.getPathValue("listPropery");
  if (oldValue == null)
    throw new IllegalStateException("No ArrayList instance exists under the given path 
                                     'listProperty', in order to add a value an ArrayList 
                                     instance must exist");
  Collection newValue = (ArrayList)oldValue.clone()
  newValue.add(2);
  int size = newValue.size();
  entry.setPathValue("listProperty", newValue);
  return size;
}
```

{{% tip %}}
`getPathValue`, `setPathValue` operations support nested paths, it will traverse on properties and map keys if the path contains '.' in it (e.g. "myPojo.mapProperty.key")
{{% /tip %}}   
	 
{{% info %}}
When using a replicated topology (e.g. with backup space instances, gateways, mirror) the change operation itself is replicated (and *NOT* the modified entry). Hence, it is imperative that this method will always cause the exact same affect on the entry (assuming the same entry was provided). For example it should not rely 
on variables that may change between executions, such as system time, random, machine name etc.
If the operation is not structured that way, the state can be inconsistent in the different locations after being replicate. {{% /info %}}

# Space Security

Custom change operation lets you run custom code on the space, hence the space security privilege required in order to execute a custom change operation is `EXECUTE` (the same role which allows executing space tasks).

# Custom Operation and Space Integration Points

Using a custom operation with a [Replication Filter]({{%currentadmurl%}}/cluster-replication-filters.html), [Space Filter](./the-space-filters.html) and [Space Synchronization Endpoint](./space-synchronization-endpoint-api.html) is supported
and behaves the same as the built-in operations. You can get a reference to the instance of the `CustomChangeOperation` by checking its name (or `instanceof`) and casting to the specific type.


```java
DataSyncChangeSet dataSyncChangeSet = ChangeDataSyncOperation.getChangeSet(dataSyncOperation);
Collection<ChangeOperation> operations = dataSyncChangeSet.getOperations();
for(ChangeOperation operation : operations) {
  if (operation.getName().equals("multiply") {
    String path = ((MultiplyIntegerChangeOperation)operation).getPath();
	int multiplier = ((MultiplyIntegerChangeOperation)operation).getMultiplier();    
    // ... do something with the path and multiplier
  }
  //...
}
```

# Change code without restarts

When executing a change over the space, the code is loaded from the remote client and cached for future executions.
Since the code is cached, modifications are ignored, and users are forced to restart the space whenever they modify the code.

Starting with 12.1, you can use the `@SupportCodeChange` annotation to tell the space your code has changed.
The space can store multiple versions of the same task. This is ideal for supporting clients using different versions of a task.


For example, start with annotating your task with @SupportCodeChange(id="1"), and when the code changes, set the annotation to @SupportCodeChange(id="2"), and the space will load the new task.

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
[Change code without restarts](./the-space-no-restart.html)
{{%/refer%}}



