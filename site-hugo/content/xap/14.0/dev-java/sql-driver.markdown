---
type: post140
title:  SQL Driver
categories:  XAP122, IEE
parent: sql-query-intro.html
weight: 100
---

 

The JDBC Driver is broadly compatible with the SQL-99 Core specification. It allows database-driven applications to interact with the Space via SQL read queries. The driver performs query optimization if needed and translates the SQL query into Space operations.
 

# Installation

Include all the JAR files from the `<XAP-HOME>\insightedge\lib\jdbc` and `<XAP-HOME>\lib\required` directories in your project.


# Usage

The JDBC driver can query either [POJOs](./pojo-overview.html) or [Space Documents](./document-overview.html).


## POJO Support
 
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
		Connection connection = DriverManager.getConnection("jdbc:insightedge:url=/./space;logLevel=debug");

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
2018-04-15 09:45:54,832 [main] INFO  - Initializing connection to space jini://*/*/space
2018-04-15 09:45:54,872 [main] INFO  - Connection to space has been initialized
2018-04-15 09:45:54,902 [main] DEBUG - Creating prepared statement for query: SELECT e.firstName, e.age FROM Person e WHERE e.age = ?
2018-04-15 09:45:55,439 [main] DEBUG - Looking for XAP tables ...
2018-04-15 09:45:55,464 [main] DEBUG - Found registered types in the space [java.lang.Object, Person]
2018-04-15 09:45:55,465 [main] TRACE - Found [2] space types
2018-04-15 09:45:55,465 [main] TRACE - Registering table: Person
2018-04-15 09:45:55,466 [main] DEBUG - Looking for 'Person' table row type
2018-04-15 09:45:55,493 [main] DEBUG - 'Person' table row type is RecordType(JavaType(class java.lang.Integer) age, VARCHAR(65535) firstName, JavaType(class java.util.UUID) id)
2018-04-15 09:45:55,493 [main] DEBUG - 'Person' table routing field is 'id'
2018-04-15 09:45:56,150 [main] DEBUG - Executing XAP query: SELECT * FROM Person WHERE age =  ?  Projection: [firstName, age] Parameters: [20]
| John  | 20  |
| Eric  | 20  |
```
{{%/tab%}}
{{%/tabs%}}


## Space Document Support

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
		Connection connection = DriverManager.getConnection("jdbc:insightedge:url=/./space;logLevel=trace");

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
				.addPropertyIndex("Name", SpaceIndexType.EQUAL)
				.addPropertyIndex("Price", SpaceIndexType.ORDERED).create();
		// Register type:
		gigaspace.getTypeManager().registerTypeDescriptor(typeDescriptor);
	}
```
{{%/tab%}}

{{%tab Output%}}
```
2018-04-15 09:52:36,584 [main] INFO  - Initializing connection to space /./space
2018-04-15 09:52:36,589 [main] INFO  - Connection to space has been initialized
2018-04-15 09:52:36,626 [main] DEBUG - Creating prepared statement for query: SELECT * FROM Product p
2018-04-15 09:52:37,166 [main] DEBUG - Looking for XAP tables ...
2018-04-15 09:52:37,167 [main] DEBUG - Found registered types in the space [java.lang.Object, Product]
2018-04-15 09:52:37,168 [main] TRACE - Found [2] space types
2018-04-15 09:52:37,168 [main] TRACE - Registering table: Product
2018-04-15 09:52:37,170 [main] DEBUG - Looking for 'Product' table row type
2018-04-15 09:52:37,179 [main] DEBUG - 'Product' table row type is RecordType(JavaType(class java.lang.Object) CatalogNumber, JavaType(class java.lang.Object) Category)
2018-04-15 09:52:37,179 [main] DEBUG - 'Product' table routing field is 'Category'
2018-04-15 09:52:37,624 [main] DEBUG - Executing XAP query: SELECT * FROM Product  Projection: null Parameters: null
Category :Aviation
```
{{%/tab%}}
{{%/tabs%}}

{{%refer%}}
For more information about Space documents, refer to the [Document API](./document-api.html) page.
{{%/refer%}}


# JDBC URL

In order to connect to the Space with JDBC driver, you need to specify the JDBC connection URL. The general format of the URL is:
`jdbc:insightedge:url=<space_url>;<url_properties>`

The `space_url` is mandatory. See the [Space URL configuration](./the-space-configuration.html) page. Other parameters are optional.

The following `url_properties` are available:

| Property                    | Description   | Default value  |
| --------------------------- |:--------------| :--------------|
| user                        | The user for a secured Space.          |              | 
| password                    | The password for a secured Space.      |              | 
| disableServerSideJoins      | With this parameter, all joins are executed on the client side. The data from tables are loaded taking into account filters and projections.      |     false         |
| preferSpaceIterator         | Use the Space Iterator API to execute certain types of queries. Requires less memory on the client side, but may result in slower performance.         |     false         | 
| logLevel                    | Driver log level (client side only). For more information, see the [Logging](../admin/logging-overview.html) topic.      |     INFO         |
| log4jFile                   | The path to log4j.properties file. If not provided, the default configuration is used. For more information, see the [Logging](../admin/logging-overview.html) topic. ||
| autoCommit                  | Queries are auto-commited to the databas.e | false |

Other properties inherited from `Calcite` {{%exurl "jdbc-connect-string-parameters""https://calcite.apache.org/docs/adapter.html#jdbc-connect-string-parameters"%}}

Examples:

Accessing an embedded Space with a custom log level:

```bash
jdbc:insightedge:url=/./mySpace;logLevel=DEBUG
```

