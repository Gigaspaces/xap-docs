---

type: post
title: Explain Plan

weight: 1400
---

{{%warning%}}
This is a preview feature .......
{{%/warning%}}


# Intro 


##  Index Information

 - Describes the indexes that the space considered to use, and the selected index in each stage.
 - “And” choice node behavior is to scan through the relevant indexes and choose the minimal one.
 - “Or” choice node behavior is to unify all the indexes that each “And” chose.
 - Each cluster node may produce different result
 - The information breakdown is by the Pojo  type.

##  Scanning Information

 - Describes the number of entries the space scanned in order to find the matching entries, and how many entries were a match.
 - Each cluster node may produce different result
 - The information breakdown is by the Pojo  type.



# Usage 



```java
@Service
public class PersonQuery {

	@Autowired
	private GigaSpace gigaSpace;

	public Person[] findAllPersonOlderThen() {
		SQLQuery<Person> query = new SQLQuery<Person>(Person.class, "age > 20").withExplainPlan();
		Person[] persons = gigaSpace.readMultiple(query);

		System.out.println(query.getExplainPlan());

		return persons;
	}
}
```

# Example

```java
import java.util.UUID;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;

@SpaceClass
public class Person {
	UUID id;
	String country;
	String gender;

	@SpaceId
	public UUID getId() {
		return id;
	}

	public void setId(final UUID id) {
		this.id = id;
	}

	@SpaceIndex
	public String getCountry() {
		return country;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	@SpaceIndex
	public String getGender() {
		return gender;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}
}
```


# Output

```bash

Add output info ....

```





 
# Limitations
 
 - Only Java API is supported
 - JDBC not supported (hence also Web-UI/GS-UI)
 - .NET not supported
 - Only Basic and Extended index are supported
 - No support for collection, compound, unique.
 - Off-Heap is not supported
 - FIFO grouping is not supported
 - Geospatial is not supported
 - Only Read, ReadMultiple, TakeMultiple & count  operations are supported
 - Not supported: Take/Clear and variations, blocking operations, space iterator, aggregate , change, notifications
 - Not thread Safe
 - Only Basic query type are Supported
 - Not supported: Regex, Is null, Sql function
 
