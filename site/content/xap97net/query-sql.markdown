---
type: post97
title:  SqlQuery
categories: XAP97NET
parent: querying-the-space.html
weight: 500
---

{{%ssummary%}}{{%/ssummary%}}

The `SqlQuery` class is used to query the space using SQL-like syntax. This query can be passed as a template to the Read, Take, ReadMultiple, TakeMultiple, Count and Clear operations, as well as a template when registering for notification.


# Usage

An `SqlQuery` is composed from the **type** of entry to query, an **expression** in a SQL syntax and optionally one or more **parameters** which provide values for the expression.

#### Example 1

Read all entries of type **Person** whose **Age** is greater than or equal to **21**.


```csharp
Person[] persons = proxy.ReadMultiple<Person>(new SqlQuery<Person>("Age >= 21"));
```

Note that the expression is equivalent to the WHERE part of a query. The FROM part is derived from the generic argument of the `SqlQuery` class, and the SELECT part is not needed since the result is a fully-formed object.

#### Example 2

Read up to **100** entries of type **Person** whose **Age** is greater than or equal to **21**, AND whose **FirstName** is **John**.


```csharp
SqlQuery<Person> query = new SqlQuery<Person>("Age >= ? AND FirstName = ?");
query.SetParameter(1, 21);
query.SetParameter(2, "John");
Person[] persons = proxy.ReadMultiple<Person>(query, 100);
```

This time instead of specifying the values directly in the expression we've used question marks to denote expression variables and parameters to specify the values for those variables.

# Supported SQL Features

{{%vbar "SqlQuery supports the following:"%}}

- `AND` / `OR` operators to combine two or more conditions.
- All basic logical operations to create conditions: `=, <>, <,>, >=, <=, like, NOT like, is null, is NOT null, IN`.
- `BETWEEN` (starting 8.0.1)
- `ORDER BY (ASC | DESC)` for multiple PONO properties. Supported only by readMultiple. `ORDER BY` supports also nested object fields.
- `GROUP BY` - performs DISTINCT on the PONO properties. Supported only by readMultiple. `GROUP BY` supports also nested object fields.
- `sysdate` - current system date and time.
- `rownum` - limits the number of rows to select.
- Sub queries.
{{%/vbar%}}



# Indexing

It is highly recommended to use indexes on relevant properties to increase performance when using equality , bigger / less than , BETWEEN, IN , LIKE , NOT LIKE, IS NULL statements. For more information see [Indexing](./indexing.html). The above supported query features can leverage indexes except for the `is NOT null` and `NOT IN` statement.


# Blocking Operations

Blocking operations (i.e. `Read` or `Take` with `timeout` greater than `0`) are supported with the following restrictions:

