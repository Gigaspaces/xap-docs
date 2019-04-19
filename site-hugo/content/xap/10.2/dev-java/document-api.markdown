---
type: post102
title:  Document API
categories: XAP102
parent: document-overview.html
weight: 100
canonical: auto
---


{{% ssummary %}}{{% /ssummary %}}


Unlike POJOs, which force users to design a fixed data schema (in the form of a class definition) and adhere to it, a document is much more dynamic - users can add and remove properties at runtime as necessary. A Document always belongs to a certain type, represented by the class `SpaceTypeDescriptor`.

Before a certain Document instance is written to the space, its type should be introduced to it. The type has a name and controls metadata such as identifier property, routing property and which properties are initially indexed (naturally, you can also index new properties at runtime after adding them to your documents).

{{% tip %}}
The Type controls **metadata** - so only the metadata is part of the type. A document can introduce new properties at will.
{{% /tip %}}

Note that the Document type does not describe the properties themselves (except for the names of the ID and Routing properties). These are completely dynamic and each instance can have a different set of properties (although in most cases Document instances of the same type are likely to have identical or similar set of properties).



# Schema Evolution with Space Documents

Since a `SpaceDocument` is completely dynamic by nature, it is very easy to change or evolve your data model without ever taking down the Space. Simply change your application code to add additional properties or remove existing ones, and you're good to go. Even better, old and new versions can co-exist since the space does not enforce any restriction with regards to the property set of documents that belong to a certain type. This is a much more lightweight model in comparison to the classic POJO model, where a recompilation and in many cases a full space restart is required to change the data schema.

If POJO model cannot be replaced with Document model, yet some level of schema evolution is desired within the POJO model, [Dynamic Properties](./dynamic-properties.html) can be used.

# Type Definition

Before we start writing and reading `SpaceDocument` from the space, we need an **initial** schema definition of the document type.

For example, suppose we're implementing an electronic commerce system, and decided we need a type called **Product** with the following properties:

- CatalogNumber : String
- Category : String
- Name : String
- Description : String
- Price : float
- Features : Nested document (for example: Manufacturer=Acme, RequiresAssembly=false, weight=7.5)
- Tags : Collection of Strings
- Reviews : Collection of nested documents

We also decide that **CatalogNumber** will be a primary key, partitioning will be done by the *Category* property, and the properties **Name**, **Price** should be indexed since they participate in most of the queries executed. Remember, the type definition is for metadata only, so we're not concerned about **Description** and other such fields in the type definition, because Description isn't used for indexing or any other metadata.

The following is an example of how to introduce a new document type:

{{%tabs%}}
{{%tab "  Spring Namespace Configuration "%}}


```xml
<os-core:embedded-space id="space" name="mySpace"  >
      <os-core:space-type type-name="Product" >
		<os-core:id property="CatalogNumber"/>
		<os-core:routing property="Category"/>
		<os-core:basic-index path="Name"/>
		<os-core:extended-index path="Price"/>
      </os-core:space-type>
</os-core:embedded-space>
<os-core:giga-space id="gigaSpace" space="space"/>

```

