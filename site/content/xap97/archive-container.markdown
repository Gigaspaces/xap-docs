---
type: post97
title:  Archive Container
categories: XAP97
parent: big-data.html
weight: 100
---


{{%section%}}
{{%column width="60%" %}}
The archive container is used to transfer historical data into Big-Data storage (for example Cassandra).

The typical scenario is when streaming vast number of raw events through the Space, enriching them and then moving them to a Big-Data storage. Typically, there is no  intention of keeping them in the space nor querying them in the space.

{{%/column%}}
{{%column width="35%" %}}
![](/attachment_files/archive-container.jpg)
{{%/column%}}
{{%/section%}}

{{%vbar "The Archive Container:"%}}
- automatically moves objects from the Space to the Big-Data storage.
- configures the set of objects to be archived.
- supports Space fail-over, and Big-Data storage unavailability.
- The persisted objects, can then be read by 3rd party tools directly from the Big-Data storage.
- Big-Data storage is abstracted with the [ArchiveOperationHandler]({{% api-javadoc %}}/index.html?org/openspaces/archive/ArchiveOperationHandler.html) interface. [Cassandra Archive Operation Handler](./cassandra-archive-operation-handler.html) implementation is available out of the box.
{{%/vbar%}}

# Archive Container vs Space Persistency

The Archive Container differs from [Space Persistency](./space-persistency.html) in the following ways:

- Persisted objects are not read back from the Big-Data storage into the Space.
- Objects are persisted from multiple partitions in parallel directly to the Big-Data storage (not going through the [Space Persistency](./space-persistency.html)).
- Archive Container uses the [Polling Container](./polling-container.html) behind the scenes, which can be co-located with each space partition.

## Archive Container running side-by-side with Space Persistency

There are use-cases in which the same Space uses both an Archive Container and Space Persistency. Normally types that are archived by the Archive Container, should not be handled by Space Persistency. Hence, these types should be marked as [Transient](./transient-entries.html).
In cases where the same type is handled by both, configure the Archive Container and EDS to modify a different table/keyspace.

# Configuration

Here is a simple example of an archive container configuration:

{{%tabs%}}
{{%tab "  Annotation "%}}

```xml
<!-- Enable scan for OpenSpaces and Spring components -->
<context:component-scan base-package="com.mycompany"/>

<!-- Enable support for @Archive annotation -->
<os-archive:annotation-support />

<os-core:space id="space" url="/./space" />

<os-core:distributed-tx-manager id="transactionManager" space="space"/>

<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager" tx-timeout="120"/>

<os-archive:cassandra-archive-handler id="cassandraArchiveHandler"
  giga-space="gigaSpace"
  hosts="${cassandra.hosts}"
  keyspace="${cassandra.keyspace}"
/>
```




```java
@Archive(batchSize = 100)
@TransactionalEvent(timeout = 120)
public class ExpiredTweetsArchiveContainer {

    @DynamicEventTemplate
    public SQLQuery<SpaceDocument> getUnprocessedExpiredTweets() {
        final long expired = System.currentTimeMillis() - 60000;
        final boolean processed = true;
        final SQLQuery<SpaceDocument> dynamicTemplate =
            new SQLQuery<SpaceDocument>("Tweet", "Processed = ? AND Timestamp < ?", processed, expired);
        return dynamicTemplate
    }
}
```

{{% /tab %}}
{{%tab "  Namespace "%}}


```xml

<os-core:space id="space" url="/./space" />

<os-core:distributed-tx-manager id="transactionManager" space="space"/>

<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

<os-archive:cassandra-archive-handler id="cassandraArchiveHandler"
  giga-space="gigaSpace"
  hosts="${cassandra.hosts}"
  keyspace="${cassandra.keyspace}"
/>

<os-archive:archive-container
  id="archiveContainer"
  giga-space="gigaSpace"
  archive-handler="cassandraArchiveHandler"
  batch-size="${archiver.batch.size}" >

  <os-archive:tx-support tx-manager="transactionManager" tx-timeout="120"/>
  <os-core:template ref="archiveFilter" />

</os-archive:archive-container>

<bean id="archiveFilter" class="ExpiredTweetsFilter"/>
```


