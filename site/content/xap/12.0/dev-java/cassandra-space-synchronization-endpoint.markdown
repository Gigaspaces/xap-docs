---
type: post120
title:  Synchronization Endpoint
categories: XAP120
parent: cassandra-space-persistency.html
weight: 200
---


A Cassandra based implementation of the [Space Synchronization Endpoint](./space-synchronization-endpoint-api.html).

# Library dependencies

The Cassandra Space Synchronization Endpoint uses the [Hector Library](http://hector-client.github.com/hector/build/html/index.html) For communicating with the Cassandra cluster.
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
    <version>{{%version  "hector-core"%}}</version>
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
    <version>{{%version "cassandra-thrift"%}}</version>
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

An example of how the Cassandra Space Synchronization Endpoint can be configured within a mirror.

{{%tabs%}}
{{%tab "  Spring "%}}


```xml
<?xml version="1.0"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd">

    <bean id="propertiesConfigurer"
       class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"/>

    <bean id="hectorClient"
        class="org.openspaces.persistency.cassandra.HectorCassandraClientFactoryBean">

        <!-- comma separated seed list -->
        <property name="hosts" value="${cassandra.host}" />

        <!-- cassandra rpc communication port -->
        <property name="port" value="${cassandra.port}" />

        <!-- keyspace name to work with -->
        <property name="keyspaceName" value="${cassandra.keyspace}" />

    </bean>

    <bean id="cassandraSpaceSyncEndpoint"
        class="org.openspaces.persistency.cassandra.CassandraSpaceSynchronizationEndpointFactoryBean">

        <!-- configured above -->
        <property name="hectorClient" ref="hectorClient" />

    </bean>

    <os-core:mirror id="mirror" name="mirror-service"
        space-sync-endpoint="cassandraSpaceSyncEndpoint">
	<os-core:source-space name="space" partitions="${numOfPartitiones}" backups="${numOfBackups}"/>
    </os-core:mirror>

</beans>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
HectorCassandraClient hectorClient = new HectorCassandraClientConfigurer()
    .clusterName(cluster)
    .hosts(cassandraHosts)
    .port(cassandraPort)
    .keyspaceName(cassandraKeyspaceName)
    .create();

SpaceSynchronizationEndpoint syncEndpoint = new CassandraSpaceSynchronizationEndpointConfigurer()
    .hectorClient(hectorClient)
    .create();

IJSpace mirror = new EmbeddedSpaceConfigurer("mirror-service")
    .schema("mirror")
    .spaceSynchronizationEndpoint(syncEndpoint)
    .addProperty("space-config.mirror-service.cluster.name", "space")
    .addProperty("space-config.mirror-service.cluster.partitions", String.valueOf(numOfPartitiones))
    .addProperty("space-config.mirror-service.cluster.backups-per-partition", String.valueOf(numOfBackups))
    .create();
```

{{% /tab %}}
{{% /tabs %}}

For more details about different configurations see [Space Persistency](./space-persistency.html).

## CassandraSpaceSynchronizationEndpoint Properties


|Property|Description|
|:-------|:----------|
|hectorClient|A configured [HectorCassandraClient]({{% api-javadoc %}}/index.html?org/openspaces/persistency/cassandra/HectorCassandraClient.html) bean. see [Hector Cassandra Client](./cassandra-hector-client.html).|
|fixedPropertyValueSerializer|see [Property Value Serializer]( #property-value-serializer).|
|dynamicPropertyValueSerializer|see [Property Value Serializer](#property-value-Serializer).|
|flattenedPropertiesFilter| see [Flattened Properties Filter](#flattened-properties-filter).|
|columnFamilyNameConverter| see [Column Family Name Converter](#column-family-name-converter).|

{{%anchor Property-Value-Serializer %}}

### Property Value Serializer

By default when serializing object/document properties to column values, the following serialization logic is applied:


For fixed properties:

- If the type of the value to be serialized matches a primitive type in Cassandra it will be serialized as defined by the Cassandra primitive type serialization protocol.
- Otherwise, the value will be serialized using standard java Object serialization mechanism.

For dynamic properties:

- All values will be serialized using the default dynamic property value serializer implementation: [DynamicPropertyValueSerializer]({{% api-javadoc %}}/index.html?org/openspaces/persistency/cassandra/meta/types/dynamic/DynamicPropertyValueSerializer.html)

It is possible to override this default behavior by providing a custom implementation of [PropertyValueSerializer]({{% api-javadoc %}}/index.html?org/openspaces/persistency/cassandra/meta/types/dynamic/PropertyValueSerializer.html).
This interface is defined by these 2 methods:


```java
ByteBuffer toByteBuffer(Object value);
Object fromByteBuffer(ByteBuffer byteBuffer);
```

The behavior of overriding the serialization logic is different for fixed properties and dynamic properties:

- Fixed properties will only be serialized by the custom serializer if their type does not match a primitive type in Cassandra.
- Dynamic properties will always be serialized using the provided implementation. This means that they should be able to handle primitive types such as Integer, Long, etc...

{{% note %}}
Overriding the property value serializers in the `Cassandra Space Synchronization Endpoint` must be followed by overriding the same serializers in the `Cassandra Space Data Source`. Failure to do so will prevent the `Cassandra Space Data Source` from properly deserializing values read from Cassandra.
{{% /note %}}

{{%anchor Flattened-Properties-Filter %}}

## Flattened Properties Filter

### Introduction

When a type is introduced to the `Cassandra Space Synchronzation Endpoint`, the type's fixed properties will be introspected and the final result will be a mapping from this type's nested properties to column family columns.
The default behavior of this mapping is explained in the following example.
Consider the following simple POJO (could also be a `SpaceDocument`'s fixed properties):


```java
// implementation omitted for brevity
@SpaceClass
public class Person {

    @SpaceId
    public Long getId() ...

    public String getName() ...

    public Address getAddress() ...

    ...

}

public class Address {

    public String getStreetName() ...

    public Long getStreetNumber() ...

}
```

By default, the fixed properties will be mapped to the `Person` column family in Cassandra like this:


|Property|Column Name (and type)|
|:-------|:---------------------|
|person.id|(row key) (type: `Long`)|
|person.name|name (type: `UTF8`)|
|person.address.streetName|address.streetName (type: `UTF8`)|
|person.address.streetNumber|address.streetNumber (type: `Long`)|

Notice how the `address` property was flattened and its properties are flattened as columns.

Now suppose that a `Person` is written to the space as a `SpaceDocument` which also includes these dynamic properties:

- `String newName`
- `Address newAddress`

By default, dynamic properties are not flattened and are written as is to Cassandra. Moreover, their static type is not updated in the `Column Family` metadata and they are serialized using a custom serializer. (see [Property Value Serializer](./cassandra-space-synchronization-endpoint.html#property-value-serializer)).

This is how they will be written to Cassandra:


|Property|Column Name (and type)|
|:-------|:---------------------|
|person.newName|newName (type: Bytes)|
|person.newAddress|newAddress (type: Bytes)|

## Customization

It is possible to override the above behavior by providing a [FlattenedPropertiesFilter]({{% api-javadoc %}}/index.html?org/openspaces/persistency/cassandra/meta/mapping/filter/FlattenedPropertiesFilter.html) implementation.
The implementations is used during type introspection when a type is first introduced to the synchronization endpoint and whenever an entry of that type is written which contains dynamic properties.

The interface is defined by a single method:



```java
boolean shouldFlatten(PropertyContext propertyContext);
```

The return value indicates whether the current introspected property should be serialized as is or should its nested properties be introspected as well.
As for the above example, the default implementation [DefaultFlattenedPropertiesFilter]({{% api-javadoc %}}/index.html?org/openspaces/persistency/cassandra/meta/mapping/filter/DefaultFlattenedPropertiesFilter.html) returns `true` if and only if the property is fixed and the current introspection nesting level does not exceed 10.

The [PropertyContext]({{% api-javadoc %}}/index.html?org/openspaces/persistency/cassandra/meta/mapping/filter/PropertyContext.html) contains the following details about the current introspected property:


```java
String getPath();
String getName();
Class<?> getType();
boolean isDynamic();
int getCurrentNestingLevel();
```

{{%anchor Column-Family-Name-Converter %}}

## Column Family Name Converter

Due to implementation details of Cassandra regarding Column Families there are certain limitations when converting a type name (e.g: `com.example.data.Person`) to a column family name. Among these limitations is a 48 characters max length limitation and invalid characters in the name (such as '.').
The behavior for converting a type name to a column family name when creating a column family is defined by the interface [ColumnFamilyNameConverter]({{% api-javadoc %}}/index.html?org/openspaces/persistency/cassandra/meta/conversion/ColumnFamilyNameConverter.html).
This interface is defined by 1 method:


```java
String toColumnFamilyName(String typeName);
```

The default implementation is: [DefaultColumnFamilyNameConverter]({{% api-javadoc %}}/index.html?org/openspaces/persistency/cassandra/meta/conversion/DefaultColumnFamilyNameConverter.html).

# Considerations

- Collections and Maps are not flattened and are serialized as blobs using java object serialization mechanism.
- Writing entries that only have their id property set is not supported, these entries will not be written to Cassandra.
