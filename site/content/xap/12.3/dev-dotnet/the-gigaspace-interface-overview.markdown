---
type: post123
title:  The Space Interface
categories: XAP123NET, PRM
weight: 500
parent: none
---



This section explains how to create and configure a Space, and how to interact with it using the Space API.

A Space is a logical in-memory service, which can store entries of information. An entry is a domain object; In C#, an entry can be a simple PONO or a SpaceDocument. When a client connects to a Space, a proxy is created that holds a connection which implements the Space API. All client interaction is performed through this proxy.

The Space is accessed via a programmatic interface that supports the following main functions:

- Write - the semantics of writing a new entry of information into the Space.
- Read - read the contents of a stored entry to the client side.
- Take - get the value from the Space and delete its content.
- Notify - alert when the contents of an entry of interest have registered changes.


{{%refer%}}
For more information, refer to [The Space Operations](./the-space-operations.html).
{{%/refer%}}


A Space proxy is created to interact with the Space. Several configuration parameters are available.


{{%refer%}}
For more information, refer to [The Space Configuration](./the-space-configuration.html).
{{%/refer%}}

# Embedded Space

A client communicating with a an embedded Space performs all its operations via local connection. There is no network overhead when using this approach.

{{% align center%}}
![embedded-space.jpg](/attachment_files/embedded-space.jpg)
{{%/align%}}

The following is an example of how to create an embedded Space. The `EmbeddedSpaceFactory` is used to configure the Space URL:

{{%tabs%}}
{{%tab " Code"%}}

```csharp
// Create the ISpaceProxy
ISpaceProxy spaceProxy = new EmbeddedSpaceFactory("mySpace").Create();
```
{{%/tab%}}
{{%tab " XML"%}}

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="ProcessingUnit" type="GigaSpaces.XAP.Configuration.ProcessingUnitConfigurationSection, GigaSpaces.Core"/>
  </configSections>
  <ProcessingUnit>
    <EmbeddedSpaces>
      <add Name="mySpace"/>
    </EmbeddedSpaces>
  </ProcessingUnit>
</configuration>
```
{{%/tab%}}
{{%/tabs%}}


The embedded Space can be used in a distributed architecture, such as a replicated or partitioned clustered Space:

{{% align center %}}
![replicated-space1.jpg](/attachment_files/replicated-space1.jpg)
{{% /align %}}

A simple way to use the embedded Space in a clustered architecture is by deploying a clustered Space, or packaging your application as a Processing Unit and deploying it using the relevant SLA.

# Remote Space

A client communicating with a remote Space performs all its operations via a remote connection. The remote Space can be partitioned (with or without backups) or replicated (based on synchronous or asynchronous replication).

{{% align center %}}
![remote-space.jpg](/attachment_files/remote-space.jpg)
{{% /align %}}

The following is an example of how a client application can create a proxy to interacting with a remote Space:

{{%tabs%}}
{{%tab " Code"%}}

```csharp
// Create the ISpaceProxy
ISpaceProxy spaceProxy = new SpaceProxyFactory("mySpace").Create();
```
{{%/tab%}}
{{%tab " XML"%}}

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="ProcessingUnit" type="GigaSpaces.XAP.Configuration.ProcessingUnitConfigurationSection, GigaSpaces.Core"/>
  </configSections>
  <ProcessingUnit>
    <SpaceProxies>
      <add Name="mySpace" />
    </SpaceProxies>
  </ProcessingUnit>
</configuration>
```
{{%/tab%}}
{{%/tabs%}}

{{%refer%}}
A full description of the Space URL properties can be found [here.](./the-space-configuration.html).
{{%/refer%}}


## Reconnection
When working with a **remote Space**, the Space may become unavailable (network problems, processing unit relocation, etc.). For information on how such disruptions are handled and configured, refer to [Proxy Connectivity]({{%currentadmurl%}}/tuning-proxy-connectivity.html).

# Local Cache

XAP supports a [Local Cache](./local-cache.html) (near cache) configuration. This provides a front-end client side cache that is used with the `Read` operations implicitly . The local cache is loaded on demand or when you perform a `Read` operation, and is updated implicitly by the Space.

{{% align center %}}
![local_cache.jpg](/attachment_files/local_cache.jpg)
{{% /align %}}

The following is an example of a `ISpaceProxy` construct with a local cache:


```csharp
// Create the ISpaceProxy
ISpaceProxy spaceProxy = new SpaceProxyFactory("mySpace").Create();
// Create Local Cache
ISpaceProxy localCache = GigaSpacesFactory.CreateLocalCache(spaceProxy);

```

# Local View

XAP supports a [Local View](./local-view.html) configuration. This provides a front-end client side cache that is used with any `Read` or `ReadMultiple` operations implicitly. The local view is loaded on startup, and is updated implicitly by the Space.

{{% align center %}}
![local_view.jpg](/attachment_files/local_view.jpg)
{{% /align %}}

The following is an example of a `ISpaceProxy` construct with a local cache:


```csharp
ISpaceProxy spaceProxy = new SpaceProxyFactory("mySpace").Create();

//define names for the localView
const String typeName1 = "com.gigaspaces.Employee";
const String typeName2 = "com.gigaspaces.Address";

//Create an array of views and initialize them with the select criteria
View[] views = new View[] { new View(typeName1, "DepartmentNumber=1"), new View(typeName2, "") };

//Create the local view using the GigaSpacesFactory class.
IReadOnlySpaceProxy localView = GigaSpacesFactory.CreateLocalView(spaceProxy, views);

```

# Security

A secured Space should be configured with a security context so that it can be accessed (when connecting to it remotely). See the following example of how this can be configured:


{{%tabs%}}
{{%tab "  Code"%}}

```csharp
    SecurityContext securityContext = new SecurityContext ("userName", "password");

    // Create the factory
	SpaceProxyFactory factory = new SpaceProxyFactory ("mySpaceWithSecurity");

	// Set the Cluster Info
	factory.Credentials = securityContext;

	//create the ISpaceProxy
	ISpaceProxy proxy = factory.Create ();

	// .......
	proxy.Dispose ();

```
{{%/tab%}}
{{%tab " XML"%}}

```xml
<SpaceProxies>
    <add Name="MySpaceWithCustom">
        <Properties>
            <add Name=" security.username" Value="username"/>
            <add Name=" security.password" Value="password"/>
        </Properties>
    </add>
</SpaceProxies>
```
{{%/tab%}}
{{%/tabs%}}


The grid components are secured using the `Security Administration`.


{{%refer%}}
For more information, refer to [Security Administration]({{%currentsecurl%}}/security-administration.html).
{{%/refer%}}




# Persistency

When constructing a Space, you can provide [Space Persistency](./space-persistency-overview.html) extensions using a NHibernate configuration. The following is an example of how it can be defined:


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

The above example configures both a custom JDBC `DataSource` and a NHibernate `SessionFactory`, to define and use the XAP built-in `NHibernateSpaceDataSource`. The XAP data source is then injected into the Space construction and causes the Space to use it.

