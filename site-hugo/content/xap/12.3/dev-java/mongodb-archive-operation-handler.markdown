---
type: post123
title:  Archive Handler
categories: XAP123, OSS
parent: mongodb.html
weight: 200
canonical: auto
---


{{%imagertext "/attachment_files/archive-container-mongodb.jpg"%}}
The [Archive Container](./archive-container.html) can be configured to work against MongoDB (without writing any extra code). The [ArchiveOperationHandler interface]({{% api-javadoc %}}/org/openspaces/archive/ArchiveOperationHandler.html) abstracts the Big-Data storage from the [Archive Container](./archive-container.html). The MongoDB Archive Operation Handler implements this interface by serializing space objects into MongoDB.
{{%/imagertext%}}



# Library dependencies

The MongoDB Archive Operation Handler uses the [MongoDB driver](http://docs.mongodb.org/ecosystem/drivers/java/) for communicating with the MongoDB cluster.
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
			<version>{{%version "mongo-java-driver"%}}</version>
		</dependency>

		<dependency> 
			<groupId>org.antlr</groupId> 
			<artifactId>antlr4-runtime</artifactId> 
			<version>4.0</version> 
		</dependency> 

		<dependency>
    		<groupId>org.gigaspaces</groupId>
	    	<artifactId>xap-mongodb</artifactId>
    		<version>{{%version "maven-version" %}}</version>
		</dependency>
		...
	</dependencies>
```



# Setup

{{%tabs%}}


{{%tab "  Plain XML "%}}


```xml
	<bean id="mongoArchiveHandler" class="com.gigaspaces.persistency.archive.MongoArchiveOperationHandler">
		<property name="gigaSpace" ref="gigaSpace" />
		<property name="config" ref="config" />
		<property name="db" value="${mongodb.db}" />
	</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
	ArchiveOperationHandler mongoArchiveHandler =
		new MongoArchiveOperationHandlerConfigurer()
		 .gigaSpace(gigaSpace)
		 .config(config)
		 .db("mydb")
		 .create();

	// To free the resources used by the archive container make sure you close it properly.
	// A good life cycle event to place the destroy() call would be within the @PreDestroy or 	DisposableBean#destroy() method.

	archiveContainer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

# MongoArchiveOperationHandler Properties


|Property|Description|
|:-------|:----------|
|gigaSpace| GigaSpace reference used for type descriptors. see [Archive Container](./archive-container.html#Configuration)|
|config | MongoClientConfiguration reference used to handle the mongodb driver configuration. see [MongoClient](http://api.mongodb.org/java/2.11.2/com/mongodb/MongoClient.html)|
|db | mongodb database name|