Accessing a remote secured Space:

```bash
jdbc:insightedge:url=jini://LookupServiceHostname/*/mySpace;user=admin;password=admin
```

# Explain Plan

In order to get the query execution plan, you can use `EXPLAIN PLAN FOR` keywords. For example:

{{%tabs%}}
{{%tab "Explain plan"%}}
```java
public class ExplainPlan {

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
		Connection connection = DriverManager.getConnection("jdbc:insightedge:url=/./space;logLevel=trace");

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
2018-04-15 09:56:21,616 [main] INFO  - Initializing connection to space /./space
2018-04-15 09:56:21,618 [main] INFO  - Connection to space has been initialized
2018-04-15 09:56:21,657 [main] DEBUG - Creating prepared statement for query: EXPLAIN PLAN FOR SELECT e.firstName, e.age FROM Person e WHERE e.age = ?
2018-04-15 09:56:22,213 [main] DEBUG - Looking for XAP tables ...
2018-04-15 09:56:22,213 [main] DEBUG - Found registered types in the space [java.lang.Object, Person]
2018-04-15 09:56:22,213 [main] TRACE - Found [2] space types
2018-04-15 09:56:22,213 [main] TRACE - Registering table: Person
2018-04-15 09:56:22,214 [main] DEBUG - Looking for 'Person' table row type
2018-04-15 09:56:22,219 [main] DEBUG - 'Person' table row type is RecordType(JavaType(class java.lang.Integer) age, VARCHAR(65535) firstName, JavaType(class java.util.UUID) id)
2018-04-15 09:56:22,219 [main] DEBUG - 'Person' table routing field is 'id'
XapToEnumerableConverter
  XapProject(firstName=[$1], age=[$0])
    XapFilter(condition=[=($0, ?0)])
      XapTableScan(table=[[space, Person]])
```
{{%/tab%}}
{{%/tabs%}}


# Table Mapping

The JDBC driver can query either Space classes or Space documents. When querying the Space class, the class package is stripped and only the class name is used. If there are several classes with the same name but in different packages in the classpath, the full class name needs to be specified by replacing the package separator (.) with (_).

Example:

```java
String sql = "SELECT e.firstName, e.age FROM xapsql_sandbox_Person e WHERE e.age = ?";
```

When querying Space documents, the table name is equal to document name. The table name is case sensitive.
<br>

# Indexing 

{{%refer%}}
Query performance can be greatly improved by indexing. For more information about indexing, refer to the [Indexing](./indexing-overview.html) section.
{{%/refer%}}
 
# Logging 

## Client-Side logging (Driver)

The logging granularity can be set using the JDBC URL parameter. The level can be configured with one of the following options: TRACE, DEBUG, INFO, WARN, ERROR, ALL or OFF. 

This URL parameter configuration does not apply to the logging level for third-party libraries.

The default configuration of the JDBC driver's logger:

- File appender is used, which writes to the file **&lt;user_home&gt;/xap-jdbc-driver.out**
- Log level is INFO.


## Server-Side Logging (XAP)

In order to change the logging level on the server, use the following runtime property:

```bash
com.gigaspaces.jdbc.level = FINE
```

{{%refer%}}
For more information on how to set logging levels, refer to the [Logging](../admin/logging-overview.html) section of the Administration guide.
{{%/refer%}}

# The Driver

The driver translates SQL queries into Space API calls. It is important to understand which SQL construction driver converts to which Space calls. 
This will allow you to create more sufficient queries.

There are several types of queries. We can first consider the main types of queries and how they are converted into native XAP interactions:


##  Simple Select with Filters

Consider the following query:
 
```sql
SELECT sales FROM Orders WHERE orderId = 100
```

This query creates a data grid SQLQuery, and pushes the predicate and projection down to the data grid side so only a small result subset is loaded on the client side.

{{%refer%}}
For more information about SQL support in the data grid, refer to the [SQL Query](./query-sql.html) page.
{{%/refer%}}


## Query with Aggregator

Consider the following query: 

```sql
SELECT category, sum(profit) FROM Orders ORDER BY category
```

This query uses the XAP aggregation API, and pushes the aggregation fields and the projections down to the data grid. All the calculations are performed on the server side.

{{%refer%}}
For more information about the XAP aggregation API, refer to the [Aggregation API](./aggregators.html) page.
{{%/refer%}}



## Join Query Algorithms

The JDBC driver has two join algorithms, `Hash Join` and  `Nested Loop Join`. They use the distributed tasks to execute the join. Considering two join tables, the larger table is used as the `probe` table, while the smaller is used as the `build` table.

The `Hash Join` algorithm is applied for equality conjunctions (when only equal conditions are used in the join predicates), while the `Nested Loop Join` algorithm is applied in other cases.

Each partition loads the `build` table from the entire cluster and stores it in memory. The node must have enough free memory while executing a non-collocated join to hold the entire `build` table in the memory of the single partition.

To improve the performance of the join queries, consider designing your schema to run `Collocated Join`. The benefit of the collocated join is that it doesn't load the `build` table from all partitions, but only from the current partition. This join uses the `share nothing` approach. In order to achieve this join condition, you can only use a routing field.

When using a `Nested Loop Join`, the linear search is applied against the `build` table for each item in the `probe` table. This my affect performance.

In order to turn off the server side join execution and run all calculations on the client side, set the `disableServerSideJoins=false` url parameter.


# Limitations 

- The JDBC driver was not designed for low latency operations.
- The JDBC driver allows only read operations.
- Embedded objects can't be used in the queries.
- Document's dynamic properties cann't be used in the queries.
