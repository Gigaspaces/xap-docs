---
type: post
title: Oracle Delta Server
categories: SBP
parent: data-access-patterns.html
weight: 1300
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Chris Roffler| 9.6 | Nov 2013|  |     |



# Overview

Almost every large enterprise system includes legacy applications or backend systems that are communicating with the enterprise main database system for reporting, batch processing, data mining, OLAP and other processing activity. These applications might not need to access the data grid to improve their performance or scalability. They will be using the database directly. Once these systems perform data updates , removing data or add new records to the database, these updates might need to be reflected within the data grid. This synchronization activity ensures the data grid is consistent and up to date with the enterprise main database server.

<iframe width="640" height="390" src="//www.youtube.com/embed/EOyDg-mI3z0" frameborder="0" allowfullscreen></iframe>

The Delta Server described with this pattern responsible for getting notifications from the database for any user activity (excluding data grid updates) and delegate these to the data grid. You may specify the exact data set updates to be delegated to the data grid by specifying a SQL Query that will indicate which record updates, removals, and additions should be reflected in the data grid.

# Scenario

We have an In Memory Data Grid that is used for querying information. The initial load of the data grid was performed from an Oracle Database. The data grid is not used as a system of record in this case, meaning that any changes to the objects in the data grid are not propagated back to the database. Non-Aware data grid clients are updating the database. These updates (insert,update and delete) need to be reflected in the data grid.

{{%align center%}}
[<img src="/attachment_files/sbp/oracle-dserver.png" width="400" height="300">](/attachment_files/sbp/oracle-dserver.png)
{{%/align%}}

