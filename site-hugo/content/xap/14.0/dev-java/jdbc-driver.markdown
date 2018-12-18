---
type: post140
title:  JDBC Driver
categories: XAP140, OSS
parent: other-data-access-apis.html
weight: 100
---


The XAP JDBC interface allows database-driven applications to interact with Spaces via SQL queries and commands. A query processor transparently translates SQL queries into legal Space operations. No integration is required - all you need to do is point the application to the XAP JDBC driver like any other JDBC driver.

{{%note "Note"%}}
This JDBC driver is not SQL-99 compliant. SQL-99 compliant query functionality is available in the InsightEdge Enterprise edition. Refer to the SQLQuery and [JDBC Driver](../dev-java/sql-query-intro.html) topics in the Developer Guide. 
{{%/note%}}


Applications can access the XAP data grid using the JDBC API; data written to the IMDG using the JDBC API can also be accessed using other APIs.

{{% note "Note" %}}
An alternative way of querying the Space using SQL syntax is the [SQLQuery](./query-sql.html) class. This class allows you to perform SQL queries directly against Space objects, without adding O/R mapping complexity.
{{% /note %}}





JDBC support in XAP is centered around the Space-Based Architecture - its main motivation is to enable more sophisticated querying of the Space, beyond the template matching provided by the [The GigaSpace Interface](./the-gigaspace-interface-overview.html).

