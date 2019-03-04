---
type: post
title:  Modeling your Data in a Distributed Environment
categories: SBP
parent: data-access-patterns.html
weight: 10
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim| 9.7 | March 2015|    | [Space data model examples](/download_files//sbp/Space-Data-Model-Example.zip) |



# Moving from a Centralized to a Distributed Data Model

When moving from a centralized into a distributed data store, your data must be partitioned across multiple nodes (or partitions). Implementing the partitioning mechanism isn't a technically difficult task; however, planning the distribution of your data for scalability and performanc, requires some forethought.

There are several questions that should be answered when planning your data partitioning:

**Question 1**

*What information I should store in memory?*

The answer to this question is not technical, and should not be confused with the structure of the data. This is in essence a business question; how much the data will it grow over time, and for how long should it be kept? 

You can use the following table to estimate the answer:


|Data Item|Estimated Quantity|Expected Growth|Estimated Object Size|
|:--------|:-----------------|:--------------|:--------------------|
|Data Type A|100K|10%|2K|
|Data Type B|200K|20%|4K|

After you have identified the size and expected growth of your data, you can start thinking about partitioning it.

**Question 2**

*What are my application's use cases?*

A common approach is to model data according to the logical relationship of the data items. However, for distributed data a different approach is needed. The rule of thumb is to avoid cross-cluster relationships as much as possible, because they can lead to cross-cluster queries and updates that are usually much less scalable and slower than their local counterparts.

It is deceptive to think in terms of traditional relationships ("one to one", "one to many" and "many to many") with distributed data. The first issue to consider is how many different associations each entity has. If an entity is associated with several containers (parent entities), that entity can't be embedded within the containing entity. It might be also impossible to store the entity with all of its containers on the same partition.

To answer this question effectively, you need to understand the implications of embedded relationships regarding your application. This concept is explained in further detail below.

# The Space Data Store

A Space can store many type of entities. The Space can be compared to a database that can contain many tables, in the same way that a Space can contain many space classes. Practically speaking, there is no limit to the number of entities (Space classes or data types) you can store within a given Space cluster. Each Space class can contain an unlimited number of instances (Space objects or entries).

{{%align center%}}
![in-line-cache.jpg](/attachment_files/in-line-cache.jpg)
{{%/align%}}

Unlike legacy caching products that promote a Map-per-Entity storage model, with the Space data modeling approach you can treat all your application objects naturally, having one global in-memory data source regardless of their data type.

# Embedded vs. Non-Embedded Relationships

## Embedded Relationships

With Embedded Relationships, a parent object physically contains the associated object(s) and there is a **strong** lifecycle dependency between them; when you delete the parent object, you also delete all of its contained objects. With this type of object association, all transactions are local because the entire object graph is stored in the same entry within the Space.

{{%align center %}}
![model_embed.jpg](/attachment_files/model_embed.jpg)
{{%/align%}}

### Embedded Relationship Data Retrieval Flow

When using the Embedded Relationship model, fetching objects from the Space is done using a [SQLQuery]({{%latestjavaurl%}}/query-sql.html) with the `readMultiple` call, or the [IteratorBuilder]({{%latestjavaurl%}}/query-paging-support.html) when you have large sets of objects where the [SQLQuery]({{%latestjavaurl%}}/query-sql.html) predicate uses root level or embedded object properties. With a single `SQLQuery`, you can specify a query that spans objects from different data types related to each other or contained in each other. The embedded objects can be elements within an array, any type of collection (List, Map), or just a simple referenced object.

### Updating Embedded Objects

The [Change API]({{%latestjavaurl%}}/change-api.html) allows you to modify a specific property(s) within the root space object (or any embedded object) without reading the entire object graph in an atomic manner. This optimizes the amount of data transferred between the client and the primary Space, and also between the primary and backup instances when replicating updates.

With the embedded model, updating (as well adding or removing) a nested collection with large number of elements **must use the Change API**, because the default behavior is to replicate the entire Space object and its nested collection elements from the primary instance to the backup (or other replica primary copies when using the sync-replicate or the async-replicated cluster schema). The Change API reduces CPU utilization on the primary side, the serialization overhead, and the garbage collection activity on both the primary and backup instances, which significantly improves overall system stability.

## Non-Embedded Relationships

With Non-Embedded Relationships a parent object is associated with a number of other objects, so you can navigate from one object to others. However, there is no life cycle dependency between them, so if you delete the referencing object (parent), you don't automatically delete the referenced (child) object(s). The association is therefore manifested in storing the child IDs in the parent rather than storing the actual associated object itself. This type of relationship means that you may want to access the child object separately without accessing their parent objects. This approach avoids the need to duplicate child objects if there are references from multiple parent objects. This approach may force you to perform multiple Space operations when accessing the entire parent-child graph across multiple Space cluster partitions.

{{%align center%}}
![model_non_embed.jpg](/attachment_files/model_non_embed.jpg)
{{%/align%}}

### Non-Embedded Relationship Data Retrieval Flow

The following topics describe the different data modeling options available with Non-Embedded Relationships.

{{%align center%}}
![space-data-modeling-options.jpg](/attachment_files/space-data-modeling-options.jpg)
{{%/align%}}


#### Parent-First Data Retrieval Flow

With this approach you first retrieve an initial set of "root space objects", usually using a [SQLQuery]({{%latestjavaurl%}}/query-sql.html) or a [template]({{%latestjavaurl%}}/query-template-matching.html) with the `readMultiple` call or the [IteratorBuilder]({{%latestjavaurl%}}/query-paging-support.html) if you have a large set of objects. After that, you use metadata stored within these root space objects, such as the ID or IDs of related objects, and their routing field values (if they are distributed across remote multiple partitions) to fetch the related (child) objects using the `readById` or `readByIds` calls. Both `readById` and `readByIds` allow you to provide the routing field value, so there is no need to search the entire cluster for matching objects. You can also use the [Change API]({{%latestjavaurl%}}/change-api.html) call to modify specific child objects without reading them first.

#### Child-First Data Retrieval Flow

With this approach the child object stores the parent object ID (and routing field value). You can access the referenced (child) objects directly, and from them you can access their parent object. You can query the Space for child objects via specific properties using a [SQLQuery]({{%latestjavaurl%}}/query-sql.html) or a [template]({{%latestjavaurl%}}/query-template-matching.html) with the `readMultiple` call, iterate over the child object result set to collect the parent IDs, and read all relevant parent objects via the `readByIds` call .

{{% tip %}}
The data grid supports projections where you can read specific properties (delta read) instead of reading the entire Space object content. This may optimize the data retrieval flow.
{{% /tip %}}

#### Parent-Child Bi-Directional Data Retrieval Flow

This is a hybrid approach of the Parent-First and Child-First flows, in which the parent stores the ID of the child objects and the child objects store the ID of the parent object. This enables you to choose the appropriate data retrieval flow based on the business logic requirements, which provides greater flexibility. The bi-directional model allows navigating from a child object to its sibling child via the common parent via two simple Space calls. The downside of this approach is redundant metadata maintained in memory, and extra updates required when data is deleted and a transaction which space more objects. This also affects the system concurrency level.

# Moving from a Database-Centric to a Space Model

If you have an existing application that evolved with a database as its sole system of record, you may be using Hibernate (or some other mapping layer) to bridge between the object model your application uses and the relational model the database  uses. In other cases, you may be using a JDBC API to access the database.

To leverage the Space data modeling approach, you must adapt your existing application entities to use the appropriate data access routines. The entity class should be modified to leverage the Space data model and API. If your application uses Hibernate for example, the changes can be done in a way that is relatively transparent to the application itself. The [Moving from Hibernate to Space](/sbp/moving-from-hibernate-to-space.html) topic explains how to perform these changes in the Data Access Objects (DAO). You may also be able to automate this process via auto-code generation or byte code manipulation.

# Author and Book Example

In the following example, we have **Author** and **Book** entities. This is how the original **Author** and the **Book** Entities look:


|Author|Book|
|:-----|:----|
|id:Integer| id:Integer|
|lastName:String| title:String|

## Example Code

You can [download](/download_files//sbp/Space-Data-Model-Example.zip) the code used with the examples below. See `MainEmbeddedOne2Many`, `MainEmbeddedOne2One`, `MainNonEmbeddedOne2Many`, `MainNonEmbeddedOne2One`, and `MainJDBC` that demonstrate each scenario described below.

## Remote vs. Collocated Client

The examples below can be used with a client accessing a remote space or a co-located client running within the Space, such as a [DistributedTask]({{%latestjavaurl%}}/task-execution-over-the-space.html) implementation or a [service]({{%latestjavaurl%}}/executor-based-remoting.html) method invoked in a broadcast mode. The co-located client reduces the serialization and network overhead. When using the co-located client approach with the non-embedded model, you should use the same [routing field]({{%latestjavaurl%}}/routing-in-partitioned-spaces.html) value for associated objects (parent-child).

## One-to-One Relationship

With this example, there is a one-to-one relationship between the **Author** and the **Book** entity; An author may have one (1) book.

Users can search for:

- All **Book** titles written by an **Author** with a specific last name (there may be multiple matching authors).
- An **Author** with a specific **Book**.


When using JDBC to query for all the **Books** related to an **Author** with a specific last name, the SQL query will look like this:


```java
select Book.id , Author.id,Author.lastName from Book, Author WHERE Author.lastName='AuthorX' AND Book.authorId = Author.id
```

The main problem with this approach is the execution time. The more **Books** or **Authors** you have, the greater the time required to execute the query. Using the Space API with the embedded and non-embedded model provides much better performance, which isn't affected if there are many **Books** or **Authors**.

We can compare the JDBC approach to the embedded and non-embedded models.

### Embedded Model

In the embedded model, the root Space object is the **Author**, and has a **Book** object embedded. The representation of these entities looks like this:

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

To query for all the **Books** written by an **Author** with a specific last name, the query code should look like this:

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


To query for an **Author** with a specific **Book** title, the query should look like this:

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

With the non-embedded model, the **Author** and the **Book** entities look like this; the ID of the book is stored within the author, rather than in the book object itself. It is stored as a separate Space object:

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


To query for all the **Books** written by an **Author** with a specific last name, the query code should look like this (see how the **readById** call is used):

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

{{% note %}}
See the [ID Queries]({{%latestjavaurl%}}/query-by-id.html) topic for more information on how the `readById` call can be used.
{{% /note %}}

To query for a specific **Author** with a specific **Book** title, the query code should look like this:

{{%tabs%}}

{{%tab "  Java "%}}

```java
String authoridsForTitle = "";
SQLQuery<Book> bookQuery = new SQLQuery <Book>(Book.class , "title="?");
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

In this example, there is one-to-many relationship between the **Author** and the **Book** entities; an author may write many books.

Users can search for:

- All **Book** titles written by an **Author** with a specific last name (there may be multiple matching authors).
- An **Author** with a specific **Book**.

When using JDBC to query for all the **Books** related to an **Author** with a specific last name, the query code should look like this:


```java
select Book.id , Author.id,Author.lastName from Book, Author WHERE Author.lastName='AuthorX' AND Book.authorId = Author.id
```

The main problem with this approach is the execution time. The more **Books** or **Authors** you have, the greater the time required to execute the query. Using the Space API with the embedded and non-embedded model provides much better performance, which isn't affected if there are many **Books** or **Authors**.

We can compare the JDBC approach with the embedded and non-embedded models.

### Embedded Model

In the embedded model the root Space object is the **Author**, and it has a **Book** collection embedded. The representation of these entities looks like this:

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
See the how the book **Title** property is indexed within the **Author** class.
{{% /tip %}}

To query for all the **Books** written by an **Author** with a specific last name, the query code should look like this:

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


To query for an **Author** with a specific **Book** title, the query should look like this:

{{%tabs%}}

{{%tab "  Java "%}}

```java
SQLQuery<Author> query = new SQLQuery <Author>(Author.class , "lastName=? and books[*].title="?");
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

In the non-embedded model, the **Author** and the **Book** look like this; the IDs of the books are stored within the author object rather than in the books themselves. These are stored as separate Space objects:

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


To query for all the **Books** written by an **Author** with a specific last name, the query code should look like this (see how the **readByIds** call is used):

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

{{% note %}}
See the [ID Queries]({{%latestjavaurl%}}/query-by-id.html) topic for more information about how the `readByIds` call can be used.
{{% /note %}}

To query for a specific **Author** with a specific **Book** title, the query should look like this:

{{%tabs%}}
{{%tab "  Java "%}}

```java
SQLQuery<Book> bookQuery = new SQLQuery <Book>(Book.class , "title="?");
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

In this example, there is many-to-many relationship between the **Author** and the **Book** entity; an author may write many books, and a book may be written by multiple authors.

Users can search for:

- All **Book** titles written by an **Author** with a specific last name (there may be multiple matching authors).
- An **Author** with a specific **Book**.

To model this in SQL, you need an additional table that links an author with the related books (or in other words, a book and its authors). Each entry in this table contains a foreign key to the Author table, and a foreign key to the Book table. When using JDBC to query for all the **Books** written by an **Author** with a specific last name, the SQL query should look like this:


```java
select Book.id, Author.id, Author.lastName from Book, Author, AuthorBookLink WHERE Author.lastName='AuthorX' AND Author.id = AuthorBookLink.authorId AND AuthorBookLink.bookId = Book.id
```

The main problem with this approach is the execution time. The more **Books** or **Authors** you have, the greater the time required to execute the query. Using the Space API with the non-embedded model provides much better performance, which isn't affected if there are many **Books** or **Authors**.

We can compare the JDBC approach to the embedded and non-embedded model.

### Embedded Model

In the embedded model, the root Space object is the **Author** and it has a **Book** collection embedded. The representation of these entities looks like this:

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

In the non-embedded model, the **Author** and the **Book** look like this. Note that there additional entities expressing a relationship between **Author** and **Book**; **AuthorBookLink**. In this model, **Books** are stored as separate Space objects:

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

{{% note %}}
See the [ID Queries]({{%latestjavaurl%}}/query-by-id.html) topic for more information on how the `readByIds` call can be used.
{{% /note %}}

{{% tip %}}
More examples are available in the [SQLQuery]({{%latestjavaurl%}}/query-sql.html) topic, which provides details about query and indexing embedded entities. Additionally, the [Parent Child Relationship](./parent-child-relationship.html) page contains an example of non-embedded relationships.
{{% /tip %}}


## Real World Example

In the [Pet Clinic application](http://www.openspaces.org/display/DAE/GigaSpaces+PetClinic) that is based on the [Spring pet clinic sample](https://github.com/spring-projects/spring-petclinic), a **Pet** is only associated with an **Owner**. We can therefore store each **Pet** with its **Owner** on the same partition. We can even embed the **Pet** object within the physical **Owner** entry.

{{%align center%}}
![petclinic_class_model.gif](/attachment_files/petclinic_class_model.gif)
{{%/align%}}

However, if a **Pet** is also associated with a **Vet**, we can't embed the **Pet** in the **Vet** physical entry (without duplicating each Pet entry), and can't even store the **Pet** and its **Vet** in the same partition.

# Guidelines for Choosing Embedded Relationships

- Embed when an entity is meaningful only with the context of its containing object. For example, in the Pet Clinic application, the **Pet** entity has a meaning only when it is associated with an **Owner**. A **Pet** by itself is meaningless without an **Owner** in this specific application. There is no business scenario for transferring a **Pet** from one**Owner** to another **Owner**, or for admitting a **Pet** to a **Vet** without an **Owner**.
- Embedding may sometimes mean duplicating your data. For example, if you want to reference a certain **Visit** from both the **Pet** and **Vet** classes, you'll need duplicate **Visit** entries.

Regarding duplication:

- Duplication means preferring scalability over footprint. The reason for duplicating is to avoid cluster-wide transactions and in many cases, it is the only way to partition your object in a scalable manner.
- Duplication means higher memory consumption. While memory is considered a low-cost commodity today, duplication has a hidden cost because you may have two Sspace objects that contain the same data.
- Duplication means more lenient consistency. When you add a **Visit** to a **Pet** and a **Vet**, for example, you must update them both. You can do this in one (potentially distributed) transaction, or in two separate transactions, which will scale better but be less consistent. This may be sufficient for many types of applications, such as on social networks where losing a post, although undesired, does not incur significant damage. In contrast, this is not feasible for financial applications where every operation must be accounted for.
