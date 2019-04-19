---
type: post123
title:  Free Text Search
categories: XAP123NET, PRM
parent: querying-the-space.html
weight: 550
canonical: auto
---

 


Free text search is required almost with every application.
Users placing some free text into a form and later the system allows users to search for records that includes one or more words within a free text field.
A simple way to enable such a search without using a regular expression query that my have some overhead can be done by using an array or a collection of String.

# Example

Our Space class includes the following - note the **words** and the **freeText** fields:


```csharp
public class MyData {


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


# Indexing

To speed up the query you can create an [index](./indexing-collections.html) on the fields you want to search.



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

{{% refer %}}
The same approach can be implemented also with the [SpaceDocument](./document-overview.html).
{{% /refer %}}



# Regular Expressions

You can query the space using the SQL `like` operator or {{%exurl "Java Regular Expression""http://docs.oracle.com/javase/1.5.0/docs/api/java/util/regex/Pattern.html"%}} Query syntax.

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



# Case Insensitive Query

Implementing case insensitive queries can be done via:

- `like` operator or `rlike` operator. Relatively slow. Not recommended when having large amount of objects.
- Store the data in lower case and query on via lower case String value (or upper case)


