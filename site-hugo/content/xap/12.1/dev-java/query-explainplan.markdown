---
type: post121
title:  Explain Plan
categories: XAP121
parent: querying-the-space.html
weight: 900
canonical: auto
---

{{%warning%}}
This is an experimental feature. The API and usage might change in future releases.   
{{%/warning%}}


**Explain Plan** can be used to obtain a description of the strategy or plan that XAP uses to implement a specific `SQLQuery`. 
Explain Plan reports on the way in which the query uses indices and how many entries were scanned.




{{%refer%}}
For detailed information how to assign index consult [indexing](./indexing.html#index-types)
{{%/refer%}}


##  Index Information

 - Indices that the space considered using and the selected index at each stage of the query.
 - Each cluster node may produce a different result.
 - Information breakdown by Pojo  type.

##  Scanning Information

 - Number of entries the space scanned in order to find the matching entries and how many entries were matched.
 - Each cluster node may produce a different result.
 - Information breakdown by Pojo  type.


# Index Types
The index type is determined by the SpaceIndexType enumeration. The index types are:

**NONE** - No indexing is used.<br>
**BASIC** - Basic index is used - this speeds up equality matches (equal to/not equal to).<br>
**EXTENDED** - Extended index - this speeds up comparison matches (bigger than/less than).


# Usage 

The execution plan is initiated by calling the `withExplainPlan()` method on the `SQLQuery`. After the query is executed the information is obtained with the `query.getExplainPlan()` method.

```java
SQLQuery<Person> query = new SQLQuery<Person>(Person.class, "age > 20")
        .withExplainPlan();
gigaSpace.readMultiple(query);
ExplainPlan explainPlan = query.getExplainPlan();
```
		
 

# Example

We use a `Person` class with several properties annotated with `@SpaceIndex` indicating that we want to use indexes when queries are performed.

```java
import java.util.UUID;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;

@SpaceClass
public class Person {
	private UUID id;
	private String country;
	private String gender;
	private Long age;
	
	@SpaceId
	public UUID getId() {
		return id;
	}
	@SpaceIndex
	public String getCountry() {
		return country;
	}
	
	@SpaceIndex
	public String getGender() {
		return gender;
	}
	
	@SpaceIndex
	public Long getAge() {
		return age;
	}
    .........
}

``` 

When different `SQLQuery` are executed against the space, we can display for each of them the plan that XAP chooses to execute the query.

{{%tabs%}}

{{%tab "Simple"%}}

**Query**

```java
SQLQuery<Person> query = new SQLQuery<Person>(Person.class, "age > ? ")
    .setParameter(1, 30L)
    .withExplainPlan();
gigaSpace.readMultiple(query);
System.out.println(query.getExplainPlan());
```
**Output**

```bash
******************** Explain plan report ********************
Query: SELECT * FROM sandbox.explainplan.Person WHERE age > ? 
Execution Information Summary:
	Total scanned partitions: 1
	Total scanned entries: 1000
	Total matched entries: 1000
Detailed Execution Information:
	Query Tree:
		AND
			GT(age, 30)
	Partition Id: 0
	Index Information: NO INDEX USED
		sandbox.explainplan.Person:
		Scanned entries: 1000
		Matched entries: 1000
*************************************************************
```
{{%/tab%}}

 
{{%tab "AND"%}}

**Query**

```java
query = new SQLQuery<Person>(Person.class, "age > ? and gender = ? and country = ?")
    .setParameter(1, 30L)
    .setParameter(2, "M")
    .setParameter(3, "USA")
    .withExplainPlan();

gigaSpace.readMultiple(query);
System.out.println(query.getExplainPlan());
```
**Output**

```bash
******************** Explain plan report ********************
Query: SELECT * FROM sandbox.explainplan.Person WHERE age > ? and gender = ? and country = ?
Execution Information Summary:
	Total scanned partitions: 1
	Total scanned entries: 1000
	Total matched entries: 1000
Detailed Execution Information:
	Query Tree:
		AND
			GT(age, 30)
			EQ(country, USA)
			EQ(gender, M)
	Partition Id: 0
		Scanned entries: 1000
		Matched entries: 1000
		Index scan report:
			MATCH
				Inspected: 
					[@1] EQ(country, USA), size=1000, type=BASIC
					[@2] EQ(gender, M), size=1000, type=BASIC
				Selected: [@1] EQ(country, USA), size=1000, type=BASIC
*************************************************************
```
{{%/tab%}}



{{%tab "AND with OR"%}}

**Query**

```java
 	
query = new SQLQuery<Person>(Person.class, "age > ? and gender = ? or country = ? ")
    .setParameter(1, 30L)
    .setParameter(2, "M")
    .setParameter(3, "USA")
    .withExplainPlan();

gigaSpace.readMultiple(query);
System.out.println(query.getExplainPlan());
```
**Output**

```bash
******************** Explain plan report ********************
Query: SELECT * FROM sandbox.explainplan.Person WHERE age > ? and gender = ? or country = ? 
Execution Information Summary:
	Total scanned partitions: 1
	Total scanned entries: 1000
	Total matched entries: 1000
Detailed Execution Information:
	Query Tree:
		OR
			EQ(country, USA)
			AND
				EQ(gender, M)
				GT(age, 30)
	Partition Id: 0
		Scanned entries: 1000
		Matched entries: 1000
		Index scan report:
			AND
				Inspected: 
					[@1] EQ(gender, M), size=1000, type=BASIC
					[@2] GT(age, 30), size=unknown, type=BASIC, UNUSABLE
				Selected: [@1] EQ(gender, M), size=1000, type=BASIC
			AND
				Inspected: 
					[@3] EQ(country, USA), size=1000, type=BASIC
				Selected: [@3] EQ(country, USA), size=1000, type=BASIC
			OR
				Inspected: 
					[@3] EQ(country, USA), size=1000, type=BASIC
					[@1] EQ(gender, M), size=1000, type=BASIC
				Selected: [@4] Union [@3, @1]
************************************************************* 
```
{{%/tab%}}

{{%/tabs%}}


 
# Limitations
 
 - Supported via the Java API. Not supported via the .NET API.
 - Supported operations: Read, ReadMultiple, TakeMultiple , count
 - Supported index types: Basic and Extended. Compund , Unique , Collection , Geospatial index are not supported. 
 - Supported query type : Basic.
 - Not Supported:
 	- Off-Heap 
 	- FIFO grouping 
 	- Operations: Take/Clear and variations, blocking operations, space iterator, aggregate , change, notifications
 	- JDBC API (hence also Web-UI/GS-UI)
	- Regex, Is null, Sql function
 - Not thread Safe
 
