---
type: post
title: XAP.NET SQL Server Delta Server
categories: SBP
parent: data-access-patterns.html
weight: 1302
---




|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Ronnie Guha| 9.7.0 | April 2014|    |    |



# Overview

{{%section%}}
{{%column width="70%" %}}
Almost every large enterprise system includes legacy applications or backend systems that are communicating with the enterprise main database system for reporting, batch processing, data mining, OLAP and other processing activity. These applications might not need to access the data grid to improve their performance or scalability. They will be using the database directly. Once these systems perform data updates , removing data or add new records to the database, these updates might need to be reflected within the data grid. This synchronization activity ensures the data grid is consistent and up to date with the enterprise main database server.
{{%/column%}}
{{%column width="30%" %}}
{{%youtube "SQOFuwine9g"  "XAP.NET SQL Server Delta Server"%}}
{{%/column%}}
{{%/section%}}

The Delta Server described with this pattern responsible for getting notifications from the database for any user activity (excluding data grid updates) and delegate these to the data grid. You may specify the exact data set updates to be delegated to the data grid by specifying a SQL Query that will indicate which records updates / removal / addition should be reflected within the data grid.


# Scenario
{{%section%}}
{{%column width="80%" %}}
We have an In Memory Data Grid (IMDG) that is used for querying information. The initial load of the IMDG was performed from an SQL Server Database. The IMDG is not used as a system of record in this case - in other words, any changes to the objects in the IMDG are not propagated back into the Database and instead Data-Grid Non-Aware Clients are updating the Database. These updates (insert,update and delete) need to be reflected in the IMDG.
{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/sqlserver-delta-server.jpg"%}}
{{%/column%}}
{{%/section%}}

We will show you how you can implement this scenario with XAP.NET. A fully functional example is available [here](/download_files/SqlDeltaServer.zip).


# Change Data Capture (CDC)

Change Data Capture is a new feature in SQL Server 2008 that records insert, update and delete activity in SQL Server tables.  This feature allows one to record and update an external data warehouse or IMDG with any data that has changed in the source systems since the last time the ETL process was run.  Without CDC determining any rows that were physically deleted or determining what was changed and when is quite cumbersome and difficult.  CDC provides a configurable solution that addresses these requirements and more.

Change data capture records insert, update, and delete activity that is applied to a SQL Server table. This makes the details of the changes available in an easily consumed relational format. Column information and the metadata that is required to apply the changes to a target environment is captured for the modified rows and stored in change tables that mirror the column structure of the tracked source tables. Table-valued functions are provided to allow systematic access to the change data by consumers.

For further information please consult the Microsoft [documentation](http://technet.microsoft.com/en-us/library/bb522489.aspx).
In our example we will only demonstrate the notifications for INSERT, UPDATE and Delete.

## Change Data Capture (CDC) Setup And Configuration

Note:- This setup is for XAP.NET. Please download it here (http://www.gigaspaces.com/xap-download). After downloading XAP.NET, please run the version appropriate for your platform (x86 or x64). Remember to copy the license you received via email to the appropriate location (e.g. C:\GigaSpaces\XAP.NET 9.7.0 x86\Runtime\gslicense.xml). Next, proceed with starting XAP.NET and monitoring it via the Web-GUI as follows:
a.  Gs-agent{{<wbr>}} - This can be enabled via windows service or by running Gs-agent.exe thus:

```bash
C:\GigaSpaces\XAP.NET 9.7.0 x86\NET v4.0.30319\Bin\Gs-agent.exe
```
b.  Deploy a space (e.g)

```bash
C:\GigaSpaces\XAP.NET 9.7.0 x86\NET v4.0.30319\Bin\Gs-cli deploy-space -cluster total_members=1,1 mydatagrid. Keep this grid name handy as you will be using it later on.
```
c.   Run 

```bash
C:\GigaSpaces\XAP.NET 9.7.0 x86\NET v4.0.30319\Bin\Gs-webui.exe
```
 and once successful, login to view the UI (e.g at http://localhost:8099/). You can login with the default group XAP-9.7.0-ga-NET-4.0.30319-x86

Keep this grid name (mydatagrid) handy as you will be using it later on.

NOTE: If you would like to capture all the data available before CDC is enabled and the DeltaServer is run, you would need to preload data into the datagrid. 

#### Step 1:
Note:-Make Sure Sql-Server Agent is enabled.

a Create a database first (e.g. named datagrid)
b Once a database is created, create a table - with our example, let's use the **Person** table with columns: ID, Firstname(nvarchar 255), Lastname(nvarchar 255) and Age (int)

#### Step 2:
CDC must be enabled at the database level (it is disabled by default).  To enable CDC you must be a member of the sysadmin fixed server role.  You can enable CDC ONLY on any user database (not on system databases).  Execute the following T-SQL script in the database of your choice (e.g. datagrid in the following screenshots):


```sql
declare @rc int
exec @rc = sys.sp_cdc_enable_db
select @rc
-- you will find that a new column is added to sys.databases: is_cdc_enabled
select name, is_cdc_enabled from sys.databases
```
  

![image](/attachment_files/sqlserver/sqlserver-pic3.png)

  
**(notice that datagrid has is_cdc_enabled column = 1)**

#### Step 3:
Enable CDC for the Person table. Execute the following system stored procedure to enable CDC for the Person table:

```sql
exec sys.sp_cdc_enable_table
    @source_schema = 'dbo', 
    @source_name = 'Person' ,
    @role_name = 'CDCRole',
    @supports_net_changes = 1
```

#### Step 4:

Enabling CDC at the database and table levels will create certain tables, jobs, stored procedures and functions in the CDC-enabled database. In our case, this is the datagrid table.
You will see a message that two SQL Agent jobs were created; e.g. cdc.datagrid_capture which scans the database transaction log to handle changes to the tables that have CDC enabled, and cdc.datagrid_cleanup which purges the change tables periodically.  

You can examine the schema objects created by running the following T-SQL script:

```sql
select o.name, o.type, o.type_desc from sys.objects o
join sys.schemas  s on s.schema_id = o.schema_id
where s.name = 'cdc'
```

#### Step 5:
Create another table to track the last LSN the DeltaServer is processing:


```sql
create table dbo.Person_lsn (last_lsn binary(10))
```

#### Step 6:
Next, create a function to get the last person LSN thus:

```sql
   create function dbo.get_last_Person_lsn()
         returns binary(10)
             as
           begin
         declare @last_lsn binary(10)
   select @last_lsn = last_lsn from dbo.Person_lsn
   select @last_lsn = isnull(@last_lsn, sys.fn_cdc_get_min_lsn('dbo_Person'))
      return @last_lsn
      end
```

You have created a Scalar-valued Function. Double check this function by right clicking on Programmability->Functions->Scalar Valued Functions.
 

![](/attachment_files/sqlserver-pic5.png)



#### Step 7:
Create a stored procedure that will capture data as soon as next person changes are executed:


```bash
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[Capture]
AS
BEGIN
  -- SET NOCOUNT ON added to prevent extra result sets from
  -- interfering with SELECT statements.
  SET NOCOUNT ON;
  declare @begin_lsn binary(10), @end_lsn binary(10)
    -- get the next LSN for Person changes
  select @begin_lsn = dbo.get_last_Person_lsn()
  -- get the last LSN for Person changes
  select @end_lsn = sys.fn_cdc_get_max_lsn()


  -- get all individual changes in the range
  select * from cdc.fn_cdc_get_all_changes_dbo_Person(
   @begin_lsn, @end_lsn, 'all'); 
  -- save the end_lsn in the Person_lsn table
  update dbo.Person_lsn
  set last_lsn = @end_lsn
  if @@ROWCOUNT = 0
  insert into dbo.Person_lsn values(@end_lsn)

END
GO
```


# Running the Example

1. [Download the example](/download_files/SqlDeltaServer.zip). Extract the SqlDeltaServer.zip in a directory of your choice (e.g. C:\temp\). Open the project to make some customizations for your project.
a. Change the Database properties according to your environment:


```xml
# References->App.config
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <connectionStrings>
    <add name="DBSTRING" connectionString="Data Source=YOUR-SERVER\SQL2012;Initial Catalog=datagrid;Integrated Security=True"/>
  </connectionStrings>
</configuration>

```

Please change them to reflect values according to your set up. You can easily copy this string from visual studio itself (if you have configured the database connection). Head over to Server explorer in Visual Studio, click on Data Connections->your database connection. On the right hand side, properties window you will see the connection string that you would need to copy/paste in **DBSTRING**.


![](/attachment_files/sqlserver-pic7.png)


b. In Program.cs, change your group and/or space connect string if you desire to. Currently, it is set to use:

```java
ISpaceProxy spaceProxy = GigaSpacesFactory.FindSpace("jini://*/*/mydatagrid?groups=XAP-9.7.0-ga-NET-4.0.30319-x64");
```
Please note that this is the same data grid name (mydatagrid) you have defined before (in first step).

c. If you would like to, you can change the frequency of updates thus (in program.cs) using:

```java
timer.Interval = 5000; //set interval of polling here
```

d. Now, you can build the solution and test it out. For this, you can either run it from Visual Studio (Ctrl+F5) or you can  run it from <extract directory>\DeltaServer\DeltaServer\bin\Debug(or Release) and run the DeltaServer.exe. Upon running the program, you should be prompted to insert/update/delete or exit the program


![](/attachment_files/sqlserver-pic8.png)



## Registering a listener, capturing data therefore
Lets assume we have an **Employee** represented in a Person table in the database that we want to be notified when a record is inserted, updated or deleted so we can update the IMDG with the changes. Here is an example of an **ChangeNotificationListener**.


```csharp
static void ChangeCapture(object sender, ElapsedEventArgs e)
{
         
    ISpaceProxy spaceProxy = GigaSpacesFactory.FindSpace("jini://*/*/mydatagrid?groups=XAP-9.7.0-ga-NET-4.0.30319-x64");

    string _connStr = ConfigurationManager.ConnectionStrings["DBSTRING"].ConnectionString;
            
    SqlConnection con = new SqlConnection(_connStr);
    con.Open();
    SqlCommand cmd = new SqlCommand("Capture", con);
    cmd.CommandType = CommandType.StoredProcedure;
    SqlDataAdapter da = new SqlDataAdapter(cmd);
    // this will query your database and return the result to your datatable
    DataTable dt = new DataTable();
    da.Fill(dt);

    //Insert Query starts
    List<Employee> objEmployee2 = (from item in dt.AsEnumerable()
    where item.Field<int>("__$operation") == 2
    select new Employee()
    {
        Id = item.Field<int>("Id"),
        FirstName = item.Field<string>("FirstName"),
        LastName = item.Field<string>("LastName"),
        Age = item.Field<int>("Age")
    }).ToList();

    var personarray = new Person[objEmployee2.Count];
    for (int i = 0; i < objEmployee2.Count; i++) // Loop through List with for
    {
        personarray[i] = new Person
        {
            Id = objEmployee2[i].Id,
            FirstName = objEmployee2[i].FirstName,
            LastName = objEmployee2[i].LastName,
            Age = objEmployee2[i].Age
        };
             
    }
    spaceProxy.WriteMultiple(personarray);
    //Insert Query ends

    //Update Query starts
    List<Employee> objEmployee3 = (from item in dt.AsEnumerable()
    where item.Field<int>("__$operation") == 4
    select new Employee()
         {
             Id = item.Field<int>("Id"),
             FirstName = item.Field<string>("FirstName"),
             LastName = item.Field<string>("LastName"),
             Age = item.Field<int>("Age")
         }).ToList();

    for (int i = 0; i < objEmployee3.Count; i++) // Loop through List with for
    {
        IdQuery<Person> idQuery = new IdQuery<Person>(objEmployee3[i].Id);
        IChangeResult<Person> changeResult =
                    spaceProxy.Change<Person>(idQuery,
                    new ChangeSet().Set("FirstName", objEmployee3[i].FirstName).Set("LastName", objEmployee3[i].LastName).Set("Age", objEmployee3[i].Age));
    }
    //Update Query ends

    //Delete Query starts
    List<Employee> objEmployee1 = (from item in dt.AsEnumerable()
        where item.Field<int>("__$operation") == 1
         select new Employee()
         {
             Id = item.Field<int>("Id"),
             FirstName = item.Field<string>("FirstName"),
             LastName = item.Field<string>("LastName"),
             Age = item.Field<int>("Age")
         }).ToList();

    var deltearray = new Person[objEmployee1.Count];
    for (int i = 0; i < objEmployee1.Count; i++) // Loop through List with for
    {
        Person template = new Person();
        template.Id = objEmployee1[i].Id;
        spaceProxy.Take<Person>(template);
    }

    //Delete Query ends
    da.Dispose();
    con.Close();
}
```



#### Viewing Data Changes in XAP.NET:
As you insert/update/delete, please note such changes in the XAP.NET Web UI.


[<img src="/attachment_files/sqlserver-pic9.png" width="400" height="300">](/attachment_files/sqlserver-pic9.png)


