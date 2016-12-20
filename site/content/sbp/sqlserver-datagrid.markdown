---
type: post
title: XAP.NET SQL Server Data Grid
categories: SBP
parent: data-access-patterns.html
weight: 1305
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim| 9.7 | April 2014|  |     |



# Overview

The SQL Server Data-Grid example demonstrates a common Data Grid / Caching architecture using SQL Server for persistence and initial data load into the data grid. It includes the following components:

- A Client application that perform write and read operations against a remote clustered IMDG.
- A Clustered IMDG with write-behind (Mirror service) with pre-fetch (initial load) enabled. The Data grid using [nHibernate](https://community.jboss.org/wiki/NHibernateForNET) to Map .Net objects to database tables and persist data into the SQL Server database.

![image](/attachment_files/sqlserver/datagrid-sqlserver002.jpg)

This best practice will demonstrate how to implement this with XAP.NET.

# Structure

The example includes four projects:

- `GigaSpaces.Examples.Datagrid.Commons` - contains the entities project (Person) used in the example and other common functionalities.
- `GigaSpaces.Examples.Datagrid.Mirror` - The mirror service (write-behind service) as a Processing Unit.
- `GigaSpaces.Examples.Datagrid` - The Data Grid processing unit (clustered space).
- `GigaSpaces.Examples.Datagrid.Client` - A client application performs write and read operations against the data grid.

# Prerequisites
- MS SQL Server 2012 installed and running.
- Visual Studio 2010 with Service pack 1 or Visual Studio 2013.
- XAP.NET 9.7 installed. Have the license key placed into `C:\GigaSpaces\XAP.NET 9.7.0 x64\Runtime\gslicense.xml`.

# Building the Example
Step 1:
[Download the example](/sbp/download_files/Datagrid-SQLServer.zip) and extract it into the `<GigaSpaces XAP .Net Root>\Examples` folder. A new folder called `Datagrid-SQLServer` will be created.

Step 2:
Browse to the example directory : `<GigaSpaces XAP .Net Root>\Examples\Datagrid-SQLServer`.

Step 3:
Edit the following `NHibernate` conf files and update the `connection.connection_string` to have the right value for the Data Source (database server and instance name). Keep the Initial Catalog as `datagrid`.
- <GigaSpaces XAP .Net Root>\Datagrid-SQLServer\config\nHibernate\CreateTablesNHibernate.cfg.xml
- <GigaSpaces XAP .Net Root>\Datagrid-SQLServer\config\nHibernate\nHibernate.cfg.xml
See example for the `connection.connection_string` you will need to modify:

```java
<property name="connection.connection_string">Server=MY-SERVER;Database=datagrid;Trusted_Connection=True</property>
```

Step 4: Run the `BuildAll.bat` script to compile the example. This script also copies the deployment files to the `<GigaSpaces Root>\deploy` folder.

# Running the Example

Step 1: 
The example must be compiled before running (see Building the Example).

Step 2: 
Create a Database Named `datagrid`:

- If its the first time you are running the example where the database and the `Person` table have not been created yet: 
- Go to Microsoft Sql Server Management Studio , Right click on `Databases` and Choose `New Database`, in the Database name Enter: `datagrid` and press the **OK** button. 
- To Create the database Tables run:  

```java
<GigaSpaces Root>\Examples\Datagrid-SQLServer\tools\SqlServerCreateDBTables.bat. 
```
This will generate the `Person` table within the `datagrid` database.

# Running the Data Grid
You may run the data grid as single process (good for development) or in a distributed manner (production environment). Once the data grid is started it is loading its data from the Database. You may customize this by having your own `NHibernateExternalDataSource` extension. See the `EdsUtils` and the `C:\GigaSpaces\XAP.NET 9.7.0 x64\NET v4.0.30319\Practices\ExternalDataSource\NHibernate` project for details.

## Running the Data Grid as a Single Process

To run the data grid in a single process for debug mode run the `runDebug.bat` script as an administrator. This will run the clustered data grid and the mirror service within the same process. You may run it within Visual Studio to debug the data grid construction.

## Running the Data Grid in a Distributed Configuration

1.  Run Gigaspaces Agent and containers by running the `StartAgent.bat` script. This will start 3 containers. *Run this script as an administrator*.
2.  Deploy the data grid and the Mirror service by running `DeployDataGrid.bat`. This script will undeploy first the existing data grid and mirror if they are already running.
 
Once deployed you should see the following within the GS-UI:

![image](/attachment_files/sqlserver/datagrid-sqlserver004.jpg)
 
# Running the Client Application

The client application has several options:

1. `runWrite.bat` - will write 10 Person objects into the data grid and persist to SQL Server.
2. `runWriteMultiple.bat` - will write 100 Person objects (10 batches of 10 Person objects) into the data grid and persist to SQL Server.
3. `runRead.bat` - will read a Person object from the data grid.
4. `runReadMultiple.bat` - will read a 100 Person object from the data grid.
 
When you write objects into the data grid you should see these as well within the database in the `Person` table.

# Opening .NET Solution with Microsoft Visual Studio

1.  Double-click the `GigaSpaces.Examples.Datagrid.sln` file.
2.  Choose `Build > Build Solution` menu. The project files and dependencies are created and copied to the `release` directory.
 
# Viewing the data within the Data Grid
Run GigaSpaces UI: Go to `<GigaSpaces Root>\Bin` and run `Gs-ui.exe`. This will allow you to view the content of the data grid.
 
## Viewing the data grid instances:
![image](/attachment_files/sqlserver/datagrid-sqlserver006.jpg)
 
## Viewing the data grid space types:
![<imgage](/attachment_files/sqlserver/datagrid-sqlserver008.jpg)
 
## Viewing the data within the data grid:
![imgage](/attachment_files/sqlserver/datagrid-sqlserver010.jpg)

# Important
Before you deploy the data grid make sure you will have the `nHibernate.cfg.xml` and `Person.hbm.xml` located under the following folders:

- <GigaSpaces XAP .Net Root>\Deploy\datagrid\NHibernateCfg
- <GigaSpaces XAP .Net Root>\Deploy\mirror\NHibernateCfg
 
