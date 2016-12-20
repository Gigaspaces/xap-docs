---
type: post
title:  Modeling your data
categories: SBP
parent: data-access-patterns.html
weight: 1550
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim| 9.7 | March 2015|    |    |



# Moving from Centralized to Distributed Data Model

When moving from a centralized into a distributed data store, your data needs to be partitioned across multiple nodes (AKA partitions). Implementing the partitioning mechanism technically is not a hard task; however, planning the distribution of your data for scalability and performance, requires some thinking.

There are several questions which need to be answered when planning for data partitioning:

Question 1. What is the information I should store in memory? The answer to this question is not a technical one, and should not be mistakenly confused with the structure of the data. This is in essence a business question: How much the data will it grow over time? For how long should you keep it?

We recommend using the following table for this process:


|Data item|Estimated Quantity|Expected Growth|Estimated Object Size|
|:--------|:-----------------|:--------------|:--------------------|
|Data Type A|100K|10%|2K|
|Data Type B|200K|20%|4K|

Once you have identified the size and expected growth of your data, you can start thinking about partitioning it; however, there's more to consider before doing that.

Question 2. What are my application's use cases? While you might be used to model your data by the logical relationship of your data items, in the case of distributed data, you need to think differently. The rule of thumb here is to avoid cross cluster relationships as much as possible, since they will lead to cross cluster queries and updates which are usually much less scalable and fast than their local counterparts.

Thinking in terms of traditional relationships ("one to one", "one to many" and "many to many"), is deceiving with distributed data. The first question to ask is: How many different associations does each entity have?

If an entity is associated with several containers (parent entities), it can't be embedded within the containing entity. It might be also impossible to store it with all of its containers on the same partition. We have mentioned the concept of embedded relationships above, let us now explain this concept's implications on your application.

# The Space Data Store

A space may store many type of entities. A space can be compared to a database that may have many tables, in the same way a space may have many space classes. Practically there is no limit to the number of Entities (space classes or data types) you may store within a given space cluster. Each space class may have unlimited number of instances (space objects or entries).

{{%align center%}}
![in-line-cache.jpg](/attachment_files/in-line-cache.jpg)
{{%/align%}}

Unlike legacy caching products that promote a Map per Entity approach storage model, with the space data modeling approach, you can treat your entire application objects naturally having one global In-Memory data source regardless their data type.

# Embedded vs. Non-Embedded Relationships

## Embedded Relationships

With Embedded Relationships a parent object physically contains the associated object(s) and there is a **strong** lifecycle dependency between them - once you delete the containing object, you also delete all of its contained objects. With this type of object association, you are always ensuring a local transaction since the entire object graph is stored in the same entry within the Space.

{{%align center %}}
![model_embed.jpg](/attachment_files/model_embed.jpg)
{{%/align%}}

### Embedded Relationships Data Retrieval Flow

Fetching objects from the space when using the Embedded Relationships model done by using a [SQLQuery]({{%latestjavaurl%}}/query-sql.html) with the `readMultiple` call or the [IteratorBuilder]({{%latestjavaurl%}}/query-paging-support.html) when having large set of objects where the [SQLQuery]({{%latestjavaurl%}}/query-sql.html) predicate using root level or embedded objects properties. With a single `SQLQuery` you may specify a query that span objects from different data types related to each other contained in each other. The embedded objects may be elements within an array or any type of collection (List , Map) or just a simple referenced object.

### Updating Embedded objects

With earlier versions of XAP, when updating an object you had to read the entire object back to the client, get a property value, update it and later write back the object to the space. If you had to update a property within an embedded object, you had to navigate the object graph, access the property within the embedded object and update it before writing the entire space object. When having an object with many properties or many nested embedded objects, updating data may impose an overhead as it involves serialization of large amount of properties.

Starting with XAP 9.0 the [Change API]({{%latestjavaurl%}}/change-api.html) allows you to overcome this limitation and modify a specific property(s) within the root space object or any embedded object without reading the entire object graph in an atomic manner. This optimizes the amount of data transferred between the client and the primary space and also between the primary and backup when replicating updates.

