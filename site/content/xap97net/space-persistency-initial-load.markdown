---
type: post97
title:  Initial Load
categories: XAP97NET
parent: space-persistency-overview.html
weight: 500
---


{{% ssummary  %}} {{% /ssummary %}}



The XAP Data-Grid includes special interceptor that allow users to pre-load the Data-Grid with data before it is available for clients access. This interceptor called **Initial Load** and has a default implementation that is using the [NHibernate Space Persistency](./hibernate-space-persistency.html) implementation to load data from a database directly into the Data-Grid instances.

![eds_initial_load.jpg](/attachment_files/eds_initial_load.jpg)

To enable the initial load activity a `ExternalDataSource` should be specified.

Here is an example for a space configuration that performs only initial load from the database without writing back any changes into the database (replication to the Mirror service is not enabled with this example):


```csharp
[BasicProcessingUnitComponent]
public class ProcessorComponent : IDisposable
{
	[ContainerInitializing]
	public void Initialize(BasicProcessingUnitContainer container)
	{
		//Create config for the processor
		ProcessorConfig processorConfig = new ProcessorConfig(container.Properties);

		//Create a new space configuration object that is used to start a space
		SpaceConfig spaceConfig = new SpaceConfig();

		//Set a new ExternalDataSource config object
		spaceConfig.ExternalDataSourceConfig = EdsUtils.BuildNHibernateExternalDataSource();

		//Our cluster member should only use the external data source in read only mode
		spaceConfig.ExternalDataSourceConfig.Usage = Usage.ReadOnly;

		//Add custom properties
		spaceConfig.CustomProperties = new Dictionary<string, string>();

		//State the External Data Source is in All-In-Cache mode
		spaceConfig.CustomProperties.Add("space-config.engine.cache_policy", "1");

		//Set a central data base.
		spaceConfig.CustomProperties.Add("cluster-config.cache-loader.external-data-source", "true");
		spaceConfig.CustomProperties.Add("cluster-config.cache-loader.central-data-source", "true");

		// create cluster member space:
		ISpaceProxy spaceProxy = container.CreateSpaceProxy("ProcessingSpace", processorConfig.SpaceUrl, spaceConfig);

		spaceProxy.NoWriteLeaseMode = processorConfig.ProxyNoWriteLease;
		spaceProxy.OptimisticLocking = processorConfig.ProxyOptimisticLocking;
    }
}
```

# Speeding-up Initial-Load

By default each Data-Grid primary partition loading its relevant data from the database and from there replicated to the backup instances.

All irrelevant objects are filtered out during the data load process. You may optimize this activity by instructing each Data-Grid primary instance to a load-specific data set from the database via a custom query you may construct during the initial load phase.

{{% tip %}}
The Initial Load is supported with the `partitioned-sync2backup` cluster schema. If you would like to pre-load a clustered space using the Initial-Load without running backups you can use the `partitioned-sync2backup` and have ZERO as the amount of backups.
{{% /tip %}}




# Custom Initial Load

To implement your own Initial Load when using the NHibernate `ExternalDataSource` you should implement the `InitialLoad` method to construct one or more `NHibernateDataEnumerator`.
See example below:


```csharp
public IDataEnumerator InitialLoad()
{
	int fetchSize = 100;
	int initialLoadThreadPoolSize = 10;
	bool idOrdering = false;

	Query query = new Query(null,"from Employee where age > 30");

	return new NHibernateDataEnumerator(query, _sessionFactory, fetchSize, idOrdering);
}
```



# Controlling the Initial Load

Additional level of customization can be done by loading only the relevant data into each partition.

In this case you should use the `partition ID` and `total number of partitions` parameters to form the correct database query.
The relevant table column mapped to the routing field should have `numeric` type to allow simple calculation of the matching rows that need to be retrieved from the database and loaded into the partition. This means your database query needs to "slice" the correct data set from the database tables based on the `partition ID`.
Here is an example how you can retrieve the `Partition ID` from within the `NHibernateExternalDataSource`.

Modify the `Init()` method in the `NHibernateExternalDataSource` class to retrieve the current partition number and the number of partitions from the properties. Then you will use these numbers in your sql query statement to load the correct data with each cluster member.


```csharp

    int _partitionNumber;
    int _numberOfPartitions;

	public void Init(Dictionary<string, string> properties)
	{
        String partitionNumber;
        String numberOfPartitions;

        properties.TryGetValue("com.gigaspaces.datasource.partition-number", out partitionNumber);
        properties.TryGetValue("com.gigaspaces.datasource.number-of-partitions", out numberOfPartitions);

        _partitionNumber = Convert.ToInt16(partitionNumber);
        _numberOfPartitions = Convert.ToInt16(numberOfPartitions);

        // .....
    }

```

Here is an example how to use this partition information in the query.

```csharp
public IDataEnumerator InitialLoad()
{
	String hquery;
	int fetchSize = 100;
	int initialLoadThreadPoolSize = 10;
	bool idOrdering = false;

	if (_numberOfPartitions > 1) {
		hquery = "FROM  Person WHERE MOD(Department,"
		+ _numberOfPartitions + ") = " + (_partitionNumber - 1);
	} else {
		hquery = "from  Person ";
	}

	Query query = new Query(null,hquery);

	return new NHibernateDataEnumerator(query, sessionFactory, fetchSize, idOrdering);
}
```



{{% note %}}
Make sure the routing field (i.e. `Department`) will be an Integer type.
{{%/note%}}

Since each space partition stores a subset of the data , based on the entry routing field hash code value , you need to load the data from the database in the same manner the client load balance the data when interacting with the different partitions.

The database query using the `MOD`, `Department`, `number of partitions` and the `partition ID` to perform identical activity performed by a space client when performing write/read/take operations with partitioned space to rout the operation into the correct partition.

{{%learn "./routing-in-partitioned-spaces.html"%}}

# Multi-Parallel Initial Load

The `ConcurrentMultiDataEnumerator` can be used for Multi-Parallel load. This will allow multiple threads to load data into each space primary partition. With the example below 10 threads will be used to load data into the space primary partition , each will handle a different `IDataEnumerator`:


```csharp
public IDataEnumerator InitialLoad()
{
    List<IDataEnumerator> enumerators = new List<IDataEnumerator>();

	int fetchSize = 100;
	int initialLoadThreadPoolSize = 10;
	bool idOrdering = false;

	Query query1 = new Query(null,"from Employee where Age > 30");
	enumerators.Add(new NHibernateDataEnumerator(query1, sessionFactory, fetchSize, idOrdering);

    Query query2 = new Query(null,"from Employee where LastName = ‘David’");
    enumerators.Add(new NHibernateDataEnumerator(query2, sessionFactory, fetchSize, idOrdering);

    return new ConcurrentMultiDataEnumerator(enumerators.ToArray(), fetchSize, initialLoadThreadPoolSize);
}
```