XAP is not a full-fledged relational database and it does not support the full SQL92 standard (see [JDBC Supported Features](#supported-features)). However, the existing SQL support is extremely useful for applications that need to execute queries on a Space for real-time queries.

{{% note "Info" %}}
You can use the [SQL Command Line](../admin/space-gigaspaces-cli.html) to query and fetch data from the IMDG. The SQL Command Line using the XAP JDBC Driver when accessing the IMDG.
{{% /note %}}

## Using Existing SQL Code and Porting to External Systems

The JDBC interface is mostly used to enable access to the Space through standard SQL tools and programming interfaces. You can write SQL commands against the Space, and the same code will in many (simple) cases be compatible with other SQL implementations.

Porting existing JDBC code to the Space is certainly doable (but would require some level of adaptation depending on the specifics of the case and the complexity of the SQL queries). For legacy applications, it may still be easier than porting existing code to leverage the space technology directly. The SQL support is limited, therefore this path should be taken with caution.

# Getting the XAP JDBC Connection

In order to get the JDBC connection you should use the following JDBC Driver classname:


```java
Class.forName("com.j_spaces.jdbc.driver.GDriver");
```

The connection URL should include :`jdbc:gigaspaces:url:<Space URL>` -- e.g.:


```java
Connection con = DriverManager.getConnection("jdbc:gigaspaces:url:jini://*/*/mySpace");
```
 

{{% note "Note" %}}
For more details on the Space URL, refer to the [Space URL](./the-space-configuration.html) page.
{{% /note %}}

Example:


```java
Connection conn;
Class.forName("com.j_spaces.jdbc.driver.GDriver").newInstance();
String url = "jdbc:gigaspaces:url:jini://*/*/mySpace";
conn = DriverManager.getConnection(url);
Statement st = conn.createStatement();
String createTable = "CREATE TABLE COFFEES (COF_NAME VARCHAR(32) INDEX,SUP_ID INTEGER INDEX, " +
                     "PRICE FLOAT INDEX,SALES INTEGER INDEX,TOTAL INTEGER)";
st.executeUpdate(createTable);

String query = "SELECT COF_NAME, PRICE FROM COFFEES";
st = conn.createStatement();
ResultSet rs = st.executeQuery(query);
while (rs.next()) {
	String s = rs.getString("COF_NAME");
	float n = rs.getFloat("PRICE");
	System.out.println(s + "   " + n);
}
```

{{% note "Note"%}}
There is no need to deal with JDBC connection polling when using XAP JDBC driver.
{{% /note %}}

## Embedding the Query Processor Within the Application

By default, the Query Processor is running server side.
It is possible to set the Query Processor to run embedded within the application by passing a parameter to the JDBC driver:


```java
Class.forName("com.j_spaces.jdbc.driver.GDriver").newInstance();
String url = "jdbc:gigaspaces:url:jini://*/*/mySpace";
Properties properties = new Properties();
properties.put("com.gs.embeddedQP.enabled", "true");
conn = DriverManager.getConnection(url, properties);
```

{{% note "Note"%}}
It is also possible to set the "com.gs.embeddedQP.enabled" connection property as a System property (the connection property overrides the system property).
{{% /note %}}

# Transaction Support

The XAP JDBC Driver supports the following transaction managers:

- Local Transaction Manager
- Distributed embedded Jini Transaction Manager (default)
- Lookup Distributed Transaction Manager

The transaction manager type can be configured via JDBC's connection properties (there are additional properties for lookup distributed tx manager):


|Property|Description|
|:-------|:----------|
| gs.tx_manager_type | Transaction manager type: "local"/"distributed"/"lookup_distributed" |
| gs.lookup_tx.name | Lookup service name |
| gs.lookup_tx.timeout | Lookup timeout (default=3000) |
| gs.lookup_tx.groups | Lookup groups |
| gs.lookup_tx.locators | Lookup locators |

Transaction Manager Type Configuration Example:


```java
Class.forName("com.j_spaces.jdbc.driver.GDriver");
Properties props = new Properties();
props.put("gs.tx_manager_type", "distributed");
Connection conn = DriverManager.getConnection("jdbc:gigaspaces:url:jini://*/*/mySpace", props);
```

# Getting the JDBC Connection from a Space Proxy

You can get an XAP JDBC connection from a Space proxy using the `com.j_spaces.jdbc.driver.GConnection`. See the following example:


```java
IJSpace gsSpaceProxy;  //your space proxy. You can get it using GigaSpace.getSpace()
GConnection connection = GConnection.getInstance(gsSpaceProxy);
connection.setUseSingleSpace(true); //false = cluster, true=single
```

The `setUseSingleSpace` method allows you to get a JDBC connection that encapsulates a clustered proxy or to an embedded Space proxy.

You can also use the following `GConnection` method to set the user and password for a secured Space:


```java
public static Connection getInstance(IJSpace space, String username, String password)
    throws SQLException
```

# Combining the Space API with the JDBC API

The following example uses the Space API [DistributedTask](./task-execution-overview.html) with the JDBC API. With this example we use map/reduce approach to query the Space using the JDBC API, but send the JDBC query to be executed within the Space. This approach scales very well when the Space has multiple partitions, eliminating the need to retrieve the actual Space objects from the Space to evaluate the query. Retrieving objects from the Space involves network latency and serialization overhead.

With the example below we execute the following query:


```java
Select FIELD from CLASS group by FIELD sort by FIELD
```

The query is executed in two phases:

Step 1. A `DistributedTask` is sent to each partition to execute the following JDBC query:


```java
Select FIELD from CLASS group by FIELD
```

The result is then sent into the reducer running on the client side.

Step 2. The `DistributedTask.reduce` method running on the client side aggregates the results from all the partitions and sorts the final set.


```java
public class JDBCTask implements DistributedTask<String[], String[]>{

	public JDBCTask(String queryStr){
		this.queryStr=queryStr;
	}

	@TaskGigaSpace
	transient GigaSpace gigaspace;
	transient Statement stmt;
	transient PreparedStatement perstmt;
	transient Connection con;
	String queryStr;

	public String[] execute() throws Exception{

		Connection con = getConnection();
		stmt = con.createStatement();
		ArrayList<String> result = new ArrayList<String> ();
		ResultSet rs =stmt.executeQuery(queryStr);
		while (rs.next()) {
			result.add(rs.getString(1));
		}
		stmt.close();
		con.close();
		String resultArr [] = new String[result.size()] ;
		resultArr = result.toArray(resultArr);
		return resultArr;
	}

	public String[] reduce(List<AsyncResult<String[]>> results) throws Exception {
		Iterator<AsyncResult<String[]>> iter = results.iterator();
		String[] result_ = null;
		HashSet<String> result = new HashSet<String>();
		while (iter.hasNext())
		{
			result_ = iter.next().getResult();
			for (int i=0;i<result_.length;i++)
			{
				result.add(result_[i])	;
			}

		}

		String[] fullResult = new String[result.size()];
		fullResult = result.toArray(fullResult);
		Arrays.sort(fullResult);
		return fullResult;
	}

	public Connection getConnection()
	{
		GConnection connection = null;
		try {
			Class.forName("com.j_spaces.jdbc.driver.GDriver");

		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {
			connection = GConnection.getInstance(gigaspace.getSpace());
			connection.setUseSingleSpace(true); //false = cluster, true=single

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}

		return connection;
	}
}
```


```java
GigaSpace gigapace =new GigaSpaceConfigurer(new SpaceProxyConfigurer("mySpace")).gigaSpace();
AsyncFuture<String[]> result= gigapace.execute(new JDBCTask("select str from " +MyClass.class.getName() + " group by str"));
String[] result_str = result.get();
System.out.println("The Result:" + Arrays.asList(result_str));
```

# SQL- to-Java Type Mapping

The XAP JDBC Driver translates in runtime a Space object into a relational table representation.

- All Java class attributes are translated into their corresponding SQL types.
- Class names are translated into table names.
- Field names are translated into column names.
- Indexed columns are translated into indexed fields. Make sure the `btree` index is turned on, allowing fast processing of bigger than/less than queries. For more details, refer to the [Indexing](./indexing-overview.html) section.

The following information represents the SQL-to-Java mapping conducted at runtime when a table is created via the JDBC driver.


|SQL Type|Java Type|
|:-------|:--------|
| VARCHAR, VARCHAR2 | java.lang.String |
| CHAR | java.lang.String |
| DATE | java.sql.Date |
| DATE | java.sql.Timestamp |
| TIME | java.sql.Time |
| FLOAT | java.lang.Float |
| REAL | java.lang.Float |
| NUMERIC | java.math.BigDecimal |
| DECIMAL | java.math.BigDecimal |
| DOUBLE | java.lang.Double |
| BOOLEAN | java.lang.Boolean |
| INTEGER | java.lang.Integer |
| TIMESTAMP | java.sql.Timestamp |
| LONG | java.lang.Long |
| BLOB | com.gigaspaces.jdbc.driver.Blob |
| CLOB | com.gigaspaces.jdbc.driver.Clob |

# Supported Features

**XAP JDBC supports the following**:

- All Basic SQL statements: `SELECT`, `INSERT`, `DELETE`, `UPDATE`, `CREATE TABLE`, `DROP TABLE`.
- `AND/OR` operators to join two or more conditions in a `WHERE` clause.
- `NOT IN` and `NOT NULL`
- Aggregate functions: `COUNT`, `MAX`, `MIN`, `SUM`, `AVG`.
- All basic logical operations to create conditions: =, <>, <,>, >=, <=, `[NOT]` like, is `[NOT]` `null`, `IN`, `BETWEEN`.
- Nested fields query - You may use as part of the select statement nested fields within collections (maps) or objects within the Space object.
- Multiple tables `Join` - Join supports the selection of multiple tables. It uses the Cartesian product of the tables data to form the result set. The join will perform well when having tables with small/medium size (up to 1,000,000 rows).
- `ORDER BY` for multiple columns.
- `GROUP BY` for multiple columns.
- `DISTINCT` (although not when used with functions or aggregations)
- Column aliases.




{{%accordion%}}
{{%accord title="Click here for example..."%}}

```java
Connection conn;
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT ID AS Identifier, NAME AS FullName FROM PERSON WHERE Identifier = 210");
```

{{%/accord%}}
{{%/accordion%}}

- Table aliases -- tables are allowed to use aliases throughout the query.
- Using a sub-query in the `FROM` clause.

{{%accordion%}}
{{%accord title="Click here for example..."%}}

```java
Connection conn;
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM STUDENT WHERE GRADE >= (SELECT AVG(GRADE) FROM STUDENT)");
```
{{%warning "Important"%}}
Joined sub-queries are not supported.
{{%/warning%}}

{{%/accord%}}
{{%/accordion%}}

- `sysdate` - a keyword suggesting current time and date.
- `rownum` - a keyword to use in `WHERE` clauses, setting the number of rows to select.
- Select for update -- allowing the locking of rows in order to update them later.
- Remote and embedded query processes configuration -- allows fast access to the space using embedded mode.
- Optimistic locking.
- Batch Processing.
- Increment a field in an UPDATE statement.

{{%accordion%}}
{{%accord title="Click here for example..."%}}

```java
Connection conn;
Statement stmt = conn.createStatement();
int result = stmt.executeUpdate("UPDATE PERSON SET VERSION = VERSION + 1 WHERE ID = 10000");
```

{{%warning "Important"%}}
Field incrementing is only supported for `Integer` fields using a '+' operator.
{{%/warning%}}

{{%/accord%}}
{{%/accordion%}}

- A statement caching mechanism is provided to speed up statement parsing.
- Meta Data API.
- Connection pool.
- All JDBC basic types including Blob and Clob.

# Regular Expression

XAP supports query using regular expression. You may use `like` or `rlike` expressions with your JDBC queries.

{{% note "Note"%}}
It is important to index `String` type fields used with regular expression queries. Not indexing these fields may result slow query execution and garbage creation.
{{% /note %}}

When using the SQL `like` operator you may use the following:
`%` - match any string of any length (including zero length)
`_` - match on a single character

Querying the Space using the [Java Regular Expression](http://docs.oracle.com/javase/{{%version "java-version"%}}/docs/api/java/util/regex/Pattern.html) provides more options than the SQL `like` operator. The Query syntax is done using the `rlike` operator.

When you search for Space objects with String fields that include a **single quote**, your query should use Parameterized Query. In the following example, we are searching for all `Data` objects that include the value `today's` with their `myTextField`:


```java
PreparedStatement st = con.prepareStatement("select id,text,text2 from MyData WHERE text rlike ?");
st.setString(1, "today\u0027s.*");
ResultSet rs = st.executeQuery();
```

# Text Search

XAP 12.1 introduced full text search capability leveraging the {{%exurl "Lucene" "http://lucene.apache.org"%}} search engine library supporting the {{%exurl "Lucene Query Parser Syntax" "http://lucene.apache.org/core/5_3_0/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package_description"%}} except `Fields`.
 
Text search queries are available through the `text:` extension:
 
```java
	Connection connection = GConnection.getInstance(gigaSpace.getSpace());

	// search 'java'
	String sql = "SELECT * from NewsArticle where title text:match ?";
	PreparedStatement pst = connection.prepareStatement(sql);
	pst.setObject(1, "java");
        
	ResultSet executeQuery = pst.executeQuery();
        
/....        
```        


# Indexing

It is highly recommended to use indexes on relevant properties to increase performance when using equality, bigger/less than, BETWEEN, IN, LIKE, NOT LIKE,  and IS NULL statements. For more information, see [Indexing](./indexing-overview.html). The above supported query features can leverage indexes except for the `is NOT null` and `NOT IN`statements.



# Partitioning Support

In order to partition the data and route operations to the correct partition, specify a "routing column" for each table. The "routing column" is specified through one of three mechanisms:

1. A POJO with a [@SpaceRouting](./modeling-your-data.html) field can be sent to the space via the `snapshot` call prior to calling the JDBC API.
2. Create the table through JDBC; the first index as part of the CREATE TABLE statement will be the routing field.
3. If there is no indexed column, the first column as part of the CREATE TABLE will be the routing field.

# Nested Field Query

You may use as part of the JDBC select statement nested fields. These could be a Map type fields or user-defined data type fields within the Space object. See the following example of a Space class with a nested Map and a nested object fields. Both are indexed:


```java
public class MyData implements Serializable{

	public MyData(){}
	String data1;
	String data2;
	// getter and setter methods
}
```


```java
@SpaceClass
public class MyClass {
	public MyClass (){}
	Integer num;
	String str;
	Integer id;
	HashMap<String, MyData> map; // a map within the space object
	MyData data; // an object within the space object

	@SpaceIndex (type=SpaceIndexType.EQUAL)
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}

	@SpaceIndex (type=SpaceIndexType.EQUAL)
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}

	@SpaceId (autoGenerate = false)
	@SpaceRouting
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceIndexes({@SpaceIndex(path="key1" , type = SpaceIndexType.EQUAL),
			@SpaceIndex(path="key2" , type = SpaceIndexType.EQUAL)})
	public HashMap<String, MyData> getMap() {
		return map;
	}
	public void setMap(HashMap<String, MyData> map) {
		this.map = map;
	}

	@SpaceIndexes({@SpaceIndex(path="data1" , type = SpaceIndexType.EQUAL),
			@SpaceIndex(path="data2" , type = SpaceIndexType.EQUAL)})
	public MyData getData() {
		return data;
	}
	public void setData(MyData data) {
		this.data = data;
	}
}
```

The following is an example for a JDBC query call you can use with the above Space object. Both the nested Map and nested object fields are used with the JDBC query below:


```sql
String queryString =
"select uid,
map.key1.data1, map.key1.data2,
map.key2.data1, map.key2.data2,
data.data1, data.data2,str
from
org.test.MyClass where
map.key1.data1='aa' and
map.key1.data2='bb' and
map.key2.data1='cc' and
map.key2.data2='dd' and
data.data1 = 'ee' and
data.data1 = 'ff' and
str='ABCD'";
stmt = connection.createStatement();
ResultSet rs = stmt.executeQuery(queryString );
while (rs.next()) {
	...
}
```


# Unsupported Features

The XAP JDBC Driver does not support the following:

- The SQL statements: `HAVING`, `VIEW`, `TRIGGERS`, `EXISTS`, `BETWEEN` in collections, `NOT`, `CREATE USER`, `GRANT`, `REVOKE`, `SET PASSWORD`,  `CONNECT USER`, `ON`.
- `CREATE` Database.
- `CREATE` Index, `DROP` Index.
- Constraints: `NOT NULL`, `IDENTITY`, `UNIQUE`, `PRIMARY KEY`, Foreign Key `REFERENCES`, `NO ACTION`, `CASCADE`, `SET NULL`, `SET DEFAULT`, `CHECK`.
- Set operations: `Union, Minus, Union All`.
- Aggregate Functions: `STDEV`, `STDEVP`, `VAR`, `VARP`, `FIRST`, `LAST`.
- The `UPDATE` statement does not allow the use of an expression or a `null` value in the `SET` clause.
- Using a constant instead of the column name.
- The `INSERT` statement does not allow the use of an expression in the `VALUES` clause.
- "." used to indicate a double data type.
- Using mathematical expressions in the `WHERE` clause, however the [Aggregators](./aggregators.html) functionality can be used.
- Using a non constant right-hand side comparison operator. This can be implemented via [Custom Aggregation](./aggregators.html#custom-aggregation).
- `LEFT [OUTER] JOIN`
- `RIGHT [OUTER] JOIN`
- `[INNER] JOIN`
- Statement::setFetchSize()

{{% note "Note"%}}
When having `SELECT count (*) FROM myClass` JDBC query -- `myClass` sub classes object count are not taken into consideration when processing the query result. The `SELECT count (*) FROM myClass WHERE X=Y` and `SELECT (*) from myClass` do take into consideration `myClass` sub classes objects when processing the result. Future versions will resolve this inconsistency.
As a workaround, construct a JDBC query that includes a relevant `WHERE` part.
{{% /note %}}

# JDBC Reserved Words

This section lists the JDBC reserved keywords, data types, separators and operators.

## Keywords

```bash
ALTER ADD AND ASC BETWEEN BY CREATE CALL DROP DEFAULT_NULL DESC DISTINCT
END FROM GROUP IN IS LIKE RLIKE MAX MIN NOT NULL OR ORDER SELECT SUBSTR SUM SYSDATE
UPPER WHERE COUNT DELETE EXCEPTION ROWNUM INDEX INSERT INTO SET TABLE TO_CHAR
TO_NUMBER FOR_UPDATE UPDATE UNION VALUES COMMIT ROLLBACK PRIMARY_KEY UID USING
```

## Data Types


```bash
date datetime  time float double number decimal boolean integer varchar varchar2
char timestamp long clob blob empty_clob() empty_blob() lob true false
```

## Separators and Operators


```bash
:=  || ; . ROWTYPE ~ < <= >  >= => != <> \(+\) ( ) \* / + - ? \{ \}
```

# Configuration

The JDBC Driver should be configured using the following properties. These should be part of the [The Space Component](./the-space-configuration.html#proxy) configuration when deployed:


| Parameter | Description | Default Value |
|:----------|:------------|:--------------|
|space-config.QueryProcessor.space_read_lease_time|Read timeout. Millisec units.|0|
|space-config.QueryProcessor.space_write_lease|Object lease timeout. Millisec units. |9223372036854775807L|
|space-config.QueryProcessor.transaction_timeout|Millisec unit. Transaction Timeout.|30000|
|space-config.QueryProcessor.init_jmx|Expose Tracing via JMX|false|
|space-config.QueryProcessor.trace_exec_time|Enable Tracing |false|
|space-config.QueryProcessor.debug_mode|Boolean value. When true show debug information.|false|
|<nobr>space-config.QueryProcessor.parser_case_sensetivity</nobr>|Boolean value. Determines if Column and Table names are case sensitive.|true|
|space-config.QueryProcessor.auto_commit|Boolean value. Auto Commit mode|true|
|space-config.QueryProcessor.date_format| Date Format|yyyy-MM-dd|
|space-config.QueryProcessor.datetime_format| DateTime Format|yyyy-MM-dd hh:mm:ss|
|space-config.QueryProcessor.time_format| Time Format|hh:mm:ss|

Example:


```xml
<os-core:embedded-space  id="space" space-name="space" >
    <os-core:properties>
        <props>
            <prop key="space-config.QueryProcessor.transaction_timeout">50000</prop>
            <prop key="space-config.QueryProcessor.date_format">yyyy-MM-dd</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

# JDBC Error Codes

The following is a list of JDBC error codes and their descriptions:

```bash

`100`: No (more) data
`0`: Successful Completion

- `101`: Can't alter table
- `102`: Table `<tableName>` does not exist
- `103`: Commit/Rollback failed
- `104`: Can't delete row
- `105`: Table does not exist
- `106`: Remote Exception occurred
- `107`: Failed to drop table
- `108`: All values must be set in a Prepared Statement
- `109`: Prepared value already set!
- `110`: Prepared value missing!
- `111`: Invalid data
- `112`: Invalid type for the specified column
- `113`: Unknown columns
- `114`: Unknown table in condition
- `115`: Can't set same value more than once
- `116`: Unknown column for IN condition
- `117`: Unknown execution type
- `118`: Table already exists
- `119`: Wrong data type in SUM function
- `120`: Error in rownum
- `121`: Select failed
- `122`: The selected column does not exist in the selected tables
- `123`: No such column for given alias
- `124`: Order by column should be in select list
- `125`: Must specify the column to return the sum of.
- `126`: All values must be set
- `127`: Wrong type for given column
- `128`: Incorrect number of values to insert
- `129`: Type mismatch in nested query
- `130`: Can't update row!
- `131`: Blob cannot hold `null` data
- `132`: Command not supported
- `133`: Both parameters should be greater than 1
- `134`: Clob cannot hold `null` data
- `135`: Can't convert clob to ascii, unsupported encoding
- `136`: Substring out of clob's bounds
- `137`: Error creating connection - Unknown host
- `138`: Error creating connection or reading QP properties
- `139`: Cannot commit an autocommit connection
- `140`: Cannot rollback an autocommit connection
- `141`: The given URL is not supported
- `142`: Prepared statement must contain prepared values
- `143`: Cannot call `SELECT` with `executeUpdate`. Use `executeQuery` instead
- `144`: Cannot set a `null` object
- `145`: Cannot set object, unknown type
- `146`: Used `executeQuery` instead of `executeUpdate`
- `147`: Cannot set a value
- `148`: Cannot represent this value as `byte`
- `149`: Cannot represent this value as `double`
- `150`: Cannot represent this value as `float`
- `151`: Cannot represent this value as `int`
- `152`: Cannot represent this value as `long`
- `153`: Cannot represent this value as `short`
- `154`: Cannot represent this value as boolean
- `155`: Column found in result
- `156`: Cannot represent this value as `Blob`
- `157`: Cannot represent this value as `Clob`
- `158`: Cannot represent this value as `Date`
- `159`: Cannot represent this value as `Time`
- `160`: Cannot represent this value as `Timestamp`
- `161`: The `next()` method must be called at least once
- `162`: Exhausted `ResultSet`
- `201`: Invalid SQL syntax
```
