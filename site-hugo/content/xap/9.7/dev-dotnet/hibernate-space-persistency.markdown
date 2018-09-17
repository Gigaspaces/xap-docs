---
type: post97
title:  NHibernate External Data Source
categories: XAP97NET
parent: space-persistency-overview.html
weight: 200
---

{{% ssummary  %}}{{% /ssummary %}}



This page demonstrates how to use the GigaSpaces .NET NHibernate External Data Source  in a common scenario: a cluster topology with a mirror. This is asynchronous persistency, which means that the operation against the cluster members is persisted to the database in an asynchronous manner. Each cluster member uses the External Data Source in read-only mode. Therefore it only reads data from the External Data Source, and each write operation is replicated to the mirror space.

The mirror uses the External Data Source interface in write mode, and delegates destructive space operations (write, update, take) to the database through the External Data Source implementation.

{{% note %}}
Before using the `ExternalDataSource.NHibernate` practice, compile it by calling `<XAP Root>\Bin\Practices\ExternalDataSource\NHibernate\build.bat`.
{{%/note%}}

{{% info %}}
The database server used in this walkthrough is MySQL, and a database named `dotnetpersistency` is created in it.
{{%/info%}}

# NHibernate Mapping and Configuration Files

{{% anchor sessionfactory %}}

## NHibernate Session Factory Configuration File

NHibernate requires a session factory that creates new sessions over the database for each operation executed on it. You can create such a session factory, either with a configuration file or by code. This walkthrough demonstrates a simple configuration file for the session factory, over a MySQL database server into a database named dotnetpersistency. These parameters are configured in the `Connection` string property.


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
    <![--If the tables are already created this will recreate them and clear the data](/attachment_files/dotnet/--If the tables are already created this will recreate them and clear the data)-->
    <property name="hbm2ddl.auto">create</property>

  </session-factory>
</hibernate-configuration>
```

## NHibernate Mapping File

Each persistent class requires a mapping file that defines how to map the object to and from the database. This walkthrough shows a simple `Person` class and its corresponding mapping file.

{{%tabs%}}

{{%tab "  Person Class "%}}
Our `Person` is defined in Assembly name entities.


```csharp
namespace Entities
{
  [SpaceClass(Persist = true)]
  public class Person
  {
    [SpaceID, SpaceRouting]
    public string Name {set; get;}

    [SpaceProperty(NullValue = 0)]
    public int Age{set; get;}

    public Person(string name, int age)
    {
      this.Name = name;
      this.Age = age;
    }

    public Person() { }
  }
}
```

{{% /tab %}}

{{%tab "  NHibernate Mapping File "%}}


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

{{% /tab %}}

{{% /tabs %}}

# Starting the Spaces with NHibernate External Data Source

This walk through demonstrates how to start the spaces with the NHibernate External Data Source from code, using a cluster in a [partitioned-sync2backup](/product_overview/terminology.html) topology.
Our cluster is 2,1 and a mirror. Therefore it consists of:

- 2 partitioned primary spaces
- 2 backup spaces, one for each partition
- 1 mirror space

## Starting a Cluster Member Space

The following code starts a cluster member space with the NHibernate External Data Source:


```csharp
//Create a new space configuration object that is used to start a space
SpaceConfig spaceConfig = new SpaceConfig();
//Start a new ExternalDataSource config object
spaceConfig.ExternalDataSourceConfig = new ExternalDataSourceConfig();
//Start a new instance of NHibernateExternalDataSource and attach it to the config
spaceConfig.ExternalDataSourceConfig.Instance = new NHibernateExternalDataSource();
//Create custom properties that are required to build NHibernate session factory
spaceConfig.ExternalDataSourceConfig.CustomProperties = new Dictionary<string, string>();
//Point to NHibernate session factory config file
spaceConfig.ExternalDataSourceConfig.CustomProperties.Add(NHibernateExternalDataSource.NHibernateConfigProperty, "<NHibernate config file>");
//Optional - points to a directory that contains the NHibernate mapping files (hbm)
spaceConfig.ExternalDataSourceConfig.CustomProperties.Add(NHibernateExternalDataSource.NHibernateHbmDirectory, "<NHibernate HBM files location>");
//Our cluster member should only use the External Data Source in read only mode since the
//mirror persist the data to the database (Async persistency)
spaceConfig.ExternalDataSourceConfig.Usage = Usage.ReadOnly
//Add custom properties
spaceConfig.CustomProperties = new Dictionary<string, string>();
//State the External Data Source is in All-In-Cache mode
spaceConfig.CustomProperties.Add("space-config.engine.cache_policy", "1");

