---
type: post120
title:  Free Text Search
categories: XAP120
parent: querying-the-space.html
weight: 350
---

{{%ssummary%}}{{%/ssummary%}}


# Regular Expressions

You can query the space using the SQL `like` operator or [rlike - Java Regular Expression](http://docs.oracle.com/javase/{{%version "java-version"%}}/docs/api/java/util/regex/Pattern.html) Query syntax.

When using the SQL `like` operator you may use the following:
`%` - match any string of any length (including zero length)
`_` - match on a single character


```java
SQLQuery<MyClass> query = new SQLQuery<MyClass>(MyClass.class,"name like 'A%'")
```

Querying the space using the **Java Regular Expression** provides more options than the SQL `like` operator. The Query syntax is done using the `rlike` operator:


```java
// Match all entries of type MyClass that have a name that starts with a or c:
SQLQuery<MyClass> query = new SQLQuery<MyClass>(MyClass.class,"name rlike '(a|c).*'");
```

When you search for space objects with String fields that includes a **single quote** your query should use Parameterized Query - with the following we are searching for all `Data` objects that include the value `today's` with their `myTextField`:


```java
String queryStr = "myTextField rlike ?";
SQLQuery<Data> query = new SQLQuery<Data>(Data.class, queryStr);
query.setParameter(1, "(today\u0027s)");
Data ret[] = space.readMultiple(query);
```

All the Query options are supported both with `rlike` and `like` queries.

{{% note %}}
It is important you index `String` type fields used with regular expression queries. Not indexing such fields may result slow query execution and garbage creation.
{{% /note %}}


# Free text search

Free text search is required almost with every application. Users placing some free text into a form and later the system allows users to search for records that includes one or more words within a free text field. A simple way to enable such a search can be done by using an array or a collection of String.

# Example

Our Space class includes the following - note the **words** and the **freeText** fields:


```java
public class MyData {
	String[] words;
	String freeText;

	public String[] getWords() {
		return words;
	}

	public void setWords(String words[]) {
		this.words=words;
	}

	public String getFreeText() {
		return freeText;
	}
	public void setFreeText(String freeText) {
		this.freeText = freeText;
		this.words = freeText.split(" ");
	}
....
}
```

{{% note %}} Note how the **freeText** field is broken into the **words** array before placed into the indexed field.
{{%/note%}}

You may write the data into the space using the following:


```java
MyData data = new MyPOJO(...);
data.setFreeText(freetext);
gigaspace.write(data);
```

You can query for objects having the word **hello** as part of the freeText field using the following:


```java
MyData results[] = gigaspace.readMultiple(new SQLQuery<MyData>(MyData.class, words[*]='hello'));
```

You can also execute the following to search for object having the within the freeText field the word **hello** or **everyone**:


```java
MyData results[] = gigaspace.readMultiple(new SQLQuery<MyData>(MyData.class, words[*]='hello' OR words[*]='everyone'));
```

With the above approach you avoid the overhead with regular expression queries.


# Indexing

To speed up the query you can create an [index](./indexing-collections.html) on the fields you want to search.

Example:


```java
public class MyData {
	String[] words;
	String freeText;

	@SpaceIndex (path="[*]")
	public String[] getWords() {
		return words;
	}

	public void setWords(String words[]) {
		this.words=words;
	}

	public String getFreeText() {
		return freeText;
	}
	public void setFreeText(String freeText) {
		this.freeText = freeText;
		this.words = freeText.split(" ");
	}
....
}
```

{{% refer %}}
The same approach can be implemented also with the [SpaceDocument](./document-overview.html).
{{% /refer %}}


# Case Insensitive Query

Implementing case insensitive queries can be done via:

- `like` operator or `rlike` operator.
- Store the data in lower case and query on via lower case String value (or upper case)

