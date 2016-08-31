---
type: post
title:  What's New
categories: RELEASE_NOTES
parent: xap97.html
weight: 100
---

{{%ssummary%}}{{%/ssummary%}}

# Overview

This page lists the main new features in XAP 9.7 (Java and .Net editions). It's not an exhaustive list of all new features. For a full change log for 9.7 please refer to the [new features](97new-features.html) and [fixed issues](97new-features.html) pages. 

# MongoDB External Data Source

[MongoDB](http://www.mongodb.org) is an extremely popular and powerful NoSQL database. XAP 9.7 introduces full support for using Mongo as a write behind data store for the space, and as a source for initial data load and read through scenario. This means that you can now use Mongo as an extension to the space, allowing two-way integration between XAP and Mongo.

# LINQ Support for XAP.NET 

LINQ (which stands for Language-Integrated Query) is a feature of the .Net framework and allows the user to use SQL like syntax within their code to filter and select subsets information from various data sources (relational and non relational databases, arrays, collections, etc.). XAP.NET 9.7 implements a custom LINQ provider which allows the developer to use LINQ expressions to query the space. Here's an example:


```csharp
var query = from p in spaceProxy.Query<Person>() 
            where p.Name == "Smith" 
            select p; 

foreach (var person in query) 
{ 
    // ... 
} 
```

For more details please refer to the [LINQ reference page](/xap97net/query-linq.html) in the XAP.Net 9.7 documentation.

# Unique Indexes 

You can now add a uniqueness constraint for indexes fields, making sure that values stay unique for that fields in the boundaries of a single space partition. This available for both [Java](/xap97/indexing.html#unique-index) and [.Net](/xap97net/indexing-unique.html). Here's an example for how it's defined:

{{%tabs%}}

{{%tab "  Java "%}}

```java
@SpaceClass
public class Person
{
    ...

    @SpaceIndex(type=SpaceIndexType.BASIC, unique = true)
    private String lastName;

    ...
}
```
{{% /tab %}}

{{%tab "  .Net "%}}

```csharp
[SpaceClass]
public class Person
{
    ...

    [SpaceIndex(Type=SpaceIndexType.Basic, Unique=true)]
    public String LastName{ get; set; }

    ...
}

```
{{% /tab %}}
{{% /tabs %}}

# Deterministic Deployment across Zones 

Zones provide a powerful tagging mechanism for GSC which allow users to group together multiple GSCs and restrict processing unit instances only to these GSC. They also support SLA restrictions to make sure a primary and a backup of the same partition never end up in the same zone. This can be used to tag racks or data centers and make sure that high availability is maintained across them. When using the latter functionality, in many cases one zone has priority over another (e.g. the data center which is represents is geographically closer to your application's end users). 9.7 adds support for prioritized zones. When tagging as zone as prioritized for a specific processing unit, the GSM will extend its best effort to make sure that all primary instances of that processing unit will be started on the prioritized zone. You can refer to the [Deterministic Deployment Section](/xap97/configuring-the-processing-unit-sla.html#deterministic-deployment) in the docs for more details.

# Change API Enhancements
The change API now includes an option to get the previous value of a changed field. XAP 9.7 also includes a new mechanism called [Change Extension](/xap97/change-api.html) that encapsulates common usage patterns (such as Add and Get) into simpler to use API calls. Here's an example for an add and get operation that uses the change extension mechanism:


```java
GigaSpace space = // ... obtain a space reference
Integer id = ...;
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(WordCount.class, id, routing);
Integer newCounter = ChangeExtension.addAndGet(space, idQuery, "counter", 1);
```

# Advanced Projections 

[Projections](/xap97/query-partial-results.html) are supported since XAP 9.5, however it only supported the projection of top level properties (i.e. properties that are belong to the top level space class and not to a nested object). Starting from version 9.7, you can also project nested properties (e.g. `user.address.street`) and not just top level properties. This can help in reducing the amount of traffic and serialization overhead when querying for specific properties.


```java
SQLQuery<SpaceDocument> docQuery = new SQLQuery<SpaceDocument>(Person.class.getName() ,"",
	QueryResultType.DOCUMENT).setProjections("user.address.street", "user.address.zipCode");
SpaceDocument docresult[] = gigaSpace.readMultiple(docQuery);
```

# Immutable Objects Support

Up to version 9.7, every space property needed to have both a getter and a setter, which made it impossible to implement read only properties. In version 9.7, you can define space properties that have only a getter, or no accessors at all. Refer to the [Constructor Based Property Injection](/xap97/modeling-your-data.html) for more details.

# Grid Activity Breakdown by Client in the Web UI

The web UI has a new functionality that enables you to view the activity in the data grid (throughput and breakdown of operations, client libraries version, etc.).

# Better Separation of Thread Pools 

Starting from version 9.7, XAP implements separate thread pools for client notifications and task executions, and these operations no longer use the same thread pool as regular CRUD operations. This makes the space more robust and less sensitive to excessive load of tasks and notification clients.

# New Documentation Infrastructure and Enhanced Search 

If you got here, you've been using our new docs infrastructure. We hope you like it. We've significantly improved the visuals, search experience, and also beefed up some of the content (checkout [our all new XAP tutorial](/xap97/java-home.html) for example). Last, all of the documentation website source are [stored in a public github repo](http://github.com/gigaspaces/xap-docs) and are based on [Hugo](http://gohugo.io) and [Markdown](http://daringfireball.net/projects/markdown/). If you see an error or something you'd like to improve, you're more than welcome to fork this repository and submit pull requests.

![New Docs](/attachment_files/new-docs.png)