With the embedded model, updating (as well adding or removing) a nested collection with large number of elements **must use the change API** since the default behavior would be to replicate the entire space object and its nested collection elements from the primary to the backup (or other replica primary copies when using the sync-replicate or the async-replicated cluster schema). The Change API reduces the CPU utilization at the primary side, reduce the serialization overhead and reduce the garbage collection activity both at the primary and backup. This improves the overall system stability significantly.

## Non-Embedded Relationships

With Non-Embedded Relationships a parent object is associated with a number of other objects, so you can navigate from one object to others. However, there is no life cycle dependency between them, so if you delete the referencing object (parent), you don't automatically delete the referenced (child) object(s). The association is therefore manifested in storing the child IDs in the parent rather than storing the actual associated object itself. This type of relationship means that you might want to access the child object separately  without accessing their parent objects. This approach avoid the need to duplicate child object in case these are references by more than a single parent object. This approach might enforce you to perform multiple space operations when accessing the entire parent-child graph across multiple space cluster partitions.

{{%align center%}}
![model_non_embed.jpg](/attachment_files/model_non_embed.jpg)
{{%/align%}}

### Non-Embedded Relationships Data Retrieval Flow

The following describes the different data modeling options available with Non-Embedded Relationships:

{{%align center%}}
![space-data-modeling-options.jpg](/attachment_files/space-data-modeling-options.jpg)
{{%/align%}}


#### Parent-First Data Retrieval Flow

With this approach you first retrieve an initial set of "root space objects" usually using a [SQLQuery]({{%latestjavaurl%}}/query-sql.html) or a [template]({{%latestjavaurl%}}/query-template-matching.html) with the `readMultiple` call or the [IteratorBuilder]({{%latestjavaurl%}}/query-paging-support.html) when having large set of objects and later using some meta data stored within these root space objects such as the ID or IDs of related objects and their routing field value (when having these distributed across remote multiple partitions) to fetch the related (child) objects. Fetching these should use the `readById` or `readByIds` calls. Both the `readById` or `readByIds` allows you to provide the routing field value avoiding the need to search the entire cluster for matching objects. You may also use the [Change API]({{%latestjavaurl%}}/change-api.html) call to modify specific child objects without even reading these first.

#### Child-First Data Retrieval Flow

With this approach you access the referenced (child) objects directly and from these access their parent object. With this flow the child object store the parent object ID (and routing field value). You query the space for child objects via some property(s) using a [SQLQuery]({{%latestjavaurl%}}/query-sql.html) or a [template]({{%latestjavaurl%}}/query-template-matching.html) with the `readMultiple` call , iterate over the child objects result set collecting getting the parent IDs and via the `readByIds` call read all relevant parent objects.

{{% tip %}}
Since version 9.5, XAP supports projections where you can read specific properties (delta read) instead of reading the entire space object content. This may optimize the data retrieval flow.
{{% /tip %}}

#### Parent-Child Bi-Directional Data Retrieval Flow

An hybrid approach of the Parent-First and Child-First involves having both the Parent storing the ID of the child objects and also having the child objects storing the ID of the parent object. With this approach you may choose the right data retrieval flow based on the business logic requirements which provide greater flexibility. Such model allows navigating from a child object to its sibling child via the common parent via 2 simple space calls. The downside of this approach is redundant meta-data maintained in memory and extra updates required when data is deleted and a transaction which space more objects. This impacts system concurrency level.

# Moving from Database Centric to Space Model

Many times you have an existing application that has been evolved around a database as its sole system of record. In such a case you might be using Hibernate (or some other mapping layer) to bridge between the Object model your application is using and the relational model the database is using. In some other cases you might be using JDBC API to access the database.

To leverage the space data modeling approach you will need to adapt your existing application entities to use the right data access routines. The Entity class should be modified to leverage the space data model and API. When the application using Hibernate for example, the changes can be done in a relatively transparent manner to the application itself. The [Moving from Hibernate to Space](/sbp/moving-from-hibernate-to-space.html) provides simple guide how to perform these changes at the Data Access Objects (DAO). You might be able to automate this process via auto-code generation or byte code manipulation.

# The Author and the Book Example

With the following example we have the **Author** and the **Book** entities. Here is how the original **Author** and the **Book** Entities looks like:


|Author|Book|
|:-----|:----|
|id:Integer| id:Integer|
|lastName:String| title:String|

## Example Code