```java
public class ExpiredTweetsFilter implements DynamicEventTemplateProvider{

    @Override
    public Object getDynamicEventTemplate() {
        final long expired = System.currentTimeMillis() - 60000;
        final boolean processed = true;
        final SQLQuery<SpaceDocument> dynamicTemplate =
            new SQLQuery<SpaceDocument>("Tweet", "Processed = ? AND Timestamp < ?", processed, expired);
        return dynamicTemplate;
    }
}
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.UrlSpaceFactoryBean">
    <property name="url" value="/./space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="cassandraArchiveHandler" class="org.openspaces.persistency.cassandra.archive.CassandraArchiveOperationHandler">
	<property name="gigaSpace" ref="gigaSpace"/>
	<property name="hosts" value="${cassandra.hosts}" />
	<property name="keyspace" value="${cassandra.keyspace}" />
</bean>

<bean id="archiver" class="org.openspaces.archive.ArchivePollingContainer">
  <property name="gigaSpace" ref="gigaSpace" />
  <property name="dynamicTemplate" ref="archiveFilter" />
  <property name="archiveHandler" ref="cassandraArchiveHandler" />
</bean>
<bean id="archiveFilter" class="ExpiredTweetsFilter"/>
```


```java
public class ExpiredTweetsFilter implements DynamicEventTemplateProvider{

    @Override
    public Object getDynamicEventTemplate() {
        final long expired = System.currentTimeMillis() - 60000;
        final boolean processed = true;
        final SQLQuery<SpaceDocument> dynamicTemplate =
            new SQLQuery<SpaceDocument>("Tweet", "Processed = ? AND Timestamp < ?", processed, expired);
        return dynamicTemaplte;
    }
}
```

{{% /tab %}}
{{%tab "  Code "%}}


```java

TransactionManager txManager = new DistributedJiniTxManagerConfigurer().transactionManager();
UrlSpaceConfigurer urlSpaceConfigurer = new UrlSpaceConfigurer("/./space");
IJSpace space = urlSpaceConfigurer.create();
GigaSpace gigaSpace = new GigaSpaceConfigurer(space).transactionManager(txManager).create();

ArchiveOperationHandler cassandraArchiveHandler =
    new CassandraArchiveOperationHandlerConfigurer()
    .keyspace(cassandraKeyspace)
    .hosts(cassandraHost)
    .gigaSpace(gigaSpace)
    .create();

ArchivePollingContainer archiveContainer =
  new ArchivePollingContainerConfigurer(gigaSpace)
  .archiveHandler(cassandraArchiveHandler);
  .transactionManager(txManager);
  .batchSize(100);
  .dynamicTemplate(new DynamicEventTemplateProvider() {
        @Override
   	public Object getDynamicEventTemplate() {
   	  final long expired = System.currentTimeMillis() - 60000;
   	  final boolean processed = true;
	  final SQLQuery<SpaceDocument> dynamicTemplate =
			new SQLQuery<SpaceDocument>("Tweet", "Processed = ? AND Timestamp < ?", processed, expired);
	  return dynamicTemplate;
    }
  })
  .create();

// To free the resources used by the archive container make sure you close it properly.
// A good life cycle event to place the destroy() call would be within the @PreDestroy or DisposableBean#destroy() method.

archiveContainer.destroy();
cassandraArchiveHandler.destroy();
urlSpaceConfigurer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

{{%note%}}
For all possible Spring configuration options see the [schema definitions](/api_documentation/)
{{%/note%}}


The example above removes (takeMultiple) objects with a certain timestamp member value from space and writes them onto Cassandra.
The takeMultiple operations are performed on the configured [GigaSpace](./the-gigaspace-interface.html) bean (in this case, if working in a clustered topology, it is performed directly on the cluster member).
The archive operation is performed on the bean that implements the ArchiveOpertaionHandler interface, in this case the CassandraArchiveOperationHandler bean.

For a  real-world example consult the streaming big data example :

- [Big-Data example spring xml file](https://github.com/CloudifySource/cloudify-recipes/blob/master/apps/streaming-bigdata/processor/src/main/resources/META-INF/spring/pu.xml)
- [Custom Archive Container template and error handler](https://github.com/CloudifySource/cloudify-recipes/blob/master/apps/streaming-bigdata/processor/src/main/java/org/openspaces/bigdata/processor/TweetArchiveFilter.java )

# Primary/Backup

The archive container, performs take operations only when the relevant space it is working against is in primary mode. When the space is in backup mode, no take operations are performed. If the space moves from backup mode to primary mode, the take operations are started.

{{% info %}}
This mostly applies when working with an embedded space directly with a cluster member. When working with a clustered space (performing operations against the whole cluster), the mode of the space is always primary.
{{%/info%}}

# Static Template Definition

When removing objects from the space, a template is defined, creating a virtualized subset of data within the space that matches it. GigaSpaces supports templates based on the actual domain model (with `null` values denoting wildcards), which are shown in the examples. GigaSpaces allows the use of [SQLQuery](./query-sql.html) in order to query the space, which can be easily used with the event container as the template. Here is an example of how it can be defined:

{{%tabs%}}
{{%tab "  Annotation "%}}


```java
@Archive
public class ProcessedTweetsFilter {

