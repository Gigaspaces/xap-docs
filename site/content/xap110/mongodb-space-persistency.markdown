---
type: post110
title: Mongo Persistency
categories: XAP110
parent: mongodb.html
weight: 100
---




XAP comes with built in implementations of [Space Data Source](./space-data-source-api.html) and [Space Synchronization Endpoint](./space-synchronization-endpoint-api.html)
 for MongoDB, called `MongoSpaceDataSource` and `MongoSpaceSynchronizationEndpoint`, respectively.

{{<wbr>}}

{{% align center %}}
![mongodbPersistence.jpg](/attachment_files/mongodbPersistence.jpg)
{{% /align%}}


For further details about the persistency APIs used see [Space Persistency](./space-persistency.html).


{{%fpanel%}}

- [Data Source](./mongodb-space-data-source.html){{<wbr>}}
The Space Data Source API is used for reading data and meta data from the MongoDB.

- [Synchronization Endpoint](./mongodb-space-synchronization-endpoint.html){{<wbr>}}
The space synchronization endpoint API is used for synchronizing data from the space to MongoDB data base.
{{%/fpanel%}}
