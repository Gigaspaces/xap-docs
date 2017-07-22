---
type: post122
title:  Full Text Search
categories: XAP122
parent: querying-the-space.html
weight: 360
---
 

XAP 12.1 introduces full text search capability leveraging the {{%exurl "Lucene" "http://lucene.apache.org"%}} search engine library. 

The following features are supported:<br>
- Keyword matching<br>
- Search for phrase<br> 
- Wildcard matching<br>
- Proximity matching<br>
- Range searching<br>
- Boosting a term<br>
- Regular expressions<br>
- Fuzzy search
    
{{%note%}}
Full text search queries can be used with any space operation which supports SQL queries (read, readMultiple, take, etc.)
{{%/note%}}

 

**Dependencies**<br>
In order to use this feature, include the `${XAP_HOME}/lib/optional/full-text-search/xap-full-text-search.jar` file on your classpath or use maven dependencies:

```xml
<dependency>
    <groupId>org.gigaspaces</groupId>
    <artifactId>xap-full-text-search</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
```
{{%refer%}}
For more information on dependencies see [Maven Artifacts](./maven-artifacts.html)
{{%/refer%}} 



# Examples
 
 
Text search queries are available through the `text:` extension to the [SQL query syntax](./query-sql.html).  

For example, suppose we have a class called `NewsArticle` with a String property called `content` and a String property called `type`:
 
```java
// Matching 
SQLQuery<NewsArticle> query = new SQLQuery<NewsArticle>(NewsArticle.class, "content text:match ?");
query.setParameter(1, "deployment"); 
    
// Wildcard search
// To perform a single character wildcard search use the "?" symbol. 
SQLQuery<NewsArticle> query = new SQLQuery<NewsArticle>(NewsArticle.class, "content text:match ?");
query.setParameter(1, "GigaSpac?s");
		
// To perform a multiple character wildcard search use the "*" symbol.
SQLQuery<NewsArticle> query = new SQLQuery<NewsArticle>(NewsArticle.class, "content text:match ?");
query.setParameter(1, "clou*y");
	
//Regular Expression search
SQLQuery<NewsArticle> query = new SQLQuery<NewsArticle>(NewsArticle.class, "content text:match ?");
query.setParameter(1, "/[tp]es/");

// Fuzzy Search
SQLQuery<NewsArticle> query = new SQLQuery<NewsArticle>(NewsArticle.class, "content text:match ?");
query.setParameter(1, "space~");

// Boolean operator
SQLQuery<NewsArticle> query = new SQLQuery<NewsArticle>(NewsArticle.class, "content text:match ? AND type text:match ?");
query.setParameter(1, "space");
query.setParameter(1, "blog");
``` 


# Supported Search Operations
 
XAP supports the {{%exurl "Lucene Query Parser Syntax" "http://lucene.apache.org/core/5_3_0/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package_description"%}} except `Fields`.
 
  
 
# Nested Properties

In the example below, the `author` is a property of type `Person`  which is a property of `NewsArticle`:

{{%tabs%}}
{{%tab "NewsAricle" %}}
```java
@SpaceClass
public class NewsArticle {
	private UUID id;
	private String content;
	private Person author;
	private Long articleNumber;
    private String type;

	public String getContent() {
		return content;
	}
	public Person getAuthor() {
		return author;
	}
	public void setAuthor(Person author) {
		this.author = author;
	}
    //......
}
```
{{%/tab%}}

{{%tab "Person" %}}
```java
public class Person {
	private String firstName;
	private String lastName;

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
```
{{%/tab%}}
{{%/tabs%}}

And here is an example how you can query for nested properties:

```java
SQLQuery<NewsArticle> query = new SQLQuery<NewsArticle>(NewsArticle.class, "author.firstName text:match ? AND  author.lastName text:match ?");
query.setParameter(1, "Friedrich");
query.setParameter(2, "Durrenmatt");
```

 
 


# Combining Text and Standard Predicates

Suppose our `NewsArticle` class contains a articleNumber property as well, and we want to enhance our query and find the NewsArticle with a articleNumber. We can simply add the relevant predicate to the query’s criteria:

