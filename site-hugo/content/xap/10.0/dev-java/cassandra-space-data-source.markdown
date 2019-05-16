---
type: post100
title:  Data Source
categories: XAP100
parent: cassandra-space-persistency.html
weight: 100
canonical: auto
---





A Cassandra based implementation of the [Space Data Source](./space-data-source-api.html).

### Library dependencies

The Cassandra Space Data Source uses [Cassandra JDBC Driver](http://code.google.com/a/apache-extras.org/p/cassandra-jdbc/) and [Hector Library](http://hector-client.github.com/hector/build/html/index.html) when interacting with the Cassandra cluster.
include the following in your `pom.xml`


```xml
<!-- currently the cassandra-jdbc library is not the central maven repository -->
<repository>
    <id>org.openspaces</id>
    <name>OpenSpaces</name>
    <url>http://maven-repository.openspaces.org</url>
</repository>

<dependency>
    <groupId>org.apache.cassandra</groupId>
    <artifactId>cassandra-clientutil</artifactId>
cassandra-clientutil
    <version>{{%version "cassandra-clientutil"%}}</version>
</dependency>

<dependency>
    <groupId>org.apache.cassandra</groupId>
    <artifactId>cassandra-thrift</artifactId>
cassandra-thrift
    <version>{{%version "cassandra-thrift"%}}</version>
</dependency>

<dependency>
    <groupId>org.apache.cassandra</groupId>
    <artifactId>cassandra-jdbc</artifactId>
cassandra-jdbc
    <version>{{%version "cassandra-jdbc"%}}</version>
</dependency>

<dependency>
    <groupId>org.hectorclient</groupId>
    <artifactId>hector-core</artifactId>
hector-core
    <version>{{%version "hector-core"%}}</version>
</dependency>
```

### Setup

An example of how the Cassandra Space Data Source can be configured for a space that loads data back from Cassandra once initialized and
also asynchronously persists the data using a mirror (see [Cassandra Space Synchronization Endpoint](./cassandra-space-synchronization-endpoint.html)).

{{%tabs%}}
{{%tab "  Spring "%}}


```xml
<?xml version="1.0"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
spring
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd">

    <bean id="propertiesConfigurer"
       class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"/>

    <bean id="cassandraDataSource" class="org.apache.cassandra.cql.jdbc.CassandraDataSource">
        <constructor-arg value="${cassandra.host}" />
        <constructor-arg value="${cassandra.port}" />
        <constructor-arg value="${cassandra.keyspace}" />
        <constructor-arg value="${cassandra.user}" />
        <constructor-arg value="${cassandra.password}" />
        <constructor-arg value="2.0.0" />
    </bean>

    <bean id="hectorClient"
        class="org.openspaces.persistency.cassandra.HectorCassandraClientFactoryBean">

        <!-- comma separated seed list -->
        <property name="hosts" value="${cassandra.host}" />

        <!-- cassandra rpc communication port -->
        <property name="port" value="${cassandra.port}" />

        <!-- keyspace name to work with -->
        <property name="keyspaceName" value="${cassandra.keyspace}" />

    </bean>

    <bean id="cassandraSpaceDataSource"
        class="org.openspaces.persistency.cassandra.CassandraSpaceDataSourceFactoryBean">

        <!-- configured above -->
        <property name="cassandraDataSource" ref="cassandraDataSource" />

        <!-- configured above -->
        <property name="hectorClient" ref="hectorClient" />

    </bean>

   <os-core:embedded-space id="space" name="dataSourceSpace"
        space-data-source="cassandraSpaceDataSource"
        schema="persistent"
        mirror="true">
        <os-core:properties>
            <props>
                <prop key="space-config.engine.cache_policy">1</prop>
                <prop key="cluster-config.cache-loader.central-data-source">true</prop>
                <prop key="cluster-config.mirror-service.supports-partial-update">true</prop>
            </props>
        </os-core:properties>
    </os-core:embedded-space>

    <os-core:giga-space id="gigaSpace" space="space" />

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

CassandraDataSource ds = new CassandraDataSource(
                cassandraHosts,
                cassandraPort,
                cassandraKeyspaceName,
                cassandraUser,
                cassandraPassword,
                "2.0.0");

CassandraSpaceDataSource spaceDataSource = new CassandraSpaceDataSourceConfigurer()
            .cassandraDataSource(ds)
            .hectorClient(hectorClient)
            .create();

GigaSpace gigaSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer("/./mySpace")
  .schema("persistent")
  .mirror(true)
  .cachePolicy(new AllInCachePolicy())
  .addProperty("cluster-config.cache-loader.central-data-source", "true")
  .addProperty("cluster-config.mirror-service.supports-partial-update", "true")
  .spaceDataSource(spaceDataSource)
  .space()).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

For more details about different configurations see [Space Persistency](./space-persistency.html).

### `CassandraSpaceDataSource` Properties


|Property|Description|Default|
|:-------|:----------|:------|
|cassandraDataSource|A configured `org.apache.cassandra.cql.jdbc.CassandraDataSource` bean. Must be configured to use CQL 2.0.0.| |
|hectorClient|A configured [HectorCassandraClient]({{% api-javadoc %}}/org/openspaces/persistency/cassandra/HectorCassandraClient.html) bean. see [Hector Cassandra Client](./cassandra-hector-client.html).| |
|minimumNumberOfConnections|The minimum number of jdbc connections to hold in the pool.|5|
|maximumNumberOfConnections|The maximum number of jdbc connections to hold in the pool. If a connection is required and the pool is full, a new connection will be opened which will be closed shortly after its usage is completed.|30|
|batchLimit|The underlying cassandra-jdbc implementation brings the entire result set in one batch. If paging is required, this parameter will control the maximum number of entries to fetch in each batch. (this parameter controls both initial data load and general cache miss queries) |10000|
|fixedPropertyValueSerializer|see [Property Value Serializer](./cassandra-space-synchronization-endpoint.html#Property Value Serializer).| |
|dynamicPropertyValueSerializer|see [Property Value Serializer](./cassandra-space-synchronization-endpoint.html#Property Value Serializer).| |

## Considerations

### General limitations

- Extended indexes are not supported. (If one is set on a property, it will be treated as Basic index).
- All classes that belong to types that are to be introduced to the space during the initial metadata load must exist on the classpath of the JVM the Space is running on.
- Unindexed properties cannot be queried.

### Cache miss Query limitations

Supported queries:

- `id = 1234`
- `name = 'John' AND age = 13`
- `address.streetName = 'Liberty'`

Unsupported queries:

- `age > 15`
- `name = 'John' OR name = 'Jane'`

Unsupported queries and queries on unindexed properties will result in a runtime exception.
