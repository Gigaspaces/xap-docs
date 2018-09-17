---
type: post121
title:  NHibernate Integration
categories: XAP121NET, PRM
parent: space-persistency-overview.html
weight: 200
---

{{% ssummary  %}}{{% /ssummary %}}

XAP comes with a built in implementation of [Space Persistency](./space-persistency.html) APIs for NHibernate. This implementation is an extension of the `AbstractExternalDataSource` class. The implementation allows a custom objects persistency using NHibernate mappings.

The `NHibernate Space Persistency Implementation` is used both with the [Synchronous](./direct-persistency.html) and the [Asynchronous Persistency](./asynchronous-persistency-with-the-mirror.html) modes.

{{% info "Building the plugin "%}}
The NHibernate External Data Source is provided as a reference implementation under `<XAP Root>\Practices\ExternalDataSource\NHibernate`. You'll need to build it before using it by using the `build.bat` script.
{{%/info%}}

# Configuration

The external data source configuration is done via the embedded space configuration within `pu.config` - Add an `ExternalDataSource` tag and set the `Type` to the class implementing the external data source (in this case - `NHibernateExternalDataSource`). For example:


```xml
<ProcessingUnit>
  <EmbeddedSpaces>
    <add Name="space">
      <Properties>
	    <!-- Space properties to enable External Data Source -->
        <add Name="cluster-config.cache-loader.external-data-source" Value="true"/>
        <add Name="cluster-config.cache-loader.central-data-source" Value="true"/>
      </Properties>
      <ExternalDataSource Type="GigaSpaces.Practices.ExternalDataSource.NHibernate.NHibernateExternalDataSource">
        <Properties>
		  <!-- NHibernate session factory properties: Config file and Location of HBM files -->
          <add Name="nhibernate-config-file" Value="NHibernateCfg\nHibernate.cfg.xml"/>
          <add Name="nhibernate-hbm-dir" Value="NHibernateCfg"/>
		  <!-- NHibernate data source properties, for example initial load chunk size -->
          <add name="InitialLoadChunkSize" value="2000"/>
        </Properties>
      </ExternalDataSource>
    </add>
  </EmbeddedSpaces>
</ProcessingUnit>
```

In addition to the `Type`, the `ExternalDataSource` tag contains properties which are injected at runtime to initialize the actual instance. In this case, the NHibernate Session Factory requires 2 configuration settings: A configuration file and the location of the`HBM` files. Additional properties can be set to customize the external data source (for example, `InitialLoadChuckSize`).

### NHibernate Session Factory Configuration File

NHibernate requires a session factory that creates new sessions over the database for each operation executed on it. This walkthrough demonstrates a simple configuration file for the session factory, over a MySQL database server into a database named **dotnetpersistency**. These parameters are configured in the `Connection` string property.


```xml
<?xml version="1.0" ?>
<hibernate-configuration  xmlns="urn:nhibernate-configuration-2.2" >
  <session-factory>
    <property name="dialect">NHibernate.Dialect.MySQLDialect</property>
    <property name="connection.provider">NHibernate.Connection.DriverConnectionProvider</property>
    <property name="connection.driver_class">NHibernate.Driver.MySQLDataDriver</property>
    <!--Connection String-->
    <property name="connection.connection_string">Server=localhost;Database=dotnetpersistency;User ID=root;CharSet=utf8</property>
    <!--Disable the writing of all the SQL statements to the console-->
    <property name="show_SQL">false</property>
    <!--Disabled the validation of your persistent classes, allows using .NET properties and not getters and setters on your fields-->
    <property name="use_proxy_validator">false</property>
    <!--This will create the tables in the database for your persistent classes according to the mapping file.-->
    <![--If the tables are already created this will recreate them and clear the data](/attachment_files/dotnet/--If the tables are already created this will recreate them and clear the data)-->
    <property name="hbm2ddl.auto">create</property>
  </session-factory>
</hibernate-configuration>
```

