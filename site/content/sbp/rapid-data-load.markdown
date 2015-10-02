---
type: postsbp
title:  Rapid Data Load
categories: SBP
parent: data-access-patterns.html
weight: 1400
---



{{% tip %}}
**Summary:**  This article illustrates the usage of Task Executors to rapidly load data into the In-Memory-Data-Grid <br/>
**Author**: Shay Hassidim, Deputy CTO, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 7.0<br/>
**Last Update:** July 2009<br/>


{{% /tip %}}

# Overview
In some cases you might need to load data into the data grid in a very fast manner. This is mostly needed as part of your development phase or unit tests. You might not have a fast database on hand to load data, from using the [HibernateExternalDataSource]({{%latestjavaurl%}}/space-persistency-initial-load.html) implementation, or it might be easier for you to create a data generator utility that simulates the real life data your application needs. A simple technique to load data very rapidly into the data grid, is to use a [DistributedTask]({{%latestjavaurl%}}/executor-based-remoting.html) implementation that generates the data and writes it into the collocated space. The generated data is constructed in such a way that its routing field value "matches" the collocated space.

![rapid_data_load.jpg](/attachment_files/sbp/rapid_data_load.jpg)
The Distributed Task is executed for each Space class you have, allowing you to load data in a parallel manner across all partitions (this is in fact 2 dimensional parallel data load).

# Example
Here is a simple example: we have 3 types of Space Classes:
`StockMktHist`, `LastPrice`, `StockHist` - all of these use the `Currency` field as their routing field. The Currency routing field is a string-based field, where its values could be USD,EUR,GBP, etc.

## The createCurrencyGroups
A Data generator utility class has a `createCurrencyGroups()` method that generates a Hash Map that groups currencies that belong to the same partition using the Currency String hashcode - here is a simple implementation of such a method:


```java
static String currencies[] = { "AFN","ALL","AMD","ANG","AOA","ARS","AUD","AWG","AZN","BAM","BBD",
"BDT","BGN","BHD","BIF","BMD","BND","BOB","BRL","BSD","BTN",
	"BWP","BYR","BZD","CAD","CVE","DZD","EUR","GBP","INR","KHR",
	"KYD","MMK","NOK","USD","XAF","XCD","XOF" };

public static void createCurrencyGroups(int maxPartitions) {
	if (currencyGroups != null )
		return;
	currencyGroups = new ConcurrentHashMap<Integer,List <String> >();

	for (String  currency: currencies) {
		int group = currency.hashCode() % maxPartitions;
		if (!currencyGroups.containsKey(group))
		{
			currencyGroups.put(group , new ArrayList<String>());
		}
		currencyGroups.get(group).add(currency);
	}
}
```

With the above implementation, we generate several lists of currencies, all of these are maintained within `currencyGroups` - one for each partition.

## The getRandomCurrency
The Data generator also has the `getRandomCurrency` method that returns a random currency, based on a given partition - it uses the currencyGroups we created above:


```java
public static String getRandomCurrency(int partition) {

	// for a Single space
	if (partition ==0)
		return currency[random.nextInt(currency.length)];

	List<String> list = currencyGroups.get(partition-1);
	return list.get (random.nextInt(list.size()));
}
```

The getRandomCurrency is used with our data generator utility.

## The LoaderRequest
The LoaderRequest execute method implementation generates an array of the requested type and writes it using one method call (writeMultiple) into its collocated space:


```java
public class LoaderRequest implements DistributedTask<String, String>{

	public static final int RequestTypeLastPrice =1;
	public static final int RequestTypeStockHist=2;
	public static final int RequestTypeStockMktHist=3;

	@TaskGigaSpace
	transient GigaSpace gigaspace;

	transient static int batchSize = 5000;

	transient int partition ;
	transient int maxPartitions ;

	int amount;
	int requestType;

	public LoaderRequest (int amount ,  int requestType)
	{
		this.amount=amount;
		this.requestType=requestType;
	}

	@Override
	public String execute() throws Exception {
		System.out.println(System.currentTimeMillis() + " " +
                Thread.currentThread().getName() + "-------> Loading Data into Partition:"
                +gigaspace.getSpace().getURL().getMemberName() + " - requestType:"+ requestType);

		// for a single space
		if (gigaspace.getSpace().getURL().getProperty(SpaceURL.CLUSTER_SCHEMA) == null)
		{
			partition =1;
			maxPartitions =1;
		}
		else
		{
			partition  = Integer.valueOf(gigaspace.getSpace().getURL().getProperty(SpaceURL.CLUSTER_MEMBER_ID)).intValue();
			maxPartitions = Integer.valueOf(gigaspace.getSpace().getURL().getProperty(SpaceURL.CLUSTER_TOTAL_MEMBERS)).intValue();
		}

		DataGenerator.createCurrencyGroups(maxPartitions);

		LastPrice lastPriceBatch[] = null;
		StockHist stockHistBatch[] = null ;
		StockMktHist stockMktHistBatch[]= null;
		switch (requestType) {
		case RequestTypeLastPrice :
		{
			lastPriceBatch = new LastPrice [batchSize ];
			break;
		}
		case RequestTypeStockHist:
		{
			stockHistBatch = new StockHist [batchSize ];
			break;
		}
		case RequestTypeStockMktHist :
		{
			stockMktHistBatch = new StockMktHist [batchSize ];
			break;
		}
		default:
			{
			System.out.println(" ========= Non supported type ========= ");
				break;
			}
		}
		int cycles= amount/batchSize;
		for (int i=0;i<cycles;i++)
		{
			switch (requestType) {
				case RequestTypeLastPrice :
				{
					for (int j=0;j<batchSize;j++)
					{
						lastPriceBatch[j]=DataGenerator.createLastPrice(partition);
					}
					gigaspace.writeMultiple(lastPriceBatch);
					break;
				}
				case RequestTypeStockHist:
				{
					for (int j=0;j<batchSize;j++)
					{
						stockHistBatch[j]=DataGenerator.createStockHist(partition);
					}
					gigaspace.writeMultiple(stockHistBatch);
					break;
				}
				case RequestTypeStockMktHist :
				{
					for (int j=0;j<batchSize;j++)
					{
						stockMktHistBatch[j] = DataGenerator.createStockMktHist(partition);
					}
					gigaspace.writeMultiple(stockMktHistBatch);
					break;
				}
			}
		}
		return "OK";
	}

	@Override
	public String reduce(List<AsyncResult<String>> result) throws Exception {
		return "OK";
	}

	int routing;

	@SpaceRouting
	public Integer routing() {
		return routing;
	}

}
```

## The Client Application
The client application creates a `LoaderRequest` object and executes it, one for each space Class, where in reality all these `LoaderRequest` objects are sent in parallel to all running partitions to be executed. This is how you have these 3 types of objects loaded into all partitions simultaneously:


```java
GigaSpace gigaSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer("jini://*/*/space").space()).gigaSpace();
AsyncFuture<String> future1 = gigaSpace.execute(new LoaderRequest(objectsToLoad,LoaderRequest.RequestTypeLastPrice));
AsyncFuture<String> future2 = gigaSpace.execute(new LoaderRequest(objectsToLoad,LoaderRequest.RequestTypeStockHist));
AsyncFuture<String> future3 = gigaSpace.execute(new LoaderRequest(objectsToLoad,LoaderRequest.RequestTypeStockMktHist));
String result1 = future1.get();
String result2 = future2.get();
String result3 = future3.get();
```