```java
SQLQuery<NewsArticle> query = new SQLQuery<NewsArticle>(NewsArticle.class, "content text:match ? AND articleNumber < ?");
query.setParameter(1, "deployment");
query.setParameter(2, new Long(1000));	
```


# Analyzer

An {{%exurl "Analyzer" "http://lucene.apache.org/core/5_3_0/core/org/apache/lucene/analysis/package-summary.html#package_description"%}} is responsible for supplying a TokenStream which can be consumed by the indexing and searching processes in Lucene. There are several different {{%exurl "Analyzers" "https://lucene.apache.org/core/3_0_3/api/core/org/apache/lucene/analysis/Analyzer.html"%}} available.


You can use the `@SpaceTextAnalyzer`  annotation to choose the Analyzer:

```java
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.openspaces.textsearch.SpaceTextAnalyzer;
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
	private String type;
	
	@SpaceTextIndex
	@SpaceTextAnalyzer(analyzer = StandardAnalyzer.class)
	public String getContent() {
		return content;
	}

	@SpaceTextAnalyzer(analyzer = KeywordAnalyzer.class)
	public String getType() {
		return type;
	}
  // ....
}
```
 
For nested properties you  can use the `@SpaceTextAnalyzers`annotation:

```java
@SpaceClass
public class NewsArticle {
	private UUID id;
	private String content;
	private Person author;
	private Long articleNumber;
	private String type;

	@SpaceTextAnalyzers({ @SpaceTextAnalyzer(path = "firstName", analyzer = KeywordAnalyzer.class),
			@SpaceTextAnalyzer(path = "lastName", analyzer = StandardAnalyzer.class) })
	public Person getAuthor() {
		return author;
	}
	// .....
```
 
{{%note%}}
If the `@SpaceTextAnalyzer` annotation is omitted, the `StandardAnalyzer` is applied. 
{{%/note%}}

# Indexing

{{%refer%}}
The performance of text search queries can be vastly improved by indexing the relevant  properties. For detailed information see See [Indexing](./indexing-text-search.html) for more information.
{{%/refer%}}


 
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

// Simple 
gigaSpace.getTypeManager().registerTypeDescriptor(new SpaceTypeDescriptorBuilder(typeName).idProperty("id").create());
				
// Analyzer										
gigaSpace.getTypeManager().registerTypeDescriptor(new SpaceTypeDescriptorBuilder(typeName).idProperty("id")
				.addQueryExtensionInfo("content",LuceneTextSearchQueryExtensionProvider.analyzer(KeywordAnalyzer.class))
				.create());

// Nested Analyzer
gigaSpace.getTypeManager().registerTypeDescriptor(new SpaceTypeDescriptorBuilder(typeName).idProperty("id")
				.addQueryExtensionInfo("author.firstName",LuceneTextSearchQueryExtensionProvider.analyzer(KeywordAnalyzer.class))
				.addQueryExtensionInfo("author.LastName",LuceneTextSearchQueryExtensionProvider.analyzer(StandardAnalyzer.class)).create());
```

Search the space for SpaceDocuments:

```java
SQLQuery<SpaceDocument> query = new SQLQuery("NewsArticle", "content text:match ?").setParameter(1, "The quick brown fox jumps over the lazy dog");
SpaceDocument result = this.gigaSpace.read(query);

```

{{%refer%}}
Refer to [SpaceDocument](./document-overview.html) for more information on SpaceDocument.
{{%/refer%}}

# Configuration


|   Property   | Description |  Default |
|--------------|-------------|----------|
|lucene.storage.location        | The location of the lucene index|Deploy path of this space instance, when deployed in the service grid. When not deployed in the service grid <user.dir>/xap/full_text_search|
|lucene.storage.directory-type  | The directory type. Available values: MMapDirectory, RAMDirectory. | MMapDirectory|
|<nobr>lucene.max-uncommitted-changes<nobr> | The buffer size of uncommitted changes. When user write indexed document to the space, the document doesn’t flushes to the lucene index immediately. It flushes after search or after overflowing the buffer.| 1000 |
|lucene.max-results | The max number of the document retrieved from lucene during the search. | Integer.MAX_VALUE |


 