{{% /tab %}}
{{%tab "  Plain Spring XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="spaceTypes" >
   	      <list>
   		<ref bean="productType"/>
   	      </list>
     </property>
</bean>
<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space"/>
</bean>

<bean name="productType"
        class="org.openspaces.core.config.GigaSpaceDocumentTypeDescriptorFactoryBean">
        <property name="typeName" value="Product"/>
        <property name="idProperty">
           <bean class="org.openspaces.core.config.SpaceIdProperty">
	         <property name="propertyName" value="CatalogNumber"></property>

           </bean>
        </property>
        <property name="routingProperty">
           <bean class="org.openspaces.core.config.SpaceRoutingProperty">
           	 <property name="propertyName" value="Category"></property>
           </bean>
        </property>
        <property name="indexes">
             <list>
   		 <bean class="org.openspaces.core.config.BasicIndex">
           	       <property name="path" value="Name"></property>
                 </bean>
                 <bean class="org.openspaces.core.config.ExtendedIndex">
           	       <property name="path" value="Price"></property>
                 </bean>
   	      </list>
        </property>
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
public void registerProductType(GigaSpace gigaspace) {
    // Create type descriptor:
    SpaceTypeDescriptor typeDescriptor = new SpaceTypeDescriptorBuilder("Product")
        .idProperty("CatalogNumber")
        .routingProperty("Category")
        .addPropertyIndex("Name", SpaceIndexType.BASIC)
        .addPropertyIndex("Price", SpaceIndexType.EXTENDED)
        .create();
    // Register type:
    gigaspace.getTypeManager().registerTypeDescriptor(typeDescriptor);
}
```

{{% /tab %}}
{{% /tabs %}}

Note that this code does not reflect the complete model - most of the properties does not need to be introduced to the schema. Only properties with special roles (ID, Routing) are part of the schema definition. These meta model **settings cannot be changed** without restarting the space or dropping the type, clearing all its instances and reintroducing it again.

# Creating and Writing Documents

To create a document create a `Map<String,Object>` with the requested properties, create a `SpaceDocument` object using the type name and properties, and write it to the space using the regular `GigaSpace` write method:


```java
public void writeProduct1(GigaSpace gigaspace) {
    // 1. Create the properties:
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put("CatalogNumber", "hw-1234");
    properties.put("Category", "Hardware");
    properties.put("Name", "Anvil");
    properties.put("Price", 9.99f);
    properties.put("Tags", new String[] {"heavy", "anvil"});

    Map<String, Object> features = new HashMap<String, Object>();
    features.put("Manufacturer", "Acme");
    features.put("RequiresAssembly", false);
    features.put("Weight", 100);
    properties.put("Features", features);

    Map<String, Object> review1 = new HashMap<String, Object>();
    review1.put("Name", "Wile E. Coyote");
    review1.put("Rate", 1);
    review1.put("Comments", "Don't drop this on your toe, it will hurt.");
    Map<String, Object> review2 = new HashMap<String, Object>();
    review2.put("Name", "Road Runner");
    review2.put("Rate", 5);
    review2.put("Comments", "Beep Beep!");
    properties.put("Reviews", new Map[] {review1, review2});

    // 2. Create the document using the type name and properties:
    SpaceDocument document = new SpaceDocument("Product", properties);
    // 3. Write the document to the space:
    gigaspace.write(document);
}
```

Another way is to use the `DocumentProperties` class provided, which extends HashMap to provide fluent coding:


```java
public void writeProduct2(GigaSpace gigaspace) {
    // 1. Create the properties:
    DocumentProperties properties = new DocumentProperties()
        .setProperty("CatalogNumber", "av-9876")
        .setProperty("Category", "Aviation")
        .setProperty("Name", "Jet Propelled Pogo Stick")
        .setProperty("Price", 19.99f)
        .setProperty("Tags", new String[] {"New", "Cool", "Pogo", "Jet"})
        .setProperty("Features", new DocumentProperties()
            .setProperty("Manufacturer", "Acme")
            .setProperty("RequiresAssembly", true)
            .setProperty("NumberOfParts", 42))
        .setProperty("Reviews", new DocumentProperties[] {
            new DocumentProperties()
                .setProperty("Name", "Wile E. Coyote")
                .setProperty("Rate", 1),
            new DocumentProperties()
                .setProperty("Name", "Road Runner")
                .setProperty("Rate", 5)});

    // 2. Create the document using the type name and properties:
    SpaceDocument document = new SpaceDocument("Product", properties);
    // 3. Write the document to the space:
    gigaspace.write(document);
}
```

You can map JSON to and from a SpaceDocument with any parser. Here is an example:
 
	          
```java
package xap.sandbox.document;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.EmbeddedSpaceConfigurer;

import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.metadata.SpaceTypeDescriptor;
import com.gigaspaces.metadata.SpaceTypeDescriptorBuilder;
import com.gigaspaces.metadata.index.SpaceIndexType;
import com.j_spaces.core.client.SQLQuery;

public class ConvertJSONTODocument {

	@SuppressWarnings("unchecked")
	public static void main (String[] args) throws JsonGenerationException, JsonMappingException, IOException{
		
		GigaSpace gigaSpace = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer("mySpace")).gigaSpace();
		
		/****
	     * We Need to Register a type by specifying a name, id, and routing field.
	     * Routing is used to partition data across the grid, similar to a shard key 
	     * This only needs to be done once 
	     */
	    final String PRODUCT_TYPE_NAME = "Product";

	    SpaceTypeDescriptor typeDescriptor = new SpaceTypeDescriptorBuilder(PRODUCT_TYPE_NAME)
	                .idProperty("id")
	                .routingProperty("location")
	                .addPropertyIndex("processed", SpaceIndexType.BASIC)
	                .create();

	    gigaSpace.getTypeManager().registerTypeDescriptor(typeDescriptor);

	    /****
	     * Example JSON payload containing the properties 
	     * of the new Type. In this example we use jaxson object mapper.
	     * You can use any parser you would like
	     */
	    String jsonPayload = "{\"id\":1, \"location\":\"usa\", \"processed\":false}";

		HashMap<String,Object> jsonProperties =
	            new ObjectMapper().readValue(jsonPayload, HashMap.class);

	    /****
	     * Convert to a space document simply pass the hash map to the 
	     * in to the SpaceDocument Constructor along with the document type name
	     * from above
	     */
	    SpaceDocument dataAsDocument = new SpaceDocument(PRODUCT_TYPE_NAME, jsonProperties);

	    /***
	     * Insert to the grid
	     */
	    gigaSpace.write(dataAsDocument);

	    /***
	     * To confirm the result. Read the document from the grid
	     */
	    SpaceDocument dataAsDocumentFromGrid = gigaSpace.read(new SQLQuery<SpaceDocument>(PRODUCT_TYPE_NAME, "id = ?", 1));

	    /***
	     * Map the Object back to JSON
	     */
	    String jsonFromGrid = new ObjectMapper().writeValueAsString(dataAsDocumentFromGrid);
	    
	    System.out.println(jsonFromGrid);
	}
}
```



{{% note %}}
- The `GigaSpace.writeMultiple` method can be used to write a batch of documents.
- Update semantics are the same as POJO, except **partial update** that is not currently supported.
- Use only alphanumeric characters (a-z, A-Z, 0-9) and the underscore ('_') to construct properties keys. Other characters might have a special behavior in GigaSpaces (for example: the dot ('.') is used to distinguish nested paths).
{{%/note%}}

# Reading and Removing Documents

There are three types of document queries:

## Template Query

This type of query uses a SpaceDocument with _type_ and any other set of properties values as a template for the query
For example: Read a document of type **Product** whose **Name** is **Anvil**:


```java
public SpaceDocument readProductByTemplate(GigaSpace gigaSpace) {
    // Create template:
    SpaceDocument template = new SpaceDocument("Product");
    template.setProperty("Name", "Anvil");
    // Read:
    SpaceDocument result = gigaSpace.read(template);
    return result;
}
```

## SQL Query

You can use the [SQLQuery](././query-sql.html) to search for matching `SpaceDocument` entries.
For example: Read a document of type **Product** whose **Price** is greater than 15:


```java
public SpaceDocument readProductBySQL(GigaSpace gigaSpace) {
    // Create query:
    SQLQuery<SpaceDocument> query =
        new SQLQuery<SpaceDocument>("Product", "Price > ?");
    query.setParameter(1, 15f);
    // Read:
    SpaceDocument result = gigaSpace.read(query);
    return result;
}
```

{{% tip %}}
Consider indexing properties used in queries to boost performance.
{{% /tip %}}

Queries on nested properties are supported. For example, to read products manufactured by **Acme**:


```java
public SpaceDocument[] readProductBySQLNested(GigaSpace gigaSpace) {
    // Create query:
    SQLQuery<SpaceDocument> query =
        new SQLQuery<SpaceDocument>("Product", "Features.Manufacturer = ?");
    query.setParameter(1, "Acme");
    // Read:
    SpaceDocument[] result = gigaSpace.readMultiple(query, 10);
    return result;
}
```

## ID Based Query

For example: Read a document of type **Product** whose ID is **hw-1234**:


```java
public SpaceDocument readProductById(GigaSpace gigaSpace) {
    return gigaSpace.readById(new IdQuery<SpaceDocument>("Product", "hw-1234"));
}
```

Queries by multiple Ids are supported. For example:


```java
public SpaceDocument[] readProductByMultipleIds(GigaSpace gigaSpace) {
    Object[] ids = new Object[] {"hw-1234", "av-9876"};
    ReadByIdsResult<SpaceDocument> result =
        gigaSpace.readByIds(new IdsQuery<SpaceDocument>("Product", ids));
    return result.getResultsArray();
}
```

{{%tip%}}
- All other `GigaSpace` query operations (readIfExists, readMultiple, take, takeIfExists, takeMultiple, count, clear) are supported for documents entries as well.
- All other Id based operations (readIfExists, takeById, takeIfExistsById, takeByIds) are supported for documents as well.
- All overloads of those operations with timeout, transactions, modifiers etc. are supported for documents. The semantics is similar to POJOs.
{{%/tip%}}

# Nested Properties

The `Document` properties values can be either scalars (integers, strings, enumerations, etc), collections (arrays, lists), or nested properties (Map or an extension of map, such as `DocumentProperties`). Values must adhere to the same restrictions as in the POJO model (e.g. be serializable). Nested properties can be queried by using the dot ('.') notation to describe paths, as shown above.

{{% note %}} It's highly recommended to use `DocumentProperties` for nested documents since it contains performance and memory footprint optimizations which are tailored for GigaSpaces usage.

- While it's possible to use  `SpaceDocument` as a property, it is probably a mistake, since it contains extra information which is not relevant for nested properties (type name, version, etc.).

- Changing nested properties in an embedded space is not safe.
{{%/note%}}

# Document Hierarchy

SpaceDocument query supports hierarchical relationships so that entries of a child are visible in the context of the parent document, but not the other way around. For example, a document with name `Employee`   can register its parent document `Person` in the following way:


```java
SpaceTypeDescriptor employeeDescriptor = new SpaceTypeDescriptorBuilder(
				"Child Document Type Name", parentSpaceTypeDescriptor).create();
```

Here is an example:

{{%tabs%}}
{{%tab " Program"%}}

```java
	public static void main(String[] args) {

		// Create the Space
		GigaSpace space = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer(
				"mySpace")).gigaSpace();

		registerDocument(space);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("Id", "1234");
		properties.put("FirstName", "John");
		properties.put("LastName", "Fellner");

		SpaceDocument document1 = new SpaceDocument("Person", properties);

		space.write(document1);

		properties = new HashMap<String, Object>();
		properties.put("Id", "12345");
		properties.put("FirstName", "John");
		properties.put("LastName", "Walters");
		properties.put("employeeId", "1234");

		SpaceDocument document2 = new SpaceDocument("Employee", properties);

		space.write(document2);

		SQLQuery<SpaceDocument> query1 = new SQLQuery<SpaceDocument>(
				"Person", "");

		SpaceDocument[] result = space.readMultiple(query1);

		// You should see two documents
		System.out.println(result.length);

		SQLQuery<SpaceDocument> query2 = new SQLQuery<SpaceDocument>(
				"Employee", "");

		SpaceDocument[] result2 = space.readMultiple(query2);

		// You should see one document
		System.out.println(result2.length);

		System.exit(1);

	}
