---
type: post
title:  Tableau Integration
categories: SBP
parent: insightedge.html
weight: 20
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Vitalii Zinchenko    | 12.2 | September 2017|  [Tableau](https://www.tableau.com/)  |     |




Organizations often require quick insight into the data to understand the business without involving  the IT team. With the [SQL Driver]({{%latestjavaurl%}}/sql-query-intro.html) 
it is possible to achieve this with the data stored in XAP Data grid. To visualize  the data, [Tableau](https://www.tableau.com/) can be connected to the XAP Datagrid through the ODBC-JDBC driver.

So, on this topic, we are going to setup the Tableau-XAP integration.


## Dataset

We will use the familiar  Tableau users data set - `Simple - Superstore`. 
The original scheme is too simple to show the join capabilities so we made some modifications.
The location information is referenced by the orders entity. Here is the schema:

 
![](/attachment_files/sbp/tableau/schema.png)
 
## Architecture

 
![image](/attachment_files/sbp/tableau/diagram.png)
 

Now it is possible to connect to the XAP Datagrid with the SQL-92 compatible JDBC driver. However, there is no possibility to use JDBC driver from Tableau to query the data behind JDBC driver. Tableau can only use ODBC as a general connection option, although we can use ODBC-JDBC bridge to convert ODBC requests from Tableau side to JDBC requests to XAP Datagrid side.

Tableau connects to `ODBC-JDBC` bridge which the XAP JDBC driver to query the Datagrid.

## Configuration

**Space setup:**

- Make sure you have running Datagrid and you have access to it.
```bash
<XAP-HOME>/bin/gs-agent.sh 
```
- Create/observe the space with name `tableauSpace`

- Filling space: 

[Download](/download_files/sbp/xap-sql-demo.rar) and build the sample.

Execute the following command from folder "xap-sql-demo" or run it from within your IDE. 

```bash
java -jar target\xap-sql-demo.jar --space-url "jini://*/*/space?locators=<DATA_GRID_IP>" --lookup-group <DATA_GRID_LOOKUP_GROUP>
```

Substitute values <DATA_GRID_IP> and <DATA_GRID_LOOKUP_GROUP> with appropriate values of your deployment.


- View the objects in the space

 
![image](/attachment_files/sbp/tableau/xap_1.png)
 

**Bridge configuration**

Follow the step-by-step [instruction](./tableau-configuration.html) to set up the EasySoft bridge or use any other bridge implementation.


**Tableau configuration**

 - Open Tableau
 - Choose `Other Database (ODBC)` as data source


![image](/attachment_files/sbp/tableau/tableau_0.png)

 - Choose DSN: `easysoft-xap-odbc-jdbc`
 - Press: `Connect`
 - Fill Database: `space`
 - Press `Sign In`

 
![image](/attachment_files/sbp/tableau/tableau_2_1.png)
 

 - Search the schema and choose your `space`:

 
![image](/attachment_files/sbp/tableau/tableau_3.png)
 

**Table search:**

 
![image](/attachment_files/sbp/tableau/tableau_1.png)
 

**Query data with Tableau**

First, let's take a look at the space before executing any query.

As we can see there were no read operations yet:

 
![image](/attachment_files/sbp/tableau/xap_2.png)
 

At this point, we have everything set up and ready to make queries against the data in the grid.

To do so, drag and drop the two tables: "Orders" a "Locations" and join them by fields as shown below:

 
![image](/attachment_files/sbp/tableau/tableau_2.png)
 

When the tables are joined let's switch to the "Sheet1"

Choose "Country" and "State" fields. Then click "Create Hierarchy..." as shown on the image:

 
![image](/attachment_files/sbp/tableau/tableau_3.png)
 

Then double click on the "State" field.

Drop the field `Sales` as "Size" mark and the field `Profit` as `Color` mark.

Observe the sales-profit insight made easily from data stored in the XAP Datagrid:

 
![image](/attachment_files/sbp/tableau/tableau_5.png)
 

Let's check the space operations statistics. You can see the reading operations performed while constructing the view:

 
![image](/attachment_files/sbp/tableau/xap_3.png)
 

 