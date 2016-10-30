---
type: post121
title:  Advanced Topics
categories: XAP121NET, PRM
parent: space-persistency-overview.html
weight: 800
---

{{% ssummary %}} {{% /ssummary %}}

This sections covers advanced options related to Space Persistency.

# Properties

Here are the Space Persistency Properties:


| Property | Description | Default |
|:---------|:------------|:--------|
|space-config.external-data-source.shared-iterator.enabled| This attribute enables shared iterator mode which tries to optimize data source access by sharing the same iterator for the same query operations when possible. |true|
|<nobr>space-config.external-data-source.shared-iterator.time-to-live</nobr>| This attribute specify for how long in milliseconds an iterator can be shared in shared iterator mode. If two equivalent queries are done concurrently, but the time that elapsed between the first query and the second query exceeds the time to live, the second query will open a new iterator on the data source and will not share the first one.|10000|
|cluster-config.cache-loader.external-data-source| Provides cluster-wide support. |true|
|cluster-config.cache-loader.central-data-source| Provides clustered database wide support. |true|

{{% anchor TR %}}

# Troubleshooting

To enable Space Persistency logging, edit the `<GigaSpaces Root>\config\gs_logging.properties` file, and set the persistent level to `CONFIG` or `FINER`.

- `CONFIG` messages are intended to provide a variety of static configuration information, and to assist in debugging problems that may be associated with particular configurations.


```bash
com.gigaspaces.persistent.level = CONFIG
```

- `FINER` messages log calls for entering, returning, or throwing an exception to and from the cache interface implementations.


```bash
com.gigaspaces.persistent.level = FINER
```

{{% anchor ISL %}}

# Initial Space Load

When the space is started, restarted, or cleaned, the system can initially fill the space with space objects that are likely to be required by the application. You can specify the data to loaded using the `ExternalDataSource.initialLoad` method that is called once the space is started. See the [Space Persistency Initial Load](./space-persistency-initial-load.html) for details. The space is not available for clients, until the data load process has been completed.

{{% tip %}}
The Initial Load is supported with the `partitioned` cluster schema. If you would like to pre-load a clustered space using the Initial-Load without running backups you can use the `partitioned` and have ZERO as the amount of backups.
{{% /tip %}}

# Refreshing Space Object when using an external data source

In order to refresh a space object, you should remove it from the space using the `Take`, `TakeById` , `Write` with short lease or `Clear` operations and read it back using the `Read` or `ReadById` operations. This in turn will load the latest version of the object from the data source (i.e. database) via the `ExternalDataSource` implementation back into the space. When having such a scenario you should be aware of the **recent deletes mechanism**.

The recent deletes mechanism running within the space ensures that there will not be a race condition, if an object is removed from the space and loaded by two or more different threads at the same time. This mechanism **prevents the `ExternalDataSource` implementation from loading an object that has been removed from the space and after a short time been loaded again (with the same ID) to happen**.

In order to allow an object that has been removed to be loaded again into the space you should use one the following options:

- Use the `ExternalDataSource` extension in read-only mode. Using the read-write mode turns on the recent deletes mechanism.
- Perform the read operation several times (with short sleeps in between) until the object will be loaded into the space (you will not get a null back).

# Eliminating Resonance Affect when Using Mirror Service

When using the [Mirror Service](./asynchronous-persistency-with-the-mirror.html), and the `ExternalDatasource` is enabled for the space, all data loaded into the space using `InitialLoad` while it is being started, **is not replicated back to the Mirror Service**.

# Count Operation

The scope of `ISpaceProxy.Count()` is the data stored within the space. These methods do not take into account the data stored within the underlying data source.

# Recursive Calls

The `ExternalDataSource` implementation should avoid performing space operations to prevent deadlocks and recursive behavior.

{{% anchor UIDG %}}

# UID Generation

The space embeds a unique identifier into each space Object. This ID is used implicitly when performing update operations, and read/take operations based on ID.

If you write an object into the space, or load an object that has a ID that already exists within the space, the operation will be rejected with the exception: `EntryAlreadyInSpaceException`. When specifying the `SpaceId` field make sure the `auto-generate` attribute should be set to `false`.

{{% anchor HibernateID %}}

# Hibernate ID Generation

Hibernate supports multiple ID generators as detailed in [Hibernate documentation](http://docs.jboss.org/hibernate/core/3.3/reference/en/html/mapping.html#mapping-declaration-id). Your hibernate mapping file should use algorithm that is appropriate for your use case.

Some generators increase the number of database operations and result into overall adverse performance. You need to watch out for generators + database combinations that automatically disable the batch insert mode transparently as mentioned [here](http://docs.jboss.org/hibernate/core/3.3/reference/en/html/batch.html).

_"Hibernate disables insert batching at the JDBC level transparently if you use an identity identifier generator."_

{{% tip %}}
Using a sequence number increases the database reads on some databases, because Hibernate reads the next sequence number before each new `INSERT` in the batch. This also disables batch persistence used by GigaSpaces HibernateExternalDataSource.

A better strategy would be to use a dummy generator like "increment" in hibernate mapping file, on the database side define a INSERT trigger on this table to generate a new id using a sequence. You will see orders of magnitude performance improvement in the database operations making this simple change.
{{% /tip %}}

{{% anchor limits %}}

# Considerations

- When a space is configured to be persistent, and a `PONO` is used as the Space Domain class, it must use the `SpaceId(autogenerate=false)` decoration.
- When running in LRU Cache policy the `ISpaceProxy.Count()` operation using the data within the space only and does not access the space data source (database) to return the object count.
- The `ExternalDataSource.InitialLoad()` loads data into partitioned spaces, by reading all the data into the space and filtering it at the space side. To tune this behavior, you should execute the relevant query from the database on the partition ID, to fetch the relevant result set to load into the space. See the [Space Persistency Initial Load](./space-persistency-initial-load.html) for more details.
- [Hibernate Lazy load](http://www.hibernate.org/162.html) is not supported when using the `HibernateExternalDataSource` implementation. See [Space Object Modeling](/sbp/space-object-modeling.html) for more details.
- When running in `ALL_IN_CACHE` cache policy mode, optimistic locking is supported - i.e. updates in optimistic locking mode will be rejected, in case the client performs an update with the non-latest version of the entry. The loaded object from the database should include the latest version or the value 1.
- When running a local cache, the client cache will be updated using an optimistic locking mode - i.e. updates will include the correct version of the entry.
- Optimistic locking is not supported when running in `LRU` cache policy mode, in case the loaded object does not include data within the `SpaceVersion` field.
- When running in LRU Cache policy the `engine.initial_load` property should be configured with a small number, to avoid memory shortage in persistent spaces with large data.
- Optimal number of connection to database would be number of partitions in the cluster.
