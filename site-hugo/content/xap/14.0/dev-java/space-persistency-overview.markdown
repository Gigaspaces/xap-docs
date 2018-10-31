---
type: post140
title:  Space Persistency
categories: XAP140, OSS
parent: none
weight: 1100
---



Space persistency in XAP is comprised of two components, a Space data source and a Space synchronization endpoint. These components provide advanced persistency capabilities for the Space architecture to interact with a persistency layer, and are in charge of the following activities:

- The Space data source component handles pre-loading data from the persistency layer and lazy load data from the persistency (available when the Space is running in LRU mode).
- The Space synchronization endpoint component handles changes made within the Space delegation to the persistency layer.


XAP's Space persistency provides the [SpaceDataSource]({{% api-javadoc %}}/com/gigaspaces/datasource/SpaceDataSource.html) and [SpaceSynchronizationEndpoint]({{% api-javadoc %}}/com/gigaspaces/sync/SpaceSynchronizationEndpoint.html) classes, which can be extended and then used to load data and store data into an existing data source. Data is [loaded from the data source](./space-persistency-initial-load.html) during space initialization (`SpaceDataSource`), and from then onwards the application works with the Space directly.

{{% align center %}}
![data-grid-initial-loadNew.jpg](/attachment_files/data-grid-initial-loadNew.jpg)
{{% /align %}}

Meanwhile, the Space persists the changes that were made via a `SpaceSynchronizationEndpoint` implementation.

Persistency can be configured to run in Synchronous or Asynchronous mode:

- Synchronous Mode - see [Direct Persistency](./direct-persistency.html)

{{% align center %}}
![data-grid-sync-persistNew.jpg](/attachment_files/data-grid-sync-persistNew.jpg)
{{% /align %}}

- Asynchronous Mode - see  [Asynchronous Persistency with the Mirror](./asynchronous-persistency-with-the-mirror.html)

{{% align center %}}
![data-grid-async-persistNew.jpg](/attachment_files/data-grid-async-persistNew.jpg)
{{% /align %}}

{{% note "Synchronous verses Asynchronous"%}}
The difference between Synchronous and Asynchronous persistency mode is the way data is persisted back to the database. In Synchronous mode, data is persisted immediately after the operation is conducted where the client application waits for the `SpaceDataSource`/`SpaceSynchronizationEndpoint` to confirm the write. In Asynchronous mode (mirror Service), data is persisted in a **reliable** asynchronous manner using the mirror service as a write-behind activity. This mode provides maximum performance.

If you are upgrading from a version of XAP prior to 9.7, refer to the [Migrating From External Data Source API](./migrating-from-external-data-source-api.html) page.
{{% /note %}}

# Space Persistency API

The Space Persistency API contains two abstract classes that should be extended in order to customize the Space persistency functionality.
The ability to customize the Space persistency functionality allows XAP to interact with any external application or data source.



| Client Call | Space Data Source/<br>Synchronization Endpoint Call| Cache Policy Mode|EDS Usage Mode|
|:------------|:-----------------------------------------------|:-----------------|:-------------|
|`write`, `change`, `take`, `asyncTake`, `writeMultiple`, `takeMultiple`, `clear`|`onOperationsBatchSynchronization`, `afterOperationsBatchSynchronization`|ALL_IN_CACHE, LRU|read-write|
|`readById`|`getById`|ALL_IN_CACHE, LRU|read-write,read-only|
|`readByIds`|`getDataIteratorByIds`|ALL_IN_CACHE, LRU|read-write,read-only|
|`read`, `asyncRead`|`getDataIterator`|LRU|read-write,read-only|
|`readMultiple`, `count`|`getDataIterator`|LRU|read-write,read-only|
|`takeMultiple`|`getDataIterator`|ALL_IN_CACHE, LRU|read-write|
|`transaction committed`|`onTransactionSynchronization`, `afterTransactionSynchronization`|ALL_IN_CACHE, LRU|read-write|
|`transaction failed`|`onTransactionConsolidationFailure`|ALL_IN_CACHE, LRU|read-write|


{{%refer%}}
XAP's built in Hibernate Persistency implementation is an extension of the SpaceDataSource and SpaceSynchronizationEndpoint classes. For detailed API information, refer to [Space Data Source API](./space-data-source-api.html) and [Space Synchronization Endpoint API](./space-synchronization-endpoint-api.html).
{{%/refer%}}

# RDBMS Space Persistency

XAP comes with a built-in implementation of `SpaceDataSource` and `SpaceSynchronizationEndpoint` called [Hibernate Space Persistency](./hibernate-space-persistency.html). See [Space Persistency Initial Load](./space-persistency-initial-load.html) to allow the Space to pre-load its data. You can also use splitter data source `SpaceDataSourceSplitter`, which allows you to split data sources according to entry type.

# NoSQL DB Space Persistency

The [Cassandra Space Persistency Solution](./cassandra-space-persistency.html) allows applications to push  long-term data into the Cassandra database in an asynchronous manner, without affecting the application response time, and to also load data from the Cassandra database after the XAP data grid is started or in a lazy manner when there is a cache miss when reading data from the XAP data grid.

The GigaSpaces Cassandra Space Peristency Solution leverages the [Cassandra CQL](http://www.datastax.com/docs/0.8/dml/using_cql), [Cassandra JDBC Driver](http://code.google.com/a/apache-extras.org/p/cassandra-jdbc) and the [Cassandra Hector Library](http://hector-client.github.com/hector/build/html/index.html). Every application's write or take operation against the data grid is delegated into the Mirror service that uses the Cassandra Mirror implementation to push the changes into the Cassandra database.
