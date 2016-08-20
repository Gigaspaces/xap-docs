---
type: post110
title:  SQLQuery
categories: XAP110
parent: querying-the-space.html
weight: 300
---

{{%ssummary%}}{{%/ssummary%}}


The `SQLQuery` class is used to query the space using SQL-like syntax. The query statement includes only the `WHERE` statement part - the selection aspect of a SQL statement is embedded in other parameters for a SQL query.

{{% refer %}}
For the full documentation of the class's methods and constructors, see [Javadoc](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?com/j_spaces/core/client/SQLQuery.html).
{{% /refer %}}




# Examples

An `SQLQuery` is composed from the **type** of entry to query and an **expression** in a SQL syntax.

For example, suppose we have a class called `MyClass` with an `Integer` property called **num** and a `String` property called **name**:


```java
// Read an entry of type MyClass whose num property is greater than 500:
MyClass result1 = gigaSpace.read(new SQLQuery<MyClass>(MyClass.class, "num > 500"));

// Take an entry of type MyClass whose num property is less than 500:
MyClass result2 = gigaSpace.take(new SQLQuery<MyClass>(MyClass.class, "num < 500"));

MyClass[] results;
// Read all entries of type MyClass whose num is between 1 and 100:
results = gigaSpace.readMultiple(new SQLQuery<MyClass>(MyClass.class, "num >= 1 AND num <= 100"));

// Read all entries of type MyClass who num is between 1 and 100 using BETWEEN syntax:
results = gigaSpace.readMultiple(new SQLQuery<MyClass>(MyClass.class, "num BETWEEN 1 AND 100"));

// Read all entries of type MyClass whose num is either 1, 2, or 3:
results = gigaSpace.readMultiple(new SQLQuery<MyClass>(MyClass.class, "num IN (1,2,3)"));

// Read all entries of type MyClass whose num is greater than 1,
// and order the results by the name property:
results = gigaSpace.readMultiple(new SQLQuery<MyClass>(MyClass.class, "num > 1 ORDER BY name"));
```

