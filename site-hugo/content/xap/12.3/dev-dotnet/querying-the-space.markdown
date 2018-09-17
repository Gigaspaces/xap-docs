---
type: post123
title:  Querying the Space
categories: XAP123NET, PRM
parent: none
weight: 600
---





This section explains the various mechanisms offered by GigaSpaces XAP to query the space for data:

- [ID query](./query-by-id.html) - Retrieve objects from the Space based on the Primary Key.
- [Query by template](./query-template-matching.html) - Find data based on template matching ((a.k.a. Match by example).
- [Prepared template](./query-prepared-template.html) - Query the Space using a Prepared Template.
- [SQL query](./query-sql.html) - The SQLQuery class is used to query the Space using SQL-like syntax.
- [Nested property queries](./query-nested-properties.html) - Use SQL queries on nested properties, maps and collections.
- [LINQ](./query-linq.html) - Query the Space using LINQ.

You can also perform a [free text search](./query-free-text-search.html) for records that include one or more words within a free text field.
Search for records that include one or more words within a free text field.

The following related topics describe how to use indexing to boost query performance, and how the Space can be iterated to fetch entries more efficiently:

- [Paging support](./query-paging-support.html) - Reading large number of objects using multiple queries in one call, in a continuous manner.
- [Partial results - Projection](./query-partial-results.html) - YOu can obtain partial results when querying the Space, to improve application performance and reduce the memory footprint.

