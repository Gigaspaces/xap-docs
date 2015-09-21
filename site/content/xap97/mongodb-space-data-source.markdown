---
type: post97
title:  Data Source
categories: XAP97
parent: mongodb-space-persistency.html
weight: 100
---

A MongoDB based implementation of the [Space Data Source](./space-data-source-api.html). 

### Library dependencies 
The MongoDB Space Data Source uses [MongoDB Driver](http://www.allanbank.com/mongodb-async-driver/index.html) For communicating with the MongoDB cluster.
 
include the following in your `pom.xml`

```xml
<!-- currently the MongoDB library is not the central maven repository --> 
<repositories>
		<repository>
			<id>org.openspaces</id>
			<name>OpenSpaces</name>
			<url>http://maven-repository.openspaces.org</url>
		</repository>

		<repository>
			<releases>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
				<checksumPolicy>warn</checksumPolicy>
			</releases>
			<id>allanbank</id>
			<name>Allanbank Releases</name>
			<url>http://www.allanbank.com/repo/</url>
			<layout>default</layout>
		</repository>
</repositories>


<!-- mongodb java driver -->
<dependency>
	<groupId>org.mongodb</groupId>
	<artifactId>mongo-java-driver</artifactId>
	<version>2.11.2</version>
</dependency>
<dependency>
	<groupId>com.allanbank</groupId>
	<artifactId>mongodb-async-driver</artifactId>
	<version>1.2.3</version>
</dependency>

<dependency> 
	<groupId>org.antlr</groupId> 
	<artifactId>antlr4-runtime</artifactId> 
	<version>4.0</version> 
</dependency> 

<dependency>
    <groupId>com.gigaspaces</groupId>
    <artifactId>mongo-datasource</artifactId>
    <version>9.7.0-SNAPSHOT</version>
</dependency>

```

### Setup 

An example of how the MongoDB Space Data Source can be configured for a space that loads data back from MongoDB once initialized and 
also asynchronously persists the data using a mirror (see [MongoDB Space Synchronization Endpoint](./mongodb-space-synchronization-endpoint.html))).

{{%note%}}
With XAP 9.7.1 version please use the code example provided in [version 10.0](/xap100/mongodb-space-data-source.html)
{{%/note%}}


{{%tabs%}}

{{%tab Spring%}}
```xml
<?xml version="1.0" encoding="utf-8"?> 
<beans xmlns="http://www.springframework.org/schema/beans" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context" 
xmlns:os-core="http://www.openspaces.org/schema/core" xmlns:os-jms="http://www.openspaces.org/schema/jms" 
xmlns:os-events="http://www.openspaces.org/schema/events" 
xmlns:os-remoting="http://www.openspaces.org/schema/remoting" 
xmlns:os-sla="http://www.openspaces.org/schema/sla" xmlns:tx="http://www.springframework.org/schema/tx" 

xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd 
http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd 
http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{%currentversion%}}/events/openspaces-events.xsd
http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.1.xsd 
http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{%currentversion%}}/remoting/openspaces-remoting.xsd">

<bean id="propertiesConfigurer" 
class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer" /> 

<os-core:space id="space" url="/./dataSourceSpace"
	space-data-source="spaceDataSource" mirror="true" schema="persistent">
	<os-core:properties>
		<props>
			<!-- Use ALL IN CACHE, put 0 for LRU --> 
			<prop key="space-config.engine.cache_policy">1</prop>				
			<prop key="cluster-config.cache-loader.central-data-source">true</prop>
			<prop key="cluster-config.mirror-service.supports-partial-update">true</prop>
		</props>
	</os-core:properties>
</os-core:space>

<os-core:giga-space id="gigaSpace" space="space" /> 

<bean id="mongoClient"
		class="com.gigaspaces.persistency.MongoClientConnectorBeanFactory">
		<property name="db" value="${mongo.db}" />
		<property name="config">
			<bean class="com.allanbank.mongodb.MongoClientConfiguration">
				<constructor-arg value="mongodb://${mongo.host}:${mongo.port}/${mongo.db}"
					type="java.lang.String" />
				<property name="defaultDurability" value="ACK"/>		
			</bean>
		</property>
</bean>

<bean id="spaceDataSource" 
		class="com.gigaspaces.persistency.MongoSpaceDataSourceBeanFactory">
		<property name="mongoClientConnector" ref="mongoClient" />
</bean>


</beans> 

```
{{% /tab %}}
{{%tab "  Code "%}}

```java
MongoClientConfiguration config = new MongoClientConfiguration();

config.addServer(host);				
config.setDefaultDurability(Durability.ACK);

MongoClientConnector client = new MongoClientConnectorConfigurer()
		.config(config)
		.db(dbName)
		.create();	

MongoSpaceDataSource spaceDataSource = new MongoSpaceDataSourceConfigurer()
		.mongoClientConnector(client)
		.create();

GigaSpace gigaSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer("/./space")	
.schema("persistent") 
.mirror(true) 
.cachePolicy(new LruCachePolicy()) 
.addProperty("cluster-config.cache-loader.central-data-source", "true") 
.addProperty("cluster-config.mirror-service.supports-partial-update", "true") 
.spaceDataSource(spaceDataSource) 
.space()).gigaSpace(); 

```
{{% /tab %}}
{{% /tabs %}}

For more details about different configurations see [Space Persistency](./space-persistency.html). 

###Before you begin###

Before deploying your Processing Unit, please do the following:

1. Copy the `mongo-datasource.jar` from `lib\optional\datasource\mongo` to `lib\optional\pu-common`.
2. download the following jars and copy them to `lib\optional\pu-common`:

	- `antlr-runtime.jar` from [antlr's website](http://www.antlr.org/download.html) .

	- `mongo-java-driver-2.9.3.jar` from [mongoDB's website](http://docs.mongodb.org/ecosystem/drivers/java/) .

	- `mongodb-async-driver-1.2.3.jar` from [allanbank's website](http://www.allanbank.com/mongodb-async-driver/download.html) .

	- `guava-r08.jar` from [Guava-project's website](https://code.google.com/p/guava-libraries/wiki/Release08) . **NOTE:** you must download Guava's release 08, and extract the `guava-r08.jar` from within the `guava-r08.zip` that you have downloaded

### `MongoSpaceDataSource` Properties


|Property|Description|Default|
|:-------|:----------|:------|
|mongoClientConnector|A configured `com.gigaspaces.persistency.MongoClientConnector` bean. Must be configured| | 

## Considerations 

### General limitations 
- All classes that belong to types that are to be introduced to the space during the initial metadata load must exist on the classpath of the JVM the Space is running on. 

### Cache miss Query limitations 
Supported queries:

- `id = 1234` 

- `name = 'John' AND age = 13` 

- `address.streetName = 'Liberty'` 

- `age > 15`

- `age < 20`

- `age <= 20`

- ` age >= 15`

- `name = 'John' OR name = 'Jane'`

- `name rlike 'A.*B'`

- `name like 'A%'`

- `name is NULL`

- `name is NOT NULL`

note: java types Short, Float, BigDecimal and BigInt supported only =,<> queries >,<,>=,<= is not supported.

Unsupported queries:
Unsupported queries:
- Contains is unsupported
