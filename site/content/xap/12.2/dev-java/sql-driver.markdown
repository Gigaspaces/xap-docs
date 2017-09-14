---
type: post122
title:  SQL Driver
categories:  XAP122, IEE
weight: 100
---

{{%warning%}}
This page is under construction !
{{%/warning%}}


The JDBC Driver is broadly compatible with the SQL-99 Core specification. It allows database-driven applications to interact with the Space via SQL read queries. 
The driver will make the query optimization if needed and translates the SQL query into Space operations.

{{%note%}}
The JDBC driver was designed to allow only SQL compatible read operations against data stored in the grid. It does not support create/update/delete operations. Furthermore, the driver was not designed for low latency requirements, although it was developed for integration with applications, such as visualization tools, which support JDBC compliant datasources.

If you are looking for low latency read operations [SQLQuery]({{%currentjavaurl%}}/query-sql.html) is a better option.
{{%/note%}} 
 


# Installation

TBD

# Usage

The driver can query either [POJO's]({{%currentjavaurl%}}/pojo-overview.html) or [Space Documents]({{%currentjavaurl%}}/document-overview.html).


## POJO support
 
{{%tabs%}}
{{%tab Person%}}
```java
import java.util.UUID;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class Person {
	private UUID id;
	private String firstName;
	private Integer age;

	public Person() {
	}

	public Person(final UUID id, final String firstName, final int age) {
		this.id = id;
		this.firstName = firstName;
		this.age = age;
	}

	@SpaceId
	public UUID getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(final Integer age) {
		this.age = age;
	}

	public void setId(final UUID id) {
		this.id = id;
	}
}
```
{{%/tab%}}

{{%tab Program%}}
```java
public class JdbcTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String spaceUrl = "/./space";

		// Create the embedded space
		UrlSpaceConfigurer configurer = new UrlSpaceConfigurer(spaceUrl);
		GigaSpace space = new GigaSpaceConfigurer(configurer).gigaSpace();

		// write a couple of Person instances into the space
		space.writeMultiple(new Person[] {
		        new Person(UUID.randomUUID(), "John", 20),
		        new Person(UUID.randomUUID(), "Eric", 20),
		        new Person(UUID.randomUUID(), "Bert", 30)
		});

		// Query the data with jdbc
		Class.forName("com.gigaspaces.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:xap:url=/./space;logLevel=debug");

		String sql = "SELECT e.firstName, e.age FROM Person e WHERE e.age = ?";
		
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, 20);
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();

		while(resultSet.next()) {
		    String firstName = resultSet.getString("firstName");
		    Integer age = resultSet.getInt("age");
		    System.out.println("| " + firstName + "  | " + age + "  |");
		}
	}
}
```
{{%/tab%}}

{{%tab Output%}}
```bash
2017-09-09 16:56:13 INFO  XapSchema - Initializing connection to space /./space
2017-09-09 16:56:13 INFO  XapSchema - Connection to space has been initialized
2017-09-09 16:56:13 DEBUG XapSchema - Looking for XAP tables ...
2017-09-09 16:56:13 DEBUG XapSchema - Found registered types in the space [java.lang.Object, xapsql.sandbox.Person]
2017-09-09 16:56:13 TRACE XapSchema - Found [2] space types
2017-09-09 16:56:13 TRACE XapSchema - Registering table: xapsql.sandbox.Person
2017-09-09 16:56:13 DEBUG XapTable - Looking for 'xapsql.sandbox.Person' table row type
2017-09-09 16:56:13 DEBUG XapTable - 'xapsql.sandbox.Person' table row type is RecordType(JavaType(class java.lang.Integer) age, VARCHAR(65535) firstName, JavaType(class java.util.UUID) id)
2017-09-09 16:56:13 DEBUG XapTable - 'xapsql.sandbox.Person' table routing field is 'id'
2017-09-09 16:56:14 DEBUG FlatQueryExecutor - Executing XAP query: SELECT * FROM xapsql.sandbox.Person WHERE age =  ?  Projection: [firstName, age] Parameters: [20]
| John  | 20  |
| Eric  | 20  |
```
{{%/tab%}}
{{%/tabs%}}

 

## SpaceDocument Support

{{%tabs%}}
{{%tab Program %}}
```java
public class DocumentTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String spaceUrl = "/./space";

		// Create the embedded space
		UrlSpaceConfigurer configurer = new UrlSpaceConfigurer(spaceUrl);
		GigaSpace space = new GigaSpaceConfigurer(configurer).gigaSpace();

		// Register the Type Descriptor
		registerProductType(space);

		DocumentProperties properties = new DocumentProperties()
				.setProperty("CatalogNumber", "av-9876")
				.setProperty("Category", "Aviation")
				.setProperty("Name", "Jet Propelled Pogo Stick")
				.setProperty("Price", 19.99f)
				.setProperty("Tags", new String[] { "New", "Cool", "Pogo", "Jet" })
				.setProperty("Features",
						new DocumentProperties().setProperty("Manufacturer", "Acme")
								.setProperty("RequiresAssembly", true).setProperty("NumberOfParts", 42))
				.setProperty("Reviews",
						new DocumentProperties[] {
								new DocumentProperties().setProperty("Name", "Wile E. Coyote").setProperty("Rate", 1),
								new DocumentProperties().setProperty("Name", "Road Runner").setProperty("Rate", 5) });

		// Create the document using the type name and properties:
		SpaceDocument document = new SpaceDocument("Product", properties);

		space.write(document);

		// Query the data with jdbc
		Class.forName("com.gigaspaces.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:xap:url=/./space;logLevel=trace");

		String sql = "SELECT * FROM Product p";

		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();

		while (resultSet.next()) {
			String category = resultSet.getString("Category");
			System.out.println("Category :" + category);
		}
	}
```
{{%/tab%}}
{{%tab TypeRegister %}}
```java
	static void registerProductType(GigaSpace gigaspace) {
		// Create type descriptor:
		SpaceTypeDescriptor typeDescriptor = new SpaceTypeDescriptorBuilder("Product")
				.idProperty("CatalogNumber")
				.routingProperty("Category")
				.addPropertyIndex("Name", SpaceIndexType.BASIC)
				.addPropertyIndex("Price", SpaceIndexType.EXTENDED).create();
		// Register type:
		gigaspace.getTypeManager().registerTypeDescriptor(typeDescriptor);
	}
```
{{%/tab%}}

{{%tab Output%}}
```
2017-09-09 16:55:32 INFO  XapSchema - Initializing connection to space /./space
2017-09-09 16:55:32 INFO  XapSchema - Connection to space has been initialized
2017-09-09 16:55:33 DEBUG XapSchema - Looking for XAP tables ...
2017-09-09 16:55:33 DEBUG XapSchema - Found registered types in the space [java.lang.Object, Product]
2017-09-09 16:55:33 TRACE XapSchema - Found [2] space types
2017-09-09 16:55:33 TRACE XapSchema - Registering table: Product
2017-09-09 16:55:33 DEBUG XapTable - Looking for 'Product' table row type
2017-09-09 16:55:33 DEBUG XapTable - 'Product' table row type is RecordType(JavaType(class java.lang.Object) CatalogNumber, JavaType(class java.lang.Object) Category)
2017-09-09 16:55:33 DEBUG XapTable - 'Product' table routing field is 'Category'
2017-09-09 16:55:33 DEBUG FlatQueryExecutor - Executing XAP query: SELECT * FROM Product  Projection: null Parameters: null
Category :Aviation
```
{{%/tab%}}
{{%/tabs%}}

{{%refer%}}
For more information about SpaceDocuments see the [Document API]({{%currentjavaurl%}}/document-api.html)
{{%/refer%}}

 

# JDBC URL

In order to connect to the space with JDBC driver you need to specify jdbc connection URL. The general format of the URL is:
`jdbc:xap:url=<space_url>;<url_properties>`

The `space_url` is mandatory. See the Space URL configuration [page]({{%currentjavaurl%}}/the-space-configuration.html). Other parameters are optional.

The following url_properties are available:

| Property                    | Description   | Default value  |
| --------------------------- |:--------------| :--------------|
| user                        | The user for a secured space.          |              | 
| password                    | The password for a secured space.      |              | 
| disableServerSideJoins      | With this parameter all joins will be executed on client side. The data from tables will be loaded taking into account filters and projections.      |     false         |
| preferSpaceIterator         | Use Space Iterator API to execute certain types of queries. Requires less memory on the client side, but may result in slower performance.         |     false         | 
| logLevel                    | Driver log level (client side only). See more in `Logging` section.      |     INFO         |
| log4jFile                   | The path to log4j.properties file. If not provided, the default configuration is used. See more in `Logging` section. ||

Other properties inherited from `Calcite` {{%exurl "jdbc-connect-string-parameters""https://calcite.apache.org/docs/adapter.html#jdbc-connect-string-parameters"%}}

Examples:

Accessing an embedded Space with custom log level:

```bash
jdbc:xap:url=/./mySpace;logLevel=DEBUG
```

Accessing a remote secured Space:

```bash
jdbc:xap:url=jini://LookupServiceHostname/*/mySpace;user=admin;password=admin
```

 

# Explain plan

In order to get the query execution plan you can use `EXPLAIN PLAN FOR` keywords. For example:

{{%tabs%}}
{{%tab "Explain plan"%}}
```java
public class ExplainPan {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String spaceUrl = "/./space";

		// Create the embedded space
		UrlSpaceConfigurer configurer = new UrlSpaceConfigurer(spaceUrl);
		GigaSpace space = new GigaSpaceConfigurer(configurer).gigaSpace();

		// write a couple of Person instances into the space
		space.writeMultiple(new Person[] {
		        new Person(UUID.randomUUID(), "John", 20),
		        new Person(UUID.randomUUID(), "Eric", 20),
		        new Person(UUID.randomUUID(), "Bert", 30)
		});

		// Query the data with jdbc
		Class.forName("com.gigaspaces.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:xap:url=/./space;logLevel=trace");

        // Use the EXPLAIN PLAN
		String sql = "EXPLAIN PLAN FOR SELECT e.firstName, e.age FROM Person e WHERE e.age = ?";
		
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, 20);
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();

		while (resultSet.next()) {
		    System.out.println(resultSet.getString(1));
		}
	}
}
```
{{%/tab%}}
{{%tab "Output"%}}
```bash
2017-09-09 16:57:09 INFO  XapSchema - Initializing connection to space /./space
2017-09-09 16:57:09 INFO  XapSchema - Connection to space has been initialized
2017-09-09 16:57:09 DEBUG XapSchema - Looking for XAP tables ...
2017-09-09 16:57:09 DEBUG XapSchema - Found registered types in the space [java.lang.Object, xapsql.sandbox.Person]
2017-09-09 16:57:09 TRACE XapSchema - Found [2] space types
2017-09-09 16:57:09 TRACE XapSchema - Registering table: xapsql.sandbox.Person
2017-09-09 16:57:09 DEBUG XapTable - Looking for 'xapsql.sandbox.Person' table row type
2017-09-09 16:57:09 DEBUG XapTable - 'xapsql.sandbox.Person' table row type is RecordType(JavaType(class java.lang.Integer) age, VARCHAR(65535) firstName, JavaType(class java.util.UUID) id)
2017-09-09 16:57:09 DEBUG XapTable - 'xapsql.sandbox.Person' table routing field is 'id'
XapToEnumerableConverter
  XapProject(firstName=[$1], age=[$0])
    XapFilter(condition=[=($0, ?0)])
      XapTableScan(table=[[space, Person]])
```
{{%/tab%}}
{{%/tabs%}}

 


# Table mapping

The driver can query either space classes or space documents. In case of the space class, the class package is stripped and only the class name is used. 
If there are several classes with the same name but in different packages in the classpath, the full class name needs to be specified by replacing the package separator (.) with (_).
Example:

```java
String sql = "SELECT e.firstName, e.age FROM xapsql_sandbox_Person e WHERE e.age = ?";
```

In case of space documents the table name is equal to document name. The table name is case sensitive.
<br>

# Indexing 

{{%refer%}}
The performance of queries can be greatly improved by indexing. For more information about indexing see [Indexing]({{%currentjavaurl%}}/indexing-overview.html)
{{%/refer%}}
 
# Logging 

## Client Side logging (Driver)

The logging granularity can be set with the JDBC url parameter. 
The level may be configured with one of the options: TRACE, DEBUG, INFO, WARN, ERROR, ALL or OFF. 
This url parameter configuration does not cover third party libraries logger level.

The default configuration of the driver's logger:

- File appender is used, which writes to the file **<user_home>/xap-jdbc-driver.out**
- Log level is INFO.


## Server side logging (XAP)
In order to change the logging level on the server use the following runtime property:

```bash
com.gigaspaces.jdbc.level = FINE
```

{{%refer%}}
For more information on how to set logging levels see [Logging configuration]({{%currentadmurl%}}/logging.html)
{{%/refer%}}


# The Driver

The driver translates SQL queries into Space API calls. It is important to understand which SQL construction driver convert to which Space calls. 
This will allow you to create more sufficient queries.

There are several types of queries and let's first consider the main types of queries and how they are converted to the native xap interactions:


##  Simple select with filters

Consider the following query: 
```sql
SELECT sales FROM Orders WHERE orderId = 100
```

This query will create an XAP SQLQuery and will push predicate and projection down to the XAP side so only a small result subset will be loaded on the client side.

{{%refer%}}
See more about [SQLQuery]({{%currentjavaurl%}}/query-sql.html)
{{%/refer%}}


## Query with aggregator

Consider the following query: 

```sql
SELECT category, sum(profit) FROM Orders ORDER BY category
```

This query will use the XAP aggregation API and push the aggregation fields and the projections down to data grid. All the calculation will be made on the server side.

{{%refer%}}
See more about [Aggregation API]({{%currentjavaurl%}}/aggregators.html)
{{%/refer%}}



## Join query - Algorithms

There were implemented two join algorithms: `Hash Join` and  `Nested Loop Join`. They use the distributed tasks to execute the join. Considering two join tables, the bigger will be used as `probe` table while the smaller as `build` table.

The `Hash Join` algorithm will be applied in case of equality conjunction (when only equals conditions are used in join predicates) while `Nested Loop Join` algorithm will be applied in other cases.

It is important to note that each partition will load the `build` table from all the cluster and store it in memory. It is worth to mention that node has to have enough free memory while executing not collocated join to hold all the `build` table in memory of the single partition.

To gain the performance of the join queries consider to design your schema to run `Collocated Join`. The benefit of the collocated joins that it does not load the `build` table from all partitions, but only from the current partition. It uses `share nothing` approach. In order to achieve this join condition can only use a routing field.

Be aware that in case of `Nested Loop Join` the linear search will be applied against `build` table for each item in the `probe` table. It could make performance impact.

In order to turn off server side join execution and run all calculation on the client side set `disableServerSideJoins=false` url parameter.


# Limitations 

- The driver was not designed for low latency operations
- The driver allows only read operations
- Embedded objects can not be used in the queries
- Document's dynamic properties can not be used in the queries
