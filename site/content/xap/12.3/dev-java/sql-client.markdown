---
type: post123
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
1. Copy all of the XAP/InsightEdge JAR files (located in ...xap/lib and ...xap/insightedge/lib/jdbc) to the squirreL `lib` folder.
1. Start SquirreL.
1. Click the **Drivers** vertical tab on the left, and then click **+** to add a new driver.
  {{%align left%}}
  ![image](/attachment_files/xap-sql/squirrel-driver.png)
  {{%/align%}}

1. Click the **Aliases** vertical tab on the left, and then click **+** to add a new connection. Sample URL: `jdbc:xap:url=jini://*/*/mySpace?locators=127.0.1.1&groups=xap-12.3.1`
  {{%align left%}}
  ![image](/attachment_files/xap-sql/squirrel-alias.png)
  {{%/align%}}

1. Create a connection to the XAP data grid.
 {{%align left%}}
 ![image](/attachment_files/xap-sql/squirrel-connect.png)
 {{%/align%}}

1. Run a test query, for example `SELECT * FROM Product;`.
 {{%align left%}}
 ![image](/attachment_files/xap-sql/squirrel-query.png)
 {{%/align%}}


# SQLLine

Before installing and using SQLLine, make sure you have at least one Space deployed that contains data, and that the Space is accessible.

**To use SQLLine with the InsightEdge platform:**

1. Download {{%exurl "SQLLine" "http://sqlline.sourceforge.net/"%}}.
1. Open a command window to proceed with initializing the data grid and SQLLine.
1. Type `cd ./xap-calcite/`.
1. Type `./sqlline`.
1. Use the following command to access the Space with SQLLine: 

	```bash
	!connect jdbc:xap:url=jini://*/*/space?locators=127.0.0.1 Enter credentials if your space is secured (or just press Return otherwise).
	```
1. To list available tables, type: 

	```bash
	!tables
	```

1. Type a SQL query, for example:

	```sql
	SELECT * FROM Product;
	```

1.	To close sqlline, use the following command: 

	```bash
	!quit
	```