    @EventTemplate
    public SQLQuery<SpaceDocument> processedTweets() {
   	  final boolean processed = true;
	  final SQLQuery<SpaceDocument> staticTemplate =
		new SQLQuery<SpaceDocument>("Tweet", "Processed = ?", processed);
	  return staticTemplate;
    }
}
```

{{% /tab %}}
{{%tab "  Namespace "%}}


```xml

<os-archive:archive-container
  id="archiveContainer"
  giga-space="gigaSpace"
  archive-handler="cassandraArchiveHandler"
  batch-size="${archiver.batch.size}" >

  <os-archive:tx-support tx-manager="transactionManager"/>
  <os-core:sql-query where="processed = true" class-name="Tweet"/>

</os-archive:archive-container>

```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="archiveContainer" class="org.openspaces.archive.ArchivePollingContainer">

    <property name="gigaSpace" ref="gigaSpace" />
	<property name="archiveHandler" ref="cassandraArchiveHandler" />
	<property name="batchSize" value="100" />

    <property name="template">
        <bean class="com.j_spaces.core.client.SQLQuery">
            <constructor index="0" value="Tweet" />
            <constructor index="0" value="processed = true" />
        </bean>
    </property>
</bean>
```

{{% /tab %}}
{{% /tabs %}}

# Dynamic Template Definition

