---
type: post102
title:  Archive Handler
categories: XAP102
parent: cassandra.html
weight: 200
---




{{%section%}}
{{%column width="80%" %}}

The [Archive Container](./archive-container.html) can be configured to work against Cassandra (without writing any extra code). The [ArchiveOperationHandler interface]({{% api-javadoc %}}/org/openspaces/archive/ArchiveOperationHandler.html) abstracts the Big-Data storage from the [Archive Container](./archive-container.html). The Cassandra Archive Operation Handler implements this interface by [serializing](#property-value-serializer) space objects into Cassandra.

{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/archive-container-cassandra.jpg"%}}
{{%/column%}}
{{%/section%}}



# Library dependencies

The Cassandra Archive Operation Handler uses the [Hector Library](http://hector-client.github.com/hector/build/html/index.html) for communicating with the Cassandra cluster.
Include the following in your `pom.xml`

{{%tabs%}}
{{%tab "  hector using log4j "%}}


```xml
<dependency>
    <groupId>org.apache.cassandra</groupId>
    <artifactId>cassandra-clientutil</artifactId>
    <version>{{%version "cassandra-clientutil"%}}</version>
</dependency>

<dependency>
    <groupId>org.apache.cassandra</groupId>
    <artifactId>cassandra-thrift</artifactId>
    <version>{{%version "cassandra-thrift"%}}</version>
</dependency>

<dependency>
    <groupId>org.hectorclient</groupId>
    <artifactId>hector-core</artifactId>
    <version>{{%version "hector-core"%}}</version>
</dependency>
```

{{% /tab %}}
{{%tab "  hector using java.util.logging "%}}


```xml
<dependency>
    <groupId>org.apache.cassandra</groupId>
    <artifactId>cassandra-clientutil</artifactId>
    <version>{{%version "cassandra-clientutil"%}}</version>
</dependency>

<dependency>
    <groupId>org.apache.cassandra</groupId>
    <artifactId>cassandra-thrift</artifactId>
    <version>version cassandra-thrift%}}</version>
</dependency>

<dependency>
    <groupId>org.hectorclient</groupId>
    <artifactId>hector-core</artifactId>
    <version>{{%version "hector-core"%}}</version>
    <exclusions>
        <exclusion>
	    <groupId>org.slf4j</groupId>
	    <artifactId>slf4j-log4j12</artifactId>
	</exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>{{%version "slf4j-api"%}}</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-jdk14</artifactId>
    <version>{{%version "slf4j-jdk14"%}}</version>
</dependency>
```

{{% /tab %}}
{{% /tabs %}}

# Setup

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-archive-cassandra:cassandra-archive-handler id="cassandraArchiveHandler"
  giga-space="gigaSpace"
  hosts="127.0.0.1"
  port="9160"
  keyspace="mykeyspace"
  write-consistency="QUORUM"
/>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="cassandraArchiveHandler" class="org.openspaces.persistency.cassandra.archive.CassandraArchiveOperationHandler">
	<property name="gigaSpace" ref="gigaSpace"/>
	<property name="hosts" value="127.0.0.1" />
	<property name="port" value="9160" />
	<property name="keyspace" value="mykeyspace" />
	<property name="writeConsistency" value="QUORUM" />
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java

ArchiveOperationHandler cassandraArchiveHandler =
    new CassandraArchiveOperationHandlerConfigurer()
    .gigaSpace(gigaSpace)
    .hosts("127.0.0.1")
    .port(9160)
    .keyspace("mykeyspace")
    .writeConsistency(CassandraConsistencyLevel.QUORUM)
    .create();

// To free the resources used by the archive container make sure you close it properly.
// A good life cycle event to place the destroy() call would be within the @PreDestroy or DisposableBean#destroy() method.

archiveContainer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

# CassandraArchiveOperationHandler Properties


|Property|Description|
|:-------|:----------|
|gigaSpace| GigaSpace reference used for type descriptors. see [Archive Container#Configuration](./archive-container.html#Configuration)|
|hosts | Comma separated list of Cassandra host names or ip addresses|
|port | Cassandra port. By default uses 9160|
|keyspace | Cassandra keyspace|
|propertyValueSerializer|see [Property Value Serializer](#property-value-serializer).|
|flattenedPropertiesFilter| see [Flattened Properties Filter](./cassandra-space-synchronization-endpoint.html#Flattened Properties Filter).|
|columnFamilyNameConverter| see [Column Family Name Converter](./cassandra-space-synchronization-endpoint.html#Column Family Name Converter).|


#### Property Value Serializer

By default when serializing object/document properties to column values, the following serialization logic is applied:

- If the type of the value to be serialized matches a primitive type in Cassandra it will be serialized as defined by the Cassandra primitive type serialization protocol.
- Otherwise, the value will be serialized using standard java Object serialization mechanism.

It is possible to override this default behavior by providing a custom implementation of [PropertyValueSerializer]({{% api-javadoc %}}/index.html?org/openspaces/persistency/cassandra/meta/types/dynamic/PropertyValueSerializer.html).
This interface is defined by these 2 methods:


```java
ByteBuffer toByteBuffer(Object value);
Object fromByteBuffer(ByteBuffer byteBuffer);
```

Properties will only be serialized by the custom serializer if their type does not match a primitive type in Cassandra.

# Known Limitations

The CassandraArchiveHandler has the following known limitations:
 *  The archiver must not write two different entries with the same ID. This would corrupt the entry in Cassandra.
 *  Only Space Documents are supported. You can still write POJOs to the space, but the @EventTemplate used for taking objects from the space must be a SpaceDocument.
 *  The archiver is thread safe
 *  The archiver is idempotent as long as there are no two threads that are writing two different objects with the same space id.
 *  Both fixed and dynamic space properties are serialized with the same propertyValueSerializer.
