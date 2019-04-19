---
type: post110
title:  Cassandra Persistency
categories: XAP110
parent: cassandra.html
weight: 100
canonical: auto
---





XAP comes with built in implementations of [Space Data Source](./space-data-source-api.html) and [Space Synchronization Endpoint](./space-synchronization-endpoint-api.html) for Cassandra, called `CassandraSpaceDataSource` and `CassandraSpaceSynchronizationEndpoint`, respectively.


{{% align center%}}
![CassMirrorNew.jpg](/attachment_files/CassMirrorNew.jpg)
{{% /align%}}

{{<wbr>}}


For further details about the persistency APIs used see [Space Persistency](./space-persistency.html).


{{%fpanel%}}

- [Data Source](./cassandra-space-data-source.html){{<wbr>}}
The Space Data Source API is used for reading data and meta data from the Cassandra.

- [Synchronization Endpoint](./cassandra-space-synchronization-endpoint.html){{<wbr>}}
The space synchronization endpoint API is used for synchronizing data from the space to Cassandra data base.

- [Hector Library](./cassandra-hector-client.html){{<wbr>}}
Hector client library.
{{%/fpanel%}}