```
{{%/tab%}}

{{%tab " RegisterDocument"%}}

```java
	static public void registerDocument(GigaSpace space) {
		SpaceTypeDescriptor personDescriptor = new SpaceTypeDescriptorBuilder(
				"Person").idProperty("Id").create();
		// Register type:
		space.getTypeManager().registerTypeDescriptor(personDescriptor);

		SpaceTypeDescriptor employeeDescriptor = new SpaceTypeDescriptorBuilder(
				"Employee", personDescriptor).create();
		// Register type:
		space.getTypeManager().registerTypeDescriptor(employeeDescriptor);
	}
```
{{%/tab%}}
{{%/tabs%}}


# Indexing

Properties and nested paths can be [indexed](./indexing.html) to boost queries performance. In the type registration sample above the **Name** and **Price** properties are indexed.

Since the schema is flexible and new properties might be added after the type has been registered, it is possible to add indexes dynamically as well.

{{%refer%}}
For more information about indexing, see the [Indexing](./indexing.html) page.
{{%/refer%}}

# Events

Event containers (both [polling container](./polling-container.html) and [notify container](./notify-container.html)) support Space `Document` entries.

Here is a simple example of a polling event container configuration using a `Document`:

{{%tabs%}}
{{%tab "  Annotation "%}}


```xml
<!-- Enable scan for OpenSpaces and Spring components -->
<context:component-scan base-package="com.mycompany"/>

