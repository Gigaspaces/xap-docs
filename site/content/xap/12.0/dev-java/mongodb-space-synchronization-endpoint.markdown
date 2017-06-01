---
type: post120
title:  Synchronization Endpoint
categories: XAP120
parent: mongodb-space-persistency.html
weight: 200
---

A MongoDB based implementation of the [Space Synchronization Endpoint](./space-synchronization-endpoint-api.html). 

# Library dependencies
The MongoDB Space Synchronization Endpoint uses the [MongoDB Driver](http://docs.mongodb.org/ecosystem/drivers/java/) For communicating with the MongoDB cluster. 
Include the following in your `pom.xml` 


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
			<version>{{%version mongo-java-driver%}}</version>
		</dependency>

		<dependency> 
			<groupId>org.antlr</groupId> 
			<artifactId>antlr4-runtime</artifactId> 
			<version>{{%version "antlr4-runtime"%}}</version>
		</dependency> 

		<dependency>
    		<groupId>org.gigaspaces</groupId>
	    	<artifactId>xap-mongodb</artifactId>
    		<version>{{%version "maven-version" %}}</version>
    		<scope>provided</scope>
		</dependency>
		...
	</dependencies>
```

# Setup

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
		xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
		http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
		http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{%currentversion%}}/events/openspaces-events.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-{{%version "spring"%}}.xsd
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
			<property name="db" value="qadb" />
			<property name="config">
				<bean class="com.mongodb.MongoClient">
					<constructor-arg value="localhost" type="java.lang.String" />
					<constructor-arg value="27017" type="int" />
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
		MongoClient config = new MongoClient(host, port);
		
		MongoClientConnector client = new MongoClientConnectorConfigurer()
				.client(config)
				.db(dbName)
				.create();	
		
		MongoSpaceSynchronizationEndpoint syncEndpoint = new MongoSpaceSynchronizationEndpointConfigurer() 
				.mongoClientConnector(client) 
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

# Before you begin

Before deploying your Processing Unit, please do the following:

1. Copy the `xap-mongodb.jar` from `lib\optional\mongodb` to `lib\optional\pu-common`.
2. download the following jars and copy them to `lib\optional\pu-common`:

- `antlr-runtime.jar` from [antlr's website](http://www.antlr.org/download.html) .

- `mongo-java-driver-{{%version mongo-java-driver%}}.jar` from [mongoDB's website](http://docs.mongodb.org/ecosystem/drivers/java/) .

## MongoSpaceSynchronizationEndpoint Properties


|Property|Description|
|:-------|:----------|
|client|A configured com.gigaspaces.persistency.MongoClientConnector bean. Must be configured.| 

# Considerations
- Change API and Partial updates is supported
