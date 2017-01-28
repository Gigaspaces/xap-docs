---
type: post121
title:  Text Search Index
categories: XAP121
parent: indexing-overview.html
weight: 600
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

{{%refer%}}
See [Full Text Search](./query-full-text-search.html)  for more information on how geospatial queries work. 
{{%/refer%}}

 