You can [download](/sbp/download_files/Space-Data-Model-Example.zip) the code used with the example below. See `MainEmbeddedOne2Many` , `MainEmbeddedOne2One` , `MainNonEmbeddedOne2Many` , `MainNonEmbeddedOne2One` and `MainJDBC` demonstrating each scenario 	described below.

## Remote vs. Collocated Client

The examples below can be used with a client accessing a remote space or a "collocated client" running within the space - e.g. a [DistributedTask]({{%latestjavaurl%}}/task-execution-over-the-space.html) implementation or a [service]({{%latestjavaurl%}}/executor-based-remoting.html) method invoked in a broadcast mode. The collocated client will reduce the serialization and network overhead. When using the collocated client approach with the non-embedded model you should use the same [routing field]({{%latestjavaurl%}}/routing-in-partitioned-spaces.html) value for associated objects (parent-child).

## One-to-One Relationship

With this example there is One-to-One relationship between the **Author** and the **Book** entity - An Author may have one Book.

Users may search for:

- All **Book** titles written by an **Author** with a specific last name (there may be multiple matching Authors).
- An Author with a specific **Book**.


When using JDBC to query for all the **Books** an **Author** with a specific last name your SQL query would look like this:


```java
select Book.id , Author.id,Author.lastName from Book, Author WHERE Author.lastName='AuthorX' AND Book.authorId = Author.id
```

The main problem with this approach is the execution time. The more **Books** or **Authors** you have the time to execute the query will grow. Using the Space API with the embedded and non-embedded model will provide much better performance that will not be affected when having large amount of **Books** or **Authors** .

Let's compare the JDBC approach to the embedded and non-embedded model:

### Embedded Model

With the embedded model the root Space object is the **Author**. It has a **Book** object embedded. The representation of these Entities looks like this:

{{% accordion %}}
{{% accord title="Java"%}}
{{%tabs%}}

{{%tab "The Author Entity"%}}

```java
@SpaceClass
public class Author {
    Integer id;
    String lastName;
    Book book;

    @SpaceId
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceIndex
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@SpaceIndex(path = "title")
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
}
```
{{% /tab %}}

{{%tab "  The Embedded Book Entity "%}}

```java
public class Book implements Serializable{
	Integer id;
	String title;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
```
{{% /tab %}}

{{% /tabs %}}
{{% /accord %}}

{{% accord title="C#"%}}
{{%tabs%}}

{{%tab "  The Author Entity "%}}

```c#
[SpaceClass]
public class Author
{
    [SpaceID]
    public int Id { get; set; }

    [SpaceIndex]
    public string LastName { get; set; }

    [SpaceIndex(Path = "Title")]        
    [SpaceProperty(StorageType = StorageType.Document)]
    public Book Book { get; set; }
}
```
{{% /tab %}}

{{%tab "  The Embedded Book Entity "%}}

```c#
[Serializable]
public class Book
{
    public int Id { get; set; }

    public string Title { get; set; }
}
```
{{% /tab %}}

{{% /tabs %}}

{{% /accord %}}
{{% /accordion%}}

{{% tip %}}
See the how the book **Title** property is indexed within **Author** class.
{{% /tip %}}

To query for all the **Books** written by an **Author** with a specific last name your query code would look like this:

{{%tabs%}}

{{%tab "  Java "%}}

```java
SQLQuery<Author> query = new SQLQuery <Author>(Author.class , "lastName=?");
query.setParameter(1, "AuthorX");
Author authorFounds [] = space.readMultiple(query);
Set<Book> booksFound = new HashSet<Book> ();
for (int j = 0; j < authorFounds.length; j++) {
	booksFound.add(authorFounds[j].getBook());
}
return booksFound;
```
{{% /tab %}}

{{%tab "  C# "%}}

```c#
var books = new HashSet<Book>();

var query = new SqlQuery<Author>("LastName=?");
query.SetParameter(1, "AuthorX");

var authors = spaceProxy.ReadMultiple<Author>(query);

foreach (var author in authors)
{
    books.Add(author.Book);
}

return books;
```
{{% /tab %}}
{{% /tabs %}}


To query for an **Author** with a specific **Book** title the query would look like this:

{{%tabs%}}
{{%tab "  Java "%}}