<!-- Enable support for @Polling annotation -->
<os-events:annotation-support />

<os-core:embedded-space id="space" name="mySpace">
      <os-core:space-type type-name="Product" >
		<os-core:id property="CatalogNumber"/>
		<os-core:routing property="Category"/>
		<os-core:basic-index path="Name"/>
		<os-core:extended-index path="Price"/>
      </os-core:space-type>
</os-core:embedded-space>

<os-core:giga-space id="gigaSpace" space="space"/>
```


```java
@EventDriven @Polling
public class SimpleListener {

    @EventTemplate
    SpaceDocument unprocessedData() {
        SpaceDocument template = new SpaceDocument("Product");
        template.setProperty("Name","Anvil");
        return template;
    }

    @SpaceDataEvent
    public SpaceDocument eventListener(SpaceDocument event) {
        //process Data here
    }
}
```

{{% /tab %}}
{{%tab "  Namespace "%}}


```xml
<os-core:embedded-space id="space" name="mySpace">
  <os-core:space-type type-name="Product" >
		<os-core:id property="CatalogNumber"/>
		<os-core:routing property="Category"/>
		<os-core:basic-index path="Name"/>
		<os-core:extended-index path="Price"/>
      </os-core:space-type>
</os-core:embedded-space>

<os-core:giga-space id="gigaSpace" space="space"/>

