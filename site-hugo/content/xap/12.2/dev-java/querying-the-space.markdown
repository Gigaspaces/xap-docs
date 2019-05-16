---
type: post122
title:  Querying the Space
categories: XAP122, OSS
parent: none
weight: 800
canonical: auto
---





 This section explains the various mechanisms offered by GigaSpaces XAP to query the space for data, as well as related topics, such as how to use indexing to boost query performance and how the space can be iterated to fetch entries more efficiently.


<br>

{{%fpanel%}}

[Id Queries](./query-by-id.html){{<wbr>}}
Id Based query - Primary Key based object retrieval from space.

[Query by template](./query-template-matching.html){{<wbr>}}
Template matching (a.k.a. Match by example) is a simple way to query the space.

[SQL Query](./query-sql.html){{<wbr>}}
The SQLQuery class is used to query the space using SQL-like syntax.

[SQL Function](./query-sql-function.html){{<wbr>}}
You can query the Space using built in functions and user defined functions.

[Geospatial Queries](./query-geospatial.html){{<wbr>}}
GeoSpatial queries make use of geometry data types such as points, circles and polygons and these queries consider the spatial relationship between these geometries.


[Free text search](./query-free-text-search.html){{<wbr>}}
Search for records that include one or more words within a free text field.


[Full text search](./query-full-text-search.html){{<wbr>}}
Full text search capability leveraging the Lucene  search engine library.


[Nested property queries](./query-nested-properties.html){{<wbr>}}
Query nested properties, maps and collections using SQL queries

[Query with user defined classes](./query-user-defined-classes.html){{<wbr>}}
Query by matching a user defined class.

[Paging support](./query-paging-support-old.html){{<wbr>}}
Reading large number of objects using multiple queries in one call in a continuous manner.

[Partial results - Projection](./query-partial-results.html){{<wbr>}}
Obtaining partial results when querying the space to improve application performance and reduce memory footprint.

[Explain Plan](./query-explainplan.html){{<wbr>}}
Explain Plan can be used to obtain a description of the strategy or plan that XAP uses to implement a specific SQLQuery. 

{{%/fpanel%}}



## Additional Resources

{{%youtube "jC57mId3SMg"  "XAP Query APIS"%}}