```java
SQLQuery<Author> query = new SQLQuery <Author>(Author.class , "lastName=? and book.title="?");"%}}
query.setParameter(1, "AuthorX");
query.setParameter(2, "BookX");
Author authorFounds [] = space.readMultiple(query);
return authorFounds ;
```
{{% /tab %}}

{{%tab "  C# "%}}

```csharp
var query = new  SqlQuery<Author>("LastName=? and Book.Title=?");
query.SetParameter(1, "AuthorX");
query.SetParameter(2, "BookX");

Author[] authors = spaceProxy.ReadMultiple<Author>(query);
return authors;
```
{{% /tab %}}

{{% /tabs %}}


### Non-Embedded Model

With the non-Embedded model the **Author** and the **Book** would look like this - See how the ID of the Book is stored within the Author rather the Book object itself. It is stored as a separate Space object:

{{% accordion  %}}
{{% accord title="Java"%}}

{{%tabs%}}
{{%tab "  The Author Entity "%}}

```java
@SpaceClass
public class Author {
    Integer id;
    String lastName;
    Integer bookId;

    @SpaceId(autoGenerate=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceIndex
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

}
```
{{% /tab %}}

{{%tab "  The Book Entity "%}}

```java
@SpaceClass
public class Book {
	Integer id;
	Integer authorId;
	String title;

	@SpaceId (autoGenerate=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceIndex
	public Integer getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	@SpaceIndex
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
```
{{% /tab %}}

{{% /tabs %}}
{{% /accord %}}

{{% accord title="C#"%}}
{{%tabs%}}
{{%tab "  The Author Entity "%}}

```c#
[SpaceClass]
public class Author
{
    [SpaceID(AutoGenerate = false)]
    public int Id { get; set; }

    [SpaceIndex]
    public string LastName { get; set; }

    public int BookId { get; set; }
}
```
{{% /tab %}}

{{%tab "  The Book Entity "%}}

```c#
[SpaceClass]
public class Book
{
    [SpaceID(AutoGenerate = false)]
    public int Id { get; set; }

    [SpaceIndex]
    public int AuthorId { get; set; }

    [SpaceIndex]
    public string Title { get; set; }
}
```
{{% /tab%}}

{{% /tabs %}}
{{% /accord %}}

{{% /accordion %}}


To query for all the **Books** written by an **Author** with a specific last name your query code would look like this - See how the **readById** is used:

{{%tabs%}}

{{%tab "  Java "%}}

```java
SQLQuery<Author> query = new SQLQuery <Author>(Author.class , "lastName=?");
query.setParameter(1, "AuthorX");
Author authors [] = space.readMultiple(query);
ArrayList<Book> booksFound = new ArrayList<Book>() ;

// read the Author Book via its ID
for (int j=0;j<authors.length;j++)
{
    Book book = space.readById(Book.class , authors[j].getBookId());
    booksFound.add(book);
}
return booksFound;
```
{{% /tab %}}

{{%tab "  C# "%}}

```c#
var books = new HashSet<Book>();

var query = new SqlQuery<Author>("LastName=?");
query.SetParameter(1, "AuthorX");

Author[] authors = spaceProxy.ReadMultiple<Author>(query);

foreach (Author author in authors)
{
   books.Add(spaceProxy.ReadById<Book>(author.BookId));
}

return books;
```
{{% /tab %}}

{{% /tabs %}}

{{% tip %}}
See the [Id Queries]({{%latestjavaurl%}}/query-by-id.html) page for more details how `readById` can be used.
{{% /tip %}}

To query for a specific **Author** with a specific **Book** title the query code would look like this:

{{%tabs%}}

{{%tab "  Java "%}}

```java
String authoridsForTitle = "";
SQLQuery<Book> bookQuery = new SQLQuery <Book>(Book.class , "title="?");"%}}
bookQuery.setParameter(1, "BookX");
Book booksFounds [] = space.readMultiple(bookQuery);
for (int j = 0; j < booksFounds.length; j++) {
	Book book = booksFounds[j];
	authoridsForTitle = authoridsForTitle + book.getAuthorId().toString() ;
	if ((j +1)!= booksFounds.length)
		authoridsForTitle = authoridsForTitle + ",";
}

SQLQuery<Author> query = new SQLQuery <Author>(Author.class , "lastName=? AND id IN ("+ authoridsForTitle+")");
query.setParameter(1, "AuthorX");
Author authorFounds [] = space.readMultiple(query);
return authorFounds ;
```
{{% /tab %}}

