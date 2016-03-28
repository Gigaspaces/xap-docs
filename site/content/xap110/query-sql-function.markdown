---
type: post110
title:  SQLFunction
categories: XAP110
parent: query-sql.html
weight: 100
---

{{%ssummary%}}{{%/ssummary%}}

You can query the space using build in functions and user define functions.

{{% refer %}}
For the full documentation of the class's methods and constructors, see [Javadoc](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?com/j_spaces/core/client/SQLQuery.html).
{{% /refer %}}


# Examples

For example, suppose we have a class called `IntValue` with an `Integer` property called **value** and a `String` property called **name** and propery **_id** which is an `Integer`:


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

{{% refer %}} For an example of `SQLQuery` with `EventSession`, refer to the [Session Based Messaging API](./session-based-messaging-api.html#SQLQuery Template Registration) section.{{% /refer %}}

# Supported Build-in function

{{%vbar "The following operations fully support GigaSpaces `SQLFunctionQuery`:"%}}
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

{{%/vbar%}}

# User Define function

{{%vbar "Example for user define SQL function by spring:"%}}
Here, there are two user define functions: PLUSONE and PLUSTWO.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:os-core="http://www.openspaces.org/schema/core"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/11.0/core/openspaces-core.xsd">

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
{{%tabs%}}

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

{{%/vbar%}}

{{%vbar%}}
# Limitations
- `parameter` - SQL function must get exacatly one parameter that is a field of pojo the written to the sapce, and unbound number of paramter that are not field of a pojo.
- `LRU` - SQL function not supported with LRU strategy.
