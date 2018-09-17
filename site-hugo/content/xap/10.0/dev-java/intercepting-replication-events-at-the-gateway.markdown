---
type: post100
title:  Intercepting Replication Events
categories: XAP100
parent: multi-site-replication-overview.html
weight: 800
---

{{% ssummary %}}  {{% /ssummary %}}




The Synchronization Endpoint Interceptor allows a custom logic to be plugged in to the Replication Gateway, this can be used for various use cases. One use case is custom handling of distributed transaction consolidation failure events, where one for example, can keep track of such events in some database for later diagnostics and decision. The interceptor is plugged into the target gateway sink upon construction.

# The Interceptor API

The interceptor is an abstract class which may be extended to provide custom behavior only on relevant methods.


```java
public abstract class SynchronizationEndpointInterceptor
{
    /**
     * Triggered when a consolidation for a specific distributed transaction participant is failed due to
     * timeout or too much backlog accumulation while waiting for other participant parts.
     * @param participantData the transaction participant data for which the consolidation failed
     */
    public void onTransactionConsolidationFailure(ConsolidationParticipantData participantData)
    {
        participantData.commit();
    }

    /**
     * Triggered after synchronization of a transaction was completed successfully.
     * @param transactionData the transaction data
     */
    public void afterTransactionSynchronization(TransactionData transactionData)
    {
    }

    /**
     * Triggered after synchronization batch of operations was completed successfully.
     * @param batchData the batched operations data
     */
    public void afterOperationsBatchSynchronization(OperationsBatchData batchData)
    {
    }
}
```

# How to Plug a Custom Interceptor

To use a custom interceptor implementation one should first extends the `SynchronizationEndpointInterceptor` class and use the subclass in the target gateway `pu.xml` configuration:


```xml
<bean id="interceptor" class="com.gigaspaces.examples.MyCustomSynchronizationEndpointInterceptor" />

<os-gateway:sink id="sink" local-gateway-name="MY-SITE-NAME"
  gateway-lookups="gatewayLookups" local-space-url="jini://*/*/mySiteSpace">
  <os-gateway:sources>
    <os-gateway:source name="..." />
      ...
  </os-gateway:sources>
  ...
  <os-gateway:sync-endpoint-interceptor interceptor="interceptor"/>
</os-gateway:sink>
```

# Handling Intercepted events

There are three events the interceptor can receive and act upon

## On Transaction Consolidation Failure

This event is triggered upon distributed transaction consolidation failure, refer to [Gateway and Distributed Transactions](./multi-site-replication-over-the-wan.html#Configuring and Deploying the Gateway) for more info about scenarios triggering this event.
The interceptor can get data about the current transaction participant (transaction part in a specific partition) for which the consolidation had failed and decide whether to commit or abort this participant data independently of the other participants. This is done by interacting with the `ConsolidationParticipantData` which is passed to the method as argument. This object contains all the relevant data, such as the operations and entries that are under this transaction participant, transaction metadata which contains its id, the source which participate in this transaction etc.

## After Transaction Synchronization

This event is triggered after the entire transaction is successfully replicated to the final target and the `TransactionData` contains all the data that the transaction consists of, including metadata and the source of the transaction.

{{% note %}}
When a consolidated transaction is executed, the source will contain the name of one of the participants.
{{%/note%}}

## After Operations Batch Synchronization

This event is triggered after a batch of non transactional operations were successfully replicated to the final target, the operations batching is determined by the gateway logic and is not reflecting the original client batch of operations if such existed. However, it maintains the original order of operations in the source. The `OperationsBatchData` contains the relevant data, which is the batch of operations itself and the source of this operations.

# The Data Sync Operation

Each of the above intercepted events contains a data item that includes the relevant data synchronization operations. A transaction contains the operations that are executed within its boundaries. A batch of operations contains the list of operations that were synchronized in this batch. Each of this operations is a `DataSyncOperation` which expose the details of the single data synchronization operation.


```java
public interface DataSyncOperation
{

    /**
     * @return The operation UID.
     */
    String getUid();

    /**
     * @return the operation type.
     */
    DataSyncOperationType getDataSyncOperationType();

    /**
     * @return the operation data as object (i.e pojo), this can only be used
     * if {@link #supportsDataAsObject()} return true, otherwise an exception
     * will be thrown.
     */
    Object getDataAsObject();

    /**
     * @return the operation data as space document, this can only be
     * used if {@link #supportsDataAsDocument()} return true, otherwise an exception
     * will be thrown.
     */
    SpaceDocument getDataAsDocument();

    /**
     * @return the type descriptor of the data type. this can only be
     * used if {@link #supportsGetTypeDescriptor()} return true, otherwise an exception
     * will be thrown.
     */
    SpaceTypeDescriptor getTypeDescriptor();

    /**
     * @return whether this data operation support the {@link #getTypeDescriptor()} operation.
     */
    boolean supportsGetTypeDescriptor();

    /**
     * @return whether this data operation support the {@link #getDataAsObject()} operation.
     */
    boolean supportsDataAsObject();

    /**
     * @return whether this data operation support the {@link #getDataAsDocument()} operation.
     */
    boolean supportsDataAsDocument();
}
```

This API exposes the operation type, such as Write, Update, Remove and so on, as well as the entry itself where such exists.
Before calling each of the `getDataAsDocument`, `getDataAsObject` and `getTypeDescriptor`, the corresponding "supports" methods must be called to verify that the operation indeed applies to the current entry.
An invocation of `getDataAsObject` if the `supportsDataAsObject` methods return false, will throw an `UnsupportOperationException`.

# Example of an Interceptor Handling Consolidation Failure events

The following example will demonstrate how to implement an interceptor that stores in some external data store the list of distributed transactions that failed to consolidate and aborts them for later manual decision. Note, that there is a regular case where consolidation may show a false failure as described in [Gateway and Distributed Transactions](./multi-site-replication-over-the-wan.html#Configuring and Deploying the Gateway). This example will handle this case as well.


```java
public class ExampleSynchronizationEndpointInterceptor extends SynchronizationEndpointInterceptor
{
    ...

    private SomeExternalDataSource externalDataSource = ...

    public void onTransactionConsolidationFailure(ConsolidationParticipantData participantData)
    {
      TransactionParticipantMetaData metadata = participantData.getTransactionParticipantMetadata();
      if(!externalDataSource.isExecuted(metadata.getTransactionUniqueId()))
      {
        DataSyncOperation[] operations = participantData.getTransactionParticipantDataItems();
        externalDataSource.storeConsolidationFailedTransaction(metadata, operations);
      }
      participantData.abort();
    }

    public void afterTransactionSynchronization(TransactionData transactionData)
    {
      if(transactionData.isConsolidated())
        externalDataSource.storeExecutedConsolidatedTransactionMetadaa(transactionData.
                          getConsolidatedDistributedTransactionMetaData().getTransactionUniqueId());
    }

}
```
