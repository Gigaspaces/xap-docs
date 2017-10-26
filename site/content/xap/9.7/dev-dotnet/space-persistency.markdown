---
type: post97
title:  Space Persistency
categories: XAP97NET
parent: space-persistency-overview.html
weight: 100
---



{{% ssummary  %}}{{% /ssummary %}}



The XAP persistence interface is the key middleware connection link for loading and storing data to and from persistent data sources.


{{% tip %}}
For a fully running example using the Mirror Service see `XAP Root\XAP.NET\NET vX\Examples\StockSba` folder.
{{% /tip %}}



# Creating a Space with ExternalDataSource

You can either use GigaSpaces NHibernate implementation, or create a custom implementation:

{{%tabs%}}

{{%tab "  GigaSpaces NHibernate SQL Data Source Implementation "%}}
The following code demonstrates how to start an embedded space with GigaSpaces NHibernate `SqlDataSource` implementation as its External Data Source.


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
spaceConfig.ExternalDataSourceConfig.CustomProperties.Add(NHibernateExternalDataSource.NHibernateConfigProperty,
"[NHibernate config file]");

//Optional - points to a directory that contains the NHibernate mapping files (hbm)
spaceConfig.ExternalDataSourceConfig.CustomProperties.Add(NHibernateExternalDataSource.NHibernateHbmDirectory,
 "[NHibernate hbm files location]");

//Starts the space with the External Data Source
ISpaceProxy persistentSpace = GigaSpacesFactory.FindSpace("/./mySpace", spaceConfig);
```

{{% note %}}
Before using the `ExternalDataSource.NHibernate` practice, compile it by calling `<XAP Root>\dotnet\practices\ExternalDataSource\NHibernate\build.bat`.
{{%/note%}}

{{% info %}}
You can create your own NHibernate session factory and pass it to the `NHibernateExternalDataSource` constructor. In this case, there's no need to use `SpaceConfig.ExternalDataSourceConfig.CustomProperties`.
{{%/info%}}

{{% refer %}} For a demonstration of how to start a partitioned-sync2backup cluster with asynchronous NHibernate persistency, refer to the [NHibernate External Data Source](./hibernate-space-persistency.html) section.{{% /refer %}}
{{% /tab %}}

{{%tab "  Custom SQL Data Source Implementation "%}}
A custom .NET `SqlDataSource` implementation can be used as well.

The following code demonstrates how to start an embedded space with a custom .NET `SqlDataSource` implementation as its External Data Source.


```csharp
//Create a new space configuration object that is used to start a space
SpaceConfig spaceConfig = new SpaceConfig();

//Start a new ExternalDataSource config object
spaceConfig.ExternalDataSourceConfig = new ExternalDataSourceConfig();

//Start a new instance of the custom implementation and attach it to the config
spaceConfig.ExternalDataSourceConfig.Instance = new **CustomImplementation**();

//if custom properties should be passed to the Init method put them here, otherwise there's no need to create a dictionary of custom properties
spaceConfig.ExternalDataSourceConfig.CustomProperties = new Dictionary<string, string>();

//Add custom properties to the dictionary
spaceConfig.ExternalDataSourceConfig.CustomProperties.Add("[Property name]", "[Property value]");

//Starts the space with the External Data Source
ISpaceProxy persistentSpace = SpaceProxyProviderFactory.Instance.FindSpace("/./mySpace", spaceConfig);
```

{{% /tab %}}

{{% /tabs %}}

## Advanced Options

The number of objects passed between the .Net `IDataEnumerator` (Part of the `ISqlDataSource` interface) to the server on each iteration can be changed, its default value is set to `1000`.

This can be done by adding a custom property to the `ExternalDataSourceConfig` object.


```csharp
spaceConfig.ExternalDataSourceConfig.CustomProperties = new Dictionary<string, string>();
//Add custom properties to the dictionary
spaceConfig.ExternalDataSourceConfig.CustomProperties.Add("iterator-batch-size", "[batch size]");
```

## Server Side Logging

{{% refer %}}To enable the .NET `ExternalDataSource` adapter logging, refer to the [GigaSpaces Logging]({{% currentadmurl %}}/logging-overview.html) section.{{% /refer %}}



# Creating Custom ExternalDataSource Implementation

To create a custom implementation, implement the `GigaSpaces.Core.Persistency.ISqlDataSource` interface.


{{% note %}}
See an example for the NHibernate implementation under `<XAP Root>\dotnet\practices\ExternalDataSource\NHibernate`.
{{%/note%}}