We will show you how you can implement this scenario with XAP. A fully functional example is available on [GitHub](https://github.com/Gigaspaces/oracle-delta-server).


# Database Change Notification

Oracle's Database Change Notification enables client applications to register queries with the database and receive notifications in response to DML or DDL changes on the objects associated with the queries. The notifications are published by the database when the DML or DDL transaction commits. During registration, the application specifies a notification handler and associates a set of interesting queries with the notification handler. A notification handler can be either a server side PL/SQL procedure or a client side callback.

When the database issues change notification, it can contain some or all of the following information:

- Names of the modified objects. For example, the notification can specify that the hr.employees table was changed.
- The type of change. For example, the message specifies whether the change was caused by an INSERT, UPDATE, DELETE, ALTER TABLE, or DROP TABLE.
- The ROWIDs of the changed rows and the type of DML that changed them.

 
In our example we will only demonstrate the notifications for INSERT, UPDATE and DELETE.

## Registering a Listener

Lets assume we have an Employee table in the database that we want to be notified when a record is inserted, updated or deleted so we can update the data grid with the changes. Here is an example of an ChangeNotificationListener.


```java
private void registerListener() {

  Properties props = new Properties();
  props.put(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
  props.put(OracleConnection.NTF_QOS_RELIABLE, "false");
  props.setProperty(OracleConnection.DCN_BEST_EFFORT, "true");

  DatabaseChangeRegistration dcr = dbConnection.registerDatabaseChangeNotification(props);

  Statement stmt = dbConnection.createStatement();

  // Associate the statement with the registration.
  ((OracleStatement) stmt).setDatabaseChangeRegistration(dcr);
  // Register the actual query
  ResultSet rs = stmt.executeQuery("select * from employee");

  while (rs.next()) {
  // Do Nothing
  }

  // Add the Application listener
  dcr.addListener(this);
 }
```

{{%note%}}The select statement for the listener can be any valid SQL statement.{{%/note%}}

## Implementing the Listener

The listener will receive the change notifications whenever a row is inserted, updated or deleted in the employee table. Here is the code for the listener implementation:


```java
@Override
public void onDatabaseChangeNotification(DatabaseChangeEvent databaseChangeEvent) {

  TableChangeDescription[] tableChanges = databaseChangeEvent.getTableChangeDescription();

  for (TableChangeDescription tableChange : tableChanges) {
    RowChangeDescription[] rcds = tableChange.getRowChangeDescription();

     for (RowChangeDescription rcd : rcds) {
       RowOperation ro = rcd.getRowOperation();
       String rowId = rcd.getRowid().stringValue();

       if (ro.equals(RowOperation.INSERT)) {
         service.insertSpace(rowId);
       } else if (ro.equals(RowOperation.UPDATE)) {
         service.updateSpace(rowId);
       } else if (ro.equals(RowOperation.DELETE)) {
         service.removeSpace(rowId);
       } else {
         logger.info("Event Not Replicated - Only INSERT/DELETE/UPDATE are handled.");
       }
    }
  }
}
```

The notification information only contains the actual operation performed in the database and the corresponding ROWID. After receiving the ROWID, we have to retrieve the actual data from the employee table with a query that uses this ROWID in the where clause, and then we instantiate the object and write it to the data grid.

Here is an example of this query:

```java
"select rowid, id, processed, firstName, lastname, age, departmentid from employee where rowId = ? "
```

## Employee POJO

Here is the corresponding Employee POJO that we will use to write to the Space:

```java
@SpaceClass
@Entity
public class Employee implements IDomainEntity<Long> {
  private static final long serialVersionUID = 1L;
  private String rowid;
  @Id
  private Long id;
  private Boolean processed;
  private String firstName;
  private String lastName;
  private Integer age;
  private Integer departmentid;

  public Employee() {
  }

  @SpaceId
  public Long getId() {
    return id;
  }

  @SpaceRouting
  public Integer getDepartmentid() {
    return departmentid;
  }
..........
}
```

Since we are using Hibernate for the queries, we are declaring the Class as an `@Entity`. However we do not define a table for the Entity, otherwise Hibernate would try to map the ROWID attribute to a column in the Oracle table.

# Deployment

In our example, we deploy two Processing Units (PUs):

- The first PU (loader PU) creates an embedded Space and registers Ntify listeners that will tell us when objects are written, updated or deleted from the Space. This PU also performs the initial load.
- The second PU (feeder PU) creates the Oracle Change Listener and writes, updates or deletes the object from the Space.


# Initial Load

On system startup, we perform an initial load from the database. The first PU that creates the Space will load all rows from the database into the data grid. Here is an example of how we can accomplish this:


```java
@PostConstruct
private void initialize() {

   logger.debug("Starting to load data ");

   Collection<Employee> employees = null;

   if (clusterInfo.getNumberOfInstances() > 1) {
     employees = dao.findAllEmloyeesByPartition(clusterInfo.getNumberOfInstances(),
        .getInstanceId() - 1);
   } else {
     employees = dao.findAllEmloyees();
   }

   for (Employee emp : employees) {
      space.write(emp);
   }
}

```
If the Space is not partitioned, we simply read all employees from the table and write them to the Sace. If the Space is partitioned, we query the database with the following query assuming that the `departmentid` attribute is the routing attribute:


```sql
"select rowid, id, processed, firstName, lastname, age, departmentid from employee where mod(departmentid,?) = ? "
```


# Running the Example

1. Download the [example](https://github.com/Gigaspaces/oracle-delta-server) from Github.
2. Change the database properties according to your environment:


```java
# Oracle Database Config
db.type=oracle
db.driver=oracle.jdbc.driver.OracleDriver
db.dialect=org.hibernate.dialect.Oracle9iDialect
db.url=jdbc:oracle:thin:@yourhost:1521:orcl
db.username=username
db.password=password

```

These configuration files are located:

- /oracle-delta-server/loader/src/main/resources/service.properties
- /oracle-delta-server/feeder/src/main/resources/service.properties
- /oracle-delta-server/src/test/resources/service.properties

3. Build the processing units with the top POM file.
4. Deploy the loaderPU.jar file to the Data Grid (this will perform the initial load from the database).
5. Deploy the feederPU.jar file to the Data Grid (this will register the change listener with oracle).
6. Run a client that inserts, updates and deletes employees in the database: /oracle-delta-server/src/test/java/xap/oracle/test/DBTest.java.

This application will insert an employee every 10 seconds into the database, then update them and finally remove them from the database.

You can see in the feeder.log file (that the feeder PU generates) that the events are processed from the change listener:

```java
2013-11-07T14:15:50.256 DEBUG - xap.oracle.processor.OracleChangeListener -Event handler received event
2013-11-07T14:15:50.256 DEBUG - xap.oracle.processor.OracleChangeListener -Affected row : AAASNyAAEAAAAIzABk
ROW: operation=DELETE, ROWID=AAASNyAAEAAAAIzABk
```

And in the loader.log file that is generated by the loader PU, you will see that the Space receives the operations:

```java
2013-11-07T14:16:02.118 DEBUG - xap.oracle.loader.listener.RemoveEmployeeListener -An Employee was removed from space
```

