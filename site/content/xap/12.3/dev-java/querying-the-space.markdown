---
type: post123
title:  Querying the Space
categories: XAP123, OSS
parent: none
weight: 800
---





 This section explains the various mechanisms offered by GigaSpaces XAP to query the space for data:
 
- [ID query](./query-by-id.html) - Retrieve objects from the Space based on the Primary Key.
- [Query by template](./query-template-matching.html) - Find data based on template matching (a.k.a. Match by example).
- [SQL query](./query-sql.html) - The SQLQuery class is used to query the space using SQL-like syntax.
- [SQL Function](./query-sql-function.html) - You can query the Space using built-in and user-defined functions.
- [Geospatial query](./query-geospatial.html) - Use geometry data types such as points, circles and polygons (these queries consider the spatial relationship between the geometries).
- [Nested property query](./query-nested-properties.html) - Use SQL queries on nested properties, maps and collections.
- [Query with user-defined classes](./query-user-defined-classes.html) - Query by matching a user-defined class. 
 
You can also perform a [free text search](./query-free-text-search.html) for records that include one or more words within a free text field, and a [full text search](./query-full-text-search.html) that leverages the Lucene search engine library/
 
The following related topics describe how to use indexing to boost query performance and how the space can be iterated to fetch entries more efficiently:

- [Paging support](./query-paging-support-old.html) - Reading a large number of objects using multiple queries in one call, in a continuous manner.
- [Partial results - Projection](./query-partial-results.html) - You can obtain partial results when querying the Space, to improve application performance and reduce the memory footprint.
- [Explain Plan](./query-explainplan.html) - This can be used to obtain a description of the strategy or plan that XAP uses to implement a specific SQLQuery. 

# Additional Resources

{{%youtube "jC57mId3SMg"  "XAP Query APIS"%}}
