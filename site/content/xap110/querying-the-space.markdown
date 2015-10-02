---
type: post110
title:  Querying the Space
categories: XAP110
parent: none
weight: 800
---

<br>


{{%bannerleft "/attachment_files/subject/query.png" %}}
 This section explains the various mechanisms offered by GigaSpaces XAP to query the space for data, as well as related topics, such as how to use indexing to boost query performance and how the space can be iterated to fetch entries more efficiently.
{{%/bannerleft%}}

<br>

{{%fpanel%}}

[Id Queries](./query-by-id.html){{<wbr>}}
Id Based query - Primary Key based object retrieval from space.

[Query by template](./query-template-matching.html){{<wbr>}}
Template matching (a.k.a. Match by example) is a simple way to query the space.

[SQL Query](./query-sql.html){{<wbr>}}
The SQLQuery class is used to query the space using SQL-like syntax.

[Free text search](./query-free-text-search.html){{<wbr>}}
Search for records that include one or more words within a free text field.

[Nested property queries](./query-nested-properties.html){{<wbr>}}
Query nested properties, maps and collections using SQL queries

[Query with user defined classes](./query-user-defined-classes.html){{<wbr>}}
Query by matching a user defined class.

[Paging support](./query-paging-support-old.html){{<wbr>}}
Reading large number of objects using multiple queries in one call in a continuous manner.

[Partial results - Projection](./query-partial-results.html){{<wbr>}}
Obtaining partial results when querying the space to improve application performance and reduce memory footprint.
{{%/fpanel%}}

<br>

#### Additional Resources

{{%youtube "jC57mId3SMg"  "XAP Query APIS"%}}