//We need to configure this properties to let the cluster be aware that
//there's a central database and not a database per space
spaceConfig.CustomProperties.Add("cluster-config.cache-loader.external-data-source", "true");
spaceConfig.CustomProperties.Add("cluster-config.cache-loader.central-data-source", "true");
//Creates a cluster info with the partitioned-sync2backup cluster schema, stating 2,1 topology, partition id 1 and not a backup space.
spaceConfig.ClusterInfo = new ClusterInfo("partitioned-sync2backup", 1, null, 2, 1);

//Starts the space with the External Data Source
ISpaceProxy persistentSpace = GigaSpacesFactory.FindSpace("/./mySpace?mirror=true", spaceConfig);
```

{{% info %}}
`<NHibernate config file>` (see code box above) should point to the NHibernate session factory [configuration file](#sessionfactory).

 It is recommended that you put all the NHibernate HBM mapping files in one directory, and point `<NHibernate HBM files location>` (see code box above) to that directory.
{{%/info%}}

{{% note %}}
You can also construct your own NHibernate session factory in code, and pass it to the constructor of the `NHibernateExternalDataSource`. In this case, there is no need to use `SpaceConfig.ExternalDataSourceConfig.CustomProperties`.
{{%/note%}}

To start the other members of the cluster, simply change the [ClusterInfo](./processing-unit-container.html#ClusterInfo):

- Backup space of the first partition member


```java
spaceConfig.ClusterInfo = new ClusterInfo("partitioned-sync2backup", 1, 1, 2, 1);
```

- Second primary space in the partitioned cluster


```java
spaceConfig.ClusterInfo = new ClusterInfo("partitioned-sync2backup", 2, null, 2, 1);
```

- Backup space of the second partition member


```java
spaceConfig.ClusterInfo = new ClusterInfo("partitioned-sync2backup", 2, 1, 2, 1);
```

## Starting the Mirror Space

The following code starts the mirror space with the NHibernate External Data Source:


```csharp
//Create a new space configuration object that is used to start a space
SpaceConfig spaceConfig = new SpaceConfig();
//Start a new ExternalDataSource config object
spaceConfig.ExternalDataSourceConfig = new ExternalDataSourceConfig();
//Start a new instance of NHibernateExternalDataSource and attach it to the config
spaceConfig.ExternalDataSourceConfig.Instance = new NHibernateExternalDataSource();
//Create custom properties that are required to build NHibernate session factory
spaceConfig.ExternalDataSourceConfig.CustomProperties = new Dictionary<string, string>();
//Point to NHibernate session factory config file
spaceConfig.ExternalDataSourceConfig.CustomProperties.Add(NHibernateExternalDataSource.NHibernateConfigProperty, "[NHibernate config file]");
//Optional - points to a directory that contains the NHibernate mapping files (hbm)
spaceConfig.ExternalDataSourceConfig.CustomProperties.Add(NHibernateExternalDataSource.NHibernateHbmDirectory, "[NHibernate HBM files location]");
//The mirror persist the data, there for its usage is ReadWrite
//However, since its a mirror it will only write data to the External Data Source and will not read data from it.
spaceConfig.ExternalDataSourceConfig.Usage = Usage.ReadWrite
//Add custom properties
spaceConfig.CustomProperties = new Dictionary<string, string>();
//State the External Data Source is in All-In-Cache mode
spaceConfig.CustomProperties.Add("space-config.engine.cache_policy", "1");
//We need to configure this properties to let the cluster be aware that
//there's a central database and not a database per space
spaceConfig.CustomProperties.Add("cluster-config.cache-loader.external-data-source", "true");
spaceConfig.CustomProperties.Add("cluster-config.cache-loader.central-data-source", "true");

//Starts the space with the External Data Source
ISpaceProxy persistentSpace = GigaSpacesFactory.FindSpace("/./mirror-service?schema=mirror", spaceConfig);
```
