---
type: post110
title:  Overview
categories: XAP110NET
weight: 100
parent: the-gigaspace-interface-overview.html
---

{{% ssummary %}}{{%/ssummary%}}


A Space is a logical in-memory service, which can store entries of information. An entry is a domain object; In C#, an entry can be a simple PONO or a SpaceDocument.

When a client connects to a Space, a proxy is created that holds a connection which implements the Space API. All client interaction is performed through this proxy.

The Space is accessed via a programmatic interface which supports the following main functions:

- Write - the semantics of writing a new entry of information into the Space.
- Read - read the contents of a stored entry into the client side.
- Take - get the value from the Space and delete its content.
- Notify - alert when the contents of an entry of interest have registered changes.

{{%learn "./the-space-operations.html"%}}

A Space proxy is created to interact with the Space. Several configuration parameters are available.

{{%learn "./the-space-configuration.html"%}}


# Embedded Space


A client communicating with a an embedded Space performs all its operation via local connection. There is no network overhead when using this approach.

![embedded-space.jpg](/attachment_files/embedded-space.jpg)


Here is an example how to create an embedded Space. The `EmbeddedSpaceFactory` is used to configure the Space url:

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


The Embedded Space can be used in a distributed architecture such as the replicated or partitioned clustered Space:

{{% indent %}}
![replicated-space1.jpg](/attachment_files/replicated-space1.jpg)
{{% /indent %}}

A simple way to use the embedded Space in a clustered architecture would be by deploying a clustered Space or packaging your application as a Processing Unit and deploy it using the relevant SLA.



# Remote Space

A client communicating with a remote Space performs all its operation via a remote connection. The remote Space can be partitioned (with or without backups) or replicated (sync or async replication based).

{{% indent %}}
![remote-space.jpg](/attachment_files/remote-space.jpg)
{{% /indent %}}

Here is an example how a client application can create a proxy to interacting with a remote Space:

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
A full description of the Space URL Properties can be found [here.](./the-space-configuration.html)
{{%/refer%}}


## Reconnection
When working with a **remote Space**, the Space may become unavailable (network problems, processing unit relocation, etc.). For information on how such disruptions are handled and configured refer to [Proxy Connectivity]({{%currentadmurl%}}/tuning-proxy-connectivity.html).



# Local Cache

XAP supports a [Local Cache](./local-cache.html) (near cache) configuration. This provides a front-end client side cache that will be used with the `Read` operations implicitly . The local cache will be loaded on demand or when you perform a `Read` operation and will be updated implicitly by the Space.

{{% indent %}}
![local_cache.jpg](/attachment_files/local_cache.jpg)
{{% /indent %}}

Here is an example for a `ISpaceProxy` construct with a local cache:


```csharp
// Create the ISpaceProxy
ISpaceProxy spaceProxy = new SpaceProxyFactory("mySpace").Create();
// Create Local Cache
ISpaceProxy localCache = GigaSpacesFactory.CreateLocalCache(spaceProxy);

```

{{%learn "./local-cache.html"%}}


# Local View

XAP supports a [Local View](./local-view.html) configuration. This provides a front-end client side cache that will be used with any `Read` or `ReadMultiple` operations implicitly. The local view will be loaded on start and will be updated implicitly by the Space.

{{% indent %}}
![local_view.jpg](/attachment_files/local_view.jpg)
{{% /indent %}}

Here is an example for a `ISpaceProxy` construct with a local cache:




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

{{%learn "./local-view.html"%}}


# Security

A secured Space should be configured with a security context so that it can be accessed (when connecting to it remotely). Here is an example of how this can be configured:


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


The grid components are secured using the Security Administration.



{{%learn "currentsecurl/security-administration.html"%}}




# Persistency

When constructing a Space, it is possible to provide [Space Persistency](./space-persistency.html) extensions using a NHibernate configuration. Here is an example of how it can be defined:


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
    <!--Disabled the validation of your persistent classes, allows using .Net properties and not getters and setters on your fields-->
    <property name="use_proxy_validator">false</property>
    <!--This will create the tables in the database for your persistent classes according to the mapping file.-->
    <!--If the tables are already created this will recreate them and clear the data-->
    <property name="hbm2ddl.auto">create</property>

  </session-factory>
</hibernate-configuration>
```

The above example configures both a custom JDBC `DataSource` and a NHibernate `SessionFactory` to define and use the GigaSpaces built-in `NHibernateSpaceDataSource`. The GigaSpaces data source is then injected into the Space construction and causes the Space to use it.




{{%learn "space-persistency-overview.html"%}}