{{% refer %}} For an example of `SQLQuery` with `EventSession`, refer to the [Session Based Messaging API](./session-based-messaging-api.html#SQLQuery Template Registration) section.{{% /refer %}}

# Supported Space Operations

The following operations fully support GigaSpaces `SQLQuery`:

- `count`
- `clear`
- `read`, `readIfExists`, `readMultiple`
- `take`, `takeIfExists`, `takeMultiple`



The following operations support`SQLQuery` only with Simple Queries:

- `snapshot`
- `EventSession`
- `GSIterator`





# Supported SQL Features

#### SQLQuery supports the following:

- `AND` / `OR` operators to combine two or more conditions.
- All basic logical operations to create conditions: `=, <>, <, >, >=, <=, like, NOT like, is null, is NOT null, IN`.
- [Aggregate](./aggregators.html) functions: COUNT, MAX, MIN, SUM, AVG
- `BETWEEN`
- `ORDER BY (ASC | DESC)` for multiple properties. Supported only by readMultiple. `ORDER BY` supports also nested object fields. `ORDER BY` supports `NULLS LAST` to control the order behavior of a field when its value is `null`.
- `GROUP BY` - performs DISTINCT on the properties. Supported only by readMultiple. `GROUP BY` supports also nested object fields.
- `sysdate` - current system date and time.
- `rownum` - limits the number of rows to select.
- Sub queries.
- "." used to indicate a double data type.
- [Regular Index](./indexing.html) and a [Compound Index](./indexing.html#Compound Indexing) - Index a single property or multiple properties to improve query execution time.

# Unsupported SQL Features

#### SQLQuery does not support the following:

- Multiple tables select - This is supported with the [JDBC API](./jdbc-driver.html).
- `DISTINCT` - This is supported with the [JDBC API](./jdbc-driver.html).
- The SQL statements: VIEW, TRIGGERS, EXISTS, NOT, CREATE USER, GRANT, REVOKE, SET PASSWORD, CONNECT USER, ON.
- Constraints: NOT NULL, IDENTITY, UNIQUE, PRIMARY KEY, Foreign Key/REFERENCES, NO ACTION, CASCADE, SET NULL, SET DEFAULT, CHECK.
- Set operations: Union, Minus, Union All.
- Advanced Aggregate Functions: STDEV, STDEVP, VAR, VARP, FIRST, LAST. These may be implemented via [Custom Aggregation](./aggregators.html#custom-aggregation).
- Using a non constant right-hand side comparison operator. This can be implemented via [Custom Aggregation](./aggregators.html#custom-aggregation).
- `LEFT OUTER JOIN`
- `RIGHT OUTER JOIN`
- `INNER JOIN`



# Indexing

It is highly recommended to use indexes on relevant properties to increase performance when using equality , bigger / less than , BETWEEN, IN , LIKE , NOT LIKE, IS NULL statements. For more information see [Indexing](./indexing.html). The above supported query features can leverage indexes except for the `is NOT null` and `NOT IN` statement.

# Parameterized Queries

In many cases developers prefer to separate the concrete values from the SQL criteria expression. In GigaSpaces' `SQLQuery` this can be done by placing a **'?'** symbol instead of the actual value in the expression. When executing the query, the conditions that includes **'?'** are replaced with corresponding parameter values supplied via the `setParameter`/`setParameters` methods, or  the `SQLQuery` constructor. For example:


```java
// Option 1 - Use the fluent setParameter(int index, Object value) method:
SQLQuery<MyClass> query1 = new SQLQuery<MyClass>(MyClass.class,"num > ? or num < ? and name = ?")
    .setParameter(1, 2)
    .setParameter(2, 3)
    .setParameter(3, "smith");

// Option 2 - Use the setParameters(Object... parameters) method:
SQLQuery<MyClass> query2 = new SQLQuery<MyClass>(MyClass.class,"num > ? or num < ? and name = ?");
query.setParameters(2, 3, "smith");

// Option 3: Use the constructor to pass the parameters:
SQLQuery<MyClass> query3 = new SQLQuery<MyClass>(MyClass.class,"num > ? or num < ? and name = ?", 2, 3, "smith");
```

{{% info %}} The number of **'?'** symbols in the expression string must match the number of parameters set on the query. For example, when using `IN` condition:
{{%/info%}}


```java
SQLQuery<MyClass> query = new SQLQuery<MyClass>(MyClass.class,"name = ? AND num IN (?,?,?)");
query.setParameters("A", 1, 2, 3);

// Is equivalent to:
SQLQuery<MyClass> query = new SQLQuery<MyClass>(MyClass.class,"name = 'A' AND num IN (1,2,3)");
```


You can use the 'IN' condition with Java's `Collection` or primitive arrays. For example:



```java
Collection<Integer> collection = new HashSet<Integer>();
collection.add(1);
collection.add(2);
collection.add(3);

SQLQuery<MyClass> query = new SQLQuery<MyClass>(MyClass.class,"name = ? AND num IN (?)");
query.setParameter(1,"A");
query.setParameter(2,collection);
```

{{% warning %}}
Parameter assignment to the `SQLQuery` instance is not thread safe. If the query is intended to be executed on multiple threads which may change the parameters, it is recommended to use different `SQLQuery` instances. This has an analogue in JDBC, because `PreparedStatement` is not threadsafe either.
In previous options, parameters could be passed via a POJO template as well. This option is still available, but is deprecated and will be removed in future versions.
{{%/warning%}}




{{% anchor Nested Properties%}}

# Nested Properties

XAP SQL syntax contains various extensions to support matching nested properties, maps, collections and arrays.

Some examples:


```java
// Query for a Person who lives in New York:
... = new SQLQuery<Person>(Person.class, "address.city = 'New York'");
// Query for a Dealer which sales a Honda:
... = new SQLQuery<Dealer>(Dealer.class, "cars[*] = 'Honda'");
```

{{%refer%}}
For more information see [Query Nested Properties](./query-nested-properties.html).
{{%/refer%}}

# Enum Properties

An enum property can be matched either using the enum's instance value or its string representation. For example:


```java
public class Vehicle {
    public enum VehicleType { CAR, BIKE, TRUCK };

    private VehicleType type;
    // Getters and setters are omitted for brevity
}

// Query for vehicles of type CAR using the enum's value:
... = new SQLQuery<Vehicle>(Vehicle.class, "type = ?", VehicleType.CAR);
// Query for vehicles of type CAR using the enum's string representation:
... = new SQLQuery<Vehicle>(Vehicle.class, "type = 'CAR'");
```

{{% info %}} When using an Enum string value, the value must be identical (case sensitive) to the name of the Enum value.{{%/info%}}

# Date Properties

A `Date` property can be matched either using the Date instance value or its string representation. For example:


```java
// Query using a Date instance value:
... = new SQLQuery<MyClass>(MyClass.class, "birthday < ?", new java.util.Date(2020, 11, 20));
// Query using a Date string representation:
... = new SQLQuery<MyClass>(MyClass.class ,"birthday < '2020-12-20'");
```

Specifying date and time values as strings is error prone since it requires configuring the date and time format properties and adhering to the selected format. It is recommended to simply use `Date` instance parameters.

When string representation is required, the following space properties should be used:


```xml
space-config.QueryProcessor.date_format
space-config.QueryProcessor.datetime_format
space-config.QueryProcessor.time_format
```

For example:


```xml
<beans>
    <os-core:embedded-space id="space" name="mySpace">
        <os-core:properties>
            <props>
                <prop key="space-config.QueryProcessor.date_format">yyyy-MM-dd HH:mm:ss</prop>
                <prop key="space-config.QueryProcessor.time_format">HH:mm:ss</prop>
            </props>
        </os-core:properties>
    </os-core:embedded-space>
</beans>
```

These space properties should be configured with a valid Java format pattern as defined in the [official Java language documentation](http://java.sun.com/docs/books/tutorial/i18n/format/simpleDateFormat.html).

{{% info%}} The `space-config.QueryProcessor.date_format` property used when your query include a String representing the date
Date properties are often used for comparison (greater/less than). Consider using [extended indexing](./indexing.html) to boost performance.
{{%/info%}}

### sysdate

The `sysdate` value is evaluated differently when using the JDBC API vs when using it with `SQLQuery` API. When used with JDBC API it is evaluated using the space clock. When used with `SQLQuery` API it is evaluated using the client clock. If you have a partitioned space across multiple different machines and the clock across these machines is not synchronized you might not get the desired results. If you use JDBC API you should consider setting the date value as part of the SQL within the client side (since you  might write objects using the GigaSpace API). In this case , you should synchronize all the client machine time. In short - all the machines (client and server) clocks should be synchronized.

- On windows there is a [windows service](http://technet.microsoft.com/en-us/library/cc773061%28WS.10%29.aspx) that deals with clock synchronization.
- On Linux there is a [daemon service](http://www.brennan.id.au/09-Network_Time_Protocol.html#starting) that deals with clock synchronization.

{{% tip %}}
Internally dates are stored as a **TimeStamp**. This means that both time (hour/min/sec) and date (year/month/day) information are available for date range queries.
{{% /tip %}}


# Java 8 Dates

XAP supports the `LocalDate`, `LocalTime` and `LocalDateTime` classes. The following Space properties need to be defined in order to use the classes in queries:


```xml
	<os-core:embedded-space id="space" name="sandboxSpace">
		<os-core:properties>
			<props>
				<prop key="space-config.QueryProcessor.date_format">yyyy-MM-dd HH:mm:ss</prop>
				<prop key="space-config.QueryProcessor.time_format">HH:mm:ss</prop>
				<prop key="space-config.QueryProcessor.datetime_format">yyyy-MM-dd HH:mm:ss</prop>
			</props>
		</os-core:properties>
	</os-core:embedded-space>
```

Here are examples on how to use the Java8 dates:

{{%accordion%}}

{{%accord title="LocalDate"%}}

{{%tabs%}}
{{%tab " LocalDatePojo"%}}

```java
public class LocalDatePojo {
	private LocalDate myData;
	private Integer id = null;

	public LocalDatePojo() {
	}

	public LocalDate getMyDate() {
		return myData;
	}

	public void setMyDate(LocalDate date) {
		this.myData = date;
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

```
{{%/tab%}}

{{%tab " Program"%}}

```java
public void testLocalDate() {
	LocalDate d = LocalDate.now();

	LocalDatePojo pojo = new LocalDatePojo();
	pojo.setId(new Integer(1));
	pojo.setMyDate(d);
	dateSpace.write(pojo);

	DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern("yyyy-MM-dd HH:mm:ss");
	String inAnHourDate = formatter.format(LocalDateTime.now().plusDays(1));

	SQLQuery<LocalDatePojo> q = new SQLQuery<LocalDatePojo>(
				LocalDatePojo.class, "myDate < '" + inAnHourDate + "' ");
	pojo = dateSpace.read(q);
}
```
{{%/tab%}}
{{%/tabs%}}

{{%/accord%}}

{{%accord title="LocalTime"%}}

{{%tabs%}}
{{%tab " LocalTimePojo"%}}

```java
public class LocalTimePojo {
	private LocalTime myTime;
	private Integer id = null;

	public LocalTimePojo() {
	}

	public LocalTime getMyTime() {
		return myTime;
	}

	public void setMyTime(LocalTime myTime) {
		this.myTime = myTime;
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
```
{{%/tab%}}

{{%tab " Program"%}}

```java
	public void testLocalTime() {
		LocalTime t = LocalTime.now();

		LocalTimePojo pojo = new LocalTimePojo();
		pojo.setId(new Integer(1));
		pojo.setMyTime(t);
		dateSpace.write(pojo);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String inAnHourDate = formatter.format(LocalDateTime.now().plusMinutes(
				10));

		SQLQuery<LocalTimePojo> q = new SQLQuery<LocalTimePojo>(
				LocalTimePojo.class, "myTime < '" + inAnHourDate + "' ");
		pojo = dateSpace.read(q);
	}
```
{{%/tab%}}
{{%/tabs%}}

{{%/accord%}}

{{%accord title="LocalDateTime"%}}

{{%tabs%}}
{{%tab " LocalDateTimePojo"%}}

```java
public class LocalDateTimePojo {
	private LocalDateTime myData;
	private Integer id = null;

	public LocalDateTimePojo() {
	}

	public LocalDateTime getMyDate() {
		return myData;
	}

	public void setMyDate(LocalDateTime date) {
		this.myData = date;
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
```
{{%/tab%}}

{{%tab " Program"%}}

```java
public void testLocalDateTime() {
	LocalDateTime d = LocalDateTime.now();

	LocalDateTimePojo pojo = new LocalDateTimePojo();
	pojo.setMyDate(d);
	pojo.setId(new Integer(1));
	dateSpace.write(pojo);

	DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern("yyyy-MM-dd HH:mm:ss");
	String inAnHourDate = formatter.format(LocalDateTime.now()
				.minusMinutes(10));

	SQLQuery<LocalDateTimePojo> q = new SQLQuery<LocalDateTimePojo>(
				LocalDateTimePojo.class, "myDate > '" + inAnHourDate + "' ");
	pojo = dateSpace.read(q);
 }
```
{{%/tab%}}
{{%/tabs%}}
{{%/accord%}}

{{%/accordion%}}


{{%warning%}}
Java 8's LocalDate, LocalTime, LocalDateTime are currently not interoperable with the .NET DateTime class. [.NET Interoperability]({{%currentneturl%}}/dotnet-java-interoperability.html#supported-types-for-matching-and-interoperability)
{{%/warning%}}

# Aggregators

{{%section%}}
{{%column width="60%" %}}
[Aggregators](./aggregators.html) allows you to perform aggregations (Average , Max , Min , Sum , Group By , Having) on a relatively large space objects data set. A query (SQLQuery or template) may be specified to determine the exact subset of space objects to iterate while performing the aggregation. Aggregators support single and compound based execution and a fully customized Aggregation.

{{%/column%}}
{{%column width="40%" %}}
![aggreg.jpg](/attachment_files/built-in-Compound-aggregators.jpg)
{{%/column%}}
{{%/section%}}

# Blocking Operations

Blocking operations (i.e. `read` or `take` with `timeout` greater than `0`) are supported with the following restrictions:

- Blocking operations on a partitioned space require a routing value (broadcast is not supported). For more information see [Routing](#Routing).
- Blocking operations on complex queries are not supported. For more information see [Simple Queries](#SimpleQueries) definition.


```java
long timeout = 100000;
MyClass result = space.take(new SQLQuery<MyClass>(MyClass.class ,"num > 500"), timeout);
```

{{%anchor routing%}}

# Routing

When running on a partitioned space, it is important to understand how routing is determined for SQL queries.

If the routing property is part of the criteria expression with an equality operand and without ORs, its value is used for routing.

For example, suppose the routing property of `MyClass` is `num`:


```java
// Execute query on partition #1
SQLQuery<MyClass> query1 = new SQLQuery<MyClass>(MyClass.class,"num = 1");

// Execute query on all partitions - no way to tell which partitions hold matching results:
SQLQuery<MyClass> query2 = new SQLQuery<MyClass>(MyClass.class,"num > 1");

// Execute query on all partitions - no way to tell which partitions hold matching results:
SQLQuery<MyClass> query3 = new SQLQuery<MyClass>(MyClass.class,"num = 1 OR name='smith'");
```

Note that in `query1` the `num` property is used both for routing and matching.

In some scenarios we may want to execute the query on a specific partition without matching the routing property (e.g. blocking operation). This can be done via the `setRouting` method:


```java
SQLQuery<MyClass> query = new SQLQuery<MyClass>(MyClass.class,"num > 3");
query.setRouting(1);
MyClass[] result = gigaspace.readMultiple(query);
```


# Best Practice

#### Compound Index

When having an **AND** query or a template that use two or more fields for matching a [Compound Index](./indexing.html#Compound Indexing) may boost the query execution time. The Compound Index should be defined on multiple properties for a specific space class and will be used implicitly when a SQL Query or a [Template](./query-template-matching.html) will be using these properties.

#### Re-using SQLQuery

Constructing an `SQLQuery` instance is a relatively expensive operation. When possible, prefer using `SQLQuery.setParameters` and `SQLQuery.setParameter` to modify an existing query instead of creating a new one. However, remember that `SQLQuery` is not thread-safe.
XAP reuses `SQLQuery` objects by using a bounded cache mechanism - when using `SQLQuery.setParameter` as descbired above,  the queries will be fetched from the cache without the penalty of recreation `SQLQuery` objects.
The cache size can be modified by setting `com.gs.queryCache.cacheSize` system property to the desirable value (the default value is 1,000).

#### Minimize OR usage

When using the `OR` logical operator together with `AND` logical operator as part of your query you can speed up the query execution by minimizing the number of `OR` conditions in the query. For example:


```java
(A = 'X' OR A = 'Y') AND (B > '2000-10-1' AND B < '2003-11-1')
```

would be executed much faster when changing it to be:


```java
(A = 'X' AND B > '2000-10-1' AND B < '2003-11-1')
OR
(A = 'Y' AND B > '2000-10-1' AND B < '2003-11-1')
```

# Projecting Partial Results

You can specify that the `SQLQuery` should contain only partial results which means that the returned object should only be populated with the projected properties.

{{% refer %}}For details on how to use the projection API please refer to [Getting Partial Results Using Projection API](./query-partial-results.html){{% /refer %}}




{{% anchor SimpleQueries %}}

# Simple vs. Complex Queries

Most space operations and features support any SQL query, but some support only **simple** queries and not **complex** ones.

{{%vbar%}}
A query is considered complex if it contains one or more of the following:
- `GROUP BY`
- `ORDER BY`
- Sub queries

The following features support only simple SQL queries

- Snapshot
- Blocking operations
- [Notifications](./session-based-messaging-api.html)
- [GSIterator](./query-paging-support.html)
{{%/vbar%}}

# Interface Classes

`SQLQuery` supports concrete classes, derived classes and abstract classes. Interface classes are **not supported**.

# Reserved Words

The following are reserved keywords in the GigaSpaces SQL syntax:

{{%vbar "Reserved words "%}}
alter add all and asc avg between by create call drop desc bit tinyint
 	 end from group in is like rlike max min not null or distinct
 	 order select substr sum sysdate upper where count delete varchar2 char
 	 exception rownum index insert into set table to_char to_number smallint
 	 update union values commit rollback uid using as date datetime time
 	 float real double number decimal numeric boolean integer
 	 varchar bigint long clob blob lob true false int timestamp longvarchar
{{% /vbar %}}

If a reserved word needs to be used as a property name it needs to be escaped using ``.
For example: if you need to query a property by the name of count, which is a reserved word, it can be done as following:


```java
new SQLQuery<MyData>(MyData.class, "`count` = 5")
```

{{%vbar "Reserved Separators and Operators:"%}}
:= || ; . ROWTYPE ~ < <= >  >= => != <> \(+\) ( ) \* / + - ? \{ \}
{{% /vbar %}}


