---
type: post122
title:  SQL Client
categories:  XAP122, IEE
parent: in-grid-sql-query-intro.html
weight: 200
---

{{%warning%}}
This page is under construction !
{{%/warning%}}
 

 
# SquirreL

**1.** Download and install {{%exurl "SquirreL" "http://squirrel-sql.sourceforge.net/#installation"%}}

**2.** Start SquirreL

**3.** Go to `Drivers` vertical tab on the left and click + button to add a new driver.

{{%align center%}}
![image](/attachment_files/xap-sql/squirrel-driver.png)
{{%/align%}}

**4.** Go to `Aliases` vertical tab on the left and click + button to add a new connection.

{{%align center%}}
![image](/attachment_files/xap-sql/squirrel-alias.png)
{{%/align%}}

 
**5.** Connect to the data grid

{{%align center%}}
![image](/attachment_files/xap-sql/squirrel-connect.png)
{{%/align%}}


**6.** Run query, e.g. SELECT * FROM Product;

{{%align center%}}
![image](/attachment_files/xap-sql/squirrel-query.png)
{{%/align%}}


{{%note%}}
Currently there is a problem with RMI Classloader + Squirell, so as a workaround we copy our driver to the SquirreL lib folder. On Mac, copy jar as following: cp ./xap-calcite/target/gigaspaces-xap-jdbc-1.12.0-SNAPSHOT.jar /Applications/SQuirreLSQL.app/Contents/Resources/Java/lib.
{{%/note%}}

<br>

# sqlline

Make sure you have space deployed with some data (you can use com.gigaspaces.jdbc.schema.dept.DeptSchemaSetup to populate space with a sample schema)

1. cd ./xap-calcite/

2. ./sqlline

3. (in sqlline) To connect to the space: !connect jdbc:xap:url=jini://*/*/space?locators=127.0.0.1 Enter credentials if your space is secured (or just press Return otherwise).

4. To list available tables: !tables

5. Execute any query, e.g. SELECT * FROM Employee;

6. To close sqlline: !quit
