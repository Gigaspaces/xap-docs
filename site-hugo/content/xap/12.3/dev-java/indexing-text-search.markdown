---
type: post123
title:  Text Search Index
categories: XAP123, OSS
parent: indexing-overview.html
weight: 600
canonical: auto
---
 
Text Search indexes can be defined by using the `@SpaceTextIndex` and `@SpaceTextIndexs` annotations.

Lets assume we have a class called `NewsArticle` that has a `content` property that describes text we want to execute the text search queries 
against:

```java
import org.openspaces.textsearch.SpaceTextIndex;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class NewsArticle {
	private UUID id;
	private String content;
	private Long articleNumber;

	@SpaceId
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@SpaceTextIndex
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
```

Here is a query that will trigger the usage of this index:

```java
SQLQuery<NewsArticle> query = new SQLQuery<NewsArticle>(NewsArticle.class, "content text:match ?");
query.setParameter(1, "deployment"); 
```

{{%refer%}}
See [Full Text Search](./query-full-text-search.html)  for more information on how text search queries work. 
{{%/refer%}}

# Nested Index

An index can be defined on a nested property to improve performance of nested queries. Nested properties indexing uses an additional attribute - `path()`.
This attribute represents the path of the property within the nested object.

In the example below, the `author` is a property of type `Person`  which is a property of `NewsArticle`:


 
```java
import java.util.UUID;

import org.openspaces.textsearch.SpaceTextIndex;
import org.openspaces.textsearch.SpaceTextIndexes;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class NewsArticle {
	private UUID id;
	private String content;
	private Person author;
	private Long articleNumber;

	@SpaceTextIndex
	public String getContent() {
		return content;
	}

	@SpaceTextIndexes({ @SpaceTextIndex(path = "firstName"), @SpaceTextIndex(path = "lastName") })
	public Person getAuthor() {
		return author;
	}

	public void setAuthor(Person author) {
		this.author = author;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@SpaceId
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
}
```
 
The following is an example of a query that triggers this index:

```java
SQLQuery<NewsArticle> query = new SQLQuery<NewsArticle>(NewsArticle.class, "author.firstName text:match ? AND  author.lastName text:match ?");
query.setParameter(1, "Friedrich");
query.setParameter(2, "Durrenmatt");
```

# Combining Text and Standard Predicates

Suppose our `NewsArticle` class contains a articleNumber property as well, and we want to enhance our query and find the NewsArticle with a articleNumber. We can simply add the relevant predicate to the query's criteria:

```java
SQLQuery<NewsArticle> query = new SQLQuery<NewsArticle>(NewsArticle.class, "content text:match ? AND articleNumber < ?");
query.setParameter(1, "deployment");
query.setParameter(2, new Long(1000));	
```


{{%note "Choosing the optimal index"%}}
If both `content` and `articleNumber` are indexed, the index which appears first in the query is the one that will be used. This may significantly effect the performance of your query, so it's recommended to estimate which index is most efficient for each query and put it first.
{{%/note%}}



# Space Document

The text search is also supported with [Space Documents](./document-overview.html). Lets take the above example of the `NewsArticle` and use it as a `SpaceDocument`:

```java
DocumentProperties author = new DocumentProperties();
author.put("firstName", "Friedrich");
author.put("lastName", "Durrenmatt");

SpaceDocument doc =  new SpaceDocument("NewsArticle")
	.setProperty("id", 1)
	.setProperty("content", "The quick brown fox jumps over the lazy dog")
	.setProperty("author", author);
 
// ...
```


Defining the TypeDescriptor and registering with the Space is done with the `addQueryExtensionInfo` method:

```java
GigaSpace gigaSpace = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer("xapSpace")).gigaSpace();

// With Index 
gigaSpace.getTypeManager().registerTypeDescriptor(new SpaceTypeDescriptorBuilder(typeName).idProperty("id")
				.addQueryExtensionInfo("content", LuceneTextSearchQueryExtensionProvider.index()).create());
				
// Nested Index 
gigaSpace.getTypeManager().registerTypeDescriptor(new SpaceTypeDescriptorBuilder(typeName).idProperty("id")
				.addQueryExtensionInfo("author.firstName", LuceneTextSearchQueryExtensionProvider.index())
				.addQueryExtensionInfo("author.lastName", LuceneTextSearchQueryExtensionProvider.index())
				.create());	
```

The following is an example of a query that triggers this index:

```java
SQLQuery<SpaceDocument> query = new SQLQuery("NewsArticle", "author.firstName text:match ? AND author.lastName text:match ?")
	query.setParameter(1, "Frierich");
	query.setParameter(2, "Durrnmatt");
SpaceDocument result = this.gigaSpace.read(query);
```

{{%refer%}}
Refer to [SpaceDocument](./document-overview.html) for more information on SpaceDocument.
{{%/refer%}}






 
