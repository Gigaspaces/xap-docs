---
type: post120
title:  Overview
categories: XAP120NET, PRM
parent: space-persistency-overview.html
weight: 50
---

{{% ssummary  %}}  {{% /ssummary %}}

The Space Persistency is provided via a component called **External Data Source**. This component provides persistency capabilities for the space architecture to interact with a persistency layer:

- Pre-Loading data from the persistency layer and lazy load data from the persistency (available when the space is running in LRU mode).
- Delegating changes within the space to the persistency layer.

GigaSpaces Space Persistency provides the `AbstractExternalDataSource` class which can be extended and then used to load data and store data into an existing data source. Data is [loaded from the data source](./space-persistency-initial-load.html) during space initialization, and from then onwards the application works with the space directly.

{{% align center %}}
![data-grid-initial-loadNew.jpg](/attachment_files/data-grid-initial-loadNew.jpg)
{{% /align %}}


Persistency can be configured to run in Synchronous or Asynchronous mode:

- Synchronous Mode - see [Direct Persistency](./direct-persistency.html)

{{% align center %}}
![data-grid-sync-persist.jpg](/attachment_files/data-grid-sync-persist.jpg)
{{% /align %}}

- Asynchronous Mode - see  [Asynchronous Persistency with the Mirror](./asynchronous-persistency-with-the-mirror.html)

{{% align center %}}
![data-grid-async-persist.jpg](/attachment_files/data-grid-async-persist.jpg)
{{% /align %}}

{{% info %}}
The difference between the Synchronous or Asynchronous persistency mode concerns how data is persisted back to the database. The Synchronous mode data is persisted immediately once the operation is conducted where the client application wait for the `ExternalDataSource` to confirm the write. With the Asynchronous mode (mirror Service), data is persisted in a **reliable** asynchronous manner using the mirror Service as a write behind activity. This mode provides maximum performance.
{{%/info%}}

# Space Persistency API

The Space Persistency API contains an abstract class one should extend in order to customize the space persistency functionality. The ability to customize the space persistency functionality allows GigaSpaces to interact with any external application or data source.


| Client Call | External Data Source Call| Cache Policy Mode|EDS Usage Mode|
|:------------|:-----------------------------------------------|:-----------------|:-------------|
|Write, Change, Take, WriteMultiple, TakeMultiple, Clear|ExecuteBulk |ALL_IN_CACHE, LRU|read-write|
|Transaction Commit|ExecuteBulk|ALL_IN_CACHE, LRU|read-write|
|Read, ReadMultiple ,ReadById, ReadByIds, Count|GetEnumerator|LRU|read-write,read-only|
|TakeMultiple|GetEnumerator|ALL_IN_CACHE, LRU|read-write|

For detailed API information see [External Data Source API](./hibernate-space-persistency.html).

GigaSpaces comes with a built-in implementation of `AbstractExternalDataSource` called [NHibernate Space Persistency](./hibernate-space-persistency.html).

See [Space Persistency Initial Load](./space-persistency-initial-load.html) to allow the space to pre-load its data.