### NHibernate Mapping File

Each persistent class requires a mapping file that defines how to map the object to and from the database. For example, for the following `Person` class:


```csharp
[SpaceClass(Persist = true)]
public class Person
{
    [SpaceID, SpaceRouting]
    public string Name {get; set;}
    public int? Age {get; set;}
}
```

We can use the following `Person.hbm.xml` file:


```xml
<?xml version="1.0"?>
<hibernate-mapping xmlns="urn:nhibernate-mapping-2.2" assembly="Entities" namespace="Entities">
  <class name="Entities.Person" table="PERSON">
    <id name="Name" column="Name" type="string">
      <generator class="assigned"/>
    </id>
    <property name="Age" />
  </class>
</hibernate-mapping>
```

{{% info %}}
Make sure your PONO `[SpaceId]` is defined on the same property as the NHibernate `[Id]`. This is necessary for proper object mapping.
{{% /info %}}
See [Modeling Your Data](./modeling-your-data.html) for details about these decorations.

# Properties

The Hibernate Space Persistency implementation includes the following properties:


|Property|Description|Default|
|:-------|:----------|:------|
|EnumeratorLoadFetchSize|Sets the fetch size that will be used when working with scrollable results. |10,000|
|InitialLoadChunkSize|By default, the initial load process will chunk large tables and will iterate over the table (entity) per chunk (concurrently). This setting allows to control the chunk size to split the table by. Batching can be disabled by setting -1{{<wbr>}}The `InitialLoadChunkSize` property allows you to have multiple threads loading data from the same table into the space - each thread loading different rows from the same table. Having the `InitialLoadChunkSize` as 100,000 will break a 1 million rows table into ten chunks. All the chunks, from all the tables, are processes by the amount of `InitialLoadThreadPoolSize` configured.|100,000|
|InitialLoadThreadPoolSize|The initial load operation uses the `ConcurrentMultiDataIterator`. This property allows to control the thread pool size of the concurrent multi data iterator.Note, this usually will map one to one to the number of open connections / cursors against the database.|10|
|PerformOrderById|When performing initial load, this flag indicates if the generated query will order to results by the id. |false|
|UseMerge| If set to true, will use Hibernate merge to perform the create/update, and will merge before calling delete.{{<wbr>}}This might be required for complex mappings (depends on Hibernate) at the expense of slower performance.|false|

{{% tip %}}
Tuning the `EnumeratorLoadFetchSize`, `InitialLoadChunkSize`, `InitialLoadThreadPoolSize` and `PerformOrderById` will allow you to control the initial load time. 
{{% /tip %}}

See example below:


```xml
<ProcessingUnit>
  <EmbeddedSpaces>
    <add Name="space">
      <Properties>
	    <!-- Space properties to enable External Data Source -->
        <add Name="cluster-config.cache-loader.external-data-source" Value="true"/>
        <add Name="cluster-config.cache-loader.central-data-source" Value="true"/>
      </Properties>
      <ExternalDataSource Type="GigaSpaces.Practices.ExternalDataSource.NHibernate.NHibernateExternalDataSource">
        <Properties>
		  <!-- NHibernate session factory properties: Config file and Location of HBM files -->
          <add Name="nhibernate-config-file" Value="NHibernateCfg\nHibernate.cfg.xml"/>
          <add Name="nhibernate-hbm-dir" Value="NHibernateCfg"/>
		  <!-- NHibernate data source properties, for example initial load chunk size -->
          <add name="EnumeratorLoadFetchSize" value="100"/>
          <add name="InitialLoadChunkSize" value="2000"/>
          <add name="InitialLoadThreadPoolSize" value="10"/>
          <add name="PerformOrderById" value="true"/>
          <add name="UseMerge" value="true"/>
        </Properties>
      </ExternalDataSource>
    </add>
  </EmbeddedSpaces>
</ProcessingUnit>
```