<bean id="simpleListener" class="SimpleListener" />

<os-events:polling-container id="eventContainer" giga-space="gigaSpace">

    <os-core:template>
         <bean class="com.gigaspaces.document.SpaceDocument">
                <constructor-arg value="Product"/>
                <constructor-arg type="java.util.Map">
                    <map>
                        <entry key="Name" value="Anvil" />
                    </map>
                </constructor-arg>
         </bean>
    </os-core:template>

    <os-events:listener>
        <os-events:annotation-adapter>
            <os-events:delegate ref="simpleListener"/>
        </os-events:annotation-adapter>
    </os-events:listener>
</os-events:polling-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="simpleListener" class="SimpleListener" />

<bean id="eventContainer"
    class="org.openspaces.events.polling.SimplePollingEventListenerContainer">

    <property name="gigaSpace" ref="gigaSpace" />

    <property name="template">
        <bean class="com.gigaspaces.document.SpaceDocument">
                <constructor-arg value="Product"/>
                <constructor-arg type="java.util.Map">
                    <map>
                        <entry key="Name" value="Anvil" />
                    </map>
                </constructor-arg>
         </bean>
    </property>

    <property name="eventListener">
    	<bean class="org.openspaces.events.adapter.AnnotationEventListenerAdapter">
    	    <property name="delegate" ref="simpleListener" />
    	</bean>
    </property>
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java