{{%tab "  C# "%}}

```c#
var authorIds = new StringBuilder();

var bookQuery = new SqlQuery<Book>("Title=?");
bookQuery.SetParameter(1, "BookX");

var books = spaceProxy.ReadMultiple<Book>(bookQuery);

foreach (var book in books)
{
    authorIds.AppendFormat(",{0}", book.AuthorId);
}

var inCriteria = authorIds.ToString().TrimStart(',');
var authorQuery = new SqlQuery<Author>(string.Format("LastName=? AND Id IN ({0})", inCriteria));
authorQuery.SetParameter(1, "AuthorX");

var authors = spaceProxy.ReadMultiple<Author>(authorQuery);
return authors;

```
{{% /tab %}}

{{% /tabs %}}


## One-to-Many Relationship

With this example there is One-to-Many relationship between the **Author** and the **Book** entity - An Author may write many Books.

Users may search for:

- All **Book** titles written by an **Author** with a specific last name (there may be multiple matching Authors).
- An Author with a specific **Book**.


When using JDBC to query for all the **Books** an **Author** with a specific last name your query code would look like this:


```java
select Book.id , Author.id,Author.lastName from Book, Author WHERE Author.lastName='AuthorX' AND Book.authorId = Author.id
```

The main problem with this approach is the execution time. The more **Books** or **Authors** you have the time to execute the query will grow. Using the Space API with the embedded and non-embedded model will provide much better performance that will not be affected when having large amount of **Books** or **Authors** .

Let's compare the JDBC approach to the embedded and non-embedded model:

### Embedded Model

With the embedded model the root Space object is the **Author**. It has a **Book** collection embedded. The representation of these Entities looks like this:

{{% accordion  3%}}
{{% accord title="Java"%}}
{{%tabs%}}
{{%tab "  The Author Entity "%}}

```java
@SpaceClass
public class Author {
    Integer id;
    String lastName;
    List<Book> books;

    @SpaceId
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceIndex
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@SpaceIndex(path = "[*].title")
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
}
```
{{% /tab %}}
{{%tab "  The Embedded Book Entity "%}}

```java
public class Book implements Serializable{
	Integer id;
	String title;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
```
{{% /tab %}}
{{% /tabs %}}
{{% /accord %}}
{{% accord title="C#"%}}
{{%tabs%}}
{{%tab "  The Author Entity "%}}

```c#
[SpaceClass]
public class Author
{
    [SpaceID]
    public int Id { get; set; }

    [SpaceIndex]
    public string LastName { get; set; }

    [SpaceIndex(Path="[*].Title")]
    [SpaceProperty(StorageType = StorageType.Document)]
    public IList<Book> Books { get; set; }
}
```
{{% /tab %}}
{{%tab "  The Embedded Book Entity  "%}}

```c#
[Serializable]
public class Book
{
    public int Id { get; set; }

    public string Title { get; set; }
}
```
{{% /tab %}}
{{% /tabs %}}
{{% /accord %}}

{{% /accordion %}}

{{% tip %}}
See the how the book **Title** property is indexed within **Author** class.
{{% /tip %}}

To query for all the **Books** written by an **Author** with a specific last name your query code would look like this:

{{%tabs%}}

{{%tab "  Java "%}}

```java
Set<Book> booksFound = new HashSet<Book> ();
SQLQuery<Author> query = new SQLQuery <Author>(Author.class , "lastName=?");
query.setParameter(1, "AuthorX");
Author authorFounds [] = space.readMultiple(query);
for (int j = 0; j < authorFounds.length; j++) {
	booksFound.addAll(authorFounds[j].getBooks());
}
return booksFound;
```
{{% /tab %}}

{{%tab "  C# "%}}

```c#
var books = new List<Book>();

var authorQuery = new SqlQuery<Author>("LastName=?");
authorQuery.SetParameter(1, "AuthorX");
var authors = spaceProxy.ReadMultiple<Author>(authorQuery);

foreach (var author in authors)
{
    books.AddRange(author.Books);
}

return books;
```
{{% /tab %}}

{{% /tabs %}}


To query for an **Author** with a specific **Book** title the query would look like this:

{{%tabs%}}

