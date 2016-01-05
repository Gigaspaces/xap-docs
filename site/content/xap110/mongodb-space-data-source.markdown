---
type: post110
title:  Data Source
categories: XAP110
parent: mongodb-space-persistency.html
weight: 100
---

A MongoDB based implementation of the [Space Data Source](./space-data-source-api.html). 

# Library dependencies
The MongoDB Space Data Source uses [MongoDB Driver](http://docs.mongodb.org/ecosystem/drivers/java/) For communicating with the MongoDB cluster.
 
include the following in your `pom.xml`

```xml
	<!-- currently the MongoDB library is not the central maven repository --> 
	<repositories>
		<repository>
			<id>org.openspaces</id>
			<name>OpenSpaces</name>
			<url>http://maven-repository.openspaces.org</url>
		</repository>
	</repositories>


	<dependencies>
		...
		<!-- mongodb java driver -->
		<dependency>
			<groupId>org.mongodb</groupId>
			<artifactId>mongo-java-driver</artifactId>
			<version>{{%version "mongo-java-driver"%}}</version>
		</dependency>

		<dependency> 
			<groupId>org.antlr</groupId> 
			<artifactId>antlr4-runtime</artifactId> 
			<version>{{%version "antlr4-runtime"%}}</version>
		</dependency> 

		<dependency>
    		<groupId>com.gigaspaces</groupId>
	    	<artifactId>mongo-datasource</artifactId>
    	    <version>{{%version "maven-version" %}}</version>
		</dependency>
		...
	</dependencies>
```

# Setup

An example of how the MongoDB Space Data Source can be configured for a space that loads data back from MongoDB once initialized and 
also asynchronously persists the data using a mirror (see [MongoDB Space Synchronization Endpoint](./mongodb-space-synchronization-endpoint.html))). 

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
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
	http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
	http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
	http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{%currentversion%}}/events/openspaces-events.xsd
	http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-{{%version "spring"%}}.xsd
    http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{%currentversion%}}/remoting/openspaces-remoting.xsd">

	<bean id="propertiesConfigurer"
	class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer" />

	<os-core:embedded-space id="space" name="dataSourceSpace">
		space-data-source="spaceDataSource" mirrored="true" schema="persistent">
		<os-core:properties>
		 <props>
			<!-- Use ALL IN CACHE, put 0 for LRU -->
			<prop key="space-config.engine.cache_policy">1</prop>
			<prop key="cluster-config.cache-loader.central-data-source">true</prop>
			<prop key="cluster-config.mirror-service.supports-partial-update">true</prop>
		 </props>
		</os-core:properties>
	</os-core:embedded-space>
		
	<os-core:giga-space id="gigaSpace" space="space" />
		
	<bean id="mongoClient" class="com.gigaspaces.persistency.MongoClientConnectorBeanFactory">
		<property name="db" value="qadb" />
		<property name="config">
			<bean class="com.mongodb.MongoClient">
				<constructor-arg value="localhost" type="java.lang.String" />
				<constructor-arg value="27017" type="int" />
			</bean>
		</property>
	</bean>
		
	<bean id="spaceDataSource" class="com.gigaspaces.persistency.MongoSpaceDataSourceBeanFactory">
	    <property name="mongoClientConnector" ref="mongoClient" />
	</bean>
			
</beans>
```

{{% /tab %}}
{{%tab "  Code "%}}

```java

	MongoClient config = new MongoClient(host, port);

    MongoClientConnector client = new MongoClientConnectorConfigurer()
            .client(config)
            .db(dbName)
            .create();
    	
	MongoSpaceDataSource spaceDataSource = new MongoSpaceDataSourceConfigurer()
			.mongoClientConnector(client)
			.create();
	
	GigaSpace gigaSpace = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer("space")
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

# Before you begin

Before deploying your Processing Unit, please do the following:

1. Copy the `mongo-datasource.jar` from `lib\optional\datasource\mongo` to `lib\optional\pu-common`.
2. download the following jars and copy them to `lib\optional\pu-common`:

- `antlr-runtime.jar` from [antlr's website](http://www.antlr.org/download.html) .

- `mongo-java-driver-{{%version "mongo-java-driver"%}}.jar` from [mongoDB's website](http://docs.mongodb.org/ecosystem/drivers/java/) .

# MongoSpaceDataSource Properties


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

{{%note "Note:"%}}
Java types Short, Float, BigDecimal and BigInt supported only =,<> queries >,<,>=,<= is not supported.
{{%/note%}}

# Unsupported queries:

- Contains is unsupported


# Mongo As a Service
There are some Mongo DB hosting services that run on the cloud, and you can connect to them from your deployment environment for free. For example: [mongolab](https://mongolab.com)

In order to configure the connection, you would need to connect using a URI that contains the username and password.

```xml
<bean id="mongoClient"
          class="com.gigaspaces.persistency.MongoClientConnectorBeanFactory">
        <property name="db" value="xapdb" />
        <property name="config">
            <bean class="com.mongodb.MongoClient">
               <constructor-arg>
                 <bean class="com.mongodb.MongoClientURI">
                   <constructor-arg value="mongodb://<DB_USERNAME>:<DB_PASSWORD>@ds027017.mongolab.com:27017/xapdb" type="java.lang.String"/>
                 </bean>
               </constructor-arg>
            </bean>
        </property>
    </bean>​
```