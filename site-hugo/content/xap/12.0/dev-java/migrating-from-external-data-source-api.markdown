---
type: post120
title:  Migrating From External Data Source API
categories: XAP120
parent: space-persistency-overview.html
weight: 1000
---

This page describes how EDS implementations prior to XAP version 9.5 should migrate to the new Space Persistency APIs.


External Data Source API contains 5 interfaces the implementation can use:

1. `ManagedDataSource` - base interface which provides the ability to initial load data from the data source upon space initialization.
1. `DataProvider` - reading data from the data source using a template.
1. `SQLDataProvider` - reading data from the data source using SQL queries.
1. `DataPersister` - persisting non transactional data to the data source.
1. `BulkDataPersister` - persisting batched or transactional data to the data source.

These interfaces have been replaced with two new classes which can be extended:

#### SpaceDataSource

- Initial load metadata from the data source.
- Initial load data from the data source.
- Read data from data source upon space request.

#### SpaceSynchronizationEndpoint

- Persist batches of data to the data source.
- Persist transactional data to the data source.
- Persist metadata changes such as dynamic index creation and type introduction to the data source.
- Intercept events such as `Distributed Transaction Consolidation`, successful batch or write synchronization.

{{% tip %}}
Please note that the new two classes `SpaceDataSource` and `SpaceSynchronizationEndpoint` are abstract classes and therefore only methods one wants to customize should be implemented. For instance, the `SpaceDataSource.getById` method is delegated to the `SpaceDataSource.getDataIterator` method by default so in case there isn't an optimized way to implement the `getById` method it is recommended **NOT** to override it.
{{% /tip %}}

# Migrating From EDS APIs to Space Persistency APIs

One of the advantages the `SpaceDataSource` and `SpaceSynchronizationEndpoint` is the fact that it defines a single API for the purpose of persistency so instead of implementing many interfaces, one should extend only the the classes and methods which matches the requested functionality.
The following table lists the EDS interfaces methods replaced by the new Space Persistency classes:


|EDS Method|Space Persistency Method|
|:---------|:-----------------------|
|ManagedDataSource.init|Properties injected via Spring|
|ManagedDataSource.initialLoad|SpaceDataSource.initialDataLoad|
|ManagedDataSource.shutdown|Life cycle managed by user code or Spring|
|DataProvider.read|SpaceDataSource.getDataIterator/SpaceDataSource.getById|
|DataProvider.iterator|SpaceDataSource.getDataIterator|
|SQLDataProvider.iterator|SpaceDataSource.getDataIterator|
|DataPersister.write|SpaceSynchronizationEndpoint.onOperationsBatchSynchronization|
|DataPersister.update|SpaceSynchronizationEndpoint.onOperationsBatchSynchronization|
|DataPersister.remove|SpaceSynchronizationEndpoint.onOperationsBatchSynchronization|
|*BulkDataPersister.executeBulk|SpaceSynchronizationEndpoint.onOperationsBatchSynchronization|
|*BulkDataPersister.executeBulk|SpaceSynchronizationEndpoint.onTransactionSynchronization|

When using EDS API, `BulkDataPersister.executeBulk` might be invoked on two occasions:

1. Batch execution.
1. Transaction execution.

On transaction execution, getting the transaction metadata was done using the `BulkDataPersisterContext.getCurrentContext` method. This is no longer needed because on transaction execution, the `SpaceSynchronizationEndpoint.onTransactionSynchronization` method is invoked and receives the transaction data as a parameter.

# Configuration Changes

When using [Space Persistency](./space-persistency.html) - the same space properties used to configure the EDS can be used with two exceptions:

- "space-config.external-data-source.usage" specifies the EDS operation mode (read-only or read-write) is no longer used and the operation mode is determined by whether a `SpaceSynchronizationEndpoint` implementation has been specified or not. If it has been specified it means the operation mode is 'read-write', otherwise 'read-only'.

- "space-config.external-data-source.supports-inheritance" specifies whether the EDS implementation supports types inheritance. This space property is no longer and whether inheritance is supported by the implementation is determined by the `SpaceDataSource.supportsInheritance` method.

# Built In Hibernate Implementation

GigaSpaces provides a built in Hibernate EDS implementation. This implementation has been adjusted to work with the new [Space Persistency](./space-persistency.html) APIs. Therefore, two new Hibernate implementation classes were created: `DefaultHibernateSpaceDataSource` and `DefaultHibernateSpaceSynchronizationEndpoint`.

The following example shows how to configure a space with a Hibernate `SpaceDataSource` and a Mirror service with a Hibernate `SpaceSynchronizationEndpoint`:


```xml
<!-- SPACE -->
<bean id="hibernateSpaceDataSource" class="org.openspaces.persistency.hibernate.DefaultHibernateSpaceDataSourceFactoryBean">
    <property name="sessionFactory" ref="sessionFactory"/>
    <property name="initialLoadChunkSize" value="2000"/>
</bean>
<os-core:embedded-space  id="space" name="space" schema="persistent"
    mirror="true" space-data-source="hibernateSpaceDataSource">
    <os-core:properties>
        <props>
            <prop key="cluster-config.cache-loader.external-data-source">true</prop>
            <prop key="cluster-config.cache-loader.central-data-source">true</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>

<!-- MIRROR -->
<bean id="hibernateSpaceSynchronizationEndpoint" class="org.openspaces.persistency.hibernate.DefaultHibernateSpaceSynchronizationEndpointFactoryBean">
    <property name="sessionFactory" ref="sessionFactory"/>
</bean>
<os-core:mirror id="mirror" url="/./mirror-service" space-sync-endpoint="hibernateSpaceSynchronizationEndpoint" operation-grouping="group-by-space-transaction">
    <os-core:source-space name="mySpace" partitions="2" backups="1"/>
</os-core:mirror>
```
