---
type: post
title:  Database Integration
categories: PRODUCT_OVERVIEW
parent: the-in-memory-data-grid.html
weight: 300
menu: product
---

{{%  ssummary %}} {{%  /ssummary %}}



One of the first concerns people have when encountering space-based architecture is that the role of the database seems to be diminished. That makes sense. In SBA, the role of the database is different than in typical tier-based architecture - where the database is considered the only system of record and is more important than all of the other moving parts.

The location of the data should be considered, based on its life-span. For example, transaction state, which is only valid for a short time, should not be stored in a database to achieve fault-tolerance. In SBA, this type of information is stored in memory, and the space provides fault-tolerance capabilities.

In space-based architectures, databases should be used to store information that has a much longer life cycle, i.e., data that doesn't change rapidly. As a general rule, information that is accessed and changed often is stored in the space - and thus, in memory - and data that's become static in some way, is stored in a traditional relational database, where reporting engines and other tabular access mechanisms can access it.

A database has an additional role in SBA, when there is a need to extend memory to physical storage.

SBA does not deny the use of a database, but suggests a more natural role for it. For this reason, integration with the database is core to GigaSpaces XAP.

# Types of Database Integration

### Reliable Asynchronous Persistence

In a high performance transactional system, we would like the transaction to be bound to space resources only, and the update of the backing database be done after the transaction is complete, asynchronous to the transaction. The synchronization between the in-memory system of record and the database, should be reliable as well.

{{% refer%}}
GigaSpaces XAP provides a Mirror Service as a means to achieve [reliable asynchronous persistency]({{% latestjavaurl%}}/asynchronous-persistency-with-the-mirror.html).
{{% /refer%}}

The typical cluster topology is of reliable partitioning (partitions with backups) connected to the mirror service, which persists to the database.

The mirroring service is one-way only - from the space cluster to the database or other data source.

### Data Source Integration

