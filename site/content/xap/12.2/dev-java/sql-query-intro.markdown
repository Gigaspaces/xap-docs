---
type: post122
title: In-Grid SQL Query
categories:  XAP122, IEE
parent: insight-edge-apis.html
weight: 1000
---


{{%warning%}}
This page is under construction !
{{%/warning%}}

Traditionally, customers adopting our platform come from a database (SQL) background. In order to give them a familiar interface for looking into what data exists in the grid, we created a SQL adapter that lets them query the grid as if it's a traditional database. This gives the opportunity to, now, plug-in existing visualization tools (like Tableau, MicroStrategy, QlikView) 
directly into the grid and use a drag and drop interface to look into grid data.

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

