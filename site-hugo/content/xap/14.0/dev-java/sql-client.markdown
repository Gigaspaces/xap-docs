---
type: post140
title:  SQL Clients
categories:  XAP122, IEE
parent: sql-query-intro.html
weight: 200
---


The In-Grid SQL Query driver can be used with existing visualization tools, to gain insight into the data grid by using the specific tool-provided features such as drag and drop.

Examples:

- {{%exurl "Tableau""https://www.tableau.com/"%}}, 
- {{%exurl "MicroStrategy""https://www.microstrategy.com/us"%}}, 
- {{%exurl "QlikView""https://www.visualintelligence.co.nz/qlikview/"%}}) 
- {{%exurl "SquirreL" "http://squirrel-sql.sourceforge.net/"%}}
- {{%exurl "SQLLine" "http://sqlline.sourceforge.net/"%}}
 
 
# SquirreL

**To install SquirreL for use with the InsightEdge platform:**

1. Download and install {{%exurl "SquirreL" "http://squirrel-sql.sourceforge.net/#installation"%}}.
1. Copy all of the XAP JAR files (located in `<XAP-HOME>\lib\required`) to the squirreL `lib` folder.
1. Copy all of the InsightEdge JAR files (`<XAP-HOME>\insightedge\lib\jdbc`) to the squirreL `lib` folder.
1. Start SquirreL.
1. Click the **Drivers** vertical tab on the left, and then click **+** to add a new driver.
  ![image](/attachment_files/xap-sql/squirrel-driver.png)

1. Click the **Aliases** vertical tab on the left, and then click **+** to add a new connection. Sample URL: `jdbc:insightedge:url=jini://*/*/mySpace?locators=127.0.0.1&groups=xap-14.0.0`
  ![image](/attachment_files/xap-sql/squirrel-alias.png)

1. Create a connection to the XAP data grid.
 ![image](/attachment_files/xap-sql/squirrel-connect.png)

1. Run a test query, for example `SELECT * FROM Product;` (the data grid was filled with `Product` objects in advance).
 ![image](/attachment_files/xap-sql/squirrel-query.png)
	```