GigaSpaces XAP provides two extension points (`SpaceDataSource`/`SpaceSynchronizationEndpoint` to data source integration. GigaSpaces provides a built-in [Hibernate]({{% latestjavaurl%}}/hibernate-space-persistency.html) implementation of these extensions. For implementation details please refer to [Space Persistency APIs Implementation ]({{% latestjavaurl%}}/space-persistency.html) in the Programmer's Guide.

These extension points are used by the space to store and retrieve data from data sources (e.g. databases).

{{%  tip "About Memory Volatility" %}}
One of the common questions when it comes to memory as a critical system of record, is its volatility. There is no doubt that in-memory storage is much faster than disk storage, however, how can we guarantee information completeness if the data is stored in memory?
In order to preserve data, it is common topology to make sure that each memory copy has a replica. In addition it is important to put both replicas on different hardware, in order to eliminate single points of failures. If for some reason a single copy is not enough, we create as many copies as we need.
{{%  /tip %}}



# Persisting Space Data into Permanent Storage

There are many situations where space data needs to be persisted to permanent storage and retrieved from it. For example:

- A client process works primarily with the memory space for temporary storage of process data structures, and the permanent storage is used to extend or back up the physical memory of the process running the space. In case the data in the space becomes unavailable due to cache eviction, for example, the backup data in permanent storage can be accessed.
- A client process works primarily with the database storage and the space is used to make read processing more efficient. Since database access is expensive, the data read from the database is cached in the space, where it is available for subsequently fast read operations. (This is using the space as a side cache.)
- When a space is restarted, data from its persistent media files can be loaded into the space to speed up incoming query processing.

# Bridging the Gap Between Object to Relational

Object-oriented development dominates the enterprise, and most client applications today are written in the Java, C#, and C\+\+ languages. However, the majority of business-critical data is stored in relational database management systems (RDBMS) or similar systems that use record-based (non object-oriented) storage, whose data is read by query-based search schemes.

Because of this mismatch, an intermediate object-relational mapping (ORM) step is required to perform translation of objects to records when writing data to a database, and translation of records to objects when reading data from a database. This intermediate step is implemented in middleware that is detached from and transparent to the client application. Client calls to standard API read and write methods trigger the middleware functionality without a need for the client to intervene. Advanced middleware systems permit the client API to formulate and pass a database query for use when reading from the database.




The Hibernate library, an ORM persistence and query service for the Java language, can provide this service for RDBMS. Hibernate allows you to express queries in its own portable SQL extension (HQL), as well as in native SQL. However, Hibernate is restricted to run at the client level, and does not relate to read/write-through caching.




The [Moving from Hibernate to Space](/sbp/moving-from-hibernate-to-space.html) best practice includes step by step instructions for moving from Hibernate based application to GigaSpaces Data-Grid as the data access layer. This use Hibernate as the space persistency layer using write-through approach when pushing updates into the database.

{{%  refer %}}
The space can be used as a [Hibernate second level cache](/sbp/gigaspaces-for-hibernate-orm-users.html).
{{%  /refer %}}

# Caching policies

[Space Persistency]({{% latestjavaurl%}}/space-persistency.html) supports the **All In Cache** and **LRU** [Cache policies]({{% latestadmurl%}}/memory-management-facilities.html).

## All In Cache Policy

With the [All In Cache policy]({{% latestadmurl%}}/all-in-cache-cache-policy.html), the assumption is the Space holds the entire data in memory. In this case, the space communicated with the data source at startup, and loads all the data. If data within the space is updated/added/removed, the space is calling the [SpaceSynchronizationEndpoint]({{% latestjavaurl%}}/space-synchronization-endpoint-api.html) implementation to update the underlying data source. All data activities leveraging the data in memory.



## LRU Cache Policy - Read-Ahead

LRU persistency model is based on the eviction model: **Some** of the data stored In-Memory (based on auto expiration mechanism or explicit data eviction) and **ALL** the data stored on disk where the preferred disk media is a database. You may leverage Hibernate as the mapping layer when data is persist or have a custom persistency mapping implemented leveraging the [Space Data Source API]({{% latestjavaurl%}}/space-data-source-api.html).

{{%  tip %}}
GigaSpaces do not support the overflow model when persisting data since it may lead to inconsistency situations.
{{%  /tip %}}

.
Using a database to store the data allows you to:

- Reload it very fast into the space with plenty of flexibility to customize the load activity.
- Allows the system to query the database when needed.

Database technology has proven itself to be able to store vast amount of data very efficiently with very good high-availability. You may use [RDBMS](http://en.wikipedia.org/wiki/RDBMS) SQL databases (mySQL, Oracle, Sybase, DB2) or [NoSQL](http://en.wikipedia.org/wiki/NoSQL) databases (MongoDB , MarkLogic, AllegroGraph) as the space persistency layer.

{{%  refer %}}
When using NoSQL databases you may also leverage GigaSpaces [Document API]({{% latestjavaurl%}}/document-api.html) support to map complex data structure into a document data store model.
{{%  /refer %}}

With the [LRU policy]({{% latestadmurl%}}/lru-cache-policy.html), the assumption is that some of the data (recently used) is stored in memory. The amount of data stored in memory is limited by the **cache size** parameter, the memory usage watermark threshold parameters and available free GSC JVM heap size. In this case, once the space is started is loads data up 50% (you may tune this value) of the defined cache max size (total of objects per partition).

If data within the space is updated/added/removed, the space is calling the [SpaceSynchronizationEndpoint]({{% latestjavaurl%}}/space-synchronization-endpoint-api.html) implementation to update the underlying data source. When performing read operations for a single object (read/readById/readIfExists) and no matching object is found in-memory (cache miss), the [SpaceDataSource]({{% latestjavaurl%}}/space-data-source-api.html) implementation is called to search for a matching data to be loaded back into the space and from there sent to the client application (read-ahead). If a query is executed (readMultiple), and the max objects to read exceed beyond the amount of matching objects in memory, the `SpaceDataSource` is called to search for matching data elements to be loaded back into the space and from there sent to the client application. In this case, the client might have in return objects that were originally within the space, and objects that have been read from the data source and loaded into the space as a result of the query operation.

{{%  refer %}}
The [IMDG with Large Backend Database Support](/sbp/imdg-with-large-backend-database-support.html) best practice suggest a simple approach you may use to leverage LRU Space with a large database allowing the application to **execute queries** against the space in an optimal manner.
{{%  /refer %}}

In both cases (ALL_IN_CACHE and LRU cache policy), you can [customize the data load phase]({{% latestjavaurl%}}/space-persistency-initial-load.html) to speed up the space initialization phase.

# Space Persistency

The space can load data from data sources, store data into data sources, and persist data into a relational data source or any other media via a custom [SpaceSynchronizationEndpoint]({{% latestjavaurl%}}/space-synchronization-endpoint-api.html) implementation. [Space Persistency]({{% latestjavaurl%}}/space-persistency.html) a built-in implementation using [Hibernate]({{% latestjavaurl%}}/hibernate-space-persistency.html), to store data in an existing data source and in the space. Data is loaded from the data source during space initialization (via the `SpaceDataSource` implementation), and from then onwards the application works with the space directly. Meanwhile, the data source is constantly updated with all the changes made in the space (via the `SpaceSynchronizationEndpoint` implementation). This is the recommended model.

The [Hibernate Space Persistency]({{% latestjavaurl%}}/hibernate-space-persistency.html) support RDBMS. The [Cassandra Space Persistency]({{% latestjavaurl%}}/cassandra-space-persistency.html) allows applications to leverage NoSQL Cassandra DB having a distributed database infrastructure as an alternative to RDBMS.

