---
type: post122
title: In-Grid SQL Query
categories:  XAP122SQL, XAPSQL
weight: 370
---


{{%warning%}}
This page is under construction !
{{%/warning%}}

The JDBC Driver is broadly compatible with the SQL-99 Core specification. It allows database-driven applications to interact with the Space via SQL read queries. 
The driver will make the query optimization if needed and translates the SQL query into Space operations.

{{%note%}}
The JDBC driver was designed to allow only SQL compatible read operations against data stored in the grid. It does not support create/update/delete operations. Furthermore, the driver was not designed for low latency requirements, although it was developed for integration with applications, such as visualization tools, which support JDBC compliant datasources.

If you are looking for low latency read operations [SQLQuery]({{%currentjavaurl%}}/query-sql.html) is a better option.
{{%/note%}} 

 
{{%fpanel%}}

[SQL Driver](./sql-driver.html)<br>
You will learn 
 
[SQL Client](./sql-client.html)<br>
You will learn   
  
 
{{%/fpanel%}}

