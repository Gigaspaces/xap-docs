---
type: post123
title: In-Grid SQL Query
categories:  XAP122, IEE
parent: insightedge-apis.html
weight: 1000
canonical: auto
---
 

Traditionally, customers adopting our platform come from a database (SQL) background. 
In order to give them a familiar interface for looking into what data exists in the grid, we created a SQL adapter that lets them query the grid as if it's a traditional database. 

This gives the opportunity to plug-in existing visualization tools like:<br>
* {{%exurl "Tableau""https://www.tableau.com/"%}}<br>
* {{%exurl "MicroStrategy""https://www.microstrategy.com/us"%}}<br>
* {{%exurl "QlikView""https://www.visualintelligence.co.nz/qlikview/"%}}<br>
directly into the grid and use a drag and drop interface to look into grid data.

{{%note%}}
The JDBC Driver is broadly compatible with the SQL-99 Core specification. It allows database-driven applications to interact with the Space via SQL read queries. 
{{%/note%}}


The JDBC driver is based on {{%exurl "Apache Calcite" "https://calcite.apache.org/"%}} and was designed to allow only SQL compatible read operations against data stored in the grid. 
It does not support create/update/delete operations. Furthermore, the driver was not designed for low latency requirements, although it was developed for integration with applications, such as visualization tools, which support JDBC compliant data sources.


{{%refer%}}
If you are looking for low latency JDBC driver with CRUD capability, refer to the [JDBC Driver](./jdbc-driver.html)topic in the Developer guide.
{{%/refer%}}
 
