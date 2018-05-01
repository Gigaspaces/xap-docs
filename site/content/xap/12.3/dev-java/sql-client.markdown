---
type: post123
title:  SQL Client
categories:  XAP122, IEE
parent: sql-query-intro.html
weight: 200
---


The In-Grid SQL Query driver can be used with existing visualization tools to gain insight into the data grid by using the specific tool provided features like drag and drop.

Examples:

- {{%exurl "Tableau""https://www.tableau.com/"%}}, 
- {{%exurl "MicroStrategy""https://www.microstrategy.com/us"%}}, 
- {{%exurl "QlikView""https://www.visualintelligence.co.nz/qlikview/"%}}) 
- {{%exurl "SquirreL" "http://squirrel-sql.sourceforge.net/"%}}
- {{%exurl "sqlline" "http://sqlline.sourceforge.net/"%}}
 

 
# SquirreL

**1.** Download and install {{%exurl "SquirreL" "http://squirrel-sql.sourceforge.net/#installation"%}}

**2.** copy all xap/insightedge (located in xap/lib and xap/insightedge/lib/jdbc) jars to squirreL lib folder.

**4.** Start SquirreL

**5.** Go to `Drivers` vertical tab on the left and click + button to add a new driver.


{{%align center%}}
![image](/attachment_files/xap-sql/squirrel-driver.png)
{{%/align%}}

**6.** Go to `Aliases` vertical tab on the left and click + button to add a new connection.
     example url = "jdbc:xap:url=jini://*/*/mySpace?locators=127.0.1.1&groups=xap-12.3.1"

{{%align center%}}
![image](/attachment_files/xap-sql/squirrel-alias.png)
{{%/align%}}


**7.** Connect to the data grid

{{%align center%}}
![image](/attachment_files/xap-sql/squirrel-connect.png)
{{%/align%}}


**8.** Run query, e.g. SELECT * FROM Product;

{{%align center%}}
![image](/attachment_files/xap-sql/squirrel-query.png)
{{%/align%}}




<br>

# sqlline

Make sure you have Space deployed with some data and is accessible.


**1.** Download {{%exurl "sqlline" "http://sqlline.sourceforge.net/"%}}

**2.** cd ./xap-calcite/

**3.** ./sqlline

**4.** To connect to the space: 
```bash
!connect jdbc:xap:url=jini://*/*/space?locators=127.0.0.1 Enter credentials if your space is secured (or just press Return otherwise).
```

**5.** To list available tables: 

```bash
!tables
```

**6.** Execute any query, e.g. 

```sql
SELECT * FROM Product;
```
**7.** To close sqlline: 
```bash
!quit
```