{{%tab "  Java "%}}

```java
SQLQuery<Author> query = new SQLQuery <Author>(Author.class , "lastName=? and books[*].title="?");"%}}
query.setParameter(1, "AuthorX");
query.setParameter(2, "BookY");
Author authorFounds [] = space.readMultiple(query);
return authorFounds;
```
{{% /tab %}}

{{%tab "  C# "%}}

```c#
var authorQuery = new SqlQuery<Author>("LastName=? AND Books[*].Title=?");
authorQuery.SetParameter(1, "AuthorX");
authorQuery.SetParameter(2, "BookY");
var authors = spaceProxy.ReadMultiple<Author>(authorQuery);

return authors;
```
{{% /tab %}}

{{% /tabs %}}


### Non-Embedded Model

With the non-Embedded model the **Author** and the **Book** would look like this - See how the IDs of the Books are stored within the Author object rather than the Books themselves. These are stored as separate Space objects:

{{% accordion  %}}
{{% accord title="Java"%}}
{{%tabs%}}
{{%tab "  The Author Entity "%}}

```java
@SpaceClass
public class Author {
    Integer id;
    String lastName;
    List<Integer> bookIds;

    @SpaceId(autoGenerate=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceIndex
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Integer> getBookIds() {
		return bookIds;
	}
	public void setBookIds(List<Integer> bookIds) {
		this.bookIds = bookIds;
	}

}
```
{{% /tab %}}

{{%tab "  The Book Entity "%}}

```java
@SpaceClass
public class Book {
	Integer id;
	Integer authorId;
	String title;

	@SpaceId (autoGenerate=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceIndex
	public Integer getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	@SpaceIndex
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
```
{{% /tab %}}
{{% /tabs %}}
{{% /accord %}}

{{% accord title="C#"%}}
{{%tabs%}}
{{%tab "  The Author Entity "%}}

```c#
[SpaceClass]
public class Author
{
    [SpaceID(AutoGenerate = false)]
    public int Id { get; set; }

    [SpaceIndex]
    public string LastName { get; set; }

    public IList<int> BookIds { get; set; }
}
```
{{% /tab %}}

{{%tab "  The Book Entity "%}}

```c#
[SpaceClass]
public class Book
{
    [SpaceID(AutoGenerate = false)]
    public int Id { get; set; }

    [SpaceIndex]
    public int AuthorId { get; set; }

    [SpaceIndex]
    public string Title { get; set; }
}
```
{{% /tab %}}
{{% /tabs %}}
{{% /accord %}}

{{% /accordion %}}


To query for all the **Books** written by an **Author** with a specific last name your query code would look like this - See how the **readByIds** is used:

{{%tabs%}}
{{%tab "  Java "%}}

```java
SQLQuery<Author> query = new SQLQuery <Author>(Author.class , "lastName=?");
query.setParameter(1, "AuthorX");
Author authors [] = space.readMultiple(query);
ArrayList<Book> booksFound = new ArrayList<Book>() ;

// read all the Author Books via their IDs
for (int j=0;j<authors.length;j++)
{
	Integer ids [] = new Integer[authors[j].getBookIds().size()];
	ids  = authors[j].getBookIds().toArray(ids);
	Iterator<Book> bookIter = space.readByIds(Book.class ,ids).iterator();
	while (bookIter.hasNext()) {
		booksFound.add((Book) bookIter.next());
	}
}
return booksFound;
```
{{% /tab %}}

{{%tab " C# "%}}

```c#
var authorQuery = new SqlQuery<Author>("LastName=?");
authorQuery.SetParameter(1, "AuthorX");
var authors = spaceProxy.ReadMultiple<Author>(authorQuery);

var books = new List<Book>();

foreach (var author in authors)
{
    books.AddRange(spaceProxy.ReadByIds<Book>(author.BookIds.Cast<object>().ToArray()));
}


return books;
```
{{% /tab %}}

{{% /tabs %}}

{{% tip %}}
See the [Id Queries]({{%latestjavaurl%}}/query-by-id.html) page for more details how `readByIds` can be used.
{{% /tip %}}

To query for a specific **Author** with a specific **Book** title the query would look like this:

{{%tabs%}}
{{%tab "  Java "%}}