GigaSpace gigaSpace = // either create the GigaSpace or get it by injection

SpaceDocument template = new SpaceDocument("Product");
template.setProperty("Name","Anvil");
SimplePollingEventListenerContainer pollingEventListenerContainer = new SimplePollingContainerConfigurer(gigaSpace)
                .template(template)
                .eventListenerAnnotation(new Object() {
                    @SpaceDataEvent
                    public void eventHappened() {
                        eventCalled.set(true);
                    }
                }).pollingContainer();

// when needed close the polling container
pollingEventListenerContainer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

# FIFO

[FIFO support](./fifo-support.html) is off by default with `Document` entries (same as with POJO). To enable FIFO support, modify the type introduction code and set the desired FIFO support mode. For example:

{{%tabs%}}
{{%tab "  Spring Namespace Configuration "%}}


```xml
<os-core:embedded-space id="space" name="mySpace">
    <os-core:space-type type-name="Product" fifo-support="OPERATION" >
		<!-- other properties definition -->
    </os-core:space-type>
</os-core:embedded-space>
<os-core:giga-space id="gigaSpace" space="space"/>
```

{{% /tab %}}
{{%tab "  Plain Spring XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="spaceTypes" >
   	      <list>
   		<ref bean="productType"/>
   	      </list>
    </property>
</bean>
<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space"/>
</bean>

<bean name="productType"
        class="org.openspaces.core.config.GigaSpaceDocumentTypeDescriptorFactoryBean">
        <property name="typeName" value="Product"/>
        <!-- other properties definition -->
        <property name="fifoSupport" value="OPERATION"/>

</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
// Create type descriptor:
SpaceTypeDescriptor typeDescriptor = new SpaceTypeDescriptorBuilder("Product")
    // Other type descriptor settings.
    .fifoSupport(FifoSupport.OPERATION)
    .create();
// Register type:
gigaspace.getTypeManager().registerTypeDescriptor(typeDescriptor);
```

{{% /tab %}}
{{% /tabs %}}

{{% note %}} Changing FIFO support after a type has been registered is not supported.{{%/note%}}

{{%refer%}}
For more information about FIFO, see the [FIFO Support](./fifo-support.html) page.
{{%/refer%}}

# Transactions and Optimistic Locking

Transactions and isolation modifiers semantics is identical to the POJO semantics. For more information about transactions, see the [Transaction Management](./transaction-management.html) page.

Optimistic locking is disabled by default with `Document` entries (same as with POJO). To enable it, modify the type introduction code and set the optimistic locking support. For example:

{{%tabs%}}
{{%tab "  Spring Namespace Configuration "%}}


```xml
<os-core:embedded-space id="space" name="mySpace">
      <os-core:space-type type-name="Product" optimistic-lock="true" >
		<!-- other properties definition -->
      </os-core:space-type>
</os-core:embedded-space>
<os-core:giga-space id="gigaSpace" space="space"/>
```

{{% /tab %}}
{{%tab "  Plain Spring XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="spaceTypes" >
   	      <list>
   		<ref bean="productType"/>
   	      </list>
    </property>
</bean>
<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space"/>
</bean>

<bean name="productType"
        class="org.openspaces.core.config.GigaSpaceDocumentTypeDescriptorFactoryBean">
        <property name="typeName" value="Product"/>

        <!-- other properties definition -->
        <property name="optimisticLock" value="true"/>
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
// Create type descriptor:
SpaceTypeDescriptor typeDescriptor = new SpaceTypeDescriptorBuilder("Product")
    // Other type descriptor settings.
    .supportsOptimisticLocking(true)
    .create();
// Register type:
gigaspace.getTypeManager().registerTypeDescriptor(typeDescriptor);
```

{{% /tab %}}
{{% /tabs %}}

{{% note %}} Changing optimistic locking after a type has been registered is not supported. {{%/note%}}

{{%refer%}}
For more information about optimistic locking, see the [Optimistic Locking](./transaction-optimistic-locking.html) page.
{{%/refer%}}

# Local Cache / Local View

[Local View](./local-view.html) and [Local Cache](./local-cache.html) are supported for Documents. By default, the `SpaceDocument` instance is stored in the cache which speeds up query performance since the data does not need to be transformed from internal structure to `SpaceDocument`.

If you intend to use local cache or local view in a mixed POJO-Document environment, please refer to [Document-POJO Interoperability](./document-pojo-interoperability.html).

# Persistency

External Data Source is supported for space documents.
Example on how to implement an EDS that persists SpaceDocuments of type "Trade":

{{%tabs%}}
{{%tab "  Configuration "%}}


```xml
<bean id="documentDataSource" class="com.test.DocumentEDS"/>

<os-core:embedded-space id="space" name="mySpace" schema="persistent" external-data-source="documentDataSource">
    <os-core:space-type type-name="Trade" >
	   <os-core:id property="uid" auto-generate="true"/>
	   <os-core:routing property="symbolLabel"/>
    </os-core:space-type>
    <os-core:properties>
        <props>
            <prop key="space-config.external-data-source.data-class">com.gigaspaces.document.SpaceDocument</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

{{% /tab %}}

{{%tab "  The EDS Implementation "%}}


```java
package com.test;

public class DocumentEDS
        implements ManagedDataSource<SpaceDocument>, BulkDataPersister
{

    public void init(Properties prop) throws DataSourceException
    {
        // initialize persistency layer
    }

    public DataIterator<SpaceDocument> initialLoad() throws DataSourceException
    {
        // load all the data from persistency
        // build and return an iterator of documents
    }

    public void executeBulk(List<BulkItem> bulk) throws DataSourceException
    {
        for (BulkItem bulkItem : bulk)
        {
            SpaceDocument document = (SpaceDocument) bulkItem.getItem();

            switch (bulkItem.getOperation())
            {
                case BulkItem.WRITE:

                   // writeDocument(document);
                    break;
                case BulkItem.UPDATE:

                   // updateDocument(document, bulkItem.getIdPropertyName());
                    break;
                case BulkItem.REMOVE:
                    //removeDocument(document, bulkItem.getIdPropertyName());

                    break;

                default:
                    break;
            }
        }
    }

    public void shutdown() throws DataSourceException
    {
        //cleanup resources and close the persistency
    }

}
```

{{% /tab %}}
{{% /tabs %}}

Different document database can be used to implement the document persistency - MongoDB, CouchDB and others.
Pojos can be persisted via document EDS as well, in the same way.

{{% note %}}
- In order to support initialLoad of documents the relevant types must be declared in the "space" bean, so that they are registered in the space before initialLoad is invoked.
- Document persistence is currently not provided by default - If needed, the External Data Source should be implemented to fit the required solution.
{{%/note%}}


## Transient Document


When using a persistent space, there are situations where not all SpaceDocuments need to be persisted. You can specify the document to be transient by invoking the `setTransient()` method.

```java
   SpaceDocument doc = new SpaceDocument("Entity");
   ......		
   doc.setTransient(true);
```


# Space Filters

Space Filter are supported for space documents.

{{%refer%}}
If you intend to use space filters in a mixed POJO-Document environment, please refer to [Document-POJO Interoperability](./document-pojo-interoperability.html).
{{%/refer%}}

# Space Replication Filters

Space Replication Filter are supported for space documents.

{{%refer%}}
If you intend to use space replication filters in a mixed POJO-Document environment, please refer to [Document-POJO Interoperability](./document-pojo-interoperability.html).
{{%/refer%}}
