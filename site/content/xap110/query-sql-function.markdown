---
type: post110
title:  SQL Functions
categories: XAP110
parent: querying-the-space.html
weight: 320
---


You can query the Space using **built in** functions and **user defined** functions.

The user defined functions - UDF , should be implemented in Java and can be called from any data access API that support SQL Query. This means you can call them from a client using the Space API , JDBC API , .Net API , C++ API , Rest API , JPA API , Scala API , UI , CLI etc.   

UDF allows you to access the relevant space object data without serializing or materializing it. This means the overall overhead of calling UDF is minimal. The `SqlFunctionExecutionContext` provides you direct access to the entry fields maintained within the space. You can override existing UDF such as `ABS` to perform customized functionality. Simply implement them as described below and register them using the `ABS` name.

UDF support root level and nested fields (e.g. foo(person.address.street). With nested fields as part of a user-defined class , the UDF should handle the user-defined type (e.g. Address) as an argument.

### Examples

For example, lets assume we have a class called `IntValue` with an `Integer` property called **value** and a `String` property called **name** and a property **_id** which is an `Integer`:


```java
// An SQL query with ABS function,
// which will return all the entries that after ABS function are equal to 1 or 4
SQLQuery<IntValue> query = new SQLQuery<IntValue>(IntValue.class, "ABS(value) in (1, 4)");
IntValue[] values = gigaSpace.readMultiple(query);

// An SQL query with MOD function,
// which will return all the entries that after modulo 10 are equal to 6 or 7
SQLQuery<IntValue> query = new SQLQuery<IntValue>(IntValue.class, "MOD(value, 10) in (6, 7)");
IntValue[] values = gigaSpace.readMultiple(query);

// An SQL query with CHAR_LENGTH function,
// which will return all the entries that their name length is 0 or 3
SQLQuery<IntValue> query = new SQLQuery<IntValue>(IntValue.class, "CHAR_LENGTH(name) in (0, 3)");
IntValue[] values = gigaSpace.readMultiple(query);

// An SQL query with, user define function, PLUSONE,
// which will return all the entries that their age is eqaul to 23.2
query = new SQLQuery<Person>(Person.class, "PLUSONE(age) = ?", 23.2);
IntValue[] values = gigaSpace.readMultiple(query);
```

{{% refer %}}
For the full documentation of the class's methods and constructors, see [Javadoc](http://www.gigaspaces.com/docs/JavaDoc{{%currentversion%}}/index.html?com/j_spaces/core/client/SQLQuery.html).
{{% /refer %}}


### User Defined function

Here is an example of user defined functions:  `PLUSONE` and `PLUSTWO`. The `space-sql-function` bean include the `name` property. You should use it to register the UDF with the space. Once this is has been registered you can call it from your client application. 

{{%tabs%}}
{{%tab "Configuration"%}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:os-core="http://www.openspaces.org/schema/core"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd">

	<bean id="myFunction" class="com.gigaspaces.test.document.SqlFunctionPlusTwo" />
	<bean id="myFunction1" class="com.gigaspaces.test.document.SqlFunctionPlusOne" />

	<os-core:embedded-space id="space" name="mySpace">
		<os-core:space-sql-function name="PLUSTWO">
			<os-core:sql-function ref="myFunction" />
		</os-core:space-sql-function>

		<os-core:space-sql-function name="PLUSONE">
			<os-core:sql-function ref="myFunction1" />
		</os-core:space-sql-function>
	</os-core:embedded-space>

</beans>
```
{{%/tab%}}

{{%tab "SQL function Abstract class"%}}
```java
public abstract class SqlFunction {
	// This is the method that should be implemented by the user
    public abstract Object apply(SqlFunctionExecutionContext context);

    // User can use this function to ensure the number of arguments the function get
    protected void assertNumberOfArguments(int expected, SqlFunctionExecutionContext context){
        if (context.getNumberOfArguments() != expected){
            throw new RuntimeException("wrong number of arguments - expected: " + expected + " ,but actual number of arguments is: " + context.getNumberOfArguments());
        }
    }
}
```
{{% /tab %}}
{{%tab "SQL function implementation"%}}

```java
public class SqlFunctionPlusTwo extends SqlFunction {
    @Override
    public Object apply(SqlFunctionExecutionContext context) {
        return (Double)context.getArgument(0) + 2;
    }
}
```
{{% /tab %}}
{{%/tabs%}}




{{%note "Limitations" %}}
- `parameter` - The SQL function must get exactly one parameter that is a field of a POJO that is written to the Space and a number of unbound parameters that are not properties of a POJO.
- `LRU` - SQL functions are not supported with the `LRU` space caching policy.
{{%/note%}}




# Supported SQL Features

#### SQLQuery supports the following:

- `ABS`
- `MOD`
- `ROUND`
- `CEIL`, `FLOOR`
- `CHAR_LENGTH`
- `LOWER`, `UPPER`
- `CONCAT`, `APPEND`
- `INSTR`
- `TO_NUMBER`
- `TO_CHAR (datetime)`, `TO_CHAR (number)`

