---
type: post122
title:  SQL Client
categories:  XAP122SQL, XAPSQL
weight: 200
---

{{%warning%}}
This page is under construction !
{{%/warning%}}
 

 
# SquirreL


download and install {{%exurl "SquirreL" "http://squirrel-sql.sourceforge.net/#installation"%}}

Currently there is a problem with RMI Classloader + Squirell, so as a workaround we have to copy our driver to SquirreL lib folder. On Mac I copy jar as following: cp ./xap-calcite/target/gigaspaces-xap-jdbc-1.12.0-SNAPSHOT.jar /Applications/SQuirreLSQL.app/Contents/Resources/Java/lib.

Start SquirreL

Go to Drivers vertical tab on the left and click + button to add a new driver.

Go to Aliases vertical tab on the left and click + button to add a new connection.

Connect and run query, e.g. SELECT * FROM Employee;



<br>

# sqlline

Make sure you have space deployed with some data (you can use com.gigaspaces.jdbc.schema.dept.DeptSchemaSetup to populate space with a sample schema)

cd ./xap-calcite/

./sqlline

(in sqlline) To connect to the space: !connect jdbc:xap:url=jini://*/*/space?locators=127.0.0.1 Enter credentials if your space is secured (or just press Return otherwise).

To list available tables: !tables

Execute any query, e.g. SELECT * FROM Employee;

To close sqlline: !quit
