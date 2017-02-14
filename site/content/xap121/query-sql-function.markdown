---
type: post121
title:  SQL Functions
categories: XAP121
parent: querying-the-space.html
weight: 320
---


You can query the Space using **built in** functions and **user defined** functions.


# Built in functions

For example, lets assume we have a class called `Person` with an `Addrees` property:


{{%tabs%}}
{{%tab Person%}}
```java
@SpaceClass
public class Person  {
	private UUID id;
	private String firstName;
	private String lastName;
	private Address address;
	
	@SpaceId
	public UUID getId() {
		return id;
	}
    ....
}        
```
{{%/tab%}}

{{%tab Address%}}
```java
public class Address {
	private String street;
	private String city;
	private String state;
	......
}
```
{{%/tab%}}
{{%/tabs%}}

We can query the Space with the built in SQL functions:

```java
// Find all persons with a firstName longer then tree characters
SQLQuery<Person> query = new SQLQuery<Person>(Person.class, "CHAR_LENGTH(firstName) > 3");
Person[] persons = space.readMultiple(query);

// Find all persons with a lastName length equal 22 or  25
SQLQuery<Person> query = new SQLQuery<Person>(Person.class, "CHAR_LENGTH(lastName) IN (22, 25");
Person[] persons = space.readMultiple(query); 
```


It is also possible to query for embedded properties:


```java
// Find all persons with a city name greater then 6
query = new SQLQuery<Person>(Person.class, "CHAR_LENGTH(address.city) > 6");
Person[] persons = space.readMultiple(query);

// Find all persons with city name lenght 0 or 13
query = new SQLQuery<Person>(Person.class, "CHAR_LENGTH(address.city) IN (0,13)");
Person[] persons = space.readMultiple(query);
 
```

###  Supported SQL Functions

SQLQuery supports the following functions:

- ABS
- MOD
- ROUND
- CEIL, FLOOR
- CHAR_LENGTH
- LOWER, UPPER
- CONCAT, APPEND
- INSTR
- TO_NUMBER
- TO_CHAR (datetime), TO_CHAR (number)


For example, lets assume we have a class called `Product` with a `Double` property called `price` and a `String` property called name:


```java
// An SQL query with ABS function,
// which will return all the entries that after ABS function are equal to 1 or 4
SQLQuery<Product> query = new SQLQuery<Product>(Product.class, "ABS(price) in (1, 4)");
Product[] products = gigaSpace.readMultiple(query);

// An SQL query with MOD function,
// which will return all the entries that after modulo 10 are equal to 6 or 7
SQLQuery<Product> query = new SQLQuery<Product>(Product.class, "MOD(price, 10) IN (6, 7)");
Product[] products = gigaSpace.readMultiple(query);
```


{{% refer %}}
For the full documentation of the class's methods and constructors, see [Javadoc]({{% api-javadoc %}}/index.html?com/j_spaces/core/client/SQLQuery.html).
{{% /refer %}}


# User defined functions

The user defined functions - UDF , should be implemented in Java and can be called from any data access API that supports SQL Queries. This means, you can call them from a client using the Space API , JDBC API , .Net API , C++ API , Rest API , JPA API , Scala API , UI , CLI etc.

UDF allows you to access the relevant Space objects without serializing or materializing them. This means, the overall overhead of calling these functions is minimal. The `SqlFunctionExecutionContext` provides you direct access to the Space class properties. You can override existing functions such as `ABS` to perform customized functionality. Simply implement them as described below and register them using the `ABS` name.

### Example

Here is an example of a user defined function: 


 
 

```java
import com.gigaspaces.query.sql.functions.SqlFunctionExecutionContext;

public class MyCustomSqlFunction extends SqlFunction {
	@Override
	public Object apply(SqlFunctionExecutionContext context) {
		// Increment by two before we return the value
		return (Double) context.getArgument(0) + 2;
	}
}
```
 

### Space registration

`PLUSTWO` is the new function that needs to be registered with the Space : 
 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:os-core="http://www.openspaces.org/schema/core"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd">

	<bean id="myCustomSqlFunction" class="sandbox.sqlcustomfunction.MyCustomSqlFunction" />
 
	<os-core:embedded-space id="space" space-name="mySpace">
		<os-core:space-sql-function name="PLUSTWO">
			<os-core:sql-function ref="myCustomSqlFunction" />
		</os-core:space-sql-function>
	</os-core:embedded-space>
	
	<os-core:giga-space id="gigaSpace" space="space"/>
</beans>
```
And here is the usage of the newly registered SQL function:

```java
// An SQL query with PLUSTWO function,
// which will return all the entries that after PLUSTWO function are 20.0
SQLQuery<Product> query = new SQLQuery<Product>(Product.class, "PLUSTWO(price) = 20.0");
Product[] products = gigaSpace.readMultiple(query);
```


{{%note%}}
UDF supports root level and nested properties (e.g. foo(person.address.street). With nested properties as part of a user-defined class , the UDF will handle the user-defined type (e.g. Address) as an argument.
{{%/note%}}


### Limitations
 
- `parameter` - The SQL function must get exactly one parameter that is a property of a POJO that is written to the Space and a number of unbound parameters that are not properties of a POJO.
- `LRU` - SQL functions are not supported with the `LRU` space caching policy.
 


 
