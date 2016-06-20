---
type: post120
title:  Counters
categories: XAP120NET
weight: 500
parent: the-gigaspace-interface-overview.html
---



A growing number of applications such as real time ad impressions , ad optimization engines, social network , on-line gaming , need real-time counters when processing incoming streaming of events. The challenge is to update the counter in atomic manner without introducing a bottleneck event processing flow.


XAP introducing Counter functionality via the `ISpaceProxy.Change` API. It allows you to increment or decrement an Numerical field within your Space object (PONO or Document). This change may operate on a numeric property only (byte,short,int,long,float,double) or their corresponding Boxed variation. To maintain a counter you should use the Change operation with the `ChangeSet` increment/decrement method that adds/subtract the provided numeric value to the existing counter.


{{%imagertext "/attachment_files/change-api-counter.jpg" %}}
There is no need to use a transaction when getting the counter value as the counter is atomic.
If the counter property does not exists, the delta will be set as its initial state. This simple API allows you to maintain counters with minimal impact on the system performance as it is replicating only the `ChangeSet` command and not the entire space object to the backup copy when running a clustered data-grid.

{{% /imagertext %}}

# Incrementing

Incrementing a Counter done using the `ChangeSet().Increment` call:


```csharp
ISpaceProxy spaceProxy = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(id);
spaceProxy.Change(idQuery, new ChangeSet().Increment("MyCounter", 1));
```

# Decrementing

Decrementing a Counter done using the `ChangeSet().Decrement` call:


```csharp
ISpaceProxy spaceProxy = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(id);
spaceProxy.Change(idQuery, new ChangeSet().Decrement("MyCounter", 1));
```

# Clearing

Clearing the Counter value done using the `ChangeSet().Unset` call:


```csharp
ISpaceProxy spaceProxy = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(id);
spaceProxy.Change(idQuery, new ChangeSet().Unset("MyCounter"));
```

# Getting the value

Getting the Counter value done via the read call:


```csharp
ISpaceProxy spaceProxy = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>( id);
WordCount wordount = spaceProxy.ReadById<WordCount>(idQuery);
int counterValue = wordount.MyCounter;
```

Another way getting the Counter value without reading the space object back to the client would be via a [Task](./task-execution-over-the-space.html):


```csharp
public class GetCounterTask : ISpaceTask<int>
{
    String id ;

	public GetCounterTask (string id) {
		this.id= id;
	}

	public int Execute(ISpaceProxy spaceProxy, ITransaction tx)
	{
		IdQuery<WordCount> idQuery = new IdQuery<WordCount>(id);
		WordCount wordcount = spaceProxy.ReadById<WordCount>(idQuery);
		return wordcount.MyCounter;
	}
}
```

Call the execute method to fetch the current Counter value:


```csharp
GetCounterTask task = new GetCounterTask ("1");
//Execute the task on all the primary nodes with in the cluster
int result = spaceProxy.Execute(task,0);
```

# Pre-Loading

When pre-loading the space via the [External Data Source initial-load](./space-persistency-initial-load.html) you may need to construct Counters data. The `initialLoad` method allows you to implement the logic to generate the counter data and load it into the space after the actual data been loaded from the external data source (database).

# Example

With the following example the `Counter` class wraps the `GigaSpace.change` operation providing simple `increment`,`decrement`,`get` and `unset` methods to manage counters. The example using an [extended SpaceDocument](./document-extending.html) as the space object storing the counters data. To retrieve the counter existing value a [Task](./task-execution-over-the-space.html) is used. To launch the example run the `CounterTest` unit test.

{{%tabs%}}
{{%tab " CounterTest "%}}


```csharp
public class CounterTest
{
	static ISpaceProxy space = null;
	static ISpaceProxy spaceEmb = null;
	static Counter counter = null;

	public void setUp ()
	{
		if (space == null) {
			spaceEmb = new EmbeddedSpaceFactory("mySpace").Create();
			space = new SpaceProxyFactory("mySpace").Create();
			space.Clear (null);
			counter = new Counter (space, "id", "counter1", 10);
		}
	}

	public void test1 ()
	{
		counter.increment ("counter1", 1);
		int? counterValue = counter.get ("counter1");
		Console.WriteLine ("test increment - Counter Value:" + counterValue + " - should be: 11");
	}

	public void test2 ()
	{
		counter.decrement ("counter1", 1);
		int? counterValue = counter.get ("counter1");
		Console.WriteLine ("test decrement - Counter Value:" + counterValue + " - should be: 10");
	}

	public void test3 ()
	{
		counter.unset ("counter1");
		int? counterValue = counter.get ("counter1");
		Console.WriteLine ("test unset - Counter Value:" + counterValue + " - should be: null");
	}
}
```
{{%/tab%}}

{{%tab " Counter"%}}

```csharp
public class Counter
{
	ISpaceProxy spaceProxy = null;
	String id = null;

	public Counter (ISpaceProxy spaceProxy, String id, String name, int initialValue)
	{
		this.spaceProxy = spaceProxy;
		this.id = id;
		CounterData.registerType (spaceProxy);
		CounterData counterData = new CounterData ();
		counterData.Properties.Add (name, initialValue);
		counterData.Properties.Add ("id", id);
		spaceProxy.Write (counterData);
	}

	public int? get (String name)
	{
		CounterTask task = new CounterTask (id, name);
		int? res = spaceProxy.Execute (task, id);
		return res;
	}

	public void increment (String name, int value)
	{
		IdQuery<CounterData> query = new IdQuery<CounterData> (CounterData.TYPE_NAME, id, id);
		spaceProxy.Change (query, new ChangeSet ().Increment (name, value));
	}

	public void decrement (String name, int value)
	{
		IdQuery<CounterData> query = new IdQuery<CounterData> (CounterData.TYPE_NAME, id, id);
		spaceProxy.Change (query, new ChangeSet ().Decrement (name, value));
	}

	public void unset (String name)
	{
		IdQuery<CounterData> query = new IdQuery<CounterData> (CounterData.TYPE_NAME, id, id);
		spaceProxy.Change (query, new ChangeSet ().Unset (name));
	}
}
```
{{%/tab%}}

{{%tab " CounterData"%}}

```csharp
public class CounterData : SpaceDocument
{
    public readonly static String TYPE_NAME = "CounterData";

	public CounterData(): base("CounterData")
	{
	}

	static public void registerType(ISpaceProxy spaceProxy)
	{
	    SpaceTypeDescriptorBuilder typeBuilder = new SpaceTypeDescriptorBuilder ("CounterData");
	    typeBuilder.SetIdProperty ("id", false, SpaceIndexType.Extended);
		typeBuilder.AddFixedProperty ("id",typeof(CounterData));
		ISpaceTypeDescriptor typeDescriptor = typeBuilder.Create ();
		// Register type descriptor:
		spaceProxy.TypeManager.RegisterTypeDescriptor (typeDescriptor);
	}
}
```
{{%/tab%}}

{{%tab " CounterTask"%}}

```csharp
public class CounterTask : ISpaceTask<int?> {

    String id ;
	String name ;

	public CounterTask(String id , String name ) {
		this.id= id;
		this.name = name;
	}

	public int? Execute (ISpaceProxy spaceProxy, ITransaction tx)
	{
		Object value = null;

		IdQuery<CounterData> query= new IdQuery<CounterData> (CounterData.TYPE_NAME , id , id);
		CounterData counterData = spaceProxy.ReadById(query);
		counterData.Properties.TryGetValue(name, out value);

		return (int?)value;
	}
}
```
{{%/tab%}}
{{%/tabs%}}