```java
SQLQuery<Book> bookQuery = new SQLQuery <Book>(Book.class , "title="?");"%}}
bookQuery.setParameter(1, "BookX");
Book booksFounds [] = space.readMultiple(bookQuery);
String authoridsForTitle="";
for (int j = 0; j < booksFounds.length; j++) {
	Book book = booksFounds[j];
	authoridsForTitle = authoridsForTitle + book.getAuthorId().toString() ;
	if ((j +1)!= booksFounds.length)
		authoridsForTitle = authoridsForTitle + ",";
}

SQLQuery<Author> query = new SQLQuery <Author>(Author.class , "lastName=? AND id IN ("+ authoridsForTitle+")");
query.setParameter(1, "AuthorX");
Author authorFounds [] = space.readMultiple(query);
return authorFounds ;
```
{{% /tab %}}

{{%tab "  C# "%}}

```csharp
var bookQuery = new SqlQuery<Book>("Title=?");
bookQuery.SetParameter(1, "BookX");
var books = spaceProxy.ReadMultiple<Book>(bookQuery);

var authorIds = new StringBuilder();

foreach (var book in books)
{
    authorIds.AppendFormat(",{0}", book.AuthorId);
}

var authorQueryCriteria = authorIds.ToString().TrimStart(',');
var authorQuery = new SqlQuery<Author>(string.Format("LastName=? AND Id in ({0})", authorQueryCriteria));
authorQuery.SetParameter(1, "AuthorX");

var authors = spaceProxy.ReadMultiple<Author>(authorQuery);

return authors;
```
{{% /tab %}}

{{% /tabs %}}

## Many-to-Many Relationship

With this example there is Many-to-Many relationship between the **Author** and the **Book** entity - an Author may write many Books, a Book may be written by many Authors.

Users may search for:

- All **Book** titles written by an **Author** with a specific last name (there may be multiple matching Authors).
- An Author with a specific **Book**.

To model this in SQL, you would need an additional table that would link an Author and his or her Books (or in other words a Book and its Authors) - each entry in such a table would contain a foreign key to Author table and a foreign key to Book table. When using JDBC to query for all the **Books** written by an **Author** with a specific last name your SQL query would look like this:


```java
select Book.id, Author.id, Author.lastName from Book, Author, AuthorBookLink WHERE Author.lastName='AuthorX' AND Author.id = AuthorBookLink.authorId AND AuthorBookLink.bookId = Book.id
```

The main problem with this approach is the execution time. The more Books or Authors you have the time to execute the query will grow. Using the Space API with the non-embedded model will provide much better performance that will not be affected when having large amount of Books or Authors.

Let's compare the JDBC approach to the embedded and non-embedded model:

### Embedded Model

With the embedded model the root Space object is the **Author**. It has a **Book** collection embedded. The representation of these Entities looks like this:

{{% accordion %}}
{{% accord title="Java"%}}
{{%tabs%}}
{{%tab "  The Author Entity "%}}

```java
@SpaceClass
public class Author {
    Integer id;
    String lastName;
    List<Book> books;

    @SpaceId
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceIndex
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@SpaceIndex(path = "[*].title")
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
}
```
{{% /tab %}}
{{%tab "  The Embedded Book Entity "%}}

```java
public class Book implements Serializable {
	Integer id;
	String title;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
```
{{% /tab %}}
{{% /tabs %}}
{{% /accord %}}
{{% accord title="C#"%}}
{{%tabs%}}
{{%tab "  The Author Entity "%}}

```csharp
[SpaceClass]
public class Author
{
    [SpaceID]
    public int Id { get; set; }

    [SpaceIndex]
    public string LastName { get; set; }

    [SpaceIndex(Path="[*].Title")]
    [SpaceProperty(StorageType = StorageType.Document)]
    public IList<Book> Books { get; set; }
}
```
{{% /tab %}}
{{%tab "  The Embedded Book Entity  "%}}

```csahrp
[Serializable]
public class Book
{
    public int Id { get; set; }

    public string Title { get; set; }
}
```
{{% /tab %}}
{{% /tabs %}}
{{% /accord %}}

{{% /accordion %}}

### Non-Embedded Model

With the non-Embedded model the **Author** and the **Book** would look like this. Note that there additional entity expressing relation between **Author** and **Book** - **AuthorBookLink**. In this model **Books** are stored as separate Space objects:

{{% accordion%}}
{{% accord title="Java"%}}
{{%tabs%}}
{{%tab "  The Author Entity "%}}

```java
@SpaceClass
public class Author {
    Integer id;
    String lastName;

    @SpaceId
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceIndex
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
```
{{% /tab %}}

{{%tab "  The Book Entity "%}}

```java
@SpaceClass
public class Book {
	Integer id;
	String title;

	@SpaceId
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceIndex
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
```
{{% /tab %}}

{{%tab "  The AuthorBookLink Entity "%}}

```java
@SpaceClass
public class AuthorBookLink {
	Integer id;
    Integer authorId;
    Integer bookId;

	@SpaceId
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

    @SpaceIndex
    public Integer getAuthorId() {
        return authorId;
    }
    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @SpaceIndex
    public Integer getBookId() {
        return bookId;
    }
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
}
```
{{% /tab %}}

{{% /tabs %}}
{{% /accord %}}

{{% accord title="C#"%}}
{{%tabs%}}
{{%tab "  The Author Entity "%}}

```c#
[SpaceClass]
public class Author
{
    [SpaceID]
    public int? Id { get; set; }

    [SpaceIndex]
    public string LastName { get; set; }
}
```
{{% /tab %}}

{{%tab "  The Book Entity "%}}

```c#
[SpaceClass]
public class Book
{
	[SpaceID]
    public int? Id { get; set; }

	[SpaceIndex]
    public string Title { get; set; }
}
```
{{% /tab %}}
{{%tab "  The AuthorBookLink Entity "%}}

```c#
[SpaceClass]
public class AuthorBookLink
{
	[SpaceID]
    public int? Id { get; set; }

	[SpaceIndex]
    public int? AuthorId { get; set; }
    
	[SpaceIndex]
    public int? BookId { get; set; }
}
```
{{% /tab %}}
{{% /tabs %}}
{{% /accord %}}

{{% /accordion %}}

{{% tip %}}
See the [Id Queries]({{%latestjavaurl%}}/query-by-id.html) page for more details how `readByIds` can be used.
{{% /tip %}}

{{% tip %}}
**More Examples**
See the [SQLQuery]({{%latestjavaurl%}}/query-sql.html) section for details about embedded entities query and indexing.
See the [Parent Child Relationship](./parent-child-relationship.html) for an example for non-embedded relationships.
{{% /tip %}}


## Real World Example

In the [Pet Clinic application](http://www.openspaces.org/display/DAE/GigaSpaces+PetClinic) that is based on the [Spring pet clinic sample](http://static.springsource.org/docs/petclinic.html), a Pet is only associated with an Owner. We can therefore store each Pet with its owner on the same partition. We can even embed the Pet object within the physical Owner entry.

{{%align center%}}
![petclinic_class_model.gif](/attachment_files/petclinic_class_model.gif)
{{%/align%}}

However, if a Pet would have been associated with a Vet as well, we could have certainly not embedded the Pet in the Vet physical entry (without duplicating each Pet entry) and could not even store the Pet and its Vet in the same partition.

# Thumb Rules for Choosing Embedded Relationships

- Embed when an entity is meaningful only with the context of its containing object. For example, in the petclinic application - a Pet has a meaning only when it has an Owner. A Pet in itself is meaningless without an Owner in this specific application. There is no business scenario for transferring a Pet from owner to owner or admitting a Pet to a Vet without the owner.
- Embedding may sometimes mean duplicating your data. For example, if you want to reference a certain Visit from both the Pet and Vet class, you'll need to have duplicate Visit entries. So let's look into duplication:
    - Duplication means preferring scalability over footprint - the reason to duplicate is to avoid cluster wide transactions and in many cases it's the only way to partition your object in a scalable manner.
    - Duplication means higher memory consumption: While memory is considered a commodity and low cost today, duplication has a bigger price to pay - you might have two space objects that contain the same data.
    - Duplication means more lenient consistency. When you add a Visit to a Pet and Vet for example, you need to update them both. You can do it in one (potentially distributed) transaction, or in two separate transactions, which will scale better but be less consistent. This may be sufficient for many types of applications (e.g. social networks), where losing a post, although undesired, does not incur significant damage. In contrast, this is not feasible for financial applications where every operation should be accounted for.