- Blocking operations on a partitioned space require a routing value (broadcast is not supported). For more information see [Routing](#Routing).
- Blocking operations on complex queries are not supported. For more information see [Simple Queries](#SimpleQueries) definition.


```csharp
long timeout = 100000;
MyClass result = space.Take<MyClass>(new SQLQuery<MyClass>("Num > 500"), timeout);
```

# Routing

When running on a partitioned space, it is important to understand how routing is determined for SQL queries. Routing is how the partitioned space determines on which partition a given data element is stored.

If the routing property is part of the criteria expression with an equality operand and without ORs, its value is used for routing.

For example, suppose the routing property of `MyClass` is `Num`:


```csharp
// Execute query on partition #1
SQLQuery<MyClass> query1 = new SQLQuery<MyClass>("Num = 1");

// Execute query on all partitions -
// no way to tell which partitions hold matching results:
SQLQuery<MyClass> query2 = new SQLQuery<MyClass>("Num > 1");

// Execute query on all partitions -
// no way to tell which partitions hold matching results:
SQLQuery<MyClass> query3 = new SQLQuery<MyClass>("Num = 1 OR Name='smith'");
```

Note that in `query1` the `Num` property is used both for routing and matching.

In some scenarios we may want to execute the query on a specific partition without matching the routing property (e.g. blocking operation). Starting 8.0.1, this can be done via the `Routing` property:


```csharp
SQLQuery<MyClass> query = new SQLQuery<MyClass>("Num > 3");
query.Routing = 1;
MyClass[] result = space.ReadMultiple<MyClass>(query);
```

# Regular Expressions

You can query the space using the SQL `like` operator or [Java Regular Expression](http://docs.oracle.com/javase/1.5.0/docs/api/java/util/regex/Pattern.html) Query syntax.

When using the SQL `like` operator you may use the following:
`%` - match any string of any length (including zero length)
`_` - match on a single character


```csharp
SqlQuery<MyClass> query = new SqlQuery<MyClass>("Name like 'A%'")
```

Querying the space using the Java Regular Expression provides more options than the SQL `like` operator. The Query syntax is done using the `rlike` operator:


```csharp
// Match all entries of type MyClass that have a name that starts with a or c:
SqlQuery<MyClass> query = new SqlQuery<MyClass>("Name rlike '(a|c).*'");
```

All the supported methods and options above are relevant also for using `rlike` with `SqlQuery`.

{{% note %}} `like` and `rlike` queries are not using indexed data, hence executing such may be relatively time consuming compared to other queries that do leverage indexed data. This means the space engine iterate the potential candidate list to find matching object using the Java regular expression utilizes. A machine using 3GHz CPU may iterate 100,000-200,000 objects per second when executing regular expression query. To speed up `like` and `rlike` queries make sure your query leveraging also at least one indexed field to minimize the candidate list. Running multiple partitions will also speed up the query execution since this will allow the system to iterate over the potential matching objects in a parallel manner across the different partitions.
{{%/note%}}

# Free Text Search

Free text search required almost with every application. Users placing some free text into a form and later the system allows users to search for records that includes one or more words within a free text field. A simple way to enable such fast search without using regular expression query that my have some overhead can be done using the [Collection Indexing](./indexing.html#Collection Indexing), having an array or a collection of String values used for the query. Once the query is executed the SQL Query should use the searched words as usual. See example below:

Our Space class includes the following - note the **words** and the **freeText** fields:


```csharp
public class MyData {

    [@SpaceIndex (path="[*]")]
	private String[] Words {set; get;}

	private String FreeText{set; get;}

	public String[] GetWords() {
		return Words;
	}

	public void SetWords(String[] ws) {
		this.Words=ws;
	}

	public String getFreeText() {
		return FreeText;
	}

	public void setFreeText(String freeText) {
		this.FreeText = freeText;
		this.Words = FreeText.Split(" ");
	}
}
```

{{% note %}} Note how the **freeText** field is broken into the **words** array before placed into the indexed field.
{{%/note%}}

You may write the data into the space using the following:


```csharp
MyData data = new MyData(...);
data.FreeText(freetext);
proxy.Write(data);
```

You can query for objects having the word **hello** as part of the freeText field using the following:


```csharp
MyData results[] = proxy.ReadMultiple<MyData>(new SqlQuery<MyData>("Words[*]='hello'"));
```

You can also execute the following to search for object having the within the freeText field the word **hello** or **everyone**:


```csharp
MyData results[] = proxy.ReadMultiple(new SqlQuery<MyData>("Words[*]='hello' OR Words[*]='everyone')");
```

With the above approach you avoid the overhead with regular expression queries.

{{% tip %}}
The same approach can be implemented also with the [SpaceDocument](./document-api.html).
{{% /tip %}}


# Case Insensitive Query

Implementing case insensitive queries can be done via:

- `like` operator or `rlike` operator. Relatively slow. Not recommended when having large amount of objects.
- Store the data in lower case and query on via lower case String value (or upper case)

# Limitations

### Enums

Since Enums are stored in the space as their underlying primitive type, they must be explicitly cast to that primitive type to be used with SqlQuery.

{{% anchor SimpleQueries %}}

### Simple vs. Complex Queries

Most space operations and features support any SQL query, but some support only **simple** queries and not **complex** ones.

A query is considered complex if it contains one or more of the following:

- `NOT LIKE`
- `GROUP BY`
- `ORDER BY`
- Subqueries
- `OR` (before 8.0.1)

The following features support only simple SQL queries

- Snapshot
- Blocking operations
- Notifications
- SpaceIterator

### Unsupported SQL Features



{{%vbar "SqlQuery does not support the following:"%}}
- Aggregate functions: COUNT, MAX, MIN, SUM, AVG.
- Multiple tables select.
- `DISTINCT`
- The SQL statements: HAVING, VIEW, TRIGGERS, EXISTS, BETWEEN, NOT, CREATE USER, GRANT, REVOKE, SET PASSWORD, CONNECT USER, ON.
- Constraints: NOT NULL, IDENTITY, UNIQUE, PRIMARY KEY, Foreign Key/REFERENCES, NO ACTION, CASCADE, SET NULL, SET DEFAULT, CHECK.
- Set operations: Union, Minus, Union All.
- Advanced Aggregate Functions: STDEV, STDEVP, VAR, VARP, FIRST, LAST.
- Mathematical expressions.
- `LEFT OUTER JOIN`
- `RIGHT OUTER JOIN`
- `INNER JOIN`
{{%/vbar%}}

### Reserved Words



{{%vbar "The following are reserved keywords in the GigaSpaces SQL syntax:"%}}
alter add all and asc avg between by create call drop desc bit tinyint
 	 end from group in is like rlike max min not null or distinct
 	 order select substr sum sysdate upper where count delete varchar2 char
 	 exception rownum index insert into set table to_char to_number smallint
 	 update union values commit rollback uid using as date datetime time
 	 float real double number decimal numeric boolean integer
 	 varchar bigint long clob blob lob true false int timestamp longvarchar
{{%/vbar%}}

### Reserved Separators and Operators:



{{%vbar "Reserved syntax:"%}}
:= || ; . ROWTYPE ~ < <= >  >= => != <> \(+\) ( ) \* / + - ? \{ \}
{{%/vbar%}}
