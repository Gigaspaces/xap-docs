---
type: post140
title:  Space Persistency
categories: XAP140NET, PRM
parent: none
weight: 900
---


The Space Persistency is provided via a component called **External Data Source**. This component provides persistency capabilities for the space architecture to interact with a persistency layer:

- Pre-loading data from the persistency layer and lazy load data from the persistency (available when the Space is running in LRU mode).
- Delegating changes within the Space to the persistency layer.

XAP Space Persistency provides the `AbstractExternalDataSource` class, which can be extended and then used to load and store data into an existing data source. Data is [loaded from the data source](./space-persistency-initial-load.html) during Space initialization, and from then onwards the application works directly with the Space.

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

{{% note "Info"%}}
The difference between Synchronous and Asynchronous persistency mode is the way that data is persisted back to the database. In Synchronous mode, data is persisted immediately after the operation is conducted where the client application waits for the `ExternalDataSource` to confirm the write. In Asynchronous mode (mirror Service), data is persisted in a **reliable** asynchronous manner using the mirror Service as a write-behind activity. This mode provides maximum performance.
{{%/note%}}

# Space Persistency API

The Space Persistency API contains an abstract class that should be extended in order to customize the Space persistency functionality. The ability to customize the Space persistency functionality allows XAP to interact with any external application or data source.


| Client Call | External Data Source Call| Cache Policy Mode|EDS Usage Mode|
|:------------|:-----------------------------------------------|:-----------------|:-------------|
|`Write`, `Change`, `Take`, `WriteMultiple`, `TakeMultiple`, `Clear`|`ExecuteBulk` |ALL_IN_CACHE, LRU|read-write|
|`Transaction Commit`|`ExecuteBulk`|ALL_IN_CACHE, LRU|read-write|
|`Read`, `ReadMultiple`, `ReadById`, `ReadByIds`, `Count`|`GetEnumerator`|LRU|read-write,read-only|
|`TakeMultiple`|`GetEnumerator`|ALL_IN_CACHE, LRU|read-write|

For detailed API information, refer to [External Data Source API](./hibernate-space-persistency.html).

XAP comes with a built-in implementation of `AbstractExternalDataSource` called [NHibernate Space Persistency](./hibernate-space-persistency.html).

Refer to [Space Persistency Initial Load](./space-persistency-initial-load.html) for information on how to to allow the Space to pre-load its data.




