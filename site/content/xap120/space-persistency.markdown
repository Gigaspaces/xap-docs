---
type: post120
title:  Overview
categories: XAP120
parent: space-persistency-overview.html
weight: 50
---

{{% ssummary  %}}  {{% /ssummary %}}

The Space Persistency is made of two components, A space data source & a space synchronization endpoint.
These components provide advanced persistency capabilities for the space architecture to interact with a persistency layer.

The two components mentioned above are in charge of the following activities:

- The Space data source component handles Pre-Loading data from the persistency layer and lazy load data from the persistency (available when the space is running in LRU mode).
- The Space synchronization endpoint component handles changes done within the space delegation to the persistency layer.


XAPs Space Persistency provides the [SpaceDataSource]({{% api-javadoc %}}/com/gigaspaces/datasource/SpaceDataSource.html) and [SpaceSynchronizationEndpoint]({{% api-javadoc %}}/com/gigaspaces/sync/SpaceSynchronizationEndpoint.html) classes which can be extended and then used to load data and store data into an existing data source. Data is [loaded from the data source](./space-persistency-initial-load.html) during space initialization (`SpaceDataSource`), and from then onwards the application works with the space directly.

{{% align center %}}
![data-grid-initial-loadNew.jpg](/attachment_files/data-grid-initial-loadNew.jpg)
{{% /align %}}

Meanwhile, the space persisting the changes made in the space via a `SpaceSynchronizationEndpoint` implementation.

Persistency can be configured to run in Synchronous or Asynchronous mode:

- Synchronous Mode - see [Direct Persistency](./direct-persistency.html)

{{% align center %}}
![data-grid-sync-persistNew.jpg](/attachment_files/data-grid-sync-persistNew.jpg)
{{% /align %}}

- Asynchronous Mode - see  [Asynchronous Persistency with the Mirror](./asynchronous-persistency-with-the-mirror.html)

{{% align center %}}
![data-grid-async-persistNew.jpg](/attachment_files/data-grid-async-persistNew.jpg)
{{% /align %}}

{{% info "Synchronous verses Asynchronous"%}}
The difference between the Synchronous or Asynchronous persistency mode concerns how data is persisted back to the database. The Synchronous mode data is persisted immediately once the operation is conducted where the client application wait for the `SpaceDataSource`/`SpaceSynchronizationEndpoint` to confirm the write. With the Asynchronous mode (mirror Service), data is persisted in a **reliable** asynchronous manner using the mirror service as a write behind activity. This mode provides maximum performance.

If you're migrating from a GigaSpaces version prior to 9.7 please see the [Migrating From External Data Source API](./migrating-from-external-data-source-api.html) page.
{{% /info %}}

# Space Persistency API

The Space Persistency API contains two abstract classes one should extend in order to customize the space persistency functionality.
The ability to customize the space persistency functionality allows GigaSpaces to interact with any external application or data source.



| Client Call | Space Data Source/{{<wbr>}}Synchronization Endpoint Call| Cache Policy Mode|EDS Usage Mode|
|:------------|:-----------------------------------------------|:-----------------|:-------------|
|write , change , take , asyncTake , writeMultiple , takeMultiple , clear|onOperationsBatchSynchronization , afterOperationsBatchSynchronization|ALL_IN_CACHE, LRU|read-write|
|readById|getById|ALL_IN_CACHE, LRU|read-write,read-only|
|readByIds|getDataIteratorByIds|ALL_IN_CACHE, LRU|read-write,read-only|
|read , asyncRead|getDataIterator|LRU|read-write,read-only|
|readMultiple , count|getDataIterator|LRU|read-write,read-only|
|takeMultiple|getDataIterator|ALL_IN_CACHE, LRU|read-write|
|transaction committed|onTransactionSynchronization , afterTransactionSynchronization|ALL_IN_CACHE, LRU|read-write|
|transaction failed|onTransactionConsolidationFailure|ALL_IN_CACHE, LRU|read-write|


{{%refer%}}
XAP's built in Hibernate Persistency implementation is an extension of SpaceDataSource and SpaceSynchronizationEndpoint classes. For detailed API information see [Space Data Source API](./space-data-source-api.html) and [Space Synchronization Endpoint API](./space-synchronization-endpoint-api.html).
{{%/refer%}}

# RDBMS Space Persistency

GigaSpaces comes with a built-in implementation of `SpaceDataSource` and `SpaceSynchronizationEndpoint` called [Hibernate Space Persistency](./hibernate-space-persistency.html). See [Space Persistency Initial Load](./space-persistency-initial-load.html) to allow the space to pre-load its data. You can also use splitter data source `SpaceDataSourceSplitter`  that allows you to split data sources according to entry type.

# NoSQL DB Space Persistency

The [Cassandra Space Persistency Solution](./cassandra-space-persistency.html) allows applications to push the long term data into Cassandra database in an asynchronous manner without impacting the application response time and also load data from the Cassandra database once the GigaSpaces IMDG is started or in a lazy manner once there is a cache miss when reading data from GigaSpaces IMDG.

The GigaSpaces Cassandra Space Peristency Solution leverages the [Cassandra CQL](http://www.datastax.com/docs/0.8/dml/using_cql), [Cassandra JDBC Driver](http://code.google.com/a/apache-extras.org/p/cassandra-jdbc) and the [Cassandra Hector Library](http://hector-client.github.com/hector/build/html/index.html). Every application's write or take operation against the IMDG is delegated into the Mirror service that is using the Cassandra Mirror implementation to push the changes into the Cassandra database.

