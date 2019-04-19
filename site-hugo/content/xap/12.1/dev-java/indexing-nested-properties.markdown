---
type: post121
title:  Nested Property Index
categories: XAP121
parent: indexing-overview.html
weight: 200
canonical: auto
---


An index can be defined on a nested property to improve performance of nested queries - this is highly recommended.
Nested properties indexing uses an additional `@SpaceIndex` attribute - `path()`.

## The SpaceIndex.path() Attribute

The `path()` attribute represents the path of the property within the nested object.

Below is an example of defining an index on a nested property:

{{%tabs%}}
{{%tab "  Single Index Annotation "%}}

```java
@SpaceClass
public class Person {
    private int id;
    private Info personalInfo;
    private String description;
    //getter and setter methods
    ...

    // this defines and EXTENDED index on the personalInfo.socialSecurity property
    @SpaceIndex(path = "socialSecurity", type = SpaceIndexType.EXTENDED)
    public Info getPersonalInfo() {
         return personalInfo;
    }
}

public static class Info implements Serializable {
	private String name;
	private Address address;
	private Date birthday;
	private long socialSecurity;
	private int _id;
	//getter and setter methods
}

public static class Address implements Serializable {
	private int zipCode;
	private String street;
	//getter and setter methods
}
```

{{% /tab %}}
{{%tab "  Multiple Indexes Annotation "%}}


```java
@SpaceClass
public static class Person {
	private int id;
	private Info personalInfo;
	private String description;
	private HashMap<String, String> map;

	//getter and setter methods
	...

	// this defines several indexes on the same personalInfo property
	 @SpaceIndexes( { @SpaceIndex(path = "socialSecurity", type = SpaceIndexType.EXTENDED),
			  @SpaceIndex(path = "address.zipCode", type = SpaceIndexType.BASIC)})
	public Info getPersonalInfo() {
		 return personalInfo;
	}

	// this defines indexes on map keys
	@SpaceIndexes(	{@SpaceIndex(path="key1" , type = SpaceIndexType.BASIC),
			@SpaceIndex(path="key2" , type = SpaceIndexType.BASIC)})
	public HashMap<String, String> getMap() {
		return map;
	}
	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}
}

public static class Info implements Serializable {
	private String name;
	private Address address;
	private Date birthday;
	private long socialSecurity;
	private int _id;
	//getter and setter methods
}

public static class Address implements Serializable {
	private int zipCode;
	private String street;
	//getter and setter methods
}
```

{{% /tab %}}
{{%tab "  XML "%}}


```xml
<gigaspaces-mapping>
    <class name="com.gigaspaces.examples.Person"  >
         <property name="personalInfo">
		<index path="socialSecurity" type = "extended"/>
		<index path="address.zipCode" type = "basic"/>
	</property>
    </class>
</gigaspaces-mapping>
```

{{% /tab %}}
{{% /tabs %}}

The following is an example of query code that automatically triggers this index:


```java
SQLQuery<Person> query = new SQLQuery<Person>(Person.class,
	"personalInfo.socialSecurity<10000050L and personalInfo.socialSecurity>=10000010L");
```

{{%refer%}}
[SQL Query Nested Properties](./query-sql.html#Nested Properties)
{{%/refer%}}



{{% note "Map based nested properties "%}}
Note that the same indexing techniques above are also applicable to Map-based nested properties, which means that in the example above the `Info` and `Address` classes could be replaced with a `java.util.Map<String,Object>`, with the map keys representing the property names.
{{% /note %}}