When removing objects from the space a dynamic template can be used. A method providing a dynamic template is called before each take operation, and can return a different object in each call.
The event template object has the same syntax rules as with @EventTemplate.
See [Configuration](#configuration) for a complete example

# Batch Operations

Archiving in batches may improve the processing throughput performance. Instead of consuming object one at a time from the space and archiving it, you may consume a batch with multiple objects and process these in one transaction. This may improve the overall throughput rate, but may impact the latency of the individual object archiving time.

The archive operation handler determines if it can archive more than one object at a time by implementing `ArchiveOperationHandler.supportsBatchArchiving()`.
If the archive handler supports batch archiving then multiple objects are removed from the space using takeMultiple and handed over to the `ArchiveOperationHandler.archive()` method.
If the archive handler does not support batch archiving, or if the archive container batch-size is 1, then batch operation is disabled. Otherwise, batch operation is enabled.

# Transaction Support

Both the space take operation and the archive action should be configured to be performed under a transaction. When an exception occurs in the archiver under transaction, the take operation is rolled back and the object is returned to the space. In case of an exception during a batch archive operation, the complete object batch is returned to the space. The next time objects are archived, they would be taken from the space and archived again. This means that the archive operation handler archive needs to do one of the following:

- `ArchiveOperationHandler.supportsBatchArchiving()` returns true, and `ArchiveOperationHandler.archive()` is atomic (meaning that throwing an exception cancels all writes - all or nothing semantics).
- `ArchiveOperationHandler.supportsBatchArchiving()` returns true, and `ArchiveOperationHandler.archive()` is idempotent (meaning that writing the same objects the second time has no effect).
- `ArchiveOperationHandler.supportsBatchArchiving()` returns false (meaning `ArchiveOperationHandler.archive()` is served with one object at a time). Even when returning false, there is a possibility of the same object being archived twice in case of a space primary/backup fail-over. So it has to be able to handle this scenario.

When using transactions with archive container a special care should be taken with timeout values. Transactions started by the archive container can have a timeout value associated with them (if not set will default to the default timeout value of the transaction manager, which is 90 Sec). If setting a specific timeout value, make sure the timeout value is higher than receive-timeout and the `ArchiveOperationHandler#archive()` time together.

Adding transaction support is done by injecting a transaction manager into the archive-container and giga-space beans. See the example at the [Configuration](#configuration) section.

{{% note %}}
Note the timeout value is in seconds as per Spring spec for TransactionDefinition.
{{%/note%}}

# Default Values of Archive Container Configuration Parameters

The default values for all of the polling container configuration parameters such as `concurrent-consumers, batch-size, receive-timeout` and others can be found in the JavaDoc (and sources) of the class [ArchivePollingContainer]({{% api-javadoc %}}/index.html?org/openspaces/archive/ArchivePollingContainer.html) and its super class, namely [SimplePollingEventListenerContainer]({{% api-javadoc %}}/index.html?org/openspaces/events/polling/SimplePollingEventListenerContainer.html) and  [AbstractPollingEventListenerContainer]({{% api-javadoc %}}/index.html?org/openspaces/events/polling/AbstractPollingEventListenerContainer.html).
For example, `concurrent-consumers` default value is documented in the method `SimplePollingEventListenerContainer.setConcurrentConsumers(int)`

# FIFO Grouping

The FIFO Grouping designed to allow efficient processing of events with partial ordering constraints. Instead of maintaining a FIFO queue per class type, it lets you have a higher level of granularity by having FIFO queue maintained according to a specific value of a specific property. For more details see [FIFO grouping](./fifo-grouping.html).

# Concurrent Consumers

By default, the archive container starts a single thread that performs take operations and invokes the archive handler. It can be configured to start several concurrent consumer threads and have an upper limit to the concurrent consumer threads. This provides faster archiving, however, any FIFO behavior that might be configured in the space and/or template is lost.
In order to archive objects using multiple consumer threads, in the same order they are written to the Space:

- You may start multiple archive containers consuming data in FIFO manner , each running a single consumer thread using different (mutually exclusive) templates.
- You may use one archive container with multiple consumer threads and enable FIFO Grouping. The FIFO order of each value is not broken. See [FIFO Grouping](./fifo-grouping.html) page for more details.

Here is an example of an archive container with 3 concurrent consumers and 5 maximum concurrent consumers and fifo grouping enabled:

{{%tabs%}}
{{%tab "  Annotation "%}}


```java
@Archive(batchSize = 100, concurrentConsumers = 3, maxConcurrentConsumers = 5, useFifoGrouping = true)
@TransactionalEvent(timeout = 120)
public class ExpiredTweetsFilter {

    @DynamicEventTemplate
   	public SQLQuery<SpaceDocument> expiredTweets() {
   	  // ...
    }
}
```

{{% /tab %}}
{{%tab "  Namespace "%}}


```xml

<os-archive:archive-container id="archiveContainer" giga-space="gigaSpace" batch-size="100"
                             concurrent-consumers="3" max-concurrent-consumers="5" useFifoGrouping="true" >
    <!-- ... -->
</os-archive:archive-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="archiveContainer" class="org.openspaces.archive.ArchivePollingContainer">
    <property name="batchSize" value="100" />
	<property name="concurrentConsumers" value="3" />
    <property name="maxConcurrentConsumers" value="5" />
	<property name="useFifoGrouping" value="true" />
    <!-- ... -->
</bean>
```

{{% /tab %}}
{{% /tabs %}}




- Spring pu.xml file header


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:os-sla="http://www.openspaces.org/schema/sla"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:os-core="http://www.openspaces.org/schema/core"
	xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:os-archive="http://www.openspaces.org/schema/archive"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
	                    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.1.xsd
	                    http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
	                    http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{%currentversion%}}/events/openspaces-events.xsd
	                    http://www.openspaces.org/schema/archive http://www.openspaces.org/schema/{{%currentversion%}}/archive/openspaces-archive.xsd">
...

</beans>
```
