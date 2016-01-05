---
type: post97
title:  Synchronization Endpoint
categories: XAP97
parent: mongodb-space-persistency.html
weight: 200
---

A MongoDB based implementation of the [Space Synchronization Endpoint](./space-synchronization-endpoint-api.html). 

### Library dependencies 
The MongoDB Space Synchronization Endpoint uses the [MongoDB Driver](http://www.allanbank.com/mongodb-async-driver/index.html) For communicating with the MongoDB cluster. 
Include the following in your `pom.xml` 


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

<dependeny> 
	<groupId>org.antlr</groupId> 
	<artifactId>antlr4-runtime</artifactId> 
	<version>4.0</version> 
</dependency> 

<dependency>
    <groupId>com.gigaspaces</groupId>
    <artifactId>mongo-datasource</artifactId>
    <version>9.7.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>

```

### Setup 

An example of how the MongoDB Space Synchronization Endpoint can be configured within a mirror. 

{{%tabs%}}
{{%tab "  Spring "%}}


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

<os-core:mirror id="mirror" url="/./mirror-service"
	space-sync-endpoint="spaceSynchronizationEndpoint" operation-grouping="group-by-replication-bulk">
	<os-core:source-space name="dataSourceSpace" partitions="2"
		backups="1" />
</os-core:mirror>

<bean id="mongoClient"
		class="com.gigaspaces.persistency.MongoClientConnectorBeanFactory">
		<property name="db" value="${mongo.db}" />
		<property name="config">
			<bean class="com.allanbank.mongodb.MongoClientConfiguration">
				<constructor-arg value="mongodb://${mongo.user}:${mongo.password}@${mongo.host}:${mongo.port}/${mongo.db}"
					type="java.lang.String" />
				<property name="defaultDurability" value="ACK" />
			</bean>
		</property>
</bean>

<bean id="spaceSynchronizationEndpoint"
	class="com.gigaspaces.persistency.MongoSpaceSynchronizationEndpointBeanFactory">
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

MongoSpaceSynchronizationEndpoint syncEndpoint = new MongoSpaceSynchronizationEndpointConfigurer() 
		.mongoClientConnector(client) 
		.create(); 

IJSpace mirror = new UrlSpaceConfigurer("/./mirror-service") 
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

### Before you begin

Before deploying your Processing Unit, please do the following:

1. Copy the `mongo-datasource.jar` from `lib\optional\datasource\mongo` to `lib\optional\pu-common`.
2. download the following jars and copy them to `lib\optional\pu-common`:

	- `antlr-runtime.jar` from [antlr's website](http://www.antlr.org/download.html) .

	- `mongo-java-driver-2.9.3.jar` from [mongoDB's website](http://docs.mongodb.org/ecosystem/drivers/java/) .

	- `mongodb-async-driver-1.2.3.jar` from [allanbank's website](http://www.allanbank.com/mongodb-async-driver/download.html) .

	- `guava-r08.jar` from [Guava-project's website](https://code.google.com/p/guava-libraries/wiki/Release08) . **NOTE:** you must download Guava's release 08, and extract the `guava-r08.jar` from within the `guava-r08.zip` that you have downloaded

## `MongoSpaceSynchronizationEndpoint` Properties


|Property|Description|
|:-------|:----------|
|client|A configured com.gigaspaces.persistency.MongoClientConnector bean. Must be configured.| 

## Considerations 
- Change API and Partial updates is supported
