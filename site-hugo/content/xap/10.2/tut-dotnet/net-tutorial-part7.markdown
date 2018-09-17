---
type: post102
title:  Space Persistence
categories: XAP102NETTUT
weight: 800
parent: none
---



There are many situations where space data needs to be persisted to permanent storage and retrieved from it.

For example:

* Our online payment system works primarily with the memory space for temporary storage of process data structures, and the permanent storage is used to extend or back up the physical memory of the process running the space.{{<wbr>}}
* Our online payment system works primarily with the database storage and the space is used to make read processing more efficient. Since database access is expensive, the data read from the database is cached in the space, where it is available for subsequently fast read operations.{{<wbr>}}
* When a space is restarted, data from its persistent store can be loaded into the space to speed up incoming query processing.



Persistency can be configured to run in Synchronous(direct persistence) or Asynchronous mode.

# Synchronous Persistence
{{%section%}}

{{%column width="80%" %}}
When running in direct persistency mode the IMDG interacts with the data source to persist its data where the client application would get the acknowledgment for the IMDG operation only after both the IMDG and the SpaceDataSource finished the operation. With this persistency mode, the IMDG operations performance would be heavily depended on the speed of the Space Synchronization Endpoint to commit the data and provide acknowledgment back to the IMDG for the successful operation of the transaction.
{{%/column%}}
{{%column width="20%" %}}

{{%popup   "/attachment_files/qsg/data-grid-sync-persistNew.jpg"%}}


{{%/column%}}
{{%/section%}}

{{%learn "/xap/10.2/dev-dotnet/direct-persistency.html"%}}



# Asynchronous Persistence
{{%section%}}
{{%column width="80%" %}}
The XAP Mirror Service provides reliable asynchronous persistency. This allows you to asynchronously delegate the operations conducted with the IMDG into a backend database, significantly reducing the performance overhead. The Mirror service ensures that data will not be lost in the event of a failure. This way, you can add persistency to your application just by running the Mirror Service, without touching the real-time portion of your application in either configuration or code. This service provides fine-grained control of which object needs to be persisted.
{{%/column%}}
{{%column width="20%" %}}

{{%popup   "/attachment_files/qsg/data-grid-async-persistNew.jpg"%}}


{{%/column%}}
{{%/section%}}

{{%learn "/xap/10.2/dev-dotnet/asynchronous-persistency-with-the-mirror.html"%}}

 


# Space Persistence
The Space Persistency is implemented with the `ExternalDataSource`. These `ExternalDataSource` provides advanced persistence capabilities for the space to interact with a persistence layer.

- handles Pre-Loading data from the persistence layer and lazy load data from the persistence layer.
- handles changes done within the space delegating it them to the persistence layer.


### Persistence Adapter
XAP comes with a built in implementation of Space Persistency for NHibernate. This implementation is an extension of the `AbstractExternalDataSource` class. The implementation allows objects to be persisted using NHibernate mappings.






Let's use our online payment system to demonstrate how we can implement direct persistence with XAP. 

First, we implement the class we want to write into the space and provide persistency for it.
Note that we use the `[SpaceClass(persist=true)]` annotation to indicate that the space needs to persist objects of this type.


```csharp
using System;
using GigaSpaces.Core.Metadata;
using GigaSpaces.Core.Document;

namespace xaptutorial.model
{
  [SpaceClass(persist=true)]
  public class Merchant {

	[SpaceID(AutoGenerate = false)]
	[SpaceRouting]
	public long Id {set; get;}
	public String Name{set; get;}
	public double Receipts{set; get;}
	public double FeeAmount{set; get;}
	public ECategoryType Category{set; get;}
	public EAccountStatus Status{set; get;}

	public Merchant() {
	}
  }
}
```

## NHibernate Mapping File

In the next step we are creating the NHibernate mapping file. In Visual Studio create a folder to host all `hbm` files (for example `NHibernateCfg`) and place the following `Merchant.hbm.xml` file in it:


```xml
<?xml version="1.0"?>
<hibernate-mapping xmlns="urn:nhibernate-mapping-2.2">
  <class name="xaptutorial.model.Merchant" table="MERCHANT">
    <id name="Id" column="ID" type="int">
      <generator class="assigned"/>
    </id>
    <property name="Name" />
    .....
    .....
  </class>
</hibernate-mapping>
```

## NHibernate Session Factory Configuration File

NHibernate requires a session factory that creates new sessions over the database for each operation executed on it. You can create such a session factory, either with a configuration file or by code. This walkthrough demonstrates a simple configuration file for the session factory, over a MySQL database server into a database named `dotnetpersistency`. These parameters are configured in the `Connection` string property.

Create a file called `nHibernate.cfg.xml` in the `NHibernateCfg` folder created earlier and copy the following configuration to it:


```xml
<?xml version="1.0" ?>
<hibernate-configuration  xmlns="urn:nhibernate-configuration-2.2" >
  <session-factory>

    <property name="dialect">NHibernate.Dialect.MySQLDialect</property>
    <property name="connection.provider">NHibernate.Connection.DriverConnectionProvider</property>
    <property name="connection.driver_class">NHibernate.Driver.MySQLDataDriver</property>
    <property name="connection.connection_string">Server=localhost;Database=dotnetpersistency;User ID=root;CharSet=utf8</property>

    <!--Disable the writing of all the SQL statments to the console-->
    <property name="show_SQL">false</property>

    <!--Disabled the validation of your persistent classes, allows using .NET properties and not getters and setters on your fields-->
    <property name="use_proxy_validator">false</property>

    <!--This will create the tables in the database for your persistent classes according to the mapping file.-->
    <!--If the tables are already created this will recreate them and clear the data-->
    <property name="hbm2ddl.auto">create</property>

  </session-factory>
</hibernate-configuration>
```

## Creating a Space with an ExternalDataSource

Now we will configure our processing unit to host an embedded space with our mapping and configuration files:


```xml
<ProcessingUnit>
  <EmbeddedSpaces>
    <add Name="tutorialSpace">
      <Properties>
        <!-- Set space cache policy to All-In-Cache -->
        <add Name="space-config.engine.cache_policy" Value="1"/>
        <add Name="cluster-config.cache-loader.external-data-source" Value="true"/>
        <add Name="cluster-config.cache-loader.central-data-source" Value="true"/>
      </Properties>
      <ExternalDataSource Type="GigaSpaces.Practices.ExternalDataSource.NHibernate.NHibernateExternalDataSource">
        <Properties>
          <add Name="nhibernate-config-file" Value="NHibernateCfg\nHibernate.cfg.xml"/>
          <add Name="nhibernate-hbm-dir" Value="NHibernateCfg"/>
        </Properties>
      </ExternalDataSource>
    </add>
  </EmbeddedSpaces>
</ProcessingUnit>
```

Note that the external data source properties include the `nHibernate.cfg.xml` and hbm files location. Since these files are needed at runtime, make sure you modify their **Copy to Output Directory** settings accordingly (same as `pu.config`).

When you write new or update existing Merchant objects in the space, the objects are automatically saved in the data base. On startup of the above example, the entries from the MERCHANT table are loaded into the space.

{{% note %}}
Before using the `ExternalDataSource.NHibernate` practice, compile it by calling `<XAP Root>\dotnet\practices\ExternalDataSource\NHibernate\build.bat`. and include the resource in your project.
{{%/note%}}


{{%learn "/xap/10.2/dev-dotnet/hibernate-space-persistency.html"%}}

