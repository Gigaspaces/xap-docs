---
type: post123
title:  Counters
categories: XAP123, OSS
weight: 500
parent: the-gigaspace-interface-overview.html
---



A growing number of applications such as real time ad impressions , ad optimization engines, social network , on-line gaming , need real-time counters when processing incoming streaming of events. The challenge is to update the counter in atomic manner without introducing a bottleneck event processing flow.


{{% imagertext "/attachment_files/change-api-counter.jpg" %}}
XAP introducing Counter functionality via the `GigaSpace.change` API. It allows you to increment or decrement an Numerical field within your Space object (POCO or Document). This change may operate on a numeric property only (byte,short,int,long,float,double) or their corresponding Boxed variation. To maintain a counter you should use the Change operation with the `ChangeSet` increment/decrement method that adds/subtract the provided numeric value to the existing counter.
{{% /imagertext%}}



There is no need to use a transaction when getting the counter value as the counter is atomic.
If the counter property does not exists, the delta will be set as its initial state. This simple API allows you to maintain counters with minimal impact on the system performance as it is replicating only the `ChangeSet` command and not the entire space object to the backup copy when running a clustered data-grid.


# Incrementing

Incrementing a Counter done using the `ChangeSet().increment` call:


```java
GigaSpace space = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(WordCount.class, id);
space.change(idQuery, new ChangeSet().increment("mycounter", 1));
```

# Decrementing

Decrementing a Counter done using the `ChangeSet().decrement` call:


```java
GigaSpace space = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(WordCount.class, id);
space.change(idQuery, new ChangeSet().decrement("mycounter", 1));
```

# Clearing

Clearing the Counter value done using the `ChangeSet().unset` call:


```java
GigaSpace space = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(WordCount.class, id);
space.change(idQuery, new ChangeSet().unset("mycounter"));
```

# Getting the value

Getting the Counter value done via the read call:


```java
GigaSpace space = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(WordCount.class, id);
WordCount wordount = space.readById(WordCount.class , idQuery);
int counterValue = wordount.getMycounter();
```

Another way getting the Counter value without reading the space object back to the client would be via a [Task](./task-execution-over-the-space.html):


```java
public class GetCounterTask implements Task<Integer> {

	String id ;

	public GetCounterTask (string id) {
		this.id= id;
	}

  	@TaskGigaSpace
  	private transient GigaSpace space;

  	public Integer execute() throws Exception {
		IdQuery<WordCount> idQuery = new IdQuery<WordCount>(WordCount.class, id);
		WordCount wordount = space.readById(WordCount.class , idQuery);
		return wordount.getMycounter();
  }
}
```

Call the execute method to fetch the current Counter value:


```java
AsyncFuture<Integer> future = gigaSpace.execute(new GetCounterTask("myID"), routingValue);
int counterValue= future.get();
```

# Pre-Loading

When pre-loading the space via the [External Data Source initial-load](./space-persistency-initial-load.html) you may need to construct Counters data. The `initialLoad` method allows you to implement the logic to generate the counter data and load it into the space after the actual data been loaded from the external data source (database).

# Example

With the following example the `Counter` class wraps the `GigaSpace.change` operation providing simple `increment`,`decrement`,`get` and `unset` methods to manage counters. The example using an [extended SpaceDocument](./document-extending.html) as the space object storing the counters data. To retrieve the counter existing value a [Task](./task-execution-over-the-space.html) is used. To launch the example run the `CounterTest` unit test.

{{%tabs%}}
{{%tab " CounterTest.java"%}}

```java
package org.openspaces;
import org.junit.Before;
import org.junit.Test;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.EmbeddedSpaceConfigurer;
import static org.junit.Assert.*;

public class CounterTest {

	static GigaSpace space = null;
	static GigaSpace spaceEmb = null;
	static Counter counter = null;

	@Before
	public void setUp() throws Exception {
		if (space == null)
		{
			spaceEmb= new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer("mySpace")).gigaSpace();
			space = new GigaSpaceConfigurer(new SpaceProxyConfigurer("mySpace")).gigaSpace();
			space.clear(null);
			counter = new Counter(space, "id", "counter1", 10);
		}
	}

	@Test
	public void test1() throws Exception
	{
		counter.increment("counter1", 1);
		int counterValue = counter.get("counter1");
		System.out.println("test increment - Counter Value:" + counterValue + " - should be: 11");
		assertEquals(counterValue , 11);
	}

	@Test
	public void test2() throws Exception
	{
		counter.decrement("counter1", 1);
		int counterValue = counter.get("counter1");
		System.out.println("test decrement - Counter Value:" + counterValue + " - should be: 10");
		assertEquals(counterValue , 10);
	}


	@Test
	public void test3() throws Exception
	{
		counter.unset("counter1");
		Integer counterValue = counter.get("counter1");
		System.out.println("test unset - Counter Value:" + counterValue + " - should be: null");
		assertEquals(counterValue , null);
	}
}
```
{{%/tab%}}

{{%tab " Counter.java"%}}


```java
package org.openspaces;

import org.openspaces.core.GigaSpace;

import com.gigaspaces.async.AsyncFuture;
import com.gigaspaces.client.ChangeSet;
import com.gigaspaces.query.IdQuery;

public class Counter {

	GigaSpace space = null;
	String id = null;

	public Counter(GigaSpace space, String id, String name, int initialValue) {
		this.space = space;
		this.id = id;
		CounterData.registerType(space);
		CounterData counterData = new CounterData();
		counterData.setProperty(name, initialValue);
		counterData.setProperty("id", id);
		space.write(counterData);
	}

	public Integer get(String name) throws Exception {
		return ChangeExtension.addAndGet(space, getSpaceDocumentIdQuery(id),
				name, 1);
	}

	public void increment(String name, int value) {
		space.change(getSpaceDocumentIdQuery(id),
				new ChangeSet().increment(name, value));
	}

	public void decrement(String name, int value) {
		space.change(getSpaceDocumentIdQuery(id),
				new ChangeSet().decrement(name, value));
	}

	public void unset(String name) {
		space.change(getSpaceDocumentIdQuery(id), new ChangeSet().unset(name));
	}

	private IdQuery<SpaceDocument> getSpaceDocumentIdQuery(String id) {
		return new IdQuery<SpaceDocument>(CounterData.TYPE_NAME, id);
	}
}
```
{{%/tab%}}

{{%tab " CounterData.java"%}}

```java
package org.openspaces;

import org.openspaces.core.GigaSpace;

import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.metadata.SpaceTypeDescriptor;
import com.gigaspaces.metadata.SpaceTypeDescriptorBuilder;
import com.gigaspaces.metadata.index.SpaceIndexType;

public class CounterData extends SpaceDocument{
	   public static final String TYPE_NAME = "CounterData";

	   public CounterData() {
	        super(TYPE_NAME);
	    }

	   static public void registerType(GigaSpace gigaspace) {
		    // Create type descriptor:
		    SpaceTypeDescriptor typeDescriptor =
		        new SpaceTypeDescriptorBuilder(TYPE_NAME)
		        // ... Other type settings
		        .documentWrapperClass(CounterData.class)
		        .addFixedProperty("id", String.class)
		        .idProperty("id" ,false , SpaceIndexType.BASIC)
		        .create();
		    // Register type:
		    gigaspace.getTypeManager().registerTypeDescriptor(typeDescriptor);
		}
}
```
{{%/tab%}}


{{%/tabs%}}
